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
package de.hybris.platform.commerceservices.url;

/**
 * Interface used to resolve a URL path for a parametrized type
 * 
 * @param <T>
 *           the type of the source item to resolve into a URL.
 */
public interface UrlResolver<T>
{
	/**
	 * Resolve the url path for the source type.
	 * 
	 * @param source
	 *           the source type.
	 * @return the URL path
	 */
	String resolve(T source);
}
