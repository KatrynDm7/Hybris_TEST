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
package de.hybris.platform.b2bacceleratorfacades.product.converters.populator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.platform.b2bacceleratorfacades.product.converters.populator.StockThresholdPopulator;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSSiteService;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.variants.model.VariantProductModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;


public class ProductStockThresholdPopulatorTest
{
	private StockThresholdPopulator<ProductModel, StockData> populator;
	private CategoryService categoryService;
	private BaseStoreService baseStoreService;
	private CommerceStockService commerceStockService;
	private VariantProductModel variant;
	private StockData data;
	private ProductModel productModel;
	private CMSSiteModel cmssiteModel;
	private DefaultCMSSiteService cmssiteService;
	private BaseStoreModel baseStore = mock(BaseStoreModel.class);

	@Before
	public void setUp()
	{

		populator = new StockThresholdPopulator<ProductModel, StockData>();

		categoryService = mock(CategoryService.class);
		populator.setCategoryService(categoryService);

		baseStoreService = mock(BaseStoreService.class);
		populator.setBaseStoreService(baseStoreService);

		commerceStockService = mock(CommerceStockService.class);

		populator.setCommerceStockService(commerceStockService);

		cmssiteService = Mockito.mock(DefaultCMSSiteService.class);
		cmssiteModel = Mockito.mock(CMSSiteModel.class);


		populator.setCmsSiteService(cmssiteService);
		populator.setCategoryService(categoryService);

		data = new StockData();
		productModel = new ProductModel();
		variant = new VariantProductModel();
		baseStore = mock(BaseStoreModel.class);

	}

	@SuppressWarnings("boxing")
	@Test
	public void testThreshold1()
	{

		final CategoryModel size7 = new PKCategoryModel(1, 40);
		final CategoryModel yellow = new PKCategoryModel(2, 100);
		final CategoryModel fit_W = new PKCategoryModel(12);
		final CategoryModel size = new PKCategoryModel(3, 20);
		final CategoryModel fit = new PKCategoryModel(4);
		final CategoryModel color = new PKCategoryModel(5, 10);
		final CategoryModel men = new PKCategoryModel(8);
		final CategoryModel shoes = new PKCategoryModel(9, 15);
		final CategoryModel safety = new PKCategoryModel(10);
		final CategoryModel apparel = new PKCategoryModel(11, 5);

		final ProductModel base = new ProductModel();
		base.setSupercategories(Lists.newArrayList(size, color, fit, men));

		final VariantProductModel variant = new VariantProductModel();
		variant.setBaseProduct(base);
		variant.setSupercategories(Lists.newArrayList(size7, yellow, fit_W));

		final StockData data = new StockData();
		data.setStockLevelStatus(StockLevelStatus.INSTOCK);

		setSuperCategories(color, fit, size, size7, yellow, fit_W, men, shoes, safety, apparel);
		mockSuperCategories(size7, yellow, fit_W, size, fit, color, men, shoes, safety, apparel);
		mockSomeStuff();

		populator.populate(variant, data);

		Assert.assertTrue(data.getStockThreshold() == 15);

	}

	/***
	 *
	 * Product belongs to only one category
	 *
	 */
	@SuppressWarnings("boxing")
	@Test
	public void productBelongsToOnlyOneCat()
	{

		final CategoryModel fit = new PKCategoryModel(4, 11);
		final CategoryModel size7 = new PKCategoryModel(1, 10);

		Mockito.when(categoryService.getAllSupercategoriesForCategory(fit)).thenReturn(null);
		Mockito.when(categoryService.getAllSupercategoriesForCategory(size7)).thenReturn(null);
		mockSomeStuff();

		productModel.setSupercategories(Lists.newArrayList(fit));
		data.setStockLevelStatus(StockLevelStatus.INSTOCK);

		populator.populate(productModel, data);
		Assert.assertTrue(data.getStockThreshold() == 11);

		variant.setSupercategories(Lists.newArrayList(size7));
		variant.setBaseProduct(productModel);

		populator.populate(variant, data);
		Assert.assertTrue(data.getStockThreshold() == 10);

	}

	/**
	 * when there are only one category set for the product. the stock threshold value is not null.
	 *
	 *
	 */
	@SuppressWarnings("boxing")
	@Test
	public void onlyOneCategorySetForTheProduct_WithValue()
	{

		final CategoryModel color = new PKCategoryModel(5, 10);

		productModel.setSupercategories(Lists.newArrayList(color));

		Mockito.when(categoryService.getAllSupercategoriesForCategory(color)).thenReturn(null);
		mockSomeStuff();

		populator.populate(productModel, data);
		Assert.assertTrue(data.getStockThreshold() == 10);

	}

