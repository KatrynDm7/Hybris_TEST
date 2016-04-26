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


/**
 * Abstraction for service to calculate 3rd party taxes
 * 
 */
public interface ExternalTaxesService
{
	/**
	 * Calculate the taxes for order via an external service
	 * @param abstractOrder A Hybris cart or order
	 * @return True if calculation was successful and false otherwise
	 */
	boolean calculateExternalTaxes(final AbstractOrderModel abstractOrder);

	void clearSessionTaxDocument();
}
