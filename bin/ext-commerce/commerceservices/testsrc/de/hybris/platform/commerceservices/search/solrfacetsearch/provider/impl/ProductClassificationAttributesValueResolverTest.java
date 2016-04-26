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
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.classification.features.LocalizedFeature;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.provider.ValueFilter;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractLocalizedValueResolverTest;
import de.hybris.platform.solrfacetsearch.provider.impl.FormatValueFilter;
import de.hybris.platform.solrfacetsearch.provider.impl.SplitValueFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


@UnitTest
public class ProductClassificationAttributesValueResolverTest extends AbstractLocalizedValueResolverTest
{
	@Mock
	private ClassificationService classificationService;

	@Mock
	private ProductModel product;

	@Mock
	private ClassAttributeAssignmentModel indexedPropertyAssignment;

	@Mock
	private ClassAttributeAssignmentModel localizedIndexedPropertyAssignment;

	@Mock
	private FeatureList featureList;

	@Mock
	private Feature feature;

	@Mock
	private LocalizedFeature localizedFeature;

	@Mock
	private FeatureValue featureValue1;

	@Mock
	private FeatureValue featureValue2;

	@Mock
	private FormatValueFilter formatValueFilter;

	@Mock
	private SplitValueFilter splitValueFilter;

	private ProductClassificationAttributesValueResolver valueResolver;

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

		getIndexedProperty().setClassAttributeAssignment(indexedPropertyAssignment);
		getLocalizedIndexedProperty().setClassAttributeAssignment(localizedIndexedPropertyAssignment);

		valueResolver = new ProductClassificationAttributesValueResolver();
		valueResolver.setSessionService(getSessionService());
		valueResolver.setQualifierProvider(getQualifierProvider());
		valueResolver.setClassificationService(classificationService);

		final List<ValueFilter> valueFilters = new ArrayList<>();
		valueFilters.add(formatValueFilter);
		valueFilters.add(splitValueFilter);

