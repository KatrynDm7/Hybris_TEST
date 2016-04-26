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

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.insurance.data.PolicyItemRequestData;
import de.hybris.platform.commercefacades.insurance.data.PolicyItemResponseData;
import de.hybris.platform.commercefacades.insurance.data.PolicyRequestData;
import de.hybris.platform.commercefacades.insurance.data.PolicyResponseData;
import de.hybris.platform.financialfacades.facades.PolicyServiceFacade;
import de.hybris.platform.financialfacades.strategies.PolicyPdfUrlGeneratorStrategy;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Lists;


/**
 * The class of MockPolicyServiceFacade.
 */
public class MockPolicyServiceFacade implements PolicyServiceFacade
{
	private ConfigurationService configurationService;

	private PolicyPdfUrlGeneratorStrategy policyPdfUrlGeneratorStrategy;

	private String dateFormat;
	protected final int ONE_MONTH = 1;
	protected final int TWELVE_MONTH = 12;

	/**
	 * Request to create a policy to an external system, and return to hybris the information about the policy creation.
	 * NOTE: this is a mock implementation, which simply pass the request data back as a response data. Create mock
	 * information of policy.
	 *
	 * @param requestData
	 *           the policy request data
	 * @return policy response data
	 */
	@Override
	public PolicyResponseData requestPolicyCreation(final PolicyRequestData requestData)
	{
		validateParameterNotNullStandardMessage("PolicyRequestData cannot be null.", requestData);
		final PolicyResponseData responseData = new PolicyResponseData();
		responseData.setItems(Lists.<PolicyItemResponseData> newArrayList());

		if (CollectionUtils.isNotEmpty(requestData.getItems()))
		{
			for (final PolicyItemRequestData policyItemRequestData : requestData.getItems())
			{
				final PolicyItemResponseData itemData = new PolicyItemResponseData();
				itemData.setId(policyItemRequestData.getId());
				itemData.setProperties(policyItemRequestData.getProperties());

				itemData.getProperties().put("startDate", createMockPolicyStartDate(policyItemRequestData));
				itemData.getProperties().put("expiryDate", createMockPolicyExpiryDate(policyItemRequestData));
				itemData.getProperties().put("policyId", createMockPolicyId(policyItemRequestData));

				final String policyOrderId = policyItemRequestData.getProperties().get("orderId");

				itemData.getProperties().put("policyUrl", getPolicyPdfUrlGeneratorStrategy().generatePdfUrlForPolicy(policyOrderId));

				responseData.getItems().add(itemData);
			}
		}

		return responseData;
	}

	protected String createMockPolicyStartDate(final PolicyItemRequestData itemRequestData)
	{
		final SimpleDateFormat sdf = new SimpleDateFormat(getDateFormat());
		return sdf.format(new Date());
	}

	protected String createMockPolicyExpiryDate(final PolicyItemRequestData itemRequestData)
	{
		final String type = "type";
		int month = ONE_MONTH;
		if (itemRequestData.getProperties().containsKey(type))
		{
			if ("insurances_event".equalsIgnoreCase(itemRequestData.getProperties().get(type)))
			{
				month = ONE_MONTH;
			}
			else if ("insurances_travel".equalsIgnoreCase(itemRequestData.getProperties().get(type)))
			{
				month = ONE_MONTH;
			}
			else if ("insurances_property_homeowners".equalsIgnoreCase(itemRequestData.getProperties().get(type)))
			{
				month = TWELVE_MONTH;
			}
			else if ("insurances_property_renters".equalsIgnoreCase(itemRequestData.getProperties().get(type)))
			{
				month = TWELVE_MONTH;
			}
		}
		final DateTime expireDate = DateTime.now().plusMonths(month);
		final SimpleDateFormat sdf = new SimpleDateFormat(getDateFormat());
		return sdf.format(expireDate.toDate());
	}

	protected String createMockPolicyId(final PolicyItemRequestData itemRequestData)
	{
		if (itemRequestData.getProperties().containsKey("orderId"))
		{
			return itemRequestData.getProperties().get("orderId");
		}
		return itemRequestData.getId();
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

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	public PolicyPdfUrlGeneratorStrategy getPolicyPdfUrlGeneratorStrategy()
	{
		return policyPdfUrlGeneratorStrategy;
	}

	public void setPolicyPdfUrlGeneratorStrategy(final PolicyPdfUrlGeneratorStrategy policyPdfUrlGeneratorStrategy)
	{
		this.policyPdfUrlGeneratorStrategy = policyPdfUrlGeneratorStrategy;
	}
}
