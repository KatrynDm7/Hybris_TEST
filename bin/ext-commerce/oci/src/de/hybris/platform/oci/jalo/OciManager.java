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
package de.hybris.platform.oci.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.oci.constants.OciConstants;
import de.hybris.platform.oci.jalo.exception.OciException;
import de.hybris.platform.oci.jalo.interfaces.CatalogLoginPerformer;
import de.hybris.platform.oci.jalo.interfaces.SAPProduct;
import de.hybris.platform.oci.jalo.interfaces.SAPProductList;
import de.hybris.platform.oci.jalo.utils.OutboundSection;
import de.hybris.platform.oci.jalo.utils.Utils;
import de.hybris.platform.util.Base64;
import de.hybris.platform.util.WebSessionFunctions;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;



/**
 * This is the extension manager of the OCI extension.<br>
 * The OCI extension is an interface which provides methods for communicating from a SAP SRM Server to a (hybris)
 * catalog.
 * <p>
 * The SAP Server sends requests and data (such as search strings, ids, ...) per http POST and the oci extension
 * response with URL redirects (for example: product detail page) or http forms with included xml data.<br>
 * <p>
 * The oci extension support the version OCI2.x, OCI3.x, OCI4.x. For a limitation in OCI1.0 (no xml export and max. 25
 * products) this version is not supported.
 * 
 * 
 * 
 */
public class OciManager extends GeneratedOciManager
{
	//Log4J implementation - edit log4j.properties to LOG to your own LOG channel
	static final Logger LOG = Logger.getLogger(OciManager.class.getName());

	//constants for the four special SAP functions (one to four) and the standard case (zero)
	private final static int SHOP_START_PAGE_REDIRECT = 0;
	private final static int PRODUCT_DETAIL_PAGE_REDIRECT = 1;
	private final static int SEARCH_PAGE_REDIRECT = 2;
	private final static int VALIDATE_PRODUCT = 3;
	private final static int BACKGROUND_SEARCH = 4;

	/**
	 * Returns the instance of this manager.
	 * 
	 * @return instance of this manager
	 */
	public static OciManager getInstance()
	{
		final JaloSession jaloSession = JaloSession.getCurrentSession();
		return (OciManager) jaloSession.getExtensionManager().getExtension(OciConstants.EXTENSIONNAME);
	}


	/**
	 * This method returns <code>true</code>.
	 * 
	 * @return <code>true</code>
	 */
	@Override
	public boolean isCreatorDisabled()
	{
		return true;
	}


	// ------------ new public methods --------------------------------------

	// ------------ Shop -> SRM Server --------------------------------------	

	/**
	 * Generates a standard button (with a hidden formular) which submits this formular (here: the included xml data) to
	 * the SRM server. The inserted button has the html code
	 * <code> &lt;input type="submit" value="SAP OCI Buyer" id=submit1 name=submit1&gt; </code> By default the data will
	 * be exported as XML data in the <code>&lt;form&gt;</code>. For export the data as html use
	 * {@link #createOciBuyerButton(SAPProductList, boolean)} with last parameter set to <code>true</code>.
	 * 
	 * @param sapproductlist
	 *           the product list which has to be transfered to the SRM server
	 * @return html code, should be implemented in the cart
	 * @throws OciException
	 *            throwed by problems
	 */
	public static String createOciBuyerButton(final SAPProductList sapproductlist) throws OciException
	{
		return createOciBuyerButton(sapproductlist, null, false);
	}

	/**
	 * Generates a standard button (with a hidden formular) which submits this formular (and the included xml or html
	 * data) to the SRM server. The inserted button has the html code
	 * <code> &lt;input type="submit" value="SAP OCI Buyer" id=submit1 name=submit1&gt; </code>
	 * 
	 * @param sapproductlist
	 *           the product list which has to be transfered to the SRM server
	 * @param useHtml
	 *           use <code>true</code> if the data should be exported as html code in the <code>&lt;form&gt;</code>, use
	 *           <code>false</code> for export the data as XML in the <code>&lt;form&gt;</code>
	 * @return html code, should be implemented in the cart
	 * @throws OciException
	 *            throwed by problems
	 */
	public static String createOciBuyerButton(final SAPProductList sapproductlist, final boolean useHtml) throws OciException
	{
		return createOciBuyerButton(sapproductlist, null, useHtml);
	}

