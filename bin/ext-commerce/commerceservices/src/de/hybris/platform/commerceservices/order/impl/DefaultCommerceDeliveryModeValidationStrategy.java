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
package de.hybris.platform.commerceservices.order.impl;

import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.commerceservices.order.CommerceCartCalculationStrategy;
import de.hybris.platform.commerceservices.order.CommerceDeliveryModeValidationStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.servicelayer.util.ServicesUtil.*;


public class DefaultCommerceDeliveryModeValidationStrategy implements CommerceDeliveryModeValidationStrategy
{
	private ModelService modelService;
	private CommerceCartCalculationStrategy commerceCartCalculationStrategy;
	private DeliveryService deliveryService;


	@Override
	public void validateDeliveryMode(final CommerceCheckoutParameter parameter)
	{
		final CartModel cartModel = parameter.getCart();
		validateParameterNotNull(cartModel, "Cart model cannot be null");

		final DeliveryModeModel currentDeliveryMode = cartModel.getDeliveryMode();
		if (currentDeliveryMode != null)
		{
			final Collection<DeliveryModeModel> supportedDeliveryModes = getDeliveryService().getSupportedDeliveryModeListForOrder(
					cartModel);
			final CommerceCartParameter commerceCartParameter = new CommerceCartParameter();
			commerceCartParameter.setEnableHooks(true);
			commerceCartParameter.setCart(cartModel);

			getCommerceCartCalculationStrategy().calculateCart(commerceCartParameter);

			if (!supportedDeliveryModes.contains(currentDeliveryMode))
			{
				cartModel.setDeliveryMode(null);
				getModelService().save(cartModel);
				getModelService().refresh(cartModel);


				// Calculate cart after removing delivery mode
				getCommerceCartCalculationStrategy().calculateCart(commerceCartParameter);
			}
		}
	}

	protected CommerceCartCalculationStrategy getCommerceCartCalculationStrategy()
	{
		return commerceCartCalculationStrategy;
	}

	@Required
	public void setCommerceCartCalculationStrategy(final CommerceCartCalculationStrategy commerceCartCalculationStrategy)
	{
		this.commerceCartCalculationStrategy = commerceCartCalculationStrategy;
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

	protected DeliveryService getDeliveryService()
	{
		return deliveryService;
	}

	@Required
	public void setDeliveryService(final DeliveryService deliveryService)
	{
		this.deliveryService = deliveryService;
	}
}
