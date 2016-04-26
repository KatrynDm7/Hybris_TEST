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
package de.hybris.platform.ycommercewebservices.request.support;

import de.hybris.platform.ycommercewebservices.exceptions.UnsupportedRequestException;


/**
 * Interface for checking if request is supported in current configuration (e.g. for current base store, for payment
 * provider)
 * 
 */
public interface RequestSupportedStrategy
{
	/**
	 * Method check if request is supported
	 * 
	 * @param requestId
	 *           request identifier (e.g. request path)
	 * @return true if request is supported<br/>
	 *         false if request is not supported
	 * 
	 */
	boolean isRequestSupported(String requestId);

	/**
	 * Method check if request is supported and throws exception if not
	 * 
	 * @param requestId
	 *           request identifier (e.g. request path)
	 * @throws UnsupportedRequestException
	 *            when request is not supported
	 */
	void checkIfRequestSupported(String requestId) throws UnsupportedRequestException;
}
