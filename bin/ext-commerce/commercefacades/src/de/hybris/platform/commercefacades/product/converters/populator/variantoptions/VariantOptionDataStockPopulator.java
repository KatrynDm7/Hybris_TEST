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
package de.hybris.platform.commercefacades.product.converters.populator.variantoptions;

import de.hybris.platform.commercefacades.product.converters.populator.StockPopulator;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.variants.model.VariantProductModel;

import org.springframework.beans.factory.annotation.Required;


public class VariantOptionDataStockPopulator<SOURCE extends VariantProductModel, TARGET extends VariantOptionData> implements
		Populator<SOURCE, TARGET>
{

	private StockPopulator<ProductModel, StockData> stockPopulator;

	@Override
	public void populate(final VariantProductModel variantProductModel, final VariantOptionData variantOptionData)
			throws ConversionException
	{
		long stockLevel = 0;
		for (final StockLevelModel stockLevelModel : variantProductModel.getStockLevels())
		{
			stockLevel += stockLevelModel.getAvailable();
		}

		final StockData stock = new StockData();
		stock.setStockLevel(stockLevel);

		stockPopulator.populate(variantProductModel, stock);

		variantOptionData.setStock(stock);
	}

	public StockPopulator getStockPopulator()
	{
		return stockPopulator;
	}

	@Required
	public void setStockPopulator(final StockPopulator stockPopulator)
	{
		this.stockPopulator = stockPopulator;
	}

}