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
package de.hybris.platform.customerticketingfacades.service;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketService;

import java.util.List;


/**
 * Business service for handling customer support tickets. Use this service to create new customer support tickets and
 * view them.
 *
 */
public interface CustomerTicketingService extends TicketService
{

	/**
	 *
	 * Find all tickets that are associated with the customer in order by Modified date and time.
	 *
	 * @param customer
	 * @return List<CsTicketModel>
	 */
	List<CsTicketModel> getTicketsForCustomerOrderByModifiedTime(UserModel customer);
}
