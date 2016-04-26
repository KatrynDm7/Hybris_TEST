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
package de.hybris.eventtracking.services.validators;

import java.io.IOException;

import org.springframework.core.io.Resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;


/**
 * @author stevo.slavic
 *
 */
public class TrackingEventJsonValidator
{
	private final ObjectMapper mapper;

	private final JsonSchema eventTrackingSchema;

	public TrackingEventJsonValidator(final ObjectMapper mapper, final Resource eventTrackingSchema)
			throws JsonProcessingException, ProcessingException, IOException
	{
		this.mapper = mapper;

		final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
		this.eventTrackingSchema = factory.getJsonSchema(mapper.readTree(eventTrackingSchema.getInputStream()));
	}

	public ProcessingReport validate(final String rawTrackingEvent) throws JsonProcessingException, IOException,
			ProcessingException
	{
		final JsonNode instance = mapper.readTree(rawTrackingEvent);
		final ProcessingReport processingReport = eventTrackingSchema.validate(instance);

		return processingReport;
	}
}
