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
package de.hybris.platform.ycommercewebservices.request.support.impl;

import de.hybris.platform.commerceservices.order.CommercePaymentProviderStrategy;
import de.hybris.platform.ycommercewebservices.exceptions.UnsupportedRequestException;
import de.hybris.platform.ycommercewebservices.request.support.RequestSupportedStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


/**
 * Implementation for {@link RequestSupportedStrategy} which checks if request is supported based on payment provider
 * name
 * 
 * 
 */
public class PaymentProviderRequestSupportedStrategy implements RequestSupportedStrategy
{
	private CommercePaymentProviderStrategy paymentProviderStrategy;

	private Map<String, List<String>> unsupportedRequestMap = new HashMap();

	@Override
	public boolean isRequestSupported(final String requestId)
	{
		final String paymentProvider = paymentProviderStrategy.getPaymentProvider();
		if (paymentProvider != null)
		{
			final List<String> unsupportedRequests = unsupportedRequestMap.get(paymentProvider);
			if (unsupportedRequests != null)
			{
				return !unsupportedRequests.contains(requestId);
			}
		}
		return true;
	}

	@Override
	public void checkIfRequestSupported(final String requestId) throws UnsupportedRequestException
	{
		final String paymentProvider = paymentProviderStrategy.getPaymentProvider();
		if (paymentProvider != null)
		{
			final List<String> unsupportedRequests = unsupportedRequestMap.get(paymentProvider);
			if (unsupportedRequests != null)
			{
				if (unsupportedRequests.contains(requestId))
				{
					throw new UnsupportedRequestException("This request is not supported for payment provider : " + paymentProvider);
				}
			}
		}
	}

	public CommercePaymentProviderStrategy getPaymentProviderStrategy()
	{
		return paymentProviderStrategy;
	}

	@Required
	public void setPaymentProviderStrategy(final CommercePaymentProviderStrategy paymentProviderStrategy)
	{
		this.paymentProviderStrategy = paymentProviderStrategy;
	}

	public Map<String, List<String>> getUnsupportedRequestMap()
	{
		return unsupportedRequestMap;
	}

	public void setUnsupportedRequestMap(final Map<String, List<String>> unsupportedRequestMap)
	{
		this.unsupportedRequestMap = unsupportedRequestMap;
	}
}
