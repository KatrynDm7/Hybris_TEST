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
import de.hybris.platform.financialservices.constants.FinancialservicesConstants;
import de.hybris.platform.financialservices.model.components.CMSAutoDetailsSubmitComponentModel;

import javax.servlet.jsp.PageContext;
import javax.xml.xpath.XPath;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;


/**
 * The class of AutoDetailsSubmitComponentRenderer.
 */
public class AutoDetailsSubmitComponentRenderer<C extends CMSAutoDetailsSubmitComponentModel> extends
		AbstractFormSubmitComponentRenderer<C>
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(AutoDetailsSubmitComponentRenderer.class);

	@Override
	protected void setSessionAttributes(final String pString, final PageContext pPageContext, final C component)
	{
		//Create DOM document
		final Document document = createDocument(pString);

		// Create XPath object
		final XPath xpath = createXPath();

		// Stash the nodes..
		safelyStashNodeDataInSession(pPageContext, FinancialservicesConstants.AUTO_DRIVER_DOB,
				getNodeList(xpath, document, "/form/section-1/driver-date-of-birth/text()"));
		safelyStashNodeDataInSession(pPageContext, FinancialservicesConstants.AUTO_COVER_START,
				getNodeList(xpath, document, "/form/section-1/coverage-start-date/text()"));
		safelyStashNodeDataInSession(pPageContext, FinancialservicesConstants.AUTO_VEHICLE_VALUE,
				getNodeList(xpath, document, "/form/section-1/vehicle-value/text()"));
		safelyStashNodeDataInSession(pPageContext, FinancialservicesConstants.AUTO_VEHICLE_MAKE,
				getNodeList(xpath, document, "/form/section-1/vehicle-make/text()"));
		safelyStashNodeDataInSession(pPageContext, FinancialservicesConstants.AUTO_VEHICLE_MODEL,
				getNodeList(xpath, document, "/form/section-1/vehicle-model/text()"));
		safelyStashNodeDataInSession(pPageContext, FinancialservicesConstants.AUTO_VEHICLE_LICENSE,
				getNodeList(xpath, document, "/form/section-1/vehicle-license/text()"));
		safelyStashNodeDataInSession(pPageContext, FinancialservicesConstants.AUTO_VEHICLE_YEAR,
				getNodeList(xpath, document, "/form/section-1/year-manufacture/text()"));
		safelyStashNodeDataInSession(pPageContext, FinancialservicesConstants.AUTO_STATE,
				getNodeList(xpath, document, "/form/section-1/state/text()"));

	}

	@Override
	protected String getSessionFormId(final PageContext pageContext, final C component)
	{
		return (String) pageContext.getSession().getAttribute(FinancialacceleratorstorefrontConstants.AUTO_DETAILS_FORM_DATA_ID);
	}

	@Override
	protected void storeSessionFormId(final PageContext pageContext, final String newSessionFormDataId, final C component)
	{
		pageContext.getSession().setAttribute(FinancialacceleratorstorefrontConstants.AUTO_DETAILS_FORM_DATA_ID,
				newSessionFormDataId);
		getSessionService().setAttribute(FinancialacceleratorstorefrontConstants.AUTO_DETAILS_FORM_DATA_ID, newSessionFormDataId);
	}
}
