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

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.integration.AbstractIntegrationTest;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;

import java.util.Collection;

import javax.annotation.Resource;

import org.junit.Test;


/**
 * Test for {@link SpELValueProvider}
 */
@IntegrationTest
public class SpELValueProviderTest extends AbstractIntegrationTest
{
	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private SpELValueProvider springELValueProvider;

	@Resource
	private ProductService productService;

	@Test
	public void testUsingExpression() throws Exception
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();

		final IndexConfig indexConfig = facetSearchConfig.getIndexConfig();
		assertThat(indexConfig).isNotNull();

		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		assertThat(indexedType).isNotNull();

		final IndexedProperty indexedProperty = indexedType.getIndexedProperties().get("code");
		assertThat(indexedProperty).isNotNull();
		indexedProperty.setValueProviderParameter("code");

		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("hwcatalog", "Online");
		final ProductModel product = productService.getProductForCode(catalogVersion, "HW2300-2356");
		assertThat(product).isNotNull();

		final Collection<FieldValue> fieldValues = springELValueProvider.getFieldValues(indexConfig, indexedProperty, product);

		assertThat(fieldValues).hasSize(1);
		assertThat(fieldValues.iterator().next().getFieldName()).isEqualTo("code_string");
		assertThat(fieldValues.iterator().next().getValue()).isEqualTo("HW2300-2356");
	}

	@Test
	public void testWithoutExpression() throws Exception
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();

		final IndexConfig indexConfig = facetSearchConfig.getIndexConfig();
		assertThat(indexConfig).isNotNull();

		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		assertThat(indexedType).isNotNull();

		final IndexedProperty indexedProperty = indexedType.getIndexedProperties().get("code");
		assertThat(indexedProperty).isNotNull();

		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("hwcatalog", "Online");
		final ProductModel product = productService.getProductForCode(catalogVersion, "HW2300-2356");
		assertThat(product).isNotNull();

		final Collection<FieldValue> fieldValues = springELValueProvider.getFieldValues(indexConfig, indexedProperty, product);

		assertThat(fieldValues).hasSize(1);
		assertThat(fieldValues.iterator().next().getFieldName()).isEqualTo("code_string");
		assertThat(fieldValues.iterator().next().getValue()).isEqualTo("HW2300-2356");
	}

	@Test
	public void testWithExpressionMultilanguage() throws Exception
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();

		final IndexConfig indexConfig = facetSearchConfig.getIndexConfig();
		assertThat(indexConfig).isNotNull();

		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		assertThat(indexedType).isNotNull();

		final IndexedProperty indexedProperty = indexedType.getIndexedProperties().get("name");
		assertThat(indexedProperty).isNotNull();
		indexedProperty.setValueProviderParameter("getName(#lang)");

		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("hwcatalog", "Online");
		final ProductModel product = productService.getProductForCode(catalogVersion, "HW2300-2356");
		assertThat(product).isNotNull();

		final Collection<FieldValue> fieldValues = springELValueProvider.getFieldValues(indexConfig, indexedProperty, product);

		assertThat(fieldValues).hasSize(4);
		assertThat(fieldValues).onProperty("fieldName").contains("name_text_de", "name_sortable_de_sortabletext", "name_text_en",
				"name_sortable_en_sortabletext");
	}

	@Test
	public void testWithExpressionMulticurrency() throws Exception
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();

		final IndexConfig indexConfig = facetSearchConfig.getIndexConfig();
		assertThat(indexConfig).isNotNull();

		final IndexedType indexedType = indexConfig.getIndexedTypes().values().iterator().next();
		assertThat(indexedType).isNotNull();

		final IndexedProperty indexedProperty = indexedType.getIndexedProperties().get("price");
		assertThat(indexedProperty).isNotNull();
		indexedProperty
				.setValueProviderParameter("@priceService.getPriceInformationsForProduct(#item).![priceValue].?[currencyIso == #currency.isocode].![value]");

		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("hwcatalog", "Online");
		final ProductModel product = productService.getProductForCode(catalogVersion, "HW2300-2356");
		assertThat(product).isNotNull();

		final Collection<FieldValue> fieldValues = springELValueProvider.getFieldValues(indexConfig, indexedProperty, product);

		assertThat(fieldValues).hasSize(2);
		assertThat(fieldValues).onProperty("fieldName").containsOnly("price_eur_string", "price_usd_string");
		assertThat(fieldValues).onProperty("value").containsOnly("157.95", "217.97099999999998");
	}
}
