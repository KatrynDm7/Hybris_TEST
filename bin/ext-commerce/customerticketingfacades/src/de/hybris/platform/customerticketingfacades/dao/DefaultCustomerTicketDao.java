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
package de.hybris.platform.customerticketingfacades.dao;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ticket.dao.TicketDao;
import de.hybris.platform.ticket.model.CsTicketModel;

import java.util.List;


public interface DefaultCustomerTicketDao extends TicketDao
{
	/**
	 * Find all tickets that are associated with the customer in descending and order by Modified date time.
	 *
	 * @param customer
	 * @return List<CsTicketModel>
	 */
	List<CsTicketModel> findTicketsByCustomerOrderByModifiedTime(UserModel customer);
}
