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
package de.hybris.platform.financialfacades.facades.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.order.SaveCartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.CartRestorationData;
import de.hybris.platform.commercefacades.order.data.CommerceSaveCartParameterData;
import de.hybris.platform.commercefacades.order.data.CommerceSaveCartResultData;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commerceservices.order.CommerceCartMergingException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.configurablebundlefacades.order.impl.DefaultBundleCartFacade;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.financialfacades.facades.InsuranceCartFacade;
import de.hybris.platform.financialfacades.facades.InsuranceQuoteFacade;
import de.hybris.platform.financialfacades.facades.QuotationPricingFacade;
import de.hybris.platform.financialfacades.strategies.InsuranceAddToCartStrategy;
import de.hybris.platform.financialservices.enums.QuoteBindingState;
import de.hybris.platform.financialservices.model.InsuranceQuoteModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Maps;


public class DefaultInsuranceCartFacade extends DefaultBundleCartFacade implements InsuranceCartFacade
{

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultInsuranceCartFacade.class);
	public static final String SESSION_CART_PARAMETER_NAME = "cart";

	private List<InsuranceAddToCartStrategy> addToCartStrategies;
	private QuotationPricingFacade quotationFacade;
	private InsuranceQuoteFacade insuranceQuoteFacade;
	private SaveCartFacade saveCartFacade;
	private UserFacade userFacade;
	private SessionService sessionService;
	private Converter<CategoryModel, CategoryData> categoryConverter;

	@Override
	public List<CartModificationData> addToCart(final String productCode, final long quantity, final int bundleNo,
			final String bundleTemplateId, final boolean removeCurrentProducts) throws CommerceCartModificationException
	{
		final List<CartModificationData> cartModificationData = super.addToCart(productCode, quantity, bundleNo, bundleTemplateId,
				removeCurrentProducts);
        
        getInsuranceQuoteFacade().createQuoteInSessionCart();
        
		final Map<String, Object> properties = Maps.newHashMap();
		properties.put("productCode", productCode);
		properties.put("bundleNo", bundleNo);

		processAddToCartStrategies(properties);

		if (hasSessionCart())
		{
			recalculateCart(getCartService().getSessionCart());
		}

		return cartModificationData;
	}

	@Override
	public CommerceSaveCartResultData saveCurrentUserCart() throws CommerceSaveCartException
	{
		if (canCurrentUserCartBeSaved())
		{
			final CommerceSaveCartResultData commerceSaveCartResultData = getSaveCartFacade().saveCart(
					new CommerceSaveCartParameterData());
			LOG.debug("Cart [" + commerceSaveCartResultData.getSavedCartData().getCode() + "] saved.");
			return commerceSaveCartResultData;
		}
		else
		{
			LOG.debug("No need to save cart, because is anonymous user/no session cart/cart no entries/no quote/already saved cart.");
		}

		return null;
	}

	/*
	 * Get saved carts for the current user.
	 */
	@Override
	public List<CartData> getSavedCartsForCurrentUser()
	{
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(0);
		pageableData.setPageSize(Integer.MAX_VALUE);
		final List<OrderStatus> statuses = new ArrayList<>();
		final SearchPageData<CartData> searchPageData = getSaveCartFacade().getSavedCartsForCurrentUser(pageableData, statuses);
		return searchPageData.getResults();
	}

	/**
	 * Remove cart from session.
	 */
	@Override
	public void removeSessionCart()
	{
		final CartModel sessionCart = getCartService().hasSessionCart() ? getCartService().getSessionCart() : null;
		if (sessionCart != null)
		{
			final boolean isSavedCart = isSavedCart(sessionCart.getCode());
			if (!isSavedCart)
			{
				// Temporary solution until below changes.
				// Later on should extend to use "cartFacade.deleteCartBundle(bundle);" however that would require improvements
				// from the facade to cater the case where bundleNo is 0 for the potential products.  At moment it simply
				// throws exception for bundleNo 0. (As the bundleNo 0 only exists in CartData for potential product but not
				// actually in the model.)
				super.removeSessionCart();
			}
			else
			{
				getSessionService().removeAttribute(SESSION_CART_PARAMETER_NAME);
			}
		}
	}

	/**
	 * Clear all items in the session cart.
	 */
	@Override
	public void removeMainBundleFromSessionCart() throws CommerceCartModificationException
	{
		if (getCartService().hasSessionCart())
		{
			final CartModel cart = getCartService().getSessionCart();
			if (!cart.getEntries().isEmpty())
			{
				if (cart.getEntries().get(0).getBundleNo() != null)
				{
					super.deleteCartBundle(cart.getEntries().get(0).getBundleNo());
				}
			}
		}
	}

	/**
	 * Check if the sessionCart is from the saved cart.
	 */
	@Override
	public boolean isSavedCart(final String sessionCartCode)
	{
		boolean isSavedCart = Boolean.FALSE;
		for (final CartData savedCartData : getSavedCartsForCurrentUser())
		{
			if (savedCartData.getCode().equals(sessionCartCode))
			{
				isSavedCart = Boolean.TRUE;
				break;
			}
		}
		return isSavedCart;
	}

	/**
	 * My cart is only saved if There is a cart in session. The cart contains an insurance quote object. The current user
	 * is logged in. The current session cart haven't been saved.
	 *
	 * @return boolean
	 */
	protected boolean canCurrentUserCartBeSaved()
	{
		final boolean result = !getUserFacade().isAnonymousUser() && hasSessionCart() && hasEntries()
				&& getCartService().getSessionCart().getInsuranceQuote() != null;

		if (!result)
		{
			return false;
		}

		final CartModel sessionCart = getCartService().getSessionCart();

		return sessionCart.getSaveTime() == null;
	}

	/**
	 * Set the visited step to the quote in the current cart.
	 */
	@Override
	public void setVisitedStepToQuoteInCurrentCart(final String progressStepId)
	{
		final CartModel cart = getCartService().hasSessionCart() ? getCartService().getSessionCart() : null;

		if (cart != null)
		{
			final InsuranceQuoteModel quote = cart.getInsuranceQuote();
			if (quote != null)
			{
				if (quote.getQuoteVisitiedStepList() == null || !quote.getQuoteVisitiedStepList().contains(progressStepId))
				{
					final List<String> visitedStepList = new ArrayList<String>();
					if (quote.getQuoteVisitiedStepList() != null)
					{
						visitedStepList.addAll(quote.getQuoteVisitiedStepList());
					}
					visitedStepList.add(progressStepId);
					quote.setQuoteVisitiedStepList(visitedStepList);
					getModelService().save(quote);
				}
			}
		}
	}

	/**
	 * Check the isVisited status for the step against the quote from the current cart
	 *
	 * @param progressStepId
	 */
	@Override
	public boolean checkStepIsVisitedFromQuoteInCurrentCart(final String progressStepId)
	{
		final CartModel cart = getCartService().hasSessionCart() ? getCartService().getSessionCart() : null;

		if (cart != null)
		{
			final InsuranceQuoteModel quote = cart.getInsuranceQuote();
			if (quote != null)
			{
				if (quote.getQuoteVisitiedStepList() != null && quote.getQuoteVisitiedStepList().contains(progressStepId))
				{
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	/**
	 * Get the quote state from the current cart.
	 */
	@Override
	public boolean checkStepIsEnabledFromQuoteInCurrentCart()
	{
		final CartModel cart = getCartService().hasSessionCart() ? getCartService().getSessionCart() : null;
		if (cart != null && cart.getInsuranceQuote() != null)
		{
			final QuoteBindingState state = cart.getInsuranceQuote().getState();
			if (QuoteBindingState.BIND.equals(state))
			{
				return Boolean.FALSE;
			}
			else
			{
				return Boolean.TRUE;
			}
		}
		return Boolean.TRUE;
	}

	/**
	 * Unbind the quote object in the session cart.
	 */
	@Override
	public boolean unbindQuoteInSessionCart()
	{
		final CartModel cart = getCartService().hasSessionCart() ? getCartService().getSessionCart() : null;
		if (cart != null && cart.getInsuranceQuote() != null)
		{
			final QuoteBindingState state = cart.getInsuranceQuote().getState();
			if (QuoteBindingState.BIND.equals(state))
			{
				getInsuranceQuoteFacade().unbindQuote(cart.getInsuranceQuote());
				recalculateCart(cart);
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	/**
	 * Bind the quote object in the session cart.
	 */
	@Override
	public boolean bindQuoteInSessionCart()
	{
		final CartModel cart = getCartService().hasSessionCart() ? getCartService().getSessionCart() : null;
		if (cart != null && cart.getInsuranceQuote() != null)
		{
			final QuoteBindingState state = cart.getInsuranceQuote().getState();
			if (QuoteBindingState.UNBIND.equals(state))
			{
				getInsuranceQuoteFacade().bindQuote(cart.getInsuranceQuote());
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	protected void recalculateCart(final CartModel cart)
	{
		try
		{
			final CommerceCartParameter parameter = new CommerceCartParameter();
			parameter.setEnableHooks(true);
			parameter.setCart(cart);
			getCommerceCartService().recalculateCart(parameter);
		}
		catch (final CalculationException ex)
		{
			LOG.error("Failed to recalculate cart [" + cart.getCode() + "] during unbinding quote", ex);
		}
	}

	protected void processAddToCartStrategies(final Map<String, Object> properties)
	{
		for (final InsuranceAddToCartStrategy insuranceAddToCartStrategy : getAddToCartStrategies())
		{
			insuranceAddToCartStrategy.addToCart(properties);
		}
	}

	/**
	 * Check if the give product has the same default category as session cart product.
	 *
	 * @param productCode
	 *           product code
	 */
	@Override
	public boolean isSameInsuranceInSessionCart(final String productCode) throws InvalidCartException
	{
		if (hasSessionCart() && hasEntries())
		{
			final ProductModel product = getProductService().getProductForCode(productCode);
			final CartModel sessionCart = getCartService().getSessionCart();

			final ProductModel productInSessionCart = sessionCart.getEntries().get(0).getProduct();

			if (productInSessionCart.getDefaultCategory() != null && product.getDefaultCategory() != null)
			{
				return productInSessionCart.getDefaultCategory().getCode().equals(product.getDefaultCategory().getCode());
			}
			else
			{
				return false;
			}
		}
		else
		{
			throw new InvalidCartException("No valid session cart");
		}
	}

	/**
	 * Restore cart with code for current user and the site.
	 */
	@Override
	public void restoreCart(final String code, final Boolean isSaveCurrentCart) throws CommerceSaveCartException
	{
		if (isSaveCurrentCart)
		{
			saveCurrentUserCart();
		}
		final CommerceSaveCartParameterData saveCartParameterData = new CommerceSaveCartParameterData();
		saveCartParameterData.setCartId(code);
		getSaveCartFacade().restoreSavedCart(saveCartParameterData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.order.impl.DefaultCartFacade#restoreCartAndMerge(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public CartRestorationData restoreCartAndMerge(final String fromUserCartGuid, final String toUserCartGuid)
			throws CommerceCartRestorationException, CommerceCartMergingException
	{
		final CartRestorationData restoreCartAndMerge = super.restoreCartAndMerge(fromUserCartGuid, toUserCartGuid);
		if (restoreCartAndMerge != null)
		{
			try
			{
				saveCurrentUserCart();
			}
			catch (final CommerceSaveCartException e)
			{
				LOG.error("Unable to save newly restored cart : " + e);
			}
		}

		return restoreCartAndMerge;
	}

	protected List<InsuranceAddToCartStrategy> getAddToCartStrategies()
	{
		return addToCartStrategies;
	}

	@Required
	public void setAddToCartStrategies(final List<InsuranceAddToCartStrategy> addToCartStrategies)
	{
		this.addToCartStrategies = addToCartStrategies;
	}

	protected QuotationPricingFacade getQuotationFacade()
	{
		return quotationFacade;
	}

	@Required
	public void setQuotationFacade(final QuotationPricingFacade quotationFacade)
	{
		this.quotationFacade = quotationFacade;
	}

	protected InsuranceQuoteFacade getInsuranceQuoteFacade()
	{
		return insuranceQuoteFacade;
	}

	@Required
	public void setInsuranceQuoteFacade(final InsuranceQuoteFacade insuranceQuoteFacade)
	{
		this.insuranceQuoteFacade = insuranceQuoteFacade;
	}

	protected SaveCartFacade getSaveCartFacade()
	{
		return saveCartFacade;
	}

	@Required
	public void setSaveCartFacade(final SaveCartFacade saveCartFacade)
	{
		this.saveCartFacade = saveCartFacade;
	}

	protected UserFacade getUserFacade()
	{
		return userFacade;
	}

	@Required
	public void setUserFacade(final UserFacade userFacade)
	{
		this.userFacade = userFacade;
	}

	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @return the categoryConverter
	 */
	public Converter<CategoryModel, CategoryData> getCategoryConverter()
	{
		return categoryConverter;
	}

	/**
	 * @param categoryConverter
	 *           the categoryConverter to set
	 */
	public void setCategoryConverter(final Converter<CategoryModel, CategoryData> categoryConverter)
	{
		this.categoryConverter = categoryConverter;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	@Override
	public CategoryData getSelectedInsuranceCategory()
	{
		if (getCartService().hasSessionCart())
		{
			final CartModel cart = getCartService().getSessionCart();

			if (!cart.getEntries().isEmpty())
			{
				final ProductModel product = cart.getEntries().iterator().next().getProduct();
				return categoryConverter.convert(product.getDefaultCategory());
			}
		}

		return null;
	}
}
