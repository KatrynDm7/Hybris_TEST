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

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.ExpressionEvaluator;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.solrfacetsearch.provider.impl.ValueProviderParameterUtils;

import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Resolver that gets the product promotion values from attributes on the promotion model. By default, if parameter attribute is
 * not
 * specified, it
 * tries to get the attribute with the same name as the one configured on the indexed property.
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
 * <td>evaluateExpression
 * <td>false
 * <td>If true the attribute name is assumed to be a spring expression language that need to be evaluated
 * </table>
 * </blockquote>
 */
public class ProductPromotionAttributesValueResolver
		extends AbstractValueResolver<ProductModel, Collection<ProductPromotionModel>, Object>
{
	public static final String OPTIONAL_PARAM = "optional";
	public static final boolean OPTIONAL_PARAM_DEFAULT_VALUE = true;

	public static final String ATTRIBUTE_PARAM = "attribute";
	public static final String ATTRIBUTE_PARAM_DEFAULT_VALUE = null;

	public static final String EVALUATE_EXPRESSION_PARAM = "evaluateExpression";
	public static final boolean EVALUATE_EXPRESSION_PARAM_DEFAULT_VALUE = false;

	private PromotionsService promotionsService;
	private TypeService typeService;
	private ModelService modelService;

	private ExpressionEvaluator expressionEvaluator;

	public PromotionsService getPromotionsService()
	{
		return promotionsService;
	}

	@Required
	public void setPromotionsService(final PromotionsService promotionsService)
	{
		this.promotionsService = promotionsService;
	}

	public ExpressionEvaluator getExpressionEvaluator()
	{
		return expressionEvaluator;
	}

	@Required
	public void setExpressionEvaluator(final ExpressionEvaluator expressionEvaluator)
	{
		this.expressionEvaluator = expressionEvaluator;
	}

	public TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	protected void addFieldValues(final InputDocument document, final IndexerBatchContext batchContext,
			final IndexedProperty indexedProperty, final ProductModel model,
			final ValueResolverContext<Collection<ProductPromotionModel>, Object> resolverContext)
			throws FieldValueProviderException
	{
		boolean hasPromotion = false;

		final Collection<ProductPromotionModel> productPromotionModels = resolverContext.getData();

		if (productPromotionModels != null)
		{
			final String attributeName = getAttributeName(indexedProperty);
			final boolean isMultiValue = indexedProperty.isMultiValue();

			for (final ProductPromotionModel promotion : productPromotionModels)
			{
				final Object attributeValue = getAttributeValue(indexedProperty, batchContext, promotion, attributeName);
				hasPromotion = filterAndAddFieldValues(document, batchContext, indexedProperty, attributeValue,
						resolverContext.getFieldQualifier());

				//if not multi value stop the loop after the first iteration
				if (!isMultiValue)
				{
					break;
				}
			}
		}

		if (!hasPromotion)
		{
			final boolean isOptional = ValueProviderParameterUtils.getBoolean(indexedProperty, OPTIONAL_PARAM,
					OPTIONAL_PARAM_DEFAULT_VALUE);
			if (!isOptional)
			{
				throw new FieldValueProviderException("No value resolved for indexed property " + indexedProperty.getName());
			}
		}

	}

	protected String getAttributeName(final IndexedProperty indexedProperty)
	{
		String attributeName = ValueProviderParameterUtils.getString(indexedProperty, ATTRIBUTE_PARAM,
				ATTRIBUTE_PARAM_DEFAULT_VALUE);

		if (attributeName == null)
		{
			attributeName = indexedProperty.getName();
		}

		return attributeName;
	}

	protected Object getAttributeValue(final IndexedProperty indexedProperty, final IndexerBatchContext batchContext, final
	ProductPromotionModel promotionModel,
			final String
					attributeName)
			throws FieldValueProviderException
	{
		Object value = null;

		if (StringUtils.isNotEmpty(attributeName))
		{
			final boolean evaluateExpression = ValueProviderParameterUtils.getBoolean(indexedProperty, EVALUATE_EXPRESSION_PARAM,
					EVALUATE_EXPRESSION_PARAM_DEFAULT_VALUE);
			if (evaluateExpression)
			{
				value = expressionEvaluator.evaluate(attributeName, promotionModel);
			}
			else
			{
				final ComposedTypeModel composedType = typeService.getComposedTypeForClass(promotionModel.getClass());

				if (typeService.hasAttribute(composedType, attributeName))
				{
					value = modelService.getAttributeValue(promotionModel, attributeName);
				}
			}
		}

		return value;
	}

	@Override
	protected Collection<ProductPromotionModel> loadData(final IndexerBatchContext batchContext,
			final Collection<IndexedProperty> indexedProperties, final ProductModel model) throws FieldValueProviderException
	{
		final BaseSiteModel baseSiteModel = batchContext.getFacetSearchConfig().getIndexConfig().getBaseSite();

		if (baseSiteModel != null && baseSiteModel.getDefaultPromotionGroup() != null)
		{
			return promotionsService
					.getProductPromotions(Collections.singletonList(baseSiteModel.getDefaultPromotionGroup()), model);
		}

		return null;
	}
}
