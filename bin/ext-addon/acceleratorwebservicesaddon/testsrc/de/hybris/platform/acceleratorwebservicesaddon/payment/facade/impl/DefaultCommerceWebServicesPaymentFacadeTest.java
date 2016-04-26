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
package de.hybris.platform.acceleratorwebservicesaddon.payment.facade.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorfacades.payment.data.PaymentSubscriptionResultData;
import de.hybris.platform.acceleratorwebservicesaddon.model.payment.PaymentSubscriptionResultModel;
import de.hybris.platform.acceleratorwebservicesaddon.payment.service.PaymentSubscriptionResultService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultCommerceWebServicesPaymentFacadeTest
{
	private static String CART_CODE = "1";

	@Mock
	private PaymentSubscriptionResultService paymentSubscriptionResultService;
	@Mock
	private ModelService modelService;

	private DefaultCommerceWebServicesPaymentFacade commerceWebServicesPaymentFacade;

	private final PaymentSubscriptionResultData paymentSubscriptionResultData = new PaymentSubscriptionResultData();
	private final PaymentSubscriptionResultModel paymentSubscriptionResultModel = new PaymentSubscriptionResultModel();

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		commerceWebServicesPaymentFacade = new DefaultCommerceWebServicesPaymentFacade();
		commerceWebServicesPaymentFacade.setModelService(modelService);
		commerceWebServicesPaymentFacade.setPaymentSubscriptionResultService(paymentSubscriptionResultService);

		paymentSubscriptionResultModel.setCartId(CART_CODE);
		paymentSubscriptionResultModel.setSuccess(true);
		paymentSubscriptionResultModel.setResult(paymentSubscriptionResultData);
	}

	@Test
	public void testSavePaymentSubscriptionResult()
	{
		given(paymentSubscriptionResultService.findPaymentSubscriptionResultByCart(CART_CODE)).willThrow(
				new UnknownIdentifierException("not found"));
		final PaymentSubscriptionResultModel newPaymentSubscriptionResultModel = new PaymentSubscriptionResultModel();
		given(modelService.create(PaymentSubscriptionResultModel.class)).willReturn(newPaymentSubscriptionResultModel);

		commerceWebServicesPaymentFacade.savePaymentSubscriptionResult(paymentSubscriptionResultData, CART_CODE);

		verify(paymentSubscriptionResultService, times(1)).savePaymentSubscriptionResult(newPaymentSubscriptionResultModel);
	}

	@Test
	public void testExistingSavePaymentSubscriptionResult()
	{
		given(paymentSubscriptionResultService.findPaymentSubscriptionResultByCart(CART_CODE)).willReturn(
				paymentSubscriptionResultModel);

		commerceWebServicesPaymentFacade.savePaymentSubscriptionResult(paymentSubscriptionResultData, CART_CODE);

		verify(paymentSubscriptionResultService, times(1)).savePaymentSubscriptionResult(paymentSubscriptionResultModel);
	}

	@Test
	public void testGetNotExistingPaymentSubscriptionResult()
	{
		given(paymentSubscriptionResultService.findPaymentSubscriptionResultByCart(CART_CODE)).willThrow(
				new UnknownIdentifierException("not found"));

		final PaymentSubscriptionResultData result = commerceWebServicesPaymentFacade.getPaymentSubscriptionResult(CART_CODE);

		Assert.assertNull(result);
	}

	@Test
	public void testGetPaymentSubscriptionResult()
	{
		given(paymentSubscriptionResultService.findPaymentSubscriptionResultByCart(CART_CODE)).willReturn(
				paymentSubscriptionResultModel);

		final PaymentSubscriptionResultData result = commerceWebServicesPaymentFacade.getPaymentSubscriptionResult(CART_CODE);

		Assert.assertEquals(paymentSubscriptionResultModel.getResult(), result);
	}

	@Test
	public void testRemovePaymentSubscriptionResult()
	{
		given(paymentSubscriptionResultService.findPaymentSubscriptionResultByCart(CART_CODE)).willReturn(
				paymentSubscriptionResultModel);

		commerceWebServicesPaymentFacade.removePaymentSubscriptionResult(CART_CODE);

		verify(paymentSubscriptionResultService, times(1)).removePaymentSubscriptionResultForCart(CART_CODE);
	}

	@Test
	public void testRemoveNotExistingPaymentSubscriptionResult()
	{
		willThrow(new UnknownIdentifierException("not found")).given(paymentSubscriptionResultService)
				.removePaymentSubscriptionResultForCart(CART_CODE);

		commerceWebServicesPaymentFacade.removePaymentSubscriptionResult(CART_CODE);

		verify(paymentSubscriptionResultService, times(1)).removePaymentSubscriptionResultForCart(CART_CODE);
	}
}
