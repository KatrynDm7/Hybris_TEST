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
package de.hybris.platform.assistedservicefacades.event;

import de.hybris.platform.assistedservicefacades.AssistedServiceFacade;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.events.SubmitOrderEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;


/**
 * Submit order event listener for populating agent creator.
 */
public class SubmitOrderEventListener extends AbstractEventListener<SubmitOrderEvent>
{

	private AssistedServiceFacade assistedServiceFacade;
	private ModelService modelService;

	@Override
	protected void onEvent(final SubmitOrderEvent event)
	{
		if (assistedServiceFacade.isAssistedServiceAgentLoggedIn())
		{
			final OrderModel order = event.getOrder();
			order.setPlacedBy(assistedServiceFacade.getAsmSession().getAgent());
			modelService.save(order);
		}
	}

	/**
	 * @return the assistedServiceFacade
	 */
	public AssistedServiceFacade getAssistedServiceFacade()
	{
		return assistedServiceFacade;
	}

	/**
	 * @param assistedServiceFacade
	 *           the assistedServiceFacade to set
	 */
	public void setAssistedServiceFacade(final AssistedServiceFacade assistedServiceFacade)
	{
		this.assistedServiceFacade = assistedServiceFacade;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}