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

import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.PerUnitUsageChargeData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.VolumeUsageChargeData;
import de.hybris.platform.subscriptionservices.enums.UsageChargeType;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.hybris.cis.api.subscription.model.CisUsageCharge;
import com.hybris.cis.api.subscription.model.CisUsageChargeTier;


/**
 * Populate the CisUsageCharge with the UsageChargeData information
 */
public class CisUsageChargePopulator implements Populator<UsageChargeData, CisUsageCharge>
{

	private static final String UNIT_EACH_RESPECTIVE_TIER = "UNIT_EACH_RESPECTIVE_TIER";
	private static final String VOLUME = "VOLUME";
	private static final String UNIT_HIGHEST_APPLICABLE_TIER = "UNIT_HIGHEST_APPLICABLE_TIER";

	private Converter<UsageChargeEntryData, CisUsageChargeTier> cisUsageChargeTierConverter;

	@Override
	public void populate(final UsageChargeData source, final CisUsageCharge target) throws ConversionException
	{
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source == null)
		{
			return;
		}

		target.setName(source.getName());
		target.setUnitId(source.getUsageUnit().getId());

		if (source instanceof VolumeUsageChargeData)
		{
			target.setType(VOLUME);
		}
		else if (source instanceof PerUnitUsageChargeData)
		{
			final PerUnitUsageChargeData perUnitUsageChargeData = ((PerUnitUsageChargeData) source);

			if (UsageChargeType.EACH_RESPECTIVE_TIER.name().equalsIgnoreCase(perUnitUsageChargeData.getUsageChargeType().getName()))
			{
				target.setType(UNIT_EACH_RESPECTIVE_TIER);
			}
			else if (UsageChargeType.HIGHEST_APPLICABLE_TIER.name().equalsIgnoreCase(
					perUnitUsageChargeData.getUsageChargeType().getName()))
			{
				target.setType(UNIT_HIGHEST_APPLICABLE_TIER);
			}
		}

		final List<CisUsageChargeTier> cisTiers = new ArrayList<CisUsageChargeTier>();

		cisTiers.addAll(Converters.convertAll(source.getUsageChargeEntries(), getCisUsageChargeTierConverter()));


		target.setTiers(cisTiers);
	}

	protected Converter<UsageChargeEntryData, CisUsageChargeTier> getCisUsageChargeTierConverter()
	{
		return cisUsageChargeTierConverter;
	}

	@Required
	public void setCisUsageChargeTierConverter(
			final Converter<UsageChargeEntryData, CisUsageChargeTier> cisUsageChargeTierConverter)
	{
		this.cisUsageChargeTierConverter = cisUsageChargeTierConverter;
	}

}
