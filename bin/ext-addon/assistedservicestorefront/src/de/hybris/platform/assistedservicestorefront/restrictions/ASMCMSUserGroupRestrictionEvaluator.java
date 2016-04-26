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
package de.hybris.platform.assistedservicestorefront.restrictions;

import de.hybris.platform.assistedservicefacades.constants.AssistedservicefacadesConstants;
import de.hybris.platform.assistedservicefacades.util.AssistedServiceSession;
import de.hybris.platform.cms2.model.restrictions.ASMCMSUserGroupRestrictionModel;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.evaluator.CMSRestrictionEvaluator;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Evaluates a user group restriction accordingly to context information.
 * <p>
 * <b>Evaluates to true in the following cases, <br/>
 * <br/>
 * 1) If ASM agent is in Session, then both ASM Agent and current user should be part of Restricted group. <br/>
 * 2) If ASM agent is not in Session, then current user should be part of Restricted group. </b>
 * </p>
 */
public class ASMCMSUserGroupRestrictionEvaluator implements CMSRestrictionEvaluator<ASMCMSUserGroupRestrictionModel>
{
	private final static Logger LOG = Logger.getLogger(ASMCMSUserGroupRestrictionEvaluator.class);

	private UserService userService;
	private SessionService sessionService;

	@Override
	public boolean evaluate(final ASMCMSUserGroupRestrictionModel asmCMSUserGroupRestrictionModel, final RestrictionData context)
	{
		final boolean isCurrentUserPartOfRestrictedGroup = checkWhetherUserIsPartOfRestrictedGroup(getUserService()
				.getCurrentUser(), asmCMSUserGroupRestrictionModel);
		final AssistedServiceSession asmSession = getSessionService().getAttribute(
				AssistedservicefacadesConstants.ASM_SESSION_PARAMETER);

		// If ASM Agent is in session, make sure ASM Agent is also part of the Restricted Group as well as the Current Customer
		if (asmSession != null)
		{
			final UserModel asmAgent = asmSession.getAgent();
			if (asmAgent != null)
			{
				final boolean isAsmAgentPartOfRestrictedGroup = checkWhetherUserIsPartOfRestrictedGroup(asmAgent,
						asmCMSUserGroupRestrictionModel);
				return isAsmAgentPartOfRestrictedGroup && isCurrentUserPartOfRestrictedGroup;
			}
		}
		return isCurrentUserPartOfRestrictedGroup;
	}

	/**
	 * Checks Whether the supplied User is part of the Restricted Groups
	 *
	 * @param userModel
	 * @param asmCMSUserGroupRestrictionModel
	 * @return true or false
	 */
	protected boolean checkWhetherUserIsPartOfRestrictedGroup(final UserModel userModel,
			final ASMCMSUserGroupRestrictionModel asmCMSUserGroupRestrictionModel)
	{
		final Collection<UserGroupModel> restrictionGroups = asmCMSUserGroupRestrictionModel.getUserGroups();
		final Set<PrincipalGroupModel> userGroups = new HashSet<PrincipalGroupModel>(userModel.getGroups());

		if (asmCMSUserGroupRestrictionModel.isIncludeSubgroups())
		{
			userGroups.addAll(getSubgroups(userGroups));
		}

		if (LOG.isDebugEnabled())
		{
			final List<String> restrictionGroupNames = new ArrayList<String>();
			for (final UserGroupModel group : restrictionGroups)
			{
				restrictionGroupNames.add(group.getUid());
			}

			final List<String> currentGroupNames = new ArrayList<String>();
			for (final PrincipalGroupModel group : userGroups)
			{
				currentGroupNames.add(group.getUid());
			}

			LOG.debug(String.format("Current UserGroups: %s", StringUtils.join(currentGroupNames, "; ")));
			LOG.debug(String.format("Restricted UserGroups: %s", StringUtils.join(restrictionGroupNames, "; ")));
		}

		for (final UserGroupModel restrictionGroupModel : restrictionGroups)
		{
			if (userGroups.contains(restrictionGroupModel))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Fetches all Subgroups of the supplied groups.
	 *
	 * @param groups
	 * @return the groups
	 */
	protected List<PrincipalGroupModel> getSubgroups(final Collection<PrincipalGroupModel> groups)
	{
		final List<PrincipalGroupModel> ret = new ArrayList<PrincipalGroupModel>(groups);

		for (final PrincipalGroupModel principalGroup : groups)
		{
			ret.addAll(getSubgroups(principalGroup.getGroups()));
		}
		return ret;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	/**
	 * @return the userService
	 */
	protected UserService getUserService()
	{
		return userService;
	}

	/**
	 * @return the sessionService
	 */
	protected SessionService getSessionService()
	{
		return sessionService;
	}
}