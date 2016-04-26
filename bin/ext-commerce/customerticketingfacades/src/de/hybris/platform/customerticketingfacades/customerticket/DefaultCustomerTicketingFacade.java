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
package de.hybris.platform.customerticketingfacades.customerticket;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.customerticketingfacades.TicketFacade;
import de.hybris.platform.customerticketingfacades.constants.CustomerticketingfacadesConstants;
import de.hybris.platform.customerticketingfacades.dao.DefaultCustomerTicketDao;
import de.hybris.platform.customerticketingfacades.data.StatusData;
import de.hybris.platform.customerticketingfacades.data.TicketData;
import de.hybris.platform.customerticketingfacades.service.CustomerTicketingService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ClassMismatchException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.ticket.enums.CsEventReason;
import de.hybris.platform.ticket.enums.CsInterventionType;
import de.hybris.platform.ticket.enums.CsResolutionType;
import de.hybris.platform.ticket.enums.CsTicketCategory;
import de.hybris.platform.ticket.enums.CsTicketPriority;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.events.model.CsCustomerEventModel;
import de.hybris.platform.ticket.model.CsAgentGroupModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketBusinessService;
import de.hybris.platform.ticket.service.TicketException;
import de.hybris.platform.ticket.strategies.TicketEventStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * CS Integration Ticket Facade Facade should provide access to a user's support ticket details and full details of
 * support ticket.
 *
 */

public class DefaultCustomerTicketingFacade implements TicketFacade
{
	protected static final Logger LOG = Logger.getLogger(DefaultCustomerTicketingFacade.class);

	@Resource(name = "ticketService")
	private CustomerTicketingService ticketService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource
	private ConfigurationService configurationService;

	@Resource(name = "ticketBusinessService")
	private TicketBusinessService ticketBusinessService;

	@Resource(name = "ticketEventStrategy")
	private TicketEventStrategy ticketEventStrategy;

	@Resource(name = "ticketDao")
	private DefaultCustomerTicketDao ticketDao;

	@Resource
	private Map<String, StatusData> statusMapping;

	@Resource(name = "ticketConverter")
	private Converter<CsTicketModel, TicketData> ticketConverter;

	@Override
	public TicketData createTicket(final TicketData ticketData)
	{
		CsTicketModel ticket = this.populateTicket(ticketData);
		final CsCustomerEventModel creationEvent = ticketEventStrategy.createCreationEventForTicket(ticket,
				CsEventReason.FIRSTCONTACT, CsInterventionType.IM, ticketData.getMessage());
		try
		{
			ticket = ticketBusinessService.createTicket(ticket, creationEvent);
		}
		catch (final ModelSavingException msEx)
		{
			LOG.error(msEx.getMessage(), msEx);
		}
		catch (final Exception ex)
		{
			// We can treat other exceptions, other than ModelSavingException, as the ticket has been created.
			LOG.error(ex.getMessage(), ex);
		}

		if (ticket.getTicketID() == null)
		{
			return null;
		}
		else
		{
			ticketData.setId(ticket.getTicketID());
		}
		return ticketData;
	}

	@Override
	public TicketData updateTicket(final TicketData ticketData)
	{
		CsTicketModel ticket = ticketService.getTicketForTicketId(ticketData.getId());

		// if with status change
		if (!statusMapping.get(ticket.getState().getCode()).getId().equalsIgnoreCase(ticketData.getStatus().getId()))
		{
			ticket = stateChanges.get(getCsStatus(ticketData)).apply(ticket, ticketData);
		}
		else
		// only a note
		{
			final CsCustomerEventModel customerEventModel = ticketBusinessService.addNoteToTicket(ticket, CsInterventionType.IM,
					CsEventReason.UPDATE, ticketData.getMessage(), null);
			ticket = customerEventModel != null ? ticketService.getTicketForTicketId(ticket.getTicketID()) : null;
		}

		if (ticket == null)
		{
			return null;
		}
		return ticketData;
	}

	protected CsTicketState getCsStatus(final TicketData data)
	{
		for (final String key : statusMapping.keySet())
		{
			if (data.getStatus().getId().equalsIgnoreCase(statusMapping.get(key).getId()))
			{
				LOG.info("matching with: " + key);
				return CsTicketState.valueOf(key);
			}
		}
		LOG.warn("Status key not found");
		return null;
	}

	protected Map<CsTicketState, BiFunction<CsTicketModel, TicketData, CsTicketModel>> stateChanges = new HashMap<CsTicketState, BiFunction<CsTicketModel, TicketData, CsTicketModel>>()
	{
		{
			put(CsTicketState.CLOSED, (updatedTicket, ticketData) -> {
				try
				{
					return ticketBusinessService.resolveTicket(updatedTicket, CsInterventionType.IM, CsResolutionType.CLOSED,
							ticketData.getMessage()) != null ? ticketService.getTicketForTicketId(updatedTicket.getTicketID()) : null;
				}
				catch (final TicketException ex)
				{
					LOG.error(ex.getMessage(), ex);
					return null;
				}
			});

			put(CsTicketState.OPEN, (updatedTicket, ticketData) -> {
				try
				{
					return ticketBusinessService.unResolveTicket(updatedTicket, CsInterventionType.IM, CsEventReason.UPDATE,
							ticketData.getMessage()) != null ? ticketService.getTicketForTicketId(updatedTicket.getTicketID()) : null;
				}
				catch (final TicketException ex)
				{
					LOG.error(ex.getMessage(), ex);
					return null;
				}
			});
		}
	};

	@Override
	public TicketData getTicket(final String ticketId)
	{
		final CsTicketModel ticketModel = ticketService.getTicketForTicketId(ticketId);
		return ticketConverter.convert(ticketModel, new TicketData());
	}

	@SuppressWarnings("deprecation")
	@Override
	public SearchPageData<TicketData> getTickets(final PageableData pageableData)
	{
		final List<TicketData> tickets = new ArrayList<TicketData>();
		final List<CsTicketModel> ticketsForCustomer = ticketService.getTicketsForCustomerOrderByModifiedTime(userService
				.getCurrentUser());
		for (final CsTicketModel csTicketModel : ticketsForCustomer)
		{
			tickets.add(ticketConverter.convert(csTicketModel, new TicketData()));
		}

		final SearchPageData<TicketData> results = new SearchPageData<TicketData>();
		results.setResults(tickets);

		return results;
	}

	/**
	 * @param ticketData
	 * @return CsTicketModel
	 */
	private CsTicketModel populateTicket(final TicketData ticketData)
	{
		final CsTicketModel newTicket = modelService.create(CsTicketModel.class);
		newTicket.setHeadline(ticketData.getSubject());
		newTicket.setCategory(CsTicketCategory.INCIDENT);
		newTicket.setPriority(CsTicketPriority.MEDIUM);
		newTicket.setState(CsTicketState.NEW);

		final String csManagerGroup = configurationService.getConfiguration().getString(
				CustomerticketingfacadesConstants.DEFAULT_CS_AGENT_MANAGER_GROUP_UID, "");
		if (StringUtils.isNotBlank(csManagerGroup))
		{
			try
			{
				newTicket.setAssignedGroup(userService.getUserGroupForUID(csManagerGroup, CsAgentGroupModel.class));
			}
			catch (ClassMismatchException | UnknownIdentifierException exception)
			{
				LOG.error("Can't set AssignedGroup for ticket " + newTicket + ", cause: " + exception);
			}
		}

		newTicket.setCustomer(userService.getUserForUID(ticketData.getCustomerId()));
		return newTicket;
	}
}
