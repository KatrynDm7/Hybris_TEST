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
package de.hybris.platform.b2bacceleratorservices.dao.impl;

import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.b2bacceleratorservices.dao.PagedB2BUserGroupDao;
import de.hybris.platform.commerceservices.search.dao.impl.DefaultPagedGenericDao;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class DefaultPagedB2BUserGroupDao extends DefaultPagedGenericDao<B2BUserGroupModel> implements
		PagedB2BUserGroupDao<B2BUserGroupModel>
{
	private static final String FIND_USERGROUP_BY_PARENT_UNIT = "SELECT {ug:pk}     "
			+ " FROM { B2BUserGroup 	as ug "
			+ " JOIN   B2BUnit 			as u ON  {ug:unit} = {u:pk} }";

	public DefaultPagedB2BUserGroupDao(final String typeCode)
	{
		super(typeCode);
	}

	@Override
	public SearchPageData<B2BUserGroupModel> findPagedB2BUserGroup(final String sortCode, final PageableData pageableData)
	{
		final List<SortQueryData> sortQueries = Arrays.asList(
				createSortQueryData("byUnitName", FIND_USERGROUP_BY_PARENT_UNIT + " ORDER BY {u:name} "),
                createSortQueryData("byGroupID", FIND_USERGROUP_BY_PARENT_UNIT + " ORDER BY {ug:uid} "),
                createSortQueryData("byName", FIND_USERGROUP_BY_PARENT_UNIT + " ORDER BY {ug:name} "));



		return getPagedFlexibleSearchService().search(sortQueries, sortCode, new HashMap<String, Object>(), pageableData);
	}
}
