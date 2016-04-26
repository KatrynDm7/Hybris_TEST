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
package de.hybris.platform.b2bacceleratorfacades.order.impl;

import de.hybris.platform.acceleratorservices.enums.CheckoutFlowEnum;
import de.hybris.platform.acceleratorservices.enums.CheckoutPciOptionEnum;
import de.hybris.platform.b2bacceleratorfacades.api.cart.CheckoutFlowFacade;
import de.hybris.platform.b2bacceleratorservices.order.checkout.flow.B2BCheckoutFlowStrategy;
import de.hybris.platform.b2bacceleratorservices.order.checkout.pci.B2BCheckoutPciStrategy;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link CheckoutFlowFacade}. Delegates resolving the checkout flow to an injected
 * {@link B2BCheckoutFlowStrategy}.
 */
public class DefaultB2BCheckoutFlowFacade implements CheckoutFlowFacade
{
	private B2BCheckoutFlowStrategy checkoutFlowStrategy;
	private B2BCheckoutPciStrategy b2BCheckoutPciStrategy;

	@Override
	public CheckoutFlowEnum getCheckoutFlow()
	{
		return getCheckoutFlowStrategy().getCheckoutFlow();
	}

	@Override
	public CheckoutPciOptionEnum getSubscriptionPciOption()
	{
		return getCheckoutPciStrategy().getSubscriptionPciOption();
	}

	protected B2BCheckoutFlowStrategy getCheckoutFlowStrategy()
	{
		return checkoutFlowStrategy;
	}

	@Required
	public void setCheckoutFlowStrategy(final B2BCheckoutFlowStrategy strategy)
	{
		this.checkoutFlowStrategy = strategy;
	}

	protected B2BCheckoutPciStrategy getCheckoutPciStrategy()
	{
		return this.b2BCheckoutPciStrategy;
	}

	@Required
	public void setCheckoutPciStrategy(final B2BCheckoutPciStrategy strategy)
	{
		this.b2BCheckoutPciStrategy = strategy;
	}
}
