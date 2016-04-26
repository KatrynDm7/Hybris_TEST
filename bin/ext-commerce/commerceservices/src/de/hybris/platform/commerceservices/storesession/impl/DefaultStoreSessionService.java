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
package de.hybris.platform.commerceservices.storesession.impl;

import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.i18n.LanguageResolver;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.storesession.StoreSessionService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


public class DefaultStoreSessionService implements StoreSessionService
{
	private static final Logger LOG = Logger.getLogger(DefaultStoreSessionService.class);

	private LanguageResolver languageResolver;
	private CommerceCommonI18NService commerceCommonI18NService;
	private CommonI18NService commonI18NService;
	private CommerceCartService commerceCartService;
	private CartService cartService;

	@Override
	public void setCurrentLanguage(final String isocode)
	{
		final LanguageModel languageModel = getLanguageResolver().getLanguage(isocode);
		getCommonI18NService().setCurrentLanguage(languageModel);
	}

	@Override
	public void setCurrentCurrency(final String isocode)
	{
		Collection<CurrencyModel> currencies = getCommerceCommonI18NService().getAllCurrencies();
		if (currencies.isEmpty())
		{
			LOG.debug("No supported currencies found for the current site, look for all session currencies instead.");
			currencies = getCommonI18NService().getAllCurrencies();
		}
		Assert.notEmpty(currencies,
				"No supported currencies found for the current site. Please create currency for proper base store.");
		CurrencyModel currencyModel = null;
		for (final CurrencyModel currency : currencies)
		{
			if (StringUtils.equals(currency.getIsocode(), isocode))
			{
				currencyModel = currency;
			}
		}
		Assert.notNull(currencyModel, "Currency to set is not supported.");


		if (getCommonI18NService().getCurrentCurrency() != null)
		{
			if (!getCommonI18NService().getCurrentCurrency().getIsocode().equals(currencyModel.getIsocode()))
			{
				getCommonI18NService().setCurrentCurrency(currencyModel);
			}
		}
		else
		{
			getCommonI18NService().setCurrentCurrency(currencyModel);
		}

		if (getCartService().hasSessionCart())
		{
			final CartModel cart = getCartService().getSessionCart();
			try
			{
				if (cart.getCurrency() != null)
				{
					/*
					 * removed following condition: if (!cart.getCurrency().equals(currencyModel)) because after currency
					 * change, items in cart wasn't recalculated (cart's currency was changed by
					 * SessionCurrencyChangeListener)
					 */
					cart.setCurrency(currencyModel);
					final CommerceCartParameter parameter = new CommerceCartParameter();
					parameter.setEnableHooks(true);
					parameter.setCart(cart);
					getCommerceCartService().recalculateCart(parameter);
				}
				else
				{
					cart.setCurrency(currencyModel);
					final CommerceCartParameter parameter = new CommerceCartParameter();
					parameter.setEnableHooks(true);
					parameter.setCart(cart);
					getCommerceCartService().recalculateCart(parameter);
				}
			}
			catch (final CalculationException e)
			{
				LOG.warn("Could not recalculate the session cart.");
			}
		}
	}

	protected LanguageResolver getLanguageResolver()
	{
		return languageResolver;
	}

	@Required
	public void setLanguageResolver(final LanguageResolver languageResolver)
	{
		this.languageResolver = languageResolver;
	}

	protected CommerceCommonI18NService getCommerceCommonI18NService()
	{
		return commerceCommonI18NService;
	}

	@Required
	public void setCommerceCommonI18NService(final CommerceCommonI18NService commerceCommonI18NService)
	{
		this.commerceCommonI18NService = commerceCommonI18NService;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	protected CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}

	@Required
	public void setCommerceCartService(final CommerceCartService commerceCartService)
	{
		this.commerceCartService = commerceCartService;
	}

	protected CartService getCartService()
	{
		return cartService;
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}
}
