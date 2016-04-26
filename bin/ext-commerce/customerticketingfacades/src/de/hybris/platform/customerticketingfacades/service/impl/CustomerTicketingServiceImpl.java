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
package de.hybris.platform.customerticketingfacades.service.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerticketingfacades.dao.DefaultCustomerTicketDao;
import de.hybris.platform.customerticketingfacades.service.CustomerTicketingService;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.impl.DefaultTicketService;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;


public class CustomerTicketingServiceImpl extends DefaultTicketService implements CustomerTicketingService
{

	protected static final Logger LOG = Logger.getLogger(CustomerTicketingServiceImpl.class);

	@Resource(name = "ticketDao")
	private DefaultCustomerTicketDao ticketDao;

	@Override
	public List<CsTicketModel> getTicketsForCustomerOrderByModifiedTime(final UserModel customer)
	{
		if (customer == null)
		{
			return Collections.emptyList();
		}

		return ticketDao.findTicketsByCustomerOrderByModifiedTime(customer);
	}
}
