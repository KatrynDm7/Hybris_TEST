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
package de.hybris.platform.timedaccesspromotionsfacades.facades.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.timedaccesspromotionsfacades.facades.FlashbuyPromotionFacade;
import de.hybris.platform.timedaccesspromotionsfacades.order.ExtendedCartFacade;
import de.hybris.platform.timedaccesspromotionsservices.exception.MultipleEnqueueException;
import de.hybris.platform.timedaccesspromotionsservices.service.FlashbuyPromotionService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link FlashbuyPromotionFacade}
 */
public class DefaultFlashbuyPromotionFacade implements FlashbuyPromotionFacade
{
	protected static final Logger LOG = Logger.getLogger(DefaultFlashbuyPromotionFacade.class);

	private FlashbuyPromotionService flashbuyPromotionService;
	private ExtendedCartFacade extendedCartFacade;

	@Override
	public boolean hasProductAvailable(final String productCode, final String promotionCode)
	{
		validateParameterNotNull(promotionCode, "Promotion Code can not be null");
		validateParameterNotNull(productCode, "Product Code can not be null");

		if (getFlashbuyPromotionService().getRemainingQuantity(promotionCode, productCode) > 0)
		{
			return true;
		}
		return false;
	}


	@Override
	public boolean enqueue(final String productCode, final String promotionCode, final String customerUID, final long quantity)
			throws CommerceCartModificationException, MultipleEnqueueException
	{
		validateParameterNotNull(promotionCode, "Promotion Code can not be null");
		validateParameterNotNull(productCode, "Product Code can not be null");
		validateParameterNotNull(customerUID, "Customer Uid can not be null");

		if (quantity <= 0)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Enqueue Failed: enqueue quantity is supposed to be a positive number. The actual request quantity is "
						+ quantity);
			}
		}
		else if (getFlashbuyPromotionService().getRemainingQuantity(promotionCode, productCode) == 0)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Enqueue Failed: product " + productCode + " has already sold out.");
			}
		}
		else if (getFlashbuyPromotionService().enqueue(promotionCode, productCode, customerUID, quantity))
		{
			extendedCartFacade.addNewEntryToCart(productCode, quantity, promotionCode);
			return true;
		}
		return false;
	}

	protected FlashbuyPromotionService getFlashbuyPromotionService()
	{
		return flashbuyPromotionService;
	}

	@Required
	public void setFlashbuyPromotionService(final FlashbuyPromotionService flashbuyPromotionService)
	{
		this.flashbuyPromotionService = flashbuyPromotionService;
	}

	protected ExtendedCartFacade getExtendedCartFacade()
	{
		return extendedCartFacade;
	}

	@Required
	public void setExtendedCartFacade(final ExtendedCartFacade extendedCartFacade)
	{
		this.extendedCartFacade = extendedCartFacade;
	}

}
