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
package de.hybris.platform.chinaaccelerator.services.location;

import de.hybris.platform.core.model.c2l.RegionModel;

import java.util.List;


public interface RegionService
{
	RegionModel getRegionByCountryAndCode(final String countryCode, final String regionCode);

	List<RegionModel> getRegionsForCountryCode(final String countryCode);

}