	/**
	 * when there are only one category set for the product. the stock threshold value is null.
	 *
	 *
	 */
	@SuppressWarnings("boxing")
	@Test
	public void onlyOneCategorySetForTheProduct_WithoutValue()
	{

		final CategoryModel color = new PKCategoryModel(5);

		productModel.setSupercategories(Lists.newArrayList(color));
		Mockito.when(categoryService.getAllSupercategoriesForCategory(color)).thenReturn(null);

		Mockito.when(cmssiteService.getCurrentSite()).thenReturn(cmssiteModel);
		Mockito.when(cmssiteModel.getDefaultStockLevelThreshold()).thenReturn(null);

		mockSomeStuff();

		populator.populate(productModel, data);
		Assert.assertTrue(data.getStockThreshold() == null);

	}

	/**
	 * when no stock threshold values defined.
	 *
	 *
	 */
	@SuppressWarnings("boxing")
	@Test
	public void noStockThresholdValuesAtAll()
	{

		final CategoryModel color = new PKCategoryModel(5);
		final CategoryModel fit = new PKCategoryModel(4);
		final CategoryModel size = new PKCategoryModel(3);
		final CategoryModel size7 = new PKCategoryModel(1);
		final CategoryModel yellow = new PKCategoryModel(2);
		final CategoryModel fit_W = new PKCategoryModel(12);
		final CategoryModel men = new PKCategoryModel(8);
		final CategoryModel shoes = new PKCategoryModel(9);
		final CategoryModel safety = new PKCategoryModel(10);
		final CategoryModel apparel = new PKCategoryModel(11);

		setSuperCategories(color, fit, size, size7, yellow, fit_W, men, shoes, safety, apparel);
		mockSuperCategories(size7, yellow, fit_W, size, fit, color, men, shoes, safety, apparel);

		productModel.setSupercategories(Lists.newArrayList(size, color, fit, men));
		variant.setSupercategories(Lists.newArrayList(size7, yellow, fit_W));
		variant.setBaseProduct(productModel);

		Mockito.when(cmssiteService.getCurrentSite()).thenReturn(cmssiteModel);
		Mockito.when(cmssiteModel.getDefaultStockLevelThreshold()).thenReturn(null);
		mockSomeStuff();

		populator.populate(variant, data);
		Assert.assertTrue(data.getStockThreshold() == null);

	}

	/***
	 * Last Cat Level Does not Have Values (size7 , fit_W , yellow , men )
	 */
	@SuppressWarnings("boxing")
	@Test
	public void lastCategoryLevelDoesnotHaveValue()
	{

		final CategoryModel color = new PKCategoryModel(5, 10);
		final CategoryModel fit = new PKCategoryModel(4, 11);
		final CategoryModel size = new PKCategoryModel(3, 20);
		final CategoryModel size7 = new PKCategoryModel(1);
		final CategoryModel yellow = new PKCategoryModel(2);
		final CategoryModel fit_W = new PKCategoryModel(12);
		final CategoryModel men = new PKCategoryModel(8);
		final CategoryModel shoes = new PKCategoryModel(9, 15);
		final CategoryModel safety = new PKCategoryModel(10, 16);
		final CategoryModel apparel = new PKCategoryModel(11, 50);

		setSuperCategories(color, fit, size, size7, yellow, fit_W, men, shoes, safety, apparel);
		mockSuperCategories(size7, yellow, fit_W, size, fit, color, men, shoes, safety, apparel);
		mockSomeStuff();

		productModel.setSupercategories(Lists.newArrayList(size, color, fit, men));
		variant.setBaseProduct(productModel);
		variant.setSupercategories(Lists.newArrayList(size7, yellow, fit_W));

		populator.populate(variant, data);
		Assert.assertTrue(data.getStockThreshold() == 15);

	}

	/***
	 * when all categories have the same Stock Threshold Value.
	 */
	@SuppressWarnings("boxing")
	@Test
	public void allCatHaveTheSameStockThresholdValue()
	{

		final CategoryModel color = new PKCategoryModel(5, 10);
		final CategoryModel fit = new PKCategoryModel(4, 10);
		final CategoryModel size = new PKCategoryModel(3, 10);
		final CategoryModel size7 = new PKCategoryModel(1, 10);
		final CategoryModel yellow = new PKCategoryModel(2, 10);
		final CategoryModel fit_W = new PKCategoryModel(12, 10);
		final CategoryModel men = new PKCategoryModel(8, 10);
		final CategoryModel shoes = new PKCategoryModel(9, 10);
		final CategoryModel safety = new PKCategoryModel(10, 10);
		final CategoryModel apparel = new PKCategoryModel(11, 10);

		setSuperCategories(color, fit, size, size7, yellow, fit_W, men, shoes, safety, apparel);
		mockSuperCategories(size7, yellow, fit_W, size, fit, color, men, shoes, safety, apparel);
		mockSomeStuff();

		productModel.setSupercategories(Lists.newArrayList(size, color, fit, men));
		variant.setBaseProduct(productModel);
		variant.setSupercategories(Lists.newArrayList(size7, yellow, fit_W));

		populator.populate(variant, data);
		Assert.assertTrue(data.getStockThreshold() == 10);

	}

