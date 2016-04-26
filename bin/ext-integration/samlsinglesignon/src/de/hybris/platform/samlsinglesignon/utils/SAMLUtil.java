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
package de.hybris.platform.samlsinglesignon.utils;

import java.util.List;

import org.springframework.security.saml.SAMLCredential;


/**
 * Utlity class for SAML
 */
public interface SAMLUtil
{
	/**
	 * retrieve the userId from SAML assertion
	 *
	 * @param credential
	 *           the credential that has the user Id data
	 * @return userId
	 */
	String getUserId(final SAMLCredential credential);

	/**
	 * retrieve user name
	 *
	 * @param credential
	 *           the credential that has the user name data
	 * @return user name
	 */
	String getUserName(final SAMLCredential credential);

	/**
	 * return a custom attribute list value
	 *
	 * @param credential
	 *           the credential that has the list data
	 * @param attributeName
	 *           attribute to retrieve its value
	 * @return attribute's list value
	 */
	<T> List<T> getCustomAttributes(final SAMLCredential credential, final String attributeName);

	/**
	 * return a custom attribute value
	 *
	 * @param credential
	 *           the credential that has the data
	 * @param attributeName
	 *           attribute to retrieve its value
	 * @return the attribute value
	 */
	<T> T getCustomAttribute(final SAMLCredential credential, final String attributeName);
}
