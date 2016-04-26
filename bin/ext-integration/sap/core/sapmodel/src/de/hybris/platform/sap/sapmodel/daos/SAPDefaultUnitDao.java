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
package de.hybris.platform.sap.sapmodel.daos;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import java.util.Collections;
import java.util.List;

import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.product.daos.impl.DefaultUnitDao;

public class SAPDefaultUnitDao extends DefaultUnitDao implements SAPUnitDao
{
	
	public SAPDefaultUnitDao()
	{
		super(UnitModel._TYPECODE);
	}
	public SAPDefaultUnitDao(final String typecode)
	{
		super(typecode);
	}
	
	
	public List<UnitModel> findUnitBySAPUnitCode(final String unitType)
	{
		validateParameterNotNull(unitType, "unitType must not be null!");
		final List<UnitModel> result = find(Collections.singletonMap(UnitModel.SAPCODE, (Object) unitType));
		return result;
	}
	
}
