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
package de.hybris.platform.financialfacades.populators;

import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyListingData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.financialservices.model.InsurancePolicyModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Required;

public class InsurancePolicyListPopulator<SOURCE extends InsurancePolicyModel, TARGET extends InsurancePolicyListingData>
		implements Populator<SOURCE, TARGET>
{
	private static SimpleDateFormat dateFormatter;

	private String dateFormatForDisplay;

	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException
	{
		if (dateFormatter == null)
		{
			dateFormatter = new SimpleDateFormat(getDateFormatForDisplay());
		}

		target.setPolicyNumber(source.getPolicyId());
		if (source.getPolicyStartDate() != null)
		{
			target.setPolicyRawStartDate(source.getPolicyStartDate());
			target.setPolicyStartDate(dateFormatter.format(source.getPolicyStartDate()));
		}
		if (source.getPolicyExpiryDate() != null)
		{
			target.setPolicyRawExpiryDate(source.getPolicyExpiryDate());
			target.setPolicyExpiryDate(dateFormatter.format(source.getPolicyExpiryDate()));
		}
		target.setPolicyUrl(source.getPolicyUrl());
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
