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
import de.hybris.platform.commerceservices.order.CommerceDeliveryAddressStrategy;
import de.hybris.platform.commerceservices.order.CommerceDeliveryModeValidationStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.servicelayer.util.ServicesUtil.*;

public class DefaultCommerceDeliveryAddressStrategy implements CommerceDeliveryAddressStrategy
{
	private ModelService modelService;
	private DeliveryService deliveryService;
	private CommerceCartCalculationStrategy commerceCartCalculationStrategy;
	private CommerceDeliveryModeValidationStrategy commerceDeliveryModeValidationStrategy;

	@Override
	public boolean storeDeliveryAddress(final CommerceCheckoutParameter parameter)
	{

		final CartModel cartModel = parameter.getCart();
		final AddressModel addressModel = parameter.getAddress();
		final boolean flagAsDeliveryAddress = parameter.isIsDeliveryAddress();

		validateParameterNotNull(cartModel, "Cart model cannot be null");
		getModelService().refresh(cartModel);

		final UserModel user = cartModel.getUser();
		getModelService().refresh(user);

		cartModel.setDeliveryAddress(addressModel);

		// Check that the address model belongs to the same user as the cart
		if (isValidDeliveryAddress(cartModel, addressModel))
		{
			getModelService().save(cartModel);

			if (addressModel != null && flagAsDeliveryAddress && !Boolean.TRUE.equals(addressModel.getShippingAddress()))
			{
				// Flag the address as a delivery address
				addressModel.setShippingAddress(Boolean.TRUE);
				getModelService().save(addressModel);
			}
			getCommerceCartCalculationStrategy().calculateCart(cartModel);
			// verify if the current delivery mode is still valid for this address
			getCommerceDeliveryModeValidationStrategy().validateDeliveryMode(parameter);
			getModelService().refresh(cartModel);

			return true;
		}

		return false;
	}

	protected boolean isValidDeliveryAddress(final CartModel cartModel, final AddressModel addressModel)
	{
		if (addressModel != null)
		{
			final List<AddressModel> supportedAddresses = getDeliveryService().getSupportedDeliveryAddressesForOrder(cartModel,
					false);
			return supportedAddresses != null && supportedAddresses.contains(addressModel);
		}
		else
		{
			return true;
		}
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

	protected CommerceCartCalculationStrategy getCommerceCartCalculationStrategy()
	{
		return commerceCartCalculationStrategy;
	}

	@Required
	public void setCommerceCartCalculationStrategy(final CommerceCartCalculationStrategy commerceCartCalculationStrategy)
	{
		this.commerceCartCalculationStrategy = commerceCartCalculationStrategy;
	}

	protected CommerceDeliveryModeValidationStrategy getCommerceDeliveryModeValidationStrategy()
	{
		return commerceDeliveryModeValidationStrategy;
	}

	@Required
	public void setCommerceDeliveryModeValidationStrategy(final CommerceDeliveryModeValidationStrategy commerceDeliveryModeValidationStrategy)
	{
		this.commerceDeliveryModeValidationStrategy = commerceDeliveryModeValidationStrategy;
	}
}
