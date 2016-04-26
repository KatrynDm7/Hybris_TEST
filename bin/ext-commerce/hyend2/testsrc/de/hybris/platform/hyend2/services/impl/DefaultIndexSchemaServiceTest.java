/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.hyend2.services.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.hyend2.daos.IndexSchemaDao;
import de.hybris.platform.hyend2.items.Dimension;
import de.hybris.platform.hyend2.items.HyendIndexSchema;
import de.hybris.platform.hyend2.items.IndexElement;
import de.hybris.platform.hyend2.items.Property;
import de.hybris.platform.hyend2.model.Hyend2CasEacConfigurationModel;
import de.hybris.platform.hyend2.model.Hyend2IndexSchemaModel;
import de.hybris.platform.hyend2.services.IndexSchemaService;
import de.hybris.platform.hyend2.services.PropertyResolverService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import java.util.Date;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * @author michal.flasinski
 * 
 */
@IntegrationTest
public class DefaultIndexSchemaServiceTest extends ServicelayerTransactionalTest
{
	@Resource
	private IndexSchemaService indexSchemaService;
	@Resource
	private IndexSchemaDao indexSchemaDao;
	@Resource
	private PropertyResolverService propertyResolverService;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createHardwareCatalog();
		importCsv("/data/hyend2essentialData.csv", "UTF-8");
		importCsv("/data/hyend2sampleData.csv", "UTF-8");
	}

	@Test
	public void testGetIndexSchemaByName()
	{
		final HyendIndexSchema indexSchema = indexSchemaService.getIndexSchemaByName("my index schema");

		assertThat(indexSchema).isNotNull();
		assertThat(indexSchema.getCatalogVersions()).hasSize(1);
		assertThat(indexSchema.getDimensionsCount()).isEqualTo(3);
		assertThat(indexSchema.getIndexElements()).hasSize(1);
		assertThat(indexSchema.getPrecedenceRules()).isEmpty();
		assertThat(indexSchema.getRecordStoreData()).isNotNull();

		final IndexElement indexElement = indexSchema.getIndexElements().get(0);

		//pre-condition for the following tests for explicit property names
		assertThat(((DefaultIndexSchemaService) indexSchemaService).isUseTypePrefix()).isTrue();

		final Set<Dimension> dimensions = indexElement.getDimensions();
		assertThat(dimensions).hasSize(3);
		assertThat(dimensions).onProperty("attribute").containsOnly("Product.clockSpeed", "Product.Categories",
				"Product.Price_range");

		final Set<Property> properties = indexElement.getProperties();
		assertThat(properties).hasSize(3);
		assertThat(properties).onProperty("attribute").containsOnly("Product.code", "Product.name", "Product.price_usd");
	}

	@Test
	public void testAttributeNamesWithoutTypePrefix()
	{
		//set up new iss without a type prefix
		final DefaultIndexSchemaService iss = new DefaultIndexSchemaService();
		iss.setPropertyResolverService(propertyResolverService);
		iss.setIndexSchemaDao(indexSchemaDao);
		iss.setUseTypePrefix(false);

		final HyendIndexSchema indexSchema = iss.getIndexSchemaByName("my index schema");
		final IndexElement indexElement = indexSchema.getIndexElements().get(0);

		final Set<Dimension> dimensions = indexElement.getDimensions();
		assertThat(dimensions).hasSize(3);
		assertThat(dimensions).onProperty("attribute").containsOnly("clockSpeed", "Categories", "Price_range");

		final Set<Property> properties = indexElement.getProperties();
		assertThat(properties).hasSize(3);
		assertThat(properties).onProperty("attribute").containsOnly("code", "name", "price_usd");

	}

	@Test
	public void testUpdateLastIndexTime()
	{
		final Date currentDate = new Date();

		final Date updatedTime = indexSchemaService.updateLastIndexTime("my index schema");

		assertThat(currentDate.getTime()).isLessThanOrEqualTo(updatedTime.getTime());
	}

	@Test
	public void testCheckIfEacConfigurationExists()
	{
		final Hyend2CasEacConfigurationModel mockIndexSchema = mock(Hyend2CasEacConfigurationModel.class);
		given(mockIndexSchema.getName()).willReturn("qaendeca cas");
		given(mockIndexSchema.getPk()).willReturn(PK.parse("123456"));

		assertThat(indexSchemaService.checkIfEacConfigurationExists(mockIndexSchema)).isTrue();
	}

	@Test
	public void testCheckIfIndexSchemaExists()
	{
		final Hyend2IndexSchemaModel mockIndexSchema = mock(Hyend2IndexSchemaModel.class);
		given(mockIndexSchema.getName()).willReturn("mockName");

		assertThat(indexSchemaService.checkIfIndexSchemaExists(mockIndexSchema)).isFalse();
	}
}
