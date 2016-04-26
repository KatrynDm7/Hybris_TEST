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
package de.hybris.platform.ycommercewebservices.util.ws.impl;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.Lists;


@UnitTest
@RunWith(Parameterized.class)
public class AddonAwareMessageSourceTest
{
	String input;
	String output;

	AddonAwareMessageSource ms;

	public AddonAwareMessageSourceTest(final String in, final String out)
	{
		input = in;
		output = out;

		ms = new AddonAwareMessageSource();
	}

	@Parameters
	public static Collection<String[]> parameters()
	{
		return Lists
				.newArrayList(
						new String[]
						{
								"/Users/Develop/yenv/dev/sources/commercewebservices/ycommercewebservices/web/webroot/WEB-INF/messages/addons/webserviceaddon/base.properties",
								"/WEB-INF/messages/addons/webserviceaddon/base" },
						new String[]
						{
								"/Users/Develop/yenv/dev/sources/commercewebservices/ycommercewebservices/web/webroot/WEB-INF/messages/addons/webserviceaddon/base_en.properties",
								"/WEB-INF/messages/addons/webserviceaddon/base" },
						new String[]
						{
								"/Users/Develop/yenv/dev/sources/commercewebservices/ycommercewebservices/web/webroot/WEB-INF/messages/addons/webserviceaddon/base_en_US.properties",
								"/WEB-INF/messages/addons/webserviceaddon/base" },
						new String[]
						{
								"/Users/Develop/yenv/dev/sources/commercewebservices/ycommercewebservices/web/webroot/WEB-INF/messages/addons/webserviceaddon/base_message_en_US.properties",
								"/WEB-INF/messages/addons/webserviceaddon/base" },
						new String[]
						{
								"/Users/Develop/yenv/dev/sources/commercewebservices/WEB-INF/messages/addons/ycommercewebservices/web/webroot/WEB-INF/messages/addons/webserviceaddon/base.properties",
								"/WEB-INF/messages/addons/webserviceaddon/base" });
	}

	@Test
	public void formatPathTest()
	{
		final String actual = ms.formatPath(input, "/WEB-INF/messages/addons/");
		Assert.assertEquals(output, actual);
	}


}
