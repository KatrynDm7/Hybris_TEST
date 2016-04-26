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
package de.hybris.platform.timedaccesspromotionsfacades.facades;

import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.timedaccesspromotionsservices.exception.MultipleEnqueueException;


/**
 * Facade providing Flashbuy promotion oriented functionality.
 */
public interface FlashbuyPromotionFacade
{
	/**
	 * Judge whether there are products remaining for the promotion.
	 *
	 * @param productCode
	 *           the product code
	 * @param promotionCode
	 *           the promotion code
	 * @return true if there is product remaining
	 */
	boolean hasProductAvailable(String productCode, String promotionCode);

	/**
	 * Create enqueue request for specific promotion, product and customer with request quantity
	 *
	 * @param productCode
	 *           the product code
	 * @param promotionCode
	 *           the promotion code
	 * @param customerUID
	 *           the customer uid
	 * @param quantity
	 *           the quantity requested
	 * @return true if enqueue success
	 * @throws CommerceCartModificationException
	 * @throws MultipleEnqueueException
	 */
	boolean enqueue(String productCode, String promotionCode, String customerUID, long quantity)
			throws CommerceCartModificationException, MultipleEnqueueException;
}
