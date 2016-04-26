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
package de.hybris.platform.financialfacades.util;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.financialfacades.util.OneTimeChargeEntryDataComparator;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;

import java.util.Comparator;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class OneTimeChargeEntryDataComparatorTest
{

	@Mock
	private Comparator<BillingTimeData> billingTimeDataComparator;

	@InjectMocks
	private OneTimeChargeEntryDataComparator comparator;

	@Before
	public void setup()
	{
		comparator = new OneTimeChargeEntryDataComparator();

		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void testCompare()
	{

		final OneTimeChargeEntryData data1 = new OneTimeChargeEntryData();
		final BillingTimeData billingTime1 = new BillingTimeData();
		data1.setBillingTime(billingTime1);

		final OneTimeChargeEntryData data2 = new OneTimeChargeEntryData();
		final BillingTimeData billingTime2 = new BillingTimeData();
		data2.setBillingTime(billingTime2);

		Mockito.doReturn(Integer.valueOf(1)).when(billingTimeDataComparator).compare(billingTime1, billingTime2);

		Assert.assertEquals(1, comparator.compare(data1, data2));

		Mockito.verify(billingTimeDataComparator).compare(billingTime1, billingTime2);
	}
}
