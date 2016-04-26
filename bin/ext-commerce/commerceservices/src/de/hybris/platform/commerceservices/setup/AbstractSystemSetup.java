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
package de.hybris.platform.commerceservices.setup;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.util.JspContext;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Base class for system setup spring bean. Provides support for importing impex files and managing sync jobs.
 */
public abstract class AbstractSystemSetup
{
	private static final Logger LOG = Logger.getLogger(AbstractSystemSetup.class);

	protected static final String BOOLEAN_TRUE = "yes";
	protected static final String BOOLEAN_FALSE = "no";

	private SetupImpexService setupImpexService;
	private SetupSyncJobService setupSyncJobService;
	private SetupSolrIndexerService setupSolrIndexerService;
	private CatalogVersionService catalogVersionService;
	private EventService eventService;

	/**
	 * Abstract method to be implemented in subclasses.
	 * 
	 * @return the list of SystemSetupParameters for the SystemSetup class.
	 */
	public abstract List<SystemSetupParameter> getInitializationOptions();

	/**
	 * Log an info message in to the jsp context
	 * 
	 * @param context
	 *           the system setup context
	 * @param message
	 *           the message to log
	 */
	public void logInfo(final SystemSetupContext context, final String message)
	{
		LOG.info(message);

		final JspContext jspContext = context.getJspContext();
		// check if jspContext is not null, this is a hybris bug, should be fixed in next release
		if (jspContext != null)
		{
			jspContext.println(message);
		}
	}

	/**
	 * Log an error message in to the jsp context
	 * 
	 * @param context
	 *           the system setup context
	 * @param message
	 *           the message to log
	 * @param throwable
	 *           thrown exception, may be null
	 */
	public void logError(final SystemSetupContext context, final String message, final Throwable throwable)
	{
		LOG.error(message, throwable);

		final JspContext jspContext = context.getJspContext();
		// check if jspContext is not null, this is a hybris bug, should be fixed in next release
		if (jspContext != null)
		{
			jspContext.println("<font color='red'>" + message + "</font>");
		}
	}

	/**
	 * Helper method for creating a Boolean setup parameter.
	 * 
	 * @param key
	 * @param label
	 * @param defaultValue
	 */
	public SystemSetupParameter createBooleanSystemSetupParameter(final String key, final String label,
			final boolean defaultValue)
	{
		final SystemSetupParameter syncProductsParam = new SystemSetupParameter(key);
		syncProductsParam.setLabel(label);
		syncProductsParam.addValue(BOOLEAN_TRUE, defaultValue);
		syncProductsParam.addValue(BOOLEAN_FALSE, !defaultValue);
		return syncProductsParam;
	}

	/**
	 * Helper method for checking setting of a Boolean setup parameter.
	 * 
	 * @param context
	 * @param key
	 * @return true if parameter is set to Yes
	 */
	public boolean getBooleanSystemSetupParameter(final SystemSetupContext context, final String key)
	{
		final String parameterValue = context.getParameter(context.getExtensionName() + "_" + key);
		if (parameterValue != null)
		{
			if (BOOLEAN_TRUE.equals(parameterValue))
			{
				return true;
			}
			else if (BOOLEAN_FALSE.equals(parameterValue))
			{
				return false;
			}
		}

		// Have not been able to determine value from context, fallback to default value
		final boolean defaultValue = getDefaultValueForBooleanSystemSetupParameter(key);
		LOG.warn("Missing setup parameter for key [" + key + "], falling back to defined default [" + defaultValue + "]");
		return defaultValue;
	}

	/**
	 * Helper method for getting default value of a Boolean setup parameter
	 * 
	 * @param key
	 * @return the default value
	 */
	public boolean getDefaultValueForBooleanSystemSetupParameter(final String key)
	{
		final List<SystemSetupParameter> initializationOptions = getInitializationOptions();
		if (initializationOptions != null)
		{
			for (final SystemSetupParameter option : initializationOptions)
			{
				if (key.equals(option.getKey()))
				{
					final String[] defaults = option.getDefaults();
					if (defaults != null && defaults.length > 0)
					{
						return BOOLEAN_TRUE.equals(defaults[0]);
					}
				}
			}
		}
		return false;
	}

	/**
	 * Searches for synchronization jobs of given catalog id
	 *
	 * @param catalogId
	 *           to search synchronization jobs for.
	 * @return list of synchronizations
	 * @throws UnknownIdentifierException
	 *            if no catalog with given id is found
	 */
	public List<SyncItemJobModel> getCatalogSyncJob(final String catalogId) throws UnknownIdentifierException
	{
		return catalogVersionService.getCatalogVersion(catalogId, CatalogManager.OFFLINE_VERSION).getSynchronizations();
	}

	public PerformResult executeCatalogSyncJob(final SystemSetupContext context, final String catalogId)
	{
		return getSetupSyncJobService().executeCatalogSyncJob(catalogId);
	}

	public void importImpexFile(final SystemSetupContext context, final String file)
	{
		getSetupImpexService().importImpexFile(file, true);
	}

	public void importImpexFile(final SystemSetupContext context, final String file, final boolean errorIfMissing)
	{
		getSetupImpexService().importImpexFile(file, errorIfMissing);
	}

	public void importImpexFile(final SystemSetupContext context, final String file, final boolean errorIfMissing,
			final boolean legacyMode)
	{
		getSetupImpexService().importImpexFile(file, errorIfMissing, legacyMode);
	}

	public void createProductCatalogSyncJob(final SystemSetupContext context, final String catalogId)
	{
		getSetupSyncJobService().createProductCatalogSyncJob(catalogId);
	}

	public void createContentCatalogSyncJob(final SystemSetupContext context, final String catalogId)
	{
		getSetupSyncJobService().createContentCatalogSyncJob(catalogId);
	}

	public void createSolrIndexerCronJobs(final String solrFacetSearchConfigName)
	{
		getSetupSolrIndexerService().createSolrIndexerCronJobs(solrFacetSearchConfigName);
	}

	public void executeSolrIndexerCronJob(final String solrFacetSearchConfigName, final boolean fullReIndex)
	{
		getSetupSolrIndexerService().executeSolrIndexerCronJob(solrFacetSearchConfigName, fullReIndex);
	}

	public void activateSolrIndexerCronJobs(final String solrFacetSearchConfigName)
	{
		getSetupSolrIndexerService().activateSolrIndexerCronJobs(solrFacetSearchConfigName);
	}
	
	public boolean isSyncRerunNeeded(final PerformResult syncCronJobResult)
	{
		return CronJobStatus.FINISHED.equals(syncCronJobResult.getStatus())
				&& !CronJobResult.SUCCESS.equals(syncCronJobResult.getResult());
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