	/**
	 * Same as {@link #createOciBuyerButton(SAPProductList sapproductlist)} but the html code for the button can be
	 * specified here.<br>
	 * For the ociButton you can use a normal String to change only the label of the button or you can define your own
	 * input button in html code (for example you want an image button)<br>
	 * The html code for an input button looks like
	 * <code>&lt;input type="submit" value="button label" id=submit1 name=submit1&gt;</code>
	 * 
	 * @param sapproductlist
	 *           the product list which has to be transfered to the SRM server
	 * @param ociButton
	 *           your own html code for the button
	 * @return a html formular with your own button, should be implemented in the cart
	 * @throws OciException
	 *            throwed by problems
	 */
	public static String createOciBuyerButton(final SAPProductList sapproductlist, final String ociButton) throws OciException
	{
		return createOciBuyerButton(sapproductlist, ociButton, false);
	}

	/**
	 * Same as {@link #createOciBuyerButton(SAPProductList, boolean)} but the html code for the button and the exported
	 * format (XML/HTML) can be specified here.<br>
	 * For the ociButton you can use a normal String to change only the label of the button or you can define your own
	 * input button in html code (for example you want an image button)<br>
	 * The html code for an input button looks like
	 * <code>&lt;input type="submit" value="button label" id=submit1 name=submit1&gt;</code>
	 * 
	 * @param useHtml
	 *           use <code>true</code> if the data should be exported as html code in the <code>&lt;form&gt;</code>, use
	 *           <code>false</code> for export the data as XML in the <code>&lt;form&gt;</code>
	 * @param sapproductlist
	 *           the product list which has to be transfered to the SRM server
	 * @param ociButton
	 *           your own html code for the button
	 * 
	 * @return a html formular with your own button, should be implemented in the cart
	 * @throws OciException
	 *            throwed by problems
	 */
	public static String createOciBuyerButton(final SAPProductList sapproductlist, final String ociButton, final boolean useHtml)
			throws OciException
	{

		if (!isOciSession(JaloSession.getCurrentSession()))
		{
			throw new OciException("The current jaloSession is not an Oci Session", OciException.NO_OCI_SESSION);
		}
		final StringBuilder tempform = new StringBuilder(); //contains the whole <form>...</form> fragment which is send to the SRM Server			
		final OutboundSection outboundSection = (OutboundSection) JaloSession.getCurrentSession().getAttribute(
				OciConstants.OUTBOUND_SECTION_DATA);

		tempform.append("<form action=\"").append(outboundSection.getField(outboundSection.getHookURLFieldName()))
				.append("\" method=\"post\" name=\"OrderForm\" accept-charset=\"UTF-8\"");
		tempform.append(outboundSection.getField(OutboundSection.Fields.TARGET) != null
				&& outboundSection.getField(OutboundSection.Fields.TARGET).length() > 0 ? " target=\""
				+ outboundSection.getField(OutboundSection.Fields.TARGET) + "\"" : "");
		tempform.append(">\n");

		if (!useHtml)
		{
			tempform.append("<input type=\"hidden\" name=\"~xmlDocument\" value=\"");
			tempform.append(
					Base64.encodeBytes(Utils.generateXMLData(sapproductlist, outboundSection).getBytes(), Base64.DONT_BREAK_LINES))
					.append("\">\n");
			tempform.append("<input type=\"hidden\" name=\"~xml_type\" value=\"ESAPO\">");
		}
		else
		{
			tempform.append("<input type=\"hidden\" id=\"").append(OutboundSection.Fields.OK_CODE).append("\" ");
			tempform.append("name=\"").append(OutboundSection.Fields.OK_CODE).append("\" ");
			tempform.append("value=\"").append(outboundSection.getField(OutboundSection.Fields.OK_CODE)).append("\"/>\n");

			tempform.append("<input type=\"hidden\" id=\"").append(OutboundSection.Fields.CALLER).append("\" ");
			tempform.append("name=\"").append(OutboundSection.Fields.CALLER).append("\" ");
			tempform.append("value=\"").append(outboundSection.getField(OutboundSection.Fields.CALLER)).append("\"/>\n");

			tempform.append("<input type=\"hidden\" id=\"").append(OutboundSection.Fields.TARGET).append("\" ");
			tempform.append("name=\"").append(OutboundSection.Fields.TARGET).append("\" ");
			tempform.append("value=\"").append(outboundSection.getField(OutboundSection.Fields.TARGET)).append("\"/>\n");

			tempform.append(Utils.generateHtmlData(sapproductlist, outboundSection));
		}
		if (ociButton == null || ociButton.length() == 0) //default input button
		{
			tempform.append("<input type=\"submit\" value=\"").append(OciConstants.DEFAULT_OCI_BUTTON_VALUE)
					.append("\" id=submit1 name=submit1>").append("\n</form>\n");
		}
		else if (ociButton.indexOf('<') == -1 && ociButton.indexOf('>') == -1) //change only the button label
		{
			tempform.append("<input type=\"submit\" value=\"").append(ociButton).append("\" id=submit1 name=submit1>")
					.append("\n</form>\n");
		}
		else
		//the input button code is given
		{
			tempform.append(ociButton).append("\n</form>\n");
		}


		return tempform.toString();
	}

