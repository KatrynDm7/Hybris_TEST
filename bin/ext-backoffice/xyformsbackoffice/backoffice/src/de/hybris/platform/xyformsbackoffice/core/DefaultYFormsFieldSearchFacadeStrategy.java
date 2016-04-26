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
 */
package de.hybris.platform.xyformsbackoffice.core;

import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.GenericSearchOrderBy;
import de.hybris.platform.core.Operator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.genericsearch.GenericSearchQuery;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collection;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.StringUtils;

import com.hybris.backoffice.cockpitng.dataaccess.facades.search.DefaultPlatformFieldSearchFacadeStrategy;
import com.hybris.cockpitng.search.data.SearchQueryData;


/**
 * FieldSearchFacadeStrategy that filters out system records for yForms.
 */
public class DefaultYFormsFieldSearchFacadeStrategy<T extends ItemModel> extends DefaultPlatformFieldSearchFacadeStrategy<T>
{
	private final static Logger LOG = Logger.getLogger(DefaultYFormsFieldSearchFacadeStrategy.class);

	private Set<String> types;
	private Set<String> rolesNotAllowed;

	private UserService userService;

	@Override
	public boolean canHandle(final String typeCode)
	{
		return types.contains(typeCode);
	}

	@Override
	protected GenericSearchQuery buildQuery(final SearchQueryData searchQueryData)
	{
		final GenericSearchQuery searchQuery = super.buildQuery(searchQueryData);

		if (!shouldFilterOutSystemRecords())
		{
			return searchQuery;
		}

		final GenericQuery originalQuery = searchQuery.getQuery();
		final String typeCode = searchQueryData.getSearchType();

		LOG.debug("Filtering out system records for [" + typeCode + "]");

		final GenericQuery newQuery = new GenericQuery(typeCode);

		final GenericCondition systemCondition = GenericCondition.createConditionForValueComparison(
				new GenericSearchField("system"), Operator.EQUAL, new Integer(0));

		GenericCondition allConditions = originalQuery.getCondition();
		allConditions = allConditions != null ? GenericConditionList.and(systemCondition, allConditions) : systemCondition;

		newQuery.addCondition(allConditions);

		final Collection<GenericSearchOrderBy> orderByList = originalQuery.getOrderByList();
		for (final GenericSearchOrderBy orderBy : orderByList)
		{
			newQuery.addOrderBy(orderBy);
		}

		newQuery.setTypeExclusive(originalQuery.isTypeExclusive());

		return new GenericSearchQuery(newQuery);
	}

	/**
	 * Decides if the user has the rights to see system records.
	 */
	private boolean shouldFilterOutSystemRecords()
	{
		final UserModel userModel = userService.getCurrentUser();
		if (userService.isAdmin(userModel))
		{
			LOG.debug("User has admin rights, system records will be shown");
			return false;
		}

		final Set<PrincipalGroupModel> groups = userModel.getGroups();

		for (final PrincipalGroupModel g : groups)
		{
			final String name = g.getUid();
			if (this.rolesNotAllowed.contains(name))
			{
				LOG.debug(name + " is not allowed to see system records");
				return true;
			}
		}

		return false;
	}


	@Required
	public void setTypes(final String types)
	{
		this.types = StringUtils.commaDelimitedListToSet(types);
	}

	@Required
	public void setRolesNotAllowed(final String rolesNotAllowed)
	{
		this.rolesNotAllowed = StringUtils.commaDelimitedListToSet(rolesNotAllowed);
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}
}