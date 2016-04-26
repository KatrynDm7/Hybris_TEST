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
package de.hybris.platform.customerticketingfacades.converters.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.customerticketingfacades.data.StatusData;
import de.hybris.platform.customerticketingfacades.data.TicketData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketService;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;


/*
 * Converter implementation for {@link de.hybris.platform.ticket.model.CsTicketModel} as source and
 * {@link de.hybris.platform.customerticketingfacades.data.TicketData} as target type.
 */
public class DefaultTicketPopulator<SOURCE extends CsTicketModel, TARGET extends TicketData> implements Populator<SOURCE, TARGET>
{
	@Resource
	private Map<String, StatusData> statusMapping;

	@Resource
	private Map<StatusData, List<StatusData>> validTransitions;

	@Autowired
	private TicketService ticketService;

	@Resource(name = "i18nService")
	private I18NService i18nService;

	@Override
	public void populate(final CsTicketModel source, final TicketData target) throws ConversionException
	{
		target.setSubject(source.getHeadline());
		target.setId(source.getTicketID());
		final CsTicketState csTicketState = source.getState();
		target.setStatus(statusMapping.get(csTicketState.getCode()));
		target.setAvailableStatusTransitions(validTransitions.get(target.getStatus()));
		target.setCreationDate(source.getCreationtime());
		target.setLastModificationDate(source.getModifiedtime());

		target.setMessageHistory(getMessageHistory(source));
	}

	/**
	 * @param source
	 * @return messageHistory
	 */
	private String getMessageHistory(final CsTicketModel source)
	{
		final List<CsTicketEventModel> events = ticketService.getEventsForTicket(source);
		final StringBuilder sb = new StringBuilder();
		final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy hh:mm a");
		for (final CsTicketEventModel csTicketEventModel : events)
		{
			if (StringUtils.isNotEmpty(csTicketEventModel.getText()))
			{
				sb.append(format.format(csTicketEventModel.getCreationtime()) + " added by ");
				if (csTicketEventModel.getAuthor() != null && csTicketEventModel.getAuthor() instanceof EmployeeModel)
				{
					sb.append("Customer Service");
				}
				else
				{
					sb.append(source.getCustomer().getName());
				}
				sb.append("\n" + csTicketEventModel.getText() + "\n\n");
			}
		}
		return sb.toString();
	}
}
