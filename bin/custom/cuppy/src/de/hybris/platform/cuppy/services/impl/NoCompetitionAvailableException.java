/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2011 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.cuppy.services.impl;

import de.hybris.platform.servicelayer.exceptions.SystemException;


/**
 * Indicates that in current session setup no competition is available.
 */
public class NoCompetitionAvailableException extends SystemException
{
	public NoCompetitionAvailableException()
	{
		super("No competition available");
	}
}
