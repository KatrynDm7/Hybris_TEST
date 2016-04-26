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
package de.hybris.platform.subscriptionfacades.product.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.subscriptionfacades.data.UsageUnitData;
import de.hybris.platform.subscriptionservices.model.UsageUnitModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.OverageUsageChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.TierUsageChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeEntryData;
import de.hybris.platform.subscriptionservices.model.OverageUsageChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.TierUsageChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.UsageChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.UsageChargeModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

/**
 * Populate DTO {@link UsageChargeData} with data from {@link UsageChargeModel}.
 *
 * @param <SOURCE> source class
 * @param <TARGET> target class
 */
public abstract class AbstractUsageChargePopulator<SOURCE extends UsageChargeModel, TARGET extends UsageChargeData> implements
		Populator<SOURCE, TARGET>
{

	private Converter<UsageUnitModel, UsageUnitData> usageUnitConverter;
	private Converter<TierUsageChargeEntryModel, TierUsageChargeEntryData> tierUsageChargeEntryConverter;
	private Converter<OverageUsageChargeEntryModel, OverageUsageChargeEntryData> overageUsageChargeEntryConverter;

	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		target.setName(source.getName());

		if (source.getUsageUnit() != null)
		{
			target.setUsageUnit(getUsageUnitConverter().convert(source.getUsageUnit()));
		}

		if (CollectionUtils.isEmpty(source.getUsageChargeEntries()))
		{
			target.setUsageChargeEntries(Collections.emptyList());
		}
		else
		{
			final List<UsageChargeEntryData> usageChargeEntries = new ArrayList<>();

			for (final UsageChargeEntryModel usageChargeEntry : source.getUsageChargeEntries())
			{
				if (usageChargeEntry instanceof TierUsageChargeEntryModel)
				{
					usageChargeEntries.add(getTierUsageChargeEntryConverter().convert((TierUsageChargeEntryModel) usageChargeEntry));
				}
				else if (usageChargeEntry instanceof OverageUsageChargeEntryModel)
				{
					usageChargeEntries.add(getOverageUsageChargeEntryConverter().convert(
							(OverageUsageChargeEntryModel) usageChargeEntry));
				}
			}

			target.setUsageChargeEntries(usageChargeEntries);
		}
	}

	protected Converter<UsageUnitModel, UsageUnitData> getUsageUnitConverter()
	{
		return usageUnitConverter;
	}

	@Required
	public void setUsageUnitConverter(final Converter<UsageUnitModel, UsageUnitData> usageUnitConverter)
	{
		this.usageUnitConverter = usageUnitConverter;
	}

	protected Converter<TierUsageChargeEntryModel, TierUsageChargeEntryData> getTierUsageChargeEntryConverter()
	{
		return tierUsageChargeEntryConverter;
	}

	@Required
	public void setTierUsageChargeEntryConverter(
			final Converter<TierUsageChargeEntryModel, TierUsageChargeEntryData> tierUsageChargeEntryConverter)
	{
		this.tierUsageChargeEntryConverter = tierUsageChargeEntryConverter;
	}

	protected Converter<OverageUsageChargeEntryModel, OverageUsageChargeEntryData> getOverageUsageChargeEntryConverter()
	{
		return overageUsageChargeEntryConverter;
	}

	@Required
	public void setOverageUsageChargeEntryConverter(
			final Converter<OverageUsageChargeEntryModel, OverageUsageChargeEntryData> overageUsageChargeEntryConverter)
	{
		this.overageUsageChargeEntryConverter = overageUsageChargeEntryConverter;
	}

}
