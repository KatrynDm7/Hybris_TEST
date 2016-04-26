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
package de.hybris.platform.timedaccesspromotionsservices.order;

import de.hybris.platform.core.model.order.AbstractOrderModel;


/**
 * A strategy defined for flashbuy order is complete
 */
public interface FlashbuyOrderCompleteStrategy
{

	/**
	 * If get the order complete point from check process, Need to make flashbuy order complete <li>
	 * flashbuyPromotionService.allocate</li>
	 *
	 * @param order
	 */
	public void orderComplete(AbstractOrderModel order);

}
