/*
 *
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
 */
package de.hybris.platform.financialfacades.facades.impl;

import de.hybris.platform.commercefacades.insurance.data.InsuranceQuoteListingData;
import de.hybris.platform.commercefacades.insurance.data.QuoteItemRequestData;
import de.hybris.platform.commercefacades.insurance.data.QuoteRequestData;
import de.hybris.platform.commercefacades.insurance.data.QuoteResponseData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.quotation.InsuranceQuoteData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.financialfacades.facades.InsuranceQuoteFacade;
import de.hybris.platform.financialfacades.facades.QuoteServiceFacade;
import de.hybris.platform.financialfacades.services.InsuranceQuoteService;
import de.hybris.platform.financialservices.enums.QuoteBindingState;
import de.hybris.platform.financialservices.enums.QuoteWorkflowStatus;
import de.hybris.platform.financialservices.model.InsuranceQuoteModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Lists;

public class DefaultInsuranceQuoteFacade implements InsuranceQuoteFacade
{

	private ModelService modelService;
	private CartService cartService;
	private InsuranceQuoteService quoteService;
	private QuoteServiceFacade quoteServiceFacade;
	private Converter<CartData, InsuranceQuoteListingData> insuranceQuoteListingConverter;

	/**
	 * When the customer selects get Quote, a quote object is created and put it in session cart. This object can be
	 * viewed via the admin interface by browsing to the cart type and then viewing the associated cart object. On first
	 * creation, the state will be non-binding.
	 */
	@Override
	public void createQuoteInSessionCart()
	{
		if (getCartService().hasSessionCart())
		{
			final CartModel sessionCart = getCartService().getSessionCart();
			if (sessionCart.getInsuranceQuote() == null)
			{
				final InsuranceQuoteModel quote = getModelService().create(InsuranceQuoteModel.class);
				quote.setState(QuoteBindingState.UNBIND);
				getModelService().save(quote);

				sessionCart.setInsuranceQuote(quote);
				getModelService().save(sessionCart);
			}
		}
	}

	/**
	 * Unbind quote for the quote
	 */
	@Override
	public InsuranceQuoteModel unbindQuote(final InsuranceQuoteModel quoteModel)
	{
		ServicesUtil.validateParameterNotNull(quoteModel, "quoteModel cannot be null");

		getQuoteService().updateQuoteByBindingState(quoteModel, QuoteBindingState.UNBIND);
		getModelService().save(quoteModel);
		return quoteModel;
	}

	/**
	 * Bind quote for the quote
	 *
	 * @param quoteModel
	 */
	@Override
	public InsuranceQuoteModel bindQuote(final InsuranceQuoteModel quoteModel)
	{
		ServicesUtil.validateParameterNotNull(quoteModel, "quoteModel cannot be null");

		getQuoteService().updateQuoteByBindingState(quoteModel, QuoteBindingState.BIND);
		getModelService().save(quoteModel);
		return quoteModel;
	}

	/**
	 * Get quote state from the session cart
	 */
	@Override
	public QuoteBindingState getQuoteStateFromSessionCart()
	{
		if (getCartService().hasSessionCart())
		{
			final CartModel sessionCart = getCartService().getSessionCart();
			if (sessionCart.getInsuranceQuote() != null)
			{
				return sessionCart.getInsuranceQuote().getState();
			}
		}
		return null;
	}

	/**
	 * Sort the cart data list based on the quote listing
	 *
	 * @param cartDataList
	 */
	@Override
	public List<InsuranceQuoteListingData> sortCartListForQuoteListing(final List<CartData> cartDataList)
	{
		ServicesUtil.validateParameterNotNull(cartDataList, "The parameter cartDataList cannot be null.");
		final List<InsuranceQuoteListingData> bindQuotes = new ArrayList<>();
		final List<InsuranceQuoteListingData> unbindQuotes = new ArrayList<>();
		final List<InsuranceQuoteListingData> expiredQuotes = new ArrayList<>();

		for (final CartData cartData : cartDataList)
		{
			final InsuranceQuoteData quoteData = cartData.getInsuranceQuote();
			if (quoteData != null)
			{
				final InsuranceQuoteListingData listingData = getInsuranceQuoteListingConverter().convert(cartData);
                listingData.setQuoteWorkflowStatus(getQuoteWorkflowStatus(cartData.getCode()));
				if (getCartService().hasSessionCart())
				{
					final CartModel sessionCart = getCartService().getSessionCart();
					listingData.setCurrentCartCode(sessionCart.getCode());
				}

				if (listingData.getQuoteIsExpired())
				{
					expiredQuotes.add(listingData);
				}
				else
				{
					if (QuoteBindingState.BIND.equals(quoteData.getState()))
					{
						bindQuotes.add(listingData);
					}
					else
					{
						unbindQuotes.add(listingData);
					}
				}
			}
		}
		final List<InsuranceQuoteListingData> sortedQuoteListingData = new ArrayList<>();
		sortedQuoteListingData.addAll(sortQuoteListOnExpireDate(bindQuotes));
		sortedQuoteListingData.addAll(unbindQuotes);
		sortedQuoteListingData.addAll(sortQuoteListOnExpireDate(expiredQuotes));
		return sortedQuoteListingData;
	}

