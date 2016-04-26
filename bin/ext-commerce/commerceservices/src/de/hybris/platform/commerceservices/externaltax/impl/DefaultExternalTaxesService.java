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

import de.hybris.platform.commerceservices.externaltax.CalculateExternalTaxesStrategy;
import de.hybris.platform.commerceservices.externaltax.DecideExternalTaxesStrategy;
import de.hybris.platform.commerceservices.externaltax.ExternalTaxesService;
import de.hybris.platform.commerceservices.externaltax.RecalculateExternalTaxesStrategy;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.externaltax.ApplyExternalTaxesStrategy;
import de.hybris.platform.externaltax.ExternalTaxDocument;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 * Base {@link ExternalTaxesService} implementation, to determine if the 3rd party call is necessary and apply the
 * results to the cart.
 */
public class DefaultExternalTaxesService implements ExternalTaxesService
{
	public static final String SESSION_EXTERNAL_TAX_DOCUMENT = "externalTaxDocument";

	private CalculateExternalTaxesStrategy calculateExternalTaxesStrategy;

	private ApplyExternalTaxesStrategy applyExternalTaxesStrategy;

	private DecideExternalTaxesStrategy decideExternalTaxesStrategy;

	private RecalculateExternalTaxesStrategy recalculateExternalTaxesStrategy;

	private ModelService modelService;

	private SessionService sessionService;

	@Override
	public boolean calculateExternalTaxes(final AbstractOrderModel abstractOrder)
	{
		if (getDecideExternalTaxesStrategy().shouldCalculateExternalTaxes(abstractOrder))
		{
			if (getRecalculateExternalTaxesStrategy().recalculate(abstractOrder))
			{
				final ExternalTaxDocument exTaxDocument = getCalculateExternalTaxesStrategy().calculateExternalTaxes(abstractOrder);
				Assert.notNull(exTaxDocument, "ExternalTaxDocument should not be null");
				// check if external tax calculation was successful
				if (!exTaxDocument.getAllTaxes().isEmpty() && !exTaxDocument.getShippingCostTaxes().isEmpty())
				{
					getApplyExternalTaxesStrategy().applyExternalTaxes(abstractOrder, exTaxDocument);
					getSessionService().setAttribute(SESSION_EXTERNAL_TAX_DOCUMENT, exTaxDocument);
					saveOrder(abstractOrder);
					return true;
				}
				else
				{
					// the external tax calculation failed
					getSessionService().removeAttribute(RecalculateExternalTaxesStrategy.SESSION_ATTIR_ORDER_RECALCULATION_HASH);
					clearSessionTaxDocument();
					clearTaxValues(abstractOrder);
					saveOrder(abstractOrder);
				}
			}
			else
			{
				// get the cached tax document
				getApplyExternalTaxesStrategy().applyExternalTaxes(abstractOrder, getSessionExternalTaxDocument(abstractOrder));
				saveOrder(abstractOrder);
				return true;
			}
		}
		return false;
	}

	/**
	 * Resets all the tax values for order and its entries
	 * 
	 * @param abstractOrder
	 *           A hybris cart or order
	 */
	protected void clearTaxValues(final AbstractOrderModel abstractOrder)
	{
		abstractOrder.setTotalTaxValues(Collections.EMPTY_LIST);
		for (final AbstractOrderEntryModel entryModel : abstractOrder.getEntries())
		{
			entryModel.setTaxValues(Collections.EMPTY_LIST);
		}
	}

	protected ExternalTaxDocument getSessionExternalTaxDocument(final AbstractOrderModel abstractOrder)
	{
		if (getSessionService().getAttribute(SESSION_EXTERNAL_TAX_DOCUMENT) == null)
		{
			getSessionService().setAttribute(SESSION_EXTERNAL_TAX_DOCUMENT,
					getCalculateExternalTaxesStrategy().calculateExternalTaxes(abstractOrder));
		}
		return (ExternalTaxDocument) getSessionService().getAttribute(SESSION_EXTERNAL_TAX_DOCUMENT);
	}

	protected void saveOrder(final AbstractOrderModel abstractOrder)
	{
		getModelService().save(abstractOrder);
		getModelService().saveAll(abstractOrder.getEntries());
		setCalculatedStatus(abstractOrder);
	}

	protected void setCalculatedStatus(final AbstractOrderModel order)
	{
		order.setCalculated(Boolean.TRUE);
		final List<AbstractOrderEntryModel> entries = order.getEntries();
		if (entries != null)
		{
			for (final AbstractOrderEntryModel entry : entries)
			{
				entry.setCalculated(Boolean.TRUE);
			}
			getModelService().saveAll(entries);
		}
		getModelService().save(order);
	}

	@Override
	public void clearSessionTaxDocument()
	{
		if (getSessionService().getAttribute(SESSION_EXTERNAL_TAX_DOCUMENT) != null)
		{
			getSessionService().removeAttribute(SESSION_EXTERNAL_TAX_DOCUMENT);
		}
	}


	protected CalculateExternalTaxesStrategy getCalculateExternalTaxesStrategy()
	{
		return calculateExternalTaxesStrategy;
	}

	@Required
	public void setCalculateExternalTaxesStrategy(final CalculateExternalTaxesStrategy calculateExternalTaxesStrategy)
	{
		this.calculateExternalTaxesStrategy = calculateExternalTaxesStrategy;
	}

	protected ApplyExternalTaxesStrategy getApplyExternalTaxesStrategy()
	{
		return applyExternalTaxesStrategy;
	}

	@Required
	public void setApplyExternalTaxesStrategy(final ApplyExternalTaxesStrategy applyExternalTaxesStrategy)
	{
		this.applyExternalTaxesStrategy = applyExternalTaxesStrategy;
	}

	protected DecideExternalTaxesStrategy getDecideExternalTaxesStrategy()
	{
		return decideExternalTaxesStrategy;
	}

	@Required
	public void setDecideExternalTaxesStrategy(final DecideExternalTaxesStrategy decideExternalTaxesStrategy)
	{
		this.decideExternalTaxesStrategy = decideExternalTaxesStrategy;
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

	protected RecalculateExternalTaxesStrategy getRecalculateExternalTaxesStrategy()
	{
		return recalculateExternalTaxesStrategy;
	}

	@Required
	public void setRecalculateExternalTaxesStrategy(final RecalculateExternalTaxesStrategy recalculateExternalTaxesStrategy)
	{
		this.recalculateExternalTaxesStrategy = recalculateExternalTaxesStrategy;
	}

	public SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}


}
