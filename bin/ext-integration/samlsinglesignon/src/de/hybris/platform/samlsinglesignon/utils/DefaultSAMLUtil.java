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

import de.hybris.platform.samlsinglesignon.constants.SamlsinglesignonConstants;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.saml.SAMLCredential;


/**
 *
 */
public class DefaultSAMLUtil implements SAMLUtil
{

	@Override
	public String getUserId(final SAMLCredential credential)
	{
		String userId = "";

		final String userIdAttributeKey = StringUtils.defaultIfEmpty(Config.getParameter(SamlsinglesignonConstants.SSO_USERID_KEY),
				null);

		if (null != userIdAttributeKey)
		{
			userId = null != credential.getAttributeAsString(userIdAttributeKey) ? credential
					.getAttributeAsString(userIdAttributeKey) : "";
		}

		return userId;
	}

	@Override
	public String getUserName(final SAMLCredential credential)
	{
		String firstName = "";
		String lastName = "";
		String userName = "";

		final String firstNameAttributeKey = StringUtils.defaultIfEmpty(
				Config.getParameter(SamlsinglesignonConstants.SSO_FIRSTNAME_KEY), null);

		final String lastNameAttributeKey = StringUtils.defaultIfEmpty(
				Config.getParameter(SamlsinglesignonConstants.SSO_LASTNAME_KEY), null);

		if (null != firstNameAttributeKey)
		{

			firstName = null != credential.getAttributeAsString(firstNameAttributeKey) ? credential
					.getAttributeAsString(firstNameAttributeKey) : "";
		}

		if (null != lastNameAttributeKey)
		{
			lastName = null != credential.getAttributeAsString(lastNameAttributeKey) ? credential
					.getAttributeAsString(lastNameAttributeKey) : "";
		}

		userName = firstName + " " + lastName;

		return userName;
	}

	@Override
	public String getCustomAttribute(final SAMLCredential credential, final String attributeName)
	{
		String customerAttrValue = "";

		if (null != attributeName)
		{
			customerAttrValue = null != credential.getAttributeAsString(attributeName) ? credential
					.getAttributeAsString(attributeName) : "";
		}

		return customerAttrValue;
	}

	@Override
	public List<String> getCustomAttributes(final SAMLCredential credential, final String attributeName)
	{
		final List<String> groups = new ArrayList<String>();

		if (null != attributeName && null != credential.getAttributeAsString(attributeName))
		{
			final String separators = credential.getAttributeAsString(attributeName);

			groups.addAll(Arrays.asList(StringUtils.split(separators, ",")));
		}
		return groups;
	}
}
