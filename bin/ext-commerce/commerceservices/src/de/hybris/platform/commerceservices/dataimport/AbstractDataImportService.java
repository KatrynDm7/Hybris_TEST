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
package de.hybris.platform.commerceservices.dataimport;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.SetupImpexService;
import de.hybris.platform.commerceservices.setup.SetupSolrIndexerService;
import de.hybris.platform.commerceservices.setup.SetupSyncJobService;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.event.EventService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


public abstract class AbstractDataImportService
{
	public static final String ACTIVATE_SOLR_CRON_JOBS = "activateSolrCronJobs";

	private SetupImpexService setupImpexService;
	private SetupSyncJobService setupSyncJobService;
	private SetupSolrIndexerService setupSolrIndexerService;
	private CatalogVersionService catalogVersionService;
	private EventService eventService;

	public abstract void execute(final AbstractSystemSetup systemSetup, final SystemSetupContext context,
			final List<ImportData> importData);

	protected abstract void importCommonData(final String extensionName);

	protected abstract void importProductCatalog(final String extensionName, final String productCatalogName);

	protected abstract void importContentCatalog(final String extensionName, final String contentCatalogName);

	protected abstract void importStore(final String extensionName, final String storeName, final String productCatalogName);

	protected abstract void importSolrIndex(final String extensionName, final String storeName);

	protected abstract void importJobs(final String extensionName, final String storeName);

	protected void importAllData(final AbstractSystemSetup systemSetup, final SystemSetupContext context,
			final ImportData importData, final boolean syncCatalogs)
	{
		systemSetup.logInfo(context, String.format("Begin importing common data for [%s]", context.getExtensionName()));
		importCommonData(context.getExtensionName());

		systemSetup.logInfo(context,
				String.format("Begin importing product catalog data for [%s]", importData.getProductCatalogName()));
		importProductCatalog(context.getExtensionName(), importData.getProductCatalogName());

		for (final String contentCatalogName : importData.getContentCatalogNames())
		{
			systemSetup.logInfo(context, String.format("Begin importing content catalog data for [%s]", contentCatalogName));
			importContentCatalog(context.getExtensionName(), contentCatalogName);
		}

		synchronizeProductCatalog(systemSetup, context, importData.getProductCatalogName(), false);
		for (final String contentCatalog : importData.getContentCatalogNames())
		{
			synchronizeContentCatalog(systemSetup, context, contentCatalog, false);
		}
		assignDependent(importData.getProductCatalogName(), importData.getContentCatalogNames());

		if (syncCatalogs)
		{
			systemSetup
					.logInfo(context, String.format("Synchronizing product catalog for [%s]", importData.getProductCatalogName()));
			final boolean productSyncSuccess = synchronizeProductCatalog(systemSetup, context, importData.getProductCatalogName(),
					true);

			for (final String contentCatalogName : importData.getContentCatalogNames())
			{
				systemSetup.logInfo(context, String.format("Synchronizing content catalog for [%s]", contentCatalogName));
				synchronizeContentCatalog(systemSetup, context, contentCatalogName, true);
			}

			if (!productSyncSuccess)
			{
				// Rerun the product sync if required
				systemSetup.logInfo(context,
						String.format("Rerunning product catalog synchronization for [%s]", importData.getProductCatalogName()));
				if (!synchronizeProductCatalog(systemSetup, context, importData.getProductCatalogName(), true))
				{
					systemSetup.logInfo(context, String.format(
							"Rerunning product catalog synchronization for [%s], failed. Please consult logs for more details.",
							importData.getProductCatalogName()));
				}
			}
		}

		for (final String storeName : importData.getStoreNames())
		{
			systemSetup.logInfo(context, String.format("Begin importing store data for [%s]", storeName));
			importStore(context.getExtensionName(), storeName, importData.getProductCatalogName());

			systemSetup.logInfo(context, String.format("Begin importing job data for [%s]", storeName));
			importJobs(context.getExtensionName(), storeName);

			systemSetup.logInfo(context, String.format("Begin importing solr index data for [%s]", storeName));
			importSolrIndex(context.getExtensionName(), storeName);

			if (systemSetup.getBooleanSystemSetupParameter(context, ACTIVATE_SOLR_CRON_JOBS))
			{
				systemSetup.logInfo(context, String.format("Activating solr index for [%s]", storeName));
				runSolrIndex(context.getExtensionName(), storeName);
			}
		}
	}

