/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *  
 */
package de.hybris.platform.commerceservices.order.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.commerceservices.externaltax.ExternalTaxesService;
import de.hybris.platform.commerceservices.order.CommercePlaceOrderStrategy;
import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.payment.PaymentService;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class DefaultCommercePlaceOrderStrategy implements CommercePlaceOrderStrategy
{
	private static final Logger LOG = Logger.getLogger(DefaultCommercePlaceOrderStrategy.class);

	private ModelService modelService;
	private DeliveryService deliveryService;
	private CommonI18NService commonI18NService;
	private PaymentService paymentService;
	private OrderService orderService;
	private BaseSiteService baseSiteService;
	private BaseStoreService baseStoreService;
	private PromotionsService promotionsService;
	private CalculationService calculationService;
	private ExternalTaxesService externalTaxesService;
	private List<CommercePlaceOrderMethodHook> commercePlaceOrderMethodHooks;
	private ConfigurationService configurationService;

	@Override
	public CommerceOrderResult placeOrder(final CommerceCheckoutParameter parameter) throws InvalidCartException
	{
		final CartModel cartModel = parameter.getCart();
		validateParameterNotNull(cartModel, "Cart model cannot be null");
		final CommerceOrderResult result = new CommerceOrderResult();
		try
		{
			beforePlaceOrder(parameter);
			if (calculationService.requiresCalculation(cartModel))
			{
				// does not make sense to fail here especially since we don't fail below when we calculate order.
				// throw new IllegalArgumentException(String.format("Cart [%s] must be calculated", cartModel.getCode()));
				LOG.error(String.format("CartModel's [%s] calculated flag was false", cartModel.getCode()));
			}

			final CustomerModel customer = (CustomerModel) cartModel.getUser();
			validateParameterNotNull(customer, "Customer model cannot be null");

			final OrderModel orderModel = getOrderService().createOrderFromCart(cartModel);
			if (orderModel != null)
			{
				// Reset the Date attribute for use in determining when the order was placed
				orderModel.setDate(new Date());

				// Store the current site and store on the order
				orderModel.setSite(getBaseSiteService().getCurrentBaseSite());
				orderModel.setStore(getBaseStoreService().getCurrentBaseStore());
				orderModel.setLanguage(getCommonI18NService().getCurrentLanguage());

				if (parameter.getSalesApplication() != null)
				{
					orderModel.setSalesApplication(parameter.getSalesApplication());
				}

				// clear the promotionResults that where cloned from cart PromotionService.transferPromotionsToOrder will copy them over bellow.
				orderModel.setAllPromotionResults(Collections.<PromotionResultModel> emptySet());

				getModelService().saveAll(customer, orderModel);

				if (cartModel.getPaymentInfo() != null && cartModel.getPaymentInfo().getBillingAddress() != null)
				{
					final AddressModel billingAddress = cartModel.getPaymentInfo().getBillingAddress();
					orderModel.setPaymentAddress(billingAddress);
					orderModel.getPaymentInfo().setBillingAddress(getModelService().clone(billingAddress));
					getModelService().save(orderModel.getPaymentInfo());
				}
				getModelService().save(orderModel);
				// Transfer promotions to the order
				getPromotionsService().transferPromotionsToOrder(cartModel, orderModel, false);

				// Calculate the order now that it has been copied
				try
				{
					getCalculationService().calculateTotals(orderModel, false);
					getExternalTaxesService().calculateExternalTaxes(orderModel);
				}
				catch (final CalculationException ex)
				{
					LOG.error("Failed to calculate order [" + orderModel + "]", ex);
				}

				getModelService().refresh(orderModel);
				getModelService().refresh(customer);

				result.setOrder(orderModel);

				this.beforeSubmitOrder(parameter, result);

				getOrderService().submitOrder(orderModel);
			}
			else
			{
				throw new IllegalArgumentException(String.format("Order was not properly created from cart %s", cartModel.getCode()));
			}
		}
		finally
		{
			getExternalTaxesService().clearSessionTaxDocument();
		}

		this.afterPlaceOrder(parameter, result);
		return result;
	}

	protected void beforeSubmitOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult result)
			throws InvalidCartException
	{
		if (getCommercePlaceOrderMethodHooks() != null
				&& (parameter.isEnableHooks() && getConfigurationService().getConfiguration().getBoolean(
						CommerceServicesConstants.PLACEORDERHOOK_ENABLED, true)))
		{
			for (final CommercePlaceOrderMethodHook commercePlaceOrderMethodHook : getCommercePlaceOrderMethodHooks())
			{
				commercePlaceOrderMethodHook.beforeSubmitOrder(parameter, result);
			}
		}
	}

	protected void afterPlaceOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult result)
			throws InvalidCartException
	{
		if (getCommercePlaceOrderMethodHooks() != null
				&& (parameter.isEnableHooks() && getConfigurationService().getConfiguration().getBoolean(
						CommerceServicesConstants.PLACEORDERHOOK_ENABLED, true)))
		{
			for (final CommercePlaceOrderMethodHook commercePlaceOrderMethodHook : getCommercePlaceOrderMethodHooks())
			{
				commercePlaceOrderMethodHook.afterPlaceOrder(parameter, result);
			}
		}
	}

	protected void beforePlaceOrder(final CommerceCheckoutParameter parameter) throws InvalidCartException
	{
		if (getCommercePlaceOrderMethodHooks() != null && parameter.isEnableHooks())
		{
			for (final CommercePlaceOrderMethodHook commercePlaceOrderMethodHook : getCommercePlaceOrderMethodHooks())
			{
				commercePlaceOrderMethodHook.beforePlaceOrder(parameter);
			}
		}
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected DeliveryService getDeliveryService()
	{
		return deliveryService;
	}

	@Required
	public void setDeliveryService(final DeliveryService deliveryService)
	{
		this.deliveryService = deliveryService;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	protected PaymentService getPaymentService()
	{
		return paymentService;
	}

	@Required
	public void setPaymentService(final PaymentService paymentService)
	{
		this.paymentService = paymentService;
	}

	protected OrderService getOrderService()
	{
		return orderService;
	}

	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService siteService)
	{
		this.baseSiteService = siteService;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService service)
	{
		this.baseStoreService = service;
	}

	protected PromotionsService getPromotionsService()
	{
		return promotionsService;
	}

	@Required
	public void setPromotionsService(final PromotionsService promotionsService)
	{
		this.promotionsService = promotionsService;
	}

	protected CalculationService getCalculationService()
	{
		return calculationService;
	}

	@Required
	public void setCalculationService(final CalculationService calculationService)
	{
		this.calculationService = calculationService;
	}

	public ExternalTaxesService getExternalTaxesService()
	{
		return externalTaxesService;
	}

	@Required
	public void setExternalTaxesService(final ExternalTaxesService externalTaxesService)
	{
		this.externalTaxesService = externalTaxesService;
	}

	protected List<CommercePlaceOrderMethodHook> getCommercePlaceOrderMethodHooks()
	{
		return commercePlaceOrderMethodHooks;
	}

	public void setCommercePlaceOrderMethodHooks(final List<CommercePlaceOrderMethodHook> commercePlaceOrderMethodHooks)
	{
		this.commercePlaceOrderMethodHooks = commercePlaceOrderMethodHooks;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
}
