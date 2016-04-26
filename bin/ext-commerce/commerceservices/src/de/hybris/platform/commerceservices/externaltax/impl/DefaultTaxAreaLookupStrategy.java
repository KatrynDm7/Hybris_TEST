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
package de.hybris.platform.commerceservices.externaltax.impl;


import de.hybris.platform.commerceservices.externaltax.TaxAreaLookupStrategy;
import de.hybris.platform.core.model.order.AbstractOrderModel;


public class DefaultTaxAreaLookupStrategy implements TaxAreaLookupStrategy
{
	@Override
	public String getTaxAreaForOrder(final AbstractOrderModel orderModel)
	{
		if (orderModel == null || orderModel.getDeliveryAddress() == null)
		{
			throw new IllegalArgumentException("Can not determine taxArea for order without delivery address");
		}
		return orderModel.getDeliveryAddress().getCountry().getIsocode();
	}
}
