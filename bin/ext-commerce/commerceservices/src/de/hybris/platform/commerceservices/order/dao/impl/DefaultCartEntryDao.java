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
package de.hybris.platform.commerceservices.order.dao.impl;

import de.hybris.platform.commerceservices.order.dao.CartEntryDao;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.hybris.platform.servicelayer.util.ServicesUtil.*;


public class DefaultCartEntryDao extends AbstractItemDao implements CartEntryDao
{
	protected static final String CART_ENTRY_QUERY = "SELECT {" + CartEntryModel.PK + "} FROM {" + CartEntryModel._TYPECODE
			+ "} WHERE {" + CartEntryModel.ORDER + "} = ?cart AND {" + CartEntryModel.PRODUCT + "} = ?product";
	protected static final String CART_ENTRY_QUERY_POS = " AND {" + CartEntryModel.DELIVERYPOINTOFSERVICE + "} = ?pointOfService";
	protected static final String CART_ENTRY_QUERY_POS_NULL = " AND {" + CartEntryModel.DELIVERYPOINTOFSERVICE + "} IS NULL";

	@Override
	public List<CartEntryModel> findEntriesByProductAndPointOfService(final CartModel cart, final ProductModel product,
	                                                                  final PointOfServiceModel pointOfService)
	{
		validateParameterNotNull(cart, "cart must not be null");
		validateParameterNotNull(product, "product must not be null");

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("cart", cart);
		params.put("product", product);

		final StringBuffer query = new StringBuffer(CART_ENTRY_QUERY);

		if (pointOfService == null)
		{
			query.append(CART_ENTRY_QUERY_POS_NULL);
		}
		else
		{
			query.append(CART_ENTRY_QUERY_POS);
			params.put("pointOfService", pointOfService);
		}

		final SearchResult<CartEntryModel> res = getFlexibleSearchService().search(query.toString(), params);
		return res.getResult() == null ? Collections.EMPTY_LIST : res.getResult();
	}
}
