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

import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.cronjob.constants.CronJobConstants;
import de.hybris.platform.cronjob.constants.GeneratedCronJobConstants;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.jalo.CronJob.CronJobResult;
import de.hybris.platform.jalo.enumeration.EnumerationValue;

import java.util.Map;

import org.apache.log4j.Logger;


/**
 * This is the batch job for BMECat import which executes the import steps.
 * 
 * 
 */
public class BMECatImportBatchJob extends GeneratedBMECatImportBatchJob
{

	private static final Logger log = Logger.getLogger(BMECatImportBatchJob.class.getName());

	/**
	 * Sets prev_version on successful import / update
	 * 
	 * @see de.hybris.platform.cronjob.jalo.Job#performCronJob(de.hybris.platform.cronjob.jalo.CronJob)
	 * @param cronJob
	 *           the context under which the batch job is executed
	 */
	@Override
	protected CronJobResult performCronJob(final CronJob cronJob)
	{
		final CronJobResult result = super.performCronJob(cronJob);
		// increase prevVersion on successful T_UPDATE_PRODUCTS or T_UPDATE_PRICES
		final BMECatImportCronJob bmeCatImportCronJob = (BMECatImportCronJob) cronJob;
		if (bmeCatImportCronJob.getTransactionMode() != null)
		{
			final String transactionCode = bmeCatImportCronJob.getTransactionMode().getCode();
			if (CronJobConstants.Enumerations.CronJobResult.SUCCESS.equals(result.getResult().getCode())
					&& CronJobConstants.Enumerations.CronJobStatus.FINISHED.equals(result.getStatus().getCode())
					&& (BMECatConstants.Enumerations.TransactionModeEnum.T_UPDATE_PRODUCTS.equals(transactionCode) || BMECatConstants.Enumerations.TransactionModeEnum.T_UPDATE_PRICES
							.equals(transactionCode)))
			{
				//bmeCatImportCronJob.getCatalogVersion().setPreviousUpdateVersion(bmeCatImportCronJob.getPreviousUpdateVersion());
				final Map importedVersions = BMECatManager.getInstance().getImportedVersions(bmeCatImportCronJob.getCatalogVersion());
				final String key = BMECatManager.getInstance().getImportedVersionsKey(bmeCatImportCronJob.getCataloValueObject());

				final Integer counter = (Integer) importedVersions.get(key);

				importedVersions.put(key, counter != null ? Integer.valueOf(counter.intValue() + 1) : Integer.valueOf(0));
				BMECatManager.getInstance().setAllImportedVersions(bmeCatImportCronJob.getCatalogVersion(), importedVersions);

				if (log.isInfoEnabled())
				{
					log.info("Finished cronjob >" + cronJob.getCode() + "< mode >" + transactionCode
							+ " set previousUpdateVersion to: " + bmeCatImportCronJob.getPreviousUpdateVersion());
				}
			}
		}
		return result;
	}

