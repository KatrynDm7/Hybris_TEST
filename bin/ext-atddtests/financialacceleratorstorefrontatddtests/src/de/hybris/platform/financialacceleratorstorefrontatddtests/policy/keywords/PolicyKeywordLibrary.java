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
package de.hybris.platform.financialacceleratorstorefrontatddtests.policy.keywords;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import de.hybris.platform.atddengine.keywords.AbstractKeywordLibrary;
import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyResponseData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.financialacceleratorstorefrontatddtests.converters.XmlConverters;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


public class PolicyKeywordLibrary extends AbstractKeywordLibrary
{

	private static final Logger LOG = Logger.getLogger(PolicyKeywordLibrary.class);

	@Autowired
	private CartService cartService;

	@Autowired
	private CommerceCheckoutService commerceCheckoutService;

	@Autowired
	private Converter<OrderModel, OrderData> orderConverter;

	@Autowired
	private XmlConverters xmlConverters;

	@Autowired
	private String globalDateFormat;

	/**
	 * 
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>verify the insurance policy that have been created after placing the order</i>
	 * <p>
	 * *
	 * 
	 */
	public void verifyInsurancePolicy()
	{
		try
		{
			final CartModel cartModel = cartService.getSessionCart();

			final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();

			parameter.setEnableHooks(true);
			parameter.setCart(cartModel);
			final CommerceOrderResult commerceOrderResult = commerceCheckoutService.placeOrder(parameter);
			final OrderData ordrData = orderConverter.convert(commerceOrderResult.getOrder());

			verifyMandatoryData(ordrData);

		}
		catch (final IllegalArgumentException e)
		{
			LOG.error("Either the expected XML is malformed or the Card is null", e);
			fail("Either the expected XML is malformed or the Card is null");
		}
		catch (final InvalidCartException e)
		{
			LOG.error("Cart validation failed it is invalid cart", e);
			fail("Cart validation failed it is invalid cart");
		}
	}

	/**
	 * Method used to verify all the mandatory data for the policy that have been created after order placement.
	 * 
	 * @param ordrData
	 */
	protected void verifyMandatoryData(final OrderData ordrData)
	{
		final List<InsurancePolicyResponseData> insurancePolicyResponses = ordrData.getInsurancePolicyResponses();

		for (final InsurancePolicyResponseData responseData : insurancePolicyResponses)
		{
			assertNotNull(responseData.getPolicyStartDate());

			assertNotNull(responseData.getPolicyExpiryDate());

			if (isValidPolicy(responseData))
			{
				assertNotNull(responseData.getPolicyNumber());

				assertNotNull(responseData.getPolicyUrl());
			}
		}
	}

	/**
	 * Method used validate the policy data.
	 * 
	 * @param responseData
	 * @return true if startDate is before endDate otherwise false
	 */
	protected boolean isValidPolicy(final InsurancePolicyResponseData responseData)
	{
		boolean isValid = false;
		final SimpleDateFormat formatter = new SimpleDateFormat(globalDateFormat);

		Date startDate = null;
		Date endDate = null;
		try
		{
			startDate = formatter.parse(responseData.getPolicyStartDate());
			endDate = formatter.parse(responseData.getPolicyExpiryDate());
		}
		catch (final ParseException e)
		{
			LOG.error("Invalid start/end date of the insurance policy", e);
			fail("Invalid start/end date of the insurance policy");
		}

		if (startDate.before(endDate))
		{
			isValid = true;
		}

		return isValid;
	}

}
