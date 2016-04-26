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
package de.hybris.platform.commerceservices.order.hook;

import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;

/**
 * A hook interface into before and after cart calculation lifecycle
 */
public interface CommerceCartCalculationMethodHook
{
	/**
	 * Executed after commerce cart calculation
	 *
	 * @param parameter   a parameter object holding the cart data
	 */

	void afterCalculate(final CommerceCartParameter parameter);

	/**
	 * Executed before commerce cart calculation
	 *
	 * @param parameter   a parameter object holding the cart data
	 */
	void beforeCalculate(final CommerceCartParameter parameter);
}
