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
package de.hybris.platform.subscriptionfacades.order;

import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;

import javax.annotation.Nonnull;


/**
 * This interface declares methods for manipulating subscription products in the cart.
 */
public interface SubscriptionCartFacade extends CartFacade
{
	/**
	 * Refreshes the xml of a cart entry, if the currency has changed.
	 */
	void refreshProductXMLs();

	/**
	 * Method for upgrading a subscription product. It adds the new subscription product to the cart and sets a reference
	 * to the original subscription
	 * 
	 * @param code
	 *           code of product to add
	 * @param originalSubscriptionId
	 *           the id of the original subscription which is upgraded by this addToCart
	 * @param originalOrderCode
	 *           the code of the original order in which the original subscription was bought
	 * @param originalEntryNumber
	 * @return the cart modification data that includes a statusCode and the actual quantity added to the cart
	 * @throws CommerceCartModificationException
	 *            if the cart cannot be modified
	 */
	@Nonnull
	CartModificationData addToCart(@Nonnull String code,@Nonnull final String originalSubscriptionId,@Nonnull final String originalOrderCode,
			final int originalEntryNumber) throws CommerceCartModificationException;

}
