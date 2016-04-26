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

/**
 * Status values for the CommerceCartModification statusCode. As the statusCode is a string it is not limited to any
 * specific set of values. These are just the values exposed by the commerceservices.
 */
public interface CommerceCartModificationStatus
{
	/**
	 * Indicates a successful modification of the cart
	 */
	String SUCCESS = "success";

	/**
	 * Indicates a failure to add the requested number of items to cart due to low stock.
	 */
	String LOW_STOCK = "lowStock";

	/**
	 * Indicates a failure to add the requested number of items to cart due to no stock.
	 */
	String NO_STOCK = "noStock";

	/**
	 * Indicates a failure to add the requested number of items to cart due to max order quantity exceeded.
	 */
	String MAX_ORDER_QUANTITY_EXCEEDED = "maxOrderQuantityExceeded";

	/**
	 * Indicates a failure to add the requested number of items to cart due to no stock in point of service.
	 */
	String MOVED_FROM_POS_TO_STORE = "movedFromPOSToStore";

	/**
	 * Indicates a failure to add the requested number of items to cart due to product unavailability (approval, online
	 * dates etc).
	 */
	String UNAVAILABLE = "unavailable";

}
