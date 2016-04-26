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
package de.hybris.eventtracking.services.converters;

import de.hybris.eventtracking.model.events.AbstractTrackingEvent;

import java.util.Map;


/**
 * @author stevo.slavic
 *
 */
public class JsonToTrackingEventConverter extends AbstractPopulatingDynamicConverter<Map<String, Object>, AbstractTrackingEvent>
{

	public JsonToTrackingEventConverter(final TypeResolver<Map<String, Object>, AbstractTrackingEvent> typeResolver)
	{
		super(typeResolver);
	}
}
