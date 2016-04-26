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

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.RecurringChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;
import de.hybris.platform.subscriptionservices.model.RecurringChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Populate DTO {@link SubscriptionPricePlanData}.recurringChargeEntries with data from
 * {@link SubscriptionPricePlanModel#_recurringChargeEntries}.
 *
 * @param <SOURCE> source class
 * @param <TARGET> target class
 */
public class SubscriptionPricePlanRecurringChargePopulator<SOURCE extends SubscriptionPricePlanModel,
		TARGET extends SubscriptionPricePlanData> implements Populator<SOURCE, TARGET>
{
	private Converter<RecurringChargeEntryModel, RecurringChargeEntryData> recurringChargeEntryConverter;

	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		if (CollectionUtils.isEmpty(source.getRecurringChargeEntries()))
		{
			target.setRecurringChargeEntries(Collections.emptyList());
		}
		else
		{
			final List<RecurringChargeEntryData> recurringChargeEntries = Converters.convertAll(source.getRecurringChargeEntries(),
					getRecurringChargeEntryConverter());
			target.setRecurringChargeEntries(recurringChargeEntries);
		}
	}

	protected Converter<RecurringChargeEntryModel, RecurringChargeEntryData> getRecurringChargeEntryConverter()
	{
		return recurringChargeEntryConverter;
	}

	@Required
	public void setRecurringChargeEntryConverter(
			final Converter<RecurringChargeEntryModel, RecurringChargeEntryData> recurringChargeEntryConverter)
	{
		this.recurringChargeEntryConverter = recurringChargeEntryConverter;
	}

}
