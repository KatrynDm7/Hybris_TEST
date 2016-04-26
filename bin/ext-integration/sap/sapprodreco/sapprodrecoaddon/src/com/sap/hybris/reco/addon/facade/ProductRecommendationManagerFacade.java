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
package com.sap.hybris.reco.addon.facade;

import java.util.List;

import com.sap.hybris.reco.model.CMSSAPRecommendationComponentModel;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;

import com.sap.hybris.reco.dao.InteractionContext;
import com.sap.hybris.reco.dao.RecommendationContext;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 */
public interface ProductRecommendationManagerFacade
{
	/**
	 * @param context
	 * @return list of recommended products
	 */
	public List<ProductReferenceData> getProductRecommendation(RecommendationContext context);
	
	/**
	 * @param context
	 */
	public void postInteraction(InteractionContext context);

	/**
	 * @return RecommendationContextProvider
	 */
	public RecommendationContext createRecommendationContextProvider();
	
	/**
	 * Get the logged in User ID
	 * @return String
	 */
	public String getSessionUserId();

	/**
	 * @param request
	 * @param context
	 * @param component
	 * @param productCode
	 */
	public void populateContext(HttpServletRequest request, RecommendationContext context, CMSSAPRecommendationComponentModel component, String productCode);

	/**
	 * @param request
	 * @param component
	 * @param productCode
	 */
	public void prefetchRecommendations(HttpServletRequest request, CMSSAPRecommendationComponentModel component, String productCode);
	
	public String getAnonOriginOfContactId();
	
	public void setAnonOriginOfContactId(String anonOriginOfContactId);
}
