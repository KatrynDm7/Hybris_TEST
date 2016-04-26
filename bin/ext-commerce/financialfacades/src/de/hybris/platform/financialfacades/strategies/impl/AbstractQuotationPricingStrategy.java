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
package de.hybris.platform.financialfacades.strategies.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.quotation.QuotationItemRequestData;
import de.hybris.platform.commercefacades.quotation.QuotationItemResponseData;
import de.hybris.platform.commercefacades.quotation.QuotationRequestData;
import de.hybris.platform.commercefacades.quotation.QuotationResponseData;
import de.hybris.platform.commercefacades.quotation.Status;
import de.hybris.platform.financialfacades.strategies.QuotationPricingStrategy;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.configuration.Configuration;
import org.springframework.beans.factory.annotation.Required;


public abstract class AbstractQuotationPricingStrategy implements QuotationPricingStrategy
{
	/** Configurable variables in project.properties **/
	protected static String DEFAULT_DATA_SEPERATOR = ",";
	protected static String STATUS_CODE_INVALID;
	protected static String STATUS_CODE_SUCCESS;
	protected static String BASE_PRICE_TAG;
	private String mainProductRegexp;

	@Resource
	private ConfigurationService configurationService;

	/**
	 * Initialization method that must be called immediately after construction (spring wiring at runtime using
	 * init-method, or directly for unit tests)
	 */
	public void initStrategy()
	{
		final Configuration configuration = getConfigurationService().getConfiguration();

		STATUS_CODE_INVALID = configuration.getString("quotation.status.invalid", "INVALID_REQUEST");
		STATUS_CODE_SUCCESS = configuration.getString("quotation.status.sucess", "SUCCESS");
		BASE_PRICE_TAG = configuration.getString("insurance.baseprice.tag.name", "baseprice");
	}

	/**
	 * Method to initialize the response data with default values.
	 *
	 * @return QuotationResponseData with default values.
	 */
	public QuotationResponseData initResponseData()
	{
		final QuotationResponseData responseData = new QuotationResponseData();

		final Status status = new Status();

		//This default reason code should be overridden at concrete Strategy implementation based on business requirement.
		status.setReasonCode(STATUS_CODE_INVALID);
		responseData.setStatus(status);

		return responseData;
	}

	@Override
	public QuotationResponseData getQuote(final QuotationRequestData quotationRequestData)
	{
		validateParameterNotNullStandardMessage("QuotationRequestData", quotationRequestData);

		//Initialise the response data with default values.
		final QuotationResponseData responseData = initResponseData();

		final Map<String, String> value = quotationRequestData.getProperties();
		final String basePrice = value.get(BASE_PRICE_TAG);

		// if part is the temporary implementation for demo to calculate optional product based on base price.
		// else part is the more generic one which will consider the property values from the request data to calculate the
		// paynow price for the main and optional product.
		if (basePrice != null && Double.valueOf(basePrice) > 0 && isNotMainProduct(quotationRequestData))
		{
			responseData.setItems(executeAlternativeAlgorithm(quotationRequestData, basePrice));
			responseData.getStatus().setReasonCode(STATUS_CODE_SUCCESS);
		}
		else
		{
			if (isValidQuote(quotationRequestData))
			{
				responseData.setItems(populateQuotationProductData(quotationRequestData));
				responseData.getStatus().setReasonCode(STATUS_CODE_SUCCESS);
			}
		}

		return responseData;
	}

	/**
	 * Helper method used to populate the ProductResponseData with list of QuotationItemResponseData and its
	 * corresponding paynow price
	 *
	 * @param quotationRequestData
	 * @return List of QuotationItemResponseData
	 */
	protected List<QuotationItemResponseData> populateQuotationProductData(final QuotationRequestData quotationRequestData)
	{
		validateParameterNotNullStandardMessage("QuotationRequestData", quotationRequestData);
		validateParameterNotNullStandardMessage("QuotationItemRequestData", quotationRequestData.getItems());

		final List<QuotationItemResponseData> productList = new ArrayList<QuotationItemResponseData>();
		QuotationItemResponseData productData;

		for (final QuotationItemRequestData itemData : quotationRequestData.getItems())
		{
			productData = new QuotationItemResponseData();

			// main product plan
			productData.setId(itemData.getId());
			productData.setPayNowPrice(executeAlgorithm(quotationRequestData, itemData));

			productList.add(productData);
		}

		return productList;
	}

	protected abstract String executeAlgorithm(final QuotationRequestData requestData, final QuotationItemRequestData itemData);

	protected abstract boolean isValidQuote(final QuotationRequestData requestData);

	protected boolean isNotMainProduct(final QuotationRequestData quotationRequestData)
	{
		boolean isNotMainProduct = true;

		for (final QuotationItemRequestData itemData : quotationRequestData.getItems())
		{
			if (itemData.getId().matches(getMainProductRegexp()))
			{
				isNotMainProduct = false;
			}
		}

		return isNotMainProduct;
	}

	protected abstract List<QuotationItemResponseData> executeAlternativeAlgorithm(
			final QuotationRequestData quotationRequestData, String basePrice);

	protected String getMainProductRegexp()
	{
		return mainProductRegexp;
	}

	@Required
	public void setMainProductRegexp(final String mainProductRegexp)
	{
		this.mainProductRegexp = mainProductRegexp;
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}
}
