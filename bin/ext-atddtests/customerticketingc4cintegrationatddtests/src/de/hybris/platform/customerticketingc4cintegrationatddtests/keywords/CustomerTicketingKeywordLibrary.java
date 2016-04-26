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
package de.hybris.platform.customerticketingc4cintegrationatddtests.keywords;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.atddengine.keywords.AbstractKeywordLibrary;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.customerticketingfacades.TicketFacade;
import de.hybris.platform.customerticketingfacades.data.StatusData;
import de.hybris.platform.customerticketingfacades.data.TicketData;
import de.hybris.platform.site.BaseSiteService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


public class CustomerTicketingKeywordLibrary extends AbstractKeywordLibrary
{
	private static final Logger LOGGER = Logger.getLogger(CustomerTicketingKeywordLibrary.class);

	@Autowired
	private TicketFacade ticketFacade;
	@Autowired
	private BaseSiteService baseSiteService;
	@Autowired
	private CustomerFacade customerFacade;

	public TicketData createTicketWithTitleAndMessage(final String title, final String message)
	{
		TicketData data = new TicketData();
		data.setSubject(title);
		data.setMessage(message);
		LOGGER.info("uid:" + customerFacade.getCurrentCustomerUid());
		data.setCustomerId(customerFacade.getCurrentCustomerUid());
		data = ticketFacade.createTicket(data);
		LOGGER.info("Created ticket: " + data);
		LOGGER.info("Tickets id: " + data.getId());
		return data;
	}

	public TicketData getTicketById(final String ticketId)
	{
		LOGGER.info("Getting ticket with id: " + ticketId);
		final TicketData ticket = ticketFacade.getTicket(ticketId);
		LOGGER.info("Ticket from GET:" + ticket);
		return ticket;
	}

	public TicketData updateTicketByIdWithStatusAndMessage(final String ticketId, final String status, final String message)
	{
		final TicketData ticket = ticketFacade.getTicket(ticketId);
		LOGGER.info("Ticket from GET:" + ticket);
		assertTrue(ticket != null);

		ticket.setMessage(message);
		final StatusData statusData = new StatusData();
		statusData.setId(status);
		ticket.setStatus(statusData);
		final TicketData updateT = ticketFacade.updateTicket(ticket);
		return updateT;
	}

	public List<TicketData> getTickets()
	{
		final PageableData pd = new PageableData();
		final SearchPageData<TicketData> tickets = ticketFacade.getTickets(pd);
		LOGGER.info("Founded tickets: " + tickets);
		assertFalse(tickets.getResults().isEmpty());

		return tickets.getResults();
	}

	public void setTestBaseSite()
	{
		baseSiteService.setCurrentBaseSite(baseSiteService.getBaseSiteForUID("testSite"), true);
	}

	public TicketFacade getTicketFacade()
	{
		return ticketFacade;
	}

	public void setTicketFacade(final TicketFacade ticketFacade)
	{
		this.ticketFacade = this.ticketFacade;
	}

	public CustomerFacade getCustomerFacade()
	{
		return customerFacade;
	}

	public void setCustomerFacade(final CustomerFacade customerFacade)
	{
		this.customerFacade = customerFacade;
	}
}
