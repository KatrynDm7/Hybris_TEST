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
package de.hybris.platform.subscriptionfacades.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.subscriptionfacades.SubscriptionFacade;
import de.hybris.platform.subscriptionfacades.data.SubscriptionData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPaymentData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;
import de.hybris.platform.subscriptionfacades.data.TermOfServiceFrequencyData;
import de.hybris.platform.subscriptionfacades.exceptions.SubscriptionFacadeException;
import de.hybris.platform.subscriptionservices.subscription.CustomerResolutionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Abstract subscription facade.
 */
public abstract class AbstractSubscriptionFacade implements SubscriptionFacade
{
	private static final Logger LOG = Logger.getLogger(AbstractSubscriptionFacade.class);

	private CartService cartService;
	private CheckoutFacade checkoutFacade;
	private Converter<CreditCardPaymentInfoModel, CCPaymentInfoData> creditCardPaymentInfoConverter;
	private CustomerResolutionService customerResolutionService;
	private ProductFacade productFacade;
	private CustomerAccountService customerAccountService;
	private BaseStoreService baseStoreService;
	private OrderService orderService;
	private ModelService modelService;


	/**
	 * Creates an empty {@link CCPaymentInfoData} object.
	 *
	 * @return An empty {@link CCPaymentInfoData} object
	 */
	@Nonnull
	protected CCPaymentInfoData createEmptyCCPaymentInfoData()
	{
		return new CCPaymentInfoData();
	}

	/**
	 * Creates an empty {@link SubscriptionPaymentData} object.
	 *
	 * @return An empty {@link SubscriptionPaymentData} object
	 */
	@Nonnull
	protected SubscriptionPaymentData createEmptySubscriptionPaymentData()
	{
		final SubscriptionPaymentData subscriptionPaymentData = new SubscriptionPaymentData();
		subscriptionPaymentData.setParameters(new HashMap<>());
		subscriptionPaymentData.setPostUrl(StringUtils.EMPTY);
		return subscriptionPaymentData;
	}

	@Nullable
	protected String getCurrencyIso()
	{
		final CartData cart = getCheckoutFacade().getCheckoutCart();
		if (cart != null && cart.getTotalPrice() != null)
		{
			return cart.getTotalPrice().getCurrencyIso();
		}
		if (getCustomerResolutionService().getCurrentCustomer() != null)
		{
			return getCustomerResolutionService().getCurrentCustomer().getSessionCurrency().getIsocode();
		}

		return null;
	}

	@Override
	@Nonnull
	public Date getSubscriptionEndDate(@Nonnull final ProductData subscriptionProductData, @Nullable final Date startDate)
	{

		validateParameterNotNullStandardMessage("subscriptionProductData", subscriptionProductData);

		final SubscriptionTermData subscriptionTerm = subscriptionProductData.getSubscriptionTerm();
		if (subscriptionTerm == null)
		{
			throw new IllegalStateException("Subscription product has no subscription term");
		}
		int contractDuration = subscriptionTerm.getTermOfServiceNumber();

		String frequencyCode;
		if (contractDuration == 0)
		{
			// if there is no contract, the duration corresponds to one billing cycle
			frequencyCode = subscriptionTerm.getBillingPlan().getBillingTime().getCode();
			contractDuration = 1;
		}
		else
		{
			final TermOfServiceFrequencyData termOfServiceFrequency = subscriptionTerm.getTermOfServiceFrequency();
			frequencyCode = termOfServiceFrequency == null
					? "null termOfServiceFrequency"
					: termOfServiceFrequency.getCode();
		}

		final Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);

		switch (frequencyCode)
		{
			case "annually":
			case "yearly":
				cal.add(Calendar.YEAR, contractDuration);
				break;
			case "quarterly":
				cal.add(Calendar.MONTH, 3 * contractDuration);
				break;
			case "monthly":
				cal.add(Calendar.MONTH, contractDuration);
				break;
			default:
				LOG.warn(String.format("Unknown frequency code \"%s\"", frequencyCode));
				break;
		}

