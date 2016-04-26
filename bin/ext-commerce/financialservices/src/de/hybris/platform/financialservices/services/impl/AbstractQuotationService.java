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
package de.hybris.platform.financialservices.services.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.quotation.QuotationItemRequestData;
import de.hybris.platform.commercefacades.quotation.QuotationItemResponseData;
import de.hybris.platform.commercefacades.quotation.QuotationRequestData;
import de.hybris.platform.commercefacades.quotation.QuotationResponseData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.financialservices.model.InsuranceQuoteModel;
import de.hybris.platform.financialservices.services.QuotationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.subscriptionservices.model.OneTimeChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionProductModel;
import de.hybris.platform.subscriptionservices.price.SubscriptionCommercePriceService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Lists;


/**
 * Abstract class which implements the <QuotationService> interface so that it can be extended by different, more
 * specialised quotation services.
 */
public abstract class AbstractQuotationService implements QuotationService
{
	private SessionService sessionService;
	private SubscriptionCommercePriceService subscriptionCommercePriceService;
	private ProductService productService;
	private CartService cartService;

	private static final String TERM_FREQUENCY = "termOfServiceFrequency";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public QuotationRequestData createQuotationRequestData(final String providerName, final String itemId,
			final InsuranceQuoteModel quote)
	{
		validateParameterNotNullStandardMessage("Quotation ProviderName", providerName);
		validateParameterNotNullStandardMessage("Quotation Item code", itemId);

		final QuotationRequestData requestData = buildQuotationRequest(providerName, itemId, quote);

		return requestData;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double getPrice(final QuotationResponseData responseData, final String itemId, final boolean isPaynow)
	{
		validateParameterNotNullStandardMessage("QuotationResponseData", responseData);
		validateParameterNotNullStandardMessage("Quotation Item code", itemId);

		Double payNowPrice = 0d;

		final QuotationItemResponseData quotationItem = getQuotationItem(responseData, itemId);

		if (quotationItem != null)
		{
			if (isPaynow)
			{
				payNowPrice = StringUtils.isNotEmpty(quotationItem.getPayNowPrice()) ? new Double(quotationItem.getPayNowPrice())
						: payNowPrice;
			}
			else
			{
				payNowPrice = StringUtils.isNotEmpty(quotationItem.getRecurringPrice()) ? new Double(
						quotationItem.getRecurringPrice()) : payNowPrice;
			}
		}

		return payNowPrice;
	}


	/**
	 * Helper method to extract the QuotationItemResponseData from QuotationResponseData for given itemId
	 *
	 * @param responseData
	 * @param itemId
	 * @return QuotationItemResponseData
	 */
	protected QuotationItemResponseData getQuotationItem(final QuotationResponseData responseData, final String itemId)
	{
		QuotationItemResponseData quotationItem = null;

		if (responseData.getItems() != null)
		{
			for (final QuotationItemResponseData data : responseData.getItems())
			{
				if (data.getId().equals(itemId))
				{
					quotationItem = data;
					break;
				}
			}
		}

		return quotationItem;
	}

	/**
	 * Method used to build the QuotationRequestData based on request parameters.
	 *
	 * @param providerName
	 * @param itemId
	 * @return QuotationRequestData with mandatory information to the quote engine.
	 */
	protected QuotationRequestData buildQuotationRequest(final String providerName, final String itemId,
			final InsuranceQuoteModel quote)
	{
		final QuotationRequestData requestData = new QuotationRequestData();
		final List<QuotationItemRequestData> productRequestDataList = Lists.newArrayList();
		final QuotationItemRequestData productRequestData = new QuotationItemRequestData();

		//populating requested items
		productRequestData.setId(itemId);
		productRequestData.setProperties(populateItemProperteis(itemId));
		productRequestDataList.add(productRequestData);

		//add parameters to the request data.
		requestData.setProviderName(providerName);
		requestData.setProperties(populateCommonProperties(quote));

		requestData.setItems(productRequestDataList);

		return requestData;

	}

	/**
	 * Helper method used to populate all the coverage corresponding to the given product code.
	 *
	 * @param itemId
	 * @return returns Map contains all the coverage as (key=billingEvent value=coverageValue)
	 */
	protected Map<String, String> populateItemProperteis(final String itemId)
	{
		final ProductModel product = getProductService().getProductForCode(itemId);
		final Map<String, String> coverageProperties = new HashMap<String, String>();

		if (product instanceof SubscriptionProductModel)
		{

			final SubscriptionProductModel subscriptionProduct = (SubscriptionProductModel) product;
			final SubscriptionPricePlanModel pricePlan = getSubscriptionCommercePriceService().getSubscriptionPricePlanForProduct(
					subscriptionProduct);

			if (subscriptionProduct.getSubscriptionTerm() != null
					&& subscriptionProduct.getSubscriptionTerm().getBillingPlan() != null)
			{
				coverageProperties.put(TERM_FREQUENCY, subscriptionProduct.getSubscriptionTerm().getBillingPlan()
						.getBillingFrequency().getCode());
			}
			final Collection<OneTimeChargeEntryModel> oneTimeChargeList = pricePlan.getOneTimeChargeEntries();


			for (final OneTimeChargeEntryModel oneTimeChargeEntry : oneTimeChargeList)
			{
				coverageProperties
						.put(oneTimeChargeEntry.getBillingEvent().getCode(), Double.toString(oneTimeChargeEntry.getPrice()));
			}
		}

		return coverageProperties;

	}

	protected abstract Map<String, String> populateCommonProperties(final InsuranceQuoteModel quote);


	public SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}


	protected SubscriptionCommercePriceService getSubscriptionCommercePriceService()
	{
		return subscriptionCommercePriceService;
	}

	@Required
	public void setSubscriptionCommercePriceService(final SubscriptionCommercePriceService subscriptionCommercePriceService)
	{
		this.subscriptionCommercePriceService = subscriptionCommercePriceService;
	}

	protected ProductService getProductService()
	{
		return productService;
	}

	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
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
