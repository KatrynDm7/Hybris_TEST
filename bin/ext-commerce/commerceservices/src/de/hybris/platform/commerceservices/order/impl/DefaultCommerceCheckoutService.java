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

import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.externaltax.ExternalTaxesService;
import de.hybris.platform.commerceservices.order.CommerceCartCalculationStrategy;
import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import de.hybris.platform.commerceservices.order.CommerceDeliveryAddressStrategy;
import de.hybris.platform.commerceservices.order.CommerceDeliveryModeStrategy;
import de.hybris.platform.commerceservices.order.CommerceDeliveryModeValidationStrategy;
import de.hybris.platform.commerceservices.order.CommercePaymentAuthorizationStrategy;
import de.hybris.platform.commerceservices.order.CommercePaymentInfoStrategy;
import de.hybris.platform.commerceservices.order.CommercePaymentProviderStrategy;
import de.hybris.platform.commerceservices.order.CommercePlaceOrderStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.payment.PaymentService;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.servicelayer.util.ServicesUtil.*;


public class DefaultCommerceCheckoutService implements CommerceCheckoutService
{
	private ModelService modelService;
	private DeliveryService deliveryService;
	private I18NService i18nService;
	private CommonI18NService commonI18NService;
	private PaymentService paymentService;
	private OrderService orderService;
	private BaseSiteService baseSiteService;
	private BaseStoreService baseStoreService;
	private PromotionsService promotionsService;
	private CalculationService calculationService;
	private ExternalTaxesService externalTaxesService;
	private CommerceCartCalculationStrategy commerceCartCalculationStrategy;
	private CommercePlaceOrderStrategy commercePlaceOrderStrategy;
	private CommercePaymentAuthorizationStrategy commercePaymentAuthorizationStrategy;
	private CommercePaymentInfoStrategy commercePaymentInfoStrategy;
	private CommerceDeliveryModeValidationStrategy commerceDeliveryModeValidationStrategy;
	private CommercePaymentProviderStrategy commercePaymentProviderStrategy;
	private CommerceDeliveryAddressStrategy commerceDeliveryAddressStrategy;
	private CommerceDeliveryModeStrategy commerceDeliveryModeStrategy;

	@Override
	@Deprecated
	public boolean setDeliveryAddress(final CartModel cartModel, final AddressModel addressModel)
	{
		return setDeliveryAddress(cartModel, addressModel, false);
	}

	@Override
	@Deprecated
	public boolean setDeliveryAddress(final CartModel cartModel, final AddressModel addressModel,
	                                  final boolean flagAsDeliveryAddress)
	{
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setAddress(addressModel);
		parameter.setIsDeliveryAddress(flagAsDeliveryAddress);
		return this.setDeliveryAddress(parameter);
	}

	@Override
	public boolean setDeliveryAddress(final CommerceCheckoutParameter parameter)
	{
		return this.getCommerceDeliveryAddressStrategy().storeDeliveryAddress(parameter);
	}

	@Override
	public boolean setDeliveryMode(final CommerceCheckoutParameter parameter)
	{
		return this.getCommerceDeliveryModeStrategy().setDeliveryMode(parameter);
	}

	@Override
	@Deprecated
	public boolean setDeliveryMode(final CartModel cartModel, final DeliveryModeModel deliveryModeModel)
	{
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setDeliveryMode(deliveryModeModel);
		return this.setDeliveryMode(parameter);
	}

	@Override
	public boolean removeDeliveryMode(final CommerceCheckoutParameter parameter)
	{
		return this.getCommerceDeliveryModeStrategy().removeDeliveryMode(parameter);
	}

	@Override
	@Deprecated
	public boolean removeDeliveryMode(final CartModel cartModel)
	{
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		return this.removeDeliveryMode(parameter);
	}

