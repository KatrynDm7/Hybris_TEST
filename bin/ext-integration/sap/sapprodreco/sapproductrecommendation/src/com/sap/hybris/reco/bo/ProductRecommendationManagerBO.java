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

import java.util.List;

import com.sap.hybris.reco.dao.InteractionContext;
import com.sap.hybris.reco.dao.ProductRecommendation;
import com.sap.hybris.reco.dao.RecommendationContext;
import com.sap.hybris.reco.dao.ProductRecommendationData;

import de.hybris.platform.sap.core.bol.businessobject.BusinessObject;


/**
 * 
 */
public interface ProductRecommendationManagerBO extends BusinessObject
{
	/**
	 * @param context
	 * @return list of recommended products
	 */
	public List<ProductRecommendationData> getProductRecommendation(RecommendationContext context);
	/**
	 * @param context 
	 */
	public void postInteraction(InteractionContext context);
	
	void prefetchRecommendations(RecommendationContext context);
}