	/**
	 * Takes care of undoing units, countries, currencies and languages that have been created during import
	 * 
	 * @see de.hybris.platform.cronjob.jalo.BatchJob#undoCronJob(de.hybris.platform.cronjob.jalo.CronJob)
	 * @param cronJob
	 *           the context under which the batch job is executed *
	 */
	@Override
	protected CronJobResult undoCronJob(final CronJob cronJob)
	{
		final BMECatImportCronJob bmeCatImportCronJob = (BMECatImportCronJob) cronJob;

		final Map importedVersion = BMECatManager.getInstance().getImportedVersions(bmeCatImportCronJob.getCatalogVersion());

		//Integer catalogUpdateVersion = bmeCatImportCronJob.getCatalogVersion() == null ? null : bmeCatImportCronJob.getCatalogVersion().getPreviousUpdateVersion();
		final Integer catalogUpdateVersion = bmeCatImportCronJob.getCatalogVersion() == null ? null : (Integer) importedVersion
				.get(bmeCatImportCronJob.getCatalogID() + "_" + bmeCatImportCronJob.getCatalogVersion());

		final EnumerationValue transactionMode = bmeCatImportCronJob.getTransactionMode();
		if (log.isDebugEnabled())
		{
			log.debug("Undoing BmeCatCronJob >" + bmeCatImportCronJob.getCode() + "< from previousUpdateVersion: "
					+ (catalogUpdateVersion == null ? "null" : catalogUpdateVersion.toString()) + " transactionCode >"
					+ (transactionMode == null ? "null" : transactionMode.getCode()) + "<.");
		}
		final CronJobResult result = super.undoCronJob(cronJob);

		bmeCatImportCronJob.undoUnits();
		bmeCatImportCronJob.undoCountries();
		bmeCatImportCronJob.undoCurrencies();
		bmeCatImportCronJob.undoLanguages();
		if (transactionMode != null)
		{
			// set back previous update version on successful undo 
			if (GeneratedCronJobConstants.Enumerations.CronJobResult.SUCCESS.equals(result.getResult().getCode())
					&& (BMECatConstants.Enumerations.TransactionModeEnum.T_UPDATE_PRODUCTS.equals(transactionMode.getCode()) || BMECatConstants.Enumerations.TransactionModeEnum.T_UPDATE_PRICES
							.equals(transactionMode.getCode())) && bmeCatImportCronJob.getCatalogVersion() != null
					&& catalogUpdateVersion != null)
			{
				//Integer newPrevVersion = catalogUpdateVersion.intValue() == 0 ? null : new Integer(catalogUpdateVersion.intValue()-1);
				//bmeCatImportCronJob.getCatalogVersion().setPreviousUpdateVersion( newPrevVersion);

				final Map importedVersions = BMECatManager.getInstance().getImportedVersions(bmeCatImportCronJob.getCatalogVersion());
				final String key = BMECatManager.getInstance().getImportedVersionsKey(bmeCatImportCronJob.getCataloValueObject());

				final Integer newPrevVersion = (Integer) importedVersions.get(key);
				importedVersions.put(key, newPrevVersion == Integer.valueOf(1) ? null : Integer
						.valueOf(newPrevVersion.intValue() - 1));
				BMECatManager.getInstance().setAllImportedVersions(bmeCatImportCronJob.getCatalogVersion(), importedVersion);
				if (log.isInfoEnabled())
				{
					log.info("Undo of cronjob >" + cronJob.getCode() + "< >" + transactionMode.getCode()
							+ "< successfully finished. Set previous update version of CatalogVersion back to: " + newPrevVersion);
				}
			}
		}
		return result;
	}


	/**
	 * Allows undo only if the cronjob has executed the last update on the catalog (cronJob imported latest
	 * previousUpdateVersion)
	 * 
	 * @param cronJob
	 *           the context under which the batch job is executed
	 */
	@Override
	protected boolean canUndo(final CronJob cronJob)
	{
		final BMECatImportCronJob bmeCatCronJob = (BMECatImportCronJob) cronJob;
		final Map importedVersion = BMECatManager.getInstance().getImportedVersions(bmeCatCronJob.getCatalogVersion());

		//Integer catalogUpdateVersion = bmeCatCronJob.getCatalogVersion() == null ? null : bmeCatCronJob.getCatalogVersion().getPreviousUpdateVersion();
		final Integer catalogUpdateVersion = bmeCatCronJob.getCatalogVersion() == null ? null : (Integer) importedVersion
				.get(bmeCatCronJob.getCatalogID() + "_" + bmeCatCronJob.getCatalogVersion());

		final Integer cronJobUpdateVersion = bmeCatCronJob.getPreviousUpdateVersion();
		if (log.isDebugEnabled())
		{
			log.debug("can undo cronjob >" + cronJob.getCode() + "<:  catalog updateversion>" + catalogUpdateVersion
					+ "< cronJObUpdateVersion >" + cronJobUpdateVersion + "<");
		}
		if (catalogUpdateVersion == null)
		{
			return cronJobUpdateVersion == null ? true : false;
		}
		return catalogUpdateVersion.equals(cronJobUpdateVersion);
	}
}
