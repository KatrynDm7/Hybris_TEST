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

import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;

/**
 *  A strategies for removing cart entries
 *
 */
public interface CommerceRemoveEntriesStrategy
{
	/**
	 * Removes all entries from the given {@link de.hybris.platform.core.model.order.CartModel}.
	 *
	 * @param parameter A parameter object holding the {@link de.hybris.platform.core.model.order.CartModel} that will be emptied
	 */
	 void removeAllEntries(final CommerceCartParameter parameter);
}
