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
package de.hybris.platform.financialfacades.strategies;

public interface StepVisitedAnalysisStrategy
{
	/**
	 * Check if step is visited strategy.
	 */
	boolean isVisited(final String progressStepId);

	/**
	 * Set step isVisited.
	 */
	public void setVisited(final boolean isVisited, final String progressStepId);
}