	// -----------------------------------------------------------------------------------------------	
	/**
	 * This method checks if the current jaloSession is an OciSession.
	 * 
	 * @return true if OciSession, false otherwise
	 */
	public static boolean isOciSession(final JaloSession jaloSession)
	{
		return !(jaloSession.getAttribute(OciConstants.IS_OCI_LOGIN) == null || jaloSession.getAttribute(OciConstants.IS_OCI_LOGIN)
				.equals(Boolean.FALSE));
	}

	// ------------ SRM Server -> Shop  --------------------------------------	

	/**
	 * Same as {@link #ociLogin(HttpServletRequest, HttpServletResponse, CatalogLoginPerformer, boolean, boolean )} but
	 * disableRedirect and useHtml is set to <code>false</code> as default.<br>
	 * <b>This method should be used as main entry point from the SRM server to the external catalog (your shop).</b>
	 * 
	 * @param request
	 *           contains information from the SRM server
	 * @param response
	 *           the oci send the data to this response
	 * @param catalogLoginPerformer
	 *           implemented interface for logging in into the external catalog
	 * @throws Exception
	 *            throws OciExceptions or other exceptions
	 */
	public static void ociLogin(final HttpServletRequest request, final HttpServletResponse response,
			final CatalogLoginPerformer catalogLoginPerformer) throws Exception
	{
		ociLogin(request, response, catalogLoginPerformer, false, false);
	}

	/**
	 * This method calls
	 * {@link #ociLogin(HttpServletRequest, HttpServletResponse, CatalogLoginPerformer, boolean, boolean)} with the last
	 * parameter set to false.
	 * 
	 * @param request
	 *           contains information from the SRM server
	 * @param response
	 *           the oci send the data to this response
	 * @param catalogLoginPerformer
	 *           implemented interface for logging in into the external catalog<br>
	 * @param disableRedirect
	 *           if set to true, all redirects (like CatalogLoginPerformer.getProductSearchURL() or
	 *           CatalogLoginPerformer.getProductDetailURL() ) are disabled. Then you could implement your own logic for
	 *           SRM field FUNCTION
	 * 
	 * @throws Exception
	 *            throws OciExceptions or other exceptions
	 */
	public static void ociLogin(final HttpServletRequest request, final HttpServletResponse response,
			final CatalogLoginPerformer catalogLoginPerformer, final boolean disableRedirect) throws Exception
	{
		ociLogin(request, response, catalogLoginPerformer, disableRedirect, false);
	}

