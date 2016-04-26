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
package de.hybris.y2ysync.task.runner.internal;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.StreamConfiguration;
import de.hybris.deltadetection.impl.InMemoryChangesCollector;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.task.TaskConditionModel;
import de.hybris.platform.task.TaskService;
import de.hybris.y2ysync.enums.Y2YSyncType;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import de.hybris.y2ysync.model.media.SyncImpExMediaModel;
import de.hybris.y2ysync.task.dao.Y2YSyncDAO;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;


@IntegrationTest
public class ProcessChangesTaskTest extends ServicelayerBaseTest
{
	private static final String TEST_SYNC_EXECUTION_ID = "TEST_SYNC_EXECUTION_ID";
	private static final String TEST_CONDITION_ID = "TEST_CONDITION_ID";
	private static final String TEST_INSERT_UPDATE_HEADER = "INSERT_UPDATE Product;code[unique=true];name[lang=pl]";
	private static final String TEST_REMOVE_HEADER = "REMOVE Product;code[unique=true]";
	private static final String TEST_TYPE_CODE = "Product";

	@Resource
	private ModelService modelService;
	@Resource
	private MediaService mediaService;
	@Resource
	private ChangeDetectionService changeDetectionService;
	@Resource
	private TaskService taskService;
	@Resource
	private TypeService typeService;
	@Resource
	private Y2YSyncDAO y2ySyncDAO;

	private TaskConditionModel condition;

	@Before
	public void setUp()
	{
		createExportCronJob();

		condition = modelService.create(TaskConditionModel.class);
		condition.setUniqueID(TEST_CONDITION_ID);

		modelService.saveAll();
	}

	private Y2YSyncCronJobModel createExportCronJob()
	{
		final Y2YStreamConfigurationContainerModel container = modelService.create(Y2YStreamConfigurationContainerModel.class);
		container.setId("testContainer");
		modelService.save(container);

		final Y2YSyncJobModel syncJob = modelService.create(Y2YSyncJobModel.class);
		syncJob.setCode("testJob");
		syncJob.setSyncType(Y2YSyncType.ZIP);
		syncJob.setStreamConfigurationContainer(container);
		modelService.save(syncJob);

		final Y2YSyncCronJobModel cronJob = modelService.create(Y2YSyncCronJobModel.class);
		cronJob.setCode(TEST_SYNC_EXECUTION_ID);
		cronJob.setJob(syncJob);
		modelService.save(cronJob);

		return cronJob;
	}

	@Test
	public void shouldNotCreateMediaWhenThereIsNoscript() throws Exception
	{
		final ProcessChangesTask task = givenProcessChangesTaskWith(noScript());

		final List<SyncImpExMediaModel> medias = whenExecuted(task);

		assertThat(medias).isEmpty();
	}

	@Test
	public void shouldTriggerConditionAfterFinishingTask() throws Exception
	{
		final ProcessChangesTask task = givenProcessChangesTaskWith(noScript());

		whenExecuted(task);
		final TaskConditionModel conditionToCheck = modelService.<TaskConditionModel> get(condition.getPk());

		assertThat(conditionToCheck.getFulfilled()).isTrue();
	}

	@Test
	public void shouldCreateMediaForGivenInsertUpdateScript() throws Exception
	{
		final String content = ";KEB;Kebab\n;KIE;Kielbasa";
		final ImportScript script = givenInsertUpdateScript(content);
		final ProcessChangesTask task = givenProcessChangesTaskWith(script);

		final List<SyncImpExMediaModel> medias = whenExecuted(task);

		assertThat(medias).hasSize(1);
		final SyncImpExMediaModel media = medias.get(0);
		assertThat(media).isNotNull();
		assertThat(media.getImpexHeader()).isEqualTo(TEST_INSERT_UPDATE_HEADER);
		assertThat(media.getSyncType().getCode()).isEqualTo(TEST_TYPE_CODE);
		assertThat(media.getExportCronJob().getCode()).isEqualTo(TEST_SYNC_EXECUTION_ID);
		assertThat(new String(mediaService.getDataFromMedia(media))).isEqualTo(content);
	}


	@Test
	public void shouldCreateMediaForGivenRemoveScript() throws Exception
	{
		final String content = ";KEB\n;BUR";
		final ImportScript script = givenRemoveScript(content);
		final ProcessChangesTask task = givenProcessChangesTaskWith(script);

		final List<SyncImpExMediaModel> medias = whenExecuted(task);

		assertThat(medias).hasSize(1);
		final SyncImpExMediaModel media = medias.get(0);
		assertThat(media).isNotNull();
		assertThat(media.getImpexHeader()).isEqualTo(TEST_REMOVE_HEADER);
		assertThat(media.getSyncType().getCode()).isEqualTo(TEST_TYPE_CODE);
		assertThat(media.getExportCronJob().getCode()).isEqualTo(TEST_SYNC_EXECUTION_ID);
		assertThat(new String(mediaService.getDataFromMedia(media))).isEqualTo(content);
	}

