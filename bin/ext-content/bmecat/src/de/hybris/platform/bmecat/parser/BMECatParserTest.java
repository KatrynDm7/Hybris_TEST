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

import de.hybris.bootstrap.xml.AbstractValueObject;
import de.hybris.bootstrap.xml.TagListener;
import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.bmecat.parser.taglistener.ArticleTagListener;
import de.hybris.platform.bmecat.parser.taglistener.CatalogTagListener;
import de.hybris.platform.bmecat.parser.taglistener.TNewCatalogTagListener;
import de.hybris.platform.core.Log4JUtils;
import de.hybris.platform.cronjob.jalo.AbortCronJobException;

import java.io.File;
import java.util.Map;

import org.apache.log4j.Logger;


public class BMECatParserTest implements BMECatObjectProcessor
{
	static
	{
		Log4JUtils.startup();
	}

	private static final Logger LOG = Logger.getLogger(BMECatParserTest.class);
	private static String filename = null;
	private Map props;

	public BMECatParserTest()
	{
		final BMECatParser bmecatparser = new BMECatParser(this, props);
		try
		{
			bmecatparser.parse(new File(filename));
		}
		catch (final AbortCronJobException e)
		{
			LOG.error("Error while parsing", e);
		}
	}

	public void process(final TagListener listener, final AbstractValueObject obj)
	{
		LOG.info("\n\n### BMECatParserTest.process( " + listener + ") ");

		if (listener instanceof TNewCatalogTagListener)
		{
			if (obj instanceof Catalog)
			{
				switch (((Catalog) obj).getTransactionMode())
				{
					case BMECatConstants.TRANSACTION.T_NEW_CATALOG:
						LOG.info("TransactionMode: T_NEW_CATALOG");
						break;
					case BMECatConstants.TRANSACTION.T_UPDATE_PRICES:
						LOG.info("TransactionMode: T_UPDATE_PRICES");
						break;
					case BMECatConstants.TRANSACTION.T_UPDATE_PRODUCTS:
						LOG.info("TransactionMode: T_UPDATE_PRODUCTS");
						break;
				}
				LOG.info(obj);
			}
		}
		else if (listener instanceof CatalogTagListener)
		{
			LOG.info("Create new Catalog");
		}
		else if (listener instanceof ArticleTagListener)
		{
			LOG.info("Importing ARTICLE:\n" + obj);
		}
	}

	public static void main(final String args[])
	{
		if (args.length == 1)
		{
			filename = args[0];
			new BMECatParserTest();
		}
		else
		{
			LOG.info("USAGE: BMECatParserTest( <FILENAME> )");
		}
	}
}