	/**
	 * This is the only method which will be called by the SRM server. It is technically the main method of the oci
	 * extension.
	 * 
	 * @param request
	 *           contains information from the SRM server
	 * @param response
	 *           the oci send the data to this response
	 * @param catalogLoginPerformer
	 *           implemented interface for logging in into the external catalog<br>
	 * @param disableRedirect
	 *           if set to true, all redirects (like CatalogLoginPerformer.getProductSearchURL() or
	 *           CatalogLoginPerformer.getProductDetailURL() ) are disabled. Then you could implement your own logic for
	 *           SRM field FUNCTION
	 * @param useHtml
	 *           use <code>true</code> if the data should be exported as html code in the <code>&lt;form&gt;</code>, use
	 *           <code>false</code> for export the data as XML in the <code>&lt;form&gt;</code>
	 * 
	 * @throws Exception
	 *            throws OciExceptions or other exceptions
	 */
	public static void ociLogin(final HttpServletRequest request, final HttpServletResponse response,
			final CatalogLoginPerformer catalogLoginPerformer, final boolean disableRedirect, final boolean useHtml)
			throws Exception
	{

		//init
		int redirectmerker = SHOP_START_PAGE_REDIRECT; //for standart login or the 4 SAP FUNCTIONs
		final JaloSession jalo = WebSessionFunctions.getSession(request);
		final OutboundSection outboundSection = new OutboundSection(getRequestParameters(request),
				catalogLoginPerformer.getHookURLFieldName());

		//check for nessessary fields
		if (Utils.isEmpty(catalogLoginPerformer.getHookURLFieldName()))
		{
			throw new OciException(
					"Fieldname \""
							+ catalogLoginPerformer.getHookURLFieldName()
							+ "\" (defaultname: HOOK_URL) from SRM Server to shop is null or empty. I need this field for returning my data to SRM Server",
					OciException.NO_HOOK_URL);
		}
		else if (Utils.isEmpty(outboundSection.getField(catalogLoginPerformer.getHookURLFieldName())))
		{
			throw new OciException(
					"Value of Field \""
							+ catalogLoginPerformer.getHookURLFieldName()
							+ "\" (defaultname: HOOK_URL) from SRM Server to shop is null or empty. I need this field for returning my data to SRM Server",
					OciException.NO_HOOK_URL);
		}
		//-----------------------------------------------------------------------------------------------------------------

		catalogLoginPerformer.login(request, response, outboundSection); //Interface call - userlogin into catalog
		jalo.setAttribute(OciConstants.IS_OCI_LOGIN, Boolean.TRUE); //jalo session ist eine oci session
		jalo.setAttribute(OciConstants.OUTBOUND_SECTION_DATA, outboundSection); //haenge outboundsection als object an jalosession

		//-------ab hier entweder nix machen oder eine der 4 special srm server funktionalitaeten ausfuehrten ---------------------
		if (!disableRedirect)
		{
			final String functiontemp = outboundSection.getField(OciConstants.FUNCTION);

			if (functiontemp != null)
			{
				if (functiontemp.equals(OciConstants.DETAIL))
				{
					if (Utils.isEmpty(outboundSection.getField(OciConstants.PRODUCTID)))
					{
						throw new OciException("Field " + OciConstants.PRODUCTID + " is missing", OciException.SRM_FIELD_MISSING);
					}
					else
					{
						redirectmerker = PRODUCT_DETAIL_PAGE_REDIRECT;
					}
				}
				else if (functiontemp.equals(OciConstants.SOURCING))
				{
					if (Utils.isEmpty(outboundSection.getField(OciConstants.SEARCHSTRING)))
					{
						throw new OciException("Field " + OciConstants.SEARCHSTRING + " is missing", OciException.SRM_FIELD_MISSING);
					}
					else if (Utils.isEmpty(outboundSection.getField(OciConstants.VENDOR)))
					{
						throw new OciException("Field " + OciConstants.VENDOR + " is missing", OciException.SRM_FIELD_MISSING);
					}
					else
					{
						redirectmerker = SEARCH_PAGE_REDIRECT;
					}
				}
				else if (functiontemp.equals(OciConstants.VALIDATE))
				{
					if (!Utils.isEmpty(outboundSection.getField(OciConstants.PRODUCTID)))
					{
						if (Utils.isEmpty(outboundSection.getField(OciConstants.QUANTITY)))
						{
							throw new OciException("Field " + OciConstants.QUANTITY + " is missing", OciException.SRM_FIELD_MISSING);
						}
						else
						{
							redirectmerker = VALIDATE_PRODUCT;
						}
					}
					//wegen oci3.0 downward compatibility 
					else if (!Utils.isEmpty(outboundSection.getField(OciConstants.SEARCHSTRING)))
					{
						if (Utils.isEmpty(outboundSection.getField(OciConstants.VENDOR)))
						{
							throw new OciException("Field " + OciConstants.VENDOR + " is missing", OciException.SRM_FIELD_MISSING);
						}
						else
						{
							redirectmerker = SEARCH_PAGE_REDIRECT;
						}
					}
					else
					{
						throw new OciException("Field " + OciConstants.PRODUCTID + " or " + OciConstants.SEARCHSTRING + " for "
								+ OciConstants.FUNCTION + "=" + OciConstants.VALIDATE + " is missing", OciException.SRM_FIELD_MISSING);
					}
				}
				else if (functiontemp.equals(OciConstants.BACKGROUND_SEARCH))
				{
					if (Utils.isEmpty(outboundSection.getField(OciConstants.SEARCHSTRING)))
					{
						throw new OciException("Field " + OciConstants.SEARCHSTRING + " is missing", OciException.SRM_FIELD_MISSING);
					}
					else
					{
						redirectmerker = BACKGROUND_SEARCH;
					}
				}
			}


			final StringBuilder data = new StringBuilder("");
			//OutputStream htmlout;
			PrintWriter htmlout;
			String flushString; // for flusching the Bytes to the respond
			switch (redirectmerker)
			{
				case PRODUCT_DETAIL_PAGE_REDIRECT:
					response.sendRedirect(response.encodeRedirectURL(catalogLoginPerformer.getProductDetailURL(outboundSection
							.getField(OciConstants.PRODUCTID))));
					break;
				//					---------------------------------------------------------------------------------------------------------------------------------------
				case SEARCH_PAGE_REDIRECT:
					response.sendRedirect(response.encodeRedirectURL(catalogLoginPerformer.getProductSearchURL(
							outboundSection.getField(OciConstants.SEARCHSTRING), outboundSection.getField(OciConstants.VENDOR))));
					break;
				//					---------------------------------------------------------------------------------------------------------------------------------------
				case VALIDATE_PRODUCT:
					//htmlout = response.getOutputStream();
					htmlout = response.getWriter();
					response.setContentType("text/html; charset=UTF-8");
					final SAPProduct sapproduct = catalogLoginPerformer.getProductInfoForValidation(
							outboundSection.getField(OciConstants.PRODUCTID),
							Double.parseDouble(outboundSection.getField(OciConstants.QUANTITY)));

					if (!useHtml)
					{
						data.append(sapproduct != null ? Utils.generateXMLData(sapproduct, outboundSection) : "");
					}
					else
					{
						data.append(sapproduct != null ? Utils.generateHtmlData(sapproduct, outboundSection) : "");
					}

					flushString = Utils.generateHTML(data.toString(), outboundSection, true, useHtml);
					//htmlout.write(x.getBytes());
					htmlout.write(flushString);
					htmlout.flush();
					break;
				//					---------------------------------------------------------------------------------------------------------------------------------------
				case BACKGROUND_SEARCH:
					//htmlout = response.getOutputStream();
					htmlout = response.getWriter();
					response.setContentType("text/html; charset=UTF-8");
					final SAPProductList sapproductlist = catalogLoginPerformer.backgroundSearch(outboundSection
							.getField(OciConstants.SEARCHSTRING));

					if (!useHtml)
					{
						data.append(sapproductlist != null && sapproductlist.size() > 0 ? Utils.generateXMLData(sapproductlist,
								outboundSection) : "");
					}
					else
					{
						data.append(sapproductlist != null && sapproductlist.size() > 0 ? Utils.generateHtmlData(sapproductlist,
								outboundSection) : "");
					}
					flushString = Utils.generateHTML(data.toString(), outboundSection, false, useHtml);
					//htmlout.write(x.getBytes());
					htmlout.write(flushString);
					htmlout.flush();
					break;
				//					-----------------------------------------------------------------------------------------------------------------------------------------
				case SHOP_START_PAGE_REDIRECT:
				default:
					response.sendRedirect(response.encodeRedirectURL(catalogLoginPerformer.getShopURL()));
					//response.sendRedirect(catalogLoginPerformer.getShopURL());
					break;
			}
		}
	}


	// ------------ private methods --------------------------------------	

	/**
	 * Returns from the HttpServletRequest the ParameterMap as Map.
	 */
	private static Map getRequestParameters(final HttpServletRequest request)
	{
		final Map result = new HashMap();
		final Map paramMap = request.getParameterMap();
		for (final Iterator it = paramMap.entrySet().iterator(); it.hasNext();)
		{
			final Map.Entry entry = (Map.Entry) it.next();
			final String[] values = (String[]) entry.getValue();
			if (values != null && values.length > 0)
			{
				result.put(entry.getKey(), values[0]);
			}
		}

		return result;
	}
}
