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
package de.hybris.platform.assistedservicefacades.exception;

import de.hybris.platform.assistedservicefacades.AssistedServiceFacade;


/**
 * Exception for the {@link AssistedServiceFacade} which is used when AS agent isn't logged in yet.
 */
public class AssistedServiceAgentNotLoggedInException extends AssistedServiceFacadeException
{

	public AssistedServiceAgentNotLoggedInException(final String message)
	{
		super(message);
	}

	@Override
	public String getMessageCode()
	{
		return "asm.emulate.error.agent_missed";
	}
}