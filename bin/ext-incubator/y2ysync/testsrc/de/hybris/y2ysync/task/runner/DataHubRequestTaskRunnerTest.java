package de.hybris.y2ysync.task.runner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import de.hybris.y2ysync.task.dao.Y2YSyncDAO;
import de.hybris.y2ysync.task.internal.SyncTaskFactory;
import de.hybris.y2ysync.task.runner.internal.DataHubRequestCreator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableMap;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class DataHubRequestTaskRunnerTest
{
	public static final String TEST_EXECUTION_ID = "testExecutionID";
	public static final String TEST_DATAHUB_URL = "http://localhost:8080/some/feed";
	public static final String DEFAULT_DATAHUB_URL = "http://localhost:5050/default/feed";

	@InjectMocks
	private final DataHubRequestTaskRunner taskRunner = new DataHubRequestTaskRunner()
	{
		@Override
		String getDataHubUrlFromProperties()
		{
			return DEFAULT_DATAHUB_URL;
		}
	};

	@InjectMocks
	private final DataHubRequestTaskRunner wronglyConfiguredRunner = new DataHubRequestTaskRunner()
	{
		@Override
		String getDataHubUrlFromProperties()
		{
			return null;
		}
	};
	@Mock
	private Y2YSyncDAO syncDAO;
	@Mock
	private DataHubRequestCreator requestCreator;
	@Mock
	private ModelService modelService;
	@Mock
	private TaskService taskService;
	@Mock
	private TaskModel task;
	@Mock
	private Y2YSyncCronJobModel cronJob;
	@Mock
	private Y2YSyncJobModel job;

	@Before
	public void setUp() throws Exception
	{
		given(task.getContext()).willReturn(
				ImmutableMap.builder().put(SyncTaskFactory.SYNC_EXECUTION_ID_KEY, TEST_EXECUTION_ID).build());
		given(syncDAO.findSyncCronJobByCode(TEST_EXECUTION_ID)).willReturn(cronJob);
		given(cronJob.getJob()).willReturn(job);
	}

	@Test
	public void shouldUseDataHubUrlFromY2YSyncJobIfPresent() throws Exception
	{
		// given
		given(job.getDataHubUrl()).willReturn(TEST_DATAHUB_URL);

		// when
		taskRunner.run(taskService, task);

		// then
		verify(requestCreator).sendRequest(TEST_EXECUTION_ID, TEST_DATAHUB_URL);
	}

	@Test
	public void shouldUseDataHubUrlFromPropertiesIfY2YSyncJobHasntConfiguredItDirectly() throws Exception
	{
		// given
		given(job.getDataHubUrl()).willReturn(null);

		// when
		taskRunner.run(taskService, task);

		// then
		verify(requestCreator).sendRequest(TEST_EXECUTION_ID, DEFAULT_DATAHUB_URL);
	}

	@Test
	public void shouldSaveRelatedCronJobWithResultErrorWhenDataHubUrlCannotBeDetermined() throws Exception
	{
		// when
		wronglyConfiguredRunner.run(taskService, task);

		// then
		verify(cronJob).setStatus(CronJobStatus.FINISHED);
		verify(cronJob).setResult(CronJobResult.ERROR);
		verify(modelService).save(cronJob);
	}
}
