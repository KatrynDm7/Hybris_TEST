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
import de.hybris.platform.commerceservices.order.CommerceFlagForDeletionStrategy;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.commerceservices.order.hook.CommerceFlagForDeletionMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Abstract strategy for flagging a cart for deletion
 */
public abstract class AbstractCommerceFlagForDeletionStrategy extends AbstractCommerceCartStrategy implements
		CommerceFlagForDeletionStrategy
{
	private List<CommerceFlagForDeletionMethodHook> commerceFlagForDeletionMethodHooks;
	private ConfigurationService configurationService;

	protected void validateFlagForDeletionParameters(final CommerceSaveCartParameter parameters)
	{
		validateParameterNotNull(parameters, "parameters cannot be null");
		validateParameterNotNull(parameters.getCart(), "cart cannot not be null");
	}

	protected void beforeFlagForDeletion(final CommerceSaveCartParameter parameters) throws CommerceSaveCartException
	{
		if (CollectionUtils.isNotEmpty(getCommerceFlagForDeletionMethodHooks())
				&& (parameters.isEnableHooks() && getConfigurationService().getConfiguration().getBoolean(
						CommerceServicesConstants.FLAGFORDELETIONHOOK_ENABLED, true)))
		{
			for (final CommerceFlagForDeletionMethodHook commerceFlagForDeletionMethodHook : getCommerceFlagForDeletionMethodHooks())
			{
				commerceFlagForDeletionMethodHook.beforeFlagForDeletion(parameters);
			}
		}
	}

	protected void afterFlagForDeletion(final CommerceSaveCartParameter parameters,
			final CommerceSaveCartResult flagForDeletionResult) throws CommerceSaveCartException
	{
		if (CollectionUtils.isNotEmpty(getCommerceFlagForDeletionMethodHooks())
				&& (parameters.isEnableHooks() && getConfigurationService().getConfiguration().getBoolean(
						CommerceServicesConstants.FLAGFORDELETIONHOOK_ENABLED, true)))
		{
			for (final CommerceFlagForDeletionMethodHook commerceFlagForDeletionMethodHook : getCommerceFlagForDeletionMethodHooks())
			{
				commerceFlagForDeletionMethodHook.afterFlagForDeletion(parameters, flagForDeletionResult);
			}
		}
	}

	protected List<CommerceFlagForDeletionMethodHook> getCommerceFlagForDeletionMethodHooks()
	{
		return commerceFlagForDeletionMethodHooks;
	}

	@Required
	public void setCommerceFlagForDeletionMethodHooks(
			final List<CommerceFlagForDeletionMethodHook> commerceFlagForDeletionMethodHooks)
	{
		this.commerceFlagForDeletionMethodHooks = commerceFlagForDeletionMethodHooks;
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
