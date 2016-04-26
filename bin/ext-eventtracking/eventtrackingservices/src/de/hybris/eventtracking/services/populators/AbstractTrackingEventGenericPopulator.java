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
import de.hybris.eventtracking.services.constants.TrackingEventJsonFields;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author stevo.slavic
 *
 */
public abstract class AbstractTrackingEventGenericPopulator implements
		GenericPopulator<Map<String, Object>, AbstractTrackingEvent>
{
	private final ObjectMapper mapper;

	public AbstractTrackingEventGenericPopulator(final ObjectMapper mapper)
	{
		this.mapper = mapper;
	}

	public ObjectMapper getMapper()
	{
		return mapper;
	}

	protected Map<String, Object> getPageScopedCvar(final Map<String, Object> trackingEventData)
	{
		final String cvar = (String) trackingEventData.get(TrackingEventJsonFields.COMMON_CVAR_PAGE.getKey());
		Map<String, Object> customVariablesPageScoped = null;
		if (StringUtils.isNotBlank(cvar))
		{
			try
			{
				customVariablesPageScoped = getMapper().readValue(cvar, Map.class);
			}
			catch (final IOException e)
			{
				throw new ConversionException("Error extracting custom page scoped variables from: " + cvar, e);
			}
		}
		return customVariablesPageScoped;
	}
}
