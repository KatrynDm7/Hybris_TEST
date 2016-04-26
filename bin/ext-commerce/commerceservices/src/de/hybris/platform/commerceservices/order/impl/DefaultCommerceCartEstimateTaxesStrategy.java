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


import de.hybris.platform.commerceservices.order.CommerceCartEstimateTaxesStrategy;
import de.hybris.platform.core.model.order.CartModel;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

public class DefaultCommerceCartEstimateTaxesStrategy implements CommerceCartEstimateTaxesStrategy
{
	private final static Logger LOG = Logger.getLogger(DefaultCommerceCartEstimateTaxesStrategy.class);

	@Override
	public BigDecimal estimateTaxes(final CartModel cartModel, final String deliveryZipCode, final String deliveryCountryIsocode)
	{
		LOG.warn("Default external taxes estimation being called... This will always return 0.");
		return BigDecimal.ZERO;
	}
}
