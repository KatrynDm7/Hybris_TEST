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
package de.hybris.platform.samlsinglesignon;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.samlsinglesignon.constants.SamlsinglesignonConstants;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.service.AbstractService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default SSO service for getting/creating user
 */
public class DefaultSSOService extends AbstractService implements SSOUserService
{
	private ModelService modelService;

	private UserService userService;

	@Override
	public UserModel getOrCreateSSOUser(final String id, final String name, final Collection<String> roles)
	{
		final SSOUserMapping userMapping = findMapping(roles);

		if (StringUtils.isEmpty(id) || StringUtils.isEmpty(name))
		{
			throw new IllegalArgumentException("User info must not be empty");
		}

		if (userMapping != null)
		{

			UserModel user = lookupExisting(id, userMapping);
			if (user == null)
			{
				user = createNewUser(id, name, userMapping);
			}
			adjustUserAttributes(user, userMapping);

			modelService.save(user);

			return user;
		}
		else
		{
			throw new IllegalArgumentException("No SSO user mapping available for roles " + roles + " - cannot accept user " + id);
		}
	}

	/**
	 * create a new user
	 *
	 * @param id
	 *           to be used as the user Id
	 * @param name
	 *           name of the user
	 * @param userMapping
	 *           user mappings (groups and user type)
	 * @return a new user model
	 */
	protected UserModel createNewUser(final String id, final String name, final SSOUserMapping userMapping)
	{
		UserModel user;
		user = modelService.create(userMapping.type);
		user.setUid(id);
		user.setName(name);

		final String defaultPasswordEncoder = StringUtils
				.defaultIfEmpty(Config.getParameter(SamlsinglesignonConstants.SSO_PASSWORD_ENCODING),
						SamlsinglesignonConstants.MD5_PASSWORD_ENCODING);

		userService.setPassword(user, UUID.randomUUID().toString(), defaultPasswordEncoder);//should be default password but the token is encoded with md5
		return user;
	}

	/**
	 * Check if a user exists or not
	 *
	 * @param id
	 *           the user id to search for
	 * @param mapping
	 *           groups/user type
	 * @return return user model in case the user is found or null if not found
	 */
	protected UserModel lookupExisting(final String id, final SSOUserMapping mapping)
	{
		try
		{
			return userService.getUserForUID(id);
		}
		catch (final UnknownIdentifierException e)
		{
			return null;
		}
	}

	/**
	 * Adjusting user groups
	 *
	 * @param user
	 *           the user to adjust the groups for
	 * @param mapping
	 *           the mapping which holds the groups
	 */
	protected void adjustUserAttributes(final UserModel user, final SSOUserMapping mapping)
	{
		user.setGroups(mapping.groups.stream().map(it -> userService.getUserGroupForUID(it)).collect(Collectors.toSet()));
	}

	protected SSOUserMapping findMapping(final Collection<String> roles)
	{
		SSOUserMapping mergedMapping = null;
		for (final String role : roles)
		{
			final SSOUserMapping mapping = getMappingForRole(role);
			if (mapping != null)
			{
				if (mergedMapping == null)
				{
					mergedMapping = new SSOUserMapping();
					mergedMapping.type = mapping.type;
				}
				if (mapping.type.equals(mergedMapping.type))
				{
					mergedMapping.groups.addAll(mapping.groups);
				}
				else
				{
					throw new IllegalArgumentException("SSO user cannot be configured due to ambigous type mappings (roles: " + roles
							+ ")");
				}
			}
		}
		return mergedMapping;
	}

	/**
	 * getting the mapping for roles
	 *
	 * @param role
	 *           the role to get the mapping for
	 * @return SSO user mapping object which has the user type and the groups
	 */
	protected SSOUserMapping getMappingForRole(final String role)
	{
		final Map<String, String> params = Registry.getCurrentTenantNoFallback().getConfig()
				.getParametersMatching("sso\\.mapping\\." + role + "\\.(.*)", true);
		if (MapUtils.isNotEmpty(params))
		{
			final SSOUserMapping mapping = new SSOUserMapping();
			mapping.type = params.get("usertype");
			mapping.groups = new LinkedHashSet<>(Arrays.asList(params.get("groups").split(",; ")));

			return mapping;
		}
		return null;
	}

	protected class SSOUserMapping
	{
		String type;
		Collection<String> groups = new LinkedHashSet<String>();
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}
}