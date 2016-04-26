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
package de.hybris.platform.catalog.jalo.classification;

import de.hybris.platform.impex.jalo.cronjob.ImpExImportCronJob;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.impex.jalo.imp.ImportProcessor;
import de.hybris.platform.util.CSVReader;


/**
 * Job class for importing classification systems. This class works like
 * {@link de.hybris.platform.impex.jalo.cronjob.ImpExImportJob} except that it provides some preset ImpEx definitions to
 * the executed script as follows:
 * <ul>
 * <li>{@link #SYSTEM_NAME_DEFINITION} == <code>$systemName</code> containing the configured classification system name</li>
 * <li>{@link #SYSTEM_VERSION_DEFINITION} == <code>$systemVersion</code> containing the configured classification system
 * version</li>
 * <li>{@link #SYSTEM_LANGUAGE_DEFINITION} == <code>$systemLanguage</code> containing the configured classification
 * system language</li>
 * </ul>
 * These definitions may be freely used within the import script without having to declare them first.
 * 
 */
public class ClassificationImportJob extends GeneratedClassificationImportJob
{

	public static final String SYSTEM_NAME_DEFINITION = "$systemName";
	public static final String SYSTEM_VERSION_DEFINITION = "$systemVersion";
	public static final String SYSTEM_LANGUAGE_DEFINITION = "$systemLanguage";

	public class MyClassificationImpExImportReader extends ImpExImportReader
	{
		public MyClassificationImpExImportReader(final ClassificationImportCronJob importCronJob, final CSVReader csvReader)
		{
			super(csvReader);
			addDefinitions(importCronJob);
		}

		public MyClassificationImpExImportReader(final ClassificationImportCronJob importCronJob, final CSVReader csvReader,
				final ImportProcessor processor)
		{
			super(csvReader, null, processor);
			addDefinitions(importCronJob);
		}

		protected void addDefinitions(final ClassificationImportCronJob importCronJob)
		{
			addDefinition(SYSTEM_NAME_DEFINITION + "=" + importCronJob.getClassificationSystem().trim());
			addDefinition(SYSTEM_VERSION_DEFINITION + "=" + importCronJob.getVersion().trim());
			addDefinition(SYSTEM_LANGUAGE_DEFINITION + "=" + importCronJob.getLanguage().getIsoCode());
		}
	}

	@Override
	protected ImpExImportReader createImportReader(final ImpExImportCronJob cronJob, final CSVReader csvReader,
			final ImportProcessor processor)
	{
		if (processor == null)
		{
			return new MyClassificationImpExImportReader((ClassificationImportCronJob) cronJob, csvReader);
		}
		else
		{
			return new MyClassificationImpExImportReader((ClassificationImportCronJob) cronJob, csvReader, processor);
		}
	}
}
