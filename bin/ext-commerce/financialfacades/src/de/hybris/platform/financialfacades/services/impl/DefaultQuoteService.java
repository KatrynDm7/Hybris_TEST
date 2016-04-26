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
package de.hybris.platform.financialfacades.services.impl;

import de.hybris.platform.commercefacades.insurance.data.QuoteItemRequestData;
import de.hybris.platform.commercefacades.insurance.data.QuoteRequestData;
import de.hybris.platform.commercefacades.insurance.data.QuoteResponseData;
import de.hybris.platform.financialfacades.facades.QuoteServiceFacade;
import de.hybris.platform.financialfacades.services.InsuranceQuoteService;
import de.hybris.platform.financialfacades.strategies.ProcessQuoteResponseDataStrategy;
import de.hybris.platform.financialservices.enums.QuoteBindingState;
import de.hybris.platform.financialservices.model.InsuranceQuoteModel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public class DefaultQuoteService implements InsuranceQuoteService
{
	private QuoteServiceFacade quoteServiceFacade;
	private ProcessQuoteResponseDataStrategy processQuoteResponseDataStrategy;


	/**
	 * ProcessResponseData into quoteModel from the quoteResponseData
	 *
	 * @param quoteModel
	 * @param state
	 */
	@Override
	public InsuranceQuoteModel updateQuoteByBindingState(final InsuranceQuoteModel quoteModel, final QuoteBindingState state)
	{
		final QuoteRequestData requestData = new QuoteRequestData();
		final List<QuoteItemRequestData> quoteItemRequestList = new ArrayList<QuoteItemRequestData>();
		final QuoteItemRequestData itemRequestData = new QuoteItemRequestData();
		quoteItemRequestList.add(itemRequestData);
		itemRequestData.setId(quoteModel.getQuoteId());
		requestData.setItems(quoteItemRequestList);

		QuoteResponseData responseData;
		if (QuoteBindingState.UNBIND.equals(state))
		{
			responseData = getQuoteServiceFacade().requestQuoteUnbind(requestData);
			processResponseData(quoteModel, responseData);
		}

		if (QuoteBindingState.BIND.equals(state))
		{
			responseData = getQuoteServiceFacade().requestQuoteBind(requestData);
			processResponseData(quoteModel, responseData);
		}
		return quoteModel;
	}

	protected InsuranceQuoteModel processResponseData(final InsuranceQuoteModel quoteModel,
			final QuoteResponseData quoteResponseData)
	{
		return getProcessQuoteResponseDataStrategy().processResponseData(quoteModel, quoteResponseData);
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

	protected ProcessQuoteResponseDataStrategy getProcessQuoteResponseDataStrategy()
	{
		return processQuoteResponseDataStrategy;
	}

	@Required
	public void setProcessQuoteResponseDataStrategy(final ProcessQuoteResponseDataStrategy processQuoteResponseDataStrategy)
	{
		this.processQuoteResponseDataStrategy = processQuoteResponseDataStrategy;
	}
}
