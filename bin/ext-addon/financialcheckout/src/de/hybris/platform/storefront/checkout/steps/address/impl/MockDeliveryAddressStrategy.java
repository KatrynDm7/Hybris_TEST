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
package de.hybris.platform.storefront.checkout.steps.address.impl;

import de.hybris.platform.acceleratorfacades.order.AcceleratorCheckoutFacade;
import de.hybris.platform.commercefacades.i18n.I18NFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.storefront.checkout.steps.address.DeliveryAddressStrategy;

import javax.annotation.Resource;


/**
 * MockDeliveryAddressStrategy set delivery address at checkout.
 */
public class MockDeliveryAddressStrategy implements DeliveryAddressStrategy
{
	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = "i18NFacade")
	private I18NFacade i18NFacade;

	@Resource(name = "acceleratorCheckoutFacade")
	private AcceleratorCheckoutFacade checkoutFacade;

	/**
	 * Setup sample delivery address to checkout.
	 */
	@Override
	public void setupDeliveryAddressToCheckout()
	{

		final CartData cartData = getCheckoutFacade().getCheckoutCart();

		final AddressData newAddress = new AddressData();
		newAddress.setTitleCode("mr");
		newAddress.setFirstName("Firstname");
		newAddress.setLastName("Lastname");
		newAddress.setLine1("line1");
		newAddress.setLine2("line2");
		newAddress.setTown("town");
		newAddress.setPostalCode("postcode");
		newAddress.setBillingAddress(false);
		newAddress.setShippingAddress(true);

		final CountryData countryData = getI18NFacade().getCountryForIsocode("US");
		newAddress.setCountry(countryData);
		newAddress.setDefaultAddress(true);
		newAddress.setVisibleInAddressBook(true);

		getUserFacade().addAddress(newAddress);

		final AddressData previousSelectedAddress = getCheckoutFacade().getCheckoutCart().getDeliveryAddress();
		// Set the new address as the selected checkout delivery address
		getCheckoutFacade().setDeliveryAddress(newAddress);
		if (previousSelectedAddress != null && !previousSelectedAddress.isVisibleInAddressBook())
		{ // temporary address should be removed
			getUserFacade().removeAddress(previousSelectedAddress);
		}
		// Set the new address as the selected checkout delivery address
		getCheckoutFacade().setDeliveryAddress(newAddress);
	}

	protected UserFacade getUserFacade()
	{
		return userFacade;
	}

	protected I18NFacade getI18NFacade()
	{
		return i18NFacade;
	}

	protected AcceleratorCheckoutFacade getCheckoutFacade()
	{
		return checkoutFacade;
	}
}
