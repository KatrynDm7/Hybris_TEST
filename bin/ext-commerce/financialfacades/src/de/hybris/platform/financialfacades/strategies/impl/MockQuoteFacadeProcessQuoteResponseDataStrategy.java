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
package de.hybris.platform.financialfacades.strategies.impl;

import de.hybris.platform.commercefacades.insurance.data.QuoteItemResponseData;
import de.hybris.platform.commercefacades.insurance.data.QuoteResponseData;
import de.hybris.platform.financialfacades.facades.impl.MockQuoteServiceFacade;
import de.hybris.platform.financialfacades.strategies.ProcessQuoteResponseDataStrategy;
import de.hybris.platform.financialservices.enums.QuoteBindingState;
import de.hybris.platform.financialservices.model.InsuranceQuoteModel;

import java.util.Date;
import java.util.Map;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Required;


public class MockQuoteFacadeProcessQuoteResponseDataStrategy implements ProcessQuoteResponseDataStrategy
{
	private String dateFormat;

	/**
	 * ProcessResponseData into quoteModel from the quoteResponseData
	 *
	 * @param quoteModel
	 * @param quoteResponseData
	 */
	@Override
	public InsuranceQuoteModel processResponseData(InsuranceQuoteModel quoteModel, QuoteResponseData quoteResponseData)
	{
		if (quoteResponseData.getItems() != null)
		{
			QuoteItemResponseData itemResponseData = quoteResponseData.getItems().get(0);
			if (itemResponseData.getProperties() != null)
			{
				Map<String, String> itemResponseDataMap = itemResponseData.getProperties();

				String id = itemResponseData.getId();
				final DateTimeFormatter formatter = DateTimeFormat.forPattern(getDateFormat());
				Date startDate = null;
				Date expiryDate = null;
				if (itemResponseData.getProperties().get(MockQuoteServiceFacade.START_DATE) != null)
				{
					startDate = formatter.parseDateTime(itemResponseDataMap.get(MockQuoteServiceFacade.START_DATE)).toDate();
				}
				if (itemResponseData.getProperties().get(MockQuoteServiceFacade.END_DATE) != null)
				{
					expiryDate = formatter.parseDateTime(itemResponseDataMap.get(MockQuoteServiceFacade.END_DATE)).toDate();
				}
				String quoteState = itemResponseDataMap.get(MockQuoteServiceFacade.QUOTE_STATE);

				quoteModel.setQuoteId(id);
				quoteModel.setStartDate(startDate);
				quoteModel.setExpiryDate(expiryDate);
				if (quoteState.equals(MockQuoteServiceFacade.QUOTE_STATE_BIND))
				{
					quoteModel.setState(QuoteBindingState.BIND);
				}
				else
				{
					quoteModel.setState(QuoteBindingState.UNBIND);
				}
			}
		}
		return quoteModel;
	}


	protected String getDateFormat()
	{
		return dateFormat;
	}

	@Required
	public void setDateFormat(String dateFormat)
	{
		this.dateFormat = dateFormat;
	}
}
