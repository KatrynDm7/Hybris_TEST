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

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.product.converters.populator.StockPopulator;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Threshold values are set on categories levels, Stock Threshold Populator populate the value to StockData for a
 * product.
 *
 */
public class StockThresholdPopulator<SOURCE extends ProductModel, TARGET extends StockData> extends
		StockPopulator<SOURCE, TARGET>
{

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(StockThresholdPopulator.class.getName());

	private CMSSiteService cmsSiteService;
	private CategoryService categoryService;


	@Override
	public void populate(final SOURCE productModel, final TARGET stockData) throws ConversionException
	{

		super.populate(productModel, stockData);

		final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
		if (isStockSystemEnabled(baseStore))
		{
			stockData.setStockThreshold(getThresholdValue(productModel));
		}

	}

	/**
	 * Loop in all the super categories of a product belongs to, if it is a variant product, get all super categories
	 * from its base product also, then get the lowest level value from each of the categories tree, then the minimum
	 * values from all the categories trees is the threshold will be applied on a product. If no threshold is set on any
	 * categories, then apply the default threshold value set at site level.
	 *
	 * @param productModel
	 * @return the threshold value to apply on the given product
	 *
	 */
	protected Integer getThresholdValue(final ProductModel productModel)
	{
		final Map<CategoryModel, Integer> thresholdOnCategory = new HashMap<>();
		final Set<CategoryModel> processedCategories = new HashSet<>();

		final Collection<CategoryModel> productSupercategories = new HashSet<>();
		productSupercategories.addAll(productModel.getSupercategories());

		if (productModel instanceof VariantProductModel)
		{
			final ProductModel baseProduct = ((VariantProductModel) productModel).getBaseProduct();
			final Collection<CategoryModel> baseProductSupercategories = baseProduct.getSupercategories();
			if (!CollectionUtils.isEmpty(baseProductSupercategories))
			{
				productSupercategories.addAll(baseProductSupercategories);
			}
		}

		for (final CategoryModel category : productSupercategories)
		{
			getThresholdfromOneTree(category, processedCategories, thresholdOnCategory);
		}

		if (MapUtils.isEmpty(thresholdOnCategory))
		{
			Integer stockTresholdLevel = 0;

			try
			{
				stockTresholdLevel = getCmsSiteService().getCurrentSite().getDefaultStockLevelThreshold();
			}
			catch (final IllegalStateException e)
			{
				LOG.warn("The method getCurrentSite() is not a CMSSite. Stock threshold level will be zero.");
			}

			return stockTresholdLevel;
		}
		else
		{
			for (final CategoryModel processedCategory : processedCategories)
			{
				if (thresholdOnCategory.containsKey(processedCategory))
				{
					thresholdOnCategory.remove(processedCategory);
				}
			}

			final Collection<Integer> thresholdToCompare = thresholdOnCategory.values();
			return Collections.min(thresholdToCompare);
		}
	}

	/**
	 * The recursive method get the threshold value from a category tree one level up each time, it will exit the
	 * recursion when get an value from one level of category and flag all its super categories as processed
	 *
	 * @param currentCategory
	 * @param processedCategories
	 * @param thresholdOnCategory
	 *           a map contains the threshold value and the category where it set from
	 *
	 */
	protected void getThresholdfromOneTree(final CategoryModel currentCategory, final Set<CategoryModel> processedCategories,
			final Map<CategoryModel, Integer> thresholdOnCategory)
	{

		if (currentCategory.getStockLevelThreshold() != null)
		{
			final Collection<CategoryModel> allSupercategories = getCategoryService().getAllSupercategoriesForCategory(
					currentCategory);
			if (!CollectionUtils.isEmpty(allSupercategories))
			{
				processedCategories.addAll(allSupercategories);
			}
			thresholdOnCategory.put(currentCategory, currentCategory.getStockLevelThreshold());
			return;
		}

		final Collection<CategoryModel> supercategories = currentCategory.getSupercategories();

		if (!CollectionUtils.isEmpty(supercategories))
		{
			for (final CategoryModel category : supercategories)
			{
				getThresholdfromOneTree(category, processedCategories, thresholdOnCategory);
			}
		}

	}

	public CMSSiteService getCmsSiteService()
	{
		return cmsSiteService;
	}

	@Required
	public void setCmsSiteService(final CMSSiteService cmsSiteService)
	{
		this.cmsSiteService = cmsSiteService;
	}

	public CategoryService getCategoryService()
	{
		return categoryService;
	}

	@Required
	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}

}