	@Override
	public void calculateCart(final CommerceCheckoutParameter parameter)
	{
		final CartModel cartModel = parameter.getCart();
		validateParameterNotNull(cartModel, "Cart model cannot be null");
		final CommerceCartParameter commerceCartParameter = new CommerceCartParameter();
		commerceCartParameter.setEnableHooks(true);
		commerceCartParameter.setCart(cartModel);
		getCommerceCartCalculationStrategy().calculateCart(commerceCartParameter);
	}

	@Override
	@Deprecated
	public void calculateCart(final CartModel cartModel)
	{
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		this.calculateCart(parameter);
	}

	@Override
	public void validateDeliveryMode(final CommerceCheckoutParameter parameter)
	{
		getCommerceDeliveryModeValidationStrategy().validateDeliveryMode(parameter);
	}

	@Override
	@Deprecated
	public void validateDeliveryMode(final CartModel cartModel)
	{
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		this.validateDeliveryMode(parameter);
	}

	@Override
	public boolean setPaymentInfo(final CommerceCheckoutParameter parameter)
	{
		return this.getCommercePaymentInfoStrategy().storePaymentInfoForCart(parameter);
	}

	@Override
	@Deprecated
	public boolean setPaymentInfo(final CartModel cartModel, final PaymentInfoModel paymentInfoModel)
	{
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setPaymentInfo(paymentInfoModel);
		return this.setPaymentInfo(parameter);
	}

	@Override
	public PaymentTransactionEntryModel authorizePayment(final CommerceCheckoutParameter parameter)
	{
		final CartModel cartModel = parameter.getCart();

		validateParameterNotNull(cartModel, "Cart model cannot be null");
		validateParameterNotNull(cartModel.getPaymentInfo(), "Payment information on cart cannot be null");

		// if the authorization amount is not passed in figure it out from the cart.
		if (parameter.getAuthorizationAmount() == null)
		{
			final Double totalPrice = cartModel.getTotalPrice();
			final Double totalTax = (cartModel.getNet().booleanValue() && cartModel.getStore() != null && cartModel.getStore()
					.getExternalTaxEnabled().booleanValue()) ? cartModel.getTotalTax() : Double.valueOf(0d);
			final BigDecimal totalPriceWithoutTaxBD = new BigDecimal(totalPrice == null ? 0d : totalPrice.doubleValue()).setScale(2,
					RoundingMode.HALF_EVEN);
			final BigDecimal totalPriceBD = new BigDecimal(totalTax == null ? 0d : totalTax.doubleValue()).setScale(2,
					RoundingMode.HALF_EVEN).add(totalPriceWithoutTaxBD);

			parameter.setAuthorizationAmount(totalPriceBD);
		}

		return getCommercePaymentAuthorizationStrategy().authorizePaymentAmount(parameter);
	}

	@Override
	@Deprecated
	public PaymentTransactionEntryModel authorizePayment(final CartModel cartModel, final String securityCode,
	                                                     final String paymentProvider)
	{
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setSecurityCode(securityCode);
		parameter.setPaymentProvider(paymentProvider);
		return this.authorizePayment(parameter);
	}

	@Override
	@Deprecated
	public PaymentTransactionEntryModel authorizePayment(final CartModel cartModel, final String securityCode,
	                                                     final String paymentProvider, final BigDecimal amount)
	{
		validateParameterNotNull(cartModel, "Cart model cannot be null");
		validateParameterNotNull(cartModel.getPaymentInfo(), "Payment information on cart cannot be null");
		validateParameterNotNull(amount, "Amount cannot be null");
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setSecurityCode(securityCode);
		parameter.setPaymentProvider(paymentProvider);
		parameter.setAuthorizationAmount(amount);
		return this.authorizePayment(parameter);
	}

	@Override
	@Deprecated
	public OrderModel placeOrder(CartModel cartModel, SalesApplication salesApplication) throws InvalidCartException
	{
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setSalesApplication(salesApplication);
		return placeOrder(parameter).getOrder();
	}

