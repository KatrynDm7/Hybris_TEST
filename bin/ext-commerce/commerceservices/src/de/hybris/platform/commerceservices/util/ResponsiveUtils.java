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
package de.hybris.platform.commerceservices.util;

import de.hybris.platform.util.Config;

import org.apache.commons.lang.StringUtils;


/**
 * Temporary class to verify if the current experience is responsive or 'classic' desktop. This will be deleted at the
 * same time as the 'classic' desktop experience.
 * 
 */
public class ResponsiveUtils
{
	public static final String DEFAULT_UI_EXPERIENCE = "commerceservices.default.desktop.ui.experience";
	public static final String RESPONSIVE = "responsive";

	/**
	 * @return true if the application is set up as responsive
	 */
	public static boolean isResponsive()
	{
		if (StringUtils.isBlank(Config.getParameter(DEFAULT_UI_EXPERIENCE)))
		{
			return false;
		}
		return StringUtils.equalsIgnoreCase(Config.getParameter(DEFAULT_UI_EXPERIENCE), RESPONSIVE);
	}

}
