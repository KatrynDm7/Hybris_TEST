/*
 *
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
 */
package de.hybris.platform.financialfacades.populators;


import de.hybris.platform.commercefacades.insurance.data.InsuranceQuoteListingData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.quotation.InsuranceQuoteData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.financialservices.enums.QuoteBindingState;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

import java.util.Date;

import net.sourceforge.pmd.util.StringUtil;

import org.apache.commons.collections.CollectionUtils;


public class InsuranceCart2QuoteListingPopulator<SOURCE extends CartData, TARGET extends InsuranceQuoteListingData>

implements Populator<SOURCE, TARGET>
{
	/**
	 * Populate the target instance with values from the source instance.
	 *
	 * @param source
	 *           the source object
	 * @param target
	 *           the target to fill
	 * @throws de.hybris.platform.servicelayer.dto.converter.ConversionException
	 *            if an error occurs
	 */
	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException
	{
		ServicesUtil.validateParameterNotNull(source, "CartData cannot be null.");
		ServicesUtil.validateParameterNotNull(target, "InsuranceQuoteListingData cannot be null.");

		target.setQuotePrice(source.getTotalPrice().getFormattedValue());
		if (source.getEntries() != null && !source.getEntries().isEmpty())
		{
			// The quotes listing item details should come from the Main Product Plan, which in this project,
			// with no hard-wired flagging or identifier, it is as a practice to assume the Main Product Plan will be
			// inside the first entry of the cart.
			final ProductData product = source.getEntries().get(0).getProduct();
			final SubscriptionPricePlanData pricePlan = ((SubscriptionPricePlanData) product.getPrice());
			if (CollectionUtils.isNotEmpty(pricePlan.getRecurringChargeEntries()))
			{
				target.setIsMonthly(Boolean.TRUE);
			}
			else
			{
				target.setIsMonthly(Boolean.FALSE);
			}
			target.setPlanName(product.getName());
			target.setQuoteImages(product.getImages());
		}

		final InsuranceQuoteData quoteData = source.getInsuranceQuote();
		if (quoteData != null)
		{
			if (StringUtil.isNotEmpty(quoteData.getQuoteId()))
			{
				target.setQuoteNumber(quoteData.getQuoteId());
			}
			else
			{
				target.setQuoteNumber(source.getCode());
			}
			target.setRetrieveQuoteCartCode(source.getCode());
			target.setQuoteExpiryDate(quoteData.getFormattedExpiryDate());
			target.setQuoteRawExpiryDate(quoteData.getExpiryDate());

			if (QuoteBindingState.BIND.equals(quoteData.getState()))
			{
				target.setQuoteIsBind(Boolean.TRUE);
			}
			else
			{
				target.setQuoteIsBind(Boolean.FALSE);
			}


		}
		setQuoteIsExpired(target, quoteData);
		setQuoteStatus(target);
	}

	/**
	 * Sets the expiry status according to the quote expiry date
	 *
	 * @param target
	 * @param quoteData
	 */
	protected void setQuoteIsExpired(final InsuranceQuoteListingData target, final InsuranceQuoteData quoteData)
	{
		final Date expiryDate = quoteData.getExpiryDate();
		if (expiryDate != null && expiryDate.compareTo(new Date()) < 0)
		{
			target.setQuoteIsExpired(Boolean.TRUE);
		}
		else
		{
			target.setQuoteIsExpired(Boolean.FALSE);

		}
	}

	/**
	 * The status of the quote 'Unfinished' <when it's not bound> 'Ready' for purchase <when it's bound and not expired>
	 * 'Expired'
	 *
	 * @param quoteData
	 *
	 */
	protected void setQuoteStatus(final InsuranceQuoteListingData target)
	{
		if (target.getQuoteIsExpired())
		{
			target.setQuoteStatus("expired");
		}
		else if (!target.getQuoteIsBind())
		{
			target.setQuoteStatus("unfinished");
		}
		else
		{
			target.setQuoteStatus("readyForPurchase");
		}
	}

}
