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
package de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.product.PriceService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolverTest;
import de.hybris.platform.util.PriceValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;


@UnitTest
public class ProductPricesValueResolverTest extends AbstractValueResolverTest
{
	protected static final String EUR_CURRENCY_NAME = "Euro";
	protected static final String EUR_CURRENCY_ISOCODE = "EUR";
	protected static final String EUR_CURRENCY_QUALIFIER = "EUR";
	protected static final String USD_CURRENCY_NAME = "US Dollar";
	protected static final String USD_CURRENCY_ISOCODE = "USD";
	protected static final String USD_CURRENCY_QUALIFIER = "USD";

	protected static final String INDEXED_PROPERTY_1_NAME = "price";
	protected static final String INDEXED_PROPERTY_2_NAME = "priceValue";

	@Mock
	private PriceService priceService;

	@Mock
	private ProductModel product;

	@Mock
	private CurrencyModel eurCurrency;

	@Mock
	private CurrencyModel usdCurrency;

	@Mock
	private Qualifier eurQualifier;

	@Mock
	private Qualifier usdQualifier;

	@Mock
	private PriceInformation priceInformation;

	private IndexedProperty indexedProperty1;
	private IndexedProperty indexedProperty2;

	private ProductPricesValueResolver valueResolver;

	@Before
	public void setUp()
	{
		indexedProperty1 = new IndexedProperty();
		indexedProperty1.setName(INDEXED_PROPERTY_1_NAME);
		indexedProperty1.setValueProviderParameters(new HashMap<String, String>());

		indexedProperty2 = new IndexedProperty();
		indexedProperty2.setName(INDEXED_PROPERTY_2_NAME);
		indexedProperty2.setValueProviderParameters(new HashMap<String, String>());

		when(eurCurrency.getName()).thenReturn(EUR_CURRENCY_NAME);
		when(eurCurrency.getIsocode()).thenReturn(EUR_CURRENCY_ISOCODE);
		when(usdCurrency.getName()).thenReturn(USD_CURRENCY_NAME);
		when(usdCurrency.getIsocode()).thenReturn(USD_CURRENCY_ISOCODE);

		when(eurQualifier.toFieldQualifier()).thenReturn(EUR_CURRENCY_QUALIFIER);
		when(usdQualifier.toFieldQualifier()).thenReturn(USD_CURRENCY_QUALIFIER);

		when(Boolean.valueOf(getQualifierProvider().canApply(any(IndexedProperty.class)))).thenReturn(Boolean.TRUE);

		valueResolver = new ProductPricesValueResolver();
		valueResolver.setSessionService(getSessionService());
		valueResolver.setQualifierProvider(getQualifierProvider());
		valueResolver.setPriceService(priceService);
	}

	@Test
	public void resolveProductWithNoPrice() throws Exception
	{
		// given
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty1);
		final List<PriceInformation> priceInformations = Collections.emptyList();

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(eurQualifier));
		when(priceService.getPriceInformationsForProduct(product)).thenReturn(priceInformations);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument(), Mockito.never()).addField(any(IndexedProperty.class), any());
		verify(getInputDocument(), Mockito.never()).addField(any(IndexedProperty.class), any(), any(String.class));
	}

	@Test
	public void resolveProductWithPrice() throws Exception
	{
		// given
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty1);
		final PriceValue priceValue = new PriceValue(eurCurrency.getIsocode(), 120, false);

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(eurQualifier));
		when(priceService.getPriceInformationsForProduct(product)).thenReturn(Collections.singletonList(priceInformation));
		when(priceInformation.getPriceValue()).thenReturn(priceValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument()).addField(indexedProperty1, Double.valueOf(priceValue.getValue()), eurCurrency.getIsocode());
	}

	@Test
	public void resolveWithMultipleCurrencies() throws Exception
	{
		// given
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty1);
		final PriceValue eurPriceValue = new PriceValue(eurCurrency.getIsocode(), 120, false);
		final PriceValue usdPriceValue = new PriceValue(usdCurrency.getIsocode(), 150, false);

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(eurQualifier, usdQualifier));
		when(priceService.getPriceInformationsForProduct(product)).thenReturn(Collections.singletonList(priceInformation));
		when(priceInformation.getPriceValue()).thenReturn(eurPriceValue).thenReturn(usdPriceValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument()).addField(indexedProperty1, Double.valueOf(eurPriceValue.getValue()), eurCurrency.getIsocode());
		verify(getInputDocument()).addField(indexedProperty1, Double.valueOf(usdPriceValue.getValue()), usdCurrency.getIsocode());
	}

	@Test
	public void resolveMultipleIndexedProperties() throws Exception
	{
		// given
		final Collection<IndexedProperty> indexedProperties = Arrays.asList(indexedProperty1, indexedProperty2);
		final PriceValue priceValue = new PriceValue(eurCurrency.getIsocode(), 120, false);

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(eurQualifier));
		when(priceService.getPriceInformationsForProduct(product)).thenReturn(Collections.singletonList(priceInformation));
		when(priceInformation.getPriceValue()).thenReturn(priceValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument()).addField(indexedProperty1, Double.valueOf(priceValue.getValue()), eurCurrency.getIsocode());
	}

	@Test
	public void resolveNonOptionalWithPrice() throws Exception
	{
		// given
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty1);
		final PriceValue priceValue = new PriceValue(eurCurrency.getIsocode(), 120, false);

		indexedProperty1.getValueProviderParameters().put(ProductPricesValueResolver.OPTIONAL_PARAM, Boolean.FALSE.toString());

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(eurQualifier));
		when(priceService.getPriceInformationsForProduct(product)).thenReturn(Collections.singletonList(priceInformation));
		when(priceInformation.getPriceValue()).thenReturn(priceValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument()).addField(indexedProperty1, Double.valueOf(priceValue.getValue()), eurCurrency.getIsocode());
	}

	@Test
	public void resolveNonOptionalWithNoPrice() throws Exception
	{
		// given
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty1);
		final List<PriceInformation> priceInformations = Collections.emptyList();

		indexedProperty1.getValueProviderParameters().put(ProductPricesValueResolver.OPTIONAL_PARAM, Boolean.FALSE.toString());

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(eurQualifier));
		when(priceService.getPriceInformationsForProduct(product)).thenReturn(priceInformations);

		// expect
		expectedException.expect(FieldValueProviderException.class);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);
	}
}