	/**
	 * Sort the quote list on the expire date, where the nearest expire date, i.e. closest to the current date, should be
	 * put before the later expiring date. Whereas the items with null expiring date will be put at last of the list.
	 * */
	protected List<InsuranceQuoteListingData> sortQuoteListOnExpireDate(final List<InsuranceQuoteListingData> quoteListingDataList)
	{
		if (CollectionUtils.isNotEmpty(quoteListingDataList))
		{
			Collections.sort(quoteListingDataList, new Comparator<InsuranceQuoteListingData>()
			{
				@Override
				public int compare(final InsuranceQuoteListingData data1, final InsuranceQuoteListingData data2)
				{
					if (data1.getQuoteRawExpiryDate() != null && data2.getQuoteRawExpiryDate() != null)
					{
						return (data1.getQuoteRawExpiryDate()).compareTo(data2.getQuoteRawExpiryDate());
					}
					return 0;
				}
			});
		}
		return quoteListingDataList;
	}

	/**
	 * Get the quote workflow type.
	 */
	@Override
	public String getQuoteWorkflowType()
	{
		if (getCartService().hasSessionCart())
		{
			final CartModel cart = getCartService().getSessionCart();
			if (!cart.getEntries().isEmpty() && cart.getEntries().get(0).getProduct() != null)
			{
				final QuoteRequestData requestData = buildQuoteRequestData(cart.getEntries().get(0).getProduct());

				final QuoteResponseData responseData = getQuoteServiceFacade().requestQuoteWorkFlowType(requestData);

				if (responseData.getItems() != null && !responseData.getItems().isEmpty())
				{
					return responseData.getItems().get(0).getId();
				}
			}
		}

		return null;
	}

	protected QuoteRequestData buildQuoteRequestData(final ProductModel product)
	{
		final QuoteRequestData requestData = new QuoteRequestData();
		final QuoteItemRequestData requestItem = new QuoteItemRequestData();
		final List<QuoteItemRequestData> requestDataList = Lists.newArrayList();

		if (product.getDefaultCategory() != null)
		{
			requestItem.setId(product.getDefaultCategory().getCode());
		}

		requestDataList.add(requestItem);
		requestData.setItems(requestDataList);

		return requestData;
	}

	@Override
	public QuoteWorkflowStatus getQuoteWorkflowStatus()
	{
		if (getCartService().hasSessionCart())
		{
			final CartModel cart = getCartService().getSessionCart();

            return getQuoteWorkflowStatus(cart.getCode());
		}

		return null;
	}
    
	protected QuoteWorkflowStatus getQuoteWorkflowStatus(final String codeCode)
	{
		final QuoteRequestData requestData = buildQuoteRequestData(codeCode);
		final QuoteResponseData responseData = getQuoteServiceFacade().requestQuoteWorkFlowStatus(requestData);

		if (responseData.getItems() != null && !responseData.getItems().isEmpty())
		{
			return QuoteWorkflowStatus.valueOf(responseData.getItems().get(0).getId());
		}
        
        return null;
	}
    
	protected QuoteRequestData buildQuoteRequestData(final String cartCode)
	{
		final QuoteRequestData requestData = new QuoteRequestData();
		final QuoteItemRequestData requestItem = new QuoteItemRequestData();
		final List<QuoteItemRequestData> requestDataList = Lists.newArrayList();

        requestItem.setId(cartCode);

		requestDataList.add(requestItem);
		requestData.setItems(requestDataList);

		return requestData;
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

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected InsuranceQuoteService getQuoteService()
	{
		return quoteService;
	}

	@Required
	public void setQuoteService(final InsuranceQuoteService quoteService)
	{
		this.quoteService = quoteService;
	}

	protected Converter<CartData, InsuranceQuoteListingData> getInsuranceQuoteListingConverter()
	{
		return insuranceQuoteListingConverter;
	}

	@Required
	public void setInsuranceQuoteListingConverter(
			final Converter<CartData, InsuranceQuoteListingData> insuranceQuoteListingConverter)
	{
		this.insuranceQuoteListingConverter = insuranceQuoteListingConverter;
	}

	protected QuoteServiceFacade getQuoteServiceFacade()
	{
		return quoteServiceFacade;
	}

	@Required
	public void setQuoteServiceFacade(final QuoteServiceFacade quoteServiceFacade)
	{
		this.quoteServiceFacade = quoteServiceFacade;
	}
}
