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

import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.SyncAttributeDescriptorConfig;
import de.hybris.platform.catalog.jalo.SyncItemCronJob;
import de.hybris.platform.catalog.jalo.SyncItemJob;
import de.hybris.platform.catalog.jalo.synchronization.CatalogVersionSyncJob;
import de.hybris.platform.commerceservices.setup.SetupSyncJobService;
import de.hybris.platform.commerceservices.setup.data.EditSyncAttributeDescriptorData;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link SetupSyncJobService}
 */
public class DefaultSetupSyncJobService implements SetupSyncJobService
{
	private static final Logger LOG = Logger.getLogger(DefaultSetupSyncJobService.class);

	private ModelService modelService;
	private List<String> productCatalogRootTypeCodes = new LinkedList<>();
	private List<String> contentCatalogRootTypeCodes = new LinkedList<>();
	private Map<Class<?>, List<EditSyncAttributeDescriptorData>> contentCatalogEditSyncDescriptors = new LinkedHashMap<>();
	private Map<Class<?>, List<EditSyncAttributeDescriptorData>> productCatalogEditSyncDescriptors = new LinkedHashMap<>();

	@Override
	public void createProductCatalogSyncJob(final String catalogId)
	{
		// Check if the sync job already exists
		if (getCatalogSyncJob(catalogId) == null)
		{
			LOG.info("Creating product sync item job for [" + catalogId + "]");

			// Lookup the catalog name
			final Catalog catalog = CatalogManager.getInstance().getCatalog(catalogId);

			// Create the sync job name
			final String jobName = createJobIdentifier(catalogId);
			final SyncItemJob syncItemJob = CatalogManager.getInstance().configureSynchronizationJob(jobName, catalog,
					CatalogManager.OFFLINE_VERSION, CatalogManager.ONLINE_VERSION, true, false);

			processRootTypes(syncItemJob, catalogId, getProductCatalogRootTypeCodes());
			processEditSyncAttributeDescriptors(syncItemJob, catalogId, getProductCatalogEditSyncDescriptors());

			LOG.info("Created product sync item job [" + syncItemJob.getCode() + "]");
		}
	}

	@Override
	public void assignDependentSyncJobs(final String catalogId, final Set<String> dependentCatalogIds)
	{
		final SyncItemJob masterSyncJob = getSyncJobForCatalog(catalogId);
		if (masterSyncJob instanceof CatalogVersionSyncJob)
		{
			final CatalogVersionSyncJob masterCatSyncJob = (CatalogVersionSyncJob) masterSyncJob;

			final Set<CatalogVersionSyncJob> dependentSyncJobs = new LinkedHashSet<CatalogVersionSyncJob>();

			// Get the existing dependent sync jobs
			final Set<CatalogVersionSyncJob> existingDependentSyncJobs = masterCatSyncJob.getDependentSyncJobs();
			if (existingDependentSyncJobs != null)
			{
				dependentSyncJobs.addAll(existingDependentSyncJobs);
			}

			// Add the new dependent sync jobs for the catalog Ids
			for (final String dependantCatalogId : dependentCatalogIds)
			{
				final SyncItemJob dependantSyncJob = getSyncJobForCatalog(dependantCatalogId);
				if (dependantSyncJob instanceof CatalogVersionSyncJob)
				{
					dependentSyncJobs.add((CatalogVersionSyncJob) dependantSyncJob);
				}
			}

			// Set the new dependent sync jobs set
			masterCatSyncJob.setDependentSyncJobs(dependentSyncJobs);

			if (LOG.isInfoEnabled())
			{
				LOG.info("Set DependentSyncJobs on CatalogVersionSyncJob [" + masterCatSyncJob.getCode() + "] to ["
						+ catalogVersionSyncJobsToString(dependentSyncJobs) + "]");
			}
		}
	}

	protected String catalogVersionSyncJobsToString(final Collection<CatalogVersionSyncJob> catalogVersionSyncJobs)
	{
		final StringBuilder buf = new StringBuilder();
		for (final CatalogVersionSyncJob catalogVersionSyncJob : catalogVersionSyncJobs)
		{
			if (buf.length() > 0)
			{
				buf.append(", ");
			}

			buf.append(catalogVersionSyncJob.getCode());
		}
		return buf.toString();
	}


