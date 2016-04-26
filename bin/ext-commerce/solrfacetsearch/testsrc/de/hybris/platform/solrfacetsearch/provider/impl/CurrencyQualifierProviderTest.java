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
package de.hybris.platform.solrfacetsearch.provider.impl;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;
import de.hybris.platform.solrfacetsearch.provider.impl.CurrencyQualifierProvider.CurrencyQualifier;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class CurrencyQualifierProviderTest
{
	protected static final String CURRENCY_ISOCODE = "eur";

	@Rule
	public ExpectedException expectedException = ExpectedException.none(); //NOPMD

	@Mock
	private CommonI18NService commonI18NService;

	@Mock
	private CurrencyModel currency;

	private Qualifier qualifier;

	private CurrencyQualifierProvider qualifierProvider;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		qualifier = new CurrencyQualifier(currency);

		qualifierProvider = new CurrencyQualifierProvider();
		qualifierProvider.setCommonI18NService(commonI18NService);
	}

	@Test
	public void cannotApplyWithNonCurrencyProperty() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = new IndexedProperty();
		indexedProperty.setCurrency(false);

		// when
		final boolean canApply = qualifierProvider.canApply(indexedProperty);

		// then
		assertFalse(canApply);
	}

	@Test
	public void canApplyWithCurrencyProperty() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = new IndexedProperty();
		indexedProperty.setCurrency(true);

		// when
		final boolean canApply = qualifierProvider.canApply(indexedProperty);

		// then
		assertTrue(canApply);
	}

	@Test
	public void getEmptyAvailableQualifiers() throws Exception
	{
		// given
		final FacetSearchConfig facetSearchConfig = new FacetSearchConfig();
		final IndexConfig indexConfig = new IndexConfig();
		final IndexedType indexedType = new IndexedType();

		facetSearchConfig.setIndexConfig(indexConfig);

		indexConfig.setCurrencies(Collections.<CurrencyModel> emptyList());

		// when
		final Collection<Qualifier> qualifiers = qualifierProvider.getAvailableQualifiers(facetSearchConfig, indexedType);

		// then
		assertEquals(0, qualifiers.size());
	}

	@Test
	public void getAvailableQualifierForCurrency() throws Exception
	{
		// given
		final FacetSearchConfig facetSearchConfig = new FacetSearchConfig();
		final IndexConfig indexConfig = new IndexConfig();
		final IndexedType indexedType = new IndexedType();

		facetSearchConfig.setIndexConfig(indexConfig);

		indexConfig.setCurrencies(Arrays.asList(currency));

		// when
		final Collection<Qualifier> qualifiers = qualifierProvider.getAvailableQualifiers(facetSearchConfig, indexedType);

		// then
		assertEquals(1, qualifiers.size());
		assertThat(qualifiers, hasItem(qualifier));
	}

	@Test
	public void applyNonValidQualifier() throws Exception
	{
		// given
		qualifier = mock(Qualifier.class);

		// expect
		expectedException.expect(IllegalArgumentException.class);

		// when
		qualifierProvider.applyQualifier(qualifier);
	}

	@Test
	public void applyQualifier() throws Exception
	{
		// when
		qualifierProvider.applyQualifier(qualifier);

		// then
		verify(commonI18NService).setCurrentCurrency(currency);
	}

	@Test
	public void getNullCurrentQualifier() throws Exception
	{
		when(commonI18NService.getCurrentCurrency()).thenReturn(null);

		// when
		final Qualifier currentQualifier = qualifierProvider.getCurrentQualifier();

		// then
		assertNull(currentQualifier);
	}

	@Test
	public void getCurrentQualifierForLanguage() throws Exception
	{
		// given
		when(commonI18NService.getCurrentCurrency()).thenReturn(currency);

		// when
		final Qualifier currentQualifier = qualifierProvider.getCurrentQualifier();

		// then
		assertEquals(qualifier, currentQualifier);
	}

	@Test
	public void getValueForObject() throws Exception
	{
		// expect
		expectedException.expect(IllegalArgumentException.class);

		// when
		qualifier.getValueForType(Object.class);
	}

	@Test
	public void getValueForCurrencyModel() throws Exception
	{
		// when
		final CurrencyModel value = qualifier.getValueForType(CurrencyModel.class);

		// then
		assertEquals(currency, value);
	}

	@Test
	public void toFieldQualifier() throws Exception
	{
		// given
		when(currency.getIsocode()).thenReturn(CURRENCY_ISOCODE);

		// when
		final String fieldQualifier = qualifier.toFieldQualifier();

		// then
		assertEquals(CURRENCY_ISOCODE, fieldQualifier);
	}
}
