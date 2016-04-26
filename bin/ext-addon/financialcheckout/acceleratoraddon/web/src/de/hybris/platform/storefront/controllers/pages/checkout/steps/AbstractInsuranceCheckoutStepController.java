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
package de.hybris.platform.storefront.controllers.pages.checkout.steps;

import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutStep;
import de.hybris.platform.financialacceleratorstorefront.checkout.step.InsuranceCheckoutStep;
import de.hybris.platform.financialacceleratorstorefront.strategies.StepTransitionStrategy;
import de.hybris.platform.storefront.checkout.steps.InsuranceCheckoutGroup;
import de.hybris.platform.storefront.checkout.steps.address.DeliveryAddressStrategy;
import de.hybris.platform.storefront.checkout.steps.strategy.DynamicFormCheckoutStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;


public abstract class AbstractInsuranceCheckoutStepController extends AbstractCheckoutStepController
{
	public static final String HIDE_CART_BILLING_EVENTS_OPTIONS = "storefront.hide.cart.billing.events";

	protected static final String REDIRECT_URL_FORM_METHOD = REDIRECT_PREFIX + "/checkout/multi/form";
	protected static final String REDIRECT_URL_QUOTE_REVIEW = REDIRECT_PREFIX + "/checkout/multi/quote/review";

	@Resource(name = "dynamicFormCheckoutStrategy")
	private DynamicFormCheckoutStrategy dynamicFormCheckoutStrategy;

	@Resource(name = "FORMS_CHECKOUT_STEP_PLACEHOLDER")
	private CheckoutStep formPlaceholder;

	@Resource(name = "stepTransitionStrategy")
	private StepTransitionStrategy stepTransitionStrategy;

	@Resource(name = "deliveryAddressStrategy")
	private DeliveryAddressStrategy deliveryAddressStrategy;

	/**
	 * Create the checkout progress bar to include dynamic form steps.
	 */
	@Override
	@ModelAttribute("checkoutSteps")
	public List<CheckoutSteps> addCheckoutStepsToModel()
	{
		setVisitedToStep(getCheckoutStep());
		return addDynamicSteps(false);
	}

	/**
	 * Setup default mock delivery address; reuse the existed delivery address from the returning customer if available.
	 */
	protected void setupDeliveryAddress()
	{
		if (!getCheckoutFacade().setDeliveryAddressIfAvailable())
		{
			deliveryAddressStrategy.setupDeliveryAddressToCheckout();
		}
	}

	@ModelAttribute("hideCartBillingEvents")
	public boolean isHideCartBillingEvents()
	{
		return getSiteConfigService().getBoolean(HIDE_CART_BILLING_EVENTS_OPTIONS, true);
	}

	protected abstract CheckoutStep getCheckoutStep();

	protected void setVisitedToStep(final CheckoutStep checkoutStep)
	{
		if (checkoutStep instanceof InsuranceCheckoutStep)
		{
			getStepTransitionStrategy().setVisited((InsuranceCheckoutStep) checkoutStep);
		}
	}

	/**
	 * Setup the checkout progress bar with flag to specify if to include dynamic steps
	 */
	protected List<CheckoutSteps> addDynamicSteps(final Boolean includeDynamicSteps)
	{
		final InsuranceCheckoutGroup checkoutGroup = (InsuranceCheckoutGroup) getCheckoutFlowGroupMap().get(
				getCheckoutFacade().getCheckoutFlowGroupForCheckout());
		final Map<String, InsuranceCheckoutStep> progressBarMap = checkoutGroup.getInsuranceCheckoutProgressBar();
		final List<CheckoutSteps> originalCheckoutSteps = new ArrayList<CheckoutSteps>(progressBarMap.size());

		for (final Map.Entry<String, InsuranceCheckoutStep> entry : progressBarMap.entrySet())
		{
			final InsuranceCheckoutStep checkoutStep = entry.getValue();
			originalCheckoutSteps.add(new InsuranceCheckoutSteps(checkoutStep.getProgressBarId(), StringUtils.remove(
					checkoutStep.currentStep(), "redirect:"), Integer.valueOf(entry.getKey()), checkoutStep.getCurrentStatus(),
					checkoutStep.getIsEnabled()));
		}

		final List<CheckoutSteps> combinedCheckoutSteps = new ArrayList<CheckoutSteps>();

		if (includeDynamicSteps)
		{
			combinedCheckoutSteps.addAll(getDynamicFormCheckoutStrategy().combineFormCheckoutStepProgressBar(getFormPlaceholder(),
					originalCheckoutSteps));
			return combinedCheckoutSteps;
		}
		return originalCheckoutSteps;
	}

	public static class InsuranceCheckoutSteps extends CheckoutSteps
	{
		private final String status;
		private final Boolean isEnabled;

		public InsuranceCheckoutSteps(final String progressBarId, final String url, final Integer stepNumber)
		{
			super(progressBarId, url, stepNumber);
			this.status = "";
			this.isEnabled = Boolean.FALSE;
		}

		public InsuranceCheckoutSteps(final String progressBarId, final String url, final Integer stepNumber, final String status,
				final Boolean isEnabled)
		{
			super(progressBarId, url, stepNumber);
			this.status = status;
			this.isEnabled = isEnabled;
		}

		public String getStatus()
		{
			return status;
		}

		public Boolean getIsEnabled()
		{
			return isEnabled;
		}
	}

	protected CheckoutStep getFormPlaceholder()
	{
		return formPlaceholder;
	}

	public void setFormPlaceholder(final CheckoutStep formPlaceholder)
	{
		this.formPlaceholder = formPlaceholder;
	}

	protected DynamicFormCheckoutStrategy getDynamicFormCheckoutStrategy()
	{
		return dynamicFormCheckoutStrategy;
	}

	public void setDynamicFormCheckoutStrategy(final DynamicFormCheckoutStrategy dynamicFormCheckoutStrategy)
	{
		this.dynamicFormCheckoutStrategy = dynamicFormCheckoutStrategy;
	}

	protected StepTransitionStrategy getStepTransitionStrategy()
	{
		return stepTransitionStrategy;
	}

	public void setStepTransitionStrategy(final StepTransitionStrategy stepTransitionStrategy)
	{
		this.stepTransitionStrategy = stepTransitionStrategy;
	}
}
