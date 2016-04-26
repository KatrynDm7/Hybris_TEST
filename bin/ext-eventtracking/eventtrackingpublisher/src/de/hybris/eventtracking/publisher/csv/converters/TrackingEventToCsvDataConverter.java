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
package de.hybris.eventtracking.publisher.csv.converters;

import de.hybris.eventtracking.model.events.AbstractTrackingEvent;
import de.hybris.eventtracking.publisher.csv.model.TrackingEventCsvData;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;


/**
 * @author stevo.slavic
 *
 */
public class TrackingEventToCsvDataConverter extends AbstractPopulatingConverter<AbstractTrackingEvent, TrackingEventCsvData>
{

	public TrackingEventToCsvDataConverter()
	{
		setTargetClass(TrackingEventCsvData.class);
		
	}
}
