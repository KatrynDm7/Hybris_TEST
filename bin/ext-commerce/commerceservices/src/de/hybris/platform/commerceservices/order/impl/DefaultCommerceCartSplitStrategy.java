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

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.order.CommerceAddToCartStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartSplitStrategy;
import de.hybris.platform.commerceservices.order.CommerceUpdateCartEntryStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import org.springframework.beans.factory.annotation.Required;


public class DefaultCommerceCartSplitStrategy extends AbstractCommerceCartStrategy implements CommerceCartSplitStrategy
{

	private CommerceAddToCartStrategy commerceAddToCartStrategy;
	private CommerceUpdateCartEntryStrategy commerceUpdateCartEntryStrategy;


	@Override
	public long split(final CommerceCartParameter parameters) throws CommerceCartModificationException
	{
		validateParameterNotNull(parameters.getCart(), "Cart model cannot be null");

		final AbstractOrderEntryModel cartEntry = getEntryForNumber(parameters.getCart(), (int) parameters.getEntryNumber());
		if (cartEntry != null && cartEntry.getQuantity().longValue() > 1)
		{
			// Reduce quantity on existing entry by one

			final CommerceCartParameter updateQuantityParams = new CommerceCartParameter();
			updateQuantityParams.setEnableHooks(true);
			updateQuantityParams.setCart(parameters.getCart());
			updateQuantityParams.setEntryNumber(parameters.getEntryNumber());
			updateQuantityParams.setQuantity(cartEntry.getQuantity().longValue() - 1);
			this.getCommerceUpdateCartEntryStrategy().updateQuantityForCartEntry(updateQuantityParams);

			// Add a new entry with the same product quantity one
			final CommerceCartParameter addToCartParams = new CommerceCartParameter();
			addToCartParams.setEnableHooks(true);
			addToCartParams.setCart(parameters.getCart());
			addToCartParams.setProduct(cartEntry.getProduct());
			addToCartParams.setUnit(cartEntry.getUnit());
			addToCartParams.setQuantity(1);
			addToCartParams.setCreateNewEntry(true);

			final CommerceCartModification modification = this.getCommerceAddToCartStrategy().addToCart(addToCartParams);
			return (modification != null && modification.getEntry() != null && modification.getEntry().getEntryNumber() != null) ? modification
					.getEntry().getEntryNumber().longValue()
					: 0;
		}
		return 0;
	}

	protected CommerceAddToCartStrategy getCommerceAddToCartStrategy()
	{
		return commerceAddToCartStrategy;
	}

	@Required
	public void setCommerceAddToCartStrategy(final CommerceAddToCartStrategy commerceAddToCartStrategy)
	{
		this.commerceAddToCartStrategy = commerceAddToCartStrategy;
	}

	protected CommerceUpdateCartEntryStrategy getCommerceUpdateCartEntryStrategy()
	{
		return commerceUpdateCartEntryStrategy;
	}

	@Required
	public void setCommerceUpdateCartEntryStrategy(final CommerceUpdateCartEntryStrategy commerceUpdateCartEntryStrategy)
	{
		this.commerceUpdateCartEntryStrategy = commerceUpdateCartEntryStrategy;
	}
}
