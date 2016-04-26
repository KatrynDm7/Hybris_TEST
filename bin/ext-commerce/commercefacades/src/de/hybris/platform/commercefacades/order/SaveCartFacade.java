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
package de.hybris.platform.commercefacades.order;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartRestorationData;
import de.hybris.platform.commercefacades.order.data.CommerceSaveCartParameterData;
import de.hybris.platform.commercefacades.order.data.CommerceSaveCartResultData;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;

import java.util.List;


/**
 * Save Cart facade interface. Service is responsible for saved cart related functionality such as saving a cart,
 * retrieving saved cart(s), restoring a saved cart etc.
 */
public interface SaveCartFacade
{
	/**
	 * Method saves a cart
	 *
	 * @param parameters
	 *           {@link CommerceSaveCartParameterData} parameter object that holds the Id of the cart to be saved along
	 *           with some additional details such as a name and a description for this cart
	 * @return {@link CommerceSaveCartResultData}
	 * @throws CommerceSaveCartException
	 *            if cart cannot be saved
	 */
	CommerceSaveCartResultData saveCart(CommerceSaveCartParameterData parameters) throws CommerceSaveCartException;

	/**
	 * Method flag a saved cart for deletion
	 *
	 * @param cartId
	 *           unique identifier of cart to be flagged
	 * @return {@link CommerceSaveCartResultData}
	 * @throws CommerceSaveCartException
	 *            if cart cannot be flagged for deletion
	 */
	CommerceSaveCartResultData flagForDeletion(String cartId) throws CommerceSaveCartException;

	/**
	 * Get details for a specific saved cart for current user
	 *
	 * @param parameters
	 *           {@link CommerceSaveCartParameterData} parameter object that holds the Id of the cart to be retrieved
	 * @return {@link CartData}
	 * @throws CommerceSaveCartException
	 *            if cart can't be found/retrieved
	 */
	CommerceSaveCartResultData getCartForCodeAndCurrentUser(CommerceSaveCartParameterData parameters)
			throws CommerceSaveCartException;

	/**
	 * Get details for a specific saved cart for current user
	 *
	 * @param parameters
	 *           {@link CommerceSaveCartParameterData} parameter object that holds the Id of the cart to be retrieved
	 * @return {@link CartRestorationData}
	 * @throws CommerceSaveCartException
	 *            if cart can't be found/retrieved
	 */
	CartRestorationData restoreSavedCart(CommerceSaveCartParameterData parameters) throws CommerceSaveCartException;

	/**
	 * Retrieve carts where order status equals ones of the status in the list.
	 *
	 * @param pageableData
	 * @param orderStatus
	 * @return list of saved user carts
	 */
	SearchPageData<CartData> getSavedCartsForCurrentUser(PageableData pageableData, List<OrderStatus> orderStatus);

	/**
	 * For a given save cart create a copy and store it.
	 *
	 * @param parameter
	 *           {@link CommerceSaveCartParameterData} parameter object that holds the cart code to be cloned
	 * @return {@link CommerceSaveCartResultData}
	 * @throws CommerceSaveCartException
	 */
	CommerceSaveCartResultData cloneSavedCart(CommerceSaveCartParameterData parameter) throws CommerceSaveCartException;
}
