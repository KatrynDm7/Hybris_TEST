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
package de.hybris.platform.commerceservices.strategies;



/**
 * Interface for strategy, which allows for overriding the default behavior to retrieve the net/gross setting.
 * 
 * @spring.bean netGrossStrategy
 */
public interface NetGrossStrategy
{
	/**
	 * Method for retrieving the net/gross setting
	 * 
	 * @return the net/gross setting to be used for retrieving price information
	 */
	boolean isNet();
}
