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
package de.hybris.platform.commerceservices.product.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.commerceservices.product.CommerceProductReferenceService;
import de.hybris.platform.commerceservices.product.data.ReferenceData;
import de.hybris.platform.commerceservices.strategies.ProductReferenceTargetStrategy;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 *
 */
public class DefaultCommerceProductReferenceService implements
		CommerceProductReferenceService<ProductReferenceTypeEnum, ProductModel>
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultCommerceProductReferenceService.class);

	private ProductService productService;
	private ModelService modelService;
	private Map<ProductReferenceTypeEnum, ProductReferenceTargetStrategy> productReferenceTargetStrategies;
	private ProductReferenceTargetStrategy defaultProductReferenceTargetStrategy;

	protected ProductService getProductService()
	{
		return productService;
	}

	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected Map<ProductReferenceTypeEnum, ProductReferenceTargetStrategy> getProductReferenceTargetStrategies()
	{
		return productReferenceTargetStrategies;
	}

	@Required
	public void setProductReferenceTargetStrategies(
			final Map<ProductReferenceTypeEnum, ProductReferenceTargetStrategy> productReferenceTargetStrategies)
	{
		this.productReferenceTargetStrategies = productReferenceTargetStrategies;
	}

	protected ProductReferenceTargetStrategy getDefaultProductReferenceTargetStrategy()
	{
		return defaultProductReferenceTargetStrategy;
	}

	@Required
	public void setDefaultProductReferenceTargetStrategy(final ProductReferenceTargetStrategy defaultProductReferenceTargetStrategy)
	{
		this.defaultProductReferenceTargetStrategy = defaultProductReferenceTargetStrategy;
	}

	@Override
	public List<ReferenceData<ProductReferenceTypeEnum, ProductModel>> getProductReferencesForCode(final String code,
			final List<ProductReferenceTypeEnum> referenceTypes, final Integer limit)
	{
		validateParameterNotNull(code, "Parameter code must not be null");
		validateParameterNotNull(referenceTypes, "Parameter referenceType must not be null");

		final List<ReferenceData<ProductReferenceTypeEnum, ProductModel>> result = new ArrayList<ReferenceData<ProductReferenceTypeEnum, ProductModel>>();

		final ProductModel product = getProductService().getProductForCode(code);
		final List<ProductReferenceModel> references = getAllActiveProductReferencesFromSourceOfType(product, referenceTypes);
		if (references != null && !references.isEmpty())
		{
			for (final ProductReferenceModel reference : references)
			{
				final ProductModel targetProduct = resolveTarget(product, reference);

				final ReferenceData<ProductReferenceTypeEnum, ProductModel> referenceData = createReferenceData();
				referenceData.setTarget(targetProduct);
				referenceData.setDescription(reference.getDescription());
				referenceData.setQuantity(reference.getQuantity());
				referenceData.setReferenceType(reference.getReferenceType());
				result.add(referenceData);

				// Check the limit
				if (limit != null && result.size() >= limit.intValue())
				{
					break;
				}
			}
		}

		return result;
	}

	protected List<ProductReferenceModel> getAllActiveProductReferencesFromSourceOfType(final ProductModel product,
			final List<ProductReferenceTypeEnum> referenceTypes)
	{
		final Collection<ProductReferenceModel> allReferences = getProductReferencesForProduct(product);
		if (allReferences != null && !allReferences.isEmpty())
		{
			final Set<ProductModel> allSourceProducts = getAllBaseProducts(product);
			final List<ProductReferenceModel> matchingReferences = new ArrayList<ProductReferenceModel>();

			for (final ProductReferenceModel reference : allReferences)
			{
				if (reference != null && Boolean.TRUE.equals(reference.getActive())
						&& referenceTypes.contains(reference.getReferenceType()) && allSourceProducts.contains(reference.getSource()))
				{
					matchingReferences.add(reference);
				}
			}

			return matchingReferences;
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	@Deprecated
	public List<ReferenceData<ProductReferenceTypeEnum, ProductModel>> getProductReferencesForCode(final String code,
			final ProductReferenceTypeEnum referenceType, final Integer limit)
	{
		validateParameterNotNull(code, "Parameter code must not be null");
		validateParameterNotNull(referenceType, "Parameter referenceType must not be null");

		final List<ReferenceData<ProductReferenceTypeEnum, ProductModel>> result = new ArrayList<ReferenceData<ProductReferenceTypeEnum, ProductModel>>();

		final ProductModel product = getProductService().getProductForCode(code);
		final List<ProductReferenceModel> references = getAllActiveProductReferencesFromSourceOfType(product, referenceType);
		if (references != null && !references.isEmpty())
		{
			for (final ProductReferenceModel reference : references)
			{
				final ProductModel targetProduct = resolveTarget(product, reference);

				final ReferenceData<ProductReferenceTypeEnum, ProductModel> referenceData = createReferenceData();
				referenceData.setTarget(targetProduct);
				referenceData.setDescription(reference.getDescription());
				referenceData.setQuantity(reference.getQuantity());
				referenceData.setReferenceType(reference.getReferenceType());
				result.add(referenceData);

				// Check the limit
				if (limit != null && result.size() >= limit.intValue())
				{
					break;
				}
			}
		}

		return result;
	}

	protected List<ProductReferenceModel> getAllActiveProductReferencesFromSourceOfType(final ProductModel product,
			final ProductReferenceTypeEnum referenceType)
	{
		final Collection<ProductReferenceModel> allReferences = getProductReferencesForProduct(product);
		if (allReferences != null && !allReferences.isEmpty())
		{
			final Set<ProductModel> allSourceProducts = getAllBaseProducts(product);
			final List<ProductReferenceModel> matchingReferences = new ArrayList<ProductReferenceModel>();

			for (final ProductReferenceModel reference : allReferences)
			{
				if (reference != null && Boolean.TRUE.equals(reference.getActive())
						&& referenceType.equals(reference.getReferenceType()) && allSourceProducts.contains(reference.getSource()))
				{
					matchingReferences.add(reference);
				}
			}

			return matchingReferences;
		}
		return null;
	}

	protected Set<ProductModel> getAllBaseProducts(final ProductModel productModel)
	{
		final Set<ProductModel> allBaseProducts = new HashSet<ProductModel>();

		ProductModel currentProduct = productModel;
		allBaseProducts.add(currentProduct);

		while (currentProduct instanceof VariantProductModel)
		{
			currentProduct = ((VariantProductModel) currentProduct).getBaseProduct();

			if (currentProduct == null)
			{
				break;
			}
			else
			{
				allBaseProducts.add(currentProduct);
			}
		}
		return allBaseProducts;
	}

	protected Collection<ProductReferenceModel> getProductReferencesForProduct(final ProductModel product)
	{
		return (Collection<ProductReferenceModel>) getProductAttribute(product, ProductModel.PRODUCTREFERENCES);
	}

	protected ProductModel resolveTarget(final ProductModel sourceProduct, final ProductReferenceModel reference)
	{
		// Look for a strategy for the specific type of reference
		final Map<ProductReferenceTypeEnum, ProductReferenceTargetStrategy> strategiesMap = getProductReferenceTargetStrategies();
		if (strategiesMap != null)
		{
			final ProductReferenceTargetStrategy strategy = strategiesMap.get(reference.getReferenceType());
			if (strategy != null)
			{
				return resolveTarget(sourceProduct, reference, strategy);
			}
		}

		// Fallback to the default strategy
		return resolveTarget(sourceProduct, reference, getDefaultProductReferenceTargetStrategy());
	}

	protected ProductModel resolveTarget(final ProductModel sourceProduct, final ProductReferenceModel reference,
			final ProductReferenceTargetStrategy strategy)
	{
		final ProductModel target = strategy.getTarget(sourceProduct, reference);
		if (target != null)
		{
			return target;
		}
		return reference.getTarget();
	}

	/**
	 * Get an attribute value from a product. If the attribute value is null and the product is a variant then the same
	 * attribute will be requested from the base product.
	 * 
	 * @param product
	 *           the product
	 * @param attribute
	 *           the name of the attribute to lookup
	 * @return the value of the attribute
	 */
	protected Object getProductAttribute(final ProductModel product, final String attribute)
	{
		final Object value = getModelService().getAttributeValue(product, attribute);
		if (product instanceof VariantProductModel
				&& (value == null || (value instanceof Collection && ((Collection) value).isEmpty())))
		{
			final ProductModel baseProduct = ((VariantProductModel) product).getBaseProduct();
			if (baseProduct != null)
			{
				return getProductAttribute(baseProduct, attribute);
			}
		}
		return value;
	}

	protected ReferenceData<ProductReferenceTypeEnum, ProductModel> createReferenceData()
	{
		return new ReferenceData<ProductReferenceTypeEnum, ProductModel>();
	}
}
