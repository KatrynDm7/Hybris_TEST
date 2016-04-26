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
 */

package com.hybris.datahub.core.facades.impl;

import static org.mockito.Matchers.any;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportConfig.ValidationMode;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;

import com.hybris.datahub.core.dto.ItemImportTaskData;
import com.hybris.datahub.core.facades.ImportError;
import com.hybris.datahub.core.facades.ItemImportResult;
import com.hybris.datahub.core.services.ImpExResourceFactory;
import com.hybris.datahub.core.services.impl.DataHubFacade;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


/**
 * A unit test for <code>DefaultItemImportFacade</code>
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("javadoc")
public class DefaultItemImportFacadeUnitTest
{
	private static final ItemImportTaskData taskData = createTaskData();
	@InjectMocks
	private final DefaultItemImportFacade facade = new DefaultItemImportFacade();
	private ItemImportResult itemImportResult;

	@Mock
	private ImportService importService;
	@Mock
	private ImpExResourceFactory resourceFactory;
	@Mock
	private ImportResultConverter resultConverter;
	@Mock
	private EventService eventService;
	@Mock
	private DataHubFacade dataHubFacade;

	private static ItemImportTaskData createTaskData()
	{
		final ItemImportTaskData data = new ItemImportTaskData();
		data.setImpexMetaData("INSERT_UPDATE SomeProduct".getBytes());
		data.setPoolName("Test pool");
		data.setPublicationId(1L);
		data.setResultCallbackUrl("http://localhost");
		return data;
	}

	@Before
	public void setUp() throws ImpExException
	{
		setUpResourceFactory();
		final ImportResult importResult = setUpImportService();
		setUpImportResultConverter(importResult);
	}

	private void setUpResourceFactory() throws ImpExException
	{
		final ImpExResource resource = Mockito.mock(ImpExResource.class);
		Mockito.doReturn(resource).when(resourceFactory).createResource(taskData);
	}

	private ImportResult setUpImportService()
	{
		final ImportResult res = Mockito.mock(ImportResult.class);
		Mockito.doReturn(Boolean.TRUE).when(res).isSuccessful();
		Mockito.doReturn(res).when(importService).importData(any(ImportConfig.class));
		return res;
	}

	private void setUpImportResultConverter(final ImportResult importRes)
	{
		itemImportResult = Mockito.mock(ItemImportResult.class);
		Mockito.doReturn(true).when(itemImportResult).isSuccessful();
		Mockito.doReturn(itemImportResult).when(resultConverter).convert(importRes);
	}

	@Test
	public void testCorrectlyCreatesImportConfigBeforeCallingTheImportService() throws Exception
	{
		final ArgumentCaptor<ImportConfig> capturedConfig = ArgumentCaptor.forClass(ImportConfig.class);

		facade.importItems(taskData);

		Mockito.verify(importService).importData(capturedConfig.capture());
		assertCreatedImportConfig(capturedConfig.getValue());
	}

	private void assertCreatedImportConfig(final ImportConfig cfg)
	{
		Assert.assertNotNull("Config not created", cfg);
		Assert.assertNotNull("Script not set", cfg.getScript());
		Assert.assertEquals("Strict validation not set", ValidationMode.STRICT, cfg.getValidationMode());
		Assert.assertTrue("Synchronous processing not set", cfg.isSynchronous());
		Assert.assertFalse("Legacy mode", cfg.isLegacyMode());
	}

	@Test
	public void testImportResultIsReturnedSuccessfully() throws Exception
	{
		facade.importItems(taskData);

		Mockito.verify(dataHubFacade).returnImportResult(taskData.getResultCallbackUrl(), itemImportResult);
	}

	@Test
	public void testImportResultWithHeaderErrors() throws Exception
	{
		itemImportResult = new ItemImportResult();
		Mockito.when(resultConverter.convert(setUpImportService())).thenReturn(itemImportResult);
		taskData.setHeaderErrors(Lists.newArrayList(new ImportError()));
		final ItemImportResult result = facade.importItems(taskData);
		Assert.assertEquals(1, result.getErrors().size());
	}

	@Test
	public void testImportResultWithOutHeaderErrors() throws Exception
	{
		itemImportResult = new ItemImportResult();
		Mockito.when(resultConverter.convert(setUpImportService())).thenReturn(itemImportResult);
		final ItemImportResult result = facade.importItems(taskData);
		Assert.assertEquals(1, result.getErrors().size());
	}

	@Test
	public void testImportResultIsSuccessfulWhenImportCompletesSuccessfully() throws Exception
	{
		final ItemImportResult res = facade.importItems(taskData);

		Assert.assertNotNull("Result not returned", res);
		Assert.assertTrue("Result not successful", res.isSuccessful());
	}

	@Test
	public void testImportResultIsErrorWhenErrorIsReportedFromTheImportService() throws Exception
	{
		simulateErrorResultFromTheImportService();

		final ItemImportResult res = facade.importItems(taskData);

		Assert.assertFalse(res.isSuccessful());
	}

	@Test
	public void testImportResultAndHeaderErrorsCombined() throws Exception
	{
		itemImportResult = new ItemImportResult();
		itemImportResult.addErrors(Lists.newArrayList(new ImportError()));
		Mockito.when(resultConverter.convert(setUpImportService())).thenReturn(itemImportResult);
		taskData.setHeaderErrors(Lists.newArrayList(new ImportError()));
		final ItemImportResult result = facade.importItems(taskData);
		Assert.assertEquals(2, result.getErrors().size());
		Assert.assertFalse(result.isSuccessful());
	}

	private void simulateErrorResultFromTheImportService()
	{
		Mockito.doReturn(Boolean.FALSE).when(itemImportResult).isSuccessful();
	}

	@Test
	public void testImportResultIsErrorWhenImportServiceCrashes() throws Exception
	{
		simulateExceptionOnImport();

		final ItemImportResult res = facade.importItems(taskData);

		Assert.assertFalse(res.isSuccessful());
	}

	private void simulateExceptionOnImport()
	{
		Mockito.doThrow(new RuntimeException()).when(importService).importData(any(ImportConfig.class));
	}

	@Test
	public void testImportResultIsErrorWhenImpExScriptIsInvalid() throws Exception
	{
		simulateExceptionOnReadingImpExScript();

		final ItemImportResult res = facade.importItems(taskData);

		Assert.assertFalse(res.isSuccessful());
	}

	private void simulateExceptionOnReadingImpExScript() throws ImpExException
	{
		Mockito.doThrow(new ImpExException("Invalid script")).when(resourceFactory).createResource(taskData);
	}

	@Test
	public void testReturnImportResultSuccess() throws Exception
	{
		facade.importItems(taskData);

		Mockito.verify(dataHubFacade).returnImportResult(taskData.getResultCallbackUrl(), itemImportResult);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testReturnImportResultFailure() throws Exception
	{
		Mockito.doThrow(new IllegalArgumentException()).when(dataHubFacade)
				.returnImportResult(taskData.getResultCallbackUrl(), itemImportResult);
		facade.importItems(taskData);
	}
}
