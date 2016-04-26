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
package de.hybris.platform.financialfacades.strategies;

import java.util.Map;


/**
 * The class of AddToCartStrategy.
 */
public interface InsuranceAddToCartStrategy
{
	/**
	 * Add to cart method to implement the strategy.
	 * 
	 * @param properties
	 */
	void addToCart(final Map<String, Object> properties);
}
