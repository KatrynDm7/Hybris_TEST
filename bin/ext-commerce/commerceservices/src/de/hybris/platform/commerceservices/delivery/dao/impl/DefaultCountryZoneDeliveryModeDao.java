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
package de.hybris.platform.commerceservices.delivery.dao.impl;

import de.hybris.platform.commerceservices.delivery.dao.CountryZoneDeliveryModeDao;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Default implementation of {@link de.hybris.platform.commerceservices.delivery.dao.CountryZoneDeliveryModeDao}.
 */
public class DefaultCountryZoneDeliveryModeDao extends AbstractItemDao implements CountryZoneDeliveryModeDao
{
	private static final String ZONE_COUNTRY_RELATION = "ZoneCountryRelation";
	private static final String STORE_TO_DELIVERY_MODE_RELATION = "BaseStore2DeliveryModeRel";

	@Override
	@Deprecated
	public Collection<DeliveryModeModel> findDeliveryModesByCountryAndCurrency(final CountryModel deliveryCountry,
			final CurrencyModel currency, final Boolean net)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("deliveryCountry", deliveryCountry);
		params.put("currency", currency);
		params.put("net", net);
		params.put("active", Boolean.TRUE);

		return doSearch(getQuery(), params, DeliveryModeModel.class);
	}

	@Override
	public Collection<DeliveryModeModel> findDeliveryModes(final AbstractOrderModel abstractOrder)
	{
		final StringBuilder query = new StringBuilder("SELECT DISTINCT {zdm:").append(ItemModel.PK).append("}");
		query.append(" FROM { ").append(ZoneDeliveryModeValueModel._TYPECODE).append(" AS val");
		query.append(" JOIN ").append(ZoneDeliveryModeModel._TYPECODE).append(" AS zdm");
		query.append(" ON {val:").append(ZoneDeliveryModeValueModel.DELIVERYMODE).append("}={zdm:").append(ItemModel.PK)
				.append('}');
		query.append(" JOIN ").append(ZONE_COUNTRY_RELATION).append(" AS z2c");
		query.append(" ON {val:").append(ZoneDeliveryModeValueModel.ZONE).append("}={z2c:").append(Link.SOURCE).append('}');
		query.append(" JOIN ").append(STORE_TO_DELIVERY_MODE_RELATION).append(" AS s2d");
		query.append(" ON {val:").append(ZoneDeliveryModeValueModel.DELIVERYMODE).append("}={s2d:").append(Link.TARGET).append('}');
		query.append(" } WHERE {val:").append(ZoneDeliveryModeValueModel.CURRENCY).append("}=?currency");
		query.append(" AND {z2c:").append(Link.TARGET).append("}=?deliveryCountry");
		query.append(" AND {s2d:").append(Link.SOURCE).append("}=?store");
		query.append(" AND {zdm:").append(ZoneDeliveryModeModel.NET).append("}=?net");
		query.append(" AND {zdm:").append(ZoneDeliveryModeModel.ACTIVE).append("}=?active");

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("deliveryCountry", abstractOrder.getDeliveryAddress().getCountry());
		params.put("currency", abstractOrder.getCurrency());
		params.put("net", abstractOrder.getNet());
		params.put("active", Boolean.TRUE);
		params.put("store", abstractOrder.getStore());

		return doSearch(query.toString(), params, DeliveryModeModel.class);
	}

	@Deprecated
	protected String getQuery()
	{
		final StringBuilder query = new StringBuilder("SELECT DISTINCT {zdm:").append(ItemModel.PK).append("}");
		query.append(" FROM { ").append(ZoneDeliveryModeValueModel._TYPECODE).append(" AS val");
		query.append(" JOIN ").append(ZoneDeliveryModeModel._TYPECODE).append(" AS zdm");
		query.append(" ON {val:").append(ZoneDeliveryModeValueModel.DELIVERYMODE).append("}={zdm:").append(ItemModel.PK)
				.append('}');
		query.append(" JOIN ").append(ZONE_COUNTRY_RELATION).append(" AS z2c");
		query.append(" ON {val:").append(ZoneDeliveryModeValueModel.ZONE).append("}={z2c:").append(Link.SOURCE).append('}');
		query.append(" } WHERE {val:").append(ZoneDeliveryModeValueModel.CURRENCY).append("}=?currency");
		query.append(" AND {z2c:").append(Link.TARGET).append("}=?deliveryCountry");
		query.append(" AND {zdm:").append(ZoneDeliveryModeModel.NET).append("}=?net");
		query.append(" AND {zdm:").append(ZoneDeliveryModeModel.ACTIVE).append("}=?active");
		return query.toString();
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
