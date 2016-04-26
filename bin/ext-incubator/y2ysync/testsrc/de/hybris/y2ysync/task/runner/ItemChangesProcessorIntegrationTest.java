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
package de.hybris.y2ysync.task.runner;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.task.TaskConditionModel;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.task.utils.NeedsTaskEngine;
import de.hybris.platform.util.Config;
import de.hybris.y2ysync.deltadetection.collector.MediaBatchingCollector;
import de.hybris.y2ysync.enums.Y2YSyncType;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationModel;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import de.hybris.y2ysync.model.media.SyncImpExMediaModel;
import de.hybris.y2ysync.task.dao.Y2YSyncDAO;
import de.hybris.y2ysync.task.internal.MediasForType;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.util.SerializationUtils;

import com.google.common.collect.ImmutableMap;


/**
 * Integration tests for ItemChangesProcessor.
 */
@IntegrationTest
@NeedsTaskEngine
public class ItemChangesProcessorIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	private TaskService taskService;
	@Resource
	private ModelService modelService;
	@Resource
	private MediaService mediaService;
	@Resource
	private TypeService typeService;
	@Resource
	private ChangeDetectionService changeDetectionService;
	@Resource
	private Y2YSyncDAO y2ySyncDAO;

	private TitleModel testTitleFoo;
	private TitleModel testTitleBar;
	private TaskConditionModel condition;
	private Y2YStreamConfigurationContainerModel testContainer;
	private Y2YSyncCronJobModel testCronJob;

	private static final String CHANGES_PROCESSOR_RUNNER_BEAN_ID = "itemChangesProcessor";
	private static final String MEDIA_PK_KEY = "mediaPK";
	private static final String CONDITION_NAME_KEY = "conditionName";
	private static final String IMPEX_HEADER_KEY = "impexHeader";
	private static final String TYPE_CODE_KEY = "typeCode";
	private static final String SYNC_EXECUTION_ID_KEY = "syncExecutionID";

	private static final String STREAM_ID_XXX = "FeedXXX";
	private static final String TEST_SYNC_EXECUTION_ID = "TEST_SYNC_EXECUTION_ID";
	private static final String TEST_CONDITION_ID = "TEST_CONDITION_ID";

	private static final String IMPEX_HEADER_CORRECT = "code[unique=true];";
	private static final String IMPEX_HEADER_ERROR = "code[unique=true];ERROR_IN_HEADER";

	final double timeFactor = Math.max(15.0, Config.getDouble("platform.test.timefactor", 15.0));

	@Before
	public void setUp() throws Exception
	{
		assertThat(taskService.getEngine().isRunning()).isTrue();
		testTitleFoo = modelService.create(TitleModel.class);
		testTitleFoo.setCode("Foo");
		testTitleBar = modelService.create(TitleModel.class);
		testTitleBar.setCode("Bar");

		testContainer = modelService.create(Y2YStreamConfigurationContainerModel.class);
		testContainer.setId("testContainer");
		modelService.save(testContainer);

		testCronJob = createExportCronJob();

		condition = modelService.create(TaskConditionModel.class);
		condition.setUniqueID(TEST_CONDITION_ID);
		modelService.save(condition);
	}

	@Test
	public void testRunSuccessfully() throws Exception
	{
		// given - delta  2 new titles
		modelService.saveAll(testTitleFoo, testTitleBar);
		final ComposedTypeModel titleComposedType = typeService.getComposedTypeForClass(TitleModel.class);
		final MediaBatchingCollector collector = findChangesWithBatchingCollector(titleComposedType);

		final List<PK> mediaPks = collector.getPksOfBatches();
		assertThat(mediaPks).hasSize(1);
		final List<ItemChangeDTO> deserializedChanges = getDeserializedChanges("testDeltaMedia-1-0");
		assertThat(deserializedChanges).hasSize(2);

		final MediasForType mediasForType1 = MediasForType.builder().withComposedTypeModel(titleComposedType)
				.withImpExHeader(IMPEX_HEADER_CORRECT).withDataHubColumns(StringUtils.EMPTY).withMediaPks(mediaPks).build();

		final TaskModel task = prepareTask(getTaskContext(TEST_SYNC_EXECUTION_ID, mediaPks.get(0), mediasForType1));
		task.setExecutionDate(new Date());
		//when
		taskService.scheduleTask(task);

		Thread.sleep((long) (1000 * timeFactor));

		//then
		final String content = testTitleFoo.getCode() + "\n" + testTitleBar.getCode();
		verifyScriptMediaCreated("INSERT_UPDATE Title;" + IMPEX_HEADER_CORRECT, content);

		verifyChangeConsumption(testTitleFoo, true); //consumed
		verifyChangeConsumption(testTitleBar, true); //consumed
		modelService.refresh(condition);
		assertThat(condition.getFulfilled()).isTrue(); //event triggered
	}

	private void verifyScriptMediaCreated(final String expectedImpexHeader, final String expectedContent)
	{
		final List<SyncImpExMediaModel> syncImpexMedias = y2ySyncDAO.findSyncMediasBySyncCronJob(testCronJob.getCode());
		assertThat(syncImpexMedias).hasSize(1);
		final SyncImpExMediaModel media = syncImpexMedias.get(0);
		assertThat(media.getImpexHeader()).isEqualTo(expectedImpexHeader);
		assertThat(new String(mediaService.getDataFromMedia(media))).contains(expectedContent);
	}


	@Test
	public void testRunWithErrorsInImpexHeader() throws Exception
	{
		// given - delta 1 new title
		modelService.save(testTitleFoo);
		final ComposedTypeModel titleComposedType = typeService.getComposedTypeForClass(TitleModel.class);
		final MediaBatchingCollector collector = findChangesWithBatchingCollector(titleComposedType);

		final List<PK> mediaPks = collector.getPksOfBatches();
		assertThat(mediaPks).hasSize(1);
		final List<ItemChangeDTO> deserializedChanges = getDeserializedChanges("testDeltaMedia-1-0");
		assertThat(deserializedChanges).hasSize(1);

		final MediasForType mediasForType1 = MediasForType.builder().withComposedTypeModel(titleComposedType)
				.withImpExHeader(IMPEX_HEADER_ERROR).withDataHubColumns(StringUtils.EMPTY).withMediaPks(mediaPks).build();

		final TaskModel task = prepareTask(getTaskContext(TEST_SYNC_EXECUTION_ID, mediaPks.get(0), mediasForType1));
		task.setExecutionDate(new Date());
		//when
		try
		{
			taskService.scheduleTask(task);
			Thread.sleep((long) (1000 * timeFactor));
		}
		catch (final Exception e)
		{
			//correct
			assertThat(e).isInstanceOf(ImpExException.class);
		}
		finally
		{
			//then
			final List<SyncImpExMediaModel> syncImpexMedias = y2ySyncDAO.findSyncMediasBySyncCronJob(testCronJob.getCode());
			assertThat(syncImpexMedias).isEmpty(); //no script media created
			verifyChangeConsumption(testTitleFoo, false); //not consumed
			modelService.refresh(condition);
			assertThat(condition.getFulfilled()).isFalse(); //event not triggered
		}
	}

	@Test
	public void testRunWithErrorsDuringChangeConsumption() throws Exception
	{
		// given - delta 1 new title
		modelService.save(testTitleFoo);
		final ComposedTypeModel titleComposedType = typeService.getComposedTypeForClass(TitleModel.class);
		final MediaBatchingCollector collector = findChangesWithBatchingCollector(titleComposedType);
		final Y2YStreamConfigurationModel badStream = modelService.create(Y2YStreamConfigurationModel.class);
		badStream.setContainer(testContainer);
		badStream.setItemTypeForStream(titleComposedType);
		badStream.setStreamId(STREAM_ID_XXX);
		badStream.setInfoExpression("#{getBADMETHOD()}"); //this will lead to parse spel exp error during consuming changes
		modelService.save(badStream);

		final List<PK> mediaPks = collector.getPksOfBatches();
		assertThat(mediaPks).hasSize(1);
		final List<ItemChangeDTO> deserializedChanges = getDeserializedChanges("testDeltaMedia-1-0");
		assertThat(deserializedChanges).hasSize(1);

		final MediasForType mediasForType1 = MediasForType.builder().withComposedTypeModel(titleComposedType)
				.withImpExHeader(IMPEX_HEADER_CORRECT).withDataHubColumns(StringUtils.EMPTY).withMediaPks(mediaPks).build();

		final TaskModel task = prepareTask(getTaskContext(TEST_SYNC_EXECUTION_ID, mediaPks.get(0), mediasForType1));
		task.setExecutionDate(new Date());
		//when
		try
		{
			taskService.scheduleTask(task);
			Thread.sleep((long) (1000 * timeFactor));
		}
		catch (final Exception e)
		{
			assertThat(e).isInstanceOf(SpelEvaluationException.class);
		}
		finally
		{
			//then
			final List<SyncImpExMediaModel> syncImpexMedias = y2ySyncDAO.findSyncMediasBySyncCronJob(testCronJob.getCode());
			assertThat(syncImpexMedias).isEmpty(); //no script media created
			verifyChangeConsumption(testTitleFoo, false); //not consumed
			modelService.refresh(condition);
			assertThat(condition.getFulfilled()).isFalse(); //event not triggered
		}
	}

	private TaskModel prepareTask(final Map<String, Object> ctx)
	{
		final TaskModel task = modelService.create(TaskModel.class);
		task.setContext(ctx);
		task.setRunnerBean(CHANGES_PROCESSOR_RUNNER_BEAN_ID);
		return task;
	}

	private Map<String, Object> getTaskContext(final String syncExecutionId, final PK mediaPK, final MediasForType mediasForType)
	{
		final ImmutableMap.Builder<String, Object> result = ImmutableMap.<String, Object> builder()
				.put(SYNC_EXECUTION_ID_KEY, syncExecutionId) //
				.put(MEDIA_PK_KEY, mediaPK) //
				.put(CONDITION_NAME_KEY, TEST_CONDITION_ID) //
				.put(IMPEX_HEADER_KEY, mediasForType.getImpexHeader()) //
				.put(TYPE_CODE_KEY, mediasForType.getComposedTypeModel().getCode());//
		return result.build();
	}

	private Y2YSyncCronJobModel createExportCronJob()
	{
		final Y2YSyncJobModel syncJob = modelService.create(Y2YSyncJobModel.class);
		syncJob.setCode("testJob");
		syncJob.setSyncType(Y2YSyncType.ZIP);
		syncJob.setStreamConfigurationContainer(testContainer);
		modelService.save(syncJob);

		final Y2YSyncCronJobModel cronJob = modelService.create(Y2YSyncCronJobModel.class);
		cronJob.setCode(TEST_SYNC_EXECUTION_ID);
		cronJob.setJob(syncJob);
		modelService.save(cronJob);

		return cronJob;
	}

	private MediaBatchingCollector findChangesWithBatchingCollector(final ComposedTypeModel composedType)
	{
		final MediaBatchingCollector collector = new MediaBatchingCollector("testDeltaMedia", 3, modelService, mediaService);
		collector.setId("1");
		changeDetectionService.collectChangesForType(composedType, STREAM_ID_XXX, collector);
		return collector;
	}

	private void verifyChangeConsumption(final ItemModel item, final boolean consumptionExpected)
	{
		final ItemChangeDTO change = changeDetectionService.getChangeForExistingItem(item, STREAM_ID_XXX);
		if (consumptionExpected)
		{
			assertThat(change).isNull();
		}
		else
		{
			assertThat(change).isNotNull();
		}
	}

	private List<ItemChangeDTO> getDeserializedChanges(final String mediaCode)
	{
		final CatalogUnawareMediaModel media0 = (CatalogUnawareMediaModel) mediaService.getMedia(mediaCode);
		final byte[] data0 = mediaService.getDataFromMedia(media0);
		return (List<ItemChangeDTO>) SerializationUtils.deserialize(data0);
	}

}
