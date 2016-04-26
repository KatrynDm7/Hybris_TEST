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
import de.hybris.platform.financialservices.model.components.CMSPropertyDetailsSubmitComponentModel;

import java.util.List;
import java.util.Map;

import javax.servlet.jsp.PageContext;
import javax.xml.xpath.XPath;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.google.common.collect.Maps;


/**
 * The class of PropertyDetailsSubmitComponentRenderer.
 */
public class PropertyDetailsSubmitComponentRenderer<C extends CMSPropertyDetailsSubmitComponentModel> extends
		AbstractFormSubmitComponentRenderer<C>
{
	private static final Logger LOG = Logger.getLogger(PropertyDetailsSubmitComponentRenderer.class);

	private Map<String, List<String>> insurancePropertyDetailsKeysMap;

	@Override
	protected String getSessionFormId(final PageContext pageContext, final C component)
	{
		return (String) pageContext.getSession().getAttribute(buildSessionFormDataId(component));
	}

	@Override
	protected void storeSessionFormId(final PageContext pageContext, final String newSessionFormDataId, final C component)
	{
		pageContext.getSession().setAttribute(buildSessionFormDataId(component), newSessionFormDataId);
		getSessionService().setAttribute(buildSessionFormDataId(component), newSessionFormDataId);
	}

	protected String buildSessionFormDataId(final C component)
	{
		return getComponentCategoryCode(component) + FinancialacceleratorstorefrontConstants.PROPERTY_DETAILS_FORM_DATA_ID;
	}

	@Override
	protected void setSessionAttributes(final String pString, final PageContext pPageContext, final C component)
	{
		try
		{
			setQuotationAndAddressAttributes(pString, pPageContext);

			final Map<String, String> sessionMap = Maps.newLinkedHashMap();
			final String categoryCode = getComponentCategoryCode(component);
			if (StringUtils.isNotEmpty(categoryCode) && getInsurancePropertyDetailsKeysMap().containsKey(categoryCode))
			{
				for (final String formKey : getInsurancePropertyDetailsKeysMap().get(categoryCode))
				{
					setSessionAttribute(pString, component, formKey, sessionMap);
				}

				pPageContext.getSession().setAttribute(FinancialacceleratorstorefrontConstants.PROPERTY_FORM_SESSION_MAP, sessionMap);
				getSessionService().setAttribute(FinancialacceleratorstorefrontConstants.PROPERTY_FORM_SESSION_MAP, sessionMap);
			}
		}
		catch (final DOMException e)
		{
			LOG.error(e.getMessage(), e);
		}
	}

	protected void setQuotationAndAddressAttributes(final String pString, final PageContext pPageContext)
	{
		//Create DOM document
		final Document document = createDocument(pString);

		// Create XPath object
		final XPath xpath = createXPath();

		// Quotation attributes
		safelyStashNodeDataInSession(pPageContext, FinancialacceleratorstorefrontConstants.PROPERTY_DETAILS_VALUE,
				getNodeList(xpath, document, "/form/property-details-section/property-value/text()"));
		safelyStashNodeDataInSession(pPageContext, FinancialacceleratorstorefrontConstants.PROPERTY_DETAILS_TYPE,
				getNodeList(xpath, document, "/form/property-details-section/property-type/text()"));
		safelyStashNodeDataInSession(pPageContext, FinancialacceleratorstorefrontConstants.PROPERTY_DETAILS_REBUILD_COST,
				getNodeList(xpath, document, "/form/property-details-section/rebuild-value-of-property/text()"));

		safelyStashNodeDataInSession(pPageContext,
				FinancialacceleratorstorefrontConstants.PROPERTY_DETAILS_IS_STANDARD_50000_CONTENT_COVER,
				getNodeList(xpath, document, "/form/property-contents-cover-section/standard-50000-contents-cover/text()"));
		safelyStashNodeDataInSession(pPageContext,
				FinancialacceleratorstorefrontConstants.PROPERTY_DETAILS_CONTENT_COVER_MULTIPLE_OF_10000,
				getNodeList(xpath, document, "/form/property-contents-cover-section/amount-multiples-10000/text()"));

		// Address attributes
		safelyStashNodeDataInSession(pPageContext, FinancialacceleratorstorefrontConstants.PROPERTY_ADDRESS1,
				getNodeList(xpath, document, "/form/property-address-section/property-address-line-1/text()"));
		safelyStashNodeDataInSession(pPageContext, FinancialacceleratorstorefrontConstants.PROPERTY_ADDRESS2,
				getNodeList(xpath, document, "/form/property-address-section/property-address-line-2/text()"));
		safelyStashNodeDataInSession(pPageContext, FinancialacceleratorstorefrontConstants.PROPERTY_CITY,
				getNodeList(xpath, document, "/form/property-address-section/property-address-city/text()"));
		safelyStashNodeDataInSession(pPageContext, FinancialacceleratorstorefrontConstants.PROPERTY_POSTCODE,
				getNodeList(xpath, document, "/form/property-address-section/property-address-postcode/text()"));
		safelyStashNodeDataInSession(pPageContext, FinancialacceleratorstorefrontConstants.PROPERTY_COUNTRY,
				getNodeList(xpath, document, "/form/property-address-section/property-address-country/text()"));

		// PDF generation attributes
		safelyStashNodeDataInSession(pPageContext, FinancialacceleratorstorefrontConstants.PROPERTY_DETAILS_COVER_REQUIRED,
				getNodeList(xpath, document, "/form/property-coverage-details-section/property-cover-required/text()"));
		safelyStashNodeDataInSession(pPageContext, FinancialacceleratorstorefrontConstants.PROPERTY_DETAILS_START_DATE,
				getNodeList(xpath, document, "/form/property-coverage-details-section/property-cover-start-date/text()"));
	}

	protected Map<String, List<String>> getInsurancePropertyDetailsKeysMap()
	{
		return insurancePropertyDetailsKeysMap;
	}

	@Required
	public void setInsurancePropertyDetailsKeysMap(final Map<String, List<String>> insurancePropertyDetailsKeysMap)
	{
		this.insurancePropertyDetailsKeysMap = insurancePropertyDetailsKeysMap;
	}
}
