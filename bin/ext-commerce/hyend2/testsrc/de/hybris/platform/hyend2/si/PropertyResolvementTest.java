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
package de.hybris.platform.hyend2.si;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.hyend2.items.HyendIndexSchema;
import de.hybris.platform.hyend2.items.IndexOperation;
import de.hybris.platform.hyend2.items.Property;
import de.hybris.platform.hyend2.resolvers.PropertyResolver;
import de.hybris.platform.hyend2.resolvers.impl.ExportContext;
import de.hybris.platform.hyend2.services.PropertyResolverService;
import de.hybris.platform.hyend2.si.data.PropertyResolvement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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
public class PropertyResolvementTest
{
	private final PropertyResolvement propertyResolvement = new PropertyResolvement();

	@Mock
	private PropertyResolverService propertyResolverService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		propertyResolvement.setPropertyResolverService(propertyResolverService);
	}

	@Test
	public void testResolveValues()
	{
		//given
		final HyendIndexSchema indexSchema = mock(HyendIndexSchema.class);
		final ItemModel item = mock(ProductModel.class);

		final Set<Property> resolversMapping = new HashSet<Property>();
		final PropertyResolver resolverForCode = mock(PropertyResolver.class);
		given(resolverForCode.resolve(null, item, null)).willReturn(Arrays.asList("ABC"));
		final PropertyResolver resolverForName = mock(PropertyResolver.class);
		given(resolverForName.resolve(null, item, null)).willReturn(Arrays.asList("DEF"));

		resolversMapping.add(new Property("code", resolverForCode, null));
		resolversMapping.add(new Property("name", resolverForName, null));

		final ExportContext exportContext = new ExportContext(Locale.ENGLISH, IndexOperation.BASELINE);
		given(propertyResolverService.getPropertiesForType(indexSchema, item, exportContext)).willReturn(resolversMapping);
		final List<PropertyValue> coll = new ArrayList<PropertyValue>();
		given(propertyResolverService.resolveValues(resolversMapping, exportContext, item)).willReturn(coll);

		//when
		final List<PropertyValue> resolve = propertyResolvement.resolve(exportContext, indexSchema, item);

		//then
		assertThat(resolve).isSameAs(coll);
	}
}
