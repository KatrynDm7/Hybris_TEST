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

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.springframework.util.Assert;


/**
 * Subscription mini-cart populator.
 */
public class SubscriptionMiniCartPopulator extends AbstractSubscriptionOrderPopulator<CartModel, CartData>
{
	// Concrete implementation of the SubscriptionMiniCartPopulator that should be used for further customizations

	@Override
	public void populate(@Nullable final CartModel source, @Nonnull final CartData target) throws ConversionException
	{
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source == null)
		{
			target.setTotalPrice(createZeroPrice());
			target.setDeliveryCost(null);
			target.setSubTotal(createZeroPrice());
			target.setTotalItems(0);
			target.setTotalUnitCount(Integer.valueOf(0));
		}
		else
		{
			if (source.getBillingTime() == null)
			{
				// compatibility mode: do not perform the subscription specific populator tasks
				return;
			}

			super.populate(source, target);
		}
	}

}
