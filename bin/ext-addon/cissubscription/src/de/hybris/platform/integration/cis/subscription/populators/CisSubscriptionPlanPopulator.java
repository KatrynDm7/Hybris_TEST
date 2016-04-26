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
package de.hybris.platform.integration.cis.subscription.populators;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.ChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.RecurringChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeData;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.hybris.cis.api.subscription.model.CisChargeEntry;
import com.hybris.cis.api.subscription.model.CisSubscriptionPlan;
import com.hybris.cis.api.subscription.model.CisUsageCharge;


/**
 * Populate the CisSubscriptionPlan with the SubscriptionPricePlanData information
 */
public class CisSubscriptionPlanPopulator implements Populator<ProductData, CisSubscriptionPlan>
{
	private Converter<ChargeEntryData, CisChargeEntry> cisChargeEntryConverter;
	private Converter<UsageChargeData, CisUsageCharge> cisUsageChargeConverter;

	@Override
	public void populate(final ProductData source, final CisSubscriptionPlan target) throws ConversionException
	{
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source == null)
		{
			return;
		}

		if (!(source.getPrice() instanceof SubscriptionPricePlanData))
		{
			return;
		}

		final SubscriptionPricePlanData pricePlan = (SubscriptionPricePlanData) source.getPrice();

		target.setId(source.getCode() + "_" + source.getModifiedTime().getTime());
		target.setName(pricePlan.getName());

		final List<CisChargeEntry> cisCharges = new ArrayList<CisChargeEntry>();
		final List<CisUsageCharge> cisUsageCharges = new ArrayList<CisUsageCharge>();

		for (final OneTimeChargeEntryData oneTimeChargeEntry : pricePlan.getOneTimeChargeEntries())
		{
			// ignore "paynow", because that must be handled with a transaction call
			if (!"paynow".equalsIgnoreCase(oneTimeChargeEntry.getBillingTime().getCode()))
			{
				final CisChargeEntry cisChargeEntry = this.getCisChargeEntryConverter().convert(oneTimeChargeEntry);
				cisCharges.add(cisChargeEntry);
			}
		}

		for (final RecurringChargeEntryData recurringChargeEntry : pricePlan.getRecurringChargeEntries())
		{
			final CisChargeEntry cisChargeEntry = this.getCisChargeEntryConverter().convert(recurringChargeEntry);
			cisCharges.add(cisChargeEntry);
		}

		cisUsageCharges.addAll(Converters.convertAll(pricePlan.getUsageCharges(), getCisUsageChargeConverter()));

		target.setCharges(cisCharges);
		target.setUsageCharges(cisUsageCharges);
	}

	protected Converter<ChargeEntryData, CisChargeEntry> getCisChargeEntryConverter()
	{
		return cisChargeEntryConverter;
	}

	@Required
	public void setCisChargeEntryConverter(final Converter<ChargeEntryData, CisChargeEntry> cisChargeEntryConverter)
	{
		this.cisChargeEntryConverter = cisChargeEntryConverter;
	}

	protected Converter<UsageChargeData, CisUsageCharge> getCisUsageChargeConverter()
	{
		return cisUsageChargeConverter;
	}

	@Required
	public void setCisUsageChargeConverter(final Converter<UsageChargeData, CisUsageCharge> cisUsageChargeConverter)
	{
		this.cisUsageChargeConverter = cisUsageChargeConverter;
	}

}
