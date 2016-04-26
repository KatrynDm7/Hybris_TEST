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
package de.hybris.platform.b2bacceleratorservices.dao;

import de.hybris.platform.b2b.model.FutureStockModel;

import java.util.List;


/**
 * DAO for Future Stock.
 *
 */
public interface B2BFutureStockDao
{
	List<FutureStockModel> getFutureStocksByProductCode(final String productCode);
}
