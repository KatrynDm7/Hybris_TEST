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
package de.hybris.platform.acceleratorwebservicesaddon.payment.service.impl;


import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.platform.acceleratorfacades.payment.data.PaymentSubscriptionResultData;
import de.hybris.platform.acceleratorwebservicesaddon.model.payment.PaymentSubscriptionResultModel;
import de.hybris.platform.acceleratorwebservicesaddon.payment.dao.PaymentSubscriptionResultDao;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class DefaultPaymentSubscriptionResultServiceTest
{
	private static String CART_CODE = "1";
	private static String UNKNOWN_CART_CODE = "1";
	private static String CART_GUID = "1111-11111-111-111";
	private final PaymentSubscriptionResultData paymentSubscriptionResultData = new PaymentSubscriptionResultData();
	private final PaymentSubscriptionResultModel paymentSubscriptionResultModel = new PaymentSubscriptionResultModel();

	@Mock
	private PaymentSubscriptionResultDao paymentSubscriptionResultDao;
	@Mock
	private ModelService modelService;

	private DefaultPaymentSubscriptionResultService paymentSubscriptionResultService;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		paymentSubscriptionResultService = new DefaultPaymentSubscriptionResultService();
		paymentSubscriptionResultService.setModelService(modelService);
		paymentSubscriptionResultService.setPaymentSubscriptionResultDao(paymentSubscriptionResultDao);

		paymentSubscriptionResultModel.setCartId(CART_CODE);
		paymentSubscriptionResultModel.setSuccess(true);
		paymentSubscriptionResultModel.setResult(paymentSubscriptionResultData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindPaymentSubscriptionResultByCartWithNullParam()
	{
		paymentSubscriptionResultService.findPaymentSubscriptionResultByCart(null);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void testFindPaymentSubscriptionResultByCartForNotExistingObject()
	{
		given(paymentSubscriptionResultDao.findPaymentSubscriptionResultByCart(UNKNOWN_CART_CODE)).willThrow(
				new ModelNotFoundException("not found"));
		paymentSubscriptionResultService.findPaymentSubscriptionResultByCart(UNKNOWN_CART_CODE);
	}

	@Test
	public void testFindPaymentSubscriptionResultByCart()
	{
		given(paymentSubscriptionResultDao.findPaymentSubscriptionResultByCart(CART_CODE)).willReturn(
				paymentSubscriptionResultModel);

		final PaymentSubscriptionResultModel result = paymentSubscriptionResultService
				.findPaymentSubscriptionResultByCart(CART_CODE);
		Assert.assertEquals(paymentSubscriptionResultModel, result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemovePaymentSubscriptionResultForCartWithNullParam()
	{
		paymentSubscriptionResultService.removePaymentSubscriptionResultForCart(null);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void testRemovePaymentSubscriptionResultForCartForNotExistingObject()
	{
		given(paymentSubscriptionResultDao.findPaymentSubscriptionResultByCart(UNKNOWN_CART_CODE)).willThrow(
				new ModelNotFoundException("not found"));

		paymentSubscriptionResultService.removePaymentSubscriptionResultForCart(UNKNOWN_CART_CODE);
	}

	@Test
	public void testRemovePaymentSubscriptionResultForCart()
	{
		given(paymentSubscriptionResultDao.findPaymentSubscriptionResultByCart(CART_CODE)).willReturn(
				paymentSubscriptionResultModel);

		paymentSubscriptionResultService.removePaymentSubscriptionResultForCart(CART_CODE);

		verify(modelService, times(1)).remove(paymentSubscriptionResultModel);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemovePaymentSubscriptionResultForCartAndGuidWithNullParam1()
	{
		paymentSubscriptionResultService.removePaymentSubscriptionResultForCart(null, CART_GUID);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemovePaymentSubscriptionResultForCartAndGuidWithNullParam2()
	{
		paymentSubscriptionResultService.removePaymentSubscriptionResultForCart(CART_CODE, null);
	}

	@Test
	public void testRemovePaymentSubscriptionResultForCartAndGuiForNotExistingObject()
	{
		given(paymentSubscriptionResultDao.findPaymentSubscriptionResultByCart(CART_CODE, CART_GUID)).willReturn(new ArrayList());
		paymentSubscriptionResultService.removePaymentSubscriptionResultForCart(CART_CODE, CART_GUID);

		verify(modelService, times(0)).remove(Mockito.any());
	}

	@Test
	public void testRemovePaymentSubscriptionResultForCartAndGuid()
	{
		final List<PaymentSubscriptionResultModel> resultList = new ArrayList<>();
		resultList.add(paymentSubscriptionResultModel);
		given(paymentSubscriptionResultDao.findPaymentSubscriptionResultByCart(CART_CODE, CART_GUID)).willReturn(resultList);

		paymentSubscriptionResultService.removePaymentSubscriptionResultForCart(CART_CODE, CART_GUID);

		verify(modelService, times(1)).remove(paymentSubscriptionResultModel);
	}

	@Test(expected = IllegalArgumentException.class)
	public void savePaymentSubscriptionResultWithNullParam()
	{
		paymentSubscriptionResultService.savePaymentSubscriptionResult(null);
	}

	@Test
	public void savePaymentSubscriptionResult()
	{
		paymentSubscriptionResultService.savePaymentSubscriptionResult(paymentSubscriptionResultModel);

		verify(modelService, times(1)).save(paymentSubscriptionResultModel);
	}
}
