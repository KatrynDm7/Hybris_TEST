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
package de.hybris.platform.commerceservices.price.impl;

import de.hybris.platform.commerceservices.strategies.NetGrossStrategy;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.product.PriceService;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.time.TimeService;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Implementation of {@link PriceService} which is parameterized with a strategy for determining the net/gross attribute
 * of returned prices from a strategy.
 */
public class NetPriceService extends AbstractBusinessService implements PriceService
{
	private NetGrossStrategy netGrossStrategy;
	private TimeService timeService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.product.PriceService#getPriceInformationsForProduct(de.hybris.platform.core.model.product.
	 * ProductModel)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public List<PriceInformation> getPriceInformationsForProduct(final ProductModel model)
	{
		final boolean net = getNetGrossStrategy().isNet();
		final Product product = (Product) getModelService().getSource(model);
		try
		{
			return product.getPriceInformations(timeService.getCurrentTime(), net);
		}
		catch (final JaloPriceFactoryException e)
		{
			throw new SystemException(e.getMessage(), e);
		}
	}

	protected NetGrossStrategy getNetGrossStrategy()
	{
		return netGrossStrategy;
	}

	@Required
	public void setNetGrossStrategy(final NetGrossStrategy netGrossStrategy)
	{
		this.netGrossStrategy = netGrossStrategy;
	}

	@Required
	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}
}
