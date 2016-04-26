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
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;

import java.util.List;


/**
 * A strategy to look up delivery modes based on the {@link AbstractOrderModel}
 * 
 */
public interface DeliveryModeLookupStrategy
{

	/**
	 * Gets the list of delivery modes for given order/cart
	 * 
	 * @param abstractOrderModel
	 * @return sorted list of delivery modes
	 */
	List<DeliveryModeModel> getSelectableDeliveryModesForOrder(AbstractOrderModel abstractOrderModel);
}
