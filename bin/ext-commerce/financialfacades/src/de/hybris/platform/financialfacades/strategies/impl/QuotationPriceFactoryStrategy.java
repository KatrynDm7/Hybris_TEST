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

package de.hybris.platform.financialfacades.strategies.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.quotation.QuotationRequestData;
import de.hybris.platform.commercefacades.quotation.QuotationResponseData;
import de.hybris.platform.configurablebundleservices.bundle.impl.FindBundlePricingWithCurrentPriceFactoryStrategy;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.financialfacades.facades.QuotationPricingFacade;
import de.hybris.platform.financialservices.services.QuotationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.subscriptionservices.enums.TermOfServiceFrequency;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionProductModel;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.PriceValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


public class QuotationPriceFactoryStrategy extends FindBundlePricingWithCurrentPriceFactoryStrategy
{
	private Map<String, QuotationService> quotationServices;
	private SubscriptionPricePlanModel pricePlan;
	private QuotationPricingFacade quotationPricingFacade;
	protected static String BASE_PRICE_TAG = Config.getString("insurance.baseprice.tag.name", "baseprice");


	/**
	 * If the PricePlan is configured with the quotation engine then resolves the subscription price value for the given
	 * AbstractOrderEntryModel by getting the price value from the quotation engine. Otherwise the standard method in the
	 * super implementations is called
	 */
	@Override
	public PriceValue findBasePrice(final AbstractOrderEntryModel entry) throws CalculationException
	{
		final AbstractOrderModel order = entry.getOrder();

		PriceValue price;

		if (isQuotationPrice(entry))
		{
			final QuotationRequestData quotationRequestData = getQuotationService(pricePlan.getQuotationProvider())
					.createQuotationRequestData(pricePlan.getQuotationProvider(), entry.getProduct().getCode(),
							order.getInsuranceQuote());

			// temporary method implementation for demo to calculate optional product based on base price.
			final String basePrice = getBasePrice(entry);
			if (basePrice != null)
			{
				quotationRequestData.getProperties().put(BASE_PRICE_TAG, basePrice);
			}

			final QuotationResponseData responseData = getQuotationPricingFacade().getQuote(quotationRequestData);

			Double payNowPrice = 0d;

			if (getQuotationService(pricePlan.getQuotationProvider()) != null)
			{
				if (TermOfServiceFrequency.MONTHLY.equals(((SubscriptionProductModel) entry.getProduct()).getSubscriptionTerm()
						.getTermOfServiceFrequency()))
				{
					payNowPrice = getQuotationService(pricePlan.getQuotationProvider()).getPrice(responseData,
							entry.getProduct().getCode(), false);
				}
				else
				{
					payNowPrice = getQuotationService(pricePlan.getQuotationProvider()).getPrice(responseData,
							entry.getProduct().getCode(), true);
				}

			}

			price = new PriceValue(order.getCurrency().getIsocode(), payNowPrice, order.getNet().booleanValue());
		}
		else
		{
			price = super.findBasePrice(entry);
		}

		return price;
	}

	// temporary method implementation for demo to calculate optional product based on base price.
	private String getBasePrice(final AbstractOrderEntryModel entry)
	{
		String returnValue = null;

		if (entry != null && entry.getOrder() != null && entry.getOrder().getEntries() != null)
		{
			//returnValue = entry.getBasePrice().toString();
			final AbstractOrderEntryModel orderEntry = entry.getOrder().getEntries().get(0);

			returnValue = orderEntry.getBasePrice().toString();
		}

		return returnValue;
	}

	protected QuotationService getQuotationService(final String serviceName)
	{
		validateParameterNotNullStandardMessage("There is no QuotationServices mapped to this strategy", getQuotationServices());
		validateParameterNotNullStandardMessage("Quotation service name", serviceName);

		return getQuotationServices().get(serviceName);
	}

	@Override
	public List<DiscountValue> findDiscountValues(final AbstractOrderEntryModel entry) throws CalculationException
	{
		List<DiscountValue> discountValues = new ArrayList<DiscountValue>();

		//If we are looking for the hybris price then find the discount values from the existing strategy.
		if (!isQuotationPrice(entry))
		{
			discountValues = super.findDiscountValues(entry);
		}

		return discountValues;
	}

	/**
	 * Helper method to find whether the PricePlan is configured with the quotation engine.
	 *
	 * @param entry
	 * @return true if the PricePlan has quotation engine else false
	 */
	protected boolean isQuotationPrice(final AbstractOrderEntryModel entry)
	{
		boolean returnValue = false;

		if (entry.getProduct() instanceof SubscriptionProductModel)
		{
			pricePlan = getCommercePriceService().getSubscriptionPricePlanForEntry(entry);

			if (pricePlan != null && getQuotationServices().containsKey(pricePlan.getQuotationProvider()))
			{
				returnValue = true;
			}
		}

		return returnValue;
	}

	private Map<String, QuotationService> getQuotationServices()
	{
		return quotationServices;
	}

	@Required
	public void setQuotationServices(final Map<String, QuotationService> quotationServices)
	{
		this.quotationServices = quotationServices;
	}

	public QuotationPricingFacade getQuotationPricingFacade()
	{
		return quotationPricingFacade;
	}

	@Required
	public void setQuotationPricingFacade(final QuotationPricingFacade quotationPricingFacade)
	{
		this.quotationPricingFacade = quotationPricingFacade;
	}

}
