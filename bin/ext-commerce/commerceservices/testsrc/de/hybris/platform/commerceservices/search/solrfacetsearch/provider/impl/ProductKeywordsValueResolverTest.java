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
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractLocalizedValueResolverTest;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;


@UnitTest
public class ProductKeywordsValueResolverTest extends AbstractLocalizedValueResolverTest
{
	protected static final String KEYWORD_1 = "keyword1";
	protected static final String KEYWORD_2 = "keyword2";

	@Mock
	private ProductModel product;

	@Mock
	private VariantProductModel variantProduct;

	@Mock
	private KeywordModel keyword1;

	@Mock
	private KeywordModel keyword2;

	private ProductKeywordsValueResolver valueResolver;

	@Before
	public void setUp()
	{
		when(keyword1.getKeyword()).thenReturn(KEYWORD_1);
		when(keyword2.getKeyword()).thenReturn(KEYWORD_2);

		when(variantProduct.getBaseProduct()).thenReturn(product);

		valueResolver = new ProductKeywordsValueResolver();
		valueResolver.setSessionService(getSessionService());
		valueResolver.setQualifierProvider(getQualifierProvider());
	}

	@Test
	public void resolveNoKeyword() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);

		when(product.getKeywords()).thenReturn(Collections.<KeywordModel> emptyList());

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument(), Mockito.never()).addField(any(IndexedProperty.class), any());
		verify(getInputDocument(), Mockito.never()).addField(any(IndexedProperty.class), any(), any(String.class));
	}

	@Test
	public void resolveKeyword() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final List<KeywordModel> keywords = Collections.singletonList(keyword1);

		when(product.getKeywords()).thenReturn(keywords);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument()).addField(indexedProperty, keyword1.getKeyword(), null);
	}

	@Test
	public void resolveLocalizedKeyword() throws Exception
	{
		// given
		final IndexedProperty localizedIndexedProperty = getLocalizedIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(localizedIndexedProperty);
		final List<KeywordModel> keywords = Collections.singletonList(keyword1);

		when(product.getKeywords()).thenReturn(keywords);

		// when
		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument()).addField(localizedIndexedProperty, keyword1.getKeyword(), EN_LANGUAGE_QUALIFIER);
	}

	@Test
	public void resolveNoKeywordNonOptional() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);

		indexedProperty.getValueProviderParameters().put(ProductKeywordsValueResolver.OPTIONAL_PARAM, Boolean.FALSE.toString());

		when(product.getKeywords()).thenReturn(Collections.<KeywordModel> emptyList());

		// expect
		expectedException.expect(FieldValueProviderException.class);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);
	}

	@Test
	public void resolveKeywordsSplit() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final List<KeywordModel> keywords = Arrays.asList(keyword1, keyword2);

		when(product.getKeywords()).thenReturn(keywords);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument()).addField(indexedProperty, keyword1.getKeyword(), null);
		verify(getInputDocument()).addField(indexedProperty, keyword2.getKeyword(), null);
	}

	@Test
	public void resolveKeywordsNoSplit() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final List<KeywordModel> keywords = Arrays.asList(keyword1, keyword2);
		final String separator = ProductKeywordsValueResolver.SEPARATOR_PARAM_DEFAULT_VALUE;
		final String value = keyword1.getKeyword() + separator + keyword2.getKeyword();

		indexedProperty.getValueProviderParameters().put(ProductKeywordsValueResolver.SPLIT_PARAM, Boolean.FALSE.toString());

		when(product.getKeywords()).thenReturn(keywords);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument()).addField(indexedProperty, value, null);
	}

	@Test
	public void resolveKeywordsNoSplitWithCustomSeparator() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final List<KeywordModel> keywords = Arrays.asList(keyword1, keyword2);
		final String separator = "...";
		final String value = keyword1.getKeyword() + separator + keyword2.getKeyword();

		indexedProperty.getValueProviderParameters().put(ProductKeywordsValueResolver.SPLIT_PARAM, Boolean.FALSE.toString());
		indexedProperty.getValueProviderParameters().put(ProductKeywordsValueResolver.SEPARATOR_PARAM, separator);

		when(product.getKeywords()).thenReturn(keywords);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument()).addField(indexedProperty, value, null);
	}

	@Test
	public void resolveKeywordsForVariantProduct() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);

		when(variantProduct.getKeywords()).thenReturn(Collections.singletonList(keyword1));
		when(product.getKeywords()).thenReturn(Collections.singletonList(keyword2));

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, variantProduct);

		// then
		verify(getInputDocument()).addField(indexedProperty, keyword1.getKeyword(), null);
		verify(getInputDocument()).addField(indexedProperty, keyword2.getKeyword(), null);
	}
}
