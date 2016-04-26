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

import java.io.IOException;
import java.io.StringReader;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * This is a helper class for the <EmbeddedFormXml> type and is used to parse the XML content of a <YFormDataData>
 * object into a standard XML DOM Document.
 *
 */
public class EmbeddedFormXmlParser
{
	private static final Logger LOG = Logger.getLogger(EmbeddedFormXmlParser.class);
	private DocumentBuilder builder;

	/**
	 * This method is called once the bean has been constructed and ensures that the members are correctly set up before
	 * the <parse> call is made.
	 */
	@PostConstruct
	public void init()
	{
		try
		{
			LOG.info("Initializing embedded form XML parser.");
			final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			this.setBuilder(builderFactory.newDocumentBuilder());
		}
		catch (final ParserConfigurationException e)
		{
			LOG.error("Unable to build new document for builder factory.", e);
		}

	}

	/**
	 * This method accepts a string which contains XML content and parses it to return a standard DOM Document which can
	 * then be used in a standard <EmbeddedFormXml> bean.
	 *
	 * @param xmlContent
	 *           a string containing xml content
	 * @return an XML Dom <Document> which can be consumed by an <EmbeddedFormXml> bean. If parsing fails, or an empty
	 *         document is passed in, OR if the initialization has failed to produce a valid <DocumentBuilder>,then a
	 *         NULL is returned instead
	 */
	public synchronized Document parseContent(final String xmlContent)
	{
		if (this.getBuilder() != null && xmlContent != null && xmlContent.length() > 0)
		{
			try
			{
				final Document xmlDocument = this.getBuilder().parse(new InputSource(new StringReader(xmlContent)));
				return xmlDocument;
			}
			catch (SAXException | IOException e)
			{
				LOG.error("Unable to parse xml content for : " + xmlContent, e);
			}
		}
		return null;
	}

	/**
	 * @return the builder
	 */
	public DocumentBuilder getBuilder()
	{
		return builder;
	}

	/**
	 * @param builder
	 *           the builder to set
	 */
	public void setBuilder(final DocumentBuilder builder)
	{
		this.builder = builder;
	}
}
