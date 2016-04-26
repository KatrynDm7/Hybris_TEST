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
package de.hybris.platform.timedaccesspromotionsservices.service;

import de.hybris.platform.timedaccesspromotionsservices.exception.MultipleEnqueueException;



/**
 * Service providing flashbuy promotion oriented functionality.
 */
public interface FlashbuyPromotionService
{
	/**
	 * Get the remaining quantity for the specific Product and Promotion
	 *
	 * @param promotionCode
	 *           Promotion Code
	 * @param productCode
	 *           Product Code
	 * @return Remaining quantity
	 */
	long getRemainingQuantity(String promotionCode, String productCode);

	/**
	 * Get the reserved quantity for the specific cartEntry.
	 *
	 * @param promotionCode
	 *           Promotion Code
	 * @param productCode
	 *           Product Code
	 * @param promotionMatcher
	 *           Generated Id to match cart entry and order entry
	 * @return Reserved quantity
	 */
	long getReserverdQuantity(String promotionCode, String productCode, String promotionMatcher);

	/**
	 * Save the enqueue request to PromotionEnqueue with productCode, promotionCode and customerID
	 *
	 * @param promotionCode
	 *           Promotion Code
	 * @param productCode
	 *           Product Code
	 * @param customerUID
	 *           Customer Uid
	 * @param quantity
	 *           Request quantity
	 * @return true if enqueue success
	 * @throws MultipleEnqueueException
	 *            if not first time request to enqueue
	 */
	boolean enqueue(String promotionCode, String productCode, String customerUID, long quantity) throws MultipleEnqueueException;

	/**
	 * Move request from enqueue and save the request to PromotionReservation with productCode, promotionCode and
	 * cartEntry
	 *
	 * @param promotionCode
	 *           Promotion Code
	 * @param productCode
	 *           Product Code
	 * @param customerUID
	 *           Customer Uid
	 * @param promotionMatcher
	 *           Generated Id to match cart entry and order entry
	 * @return true if reserve success
	 */
	boolean reserve(String promotionCode, String productCode, String customerUID, String promotionMatcher);

	/**
	 * Move request from reserve and save the request to PromotionAllocation with productCode, promotionCode and
	 * cartEntry
	 *
	 * @param promotionCode
	 *           Promotion Code
	 * @param productCode
	 *           Product Code
	 * @param promotionMatcher
	 *           Generated Id to match cart entry and order entry
	 * @return true if allocate success
	 */
	boolean allocate(String promotionCode, String productCode, String promotionMatcher);

}
