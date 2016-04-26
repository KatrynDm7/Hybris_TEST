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
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.product.PriceService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.solrfacetsearch.provider.impl.ValueProviderParameterUtils;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Resolver for product prices.
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
 * <td>If false, indicates that every product must have a price (for every qualifier). An exception of type
 * {@link FieldValueProviderException} is thrown if a price cannot be resolved.
 * </table>
 * </blockquote>
 */
public class ProductPricesValueResolver extends AbstractValueResolver<ProductModel, Object, List<PriceInformation>>
{
	public static final String OPTIONAL_PARAM = "optional";
	public static final boolean OPTIONAL_PARAM_DEFAULT_VALUE = true;

	private PriceService priceService;

	public PriceService getPriceService()
	{
		return priceService;
	}

	@Required
	public void setPriceService(final PriceService priceService)
	{
		this.priceService = priceService;
	}

	@Override
	protected List<PriceInformation> loadQualifierData(final IndexerBatchContext batchContext,
			final Collection<IndexedProperty> indexedProperties, final ProductModel product, final Qualifier qualifier)
	{
		return loadPriceInformations(indexedProperties, product);
	}

	@Override
	protected void addFieldValues(final InputDocument document, final IndexerBatchContext batchContext,
			final IndexedProperty indexedProperty, final ProductModel model,
			final ValueResolverContext<Object, List<PriceInformation>> resolverContext) throws FieldValueProviderException
	{
		boolean hasPrice = false;

		final List<PriceInformation> priceInformations = resolverContext.getQualifierData();
		if (priceInformations != null)
		{
			final Double priceValue = getPriceValue(indexedProperty, priceInformations);
			if (priceValue != null)
			{
				hasPrice = true;
				document.addField(indexedProperty, priceValue, resolverContext.getFieldQualifier());
			}
		}

		if (!hasPrice)
		{
			final boolean isOptional = ValueProviderParameterUtils.getBoolean(indexedProperty, OPTIONAL_PARAM,
					OPTIONAL_PARAM_DEFAULT_VALUE);
			if (!isOptional)
			{
				throw new FieldValueProviderException("No value resolved for indexed property " + indexedProperty.getName());
			}
		}
	}

	protected List<PriceInformation> loadPriceInformations(final Collection<IndexedProperty> indexedProperties,
			final ProductModel product)
	{
		return priceService.getPriceInformationsForProduct(product);
	}

	protected Double getPriceValue(final IndexedProperty indexedProperty, final List<PriceInformation> priceInformations)
			throws FieldValueProviderException
	{
		Double value = null;

		if (priceInformations != null && !priceInformations.isEmpty())
		{
			value = Double.valueOf(priceInformations.get(0).getPriceValue().getValue());
		}

		return value;
	}
}
