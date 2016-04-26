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
package de.hybris.platform.sap.sappricing.services.impl;

import org.springframework.beans.factory.annotation.Required;

import sap.hybris.integration.models.services.impl.SAPDefaultUnitService;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.sap.sappricingbol.converter.ConversionService;

public class DefaultConversionService implements ConversionService
{
	private SAPDefaultUnitService unitService;
	
	
	protected SAPDefaultUnitService getUnitService()
	{
		return unitService;
	}
	@Required
	public void setUnitService(SAPDefaultUnitService unitService)
	{
		this.unitService = unitService;
	}

	@Override
	public String getSAPUnitforISO(String code)
	{
		UnitModel unit = this.getUnitService().getUnitForCode(code);
		if (unit == null)
		{
			return null; 
		}
		return unit.getSapCode();
	}

	@Override
	public String getISOUnitforSAP(String code)
	{
		
		UnitModel unit =this.getUnitService().getUnitForSAPCode(code);
		if (unit == null)
		{
			return null;
		}
		
		return unit.getCode();
	}

}
