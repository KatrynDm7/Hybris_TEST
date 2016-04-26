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
package de.hybris.platform.storefront.checkout.steps.delivery;

import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.commerceservices.strategies.impl.DefaultDeliveryModeLookupStrategy;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * InsuranceDeliveryModeLookupStrategy set delivery mode at check out
 * */
public class InsuranceDeliveryModeLookupStrategy extends DefaultDeliveryModeLookupStrategy
{
	protected static final String DEFAULT_INSURANCE_DELIVERY_MODE = "insurance-default";

	private DeliveryService deliveryService;

	@Override
	public List<DeliveryModeModel> getSelectableDeliveryModesForOrder(final AbstractOrderModel abstractordermodel)
	{
		//there are no real delivery modes for insurance products, so it returns a default/mocked one
		final DeliveryModeModel deliveryMode = getDeliveryService().getDeliveryModeForCode(DEFAULT_INSURANCE_DELIVERY_MODE);

		if (deliveryMode == null)
		{
			return super.getSelectableDeliveryModesForOrder(abstractordermodel);
		}

		return Arrays.asList(deliveryMode);
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
