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

package com.hybris.datahub.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.impex.jalo.ImpExException;

import com.hybris.datahub.core.dto.ItemImportTaskData;
import com.hybris.datahub.core.facades.ImportError;
import com.hybris.datahub.core.rest.client.ImpexDataImportClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@SuppressWarnings("javadoc")
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultFragmentReaderUnitTest
{
	private static String IMPEX_MACROS = "$baseProduct=baseProduct(code, catalogVersion(catalog(id[default='apparelProductCatalog']),version[default='Staged']))\n" +
			"$catalogVersion=catalogversion(catalog(id[default=apparelProductCatalog]),version[default='Staged'])[unique=true,default=apparelProductCatalog:Staged]\n";
	private static String IMPEX_HEADER = "INSERT_UPDATE Product;code[unique=true]; name[lang=en]; Unit(code); $catalogVersion[unique=true,allowNull=true];description[lang=en];approvalStatus(code);ean;manufacturerName\n";
	private static String CALLBACK_URL = "#$URL: https://integration.layer.host/rest/123/Product/en?fields=code,name,unit,catalogVersion,description,approvalStatus\n";
	private static String CALLBACK_HEADERS = "#$HEADER: x-TenantId=master\n#$HEADER: someOtherHeader=boo\n";
	private static String SEPERATOR_LINE = "###########";

	@Spy
	private DefaultFragmentReader reader = new DefaultFragmentReader();

	@Captor
	private ArgumentCaptor<String> impexLineCaptor;

	private DataHubDataFragment dataHubDataFragmentSpy;
	private ImpExFragment impexMacroFragmentSpy;

	@Before
	public void setUp() throws Exception
	{
		final DataHubDataFragment dataHubDataFragment = new DataHubDataFragment(reader.getFacade());
		dataHubDataFragmentSpy = spy(dataHubDataFragment);
		final ImpexMacroFragment impexMacroFragment = new ImpexMacroFragment();
		impexMacroFragmentSpy = spy(impexMacroFragment);
		doNothing().when(dataHubDataFragmentSpy).validateImpexHeader(any(String.class), any(String.class));
		doReturn(new ImpExFragment[]{impexMacroFragmentSpy, dataHubDataFragmentSpy, new ConstantTextFragment()}).when(reader).getFragmentsToTry();
	}

	@Test
	public void testSplitsImpexScriptIntoLogicalBlocks() throws ImpExException
	{
		final List<ImpExFragment> blocks = reader.readScriptFragments(scriptCtx());

		assertEquals(3, blocks.size());
	}

	@Test
	public void testPreservesTheOrderOfTheFragmentsAsInTheOriginalScript() throws ImpExException
	{
		final ImpExFragment[] blocks = reader.readScriptFragments(scriptCtx()).toArray(new ImpExFragment[3]);

		Assert.assertTrue(blocks[0] instanceof ImpexMacroFragment);
		Assert.assertTrue(blocks[1] instanceof DataHubDataFragment);
	}

	@Test
	public void testInjectsDefaultFacadeIntoTheDataFragmentWhenNoFacadeWasSet() throws ImpExException
	{
		final DataHubDataFragment dataFrag = extractIntegrationLayerDataFragment(scriptCtx());

		Assert.assertNotNull(dataFrag.getDataHubFacade());
		Assert.assertTrue(dataFrag.getDataHubFacade() instanceof ImpexDataImportClient);
	}

	public void testImportErrorIsCreatedWhenMacroFragmentCannotBeRed() throws Exception
	{
		doThrow(new IOException()).when(impexMacroFragmentSpy).getContent();
		final ItemImportTaskData itemImportTaskData = scriptCtx();
		reader.readScriptFragments(itemImportTaskData);
		assertFalse(itemImportTaskData.getHeaderErrors().isEmpty());
	}

	@Test
	public void testValidationErrorIsAddedToContext() throws Exception
	{
		final Stack<ImpExFragment> fragments = new Stack<>();
		fragments.push(impexMacroFragmentSpy);
		fragments.push(dataHubDataFragmentSpy);
		doThrow(new ImpexValidationException(Arrays.asList(new ImportError()))).when(dataHubDataFragmentSpy).addLine(SEPERATOR_LINE, fragments);
		final ItemImportTaskData itemImportTaskData = scriptCtx();
		reader.readScriptFragments(itemImportTaskData);
		assertFalse(itemImportTaskData.getHeaderErrors().isEmpty());
	}

	@Test
	public void testDataHubDataFragmentIsNotIncludedInFragmentsListWhenItContainsAnInvalidHeader() throws Exception
	{
		final Stack<ImpExFragment> fragments = new Stack<>();
		fragments.push(impexMacroFragmentSpy);
		fragments.push(dataHubDataFragmentSpy);
		doThrow(new ImpexValidationException(Arrays.asList(new ImportError()))).when(dataHubDataFragmentSpy).addLine(SEPERATOR_LINE, fragments);
		final List<ImpExFragment> validFragments = reader.readScriptFragments(scriptCtx());
		assertFalse(validFragments.contains(dataHubDataFragmentSpy));
	}

	private DataHubDataFragment extractIntegrationLayerDataFragment(final ItemImportTaskData scriptCtx) throws ImpExException
	{
		final List<ImpExFragment> blocks = reader.readScriptFragments(scriptCtx);
		final DataHubDataFragment dataFrag = (DataHubDataFragment) blocks.get(1);
		return dataFrag;
	}

	private ItemImportTaskData scriptCtx()
	{
		final ItemImportTaskData ctx = new ItemImportTaskData();
		final StringBuilder script = new StringBuilder()
				.append(IMPEX_MACROS)
				.append("\n")
				.append(IMPEX_HEADER)
				.append(CALLBACK_URL)
				.append(CALLBACK_HEADERS)
				.append(SEPERATOR_LINE);

		ctx.setImpexMetaData(script.toString().getBytes());
		return ctx;
	}
}
