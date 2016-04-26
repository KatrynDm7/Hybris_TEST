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
package de.hybris.platform.sap.sapordermgmtb2bfacades.checkout.impl;

import de.hybris.platform.acceleratorfacades.flow.CheckoutFlowFacade;
import de.hybris.platform.acceleratorservices.enums.CheckoutFlowEnum;
import de.hybris.platform.acceleratorservices.enums.CheckoutPciOptionEnum;
import de.hybris.platform.b2bacceleratorservices.order.checkout.flow.B2BCheckoutFlowStrategy;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link CheckoutFlowFacade}. Delegates resolving the checkout flow to an injected
 * {@link B2BCheckoutFlowStrategy}.
 */
public class SapB2BCheckoutFlowFacade extends SapB2BAcceleratorCheckoutFacade implements CheckoutFlowFacade
{
	private CheckoutFlowFacade checkoutFlowFacade;

	@Deprecated
	@Override
	public CheckoutFlowEnum getCheckoutFlow()
	{
		return getCheckoutFlowFacade().getCheckoutFlow();
	}

	@Override
	public CheckoutPciOptionEnum getSubscriptionPciOption()
	{
		return getCheckoutFlowFacade().getSubscriptionPciOption();
	}

	protected CheckoutFlowFacade getCheckoutFlowFacade()
	{
		return checkoutFlowFacade;
	}

	@Required
	public void setCheckoutFlowFacade(final CheckoutFlowFacade facade)
	{
		checkoutFlowFacade = facade;
	}
}
