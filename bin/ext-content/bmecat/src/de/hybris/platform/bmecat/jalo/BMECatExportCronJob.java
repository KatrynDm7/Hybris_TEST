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
package de.hybris.platform.bmecat.jalo;

import de.hybris.platform.bmecat.xmlwriter.BMECatWriter;
import de.hybris.platform.jalo.c2l.Language;

import org.apache.log4j.Logger;


/**
 * <code>CronJob</code> for BMECat export.
 * 
 * 
 */
public class BMECatExportCronJob extends GeneratedBMECatExportCronJob
{
	private static final Logger LOG = Logger.getLogger(BMECatExportCronJob.class.getName()); //NOPMD

	protected void registerWriterForInfo(final BMECatWriter writer, final int completedlanguageCount)
	{
		setTransientObject("bmecat.export.currentwriter", writer);
		setTransientObject("bmecat.export.completedlanguagecount", Integer.valueOf(completedlanguageCount));
	}

	public int getTotalLanguagesCount()
	{
		return getExportLanguages().size();
	}

	public int getCompletedLanguageCount()
	{
		final Integer integer = (Integer) getTransientObject("bmecat.export.completedlanguagecount");
		return integer != null ? integer.intValue() : 0;
	}

	public int getCurrentItemsCount()
	{
		final BMECatWriter writer = (BMECatWriter) getTransientObject("bmecat.export.currentwriter");
		return writer != null ? writer.getCurrentItemsCount() : 0;
	}

	public int getTotalItemsCount()
	{
		final BMECatWriter writer = (BMECatWriter) getTransientObject("bmecat.export.currentwriter");
		return writer != null ? writer.getTotalItemsCount() : 0;
	}

	protected void unsetWriterForInfo(final BMECatWriter writer)
	{
		setTransientObject("bmecat.export.currentwriter", writer);
	}

	public Language getCurrentlyExportingLanguage()
	{
		final BMECatWriter writer = (BMECatWriter) getTransientObject("bmecat.export.currentwriter");
		return writer != null ? writer.getExportLang() : null;
	}

	public int getPercentage()
	{
		final int totalLanguages = getExportLanguages().size();
		final int completedLanguages = getCompletedLanguageCount();
		final int totalItemsForLanguage = getTotalItemsCount();
		final int currentItemsForLanguage = getCurrentItemsCount();
		return completedLanguages == totalLanguages ? 100
				: (totalItemsForLanguage > 0 ? (completedLanguages * 100 / totalLanguages)
						+ ((currentItemsForLanguage * 100) / (totalItemsForLanguage * totalLanguages)) : 0);
	}
}
