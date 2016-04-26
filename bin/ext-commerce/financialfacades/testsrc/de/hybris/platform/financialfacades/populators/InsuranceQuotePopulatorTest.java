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
package de.hybris.platform.financialfacades.populators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.quotation.InsuranceQuoteData;
import de.hybris.platform.financialservices.enums.QuoteBindingState;
import de.hybris.platform.financialservices.enums.QuoteWorkflowStatus;
import de.hybris.platform.financialservices.model.InsuranceQuoteModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;


@UnitTest
public class InsuranceQuotePopulatorTest
{
	@InjectMocks
	private InsuranceQuotePopulator insuranceQuotePopulator;

	private final String displayDateFormat = "yyyy-MM-dd";

	@Before
	public void setup()
	{
		insuranceQuotePopulator = new InsuranceQuotePopulator();
		insuranceQuotePopulator.setDateFormatForDisplay(displayDateFormat);
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldPopulateData()
	{
		final InsuranceQuoteModel model = new InsuranceQuoteModel();
		final InsuranceQuoteData data = new InsuranceQuoteData();
		final String quoteId = "testQuoteId";
		final Date date = new Date();
		model.setQuoteId(quoteId);
		model.setExpiryDate(date);
		model.setStartDate(date);
        model.setQuoteWorkflowStatus(QuoteWorkflowStatus.APPROVED);
		final SimpleDateFormat format = new SimpleDateFormat();
		format.applyPattern(displayDateFormat);
		model.setState(QuoteBindingState.BIND);
		insuranceQuotePopulator.populate(model, data);
		Assert.assertEquals(quoteId, data.getQuoteId());
		Assert.assertEquals(date, data.getExpiryDate());
		Assert.assertEquals(date, data.getStartDate());
		Assert.assertEquals(format.format(date), data.getFormattedExpiryDate());
		Assert.assertEquals(format.format(date), data.getFormattedStartDate());
		Assert.assertEquals(QuoteBindingState.BIND, data.getState());
        Assert.assertEquals(QuoteWorkflowStatus.APPROVED, data.getQuoteWorkflowStatus());
	}

	@Test
	public void shouldHandlePopulateNullDates()
	{
		final InsuranceQuoteModel model = new InsuranceQuoteModel();
		final InsuranceQuoteData data = new InsuranceQuoteData();
		final String quoteId = "testQuoteId";
		model.setQuoteId(quoteId);
		model.setState(QuoteBindingState.BIND);
		insuranceQuotePopulator.populate(model, data);
		Assert.assertEquals(quoteId, data.getQuoteId());
		Assert.assertEquals(null, data.getExpiryDate());
		Assert.assertEquals(null, data.getStartDate());
		Assert.assertEquals(null, data.getFormattedExpiryDate());
		Assert.assertEquals(null, data.getFormattedStartDate());
		Assert.assertEquals(null, data.getQuoteWorkflowStatus());
		Assert.assertEquals(QuoteBindingState.BIND, data.getState());
	}
}
