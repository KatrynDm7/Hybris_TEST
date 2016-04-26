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
package de.hybris.platform.financialfacades.populators;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Lists;


/**
 * Search insurance product populator.
 *
 * @param <SOURCE>
 *           SearchResultValueData
 * @param <TARGET>
 *           ProductData
 * @param <PRODUCT>
 *           ProductModel
 */
public class SearchProductInsurancePopulator<SOURCE extends SearchResultValueData, TARGET extends ProductData, PRODUCT extends ProductModel>
		implements Populator<SOURCE, TARGET>
{

	protected static final String CODE = "code";
	protected static final String ALL_CATEGORIES = "allCategories";
	private ProductService productService;
	private Map<String, Populator<PRODUCT, TARGET>> categoryProductInsurancePopulators;

	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException
	{

		final List<Populator<PRODUCT, TARGET>> populators = getPopulatorsForCategories(source);

		if (CollectionUtils.isNotEmpty(populators))
		{

			for (final Populator<PRODUCT, TARGET> populator : populators)
			{

				if (populator != null)
				{
					populator.populate(getProduct(source), target);
				}
			}
		}
	}

	protected PRODUCT getProduct(final SOURCE source)
	{
		final String productCode = getValue(source, CODE);
		return (PRODUCT) getProductService().getProductForCode(productCode);
	}

	protected List<Populator<PRODUCT, TARGET>> getPopulatorsForCategories(final SOURCE source)
	{

		final List<Populator<PRODUCT, TARGET>> populators = Lists.newArrayList();

		final List<String> categoryCodes = getValue(source, ALL_CATEGORIES);

		final Map<String, Populator<PRODUCT, TARGET>> populatorMap = getCategoryProductInsurancePopulators();

		if (CollectionUtils.isNotEmpty(categoryCodes) && MapUtils.isNotEmpty(populatorMap))
		{

			for (final String categoryCode : categoryCodes)
			{
				if (populatorMap.containsKey(categoryCode))
				{
					populators.add(populatorMap.get(categoryCode));
				}

			}

		}

		return populators;
	}

	protected <T> T getValue(final SOURCE source, final String propertyName)
	{
		if (source.getValues() == null)
		{
			return null;
		}

		// DO NOT REMOVE the cast (T) below, while it should be unnecessary it is required by the javac compiler
		return (T) source.getValues().get(propertyName);
	}

	@Required
	protected Map<String, Populator<PRODUCT, TARGET>> getCategoryProductInsurancePopulators()
	{
		return categoryProductInsurancePopulators;
	}

	public void setCategoryProductInsurancePopulators(
			final Map<String, Populator<PRODUCT, TARGET>> categoryProductInsurancePopulators)
	{
		this.categoryProductInsurancePopulators = categoryProductInsurancePopulators;
	}

	protected ProductService getProductService()
	{
		return productService;
	}

	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

}
