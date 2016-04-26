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

import de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult;


/**
 * A strategy interface for flagging a cart for deletion.
 */
public interface CommerceFlagForDeletionStrategy
{
	/**
	 * Method for explicitly flagging a cart for deletion
	 *
	 * @param parameters
	 *           {@link de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter} parameter object that
	 *           holds the cart to be flagged for deletion along with some additional details such as a name and a
	 *           description for this cart
	 * @return {@link de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult}
	 * @throws CommerceSaveCartException
	 *            if cart cannot be flagged for deletion
	 */
	CommerceSaveCartResult flagForDeletion(final CommerceSaveCartParameter parameters) throws CommerceSaveCartException;
}
