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
package de.hybris.platform.financialacceleratorstorefront.checkout.step;

import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutStep;
import de.hybris.platform.financialfacades.strategies.StepEnabledAnalysisStrategy;
import de.hybris.platform.financialfacades.strategies.StepValidationStrategy;
import de.hybris.platform.financialfacades.strategies.StepVisitedAnalysisStrategy;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public abstract class AbstractInsuranceStepTransition extends CheckoutStep implements InsuranceStepTransition
{
	private List<StepValidationStrategy> checkoutStepValidatorList;
	private List<StepVisitedAnalysisStrategy> stepVisitedAnalysisStrategyList;
	private List<StepEnabledAnalysisStrategy> stepEnabledAnalysisStrategyList;

	@Override
	public void setVisited(final boolean isVisited)
	{
		if (isVisited == Boolean.TRUE)
		{
			for (final StepVisitedAnalysisStrategy strategy : getStepVisitedAnalysisStrategyList())
			{
				strategy.setVisited(isVisited, getProgressBarId());
			}
		}
	}

	@Override
	public boolean getIsEnabled()
	{
		boolean isEnabled = Boolean.TRUE;
		for (final StepEnabledAnalysisStrategy strategy : getStepEnabledAnalysisStrategyList())
		{
			if (strategy.isEnabled() == Boolean.FALSE)
			{
				isEnabled = Boolean.FALSE;
				break;
			}
		}
		return isEnabled;
	}

	@Override
	public boolean isVisited()
	{
		boolean isVisited = Boolean.FALSE;
		for (final StepVisitedAnalysisStrategy strategy : getStepVisitedAnalysisStrategyList())
		{
			if (strategy.isVisited(getProgressBarId()) == Boolean.TRUE && isSameCategory(strategy))
			{
				isVisited = Boolean.TRUE;
				break;
			}
		}
		return isVisited;
	}

	@Override
	public boolean isValid()
	{
		boolean isValid = Boolean.TRUE;
		for (final StepValidationStrategy validationStrategy : getCheckoutStepValidatorList())
		{
			if (validationStrategy.isValid() == Boolean.FALSE)
			{
				isValid = Boolean.FALSE;
				break;
			}
		}
		return isValid;
	}

	@Override
	public String getCurrentStatus()
	{
		if (isVisited())
		{
			if (isValid())
			{
				return VALIDATED;
			}
			else
			{
				return INVALID;
			}
		}
		else
		{
			return UNSET;
		}
	}

	abstract boolean isSameCategory(final StepVisitedAnalysisStrategy strategy);

	public List<StepValidationStrategy> getCheckoutStepValidatorList()
	{
		return checkoutStepValidatorList;
	}

	@Required
	public void setCheckoutStepValidatorList(final List<StepValidationStrategy> checkoutStepValidatorList)
	{
		this.checkoutStepValidatorList = checkoutStepValidatorList;
	}

	public List<StepVisitedAnalysisStrategy> getStepVisitedAnalysisStrategyList()
	{
		return stepVisitedAnalysisStrategyList;
	}

	@Required
	public void setStepVisitedAnalysisStrategyList(final List<StepVisitedAnalysisStrategy> stepVisitedAnalysisStrategyList)
	{
		this.stepVisitedAnalysisStrategyList = stepVisitedAnalysisStrategyList;
	}

	public List<StepEnabledAnalysisStrategy> getStepEnabledAnalysisStrategyList()
	{
		return stepEnabledAnalysisStrategyList;
	}

	@Required
	public void setStepEnabledAnalysisStrategyList(final List<StepEnabledAnalysisStrategy> stepEnabledAnalysisStrategyList)
	{
		this.stepEnabledAnalysisStrategyList = stepEnabledAnalysisStrategyList;
	}
}
