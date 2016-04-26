/*
 *
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.catalog.synchronization;

import de.hybris.platform.catalog.jalo.SyncItemCronJob;
import de.hybris.platform.catalog.jalo.SyncItemJob;
import de.hybris.platform.catalog.jalo.synchronization.CatalogVersionSyncJob;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemCronJobModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncCronJobModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncJobModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Required;


public class DefaultCatalogSynchronizationService implements CatalogSynchronizationService
{
	private CronJobService cronJobService;
	private ModelService modelService;

	@Override
	public void synchronizeFully(final CatalogVersionModel source, final CatalogVersionModel target)
	{
		final CatalogVersionSyncJobModel syncJob = createSyncJob(source, target);
		final CatalogVersionSyncCronJobModel syncCronJob = (CatalogVersionSyncCronJobModel) createSyncCronJob(syncJob);

		cronJobService.performCronJob(syncCronJob, true);
	}

	@Override
	public void synchronizeFully(final CatalogVersionModel source, final CatalogVersionModel target, final int numberOfThreads)
	{
		final CatalogVersionSyncJobModel syncJob = createSyncJob(source, target, numberOfThreads);
		final CatalogVersionSyncCronJobModel syncCronJob = (CatalogVersionSyncCronJobModel) createSyncCronJob(syncJob);

		cronJobService.performCronJob(syncCronJob, true);
	}

	@Override
	public void synchronizeFullyInBackground(final CatalogVersionModel source, final CatalogVersionModel target)
	{
		final CatalogVersionSyncJobModel syncJob = createSyncJob(source, target);
		final CatalogVersionSyncCronJobModel syncCronJob = (CatalogVersionSyncCronJobModel) createSyncCronJob(syncJob);

		cronJobService.performCronJob(syncCronJob, false);
	}

	@Override
	public SyncResult synchronize(final SyncItemJobModel syncJob, final SyncConfig syncConfig)
	{
		if (!Boolean.TRUE.equals(syncConfig.getFullSync()))
		{
			throw new UnsupportedOperationException("Only 'Full sync' is currently supported here but was NOT set! ");
		}

		final SyncItemCronJobModel syncCronJob = createSyncCronJob(syncJob);
		syncCronJob.setForceUpdate(syncConfig.getForceUpdate());
		syncCronJob.setCreateSavedValues(syncConfig.getCreateSavedValues());
		syncCronJob.setLogToDatabase(syncConfig.getLogToDatabase());
		syncCronJob.setLogToFile(syncConfig.getLogToFile());
		syncCronJob.setLogLevelDatabase(syncConfig.getLogLevelDatabase());
		syncCronJob.setLogLevelFile(syncConfig.getLogLevelFile());
		syncCronJob.setErrorMode(syncConfig.getErrorMode());

		modelService.save(syncCronJob);
		modelService.refresh(syncCronJob);
		final SyncResult syncResult = new SyncResult(syncCronJob);

		cronJobService.performCronJob(syncCronJob, syncConfig.getSynchronous());

		return syncResult;
	}

	private CatalogVersionSyncJobModel createSyncJob(final CatalogVersionModel source, final CatalogVersionModel target)
	{
		return createSyncJob(source, target, getMaxThreads());
	}

	private CatalogVersionSyncJobModel createSyncJob(final CatalogVersionModel source, final CatalogVersionModel target,
			final int numberOfThreads)
	{
		final CatalogVersionSyncJobModel job = modelService.create(CatalogVersionSyncJobModel.class);
		job.setCode(RandomStringUtils.randomAlphanumeric(10));
		job.setSourceVersion(source);
		job.setTargetVersion(target);
		job.setRemoveMissingItems(true);
		job.setCreateNewItems(true);
		job.setMaxThreads(numberOfThreads);

		modelService.save(job);

		return job;
	}

	private SyncItemCronJobModel createSyncCronJob(final SyncItemJobModel job)
	{
		final SyncItemJob jobItem = modelService.getSource(job);
		final SyncItemCronJob cronJob = jobItem.newExecution();
		jobItem.configureFullVersionSync(cronJob);

		return modelService.get(cronJob.getPK());
	}

	private Integer getMaxThreads()
	{
		return Integer.valueOf(CatalogVersionSyncJob.getDefaultMaxThreads(Registry.getCurrentTenantNoFallback()));
	}

	@Required
	public void setCronJobService(final CronJobService cronJobService)
	{
		this.cronJobService = cronJobService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
