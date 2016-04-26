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

package de.hybris.platform.configurablebundlefacades.converters;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.subscriptionfacades.converters.SubscriptionXStreamAliasConverter;

import org.springframework.beans.factory.annotation.Autowired;
import com.thoughtworks.xstream.XStream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


public class BundleXStreamConverter
{
	private static final Logger LOG = Logger.getLogger(BundleXStreamConverter.class);

	@Autowired
	private SubscriptionXStreamAliasConverter xStreamAliasConverter;

	public String getXStreamXmlFromCartData(final CartData cartData)
	{
		final String xml = getXstream().toXML(cartData);

		if (LOG.isDebugEnabled())
		{
			LOG.debug(xml);
		}

		return xml;
	}

	public CartData getCartDataFromXml(final String xml)
	{
		if (StringUtils.isNotEmpty(xml))
		{
			return (CartData) getXstream().fromXML(xml);
		}

		return null;
	}

	public XStream getXstream()
	{
		return xStreamAliasConverter.getXstream();
	}

}
