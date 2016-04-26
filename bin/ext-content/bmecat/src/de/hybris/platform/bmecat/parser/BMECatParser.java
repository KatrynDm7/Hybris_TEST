/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.bmecat.parser;

import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.ParseFinishedException;
import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.bmecat.parser.taglistener.ArticleUserDefinedExtensionsTagListener;
import de.hybris.platform.bmecat.parser.taglistener.CatalogStructureUserDefinedExtensionsTagListener;
import de.hybris.platform.bmecat.parser.taglistener.HeaderUserDefinedExtensionsTagListener;
import de.hybris.platform.cronjob.jalo.AbortCronJobException;
import de.hybris.platform.jalo.JaloSystemException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;


/**
 * This class parses the BMECat xml file. The BMEcat tags are processed by the
 * {@link de.hybris.platform.bmecat.parser.BMECatContentHandler}, the result of the handler returned through the
 * interface {@link de.hybris.platform.bmecat.parser.BMECatObjectProcessor} to each
 * {@link de.hybris.platform.bmecat.jalo.BMECatImportStep}.
 * 
 * 
 */
public class BMECatParser
{
	private XMLReader parser = null;

	private HeaderUserDefinedExtensionsTagListener headerUserDefinedExtensionsTagListener;
	private ArticleUserDefinedExtensionsTagListener articleUserDefinedExtensionsTagListener;
	private CatalogStructureUserDefinedExtensionsTagListener catalogStructureUserDefinedExtensionsTagListener;

	protected BMECatParser()
	{ /* nothing to do here */
	}

	public BMECatParser(final BMECatObjectProcessor processor)
	{
		this(processor, null, null);
	}

	public BMECatParser(final BMECatObjectProcessor processor, final Map props)
	{
		init(processor, props, null);
	}

	public BMECatParser(final BMECatObjectProcessor processor, final BMECatContentHandler handler)
	{
		init(processor, null, handler);
	}

	public BMECatParser(final BMECatObjectProcessor processor, final Map props, final BMECatContentHandler handler)
	{
		init(processor, props, handler);
	}

	private void init(final BMECatObjectProcessor processor, final Map props, final BMECatContentHandler handler)
	{
		//use the defaultparser
		createParser(null);

		// BUGFIX: Migration JDK 1.4 -> JDK 1.5, using the jdk default-parser all the time!
		//		if( System.getProperty("java.version").indexOf("1.3")>-1 )
		//		{	
		//			createParser(null);
		//		}
		//		else
		//		{
		//			createParser( BMECatConstants.PARSER.DEFAULT_PARSER_NAME );
		//		}

		if (props != null)
		{
			setFeatures(props);
		}
		else
		{
			setDefaultFeatures();
		}
		if (handler == null)
		{
			setContentHandler(new BMECatContentHandler(processor));
		}
		else
		{
			setContentHandler(handler);
		}
	}

	protected void createParser(final String parserName)
	{
		try
		{
			if (parserName == null)
			{
				parser = XMLReaderFactory.createXMLReader();
			}
			else
			{
				parser = XMLReaderFactory.createXMLReader(parserName);
			}
		}
		catch (final Exception e)
		{
			System.err.println("error: Unable to instantiate parser (" + parserName + ")");
			e.printStackTrace();
		}
	}

