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
package de.hybris.platform.commerceservices.url.impl;

import de.hybris.platform.storelocator.model.PointOfServiceModel;

import org.springframework.beans.factory.annotation.Required;


public class DefaultPointOfServiceUrlResolver extends AbstractUrlResolver<PointOfServiceModel>
{
	private final String CACHE_KEY = DefaultPointOfServiceUrlResolver.class.getName();

	private String pattern;

	protected String getPattern()
	{
		return pattern;
	}

	@Required
	public void setPattern(final String pattern)
	{
		this.pattern = pattern;
	}

	@Override
	protected String getKey(final PointOfServiceModel source)
	{
		return CACHE_KEY + "." + source.getPk().toString();
	}

	@Override
	protected String resolveInternal(final PointOfServiceModel source)
	{
		// /store/{store-name}?lat={latitude}&long={longitude}

		// Replace pattern values
		String url = getPattern();

		if (url.contains("{store-name}"))
		{
			url = url.replace("{store-name}", urlEncode(source.getName()).replaceAll("\\+", "%20"));
		}
		if (url.contains("{latitude}"))
		{
			url = url.replace("{latitude}", (source.getLatitude() != null ? source.getLatitude().toString() : "0"));
		}
		if (url.contains("{longitude}"))
		{
			url = url.replace("{longitude}", (source.getLongitude() != null ? source.getLongitude().toString() : "0"));
		}

		return url;
	}
}
