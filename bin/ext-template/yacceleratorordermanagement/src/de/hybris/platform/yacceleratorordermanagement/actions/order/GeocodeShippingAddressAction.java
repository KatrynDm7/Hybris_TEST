/*
 *
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
 */
package de.hybris.platform.yacceleratorordermanagement.actions.order;

import de.hybris.platform.commerceservices.model.PickUpDeliveryModeModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.GeoWebServiceWrapper;
import de.hybris.platform.storelocator.data.AddressData;
import de.hybris.platform.storelocator.exception.GeoServiceWrapperException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * Used to get an address from order.deliveryAddress and find the closest store populating back the
 * delivery latitude/longitude.
 */
public class GeocodeShippingAddressAction extends AbstractProceduralAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(GeocodeShippingAddressAction.class);

	private GeoWebServiceWrapper geoWebServiceWrapper;
	private Converter<AddressModel, AddressData> addressConverter;

	@Override
	public void executeAction(final OrderProcessModel orderProcessModel)
	{
		LOG.info("Process: " + orderProcessModel.getCode() + " in step " + getClass().getSimpleName());

		final OrderModel order = orderProcessModel.getOrder();
		try
		{
			if (!(order.getDeliveryMode() instanceof PickUpDeliveryModeModel))
			{
				LOG.debug("Getting GPS from delivery address...");
				final GPS gps = getGeoWebServiceWrapper().geocodeAddress(
						getAddressConverter().convert(order.getDeliveryAddress()));

				if (LOG.isInfoEnabled())
				{
					LOG.info(String.format("Setting up latitude: %1$,.6f and longitude: %2$,.6f to the order",
							gps.getDecimalLatitude(), gps.getDecimalLongitude()));

				}
				final AddressModel deliveryAddress = order.getDeliveryAddress();

				if (deliveryAddress != null)
				{
					deliveryAddress.setLatitude(gps.getDecimalLatitude());
					deliveryAddress.setLongitude(gps.getDecimalLongitude());
				}
				getModelService().save(deliveryAddress);
			}
		}
		catch (final ConversionException | GeoServiceWrapperException e)
		{
			LOG.info("Fail to obtain geocode from order.deliveryAddress, error message: " + e.getMessage());
		}
	}

	@Required
	public void setGeoWebServiceWrapper(final GeoWebServiceWrapper geoWebServiceWrapper)
	{
		this.geoWebServiceWrapper = geoWebServiceWrapper;
	}

	@Required
	public void setAddressConverter(final Converter<AddressModel, AddressData> addressConverter)
	{
		this.addressConverter = addressConverter;
	}

	protected GeoWebServiceWrapper getGeoWebServiceWrapper()
	{
		return geoWebServiceWrapper;
	}

	protected Converter<AddressModel, AddressData> getAddressConverter()
	{
		return addressConverter;
	}
}
