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

import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyResponseData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.financialservices.model.InsurancePolicyModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


public class InsurancePolicyResponsePopulator<SOURCE extends OrderModel, TARGET extends OrderData> implements
		Populator<SOURCE, TARGET>
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

		final Set<InsurancePolicyModel> orderPolicies = source.getOrderPolicies();
		final List<InsurancePolicyResponseData> responses = new ArrayList<InsurancePolicyResponseData>();

		for (final InsurancePolicyModel insurancePolicyModel : orderPolicies)
		{
			final InsurancePolicyResponseData data = new InsurancePolicyResponseData();

			data.setPolicyNumber(insurancePolicyModel.getPolicyId());
			if (insurancePolicyModel.getPolicyExpiryDate() != null)
			{
				data.setPolicyExpiryDate(dateFormatter.format(insurancePolicyModel.getPolicyExpiryDate()));
			}
			if (insurancePolicyModel.getPolicyStartDate() != null)
			{
				data.setPolicyStartDate(dateFormatter.format(insurancePolicyModel.getPolicyStartDate()));
			}
			data.setPolicyUrl(insurancePolicyModel.getPolicyUrl());

			responses.add(data);
		}

		target.setInsurancePolicyResponses(responses);

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
