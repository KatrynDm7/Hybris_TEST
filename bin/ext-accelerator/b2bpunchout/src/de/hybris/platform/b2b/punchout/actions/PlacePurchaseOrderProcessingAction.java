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
package de.hybris.platform.b2b.punchout.actions;

import de.hybris.platform.b2b.punchout.PunchOutException;
import de.hybris.platform.b2b.punchout.PunchOutResponseCode;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.InvalidCartException;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.cxml.CXML;
import org.springframework.beans.factory.annotation.Required;


/**
 * Places an order using the session shopping cart.
 */
public class PlacePurchaseOrderProcessingAction implements PunchOutProcessingAction<CXML, CartModel>
{
	private static final Logger LOG = Logger.getLogger(PlacePurchaseOrderProcessingAction.class);

	@Deprecated
	@Resource(name = "b2bCheckoutFacade")
	private CheckoutFacade checkoutFacade;

	@Override
	public void process(final CXML input, final CartModel output)
	{
		final String cartCode = output.getCode();
		LOG.debug(String.format("Placing an order for cart with code: %s", cartCode));

		try
		{
			final OrderData orderData = checkoutFacade.placeOrder();
			LOG.debug(String.format("Order with code %s was placed.", orderData.getCode()));
		}
		catch (final InvalidCartException e)
		{
			throw new PunchOutException(PunchOutResponseCode.CONFLICT, "Unable to checkout", e);
		}
	}

	@Required
	public void setCheckoutFacade(final CheckoutFacade checkoutFacade)
	{
		this.checkoutFacade = checkoutFacade;
	}
}
