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
package de.hybris.platform.commerceservices.order;

/**
 *  A strategy for getting a payment provider
 *
 */
public interface CommercePaymentProviderStrategy
{
	/**
	 * Get payment provider assigned to the {@link de.hybris.platform.store.BaseStoreModel}
	 * @return A payment provider name.
	 */
	String getPaymentProvider();
}