	public void setFeatures(final Map props)
	{
		try
		{
			if (props.get(BMECatConstants.PARSER.NAMESPACES_FEATURE_ID) != null)
			{
				parser.setFeature(BMECatConstants.PARSER.NAMESPACES_FEATURE_ID,
						props.get(BMECatConstants.PARSER.NAMESPACES_FEATURE_ID).equals(Boolean.TRUE.toString()) ? true : false);
			}
		}
		catch (final SAXException e)
		{
			System.err.println("warning: Parser does not support feature (" + BMECatConstants.PARSER.NAMESPACES_FEATURE_ID + ")");
		}
		try
		{
			if (props.get(BMECatConstants.PARSER.NAMESPACE_PREFIXES_FEATURE_ID) != null)
			{
				parser.setFeature(BMECatConstants.PARSER.NAMESPACE_PREFIXES_FEATURE_ID,
						props.get(BMECatConstants.PARSER.NAMESPACE_PREFIXES_FEATURE_ID).equals(Boolean.TRUE.toString()) ? true : false);
			}
		}
		catch (final SAXException e)
		{
			System.err.println("warning: Parser does not support feature (" + BMECatConstants.PARSER.NAMESPACE_PREFIXES_FEATURE_ID
					+ ")");
		}
		try
		{
			if (props.get(BMECatConstants.PARSER.VALIDATION_FEATURE_ID) != null)
			{
				parser.setFeature(BMECatConstants.PARSER.VALIDATION_FEATURE_ID,
						props.get(BMECatConstants.PARSER.VALIDATION_FEATURE_ID).equals(Boolean.TRUE.toString()) ? true : false);
			}
		}
		catch (final SAXException e)
		{
			System.err.println("warning: Parser does not support feature (" + BMECatConstants.PARSER.VALIDATION_FEATURE_ID + ")");
		}
		try
		{
			if (props.get(BMECatConstants.PARSER.LOAD_EXTERNAL_DTD_FEATURE_ID) != null)
			{
				parser.setFeature(BMECatConstants.PARSER.LOAD_EXTERNAL_DTD_FEATURE_ID,
						props.get(BMECatConstants.PARSER.LOAD_EXTERNAL_DTD_FEATURE_ID).equals(Boolean.TRUE.toString()) ? true : false);
			}
		}
		catch (final SAXNotRecognizedException e)
		{
			System.err.println("warning: Parser does not recognize feature (" + BMECatConstants.PARSER.LOAD_EXTERNAL_DTD_FEATURE_ID
					+ ")");
		}
		catch (final SAXNotSupportedException e)
		{
			System.err.println("warning: Parser does not support feature (" + BMECatConstants.PARSER.LOAD_EXTERNAL_DTD_FEATURE_ID
					+ ")");
		}
		try
		{
			if (props.get(BMECatConstants.PARSER.SCHEMA_VALIDATION_FEATURE_ID) != null)
			{
				parser.setFeature(BMECatConstants.PARSER.SCHEMA_VALIDATION_FEATURE_ID,
						props.get(BMECatConstants.PARSER.SCHEMA_VALIDATION_FEATURE_ID).equals(Boolean.TRUE.toString()) ? true : false);
			}
		}
		catch (final SAXNotRecognizedException e)
		{
			System.err.println("warning: Parser does not recognize feature (" + BMECatConstants.PARSER.SCHEMA_VALIDATION_FEATURE_ID
					+ ")");
		}
		catch (final SAXNotSupportedException e)
		{
			System.err.println("warning: Parser does not support feature (" + BMECatConstants.PARSER.SCHEMA_VALIDATION_FEATURE_ID
					+ ")");
		}
		try
		{
			if (props.get(BMECatConstants.PARSER.SCHEMA_FULL_CHECKING_FEATURE_ID) != null)
			{
				parser.setFeature(BMECatConstants.PARSER.SCHEMA_FULL_CHECKING_FEATURE_ID,
						props.get(BMECatConstants.PARSER.SCHEMA_FULL_CHECKING_FEATURE_ID).equals(Boolean.TRUE.toString()) ? true
								: false);
			}
		}
		catch (final SAXNotRecognizedException e)
		{
			System.err.println("warning: Parser does not recognize feature ("
					+ BMECatConstants.PARSER.SCHEMA_FULL_CHECKING_FEATURE_ID + ")");
		}
		catch (final SAXNotSupportedException e)
		{
			System.err.println("warning: Parser does not support feature (" + BMECatConstants.PARSER.SCHEMA_FULL_CHECKING_FEATURE_ID
					+ ")");
		}
		try
		{
			if (props.get(BMECatConstants.PARSER.DYNAMIC_VALIDATION_FEATURE_ID) != null)
			{
				parser.setFeature(BMECatConstants.PARSER.DYNAMIC_VALIDATION_FEATURE_ID,
						props.get(BMECatConstants.PARSER.DYNAMIC_VALIDATION_FEATURE_ID).equals(Boolean.TRUE.toString()) ? true : false);
			}
		}
		catch (final SAXNotRecognizedException e)
		{
			System.err.println("warning: Parser does not recognize feature (" + BMECatConstants.PARSER.DYNAMIC_VALIDATION_FEATURE_ID
					+ ")");
		}
		catch (final SAXNotSupportedException e)
		{
			System.err.println("warning: Parser does not support feature (" + BMECatConstants.PARSER.DYNAMIC_VALIDATION_FEATURE_ID
					+ ")");
		}
		try
		{
			if (props.get(BMECatConstants.PARSER.LOAD_EXTERNAL_DTD_FEATURE_ID) != null)
			{
				parser.setFeature(BMECatConstants.PARSER.LOAD_EXTERNAL_DTD_FEATURE_ID,
						props.get(BMECatConstants.PARSER.LOAD_EXTERNAL_DTD_FEATURE_ID).equals(Boolean.TRUE.toString()) ? true : false);
			}
		}
		catch (final SAXNotRecognizedException e)
		{
			System.err.println("warning: Parser does not recognize feature (" + BMECatConstants.PARSER.LOAD_EXTERNAL_DTD_FEATURE_ID
					+ ")");
		}
		catch (final SAXNotSupportedException e)
		{
			System.err.println("warning: Parser does not support feature (" + BMECatConstants.PARSER.LOAD_EXTERNAL_DTD_FEATURE_ID
					+ ")");
		}
	}

