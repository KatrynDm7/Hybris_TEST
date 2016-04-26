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
import de.hybris.platform.financialservices.model.components.CMSLifeDetailsSubmitComponentModel;

import javax.servlet.jsp.PageContext;
import javax.xml.xpath.XPath;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


/**
 * The class of LifeDetailsSubmitComponentRenderer.
 */
public class LifeDetailsSubmitComponentRenderer<C extends CMSLifeDetailsSubmitComponentModel> extends
		AbstractFormSubmitComponentRenderer<C>
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(LifeDetailsSubmitComponentRenderer.class);

    private static final String TEXT = "/text()";
    private static final String COVERAGE_INFO_SECTION = "/form/coverage-information-section/";
	
    @Override
	protected void setSessionAttributes(final String pString, final PageContext pPageContext, final C component)
	{
		//Create DOM document
		final Document document = createDocument(pString);

		// Create XPath object
		final XPath xpath = createXPath();

		safelyStashNodeDataInSession(pPageContext, FinancialservicesConstants.LIFE_WHO_COVERED,
				getNodeList(xpath, document, COVERAGE_INFO_SECTION + "who-is-being-covered" + TEXT));
		safelyStashNodeDataInSession(pPageContext, FinancialservicesConstants.LIFE_COVERAGE_REQUIRE,
				getNodeList(xpath, document, COVERAGE_INFO_SECTION + "how-much-coverage-require" + TEXT));
		safelyStashNodeDataInSession(pPageContext, FinancialservicesConstants.LIFE_COVERAGE_LAST,
				getNodeList(xpath, document, COVERAGE_INFO_SECTION + "low-long-coverage-last" + TEXT));
		safelyStashNodeDataInSession(pPageContext, FinancialservicesConstants.LIFE_COVERAGE_START_DATE,
				getNodeList(xpath, document, COVERAGE_INFO_SECTION + "coverage-start-date" + TEXT));
		safelyStashNodeDataInSession(pPageContext, FinancialservicesConstants.LIFE_MAIN_DOB,
				getNodeList(xpath, document, COVERAGE_INFO_SECTION + "date-of-birth" + TEXT));
		safelyStashNodeDataInSession(pPageContext, FinancialservicesConstants.LIFE_MAIN_SMOKE,
				getNodeList(xpath, document, COVERAGE_INFO_SECTION + "do-you-smoke" + TEXT));
		safelyStashNodeDataInSession(pPageContext, FinancialservicesConstants.LIFE_SECOND_DOB,
				getNodeList(xpath, document, COVERAGE_INFO_SECTION + "second-persons-date-of-birth" + TEXT));
		safelyStashNodeDataInSession(pPageContext, FinancialservicesConstants.LIFE_SECOND_SMOKE,
				getNodeList(xpath, document, COVERAGE_INFO_SECTION + "second-person-smoke" + TEXT));
		safelyStashNodeDataInSession(pPageContext, FinancialservicesConstants.LIFE_RELATIONSHIP,
				getNodeList(xpath, document, COVERAGE_INFO_SECTION + "relationship-to-second-person" + TEXT));

	}

	@Override
	protected String getSessionFormId(final PageContext pageContext, final C component)
	{
		return (String) pageContext.getSession().getAttribute(FinancialacceleratorstorefrontConstants.LIFE_DETAILS_FORM_DATA_ID);
	}

	@Override
	protected void storeSessionFormId(final PageContext pageContext, final String newSessionFormDataId, final C component)
	{
		pageContext.getSession().setAttribute(FinancialacceleratorstorefrontConstants.LIFE_DETAILS_FORM_DATA_ID,
				newSessionFormDataId);
		getSessionService().setAttribute(FinancialacceleratorstorefrontConstants.LIFE_DETAILS_FORM_DATA_ID, newSessionFormDataId);
	}
}
