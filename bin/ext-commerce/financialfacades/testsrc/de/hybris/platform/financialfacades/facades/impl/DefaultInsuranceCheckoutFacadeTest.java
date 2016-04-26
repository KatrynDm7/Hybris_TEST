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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.xyformsfacades.data.YFormDataData;
import de.hybris.platform.xyformsservices.enums.YFormDataTypeEnum;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;


@UnitTest
public class DefaultInsuranceCheckoutFacadeTest
{

	private static final String CART_CODE = "cartCode";
	private static final Integer ORDER_ENTRY_NUMBER = Integer.valueOf(123);

	@Mock
	private CheckoutFacade checkoutFacade;

	private OrderEntryData orderEntryData;

	private CartData cartData;

	@InjectMocks
	private DefaultInsuranceCheckoutFacade insuranceCheckoutFacade;

	@Before
	public void setup()
	{
		insuranceCheckoutFacade = new DefaultInsuranceCheckoutFacade();

		MockitoAnnotations.initMocks(this);

		final ProductData productData = new ProductData();
		final String productCode = "productCode";
		productData.setCode(productCode);

		orderEntryData = new OrderEntryData();
		orderEntryData.setProduct(productData);
		orderEntryData.setEntryNumber(ORDER_ENTRY_NUMBER);

		cartData = new CartData();
		cartData.setEntries(Lists.<OrderEntryData> newArrayList());
		cartData.getEntries().add(orderEntryData);
		cartData.setCode(CART_CODE);

		Mockito.when(checkoutFacade.getCheckoutCart()).thenReturn(cartData);
	}

	@Test
	public void shouldReturnValidFormData()
	{
		final String yFormDataContent = "yFormDataContent";

		final YFormDataData yFormDataData = new YFormDataData();
		yFormDataData.setContent(yFormDataContent);
		yFormDataData.setType(YFormDataTypeEnum.DATA);

		orderEntryData.setFormDataData(Lists.<YFormDataData> newArrayList());
		orderEntryData.getFormDataData().add(yFormDataData);

		Assert.assertTrue(insuranceCheckoutFacade.orderEntryHasValidFormData());
	}

	@Test
	public void shouldReturnInvalidFormDataWhenContentEmpty()
	{
		final YFormDataData yFormDataData = new YFormDataData();
		yFormDataData.setContent(StringUtils.EMPTY);
		yFormDataData.setType(YFormDataTypeEnum.DATA);

		orderEntryData.setFormDataData(Lists.<YFormDataData> newArrayList());
		orderEntryData.getFormDataData().add(yFormDataData);

		Assert.assertFalse(insuranceCheckoutFacade.orderEntryHasValidFormData());
	}

	@Test
	public void shouldReturnInvalidFormDataWhenDraftVersion()
	{
		final String yFormDataContent = "yFormDataContent";
		final YFormDataData yFormDataData = new YFormDataData();
		yFormDataData.setContent(yFormDataContent);
		yFormDataData.setType(YFormDataTypeEnum.DRAFT);

		orderEntryData.setFormDataData(Lists.<YFormDataData> newArrayList());
		orderEntryData.getFormDataData().add(yFormDataData);

		Assert.assertFalse(insuranceCheckoutFacade.orderEntryHasValidFormData());
	}

	@Test
	public void shouldReturnValidFormDataWhenEntryFormDataNull()
	{
		orderEntryData.setFormDataData(null);
		Assert.assertFalse(insuranceCheckoutFacade.orderEntryHasValidFormData());
	}

	@Test
	public void shouldReturnInvalidFormDataWhenNoEntries()
	{
		cartData.setEntries(null);
		Assert.assertFalse(insuranceCheckoutFacade.orderEntryHasValidFormData());
	}

	@Test
	public void shouldHasSavedFormDataWithSessionSavedFormData()
	{
		cartData.setHasSessionFormData(true);

		Assert.assertTrue(insuranceCheckoutFacade.hasSavedFormData());
	}

	@Test
	public void shouldHasSavedFormDataWithEntryFormData()
	{
		final YFormDataData yFormDataData = new YFormDataData();
		yFormDataData.setContent("any contents");
		yFormDataData.setType(YFormDataTypeEnum.DATA);

		orderEntryData.setFormDataData(Lists.<YFormDataData> newArrayList());
		orderEntryData.getFormDataData().add(yFormDataData);

		Assert.assertTrue(insuranceCheckoutFacade.hasSavedFormData());
	}

	@Test
	public void shouldHasNoSavedFormDataWithNoEntryFormDataNoSessionSavedFormData()
	{
		cartData.setHasSessionFormData(null);

		final boolean result = insuranceCheckoutFacade.hasSavedFormData();
		Assert.assertFalse(result);
	}

	@Test
	public void shouldHasNoSavedFormDataWithEmptyFormDataContent()
	{
		final YFormDataData yFormDataData = new YFormDataData();
		yFormDataData.setContent(StringUtils.EMPTY);
		yFormDataData.setType(YFormDataTypeEnum.DATA);

		orderEntryData.setFormDataData(Lists.<YFormDataData> newArrayList());
		orderEntryData.getFormDataData().add(yFormDataData);

		final boolean result = insuranceCheckoutFacade.hasSavedFormData();
		Assert.assertFalse(result);
	}

}
