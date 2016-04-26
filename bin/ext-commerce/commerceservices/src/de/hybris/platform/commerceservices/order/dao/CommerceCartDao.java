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

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.Date;
import java.util.List;


public interface CommerceCartDao extends Dao
{
	CartModel getCartForGuidAndSiteAndUser(String guid, BaseSiteModel site, UserModel user);

	CartModel getCartForGuidAndSite(String guid, BaseSiteModel site);

	CartModel getCartForCodeAndUser(String code, UserModel user);

	CartModel getCartForSiteAndUser(BaseSiteModel site, UserModel user);

	List<CartModel> getCartsForSiteAndUser(BaseSiteModel site, UserModel user);

	List<CartModel> getCartsForRemovalForSiteAndUser(Date modifiedBefore, BaseSiteModel site, UserModel user);
}
