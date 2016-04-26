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

import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.PerUnitUsageChargeData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeTypeData;
import de.hybris.platform.subscriptionservices.enums.UsageChargeType;
import de.hybris.platform.subscriptionservices.model.PerUnitUsageChargeModel;

import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

/**
 * Populate DTO {@link PerUnitUsageChargeData} with data from {@link PerUnitUsageChargeModel}.
 *
 * @param <SOURCE> source class
 * @param <TARGET> target class
 */
public class PerUnitUsageChargePopulator<SOURCE extends PerUnitUsageChargeModel, TARGET extends PerUnitUsageChargeData> extends
		AbstractUsageChargePopulator<SOURCE, TARGET>
{

	private Converter<UsageChargeType, UsageChargeTypeData> usageChargeTypeConverter;

	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		if (source.getUsageChargeType() != null)
		{
			target.setUsageChargeType(getUsageChargeTypeConverter().convert(source.getUsageChargeType()));
		}
		super.populate(source, target);
	}

	protected Converter<UsageChargeType, UsageChargeTypeData> getUsageChargeTypeConverter()
	{
		return usageChargeTypeConverter;
	}

	@Required
	public void setUsageChargeTypeConverter(final Converter<UsageChargeType, UsageChargeTypeData> usageChargeTypeConverter)
	{
		this.usageChargeTypeConverter = usageChargeTypeConverter;
	}
}
