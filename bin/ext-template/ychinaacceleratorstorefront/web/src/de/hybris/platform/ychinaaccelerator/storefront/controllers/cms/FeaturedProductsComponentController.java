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
package de.hybris.platform.ychinaaccelerator.storefront.controllers.cms;



import de.hybris.platform.chinaaccelerator.services.model.cms.components.FeaturedProductsComponentModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ychinaaccelerator.storefront.controllers.ControllerConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Controller for CMS ProductReferencesComponent.
 */
@Controller("FeaturedProductsComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.FeaturedProductsComponent)
public class FeaturedProductsComponentController extends AbstractCMSComponentController<FeaturedProductsComponentModel>
{
	protected static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE);

	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;


	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final FeaturedProductsComponentModel component)
	{
		final List<ProductData> products = new ArrayList<>();

		products.addAll(collectLinkedProducts(component));

		model.addAttribute("title", component.getTitle());
		model.addAttribute("productData", products);
	}

	protected List<ProductData> collectLinkedProducts(final FeaturedProductsComponentModel component)
	{
		final List<ProductData> products = new ArrayList<>();

		for (final ProductModel productModel : component.getProducts())
		{
			products.add(productFacade.getProductForOptions(productModel, PRODUCT_OPTIONS));
		}

		return products;
	}
}
