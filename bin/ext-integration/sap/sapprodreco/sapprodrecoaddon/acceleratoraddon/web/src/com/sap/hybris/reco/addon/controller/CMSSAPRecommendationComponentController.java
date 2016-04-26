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

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sap.hybris.reco.addon.facade.ProductRecommendationManagerFacade;
import com.sap.hybris.reco.model.CMSSAPRecommendationComponentModel;
import com.sap.hybris.reco.addon.constants.SapprodrecoaddonConstants;
import com.sap.hybris.reco.constants.SapproductrecommendationConstants;


/**
 * Controller for CMS CMSSAPRecommendationComponentController.
 */
@Controller("CMSSAPRecommendationComponentController")
@RequestMapping(value = "/view/CMSSAPRecommendationComponentController")
public class CMSSAPRecommendationComponentController extends AbstractCMSAddOnComponentController<CMSSAPRecommendationComponentModel>
{
	@Resource(name = "sapProductRecommendationManagerFacade")
	private ProductRecommendationManagerFacade productRecommendationManagerFacade;
	
	@Override
	protected String getAddonUiExtensionName(final CMSSAPRecommendationComponentModel component)
	{
		return SapprodrecoaddonConstants.EXTENSIONNAME;
	}

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final CMSSAPRecommendationComponentModel component)
	{
		String productCode = "null";
		final ProductModel currentProduct = getRequestContextData(request).getProduct();
		final CategoryModel currentCategory = getRequestContextData(request).getCategory();
		final String leadingItemType = component.getLeadingitemtype();
		if(leadingItemType != null){
   		if (currentProduct != null && leadingItemType.equalsIgnoreCase(SapproductrecommendationConstants.PRODUCT))
   		{
   			productCode = currentProduct.getCode();
   		}
   		else if (currentCategory != null && leadingItemType.equalsIgnoreCase(SapproductrecommendationConstants.CATEGORY))
   		{
   			productCode = currentCategory.getCode();
   		}
		}
	
		model.addAttribute("productCode", productCode);
		model.addAttribute("componentId", component.getUid());	
						
		productRecommendationManagerFacade.prefetchRecommendations(request, component, productCode);
	}
}