		return cal.getTime();
	}

	@Nonnull
	@Override
	public List<ProductData> getUpsellingOptionsForSubscription(@Nonnull final String productCode)
	{
		final List<ProductOption> productOptions = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE);

		return getProductFacade().getProductReferencesForCode(productCode,
				Collections.singletonList(ProductReferenceTypeEnum.UPSELLING), productOptions, 100)
				.stream()
				.map(ProductReferenceData::getTarget)
				.collect(Collectors.toList());
	}

	@Nullable
	protected String getCurrentCustomerId()
	{
		if (getCustomerResolutionService().getCurrentCustomer() != null)
		{
			return getCustomerResolutionService().getCurrentCustomer().getCustomerID();
		}
		return null;
	}

	@Override
	@Nullable
	public AbstractOrderEntryModel getOrderEntryForOrderCodeAndEntryNumber(@Nonnull final String orderCode, final int entryNumber)
	{
		try
		{
			final OrderModel orderModel = getCustomerAccountService().getOrderForCode(
					getCustomerResolutionService().getCurrentCustomer(), orderCode, getBaseStoreService().getCurrentBaseStore());
			return getOrderService().getEntryForNumber(orderModel, entryNumber);
		}
		catch (final ModelNotFoundException | UnknownIdentifierException e)
		{
			return null;
		}
	}

	@Override
	@Nonnull
	public List<SubscriptionData> getSubscriptionsForPaymentMethod(@Nullable final String paymentMethodId)
			throws SubscriptionFacadeException
	{
		final List<SubscriptionData> paymentSubscriptions = new ArrayList<>();

		final Collection<SubscriptionData> allSubscriptions = this.getSubscriptions();

		paymentSubscriptions.addAll(allSubscriptions.stream().filter(subscriptionData -> StringUtils.equals(subscriptionData.getPaymentMethodId(), paymentMethodId)).collect(Collectors.toList()));

		return paymentSubscriptions;
	}

	@Override
	@Nonnull
	public CCPaymentInfoData updateCreatedPaymentMethod(@Nonnull final CCPaymentInfoData paymentInfo,
	                                                    @Nonnull final Map<String, String> paramMap)
			throws SubscriptionFacadeException
	{

		validateParameterNotNullStandardMessage("paymentInfo", paymentInfo);
		try
		{
			if (!(getCheckoutFacade().hasCheckoutCart()))
			{
				getCartService().getSessionCart();
			}
			final CreditCardPaymentInfoModel model = getModelService().get(PK.parse(paymentInfo.getId()));
			final String merchantPaymentMethodId = paramMap.get("merchantPaymentMethodId");

			if (merchantPaymentMethodId != null)
			{
				model.setSubscriptionServiceId(merchantPaymentMethodId);
				if (model.getSubscriptionId() != null)
				{
					LOG.warn(String.format("Overwriting subscriptionId: %s --> %s",
							model.getSubscriptionId(), merchantPaymentMethodId));
				}
				model.setSubscriptionId(merchantPaymentMethodId);

				getModelService().save(model);
			}
			return (getCreditCardPaymentInfoConverter().convert(model));
		}
		catch (final ModelSavingException e)
		{
			LOG.error("Saving the new subscriptionServiceId failed", e);
			throw new SubscriptionFacadeException("Saving the new payment info failed", e);
		}
	}

	protected CartService getCartService()
	{
		return cartService;
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	protected CheckoutFacade getCheckoutFacade()
	{
		return checkoutFacade;
	}

	@Required
	public void setCheckoutFacade(final CheckoutFacade checkoutFacade)
	{
		this.checkoutFacade = checkoutFacade;
	}

	protected Converter<CreditCardPaymentInfoModel, CCPaymentInfoData> getCreditCardPaymentInfoConverter()
	{
		return creditCardPaymentInfoConverter;
	}

	@Required
	public void setCreditCardPaymentInfoConverter(
			final Converter<CreditCardPaymentInfoModel, CCPaymentInfoData> creditCardPaymentInfoConverter)
	{
		this.creditCardPaymentInfoConverter = creditCardPaymentInfoConverter;
	}

	protected CustomerResolutionService getCustomerResolutionService()
	{
		return customerResolutionService;
	}

	@Required
	public void setCustomerResolutionService(final CustomerResolutionService customerResolutionService)
	{
		this.customerResolutionService = customerResolutionService;
	}

	protected ProductFacade getProductFacade()
	{
		return productFacade;
	}

	@Required
	public void setProductFacade(final ProductFacade productFacade)
	{
		this.productFacade = productFacade;
	}

	protected CustomerAccountService getCustomerAccountService()
	{
		return customerAccountService;
	}

	@Required
	public void setCustomerAccountService(final CustomerAccountService customerAccountService)
	{
		this.customerAccountService = customerAccountService;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
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

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
