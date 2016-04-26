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

import de.hybris.platform.catalog.jalo.classification.ClassificationSystemVersion;
import de.hybris.platform.cronjob.jalo.AbortCronJobException;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.jalo.CronJob.CronJobResult;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.jalo.Europe1PriceFactory;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;


public class BMECatImportWithCsvJob extends GeneratedBMECatImportWithCsvJob
{
	private static final Logger LOG = Logger.getLogger(BMECatImportWithCsvJob.class.getName());

	//syntax for statistics.impex file: 
	//"#% importMapping.put(""unitCode"",""mappingUnitCode"");"
	private StringBuilder statistics = null;


	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes)
			throws JaloBusinessException
	{
		// business code placed here will be executed before the item is created
		// then create the item
		final Item item = super.createItem(ctx, type, allAttributes);
		// business code placed here will be executed after the item was created
		// and return the item
		return item;
	}

	@Override
	public Map getAllDefaultUnitMappings(final SessionContext ctx)
	{
		Map map = (Map) getProperty(ctx, GeneratedBMECatImportWithCsvJob.DEFAULTUNITMAPPINGS);
		if (map == null)
		{
			map = new HashMap();
		}
		map.put("C62", ProductManager.getInstance().getUnit("pieces"));
		return map;
	}

	/**
	 * task: create the statistics.impex file, and execute the impex script
	 */
	@Override
	public CronJobResult performCronJob(final CronJob cronJob) throws AbortCronJobException
	{
		CronJobResult result = performCronJob((BMECatImportWithCsvCronJob) cronJob);
		if (cronJob.getSuccessResult().equals(result.getResult()))
		{
			// execute the impex script
			result = super.performCronJob(cronJob);
		}
		return result;
	}

	private CronJobResult performCronJob(final BMECatImportWithCsvCronJob importCronJob) throws AbortCronJobException
	{
		//create the statistics.impex file, macro definitions and user customized mappings

		LOG.info("mapping:" + importCronJob.getBmecatMappings());
		this.statistics = new StringBuilder();
		this.statistics.append("$catalog_id=" + importCronJob.getBmecatInfo().get("catalog.code") + "\r\n");
		this.statistics.append("$catalog_name=" + importCronJob.getBmecatInfo().get("catalog.name") + "\r\n");
		this.statistics.append("$catalog_version=" + importCronJob.getBmecatInfo().get("catalogversion.version") + "\r\n");
		this.statistics.append("$lang_iso=" + importCronJob.getBmecatInfo().get("catalog.language") + "\r\n");
		this.statistics.append("$cur_iso=" + importCronJob.getBmecatInfo().get("catalog.currency") + "\r\n");
		if (importCronJob.getBmecatInfo().get("classification.system.versions") != null)
		{
			this.statistics.append("$class_system_versions=" + importCronJob.getBmecatInfo().get("classification.system.versions")
					+ "\r\n");
		}
		if (importCronJob.getBmecatInfo().get("classification.system.create") != null)
		{
			this.statistics.append("$class_system_create=" + importCronJob.getBmecatInfo().get("classification.system.create")
					+ "\r\n");
		}
		else
		{
			this.statistics.append("$class_system_create=false\r\n");
		}
		this.statistics.append("#% Map importMapping = new HashMap();\r\n");

		final Iterator<Map.Entry<String, Currency>> itCurrencies = importCronJob.getAllCurrencyMappings().entrySet().iterator();
		while (itCurrencies.hasNext())
		{
			final Map.Entry<String, Currency> entry = itCurrencies.next();
			if (entry.getValue() == null)
			{
				try
				{
					C2LManager.getInstance().createCurrency(entry.getKey());
				}
				catch (final ConsistencyCheckException e)
				{
					e.printStackTrace();
				}
			}
			else if (!entry.getKey().equalsIgnoreCase(entry.getValue().getIsoCode()))
			{
				this.statistics.append("\"#% importMapping.put(\"\"" + entry.getKey() + "\"\", " + "\"\""
						+ entry.getValue().getIsoCode() + "\"\");\"\r\n");
			}
		}

		final Iterator<Map.Entry<String, Unit>> itUnits = importCronJob.getAllUnitMappings().entrySet().iterator();
		while (itUnits.hasNext())
		{
			final Map.Entry<String, Unit> entry = itUnits.next();
			if (entry.getValue() == null)
			{
				ProductManager.getInstance().createUnit(entry.getKey(), entry.getKey());
			}
			else if (!entry.getKey().equalsIgnoreCase(entry.getValue().getCode()))
			{
				this.statistics.append("\"#% importMapping.put(\"\"" + entry.getKey() + "\"\", " + "\"\""
						+ entry.getValue().getCode() + "\"\");\"\r\n");
			}
		}

		final Iterator<Map.Entry<String, ClassificationSystemVersion>> itVersions = importCronJob.getAllClassificationMappings()
				.entrySet().iterator();
		while (itVersions.hasNext())
		{
			final Map.Entry<String, ClassificationSystemVersion> entry = itVersions.next();
			this.statistics.append("\"#% importMapping.put(\"\"" + entry.getKey() + "\"\", " + "\"\""
					+ (entry.getValue() == null ? entry.getKey() : entry.getValue().getClassificationSystem().getId())
					+ "\"\");\"\r\n");
		}

		final Iterator<Map.Entry<String, EnumerationValue>> itTaxTypes = importCronJob.getAllTaxTypeMappings().entrySet()
				.iterator();
		final Europe1PriceFactory europe1PriceFactory = (Europe1PriceFactory) getSession().getExtensionManager().getExtension(
				Europe1Constants.EXTENSIONNAME);
		while (itTaxTypes.hasNext())
		{
			final Map.Entry<String, EnumerationValue> entry = itTaxTypes.next();
			if (entry.getValue() == null)
			{
				try
				{
					final EnumerationValue tax = europe1PriceFactory.createProductTaxGroup(entry.getKey());
					final Map<Language, Object> values = new HashMap<Language, Object>();
					final Language german = C2LManager.getInstance().getLanguageByIsoCode("de");
					values.put(german, entry.getKey());
					final Language english = C2LManager.getInstance().getLanguageByIsoCode("en");
					values.put(english, entry.getKey());
					tax.setAllLocalizedProperties("name", values);
				}
				catch (final ConsistencyCheckException e)
				{
					e.printStackTrace();
				}
			}
			else if (!entry.getKey().equalsIgnoreCase(entry.getValue().getCode()))
			{
				this.statistics.append("\"#% importMapping.put(\"\"" + entry.getKey() + "\"\", " + "\"\""
						+ entry.getValue().getCode() + "\"\");\"\r\n");
			}
		}

		//add the content in the default impex script file or user specified file
		if (importCronJob.getImpexScriptMedia() == null)
		{
			this.statistics.append(importCronJob.getDefaultImpExContent());
		}
		else
		{
			try
			{
				//TODO append or new string builder??? overwrite in impex???
				this.statistics.append(new String(importCronJob.getImpexScriptMedia().getData()));
			}
			catch (final JaloBusinessException e)
			{
				e.printStackTrace();
				//TODO stop the process? which kind of exception would be thrown???
			}
		}

		final ImpExMedia statisticsMedia = ImpExManager.getInstance().createImpExMedia("statistics.impex");
		try
		{
			statisticsMedia.setData(this.statistics.toString().getBytes());
			statisticsMedia.setMime(ImpExConstants.File.MIME_TYPE_IMPEX);
			statisticsMedia.setRealFileName("statistics.impex");
			importCronJob.setJobMedia(statisticsMedia);
		}
		catch (final JaloBusinessException e)
		{
			e.printStackTrace();
			return getCurrentCronJobResult(importCronJob, false);
		}

		return getCurrentCronJobResult(importCronJob, true);
	}

	private CronJobResult getCurrentCronJobResult(final CronJob cronJob, final boolean result)
	{
		return cronJob.getFinishedResult(result);
	}

}
