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
package de.hybris.platform.variants.interceptor;

import de.hybris.platform.variants.model.GenericVariantProductModel;
import de.hybris.platform.variants.model.VariantCategoryModel;
import de.hybris.platform.variants.model.VariantValueCategoryModel;
import de.hybris.platform.basecommerce.util.AbstractCommerceServicelayerTransactionalTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;

import java.util.LinkedList;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;


public class VariantCategoryValidateInterceptorTest extends AbstractCommerceServicelayerTransactionalTest
{
	private final static Logger LOG = LoggerFactory.getLogger(VariantCategoryValidateInterceptorTest.class);

	@Resource
	VariantCategoryValidateInterceptor variantCategoryValidateInterceptor;

	CatalogVersionModel catalogVersion;
	UnitModel unit;

	@Before
	public void setUp() throws Exception
	{
		catalogVersion = createCatalogVersion("myCatalog", "Standard");
		unit = createUnit("PCS");
	}

	@Test
	public void shouldInitializeTheInterceptor()
	{
		Assert.assertNotNull(variantCategoryValidateInterceptor);
	}

	@Test
	public void shouldEnsureVariantCategoryOrdering()
	{
		final VariantCategoryModel color = createVariantCategory("color", catalogVersion);

		final VariantCategoryModel size = createVariantCategory("size", catalogVersion);
		size.setSupercategories(Lists.<CategoryModel> newArrayList(color));

		final VariantCategoryModel fit = createVariantCategory("fit", catalogVersion);
		fit.setSupercategories(Lists.<CategoryModel> newArrayList(size));

		final ProductModel base = createProduct("base", catalogVersion, GenericVariantProductModel._TYPECODE, unit, color, size,
				fit);

		final VariantValueCategoryModel red = createVariantValueCategory("red", color, 1, catalogVersion);
		final VariantValueCategoryModel medium = createVariantValueCategory("M", size, 1, catalogVersion);
		final VariantValueCategoryModel wide = createVariantValueCategory("wide", fit, 1, catalogVersion);

		createGenericVariantProduct("variantProduct", base, catalogVersion, red, medium, wide);


		Assert.assertTrue("The order should have been 'color (red), size (M), fit (wide)'.",
				getVariantCategoryPriority(red) < getVariantCategoryPriority(medium)
						&& getVariantCategoryPriority(medium) < getVariantCategoryPriority(wide));


	}

	@Test
	public void shouldOnlyAllowOneVariantCategoryIsSetInSupercategories()
	{
		final VariantCategoryModel color = createVariantCategory("color", catalogVersion);
		final VariantCategoryModel size = createVariantCategory("size", catalogVersion);
		final VariantCategoryModel fit = createVariantCategory("fit", catalogVersion);

		fit.setSupercategories(Lists.<CategoryModel> newArrayList(color, size));

		try
		{
			save(fit);
			Assert.fail("It shouldn't be possible to save a VariantCategory with multiple superctegories");
		}
		catch (final Exception e)
		{
			// 'ight
		}
	}

	@Test
	public void shouldOnlyAllowOneVariantCategoryIsSetInSubcategories()
	{
		final VariantCategoryModel color = createVariantCategory("color", catalogVersion);
		final VariantCategoryModel size = createVariantCategory("size", catalogVersion);
		final VariantCategoryModel fit = createVariantCategory("fit", catalogVersion);

		color.setCategories(Lists.<CategoryModel> newArrayList(size, fit));

		try
		{
			save(color);
			Assert.fail("It shouldn't be possible to save a VariantCategory with multiple superctegories");
		}
		catch (final Exception e)
		{
			// 'ight
		}
	}

	private int getVariantCategoryPriority(final VariantValueCategoryModel variantValueCategory)
	{
		return getPathToRoot(variantValueCategory).size();
	}

	private LinkedList<CategoryModel> getPathToRoot(final VariantValueCategoryModel variantValueCategory)
	{
		final LinkedList<CategoryModel> pathToRoot = new LinkedList<>(categoryService.getPathForCategory(variantValueCategory));

		while (!categoryService.isRoot(pathToRoot.get(0)))
		{
			pathToRoot.addAll(0, categoryService.getPathForCategory(pathToRoot.get(0)));
		}

		return pathToRoot;
	}

}
