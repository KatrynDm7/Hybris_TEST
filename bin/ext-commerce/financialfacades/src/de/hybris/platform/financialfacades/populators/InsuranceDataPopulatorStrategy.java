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
package de.hybris.platform.financialfacades.populators;

import de.hybris.platform.commercefacades.quotation.InsuranceQuoteData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Required;


public abstract class InsuranceDataPopulatorStrategy
{
	protected static SimpleDateFormat sdf;

	private String dateFormatForDisplay;

	/**
	 * Used to extract data from the <infoMap> object which is a <Map> of strings and objects and these are used to
	 * derive the appropriate values for the <InsuranceQuoteData> object supplied.
	 *
	 * @param quoteData
	 *           <InsuranceQuoteData> object
	 * @param infoMap
	 *           <Map> object
	 */
	public abstract void processInsuranceQuoteData(final InsuranceQuoteData quoteData, final Map<String, Object> infoMap);

	/**
	 * Utility method to look for <java.util.Date> objects in the info map which holds the various elements taken from a
	 * yform.
	 *
	 * @param infoMap
	 *           the map of properties taken from the form
	 * @param datePropertyName
	 *           the name of the date property that we are looking for
	 * @return either the found date object, or <null> if it is not found successfully
	 */
	public Date findPossibleDate(final Map<String, Object> infoMap, final String datePropertyName)
	{
		if (infoMap.containsKey(datePropertyName))
		{
			final Object obj = MapUtils.getObject(infoMap, datePropertyName);
			if (obj instanceof Date)
			{
				return (Date) obj;
			}
			else if (obj instanceof LocalDate)
			{

				return ((LocalDate) obj).toDate();
			}
		}

		return null;
	}

	public String getDateFormatForDisplay()
	{
		return dateFormatForDisplay;
	}

	@Required
	public void setDateFormatForDisplay(final String dateFormatForDisplay)
	{
		this.dateFormatForDisplay = dateFormatForDisplay;
		sdf = new SimpleDateFormat(dateFormatForDisplay);
	}
}
