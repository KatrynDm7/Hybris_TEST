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
package de.hybris.platform.timedaccesspromotionsfacades.order.hook.impl;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.order.hook.CommerceAddToCartMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.timedaccesspromotionsservices.service.FlashbuyPromotionService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Setting cartEntryPK of specific {@link ApprovedRequestModel}
 */
public class FlashbuyCommerceAddToCartMethodHook implements CommerceAddToCartMethodHook
{
	private static final Logger LOG = Logger.getLogger(FlashbuyCommerceAddToCartMethodHook.class);

	private FlashbuyPromotionService flashbuyPromotionService;
	private UserService userService;
	private CommerceCartService commerceCartService;

	@Override
	public void afterAddToCart(final CommerceCartParameter parameters, final CommerceCartModification result)
			throws CommerceCartModificationException
	{
		if (parameters.getPromotionCode() != null)
		{
			final String promotionMacher = result.getEntry().getPromotionMatcher();
			// recalculate cart if reserve success
			if (getFlashbuyPromotionService().reserve(parameters.getPromotionCode(), parameters.getProduct().getCode(),
					getUserService().getCurrentUser().getUid(), promotionMacher))
			{
				result.getEntry().setPromotionCode(parameters.getPromotionCode());

				try
				{
					getCommerceCartService().recalculateCart(parameters);
				}
				catch (final CalculationException ex)
				{
					LOG.error("Failed to recalculate order [" + parameters.getCart().getCode() + "]", ex);
				}
			}
		}
	}

	@Override
	public void beforeAddToCart(final CommerceCartParameter parameters) throws CommerceCartModificationException
	{

	}

	protected FlashbuyPromotionService getFlashbuyPromotionService()
	{
		return flashbuyPromotionService;
	}

	@Required
	public void setFlashbuyPromotionService(final FlashbuyPromotionService flashbuyPromotionService)
	{
		this.flashbuyPromotionService = flashbuyPromotionService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}

	@Required
	public void setCommerceCartService(final CommerceCartService commerceCartService)
	{
		this.commerceCartService = commerceCartService;
	}

}
