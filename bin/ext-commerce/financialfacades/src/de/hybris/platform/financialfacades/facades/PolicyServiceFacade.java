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
package de.hybris.platform.financialfacades.facades;

import de.hybris.platform.commercefacades.insurance.data.PolicyRequestData;
import de.hybris.platform.commercefacades.insurance.data.PolicyResponseData;


/**
 * The class of PolicyService.
 */
public interface PolicyServiceFacade
{

	/**
	 * Request to create a policy to a external system, and return to hybris the information about the policy creation.
	 * 
	 * @param requestData
	 *           the policy request data
	 * @return policy response data
	 */
	PolicyResponseData requestPolicyCreation(final PolicyRequestData requestData);
}
