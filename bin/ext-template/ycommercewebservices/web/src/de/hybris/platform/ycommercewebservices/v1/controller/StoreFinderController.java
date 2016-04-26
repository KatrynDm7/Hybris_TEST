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
package de.hybris.platform.ycommercewebservices.v1.controller;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import de.hybris.platform.commercefacades.storefinder.StoreFinderFacade;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.commerceservices.storefinder.data.StoreFinderSearchPageData;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Controller("storeFinderControllerV1")
public class StoreFinderController extends BaseController
{
	private static final String DEFAULT_SEARCH_RADIUS_METRES = "100000.0";
	private static final double EARTH_PERIMETER = 40075000.0;
	private static final String DEFAULT_ACCURACY = "0.0";
	@Resource(name = "storeFinderFacade")
	private StoreFinderFacade storeFinderFacade;

	@RequestMapping(value = "/{baseSiteId}/stores", method = RequestMethod.GET)
	@ResponseBody
	public StoreFinderSearchPageData<PointOfServiceData> locationSearch(@RequestParam(required = false) final String query,
			final Double latitude, final Double longitude,
			@RequestParam(required = false, defaultValue = "0") final int currentPage,
			@RequestParam(required = false, defaultValue = "10") final int pageSize,
			@RequestParam(required = false, defaultValue = "asc") final String sort,
			@RequestParam(required = false, defaultValue = "BASIC") final String options,
			@RequestParam(required = false, defaultValue = DEFAULT_SEARCH_RADIUS_METRES) final double radius,
			@RequestParam(required = false, defaultValue = DEFAULT_ACCURACY) final double accuracy) throws RequestParameterException
	{
		if (radius > EARTH_PERIMETER)
		{
			throw new RequestParameterException("Radius cannot be greater than Earth's perimeter",
					RequestParameterException.INVALID, "radius");
		}

		final double radiusToSearch = getInKilometres(radius, accuracy);
		final PageableData pageableData = createPagaable(currentPage, pageSize, sort);
		StoreFinderSearchPageData<PointOfServiceData> result = null;
		if (StringUtils.isNotBlank(query))
		{
			result = storeFinderFacade.locationSearch(query, pageableData, radiusToSearch);
		}
		else if (latitude != null && longitude != null)
		{
			final GeoPoint geoPoint = new GeoPoint();
			geoPoint.setLatitude(latitude.doubleValue());
			geoPoint.setLongitude(longitude.doubleValue());
			result = storeFinderFacade.positionSearch(geoPoint, pageableData, radiusToSearch);
		}
		else
		{
			result = storeFinderFacade.getAllPointOfServices(pageableData);
		}
		final List<PointOfServiceData> results = filterOptions(options, result);
		result.setResults(new ArrayList(results));

		return result;
	}

	@RequestMapping(value = "/{baseSiteId}/stores/{name}", method = RequestMethod.GET)
	@ResponseBody
	public PointOfServiceData locationDetails(@PathVariable final String name)
	{
		return storeFinderFacade.getPointOfServiceForName(name);
	}

	protected double getInKilometres(final double radius, final double accuracy)
	{
		return (radius + accuracy) / 1000.0;
	}

	protected List<PointOfServiceData> filterOptions(final String options,
			final StoreFinderSearchPageData<PointOfServiceData> result)
	{
		List<PointOfServiceData> results = null;
		if (!StringUtils.contains(options, "HOURS"))
		{
			results = Lists.transform(result.getResults(), new Function<PointOfServiceData, PointOfServiceData>()
			{

				@Override
				public PointOfServiceData apply(@Nullable final PointOfServiceData input)
				{
					input.setOpeningHours(null);
					input.setUrl(null);
					return input;
				}
			});
		}
		else
		{
			results = result.getResults();
		}
		return results;
	}

	protected PageableData createPagaable(final int page, final int pageSize, final String sort)
	{
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(page);
		pageableData.setPageSize(pageSize);
		pageableData.setSort(sort);
		return pageableData;
	}

}
