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

public interface ImpexHeaderValidator
{
	void validateImpexHeader(String header, String macros) throws ImpExException;
}
