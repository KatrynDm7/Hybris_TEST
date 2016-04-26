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

package com.hybris.datahub.core.facades;

import com.hybris.datahub.core.dto.ItemImportTaskData;

import java.io.IOException;


/**
 * A facade for uploading items data into the system.
 */
public interface ItemImportFacade
{
	/**
	 * Imports data into the system.
	 *
	 * @param ctx import task context data, which describes what items should be imported.
	 * @return result of the items import.
	 * @throws IOException when an input/output problem occurs during the items import.
	 */
	ItemImportResult importItems(ItemImportTaskData ctx) throws IOException;
}
