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
package de.hybris.platform.commerceservices.order.dao.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.order.dao.CommerceCartDao;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;


public class DefaultCommerceCartDao extends AbstractItemDao implements CommerceCartDao
{
	protected final static String SELECTCLAUSE = "SELECT {" + CartModel.PK + "} FROM {" + CartModel._TYPECODE + "} ";
	protected final static String ORDERBYCLAUSE = " ORDER BY {" + CartModel.MODIFIEDTIME + "} DESC";

	protected final static String FIND_CART_FOR_CODE_AND_SITE = SELECTCLAUSE + "WHERE {" + CartModel.USER + "} = ?user AND {"
			+ CartModel.CODE + "} = ?code " + ORDERBYCLAUSE;
	protected final static String FIND_CART_FOR_USER_AND_SITE = SELECTCLAUSE + "WHERE {" + CartModel.USER + "} = ?user AND {"
			+ CartModel.SITE + "} = ?site " + ORDERBYCLAUSE;
	protected final static String FIND_CART_FOR_GUID_AND_USER_AND_SITE = SELECTCLAUSE + "WHERE {" + CartModel.GUID + "} = ?guid"
			+ " AND {" + CartModel.USER + "} = ?user AND {" + CartModel.SITE + "} = ?site " + ORDERBYCLAUSE;
	protected final static String FIND_CART_FOR_GUID_AND_SITE = SELECTCLAUSE + "WHERE {" + CartModel.GUID + "} = ?guid AND {"
			+ CartModel.SITE + "} = ?site " + ORDERBYCLAUSE;
	protected final static String FIND_CARTS_FOR_SITE_AND_USER = SELECTCLAUSE + "WHERE {" + CartModel.SITE + "} = ?site AND {"
			+ CartModel.USER + "} = ?user " + ORDERBYCLAUSE;
	protected final static String FIND_CART_FOR_SITE_AND_USER_AND_EXCLUDED_GUID = SELECTCLAUSE + "WHERE {" + CartModel.GUID
			+ "} NOT IN ( ?guid )" + " AND {" + CartModel.USER + "} = ?user AND {" + CartModel.SITE + "} = ?site " + ORDERBYCLAUSE
			+ " LIMIT 1";
	protected final static String FIND_OLD_CARTS_FOR_SITE = SELECTCLAUSE + "WHERE {" + CartModel.MODIFIEDTIME
			+ "} <= ?modifiedBefore AND {" + CartModel.SITE + "} = ?site" + " and {" + CartModel.SAVETIME + "} IS NULL "
			+ ORDERBYCLAUSE;
	protected final static String FIND_OLD_CARTS_FOR_SITE_AND_USER = SELECTCLAUSE + "WHERE {" + CartModel.USER + "} = ?user AND {"
			+ CartModel.MODIFIEDTIME + "} <= ?modifiedBefore AND {" + CartModel.SITE + "} = ?site" + " AND {" + CartModel.SAVETIME
			+ "} IS NULL " + ORDERBYCLAUSE;
	protected final static String FIND_EXPIRED_SAVED_CARTS_FOR_SITE_AND_USER = SELECTCLAUSE + "WHERE {" + CartModel.MODIFIEDTIME
			+ "} <= ?modifiedBefore AND {" + CartModel.SITE + "} = ?site AND {" + CartModel.SAVETIME + "} IS NOT NULL AND {"
			+ CartModel.EXPIRATIONTIME + "} <= ?currentDate " + ORDERBYCLAUSE;

	@Override
	public CartModel getCartForCodeAndUser(final String code, final UserModel user)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", code);
		params.put("user", user);
		final List<CartModel> carts = doSearch(FIND_CART_FOR_CODE_AND_SITE, params, CartModel.class);
		if (carts != null && !carts.isEmpty())
		{
			return carts.get(0);
		}
		return null;
	}

	@Override
	public CartModel getCartForSiteAndUser(final BaseSiteModel site, final UserModel user)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("site", site);
		params.put("user", user);
		final List<CartModel> carts = doSearch(FIND_CART_FOR_USER_AND_SITE, params, CartModel.class);
		if (carts != null && !carts.isEmpty())
		{
			return carts.get(0);
		}
		return null;
	}

	@Override
	public CartModel getCartForGuidAndSiteAndUser(final String guid, final BaseSiteModel site, final UserModel user)
	{
		if (guid != null)
		{
			final Map<String, Object> params = new HashMap<String, Object>();
			params.put("guid", guid);
			params.put("site", site);
			params.put("user", user);
			final List<CartModel> carts = doSearch(FIND_CART_FOR_GUID_AND_USER_AND_SITE, params, CartModel.class);
			if (carts != null && !carts.isEmpty())
			{
				return carts.get(0);
			}
			return null;
		}
		else
		{
			return getCartForSiteAndUser(site, user);
		}
	}

	@Override
	public CartModel getCartForGuidAndSite(final String guid, final BaseSiteModel site)
	{
		final Map<String, Object> params = new HashMap<>();
		params.put("guid", guid);
		params.put("site", site);
		final List<CartModel> carts = doSearch(FIND_CART_FOR_GUID_AND_SITE, params, CartModel.class);
		if (carts != null && !carts.isEmpty())
		{
			return carts.get(0);
		}
		return null;
	}

	@Override
	public List<CartModel> getCartsForSiteAndUser(final BaseSiteModel site, final UserModel user)
	{
		final Map<String, Object> params = new HashMap<>();
		params.put("site", site);
		params.put("user", user);
		return doSearch(FIND_CARTS_FOR_SITE_AND_USER, params, CartModel.class);
	}

	@Override
	public List<CartModel> getCartsForRemovalForSiteAndUser(final Date modifiedBefore, final BaseSiteModel site,
			final UserModel user)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("site", site);
		params.put("modifiedBefore", modifiedBefore);
		if (user == null)
		{
			return doSearch(FIND_OLD_CARTS_FOR_SITE, params, CartModel.class);
		}
		else
		{
			params.put("user", user);
			return doSearch(FIND_OLD_CARTS_FOR_SITE_AND_USER, params, CartModel.class);
		}
	}

	protected <T> List<T> doSearch(final String query, final Map<String, Object> params, final Class<T> resultClass)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		if (params != null)
		{
			fQuery.addQueryParameters(params);
		}

		fQuery.setResultClassList(Collections.singletonList(resultClass));
		final SearchResult<T> searchResult = search(fQuery);
		return searchResult.getResult();
	}
}
