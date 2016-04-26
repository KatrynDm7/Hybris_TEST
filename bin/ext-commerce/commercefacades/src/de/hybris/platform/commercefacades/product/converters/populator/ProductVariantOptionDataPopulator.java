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
package de.hybris.platform.commercefacades.product.converters.populator;

import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commercefacades.product.ProductVariantOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantMatrixElementData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.variants.model.GenericVariantProductModel;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Populates the {@link VariantOptionData} for all the {@link VariantMatrixElementData} in the tree by using a list of
 * populators.
 */
public class ProductVariantOptionDataPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		AbstractProductPopulator<SOURCE, TARGET>
{

	private ConfigurablePopulator<VariantProductModel, VariantOptionData, ProductVariantOption> variantOptionDataPopulator;
	private Collection<ProductVariantOption> productVariantOptionList;


	/**
	 * Populates the elements in tree with the information retrieved from the variant..
	 *
	 *
	 * @param productModel
	 *           the product to take the data from.
	 * @param productData
	 *           the data to put the data in.
	 * @throws de.hybris.platform.servicelayer.dto.converter.ConversionException
	 */
	@Override
	public void populate(final ProductModel productModel, final ProductData productData) throws ConversionException
	{

		final Collection<VariantProductModel> variants = getVariants(productModel);
		for (final VariantProductModel variant : variants)
		{
			populateNodes(productData.getVariantMatrix(), variant, productModel);
		}
		copyDataFromNodes(productData);
	}

	protected void copyDataFromNodes(final ProductData productData)
	{
		// set url from first variant into base product, to enable links to product details and order form
		if (CollectionUtils.isNotEmpty(productData.getVariantMatrix())
				&& productData.getVariantMatrix().get(0).getVariantOption() != null)
		{
			productData.setUrl(productData.getVariantMatrix().get(0).getVariantOption().getUrl());
		}
	}

	protected void populateNodes(final List<VariantMatrixElementData> parentNodeList, final VariantProductModel variant,
			final ProductModel originalVariant)
	{
		if (parentNodeList != null)
		{
			for (final VariantMatrixElementData parentNode : parentNodeList)
			{
				populateNodes(parentNode, variant);
			}
		}
	}

	protected void populateNodes(final VariantMatrixElementData parentNode, final VariantProductModel variant)
	{
		if (parentNode.getIsLeaf())
		{
			if (variant.getCode().equals(parentNode.getVariantOption().getCode()))
			{
				getVariantOptionDataPopulator().populate(variant, parentNode.getVariantOption(), getProductVariantOptionList());
			}
		}
		else
		{
			for (final VariantMatrixElementData childNode : parentNode.getElements())
			{
				populateNodes(childNode, variant);
			}
			copyData(parentNode);
		}
	}

	protected Collection<VariantProductModel> getVariants(final ProductModel productModel)
	{
		Collection<VariantProductModel> variants = Collections.<VariantProductModel> emptyList();

		if (CollectionUtils.isNotEmpty(productModel.getVariants()))
		{
			variants = productModel.getVariants();
		}
		else if (productModel instanceof GenericVariantProductModel)
		{
			variants = ((GenericVariantProductModel) productModel).getBaseProduct().getVariants();
		}

		return variants;
	}


	/**
	 * Copies the data from the element with the same code or from the first one on the list.
	 *
	 * @param parent
	 *           the element to copy the data into.
	 */
	protected void copyData(final VariantMatrixElementData parent)
	{

		if (CollectionUtils.isNotEmpty(parent.getElements()) && parent.getVariantOption() != null)
		{

			final VariantMatrixElementData elementToCopy = getRightElementToCopy(parent);

			parent.getVariantOption().setCode(elementToCopy.getVariantOption().getCode());
			parent.getVariantOption().setUrl(elementToCopy.getVariantOption().getUrl());
			parent.getVariantOption().setVariantOptionQualifiers(elementToCopy.getVariantOption().getVariantOptionQualifiers());
		}
	}

	/**
	 * Gets the element with the same code from the children and if not found returns the first.
	 *
	 * @param parent
	 *           the parent to look for the right child.
	 */
	protected VariantMatrixElementData getRightElementToCopy(final VariantMatrixElementData parent)
	{

		for (final VariantMatrixElementData child : parent.getElements())
		{
			if (parent.getVariantOption().getCode().equals(child.getVariantOption().getCode()))
			{
				return child;
			}
		}

		throw new IllegalStateException("One of the child elements must have the same code as the parent");
	}


	protected ConfigurablePopulator<VariantProductModel, VariantOptionData, ProductVariantOption> getVariantOptionDataPopulator()
	{
		return variantOptionDataPopulator;
	}

	@Required
	public void setVariantOptionDataPopulator(
			final ConfigurablePopulator<VariantProductModel, VariantOptionData, ProductVariantOption> variantOptionDataPopulator)
	{
		this.variantOptionDataPopulator = variantOptionDataPopulator;
	}


	public Collection<ProductVariantOption> getProductVariantOptionList()
	{
		return this.productVariantOptionList;
	}

	@Required
	public void setProductVariantOptionList(final Collection<ProductVariantOption> productVariantOptionList)
	{
		this.productVariantOptionList = productVariantOptionList;
	}
}