	protected XMLReader getSAXParser()
	{
		return parser;
	}

	public void setContentHandler(final ContentHandler handler)
	{
		getSAXParser().setContentHandler(handler);
	}

	/**
	 * Convert file to URL and parse
	 * 
	 * @param file
	 */
	public void parse(final File file) throws AbortCronJobException
	{
		try
		{
			parser.parse(fileToURL(file).toString());
		}
		catch (final TestParseAbortException e)
		{
			throw new AbortCronJobException("Test: CronJob was aborted by client!");
		}
		catch (final AbortRequestedException e)
		{
			throw new AbortCronJobException("CronJob was aborted by client!");
		}
		catch (final ParseFinishedException e)
		{
			// fine here since this is the only way to abort parsing without
			// processing the whole file
		}
		catch (final ParseAbortException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
			throw new AbortCronJobException("unknow abort exception type " + e.getClass().getName() + " ");
		}
		catch (final SAXException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
			throw new JaloSystemException(e);
		}
		catch (final IOException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
			throw new JaloSystemException(e);
		}
	}

	public void parse(final InputSource inputSource) throws AbortCronJobException
	{
		try
		{
			parser.parse(inputSource);
		}
		catch (final TestParseAbortException e)
		{
			throw new AbortCronJobException("Test: CronJob was aborted by client!");
		}
		catch (final AbortRequestedException e)
		{
			throw new AbortCronJobException("CronJob was aborted by client!");
		}
		catch (final ParseFinishedException e)
		{
			// fine here since this is the only way to abort parsing without
			// processing the whole file
		}
		catch (final ParseAbortException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
			throw new AbortCronJobException("unknow abort exception type " + e.getClass().getName() + " ");
		}
		catch (final SAXException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
			throw new JaloSystemException(e);
		}
		catch (final IOException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
			throw new JaloSystemException(e);
		}
	}

	static URL fileToURL(final File file)
	{
		String path = file.getAbsolutePath();
		final String fSep = System.getProperty("file.separator");
		if (fSep != null && fSep.length() == 1)
		{
			path = path.replace(fSep.charAt(0), '/');
		}
		if (path.length() > 0 && path.charAt(0) != '/')
		{
			path = '/' + path;
		}

		try
		{
			return new URL("file", null, path);
		}
		catch (final java.net.MalformedURLException e)
		{
			throw new Error("unexpected MalformedException");
		}
	}

