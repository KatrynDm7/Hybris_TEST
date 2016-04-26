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
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.PerUnitUsageChargeData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeData;
import de.hybris.platform.subscriptionfacades.data.VolumeUsageChargeData;
import de.hybris.platform.subscriptionservices.model.PerUnitUsageChargeModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.subscriptionservices.model.UsageChargeModel;
import de.hybris.platform.subscriptionservices.model.VolumeUsageChargeModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

/**
 * Populate DTO {@link SubscriptionPricePlanData}.usageCharges with data from
 * {@link SubscriptionPricePlanModel#_usageCharges}.
 *
 * @param <SOURCE> source class
 * @param <TARGET> target class
 */
public class SubscriptionPricePlanUsageChargePopulator<SOURCE extends SubscriptionPricePlanModel,
		TARGET extends SubscriptionPricePlanData> implements Populator<SOURCE, TARGET>
{
	private Converter<PerUnitUsageChargeModel, PerUnitUsageChargeData> perUnitUsageChargeConverter;
	private Converter<VolumeUsageChargeModel, VolumeUsageChargeData> volumeUsageChargeConverter;

	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		if (CollectionUtils.isEmpty(source.getUsageCharges()))
		{
			target.setUsageCharges(Collections.emptyList());
		}
		else
		{
			final List<UsageChargeData> usageCharges = new ArrayList<>();

			for (final UsageChargeModel usageCharge : source.getUsageCharges())
			{
				if (usageCharge instanceof PerUnitUsageChargeModel)
				{
					usageCharges.add(getPerUnitUsageChargeConverter().convert((PerUnitUsageChargeModel) usageCharge));
				}
				else if (usageCharge instanceof VolumeUsageChargeModel)
				{
					usageCharges.add(getVolumeUsageChargeConverter().convert((VolumeUsageChargeModel) usageCharge));
				}
			}

			target.setUsageCharges(usageCharges);
		}
	}

	protected Converter<PerUnitUsageChargeModel, PerUnitUsageChargeData> getPerUnitUsageChargeConverter()
	{
		return perUnitUsageChargeConverter;
	}

	@Required
	public void setPerUnitUsageChargeConverter(
			final Converter<PerUnitUsageChargeModel, PerUnitUsageChargeData> perUnitUsageChargeConverter)
	{
		this.perUnitUsageChargeConverter = perUnitUsageChargeConverter;
	}

	protected Converter<VolumeUsageChargeModel, VolumeUsageChargeData> getVolumeUsageChargeConverter()
	{
		return volumeUsageChargeConverter;
	}

	@Required
	public void setVolumeUsageChargeConverter(
			final Converter<VolumeUsageChargeModel, VolumeUsageChargeData> volumeUsageChargeConverter)
	{
		this.volumeUsageChargeConverter = volumeUsageChargeConverter;
	}
}
