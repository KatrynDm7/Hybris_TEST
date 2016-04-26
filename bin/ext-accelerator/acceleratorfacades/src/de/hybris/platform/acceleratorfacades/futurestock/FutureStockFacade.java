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
package de.hybris.platform.acceleratorfacades.futurestock;

import de.hybris.platform.commercefacades.product.data.FutureStockData;

import java.util.List;
import java.util.Map;


/**
 * Facade for 'Future Stock Management'.
 */
public interface FutureStockFacade
{

	/**
	 * Gets the future product availability for the specified product, for each future date.
	 *
	 * @param productCode
	 *           the product code
	 * @return A list of quantity ordered by date. If there is no availability for this product in the future, an empty
	 *         list is returned.
	 */
	List<FutureStockData> getFutureAvailability(final String productCode);

	/**
	 * Gets the future product availability for the list of specified products, for each future date.
	 *
	 * @param productCodes
	 *           the product codes
	 * @return A map of product codes with a list of quantity ordered by date.
	 */
	Map<String, List<FutureStockData>> getFutureAvailability(final List<String> productCodes);

	/**
	 * Gets the future product availability for the list of specified variants related to a given product.
	 *
	 * @param productCode
	 *           the product code
	 * @param skus
	 *           Product codes of the desired variants related to the productCode.
	 * @return A map of product codes with a list of quantity ordered by date. If product is not variant, returns null.
	 */
	Map<String, List<FutureStockData>> getFutureAvailabilityForSelectedVariants(final String productCode, final List<String> skus);


}