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

import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.commerceservices.order.CommerceAddToCartStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.hook.CommerceAddToCartMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Abstract strategy for adding items to the cart
 */
public abstract class AbstractCommerceAddToCartStrategy extends AbstractCommerceCartStrategy implements CommerceAddToCartStrategy
{
	protected static final int APPEND_AS_LAST = -1;
	private List<CommerceAddToCartMethodHook> commerceAddToCartMethodHooks;
	private ConfigurationService configurationService;

	protected void validateAddToCart(final CommerceCartParameter parameters) throws CommerceCartModificationException
	{
		final CartModel cartModel = parameters.getCart();
		final ProductModel productModel = parameters.getProduct();

		validateParameterNotNull(cartModel, "Cart model cannot be null");
		validateParameterNotNull(productModel, "Product model cannot be null");
		if (productModel.getVariantType() != null)
		{
			throw new CommerceCartModificationException("Choose a variant instead of the base product");
		}

		if (parameters.getQuantity() < 1)
		{
			throw new CommerceCartModificationException("Quantity must not be less than one");
		}
	}

	protected void beforeAddToCart(final CommerceCartParameter parameters) throws CommerceCartModificationException
	{
		if (getCommerceAddToCartMethodHooks() != null
				&& (parameters.isEnableHooks() && getConfigurationService().getConfiguration().getBoolean(
						CommerceServicesConstants.ADDTOCARTHOOK_ENABLED)))
		{
			for (final CommerceAddToCartMethodHook commerceAddToCartMethodHook : getCommerceAddToCartMethodHooks())
			{
				commerceAddToCartMethodHook.beforeAddToCart(parameters);
			}
		}
	}

	protected void afterAddToCart(final CommerceCartParameter parameters, final CommerceCartModification result)
			throws CommerceCartModificationException
	{
		if (getCommerceAddToCartMethodHooks() != null
				&& (parameters.isEnableHooks() && getConfigurationService().getConfiguration().getBoolean(
						CommerceServicesConstants.ADDTOCARTHOOK_ENABLED)))
		{
			for (final CommerceAddToCartMethodHook commerceAddToCartMethodHook : getCommerceAddToCartMethodHooks())
			{
				commerceAddToCartMethodHook.afterAddToCart(parameters, result);
			}
		}
	}

	protected List<CommerceAddToCartMethodHook> getCommerceAddToCartMethodHooks()
	{
		return commerceAddToCartMethodHooks;
	}

	/**
	 * Optional setter for hooking into before and after execution of
	 * {@link #addToCart(de.hybris.platform.commerceservices.service.data.CommerceCartParameter)}
	 * 
	 * @param commerceAddToCartMethodHooks
	 */
	public void setCommerceAddToCartMethodHooks(final List<CommerceAddToCartMethodHook> commerceAddToCartMethodHooks)
	{
		this.commerceAddToCartMethodHooks = commerceAddToCartMethodHooks;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
}
