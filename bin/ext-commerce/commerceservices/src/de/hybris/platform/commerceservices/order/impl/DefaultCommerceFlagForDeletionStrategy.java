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

import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult;
import de.hybris.platform.core.model.order.CartModel;


/**
 * Default implementation of the interface
 * {@link de.hybris.platform.commerceservices.order.CommerceFlagForDeletionStrategy}. The provided
 * {@link de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter#cart} (or if empty the session
 * cart) is simply enriched with additional data to mark it as a saved cart. The cart is not cloned.
 */
public class DefaultCommerceFlagForDeletionStrategy extends AbstractCommerceFlagForDeletionStrategy
{
	@Override
	public CommerceSaveCartResult flagForDeletion(final CommerceSaveCartParameter parameters) throws CommerceSaveCartException
	{
		final CommerceSaveCartResult flagForDeletionResult = new CommerceSaveCartResult();
		this.beforeFlagForDeletion(parameters);

		validateFlagForDeletionParameters(parameters);

		final CartModel cartToBeFlagged = parameters.getCart();

		cartToBeFlagged.setExpirationTime(null);
		cartToBeFlagged.setSaveTime(null);
		cartToBeFlagged.setSavedBy(null);
		cartToBeFlagged.setName(null);
		cartToBeFlagged.setDescription(null);

		flagForDeletionResult.setSavedCart(cartToBeFlagged);
		getModelService().save(cartToBeFlagged);
		getModelService().refresh(cartToBeFlagged);

		this.afterFlagForDeletion(parameters, flagForDeletionResult);
		return flagForDeletionResult;
	}
}
