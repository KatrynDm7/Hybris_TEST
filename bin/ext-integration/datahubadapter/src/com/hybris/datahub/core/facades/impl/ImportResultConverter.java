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

package com.hybris.datahub.core.facades.impl;

import de.hybris.platform.servicelayer.impex.ImportResult;

import com.hybris.datahub.core.facades.ItemImportResult;


/**
 * Converts <code>ImportResult</code> from the <code>ImportService</code> into an <code>ItemImportResult</code> and
 * guarantees consistency of the item import result regardless of the import result version and implementation.
 */
public interface ImportResultConverter
{
	/**
	 * Converts service import result to item import result
	 *
	 * @param importRes result received from the import service.
	 * @return import result data corresponding to the service import result.
	 */
	ItemImportResult convert(ImportResult importRes);
}