	/***
	 * No Stock threshold value set for the categories and only the default Stock Threshold Value set at the site level
	 */
	@SuppressWarnings("boxing")
	@Test
	public void onlyDefaultStockThresholdAtSiteLevelHavingValue()
	{

		final CategoryModel color = new PKCategoryModel(5);
		final CategoryModel fit = new PKCategoryModel(4);
		final CategoryModel size = new PKCategoryModel(3);
		final CategoryModel size7 = new PKCategoryModel(1);
		final CategoryModel yellow = new PKCategoryModel(2);
		final CategoryModel fit_W = new PKCategoryModel(12);
		final CategoryModel men = new PKCategoryModel(8);
		final CategoryModel shoes = new PKCategoryModel(9);
		final CategoryModel safety = new PKCategoryModel(10);
		final CategoryModel apparel = new PKCategoryModel(11);

		setSuperCategories(color, fit, size, size7, yellow, fit_W, men, shoes, safety, apparel);
		mockSuperCategories(size7, yellow, fit_W, size, fit, color, men, shoes, safety, apparel);
		Mockito.when(cmssiteService.getCurrentSite()).thenReturn(cmssiteModel);
		Mockito.when(cmssiteModel.getDefaultStockLevelThreshold()).thenReturn(10);
		mockSomeStuff();

		productModel.setSupercategories(Lists.newArrayList(size, color, fit, men));
		variant.setBaseProduct(productModel);
		variant.setSupercategories(Lists.newArrayList(size7, yellow, fit_W));

		populator.populate(variant, data);
		Assert.assertTrue(data.getStockThreshold() == 10);

	}

	/**
	 * Only one category having a Stock threshold value.
	 */
	@SuppressWarnings("boxing")
	@Test
	public void onlyOneCategoryHavingStockThresholdValue()
	{

		final CategoryModel color = new PKCategoryModel(5, 2);
		final CategoryModel fit = new PKCategoryModel(4);
		final CategoryModel size = new PKCategoryModel(3);
		final CategoryModel size7 = new PKCategoryModel(1);
		final CategoryModel yellow = new PKCategoryModel(2);
		final CategoryModel fit_W = new PKCategoryModel(12);
		final CategoryModel men = new PKCategoryModel(8);
		final CategoryModel shoes = new PKCategoryModel(9);
		final CategoryModel safety = new PKCategoryModel(10);
		final CategoryModel apparel = new PKCategoryModel(11);

		setSuperCategories(color, fit, size, size7, yellow, fit_W, men, shoes, safety, apparel);
		mockSuperCategories(size7, yellow, fit_W, size, fit, color, men, shoes, safety, apparel);
		Mockito.when(cmssiteService.getCurrentSite()).thenReturn(cmssiteModel);
		Mockito.when(cmssiteModel.getDefaultStockLevelThreshold()).thenReturn(10);
		mockSomeStuff();

		productModel.setSupercategories(Lists.newArrayList(size, color, fit, men));
		variant.setBaseProduct(productModel);
		variant.setSupercategories(Lists.newArrayList(size7, yellow, fit_W));

		populator.populate(variant, data);
		Assert.assertTrue(data.getStockThreshold() == 2);

	}

	/***
	 * All categories have the Stock threshold value.
	 */
	@SuppressWarnings("boxing")
	@Test
	public void allCatHaveStockThresholdValue()
	{

		final CategoryModel color = new PKCategoryModel(5, 10);
		final CategoryModel fit = new PKCategoryModel(4, 11);
		final CategoryModel size = new PKCategoryModel(3, 20);
		final CategoryModel size7 = new PKCategoryModel(1, 40);
		final CategoryModel yellow = new PKCategoryModel(2, 100);
		final CategoryModel fit_W = new PKCategoryModel(12, 2);
		final CategoryModel men = new PKCategoryModel(8, 14);
		final CategoryModel shoes = new PKCategoryModel(9, 15);
		final CategoryModel safety = new PKCategoryModel(10, 16);
		final CategoryModel apparel = new PKCategoryModel(11, 50);

		setSuperCategories(color, fit, size, size7, yellow, fit_W, men, shoes, safety, apparel);
		mockSuperCategories(size7, yellow, fit_W, size, fit, color, men, shoes, safety, apparel);
		mockSomeStuff();

		productModel.setSupercategories(Lists.newArrayList(size, color, fit, men));
		variant.setBaseProduct(productModel);
		variant.setSupercategories(Lists.newArrayList(size7, yellow, fit_W));

		populator.populate(variant, data);
		Assert.assertTrue(data.getStockThreshold() == 2);

	}

