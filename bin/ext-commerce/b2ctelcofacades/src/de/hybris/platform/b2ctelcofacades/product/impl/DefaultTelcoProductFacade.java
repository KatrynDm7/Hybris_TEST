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
package de.hybris.platform.b2ctelcofacades.product.impl;

import de.hybris.platform.b2ctelcofacades.product.TelcoProductFacade;
import de.hybris.platform.b2ctelcoservices.model.AccessoryModel;
import de.hybris.platform.b2ctelcoservices.services.CompatibilityService;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;
import de.hybris.platform.commercefacades.product.data.ReviewData;
import de.hybris.platform.configurablebundleservices.bundle.BundleRuleService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of Telco Product Facade {@link TelcoProductFacade}.
 */
public class DefaultTelcoProductFacade implements TelcoProductFacade
{
	private CompatibilityService compatibilityService;

	private BundleRuleService bundleRuleService;

	private ProductFacade productFacade;

	@Override
	public List<ProductData> getProductReferencesAndFeatureCompatibleProductsForCode(final String code,
			final List<ProductReferenceTypeEnum> referenceTypes, final List<ProductOption> options, final Integer limit,
			final ClassAttributeAssignmentModel classAttributeAssignment, final ComposedTypeModel targetItemType)
	{
		final List<ProductData> productData = new ArrayList<ProductData>();
		int currentCount = 0;

		final List<ProductReferenceData> productReferences = getProductFacade().getProductReferencesForCode(code, referenceTypes,
				options, limit);

		currentCount += productReferences.size();
		for (final ProductReferenceData prdata : productReferences)
		{
			productData.add(prdata.getTarget());
		}
		if (limit != null && currentCount < limit.intValue())
		{
			final List<ProductModel> featureCompatibleProducts = compatibilityService.getFeatureCompatibleProducts(code,
					classAttributeAssignment, targetItemType);

			for (final ProductModel product : featureCompatibleProducts)
			{
				if (limit.intValue() <= currentCount)
				{
					break;
				}
				final ProductData tempProduct = getProductFacade().getProductForOptions(product, options);
				if (!contains(productData, tempProduct))
				{
					productData.add(tempProduct);
					currentCount++;
				}
			}
		}
		return productData;

	}

	@Override
	public List<ProductData> getProductReferencesAndFeatureCompatibleAndVendorCompatibleProductsForCode(final String code,
			final List<ProductReferenceTypeEnum> referenceTypes, final List<ProductOption> options, final Integer limit,
			final ClassAttributeAssignmentModel classAttributeAssigment, final ComposedTypeModel targetItemType)
	{
		final List<ProductData> productData = this.getProductReferencesAndFeatureCompatibleProductsForCode(code, referenceTypes,
				options, limit, classAttributeAssigment, targetItemType);

		int currentCount = productData.size();
		if (limit != null && currentCount < limit.intValue())
		{
			final List<ProductModel> vendorCompatibleProducts = compatibilityService.getAccessoriesForVendorCompatibility(code,
					AccessoryModel._TYPECODE);
			for (final ProductModel product : vendorCompatibleProducts)
			{

				if (limit.intValue() <= currentCount)
				{
					break;
				}
				final ProductData tempProduct = getProductFacade().getProductForOptions(product, options);
				if (!contains(productData, tempProduct))
				{
					productData.add(tempProduct);
					currentCount++;
				}
			}
		}
		return productData;
	}

	/**
	 * Check if the productData is present in list.
	 *
	 * @param productData
	 *           list of existing product data items
	 * @param newProduct
	 *           product data item to be added
	 * @return true if newProduct exists in productData
	 */
	protected boolean contains(final List<ProductData> productData, final ProductData newProduct)
	{
		final Object exists = CollectionUtils.find(productData, new Predicate()
		{

			@Override
			public boolean evaluate(final Object productDataObj)
			{
				final ProductData existingProductData = (ProductData) productDataObj;
				return existingProductData.getCode().equals(newProduct.getCode());
			}
		});
		if (exists != null)
		{
			return true;
		}
		return false;
	}

	protected CompatibilityService getCompatibilityService()
	{
		return compatibilityService;
	}

	@Required
	public void setCompatibilityService(final CompatibilityService compatibilityService)
	{

		this.compatibilityService = compatibilityService;
	}

	protected BundleRuleService getBundleRuleService()
	{
		return bundleRuleService;
	}


	@Required
	public void setBundleRuleService(final BundleRuleService bundleRuleService)
	{
		this.bundleRuleService = bundleRuleService;
	}

	/**
	 * @return the productFacade
	 */
	public ProductFacade getProductFacade()
	{
		return productFacade;
	}

	/**
	 * @param productFacade
	 *           the productFacade to set
	 */
	public void setProductFacade(final ProductFacade productFacade)
	{
		this.productFacade = productFacade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.commercefacades.product.ProductFacade#getProductForOptions(de.hybris.platform.core.model.product
	 * .ProductModel, java.util.Collection)
	 */
	@Override
	public ProductData getProductForOptions(final ProductModel productModel, final Collection<ProductOption> options)
	{

		return getProductFacade().getProductForOptions(productModel, options);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.product.ProductFacade#getProductForCodeAndOptions(java.lang.String,
	 * java.util.Collection)
	 */
	@Override
	public ProductData getProductForCodeAndOptions(final String code, final Collection<ProductOption> options)
			throws UnknownIdentifierException, IllegalArgumentException
	{
		return getProductFacade().getProductForCodeAndOptions(code, options);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.product.ProductFacade#postReview(java.lang.String,
	 * de.hybris.platform.commercefacades.product.data.ReviewData)
	 */
	@Override
	public ReviewData postReview(final String productCode, final ReviewData reviewData) throws UnknownIdentifierException,
			IllegalArgumentException
	{

		return getProductFacade().postReview(productCode, reviewData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.product.ProductFacade#getReviews(java.lang.String)
	 */
	@Override
	public List<ReviewData> getReviews(final String productCode) throws UnknownIdentifierException
	{

		return getProductFacade().getReviews(productCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.product.ProductFacade#getReviews(java.lang.String, java.lang.Integer)
	 */
	@Override
	public List<ReviewData> getReviews(final String productCode, final Integer numberOfReviews) throws UnknownIdentifierException,
			IllegalArgumentException
	{
		return getProductFacade().getReviews(productCode, numberOfReviews);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.product.ProductFacade#getProductReferencesForCode(java.lang.String,
	 * de.hybris.platform.catalog.enums.ProductReferenceTypeEnum, java.util.List, java.lang.Integer)
	 */
	@Deprecated
	@Override
	public List<ProductReferenceData> getProductReferencesForCode(final String code, final ProductReferenceTypeEnum referenceType,
			final List<ProductOption> options, final Integer limit)
	{
		return getProductFacade().getProductReferencesForCode(code, referenceType, options, limit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.product.ProductFacade#getProductReferencesForCode(java.lang.String,
	 * java.util.List, java.util.List, java.lang.Integer)
	 */
	@Override
	public List<ProductReferenceData> getProductReferencesForCode(final String code,
			final List<ProductReferenceTypeEnum> referenceTypes, final List<ProductOption> options, final Integer limit)
	{
		return getProductFacade().getProductReferencesForCode(code, referenceTypes, options, limit);
	}

}
