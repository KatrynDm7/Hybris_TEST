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

import de.hybris.eventtracking.model.events.AbstractCartAwareTrackingEvent;
import de.hybris.eventtracking.model.events.AbstractTrackingEvent;
import de.hybris.eventtracking.services.constants.TrackingEventJsonFields;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author stevo.slavic
 * 
 */
public class AbstractCartAwareTrackingEventPopulator extends AbstractTrackingEventGenericPopulator
{

	public AbstractCartAwareTrackingEventPopulator(final ObjectMapper mapper)
	{
		super(mapper);
	}

	/**
	 * @see de.hybris.eventtracking.services.populators.GenericPopulator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(final Class<?> clazz)
	{
		return AbstractCartAwareTrackingEvent.class.isAssignableFrom(clazz);
	}

	/**
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final Map<String, Object> trackingEventData, final AbstractTrackingEvent trackingEvent)
			throws ConversionException
	{
		final Map<String, Object> customVariablesPageScoped = getPageScopedCvar(trackingEventData);

		String cartId = null;
		if (customVariablesPageScoped != null)
		{
			final List<String> cvrIndex1 = (List<String>) customVariablesPageScoped.get("1");
			if (cvrIndex1 != null && cvrIndex1.size() > 1
					&& TrackingEventJsonFields.COMMERCE_ORDER_ID.getKey().equals(cvrIndex1.get(0)))
			{
				cartId = cvrIndex1.get(1);
			}
		}

		((AbstractCartAwareTrackingEvent) trackingEvent).setCartId(cartId);
	}
}
