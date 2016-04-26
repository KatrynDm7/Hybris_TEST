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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.ExpressionEvaluator;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractLocalizedValueResolverTest;
import de.hybris.platform.solrfacetsearch.provider.impl.ModelAttributesValueResolver;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;


@UnitTest
public class ProductPromotionAttributesValueResolverTest extends AbstractLocalizedValueResolverTest
{

	private ProductPromotionAttributesValueResolver valueResolver;

	@Mock
	private PromotionsService promotionsService;
	@Mock
	private TypeService typeService;
	@Mock
	private ModelService modelService;
	@Mock
	private ExpressionEvaluator expressionEvaluator;
	@Mock
	private ComposedTypeModel composedType;
	@Mock
	private ProductModel productModel;
	@Mock
	private ProductPromotionModel productPromotionModel;
	BaseSiteModel baseSiteModel;

	@Before
	public void setUp()
	{
		when(typeService.getComposedTypeForClass(productPromotionModel.getClass())).thenReturn(composedType);
		when(Boolean.valueOf(typeService.hasAttribute(eq(composedType), any(String.class)))).thenReturn(Boolean.TRUE);

		PromotionGroupModel promotionGroupModel = new PromotionGroupModel();
		promotionGroupModel.setPromotions(Collections.singletonList((AbstractPromotionModel)productPromotionModel));

		baseSiteModel = new BaseSiteModel();
		baseSiteModel.setDefaultPromotionGroup(promotionGroupModel);
		getBatchContext().getFacetSearchConfig().getIndexConfig().setBaseSite(baseSiteModel);

		when(promotionsService
				.getProductPromotions(Collections.singletonList(baseSiteModel.getDefaultPromotionGroup()), productModel))
				.thenReturn(Collections.singletonList(productPromotionModel));


		valueResolver = new ProductPromotionAttributesValueResolver();
		valueResolver.setSessionService(getSessionService());
		valueResolver.setQualifierProvider(getQualifierProvider());
		valueResolver.setPromotionsService(promotionsService);
		valueResolver.setTypeService(typeService);
		valueResolver.setModelService(modelService);
		valueResolver.setExpressionEvaluator(expressionEvaluator);
	}

	@Test
	public void resolveNonSupportedAttributeValue() throws Exception
	{
		// given
		final String attributeName = INDEXED_PROPERTY_NAME;

		when(Boolean.valueOf(typeService.hasAttribute(composedType, attributeName))).thenReturn(Boolean.FALSE);

		// when
		final Object returnedAttributeValue = valueResolver
				.getAttributeValue(getIndexedProperty(), getBatchContext(), productPromotionModel, attributeName);

		// then
		assertNull(returnedAttributeValue);
	}

	@Test
	public void resolveAttributeWithNoPromotion() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final String attributeName = indexedProperty.getName();
		final Object attributeValue = null;

		indexedProperty.getValueProviderParameters().put(ModelAttributesValueResolver.OPTIONAL_PARAM, Boolean.TRUE.toString());

		when(promotionsService
				.getProductPromotions(Collections.singletonList(baseSiteModel.getDefaultPromotionGroup()), productModel)).thenReturn(
				null);

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(modelService.getAttributeValue(productPromotionModel, attributeName)).thenReturn(attributeValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, productModel);

		// then
		verify(getInputDocument(), Mockito.never()).addField(any(IndexedProperty.class), any());
		verify(getInputDocument(), Mockito.never()).addField(any(IndexedProperty.class), any(), any(String.class));
	}

	@Test
	public void resolveAttributeWithNoValue() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final String attributeName = indexedProperty.getName();
		final Object attributeValue = null;

		indexedProperty.getValueProviderParameters().put(ModelAttributesValueResolver.OPTIONAL_PARAM, Boolean.TRUE.toString());

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(modelService.getAttributeValue(productPromotionModel, attributeName)).thenReturn(attributeValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, productModel);

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
		when(modelService.getAttributeValue(productPromotionModel, attributeName)).thenReturn(attributeValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, productModel);

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
		when(modelService.getAttributeValue(productPromotionModel, localizedAttributeName)).thenReturn(localizedAttributeValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, productModel);

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
		when(modelService.getAttributeValue(productPromotionModel, localizedAttributeName)).thenReturn(enAttributeValue).thenReturn(
				deAttributeValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, productModel);

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
		when(modelService.getAttributeValue(productPromotionModel, attributeName)).thenReturn(attributeValue);
		when(modelService.getAttributeValue(productPromotionModel, localizedAttributeName)).thenReturn(localizedAttributeValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, productModel);

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

		indexedProperty.getValueProviderParameters().put(ModelAttributesValueResolver.OPTIONAL_PARAM, Boolean.FALSE.toString());

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(modelService.getAttributeValue(productPromotionModel, attributeName)).thenReturn(attributeValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, productModel);

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

		indexedProperty.getValueProviderParameters().put(ModelAttributesValueResolver.OPTIONAL_PARAM, Boolean.FALSE.toString());

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(modelService.getAttributeValue(productPromotionModel, attributeName)).thenReturn(attributeValue);

		// expect
		expectedException.expect(FieldValueProviderException.class);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, productModel);
	}

	@Test
	public void resolveNonOptionalAttributeWithNoValue() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final String attributeName = indexedProperty.getName();
		final Object attributeValue = null;

		indexedProperty.getValueProviderParameters().put(ModelAttributesValueResolver.OPTIONAL_PARAM, Boolean.FALSE.toString());

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(modelService.getAttributeValue(productPromotionModel, attributeName)).thenReturn(attributeValue);

		// expect
		expectedException.expect(FieldValueProviderException.class);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, productModel);
	}

	@Test
	public void resolveWithAttributeParam() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final String attributeName = "innerColor";
		final Object attributeValue = new Object();

		indexedProperty.getValueProviderParameters().put(ModelAttributesValueResolver.ATTRIBUTE_PARAM, attributeName);

		when(getQualifierProvider().getAvailableQualifiers(getFacetSearchConfig(), getIndexedType())).thenReturn(
				Arrays.asList(getEnQualifier()));
		when(modelService.getAttributeValue(productPromotionModel, attributeName)).thenReturn(attributeValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, productModel);

		// then
		verify(getInputDocument()).addField(indexedProperty, attributeValue, null);
	}

	@Test
	public void resolveWithExpressionAndNoAttribute() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);
		final String attributeName = "comments[0].code";
		final String expressionValue = "testCode";

		indexedProperty.getValueProviderParameters().put(ModelAttributesValueResolver.ATTRIBUTE_PARAM, attributeName);
		indexedProperty.getValueProviderParameters()
				.put(ModelAttributesValueResolver.EVALUATE_EXPRESSION_PARAM, Boolean.TRUE.toString());

		when(expressionEvaluator.evaluate(attributeName, productPromotionModel)).thenReturn(expressionValue);

		// when
		valueResolver.resolve(getInputDocument(), getBatchContext(), indexedProperties, productModel);

		// then
		verify(getInputDocument()).addField(indexedProperty, expressionValue, null);
	}
}
