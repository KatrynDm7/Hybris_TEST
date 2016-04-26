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
package de.hybris.platform.commerceservices.setup.impl;

import de.hybris.platform.commerceservices.setup.SetupSolrIndexerService;
import de.hybris.platform.cronjob.jalo.Trigger;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.jalo.type.JaloTypeException;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationValues;
import de.hybris.platform.solrfacetsearch.jalo.SolrfacetsearchManager;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrFacetSearchConfig;
import de.hybris.platform.solrfacetsearch.jalo.indexer.cron.SolrIndexerCronJob;
import de.hybris.platform.solrfacetsearch.model.indexer.cron.SolrIndexerCronJobModel;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link de.hybris.platform.commerceservices.setup.SetupSolrIndexerService}.
 */
public class DefaultSetupSolrIndexerService implements SetupSolrIndexerService
{
	private static final Logger LOG = Logger.getLogger(DefaultSetupSolrIndexerService.class);

	private CronJobService cronJobService;
	private ModelService modelService;

	protected CronJobService getCronJobService()
	{
		return cronJobService;
	}

	@Required
	public void setCronJobService(final CronJobService cronJobService)
	{
		this.cronJobService = cronJobService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	public void createSolrIndexerCronJobs(final String solrFacetSearchConfigName)
	{
		final SolrFacetSearchConfig solrFacetConfig = getSolrFacetSearchConfigForName(solrFacetSearchConfigName);
		if (solrFacetConfig != null)
		{
			getSolrIndexerJob(solrFacetConfig, IndexerOperationValues.FULL);
			getSolrIndexerJob(solrFacetConfig, IndexerOperationValues.UPDATE);
			getSolrIndexerJob(solrFacetConfig, IndexerOperationValues.DELETE);
		}
	}

	@Override
	public void executeSolrIndexerCronJob(final String solrFacetSearchConfigName, final boolean fullReIndex)
	{
		final SolrFacetSearchConfig solrFacetConfig = getSolrFacetSearchConfigForName(solrFacetSearchConfigName);
		if (solrFacetConfig != null)
		{
			executeSolrIndexerCronJob(solrFacetConfig, fullReIndex ? IndexerOperationValues.FULL : IndexerOperationValues.UPDATE);
		}
	}

	@Override
	public void activateSolrIndexerCronJobs(final String solrFacetSearchConfigName)
	{
		final SolrFacetSearchConfig solrFacetConfig = getSolrFacetSearchConfigForName(solrFacetSearchConfigName);
		if (solrFacetConfig != null)
		{
			activateSolrIndexerCronJobs(getSolrIndexerJob(solrFacetConfig, IndexerOperationValues.FULL));
			activateSolrIndexerCronJobs(getSolrIndexerJob(solrFacetConfig, IndexerOperationValues.UPDATE));
			activateSolrIndexerCronJobs(getSolrIndexerJob(solrFacetConfig, IndexerOperationValues.DELETE));
		}
	}

	protected SolrFacetSearchConfig getSolrFacetSearchConfigForName(final String solrFacetSearchConfigName)
	{
		try
		{
			return SolrfacetsearchManager.getInstance().getSolrFacetConfig(solrFacetSearchConfigName);
		}
		catch (final FlexibleSearchException ignore)
		{
			// Ignore
		}
		return null;
	}

	protected SolrIndexerCronJob getSolrIndexerJob(final SolrFacetSearchConfig solrFacetSearchConfig,
			final IndexerOperationValues indexerOperation)
	{
		SolrIndexerCronJob indexerCronJob = getExistingSolrIndexerJob(solrFacetSearchConfig, indexerOperation);
		if (indexerCronJob == null)
		{
			indexerCronJob = createSolrIndexerJob(solrFacetSearchConfig, indexerOperation);
		}
		return indexerCronJob;
	}

	protected SolrIndexerCronJob getExistingSolrIndexerJob(final SolrFacetSearchConfig solrFacetSearchConfig,
			final IndexerOperationValues indexerOperation)
	{
		// Look to see if a cron job exists
		final String indexerCronJobName = buildSolrCronJobCode(solrFacetSearchConfig, indexerOperation);

		try
		{
			final CronJobModel cronJob = getCronJobService().getCronJob(indexerCronJobName);
			if (cronJob instanceof SolrIndexerCronJobModel)
			{
				return (SolrIndexerCronJob) getModelService().getSource(cronJob);
			}
		}
		catch (final UnknownIdentifierException ignore)
		{
			// Ignore
		}
		catch (final AmbiguousIdentifierException ignore)
		{
			// Ignore
		}
		return null;
	}

	protected SolrIndexerCronJob createSolrIndexerJob(final SolrFacetSearchConfig solrFacetSearchConfig,
			final IndexerOperationValues indexerOperation)
	{
		final String indexerCronJobName = buildSolrCronJobCode(solrFacetSearchConfig, indexerOperation);

		try
		{
			final EnumerationValue indexerOperationEnum = getModelService().getSource(indexerOperation);
			return SolrfacetsearchManager.getInstance().createSolrIndexerCronJob(indexerCronJobName, solrFacetSearchConfig,
					indexerOperationEnum);
		}
		catch (final JaloTypeException e)
		{
			throw new SystemException("Cannot create indexer job [" + indexerCronJobName + "] due to: " + e.getMessage(), e);
		}
	}

	protected String buildSolrCronJobCode(final SolrFacetSearchConfig solrFacetSearchConfig,
			final IndexerOperationValues indexerOperation)
	{
		return indexerOperation.getCode() + "-" + solrFacetSearchConfig.getName() + "-cronJob";
	}

	protected void executeSolrIndexerCronJob(final SolrFacetSearchConfig solrFacetSearchConfig,
			final IndexerOperationValues indexerOperation)
	{
		final SolrIndexerCronJob solrIndexerJob = getSolrIndexerJob(solrFacetSearchConfig, indexerOperation);
		if (solrIndexerJob != null)
		{
			LOG.info("Starting solr " + indexerOperation + " index operation for [" + solrFacetSearchConfig.getName() + "] ...");

			if (!solrIndexerJob.isActiveAsPrimitive())
			{
				solrIndexerJob.setActive(true);
			}
			solrIndexerJob.getJob().perform(solrIndexerJob, true);

			LOG.info("Completed solr " + indexerOperation + " index operation for [" + solrFacetSearchConfig.getName() + "]");
		}
	}

	protected void activateSolrIndexerCronJobs(final SolrIndexerCronJob solrIndexerJob)
	{
		if (solrIndexerJob != null)
		{
			if (!solrIndexerJob.isActiveAsPrimitive())
			{
				solrIndexerJob.setActive(true);
			}

			final List<Trigger> triggers = solrIndexerJob.getTriggers();
			if (triggers != null && !triggers.isEmpty())
			{
				final Date now = new Date();
				final Date fiveMinutesTime = new Date(now.getTime() + 1000 * 60 * 5);

				for (final Trigger trigger : triggers)
				{
					if (!trigger.isActiveAsPrimitive())
					{
						trigger.setActive(true);
						trigger.setActivationTime(fiveMinutesTime);
					}
				}
			}
		}
	}
}
