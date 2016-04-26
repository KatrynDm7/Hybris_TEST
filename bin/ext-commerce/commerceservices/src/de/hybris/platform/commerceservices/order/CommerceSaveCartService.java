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
package de.hybris.platform.commerceservices.order;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;

import java.util.List;


/**
 * Commerce service that extends the interface {@link CommerceCartService} to expose methods to deal with operations for
 * saved carts (saving, retrieving, restoring, etc).
 */
public interface CommerceSaveCartService
{
	/**
	 * Method for explicitly saving a cart along with additional parameters
	 *
	 * @param parameters
	 *           {@link CommerceSaveCartParameter} parameter object that holds the cart to be saved along with some
	 *           additional details such as a name and a description for this cart
	 * @return {@link CommerceSaveCartResult}
	 * @throws CommerceSaveCartException
	 *            if cart cannot be saved
	 */
	CommerceSaveCartResult saveCart(CommerceSaveCartParameter parameters) throws CommerceSaveCartException;

	/**
	 * Method for explicitly flagging a cart for deletion
	 *
	 * @param parameters
	 *           {@link CommerceSaveCartParameter} parameter object that holds the cart to be flagged for deletion
	 * @return {@link CommerceSaveCartResult}
	 * @throws CommerceSaveCartException
	 *            if cart cannot be flagged for deletion
	 */
	CommerceSaveCartResult flagForDeletion(CommerceSaveCartParameter parameters) throws CommerceSaveCartException;

	/**
	 * Method for explicitly restoring a cart using cart code
	 *
	 * @param parameters
	 *           {@link CommerceSaveCartParameter} parameter object that holds the cart code to be restored
	 * @return {@link CommerceCartRestoration}
	 * @throws CommerceSaveCartException
	 *            if cart cannot be restored
	 */
	CommerceCartRestoration restoreSavedCart(CommerceSaveCartParameter parameters) throws CommerceSaveCartException;

	/**
	 * Retrieve carts where order status equals ones of the status in the list.
	 *
	 * @param pageableData
	 * @param baseSite
	 * @param user
	 * @param orderStatus
	 * @return list of saved user carts
	 */
	SearchPageData<CartModel> getSavedCartsForSiteAndUser(PageableData pageableData, BaseSiteModel baseSite, UserModel user,
			List<OrderStatus> orderStatus);

	/**
	 * For a given save cart create a copy and store it.
	 *
	 * @param parameter
	 *           {@link CommerceSaveCartParameter} parameter object that holds the cart code to be cloned
	 * @return {@link CommerceSaveCartResult}
	 * @throws CommerceSaveCartException
	 */
	CommerceSaveCartResult cloneSavedCart(CommerceSaveCartParameter parameter) throws CommerceSaveCartException;

}
