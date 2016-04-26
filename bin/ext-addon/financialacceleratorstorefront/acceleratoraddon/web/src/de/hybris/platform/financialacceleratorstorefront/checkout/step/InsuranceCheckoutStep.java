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

import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.financialfacades.facades.InsuranceCartFacade;
import de.hybris.platform.financialfacades.strategies.StepVisitedAnalysisStrategy;
import de.hybris.platform.financialfacades.strategies.impl.InitialStepVisitedStrategy;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


public class InsuranceCheckoutStep extends AbstractInsuranceStepTransition
{
	public static final String ACTIVE_STEP = "activeStep";
	public static final String ALTERNATIVE_ACTIVE_STEP = "alternativeActiveStep";

	private String checkoutFlow;

	private String categoryUrl;

	private boolean isCategoryUrl = false;

	private InsuranceCartFacade insuranceCartFacade;

	public String getActiveStep()
	{
		return getTransitions().get(ACTIVE_STEP);
	}

	public String getAlternativeActiveStep()
	{
		return getTransitions().get(ALTERNATIVE_ACTIVE_STEP);
	}

	public String getCheckoutFlow()
	{
		return checkoutFlow;
	}

	public void setCheckoutFlow(final String checkoutFlow)
	{
		this.checkoutFlow = checkoutFlow;
	}

	public void setCategoryUrl(final String categoryUrl)
	{
		this.categoryUrl = categoryUrl;
	}

	public String getCategoryUrl()
	{
		return categoryUrl;
	}

	/**
	 * @return the isCategoryUrl
	 */
	public boolean isCategoryUrl()
	{
		return isCategoryUrl;
	}

	/**
	 * @param isCategoryUrl
	 *           the isCategoryUrl to set
	 */
	public void setCategoryUrl(final boolean isCategoryUrl)
	{
		this.isCategoryUrl = isCategoryUrl;
	}

	/**
	 * Helper method used to signal the caller to find whether the current requested progress bar step belongs to the
	 * quote from the session cart. This has been done by comparing the requested url (category type) matches the
	 * Category that have been stored in the cart. If the categoryUrl == null, which means the requested action is from
	 * checkout/cart.
	 * 
	 * @param strategy
	 *           StepVisitedAnalysisStrategy that belongs to the current requested progress bar step
	 * @return boolean true if the requested url contains same Category as in the cart
	 */
	@Override
	public boolean isSameCategory(final StepVisitedAnalysisStrategy strategy)
	{
		boolean isSameCategory = false;

		final CategoryData categoryData = insuranceCartFacade.getSelectedInsuranceCategory();

		if ((categoryData == null || StringUtils.isBlank(categoryUrl) || !isCategoryUrl || strategy instanceof InitialStepVisitedStrategy)
				|| (StringUtils.isNotBlank(categoryUrl) && categoryUrl.indexOf(categoryData.getCode()) > 0))
		{
			isSameCategory = true;
		}

		return isSameCategory;
	}

	public InsuranceCartFacade getInsuranceCartFacade()
	{
		return insuranceCartFacade;
	}

	@Required
	public void setInsuranceCartFacade(final InsuranceCartFacade insuranceCartFacade)
	{
		this.insuranceCartFacade = insuranceCartFacade;
	}

}
