/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package de.hybris.platform.task.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.task.TestTaskRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;


@IntegrationTest
public class DefaultTaskServiceNodeGroupsIntegrationTest extends ServicelayerBaseTest
{

	@Resource
	private ModelService modelService;
	@Resource
	private TaskService taskService;
	@Resource
	private TypeService typeService;

	private CountDownLatch countDownLatch;
	private CountDownLatch deletionCountDownLatch;
	private boolean taskEngineWasRunningBefore;

	@Before
	public void setUp()
	{
		if (taskService.getEngine().isRunning())
		{
			taskEngineWasRunningBefore = true;
			taskService.getEngine().stop();
		}
	}

	@After
	public void tearDown()
	{
		if (taskEngineWasRunningBefore)
		{
			taskService.getEngine().start();
		}
	}

	@Test
	public void shouldExecuteOnlyTasksForOwnNodeGroup() throws Exception
	{
		final String group_a = "group_a";
		final String group_b = "group_b";

		final Long tA1 = createTask(group_a);
		final Long tA2 = createTask(group_a);
		final Long tA3 = createTask(group_a);

		final Long tB1 = createTask(group_b);
		final Long tB2 = createTask(group_b);
		final Long tB3 = createTask(group_b);
		final Long tB4 = createTask(group_b);

		final Collection<Long> relevantTasks = Lists.newArrayList(tA1, tA2, tA3, tB1, tB2, tB3, tB4);

		countDownLatch = new CountDownLatch(relevantTasks.size());
		deletionCountDownLatch = new CountDownLatch(relevantTasks.size());

		final TestTaskService serviceA = new TestTaskService(0, Lists.newArrayList(group_a), relevantTasks);
		final TestTaskService serviceB = new TestTaskService(1, Lists.newArrayList(group_b), relevantTasks);

		try
		{
			serviceB.getEngine().start();
			serviceA.getEngine().start();

			assertThat(countDownLatch.await(40, TimeUnit.SECONDS)).isTrue();

			assertThat(serviceA.getExecutedTasks()).containsOnly(tA1, tA2, tA3);
			assertThat(serviceB.getExecutedTasks()).containsOnly(tB1, tB2, tB3, tB4);

			assertThat(deletionCountDownLatch.await(40, TimeUnit.SECONDS)).isTrue();
		}
		finally
		{
			serviceB.getEngine().stop();
			serviceA.getEngine().stop();
		}
	}

	@Test
	public void shouldExecuteOnlyTasksForOwnNode() throws Exception
	{
		final String group_a = "group_a";
		final String group_b = "group_b";

		final Long tA1 = createTask(0);
		final Long tA2 = createTask(0);
		final Long tA3 = createTask(0);

		final Long tB1 = createTask(1);
		final Long tB2 = createTask(1);
		final Long tB3 = createTask(1);
		final Long tB4 = createTask(1);

		final Collection<Long> relevantTasks = Lists.newArrayList(tA1, tA2, tA3, tB1, tB2, tB3, tB4);

		countDownLatch = new CountDownLatch(relevantTasks.size());
		deletionCountDownLatch = new CountDownLatch(relevantTasks.size());

		final TestTaskService serviceA = new TestTaskService(0, Lists.newArrayList(group_a), relevantTasks);
		final TestTaskService serviceB = new TestTaskService(1, Lists.newArrayList(group_b), relevantTasks);

		try
		{
			serviceB.getEngine().start();
			serviceA.getEngine().start();

			assertThat(countDownLatch.await(40, TimeUnit.SECONDS)).isTrue();

			assertThat(serviceA.getExecutedTasks()).containsOnly(tA1, tA2, tA3);
			assertThat(serviceB.getExecutedTasks()).containsOnly(tB1, tB2, tB3, tB4);

			assertThat(deletionCountDownLatch.await(40, TimeUnit.SECONDS)).isTrue();
		}
		finally
		{
			serviceB.getEngine().stop();
			serviceA.getEngine().stop();
		}
	}

	@Test
	public void shouldExecuteTasksWithConflictingNodeIfAndNodeGroupAssignment() throws Exception
	{
		final String group_a = "group_a";
		final String group_b = "group_b";

		final Long tA1 = createTask(0, group_b);
		final Long tA2 = createTask(0, group_b);
		final Long tA3 = createTask(0, group_b);

		final Long tB1 = createTask(1, group_a);
		final Long tB2 = createTask(1, group_a);
		final Long tB3 = createTask(1, group_a);
		final Long tB4 = createTask(1, group_a);

		final Collection<Long> relevantTasks = Lists.newArrayList(tA1, tA2, tA3, tB1, tB2, tB3, tB4);

		countDownLatch = new CountDownLatch(relevantTasks.size());
		deletionCountDownLatch = new CountDownLatch(relevantTasks.size());

		final TestTaskService serviceA = new TestTaskService(0, Lists.newArrayList(group_a), relevantTasks);
		final TestTaskService serviceB = new TestTaskService(1, Lists.newArrayList(group_b), relevantTasks);

		try
		{
			serviceB.getEngine().start();
			serviceA.getEngine().start();

			assertThat(countDownLatch.await(40, TimeUnit.SECONDS)).isTrue();

			assertThat(serviceA.getExecutedTasks()).containsOnly(tA1, tA2, tA3);
			assertThat(serviceB.getExecutedTasks()).containsOnly(tB1, tB2, tB3, tB4);

			assertThat(deletionCountDownLatch.await(40, TimeUnit.SECONDS)).isTrue();
		}
		finally
		{
			serviceB.getEngine().stop();
			serviceA.getEngine().stop();
		}
	}


