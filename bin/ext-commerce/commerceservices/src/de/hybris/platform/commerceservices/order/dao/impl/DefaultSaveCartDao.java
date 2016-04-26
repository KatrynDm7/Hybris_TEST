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
import de.hybris.platform.commerceservices.order.dao.SaveCartDao;
import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default dao implementation for handling the saved cart feature
 */
public class DefaultSaveCartDao extends DefaultCommerceCartDao implements SaveCartDao
{
	protected final static String FIND_SAVED_CARTS_FOR_USER_AND_SITE = SELECTCLAUSE + "WHERE {" + CartModel.USER
			+ "} = ?user AND {" + CartModel.SITE + "} = ?site AND {" + CartModel.SAVETIME + "} IS NOT NULL " + ORDERBYCLAUSE;

	protected final static String FIND_EXPIRED_SAVED_CARTS_FOR_SITE = SELECTCLAUSE + "WHERE {" + CartModel.SITE
			+ "} = ?site AND {" + CartModel.SAVETIME + "} IS NOT NULL AND {" + CartModel.EXPIRATIONTIME + "} <= ?currentDate "
			+ ORDERBYCLAUSE;

	protected final static String FIND_SAVED_CARTS_FOR_SITE_AND_USER_WITH_STATUS = "SELECT {" + CartModel.PK + "} FROM {"
			+ CartModel._TYPECODE + "}, {" + OrderStatus._TYPECODE + "} " + "WHERE {" + CartModel._TYPECODE + "." + CartModel.STATUS
			+ "} = {" + OrderStatus._TYPECODE + ".pk} AND {" + CartModel.USER + "} = ?user AND {" + CartModel.SITE
			+ "} = ?site AND {" + CartModel.SAVETIME + "} IS NOT NULL AND {OrderStatus.CODE} in (?orderStatus) " + ORDERBYCLAUSE;

	private PagedFlexibleSearchService pagedFlexibleSearchService;

	@Override
	public List<CartModel> getSavedCartsForRemovalForSite(final BaseSiteModel site)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("site", site);
		params.put("currentDate", new Date());


		return doSearch(FIND_EXPIRED_SAVED_CARTS_FOR_SITE, params, CartModel.class);
	}

	@Override
	public SearchPageData<CartModel> getSavedCartsForSiteAndUser(final PageableData pageableData, final BaseSiteModel baseSite,
			final UserModel user, final List<OrderStatus> orderStatus)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("site", baseSite);
		params.put("user", user);

		final String orderStatusList = formatOrderStatusList(orderStatus);
		final String query;
		if (StringUtils.isNotBlank(orderStatusList))
		{
			params.put("orderStatus", orderStatusList);
			query = FIND_SAVED_CARTS_FOR_SITE_AND_USER_WITH_STATUS;
		}
		else
		{
			query = FIND_SAVED_CARTS_FOR_USER_AND_SITE;
		}

		return getPagedFlexibleSearchService().search(query, params, pageableData);
	}

	/**
	 * Format given list of OrderStatus to comma-separated string of order status ids in brackets
	 * 
	 */
	private String formatOrderStatusList(final List<OrderStatus> orderStatus)
	{
		if (CollectionUtils.isNotEmpty(orderStatus))
		{
			final StringBuilder orderStatusIdList = new StringBuilder();
			for (final OrderStatus status : orderStatus)
			{
				if (status != null && StringUtils.isNotBlank(status.getCode()))
				{
					orderStatusIdList.append(status.getCode() + ",");
				}
			}
			if (orderStatusIdList.length() > 0)
			{
				orderStatusIdList.deleteCharAt(orderStatusIdList.length() - 1);
			}
			return orderStatusIdList.toString();
		}
		return null;
	}

	protected PagedFlexibleSearchService getPagedFlexibleSearchService()
	{
		return pagedFlexibleSearchService;
	}

	@Required
	public void setPagedFlexibleSearchService(final PagedFlexibleSearchService pagedFlexibleSearchService)
	{
		this.pagedFlexibleSearchService = pagedFlexibleSearchService;
	}
}
