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
package de.hybris.platform.commerceservices.price.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.price.CommercePriceService;
import de.hybris.platform.commerceservices.util.AbstractComparator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.product.PriceService;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 * Default implementation of {@link CommercePriceService}
 */
public class DefaultCommercePriceService implements CommercePriceService
{
	private PriceService priceService;

	@Override
	public PriceInformation getFromPriceForProduct(final ProductModel product)
	{
		validateParameterNotNull(product, "Product model cannot be null");
		if (CollectionUtils.isNotEmpty(product.getVariants()))
		{
			// Find the variant with the lowest price
			return getLowestVariantPrice(product);
		}

		// fallback to web price, if no variants are present
		return getWebPriceForProduct(product);
	}

	@Override
	public PriceInformation getWebPriceForProduct(final ProductModel product)
	{
		validateParameterNotNull(product, "Product model cannot be null");
		final List<PriceInformation> prices = getPriceService().getPriceInformationsForProduct(product);
		if (CollectionUtils.isNotEmpty(prices))
		{
			PriceInformation minPriceForLowestQuantity = null;
			for (final PriceInformation price : prices)
			{
				if (minPriceForLowestQuantity == null
						|| (((Long) minPriceForLowestQuantity.getQualifierValue("minqtd")).longValue() > ((Long) price
								.getQualifierValue("minqtd")).longValue()))
				{
					minPriceForLowestQuantity = price;
				}
			}
			return minPriceForLowestQuantity;
		}
		return null;
	}

	protected PriceInformation getLowestVariantPrice(final ProductModel product)
	{
		final Collection<PriceInformation> allVariantPrices = getAllVariantPrices(product);
		if (CollectionUtils.isNotEmpty(allVariantPrices))
		{
			return Collections.min(allVariantPrices, PriceInformationComparator.INSTANCE);
		}
		return null;
	}

	protected Collection<PriceInformation> getAllVariantPrices(final ProductModel product)
	{
		final Collection<PriceInformation> prices = new LinkedList<PriceInformation>();
		fillAllVariantPrices(product, prices);
		return prices;
	}

	protected void fillAllVariantPrices(final ProductModel product, final Collection<PriceInformation> prices)
	{
		// Get the price for the current product
		final PriceInformation priceInformation = getWebPriceForProduct(product);
		if (priceInformation != null)
		{
			prices.add(priceInformation);
		}

		final Collection<VariantProductModel> variants = product.getVariants();
		if (CollectionUtils.isNotEmpty(variants))
		{
			for (final VariantProductModel variant : variants)
			{
				fillAllVariantPrices(variant, prices);
			}
		}
	}

	protected PriceService getPriceService()
	{
		return priceService;
	}

	@Required
	public void setPriceService(final PriceService priceService)
	{
		this.priceService = priceService;
	}

	/**
	 * Comparator to naturally order PriceInformation by price value ascending
	 */
	public static class PriceInformationComparator extends AbstractComparator<PriceInformation>
	{
		public static final PriceInformationComparator INSTANCE = new PriceInformationComparator();

		@Override
		protected int compareInstances(final PriceInformation price1, final PriceInformation price2)
		{
			Assert.isTrue(price1.getPriceValue().getCurrencyIso().equals(price2.getPriceValue().getCurrencyIso()),
					"differing currency of web prices");

			return compareValues(price1.getPriceValue().getValue(), price2.getPriceValue().getValue());
		}
	}
}
