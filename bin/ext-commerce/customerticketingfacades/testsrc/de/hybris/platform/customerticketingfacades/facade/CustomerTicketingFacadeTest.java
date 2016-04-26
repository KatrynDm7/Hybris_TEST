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
package de.hybris.platform.customerticketingfacades.facade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.customerticketingfacades.TicketFacade;
import de.hybris.platform.customerticketingfacades.data.TicketData;
import de.hybris.platform.ticket.enums.CsEventReason;
import de.hybris.platform.ticket.enums.CsInterventionType;
import de.hybris.platform.ticket.enums.CsTicketCategory;
import de.hybris.platform.ticket.enums.CsTicketPriority;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.events.model.CsCustomerEventModel;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.jalo.AbstractTicketsystemTest;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/**
 * @author Chennadi
 *
 */
public class CustomerTicketingFacadeTest extends AbstractTicketsystemTest
{
	private final String headline = "Ticket Headline";
	private final String note = "Ticket Creation Notes";

	private final String headline1 = "Ticket Headline";
	private final String note1 = "Ticket Creation Notes";

	private final String note_update = "Ticket Updated Note";

	@Resource(name = "defaultCustomerTicketService")
	private TicketService ticketService;

	@Resource(name = "defaultCustomerTicketingFacade")
	private TicketFacade ticketFacade;

	@Ignore
	@Override
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
	}

	@Ignore
	@Test
	public void testCreateTicket()
	{
		final TicketData ticketData = new TicketData();
		ticketData.setSubject(headline);
		ticketData.setMessage(note);
		ticketData.setCustomerId(testUser.getUid());

		final TicketData ticketData1 = ticketFacade.createTicket(ticketData);
		assertNotNull(ticketData1.getId());

		final CsTicketModel ticket = ticketService.getTicketForTicketId(ticketData1.getId());
		final List<CsTicketEventModel> eventsForTicket = ticketService.getEventsForTicket(ticket);

		assertEquals(CsTicketState.NEW, ticket.getState());
		assertTrue(ticket.getCustomer().equals(testUser));
		assertTrue(ticket.getHeadline().equals(headline));
		assertTrue(StringUtils.isNotEmpty(ticket.getTicketID()));
		assertTrue(ticket.getPriority().equals(CsTicketPriority.MEDIUM));
		assertTrue(ticket.getCategory().equals(CsTicketCategory.INCIDENT));
		assertEquals(1, eventsForTicket.size());
		assertTrue(eventsForTicket.get(0) instanceof CsCustomerEventModel);
		final CsCustomerEventModel creationEvent = (CsCustomerEventModel) eventsForTicket.get(0);
		assertEquals(note, creationEvent.getText());
		assertEquals(CsInterventionType.IM, creationEvent.getInterventionType());
		assertEquals(CsEventReason.FIRSTCONTACT, creationEvent.getReason());
	}

	@Ignore
	@Test
	public void testGetTicketsForCustomerOrderByModifiedTime()
	{

		final TicketData ticketDataOne = new TicketData();
		ticketDataOne.setSubject(headline);
		ticketDataOne.setMessage(note);
		ticketDataOne.setCustomerId(testUser.getUid());

		final TicketData ticketData1 = ticketFacade.createTicket(ticketDataOne);
		assertNotNull(ticketData1.getId());


		final TicketData ticketDataTwo = new TicketData();
		ticketDataTwo.setSubject(headline1);
		ticketDataTwo.setMessage(note1);
		ticketDataTwo.setCustomerId(testUser.getUid());

		final TicketData ticketData2 = ticketFacade.createTicket(ticketDataTwo);
		assertNotNull(ticketData2.getId());

		final CsTicketModel ticket1 = ticketService.getTicketForTicketId(ticketData1.getId());
		final CsTicketModel ticket2 = ticketService.getTicketForTicketId(ticketData2.getId());
		assertNotNull(ticket1);
		assertNotNull(ticket2);

		assertTrue(ticket1.getModifiedtime().before(ticket2.getModifiedtime()));
	}

	@Ignore
	@Test
	public void testUpdateTicketsForCustomerOrderByModifiedTime()
	{
		final TicketData ticketData = new TicketData();
		ticketData.setSubject(headline);
		ticketData.setMessage(note);
		ticketData.setCustomerId(testUser.getUid());

		final TicketData ticketData1 = ticketFacade.createTicket(ticketData);
		assertNotNull(ticketData1.getId());

		CsTicketModel ticket = ticketService.getTicketForTicketId(ticketData1.getId());
		List<CsTicketEventModel> eventsForTicket = ticketService.getEventsForTicket(ticket);
		assertTrue(eventsForTicket.size() == 1);

		final TicketData ticketDetails = ticketFacade.getTicket(ticketData1.getId());
		ticketDetails.setMessage(note_update);
		ticketFacade.updateTicket(ticketDetails);


		ticket = ticketService.getTicketForTicketId(ticketData1.getId());
		eventsForTicket = ticketService.getEventsForTicket(ticket);
		assertTrue(eventsForTicket.size() == 2);

	}
}