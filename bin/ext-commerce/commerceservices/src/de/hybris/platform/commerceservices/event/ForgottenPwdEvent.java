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
package de.hybris.platform.commerceservices.event;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;


/**
 * Forgotten password event, implementation of {@link AbstractCommerceUserEvent}
 */
public class ForgottenPwdEvent extends AbstractCommerceUserEvent<BaseSiteModel>
{
	private String token;

	/**
	 * Default constructor
	 */
	public ForgottenPwdEvent()
	{
		super();
	}

	/**
	 * Parameterized Constructor
	 * 
	 * @param token
	 */
	public ForgottenPwdEvent(final String token)
	{
		super();
		this.token = token;
	}

	/**
	 * @return the token
	 */
	public String getToken()
	{
		return token;
	}

	/**
	 * @param token
	 *           the token to set
	 */
	public void setToken(final String token)
	{
		this.token = token;
	}

}
