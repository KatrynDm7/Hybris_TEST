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
package de.hybris.platform.commercefacades.order.converters.populator;

import de.hybris.platform.acceleratorfacades.order.data.PriceRangeData;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Groups multiple {@link OrderEntryData} as one entry in a {@link AbstractOrderData) based on the multidimensional
 * variants that share the same base product. All non multidimensional product entries will be leaved unmodified as a
 * single entry.
 */
public class GroupOrderEntryPopulator implements Populator<AbstractOrderModel, AbstractOrderData>
{

	public static final String VARIANT_TYPE = "GenericVariantProduct";
	public static final Integer INVALID_ENTRY_NUMBER = Integer.valueOf(-1);
	public static final Long ZERO_QUANTITY = Long.valueOf(0L);

	private ProductService productService;
	private PriceDataFactory priceDataFactory;

	@Override
	public void populate(final AbstractOrderModel source, final AbstractOrderData target) throws ConversionException
	{

		target.setEntries(groupEntries(target.getEntries()));

	}

	protected List<OrderEntryData> groupEntries(final List<OrderEntryData> entries)
	{
		final Map<String, OrderEntryData> group = new HashMap<>();
		final List<OrderEntryData> allEntries = new ArrayList<OrderEntryData>();


		boolean anyGroup = false;

		for (final OrderEntryData entry : entries)
		{
			final ProductData product = entry.getProduct();

			if (isGroupable(product))
			{
				anyGroup = true;
				if (!group.containsKey(product.getBaseProduct()))
				{
					group.put(product.getBaseProduct(), createGroupedOrderEntry(entry));
				}

				group.get(product.getBaseProduct()).getEntries().add(entry);

			}
			else
			{
				allEntries.add(entry);
			}

		}

		if (anyGroup)
		{
			consolidateGroupedOrderEntry(group);
			allEntries.addAll(group.values());
		}

		return allEntries;
	}

	// This should be replaced by product.mutidimentional but at this stage is not yet populated
	// Only works for multidimentional products
	protected boolean isGroupable(final ProductData product)
	{
		return product.getBaseProduct() != null && CollectionUtils.isNotEmpty(product.getBaseOptions())
				&& VARIANT_TYPE.equalsIgnoreCase(product.getBaseOptions().get(0).getVariantType());
	}

	protected void consolidateGroupedOrderEntry(final Map<String, OrderEntryData> group)
	{

		for (final String productCode : group.keySet())
		{
			final OrderEntryData parentEntry = group.get(productCode);
			if (parentEntry.getEntries() != null)
			{
				final PriceData firstEntryTotalPrice = parentEntry.getEntries().get(0).getTotalPrice();
				final PriceRangeData priceRange = parentEntry.getProduct().getPriceRange();

				if (firstEntryTotalPrice != null)
				{
					priceRange.setMaxPrice(getMaxPrice(parentEntry, firstEntryTotalPrice));
					priceRange.setMinPrice(getMinPrice(parentEntry, firstEntryTotalPrice));
					parentEntry.setTotalPrice(getTotalPrice(parentEntry, firstEntryTotalPrice));
				}

				parentEntry.setQuantity(getTotalQuantity(parentEntry));
			}

		}
	}

	protected PriceData getMaxPrice(final OrderEntryData parentEntry, final PriceData samplePrice)
	{
		long newMaxPrice = 0;

		for (final OrderEntryData childEntry : parentEntry.getEntries())
		{
			if (isNotEmptyPrice(childEntry.getBasePrice()))
			{
				final long basePriceValue = childEntry.getBasePrice().getValue().longValue();
				if (basePriceValue > newMaxPrice)
				{
					newMaxPrice = basePriceValue;
				}
			}
		}

		return buildPrice(samplePrice, newMaxPrice);
	}

	protected PriceData getMinPrice(final OrderEntryData parentEntry, final PriceData samplePrice)
	{
		long newMinPrice = Long.MAX_VALUE;

		for (final OrderEntryData childEntry : parentEntry.getEntries())
		{
			if (isNotEmptyPrice(childEntry.getBasePrice()))
			{
				final long basePriceValue = childEntry.getBasePrice().getValue().longValue();
				if (basePriceValue < newMinPrice)
				{
					newMinPrice = basePriceValue;
				}
			}
		}

		return buildPrice(samplePrice, newMinPrice);
	}

	private boolean isNotEmptyPrice(final PriceData price)
	{
		return price != null && price.getValue() != null;
	}


	protected PriceData getTotalPrice(final OrderEntryData parentEntry, final PriceData samplePrice)
	{
		long newTotalPrice = 0;

		for (final OrderEntryData childEntry : parentEntry.getEntries())
		{
			if (isNotEmptyPrice(childEntry.getBasePrice()))
			{
				newTotalPrice += childEntry.getTotalPrice().getValue().longValue();
			}
		}

		return buildPrice(samplePrice, newTotalPrice);
	}

	protected Long getTotalQuantity(final OrderEntryData parentEntry)
	{

		long totalQuantity = 0;
		for (final OrderEntryData childEntry : parentEntry.getEntries())
		{
			totalQuantity += (childEntry.getQuantity() != null ? childEntry.getQuantity().longValue() : 0);
		}


		return Long.valueOf(totalQuantity);
	}




	protected OrderEntryData createGroupedOrderEntry(final OrderEntryData firstEntry)
	{

		final OrderEntryData groupedEntry = new OrderEntryData();
		groupedEntry.setEntries(new ArrayList<OrderEntryData>());
		groupedEntry.setEntryNumber(INVALID_ENTRY_NUMBER);

		groupedEntry.setQuantity(ZERO_QUANTITY);

		final ProductData baseProduct = createBaseProduct(firstEntry.getProduct());
		groupedEntry.setProduct(baseProduct);

		groupedEntry.setUpdateable(firstEntry.isUpdateable());
		groupedEntry.setBasePrice(firstEntry.getBasePrice());

		return groupedEntry;
	}

	protected ProductData createBaseProduct(final ProductData variant)
	{

		final ProductData productData = new ProductData();

		productData.setUrl(variant.getUrl());
		productData.setPurchasable(variant.getPurchasable());
		productData.setMultidimensional(Boolean.TRUE);
		productData.setImages(variant.getImages());

		final ProductModel productModel = productService.getProductForCode(variant.getBaseProduct());
		productData.setCode(productModel.getCode());
		productData.setName(productModel.getName());
		productData.setDescription(productModel.getDescription());

		productData.setPriceRange(new PriceRangeData());


		return productData;

	}

	protected PriceData buildPrice(final PriceData base, final long amount)
	{
		return getPriceDataFactory().create(base.getPriceType(), BigDecimal.valueOf(amount), base.getCurrencyIso());
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

	protected PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}

	@Required
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}
}
