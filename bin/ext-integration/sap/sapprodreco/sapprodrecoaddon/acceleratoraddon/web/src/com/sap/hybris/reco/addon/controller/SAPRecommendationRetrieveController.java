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
package com.sap.hybris.reco.addon.controller;

import com.sap.hybris.reco.model.CMSSAPRecommendationComponentModel;

import de.hybris.platform.acceleratorcms.component.slot.CMSPageSlotComponentService;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sap.hybris.reco.addon.constants.SapprodrecoaddonConstants;
import com.sap.hybris.reco.addon.facade.ProductRecommendationManagerFacade;
import com.sap.hybris.reco.common.util.CookieHelper;
import com.sap.hybris.reco.common.util.HMCConfigurationReader;
import com.sap.hybris.reco.constants.SapproductrecommendationConstants;
import com.sap.hybris.reco.dao.InteractionContext;
import com.sap.hybris.reco.dao.RecommendationContext;

/**
 * Controller for RecommendationList view.
 */
@Controller("SAPRecommendationRetrieveController")
public class SAPRecommendationRetrieveController
{
	private final static Logger LOG = Logger.getLogger( SAPRecommendationRetrieveController.class.getName() );

	@Resource(name = "sapProductRecommendationManagerFacade")
	private ProductRecommendationManagerFacade productRecommendationManagerFacade;
	
	@Resource(name = "hmcConfigurationReader")
	private HMCConfigurationReader configuration;
	
	@Resource(name= "cmsPageSlotComponentService")
	private	CMSPageSlotComponentService cmsPageSlotComponentService;
	
	@Autowired
	private HttpServletRequest request;
	
	/**
	 * @param id 
	 * @param productCode
	 * @param model
	 * @return viewName
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/action/recommendations/")
	public String retrieveRecommentdations(@RequestParam("id") final String id, @RequestParam("productCode") final String productCode,
										   @RequestParam("componentId") final String componentId, final Model model)
			throws UnsupportedEncodingException
	{
		final String viewName = "addon:/sapprodrecoaddon/cms/recommendationlist";
		AbstractCMSComponentModel component = cmsPageSlotComponentService.getComponentForId(componentId);

		if(!(component instanceof CMSSAPRecommendationComponentModel))
		{
			LOG.error("Cannot find component with id " + componentId);
			return viewName;
		}

		CMSSAPRecommendationComponentModel cmssapRecommendationComponentModel = (CMSSAPRecommendationComponentModel) component;
		String recotype = cmssapRecommendationComponentModel.getRecotype();

		if (StringUtils.isEmpty(recotype))
		{
			LOG.error("Recommendation Model has to be specified.");
			return viewName;
		}

		final RecommendationContext context = productRecommendationManagerFacade.createRecommendationContextProvider();
		productRecommendationManagerFacade.populateContext(request, context, (CMSSAPRecommendationComponentModel) component, productCode);

		model.addAttribute("title", cmssapRecommendationComponentModel.getTitle());
		model.addAttribute("recoId", id);
		model.addAttribute("recoType", recotype);
		model.addAttribute("itemType", context.getItemDataSourceType());
		model.addAttribute("productReferences", productRecommendationManagerFacade.getProductRecommendation(context));
			
		return viewName;
	}

	/**
	 * @param id
	 * @param itemType 
	 * @param scenarioId 
	 * @param prodURL 
	 * @param prodImageURL 
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/action/interaction/", method = RequestMethod.POST)
	@ResponseBody
	public void registerClickthrough(@RequestParam("id") final String id, @RequestParam("itemType") final String itemType, @RequestParam("scenarioId") final String scenarioId,
			@RequestParam("prodURL") final String prodURL, @RequestParam("prodImageURL") final String prodImageURL) 
					throws UnsupportedEncodingException
	{
		if (configuration.getUsage().equals(SapproductrecommendationConstants.SCENARIO))
		{
			InteractionContext context = new InteractionContext();
			context.setProductId(id);
			context.setProductType(itemType);
			context.setScenarioId(scenarioId);
			if (productRecommendationManagerFacade.getSessionUserId().equals("anonymous"))
			{
				context.setUserId(CookieHelper.getPiwikID(request));
				context.setUserType(productRecommendationManagerFacade.getAnonOriginOfContactId());
			}
			else
			{
				context.setUserId(productRecommendationManagerFacade.getSessionUserId());
				context.setUserType(configuration.getUserType());
			}
			
			context.setSourceObjectId(request.getSession().getId());
			context.setTimestamp(getInteractionTimeStamp());
			context.setProductNavURL(prodURL);
			context.setProductImageURL(prodImageURL);
			productRecommendationManagerFacade.postInteraction(context);
		}
	}
	
	/**
	 * Get the interaction Time stamp in the following format
	 * YYYYMMDDhhmmss.mmmuuun
	 * 20150515161616.0000000
	 */
	private String getInteractionTimeStamp(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
		String time = sdf.format(cal.getTime());
		return time + "0000";
	}
	
}

