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
package de.hybris.platform.financialfacades.email;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class FindAgentMailEventBuilder
{

	private static final Logger LOGGER = Logger.getLogger(FindAgentMailEventBuilder.class);

	private String agentEmail;
	private String userMessage;
	private String userEmail;
	private String callback;
	private String phone;
	private String userName;
	private String interest;

	public FindAgentMailEventBuilder setAgentEmail(final String agentEmail)
	{
		this.agentEmail = agentEmail;
		return this;
	}

	public FindAgentMailEventBuilder setUserMessage(final String userMessage)
	{
		this.userMessage = userMessage;
		return this;
	}

	public FindAgentMailEventBuilder setUserEmail(final String userEmail)
	{
		this.userEmail = userEmail;
		return this;
	}

	public FindAgentMailEventBuilder setCallback(final String callback)
	{
		this.callback = callback;
		return this;
	}

	public FindAgentMailEventBuilder setPhone(final String phone)
	{
		this.phone = phone;
		return this;
	}

	public FindAgentMailEventBuilder setUserName(final String userName)
	{
		this.userName = userName;
		return this;
	}

	public FindAgentMailEventBuilder setInterest(final String interest)
	{
		this.interest = interest;
		return this;
	}

	public FindAgentMailEvent createFindAgentMailEvent()
	{
		return new FindAgentMailEvent(agentEmail, userMessage, userEmail, callback, phone, userName, interest);
	}


	public FindAgentMailEvent build(final String content)
	{
		final Document document = createDomDocument(content);

		final XPath xpath = createXPathObject();

		final String agentUid = getFirstNodeValueByPath(xpath, document, "/form/contact-expert/agent/text()", "");
		final String userEmail = getFirstNodeValueByPath(xpath, document, "/form/contact-expert/email-address/text()", "");
		final String message = getFirstNodeValueByPath(xpath, document, "/form/contact-expert/message/text()", "");
		final String callback = getFirstNodeValueByPath(xpath, document, "/form/contact-expert/call-back/text()", "");
		final String phone = getFirstNodeValueByPath(xpath, document, "/form/contact-expert/phone/text()", "");
		final String userName = getFirstNodeValueByPath(xpath, document, "/form/contact-expert/name/text()", "");
		final String interest = getFirstNodeValueByPath(xpath, document, "/form/contact-expert/interest/text()", "");


		return new FindAgentMailEventBuilder().setAgentEmail(agentUid).setUserMessage(message).setUserEmail(userEmail)
				.setCallback(callback).setPhone(phone).setUserName(userName).setInterest(interest).createFindAgentMailEvent();
	}

	protected String getFirstNodeValueByPath(final XPath xpath, final Document document, final String path,
			final String defaultValue)
	{
		final NodeList nlist = getNodeList(xpath, document, path);
		final Node item = nlist.item(0);
		return (item != null) ? StringEscapeUtils.escapeXml(item.getNodeValue()) : defaultValue;
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
		final XPathExpression expr;
		NodeList nodes = null;

		try
		{
			expr = xpath.compile(expression);
			nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		}
		catch (final XPathExpressionException e)
		{
			LOGGER.error(e.getMessage(), e);
		}

		return nodes;
	}

	protected XPath createXPathObject()
	{
		final XPathFactory xpathFactory = XPathFactory.newInstance();


		return xpathFactory.newXPath();
	}

	protected Document createDomDocument(final String xml)
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
			LOGGER.error(e.getMessage(), e);
		}

		return document;
	}

}