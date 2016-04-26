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
package de.hybris.platform.b2bacceleratorservices.futurestock.impl;

import de.hybris.platform.acceleratorservices.futurestock.FutureStockService;
import de.hybris.platform.b2b.model.FutureStockModel;
import de.hybris.platform.b2bacceleratorservices.dao.B2BFutureStockDao;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;


/**
 * Default B2B implementation for {@link FutureStockService}. Gets future availabilities for a product.
 */
public class DefaultB2BFutureStockService implements FutureStockService
{

	private B2BFutureStockDao futureStockDao;

	@Override
	public Map<String, Map<Date, Integer>> getFutureAvailability(final List<ProductModel> products)
	{
		final Map<String, Map<Date, Integer>> productsMap = new HashMap<>();

		for (final ProductModel product : products)
		{
			final List<FutureStockModel> futureStocks = futureStockDao.getFutureStocksByProductCode(product.getCode());
			if (!CollectionUtils.isEmpty(futureStocks))
			{
				final HashMap<Date, Integer> futureAvailability = new HashMap<>();
				for (final FutureStockModel futureStock : futureStocks)
				{
					futureAvailability.put(futureStock.getDate(), futureStock.getQuantity());
				}
				productsMap.put(product.getCode(), futureAvailability);
			}
		}

		return productsMap;
	}

	public B2BFutureStockDao getFutureStockDao()
	{
		return futureStockDao;
	}

	public void setFutureStockDao(final B2BFutureStockDao futureStockDao)
	{
		this.futureStockDao = futureStockDao;
	}

}
