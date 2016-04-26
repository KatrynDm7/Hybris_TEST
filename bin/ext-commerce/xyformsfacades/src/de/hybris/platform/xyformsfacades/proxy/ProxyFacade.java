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
 */
package de.hybris.platform.xyformsfacades.proxy;

import de.hybris.platform.xyformsservices.enums.YFormDataActionEnum;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;
import de.hybris.platform.xyformsservices.proxy.ProxyException;
import de.hybris.platform.xyformsservices.proxy.ProxyService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Orchestrates calls to {@link ProxyService}
 */
public interface ProxyFacade
{
	/**
	 * Gets the embedded HTML representation of a form definition
	 * 
	 * @param applicationId
	 * @param formId
	 * @param action
	 * @param formDataId
	 * @throws YFormServiceException
	 */
	public String getInlineFormHtml(final String applicationId, final String formId, final YFormDataActionEnum action,
			final String formDataId) throws YFormServiceException;

	/**
	 * Proxies content
	 * 
	 * @param request
	 * @param response
	 * @throws ProxyException
	 */
	public void proxy(final HttpServletRequest request, final HttpServletResponse response) throws ProxyException;
}
