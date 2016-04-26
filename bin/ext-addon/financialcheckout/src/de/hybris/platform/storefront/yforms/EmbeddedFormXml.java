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
package de.hybris.platform.storefront.yforms;

import de.hybris.platform.xyformsservices.enums.YFormDataTypeEnum;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


/**
 * This class wraps an XML DOM Document and provides xpath based convenience methods to return value from that Document.
 * This allows the <YFormDataData> to be exposed for a particular form in a generic way without manually keeping objects
 * and definitions in sync.
 *
 */
public class EmbeddedFormXml
{
	private static final Logger LOG = Logger.getLogger(EmbeddedFormXml.class);

	public static final String EMBEDDED_FORM_XMLS = "embeddedFormXmls";

	private final XPath xpath;

	private Document document;
	private String applicationId;
	private String formId;
	private String dataId;

	private YFormDataTypeEnum dataType;

	/**
	 * Default constructor which ensures that the xpath processing is set up and is ready for use.
	 */
	public EmbeddedFormXml()
	{
		this.xpath = XPathFactory.newInstance().newXPath();
	}

	/**
	 * Convenience constructor that allows an instance to be created in full.
	 *
	 * @param applicationId
	 *           the <applicationId> e.g. Insurance
	 * @param formId
	 *           the form in question e.g. 'Event-Form'
	 * @param dataId
	 *           the yFormData object data id
	 * @param document
	 *           the XML DOM document object
	 * @param dataType
	 *           the type of data - either DRAFT or DATA
	 */
	public EmbeddedFormXml(final String applicationId, final String formId, final String dataId, final Document document,
			final YFormDataTypeEnum dataType)
	{
		this();
		this.setApplicationId(applicationId);
		this.setFormId(formId);
		this.setDataId(dataId);
		this.setDocument(document);
		this.setDataType(dataType);
	}

	/**
	 * The principle check needed for a form - if the form is not committed, then it is not possible to extract values
	 * from the form as it will have an empty 'DATA' content with all the values in the 'DRAFT' version. Practically,
	 * this checks the xml document to see if it is present. If so, then the method returns true, otherwise it returns
	 * false.
	 *
	 * @return true if xml content is available to parse, false otherwise.
	 */
	public boolean isCommitted()
	{
		return (document == null) ? false : true;
	}

	/**
	 * Uses the supplied xpath expression to return a string value for the supplied expression where the expression
	 * refers only to a single element in the DOM.
	 *
	 * @param xpathExpression
	 *           expression to use to evaluate the eingle value
	 * @return the single value required
	 *
	 */
	public String safelyEvaluateSingle(final String xpathExpression)
	{
		String result = null;
		try
		{
			result = xpath.compile(xpathExpression).evaluate(document);
		}
		catch (final XPathExpressionException e)
		{
			LOG.error("Unable to resolve xpath to a single item for : " + xpathExpression, e);
		}
		return (result == null) ? "" : StringEscapeUtils.escapeXml(result);
	}

	/**
	 * Uses the supplied xpath expression to return a <List> of strings which correspond the to the various values for
	 * the supplied expression in the current document.
	 *
	 * @param xpathExpression
	 *           a <List> of <String> items - one for each element found
	 * @return the <List> of <String> values required
	 */
	public List<String> safelyEvaluateMultiple(final String xpathExpression)
	{
		final List<String> result = new ArrayList<String>();
		try
		{
			final NodeList nodeList = (NodeList) xpath.compile(xpathExpression).evaluate(document, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++)
			{
				final String nodeValue = nodeList.item(i).getFirstChild().getNodeValue();
				result.add(StringEscapeUtils.escapeXml(nodeValue));
			}
		}
		catch (final XPathExpressionException e)
		{
			LOG.error("Unable to resolve xpath to multiple items for : " + xpathExpression, e);
		}
		return result;
	}


	/**
	 * @return the document
	 */
	public Document getDocument()
	{
		return document;
	}

	/**
	 * @param document
	 *           the document to set
	 */
	public void setDocument(final Document document)
	{
		this.document = document;
	}


	/**
	 * @return the applicationId
	 */
	public String getApplicationId()
	{
		return applicationId;
	}

	/**
	 * @param applicationId
	 *           the applicationId to set
	 */
	public void setApplicationId(final String applicationId)
	{
		this.applicationId = applicationId;
	}

	/**
	 * @return the formId
	 */
	public String getFormId()
	{
		return formId;
	}

	/**
	 * @param formId
	 *           the formId to set
	 */
	public void setFormId(final String formId)
	{
		this.formId = formId;
	}

	/**
	 * @return the dataId
	 */
	public String getDataId()
	{
		return dataId;
	}

	/**
	 * @param dataId
	 *           the dataId to set
	 */
	public void setDataId(final String dataId)
	{
		this.dataId = dataId;
	}

	/**
	 * @return the dataType
	 */
	public YFormDataTypeEnum getDataType()
	{
		return dataType;
	}

	/**
	 * @param dataType
	 *           the dataType to set
	 */
	public void setDataType(final YFormDataTypeEnum dataType)
	{
		this.dataType = dataType;
	}

}
