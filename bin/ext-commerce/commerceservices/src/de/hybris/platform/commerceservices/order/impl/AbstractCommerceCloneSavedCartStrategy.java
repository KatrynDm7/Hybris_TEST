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
import de.hybris.platform.commerceservices.order.CommerceCloneSavedCartStrategy;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.commerceservices.order.hook.CommerceCloneSavedCartMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Abstract strategy for cloning a cart
 */
public abstract class AbstractCommerceCloneSavedCartStrategy extends AbstractCommerceCartStrategy implements
		CommerceCloneSavedCartStrategy
{
	private List<CommerceCloneSavedCartMethodHook> commerceCloneSavedCartMethodHooks;
	private ConfigurationService configurationService;

	protected void beforeCloneSaveCart(final CommerceSaveCartParameter parameters) throws CommerceSaveCartException
	{
		if (CollectionUtils.isNotEmpty(getCommerceCloneSavedCartMethodHooks())
				&& (parameters.isEnableHooks() && getConfigurationService().getConfiguration().getBoolean(
						CommerceServicesConstants.CLONESAVEDCARTHOOK_ENABLED, true)))
		{
			for (final CommerceCloneSavedCartMethodHook commerceCloneSavedCartMethodHook : getCommerceCloneSavedCartMethodHooks())
			{
				commerceCloneSavedCartMethodHook.beforeCloneSavedCart(parameters);
			}
		}
	}

	protected void afterCloneSaveCart(final CommerceSaveCartParameter parameters, final CommerceSaveCartResult cloneCartResult)
			throws CommerceSaveCartException
	{
		if (CollectionUtils.isNotEmpty(getCommerceCloneSavedCartMethodHooks())
				&& (parameters.isEnableHooks() && getConfigurationService().getConfiguration().getBoolean(
						CommerceServicesConstants.CLONESAVEDCARTHOOK_ENABLED, true)))
		{
			for (final CommerceCloneSavedCartMethodHook commercecloneCartMethodHook : getCommerceCloneSavedCartMethodHooks())
			{
				commercecloneCartMethodHook.afterCloneSavedCart(parameters, cloneCartResult);
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

	protected List<CommerceCloneSavedCartMethodHook> getCommerceCloneSavedCartMethodHooks()
	{
		return commerceCloneSavedCartMethodHooks;
	}

	public void setCommerceCloneSavedCartMethodHooks(final List<CommerceCloneSavedCartMethodHook> commerceCloneSavedCartMethodHooks)
	{
		this.commerceCloneSavedCartMethodHooks = commerceCloneSavedCartMethodHooks;
	}

}
