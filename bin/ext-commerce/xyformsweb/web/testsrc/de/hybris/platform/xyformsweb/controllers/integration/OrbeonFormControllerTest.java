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
 */
package de.hybris.platform.xyformsweb.controllers.integration;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.xyformsfacades.data.YFormDataData;
import de.hybris.platform.xyformsfacades.data.YFormDefinitionData;
import de.hybris.platform.xyformsfacades.form.YFormFacade;
import de.hybris.platform.xyformsservices.enums.YFormDataTypeEnum;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;
import de.hybris.platform.xyformsweb.controllers.integration.OrbeonFormController;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.DelegatingServletInputStream;


/**
 * Test class for OrbeonFromController
 */
@UnitTest
public class OrbeonFormControllerTest
{
	@InjectMocks
	private final OrbeonFormController controller = Mockito.spy(new OrbeonFormController());

	@Mock
	private HttpServletRequest httpRequest;

	@Mock
	private HttpServletResponse httpResponse;

	@Mock
	private YFormFacade yFormFacade;

	private final String applicationId = "yforms";
	private final String formId = "contact";
	private final String formDataId = "";

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test(expected = ModelNotFoundException.class)
	public void shouldThrowModelNotFoundExceptionWhenYFormDefinitionNotFound() throws ServletException, IOException,
			YFormServiceException
	{
		given(yFormFacade.getYFormDefinition(anyString(), anyString())).willThrow(new ModelNotFoundException(""));
		controller.getFormDefinition(applicationId, formId, null, httpResponse);
	}

	@Test
	public void shouldEmptyBodyIfGetYFormDefinitionReturnedNull() throws ServletException, IOException, YFormServiceException
	{
		final String expect = "";
		given(yFormFacade.getYFormDefinition(anyString(), anyString())).willReturn(null);
		final String result = controller.getFormDefinition(applicationId, formId, null, httpResponse);
		assertEquals(result, expect);
	}

	@Test
	public void shouldReturnEmptyBodyIfBodyReturnedIsNull() throws ServletException, IOException, YFormServiceException
	{
		final String content = null;
		final String expect = "";
		final YFormDefinitionData yFormDefinition = new YFormDefinitionData();
		yFormDefinition.setContent(content);
		given(yFormFacade.getYFormDefinition(anyString(), anyString())).willReturn(yFormDefinition);
		final String result = controller.getFormDefinition(applicationId, formId, null, httpResponse);
		assertEquals(result, expect);
	}

	@Test
	public void shouldReturnYFormDefinitionBody() throws ServletException, IOException, YFormServiceException
	{
		final String expect = "abc";
		final YFormDefinitionData yFormDefinition = new YFormDefinitionData();
		yFormDefinition.setContent(expect);
		given(yFormFacade.getYFormDefinition(anyString(), anyString())).willReturn(yFormDefinition);
		final String result = controller.getFormDefinition(applicationId, formId, null, httpResponse);
		assertEquals(result, expect);
	}

	@Test
	public void shouldResponseWithExpectationFailedWithNullInputStream() throws ServletException, IOException,
			YFormServiceException
	{
		given(httpRequest.getInputStream()).willReturn(null);
		controller.putFormDefinition(applicationId, formId, null, httpRequest, httpResponse);
		verify(httpRequest, times(1)).getInputStream();
		verify(httpResponse, times(1)).sendError(anyInt(), anyString());
	}

	@Test
	public void shouldResponseWithExpectationFailedWithCreateFailed() throws ServletException, IOException, YFormServiceException
	{
		final String newFormDefinition = "";
		final ServletInputStream inputStream = new DelegatingServletInputStream(IOUtils.toInputStream(newFormDefinition, "UTF-8"));
		given(httpRequest.getInputStream()).willReturn(inputStream);

		doThrow(new YFormServiceException("")).when(yFormFacade).createYFormDefinition(anyString(), anyString(), anyString(),
				anyString());

		controller.putFormDefinition(applicationId, formId, null, httpRequest, httpResponse);
		verify(httpRequest, times(1)).getInputStream();
		verify(httpResponse, times(1)).sendError(anyInt(), anyString());
	}

