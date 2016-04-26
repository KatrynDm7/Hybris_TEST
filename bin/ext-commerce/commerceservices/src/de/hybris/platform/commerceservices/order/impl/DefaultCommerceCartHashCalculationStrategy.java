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


import de.hybris.platform.commerceservices.order.CommerceCartHashCalculationStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceOrderParameter;
import de.hybris.platform.core.model.order.AbstractOrderModel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.crypto.codec.Hex;


public class DefaultCommerceCartHashCalculationStrategy implements CommerceCartHashCalculationStrategy
{
	private static final Logger LOG = Logger.getLogger(DefaultCommerceCartHashCalculationStrategy.class);

	@Override
	@Deprecated
	public String buildHashForAbstractOrder(final AbstractOrderModel abstractOrderModel, final List<String> additionalValues)
	{
		final CommerceOrderParameter parameter = new CommerceOrderParameter();
		parameter.setOrder(abstractOrderModel);
		parameter.setAdditionalValues(additionalValues);
		return this.buildHashForAbstractOrder(parameter);
	}

	@Override
	public String buildHashForAbstractOrder(final CommerceOrderParameter parameter)
	{
		final AbstractOrderModel abstractOrderModel = parameter.getOrder();
		final StringBuilder orderValues = new StringBuilder();

		if (abstractOrderModel.getModifiedtime() != null)
		{
			orderValues.append(abstractOrderModel.getModifiedtime().toString());
		}

		final String orderValue = orderValues.toString();

		try
		{
			final MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(orderValue.getBytes());
			return String.valueOf(Hex.encode(md5.digest()));
		}
		catch (final NoSuchAlgorithmException e)
		{
			LOG.error("NoSuchAlgorithmException while computing the order hash. This should never happen.", e);
		}

		return orderValue;
	}

}
