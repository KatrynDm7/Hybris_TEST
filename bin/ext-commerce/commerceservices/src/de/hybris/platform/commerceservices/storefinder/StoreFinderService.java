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
package de.hybris.platform.commerceservices.storefinder;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.commerceservices.storefinder.data.PointOfServiceDistanceData;
import de.hybris.platform.commerceservices.storefinder.data.StoreFinderSearchPageData;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;


/**
 * Store finder service. It is used for retrieving data for store finder related data.
 */
public interface StoreFinderService<ITEM extends PointOfServiceDistanceData, RESULT extends StoreFinderSearchPageData<ITEM>>
{
	/**
	 * Gets the locations retrieved based on the given arguments.
	 * 
	 * @param baseStore
	 *           {@link BaseStoreModel} instance that returned locations belongs to
	 * @param locationText
	 *           text that will be used for locations search
	 * @param pageableData
	 *           {@link PageableData} object that contains basing information for search result set
	 * @return locations found for the given parameters
	 */
	RESULT locationSearch(BaseStoreModel baseStore, String locationText, PageableData pageableData);

	/**
	 * Gets the locations retrieved based on the given arguments.
	 * 
	 * @param baseStore
	 *           {@link BaseStoreModel} instance that returned locations belongs to
	 * @param locationText
	 *           text that will be used for locations search
	 * @param pageableData
	 *           {@link PageableData} object that contains basing information for search result set
	 * @param maxRadius
	 *           the maximum radius from the location that results should be returned for.
	 * @return locations found for the given parameters
	 */
	RESULT locationSearch(BaseStoreModel baseStore, String locationText, PageableData pageableData, double maxRadius);


	/**
	 * Gets the locations retrieved basing on the given arguments.
	 * 
	 * @param baseStore
	 *           {@link BaseStoreModel} instance that returned locations belongs to
	 * @param geoPoint
	 *           geographical point that search is performed for
	 * @param pageableData
	 *           {@link PageableData} object that contains basing information for search result set
	 * @return locations found for the given parameters
	 */
	RESULT positionSearch(BaseStoreModel baseStore, GeoPoint geoPoint, PageableData pageableData);

	/**
	 * Gets the locations retrieved basing on the given arguments.
	 * 
	 * @param baseStore
	 *           {@link BaseStoreModel} instance that returned locations belongs to
	 * @param geoPoint
	 *           geographical point that search is performed for
	 * @param pageableData
	 *           {@link PageableData} object that contains basing information for search result set
	 * @param maxRadius
	 *           the maximum radius from the location that results should be returned for.
	 * @return locations found for the given parameters
	 */
	RESULT positionSearch(BaseStoreModel baseStore, GeoPoint geoPoint, PageableData pageableData, double maxRadius);

	/**
	 * Gets the {@link PointOfServiceModel} for the given name that should be unique
	 * 
	 * @param name
	 *           the requested point of service name
	 * @return {@link PointOfServiceModel} for the given parameter
	 */
	PointOfServiceModel getPointOfServiceForName(BaseStoreModel baseStore, String name);


	/**
	 * Gets the location for the given name that should be unique. Additionally the distance between the location and
	 * given coordinates is calculated.
	 * 
	 * @param name
	 *           name of the requested point of service
	 * @param geoPoint
	 *           geographical point of the origin point
	 * @return location for the given parameter
	 */
	ITEM getPointOfServiceDistanceForName(BaseStoreModel baseStore, String name, GeoPoint geoPoint);

	/**
	 * Gets the locations retrieved basing on the given arguments.
	 * 
	 * @param baseStore
	 *           {@link BaseStoreModel} instance that returned locations belongs to
	 * @param pageableData
	 *           {@link PageableData} object that contains basing information for search result set
	 * 
	 * @return locations found for the given parameters
	 */
	RESULT getAllPos(BaseStoreModel baseStore, PageableData pageableData);
}
