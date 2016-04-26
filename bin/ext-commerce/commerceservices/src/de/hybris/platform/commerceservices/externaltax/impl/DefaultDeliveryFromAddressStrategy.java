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

import de.hybris.platform.commerceservices.externaltax.DeliveryFromAddressStrategy;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


public class DefaultDeliveryFromAddressStrategy implements DeliveryFromAddressStrategy
{
	private final static Logger LOG = Logger.getLogger(DefaultDeliveryFromAddressStrategy.class);
	private BaseStoreService baseStoreService;


	@Override
	public AddressModel getDeliveryFromAddressForOrder(final AbstractOrderModel model)
	{
		Assert.notNull(model, "Order should not be null");
		AddressModel address = null;
		if (hasOnlyShippingItems(model))
		{
			final BaseStoreModel baseStore = (model.getStore() != null ? model.getStore() : getBaseStoreService()
					.getCurrentBaseStore());
			if (baseStore != null)
			{
				address = getDeliveryOriginForWarehouses(baseStore.getWarehouses());
			}
		}
		else
		{
			Assert.notNull(model, "Order should not be null");
			if (CollectionUtils.isNotEmpty(model.getEntries()))
			{
				//Compute the value of the items per POS
				final Map<PointOfServiceModel, Double> posToPriceMap = new HashMap<PointOfServiceModel, Double>();
				for (final AbstractOrderEntryModel entry : model.getEntries())
				{
					final PointOfServiceModel pointOfService = entry.getDeliveryPointOfService();
					if (pointOfService != null)
					{
						if (posToPriceMap.get(pointOfService) == null)
						{
							posToPriceMap.put(pointOfService, Double.valueOf(0D));
						}
						posToPriceMap.put(pointOfService,
								Double.valueOf(posToPriceMap.get(pointOfService).doubleValue() + entry.getTotalPrice().doubleValue()));
					}
				}

				//Sort the POS per value and return the first one that has an address
				final List<Map.Entry<PointOfServiceModel, Double>> sortedPos = new ArrayList<Map.Entry<PointOfServiceModel, Double>>(
						posToPriceMap.entrySet());
				Collections.sort(sortedPos, new Comparator<Map.Entry<PointOfServiceModel, Double>>()
				{
					@Override
					public int compare(final Map.Entry<PointOfServiceModel, Double> o1, final Map.Entry<PointOfServiceModel, Double> o2)
					{
						return o2.getValue().compareTo(o1.getValue());
					}
				});
				for (final Map.Entry<PointOfServiceModel, Double> entry : sortedPos)
				{
					address = entry.getKey().getAddress();
					if (address != null)
					{
						return address;
					}
				}
			}

			return null;
		}

		if (address == null)
		{
			LOG.warn(String.format("Could not determine ship-from address for order %s", model.getCode()));
		}

		return address;
	}

	protected AddressModel getDeliveryOriginForWarehouses(final Collection<WarehouseModel> warehouseModels)
	{
		Assert.notEmpty(warehouseModels, "Warehouses must be not be empty");

		for (final WarehouseModel warehouseModel : warehouseModels)
		{
			if (Boolean.TRUE.equals(warehouseModel.getDefault()) && CollectionUtils.isNotEmpty(warehouseModel.getPointsOfService()))
			{
				for (final PointOfServiceModel pointOfServiceModel : warehouseModel.getPointsOfService())
				{
					if (pointOfServiceModel.getAddress() != null)
					{
						return pointOfServiceModel.getAddress();
					}
				}
			}
		}
		return null;
	}

	protected boolean hasOnlyShippingItems(final AbstractOrderModel orderModel)
	{
		return (null == CollectionUtils.find(orderModel.getEntries(), new Predicate()
		{
			@Override
			public boolean evaluate(final Object entry)
			{
				return ((AbstractOrderEntryModel) entry).getDeliveryPointOfService() != null;
			}
		}));
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}
}
