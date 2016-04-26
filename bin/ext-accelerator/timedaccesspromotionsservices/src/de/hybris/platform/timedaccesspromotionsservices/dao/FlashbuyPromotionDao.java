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
package de.hybris.platform.timedaccesspromotionsservices.dao;

import de.hybris.platform.acceleratorservices.promotions.dao.PromotionsDao;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.timedaccesspromotionsservices.model.PromotionAllocationModel;
import de.hybris.platform.timedaccesspromotionsservices.model.PromotionEnqueueModel;
import de.hybris.platform.timedaccesspromotionsservices.model.PromotionReservationModel;

import java.util.List;


/**
 * Data Access for Flashbuy Promotion remaining quantity and reserved quantity.
 */
public interface FlashbuyPromotionDao extends PromotionsDao
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
	 * Get the list of PromotionEnqueueModel for removal
	 *
	 * @param productID
	 *           Product Code
	 * @param promotionID
	 *           Promotion Code
	 * @param customerUID
	 *           Customer User ID
	 * @return List of PromotionEnqueueModel
	 */
	List<PromotionEnqueueModel> getEnqueueforRemoval(String productID, String promotionID, String customerUID);

	/**
	 * Get the list of PromotionAllocationModel for removal
	 *
	 * @param productID
	 *           Product Code
	 * @param promotionID
	 *           Promotion Code
	 * @param promotionMatcher
	 *           Generated Id to match cart entry and order entry
	 * @return List of PromotionReservationModel
	 */
	List<PromotionReservationModel> getReservationforRemoval(String productID, String promotionID, String promotionMatcher);

	/**
	 * Get the list of PromotionAllocationModel for removal
	 *
	 * @param productID
	 *           Product Code
	 * @param promotionID
	 *           Promotion Code
	 * @param promotionMatcher
	 *           Generated Id to match cart entry and order entry
	 * @return List of PromotionAllocationModel
	 */
	List<PromotionAllocationModel> getAllocationforRemoval(String productID, String promotionID, String promotionMatcher);

	/**
	 * Get the list of promotionMatcher in PromotionReservation for specific promotion and product
	 *
	 * @param promotionCode
	 *           Promotion Code
	 * @param productCode
	 *           Product Code
	 * @return List of PromotionMatcher
	 */
	List<String> getReservePromotionMatcherByPromotionAndProduct(String promotionCode, String productCode);

	/**
	 * Get the list of promotionMatcher in PromotionAllocation for specific promotion and product
	 *
	 * @param promotionCode
	 *           Promotion Code
	 * @param productCode
	 *           Product Code
	 * @return List of PromotionMatcher
	 */
	List<String> getAllocatePromotionMatcherByPromotionAndProduct(String promotionCode, String productCode);

	/**
	 * Get the abstractOrderEntryModel for specific promotionMatcher
	 *
	 * @param promotionMatcher
	 *           Generated Id to match cart entry and order entry
	 * @return AbstractOrderEntryModel
	 */
	AbstractOrderEntryModel getAbstractOrderEntryByPromotionMatcher(String promotionMatcher);
}
