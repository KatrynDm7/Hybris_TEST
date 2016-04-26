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
package de.hybris.platform.commerceservices.customer;

import de.hybris.platform.core.model.user.CustomerModel;

/**
 * Service interface used to lookup the contact email address for a customer
 */
public interface CustomerEmailResolutionService
{
	/**
	 * Retrieves email address of a given customer
	 *
	 * @param customerModel the customer
	 * @return the customer's email address
	 */
	String getEmailForCustomer(CustomerModel customerModel);
}
