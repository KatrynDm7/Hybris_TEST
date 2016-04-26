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
package de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.product.VariantsService;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.impl.ModelAttributesValueResolver;
import de.hybris.platform.solrfacetsearch.provider.impl.ValueProviderParameterUtils;
import de.hybris.platform.variants.model.VariantProductModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * Resolver for product attributes. It takes into consideration variant product attributes (but not generic variant
 * attributes). If a value if not found for a variant it tries to get it from the base product. By default, if parameter
 * attribute is not specified, it tries to get the attribute with the same name as the one configured on the indexed
 * property.
 *
 * <h4>Supported parameters:</h4>
 *
 * <blockquote>
 * <table border=0 cellspacing=3 cellpadding=0 summary="Table showing supported parameters.">
 * <tr bgcolor="#ccccff">
 * <th>Parameter
 * <th>Default value
 * <th>Description
 * <tr valign=top>
 * <td>optional
 * <td>true
 * <td>If false, indicates that the resolved values should not be null and not an empty string (for every qualifier). If
 * these conditions are not met, an exception of type {@link FieldValueProviderException} is thrown.
 * <tr valign=top bgcolor="#eeeeff">
 * <td>attribute
 * <td>
 * <td>If specified, this is the name of the attribute.
 * <tr valign=top>
 * <td>split
 * <td>false
 * <td>If true, splits any resolved value around matches of a regular expression (only if the value is of type String).
 * <tr valign=top bgcolor="#eeeeff">
 * <td>splitRegex
 * <td>\s+
 * <td>If split is true this is the regular expression to use.
 * <tr valign=top>
 * <td>skipVariants
 * <td>
 * <td>If true, it ignores product variants and gets the values from the base product.
 * <tr valign=top bgcolor="#eeeeff">
 * <td>format
 * <td>null
 * <td>The ID of the Format Bean that is going to be used to format the attribute value object before applying the split
 * </table>
 * </blockquote>
 */
public class ProductAttributesValueResolver extends ModelAttributesValueResolver<ProductModel>
{
	public static final String SKIP_VARIANTS_PARAM = "skipVariants";
	public static final boolean SKIP_VARIANTS_PARAM_DEFAULT_VALUE = false;

	private VariantsService variantsService;

	public VariantsService getVariantsService()
	{
		return variantsService;
	}

	@Required
	public void setVariantsService(final VariantsService variantsService)
	{
		this.variantsService = variantsService;
	}

	@Override
	protected Object getAttributeValue(final IndexedProperty indexedProperty, final ProductModel product,
			final String attributeName) throws FieldValueProviderException
	{
		Object value = null;

		final boolean skipVariants = ValueProviderParameterUtils.getBoolean(indexedProperty, SKIP_VARIANTS_PARAM,
				SKIP_VARIANTS_PARAM_DEFAULT_VALUE);
		if (skipVariants)
		{
			ProductModel baseProduct = product;

			while (baseProduct instanceof VariantProductModel)
			{
				baseProduct = ((VariantProductModel) baseProduct).getBaseProduct();
			}

			if (baseProduct != null)
			{
				value = getModelAttributeValue(baseProduct, attributeName);
			}
		}
		else
		{
			ProductModel baseProduct = product;

			while ((value == null) && (baseProduct != null))
			{
				value = getModelAttributeValue(baseProduct, attributeName);

				if (baseProduct instanceof VariantProductModel)
				{
					if (value == null)
					{
						// maybe the attribute is not part of the model
						value = getVariantProductAttributeValue((VariantProductModel) product, attributeName);
					}

					baseProduct = ((VariantProductModel) baseProduct).getBaseProduct();
				}
				else
				{
					baseProduct = null;
				}
			}
		}

		return value;
	}

	protected Object getModelAttributeValue(final ProductModel product, final String attributeName)
	{
		Object value = null;

		final ComposedTypeModel composedType = getTypeService().getComposedTypeForClass(product.getClass());
		if (getTypeService().hasAttribute(composedType, attributeName))
		{
			value = getModelService().getAttributeValue(product, attributeName);
		}

		return value;
	}

	protected Object getVariantProductAttributeValue(final VariantProductModel variantProduct, final String attributeName)
	{
		try
		{
			return variantsService.getVariantAttributeValue(variantProduct, attributeName);
		}
		catch (final SystemException e)
		{
			// attribute could not be read
			return null;
		}
	}
}
