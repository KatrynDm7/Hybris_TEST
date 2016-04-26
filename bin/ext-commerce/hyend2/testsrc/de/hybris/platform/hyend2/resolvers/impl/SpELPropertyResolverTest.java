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
package de.hybris.platform.hyend2.resolvers.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.hyend2.items.IndexOperation;

import java.util.Collection;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;


/**
 * @author michal.flasinski
 * 
 */
public class SpELPropertyResolverTest
{
	private final SpELPropertyResolver resolver = new SpELPropertyResolver();

	@Before
	public void setUp()
	{
		final ApplicationContext mockAppContext = mock(ApplicationContext.class);
		resolver.setApplicationContext(mockAppContext);
	}

	@Test
	public void testResolver()
	{
		final ProductModel product = mock(ProductModel.class);
		final UnitModel mockUnit = mock(UnitModel.class);
		given(mockUnit.getCode()).willReturn("kg");
		given(product.getUnit()).willReturn(mockUnit);

		final Collection<String> resolvedValues = resolver.resolve(new ExportContext(Locale.ENGLISH, IndexOperation.BASELINE),
				product, "unit.code");

		assertThat(resolvedValues).hasSize(1);
		assertThat(resolvedValues).contains("kg");
	}

	@Test
	public void testResolver2()
	{
		final ProductModel product = mock(ProductModel.class);
		given(product.getDescription(Locale.ENGLISH)).willReturn("EnglishDescription");

		final Collection<String> resolvedValues = resolver.resolve(new ExportContext(Locale.ENGLISH, IndexOperation.BASELINE),
				product, "getDescription(#locale)");

		assertThat(resolvedValues).hasSize(1);
		assertThat(resolvedValues).contains("EnglishDescription");
	}

}
