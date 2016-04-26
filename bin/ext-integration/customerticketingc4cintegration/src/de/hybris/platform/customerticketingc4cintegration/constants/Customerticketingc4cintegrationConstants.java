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
package de.hybris.platform.customerticketingc4cintegration.constants;

import de.hybris.platform.util.Config;


/**
 * Global class for all Customerticketingc4cintegration constants. You can add global constants for your extension into
 * this class.
 */
public final class Customerticketingc4cintegrationConstants extends GeneratedCustomerticketingc4cintegrationConstants
{
	public static final String EXTENSIONNAME = "customerticketingc4cintegration";
	public static final String RESPONSE_COOKIE_NAME = "set-cookie";
	public static final String DEFAULT_AGENT_NAME = "Customer Service";

	public static final String ACCEPT = Config.getParameter("customerticketingc4cintegration.c4c-accept");
	public static final String URL = Config.getParameter("customerticketingc4cintegration.c4c-url");

	public static final String TICKETING_SUFFIX = Config.getParameter("customerticketingc4cintegration.c4c-ticket-suffix");
	public static final String BATCH_SUFFIX = Config.getParameter("customerticketingc4cintegration.c4c-batch-suffix");

	public static final String EXPAND_SUFFIX = Config.getParameter("customerticketingc4cintegration.c4c-expand-suffix");
	public static final String USERNAME = Config.getParameter("customerticketingc4cintegration.c4c-username");
	public static final String PASSWORD = Config.getParameter("customerticketingc4cintegration.c4c-password");
	public static final String SITE_HEADER = Config.getParameter("customerticketingc4cintegration.c4c-site-header");
	public static final String UPDATE_MESSAGE_SUFFIX = Config
			.getParameter("customerticketingc4cintegration.c4c-update-message-suffix");

	// only to get x-token
	public static final String TOKEN_URL_SUFFIX = Config.getParameter("customerticketingc4cintegration.c4c-token-url-suffix"); // it could be a param
	public static final String TOKEN_NAMING = Config.getParameter("customerticketingc4cintegration.c4c-token-naming");
	public static final String TOKEN_EMPTY = Config.getParameter("customerticketingc4cintegration.c4c-token-empty");

	// for populating
	public static final String TYPECODE_10004 = "10004";
	public static final String TYPECODE_10007 = "10007";
	public static final String LANGUAGE = "EN";
	public static final String B2B_EXTERNAL_CUSTOMER_ID = "";
	public static final String DATA_ORIGIN_TYPECODE = "4";
	public static final String RELATED_TRANSACTION_TYPECODE = "2085";
	public static final String ROLE_CODE = "1";
	public static final String FILETR_SUFFIX = "$filter";
	public static final String ORDER_BY_SUFFIX = "$orderby=LastChangeDateTime desc";

	// for multipart
	public static final String MULTIPART = "multipart";
	public static final String MIXED = "mixed";
	public static final String MULTIPART_MIXED_MODE = "multipart/mixed";
	public static final String MULTIPART_HAS_ERROR = "has_error";
	public static final String MULTIPART_ERROR_MESSAGE = "error_message";
	public static final String MULTIPART_ERROR_CODE = "error_code";
	public static final String MULTIPART_BODY = "body";
	public static final String CONTENT_ID = "Content-ID";
	public static final String CONTENT_ID_VALUE_PREFIX = "SRQ_TXT_";

	private Customerticketingc4cintegrationConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension
}
