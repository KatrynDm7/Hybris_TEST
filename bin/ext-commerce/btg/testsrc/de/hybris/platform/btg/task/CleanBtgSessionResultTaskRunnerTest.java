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
package de.hybris.platform.btg.task;

import static org.junit.Assert.assertEquals;

import de.hybris.platform.btg.condition.impl.PriceExpressionEvaluator;
import de.hybris.platform.btg.enums.BTGConditionEvaluationScope;
import de.hybris.platform.btg.enums.BTGEvaluationMethod;
import de.hybris.platform.btg.enums.BTGResultScope;
import de.hybris.platform.btg.integration.BTGIntegrationTest;
import de.hybris.platform.btg.model.BTGAbstractResultModel;
import de.hybris.platform.btg.model.BTGOrderTotalSumOperandModel;
import de.hybris.platform.btg.model.BTGRuleModel;
import de.hybris.platform.btg.model.BTGSegmentModel;
import de.hybris.platform.btg.segment.SegmentEvaluationException;
import de.hybris.platform.btg.services.impl.BTGEvaluationContext;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;


public class CleanBtgSessionResultTaskRunnerTest extends BTGIntegrationTest
{
	protected static final String SELECT_RESULT_FOR_SESSION = "SELECT {" + BTGAbstractResultModel.PK + "} FROM {"
			+ BTGAbstractResultModel._TYPECODE + "} WHERE {sessionId} = ?sessionId";

	@Resource
	private CleanBtgSessionResultTaskRunner cleanBtgSessionResultTaskRunner;
	@Resource
	private ModelService modelService;
	@Resource
	private SessionService sessionService;
	@Resource
	private TaskService taskService;
	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Test
	public void testCleanResultForUser() throws Exception
	{
		//given
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		createResults(BTGResultScope.SESSION, customerA, sessionId);

		//when
		final TaskModel task = modelService.create(TaskModel.class);
		task.setContext(sessionId);
		cleanBtgSessionResultTaskRunner.run(taskService, task);

		//then
		assertThereIsNoResult(sessionId);
	}

	@Test
	public void testResultsForPermamentScopeAreNotRemoved() throws Exception
	{
		//given
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		createResults(BTGResultScope.PERMANENT, customerA, sessionId);

		//when
		final TaskModel task = modelService.create(TaskModel.class);
		task.setContext(sessionId);
		cleanBtgSessionResultTaskRunner.run(taskService, task);

		//then
		//Permament scope result should not be removed
		assertSegmentResultExists(sessionId);
	}

	@Test
	public void testCleanResultForAnonymousUser() throws Exception
	{
		//given
		final String sessionId = sessionService.getCurrentSession().getSessionId();
		createResults(BTGResultScope.PERMANENT, anonymous, sessionId);

		//when
		final TaskModel task = modelService.create(TaskModel.class);
		task.setContext(sessionId);
		cleanBtgSessionResultTaskRunner.run(taskService, task);

		//then
		assertThereIsNoResult(sessionId);
	}

	@Test
	public void testThatResulAreRemovedOnlyForSelectedSession() throws Exception
	{
		//given
		final String firstSessionId = sessionService.getCurrentSession().getSessionId();
		createAndSaveSegment(BTGResultScope.SESSION);
		final BTGEvaluationContext context = new BTGEvaluationContext(BTGConditionEvaluationScope.ONLINE,
				BTGEvaluationMethod.OPTIMIZED, BTGResultScope.SESSION);
		evaluateSegmentsForUser(context, anonymous);
		assertSegmentResultExists(firstSessionId);

		sessionService.createNewSession();

		final String secondSessionId = sessionService.getCurrentSession().getSessionId();
		evaluateSegmentsForUser(context, anonymous);
		assertSegmentResultExists(secondSessionId);

		//when
		final TaskModel task = modelService.create(TaskModel.class);
		task.setContext(firstSessionId);
		cleanBtgSessionResultTaskRunner.run(taskService, task);

		//then
		assertThereIsNoResult(firstSessionId);
		assertSegmentResultExists(secondSessionId);
	}

	protected void createResults(final BTGResultScope scope, final UserModel user, final String sessionId)
			throws SegmentEvaluationException, Exception
	{
		createAndSaveSegment(scope);
		final BTGEvaluationContext context = new BTGEvaluationContext(BTGConditionEvaluationScope.ONLINE,
				BTGEvaluationMethod.OPTIMIZED, scope);

		evaluateSegmentsForUser(context, user);
		assertSegmentResultExists(sessionId);
	}

	protected BTGSegmentModel createAndSaveSegment(final BTGResultScope resultScope)
	{
		final BTGSegmentModel segment = createSegment();
		segment.setDefaultResultScope(resultScope);
		modelService.save(segment);
		final BTGRuleModel rule = createRule("testRule");
		segment.setRules(Collections.singletonList(rule));

		rule.setConditions(Collections.singletonList(createExpression(
				createOrderOperand(BTGOrderTotalSumOperandModel.class, 2, ""), createOperator(PriceExpressionEvaluator.EQUALS),
				createPriceReferenceOperand(5, EUR))));
		modelService.save(segment);
		return segment;
	}

	protected void evaluateSegmentsForUser(final BTGEvaluationContext context, final UserModel user) throws Exception,
			SegmentEvaluationException
	{
		userService.setCurrentUser(user);
		btgEvaluationService.evaluateAllSegments(user, Collections.EMPTY_LIST, context);
	}

	protected void assertSegmentResultExists(final String sessionId)
	{
		final List<BTGAbstractResultModel> resultList = getBtgResultForSession(sessionId);
		assertEquals(3, resultList.size());
	}

	private void assertThereIsNoResult(final String sessionId)
	{
		final List<BTGAbstractResultModel> resultList = getBtgResultForSession(sessionId);
		assertEquals(0, resultList.size());
	}

	protected List<BTGAbstractResultModel> getBtgResultForSession(final String sessionId)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(SELECT_RESULT_FOR_SESSION);
		fQuery.addQueryParameter("sessionId", sessionId);

		fQuery.setResultClassList(Collections.singletonList(BTGAbstractResultModel.class));
		final SearchResult<BTGAbstractResultModel> searchResult = flexibleSearchService.search(fQuery);
		return searchResult.getResult();
	}
}
