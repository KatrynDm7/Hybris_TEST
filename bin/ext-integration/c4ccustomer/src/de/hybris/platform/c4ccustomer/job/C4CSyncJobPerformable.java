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
package de.hybris.platform.c4ccustomer.job;

import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.StreamConfiguration;
import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.c4ccustomer.deltadetection.C4CAggregatingCollector;
import de.hybris.platform.c4ccustomer.deltadetection.TypeChangesData;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import de.hybris.y2ysync.deltadetection.collector.BatchingCollector;
import de.hybris.y2ysync.deltadetection.collector.MediaBatchingCollector;
import de.hybris.y2ysync.enums.Y2YSyncType;
import de.hybris.y2ysync.model.Y2YColumnDefinitionModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationModel;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import de.hybris.y2ysync.task.internal.MediasForType;
import de.hybris.y2ysync.task.internal.SyncTaskFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * C4C synchronization cron job.
 */
public abstract class C4CSyncJobPerformable extends AbstractJobPerformable<Y2YSyncCronJobModel>
{
	protected static final String COLUMNS_DELIMITER = ";";
	private static final Logger LOG = LoggerFactory.getLogger(C4CSyncJobPerformable.class);

	private int batchSize = 100;

	private ChangeDetectionService changeDetectionService;
	private MediaService mediaService;
	private TypeService typeService;
	private SyncTaskFactory syncTaskFactory;

	/**
	 * Get collector that joins customers and addresses.
	 *
	 * @return collector
	 */
	protected abstract C4CAggregatingCollector getAggregatingCollector();

	/**
	 * Get collector that splits change list into chunks.
	 * @return constructed collector
	 */
	protected BatchingCollector getSplittingCollector(final String syncExecutionId, final String collectorId)
	{
		final MediaBatchingCollector result = new MediaBatchingCollector(
				syncExecutionId, getBatchSize(), modelService, getMediaService());
		result.setId(collectorId);
		return result;
	}

