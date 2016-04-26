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
package de.hybris.platform.sap.sapordermgmtb2bfacades.checkout.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.b2bacceleratorfacades.order.data.B2BPaymentTypeData;
import de.hybris.platform.b2bacceleratorservices.constants.GeneratedB2BAcceleratorServicesConstants.Enumerations.CheckoutPaymentType;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.sap.core.common.exceptions.ApplicationBaseRuntimeException;
import de.hybris.platform.sap.sapordermgmtb2bfacades.ProductImageHelper;
import de.hybris.platform.sap.sapordermgmtb2bfacades.cart.impl.ProductImageHelperMockBuilder;
import de.hybris.platform.sap.sapordermgmtservices.BackendAvailabilityService;
import de.hybris.platform.sap.sapordermgmtservices.cart.CartService;
import de.hybris.platform.sap.sapordermgmtservices.checkout.CheckoutService;
import de.hybris.platform.sap.sapordermgmtservices.checkout.impl.DefaultCheckoutService;
import de.hybris.platform.sap.sapordermgmtservices.partner.SapPartnerService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 *
 */
//@UnitTest
public class SapB2BAcceleratorCheckoutFacadeTest
{

	private SapB2BAcceleratorCheckoutFacade classUnderTest;
	private final String addressId = "A";
	private final AddressData addressData = new AddressData();
	private BackendAvailabilityService backendAvailabilityService;

	@Before
	public void setUp()
	{
		classUnderTest = new SapB2BAcceleratorCheckoutFacade();
		final Converter<AddressModel, AddressData> addressConverter = EasyMock.createMock(Converter.class);
		final SapB2BAcceleratorCheckoutFacade b2bCheckoutFacade = EasyMock.createMock(SapB2BAcceleratorCheckoutFacade.class);
		EasyMock.expect(b2bCheckoutFacade.getAddressDataForId(addressId, true)).andReturn(addressData);

		EasyMock.expect(b2bCheckoutFacade.isBackendDown()).andReturn(false);

		backendAvailabilityService = EasyMock.createMock(BackendAvailabilityService.class);
		EasyMock.expect(backendAvailabilityService.isBackendDown()).andReturn(false);

		EasyMock.replay(addressConverter, b2bCheckoutFacade, backendAvailabilityService);
		classUnderTest.setAddressConverter(addressConverter);
		//classUnderTest.setB2bCheckoutFacade(b2bCheckoutFacade);
		classUnderTest.setBackendAvailabilityService(backendAvailabilityService);
	}

	@Test
	public void testSetDeliveryAddress_returnTrue()
	{

		//TODO Remove as method implementation has changed
		final AddressData usedAddress = new AddressData();
		usedAddress.setEmail("test@sap.com");


		final PK pk123 = PK.createFixedUUIDPK(0, 123);
		final PK pk124 = PK.createFixedUUIDPK(0, 124);

		usedAddress.setId(pk123.toString());


		final Collection<AddressModel> allowedDeliveryAddresses = new ArrayList<AddressModel>();
		final AddressModel address1mock = EasyMock.createNiceMock(AddressModel.class);
		EasyMock.expect(address1mock.getPk()).andReturn(pk123).anyTimes();
		EasyMock.expect(address1mock.getSapCustomerID()).andReturn("123").anyTimes();
		EasyMock.replay(address1mock);
		final AddressModel address2mock = EasyMock.createNiceMock(AddressModel.class);
		EasyMock.expect(address2mock.getPk()).andReturn(pk124).anyTimes();
		EasyMock.replay(address2mock);

		allowedDeliveryAddresses.add(address1mock);
		allowedDeliveryAddresses.add(address2mock);


		final SapPartnerService sapPartnerServiceMock = EasyMock.createNiceMock(SapPartnerService.class);
		EasyMock.expect(sapPartnerServiceMock.getAllowedDeliveryAddresses()).andReturn(allowedDeliveryAddresses).anyTimes();
		EasyMock.replay(sapPartnerServiceMock);

		final DefaultCheckoutService checkoutServiceMock = EasyMock.createNiceMock(DefaultCheckoutService.class);

		EasyMock.expect(checkoutServiceMock.setDeliveryAddress("123")).andReturn(true);
		EasyMock.replay(checkoutServiceMock);

		classUnderTest.setCheckoutService(checkoutServiceMock);
		classUnderTest.setSapPartnerService(sapPartnerServiceMock);
		assertTrue(classUnderTest.setDeliveryAddress(usedAddress));
	}

