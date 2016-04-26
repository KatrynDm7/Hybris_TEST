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
package de.hybris.platform.b2bacceleratorservices.dao.impl;

import de.hybris.platform.b2b.model.FutureStockModel;
import de.hybris.platform.b2bacceleratorservices.dao.B2BFutureStockDao;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Default implementation for {@link B2BFutureStockDao}.
 */
public class DefaultB2BFutureStockDao extends DefaultGenericDao<FutureStockModel> implements B2BFutureStockDao
{

	public DefaultB2BFutureStockDao()
	{
		super(FutureStockModel._TYPECODE);
	}

	@Override
	public List<FutureStockModel> getFutureStocksByProductCode(final String productCode)
	{
		final Map<String, String> params = new HashMap<>();
		params.put(FutureStockModel.PRODUCTCODE, productCode);
		return find(params);
	}

}
