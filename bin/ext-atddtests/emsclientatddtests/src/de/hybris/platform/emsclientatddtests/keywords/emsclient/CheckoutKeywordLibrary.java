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
 */

package de.hybris.platform.emsclientatddtests.keywords.emsclient;

import static junit.framework.Assert.assertNotNull;

import de.hybris.platform.atddengine.keywords.AbstractKeywordLibrary;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.site.BaseSiteService;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * ATDD keywords for checkout process.
 */
public class CheckoutKeywordLibrary extends AbstractKeywordLibrary
{
	public static final String ROBOT_LIBRARY_SCOPE = "GLOBAL";

	private static final Logger LOG = Logger.getLogger(CheckoutKeywordLibrary.class);

	@Autowired
	private CheckoutFacade acceleratorCheckoutFacade;

	@Autowired
	private UserFacade userFacade;

	@Autowired
	private BaseSiteService baseSiteService;

	private final Random random = new Random();

	public void updateUserDetails() throws DuplicateUidException
	{
		final Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.setTime(new Date());

		final AddressData address = new AddressData();
		address.setId(UUID.randomUUID().toString());
		address.setShippingAddress(true);
		address.setDefaultAddress(true);
		address.setBillingAddress(true);
		address.setVisibleInAddressBook(true);

		final CCPaymentInfoData creditCard = new CCPaymentInfoData();
		creditCard.setId(String.valueOf(Math.abs(random.nextInt())));
		creditCard.setAccountHolderName("HOLDER NAME");
		creditCard.setIssueNumber("123");
		creditCard.setBillingAddress(address);
		creditCard.setCardNumber("1111111111111111");
		creditCard.setCardType("visa");
		creditCard.setDefaultPaymentInfo(true);
		creditCard.setExpiryMonth(String.valueOf(calendar.get(Calendar.MONTH)));
		creditCard.setExpiryYear(String.valueOf(calendar.get(Calendar.YEAR) + 3));

		userFacade.setDefaultAddress(address);
		userFacade.setDefaultPaymentInfo(creditCard);
	}

	public OrderData doCheckout() throws InvalidCartException
	{
		baseSiteService.setCurrentBaseSite("testSite", true);
		final OrderData order = acceleratorCheckoutFacade.placeOrder();
		assertNotNull("CheckoutFacade#placeOrder returned null", order);
		return order;
	}
}
