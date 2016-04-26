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

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.util.CSVConstants;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Stack;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;


@UnitTest
@SuppressWarnings("javadoc")
public class DataHubDataFragmentUnitTest extends AbstractScriptFragmentTest<DataHubDataFragment>
{
	public static final String VALID_IMPEX_HEADER = "INSERT Category;code[unique=true];$catalogVersion";
	private static final String VALID_URL = "https://somehost/rest/123/Catalog";
	private static final String INVALID_URL = "https://somehost/rest";
	private static final String DATA = "1;Spring Catalog;";
	private DataHubFacade dataHub;

	@Before
	public void setUp() throws Exception
	{
		dataHub = setUpDataHub();
		fragment = spy(new DataHubDataFragment(dataHub));
		doNothing().when(fragment).validateImpexHeader(any(String.class), any(String.class));
	}

	@SuppressWarnings("unchecked")
	private DataHubFacade setUpDataHub()
	{
		final InputStream input = new ByteArrayInputStream(DATA.getBytes());
		final DataHubFacade facade = Mockito.mock(DataHubFacade.class);
		Mockito.doReturn(input).when(facade).readData(eq(VALID_URL), anyMap());
		return facade;
	}

	@Test
	public void testTheFacadeImplementationCanBeReadBack()
	{
		Assert.assertSame(dataHub, fragment.getDataHubFacade());
	}

	@Test
	public void testScriptFragmentIsEmptyBeforeAnyLineWasAdded() throws IOException
	{
		Assert.assertEquals("", fragment.getContent());
	}

	@Test
	public void testCommentCannotBeAdded() throws IOException
	{
		testLineThatShouldNotBeAdded("# das ist ein kommentar");
	}

	@Test
	public void testMacroCannotBeAdded() throws IOException
	{
		testLineThatShouldNotBeAdded("$catalogVersion=catalogversion(catalog(id[default=apparelProductCatalog]),version[default='Staged'])");
	}

	@Test
	public void testEmptyLineCanBeAdded() throws IOException
	{
		testLineThatShouldNotBeAdded("");
	}

	@Test
	public void testNullCannotBeAdded() throws IOException
	{
		final boolean wasAdded = fragment.addLine(null, new Stack<ImpExFragment>());

		assertLineWasNotAdded(wasAdded, null);
	}

	@Test
	public void testSomeTextCannotBeAdded() throws IOException
	{
		testLineThatShouldNotBeAdded("[unique=true,default=apparelProductCatalog:Staged]");
	}

	@Test
	public void testINSERT_UPDATEHeaderCanBeAdded() throws IOException
	{
		testLineThatShouldBeAdded("INSERT_UPDATE Category;code[unique=true];$catalogVersion");
	}

	@Test
	public void testREMOVEHeaderCanBeAdded() throws IOException
	{
		testLineThatShouldBeAdded("REMOVE Category;code[unique=true];$catalogVersion");
	}

	@Test
	public void testINSERTHeaderCanBeAdded() throws IOException
	{
		testLineThatShouldBeAdded(VALID_IMPEX_HEADER);
	}

	@Test
	public void testUrlIsEmptyBeforeTheUrlCommentWasAdded()
	{
		Assert.assertEquals("", fragment.getUrl());
	}

	@Test
	public void testURLCommentCanBeAdded()
	{
		final Stack<ImpExFragment> fragments = new Stack<>();
		fragment.addLine(VALID_IMPEX_HEADER, fragments);
		final boolean wasAdded = fragment.addLine("#$URL: https://somehost/rest/123/Catalog?fields=parent,catalog,version", fragments);

		Assert.assertTrue(wasAdded);
		Assert.assertEquals("https://somehost/rest/123/Catalog?fields=parent,catalog,version", fragment.getUrl());
	}

	@Test
	public void testHeadersAreEmptyBeforeAnyHeaderIsAdded()
	{
		final Map<String, String> headers = fragment.getHeaders();

		Assert.assertNotNull(headers);
		Assert.assertTrue(headers.isEmpty());
	}

	@Test
	public void testHEADERCommentCanBeAdded() throws IOException
	{
		fragment.addLine(VALID_IMPEX_HEADER, null);
		final boolean wasAdded = fragment.addLine("#$HEADER: x-TenantId=master", new Stack<ImpExFragment>());

		Assert.assertTrue(wasAdded);
		Assert.assertEquals("master", fragment.getHeader("x-TenantId"));
	}

	@Test
	public void testIgnoresUnparsibleHeader()
	{
		final boolean added = fragment.addLine("#$HEADER: x-Tenant: master", new Stack<ImpExFragment>());

		assertFalse(added);
		Assert.assertTrue(fragment.getHeaders().isEmpty());
	}

	@Test
	public void testContentIsDataFromTheIntegrationLayerInsteadOfAddedLines() throws IOException
	{
		final Stack<ImpExFragment> fragments = new Stack<>();
		fragment.addLine(VALID_IMPEX_HEADER, fragments);
		fragment.addLine("#$URL: " + VALID_URL, fragments);
		fragment.addLine("#$HEADER: x-TenantId=master", fragments);

		final String content = fragment.getContent();

		Assert.assertEquals(VALID_IMPEX_HEADER + CSVConstants.HYBRIS_LINE_SEPARATOR + DATA, content);
	}

