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
package de.hybris.platform.commerceservices.order.dao;

import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.List;


public interface CartEntryDao extends Dao
{
	List<CartEntryModel> findEntriesByProductAndPointOfService(CartModel cart, ProductModel product,
	                                                           PointOfServiceModel pointOfService);
}
