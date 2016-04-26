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
package de.hybris.platform.financialacceleratorstorefront.tags;

import de.hybris.platform.acceleratorservices.util.SpringHelper;
import de.hybris.platform.financialacceleratorstorefront.checkout.step.InsuranceCheckoutStep;
import de.hybris.platform.financialacceleratorstorefront.strategies.StepTransitionStrategy;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.hsqldb.lib.StringUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.tags.HtmlEscapingAwareTag;


public class InsuranceProgressStepsBarTag extends HtmlEscapingAwareTag
{
	private int scope = PageContext.REQUEST_SCOPE;
	private String progressStepMapKey;
	private String stepTransitionStrategyKey;
	private String currentUrl;
	private String var;
	private Map<String, InsuranceCheckoutStep> progressStepMap;
	protected final static String CATEGORY_MAPPING = "/c/";

	@Override
	protected int doStartTagInternal() throws Exception
	{
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException
	{
		if (this.var != null)
		{
			final Map<String, InsuranceCheckoutStep> steps = lookupProgressStepMap();
			processProgressVisited(steps);
			pageContext.setAttribute(getVar(), steps, getScope());
		}
		return EVAL_PAGE;
	}

	protected void processProgressVisited(final Map<String, InsuranceCheckoutStep> steps)
	{
		final StepTransitionStrategy strategy = lookupStepTransitionStrategy();
		for (final InsuranceCheckoutStep checkoutStep : steps.values())
		{
			setCategoryUrl(checkoutStep);
			strategy.setVisited(checkoutStep, getCurrentUrl());
		}
	}

	protected void setCategoryUrl(final InsuranceCheckoutStep checkoutStep)
	{
		final boolean isCategoryMapping = getCurrentUrl().indexOf(CATEGORY_MAPPING) > 0;

		if (StringUtils.isEmpty(checkoutStep.getCategoryUrl())
				|| (isCategoryMapping && !getCurrentUrl().equals(checkoutStep.getCategoryUrl())))
		{
			checkoutStep.setCategoryUrl(getCurrentUrl());
		}

		if (isCategoryMapping)
		{
			checkoutStep.setCategoryUrl(true);
		}
		else
		{
			checkoutStep.setCategoryUrl(false);
		}

	}

	protected Map<String, InsuranceCheckoutStep> lookupProgressStepMap()
	{
		return SpringHelper.getSpringBean(pageContext.getRequest(), getProgressStepMapKey(), Map.class, true);
	}

	protected StepTransitionStrategy lookupStepTransitionStrategy()
	{
		String key = "stepTransitionStrategy";
		if (!StringUtil.isEmpty(getStepTransitionStrategyKey()))
		{
			key = getStepTransitionStrategyKey();
		}
		return SpringHelper.getSpringBean(pageContext.getRequest(), key, StepTransitionStrategy.class, true);
	}

	protected int getScope()
	{
		return scope;
	}

	public void setScope(final int scope)
	{
		this.scope = scope;
	}

	protected Map<String, InsuranceCheckoutStep> getProgressStepMap()
	{
		return progressStepMap;
	}

	public void setProgressStepMap(final Map<String, InsuranceCheckoutStep> progressStepMap)
	{
		this.progressStepMap = progressStepMap;
	}

	protected String getProgressStepMapKey()
	{
		return progressStepMapKey;
	}

	public void setProgressStepMapKey(final String progressStepMapKey)
	{
		this.progressStepMapKey = progressStepMapKey;
	}

	protected String getVar()
	{
		return var;
	}

	public void setVar(final String var)
	{
		this.var = var;
	}

	protected String getStepTransitionStrategyKey()
	{
		return stepTransitionStrategyKey;
	}

	public void setStepTransitionStrategyKey(final String stepTransitionStrategyKey)
	{
		this.stepTransitionStrategyKey = stepTransitionStrategyKey;
	}

	public String getCurrentUrl()
	{
		return currentUrl;
	}

	public void setCurrentUrl(final String currentUrl)
	{
		this.currentUrl = currentUrl;
	}
}
