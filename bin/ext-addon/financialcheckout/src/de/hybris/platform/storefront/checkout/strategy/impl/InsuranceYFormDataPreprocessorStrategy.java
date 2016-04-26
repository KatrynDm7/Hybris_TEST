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
package de.hybris.platform.storefront.checkout.strategy.impl;

import de.hybris.platform.xyformsfacades.strategy.preprocessor.ReferenceIdTransformerYFormPreprocessorStrategy;
import de.hybris.platform.xyformsfacades.strategy.preprocessor.YFormProcessorException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * The class of InsuranceYFormDataPreprocessorStrategy.
 */
public class InsuranceYFormDataPreprocessorStrategy extends ReferenceIdTransformerYFormPreprocessorStrategy
{
	private static final Logger LOG = Logger.getLogger(InsuranceYFormDataPreprocessorStrategy.class);

	public static final String FORM_DETAIL_DATA = "formDetailData";

	/**
	 * Update the XML string by using the given params.
	 *
	 * @param xmlContent
	 *           xml content string
	 * @param params
	 *           the params map
	 * @return updated xml content string
	 * @throws YFormProcessorException
	 */
	protected String updateXmlContent(final String xmlContent, final Map<String, Object> params) throws YFormProcessorException
	{
		if (xmlContent == null || MapUtils.isEmpty(params))
		{
			return xmlContent;
		}

		final Document document = createDocument(xmlContent);

		// Create XPath object
		final XPath xpath = createXPath();

		for (final Map.Entry<String, Object> entry : params.entrySet())
		{
			if (entry.getValue() == null)
			{
				continue;
			}

			final NodeList nodeList = getNodeList(xpath, document, entry.getKey());

			if (nodeList == null || nodeList.item(0) == null)
			{
				LOG.warn("Cannot find node with xpath [" + entry.getKey() + "]!");
				continue;
			}

			final NodeList nodeTextList = getNodeList(xpath, document, entry.getKey() + "/text()");

			if (nodeTextList.item(0) != null)
			{
				nodeTextList.item(0).setNodeValue(entry.getValue().toString());
			}
			else
			{
				LOG.debug("Cannot find node with xpath [" + entry.getKey() + "], append the node.");
				final Text text = document.createTextNode(entry.getValue().toString());
				final Element p = document.createElement(nodeList.item(0).getNodeName());

				p.appendChild(text);
				nodeList.item(0).getParentNode().insertBefore(p, nodeList.item(0));
				nodeList.item(0).getParentNode().removeChild(nodeList.item(0));
			}
		}

		String xmlResult = xmlContent;

		try
		{
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			final TransformerFactory factory = TransformerFactory.newInstance();
			factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			final Transformer transformer = factory.newTransformer();
			transformer.transform(new DOMSource(document), new StreamResult(os));

			xmlResult = os.toString("UTF-8");
		}
		catch (TransformerException | UnsupportedEncodingException e)
		{
			LOG.error(e.getMessage(), e);
		}

		return xmlResult;
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
}
