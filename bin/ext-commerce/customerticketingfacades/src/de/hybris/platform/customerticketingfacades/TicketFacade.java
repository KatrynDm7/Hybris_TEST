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
package de.hybris.platform.customerticketingfacades;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.customerticketingfacades.data.TicketData;

import javax.annotation.Nonnull;


/**
 * interface that holds the operations for ticketing system
 */
public interface TicketFacade
{
	/**
	 * Creates ticket.
	 *
	 * @param ticketData
	 */
	@Nonnull
	TicketData createTicket(TicketData ticketData);

	/**
	 * Updates ticket
	 *
	 * @param ticketData
	 */
	@Nonnull
	TicketData updateTicket(TicketData ticketData);

	/**
	 * Requesting for a ticket of a customer.
	 *
	 * @param ticketId
	 * @return not-null object
	 */
	@Nonnull
	TicketData getTicket(String ticketId);

	/**
	 * Requesting for all tickets of a customer.
	 *
	 * @return not-null list
	 */
	@Nonnull
	SearchPageData<TicketData> getTickets(final PageableData pageableData);

}
