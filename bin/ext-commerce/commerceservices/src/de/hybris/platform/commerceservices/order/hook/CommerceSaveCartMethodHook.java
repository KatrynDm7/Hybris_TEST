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
 * A hook strategy to run custom code before/after saving a cart
 */
public interface CommerceSaveCartMethodHook
{

	/**
	 * Execute custom logic before saving a cart
	 *
	 * @param parameters
	 *           a {@link CommerceSaveCartParameter} parameter object
	 * @throws CommerceSaveCartException
	 *
	 */
	void beforeSaveCart(CommerceSaveCartParameter parameters) throws CommerceSaveCartException;

	/**
	 * Execute custom logic after saving a cart
	 *
	 * @param parameters
	 *           a {@link CommerceSaveCartParameter} parameter object
	 * @param saveCartResult
	 *           {@link CommerceSaveCartResult}
	 * @throws CommerceSaveCartException
	 */
	void afterSaveCart(CommerceSaveCartParameter parameters, CommerceSaveCartResult saveCartResult)
			throws CommerceSaveCartException;
}
