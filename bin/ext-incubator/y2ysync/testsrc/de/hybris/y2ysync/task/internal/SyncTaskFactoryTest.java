package de.hybris.y2ysync.task.internal;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskConditionModel;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class SyncTaskFactoryTest
{
	@InjectMocks
	private final SyncTaskFactory service = new SyncTaskFactory();
	@Mock
	private ModelService modelService;
	@Mock
	private TaskService taskService;
	@Mock
	private TaskModel task1, task2, mainTask;
	@Mock
	private TaskConditionModel condition1, condition2;
	@Mock
	private ComposedTypeModel composedType1, composedType2;

	@Before
	public void setUp() throws Exception
	{
		given(modelService.create(TaskModel.class)).willReturn(task1, task2, mainTask);
		given(modelService.create(TaskConditionModel.class)).willReturn(condition1, condition2);
		given(composedType1.getCode()).willReturn("Product");
		given(composedType2.getCode()).willReturn("Title");
	}


	@Test
	public void shouldRunChunkTasksForEachMediaPKPlusOneMainTaskWithConditions() throws Exception
	{
		// given
		final String syncExecutionID = "testSyncExecutionId";
		final PK productPk = PK.createFixedUUIDPK(100, 1);
		final PK titlePk = PK.createFixedUUIDPK(101, 1);

		final MediasForType mediasForType1 = MediasForType.builder().withComposedTypeModel(composedType1)
				.withImpExHeader(";foo;bar;baz;").withDataHubColumns(StringUtils.EMPTY).withMediaPks(Lists.newArrayList(productPk))
				.build();
		final MediasForType mediasForType2 = MediasForType.builder().withComposedTypeModel(composedType2)
				.withImpExHeader(";baz;boom;maz;").withDataHubColumns(StringUtils.EMPTY).withMediaPks(Lists.newArrayList(titlePk))
				.build();
		final ArrayList<MediasForType> mediasPerType = Lists.newArrayList(mediasForType1, mediasForType2);

		// when
		service.runSyncTasksForZipResult(syncExecutionID, mediasPerType);

		// then
		verify(task1).setRunnerBean(SyncTaskFactory.CHUNK_TASK_RUNNER_BEAN_ID);
		verify(task2).setRunnerBean(SyncTaskFactory.CHUNK_TASK_RUNNER_BEAN_ID);
		verify(task1).setContext(service.getChunkTaskContext(syncExecutionID, productPk, mediasForType1));
		verify(task2).setContext(service.getChunkTaskContext(syncExecutionID, titlePk, mediasForType2));
		verify(task1).setExecutionDate(anyDate());
		verify(task2).setExecutionDate(anyDate());

		verify(condition1).setUniqueID(syncExecutionID + "_" + productPk);
		verify(condition2).setUniqueID(syncExecutionID + "_" + titlePk);

		verify(mainTask).setContext(service.getMainTaskContext(syncExecutionID));
		verify(mainTask).setConditions(exactConditions(condition1, condition2));

		verify(taskService).scheduleTask(task1);
		verify(taskService).scheduleTask(task2);
		verify(taskService).scheduleTask(mainTask);
	}

	private static <T> T anyDate()
	{
		return (T) argThat(new DateArgumentMatcher());
	}

	private static <T> T exactConditions(final TaskConditionModel... conditions)
	{
		return (T) argThat(new SetOfTaskConditions(conditions));
	}

	private static class DateArgumentMatcher extends ArgumentMatcher<Date>
	{

		@Override
		public boolean matches(final Object argument)
		{
			return argument instanceof Date;
		}
	}

	private static class SetOfTaskConditions extends ArgumentMatcher<Set<TaskConditionModel>>
	{
		private final List<TaskConditionModel> conditions;

		public SetOfTaskConditions(final TaskConditionModel... conditions)
		{
			this.conditions = Arrays.asList(conditions);
		}


		@Override
		public boolean matches(final Object argument)
		{
			return ((Set) argument).containsAll(conditions);
		}
	}
}
