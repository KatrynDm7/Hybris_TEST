/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.productconfig.frontend.breadcrumb;


import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.history.BrowseHistory;
import de.hybris.platform.acceleratorstorefrontcommons.history.BrowseHistoryEntry;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ItemContextBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ProductConfigureBreadcrumbBuilderTest
{
	private static final String PRODUCT_URL = "productUrl";
	private static final String CATEGORY_URL = "categoryUrl";

	private ProductConfigureBreadcrumbBuilder breadCrumbBuilder;
	protected ProductModel productModel;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		productModel = createModel();
		final UrlResolver<ProductModel> productModelUrlResolver = new ProductUrlResolverTest();
		final UrlResolver<CategoryModel> categoryModelUrlResolver = new CategoryModelUrlResolverTest();
		final BrowseHistory browseHistory = new BrowseHistoryTest();

		breadCrumbBuilder = new ProductConfigureBreadcrumbBuilder()
		{
			@Override
			protected String getLinkText()
			{
				return "Product Configuration";
			}
		};
		breadCrumbBuilder.setProductModelUrlResolver(productModelUrlResolver);
		breadCrumbBuilder.setCategoryModelUrlResolver(categoryModelUrlResolver);
		breadCrumbBuilder.setBrowseHistory(browseHistory);
		breadCrumbBuilder.setProductConverter(new ProductConverterTest());

	}

	@Test
	public void getBreadCrumbTest()
	{

		final List<Breadcrumb> breadCrumbs = breadCrumbBuilder.getBreadcrumbs(productModel);

		assertEquals(3, breadCrumbs.size());

		assertEquals(CATEGORY_URL, breadCrumbs.get(0).getUrl());
		assertEquals(PRODUCT_URL, breadCrumbs.get(1).getUrl());
		assertEquals(PRODUCT_URL + "/config", breadCrumbs.get(2).getUrl());
	}

	private ProductModel createModel()
	{
		final Map<String, Object> prodValues = new HashMap<>();
		final PK prodPk = PK.fromLong(500);
		final ProductModel productModel = new ProductModel(
				ItemContextBuilder.createMockContext(ProductModel.class, prodPk, Locale.US, prodValues));
		productModel.setName("Test");
		productModel.setCode("1234");

		final Map<String, Object> catValues = new HashMap<>();

		final PK catPk = PK.fromLong(600);
		final CategoryModel catModel = new CategoryModel(
				ItemContextBuilder.createMockContext(CategoryModel.class, catPk, Locale.US, catValues));
		catModel.setName("Category");
		catModel.setCode("0815");


		final Map<String, Object> superCatValues = new HashMap<>();
		final PK superCatPk = PK.fromLong(600);
		final CategoryModel superCatModel = new ClassificationClassModel(
				ItemContextBuilder.createMockContext(ClassificationClassModel.class, superCatPk, Locale.US, superCatValues));
		superCatModel.setName("Super-Category");
		superCatModel.setCode("4711");

		final List<CategoryModel> superSuperCats = new ArrayList<>();
		superSuperCats.add(superCatModel);
		catModel.setSupercategories(superSuperCats);

		final Collection<CategoryModel> superCats = new Vector<>();
		superCats.add(catModel);
		productModel.setSupercategories(superCats);

		return productModel;
	}

	class ProductUrlResolverTest implements UrlResolver<ProductModel>
	{

		@Override
		public String resolve(final ProductModel source)
		{
			return PRODUCT_URL;
		}
	}

	class ProductConverterTest implements Converter<ProductModel, ProductData>
	{
		@Override
		public ProductData convert(final ProductModel paramSOURCE, final ProductData paramTARGET) throws ConversionException
		{
			paramTARGET.setName(paramSOURCE.getName());
			paramTARGET.setCode(paramSOURCE.getCode());
			return paramTARGET;
		}


		@Override
		public ProductData convert(final ProductModel paramSOURCE) throws ConversionException
		{
			return convert(paramSOURCE, new ProductData());
		}



	}

	class CategoryModelUrlResolverTest implements UrlResolver<CategoryModel>
	{

		@Override
		public String resolve(final CategoryModel source)
		{
			return CATEGORY_URL;
		}
	}

	class BrowseHistoryTest implements BrowseHistory
	{

		private final Map<String, BrowseHistoryEntry> history = new HashMap<>();

		@Override
		public void addBrowseHistoryEntry(final BrowseHistoryEntry browseHistoryEntry)
		{
			history.put(browseHistoryEntry.getUrl(), browseHistoryEntry);
		}

		@Override
		public BrowseHistoryEntry findEntryMatchUrlEndsWith(final String url)
		{
			return history.get(url);
		}

	}
}