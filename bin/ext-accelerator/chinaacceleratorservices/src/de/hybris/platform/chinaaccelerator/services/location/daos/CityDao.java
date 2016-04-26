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
package de.hybris.platform.chinaaccelerator.services.location.daos;




import de.hybris.platform.chinaaccelerator.services.model.location.CityModel;

import java.util.List;


public interface CityDao
{

	List<CityModel> findCitiesByRegionCode(final String regionCode);

	CityModel findCityForCode(final String cityCode);
}
