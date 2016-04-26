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
package de.hybris.platform.sap.model.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.sap.sapmodel.model.SAPProductSalesAreaToCatalogMappingModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Populator for additional fields specific for the SAPProductCatalog
 */
public class SAPProductCatalogMappingPopulator implements Populator<SAPProductSalesAreaToCatalogMappingModel, Map<String, Object>>
{
	protected static final Logger LOGGER = Logger.getLogger(SAPPricingCatalogMappingPopulator.class);
	
	public void populate(final SAPProductSalesAreaToCatalogMappingModel source, final Map<String, Object> target) throws ConversionException
	{
		CountryModel countryModel = source.getTaxClassCountry();
		
		if (countryModel == null)	
		{
			LOGGER.error("Missing customizing for Product to Catalog Mapping!");
			return;
		}

		target.put("taxClassCountry", source.getTaxClassCountry().getIsocode());
	}
}
