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
package de.hybris.platform.storefront.checkout.steps;

import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutGroup;
import de.hybris.platform.financialacceleratorstorefront.checkout.step.InsuranceCheckoutStep;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;


public class InsuranceCheckoutGroup extends CheckoutGroup
{
	private Map<String, InsuranceCheckoutStep> insuranceCheckoutProgressBar;

	public Map<String, InsuranceCheckoutStep> getInsuranceCheckoutProgressBar()
	{
		return insuranceCheckoutProgressBar;
	}

	@Required
	public void setInsuranceCheckoutProgressBar(Map<String, InsuranceCheckoutStep> insuranceCheckoutProgressBar)
	{
		this.insuranceCheckoutProgressBar = insuranceCheckoutProgressBar;
	}
}
