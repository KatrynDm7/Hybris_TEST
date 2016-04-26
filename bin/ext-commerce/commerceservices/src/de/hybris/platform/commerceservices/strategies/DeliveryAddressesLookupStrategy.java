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
package de.hybris.platform.commerceservices.strategies;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.AddressModel;

import java.util.List;


/**
 * A strategy to look up delivery addresses based on the {@link AbstractOrderModel}
 */
public interface DeliveryAddressesLookupStrategy
{
	/**
	 * Gets the list of delivery addresses for an order
	 * 
	 * @param abstractOrder the order
	 * @param visibleAddressesOnly include only the visible addresses
	 * @return A list of delivery address for an order.
	 */
	List<AddressModel> getDeliveryAddressesForOrder(AbstractOrderModel abstractOrder, boolean visibleAddressesOnly);
}
