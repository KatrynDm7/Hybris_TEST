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
package de.hybris.platform.sap.sapordermgmtservices.cart.impl;

import de.hybris.platform.b2bacceleratorfacades.order.data.B2BCostCenterData;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BPaymentTypeData;
import de.hybris.platform.b2bacceleratorservices.enums.CheckoutPaymentType;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.sap.sapordermgmtbol.transaction.businessobject.interf.Basket;
import de.hybris.platform.sap.sapordermgmtservices.bolfacade.BolCartFacade;
import de.hybris.platform.sap.sapordermgmtservices.cart.CartCheckoutBaseService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;


/**
 *
 */
public class DefaultCartCheckoutBaseService implements CartCheckoutBaseService
{

	private BolCartFacade bolCartFacade;
	private Converter<Basket, CartData> cartConverter;
	private MessageSource messageSource;
	private I18NService i18nService;

	public MessageSource getMessageSource()
	{
		return messageSource;
	}

	@Required
	public void setMessageSource(final MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	public I18NService getI18nService()
	{
		return i18nService;
	}

	@Required
	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}

	@Override
	public CartData getSessionCart()
	{
		return compileSessionCart(false);
	}

	/**
	 * Creates the session cart from the BOL cart representation, and returns it in hybris representation. In case the
	 * cart is not initialized yet (i.e. if no back end call has taken place), an empty hybris cart is returned.
	 *
	 * @param recentlyAddedFirst
	 * @return Cart in hybris format
	 */
	protected CartData compileSessionCart(final boolean recentlyAddedFirst)
	{

		final Basket currentCart = bolCartFacade.getCart();
		synchronized (currentCart)
		{

			CartData cartData = null;
			if (currentCart.isInitialized())
			{
				cartData = getCartConverter().convert(currentCart);
			}
			else
			{
				return createEmptyCart();
			}
			final B2BPaymentTypeData paymentType = new B2BPaymentTypeData();
			paymentType.setCode(CheckoutPaymentType.ACCOUNT.getCode().toLowerCase());
			paymentType.setDisplayName(getMessageSource().getMessage("sap.paymenttype.account", null,
					getI18nService().getCurrentLocale()));
			cartData.setPaymentType(paymentType);

			// Set a mandatory B2B cost center
			cartData.setCostCenter(new B2BCostCenterData());

			cartData.setQuoteAllowed(Boolean.FALSE);

			if (recentlyAddedFirst)
			{
				this.reverseCartSorting(cartData);
			}

			return cartData;
		}
	}

	@Override
	public CartData getSessionCart(final boolean recentlyAddedFirst)
	{
		return compileSessionCart(recentlyAddedFirst);
	}

	/**
	 * Creates an empty cart, just initializing the entry list
	 *
	 * @return Empty cart
	 */
	protected CartData createEmptyCart()
	{
		final CartData cart = new CartData();
		cart.setEntries(Collections.<OrderEntryData> emptyList());
		return cart;
	}


	@Override
	public boolean hasSessionCart()
	{
		final Basket currentCart = bolCartFacade.getCart();
		synchronized (currentCart)
		{
			return bolCartFacade.hasCart().booleanValue();
		}
	}

	@Override
	public void removeSessionCart()
	{
		final Basket currentCart = bolCartFacade.getCart();
		synchronized (currentCart)
		{
			bolCartFacade.releaseCart();
		}

	}

	/**
	 * Reverse the sorting of the ItemList of the Cart
	 *
	 * @param cart
	 *           the Cart object
	 */
	protected void reverseCartSorting(final Basket cart)
	{
		Collections.reverse(cart.getItemList());
	}

	/**
	 * Reverse the sorting of the ItemList of the Cart
	 *
	 * @param cart
	 *           the Cart object
	 */
	protected void reverseCartSorting(final CartData cart)
	{
		Collections.reverse(cart.getEntries());
	}

	/**
	 * @return the cartConverter
	 */
	public Converter<Basket, CartData> getCartConverter()
	{
		return cartConverter;
	}

	/**
	 * @param cartConverter
	 *           the cartConverter to set
	 */
	public void setCartConverter(final Converter<Basket, CartData> cartConverter)
	{
		this.cartConverter = cartConverter;
	}

	/**
	 * @return the bolCartFacade
	 */
	public BolCartFacade getBolCartFacade()
	{
		return bolCartFacade;
	}

	/**
	 * @param bolCartFacade
	 *           the bolCartFacade to set
	 */
	public void setBolCartFacade(final BolCartFacade bolCartFacade)
	{
		this.bolCartFacade = bolCartFacade;
	}



}
