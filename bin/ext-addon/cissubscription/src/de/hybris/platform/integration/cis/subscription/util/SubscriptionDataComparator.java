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
package de.hybris.platform.integration.cis.subscription.util;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.subscriptionfacades.data.SubscriptionData;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


/**
 * Comparator class for SubscriptionData. Order based on hard-coded status names and weights.
 */
public class SubscriptionDataComparator implements Comparator<SubscriptionData>
{

	private static final Map<String, Integer> statusOrder;
	static
	{
		statusOrder = new HashMap<>(4);
		statusOrder.put("ACTIVE", Integer.valueOf(0));
		statusOrder.put("PAUSED", Integer.valueOf(1));
		statusOrder.put("CANCELLED", Integer.valueOf(2));
		statusOrder.put("EXPIRED", Integer.valueOf(3));
	}

	@Override
	public int compare(final SubscriptionData o1, final SubscriptionData o2)
	{
		validateParameterNotNullStandardMessage("subscriptionData o1", o1);
		validateParameterNotNullStandardMessage("subscriptionData o2", o2);

		final Integer status1 = statusOrder.get(o1.getSubscriptionStatus());
		final Integer status2 = statusOrder.get(o2.getSubscriptionStatus());

		if (status1 == null || status2 == null)
		{
			return 0; // do not compare unknown statuses, let them be equal
		}
		else
		{
			final int compareResult = status1.compareTo(status2);
			if (compareResult == 0)
			{
				if (o2.getStartDate() == null || o1.getStartDate() == null)
				{
					// It most likely won't happen, but we should check for null dates
					return 0;
				}
				// compare by start dates when statuses are equal
				return o2.getStartDate().compareTo(o1.getStartDate());
			}
			return compareResult;
		}
	}
}