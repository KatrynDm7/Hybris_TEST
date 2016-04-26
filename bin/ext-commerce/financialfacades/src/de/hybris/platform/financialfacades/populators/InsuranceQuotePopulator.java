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

import de.hybris.platform.commercefacades.quotation.InsuranceQuoteData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.financialservices.model.InsuranceQuoteModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Required;


public class InsuranceQuotePopulator<SOURCE extends InsuranceQuoteModel, TARGET extends InsuranceQuoteData> implements
		Populator<SOURCE, TARGET>
{
	private String dateFormatForDisplay;

	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException
	{
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
		simpleDateFormat.applyPattern(getDateFormatForDisplay());
		if (source.getExpiryDate() != null)
		{
			target.setExpiryDate(source.getExpiryDate());
			target.setFormattedExpiryDate(simpleDateFormat.format(source.getExpiryDate()));
		}
		if (source.getStartDate() != null)
		{
			target.setStartDate(source.getStartDate());
			target.setFormattedStartDate(simpleDateFormat.format(source.getStartDate()));
		}
		target.setQuoteId(source.getQuoteId());
		target.setState(source.getState());
        target.setQuoteWorkflowStatus(source.getQuoteWorkflowStatus());
	}

	protected String getDateFormatForDisplay()
	{
		return dateFormatForDisplay;
	}

	@Required
	public void setDateFormatForDisplay(final String dateFormatForDisplay)
	{
		this.dateFormatForDisplay = dateFormatForDisplay;
	}
}
