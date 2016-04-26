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
package de.hybris.platform.commerceservices.order;


import de.hybris.platform.commerceservices.service.data.CommerceOrderParameter;
import de.hybris.platform.core.model.order.AbstractOrderModel;

import java.util.List;


/**
 * Strategy tp calculate a hash value based on the abstractOrderModel state plus the additionalValues
 */
public interface CommerceCartHashCalculationStrategy
{
	/**
	 * @deprecated user {@link #buildHashForAbstractOrder(de.hybris.platform.commerceservices.service.data.CommerceOrderParameter)} instead
	 * Calculate a hash for the order + additional values passed as a parameter
	 *
	 * @param abstractOrderModel the order to calculate the hash for
	 * @param additionalValues   the additional values
	 * @return the calculated hash
	 */
	@Deprecated
	String buildHashForAbstractOrder(AbstractOrderModel abstractOrderModel, List<String> additionalValues);


	/**
		 * Calculate a hash for the order + additional values passed as a parameter
		 * @param parameter A parameter object holding the following values
		 *  - abstractOrderModel the order to calculate the hash for
		 *  - additionalValues   the additional values
		 * @return the calculated hash
		 */
	String buildHashForAbstractOrder(final CommerceOrderParameter parameter);


}
