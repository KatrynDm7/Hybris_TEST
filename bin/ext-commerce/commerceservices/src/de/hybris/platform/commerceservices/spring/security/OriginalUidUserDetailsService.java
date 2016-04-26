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
package de.hybris.platform.commerceservices.spring.security;

import de.hybris.platform.spring.security.CoreUserDetails;
import de.hybris.platform.spring.security.CoreUserDetailsService;


/**
 * accelerator specific implementation for providing user data access
 */
public class OriginalUidUserDetailsService extends CoreUserDetailsService
{
	@Override
	public CoreUserDetails loadUserByUsername(final String username)
	{
		return super.loadUserByUsername(username.toLowerCase());
	}
}
