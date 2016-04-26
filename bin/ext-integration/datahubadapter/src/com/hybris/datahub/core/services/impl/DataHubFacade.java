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
 */

package com.hybris.datahub.core.services.impl;

import com.hybris.datahub.core.facades.ItemImportResult;

import java.io.InputStream;
import java.util.Map;


/**
 * A facade for communicating with the Data Hub.
 */
public interface DataHubFacade
{
	/**
	 * Requests data from the Data Hub.
	 *
	 * @param resource identifies what data should be retrieved.
	 * @param parameters specifies parameters for the resource data retrieval.
	 * @return an input stream to read the resource data.
	 */
	InputStream readData(String resource, Map<String, ?> parameters);

	/**
	 * Returns the <code>ItemImportResult</code> to the Data Hub
	 *
	 * @param resultCallbackUrl the callback URL to use when returning the <code>ItemImportResult</code>
	 * @param itemImportResult the resuts of the import
	 */
	void returnImportResult(final String resultCallbackUrl, final ItemImportResult itemImportResult);
}
