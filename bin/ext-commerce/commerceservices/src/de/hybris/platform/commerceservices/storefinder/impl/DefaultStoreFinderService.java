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
package de.hybris.platform.commerceservices.storefinder.impl;


import de.hybris.platform.basecommerce.enums.PointOfServiceTypeEnum;
import de.hybris.platform.commerceservices.search.dao.PagedGenericDao;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.commerceservices.storefinder.StoreFinderService;
import de.hybris.platform.commerceservices.storefinder.data.PointOfServiceDistanceData;
import de.hybris.platform.commerceservices.storefinder.data.StoreFinderSearchPageData;
import de.hybris.platform.commerceservices.util.AbstractComparator;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.GeoWebServiceWrapper;
import de.hybris.platform.storelocator.PointOfServiceDao;
import de.hybris.platform.storelocator.data.AddressData;
import de.hybris.platform.storelocator.exception.GeoLocatorException;
import de.hybris.platform.storelocator.exception.GeoServiceWrapperException;
import de.hybris.platform.storelocator.exception.LocationServiceException;
import de.hybris.platform.storelocator.exception.PointOfServiceDaoException;
import de.hybris.platform.storelocator.impl.DefaultGPS;
import de.hybris.platform.storelocator.impl.GeometryUtils;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 * Default implementation of {@link StoreFinderService}
 */
