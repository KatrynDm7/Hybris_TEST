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
package de.hybris.platform.financialfacades.constants;


/**
 * Global class for all Financialfacades constants. You can add global constants for your extension into this class.
 */
@SuppressWarnings("deprecation")
public final class FinancialfacadesConstants extends GeneratedFinancialfacadesConstants
{
	public static final String EXTENSIONNAME = "financialfacades";

	private FinancialfacadesConstants()
	{
		//empty to avoid instantiating this constant class
	}

	public static final String INSURANCE_GENERIC_DATE_FORMAT = "yyyy-MM-dd";

	public static final String TRIP_DETAILS_FORM_DATA_ID = "tripDetailsFormDataId";
	public static final String TRIP_DETAILS_DESTINATION = "tripDestination";
	public static final String TRIP_DETAILS_START_DATE = "tripStartDate";
	public static final String TRIP_DETAILS_END_DATE = "tripEndDate";
	public static final String TRIP_COST = "costOfTrip";
	public static final String COST_OF_TRIP_DIVISOR = "travel.cost.of.trip.divisor";
	public static final String TRIP_DETAILS_NO_OF_DAYS = "NoOfDays";
	public static final String TRIP_DETAILS_NO_OF_TRAVELLERS = "Travellers";
	public static final String TRIP_DETAILS_TRAVELLER_AGES = "tripDetailsTravellerAges";
	public static final String TRIP_DETAILS_TRAVELLER_AGES_FOR_PRE_FORM_POPULATE = "tripDetailsTravellerAgesForPreFormPopulate";

	public static final String TEST_TRAVEL_INSURANCE_DATE_DISPLAY_FORMAT = "dd/MM/yyyy";

	public static final String INSURANCE_STORED_CUSTOMER_FORM = "insuranceStoredCustomerForm";

	public static final String PROPERTY_DETAILS_FORM_DATA_ID = "propertyDetailsFormDataId";
	public static final String PROPERTY_FORM_SESSION_MAP = "propertyFormSessionMap";
	public static final String NOT_APPLICABLE_TEXT = "n/a";

	public static final String PROPERTY_DETAILS_VALUE = "propertyValue";
	public static final String PROPERTY_DETAILS_TYPE = "propertyType";
	public static final String PROPERTY_DETAILS_REBUILD_COST = "propertyRebuildCost";
	public static final String PROPERTY_DETAILS_IS_STANDARD_50000_CONTENT_COVER = "propertyIsStandard50000ContentCover";
	public static final String PROPERTY_DETAILS_CONTENT_COVER_MULTIPLE_OF_10000 = "propertyMultipleOf10000ContentCover";

	public static final String PROPERTY_ADDRESS1 = "property-address-line-1";
	public static final String PROPERTY_ADDRESS2 = "property-address-line-2";
	public static final String PROPERTY_CITY = "property-address-city";
	public static final String PROPERTY_POSTCODE = "property-address-postcode";
	public static final String PROPERTY_COUNTRY = "property-address-country";

	public static final String PROPERTY_DETAILS_COVER_REQUIRED = "propertyDetailsCoverRequired";
	public static final String PROPERTY_DETAILS_START_DATE = "propertyDetailsStartDate";

	public static final String AUTO_VEHICLE_VALUE = "vehicleValue";
	public static final String AUTO_DRIVER_DOB = "driverDob";
	public static final String AUTO_VEHICLE_MAKE = "vehicleMake";
	public static final String AUTO_VEHICLE_MODEL = "vehicleModel";
	public static final String AUTO_VEHICLE_YEAR = "vehicleYear";
	public static final String AUTO_VEHICLE_LICENSE = "vehicleLicense";
	public static final String DOCUMENTS_PROVIDEDED = "Provided";
	public static final String DOCUMENTS_WAITING = "Waiting";
	public static final String AUTO_COVER_START = "coverageStartDate";
	public static final String AUTO_STATE = "state";

	public static final String TRA_EU = "Europe";
	public static final String TRA_ANZAC = "Australia and New Zealand";
	public static final String TRA_WW_INC = "Worldwide (including USA, Canada and the Caribbean)";
	public static final String TRA_WW_EXC = "Worldwide (excluding USA, Canada, and the Caribbean)";
	public static final String TRA_UK = "UK";

	public static final String TRA_DAYS_0_31 = "0-31";
	public static final String TRA_DAYS_31_100 = "31-100";
	public static final String TRA_DAYS_100_365 = "100-365";

	public static final String TRA_AGE_0_17 = "0-17";
	public static final String TRA_AGE_18_65 = "18-65";
	public static final String TRA_AGE_OVER_65 = "65+";


	public static final String PROPERTY_DEST_EU = "factor.dest.eu";
	public static final String PROPERTY_DEST_ANZAC = "factor.dest.anzac";
	public static final String PROPERTY_DEST_WW_INC = "factor.dest.ww.inc";
	public static final String PROPERTY_DEST_WW_EXC = "factor.dest.ww.exc";
	public static final String PROPERTY_DEST_UK = "factor.dest.uk";

	public static final String PROPERTY_DAYS_0_31 = "factor.days.31";
	public static final String PROPERTY_DAYS_32_100 = "factor.days.100";
	public static final String PROPERTY_DAYS_101_365 = "factor.days.365";

	public static final String PROPERTY_NO_TRAVELLERS = "factor.no.travellers";

	public static final String PROPERTY_AGE_0_17 = "factor.age.17";
	public static final String PROPERTY_AGE_18_65 = "factor.age.65";
	public static final String PROPERTY_AGE_65_PLUS = "factor.age.65.plus";

	public static final String TRA_WINTER = "TRA_WINTER";
	public static final String TRA_GOLF = "TRA_GOLF";
	public static final String TRA_BUSINESS = "TRA_BUSINESS";
	public static final String TRA_VALUABLES = "TRA_VALUABLES";
	public static final String TRA_HAZARDOUS = "TRA_HAZARDOUS";
	public static final String TRA_EXCESS = "TRA_EXCESS";
}
