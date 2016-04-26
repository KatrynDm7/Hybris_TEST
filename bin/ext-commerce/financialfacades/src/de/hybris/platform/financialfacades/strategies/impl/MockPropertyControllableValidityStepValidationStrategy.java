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
package de.hybris.platform.financialfacades.strategies.impl;


import de.hybris.platform.financialfacades.strategies.StepValidationStrategy;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.springframework.beans.factory.annotation.Required;


public class MockPropertyControllableValidityStepValidationStrategy implements StepValidationStrategy
{
	private ConfigurationService configurationService;
	private String propertyFlagKey;

	/**
	 * Check if step is valid strategy.
	 */
	@Override
	public boolean isValid()
	{
		String property_str = getConfigurationService().getConfiguration().getString(propertyFlagKey);
		return Boolean.parseBoolean(property_str);
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	protected String getPropertyFlagKey()
	{
		return propertyFlagKey;
	}

	@Required
	public void setPropertyFlagKey(String propertyFlagKey)
	{
		this.propertyFlagKey = propertyFlagKey;
	}
}