	@Test
	public void shouldExecuteTaskWithoutSpecifiedNodeGroupByAnyNode() throws Exception
	{
		final String group_a = "group_a";
		final String group_b = "group_b";

		final Long t1 = createTask(null);
		final Long t2 = createTask(null);
		final Long t3 = createTask(null);
		final Long t4 = createTask(null);

		final Collection<Long> relevantTasks = Lists.newArrayList(t1, t2, t3, t4);

		countDownLatch = new CountDownLatch(relevantTasks.size());
		deletionCountDownLatch = new CountDownLatch(relevantTasks.size());

		final TestTaskService serviceA = new TestTaskService(0, Lists.newArrayList(group_a), relevantTasks);
		final TestTaskService serviceB = new TestTaskService(1, Lists.newArrayList(group_b), relevantTasks);

		try
		{
			serviceB.getEngine().start();
			serviceA.getEngine().start();

			assertThat(countDownLatch.await(40, TimeUnit.SECONDS)).isTrue();

			final Collection<Long> allExecutedTasks = new ArrayList<>();

			allExecutedTasks.addAll(serviceA.getExecutedTasks());
			allExecutedTasks.addAll(serviceB.getExecutedTasks());

			assertThat(allExecutedTasks).hasSize(4);
			assertThat(allExecutedTasks).containsOnly(t1, t2, t3, t4);

			assertThat(deletionCountDownLatch.await(40, TimeUnit.SECONDS)).isTrue();
		}
		finally
		{
			serviceB.getEngine().stop();
			serviceA.getEngine().stop();
		}
	}


	private Long createTask(final String group)
	{
		final TaskModel task = modelService.create(TaskModel.class);
		task.setNodeGroup(group);
		task.setRunnerBean("runner");
		taskService.scheduleTask(task);
		return task.getPk().getLong();
	}

	private Long createTask(final int nodeId)
	{
		final TaskModel task = modelService.create(TaskModel.class);
		task.setNodeId(nodeId);
		task.setRunnerBean("runner");
		taskService.scheduleTask(task);
		return task.getPk().getLong();
	}

	private Long createTask(final int nodeId, final String group)
	{
		final TaskModel task = modelService.create(TaskModel.class);
		task.setNodeId(nodeId);
		task.setNodeGroup(group);
		task.setRunnerBean("runner");
		taskService.scheduleTask(task);
		return task.getPk().getLong();
	}

	class TestTaskService extends DefaultTaskService
	{

		private final int nodeId;
		private final Collection<String> nodeGroups;
		private final TestExecutionStrategy testExecutionStrategy;

		public TestTaskService(final int nodeId, final Collection<String> nodeGroups, final Collection<Long> relevantTasks)
		{
			this.nodeId = nodeId;
			this.nodeGroups = nodeGroups;
			this.testExecutionStrategy = new TestExecutionStrategy(relevantTasks);

			this.setModelService(modelService);
			this.setTypeService(typeService);
			this.setTaskExecutionStrategies(Lists.newArrayList(testExecutionStrategy));
			this.setTaskDao(new TaskDAO(getTenant())
			{
				@Override
				protected int initializeClusterId()
				{
					return nodeId;
				}
			});
		}

		@Override
		protected TaskExecutionStrategy getExecutionStrategy(final TaskRunner<? extends TaskModel> runner)
		{
			return testExecutionStrategy;
		}

		@Override
		Collection<String> getClusterGroupsForThisNode()
		{
			return nodeGroups;
		}

		@Override
		int getClusterNodeID()
		{
			return nodeId;
		}

		@Override
		void scheduleTaskForExecution(final VersionPK versionedPK)
		{
			super.scheduleTaskForExecution(versionedPK);
		}

		public Collection<Long> getExecutedTasks()
		{
			return testExecutionStrategy.getExecutedTasks();
		}

		@Override
		protected TaskRunner getRunner(final String runnerBean) throws IllegalStateException
		{
			return null;
		}
	}

	class TestExecutionStrategy implements TaskExecutionStrategy
	{

		private final Collection<Long> relevantTasks;
		private final Collection<Long> executedTasks = Collections.synchronizedList(new ArrayList<Long>());

		public TestExecutionStrategy(final Collection<Long> relevantTasks)
		{
			this.relevantTasks = relevantTasks;
		}

		@Override
		public void run(final TaskService taskService, final TaskRunner<TaskModel> runner, final TaskModel model)
				throws RetryLaterException
		{
			final Long pk = model.getPk().getLong();
			if (relevantTasks.contains(pk))
			{
				executedTasks.add(pk);
				countDownLatch.countDown();
			}
		}

		@Override
		public Throwable handleError(final TaskService taskService, final TaskRunner<TaskModel> runner, final TaskModel model,
				final Throwable error)
		{
			return null;
		}

		@Override
		public void finished(final TaskService taskService, final TaskRunner<TaskModel> runner, final TaskModel model,
				final Throwable error)
		{
			modelService.remove(model);
			deletionCountDownLatch.countDown();
		}

		@Override
		public Date handleRetry(final TaskService taskService, final TaskRunner<TaskModel> runner, final TaskModel model,
				final RetryLaterException retry, final int currentRetries)
		{
			return null;
		}

		@Override
		public Class<? extends TaskRunner<? extends TaskModel>> getRunnerClass()
		{
			return TestTaskRunner.class;
		}

		public Collection<Long> getExecutedTasks()
		{
			return executedTasks;
		}
	}

}
