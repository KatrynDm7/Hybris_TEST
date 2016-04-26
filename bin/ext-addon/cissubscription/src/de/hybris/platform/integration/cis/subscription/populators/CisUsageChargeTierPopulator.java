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

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.subscriptionfacades.data.OverageUsageChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.TierUsageChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeEntryData;

import org.springframework.util.Assert;

import com.hybris.cis.api.subscription.model.CisUsageChargeTier;


/**
 * Populate the CisUsageChargeTierEntry with the UsageChargeEntryData information
 */
public class CisUsageChargeTierPopulator implements Populator<UsageChargeEntryData, CisUsageChargeTier>
{
	@Override
	public void populate(final UsageChargeEntryData source, final CisUsageChargeTier target) throws ConversionException
	{
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source == null)
		{
			return;
		}

		target.setChargePrice(source.getPrice().getValue());

		if (source instanceof OverageUsageChargeEntryData)
		{
			target.setNumberOfUnits(Integer.valueOf(0));
		}
		else if (source instanceof TierUsageChargeEntryData)
		{
			final TierUsageChargeEntryData tierUsageChargeEntryData = (TierUsageChargeEntryData) source;
			target.setNumberOfUnits(Integer.valueOf(tierUsageChargeEntryData.getTierEnd() - tierUsageChargeEntryData.getTierStart()));
		}
	}
}
