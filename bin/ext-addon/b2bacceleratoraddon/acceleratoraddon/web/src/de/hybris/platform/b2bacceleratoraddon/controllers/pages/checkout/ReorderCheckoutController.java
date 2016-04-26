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
package de.hybris.platform.b2bacceleratoraddon.controllers.pages.checkout;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractCheckoutController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.b2bacceleratorfacades.api.cart.CheckoutFacade;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.order.InvalidCartException;

import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@Scope("tenant")
@RequestMapping(value = "/checkout/summary")
public class ReorderCheckoutController extends AbstractCheckoutController
{
	@Resource(name = "b2bCheckoutFacade")
	private CheckoutFacade b2bCheckoutFacade;

	@RequestMapping(value = "/reorder", method =
	{ RequestMethod.PUT, RequestMethod.POST })
	@RequireHardLogIn
	public String reorder(@RequestParam(value = "orderCode") final String orderCode, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException, InvalidCartException, ParseException, CommerceCartModificationException
	{
		// create a cart from the order and set it as session cart.
		b2bCheckoutFacade.createCartFromOrder(orderCode);
		// validate for stock and availability
		final List<CartModificationData> cartModifications = getCartFacade().validateCartData();
		for (final CartModificationData cartModification : cartModifications)
		{
			if (CommerceCartModificationStatus.NO_STOCK.equals(cartModification.getStatusCode()))
			{
				GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
						"basket.page.message.update.reducedNumberOfItemsAdded.noStock", new Object[]
						{ cartModification.getEntry().getProduct().getName() });
				break;
			}
			else if (cartModification.getQuantity() != cartModification.getQuantityAdded())
			{
				// item has been modified to match available stock levels
				GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
						"basket.information.quantity.adjusted");
				break;
			}
			// TODO: handle more specific messaging, i.e. out of stock, product not available
		}
		return REDIRECT_PREFIX + "/checkout/multi/summary/view";
	}
}