	@Override
	@Deprecated
	public OrderModel placeOrder(final CartModel cartModel) throws InvalidCartException
	{
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setSalesApplication(SalesApplication.WEB);

		// By default use the SalesApplication.WEB
		return placeOrder(parameter).getOrder();
	}

	@Override
	public CommerceOrderResult placeOrder(final CommerceCheckoutParameter parameter) throws InvalidCartException
	{
		return this.getCommercePlaceOrderStrategy().placeOrder(parameter);
	}

	@Override
	public String getPaymentProvider()
	{
		return getCommercePaymentProviderStrategy().getPaymentProvider();
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

	protected I18NService getI18nService()
	{
		return i18nService;
	}

	@Required
	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
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

	protected CommerceCartCalculationStrategy getCommerceCartCalculationStrategy()
	{
		return commerceCartCalculationStrategy;
	}

	@Required
	public void setCommerceCartCalculationStrategy(final CommerceCartCalculationStrategy commerceCartCalculationStrategy)
	{
		this.commerceCartCalculationStrategy = commerceCartCalculationStrategy;
	}

	protected CommercePlaceOrderStrategy getCommercePlaceOrderStrategy()
	{
		return commercePlaceOrderStrategy;
	}

	@Required
	public void setCommercePlaceOrderStrategy(final CommercePlaceOrderStrategy commercePlaceOrderStrategy)
	{
		this.commercePlaceOrderStrategy = commercePlaceOrderStrategy;
	}

	protected CommercePaymentAuthorizationStrategy getCommercePaymentAuthorizationStrategy()
	{
		return commercePaymentAuthorizationStrategy;
	}

	@Required
	public void setCommercePaymentAuthorizationStrategy(
			final CommercePaymentAuthorizationStrategy commercePaymentAuthorizationStrategy)
	{
		this.commercePaymentAuthorizationStrategy = commercePaymentAuthorizationStrategy;
	}

	protected CommercePaymentInfoStrategy getCommercePaymentInfoStrategy()
	{
		return commercePaymentInfoStrategy;
	}

	@Required
	public void setCommercePaymentInfoStrategy(final CommercePaymentInfoStrategy commercePaymentInfoStrategy)
	{
		this.commercePaymentInfoStrategy = commercePaymentInfoStrategy;
	}

	protected CommerceDeliveryModeValidationStrategy getCommerceDeliveryModeValidationStrategy()
	{
		return commerceDeliveryModeValidationStrategy;
	}

	@Required
	public void setCommerceDeliveryModeValidationStrategy(
			final CommerceDeliveryModeValidationStrategy commerceDeliveryModeValidationStrategy)
	{
		this.commerceDeliveryModeValidationStrategy = commerceDeliveryModeValidationStrategy;
	}

	protected CommercePaymentProviderStrategy getCommercePaymentProviderStrategy()
	{
		return commercePaymentProviderStrategy;
	}

	@Required
	public void setCommercePaymentProviderStrategy(final CommercePaymentProviderStrategy commercePaymentProviderStrategy)
	{
		this.commercePaymentProviderStrategy = commercePaymentProviderStrategy;
	}

	protected CommerceDeliveryAddressStrategy getCommerceDeliveryAddressStrategy()
	{
		return commerceDeliveryAddressStrategy;
	}

	@Required
	public void setCommerceDeliveryAddressStrategy(final CommerceDeliveryAddressStrategy commerceDeliveryAddressStrategy)
	{
		this.commerceDeliveryAddressStrategy = commerceDeliveryAddressStrategy;
	}

	protected CommerceDeliveryModeStrategy getCommerceDeliveryModeStrategy()
	{
		return commerceDeliveryModeStrategy;
	}

	@Required
	public void setCommerceDeliveryModeStrategy(final CommerceDeliveryModeStrategy commerceDeliveryModeStrategy)
	{
		this.commerceDeliveryModeStrategy = commerceDeliveryModeStrategy;
	}
}
