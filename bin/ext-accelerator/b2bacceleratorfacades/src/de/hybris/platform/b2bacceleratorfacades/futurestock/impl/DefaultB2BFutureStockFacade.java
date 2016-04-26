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
package de.hybris.platform.b2bacceleratorfacades.futurestock.impl;

import de.hybris.platform.acceleratorservices.futurestock.FutureStockService;
import de.hybris.platform.b2bacceleratorfacades.futurestock.B2BFutureStockFacade;
import de.hybris.platform.commercefacades.product.data.FutureStockData;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.variants.model.VariantProductModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link B2BFutureStockFacade}
 */
public class DefaultB2BFutureStockFacade implements B2BFutureStockFacade
{

	private FutureStockService futureStockService;
	private ProductService productService;
	private CommerceCommonI18NService commerceCommonI18NService;

	@Override
	public List<FutureStockData> getFutureAvailability(final String productCode)
	{
		final Map<String, List<FutureStockData>> mapFutureStock = getFutureAvailability(Arrays.asList(productCode));

		if (mapFutureStock == null)
		{
			return null;
		}
		else
		{
			if (mapFutureStock.isEmpty())
			{
				return new ArrayList();
			}

			return mapFutureStock.get(productCode);
		}
	}

	@Override
	public Map<String, List<FutureStockData>> getFutureAvailabilityForSelectedVariants(final String productCode,
			final List<String> skus)
	{
		Map<String, List<FutureStockData>> result = null;
		final List<String> variantsList = filterSelectedVariantProducts(productCode, skus);
		if (CollectionUtils.isNotEmpty(variantsList))
		{
			result = getFutureAvailability(variantsList);
		}
		return result;
	}

	@Override
	public Map<String, List<FutureStockData>> getFutureAvailability(final List<String> productCodes)
	{
		Map<String, Map<Date, Integer>> productsMap = null;
		final List<ProductModel> products = new ArrayList<ProductModel>();
		for (final String productCode : productCodes)
		{
			products.add(productService.getProductForCode(productCode));
		}
		productsMap = futureStockService.getFutureAvailability(products);

		if (productsMap == null)
		{
			// future stock service not available, we will show this to the user
			return null;
		}
		else
		{
			final Map<String, List<FutureStockData>> result = new HashMap<>();
			mapMap2MapList(products, result, productsMap);

			return result;
		}
	}

	/**
	 * Get list of product codes from the variants of a product that match a given list of skus (product codes).
	 *
	 * @param productCode
	 *           The product that is related to the variants.
	 * @param skus
	 *           Product codes of the desired variants.
	 * @return List of matching codes. If product is not variant, returns null.
	 */
	public List<String> filterSelectedVariantProducts(final String productCode, final List<String> skus)
	{
		List<String> variantsList = new ArrayList<String>();
		final ProductModel productModel = productService.getProductForCode(productCode);

		if (CollectionUtils.isNotEmpty(skus))
		{
			// add list to a set to avoid duplication
			final Set<String> skusSet = new HashSet<String>(skus);
			if (CollectionUtils.isNotEmpty(productModel.getVariants()))
			{
				variantsList = getSelectedProductModels(skusSet, productModel.getVariants());
			}
			else if (productModel instanceof VariantProductModel)
			{
				variantsList = getSelectedProductModels(skusSet, ((VariantProductModel) productModel).getBaseProduct().getVariants());
			}
			else
			{
				// Product is not variant, return null.
				variantsList = null;
			}
		}

		return variantsList;
	}

	protected List<String> getSelectedProductModels(final Set<String> skus, final Collection<VariantProductModel> productModels)
	{
		final List<String> selectedProductModels = new ArrayList<String>();
		for (final ProductModel productModel : productModels)
		{
			if (skus.contains(productModel.getCode()))
			{
				selectedProductModels.add(productModel.getCode());
			}

		}
		return selectedProductModels;
	}

	/**
	 * Fills values from a Map<String, Map<Date, Integer>> into a Map<String, List<FutureStockData>>.
	 *
	 * @param products
	 * @param result
	 * @param productsMap
	 */
	private void mapMap2MapList(final List<ProductModel> products, final Map<String, List<FutureStockData>> result,
			final Map<String, Map<Date, Integer>> productsMap)
	{
		for (final ProductModel product : products)
		{
			final Map<Date, Integer> futureStockMap = productsMap.get(product.getCode());
			if (futureStockMap != null)
			{
				final List<FutureStockData> futureStock = futureMapAsList(futureStockMap);
				result.put(product.getCode(), futureStock);
			}
		}
	}

	private List<FutureStockData> futureMapAsList(final Map<Date, Integer> futureMap)
	{
		final List<FutureStockData> futureList = new ArrayList<>();

		for (final Entry<Date, Integer> entry : futureMap.entrySet())
		{
			final StockData stock = new StockData();

			stock.setStockLevel(Long.valueOf(entry.getValue().longValue()));

			final FutureStockData future = new FutureStockData();
			future.setStock(stock);

			final String formattedDate = DateFormat.getDateInstance(DateFormat.SHORT, getCurrentLocale()).format(entry.getKey());
			future.setDate(entry.getKey());
			future.setFormattedDate(formattedDate);

			futureList.add(future);
		}
		sortByDate(futureList);

		return futureList;
	}

	private void sortByDate(final List<FutureStockData> futureList)
	{
		Collections.sort(futureList, new FutureStockDataComparator());
	}

	class FutureStockDataComparator implements Comparator<FutureStockData>
	{

		@Override
		public int compare(final FutureStockData o1, final FutureStockData o2)
		{
			final boolean isDate1Null = (o1 == null || o1.getDate() == null);
			final boolean isDate2Null = (o2 == null || o2.getDate() == null);

			if (!isDate1Null)
			{
				if (!isDate2Null)
				{
					return o1.getDate().compareTo(o2.getDate());
				}
				else
				{
					return Integer.MAX_VALUE;
				}
			}
			else if (!isDate2Null)
			{
				return Integer.MIN_VALUE;
			}

			return 0;
		}

	}

	protected FutureStockService getFutureStockService()
	{
		return futureStockService;
	}

	@Required
	public void setFutureStockService(final FutureStockService service)
	{
		this.futureStockService = service;
	}

	protected CommerceCommonI18NService getCommerceCommonI18NService()
	{
		return commerceCommonI18NService;
	}

	@Required
	public void setCommerceCommonI18NService(final CommerceCommonI18NService commerceCommonI18NService)
	{
		this.commerceCommonI18NService = commerceCommonI18NService;
	}

	public ProductService getProductService()
	{
		return productService;
	}

	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	protected Locale getCurrentLocale()
	{
		final LanguageModel currentLanguage = getCommerceCommonI18NService().getDefaultLanguage();
		final Locale locale = getCommerceCommonI18NService().getLocaleForLanguage(currentLanguage);

		if (locale == null)
		{
			return new Locale(currentLanguage.getIsocode());
		}
		return locale;
	}

}