	public boolean synchronizeProductCatalog(final AbstractSystemSetup systemSetup, final SystemSetupContext context,
			final String catalogName, final boolean syncCatalogs)
	{
		systemSetup.logInfo(context, String.format("Begin synchronizing Product Catalog [%s]", catalogName));

		getSetupSyncJobService().createProductCatalogSyncJob(String.format("%sProductCatalog", catalogName));

		if (syncCatalogs)
		{
			final PerformResult syncCronJobResult = getSetupSyncJobService().executeCatalogSyncJob(
					String.format("%sProductCatalog", catalogName));
			if (isSyncRerunNeeded(syncCronJobResult))
			{
				systemSetup.logInfo(context, String.format("Product Catalog [%s] sync has issues.", catalogName));
				return false;
			}
		}

		return true;
	}

	public boolean synchronizeContentCatalog(final AbstractSystemSetup systemSetup, final SystemSetupContext context,
			final String catalogName, final boolean syncCatalogs)
	{
		systemSetup.logInfo(context, String.format("Begin synchronizing Content Catalog [%s]", catalogName));

		getSetupSyncJobService().createContentCatalogSyncJob(String.format("%sContentCatalog", catalogName));

		if (syncCatalogs)
		{
			final PerformResult syncCronJobResult = getSetupSyncJobService().executeCatalogSyncJob(
					String.format("%sContentCatalog", catalogName));
			if (isSyncRerunNeeded(syncCronJobResult))
			{
				systemSetup.logInfo(context, String.format("Content Catalog [%s] sync has issues.", catalogName));
				return false;
			}
		}

		return true;
	}

	public void assignDependent(final String dependsOnProduct, final List<String> dependentContents)
	{
		if (CollectionUtils.isNotEmpty(dependentContents) && StringUtils.isNotBlank(dependsOnProduct))
		{
			final Set<String> dependentSyncJobsNames = new HashSet<String>();
			for (final String content : dependentContents)
			{
				dependentSyncJobsNames.add(String.format("%sContentCatalog", content));
			}

			getSetupSyncJobService().assignDependentSyncJobs(String.format("%sProductCatalog", dependsOnProduct),
					dependentSyncJobsNames);
		}
	}

	public void runSolrIndex(final String extensionName, final String storeName)
	{
		getSetupSolrIndexerService().executeSolrIndexerCronJob(String.format("%sIndex", storeName), true);
		getSetupSolrIndexerService().activateSolrIndexerCronJobs(String.format("%sIndex", storeName));
	}

	public boolean isSyncRerunNeeded(final PerformResult syncCronJobResult)
	{
		return syncCronJobResult == null
				|| (CronJobStatus.FINISHED.equals(syncCronJobResult.getStatus()) && !CronJobResult.SUCCESS.equals(syncCronJobResult
						.getResult()));
	}

	public <T> T getBeanForName(final String name)
	{
		return (T) Registry.getApplicationContext().getBean(name);
	}

	public boolean isExtensionLoaded(final String extensionNameToCheck)
	{
		final List<String> loadedExtensionNames = Registry.getCurrentTenant().getTenantSpecificExtensionNames();
		return loadedExtensionNames.contains(extensionNameToCheck);
	}


	public SetupImpexService getSetupImpexService()
	{
		return setupImpexService;
	}

	@Required
	public void setSetupImpexService(final SetupImpexService setupImpexService)
	{
		this.setupImpexService = setupImpexService;
	}

	public SetupSyncJobService getSetupSyncJobService()
	{
		return setupSyncJobService;
	}

	@Required
	public void setSetupSyncJobService(final SetupSyncJobService setupSyncJobService)
	{
		this.setupSyncJobService = setupSyncJobService;
	}

	public SetupSolrIndexerService getSetupSolrIndexerService()
	{
		return setupSolrIndexerService;
	}

	@Required
	public void setSetupSolrIndexerService(final SetupSolrIndexerService setupSolrIndexerService)
	{
		this.setupSolrIndexerService = setupSolrIndexerService;
	}

	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	public EventService getEventService()
	{
		return eventService;
	}

	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}
}
