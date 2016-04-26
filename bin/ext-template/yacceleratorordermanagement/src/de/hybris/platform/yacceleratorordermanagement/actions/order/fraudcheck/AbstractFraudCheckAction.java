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
package de.hybris.platform.yacceleratorordermanagement.actions.order.fraudcheck;

import de.hybris.platform.basecommerce.enums.FraudStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.fraud.impl.FraudServiceResponse;
import de.hybris.platform.fraud.impl.FraudSymptom;
import de.hybris.platform.fraud.model.FraudReportModel;
import de.hybris.platform.fraud.model.FraudSymptomScoringModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.yacceleratorordermanagement.actions.order.AbstractOrderAction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Abstract action for fraud check action which define 3 possible transitions (OK,POTENTIAL,FRAUD) and some
 * supplementary methods
 */
public abstract class AbstractFraudCheckAction<T extends OrderProcessModel> extends AbstractOrderAction<T>
{
	protected enum Transition
	{
		OK, POTENTIAL, FRAUD;

		public static Set<String> getStringValues()
		{
			final Set<String> res = new HashSet<String>();
			for (final Transition transitions : Transition.values())
			{
				res.add(transitions.toString());
			}
			return res;
		}
	}

	/**
	 * Create a fraud report
	 *
	 * @param providerName
	 *           - the provider name
	 * @param response
	 *           - the fraud service response
	 * @param order
	 *           - the order model
	 * @param status
	 *           - the fraud status
	 * @return a fraud report model
	 */
	protected FraudReportModel createFraudReport(final String providerName, final FraudServiceResponse response,
			final OrderModel order, final FraudStatus status)
	{
		final FraudReportModel fraudReport = modelService.create(FraudReportModel.class);
		fraudReport.setOrder(order);
		fraudReport.setStatus(status);
		fraudReport.setProvider(providerName);
		fraudReport.setTimestamp(timeService.getCurrentTime());
		int reportNumber = 0;
		if (order.getFraudReports() != null && !order.getFraudReports().isEmpty())
		{
			reportNumber = order.getFraudReports().size();
		}
		fraudReport.setCode(order.getCode() + "_FR" + reportNumber);
		List<FraudSymptomScoringModel> symptoms = null;
		for (final FraudSymptom symptom : response.getSymptoms())
		{
			if (symptoms == null)
			{
				symptoms = new ArrayList<FraudSymptomScoringModel>();
			}
			final FraudSymptomScoringModel symptomScoring = modelService.create(FraudSymptomScoringModel.class);
			symptomScoring.setFraudReport(fraudReport);
			symptomScoring.setName(symptom.getSymptom());
			symptomScoring.setExplanation(symptom.getExplanation());
			symptomScoring.setScore(symptom.getScore());
			symptoms.add(symptomScoring);
		}
		fraudReport.setFraudSymptomScorings(symptoms);
		return fraudReport;
	}

	protected OrderHistoryEntryModel createHistoryLog(final String providerName, final OrderModel order, final FraudStatus status,
			final String code)
	{
		final String description;
		if (status.equals(FraudStatus.OK))
		{
			description = "Fraud check [" + providerName + "]: OK";
		}
		else
		{
			description = "Fraud check [" + providerName + "]: " + status.toString() + ". Check the fraud report :" + code;
		}
		return createHistoryLog(description, order);
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}

	@Override
	public final String execute(final T process)
	{
		return executeAction(process).toString();
	}

	/**
	 * Executes this <code>Action</code>'s business logic working on the given
	 * {@link de.hybris.platform.processengine.model.BusinessProcessModel}.
	 *
	 * @param process
	 *           - the process context to work on.
	 * @return OK; FRAUD; POTENTIAL
	 * @throws RetryLaterException
	 * @throws Exception
	 */
	public abstract Transition executeAction(T process);
}
