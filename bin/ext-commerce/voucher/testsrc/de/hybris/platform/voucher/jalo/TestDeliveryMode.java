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
package de.hybris.platform.voucher.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.order.delivery.JaloDeliveryModeException;
import de.hybris.platform.util.PriceValue;


/**
 *
 */
public class TestDeliveryMode extends DeliveryMode
{
	public final static double deliveryCost = 2.5;

	@Override
	public PriceValue getCost(final SessionContext ctx, final AbstractOrder order) throws JaloDeliveryModeException
	{
		return new PriceValue(order.getCurrency().getIsoCode(), deliveryCost, order.isNet().booleanValue());
	}
}
