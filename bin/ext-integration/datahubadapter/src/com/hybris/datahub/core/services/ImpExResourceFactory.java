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

package com.hybris.datahub.core.services;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.impex.ImpExResource;

import com.hybris.datahub.core.dto.ItemImportTaskData;


/**
 * A service for creation of ImpEx resources
 */
public interface ImpExResourceFactory
{
	/**
	 * Creates an import resource for the specified import context.
	 *
	 * @param ctx context of the import task, which contains content to import and any information relevant to the import
	 * process.
	 * @return a resource to load into the system.
	 * @throws ImpExException if the context does not contain a valid ImpEx script.
	 */
	ImpExResource createResource(ItemImportTaskData ctx) throws ImpExException;
}
