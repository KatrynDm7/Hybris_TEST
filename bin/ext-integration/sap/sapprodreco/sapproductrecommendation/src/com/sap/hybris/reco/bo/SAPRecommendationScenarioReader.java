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
package com.sap.hybris.reco.bo;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.apache.olingo.odata2.api.exception.ODataException;

import com.sap.hybris.reco.be.RecommendationEntityManager;
import com.sap.hybris.reco.dao.SAPRecommendationType;


/**
 * To fetch a list of model types from PRI using OData service
 */
public class SAPRecommendationScenarioReader
{
	private static final String ENTITY_NAME = "Scenarios";
	private static final String SCENARIO_ID = "ScenarioId";
	private static final String SCENARIO_DESCRIPTION = "ScenarioDescription";

	private static final String SELECT_FIELDS = SCENARIO_ID + "," + SCENARIO_DESCRIPTION;
	private static final String ORDER_BY = "ScenarioId";
	protected RecommendationEntityManager accessBE;



	/**
	 * @return a list of SAPRecommendationScenario
	 * @throws ODataException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public List<SAPRecommendationType> getAllRecommendationScenarios() throws ODataException, URISyntaxException,
			IOException
	{
		final ODataFeed feed = accessBE.getTypes(ENTITY_NAME, null, SELECT_FIELDS, null, ORDER_BY);
		final List<ODataEntry> foundEntries = feed.getEntries();
		return extractRecommendationScenarios(foundEntries);
	}

	/**
	 * 
	 */
	private List<SAPRecommendationType> extractRecommendationScenarios(final List<ODataEntry> foundEnties)
	{
		final List<SAPRecommendationType> recommendationScenarios = new ArrayList<SAPRecommendationType>();
		if (foundEnties != null)
		{
			final Iterator<ODataEntry> iter = foundEnties.iterator();

			while (iter.hasNext())
			{
				final ODataEntry entry = iter.next();
				final SAPRecommendationType recommendationScenario = extractRecommendationScenario(entry);
				recommendationScenarios.add(recommendationScenario);
			}
		}
		return recommendationScenarios;
	}

	/**
	 * 
	 */
	private SAPRecommendationType extractRecommendationScenario(final ODataEntry entry)
	{
		final SAPRecommendationType recommendationScenario= new SAPRecommendationType();
		final Map<String, Object> props = entry.getProperties();
		if (props != null && props.size()>0)
		{
			recommendationScenario.setId(props.get(SCENARIO_ID).toString());
			recommendationScenario.setDescription(props.get(SCENARIO_DESCRIPTION).toString());
		}
		return recommendationScenario;
	}

	/**
	 * @return accessBE
	 */
	public RecommendationEntityManager getAccessBE()
	{
		return accessBE;
	}

	/**
	 * @param accessBE
	 */
	public void setAccessBE(RecommendationEntityManager accessBE)
	{
		this.accessBE = accessBE;
	}

}