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
package de.hybris.platform.commerceservices.externaltax.impl;

import de.hybris.platform.commerceservices.externaltax.DecideExternalTaxesStrategy;
import de.hybris.platform.core.model.order.AbstractOrderModel;



/**
 * Base {@link DecideExternalTaxesStrategy} implementation, gives decision to call external taxes functionality.
 */
public class DefaultDecideExternalTaxesStrategy implements DecideExternalTaxesStrategy
{

	/**
	 * Return false to be overridden in specific application
	 */
	@Override
	public boolean shouldCalculateExternalTaxes(final AbstractOrderModel abstractOrder)
	{
		if (abstractOrder == null)
		{
			throw new IllegalStateException("Order is null. Cannot apply external tax to it.");
		}

		return false;
	}
}
