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
package de.hybris.platform.commerceservices.strategies.impl;

import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.strategies.CheckoutCustomerStrategy;
import de.hybris.platform.commerceservices.strategies.DeliveryAddressesLookupStrategy;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;


public class DefaultDeliveryAddressesLookupStrategy implements DeliveryAddressesLookupStrategy
{
	private CustomerAccountService customerAccountService;

	private CheckoutCustomerStrategy checkoutCustomerStrategy;

	@Override
	public List<AddressModel> getDeliveryAddressesForOrder(final AbstractOrderModel abstractOrder,
			final boolean visibleAddressesOnly)
	{
		final List<AddressModel> addressesForOrder = new ArrayList<AddressModel>();
		if (abstractOrder != null)
		{
			final UserModel user = abstractOrder.getUser();
			if (user instanceof CustomerModel)
			{
				if (visibleAddressesOnly)
				{
					addressesForOrder.addAll(getCustomerAccountService().getAddressBookDeliveryEntries((CustomerModel) user));
				}
				else
				{
					addressesForOrder.addAll(getCustomerAccountService().getAllAddressEntries((CustomerModel) user));
				}

				// If the user had no addresses, check the order for an address in case it's a guest checkout.
				if (getCheckoutCustomerStrategy().isAnonymousCheckout() &&
						addressesForOrder.isEmpty() && abstractOrder.getDeliveryAddress() != null)
				{
					addressesForOrder.add(abstractOrder.getDeliveryAddress());
				}
			}
		}
		return addressesForOrder;
	}

	protected CustomerAccountService getCustomerAccountService()
	{
		return customerAccountService;
	}

	@Required
	public void setCustomerAccountService(final CustomerAccountService customerAccountService)
	{
		this.customerAccountService = customerAccountService;
	}

	protected CheckoutCustomerStrategy getCheckoutCustomerStrategy()
	{
		return checkoutCustomerStrategy;
	}

	@Required
	public void setCheckoutCustomerStrategy(final CheckoutCustomerStrategy checkoutCustomerStrategy)
	{
		this.checkoutCustomerStrategy = checkoutCustomerStrategy;
	}
}
