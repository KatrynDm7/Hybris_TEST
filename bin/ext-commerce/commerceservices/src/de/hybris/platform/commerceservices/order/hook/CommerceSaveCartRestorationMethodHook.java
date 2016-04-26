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

import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;


/**
 * A hook strategy to run custom code before restoring a cart
 */
public interface CommerceSaveCartRestorationMethodHook
{

	/**
	 * Execute custom logic before restoring a cart
	 *
	 * @param parameters
	 *           a {@link CommerceCartParameter} parameter object
	 * @throws CommerceCartRestorationException
	 *
	 */
	void beforeRestoringCart(CommerceCartParameter parameters) throws CommerceCartRestorationException;


	/**
	 * Execute custom logic after restoring a cart
	 *
	 * @param parameters
	 *           a {@link CommerceCartParameter} parameter object
	 * @throws CommerceCartRestorationException
	 *
	 */
	void afterRestoringCart(CommerceCartParameter parameters) throws CommerceCartRestorationException;
}
