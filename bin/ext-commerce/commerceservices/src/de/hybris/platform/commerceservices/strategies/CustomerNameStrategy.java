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

public interface CustomerNameStrategy
{
	/**
	 * Retrieves first and lastname by splitting the given name at the last found space separator.
	 * 
	 * @param name
	 * @return array containing first- and lastname
	 */
	String[] splitName(String name);

	/**
	 * Retrieves the name by combining firstname and lastname separated by a space character.
	 * 
	 * @param firstName
	 * @param lastName
	 * @return name
	 */
	String getName(String firstName, String lastName);
}
