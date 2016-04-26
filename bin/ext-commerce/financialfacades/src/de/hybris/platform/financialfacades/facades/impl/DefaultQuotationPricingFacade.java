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
package de.hybris.platform.financialfacades.facades.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.quotation.QuotationRequestData;
import de.hybris.platform.commercefacades.quotation.QuotationResponseData;
import de.hybris.platform.financialfacades.facades.QuotationPricingFacade;
import de.hybris.platform.financialfacades.strategies.QuotationPricingStrategy;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class DefaultQuotationPricingFacade implements QuotationPricingFacade
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultQuotationPricingFacade.class);
	private Map<String, QuotationPricingStrategy> strategies;

	@Override
	public QuotationResponseData getQuote(final QuotationRequestData quotationRequestData)
	{
		QuotationPricingStrategy quotationPricingStrategy = null;

		validateParameterNotNullStandardMessage("Quotation RequestData should not be null", quotationRequestData);

		if (getStrategies().containsKey(quotationRequestData.getProviderName()))
		{
			quotationPricingStrategy = strategies.get(quotationRequestData.getProviderName());
		}

		//delegate the method call to the Strategy.
		if (quotationPricingStrategy != null)
		{
			return quotationPricingStrategy.getQuote(quotationRequestData);
		}
		else
		{
			LOG.warn("No quotation strategy found for " + quotationRequestData.getProviderName());
			return new QuotationResponseData();
		}
	}

	protected Map<String, QuotationPricingStrategy> getStrategies()
	{
		return strategies;
	}

	@Required
	public void setStrategies(final Map<String, QuotationPricingStrategy> strategies)
	{
		this.strategies = strategies;
	}
}
