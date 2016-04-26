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
package de.hybris.platform.sap.sapcoreconfigurationbackoffice.actions;

import de.hybris.platform.sap.core.configuration.model.SAPRFCDestinationModel;
import de.hybris.platform.sap.core.configuration.rfc.event.SAPRFCDestinationPingEvent;
import de.hybris.platform.servicelayer.event.EventService;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.zkoss.zul.Messagebox;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;


/**
 *
 */
public class PingDestinationAction implements CockpitAction<SAPRFCDestinationModel, String>
{


	private static final Logger LOG = Logger.getLogger(PingDestinationAction.class.getName());

	@Resource
	private EventService eventService;

	public EventService getEventService()
	{
		return eventService;
	}



	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}



	@Override
	public ActionResult<String> perform(final ActionContext<SAPRFCDestinationModel> ctx)
	{
		final SAPRFCDestinationModel destination = ctx.getData();

		if (destination == null)
		{
			return new ActionResult<String>(ActionResult.ERROR);
		}
		else
		{
			final String defDestName = destination.getRfcDestinationName();
			final SAPRFCDestinationPingEvent event = new SAPRFCDestinationPingEvent(defDestName);
			getEventService().publishEvent(event);
			ActionResult<String> result;

			if (event.getResultIndicator() == 0)
			{
				LOG.info(event.getMessage());
				result = new ActionResult<>(ActionResult.SUCCESS);
				Messagebox.show(ctx.getLabel("ActionResult.Ping.success"));
			}
			else
			{
				LOG.error(event.getMessage());
				result = new ActionResult<>(ActionResult.ERROR);
				Messagebox.show(ctx.getLabel("ActionResult.Ping.failed"));
			}

			result.setResultMessage(event.getMessage());
			return result;
		}



	}



	@Override
	public boolean canPerform(final ActionContext<SAPRFCDestinationModel> ctx)
	{
		return true;
	}

	@Override
	public boolean needsConfirmation(final ActionContext<SAPRFCDestinationModel> ctx)
	{
		return false;
	}

	@Override
	public String getConfirmationMessage(final ActionContext<SAPRFCDestinationModel> ctx)
	{
		return "";
	}


}
