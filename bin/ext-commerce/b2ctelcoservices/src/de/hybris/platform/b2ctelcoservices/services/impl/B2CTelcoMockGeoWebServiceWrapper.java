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
package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.data.AddressData;
import de.hybris.platform.storelocator.exception.GeoServiceWrapperException;
import de.hybris.platform.storelocator.impl.CommerceMockGeoWebServiceWrapper;
import de.hybris.platform.storelocator.impl.DefaultGPS;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Mock implementation of de.hybris.platform.storelocator.GeoWebServiceWrapper returning evaluated coordinates of
 * requested address using coordinates of known PointOfService and extending the default implementation giving more
 * precise coordinates.
 */
public class B2CTelcoMockGeoWebServiceWrapper extends CommerceMockGeoWebServiceWrapper
{
	private static final Logger LOG = Logger.getLogger(B2CTelcoMockGeoWebServiceWrapper.class);

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Override
	public GPS geocodeAddress(final AddressData address) throws GeoServiceWrapperException
	{
		if ("exception".equals(address.getCity()))
		{
			throw new GeoServiceWrapperException();
		}
		if (!StringUtils.isEmpty(address.getCity()))
		{
			final String queryByCityNameOrPostalCodeString = "SELECT {pos:" + PointOfServiceModel.PK + "}" + "FROM {"
					+ PointOfServiceModel._TYPECODE + " AS pos JOIN " + AddressModel._TYPECODE + " AS a ON {pos."
					+ PointOfServiceModel.ADDRESS + "}={a." + AddressModel.PK + "} } " + "WHERE lower({a." + AddressModel.TOWN
					+ "}) = ?city OR {a." + AddressModel.POSTALCODE + "} = ?postalCode";
			FlexibleSearchQuery query = new FlexibleSearchQuery(queryByCityNameOrPostalCodeString);
			query.addQueryParameter("city", address.getCity().toLowerCase());
			query.addQueryParameter("postalCode", address.getCity());
			List<PointOfServiceModel> result = flexibleSearchService.<PointOfServiceModel>search(query).getResult();
			if (CollectionUtils.isNotEmpty(result))
			{
				return evaluateGeoCenter(result);
			}
			else
			{
				final String queryByPointOfServiceNameString = "SELECT {pos:" + PointOfServiceModel.PK + "}" + "FROM {"
						+ PointOfServiceModel._TYPECODE + " AS pos} " + "WHERE lower({pos." + PointOfServiceModel.NAME
						+ "}) LIKE CONCAT('%', CONCAT(?address, '%'))";
				query = new FlexibleSearchQuery(queryByPointOfServiceNameString);
				query.addQueryParameter("address", address.getCity().toLowerCase());
				result = flexibleSearchService.<PointOfServiceModel>search(query).getResult();
				if (CollectionUtils.isNotEmpty(result))
				{
					return evaluateGeoCenter(result);
				}
			}
		}
		return super.geocodeAddress(address);
	}

	@Override
	public GPS geocodeAddress(final Location location) throws GeoServiceWrapperException
	{
		return geocodeAddress(location.getAddressData());
	}

	/**
	 * Evaluates an approximate geo center for set of Point Of Services using an average of latitudes and longitudes for
	 * every Point Of Service.
	 *
	 * @param pointOfServiceList
	 *           - a set of PointOfServiceModel class instances whose coordinates are used to calculate the 'geo center'
	 * @return GPS coordinates
	 */
	protected GPS evaluateGeoCenter(final List<PointOfServiceModel> pointOfServiceList)
	{
		if (CollectionUtils.isNotEmpty(pointOfServiceList))
		{
			final int locationNumber = pointOfServiceList.size();
			double latitudeSum = 0;
			double longitudeSum = 0;
			for (final PointOfServiceModel pos : pointOfServiceList)
			{
				try
				{
					latitudeSum += pos.getLatitude().doubleValue();
					longitudeSum += pos.getLongitude().doubleValue();
				}
				catch (final NullPointerException e)
				{
					LOG.info("Found incomplete PointOfService \"" + pos.getName() + '"');
				}
			}
			return new DefaultGPS(latitudeSum / locationNumber, longitudeSum / locationNumber);
		}
		else
		{
			throw new IllegalArgumentException("Number of point of services must not be zero to evaluate correct 'geo center'.");
		}
	}
}