	@Test
	public void shouldCreateMediaForGivenInsertUpdateAndRemoveScript() throws Exception
	{
		//given
		final String rowsToInsertUpdate = ";KEB;Kebab\n;KIE;Kielbasa";
		final ImportScript insertUpdateScript = givenInsertUpdateScript(rowsToInsertUpdate);

		final String rowsToRemove = ";KEB\n;BUR";
		final ImportScript removeScript = givenRemoveScript(rowsToRemove);

		final ProcessChangesTask task = givenProcessChangesTaskWith(insertUpdateScript, removeScript);

		//when
		final List<SyncImpExMediaModel> medias = whenExecuted(task);

		//then
		assertThat(medias).hasSize(2);

		final SyncImpExMediaModel removeMedia = medias.stream().filter(m -> m.getImpexHeader().contains("REMOVE")).findFirst()
				.orElse(null);
		final SyncImpExMediaModel insertUpdateMedia = medias.stream().filter(m -> m.getImpexHeader().contains("INSERT_UPDATE"))
				.findFirst().orElse(null);

		assertThat(removeMedia).isNotNull();
		assertThat(removeMedia.getImpexHeader()).isEqualTo(TEST_REMOVE_HEADER);
		assertThat(removeMedia.getSyncType().getCode()).isEqualTo(TEST_TYPE_CODE);
		assertThat(removeMedia.getExportCronJob().getCode()).isEqualTo(TEST_SYNC_EXECUTION_ID);
		assertThat(new String(mediaService.getDataFromMedia(removeMedia))).isEqualTo(rowsToRemove);

		assertThat(insertUpdateMedia).isNotNull();
		assertThat(insertUpdateMedia.getImpexHeader()).isEqualTo(TEST_INSERT_UPDATE_HEADER);
		assertThat(insertUpdateMedia.getSyncType().getCode()).isEqualTo(TEST_TYPE_CODE);
		assertThat(insertUpdateMedia.getExportCronJob().getCode()).isEqualTo(TEST_SYNC_EXECUTION_ID);
		assertThat(new String(mediaService.getDataFromMedia(insertUpdateMedia))).isEqualTo(rowsToInsertUpdate);
	}

	@Test
	public void shouldConsumeChangesWhenTaskIsFinished() throws Exception
	{

		final String titleCode = "TITLE-" + uniqueId();
		final TitleModel testTitle = modelService.create(TitleModel.class);
		testTitle.setCode(titleCode);
		modelService.save(testTitle);

		final InMemoryChangesCollector collector = new InMemoryChangesCollector();
		final StreamConfiguration streamConfiguration = StreamConfiguration.buildFor(uniqueId())
				.withItemSelector("{item.code}=?code").withParameters(ImmutableMap.of("code", titleCode));
		changeDetectionService.collectChangesForType(typeService.getComposedTypeForClass(TitleModel.class), streamConfiguration,
				collector);

		assertThat(collector.getChanges()).hasSize(1);

		final ProcessChangesTask task = givenProcessChangesFor(collector.getChanges());
		task.execute();

		collector.clearChanges();
		changeDetectionService.collectChangesForType(typeService.getComposedTypeForClass(TitleModel.class), streamConfiguration,
				collector);

		assertThat(collector.getChanges()).isEmpty();
	}

	private ImportScript givenInsertUpdateScript(final String rows)
	{
		return new ImportScript(TEST_TYPE_CODE, TEST_INSERT_UPDATE_HEADER, rows, null);
	}

	private ImportScript givenRemoveScript(final String rows)
	{
		return new ImportScript(TEST_TYPE_CODE, TEST_REMOVE_HEADER, rows, null);
	}

	private ProcessChangesTask givenProcessChangesTaskWith(final ImportScript... scripts)
	{
		return new ProcessChangesTask(modelService, mediaService, changeDetectionService, taskService, typeService, y2ySyncDAO,
				ImmutableList.of(), ImmutableList.copyOf(scripts), TEST_CONDITION_ID, TEST_SYNC_EXECUTION_ID, StringUtils.EMPTY);
	}

	private ProcessChangesTask givenProcessChangesFor(final List<ItemChangeDTO> changes)
	{
		return new ProcessChangesTask(modelService, mediaService, changeDetectionService, taskService, typeService, y2ySyncDAO,
				ImmutableList.copyOf(changes), ImmutableList.of(), TEST_CONDITION_ID, TEST_SYNC_EXECUTION_ID, StringUtils.EMPTY);
	}

	private List<SyncImpExMediaModel> whenExecuted(final ProcessChangesTask task) throws Exception
	{
		return task.execute().stream().map(code -> (SyncImpExMediaModel) mediaService.getMedia(code)).collect(Collectors.toList());
	}

	private ImportScript[] noScript()
	{
		return new ImportScript[] {};
	}

	private String uniqueId()
	{
		return UUID.randomUUID().toString();
	}
}
