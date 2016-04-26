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

import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;

import java.io.Serializable;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Required;


/**
 * The class of OneTimeChargeEntryDataComparator. Uses a BillingTimeData comparator to specify the display order of the
 * OneTimeCharge entries
 */
public class OneTimeChargeEntryDataComparator implements Comparator<OneTimeChargeEntryData>, Serializable
{
	private static final long serialVersionUID = -6415253131323764581L;
	private Comparator<BillingTimeData> billingTimeDataComparator;

	@Override
	public int compare(final OneTimeChargeEntryData data1, final OneTimeChargeEntryData data2)
	{
		return getBillingTimeDataComparator().compare(data1.getBillingTime(), data2.getBillingTime());
	}

	protected Comparator<BillingTimeData> getBillingTimeDataComparator()
	{
		return billingTimeDataComparator;
	}

	@Required
	public void setBillingTimeDataComparator(final Comparator<BillingTimeData> billingTimeDataComparator)
	{
		this.billingTimeDataComparator = billingTimeDataComparator;
	}
}
