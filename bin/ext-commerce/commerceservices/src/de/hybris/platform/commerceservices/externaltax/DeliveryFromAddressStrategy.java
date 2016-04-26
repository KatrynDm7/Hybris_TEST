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
package de.hybris.platform.commerceservices.externaltax;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.AddressModel;

/**
 *  A strategy to get a ship from address used for external tax calculation.
 */

public interface DeliveryFromAddressStrategy
{

	AddressModel getDeliveryFromAddressForOrder(final AbstractOrderModel orderModel);
}
