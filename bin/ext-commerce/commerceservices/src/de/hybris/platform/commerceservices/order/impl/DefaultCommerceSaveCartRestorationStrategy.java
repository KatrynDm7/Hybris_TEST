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
import de.hybris.platform.commerceservices.order.CommerceCartRestoration;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commerceservices.order.hook.CommerceSaveCartRestorationMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/***
 * Strategy class for restoring a saved cart and applying pre hooks
 */
public class DefaultCommerceSaveCartRestorationStrategy extends DefaultCommerceCartRestorationStrategy

{
	private List<CommerceSaveCartRestorationMethodHook> commerceSaveCartRestorationMethodHooks;
	private ConfigurationService configurationService;

	@Override
	public CommerceCartRestoration restoreCart(final CommerceCartParameter parameters) throws CommerceCartRestorationException
	{
		beforeCartRestoration(parameters);

		final CommerceCartParameter cartParameter = new CommerceCartParameter();
		cartParameter.setEnableHooks(parameters.isEnableHooks());
		cartParameter.setCart(parameters.getCart());

		final CommerceCartRestoration cartRestoration = super.restoreCart(cartParameter);

		afterCartRestoration(parameters);

		return cartRestoration;

	}

	protected void beforeCartRestoration(final CommerceCartParameter parameters) throws CommerceCartRestorationException
	{
		if (CollectionUtils.isNotEmpty(getCommerceSaveCartRestorationMethodHooks())
				&& (parameters.isEnableHooks() && getConfigurationService().getConfiguration().getBoolean(
						CommerceServicesConstants.SAVECARTRESTORATIONHOOK_ENABLED)))
		{

			for (final CommerceSaveCartRestorationMethodHook commerceSaveCartRestorationMethodHook : getCommerceSaveCartRestorationMethodHooks())
			{
				commerceSaveCartRestorationMethodHook.beforeRestoringCart(parameters);
			}
		}
	}

	protected void afterCartRestoration(final CommerceCartParameter parameters) throws CommerceCartRestorationException
	{
		if (CollectionUtils.isNotEmpty(getCommerceSaveCartRestorationMethodHooks())
				&& (parameters.isEnableHooks() && getConfigurationService().getConfiguration().getBoolean(
						CommerceServicesConstants.SAVECARTRESTORATIONHOOK_ENABLED)))
		{

			for (final CommerceSaveCartRestorationMethodHook commerceSaveCartRestorationMethodHook : getCommerceSaveCartRestorationMethodHooks())
			{
				commerceSaveCartRestorationMethodHook.afterRestoringCart(parameters);
			}
		}
	}

	protected List<CommerceSaveCartRestorationMethodHook> getCommerceSaveCartRestorationMethodHooks()
	{
		return commerceSaveCartRestorationMethodHooks;
	}

	public void setCommerceSaveCartRestorationMethodHooks(
			final List<CommerceSaveCartRestorationMethodHook> commerceSaveCartRestorationMethodHooks)
	{
		this.commerceSaveCartRestorationMethodHooks = commerceSaveCartRestorationMethodHooks;
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
