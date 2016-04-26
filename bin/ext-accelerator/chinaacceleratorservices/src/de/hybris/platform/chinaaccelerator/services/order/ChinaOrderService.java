/*
 *
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
 */
package de.hybris.platform.chinaaccelerator.services.order;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.OrderService;

import java.util.Date;
import java.util.List;


public interface ChinaOrderService extends OrderService
{
	/**
	 * Fetch orders for the given status and {@link OrderModel#getModifiedtime()} less than or equal to the expired date
	 *
	 * @param orderStatus
	 * @param expiredDate
	 * @return The list of orders matching orderStatus and expiredDate
	 */
	List<OrderModel> getExpiredOrderForStatus(OrderStatus orderStatus, Date expiredDate);

	List<OrderModel> getUnexportedOrderForStatus(OrderStatus... orderStatus);

	/**
	 * @param code
	 * @return
	 */
	public OrderModel getOrderByCode(final String code);
}
