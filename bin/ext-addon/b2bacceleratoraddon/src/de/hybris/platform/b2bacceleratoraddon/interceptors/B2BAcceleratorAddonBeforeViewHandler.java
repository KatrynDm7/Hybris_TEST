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
package de.hybris.platform.b2bacceleratoraddon.interceptors;


import de.hybris.platform.addonsupport.interceptors.BeforeViewHandlerAdaptee;
import de.hybris.platform.b2bacceleratoraddon.constants.B2bacceleratoraddonConstants;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;


public class B2BAcceleratorAddonBeforeViewHandler implements BeforeViewHandlerAdaptee
{

	public static final String VIEW_NAME_MAP_KEY = "viewName";
	private Map<String, Map<String, String>> viewMap;

	@Override
	public String beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelMap model,
			String viewName) throws Exception
	{
		if (viewMap.containsKey(viewName))
		{
			viewName = B2bacceleratoraddonConstants.ADDON_PREFIX + viewMap.get(viewName).get(VIEW_NAME_MAP_KEY);
		}

		return viewName;
	}

	public Map<String, Map<String, String>> getViewMap()
	{
		return viewMap;
	}

	public void setViewMap(final Map<String, Map<String, String>> viewMap)
	{
		this.viewMap = viewMap;
	}

}
