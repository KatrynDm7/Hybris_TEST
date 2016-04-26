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

import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.product.VariantsService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.provider.ValueFilter;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractLocalizedValueResolverTest;
import de.hybris.platform.solrfacetsearch.provider.impl.FormatValueFilter;
import de.hybris.platform.solrfacetsearch.provider.impl.SplitValueFilter;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


@UnitTest
public class ProductAttributesValueResolverTest extends AbstractLocalizedValueResolverTest
{
	@Mock
	private ModelService modelService;

	@Mock
	private TypeService typeService;

	@Mock
	private VariantsService variantsService;

	@Mock
	private ComposedTypeModel composedType;

	@Mock
	private ProductModel product;

	@Mock
	private VariantProductModel variantProduct1;

	@Mock
	private VariantProductModel variantProduct2;

	@Mock
	private FormatValueFilter formatValueFilter;

	@Mock
	private SplitValueFilter splitValueFilter;

	private ProductAttributesValueResolver valueResolver;

	@Before
	public void setUp()
	{
		when(formatValueFilter.doFilter(any(IndexerBatchContext.class), any(IndexedProperty.class), any(Object.class))).thenAnswer(
				new Answer<Object>()
				{
					public Object answer(final InvocationOnMock invocation)
					{
						return invocation.getArguments()[2];
					}
				});

		when(splitValueFilter.doFilter(any(IndexerBatchContext.class), any(IndexedProperty.class), any(Object.class))).thenAnswer(
				new Answer<Object>()
				{
					public Object answer(final InvocationOnMock invocation)
					{
						return invocation.getArguments()[2];
					}
				});

		when(typeService.getComposedTypeForClass(product.getClass())).thenReturn(composedType);
		when(typeService.getComposedTypeForClass(variantProduct1.getClass())).thenReturn(composedType);
		when(typeService.getComposedTypeForClass(variantProduct2.getClass())).thenReturn(composedType);
		when(Boolean.valueOf(typeService.hasAttribute(eq(composedType), any(String.class)))).thenReturn(Boolean.TRUE);

		when(variantProduct2.getBaseProduct()).thenReturn(variantProduct1);
		when(variantProduct1.getBaseProduct()).thenReturn(product);

		valueResolver = new ProductAttributesValueResolver();
		valueResolver.setSessionService(getSessionService());
		valueResolver.setQualifierProvider(getQualifierProvider());
		valueResolver.setModelService(modelService);
		valueResolver.setTypeService(typeService);
		valueResolver.setVariantsService(variantsService);

		final List<ValueFilter> valueFilters = new ArrayList<>();
		valueFilters.add(formatValueFilter);
		valueFilters.add(splitValueFilter);

		valueResolver.setValueFilters(valueFilters);
	}

	@Test
	public void resolveNonSupportedAttributeValue() throws Exception
	{
		// given
		final String attributeName = INDEXED_PROPERTY_NAME;

		when(Boolean.valueOf(typeService.hasAttribute(composedType, attributeName))).thenReturn(Boolean.FALSE);

		// when
		final Object returnedAttributeValue = valueResolver.getAttributeValue(getIndexedProperty(), product, attributeName);

		// then
		assertNull(returnedAttributeValue);
	}

	@Test
	public void resolveAttributeWithNoValue() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final String attributeName = indexedProperty.getName();
		final Object attributeValue = null;

