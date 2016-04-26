/*
 *
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
package de.hybris.platform.ychinaaccelerator.storefront.tags;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.config.impl.DefaultConfigurationService;

import javax.servlet.http.HttpServletRequest;


/**
 * JSP EL Functions. This file contains static methods that are used by JSP EL.
 */
public class Functions extends de.hybris.platform.acceleratorstorefrontcommons.tags.Functions
{
	
	//CNACC:TODO: eventually separate alttext and title into 2 separate parameters
	/**
	 * JSP EL Function to create an HTML image tag prepared for lazy-loading the image if the corresponding property
	 * ychinaacceleratorstorefront.lazyload is set to true. Else the HTML image tag is being created and returned as it
	 * used to be without the lazy-loading bit. In case the lazy-loading img tag gets generated also a noscript part will
	 * be generated in order to provide still the regular img tag for SEO purposes for example ((some) search engines for
	 * example don't execute JavaScript when indexing a website).
	 * 
	 * @param cssClass
	 *           the CSS class name used to identify img tags being aimed for usage with lazy-loading mechanism.
	 * @param mediaUrl
	 *           the actual image url.
	 * @param mediaAltTextAndTitle
	 *           the actual image alt text value. Is used for the attribute alt as well as for the attribute title as
	 *           well.
	 * @param placeholderMediaUrl
	 *           the placeholder image url to show an image as long as the actual image has not been loaded in. Usually a
	 *           grey pixel for example.
	 * @return a generated HTML string depicting an HTML img tag, prepared either for the lazy-loading usage or as it
	 *         used to be defined without lazy-loading.
	 */
	public static String createImageTag(final String cssClass, final String mediaUrl, final String mediaAltTextAndTitle,
			final String placeholderMediaUrl)
	{
		if (getConfigurationService(getCurrentRequest()).getConfiguration().getBoolean("ychinaacceleratorstorefront.lazyload",
				false))
		{
			return new StringBuilder("<img class=\"" + cssClass + "\" src=\"" + placeholderMediaUrl + "\" data-original=\""
					+ mediaUrl + "\" ><noscript><img title=\"" + mediaAltTextAndTitle + "\" alt=\""
					+ mediaAltTextAndTitle + "\" src=\"" + mediaUrl + "\"></noscript>").toString();
		}
		return new StringBuilder("<img title=\"" + mediaAltTextAndTitle + "\" alt=\"" + mediaAltTextAndTitle + "\" src=\""
				+ mediaUrl + "\">").toString();
	}


	/**
	 * Retrieves the configuration service, which provides for example access to properties set in project.properties
	 * files.
	 * 
	 * @param httpRequest
	 * @return the configuration service.
	 */
	protected static ConfigurationService getConfigurationService(final HttpServletRequest httpRequest)
	{
		return getSpringBean(httpRequest, "configurationService", DefaultConfigurationService.class);
	}
}
