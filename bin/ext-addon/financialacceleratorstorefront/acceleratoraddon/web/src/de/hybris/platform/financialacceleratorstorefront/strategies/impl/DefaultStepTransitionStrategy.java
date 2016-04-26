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
package de.hybris.platform.financialacceleratorstorefront.strategies.impl;

import de.hybris.platform.financialacceleratorstorefront.checkout.step.InsuranceCheckoutStep;
import de.hybris.platform.financialacceleratorstorefront.strategies.StepTransitionStrategy;
import de.hybris.platform.financialfacades.facades.InsuranceCartFacade;

import net.sourceforge.pmd.util.StringUtil;


public class DefaultStepTransitionStrategy implements StepTransitionStrategy
{
	private InsuranceCartFacade insuranceCartFacade;

	@Override
	public void setVisited(final InsuranceCheckoutStep checkoutStep, final String currentURL)
	{
		final String activeUrl = checkoutStep.getActiveStep();
		final String alternativeUrl = checkoutStep.getAlternativeActiveStep();
		if ((StringUtil.isNotEmpty(activeUrl) && currentURL.contains(activeUrl))
				|| (StringUtil.isNotEmpty(alternativeUrl) && currentURL.contains(alternativeUrl)))
		{
			setVisited(checkoutStep);
		}
	}

	@Override
	public void setVisited(final InsuranceCheckoutStep checkoutStep)
	{
		checkoutStep.setVisited(true);
	}

}
