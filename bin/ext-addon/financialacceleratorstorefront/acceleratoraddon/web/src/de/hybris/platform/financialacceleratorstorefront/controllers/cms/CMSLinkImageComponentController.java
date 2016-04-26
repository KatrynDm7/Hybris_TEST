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

package de.hybris.platform.financialacceleratorstorefront.controllers.cms;

import de.hybris.platform.acceleratorstorefrontcommons.tags.Functions;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.enums.LinkTargets;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.financialacceleratorstorefront.controllers.ControllerConstants;
import de.hybris.platform.financialservices.model.components.CMSLinkImageComponentModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * CMS Link Image Component Controller
 */
@Controller("CMSLinkImageComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.CMSLinkImageComponent)
public class CMSLinkImageComponentController extends SubstitutingCMSAddOnComponentController<CMSLinkImageComponentModel>
{
	@Resource(name = "productUrlConverter")
	private Converter<ProductModel, ProductData> productUrlConverter;

	@Resource(name = "categoryUrlConverter")
	private Converter<CategoryModel, CategoryData> categoryUrlConverter;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final CMSLinkImageComponentModel component)
	{
		final String url = getUrl(component);
		model.addAttribute("url", url);

		if (component.getTarget() != null && !LinkTargets.SAMEWINDOW.equals(component.getTarget()))
		{
			final String target = " target=\"_blank\"";
			model.addAttribute("target", target);
		}
	}

	protected String getUrl(final CMSLinkImageComponentModel component)
	{
		// Call the function getUrlForCMSLinkComponent so that this code is only in one place
		return Functions.getUrlForCMSLinkComponent(component, getProductUrlConverter(), getCategoryUrlConverter());
	}

	protected Converter<ProductModel, ProductData> getProductUrlConverter()
	{
		return productUrlConverter;
	}

	public void setProductUrlConverter(final Converter<ProductModel, ProductData> productUrlConverter)
	{
		this.productUrlConverter = productUrlConverter;
	}

	protected Converter<CategoryModel, CategoryData> getCategoryUrlConverter()
	{
		return categoryUrlConverter;
	}

	public void setCategoryUrlConverter(final Converter<CategoryModel, CategoryData> categoryUrlConverter)
	{
		this.categoryUrlConverter = categoryUrlConverter;
	}
}
