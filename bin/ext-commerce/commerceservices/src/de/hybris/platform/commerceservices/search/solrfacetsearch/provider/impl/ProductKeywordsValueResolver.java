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

import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.solrfacetsearch.provider.impl.ValueProviderParameterUtils;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;


/**
 * Resolver for product keywords.
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
 * <td>If false, indicates that every product must have keywords (for every qualifier). An exception of type
 * {@link FieldValueProviderException} is thrown if the keywords cannot be resolved.
 * <tr valign=top>
 * <td>split
 * <td>true
 * <td>If false, the collected keywords should be added as a single value to the index.
 * <tr valign=top>
 * <td>separator
 * <td>" "
 * <td>The separator to use when combining the keywords into a single value. Only used if split is false.
 * </table>
 * </blockquote>
 */
public class ProductKeywordsValueResolver extends AbstractValueResolver<ProductModel, Object, Object>
{
	public static final String OPTIONAL_PARAM = "optional";
	public static final boolean OPTIONAL_PARAM_DEFAULT_VALUE = true;

	public static final String SPLIT_PARAM = "split";
	public static final boolean SPLIT_PARAM_DEFAULT_VALUE = true;

	public static final String SEPARATOR_PARAM = "separator";
	public static final String SEPARATOR_PARAM_DEFAULT_VALUE = " ";

	@Override
	protected void addFieldValues(final InputDocument document, final IndexerBatchContext batchContext,
			final IndexedProperty indexedProperty, final ProductModel product,
			final ValueResolverContext<Object, Object> resolverContext) throws FieldValueProviderException
	{
		boolean hasValue = false;

		final Set<String> keywords = new HashSet<String>();
		collectKeywords(keywords, product);

		if (!keywords.isEmpty())
		{
			final boolean split = ValueProviderParameterUtils.getBoolean(indexedProperty, SPLIT_PARAM, SPLIT_PARAM_DEFAULT_VALUE);

			if (split)
			{
				for (final String keyword : keywords)
				{
					if (!StringUtils.isBlank(keyword))
					{
						hasValue = true;
						document.addField(indexedProperty, keyword, resolverContext.getFieldQualifier());
					}
				}
			}
			else
			{
				final String separator = ValueProviderParameterUtils.getString(indexedProperty, SEPARATOR_PARAM,
						SEPARATOR_PARAM_DEFAULT_VALUE);
				final String value = combineKeywords(keywords, separator);

				if (!StringUtils.isBlank(value))
				{
					hasValue = true;
					document.addField(indexedProperty, value, resolverContext.getFieldQualifier());
				}
			}
		}

		if (!hasValue)
		{
			final boolean isOptional = ValueProviderParameterUtils.getBoolean(indexedProperty, OPTIONAL_PARAM,
					OPTIONAL_PARAM_DEFAULT_VALUE);
			if (!isOptional)
			{
				throw new FieldValueProviderException("No value resolved for indexed property " + indexedProperty.getName());
			}
		}
	}

	protected void collectKeywords(final Set<String> keywords, final ProductModel product)
	{
		if (product != null)
		{
			final List<KeywordModel> productKeywords = product.getKeywords();
			if (productKeywords != null && !productKeywords.isEmpty())
			{
				for (final KeywordModel keyword : productKeywords)
				{
					keywords.add(keyword.getKeyword());
				}
			}

			if (product instanceof VariantProductModel)
			{
				collectKeywords(keywords, ((VariantProductModel) product).getBaseProduct());
			}
		}
	}

	protected String combineKeywords(final Set<String> keywords, final String separator)
	{
		final StringBuilder buffer = new StringBuilder();

		boolean isFirst = true;
		for (final String keyword : keywords)
		{
			if (!isFirst)
			{
				buffer.append(separator);
			}

			isFirst = false;
			buffer.append(keyword);
		}

		return buffer.toString();
	}
}