	@Override
	public void createContentCatalogSyncJob(final String catalogId)
	{
		// Check if the sync job already exists
		final SyncItemJob existingSyncItemJob = getCatalogSyncJob(catalogId);
		if (existingSyncItemJob == null)
		{
			LOG.info("Creating content sync item job for [" + catalogId + "]");

			// Lookup the catalog name
			final Catalog catalog = CatalogManager.getInstance().getCatalog(catalogId);

			// Create the sync job name
			final String jobName = createJobIdentifier(catalogId);
			final SyncItemJob syncItemJob = CatalogManager.getInstance().configureSynchronizationJob(jobName, catalog,
					CatalogManager.OFFLINE_VERSION, CatalogManager.ONLINE_VERSION, true, true);

			processRootTypes(syncItemJob, catalogId, getContentCatalogRootTypeCodes());


			final ComposedType cmsItemType = tryGetComposedType("CMSItem");
			final Collection<SyncAttributeDescriptorConfig> syncAttributeConfigurations = syncItemJob
					.getSyncAttributeConfigurations();
			for (final SyncAttributeDescriptorConfig syncAttributeDescriptorConfig : syncAttributeConfigurations)
			{
				final Type attributeType = syncAttributeDescriptorConfig.getAttributeDescriptor().getAttributeType();
				if (syncAttributeDescriptorConfig.getAttributeDescriptor().getEnclosingType().isAssignableFrom(cmsItemType)
						&& cmsItemType.isAssignableFrom(attributeType)
						|| (attributeType instanceof CollectionType && cmsItemType.isAssignableFrom(((CollectionType) attributeType)
								.getElementType())))
				{
					syncAttributeDescriptorConfig.setCopyByValue(true);
				}
			}

			processEditSyncAttributeDescriptors(syncItemJob, catalogId, getContentCatalogEditSyncDescriptors());



			LOG.info("Created content sync item job [" + syncItemJob.getCode() + "]");
		}
	}