	@SuppressWarnings(
			{"rawtypes", "unchecked"})
	@Test
	public void testPassesUrlAndHeadersToTheIntegrationLayer() throws IOException
	{
		final ArgumentCaptor<String> url = ArgumentCaptor.forClass(String.class);
		final ArgumentCaptor<Map> headers = ArgumentCaptor.forClass(Map.class);
		fragment.addLine(VALID_IMPEX_HEADER);
		fragment.addLine("#$URL: " + VALID_URL);
		fragment.addLine("#$HEADER: x-TenantId=master");

		fragment.getContent();

		Mockito.verify(dataHub).readData(url.capture(), headers.capture());
		Assert.assertEquals(VALID_URL, url.getValue());
		Assert.assertEquals("master", headers.getValue().get("x-TenantId"));
	}


	@Test
	public void testDataFromTheIntegrationLayerCanBeRetrievedAsStream() throws IOException
	{
		fragment.addLine(VALID_IMPEX_HEADER);
		fragment.addLine("#$URL: " + VALID_URL);
		fragment.addLine("#$HEADER: x-TenantId=master");

		final String content = readContentFromTheInputStream();

		Assert.assertEquals(VALID_IMPEX_HEADER + CSVConstants.HYBRIS_LINE_SEPARATOR + DATA, content);
	}

	@Test(expected = IOException.class)
	public void testOtherExceptionsAreConvertedToIOExceptionWhenInputStreamIsRetreived() throws IOException
	{
		throwExceptionOnReadingFromRemoteResource(new IllegalStateException(), INVALID_URL);

		fragment.addLine(VALID_IMPEX_HEADER);
		fragment.addLine("#$URL: " + INVALID_URL);
		fragment.addLine("#$HEADER: x-TenantId=master");

		readContentFromTheInputStream();
	}

	@Test
	public void testHeaderCanBeAdded() throws IOException
	{
		testLineThatShouldBeAdded("INSERT_UPDATE Category;code[unique=true];$catalogVersion");
	}

	@Test
	public void testValidMacroAndHeader() throws IOException
	{
		final ImpExFragment macroFragment = new ImpexMacroFragment();
		macroFragment.addLine("$catalogVersion=catalogversion(catalog(id[default=$productCatalog]),version[default='Staged'])[unique=true,default=$productCatalog:Staged]" + CSVConstants.HYBRIS_LINE_SEPARATOR);
		final String impexHeader = "INSERT_UPDATE Catalog;id[unique=true]";
		final Stack<ImpExFragment> fragments = new Stack<>();
		fragments.push(macroFragment);

		final boolean added = fragment.addLine(impexHeader, fragments);

		assertLineIsInTheFragment(added, impexHeader);
	}

	@Test(expected = ImpexValidationException.class)
	public void testValidMacroAndInvalidHeader() throws Exception
	{
		final ImpExFragment macroFragment = new ImpexMacroFragment();
		macroFragment.addLine("$catalogVersion=catalogversion(catalog(id[default=$productCatalog]),version[default='Staged'])[unique=true,default=$productCatalog:Staged]" + CSVConstants.HYBRIS_LINE_SEPARATOR);
		final String invalidImpexHeader = "INSERT_UPDATE jlkfsdf";
		final Stack<ImpExFragment> fragments = new Stack<>();
		fragments.push(macroFragment);

		doThrow(new ImpExException("Invalid Impex Header")).when(fragment).validateImpexHeader(invalidImpexHeader + CSVConstants.HYBRIS_LINE_SEPARATOR, fragments.get(0).getContent());

		fragment.addLine(invalidImpexHeader);
		fragment.addLine("#########", fragments);
	}

	@Test(expected = ImpexValidationException.class)
	public void testInvalidHeaderAndCannotReadBody() throws Exception
	{
		final ImpExFragment macroFragment = new ImpexMacroFragment();
		macroFragment.addLine("$catalogVersion=catalogversion(catalog(id[default=$productCatalog]),version[default='Staged'])[unique=true,default=$productCatalog:Staged]" + CSVConstants.HYBRIS_LINE_SEPARATOR);
		final String invalidImpexHeader = "INSERT_UPDATE jlkfsdf";
		final Stack<ImpExFragment> fragments = new Stack<>();
		fragments.push(macroFragment);

		doThrow(new ImpExException("Invalid Impex Header")).when(fragment).validateImpexHeader(invalidImpexHeader + CSVConstants.HYBRIS_LINE_SEPARATOR, fragments.get(0).getContent());
		doThrow(new IOException("Invalid Impex Header")).when(fragment).getImpexBody();

		fragment.addLine(invalidImpexHeader);
		fragment.addLine("#########", fragments);
	}

	@Test
	public void testHeaderCannotBeAddedTwice()
	{
		fragment.addLine("INSERT product");
		final boolean secondHeaderWasAdded = fragment.addLine("INSERT_UPDATE product");
		assertFalse(secondHeaderWasAdded);
	}

	@SuppressWarnings("unchecked")
	private void throwExceptionOnReadingFromRemoteResource(final IllegalStateException ex, final String url)
	{
		Mockito.doThrow(ex).when(dataHub).readData(eq(url), anyMap());
	}
}
