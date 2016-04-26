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
package de.hybris.platform.chinaaccelerator.services.location.impl;



import de.hybris.platform.chinaaccelerator.services.location.DistrictService;
import de.hybris.platform.chinaaccelerator.services.location.daos.DistrictDao;
import de.hybris.platform.chinaaccelerator.services.model.location.DistrictModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public class DefaultDistrictService implements DistrictService
{

	private DistrictDao districtDao;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.chinaacceleratorservices.core.service.DistrictService#getDistrictsByCityCode(java.lang.String)
	 */
	@Override
	public List<DistrictModel> getDistrictsByCityCode(final String cityCode)
	{
		return districtDao.findDistrictsByCityCode(cityCode);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.chinaacceleratorservices.core.service.DistrictService#getDistrictByCode(java.lang.String)
	 */
	@Override
	public DistrictModel getDistrictByCode(final String districtCode) throws UnknownIdentifierException
	{
		final DistrictModel result = districtDao.findDistrictByCode(districtCode);

		if (result == null)
		{
			throw new UnknownIdentifierException("District with code '" + districtCode + "' not found!");
		}
		return result;
	}

	@Required
	public void setDistrictDao(final DistrictDao districtDao)
	{
		this.districtDao = districtDao;
	}

}
