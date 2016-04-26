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
package de.hybris.platform.commerceservices.externaltax.impl;

import de.hybris.platform.commerceservices.externaltax.RecalculateExternalTaxesStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartHashCalculationStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceOrderParameter;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Base {@link RecalculateExternalTaxesStrategy} implementation, gives decision whether or not a recalculation of the
 * external taxes is necessary.
 */
public class DefaultRecalculateExternalTaxesStrategy implements RecalculateExternalTaxesStrategy
{
	private ModelService modelService;
	private CommerceCartHashCalculationStrategy commerceCartHashCalculationStrategy;
	private SessionService sessionService;

	/**
	 * Base implementation to generate the hash if not existing and compare if it does.
	 */
	@Override
	public boolean recalculate(final AbstractOrderModel abstractOrderModel)
	{

		final CommerceOrderParameter parameter = new CommerceOrderParameter();
		parameter.setOrder(abstractOrderModel);
		final String orderCalculationHash = getCommerceCartHashCalculationStrategy().buildHashForAbstractOrder(parameter);
		final String sessionHash = getSessionService().getAttribute(SESSION_ATTIR_ORDER_RECALCULATION_HASH);
		boolean needsRecalculation = false;

		if (StringUtils.isBlank(sessionHash) || !StringUtils.equals(sessionHash, orderCalculationHash))
		{
			getSessionService().setAttribute(SESSION_ATTIR_ORDER_RECALCULATION_HASH, orderCalculationHash);
			needsRecalculation = true;
		}

		return needsRecalculation;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected CommerceCartHashCalculationStrategy getCommerceCartHashCalculationStrategy()
	{
		return commerceCartHashCalculationStrategy;
	}

	public void setCommerceCartHashCalculationStrategy(final CommerceCartHashCalculationStrategy commerceCartHashCalculationStrategy)
	{
		this.commerceCartHashCalculationStrategy = commerceCartHashCalculationStrategy;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}
