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
package de.hybris.platform.commerceservices.order.hook;

import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult;


/**
 * A hook strategy to run custom code before/after flagging a cart for deletion
 */
public interface CommerceFlagForDeletionMethodHook
{
	/**
	 * Execute custom logic before flagging a cart for deletion
	 *
	 * @param parameters
	 *           a {@link de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter} parameter object
	 * @throws de.hybris.platform.commerceservices.order.CommerceSaveCartException
	 *
	 */
	void beforeFlagForDeletion(CommerceSaveCartParameter parameters) throws CommerceSaveCartException;

	/**
	 * Execute custom logic after flagging a cart for deletion
	 *
	 * @param parameters
	 *           a {@link CommerceSaveCartParameter} parameter object
	 * @param saveCartResult
	 *           {@link de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult}
	 * @throws CommerceSaveCartException
	 */
	void afterFlagForDeletion(CommerceSaveCartParameter parameters, CommerceSaveCartResult saveCartResult)
			throws CommerceSaveCartException;
}
