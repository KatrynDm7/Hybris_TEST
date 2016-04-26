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

package de.hybris.datahub.y2ysync.facade;

import com.hybris.datahub.dto.item.ResultData;
import com.hybris.datahub.validation.ValidationException;


public interface CsvImportFacade
{

	public ResultData importCsv(final String itemType, final boolean delete, final String csvInput) throws ValidationException;
}
