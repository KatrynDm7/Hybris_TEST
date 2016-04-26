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
package de.hybris.platform.commercefacades.converter.config;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.converter.impl.DefaultConfigurablePopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/commercefacades/modifiable-populator-test-spring.xml")
@UnitTest
public class ModifiablePopulatorTest
{

	@Resource
	private DefaultConfigurablePopulator<ProductModel, ProductData, String> productConfiguredPopulator;

	@Resource
	private DefaultConfigurablePopulator<ProductModel, ProductData, String> extendedProductConfiguredPopulator;

	@Resource
	private DefaultConfigurablePopulator<ProductModel, ProductData, String> noCodeProductConfiguredPopulator;

	private static ProductModel source;
	private static List<String> options;

	@BeforeClass
	public static void setUpBeforeClass()
	{
		source = mock(ProductModel.class);
		given(source.getCode()).willReturn("12345");
		given(source.getName()).willReturn("Product Name");
		given(source.getDescription()).willReturn("Product Description");

		options = new ArrayList<>();
		options.add("CODE");
		options.add("NAME");
		options.add("DESCRIPTION");
	}

	@Test
	public void testProductConfiguredPopulator()
	{
		final ProductData target = new ProductData();

		productConfiguredPopulator.populate(source, target, options);

		Assert.assertEquals(source.getCode(), target.getCode());
		Assert.assertEquals(null, target.getName());
		Assert.assertEquals(source.getDescription(), target.getDescription());
	}

	@Test
	public void testExtendedProductConfiguredPopulator()
	{
		final ProductData target = new ProductData();

		extendedProductConfiguredPopulator.populate(source, target, options);

		Assert.assertEquals(source.getCode(), target.getCode());
		Assert.assertEquals(source.getName(), target.getName());
		Assert.assertEquals(source.getDescription(), target.getDescription());
	}

	@Test
	public void testNoCodeProductConfiguredPopulator()
	{
		final ProductData target = new ProductData();

		noCodeProductConfiguredPopulator.populate(source, target, options);

		Assert.assertEquals(null, target.getCode());
		Assert.assertEquals(source.getName(), target.getName());
		Assert.assertEquals(source.getDescription(), target.getDescription());
	}
}
