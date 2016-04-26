/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.eventtracking.services.populators;

import de.hybris.eventtracking.model.events.AbstractTrackingEvent;
import de.hybris.eventtracking.model.events.CategoryPageViewEvent;
import de.hybris.eventtracking.services.constants.TrackingEventJsonFields;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author stevo.slavic
 *
 */
public class CategoryPageViewEventPopulator extends AbstractTrackingEventGenericPopulator
{

	public CategoryPageViewEventPopulator(final ObjectMapper mapper)
	{
		super(mapper);
	}

	/**
	 * @see de.hybris.eventtracking.services.populators.GenericPopulator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(final Class<?> clazz)
	{
		return CategoryPageViewEvent.class.isAssignableFrom(clazz);
	}

	/**
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final Map<String, Object> trackingEventData, final AbstractTrackingEvent trackingEvent)
			throws ConversionException
	{
		String categoryId = null;
		String categoryName = null;

		final String categoryInfo = (String) trackingEventData.get(TrackingEventJsonFields.SEARCH_CATEGORY.getKey());
		final String[] categoryInforArray = categoryInfo.split(":", 2);
		if (categoryInforArray.length == 1)
		{
			categoryName = categoryInforArray[0];
		}
		else if (categoryInforArray.length == 2)
		{
			categoryId = categoryInforArray[0];
			categoryName = categoryInforArray[1];
		}

		((CategoryPageViewEvent) trackingEvent).setCategoryId(categoryId);
		((CategoryPageViewEvent) trackingEvent).setCategoryName(categoryName);
		((CategoryPageViewEvent) trackingEvent).setEventType("CategoryPageViewEvent");
	}

}