	protected String createJobIdentifier(final String catalogId)
	{
		return "sync " + catalogId + ":" + CatalogManager.OFFLINE_VERSION + "->" + CatalogManager.ONLINE_VERSION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.acceleratorservices.setup.SetupSyncJobService#executeCatalogSyncJob(de.hybris.platform.core
	 * .initialization.SystemSetupContext, java.lang.String)
	 */
	@Override
	public PerformResult executeCatalogSyncJob(final String catalogId)
	{
		final SyncItemJob catalogSyncJob = getCatalogSyncJob(catalogId);
		if (catalogSyncJob == null)
		{
			LOG.error("Couldn't find 'SyncItemJob' for catalog [" + catalogId + "]", null);
			return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.UNKNOWN);
		}
		else
		{
			final SyncItemCronJob syncJob = getLastFailedSyncCronJob(catalogSyncJob);
			syncJob.setLogToDatabase(false);
			syncJob.setLogToFile(false);
			syncJob.setForceUpdate(false);

			LOG.info("Created cronjob [" + syncJob.getCode() + "] to synchronize catalog [" + catalogId
					+ "] staged to online version.");
			LOG.info("Configuring full version sync");

			catalogSyncJob.configureFullVersionSync(syncJob);

			LOG.info("Starting synchronization, this may take a while ...");

			catalogSyncJob.perform(syncJob, true);

			LOG.info("Synchronization complete for catalog [" + catalogId + "]");

			final CronJobResult result = modelService.get(syncJob.getResult());
			final CronJobStatus status = modelService.get(syncJob.getStatus());
			return new PerformResult(result, status);
		}
	}

	/**
	 * Returns the last cronjob if exists and failed or the new one otherwise
	 * 
	 * @param syncItemJob
	 * @return synchronization cronjob - new one or the last one if failed
	 */
	protected SyncItemCronJob getLastFailedSyncCronJob(final SyncItemJob syncItemJob)
	{
		SyncItemCronJob syncCronJob = null;
		if (CollectionUtils.isNotEmpty(syncItemJob.getCronJobs()))
		{
			final List<CronJob> cronjobs = new ArrayList<CronJob>(syncItemJob.getCronJobs());
			Collections.sort(cronjobs, new Comparator<CronJob>()
			{
				@Override
				public int compare(final CronJob cronJob1, final CronJob cronJob2)
				{

					if (cronJob1 == null || cronJob1.getEndTime() == null || cronJob2 == null || cronJob2.getEndTime() == null)
					{
						return 0;
					}
					else
					{
						return cronJob1.getEndTime().compareTo(cronJob2.getEndTime());
					}
				}
			});
			final SyncItemCronJob latestCronJob = (SyncItemCronJob) cronjobs.get(cronjobs.size() - 1);
			final CronJobResult result = modelService.get(latestCronJob.getResult());
			final CronJobStatus status = modelService.get(latestCronJob.getStatus());
			if (CronJobStatus.FINISHED.equals(status) && !CronJobResult.SUCCESS.equals(result))
			{
				syncCronJob = latestCronJob;
			}
		}
		if (syncCronJob == null)
		{
			syncCronJob = syncItemJob.newExecution();
		}
		return syncCronJob;
	}


	protected void processEditSyncAttributeDescriptors(final SyncItemJob job, final String catalogId,
			final Map<Class<?>, List<EditSyncAttributeDescriptorData>> editSyncDescriptors)
	{
		if (!editSyncDescriptors.isEmpty())
		{
			for (final Entry<Class<?>, List<EditSyncAttributeDescriptorData>> entry : editSyncDescriptors.entrySet())
			{
				for (final EditSyncAttributeDescriptorData descriptor : editSyncDescriptors.get(entry.getKey()))
				{

					processEditSyncAttributeDescriptor(job, entry.getKey(), descriptor);
				}
			}
		}
	}

	/**
	 * Configures sync attributes of a {@link ComposedType}. The following attributes can be configured: IncludeInSync,
	 * CopyByValue, Untranslatable
	 * 
	 * @param syncJob
	 *           the synchronization job
	 * @param clazz
	 *           The class of {@link ComposedType}
	 * @param descriptor
	 *           Holds values for attributes to be modified for a given {@link SyncItemJob}
	 */
	protected void processEditSyncAttributeDescriptor(final SyncItemJob syncJob, final Class<?> clazz,
			final EditSyncAttributeDescriptorData descriptor)
	{

		final ComposedType composedType = tryGetComposedType(clazz);
		final AttributeDescriptor attributeDesc = tryGetAttributeDescriptor(composedType, descriptor.getQualifier());
		if (composedType != null && attributeDesc != null)
		{
			final SyncAttributeDescriptorConfig cfg = syncJob.getConfigFor(attributeDesc, true);
			if (cfg != null && Boolean.TRUE.equals(cfg.isIncludedInSync()))
			{
				if (LOG.isInfoEnabled())
				{
					LOG.info("Editing [" + composedType.getCode() + "] attribute [" + attributeDesc.getQualifier() + "] in sync job ["
							+ syncJob.getCode() + "]");
				}

				if (descriptor.getIncludeInSync() != null)
				{
					cfg.setIncludedInSync(descriptor.getIncludeInSync());
					if (LOG.isInfoEnabled())
					{
						LOG.info("Setting attribute includeInSync to [" + descriptor.getIncludeInSync() + "]");
					}
				}
				if (descriptor.getCopyByValue() != null)
				{
					cfg.setCopyByValue(descriptor.getCopyByValue());
					if (LOG.isInfoEnabled())
					{
						LOG.info("Setting attribute copyByValue to [" + descriptor.getCopyByValue() + "]");
					}
				}
				if (descriptor.getUntranslatable() != null)
				{
					cfg.setUntranslatable(descriptor.getUntranslatable());
					if (LOG.isInfoEnabled())
					{
						LOG.info("Setting attribute untranslatable to [" + descriptor.getUntranslatable() + "]");
					}
				}
			}
		}
	}

	protected SyncItemJob getCatalogSyncJob(final String catalogId)
	{
		// Lookup the catalog name
		final Catalog catalog = CatalogManager.getInstance().getCatalog(catalogId);
		if (catalog != null)
		{
			final CatalogVersion source = catalog.getCatalogVersion(CatalogManager.OFFLINE_VERSION);
			final CatalogVersion target = catalog.getCatalogVersion(CatalogManager.ONLINE_VERSION);

			if (source != null && target != null)
			{
				return CatalogManager.getInstance().getSyncJob(source, target);
			}
		}
		return null;
	}


	protected SyncItemJob getSyncJobForCatalog(final String catalogId)
	{
		return getSyncJobForCatalogAndQualifier(catalogId, createJobIdentifier(catalogId));
	}


	protected SyncItemJob getSyncJobForCatalogAndQualifier(final String catalogId, final String qualifier)
	{
		// Lookup the catalog name
		final Catalog catalog = CatalogManager.getInstance().getCatalog(catalogId);
		if (catalog != null)
		{
			final CatalogVersion source = catalog.getCatalogVersion(CatalogManager.OFFLINE_VERSION);
			final CatalogVersion target = catalog.getCatalogVersion(CatalogManager.ONLINE_VERSION);

			if (source != null && target != null)
			{
				return CatalogManager.getInstance().getSyncJob(source, target, qualifier);
			}
		}
		return null;
	}

	protected void processRootTypes(final SyncItemJob job, final String catalogId, final List<String> rootTypes)
	{
		if (!rootTypes.isEmpty())
		{
			final List<ComposedType> newRootTypes = new LinkedList<>((job.getRootTypes() != null ? job.getRootTypes()
					: Collections.<ComposedType> emptyList()));
			for (final String rootType : rootTypes)
			{
				final ComposedType ct = tryGetComposedType(rootType);
				if (ct != null && !newRootTypes.contains(ct))
				{
					LOG.info("adding Root Type [" + ct.getCode() + "] to Sync Job for Catalog [" + catalogId + "]");
					newRootTypes.add(ct);
				}
			}
			job.setRootTypes(newRootTypes);
		}
	}

	protected ComposedType tryGetComposedType(final Class<?> clazz)
	{
		try
		{
			return TypeManager.getInstance().getComposedType(clazz);
		}
		catch (final JaloItemNotFoundException jinfe)
		{
			LOG.warn("unable to resolve typecode for class " + clazz);
		}
		return null;
	}

	protected ComposedType tryGetComposedType(final String typeCode)
	{
		try
		{
			return TypeManager.getInstance().getComposedType(typeCode);
		}
		catch (final JaloItemNotFoundException jinfe)
		{
			LOG.warn("unable to resolve typecode " + typeCode);
		}
		return null;

	}

	protected AttributeDescriptor tryGetAttributeDescriptor(final ComposedType composedType, final String attributeName)
	{
		try
		{
			if (composedType != null)
			{
				return composedType.getDeclaredAttributeDescriptor(attributeName);
			}
		}
		catch (final JaloItemNotFoundException jinfe)
		{
			LOG.warn("Attribute [" + attributeName + "] for type [" + composedType.getCode() + "] not found");
		}
		return null;
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

	public List<String> getContentCatalogRootTypeCodes()
	{
		return contentCatalogRootTypeCodes;
	}

	public void setContentCatalogRootTypeCodes(final List<String> contentCatalogRootTypeCodes)
	{
		this.contentCatalogRootTypeCodes = contentCatalogRootTypeCodes;
	}

	public List<String> getProductCatalogRootTypeCodes()
	{
		return productCatalogRootTypeCodes;
	}

	public void setProductCatalogRootTypeCodes(final List<String> productCatalogRootTypeCodes)
	{
		this.productCatalogRootTypeCodes = productCatalogRootTypeCodes;
	}

	public Map<Class<?>, List<EditSyncAttributeDescriptorData>> getContentCatalogEditSyncDescriptors()
	{
		return contentCatalogEditSyncDescriptors;
	}

	public void setContentCatalogEditSyncDescriptors(
			final Map<Class<?>, List<EditSyncAttributeDescriptorData>> contentCatalogEditSyncDescriptors)
	{
		this.contentCatalogEditSyncDescriptors = contentCatalogEditSyncDescriptors;
	}

	public Map<Class<?>, List<EditSyncAttributeDescriptorData>> getProductCatalogEditSyncDescriptors()
	{
		return productCatalogEditSyncDescriptors;
	}

	public void setProductCatalogEditSyncDescriptors(
			final Map<Class<?>, List<EditSyncAttributeDescriptorData>> productCatalogEditSyncDescriptors)
	{
		this.productCatalogEditSyncDescriptors = productCatalogEditSyncDescriptors;
	}
}
