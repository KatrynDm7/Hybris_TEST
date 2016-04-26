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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.hyend2.factories.MandatoryPropertiesFactory;
import de.hybris.platform.hyend2.items.Dimension;
import de.hybris.platform.hyend2.items.HyendIndexSchema;
import de.hybris.platform.hyend2.items.IndexElement;
import de.hybris.platform.hyend2.items.IndexOperation;
import de.hybris.platform.hyend2.items.Property;
import de.hybris.platform.hyend2.items.Resolvable;
import de.hybris.platform.hyend2.resolvers.PropertyResolver;
import de.hybris.platform.hyend2.resolvers.impl.ExportContext;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.endeca.itl.record.PropertyValue;


/**
 * @author michal.flasinski
 * 
 */
@UnitTest
public class DefaultPropertyResolverServiceTest
{
	private DefaultPropertyResolverService propertyResolverService;

	@Mock
	private MandatoryPropertiesFactory mandatoryPropertyResolversMappingFactory;

	@Mock
	private TypeService mockTypeService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		propertyResolverService = new DefaultPropertyResolverService();
		propertyResolverService.setMandatoryPropertyResolversMappingFactory(mandatoryPropertyResolversMappingFactory);
		propertyResolverService.setTypeService(mockTypeService);
	}

	@Test
	public void testGetPropertiesForType()
	{
		final HyendIndexSchema indexSchema = mock(HyendIndexSchema.class);
		final List<IndexElement> indexElements = new ArrayList<IndexElement>();
		final IndexElement indexElement = new IndexElement();
		final Property property = new Property("code", null, null);
		final Property property2 = new Property("name", null, null);
		final ComposedTypeModel product = mock(ComposedTypeModel.class);
		indexElement.setType(product);
		indexElement.addProperty(property);
		indexElement.addProperty(property2);
		indexElements.add(indexElement);
		given(indexSchema.getIndexElements()).willReturn(indexElements);
		final ItemModel item = mock(ItemModel.class);
		given(item.getItemtype()).willReturn("Product");
		given(product.getCode()).willReturn("Product");
		given(Boolean.valueOf(mockTypeService.isAssignableFrom("Product", "Product"))).willReturn(Boolean.TRUE);

		final Set<Property> properties = propertyResolverService.getPropertiesForType(indexSchema, item, new ExportContext(
				Locale.ENGLISH, IndexOperation.BASELINE));

		assertThat(properties).hasSize(2);
		assertThat(properties).containsOnly(property, property2);
	}

	@Test
	public void testGetPropertiesForTypeWithMandatoryProperties()
	{
		final HyendIndexSchema indexSchema = mock(HyendIndexSchema.class);
		final ItemModel item = mock(ItemModel.class);
		final Property property = new Property(null, null, null);
		final List<Property> properties = Collections.singletonList(property);
		given(mandatoryPropertyResolversMappingFactory.getMandatoryProperties(indexSchema, item)).willReturn(properties);

		final Set<Property> propertiesList = propertyResolverService.getPropertiesForType(indexSchema, item, new ExportContext(
				Locale.ENGLISH, IndexOperation.BASELINE));

		assertThat(propertiesList).hasSize(1);
		assertThat(propertiesList).containsOnly(property);
	}

	@Test
	public void testGetDimensionsForType()
	{
		final HyendIndexSchema indexSchema = mock(HyendIndexSchema.class);
		final List<IndexElement> indexElements = new ArrayList<IndexElement>();
		final IndexElement indexElement = new IndexElement();
		final Dimension dimension = new Dimension("clockSpeed", null, null);
		final Dimension dimension2 = new Dimension("manufacturer", null, null);
		final ComposedTypeModel product = mock(ComposedTypeModel.class);
		indexElement.setType(product);
		indexElement.addDimension(dimension);
		indexElement.addDimension(dimension2);
		indexElements.add(indexElement);
		given(indexSchema.getIndexElements()).willReturn(indexElements);
		final ItemModel item = mock(ItemModel.class);
		given(item.getItemtype()).willReturn("Product");
		given(product.getCode()).willReturn("Product");
		given(Boolean.valueOf(mockTypeService.isAssignableFrom("Product", "Product"))).willReturn(Boolean.TRUE);


		final Set<Dimension> properties = propertyResolverService.getDimensionsForType(indexSchema, item, new ExportContext(
				Locale.ENGLISH, IndexOperation.BASELINE));

		assertThat(properties).hasSize(2);
		assertThat(properties).containsOnly(dimension, dimension2);
	}

	@Test
	public void testResolveValues()
	{
		final ItemModel item = mock(ItemModel.class);
		final PropertyResolver mockResolver = mock(PropertyResolver.class);
		given(mockResolver.resolve(null, item, null)).willReturn(Arrays.asList("value1")).willReturn(Arrays.asList("value2"));
		final HyendIndexSchema indexSchema = mock(HyendIndexSchema.class);
		final List<IndexElement> indexElements = new ArrayList<IndexElement>();
		final Dimension dimension = new Dimension("clockSpeed", mockResolver, null);
		final Property property = new Property("code", mockResolver, null);
		given(indexSchema.getIndexElements()).willReturn(indexElements);

		final List<Resolvable> dimensions = new ArrayList<Resolvable>();
		dimensions.add(dimension);
		dimensions.add(property);

		final List<PropertyValue> properties = propertyResolverService.resolveValues(dimensions, null, item);

		assertThat(properties).hasSize(2);
		assertThat(properties.get(1).getName()).isEqualTo("code");
		assertThat(properties.get(1).getValue()).isEqualTo("value2");
		assertThat(properties.get(0).getName()).isEqualTo("clockSpeed");
		assertThat(properties.get(0).getValue()).isEqualTo("value1");
	}
}
