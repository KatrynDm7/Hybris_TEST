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
package de.hybris.platform.commerceservices.order.impl;

import de.hybris.platform.commerceservices.strategies.ModifiableChecker;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import org.apache.commons.lang.BooleanUtils;

public class OrderEntryModifiableChecker implements ModifiableChecker<AbstractOrderEntryModel>
{
	@Override
	public boolean canModify(final AbstractOrderEntryModel given)
	{
		return BooleanUtils.isNotTrue(given.getGiveAway());
	}
}