	@Test(expected = ApplicationBaseRuntimeException.class)
	public void testAuthorizePayment()
	{
		final String securityCode = "code";
		classUnderTest.authorizePayment(securityCode);
	}

	@Test
	public void testContainsTaxValues()
	{
		final boolean containsTaxValues = classUnderTest.containsTaxValues();
		//prices from ERP always contain taxes
		assertTrue(containsTaxValues);
	}

	@Test(expected = ApplicationBaseRuntimeException.class)
	public void testCreateCartFromOrder()
	{
		//this is not yet in scope
		classUnderTest.createCartFromOrder("A");
	}

	@Test(expected = ApplicationBaseRuntimeException.class)
	public void testCreatePaymentSubscription()
	{
		final CCPaymentInfoData paymentInfoData = null;
		//this is not yet in scope
		classUnderTest.createPaymentSubscription(paymentInfoData);
	}

	@Test
	public void testAddressConverter()
	{
		assertNotNull(classUnderTest.getAddressConverter());
	}

	@Test
	public void testAddressDataForID()
	{
		//test proper delegation
		final boolean visibleAddressesOnly = true;
		final AddressData addressDataForId = classUnderTest.getAddressDataForId(addressId, visibleAddressesOnly);
		assertEquals(addressData, addressDataForId);
	}

	@Test
	public void testUpdateCheckoutCart()
	{
		final CartData cartData = new CartData();
		final CheckoutService checkoutServiceMock = EasyMock.createNiceMock(CheckoutService.class);
		EasyMock.expect(checkoutServiceMock.updateCheckoutCart(cartData)).andReturn(cartData);
		EasyMock.replay(checkoutServiceMock);

		classUnderTest.setCheckoutService(checkoutServiceMock);
		assertNotNull(classUnderTest.updateCheckoutCart(cartData));
	}


	@Test
	public void testGetPaymentTypes()
	{
		final List<B2BPaymentTypeData> paymentTypes = classUnderTest.getPaymentTypes();
		assertFalse(paymentTypes.isEmpty());
		assertTrue(paymentTypes.size() == 1);
		assertEquals(paymentTypes.get(0).getCode(), CheckoutPaymentType.ACCOUNT.toLowerCase());
	}


	@Test
	public void testSetDeliveryMode()
	{

		final CheckoutService checkoutServiceMock = EasyMock.createNiceMock(CheckoutService.class);
		EasyMock.expect(checkoutServiceMock.setDeliveryMode("123")).andReturn(true);
		EasyMock.replay(checkoutServiceMock);
		classUnderTest.setCheckoutService(checkoutServiceMock);

		assertTrue(classUnderTest.setDeliveryMode("123"));
	}


	@Test
	public void testSetDeliveryModeBackendDown()
	{

		final BackendAvailabilityService backendAvailabilityService = EasyMock.createNiceMock(BackendAvailabilityService.class);
		EasyMock.expect(backendAvailabilityService.isBackendDown()).andReturn(true);
		EasyMock.replay(backendAvailabilityService);
		classUnderTest.setBackendAvailabilityService(backendAvailabilityService);

		assertFalse(classUnderTest.setDeliveryMode("123"));
	}

	@Test
	public void testGetCheckoutCartBackendDown()
	{

		final CartData cartData = new CartData();

		final BackendAvailabilityService backendAvailabilityService = EasyMock.createNiceMock(BackendAvailabilityService.class);
		EasyMock.expect(backendAvailabilityService.isBackendDown()).andReturn(true);
		EasyMock.replay(backendAvailabilityService);
		classUnderTest.setBackendAvailabilityService(backendAvailabilityService);

		Assert.assertNotSame(cartData, classUnderTest.getCheckoutCart());

	}

	@Test
	public void testGetCheckoutCart()
	{

		final CartData cartData = new CartData();

		final CartService cartServiceMock = EasyMock.createNiceMock(CartService.class);
		EasyMock.expect(cartServiceMock.getSessionCart()).andReturn(cartData);
		EasyMock.replay(cartServiceMock);


		final ProductImageHelper productImageHelperMock = ProductImageHelperMockBuilder.create()
				.withEnrichWithProductImages(cartData).build();

		classUnderTest.setProductImageHelper(productImageHelperMock);
		//classUnderTest.setCartService(cartServiceMock);
		Assert.assertSame(cartData, classUnderTest.getCheckoutCart());

	}




}
