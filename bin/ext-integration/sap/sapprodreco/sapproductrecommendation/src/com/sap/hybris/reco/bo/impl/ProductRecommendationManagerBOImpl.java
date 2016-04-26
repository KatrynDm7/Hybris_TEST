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
package com.sap.hybris.reco.bo.impl;

import java.util.List;

import com.sap.hybris.reco.dao.ProductRecommendationData;
import org.apache.log4j.Logger;

import de.hybris.platform.sap.core.bol.businessobject.BackendInterface;
import de.hybris.platform.sap.core.bol.businessobject.BusinessObjectBase;
import de.hybris.platform.sap.core.jco.exceptions.BackendException;

import com.sap.hybris.reco.be.ProductRecommendationManagerBackend;
import com.sap.hybris.reco.bo.ProductRecommendationManagerBO;
import com.sap.hybris.reco.dao.InteractionContext;
import com.sap.hybris.reco.dao.ProductRecommendation;
import com.sap.hybris.reco.dao.RecommendationContext;

/**
 * 
 */
@BackendInterface(ProductRecommendationManagerBackend.class)
public class ProductRecommendationManagerBOImpl extends BusinessObjectBase implements ProductRecommendationManagerBO
{
	private static final Logger LOG = Logger.getLogger(ProductRecommendationManagerBOImpl.class);

	public List<ProductRecommendationData> getProductRecommendation(final RecommendationContext context)
	{
		try
		{
			return ((ProductRecommendationManagerBackend) getBackendBusinessObject()).getProductRecommendation(context);
		}
		catch (final BackendException e)
		{
			LOG.error("", e);
		}
		return null;
	}

	/**
	 * @param context
	 */
	public void postInteraction(InteractionContext context)
	{
		try
		{
			((ProductRecommendationManagerBackend) getBackendBusinessObject()).postInteraction(context);
		}
		catch (final BackendException e)
		{
			LOG.error("", e);
		}		
	}
	
	@Override
	public void prefetchRecommendations(RecommendationContext context) {
		try
		{
			((ProductRecommendationManagerBackend) getBackendBusinessObject()).prefetchRecommendation(context);
		}
		catch (final BackendException e)
		{
			LOG.error("", e);
		}
	}
	
}
