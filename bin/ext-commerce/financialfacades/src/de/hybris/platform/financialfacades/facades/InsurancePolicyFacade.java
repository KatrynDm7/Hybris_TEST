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
package de.hybris.platform.financialfacades.facades;

import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyListingData;

import java.util.List;


public interface InsurancePolicyFacade extends PolicyFacade
{
	/**
	 * Obtain policy information for a given user ID
	 */
	List<InsurancePolicyListingData> getPoliciesForCustomer(String customerId);

	/**
	 * Obtain policy information for a the current user
	 */
	List<InsurancePolicyListingData> getPoliciesForCurrentCustomer();

}
