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
package de.hybris.platform.subscriptionfacades.order.converters.populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Nonnull;


/**
 * Subscription order populator.
 */
public class SubscriptionOrderPopulator extends AbstractSubscriptionOrderPopulator<OrderModel, OrderData>
{
	// Concrete implementation of the SubscriptionOrderPopulator that should be used for further customizations

	@Override
	public void populate(@Nonnull final OrderModel source,
	                     @Nonnull final OrderData target) throws ConversionException
	{

		validateParameterNotNullStandardMessage("source", source);

		if (source.getBillingTime() == null)
		{
			// compatibility mode: do not perform the subscription specific populator tasks
			return;
		}

		super.populate(source, target);
	}

}
