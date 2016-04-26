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

import de.hybris.platform.commerceservices.externaltax.CalculateExternalTaxesStrategy;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.externaltax.ExternalTaxDocument;


/**
 * Base {@link CalculateExternalTaxesStrategy} implementation of external tax call to return an empty
 * ExternalTaxDocument
 * 
 */
public class DefaultCalculateExternalTaxesStrategy implements CalculateExternalTaxesStrategy
{
	/**
	 * Default implementation to return an empty tax document.
	 */
	@Override
	public ExternalTaxDocument calculateExternalTaxes(final AbstractOrderModel abstractOrder)
	{
		final ExternalTaxDocument externalDocument = new ExternalTaxDocument();

		if (abstractOrder == null)
		{
			throw new IllegalStateException("Order is null. Cannot apply external tax to it.");
		}

		return externalDocument;
	}

}
