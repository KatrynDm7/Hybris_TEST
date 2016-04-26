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
package de.hybris.platform.oci.constants;

/**
 * Global class for all oci related constants. <br />
 * This includes typecodes for new items, managers and JNDI names. <br />
 * 
 * 
 */
public final class OciConstants extends GeneratedOciConstants
{
	/**
	 * field name, bound to jaloSession
	 */
	public final static String IS_OCI_LOGIN = "IS_OCI_LOGIN";

	public static final String LICENCE_OCI = "extension.oci";

	/**
	 * field name, bound to jalosession
	 */
	public final static String OUTBOUND_SECTION_DATA = "OUTBOUND_SECTION_DATA";

	/**
	 * field name, is sent by the SRM server
	 */
	public final static String OCI_VERSION = "OCI_VERSION";
	//	-----------------------------------------------------------------------

	/**
	 * field name from ParameterMap, value contains function name from SAP SRM server
	 */
	public final static String FUNCTION = "FUNCTION";

	/**
	 * field name, possible value of field FUNCTION
	 */
	public final static String DETAIL = "DETAIL";

	/**
	 * field name, possible value of field FUNCTION
	 */
	public final static String SOURCING = "SOURCING";

	/**
	 * field name, possible value of field FUNCTION
	 */
	public final static String VALIDATE = "VALIDATE";

	/**
	 * field name, possible value of field FUNCTION
	 */
	public final static String BACKGROUND_SEARCH = "BACKGROUND_SEARCH";
	//	-----------------------------------------------------------------------

	/**
	 * field name, needed for function DETAIL and VALIDATE
	 */
	public final static String PRODUCTID = "PRODUCTID";

	/**
	 * field name, needed for function SOURCING and BACKGROUND_SEARCH
	 */
	public final static String SEARCHSTRING = "SEARCHSTRING";

	/**
	 * field name, needed for function VALIDATE
	 */
	public final static String QUANTITY = "QUANTITY";

	/**
	 * field name, needed for function SOURCING
	 */
	public final static String VENDOR = "VENDOR";

	/**
	 * the default name for the oci button
	 */
	public final static String DEFAULT_OCI_BUTTON_VALUE = "SAP OCI Buyer";


	// -----------------------------------------------------------------------

	private OciConstants()
	{
		super();
	}
}
