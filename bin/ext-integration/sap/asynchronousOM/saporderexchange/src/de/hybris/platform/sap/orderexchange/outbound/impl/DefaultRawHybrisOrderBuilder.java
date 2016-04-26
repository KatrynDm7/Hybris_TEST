/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.sap.orderexchange.outbound.impl;

import de.hybris.platform.core.model.order.OrderModel;

import org.apache.log4j.Logger;


/**
 * Class definition as a workaround for Spring since it cannot handle parameterized types
 */
public class DefaultRawHybrisOrderBuilder extends AbstractRawItemBuilder<OrderModel>
{
	private static final Logger LOG = Logger.getLogger(DefaultRawHybrisOrderBuilder.class);

	@Override
	protected Logger getLogger()
	{
		return LOG;
	}

}
