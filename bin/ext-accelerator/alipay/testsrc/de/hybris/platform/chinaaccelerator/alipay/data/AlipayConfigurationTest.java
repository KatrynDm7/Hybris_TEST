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
package de.hybris.platform.chinaaccelerator.alipay.data;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class AlipayConfigurationTest {

	AlipayConfiguration alipayConfiguration = new AlipayConfiguration();
	
	@Before
	public void setUp()
	{
		alipayConfiguration.setTest_amount(Boolean.FALSE.toString());
	}
	
	@Test
	public void testGetRequestPrice_two_decimals_format(){
		final double price = 0.01000;
		final String result = alipayConfiguration.getRequestPrice(price);
		Assert.assertEquals("0.01", result);
	}
	
	@Test
	public void testGetRefundPrice_two_decimals_format(){
		final double price = 0.01000;
		final String result = alipayConfiguration.getRefundPrice(price);
		Assert.assertEquals("0.01", result);
	}
}
