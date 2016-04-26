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
package de.hybris.platform.customerticketingc4cintegration.converters;

import static org.junit.Assert.assertTrue;

import de.hybris.platform.customerticketingc4cintegration.data.Note;
import de.hybris.platform.customerticketingc4cintegration.data.ODataSingleResponseData;
import de.hybris.platform.customerticketingc4cintegration.data.ODataSingleResultsData;
import de.hybris.platform.customerticketingc4cintegration.data.RelatedTransaction;
import de.hybris.platform.customerticketingc4cintegration.data.ServiceRequestData;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Creation Json from dto. Reverse process tested in ClientMockTest
 * 
 * @see de.hybris.platform.customerticketingc4cintegration.facade.ClientTest
 */
public class JsonConvertersTest
{
	private static final Logger LOGGER = Logger.getLogger(JsonConvertersTest.class);

	private ObjectMapper jacksonObjectMapper;

	@Before
	public void startUp()
	{
		jacksonObjectMapper = new ObjectMapper();
		jacksonObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		jacksonObjectMapper.setPropertyNamingStrategy(new ODataCaseStrategy());
		jacksonObjectMapper = jacksonObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	@Test
	public void testTicketCreate2Json() throws JsonProcessingException
	{
		final String subject = "Some text qwe";
		final String cid = "Some cid";
		final ServiceRequestData data = new ServiceRequestData();
		data.setCustomerID(cid);
		data.setName(subject);
		final Note note = new Note();
		note.setText("");
		note.setTypeCode("10004");
		data.setNotes(Arrays.asList(note));
		final String result = jacksonObjectMapper.writeValueAsString(data);
		LOGGER.info(result);
		assertTrue(result.contains("Notes"));
		assertTrue(result.contains(subject));
		assertTrue(result.contains(cid));
	}

	@Test
	public void testJson2TicketConverterCreateResponse() throws JsonProcessingException
	{
		final String subject = "Some text qwe";
		final String cid = "Some cid";
		final ODataSingleResponseData data = new ODataSingleResponseData();
		data.setD(new ODataSingleResultsData());
		final ServiceRequestData c4CObject = new ServiceRequestData();
		c4CObject.setName(subject);
		c4CObject.setCustomerID(cid);
		data.getD().setResults(c4CObject);
		final String result = jacksonObjectMapper.writeValueAsString(data);
		LOGGER.info(result);
		assertTrue(result.contains("\"d\":{\"results\":{"));
		assertTrue(result.contains(subject));
		assertTrue(result.contains(cid));
	}

	@Test
	public void testUpdateStatus2JsonConverter() throws JsonProcessingException
	{
		final ServiceRequestData data = new ServiceRequestData();
		data.setStatusCode("5");
		final String result = jacksonObjectMapper.writeValueAsString(data);
		LOGGER.info(result);
		assertTrue(result.contains("\"StatusCode\":\"5\""));
	}

	@Test
	public void testUpdateMessage2JsonConverter() throws JsonProcessingException
	{
		final RelatedTransaction data = new RelatedTransaction();
		data.setBusinessSystemID("qweqweqwe");
		data.setTypeCode("100001");
		data.setID("id");
		data.setRoleCode("1");
		final String result = jacksonObjectMapper.writeValueAsString(data);
		LOGGER.info(result);
		assertTrue(result.contains("100001"));
		assertTrue(result.contains("RoleCode"));
		assertTrue(result.contains("TypeCode"));
		assertTrue(result.contains("BusinessSystemID"));
		assertTrue(result.contains("ID"));
		assertTrue(result.contains("100001"));
		assertTrue(result.contains("id"));
	}
}
