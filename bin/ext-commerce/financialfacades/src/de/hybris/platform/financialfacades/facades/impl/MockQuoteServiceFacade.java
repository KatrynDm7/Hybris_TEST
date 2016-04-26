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

import de.hybris.platform.commercefacades.insurance.data.QuoteItemRequestData;
import de.hybris.platform.commercefacades.insurance.data.QuoteItemResponseData;
import de.hybris.platform.commercefacades.insurance.data.QuoteRequestData;
import de.hybris.platform.commercefacades.insurance.data.QuoteResponseData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.financialfacades.facades.QuoteServiceFacade;
import de.hybris.platform.financialservices.enums.QuoteWorkflowStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;


public class MockQuoteServiceFacade implements QuoteServiceFacade
{
	public static final String START_DATE = "startDate";
	public static final String END_DATE = "endDate";
	public static final String QUOTE_STATE = "state";
	public static final String QUOTE_STATE_BIND = "bind";
	public static final String QUOTE_STATE_UNBIND = "unbind";
	protected static final int DAYS = 30;
	protected static final String MOCK_ID_PREFIX = "QREF";
	protected static final int MAX_ID_NUM = 999999;
	protected static final int MIN_ID_NUM = 100000;
	protected static final String CATEGORY_ID_LIFE = "insurances_life";
	protected static final String CATEGORY_LIFE = "LIFE";
	protected static final String CATEGORY_DEFAULT = "DEFAULT";

	private String dateFormat;
    
    @Autowired
    private FlexibleSearchService flexibleSearchService;

	/**
	 * Request to update a quote to a external system, and return to hybris the information about the switch from bind
	 * quote
	 *
	 * @param requestData
	 *           the quote request data
	 * @return quote response data
	 */
	@Override
	public QuoteResponseData requestQuoteUnbind(final QuoteRequestData requestData)
	{
		final QuoteResponseData responseData = new QuoteResponseData();
		final QuoteItemResponseData itemData = new QuoteItemResponseData();
		final Map<String, String> properties = new HashMap<String, String>();
		itemData.setId("");
		properties.put(START_DATE, createMockQuoteStartDate(itemData));
		properties.put(END_DATE, null);
		properties.put(QUOTE_STATE, QUOTE_STATE_UNBIND);
		itemData.setProperties(properties);
		final List<QuoteItemResponseData> responseDataList = new ArrayList<QuoteItemResponseData>();
		responseDataList.add(itemData);
		responseData.setItems(responseDataList);
		return responseData;
	}

	/**
	 * Request to update a quote to an external system, and return to hybris the information about an unbind quote quote
	 *
	 * @param requestData
	 *           the quote request data
	 * @return quote response data
	 */
	@Override
	public QuoteResponseData requestQuoteBind(final QuoteRequestData requestData)
	{
		final QuoteResponseData responseData = new QuoteResponseData();
		final QuoteItemResponseData itemData = new QuoteItemResponseData();
		final Map<String, String> properties = new HashMap<String, String>();
		itemData.setId(createMockQuoteId(itemData));
		properties.put(START_DATE, createMockQuoteStartDate(itemData));
		properties.put(END_DATE, createMockExpireDate(itemData));
		properties.put(QUOTE_STATE, QUOTE_STATE_BIND);
		itemData.setProperties(properties);
		final List<QuoteItemResponseData> responseDataList = new ArrayList<QuoteItemResponseData>();
		responseDataList.add(itemData);
		responseData.setItems(responseDataList);
		return responseData;
	}

	/**
	 * Request to get information about the Quote WorkFlow type based on the requested category id.
	 *
	 * @param requestData
	 *           the quote request data
	 * @return quote response data
	 */
	@Override
	public QuoteResponseData requestQuoteWorkFlowType(final QuoteRequestData requestData)
	{
		final QuoteResponseData responseData = new QuoteResponseData();
		final List<QuoteItemResponseData> responseDataList = new ArrayList<QuoteItemResponseData>();

		QuoteItemResponseData itemData = null;

		for (final QuoteItemRequestData quoteItem : requestData.getItems())
		{
			itemData = new QuoteItemResponseData();

			//If the category id is Life insurance (insurances_life)
			if (CATEGORY_ID_LIFE.equalsIgnoreCase(quoteItem.getId()))
			{
				itemData.setId(CATEGORY_LIFE);
			}
			else
			//else other than Life insurance type.
			{
				itemData.setId(CATEGORY_DEFAULT);
			}

			responseDataList.add(itemData);
		}

		responseData.setItems(responseDataList);

		return responseData;
	}

    @Override
    public QuoteResponseData requestQuoteWorkFlowStatus(QuoteRequestData requestData)
    {
		final QuoteResponseData responseData = new QuoteResponseData();
		final List<QuoteItemResponseData> responseDataList = new ArrayList<QuoteItemResponseData>();

		for (final QuoteItemRequestData quoteItem : requestData.getItems())
		{
            final QuoteItemResponseData itemData = new QuoteItemResponseData();

			if (StringUtils.isNotEmpty(quoteItem.getId()))
            {
                CartModel exampleCart = new CartModel();
                exampleCart.setCode(quoteItem.getId());
                List<CartModel> carts = flexibleSearchService.getModelsByExample(exampleCart);
                
                if (carts!= null && !carts.isEmpty() && !carts.get(0).getEntries().isEmpty()
                        && carts.get(0).getEntries().get(0).getProduct() != null 
                        && carts.get(0).getEntries().get(0).getProduct().getDefaultCategory() != null)
                {
                    if (CATEGORY_ID_LIFE.equalsIgnoreCase(carts.get(0).getEntries().get(0).getProduct().getDefaultCategory().getCode()))
                    {
                        if (carts.get(0).getInsuranceQuote() != null && carts.get(0).getInsuranceQuote().getQuoteWorkflowStatus() != null)
                        {
                            itemData.setId(carts.get(0).getInsuranceQuote().getQuoteWorkflowStatus().toString());
                        }
                    }
                    else
                    {
                        itemData.setId(QuoteWorkflowStatus.APPROVED.toString());
                    }
                }
                
            }

			responseDataList.add(itemData);
		}

		responseData.setItems(responseDataList);

		return responseData;
    }

    protected String createMockQuoteStartDate(final QuoteItemResponseData itemResponseData)
	{
		final SimpleDateFormat sdf = new SimpleDateFormat(getDateFormat());
		return sdf.format(new Date());
	}

	protected String createMockExpireDate(final QuoteItemResponseData itemResponseData)
	{
		final DateTime expireDate = DateTime.now().plusDays(DAYS);
		final SimpleDateFormat sdf = new SimpleDateFormat(getDateFormat());
		return sdf.format(expireDate.toDate());
	}

	protected String createMockQuoteId(final QuoteItemResponseData itemResponseData)
	{
		final Random rand = new Random();
		rand.setSeed(new Date().getTime());
		final int randomNum = rand.nextInt(MAX_ID_NUM) % (MAX_ID_NUM - MIN_ID_NUM + 1) + MIN_ID_NUM;
		return MOCK_ID_PREFIX + randomNum;
	}

	protected String getDateFormat()
	{
		return dateFormat;
	}

	@Required
	public void setDateFormat(final String dateFormat)
	{
		this.dateFormat = dateFormat;
	}

}
