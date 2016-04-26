/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.eventtracking.services.constants;

/**
 * @author stevo.slavic
 *
 */
public enum TrackingEventJsonFields
{
	COMMON_EVENT_TYPE("eventtype"), COMMON_TIMESTAMP("timestamp"), COMMON_CVAR_PAGE("cvar"), COMMON_URL("url"), COMMON_SESSION_ID(
			"session_id"), COMMON_USER_ID("user_id"), COMMON_USER_EMAIL("user_email"),
			COMMON_PIWIK_ID("_id"),REF_URL("urlref"),

	COMMERCE_PRODUCT_SKU("_pks"), COMMERCE_PRODUCT_NAME("_pkn"), COMMERCE_PRODUCT_CATEGORY("_pkc"), COMMERCE_PRODUCT_PRICE("_pkp"), COMMERCE_ORDER_ID(
			"ec_id"), COMMERCE_CART_ABANDONMENT_REASON("cart_abandonment_reason"), COMMERCE_CART_ITEMS("ec_items"),

	SEARCH_TERMS("search"), SEARCH_CATEGORY("search_cat"), SEARCH_COUNT("search_count"), SEARCH_FACETS("search_facets"), SEARCH_RESULTS_PAGE(
			"search_results_page"),

	EVENT_CATEGORY("e_c"), EVENT_ACTION("e_a"), EVENT_NAME("e_n"), EVENT_VALUE("e_v"),

	BANNER("banner"), BANNER_ID("bannerid");

	private String key;

	private TrackingEventJsonFields(final String key)
	{
		this.key = key;
	}

	public String getKey()
	{
		return key;
	}
}
