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
package de.hybris.platform.commerceservices.threadcontext;

/**
 * ThreadContext interface. Supports storing attribute values.
 */
public interface ThreadContext
{
	/**
	 * Sets the given value Object to the Session as attribute with the given name
	 *
	 * @param name  the name of the attribute
	 * @param value the value to be set
	 */
	void setAttribute(String name, Object value);

	/**
	 * Returns for the given session attribute name the value
	 *
	 * @param name the attribute name
	 * @return <code>null</code> if no attribute with the given name is stored in the session
	 */
	<T> T getAttribute(String name);

	/**
	 * Removes the given attribute from the current session. Do nothing if the attribute doesn't exists in the session.
	 *
	 * @param name the attribute name
	 */
	void removeAttribute(String name);
}
