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
package de.hybris.platform.financialfacades.facades;


import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CommerceSaveCartResultData;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.configurablebundlefacades.order.BundleCartFacade;
import de.hybris.platform.order.InvalidCartException;

import java.util.List;


public interface InsuranceCartFacade extends BundleCartFacade
{

	/**
	 * Set the visited step to the quote in the current cart.
	 */
	void setVisitedStepToQuoteInCurrentCart(String progressStepId);

	/**
	 * Check the isVisited status for the step against the quote from the current cart
	 */
	boolean checkStepIsVisitedFromQuoteInCurrentCart(String progressStepId);

	/**
	 * Get the quote state from the current cart.
	 **/
	boolean checkStepIsEnabledFromQuoteInCurrentCart();

	/**
	 * Check if the give product has the same default category as session cart product.
	 */
	boolean isSameInsuranceInSessionCart(final String productCode) throws InvalidCartException;

	/**
	 * Unbind the quote object in the session cart.
	 */
	boolean unbindQuoteInSessionCart();

	/**
	 * Bind the quote object in the session cart.
	 */
	boolean bindQuoteInSessionCart();

	/**
	 * Restore cart with code for current user and the site.
	 */
	void restoreCart(String code, Boolean isSaveCurrentCart) throws CommerceSaveCartException;

	/**
	 * Method saves a insurance cart for current user.
	 *
	 * @return {@link CommerceSaveCartResultData}
	 * @throws de.hybris.platform.commerceservices.order.CommerceSaveCartException
	 *            if cart cannot be saved
	 */
	CommerceSaveCartResultData saveCurrentUserCart() throws CommerceSaveCartException;

	/*
	 * Get saved carts for the current user.
	 */
	List<CartData> getSavedCartsForCurrentUser();

	/**
	 * Check if the cart with sessionCartCode is inside the saved cart list.
	 */
	public boolean isSavedCart(String sessionCartCode);

	/**
	 * Clear all items in the session cart by main bundle. For insurance products it expects with only 1 bundle in the
	 * cart.
	 */
	public void removeMainBundleFromSessionCart() throws CommerceCartModificationException;

	/**
	 * Returns the category chosen by the current user.
	 * 
	 * @return {@link CategoryData}
	 */
	public CategoryData getSelectedInsuranceCategory();
}
