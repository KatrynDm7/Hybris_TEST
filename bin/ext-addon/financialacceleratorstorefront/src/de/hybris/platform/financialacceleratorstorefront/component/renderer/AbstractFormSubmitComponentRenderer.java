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

import de.hybris.platform.financialservices.model.components.CMSFormSubmitComponentModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.xyformsfacades.data.YFormDataData;
import de.hybris.platform.xyformsfacades.form.YFormFacade;
import de.hybris.platform.xyformsservices.enums.YFormDataTypeEnum;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;
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
import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * The class of FormSubmitComponentRenderer.
 */
public abstract class AbstractFormSubmitComponentRenderer<C extends CMSFormSubmitComponentModel> extends
		DefaultAddOnSubstitutingCMSComponentRenderer<C>
{

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(AbstractFormSubmitComponentRenderer.class);

	private YFormFacade yformFacade;

	private SessionService sessionService;

	private String dateFormatForDisplay;

	protected static final String VIEW_STATUS = "viewStatus";
	protected static final String VIEW_STATUS_VIEW = "view";
	protected static final String TEXT = "/text()";

	@Override
	public void renderComponent(final PageContext pageContext, final C component) throws ServletException, IOException
	{
		final String viewStatus = pageContext.getRequest().getParameter(VIEW_STATUS);
		final String sessionFormDataId = getSessionFormId(pageContext, component);
		final String inlineHtml;

		final String applicationId = component.getApplicationId();
		final String formId = component.getFormId();

		try
		{
			// assumes we must have  session ID otherwise we would need to show a form
			if (viewStatus != null && viewStatus.equals(VIEW_STATUS_VIEW))
			{
				final YFormDataData yfdd = getYformFacade().getYFormData(sessionFormDataId, YFormDataTypeEnum.DATA);
				setSessionAttributes(yfdd.getContent(), pageContext, component);
			}
			else
			// this is in the edit view
			{
				// if I have a sessionFormDataId
				if (sessionFormDataId != null)
				{
					// attempt to get an existing trip form if possible
					if (getYformFacade().getYFormData(sessionFormDataId, YFormDataTypeEnum.DATA) != null)
					{
						inlineHtml = getYformFacade().getInlineFormHtml(component.getApplicationId(), component.getFormId(),
								sessionFormDataId);
						writeContent(pageContext, inlineHtml);
					}
				}
				else
				// this is where it's edit view and have never filled in a form
				{
					final String newSessionFormDataId = getYformFacade().getNewFormDataId();
					storeSessionFormId(pageContext, newSessionFormDataId, component);
					final YFormDataData yfdd = getYformFacade().createYFormData(applicationId, formId, newSessionFormDataId,
							YFormDataTypeEnum.DATA, null, null);

					final String content = getYformFacade().getInlineFormHtml(applicationId, formId, yfdd.getId());

					writeContent(pageContext, content);
				}
			}
		}
		catch (final YFormServiceException e)
		{
			LOG.error(e.getMessage(), e);
		}

		super.renderComponent(pageContext, component);
	}

	protected abstract void setSessionAttributes(final String pString, final PageContext pPageContext, final C component);

	protected abstract String getSessionFormId(final PageContext pageContext, final C component);

	protected abstract void storeSessionFormId(final PageContext pageContext, final String newSessionFormDataId, final C component);

	protected void writeContent(final PageContext pageContext, final String value) throws IOException
	{
		pageContext.getOut().write(value);
	}

	/**
	 * Method which extracts the node from the document for the given xpath.
	 *
	 * @param xpath
	 *           the xpath
	 * @param document
	 *           the document
	 * @param expression
	 *           the expression xpath
	 * @return NodeList
	 */
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

	protected String buildAttributeKey(final String str)
	{
		if (str != null)
		{
			return str.replaceAll("[^A-Za-z0-9]", "_").toLowerCase();
		}

		return null;
	}

	protected void setSessionAttribute(final String pString, final C component, final String key,
			final Map<String, String> sessionMap)
	{
		if (key != null)
		{
			//Create DOM document
			final Document document = createDocument(pString);

			// Create XPath object
			final XPath xpath = createXPath();

			final NodeList nodes = getNodeList(xpath, document, key + TEXT);
			String value = StringUtils.EMPTY;
			if (nodes != null && nodes.getLength() >= NumberUtils.INTEGER_ONE)
			{
				value = StringEscapeUtils.escapeXml(nodes.item(0).getNodeValue());
			}

			final String cleanedKey = buildAttributeKey(getComponentCategoryCode(component) + key);

			sessionMap.put(cleanedKey, value);
		}
	}

	protected String getComponentCategoryCode(final C component)
	{
		String prefix = StringUtils.EMPTY;
		if (component.getCategory() != null)
		{
			prefix = component.getCategory().getCode();
		}

		return prefix;
	}
    
    protected String safelyGetFirstNodeValue(final NodeList nodes)
   	{
   		final Node item = nodes.item(0);
   		return (item == null) ? StringUtils.EMPTY : StringEscapeUtils.escapeXml(item.getNodeValue());
   	}
    
	/**
	 * Places the data temporarily in the session so that it can be picked up once the item is actually added to the cart
	 *
	 * @param sessionKey
	 *           key for session
	 * @param nodeList
	 *           xml
	 */
	protected void safelyStashNodeDataInSession(final PageContext pageContext, final String sessionKey, final NodeList nodeList)
	{
		if (nodeList.item(0) != null)
		{
			final String nodeValue = StringEscapeUtils.escapeXml(nodeList.item(0).getNodeValue());
			if (StringUtils.isNotEmpty(nodeValue))
			{
				pageContext.getSession().setAttribute(sessionKey, nodeValue);
				getSessionService().setAttribute(sessionKey, nodeValue);
			}
		}
		else
		{
			getSessionService().removeAttribute(sessionKey);
		}
	}

	protected String getDateFormatForDisplay()
	{
		return dateFormatForDisplay;
	}

	@Required
	public void setDateFormatForDisplay(final String dateFormatForDisplay)
	{
		this.dateFormatForDisplay = dateFormatForDisplay;
	}

	protected YFormFacade getYformFacade()
	{
		return yformFacade;
	}

	@Required
	public void setYformFacade(final YFormFacade yformFacade)
	{
		this.yformFacade = yformFacade;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}
