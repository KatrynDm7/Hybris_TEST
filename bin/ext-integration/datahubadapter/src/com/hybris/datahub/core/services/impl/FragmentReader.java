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

import de.hybris.platform.impex.jalo.ImpExException;

import com.hybris.datahub.core.dto.ItemImportTaskData;

import java.util.List;


/**
 * A class that reads ImpEx script received from the Integration Layer and splits it into logical fragments based on the
 * script content.
 */
public interface FragmentReader
{
	/**
	 * Reads an ImpEx script and splits it into fragments.
	 *
	 * @param ctx .
	 * @return a list of fragments identified in the script.
	 * @throws ImpExException if the script is invalid or this reader failed to read from the input stream.
	 */
	public List<ImpExFragment> readScriptFragments(ItemImportTaskData ctx) throws ImpExException;
}
