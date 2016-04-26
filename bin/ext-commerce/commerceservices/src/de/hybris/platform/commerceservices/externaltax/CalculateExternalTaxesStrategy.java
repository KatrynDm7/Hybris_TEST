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
package de.hybris.platform.commerceservices.externaltax;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.externaltax.ExternalTaxDocument;


/**
 * Abstraction for strategy to make the call to a 3rd party tax calculation service.
 */
public interface CalculateExternalTaxesStrategy
{
	/**
	 * Calculate external taxes for the order.
	 *
	 * @param abstractOrder order to calculcate the taxes for
	 * @return a tax document holding the calculated tax values
	 */
	ExternalTaxDocument calculateExternalTaxes(AbstractOrderModel abstractOrder);

}