	/**
	 * this will allow to setup the super categories for each category.
	 *
	 * @param color
	 * @param fit
	 * @param size
	 * @param size7
	 * @param yellow
	 * @param fit_W
	 * @param men
	 * @param shoes
	 * @param safety
	 * @param apparel
	 */
	private void setSuperCategories(final CategoryModel color, final CategoryModel fit, final CategoryModel size,
			final CategoryModel size7, final CategoryModel yellow, final CategoryModel fit_W, final CategoryModel men,
			final CategoryModel shoes, final CategoryModel safety, final CategoryModel apparel)
	{
		size7.setSupercategories(Lists.newArrayList(size));
		size.setSupercategories(Lists.newArrayList(fit));
		fit_W.setSupercategories(Lists.newArrayList(fit));
		fit.setSupercategories(Lists.newArrayList(color));
		yellow.setSupercategories(Lists.newArrayList(color));
		men.setSupercategories(Lists.newArrayList(shoes));
		shoes.setSupercategories(Lists.newArrayList(safety));
		safety.setSupercategories(Lists.newArrayList(apparel));
	}

	/**
	 * the following is a mock to return a specific categories when a call of getAllSupercategoriesForCategory method is
	 * performed on the categoryService object.
	 *
	 * @param size7
	 * @param yellow
	 * @param fit_W
	 * @param size
	 * @param fit
	 * @param color
	 * @param men
	 * @param shoes
	 * @param safety
	 * @param apparel
	 */
	private void mockSuperCategories(final CategoryModel size7, final CategoryModel yellow, final CategoryModel fit_W,
			final CategoryModel size, final CategoryModel fit, final CategoryModel color, final CategoryModel men,
			final CategoryModel shoes, final CategoryModel safety, final CategoryModel apparel)
	{

		Mockito.when(categoryService.getAllSupercategoriesForCategory(size7)).thenReturn(Lists.newArrayList(size, fit, color));
		Mockito.when(categoryService.getAllSupercategoriesForCategory(fit_W)).thenReturn(Lists.newArrayList(fit, color));
		Mockito.when(categoryService.getAllSupercategoriesForCategory(yellow)).thenReturn(Lists.newArrayList(color));
		Mockito.when(categoryService.getAllSupercategoriesForCategory(size)).thenReturn(Lists.newArrayList(fit, color));
		Mockito.when(categoryService.getAllSupercategoriesForCategory(fit)).thenReturn(Lists.newArrayList(color));
		Mockito.when(categoryService.getAllSupercategoriesForCategory(color)).thenReturn(null);
		Mockito.when(categoryService.getAllSupercategoriesForCategory(men)).thenReturn(Lists.newArrayList(shoes, safety, apparel));
		Mockito.when(categoryService.getAllSupercategoriesForCategory(shoes)).thenReturn(Lists.newArrayList(safety, apparel));
		Mockito.when(categoryService.getAllSupercategoriesForCategory(safety)).thenReturn(Lists.newArrayList(apparel));
		Mockito.when(categoryService.getAllSupercategoriesForCategory(apparel)).thenReturn(null);


	}

	/**
	 *
	 */
	private void mockSomeStuff()
	{
		when(baseStoreService.getCurrentBaseStore()).thenReturn(baseStore);
		when(commerceStockService.isStockSystemEnabled(baseStore)).thenReturn(Boolean.TRUE);
		when(commerceStockService.getStockLevelForProductAndBaseStore(variant, baseStore)).thenReturn((new Long(1000)));
		when(commerceStockService.getStockLevelStatusForProductAndBaseStore(variant, baseStore)).thenReturn(
				StockLevelStatus.INSTOCK);
	}

	class PKCategoryModel extends CategoryModel
	{

		private final PK pk;

		public PKCategoryModel(final long value)
		{
			pk = de.hybris.platform.core.PK.fromLong(value);
		}

		@SuppressWarnings("boxing")
		public PKCategoryModel(final long value, final int stockThreshold)
		{
			pk = de.hybris.platform.core.PK.fromLong(value);
			setStockLevelThreshold(stockThreshold);
		}

		@Override
		public de.hybris.platform.core.PK getPk()
		{
			return pk;
		}

	}

}
