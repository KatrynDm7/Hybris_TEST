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
package de.hybris.platform.oci.jalo.interfaces;

import de.hybris.platform.oci.jalo.exception.OciException;
import de.hybris.platform.oci.jalo.utils.OutboundSection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Interface for communication between SRM Server and the catalog.<br>
 * The implementation of this interface contains the logic for login into the catalog and the logic for the SRM special
 * functions.
 * 
 * 
 * 
 */
public interface CatalogLoginPerformer
{

	/**
	 * This method logs in into the shop from the SRM Server.<br>
	 * This is catalog specific and has to be implemented in this method.
	 * 
	 * @param request
	 *           contains the needed parameter from SRM Server
	 * @param response
	 *           catalog data will be sent there
	 * @param outboundsection
	 *           implementation of the Interface OutboundSection
	 * @throws OciException
	 *            throws an OciException if an OCI-related error occurs
	 */
	public void login(HttpServletRequest request, HttpServletResponse response, OutboundSection outboundsection)
			throws OciException;

	/**
	 * This method searches the catalog using the given <code>searchstring</code> and returns the search result as
	 * <code>SAPProductList</code>.<br>
	 * A HTML document which contains the results in OCI Format (a html form with inserted xml data) will be sent to the
	 * SRM server back.<br>
	 * It is the implementation of SRM Server request: FUNCTION=BACKGROUND_SEARCH - the results will be showed in the SRM
	 * Server.
	 * 
	 * @param searchstring
	 *           the search String
	 * @return the implemented Interface of SAPProductList which contains the search result
	 */
	public SAPProductList backgroundSearch(final String searchstring);

	/**
	 * For the given searchstring and vendor, this method returns the catalog specific URL from the catalog search
	 * function. <br>
	 * This is the implementation of SRM Server request: FUNCTION=SOURCING - the results will be shown in the catalog
	 * specific result page.
	 * 
	 * @param searchstring
	 *           the search string
	 * @param vendor
	 *           vendor
	 * @return an URL to the catalog search page
	 */
	public String getProductSearchURL(final String searchstring, final String vendor);

	/**
	 * This method validates the given product and quantity in the SRM Server. As response, the method returns the
	 * product which is identified by the ID String. The data of the product will be sent back to the SRM Server. <br>
	 * This is the implementation of SRM Server request: FUNCTION=VALIDATE - the results will be showed in the SRM
	 * Server.
	 * 
	 * @param productID
	 *           the (unique) ProductID of the product in the shop
	 * @param quantity
	 *           quantity of the product
	 */
	public SAPProduct getProductInfoForValidation(final String productID, final double quantity);

	/**
	 * This method returns the entry URL (as String) of the shop (default start/home page of the shop).
	 * 
	 * @return link (as String) to the start page of the shop
	 */
	public String getShopURL();

	/**
	 * This method returns an URL of the shop where the product with the given unique <code>productID</code> is shown.
	 * For example, this could be the shop specific page for product details. This is the implementation of SRM Server
	 * request: FUNCTION=DETAIL.<br>
	 * 
	 * @param productID
	 *           this is the productID from the shop
	 * @return link (as String) to the specific product detail page
	 */
	public String getProductDetailURL(String productID);

	/**
	 * In SAP SRM Server the fieldname for the <code>HOOK_URL</code> can vary and must be specified for the oci
	 * extension. For default this method should return (default implementation in SRM Server): <code>"HOOK_URL"</code>.
	 * 
	 * @return the field name where the <code>HOOK_URL</code> is stored
	 */
	public String getHookURLFieldName();
}
