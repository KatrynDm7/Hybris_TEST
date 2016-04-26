/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.sap.sapproductavailability.constants;

/**
 * Global class for all Sapproductavailability constants. You can add global constants for your extension into this
 * class.
 */
public final class SapproductavailabilityConstants extends GeneratedSapproductavailabilityConstants
{
	public static final String EXTENSIONNAME = "sapproductavailability";

	public static final String SAP_PRODUCT_AVAILABILITY_BO = "sapProductAvailabilityBO";

	public static final String BAPI_MATERIAL_AVAILABILITY = "BAPI_MATERIAL_AVAILABILITY";

	public static final String SALES_ORG = "sapcommon_salesOrganization";
	public static final String DIS_CHANNEL = "sapcommon_distributionChannel";
	public static final String DIVISION = "sapcommon_division";

	public static final String ATPACTIVE = "sapproductavailability_atpActive";

	// plant customer + material
	public static final String BEAN_ID_CACHE_PLANT = "sapAtpCheckPlantCacheRegion";
	public static final Object CACHEKEY_SAP_ATP = "SAP_ATP";

	// plant meterial
	public static final String BEAN_ID_CACHE_PLANT_MATERIAL = "sapAtpCheckPlantMaterialCacheRegion";

	// plant customer
	public static final String BEAN_ID_CACHE_PLANT_CUSTOMER = "sapAtpCheckPlantCustomerCacheRegion";

	// availability
	public static final String BEAN_ID_CACHE_AVAILABILITY = "sapAtpCheckAvailabilityCacheRegion";







	private SapproductavailabilityConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension
}
