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
package de.hybris.platform.b2ctelcostorefront.controllers.pages.myaccount;

/**
 * Default implementation of the SOP Response URL Strategy.
 *
 */
public class DefaultSopResponseUrlStrategy implements SopResponseUrlStrategy
{

	private String url;

	/**
	 * @return the spring-configured SOP Response URL
	 */
	@Override
	public String getUrl()
	{
		return url;
	}

	public void setUrl(final String url)
	{
		this.url = url;
	}

}