public class DefaultStoreFinderService<ITEM extends PointOfServiceDistanceData> implements
		StoreFinderService<ITEM, StoreFinderSearchPageData<ITEM>>
{
	private static final Logger LOG = Logger.getLogger(DefaultStoreFinderService.class);

	private PointOfServiceDao pointOfServiceDao;
	private GeoWebServiceWrapper geoWebServiceWrapper;
	private PagedGenericDao<PointOfServiceModel> pointOfServicePagedGenericDao;
	private GenericDao<PointOfServiceModel> pointOfServiceGenericDao;

	protected GenericDao<PointOfServiceModel> getPointOfServiceGenericDao()
	{
		return pointOfServiceGenericDao;
	}

	@Required
	public void setPointOfServiceGenericDao(final GenericDao<PointOfServiceModel> pointOfServiceGenericDao)
	{
		this.pointOfServiceGenericDao = pointOfServiceGenericDao;
	}

	protected PointOfServiceDao getPointOfServiceDao()
	{
		return pointOfServiceDao;
	}

	@Required
	public void setPointOfServiceDao(final PointOfServiceDao pointOfServiceDao)
	{
		this.pointOfServiceDao = pointOfServiceDao;
	}

	protected GeoWebServiceWrapper getGeoWebServiceWrapper()
	{
		return geoWebServiceWrapper;
	}

	@Required
	public void setGeoWebServiceWrapper(final GeoWebServiceWrapper geoWebServiceWrapper)
	{
		this.geoWebServiceWrapper = geoWebServiceWrapper;
	}

	protected PagedGenericDao<PointOfServiceModel> getPointOfServicePagedGenericDao()
	{
		return pointOfServicePagedGenericDao;
	}

	@Required
	public void setPointOfServicePagedGenericDao(final PagedGenericDao<PointOfServiceModel> pointOfServicePagedGenericDao)
	{
		this.pointOfServicePagedGenericDao = pointOfServicePagedGenericDao;
	}

	// ----

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.acceleratorservices.storefinder.StoreFinderService#locationSearch(de.hybris.platform.store.
	 * BaseStoreModel, java.lang.String, de.hybris.platform.commerceservices.search.pagedata.PageableData)
	 */
	@Override
	public StoreFinderSearchPageData<ITEM> locationSearch(final BaseStoreModel baseStore, final String locationText,
			final PageableData pageableData)
	{
		final GeoPoint geoPoint = new GeoPoint();

		if (locationText != null && !locationText.isEmpty())
		{
			try
			{
				// Resolve the address to a point
				final GPS resolvedPoint = getGeoWebServiceWrapper().geocodeAddress(
						generateGeoAddressForSearchQuery(baseStore, locationText));

				geoPoint.setLatitude(resolvedPoint.getDecimalLatitude());
				geoPoint.setLongitude(resolvedPoint.getDecimalLongitude());

				return doSearch(baseStore, locationText, geoPoint, pageableData, baseStore.getMaxRadiusForPoSSearch());
			}
			catch (final GeoServiceWrapperException ex)
			{
				LOG.info("Failed to resolve location for [" + locationText + "]");
			}
		}

		// Return no results
		return createSearchResult(locationText, geoPoint, Collections.<ITEM> emptyList(), createPaginationData());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.acceleratorservices.storefinder.StoreFinderService#locationSearch(de.hybris.platform.store.
	 * BaseStoreModel, java.lang.String, de.hybris.platform.commerceservices.search.pagedata.PageableData, int)
	 */
	@Override
	public StoreFinderSearchPageData<ITEM> locationSearch(final BaseStoreModel baseStore, final String locationText,
			final PageableData pageableData, final double maxRadiusKm)
	{
		final GeoPoint geoPoint = new GeoPoint();

		if (locationText != null && !locationText.isEmpty())
		{
			try
			{
				// Resolve the address to a point
				final GPS resolvedPoint = getGeoWebServiceWrapper().geocodeAddress(
						generateGeoAddressForSearchQuery(baseStore, locationText));

				geoPoint.setLatitude(resolvedPoint.getDecimalLatitude());
				geoPoint.setLongitude(resolvedPoint.getDecimalLongitude());

				return doSearch(baseStore, locationText, geoPoint, pageableData, Double.valueOf(maxRadiusKm));
			}
			catch (final GeoServiceWrapperException ex)
			{
				LOG.info("Failed to resolve location for [" + locationText + "]");
			}
		}

		// Return no results
		return createSearchResult(locationText, geoPoint, Collections.<ITEM> emptyList(), createPaginationData());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.acceleratorservices.storefinder.StoreFinderService#positionSearch(de.hybris.platform.store.
	 * BaseStoreModel, double, double, de.hybris.platform.commerceservices.search.pagedata.PageableData)
	 */
	@Override
	public StoreFinderSearchPageData<ITEM> positionSearch(final BaseStoreModel baseStore, final GeoPoint geoPoint,
			final PageableData pageableData)
	{
		return doSearch(baseStore, null, geoPoint, pageableData, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.acceleratorservices.storefinder.StoreFinderService#positionSearch(de.hybris.platform.store.
	 * BaseStoreModel, double, double, de.hybris.platform.commerceservices.search.pagedata.PageableData, int)
	 */
	@Override
	public StoreFinderSearchPageData<ITEM> positionSearch(final BaseStoreModel baseStore, final GeoPoint geoPoint,
			final PageableData pageableData, final double maxRadius)
	{
		return doSearch(baseStore, null, geoPoint, pageableData, Double.valueOf(maxRadius));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.acceleratorservices.storefinder.StoreFinderService#getPointOfServiceForName(de.hybris.platform
	 * .store.BaseStoreModel, java.lang.String)
	 */
	@Override
	public PointOfServiceModel getPointOfServiceForName(final BaseStoreModel baseStore, final String name)
	{
		Assert.notNull(baseStore);
		Assert.notNull(name);

		final Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("name", name);
		paramMap.put("baseStore", baseStore);
		paramMap.put("type", PointOfServiceTypeEnum.STORE);

		final List<PointOfServiceModel> posModels = getPointOfServiceGenericDao().find(paramMap);
		if (posModels != null && !posModels.isEmpty())
		{
			return posModels.get(0);
		}

		throw new ModelNotFoundException("No PointOfService with name " + name + ", type STORE found in baseStore "
				+ baseStore.getUid());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.acceleratorservices.storefinder.StoreFinderService#getPointOfServiceDistanceForName(de.hybris
	 * .platform.store.BaseStoreModel, java.lang.String, double, double)
	 */
	@Override
	public ITEM getPointOfServiceDistanceForName(final BaseStoreModel baseStore, final String name, final GeoPoint geoPoint)
	{
		final PointOfServiceModel pointOfService = getPointOfServiceForName(baseStore, name);
		if (pointOfService != null)
		{
			final ITEM storeFinderResultData = createStoreFinderResultData();
			storeFinderResultData.setPointOfService(pointOfService);
			storeFinderResultData.setDistanceKm(calculateDistance(geoPoint, pointOfService));
			return storeFinderResultData;
		}
		return null;
	}

	@Override
	public StoreFinderSearchPageData<ITEM> getAllPos(final BaseStoreModel baseStore, final PageableData pageableData)
	{
		final Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("baseStore", baseStore);
		paramMap.put("type", PointOfServiceTypeEnum.STORE);

		final SearchPageData<PointOfServiceModel> posResults = getPointOfServicePagedGenericDao().find(paramMap, pageableData);

		final List<ITEM> results = new ArrayList<ITEM>();

		for (final PointOfServiceModel pointOfService : posResults.getResults())
		{
			final ITEM storeFinderResultData = createStoreFinderResultData();
			storeFinderResultData.setPointOfService(pointOfService);
			results.add(storeFinderResultData);
		}

		final StoreFinderSearchPageData<ITEM> searchPageData = createStoreFinderSearchPageData();
		searchPageData.setResults(results);
		searchPageData.setPagination(posResults.getPagination());
		searchPageData.setSorts(posResults.getSorts());

		return searchPageData;

	}

	// ----------

	/**
	 * This method generates an {@link AddressData} object based on a search location text. It sets country to the
	 * {@link AddressData} object from the first POS country in the list of base store POS. Adding the country to the
	 * {@link AddressData} object will help to narrow the search region. It must be noted however that the country is
	 * only set if the search text does not contains any comma separators whose presence indicates that the user is
	 * attempting to enter multiple fields, post code, country, etc in an attempt to narrow the search area.
	 *
	 * @param baseStore
	 *           - the base store for the current site.
	 * @param locationText
	 *           - the search location text to base the search upon.
	 * @return an {@link AddressData} object.
	 */
	protected AddressData generateGeoAddressForSearchQuery(final BaseStoreModel baseStore, final String locationText)
	{
		String country = null; //Only null check is done in GoogleMapTools so default to this.

		if (locationText != null && !locationText.contains(","))
		{
			final Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("baseStore", baseStore);
			paramMap.put("type", PointOfServiceTypeEnum.STORE);
			final Collection<PointOfServiceModel> allPos = getPointOfServiceGenericDao().find(paramMap);

			if (allPos != null && allPos.iterator().hasNext())
			{
				final PointOfServiceModel pos = allPos.iterator().next();
				if (pos != null && pos.getAddress() != null && pos.getAddress().getCountry() != null)
				{
					country = pos.getAddress().getCountry().getIsocode();
				}
			}
		}
		// Create an address data
		final AddressData addressData = new AddressData();
		addressData.setCity(locationText);
		addressData.setCountryCode(country);

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Generated Geo Address Data: City[" + locationText + "] - Country[" + country + "]");
		}

		return addressData;
	}

	protected StoreFinderSearchPageData<ITEM> doSearch(final BaseStoreModel baseStore, final String locationText,
			final GeoPoint centerPoint, final PageableData pageableData, final Double maxRadiusKm)
	{
		final Collection<PointOfServiceModel> posResults;

		final int resultRangeStart = pageableData.getCurrentPage() * pageableData.getPageSize();
		final int resultRangeEnd = (pageableData.getCurrentPage() + 1) * pageableData.getPageSize();

		if (maxRadiusKm != null)
		{
			posResults = getPointsOfServiceNear(centerPoint, maxRadiusKm.doubleValue(), baseStore);
		}
		else
		{
			final Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("baseStore", baseStore);
			paramMap.put("type", PointOfServiceTypeEnum.STORE);
			posResults = getPointOfServiceGenericDao().find(paramMap);
		}

		if (posResults != null)
		{
			// Sort all the POS
			final List<ITEM> orderedResults = calculateDistances(centerPoint, posResults);
			final PaginationData paginationData = createPagination(pageableData, posResults.size());
			// Slice the required range window out of the results
			final List<ITEM> orderedResultsWindow = orderedResults.subList(Math.min(orderedResults.size(), resultRangeStart),
					Math.min(orderedResults.size(), resultRangeEnd));

			return createSearchResult(locationText, centerPoint, orderedResultsWindow, paginationData);
		}

		// Return no results
		return createSearchResult(locationText, centerPoint, Collections.<ITEM> emptyList(), createPagination(pageableData, 0));
	}

	// ----------

	protected PaginationData createPagination(final PageableData pageableData, final long totalNumberOfResults)
	{
		final PaginationData paginationData = createPaginationData();

		// Set the page size and and don't allow it to be less than 1
		paginationData.setPageSize(Math.max(1, pageableData.getPageSize()));

		paginationData.setTotalNumberOfResults(totalNumberOfResults);

		// Calculate the number of pages
		paginationData.setNumberOfPages((int) Math.ceil(((double) totalNumberOfResults) / paginationData.getPageSize()));

		// Work out the current page, fixing any invalid page values
		paginationData.setCurrentPage(Math.max(0, Math.min(paginationData.getNumberOfPages(), pageableData.getCurrentPage())));

		return paginationData;
	}

	protected StoreFinderSearchPageData<ITEM> createSearchResult(final String locationText, final GeoPoint centerPoint,
			final List<ITEM> results, final PaginationData paginationData)
	{
		final StoreFinderSearchPageData<ITEM> searchPageData = createStoreFinderSearchPageData();
		searchPageData.setResults(results);
		searchPageData.setPagination(paginationData);

		searchPageData.setLocationText(locationText);
		searchPageData.setSourceLatitude(centerPoint.getLatitude());
		searchPageData.setSourceLongitude(centerPoint.getLongitude());

		final Boundary<GeoPoint> calculatedBounds = calculateBounds(results, centerPoint);

		searchPageData.setBoundNorthLatitude(calculatedBounds.getMin().getLatitude());
		searchPageData.setBoundEastLongitude(calculatedBounds.getMin().getLongitude());
		searchPageData.setBoundSouthLatitude(calculatedBounds.getMax().getLatitude());
		searchPageData.setBoundWestLongitude(calculatedBounds.getMax().getLongitude());

		return searchPageData;
	}

	protected Boundary<GeoPoint> calculateBounds(final List<ITEM> results, final GeoPoint centerPoint)
	{
		Boundary<GeoPoint> calculatedBoundary = new Boundary<GeoPoint>(new GeoPoint(), new GeoPoint());
		if (CollectionUtils.isNotEmpty(results) && centerPoint != null)
		{
			GeoPoint northEast = null;
			GeoPoint southWest = null;
			boolean foundPoint = false;

			for (final ITEM result : results)
			{
				final Double latitude = result.getPointOfService().getLatitude();
				final Double longitude = result.getPointOfService().getLongitude();

				if (latitude != null && longitude != null)
				{
					foundPoint = true;
					if (northEast == null)
					{
						northEast = new GeoPoint();
						northEast.setLatitude(latitude.doubleValue());
						northEast.setLongitude(longitude.doubleValue());
					}
					else
					{
						northEast.setLatitude(Math.max(northEast.getLatitude(), latitude.doubleValue()));
						northEast.setLongitude(Math.max(northEast.getLongitude(), longitude.doubleValue()));
					}

					if (southWest == null)
					{
						southWest = new GeoPoint();
						southWest.setLatitude(latitude.doubleValue());
						southWest.setLongitude(longitude.doubleValue());
					}
					else
					{
						southWest.setLatitude(Math.min(southWest.getLatitude(), latitude.doubleValue()));
						southWest.setLongitude(Math.min(southWest.getLongitude(), longitude.doubleValue()));
					}
					if (LOG.isDebugEnabled())
					{
						LOG.debug(" bounds coords NE(" + northEast + "), SW(" + southWest + ") after a point (" + latitude + ","
								+ longitude + ")");
					}
				}
			}

			if (foundPoint)
			{
				final Boundary<Double> spanLat = recalculateSpanAgainstCenter(southWest.getLatitude(), northEast.getLatitude(),
						centerPoint.getLatitude());
				final Boundary<Double> spanLong = recalculateSpanAgainstCenter(southWest.getLongitude(), northEast.getLongitude(),
						centerPoint.getLongitude());
				if (LOG.isDebugEnabled())
				{
					LOG.debug(" recalculated coords NE(" + northEast + "), SW(" + southWest + ") for a center point " + centerPoint);
				}
				northEast = new GeoPoint();
				northEast.setLatitude(spanLat.getMax().doubleValue());
				northEast.setLongitude(spanLong.getMax().doubleValue());
				southWest = new GeoPoint();
				southWest.setLatitude(spanLat.getMin().doubleValue());
				southWest.setLongitude(spanLong.getMin().doubleValue());
				calculatedBoundary = new Boundary<GeoPoint>(northEast, southWest);
			}
		}

		return calculatedBoundary;
	}

	protected Boundary<Double> recalculateSpanAgainstCenter(final double leftBorder, final double rightBorder,
			final double centerPosition) throws GeoLocatorException
	{
		final double leftSpan = leftBorder - centerPosition;
		final double rightSpan = rightBorder - centerPosition;
		final double max, min;

		if (Math.abs(leftSpan) > Math.abs(rightSpan))
		{
			max = centerPosition + Math.abs(leftSpan);
			min = centerPosition - Math.abs(leftSpan);
		}
		else
		{
			min = centerPosition - Math.abs(rightSpan);
			max = centerPosition + Math.abs(rightSpan);
		}

		return new Boundary<Double>(Double.valueOf(min), Double.valueOf(max));
	}

	protected Collection<PointOfServiceModel> getPointsOfServiceNear(final GeoPoint centerPoint, final double radiusKm,
			final BaseStoreModel baseStore) throws PointOfServiceDaoException
	{
		final GPS referenceGps = new DefaultGPS(centerPoint.getLatitude(), centerPoint.getLongitude());
		return getPointOfServiceDao().getAllGeocodedPOS(referenceGps, radiusKm, baseStore);
	}

	protected List<ITEM> calculateDistances(final GeoPoint centerPoint, final Collection<PointOfServiceModel> pointsOfService)
	{
		final List<ITEM> result = new ArrayList<ITEM>();

		for (final PointOfServiceModel pointOfService : pointsOfService)
		{
			final ITEM storeFinderResultData = createStoreFinderResultData();
			storeFinderResultData.setPointOfService(pointOfService);
			storeFinderResultData.setDistanceKm(calculateDistance(centerPoint, pointOfService));
			result.add(storeFinderResultData);
		}

		Collections.sort(result, StoreFinderResultDataComparator.INSTANCE);

		return result;
	}

	protected double calculateDistance(final GeoPoint centerPoint, final PointOfServiceModel posModel) throws GeoLocatorException,
			LocationServiceException
	{
		if (posModel.getLatitude() != null && posModel.getLongitude() != null)
		{
			final GPS positionGPS = new DefaultGPS(posModel.getLatitude().doubleValue(), posModel.getLongitude().doubleValue());
			final GPS referenceGps = new DefaultGPS(centerPoint.getLatitude(), centerPoint.getLongitude());
			return GeometryUtils.getElipticalDistanceKM(referenceGps, positionGPS);
		}
		throw new LocationServiceException("Unable to calculate a distance for PointOfService(" + posModel
				+ ") due to missing  latitude, longitude value");
	}

	public static class StoreFinderResultDataComparator extends AbstractComparator<PointOfServiceDistanceData>
	{
		public static final StoreFinderResultDataComparator INSTANCE = new StoreFinderResultDataComparator();

		@Override
		protected int compareInstances(final PointOfServiceDistanceData result1, final PointOfServiceDistanceData result2)
		{
			int result = compareValues(result1.getDistanceKm(), result2.getDistanceKm());
			if (EQUAL == result)
			{
				result = compareValues(result1.getPointOfService().getName(), result2.getPointOfService().getName(), false);
			}
			return result;
		}
	}

	// --------

	protected StoreFinderSearchPageData<ITEM> createStoreFinderSearchPageData()
	{
		return new StoreFinderSearchPageData<ITEM>();
	}

	protected ITEM createStoreFinderResultData()
	{
		return (ITEM) new PointOfServiceDistanceData();
	}

	protected PaginationData createPaginationData()
	{
		return new PaginationData();
	}

	public static class Boundary<T>
	{
		private final T min;
		private final T max;

		public Boundary(final T min, final T max)
		{
			this.min = min;
			this.max = max;
		}

		public T getMax()
		{
			return max;
		}

		public T getMin()
		{
			return min;
		}

		@Override
		public String toString()
		{
			return "<" + min + "," + max + ">";
		}
	}

}