	protected void setDefaultFeatures()
	{
		final Map props = new HashMap();

		props.put(BMECatConstants.PARSER.NAMESPACES_FEATURE_ID, Boolean.TRUE.toString());
		props.put(BMECatConstants.PARSER.NAMESPACE_PREFIXES_FEATURE_ID, Boolean.FALSE.toString());
		props.put(BMECatConstants.PARSER.VALIDATION_FEATURE_ID, Boolean.FALSE.toString());
		props.put(BMECatConstants.PARSER.LOAD_EXTERNAL_DTD_FEATURE_ID, Boolean.FALSE.toString());
		props.put(BMECatConstants.PARSER.SCHEMA_VALIDATION_FEATURE_ID, Boolean.FALSE.toString());
		props.put(BMECatConstants.PARSER.SCHEMA_FULL_CHECKING_FEATURE_ID, Boolean.FALSE.toString());
		props.put(BMECatConstants.PARSER.DYNAMIC_VALIDATION_FEATURE_ID, Boolean.FALSE.toString());
		setFeatures(props);
	}

	public static class AbortRequestedException extends de.hybris.bootstrap.xml.ParseAbortException
	{
		public AbortRequestedException(final String msg)
		{
			super(msg);
		}
	}

	public static class TestParseAbortException extends de.hybris.bootstrap.xml.ParseAbortException
	{
		public TestParseAbortException(final String msg)
		{
			super(msg);
		}
	}

	public static class MissingAttributeParseAbortException extends de.hybris.bootstrap.xml.ParseAbortException
	{
		public MissingAttributeParseAbortException(final String msg)
		{
			super(msg);
		}
	}

	public HeaderUserDefinedExtensionsTagListener getHeaderUserDefinedExtensionsTagListener()
	{
		if (headerUserDefinedExtensionsTagListener == null)
		{
			headerUserDefinedExtensionsTagListener = new HeaderUserDefinedExtensionsTagListener();
		}
		return headerUserDefinedExtensionsTagListener;
	}

	/**
	 * Use this method to overwrite the default HeaderUserDefinedExtensionsTagListener.
	 * 
	 * @param headerUserDefinedExtensionsTagListener
	 */
	public void setHeaderUserDefinedExtensionsTagListener(
			final HeaderUserDefinedExtensionsTagListener headerUserDefinedExtensionsTagListener)
	{
		this.headerUserDefinedExtensionsTagListener = headerUserDefinedExtensionsTagListener;
	}

	public ArticleUserDefinedExtensionsTagListener getArticleUserDefinedExtensionsTagListener()
	{
		if (articleUserDefinedExtensionsTagListener == null)
		{
			articleUserDefinedExtensionsTagListener = new ArticleUserDefinedExtensionsTagListener();
		}
		return articleUserDefinedExtensionsTagListener;
	}

	/**
	 * Use this method to overwrite the default ArticleUserDefinedExtensionsTagListener.
	 * 
	 * @param articleUserDefinedExtensionsTagListener
	 */
	public void setArticleUserDefinedExtensionsTagListener(
			final ArticleUserDefinedExtensionsTagListener articleUserDefinedExtensionsTagListener)
	{
		this.articleUserDefinedExtensionsTagListener = articleUserDefinedExtensionsTagListener;
	}

	public CatalogStructureUserDefinedExtensionsTagListener getCatalogStructureUserDefinedExtensionsTagListener()
	{
		if (catalogStructureUserDefinedExtensionsTagListener == null)
		{
			catalogStructureUserDefinedExtensionsTagListener = new CatalogStructureUserDefinedExtensionsTagListener();
		}
		return catalogStructureUserDefinedExtensionsTagListener;
	}

	/**
	 * Use this method to overwrite the default CatalogStructureUserDefinedExtensionsTagListener.
	 * 
	 * @param catalogStructureUserDefinedExtensionsTagListener
	 */
	public void setCatalogUserDefinedExtensionsTagListener(
			final CatalogStructureUserDefinedExtensionsTagListener catalogStructureUserDefinedExtensionsTagListener)
	{
		this.catalogStructureUserDefinedExtensionsTagListener = catalogStructureUserDefinedExtensionsTagListener;
	}
}
