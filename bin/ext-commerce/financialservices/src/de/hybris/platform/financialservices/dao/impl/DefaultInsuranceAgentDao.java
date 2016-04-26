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
 */
package de.hybris.platform.financialservices.dao.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.financialservices.dao.InsuranceAgentDao;
import de.hybris.platform.financialservices.model.AgentModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * Default Insurance Agent Dao.
 */
public class DefaultInsuranceAgentDao extends AbstractItemDao implements InsuranceAgentDao
{
	private static final String CATEGORY_QUERY_PARAMETER = "categoryCode";

	private static final String FIND_ALL = "SELECT {" + AgentModel.PK + "} " + "FROM {" + AgentModel._TYPECODE + "} ";

	private static final String FIND_BY_CATEGORY = "SELECT DISTINCT {rel.source} " + "FROM {"
			+ CategoryModel._AGENTCATEGORYRELATION + " AS rel JOIN " + CategoryModel._TYPECODE + " AS app "
			+ "ON {rel.target}={app.PK}} " + "WHERE {app.code}=?categoryCode";

	/**
	 * Finds an insurance Agent by their internal id
	 *
	 * @return AgentModel the insurance agent
	 */
	public AgentModel findAgentByUid(final String uid)
	{
		validateParameterNotNull(uid, "Agent uid must not be null");

		final AgentModel example = new AgentModel();
		example.setUid(uid);
		return getFlexibleSearchService().getModelByExample(example);
	}

	/**
	 * Finds multiple insurance agents by their common category
	 *
	 * @return List<AgentModel> the list of insurance agents found
	 */
	@Override
	public List<AgentModel> findAgentsByCategory(final String categoryCode)
	{
		if (StringUtils.isBlank(categoryCode))
		{
			throw new IllegalArgumentException("Category code must not be null or empty");
		}

		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_BY_CATEGORY);
		query.addQueryParameter(CATEGORY_QUERY_PARAMETER, categoryCode);

		final SearchResult<AgentModel> result = getFlexibleSearchService().search(query);

		return result.getResult();
	}

	/**
	 * Finds all currently registered Agents
	 *
	 * @return List<AgentModel> the list of all possible insurance agents
	 */
	@Override
	public List<AgentModel> findAllAgents()
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_ALL);
		final SearchResult<AgentModel> result = getFlexibleSearchService().search(query);
		return result.getResult();
	}

}
