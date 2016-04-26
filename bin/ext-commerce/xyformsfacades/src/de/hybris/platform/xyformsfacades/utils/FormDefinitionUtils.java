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
 */
package de.hybris.platform.xyformsfacades.utils;

import de.hybris.platform.xyformsfacades.data.YFormDefinitionData;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.EscapeStrategy;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


/**
 * Static class used to interact with yForm Definition and yForm Data
 */
public class FormDefinitionUtils
{
	/**
	 * Given a content it returns a new content containing the meta information provided by the given yForm Definition
	 * 
	 * @param content
	 * @param yformDefinition
	 * @throws YFormServiceException
	 */
	public static String getFormDefinitionContent(final String content, final YFormDefinitionData yformDefinition)
			throws YFormServiceException
	{
		// next step is to modify the metadata contained in the definition, so when publishing
		// the form will be correctly created.
		try
		{
			final SAXBuilder builder = new SAXBuilder();

			final Document doc = builder.build(IOUtils.toInputStream(content, "UTF-8"));
			final Namespace htmlNs = Namespace.getNamespace("xh", "http://www.w3.org/1999/xhtml");
			final Namespace xformsNs = Namespace.getNamespace("xf", "http://www.w3.org/2002/xforms");
			final Element rootNode = doc.getRootElement();

			final Element head = rootNode.getChild("head", htmlNs);
			final Element model = head.getChild("model", xformsNs);
			final List<Element> instances = model.getChildren("instance", xformsNs);
			Element metadata = null;
			for (final Element e : instances)
			{
				if ("fr-form-metadata".equals(e.getAttributeValue("id")))
				{
					metadata = e.getChild("metadata");

					break;
				}
			}

			if (metadata == null)
			{
				throw new YFormServiceException("Metadata TAG was not found for given formDefinition ["
						+ yformDefinition.getApplicationId() + "][" + yformDefinition.getFormId() + "]");
			}

			final Element description = metadata.getChild("description");
			final Element applicationName = metadata.getChild("application-name");
			final Element title = metadata.getChild("title");
			final Element formName = metadata.getChild("form-name");

			applicationName.setText(yformDefinition.getApplicationId());
			formName.setText(yformDefinition.getFormId());
			description.setText(yformDefinition.getDescription());
			title.setText(yformDefinition.getTitle());

			return getXML(doc);

		}
		catch (final IOException | JDOMException e)
		{
			throw new YFormServiceException(e);
		}
	}

	/**
	 * It returns the DOM representation of the Metadata TAG from the given content parameter.
	 * 
	 * @param content
	 * @throws YFormServiceException
	 */
	public static Element getFormDefinitionMetadata(final String content) throws YFormServiceException
	{
		try
		{
			final SAXBuilder builder = new SAXBuilder();

			final Document doc = builder.build(IOUtils.toInputStream(content, "UTF-8"));
			final Namespace htmlNs = Namespace.getNamespace("xh", "http://www.w3.org/1999/xhtml");
			final Namespace xformsNs = Namespace.getNamespace("xf", "http://www.w3.org/2002/xforms");
			final Element rootNode = doc.getRootElement();

			final Element head = rootNode.getChild("head", htmlNs);
			final Element model = head.getChild("model", xformsNs);
			final List<Element> instances = model.getChildren("instance", xformsNs);
			Element metadata = null;
			for (final Element e : instances)
			{
				if ("fr-form-metadata".equals(e.getAttributeValue("id")))
				{
					metadata = e.getChild("metadata");

					break;
				}
			}

			if (metadata == null)
			{
				throw new YFormServiceException("Metadata TAG was not found for given formDefinition");
			}

			return metadata.detach();
		}
		catch (final IOException | JDOMException e)
		{
			throw new YFormServiceException(e);
		}
	}

	/**
	 * Normalizes the given xml content. Useful when comparing contents that have been manipulated using other xml tools.
	 * 
	 * @param content
	 * @throws YFormServiceException
	 */
	public static String normalize(final String content) throws YFormServiceException
	{
		try
		{
			final SAXBuilder builder = new SAXBuilder();
			final Document doc = builder.build(IOUtils.toInputStream(content, "UTF-8"));
			return getXML(doc);
		}
		catch (final IOException | JDOMException e)
		{
			throw new YFormServiceException(e);
		}
	}

	/**
	 * Used to get the form definition given the content parameter.
	 * 
	 * @param content
	 * @throws YFormServiceException
	 */
	public static String getFormDefinition(final String content) throws YFormServiceException
	{
		try
		{
			final SAXBuilder builder = new SAXBuilder();

			final Document doc = builder.build(IOUtils.toInputStream(content, "UTF-8"));
			final Namespace htmlNs = Namespace.getNamespace("xh", "http://www.w3.org/1999/xhtml");
			final Namespace xformsNs = Namespace.getNamespace("xf", "http://www.w3.org/2002/xforms");
			final Element rootNode = doc.getRootElement();

			final Element head = rootNode.getChild("head", htmlNs);
			final Element model = head.getChild("model", xformsNs);
			final List<Element> instances = model.getChildren("instance", xformsNs);
			Element form = null;
			for (final Element e : instances)
			{
				if ("fr-form-instance".equals(e.getAttributeValue("id")))
				{
					form = e.getChild("form");

					break;
				}
			}

			if (form == null)
			{
				throw new YFormServiceException("Form TAG was not found for given formDefinition");
			}
			final Document doc2 = new Document();
			doc2.setRootElement(form.detach());

			return getXML(doc2);
		}
		catch (final IOException | JDOMException e)
		{
			throw new YFormServiceException(e);
		}
	}

	/**
	 * Given a DOM document it returns the String representation
	 * 
	 * @param doc
	 * @throws IOException
	 */
	public static String getXML(final Document doc) throws IOException
	{
		final XMLOutputter xmlOutput = new XMLOutputter();
		final ByteArrayOutputStream os = new ByteArrayOutputStream();

		// display nice nice
		final Format format = Format.getRawFormat().setEncoding("UTF-8").setOmitDeclaration(false).setOmitEncoding(false);
		format.setEscapeStrategy(new YFormEscapeStrategy());
		xmlOutput.setFormat(format);
		xmlOutput.output(doc, os);

		// new definition content.
		return os.toString("UTF-8");
	}

	/**
	 * It escapes non ISO-8859-1 characters
	 */
	private static class YFormEscapeStrategy implements EscapeStrategy
	{
		private static CharsetEncoder CHARSET_ENCODER = Charset.forName("US-ASCII").newEncoder();

		@Override
		public boolean shouldEscape(final char ch)
		{
			return !CHARSET_ENCODER.canEncode(ch);
		}
	}
}
