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

import de.hybris.platform.commercefacades.i18n.I18NFacade;
import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.xyformsfacades.data.YFormDataData;

import java.io.IOException;
import java.io.StringReader;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * The class of AbstractInsuranceDetailsPopulator.
 */
public abstract class AbstractInsuranceDetailsPopulator implements Populator<YFormDataData, InsurancePolicyData>
{
	@Resource(name = "i18NFacade")
	private I18NFacade i18NFacade;

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(AbstractInsuranceDetailsPopulator.class);

	protected final String TEXT = "/text()";

	protected String getTextValue(final XPath xpath, final Document document, final String expression)
	{
		final NodeList nodeList = getNodeList(xpath, document, expression + TEXT);
		if (nodeList != null && nodeList.getLength() > NumberUtils.INTEGER_ZERO)
		{
			final Node item = getNodeList(xpath, document, expression + TEXT).item(0);
			return StringEscapeUtils.escapeXml(item.getNodeValue());
		}
		else
		{
			return StringUtils.EMPTY;
		}
	}

	protected NodeList getNodeList(final XPath xpath, final Document document, final String expression)
	{
		XPathExpression expr;
		NodeList nodes = null;

		try
		{
			expr = xpath.compile(expression);
			nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		}
		catch (final XPathExpressionException e)
		{
			LOG.error(e.getMessage(), e);
		}

		return nodes;
	}

	protected XPath createXPath()
	{
		// Create XPathFactory object
		final XPathFactory xpathFactory = XPathFactory.newInstance();

		// Create XPath object
		return xpathFactory.newXPath();
	}

	protected Document createDocument(final String xml)
	{
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document document = null;

		try
		{
			builder = factory.newDocumentBuilder();
			document = builder.parse(new InputSource(new StringReader(xml)));
		}
		catch (final ParserConfigurationException | SAXException | IOException e)
		{
			LOG.error(e.getMessage(), e);
		}

		return document;
	}

	protected String getCountryNameForIsocode(final String countryIsoCode)
	{
		if (StringUtils.isNotBlank(countryIsoCode))
		{
			try
			{
				final CountryData countryData = getI18NFacade().getCountryForIsocode(countryIsoCode);
				if (countryData != null)
				{
					return countryData.getName();
				}
			}
			catch (final UnknownIdentifierException ex)
			{
				LOG.error(ex.getMessage(), ex);
			}
		}

		return countryIsoCode;
	}

	/**
	 * @return the i18NFacade
	 */
	protected I18NFacade getI18NFacade()
	{
		return i18NFacade;
	}

	/**
	 * @param i18nFacade
	 *           the i18NFacade to set
	 */
	protected void setI18NFacade(final I18NFacade i18nFacade)
	{
		i18NFacade = i18nFacade;
	}
}
