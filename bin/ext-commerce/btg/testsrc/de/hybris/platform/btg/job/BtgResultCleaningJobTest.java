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
package de.hybris.platform.btg.job;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.btg.condition.impl.PriceExpressionEvaluator;
import de.hybris.platform.btg.enums.BTGConditionEvaluationScope;
import de.hybris.platform.btg.enums.BTGEvaluationMethod;
import de.hybris.platform.btg.enums.BTGResultScope;
import de.hybris.platform.btg.integration.BTGIntegrationTest;
import de.hybris.platform.btg.model.BTGAbstractResultModel;
import de.hybris.platform.btg.model.BTGOrderTotalSumOperandModel;
import de.hybris.platform.btg.model.BTGResultCleaningCronJobModel;
import de.hybris.platform.btg.model.BTGRuleModel;
import de.hybris.platform.btg.model.BTGSegmentModel;
import de.hybris.platform.btg.segment.SegmentEvaluationException;
import de.hybris.platform.btg.services.impl.BTGEvaluationContext;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;


/**
 * Test class for testing the functionality of the BtgResultCleaningJob.
 *
 */
@IntegrationTest
public class BtgResultCleaningJobTest extends BTGIntegrationTest
{
	private static final Logger LOG = Logger.getLogger(BtgResultCleaningJobTest.class);//NOPMD
	protected static final String SELECT_BTG_RESULT = "SELECT {" + BTGAbstractResultModel.PK + "} FROM {"
			+ BTGAbstractResultModel._TYPECODE + "}";


	@Resource
	private BtgResultCleaningJob btgResultCleaningJob;

	@Resource
	private ModelService modelService;

	@Resource
	private SessionService sessionService;


	@Test
	public void testNoResultToRemove()
	{
		final PerformResult result = btgResultCleaningJob.perform((BTGResultCleaningCronJobModel)modelService.create(BTGResultCleaningCronJobModel.class));

		Assert.assertEquals(CronJobResult.SUCCESS, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());
	}

	@Test
	public void testMaxResultAgeShouldBeGreaterThanZero()
	{
		final PerformResult result = btgResultCleaningJob.perform(createCronJobModel(-10, BTGResultScope.SESSION));

		Assert.assertEquals(CronJobResult.ERROR, result.getResult());
		Assert.assertEquals(CronJobStatus.ABORTED, result.getStatus());
	}


	@Test
	public void testCleanResult() throws Exception
	{
		//given
		createResults(BTGResultScope.SESSION, customerA);
		Thread.sleep(2000);// wait for resultMaxAge to be achieved

		//when
		final PerformResult result = btgResultCleaningJob.perform(createCronJobModel(1, BTGResultScope.SESSION));

		//then
		Assert.assertEquals(CronJobResult.SUCCESS, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());
		assertThereIsNoResult();
	}

	@Test
	public void testCleanOnlyOldResults() throws Exception
	{
		//given
		createResults(BTGResultScope.SESSION, customerA);

		//when
		final PerformResult result = btgResultCleaningJob.perform(createCronJobModel(200, BTGResultScope.SESSION));

		//then
		Assert.assertEquals(CronJobResult.SUCCESS, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());
		assertSegmentResultExists(3, BTGResultScope.SESSION);
	}

	@Test
	public void testCleanOnlySelectedScopeResults() throws Exception
	{
		//given
		createResultsForBothScopes();
		Thread.sleep(2000); // wait for resultMaxAge to be achieved

		//when
		final PerformResult result = btgResultCleaningJob.perform(createCronJobModel(1, BTGResultScope.PERMANENT));

		//then
		Assert.assertEquals(CronJobResult.SUCCESS, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());
		assertSegmentResultExists(3, BTGResultScope.SESSION);
	}

	protected BTGResultCleaningCronJobModel createCronJobModel(final int resultMaxAge, final BTGResultScope resultScope)
	{
		final BTGResultCleaningCronJobModel cronJobModel = modelService.create(BTGResultCleaningCronJobModel.class);
		cronJobModel.setResultMaxAge(resultMaxAge);
		cronJobModel.setResultScope(resultScope);
		return cronJobModel;
	}

	protected void createResults(final BTGResultScope scope, final UserModel user) throws SegmentEvaluationException, Exception
	{
		createAndSaveSegment(scope);
		final BTGEvaluationContext context = new BTGEvaluationContext(BTGConditionEvaluationScope.ONLINE,
				BTGEvaluationMethod.OPTIMIZED, scope);

		evaluateSegmentsForUser(context, user);
		assertSegmentResultExists(3, scope);
	}

	protected void createResultsForBothScopes() throws SegmentEvaluationException, Exception
	{
		createAndSaveSegment(BTGResultScope.PERMANENT);
		final BTGEvaluationContext sessionContext = new BTGEvaluationContext(BTGConditionEvaluationScope.ONLINE,
				BTGEvaluationMethod.OPTIMIZED, BTGResultScope.SESSION);
		final BTGEvaluationContext permanentContext = new BTGEvaluationContext(BTGConditionEvaluationScope.ONLINE,
				BTGEvaluationMethod.OPTIMIZED, BTGResultScope.PERMANENT);

		evaluateSegmentsForUser(permanentContext, customerA);
		sessionService.createNewSession();
		evaluateSegmentsForUser(sessionContext, anonymous);

		final List<BTGAbstractResultModel> resultList = getBtgResults();
		assertEquals(6, resultList.size());
		int sessionCount = 0, permanentCount = 0;
		for (final BTGAbstractResultModel result : resultList)
		{
			if (BTGResultScope.PERMANENT.equals(result.getResultScope()))
			{
				permanentCount++;
			}
			else if (BTGResultScope.SESSION.equals(result.getResultScope()))
			{
				sessionCount++;
			}
		}
		assertEquals(3, permanentCount++);
		assertEquals(3, sessionCount++);

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

	protected void assertSegmentResultExists(final int expectedSize, final BTGResultScope scope)
	{
		final List<BTGAbstractResultModel> resultList = getBtgResults();
		assertEquals(expectedSize, resultList.size());
		for (final BTGAbstractResultModel result : resultList)
		{
			assertEquals(scope, result.getResultScope());
		}
	}

	private void assertThereIsNoResult()
	{
		final List<BTGAbstractResultModel> resultList = getBtgResults();
		assertEquals(0, resultList.size());
	}

	protected List<BTGAbstractResultModel> getBtgResults()
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(SELECT_BTG_RESULT);

		fQuery.setResultClassList(Collections.singletonList(BTGAbstractResultModel.class));
		final SearchResult<BTGAbstractResultModel> searchResult = flexibleSearchService.search(fQuery);
		return searchResult.getResult();
	}
}
