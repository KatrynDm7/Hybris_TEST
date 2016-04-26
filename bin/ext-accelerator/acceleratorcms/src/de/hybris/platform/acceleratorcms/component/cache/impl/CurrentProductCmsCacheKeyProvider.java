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
package de.hybris.platform.acceleratorcms.component.cache.impl;

import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


public class CurrentProductCmsCacheKeyProvider extends DefaultCmsCacheKeyProvider
{
	@Override
	public StringBuilder getKeyInternal(final HttpServletRequest request, final SimpleCMSComponentModel component)
	{
		final StringBuilder buffer = new StringBuilder(super.getKeyInternal(request, component));
		final String currentProduct = getRequestContextData(request).getProduct().getPk().getLongValueAsString();
		if (!StringUtils.isEmpty(currentProduct))
		{
			buffer.append(currentProduct);
		}
		return buffer;
	}
}