		valueResolver.setValueFilters(valueFilters);
	}

	@Test
	public void resolveAttributeWithNoValue() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final Object attributeValue = null;

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(classificationService.getFeatures(eq(product), anyListOf(ClassAttributeAssignmentModel.class)))
				.thenReturn(featureList);
		when(featureList.getFeatureByAssignment(indexedProperty.getClassAttributeAssignment())).thenReturn(feature);
		when(feature.getValues()).thenReturn(Collections.singletonList(featureValue1));
		when(featureValue1.getValue()).thenReturn(attributeValue);

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
		final Object attributeValue = new Object();

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(classificationService.getFeatures(eq(product), anyListOf(ClassAttributeAssignmentModel.class)))
				.thenReturn(featureList);
		when(featureList.getFeatureByAssignment(indexedProperty.getClassAttributeAssignment())).thenReturn(feature);
		when(feature.getValues()).thenReturn(Collections.singletonList(featureValue1));
		when(featureValue1.getValue()).thenReturn(attributeValue);

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
		final Object localizedAttributeValue = new Object();

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(classificationService.getFeatures(eq(product), anyListOf(ClassAttributeAssignmentModel.class)))
				.thenReturn(featureList);
		when(featureList.getFeatureByAssignment(localizedIndexedProperty.getClassAttributeAssignment())).thenReturn(
				localizedFeature);
		when(localizedFeature.getValues(Locale.ENGLISH)).thenReturn(Collections.singletonList(featureValue1));
		when(featureValue1.getValue()).thenReturn(localizedAttributeValue);

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
		final Object enAttributeValue = new Object();
		final Object deAttributeValue = new Object();

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier(), getDeQualifier()));
		when(classificationService.getFeatures(eq(product), anyListOf(ClassAttributeAssignmentModel.class)))
				.thenReturn(featureList);
		when(featureList.getFeatureByAssignment(localizedIndexedProperty.getClassAttributeAssignment())).thenReturn(
				localizedFeature);
		when(localizedFeature.getValues(Locale.ENGLISH)).thenReturn(Collections.singletonList(featureValue1));
		when(localizedFeature.getValues(Locale.GERMAN)).thenReturn(Collections.singletonList(featureValue2));
		when(featureValue1.getValue()).thenReturn(enAttributeValue);
		when(featureValue2.getValue()).thenReturn(deAttributeValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument()).addField(localizedIndexedProperty, enAttributeValue, EN_LANGUAGE_QUALIFIER);
		verify(getInputDocument()).addField(localizedIndexedProperty, deAttributeValue, DE_LANGUAGE_QUALIFIER);
	}

	@Test
	public void resolveWithMultipleIndexedProperties() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final IndexedProperty localizedIndexedProperty = getLocalizedIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Arrays.asList(indexedProperty, localizedIndexedProperty);
		final Object attributeValue = new Object();
		final Object localizedAttributeValue = new Object();

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(classificationService.getFeatures(eq(product), anyListOf(ClassAttributeAssignmentModel.class)))
				.thenReturn(featureList);

		when(featureList.getFeatureByAssignment(indexedProperty.getClassAttributeAssignment())).thenReturn(feature);
		when(feature.getValues()).thenReturn(Collections.singletonList(featureValue1));
		when(featureValue1.getValue()).thenReturn(attributeValue);

		when(featureList.getFeatureByAssignment(localizedIndexedProperty.getClassAttributeAssignment())).thenReturn(
				localizedFeature);
		when(localizedFeature.getValues(Locale.ENGLISH)).thenReturn(Collections.singletonList(featureValue2));
		when(featureValue2.getValue()).thenReturn(localizedAttributeValue);

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
		final Object attributeValue = new Object();

		indexedProperty.getValueProviderParameters().put(ProductClassificationAttributesValueResolver.OPTIONAL_PARAM,
				Boolean.FALSE.toString());

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(classificationService.getFeatures(eq(product), anyListOf(ClassAttributeAssignmentModel.class)))
				.thenReturn(featureList);
		when(featureList.getFeatureByAssignment(indexedProperty.getClassAttributeAssignment())).thenReturn(localizedFeature);
		when(localizedFeature.getValues()).thenReturn(Collections.singletonList(featureValue1));
		when(featureValue1.getValue()).thenReturn(attributeValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument()).addField(indexedProperty, attributeValue, null);
	}

	@Test
	public void resolveNonOptionalAttributeWithEmtyStringValue() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final String attributeValue = "";

		indexedProperty.getValueProviderParameters().put(ProductClassificationAttributesValueResolver.OPTIONAL_PARAM,
				Boolean.FALSE.toString());

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(classificationService.getFeatures(eq(product), anyListOf(ClassAttributeAssignmentModel.class)))
				.thenReturn(featureList);
		when(featureList.getFeatureByAssignment(indexedProperty.getClassAttributeAssignment())).thenReturn(localizedFeature);
		when(localizedFeature.getValues()).thenReturn(Collections.singletonList(featureValue1));
		when(featureValue1.getValue()).thenReturn(attributeValue);

		// expect
		expectedException.expect(FieldValueProviderException.class);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);
	}

	@Test
	public void resolveNonOptionalAttributeWithNoValue() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final String attributeValue = null;

		indexedProperty.getValueProviderParameters().put(ProductClassificationAttributesValueResolver.OPTIONAL_PARAM,
				Boolean.FALSE.toString());

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(classificationService.getFeatures(eq(product), anyListOf(ClassAttributeAssignmentModel.class)))
				.thenReturn(featureList);
		when(featureList.getFeatureByAssignment(indexedProperty.getClassAttributeAssignment())).thenReturn(localizedFeature);
		when(localizedFeature.getValues()).thenReturn(Collections.singletonList(featureValue1));
		when(featureValue1.getValue()).thenReturn(attributeValue);

		// expect
		expectedException.expect(FieldValueProviderException.class);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);
	}

	@Test
	public void resolveWithSplit() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final Object attributeValue = "a b";

		indexedProperty.getValueProviderParameters().put(SplitValueFilter.SPLIT_PARAM, Boolean.TRUE.toString());

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(classificationService.getFeatures(eq(product), anyListOf(ClassAttributeAssignmentModel.class)))
				.thenReturn(featureList);
		when(featureList.getFeatureByAssignment(indexedProperty.getClassAttributeAssignment())).thenReturn(feature);
		when(feature.getValues()).thenReturn(Collections.singletonList(featureValue1));
		when(featureValue1.getValue()).thenReturn(attributeValue);
		when(splitValueFilter.doFilter(getBatchContext(), indexedProperty, attributeValue)).thenReturn(
				Arrays.asList(((String) attributeValue).split(SplitValueFilter.SPLIT_REGEX_PARAM_DEFAULT_VALUE)));

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument()).addField(indexedProperty, "a", null);
		verify(getInputDocument()).addField(indexedProperty, "b", null);
	}

	@Test
	public void resolveAttributeWithFormat() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final Object attributeValue = new Object();
		final String formatterBeanName = "dateFormatter";
		final String formattedValue = null;

		indexedProperty.getValueProviderParameters().put(FormatValueFilter.FORMAT_PARAM, formatterBeanName);

		when(classificationService.getFeatures(eq(product), anyListOf(ClassAttributeAssignmentModel.class)))
				.thenReturn(featureList);
		when(featureList.getFeatureByAssignment(indexedProperty.getClassAttributeAssignment())).thenReturn(feature);
		when(feature.getValues()).thenReturn(Collections.singletonList(featureValue1));
		when(featureValue1.getValue()).thenReturn(attributeValue);
		when(formatValueFilter.doFilter(getBatchContext(), indexedProperty, attributeValue)).thenReturn(formattedValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument()).addField(indexedProperty, formattedValue, null);
	}


	@Test
	public void resolveAttributeWithFormatAndSplit() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final Object attributeValue = new Object();
		final String formatterBeanName = "dateFormatter";
		final String formattedValue = "a b";

		indexedProperty.getValueProviderParameters().put(FormatValueFilter.FORMAT_PARAM, formatterBeanName);
		indexedProperty.getValueProviderParameters().put(SplitValueFilter.SPLIT_PARAM, Boolean.TRUE.toString());

		when(classificationService.getFeatures(eq(product), anyListOf(ClassAttributeAssignmentModel.class)))
				.thenReturn(featureList);
		when(featureList.getFeatureByAssignment(indexedProperty.getClassAttributeAssignment())).thenReturn(feature);
		when(feature.getValues()).thenReturn(Collections.singletonList(featureValue1));
		when(featureValue1.getValue()).thenReturn(attributeValue);
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

		when(classificationService.getFeatures(eq(product), anyListOf(ClassAttributeAssignmentModel.class)))
				.thenReturn(featureList);
		when(featureList.getFeatureByAssignment(indexedProperty.getClassAttributeAssignment())).thenReturn(feature);
		when(feature.getValues()).thenReturn(Collections.singletonList(featureValue1));
		when(featureValue1.getValue()).thenReturn(attributeValue);
		when(splitValueFilter.doFilter(getBatchContext(), indexedProperty, attributeValue)).thenReturn(splitResult);
		when(formatValueFilter.doFilter(getBatchContext(), indexedProperty, splitResult)).thenReturn(formateResult);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, product);

		// then
		verify(getInputDocument()).addField(indexedProperty, formattedValue1, null);
		verify(getInputDocument()).addField(indexedProperty, formattedValue2, null);
	}
}
