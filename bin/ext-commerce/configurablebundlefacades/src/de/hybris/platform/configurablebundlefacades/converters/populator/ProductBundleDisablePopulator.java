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
package de.hybris.platform.configurablebundlefacades.converters.populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.configurablebundlefacades.data.BundleTemplateData;
import de.hybris.platform.configurablebundleservices.model.AbstractBundleRuleModel;
import de.hybris.platform.configurablebundleservices.model.DisableProductBundleRuleModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;


/**
 * ProductBundleDisablePopulator to populate product visibility information based on DisableProductBundleRule.
 * 
 * @param <SOURCE>
 *           ProductModel
 * @param <TARGET>
 *           ProductData
 */
public class ProductBundleDisablePopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		AbstractProductPopulator<SOURCE, TARGET>
{
	private static final boolean DISABLE_PRODUCT = true;

	protected enum TargetType
	{
		TARGET, SOURCE
	}

	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		//In the case of a DisableProductBundleRule the role of target and conditional products 
		//changes based on the cart product that belongs to (ex. if the product in the cart belongs to conditional list then
		// the rule apples on target list and vice versa) there is no difference, but you just need one rule to mark a target and conditional product as disabled.

		if (productModel != null && productModel.getTargetBundleRules() != null && productModel.getConditionalBundleRules() != null
				&& productData != null && productData.getBundleTemplates() != null)
		{
			//Get the  disable rule from the given Product.targetBundleRules
			Collection<DisableProductBundleRuleModel> disableRules = getDisableProductBundleRules(productModel.getTargetBundleRules());

			//extract the product code which are eligible to apply disable rule.
			final Collection<String> disableProduct = getDisableProducts(disableRules, TargetType.SOURCE);

			//Get the  disable rule from the given Product.conditionalBundleRules
			disableRules = getDisableProductBundleRules(productModel.getConditionalBundleRules());

			//consolidate the disable product list which we got it from ConditionalBundleRules
			disableProduct.addAll(getDisableProducts(disableRules, TargetType.TARGET));

			if (CollectionUtils.isNotEmpty(disableProduct))
			{
				//get all the bundle components of the package (in which the product belongs to)
				for (final BundleTemplateData bundleData : productData.getBundleTemplates())
				{
					disableProducts(bundleData, disableProduct);
				}
			}
		}
	}

	/**
	 * Method to extract the product code that are eligible to apply disable rule
	 * 
	 * @param disableRules
	 *           DisableProductBundleRuleModel
	 * @param targetType
	 *           TargetType
	 * @return List of product code as string.
	 */
	protected Collection<String> getDisableProducts(final Collection<DisableProductBundleRuleModel> disableRules,
			final TargetType targetType)
	{
		final Collection<String> disableProductList = new ArrayList<>();

		for (final DisableProductBundleRuleModel rule : disableRules)
		{
			if (TargetType.TARGET == targetType)
			{
				disableProductList.addAll(extractProductCode(rule.getTargetProducts()));
			}
			else if (TargetType.SOURCE == targetType)
			{
				disableProductList.addAll(extractProductCode(rule.getConditionalProducts()));
			}
		}

		return disableProductList;
	}


	/**
	 * Helper method to extract the product code from the given list of product.
	 * 
	 * @param productList
	 * @return List of product code as string.
	 */
	private Collection<String> extractProductCode(final Collection<ProductModel> productList)
	{
		final Collection<String> disableProductList = new ArrayList<>();

		for (final ProductModel product : productList)
		{
			disableProductList.add(product.getCode());
		}

		return disableProductList;
	}

	/**
	 * Helper method used to filter out the BundleRules to get all the DisableProductBundleRules from the given
	 * AbstractBundleRuleModel.
	 * 
	 * @param rules
	 *           AbstractBundleRuleModel which holds all the rules related to the bundle.
	 * @return List of DisableProductBundleRuleModel
	 */
	protected Collection<DisableProductBundleRuleModel> getDisableProductBundleRules(
			final Collection<AbstractBundleRuleModel> rules)
	{
		final Collection<DisableProductBundleRuleModel> disableRuleList = new ArrayList<>();

		for (final AbstractBundleRuleModel rule : rules)
		{
			if (rule instanceof DisableProductBundleRuleModel)
			{
				disableRuleList.add((DisableProductBundleRuleModel) rule);
			}
		}

		return disableRuleList;
	}

	/**
	 * Method which encapsulates the logic to populate the product visibility information if the given product is the
	 * part of {@link DisableProductBundleRuleModel#getTargetProducts()} or
	 * {@link DisableProductBundleRuleModel#getConditionalProducts()}
	 * 
	 * @param data
	 *           BundleData belongs to the product
	 * @param disableProduct
	 *           List of product code that needs to be disabled
	 */
	protected void disableProducts(final BundleTemplateData data, final Collection<String> disableProduct)
	{
		validateParameterNotNullStandardMessage("data", data);

		if (data.getProducts() != null)
		{
			for (final String product : disableProduct)
			{
				for (final ProductData productData : data.getProducts())
				{
					if (StringUtils.equals(productData.getCode(), product))
					{
						productData.setDisabled(DISABLE_PRODUCT);
						break;
					}

				}
			}
		}
	}

}
