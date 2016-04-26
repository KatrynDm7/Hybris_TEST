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
package de.hybris.platform.commerceservices.strategies.impl;

import de.hybris.platform.commerceservices.strategies.NetGrossStrategy;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.price.PriceFactory;


/**
 * Default implementation of {@link NetGrossStrategy}
 */
public class DefaultNetGrossStrategy implements NetGrossStrategy
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commerceservices.strategies.NetGrossStrategy#isNet()
	 */
	@Override
	public boolean isNet()
	{
		// compare the behavior in {@link DefaultPriceService}
		final PriceFactory priceFactory = OrderManager.getInstance().getPriceFactory();
		return priceFactory.isNetUser(JaloSession.getCurrentSession().getUser());
	}

}