	@Override
	public PerformResult perform(final Y2YSyncCronJobModel cronJob)
	{
		LOG.trace("Entering into C4CSyncJobPerformable#perform");
		final Y2YSyncJobModel jobModel = cronJob.getJob();
		final Y2YStreamConfigurationContainerModel streamConfigurationContainer = jobModel.getStreamConfigurationContainer();

		final Map<String, Object> globalQueryParameters = new HashMap<>();
		final CatalogVersionModel catalogVersion = streamConfigurationContainer.getCatalogVersion();
		fillParameters(globalQueryParameters, catalogVersion);

		final Set<StreamConfigurationModel> configurations = getActiveConfigurations(streamConfigurationContainer);
		if (configurations.isEmpty())
		{
			LOG.warn("No streams configured for sync. Terminating job.");
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}

		final Map<String, TypeChangesData> changes = getChangesDataMap(
				cronJob.getCode(), globalQueryParameters, configurations);

		final List<MediasForType> allMedia = changes.entrySet().stream()
				.peek(i -> i.getValue().getCollector().finish())
				.map(e -> MediasForType.builder()
						.withComposedTypeModel(getTypeService().getComposedTypeForCode(e.getKey()))
						.withImpExHeader(e.getValue().getImpexHeader())
						.withDataHubColumns(e.getValue().getDataHubColumns())
						.withMediaPks(e.getValue().getCollector().getPksOfBatches())
						.build())
				.collect(Collectors.toList());

		LOG.debug("Created {} media records", allMedia.size());

		createAllTasksInTx(cronJob.getCode(), allMedia, jobModel.getSyncType());

		LOG.trace("Leaving C4CSyncJobPerformable#perform");

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.RUNNING);
	}

	/**
	 * Collect changes, store them on media.
	 *
	 * @param syncExecutionId unique synchronization session id
	 * @param globalQueryParameters synchronization settings
	 * @param configurations what to synchronize
	 * @return changes grouped by item type
	 */
	protected Map<String, TypeChangesData> getChangesDataMap(
			final String syncExecutionId, final Map<String, Object> globalQueryParameters,
			final Set<StreamConfigurationModel> configurations)
	{
		final Map<String, TypeChangesData> containers = new HashMap<>(2);
		final C4CAggregatingCollector collector = getAggregatingCollector();
		final String qualifier = UUID.randomUUID().toString().replace("-", "");
		final BatchingCollector customerCollector = getSplittingCollector(syncExecutionId, CustomerModel._TYPECODE + '-' + qualifier);
		final BatchingCollector addressCollector = getSplittingCollector(syncExecutionId, AddressModel._TYPECODE + '-' + qualifier);
		collector.setCustomerCollector(customerCollector);
		collector.setAddressCollector(addressCollector);
		assignStreamIds(collector, configurations);
		for (final StreamConfigurationModel streamConfiguration : configurations)
		{
			final Y2YStreamConfigurationModel y2YStreamConfigurationModel = (Y2YStreamConfigurationModel) streamConfiguration;
			LOG.debug("Adding configuration {} infoExpression='{}'",
					y2YStreamConfigurationModel.getStreamId(),
					y2YStreamConfigurationModel.getInfoExpression());
			final StreamConfiguration configuration = toStreamConfiguration(globalQueryParameters, y2YStreamConfigurationModel);
			BatchingCollector processor;
			if (CustomerModel._TYPECODE.equals(streamConfiguration.getItemTypeForStream().getCode()))
			{
				processor = customerCollector;
			}
			else if (AddressModel._TYPECODE.equals(streamConfiguration.getItemTypeForStream().getCode()))
			{
				processor = addressCollector;
			}
			else
			{
				throw new IllegalArgumentException("Unsupported type " + streamConfiguration.getItemTypeForStream().getCode());
			}
			addTypeChangesDataForConfig(containers, y2YStreamConfigurationModel, processor);
			getChangeDetectionService().collectChangesForType(
					streamConfiguration.getItemTypeForStream(),
					configuration,
					collector);
		}
		return containers;
	}

	/**
	 * Assign stream ids to corresponding collectors.
	 * <p>Needed for further call of {@code consumeChanges}.</p>
	 * @param collector aggregating collector, having separate sub-collectors for customers and for addresses.
	 * @param configurations synchronization configurations
	 */
	protected void assignStreamIds(final C4CAggregatingCollector collector, final Set<StreamConfigurationModel> configurations)
	{
		for (final StreamConfigurationModel streamConfiguration : configurations)
		{
			if (CustomerModel._TYPECODE.equals(streamConfiguration.getItemTypeForStream().getCode()))
			{
				collector.setCustomerConfigurationId(streamConfiguration.getStreamId());
			}
			else if (AddressModel._TYPECODE.equals(streamConfiguration.getItemTypeForStream().getCode()))
			{
				collector.setAddressConfigurationId(streamConfiguration.getStreamId());
			}
			else
			{
				throw new IllegalArgumentException("Unsupported type " + streamConfiguration.getItemTypeForStream().getCode());
			}
		}
	}

	/**
	 * Create entry of {@link TypeChangesData} corresponding to the configuration.
	 *
	 * @param containers receiver of the entries
	 * @param y2YStreamConfigurationModel configuration details
	 */
	protected TypeChangesData addTypeChangesDataForConfig(final Map<String, TypeChangesData> containers,
			final Y2YStreamConfigurationModel y2YStreamConfigurationModel, final BatchingCollector collector)
	{
		TypeChangesData data = containers.get(y2YStreamConfigurationModel.getItemTypeForStream().getCode());
		if (data == null)
		{
			data = new TypeChangesData(
					join(y2YStreamConfigurationModel, Y2YColumnDefinitionModel::getImpexHeader),
					join(y2YStreamConfigurationModel, Y2YColumnDefinitionModel::getColumnName),
					collector
			);
			containers.put(y2YStreamConfigurationModel.getItemTypeForStream().getCode(), data);
		}
		return data;
	}

	/**
	 * Create semicolon-delimited list of columns from configuration.
	 *
	 * @param y2yStreamConfiguration configuration
	 * @param mapper how to process each column definition
	 * @return resulting list
	 */
	protected String join(final Y2YStreamConfigurationModel y2yStreamConfiguration,
			final Function<Y2YColumnDefinitionModel, String> mapper)
	{
		final String columns = y2yStreamConfiguration.getColumnDefinitions().stream().map(mapper)
				.collect(Collectors.joining(COLUMNS_DELIMITER));

		LOG.debug("StreamConfig Columns string for type: {} >>>> {}",
				y2yStreamConfiguration.getItemTypeForStream().getCode(), columns);

		return columns;
	}

	/**
	 * All active configurations of given container.
	 *
	 * @param container configuration container
	 * @return subsequent configurations
	 */
	protected Set<StreamConfigurationModel> getActiveConfigurations(final Y2YStreamConfigurationContainerModel container)
	{
		return container.getConfigurations().stream().filter(StreamConfigurationModel::getActive).collect(Collectors.toSet());
	}

	/**
	 * Run prepared synchronization tasks.
	 *
	 * @param syncExecutionId session id
	 * @param allMedia data descriptors
	 * @param syncType how to synchronize
	 */
	protected void createAllTasksInTx(final String syncExecutionId, final List<MediasForType> allMedia, final Y2YSyncType syncType)
	{
		try
		{
			Transaction.current().execute(new TransactionBody()
			{
				@Override
				public Void execute() throws Exception
				{
					if (Y2YSyncType.ZIP == syncType)
					{
						getSyncTaskFactory().runSyncTasksForZipResult(syncExecutionId, allMedia);
					}
					else if (Y2YSyncType.DATAHUB == syncType)
					{
						getSyncTaskFactory().runSyncTasksForDatahubResult(syncExecutionId, allMedia);
					}
					return null;
				}
			});
		}
		catch (final Exception e)
		{
			throw new IllegalStateException("Exception occurred while creating tasks", e);
		}
	}

	/**
	 * Merge parameters with stream configuration.
	 *
	 * @param globalQueryParameters global settings
	 * @param y2YStreamConfigurationModel spream own settings
	 * @return merged configuration
	 */
	protected StreamConfiguration toStreamConfiguration(final Map<String, Object> globalQueryParameters,
	                                                    final Y2YStreamConfigurationModel y2YStreamConfigurationModel)
	{
		final Map<String, Object> streamQueryParameters = new HashMap<>(globalQueryParameters);
		fillParameters(streamQueryParameters, y2YStreamConfigurationModel.getCatalogVersion());

		return StreamConfiguration.buildFor(y2YStreamConfigurationModel.getStreamId())
				.withItemSelector(y2YStreamConfigurationModel.getWhereClause())
				.withVersionValue(y2YStreamConfigurationModel.getVersionSelectClause())
				.withExcludedTypeCodes(y2YStreamConfigurationModel.getExcludedTypes())
				.withParameters(streamQueryParameters);
	}

	/**
	 * Collect parameters.
	 *
	 * @param globalQueryParameters global parameters
	 * @param catalogVersion optional catalog version
	 */
	protected void fillParameters(final Map<String, Object> globalQueryParameters, final CatalogVersionModel catalogVersion)
	{
		if (catalogVersion == null)
		{
			return;
		}

		globalQueryParameters.put("catalog", catalogVersion.getCatalog());
		globalQueryParameters.put("catalogVersion", catalogVersion);
		globalQueryParameters.put("catalogVersionCode", catalogVersion.getVersion());
		final String catalogName = catalogVersion.getCatalog().getName();
		if (StringUtils.isNotEmpty(catalogName))
		{
			globalQueryParameters.put("catalogName", catalogName);
		}
	}

	@Required
	public void setChangeDetectionService(final ChangeDetectionService changeDetectionService)
	{
		this.changeDetectionService = changeDetectionService;
	}

	@Required
	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
	}

	@Required
	public void setSyncTaskFactory(final SyncTaskFactory syncTaskFactory)
	{
		this.syncTaskFactory = syncTaskFactory;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	public void setBatchSize(final int value)
	{
		batchSize = value;
	}

	/**
	 * @return change detection service.
	 */
	protected ChangeDetectionService getChangeDetectionService()
	{
		return changeDetectionService;
	}

	/**
	 * @return media service.
	 */
	protected MediaService getMediaService()
	{
		return mediaService;
	}

	/**
	 * @return synchronization task factory.
	 */
	protected SyncTaskFactory getSyncTaskFactory()
	{
		return syncTaskFactory;
	}

	/**
	 * @return type service.
	 */
	protected TypeService getTypeService()
	{
		return typeService;
	}

	/**
	 * @return chunk length.
	 */
	protected int getBatchSize()
	{
		return batchSize;
	}

}
