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
package de.hybris.platform.financialacceleratorstorefront.component.renderer;

import de.hybris.platform.constants.FinancialacceleratorstorefrontConstants;
import de.hybris.platform.financialservices.model.components.CMSTripDetailsSubmitComponentModel;
import de.hybris.platform.util.Config;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.jsp.PageContext;
import javax.xml.xpath.XPath;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.google.common.collect.Lists;



/**
 * The class of TripDetailsSubmitComponentRenderer.
 */
public class TripDetailsSubmitComponentRenderer<C extends CMSTripDetailsSubmitComponentModel> extends
		AbstractFormSubmitComponentRenderer<C>
{

	private static final Logger LOG = Logger.getLogger(TripDetailsSubmitComponentRenderer.class);

	@Override
	protected String getSessionFormId(final PageContext pageContext, final C component)
	{
		return (String) pageContext.getSession().getAttribute(FinancialacceleratorstorefrontConstants.TRIP_DETAILS_FORM_DATA_ID);
	}

	@Override
	protected void storeSessionFormId(final PageContext pageContext, final String newSessionFormDataId, final C component)
	{
		pageContext.getSession().setAttribute(FinancialacceleratorstorefrontConstants.TRIP_DETAILS_FORM_DATA_ID,
				newSessionFormDataId);
		getSessionService().setAttribute(FinancialacceleratorstorefrontConstants.TRIP_DETAILS_FORM_DATA_ID, newSessionFormDataId);
	}

	/**
	 * Method which extracts and sets all the trip information details to the session.
	 *
	 * @param pString
	 * @param pPageContext
	 */
	@Override
	public void setSessionAttributes(final String pString, final PageContext pPageContext, final C component)
	{
		try
		{
			//Create DOM document
			final Document document = createDocument(pString);

			// Create XPath object
			final XPath xpath = createXPath();

			NodeList nodes = null;

			nodes = getNodeList(xpath, document, "/form/trip-details/destination/text()");
			if (nodes.item(0) != null)
			{
				pPageContext.getSession().setAttribute(FinancialacceleratorstorefrontConstants.TRIP_DETAILS_DESTINATION,
						safelyGetFirstNodeValue(nodes));
				getSessionService().setAttribute(FinancialacceleratorstorefrontConstants.TRIP_DETAILS_DESTINATION,
						safelyGetFirstNodeValue(nodes));
			}

			nodes = getNodeList(xpath, document, "/form/trip-details/start-date/text()");
			if (nodes.item(0) != null)
			{
				pPageContext.getSession().setAttribute(FinancialacceleratorstorefrontConstants.TRIP_DETAILS_START_DATE,
						safelyGetFirstNodeValue(nodes));
				getSessionService().setAttribute(FinancialacceleratorstorefrontConstants.TRIP_DETAILS_START_DATE,
						safelyGetFirstNodeValue(nodes));
			}

			nodes = getNodeList(xpath, document, "/form/trip-details/return-date/text()");
			if (nodes.item(0) != null)
			{
				pPageContext.getSession().setAttribute(FinancialacceleratorstorefrontConstants.TRIP_DETAILS_END_DATE,
						safelyGetFirstNodeValue(nodes));
				getSessionService().setAttribute(FinancialacceleratorstorefrontConstants.TRIP_DETAILS_END_DATE,
						safelyGetFirstNodeValue(nodes));
				getSessionService().setAttribute(FinancialacceleratorstorefrontConstants.TRIP_DETAILS_NO_OF_DAYS,
						calculateNumberOfDays(pPageContext));
			}
			else
			{
				final String numberOfDays = Config.getString("travel.quotation.default.noofdays", "31");
				pPageContext.getSession().setAttribute(FinancialacceleratorstorefrontConstants.TRIP_DETAILS_END_DATE,
						FinancialacceleratorstorefrontConstants.NOT_APPLICABLE_TEXT);
				getSessionService().setAttribute(FinancialacceleratorstorefrontConstants.TRIP_DETAILS_END_DATE,
						FinancialacceleratorstorefrontConstants.NOT_APPLICABLE_TEXT);

				pPageContext.getSession().setAttribute(FinancialacceleratorstorefrontConstants.TRIP_DETAILS_NO_OF_DAYS, numberOfDays);
				getSessionService().setAttribute(FinancialacceleratorstorefrontConstants.TRIP_DETAILS_NO_OF_DAYS, numberOfDays);

			}

			nodes = getNodeList(xpath, document, "/form/trip-details/cost-of-trip/text()");
			if (nodes.item(0) != null)
			{
				pPageContext.getSession().setAttribute(FinancialacceleratorstorefrontConstants.TRIP_COST,
						safelyGetFirstNodeValue(nodes));
				getSessionService().setAttribute(FinancialacceleratorstorefrontConstants.TRIP_COST, safelyGetFirstNodeValue(nodes));
			}

			nodes = getNodeList(xpath, document, "/form/trip-details/number-of-travellers/text()");
			if (nodes.item(0) != null)
			{
				final int numberOfTravellers = Integer.parseInt(safelyGetFirstNodeValue(nodes));
				pPageContext.getSession().setAttribute(FinancialacceleratorstorefrontConstants.TRIP_DETAILS_NO_OF_TRAVELLERS,
						Integer.toString(numberOfTravellers));
				getSessionService().setAttribute(FinancialacceleratorstorefrontConstants.TRIP_DETAILS_NO_OF_TRAVELLERS,
						Integer.toString(numberOfTravellers));

				final List<String> travellerAges = Lists.newArrayList();
				for (int i = 1; i <= Integer.valueOf(safelyGetFirstNodeValue(nodes)); i++)
				{
					final NodeList ageNodes = getNodeList(xpath, document, "/form/age-of-travellers/age-" + i + "/text()");
					travellerAges.add(safelyGetFirstNodeValue(ageNodes));
				}

				pPageContext.getSession().setAttribute(FinancialacceleratorstorefrontConstants.TRIP_DETAILS_TRAVELLER_AGES,
						travellerAges);
				getSessionService().setAttribute(FinancialacceleratorstorefrontConstants.TRIP_DETAILS_TRAVELLER_AGES, travellerAges);
			}

		}
		catch (final DOMException e)
		{
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * Helper method used to calculates the number of days based on start and end date from the trip information details.
	 *
	 * @param pPageContext
	 * @return
	 */
	protected String calculateNumberOfDays(final PageContext pPageContext)
	{
		final DateTimeFormatter formatter = DateTimeFormat
				.forPattern(FinancialacceleratorstorefrontConstants.INSURANCE_GENERIC_DATE_FORMAT);

		final DateTime start = formatter.parseDateTime((String) pPageContext.getSession().getAttribute(
				FinancialacceleratorstorefrontConstants.TRIP_DETAILS_START_DATE));
		final DateTime end = formatter.parseDateTime((String) pPageContext.getSession().getAttribute(
				FinancialacceleratorstorefrontConstants.TRIP_DETAILS_END_DATE));
		final int diffInDays = Integer.valueOf(Days.daysBetween(start, end).getDays());
		pPageContext.getSession().setAttribute(FinancialacceleratorstorefrontConstants.TRIP_DETAILS_NO_OF_DAYS,
				Integer.toString(diffInDays));

		final SimpleDateFormat sdf = new SimpleDateFormat(getDateFormatForDisplay());
		pPageContext.getSession().setAttribute(FinancialacceleratorstorefrontConstants.TRIP_DETAILS_START_DATE,
				sdf.format(start.toDate()));
		pPageContext.getSession().setAttribute(FinancialacceleratorstorefrontConstants.TRIP_DETAILS_END_DATE,
				sdf.format(end.toDate()));
		return Integer.toString(diffInDays);
	}
}
