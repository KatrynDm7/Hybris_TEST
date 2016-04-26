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

import de.hybris.platform.commerceservices.order.CommercePaymentProviderStrategy;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

public class DefaultCommercePaymentProviderStrategy implements CommercePaymentProviderStrategy
{
	private BaseStoreService baseStoreService;

	@Override
	public String getPaymentProvider()
	{
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		Assert.notNull(currentBaseStore, "Current BaseStore should exist in the session.");
		final String paymentProvider = currentBaseStore.getPaymentProvider();
		Assert.notNull(paymentProvider, "Payment provider should be set on the BaseStore");
		return paymentProvider;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}
}