		indexedProperty.getValueProviderParameters().put(ProductAttributesValueResolver.OPTIONAL_PARAM, Boolean.TRUE.toString());

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(modelService.getAttributeValue(product, attributeName)).thenReturn(attributeValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument(), Mockito.never()).addField(any(IndexedProperty.class), any());
		verify(getInputDocument(), Mockito.never()).addField(any(IndexedProperty.class), any(), any(String.class));
	}

	@Test
	public void resolveNonLocalizedAttribute() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final String attributeName = indexedProperty.getName();
		final Object attributeValue = new Object();

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(modelService.getAttributeValue(product, attributeName)).thenReturn(attributeValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument()).addField(indexedProperty, attributeValue, null);
	}

	@Test
	public void resolveLocalizedAttribute() throws Exception
	{
		// given
		final IndexedProperty localizedIndexedProperty = getLocalizedIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(localizedIndexedProperty);
		final String localizedAttributeName = localizedIndexedProperty.getName();
		final Object localizedAttributeValue = new Object();

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(modelService.getAttributeValue(product, localizedAttributeName)).thenReturn(localizedAttributeValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument()).addField(localizedIndexedProperty, localizedAttributeValue, EN_LANGUAGE_QUALIFIER);
	}

	@Test
	public void resolveLocalizedAttributeWithMultipleLanguages() throws Exception
	{
		// given
		final IndexedProperty localizedIndexedProperty = getLocalizedIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(localizedIndexedProperty);
		final String localizedAttributeName = localizedIndexedProperty.getName();
		final Object enAttributeValue = new Object();
		final Object deAttributeValue = new Object();

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier(), getDeQualifier()));
		when(modelService.getAttributeValue(product, localizedAttributeName)).thenReturn(enAttributeValue).thenReturn(
				deAttributeValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument()).addField(localizedIndexedProperty, enAttributeValue, EN_LANGUAGE_QUALIFIER);
		verify(getInputDocument()).addField(localizedIndexedProperty, deAttributeValue, DE_LANGUAGE_QUALIFIER);
	}

	@Test
	public void resolveMultipleIndexedProperties() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final IndexedProperty localizedIndexedProperty = getLocalizedIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Arrays.asList(indexedProperty, localizedIndexedProperty);
		final String attributeName = indexedProperty.getName();
		final Object attributeValue = new Object();
		final String localizedAttributeName = localizedIndexedProperty.getName();
		final Object localizedAttributeValue = new Object();

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(modelService.getAttributeValue(product, attributeName)).thenReturn(attributeValue);
		when(modelService.getAttributeValue(product, localizedAttributeName)).thenReturn(localizedAttributeValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument()).addField(indexedProperty, attributeValue, null);
		verify(getInputDocument()).addField(localizedIndexedProperty, localizedAttributeValue, EN_LANGUAGE_QUALIFIER);
	}

	@Test
	public void resolveNonOptionalAttributeWithValue() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final String attributeName = indexedProperty.getName();
		final Object attributeValue = new Object();

		indexedProperty.getValueProviderParameters().put(ProductAttributesValueResolver.OPTIONAL_PARAM, Boolean.FALSE.toString());

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(modelService.getAttributeValue(product, attributeName)).thenReturn(attributeValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument()).addField(indexedProperty, attributeValue, null);
	}

	@Test
	public void resolveNonOptionalAttributeWithEmptyStringValue() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final String attributeName = indexedProperty.getName();
		final Object attributeValue = null;

		indexedProperty.getValueProviderParameters().put(ProductAttributesValueResolver.OPTIONAL_PARAM, Boolean.FALSE.toString());

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(modelService.getAttributeValue(product, attributeName)).thenReturn(attributeValue);

		// expect
		expectedException.expect(FieldValueProviderException.class);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);
	}

	@Test
	public void resolveWithAttributeParam() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final String attributeName = "innerColor";
		final Object attributeValue = new Object();

		indexedProperty.getValueProviderParameters().put(ProductAttributesValueResolver.ATTRIBUTE_PARAM, attributeName);

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(modelService.getAttributeValue(product, attributeName)).thenReturn(attributeValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument()).addField(indexedProperty, attributeValue, null);
	}

	@Test
	public void resolveNonOptionalAttributeWithNoValue() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final String attributeName = indexedProperty.getName();
		final Object attributeValue = null;

		indexedProperty.getValueProviderParameters().put(ProductAttributesValueResolver.OPTIONAL_PARAM, Boolean.FALSE.toString());

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(modelService.getAttributeValue(product, attributeName)).thenReturn(attributeValue);

		// expect
		expectedException.expect(FieldValueProviderException.class);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);
	}

	@Test
	public void resolveAttributeWithFormatAndSplit() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final String attributeName = indexedProperty.getName();
		final Object attributeValue = new Object();
		final String formatterBeanName = "dateFormatter";
		final String formattedValue = "a b";

		indexedProperty.getValueProviderParameters().put(FormatValueFilter.FORMAT_PARAM, formatterBeanName);
		indexedProperty.getValueProviderParameters().put(SplitValueFilter.SPLIT_PARAM, Boolean.TRUE.toString());

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(modelService.getAttributeValue(product, attributeName)).thenReturn(attributeValue);
		when(formatValueFilter.doFilter(getBatchContext(), indexedProperty, attributeValue)).thenReturn(formattedValue);
		when(splitValueFilter.doFilter(getBatchContext(), indexedProperty, formattedValue)).thenReturn(
				Arrays.asList(formattedValue.split(SplitValueFilter.SPLIT_REGEX_PARAM_DEFAULT_VALUE)));

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument()).addField(indexedProperty, "a", null);
		verify(getInputDocument()).addField(indexedProperty, "b", null);
	}

	@Test
	public void resolveAttributeWithSplitAndFormat() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final String attributeName = indexedProperty.getName();
		final Object attributeValue = "a b";
		final String formatterBeanName = "dateFormatter";
		final List<String> splitResult = Arrays.asList(((String) attributeValue)
				.split(SplitValueFilter.SPLIT_REGEX_PARAM_DEFAULT_VALUE));
		final String formattedValue1 = "formattedObject1";
		final String formattedValue2 = "formattedObject2";
		final List<String> formateResult = new ArrayList<>();
		formateResult.add(formattedValue1);
		formateResult.add(formattedValue2);

		indexedProperty.getValueProviderParameters().put(FormatValueFilter.FORMAT_PARAM, formatterBeanName);
		indexedProperty.getValueProviderParameters().put(SplitValueFilter.SPLIT_PARAM, Boolean.TRUE.toString());

		final List<ValueFilter> valueFilters = new ArrayList<>();
		valueFilters.add(splitValueFilter);
		valueFilters.add(formatValueFilter);

		valueResolver.setValueFilters(valueFilters);

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(modelService.getAttributeValue(product, attributeName)).thenReturn(attributeValue);
		when(splitValueFilter.doFilter(getBatchContext(), indexedProperty, attributeValue)).thenReturn(splitResult);
		when(formatValueFilter.doFilter(getBatchContext(), indexedProperty, splitResult)).thenReturn(formateResult);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument()).addField(indexedProperty, formattedValue1, null);
		verify(getInputDocument()).addField(indexedProperty, formattedValue2, null);
	}

	@Test
	public void resolveWithSplit() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final String attributeName = indexedProperty.getName();
		final Object attributeValue = "a b";

		indexedProperty.getValueProviderParameters().put(SplitValueFilter.SPLIT_PARAM, Boolean.TRUE.toString());

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(modelService.getAttributeValue(product, attributeName)).thenReturn(attributeValue);
		when(splitValueFilter.doFilter(getBatchContext(), indexedProperty, attributeValue)).thenReturn(
				Arrays.asList(((String) attributeValue).split(SplitValueFilter.SPLIT_REGEX_PARAM_DEFAULT_VALUE)));

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument()).addField(indexedProperty, "a", null);
		verify(getInputDocument()).addField(indexedProperty, "b", null);
	}

	@Test
	public void resolveVariantAttribute() throws FieldValueProviderException
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final String attributeName = indexedProperty.getName();
		final Object attributeValue = new Object();

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(modelService.getAttributeValue(variantProduct2, attributeName)).thenReturn(attributeValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, variantProduct2);

		// then
		verify(getInputDocument()).addField(indexedProperty, attributeValue, null);
	}

	@Test
	public void resolveVariantAttributeWith1Level() throws FieldValueProviderException
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final String attributeName = indexedProperty.getName();
		final Object attributeValue = new Object();

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(modelService.getAttributeValue(variantProduct2, attributeName)).thenReturn(null);
		when(modelService.getAttributeValue(variantProduct1, attributeName)).thenReturn(attributeValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, variantProduct2);

		// then
		verify(getInputDocument()).addField(indexedProperty, attributeValue, null);
	}

	@Test
	public void resolveVariantAttributeWith2Levels() throws FieldValueProviderException
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final String attributeName = indexedProperty.getName();
		final Object attributeValue = new Object();

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(modelService.getAttributeValue(variantProduct2, attributeName)).thenReturn(null);
		when(modelService.getAttributeValue(variantProduct1, attributeName)).thenReturn(null);
		when(modelService.getAttributeValue(product, attributeName)).thenReturn(attributeValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, variantProduct2);

		// then
		verify(getInputDocument()).addField(indexedProperty, attributeValue, null);
	}

	@Test
	public void resolveVariantAttributeWith2LevelsWithNoValue() throws FieldValueProviderException
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final String attributeName = indexedProperty.getName();
		final Object attributeValue = null;

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(modelService.getAttributeValue(variantProduct2, attributeName)).thenReturn(null);
		when(modelService.getAttributeValue(variantProduct1, attributeName)).thenReturn(null);
		when(modelService.getAttributeValue(product, attributeName)).thenReturn(attributeValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, variantProduct2);

		// then
		verify(getInputDocument(), never()).addField(indexedProperty, attributeValue, null);
	}

	@Test
	public void resolveBaseProductAttribute() throws FieldValueProviderException
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final String attributeName = indexedProperty.getName();
		final Object productAttributeValue = Long.valueOf(1);
		final Object variantProductAttributeValue = Long.valueOf(2);

		indexedProperty.getValueProviderParameters().put(ProductAttributesValueResolver.SKIP_VARIANTS_PARAM,
				Boolean.TRUE.toString());

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(modelService.getAttributeValue(product, attributeName)).thenReturn(productAttributeValue);
		when(modelService.getAttributeValue(variantProduct1, attributeName)).thenReturn(variantProductAttributeValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, variantProduct2);

		// then
		verify(getInputDocument()).addField(indexedProperty, productAttributeValue, null);
	}
}
