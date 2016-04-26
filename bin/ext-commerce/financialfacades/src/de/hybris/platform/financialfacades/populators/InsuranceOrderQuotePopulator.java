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

import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.quotation.InsuranceQuoteData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.financialservices.model.InsuranceQuoteModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * The class of InsuranceOrderEntryInsuranceInformationPopulator. This populator is temporary class that needed to extra
 * form data from the OrderEntry, this should later be replaced with populators that populates from the Quote Object.
 */
public class InsuranceOrderQuotePopulator implements Populator<AbstractOrderModel, AbstractOrderData>
{
	private Converter<InsuranceQuoteModel, InsuranceQuoteData> insuranceQuoteConverter;

	private List<InsuranceDataPopulatorStrategy> insuranceDataPopulatorStrategies;

	/**
	 * Populate the target instance with values from the source instance.
	 *
	 * @param orderModel
	 *           the source object
	 * @param orderData
	 *           the target to fill
	 */
	@Override
	public void populate(final AbstractOrderModel orderModel, final AbstractOrderData orderData)
	{
		if (orderModel.getInsuranceQuote() != null)
		{
			final InsuranceQuoteModel quoteModel = orderModel.getInsuranceQuote();

			if (quoteModel != null)
			{
				final InsuranceQuoteData quoteData = getInsuranceQuoteConverter().convert(orderModel.getInsuranceQuote());
				orderData.setInsuranceQuote(quoteData);

				Map<String, Object> infoMap = null;

				if (MapUtils.isNotEmpty(quoteModel.getProperties()))
				{
					infoMap = quoteModel.getProperties();
				}
				else
				{
					infoMap = new HashMap<String, Object>();
				}

				// loop through any populator strategies looking for one which will fit and be used to populate the quoteData
				if (getInsuranceDataPopulatorStrategies() != null)
				{
					for (final InsuranceDataPopulatorStrategy insuranceDataPopulatorStrategy : getInsuranceDataPopulatorStrategies())
					{
						insuranceDataPopulatorStrategy.processInsuranceQuoteData(quoteData, infoMap);
					}
				}

			}

		}
	}

	protected Converter<InsuranceQuoteModel, InsuranceQuoteData> getInsuranceQuoteConverter()
	{
		return insuranceQuoteConverter;
	}

	@Required
	public void setInsuranceQuoteConverter(final Converter<InsuranceQuoteModel, InsuranceQuoteData> insuranceQuoteConverter)
	{
		this.insuranceQuoteConverter = insuranceQuoteConverter;
	}

	public List<InsuranceDataPopulatorStrategy> getInsuranceDataPopulatorStrategies()
	{
		return insuranceDataPopulatorStrategies;
	}

	public void setInsuranceDataPopulatorStrategies(final List<InsuranceDataPopulatorStrategy> insuranceDataPopulatorStrategies)
	{
		this.insuranceDataPopulatorStrategies = insuranceDataPopulatorStrategies;
	}
}
