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

package de.hybris.platform.financialfacades.populators;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.impl.DefaultPriceDataFactory;
import de.hybris.platform.commercefacades.quotation.QuotationRequestData;
import de.hybris.platform.commercefacades.quotation.QuotationResponseData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.financialfacades.facades.QuotationPricingFacade;
import de.hybris.platform.financialservices.services.QuotationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;
import de.hybris.platform.subscriptionfacades.product.converters.populator.SubscriptionProductPricePlanPopulator;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionProductModel;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


public class DefaultProductQuotationPricePopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		SubscriptionProductPricePlanPopulator<SOURCE, TARGET>
{
	private QuotationPricingFacade quotationPricingFacade;
	private DefaultPriceDataFactory priceFactory;
	private Map<String, QuotationService> quotationServices;
	private SubscriptionPricePlanModel pricePlan;

	protected static String BILLING_EVENT_PAY_NOW = "paynow";

	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException
	{
		super.populate(source, target);

		if (!(source instanceof SubscriptionProductModel))
		{
			return;
		}

		if (target.getPrice() != null && target.getPrice() instanceof SubscriptionPricePlanData
				&& isQuotationPrice((SubscriptionProductModel) source))
		{
			final SubscriptionPricePlanData pricePlanData = (SubscriptionPricePlanData) target.getPrice();

			populatePriceData(target, pricePlanData);

			target.setPrice(pricePlanData);
		}
	}

	/**
	 * Helper method used to populate the OneTimeChargeEntryData and RecurringChargeEntryData price, which is retrieved
	 * from the quotation engine based on the QuotationProvider that have been mapped to the PricePlan
	 *
	 * @param target
	 * @param pricePlanData
	 */
	protected void populatePriceData(final TARGET target, final SubscriptionPricePlanData pricePlanData)
	{

		final QuotationService quotationService = getQuotationService(pricePlan.getQuotationProvider());
		final QuotationRequestData quotationRequestData = quotationService.createQuotationRequestData(
				pricePlan.getQuotationProvider(), target.getCode(), null);

		final QuotationResponseData responseData = getQuotationPricingFacade().getQuote(quotationRequestData);

		final Double payNowPrice = quotationService.getPrice(responseData, target.getCode(), true);

		for (final OneTimeChargeEntryData oneTimeEntry : pricePlanData.getOneTimeChargeEntries())
		{
			if (BILLING_EVENT_PAY_NOW.equals(oneTimeEntry.getBillingTime().getCode()))
			{
				oneTimeEntry.getPrice().setValue(BigDecimal.valueOf(payNowPrice));
				final PriceData priceData = getPriceFactory().create(PriceDataType.BUY, oneTimeEntry.getPrice().getValue(),
						oneTimeEntry.getPrice().getCurrencyIso());
				oneTimeEntry.getPrice().setFormattedValue(priceData.getFormattedValue());
				break;
			}
		}

		if (hasRecurringCharge())
		{
			final Double recurringPrice = quotationService.getPrice(responseData, target.getCode(), false);

			if (CollectionUtils.isNotEmpty(pricePlanData.getRecurringChargeEntries()))
			{
				final PriceData priceData = getPriceFactory().create(PriceDataType.BUY, BigDecimal.valueOf(recurringPrice),
						pricePlanData.getRecurringChargeEntries().iterator().next().getPrice().getCurrencyIso());

				final PriceData price = pricePlanData.getRecurringChargeEntries().iterator().next().getPrice();
				price.setFormattedValue(priceData.getFormattedValue());
				price.setValue(new BigDecimal(recurringPrice));
			}

		}

	}

	/**
	 * Helper method to find whether the {@link SubscriptionPricePlanModel} has any recurringCharges
	 *
	 * @return true if it has recurringCharges other wise false
	 */
	protected boolean hasRecurringCharge()
	{
		boolean returnValue = false;

		if (CollectionUtils.isNotEmpty(pricePlan.getRecurringChargeEntries()))
		{
			returnValue = true;
		}

		return returnValue;
	}

	protected QuotationService getQuotationService(final String serviceName)
	{
		validateParameterNotNullStandardMessage("There is no QuotationServices mapped to this strategy", getQuotationServices());
		validateParameterNotNullStandardMessage("Quotation service name", serviceName);

		return getQuotationServices().get(serviceName);
	}

	/**
	 * Helper method to find whether the PricePlan is configured with the quotation engine provider.
	 *
	 * @param subscriptionProduct
	 * @return true if the PricePlan has quotation engine else false
	 */
	protected boolean isQuotationPrice(final SubscriptionProductModel subscriptionProduct)
	{
		boolean returnValue = false;

		pricePlan = getCommercePriceService().getSubscriptionPricePlanForProduct(subscriptionProduct);

		if (pricePlan != null && getQuotationServices().containsKey(pricePlan.getQuotationProvider()))
		{
			returnValue = true;
		}

		return returnValue;
	}

	protected QuotationPricingFacade getQuotationPricingFacade()
	{
		return quotationPricingFacade;
	}

	@Required
	public void setQuotationPricingFacade(final QuotationPricingFacade quotationPricingFacade)
	{
		this.quotationPricingFacade = quotationPricingFacade;
	}


	protected DefaultPriceDataFactory getPriceFactory()
	{
		return priceFactory;
	}

	@Required
	public void setPriceFactory(final DefaultPriceDataFactory priceFactory)
	{
		this.priceFactory = priceFactory;
	}


	protected Map<String, QuotationService> getQuotationServices()
	{
		return quotationServices;
	}

	@Required
	public void setQuotationServices(final Map<String, QuotationService> quotationServices)
	{
		this.quotationServices = quotationServices;
	}


}
