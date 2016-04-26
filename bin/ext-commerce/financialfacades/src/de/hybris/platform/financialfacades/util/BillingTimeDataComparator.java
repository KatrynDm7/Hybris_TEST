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

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang.StringUtils;


/**
 * The class of BillingTimeDataComparator. Sorting rules </br> if the BillingTime code matches either the top or bottom
 * attribute values, then the order of the items affected respectively </br> if the top/bottom codes do not match the
 * BillingTime code, then the comparison is based on the BilingTime names
 */
public class BillingTimeDataComparator implements Comparator<BillingTimeData>, Serializable
{

	private static final long serialVersionUID = 1L;
	private String top;
	private String bottom;

	@Override
	public int compare(final BillingTimeData billingTimeData1, final BillingTimeData billingTimeData2)
	{
		if (billingTimeData1 != null && billingTimeData2 != null)
		{
			final String code1 = billingTimeData1.getCode();
			final String code2 = billingTimeData2.getCode();

			if (StringUtils.equals(code1, code2))
			{
				return 0;
			}

			if (StringUtils.isNotEmpty(getTop()))
			{
				if (getTop().equals(code1))
				{
					return -1;
				}
				if (getTop().equals(code2))
				{
					return 1;
				}
			}

			if (StringUtils.isNotEmpty(getBottom()))
			{
				if (getBottom().equals(code1))
				{
					return 1;
				}
				if (getBottom().equals(code2))
				{
					return -1;
				}
			}

			final String name1 = billingTimeData1.getName();
			final String name2 = billingTimeData2.getName();

			if (StringUtils.equals(name1, name2))
			{
				return 0;
			}

			return name1 != null ? name2 != null ? name1.compareTo(name2) : -1 : 1;

		}
		else
		{
			return billingTimeData1 != null ? -1 : billingTimeData2 != null ? 1 : 0;
		}
	}

	protected String getTop()
	{
		return top;
	}

	public void setTop(final String top)
	{
		this.top = top;
	}

	protected String getBottom()
	{
		return bottom;
	}

	public void setBottom(final String bottom)
	{
		this.bottom = bottom;
	}
}
