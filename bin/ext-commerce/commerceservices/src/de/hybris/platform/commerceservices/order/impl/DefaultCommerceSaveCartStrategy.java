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
import de.hybris.platform.commerceservices.order.CommerceSaveCartStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.time.TimeService;

import java.util.Date;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the interface {@link CommerceSaveCartStrategy}. The provided
 * {@link CommerceSaveCartParameter#cart} (or if empty the session cart) is simply enriched with additional data to mark
 * it as a saved cart. The cart is not cloned.
 */
public class DefaultCommerceSaveCartStrategy extends AbstractCommerceSaveCartStrategy
{
	private static int DEFAULT_SAVE_CART_EXPIRY_DAYS = 30;
	private TimeService timeService;

	@Override
	public CommerceSaveCartResult saveCart(final CommerceSaveCartParameter parameters) throws CommerceSaveCartException
	{
		this.beforeSaveCart(parameters);

		final CommerceSaveCartResult saveCartResult = new CommerceSaveCartResult();

		validateSaveCartParameters(parameters);

		final CartModel cartToBeSaved = parameters.getCart() == null ? getCartService().getSessionCart() : parameters.getCart();
		validateSaveCart(cartToBeSaved);

		final Date currentDate = getTimeService().getCurrentTime();

		cartToBeSaved.setExpirationTime(calculateExpirationTime(currentDate));
		cartToBeSaved.setSaveTime(currentDate);
		cartToBeSaved.setSavedBy(getUserService().getCurrentUser());
		cartToBeSaved.setName(parameters.getName());
		cartToBeSaved.setDescription(parameters.getDescription());

		saveCartResult.setSavedCart(cartToBeSaved);
		getModelService().save(cartToBeSaved);
		getModelService().refresh(cartToBeSaved);

		this.afterSaveCart(parameters, saveCartResult);
		return saveCartResult;
	}

	protected Date calculateExpirationTime(final Date currentDate)
	{
		final Integer expirationDays = getConfigurationService().getConfiguration().getInteger(
				"commerceservices.saveCart.expiryTime.days", Integer.valueOf(DEFAULT_SAVE_CART_EXPIRY_DAYS));
		return new DateTime(currentDate).plusDays(expirationDays.intValue()).toDate();
	}

	protected TimeService getTimeService()
	{
		return timeService;
	}

	@Required
	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}

}
