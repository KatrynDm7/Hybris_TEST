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

import java.util.List;

import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.product.daos.UnitDao;

public interface SAPUnitDao extends UnitDao
{
	
	
	public List<UnitModel> findUnitBySAPUnitCode(final String unitType);
}