	@Test
	public void shouldResponseWithOkeyWithCreateSuccessed() throws YFormServiceException, IOException, ServletException
	{
		final String newFormDefinition = "";
		final ServletInputStream inputStream = new DelegatingServletInputStream(IOUtils.toInputStream(newFormDefinition, "UTF-8"));
		given(httpRequest.getInputStream()).willReturn(inputStream);

		controller.putFormDefinition(applicationId, formId, null, httpRequest, httpResponse);
		verify(yFormFacade, times(1)).createYFormDefinition(anyString(), anyString(), anyString(), anyString());
		verify(httpRequest, times(1)).getInputStream();
		verify(httpResponse, times(1)).setStatus(HttpServletResponse.SC_OK);
	}

	@Test
	public void shouldResponseEmptyStringIfYFormDataNotExisted() throws YFormServiceException, ServletException, IOException
	{
		final String expected = "";
		given(yFormFacade.getYFormData(anyString(), YFormDataTypeEnum.DATA)).willReturn(null);
		given(yFormFacade.getYFormData(anyString(), YFormDataTypeEnum.DRAFT)).willReturn(null);
		final String response = controller.getFormDataData(applicationId, formId, formDataId, httpResponse);
		assertEquals(expected, response);
	}

	@Test
	public void shouldResponseEmptyStringIfYFormDataBodyEmpty() throws YFormServiceException, ServletException, IOException
	{
		final String expected = "";
		final String content = null;
		final YFormDataData yFormData = new YFormDataData();
		yFormData.setContent(content);
		given(yFormFacade.getYFormData(anyString(), YFormDataTypeEnum.DATA)).willReturn(yFormData);
		given(yFormFacade.getYFormData(anyString(), YFormDataTypeEnum.DRAFT)).willReturn(yFormData);
		final String response = controller.getFormDataData(applicationId, formId, formDataId, httpResponse);
		assertEquals(expected, response);
	}

	@Test
	public void shouldResponseWithExistedYFormDataBody() throws ServletException, YFormServiceException, IOException
	{
		final String content = "abc";
		final YFormDataData yFormData = new YFormDataData();
		yFormData.setContent(content);
		given(yFormFacade.getYFormData(anyString(), YFormDataTypeEnum.DATA)).willReturn(yFormData);
		given(yFormFacade.getYFormData(anyString(), YFormDataTypeEnum.DRAFT)).willReturn(yFormData);
		final String response = controller.getFormDataData(applicationId, formId, formDataId, httpResponse);
		assertEquals(content, response);
	}

	@Test
	public void shouldReturnYFormServiceExceptionWhenFailedCreateOrUpdatedYFormData() throws IOException, YFormServiceException,
			ServletException
	{
		final String newFormDefinition = "";
		final ServletInputStream inputStream = new DelegatingServletInputStream(IOUtils.toInputStream(newFormDefinition, "UTF-8"));
		given(httpRequest.getInputStream()).willReturn(inputStream);

		doThrow(new YFormServiceException("")).when(yFormFacade).createOrUpdateYFormData(anyString(), anyString(), anyString(),
				YFormDataTypeEnum.DATA, anyString());
		controller.putFormData(applicationId, formId, YFormDataTypeEnum.DATA.toString().toLowerCase(), formDataId, httpRequest,
				httpResponse);
		verify(httpResponse, times(1)).sendError(anyInt(), anyString());
	}

	@Test
	public void shouldReturnOkWhenSuccessCreateOrUpdatedYFormData() throws IOException, YFormServiceException, ServletException
	{
		final String newFormDefinition = "";
		final ServletInputStream inputStream = new DelegatingServletInputStream(IOUtils.toInputStream(newFormDefinition, "UTF-8"));
		given(httpRequest.getInputStream()).willReturn(inputStream);

		controller.putFormData(applicationId, formId, YFormDataTypeEnum.DATA.toString().toLowerCase(), formDataId, httpRequest,
				httpResponse);

		verify(controller, times(1)).putFormData(applicationId, formId, YFormDataTypeEnum.DATA.toString().toLowerCase(),
				formDataId, httpRequest, httpResponse);
		verify(yFormFacade, times(1)).createOrUpdateYFormData(applicationId, formId, formDataId, YFormDataTypeEnum.DATA,
				newFormDefinition);

		verify(httpResponse, times(1)).setStatus(HttpServletResponse.SC_OK);
	}
}
