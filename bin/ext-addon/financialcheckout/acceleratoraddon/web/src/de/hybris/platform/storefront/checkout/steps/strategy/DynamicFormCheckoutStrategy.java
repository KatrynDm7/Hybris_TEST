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
package de.hybris.platform.storefront.checkout.steps.strategy;

import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutStep;
import de.hybris.platform.financialacceleratorstorefront.checkout.step.InsuranceCheckoutStep;
import de.hybris.platform.storefront.controllers.pages.checkout.steps.AbstractCheckoutStepController;

import java.util.List;


public interface DynamicFormCheckoutStrategy
{
	/**
	 * Create dynamic form progress checkout steps based on the cart entries. <br/>
	 * Depends on the content of the cart, dynamically create the form steps and defined the navigation between the
	 * forms.
	 * 
	 * @param formCheckoutStepPlaceholder
	 */
	List<InsuranceCheckoutStep> createDynamicFormSteps(final CheckoutStep formCheckoutStepPlaceholder);


	/**
	 * Combine the Form Checkout Steps to the original Checkout steps for display the at the progress bar.
	 * 
	 * @param formCheckoutStepPlaceholder
	 * @param originalCheckoutSteps
	 */
	List<AbstractCheckoutStepController.CheckoutSteps> combineFormCheckoutStepProgressBar(
			CheckoutStep formCheckoutStepPlaceholder, List<AbstractCheckoutStepController.CheckoutSteps> originalCheckoutSteps);

	/**
	 * Get all the Form HTMLs to display on the given formPageId
	 * 
	 * @param formPageId
	 */
	List<String> getFormsInlineHtmlByFormPageId(final String formPageId);
}
