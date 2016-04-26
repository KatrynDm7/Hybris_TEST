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
package de.hybris.platform.b2bacceleratorfacades.customer.impl;

import de.hybris.platform.b2bacceleratorfacades.customer.exception.InvalidPasswordException;
import de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade;
import de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * B2B implementation for CustomerFacade.
 */
public class DefaultB2BCustomerFacade extends DefaultCustomerFacade
{
	private String passwordPattern;

	@Override
	public void updatePassword(final String token, final String newPassword) throws TokenInvalidatedException,
			InvalidPasswordException
	{
		validatePassword(newPassword);
		super.updatePassword(token, newPassword);
	}

	@Override
	public void changePassword(final String oldPassword, final String newPassword) throws PasswordMismatchException,
			InvalidPasswordException
	{
		validatePassword(newPassword);
		super.changePassword(oldPassword, newPassword);
	}

	public boolean validatePassword(final String password)
	{
		boolean isValid = false;
		if (StringUtils.isNotBlank(passwordPattern))
		{
			isValid = password.matches(passwordPattern);
		}
		if (!isValid)
		{
			throw new InvalidPasswordException("Password does not match pattern.");
		}
		return isValid;
	}

	public String getPasswordPattern()
	{
		return passwordPattern;
	}

	@Required
	public void setPasswordPattern(final String passwordPattern)
	{
		this.passwordPattern = passwordPattern;
	}

}
