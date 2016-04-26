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
package de.hybris.platform.financialfacades.strategies;


public interface PolicyPdfUrlGeneratorStrategy
{
	/**
	 * Creates a new url which is based upon the supplied policy specific Id. This Id is passed by the calling code and
	 * could be either a policy id, or the originating order id.
	 * 
	 * @param policyId
	 *           the ID of the newly generated policy
	 * @return a string containing the new url
	 */
	public String generatePdfUrlForPolicy(String policyId);
}
