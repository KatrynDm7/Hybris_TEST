/*
 *  
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
 */
package de.hybris.platform.financialfacades.facades.impl;

import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyListingData;
import de.hybris.platform.financialfacades.facades.InsurancePolicyFacade;
import de.hybris.platform.financialfacades.strategies.CustomerInsurancePolicyStrategy;
import de.hybris.platform.financialfacades.strategies.QuotePolicyStrategy;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public class DefaultInsurancePolicyFacade implements InsurancePolicyFacade
{
	public QuotePolicyStrategy quotePolicyStrategy;

	private CustomerInsurancePolicyStrategy customerInsurancePolicyStrategy;


	@Override
	public List<InsurancePolicyListingData> getPoliciesForCustomer(final String customerId)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("customerId", customerId);

		final List<InsurancePolicyListingData> insurancePolicyList = getCustomerInsurancePolicyStrategy().getPolicyDataForUID(
				customerId);

		return insurancePolicyList;
	}

	@Override
	public List<InsurancePolicyListingData> getPoliciesForCurrentCustomer()
	{
		final List<InsurancePolicyListingData> insurancePolicyList = getCustomerInsurancePolicyStrategy()
				.getPolicyDataForCurrentCustomer();

		return insurancePolicyList;
	}

	protected QuotePolicyStrategy getQuotePolicyStrategy()
	{
		return quotePolicyStrategy;
	}

	@Required
	public void setQuotePolicyStrategy(final QuotePolicyStrategy quotePolicyStrategy)
	{
		this.quotePolicyStrategy = quotePolicyStrategy;
	}

	protected CustomerInsurancePolicyStrategy getCustomerInsurancePolicyStrategy()
	{
		return customerInsurancePolicyStrategy;
	}

	@Required
	public void setCustomerInsurancePolicyStrategy(final CustomerInsurancePolicyStrategy customerInsurancePolicyStrategy)
	{
		this.customerInsurancePolicyStrategy = customerInsurancePolicyStrategy;
	}
}
