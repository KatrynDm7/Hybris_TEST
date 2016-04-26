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
import de.hybris.platform.financialfacades.util.BillingTimeDataComparator;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class BillingTimeDataComparatorTest
{

	private BillingTimeDataComparator comparator;


	@Before
	public void setup()
	{

		comparator = new BillingTimeDataComparator();
	}

	@Test
	public void testCompare()
	{


		final BillingTimeData billingTimeData1 = createBillingTimeData(null, null);
		final BillingTimeData billingTimeData2 = createBillingTimeData(null, null);

		Assert.assertEquals(0, comparator.compare(null, null));
		Assert.assertEquals(-1, comparator.compare(billingTimeData1, null));
		Assert.assertEquals(1, comparator.compare(null, billingTimeData2));
		Assert.assertEquals(0, comparator.compare(billingTimeData1, billingTimeData2));

	}

	@Test
	public void testCompareByName()
	{
		final String code1 = "code1";
		Assert.assertEquals(0, comparator.compare(createBillingTimeData(code1, null), createBillingTimeData(null, null)));
		Assert.assertEquals(1, comparator.compare(createBillingTimeData(code1, "B"), createBillingTimeData(null, "A")));
		Assert.assertEquals(-1, comparator.compare(createBillingTimeData(code1, "B"), createBillingTimeData(null, null)));
		Assert.assertEquals(1, comparator.compare(createBillingTimeData(code1, null), createBillingTimeData(null, "B")));

	}

	@Test
	public void testCompareByCode()
	{
		final String code = "code";

		Assert.assertEquals(0, comparator.compare(createBillingTimeData(null, null), createBillingTimeData(null, null)));
		Assert.assertEquals(0, comparator.compare(createBillingTimeData(code, null), createBillingTimeData(code, null)));

	}

	@Test
	public void testCompareByCodeTopBottomCodeSet()
	{
		final String code = "code";
		final String topCode = "topCode";
		final String bottomCode = "bottomCode";
		comparator.setTop(topCode);
		comparator.setBottom(bottomCode);

		Assert.assertEquals(0, comparator.compare(createBillingTimeData(null, null), createBillingTimeData(null, null)));
		Assert.assertEquals(0, comparator.compare(createBillingTimeData(code, null), createBillingTimeData(code, null)));
		Assert.assertEquals(-1, comparator.compare(createBillingTimeData(topCode, null), createBillingTimeData(code, null)));
		Assert.assertEquals(1, comparator.compare(createBillingTimeData(code, null), createBillingTimeData(topCode, null)));
		Assert.assertEquals(1, comparator.compare(createBillingTimeData(bottomCode, null), createBillingTimeData(code, null)));
		Assert.assertEquals(-1, comparator.compare(createBillingTimeData(code, null), createBillingTimeData(bottomCode, null)));

	}

	private BillingTimeData createBillingTimeData(final String code, final String name)
	{
		final BillingTimeData billingTimeData = new BillingTimeData();

		billingTimeData.setCode(code);
		billingTimeData.setName(name);
		return billingTimeData;
	}
}
