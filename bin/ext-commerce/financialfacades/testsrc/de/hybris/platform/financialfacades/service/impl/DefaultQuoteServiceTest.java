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
package de.hybris.platform.financialfacades.service.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.insurance.data.QuoteRequestData;
import de.hybris.platform.commercefacades.insurance.data.QuoteResponseData;
import de.hybris.platform.financialfacades.facades.QuoteServiceFacade;
import de.hybris.platform.financialfacades.services.impl.DefaultQuoteService;
import de.hybris.platform.financialfacades.strategies.ProcessQuoteResponseDataStrategy;
import de.hybris.platform.financialservices.enums.QuoteBindingState;
import de.hybris.platform.financialservices.model.InsuranceQuoteModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultQuoteServiceTest
{
	@InjectMocks
	private DefaultQuoteService defaultQuoteService;

	@Mock
	private QuoteServiceFacade quoteServiceFacade;

	@Mock
	private ProcessQuoteResponseDataStrategy processQuoteResponseDataStrategy;

	@Before
	public void setup()
	{
		defaultQuoteService = new DefaultQuoteService();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testUpdateQuoteByBindingStateUnbind()
	{
		final QuoteBindingState state = QuoteBindingState.UNBIND;
		final InsuranceQuoteModel quoteModel = new InsuranceQuoteModel();
		final QuoteResponseData responseData = new QuoteResponseData();
		Mockito.when(quoteServiceFacade.requestQuoteUnbind(Mockito.any(QuoteRequestData.class))).thenReturn(responseData);
		defaultQuoteService.updateQuoteByBindingState(quoteModel, state);
		Mockito.verify(quoteServiceFacade, Mockito.times(1)).requestQuoteUnbind(Mockito.any(QuoteRequestData.class));
		Mockito.verify(processQuoteResponseDataStrategy, Mockito.times(1)).processResponseData(quoteModel, responseData);
	}

	@Test
	public void testUpdateQuoteByBindingStateBind()
	{
		final QuoteBindingState state = QuoteBindingState.BIND;
		final InsuranceQuoteModel quoteModel = new InsuranceQuoteModel();
		final QuoteResponseData responseData = new QuoteResponseData();
		Mockito.when(quoteServiceFacade.requestQuoteBind(Mockito.any(QuoteRequestData.class))).thenReturn(responseData);
		defaultQuoteService.updateQuoteByBindingState(quoteModel, state);
		Mockito.verify(quoteServiceFacade, Mockito.times(1)).requestQuoteBind(Mockito.any(QuoteRequestData.class));
		Mockito.verify(processQuoteResponseDataStrategy, Mockito.times(1)).processResponseData(quoteModel, responseData);
	}
}
