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

import de.hybris.platform.financialfacades.strategies.PolicyPdfUrlGeneratorStrategy;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


public class DefaultPolicyPdfUrlGenertorStrategy implements PolicyPdfUrlGeneratorStrategy
{
	/**
	 * The service which exposes the configuration of the application based upon the properties supplied.
	 */
	private ConfigurationService configurationService;

	/**
	 * the property key used to derive the secure protocol start of the local instance
	 */
	protected static final String WEBSITE_INSURANCE_HTTPS = "website.insurance.https";

	/**
	 * Section of the pdf url generation URL used in creating a 'proper' url for the policy
	 */
	protected static final String URL_CHECKOUT_PDF_PRINT = "/my-account/pdf/print/";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.financialacceleratorstorefront.strategies.PolicyPdfUrlGeneratorStrategy#generatePdfUrlForPolicy
	 * (java.lang.String)
	 */
	@Override
	public String generatePdfUrlForPolicy(final String policyUrlId)
	{
		if (policyUrlId != null)
		{
			return getConfigurationService().getConfiguration().getString(WEBSITE_INSURANCE_HTTPS) + URL_CHECKOUT_PDF_PRINT
					+ policyUrlId;
		}
		return StringUtils.EMPTY;
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
