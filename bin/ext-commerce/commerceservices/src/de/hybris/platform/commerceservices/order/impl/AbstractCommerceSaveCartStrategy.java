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

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.commerceservices.order.CommerceSaveCartStrategy;
import de.hybris.platform.commerceservices.order.hook.CommerceSaveCartMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Abstract strategy for saving a cart
 */
public abstract class AbstractCommerceSaveCartStrategy extends AbstractCommerceCartStrategy implements CommerceSaveCartStrategy
{
	private List<CommerceSaveCartMethodHook> commerceSaveCartMethodHooks;
	private ConfigurationService configurationService;
	private UserService userService;

	protected void validateSaveCartParameters(final CommerceSaveCartParameter parameters)
	{
		validateParameterNotNull(parameters, "parameters cannot be null");
		validateParameterNotNull(StringUtils.defaultIfEmpty(parameters.getName(), null), "name for saved cart cannot be empty");
		validateParameterNotNull(StringUtils.defaultIfEmpty(parameters.getDescription(), null),
				"description for saved cart cannot be empty");
	}

	protected void validateSaveCart(final CartModel cartToBeSaved) throws CommerceSaveCartException
	{
		validateParameterNotNull(cartToBeSaved, "cart cannot be null");
		validateParameterNotNull(cartToBeSaved.getUser(), "cart user cannot be null");
		final UserModel cartUser = cartToBeSaved.getUser();

		if (cartUser.equals(getUserService().getAnonymousUser()))
		{
			throw new CommerceSaveCartException("Anonymous carts cannot be saved. The user must be logged in.");
		}
	}

	protected void beforeSaveCart(final CommerceSaveCartParameter parameters) throws CommerceSaveCartException
	{
		if (CollectionUtils.isNotEmpty(getCommerceSaveCartMethodHooks())
				&& (parameters.isEnableHooks() && getConfigurationService().getConfiguration().getBoolean(
						CommerceServicesConstants.SAVECARTHOOK_ENABLED)))
		{
			for (final CommerceSaveCartMethodHook commerceSaveCartMethodHook : getCommerceSaveCartMethodHooks())
			{
				commerceSaveCartMethodHook.beforeSaveCart(parameters);
			}
		}
	}

	protected void afterSaveCart(final CommerceSaveCartParameter parameters, final CommerceSaveCartResult saveCartResult)
			throws CommerceSaveCartException
	{
		if (CollectionUtils.isNotEmpty(getCommerceSaveCartMethodHooks())
				&& (parameters.isEnableHooks() && getConfigurationService().getConfiguration().getBoolean(
						CommerceServicesConstants.SAVECARTHOOK_ENABLED)))
		{
			for (final CommerceSaveCartMethodHook commerceSaveCartMethodHook : getCommerceSaveCartMethodHooks())
			{
				commerceSaveCartMethodHook.afterSaveCart(parameters, saveCartResult);
			}
		}
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

	protected List<CommerceSaveCartMethodHook> getCommerceSaveCartMethodHooks()
	{
		return commerceSaveCartMethodHooks;
	}

	/**
	 * Optional setter for hooking into before and after execution of
	 * {@link #saveCart(de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter)}
	 *
	 * @param commerceSaveCartMethodHooks
	 */
	public void setCommerceSaveCartMethodHooks(final List<CommerceSaveCartMethodHook> commerceSaveCartMethodHooks)
	{
		this.commerceSaveCartMethodHooks = commerceSaveCartMethodHooks;
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
}
