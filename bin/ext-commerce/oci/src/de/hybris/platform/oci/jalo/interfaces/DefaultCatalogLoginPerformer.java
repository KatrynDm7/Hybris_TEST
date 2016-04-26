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

import de.hybris.platform.jalo.JaloConnectException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.oci.jalo.exception.OciException;
import de.hybris.platform.oci.jalo.utils.OutboundSection;
import de.hybris.platform.util.WebSessionFunctions;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * This is a default implementation of the interface <code>CatalogLoginPerformer</code>. It contains as an example how
 * an implementation of the interface for a given shop could be.<br>
 * 
 * <br/>
 * <br/>
 * <b>This implementation is intended for demonstration purposes only. If it does not contains functionality you need,
 * you will have to implement that functionality by yourself. We do not recommend using this implementation for
 * productive systems.</b>
 */
public class DefaultCatalogLoginPerformer implements CatalogLoginPerformer
{
	/**
	 * Log into the shop with a given <code>USERNAME</code> and <code>PASSWORD</code> from the SRM server.
	 */
	public void login(final HttpServletRequest request, final HttpServletResponse response, final OutboundSection outboundsection)
			throws OciException
	{
		final HashMap loginprops = new HashMap();

		loginprops.put(JaloSession.LoginProperties.LOGIN, outboundsection.getField("USERNAME"));
		loginprops.put(JaloSession.LoginProperties.PASSWORD, outboundsection.getField("PASSWORD"));
		loginprops.put(JaloSession.LoginProperties.USER_PK, null);

		try
		{
			WebSessionFunctions.getSession(request).transfer(loginprops);
		}
		catch (final JaloSecurityException e)
		{
			throw new OciException("Login failed.", OciException.LOGIN_FAILED);
		}
		catch (final JaloInvalidParameterException e)
		{
			throw new OciException("Wrong parameter in Map", OciException.OUTBOUND_FIELDS_MISSING);
		}
		catch (final JaloConnectException e)
		{
			throw new OciException("Unable to get jalo session!", OciException.UNSPECIFIED_ERROR);
		}
	}

	/**
	 * Returns the field name where the return URL to the SRM server is stored. In this case it is <code>HOOK_URL</code>.
	 */
	public String getHookURLFieldName()
	{
		return "HOOK_URL";
	}

	/**
	 * Contains a flexible search and returns the results as <code>DefaultSAPProductList</code> which is the example
	 * implementation of the interface <code>SAPProductList</code>.
	 */
	public SAPProductList backgroundSearch(final String searchstring)
	{
		JaloSession.getCurrentSession().getTypeManager().getComposedType(Product.class);
		Map values = Collections.EMPTY_MAP;

		String query = "SELECT {p:PK} FROM {product AS p}";
		if (searchstring != null)
		{
			query += " WHERE {p:code} LIKE ?searchstring OR {p:name:o} LIKE '%" + searchstring + "%' OR {p:description:o} LIKE '%"
					+ searchstring + "%'";
			values = new HashMap();
			values.put("searchstring", "%" + searchstring + "%");
		}

		final List result = JaloSession.getCurrentSession().getFlexibleSearch().search(query, // the query text
				values, // no values needed here
				Collections.singletonList(Product.class), // the result signature list
				true, // fail on unknown (untyped) fields
				true, // dont need total
				0, -1 // result range
				).getResult();

		return result.isEmpty() ? null : new DefaultSAPProductList(result);
	}

	/**
	 * Returns the catalog specific search page (here: <i>'productsearch.jsp?searchterm=' + searchstring</i>) for the
	 * used catalog with the searchsting as URL parameter. In this example the vendor string is ignored.
	 */
	public String getProductSearchURL(final String searchstring, final String vendor)
	{
		return "productsearch.jsp?searchterm=" + searchstring;
	}


	/**
	 * Returns the start page (here: <i>shop-start.jsp</i>) of the example catalog.
	 */
	public String getShopURL()
	{
		return "shop-start.jsp";
	}

	/**
	 * returns the catalog specific URL for product details (here: <i>'productdetails.jsp?productid=' + productID</i>)
	 */
	public String getProductDetailURL(final String productID)
	{
		return "productdetails.jsp?productid=" + productID;
	}

	/**
	 * In this example the product validation is like the background search, but the return is only a single product of
	 * the catalog and not a list. <br>
	 * This concrete implementation need as parameter a product code. Other implementations are possible, for example you
	 * can use in your own implementation the product key
	 */
	public SAPProduct getProductInfoForValidation(final String productID, final double quantity)
	{
		final String query = "SELECT {p:PK} FROM {product AS p} WHERE {p:code} = '" + productID + "'";

		final List result = JaloSession.getCurrentSession().getFlexibleSearch().search(query, // the query text
				Collections.EMPTY_MAP, Collections.singletonList(Product.class), // the result signature list
				true, // fail on unknown (untyped) fields
				true, // dont need total
				0, 1 // result range
				).getResult();

		return result.isEmpty() ? null : new DefaultSAPProduct((Product) result.get(0))
						{
			@Override
			public double getItemQuantity()
			{
				return quantity;
			}
		};
	}
}
