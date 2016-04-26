/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.productconfig.frontend.breadcrumb;

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.ProductBreadcrumbBuilder;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.util.localization.Localization;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public class ProductConfigureBreadcrumbBuilder extends ProductBreadcrumbBuilder
{

	private static final String LAST_LINK_CLASS = "active";

	private Converter<ProductModel, ProductData> productConverter;

	@Override
	public List<Breadcrumb> getBreadcrumbs(final ProductModel productModel) throws IllegalArgumentException
	{
		final List<Breadcrumb> breadcrumbs = super.getBreadcrumbs(productModel);

		for (final Breadcrumb breadcrumb : breadcrumbs)
		{
			if (LAST_LINK_CLASS.equalsIgnoreCase(breadcrumb.getLinkClass()))
			{
				breadcrumb.setLinkClass(null);
			}
		}
		final Breadcrumb last = new Breadcrumb(getConfigurationUrl(productModel), getLinkText(), LAST_LINK_CLASS);
		breadcrumbs.add(last);

		return breadcrumbs;
	}

	protected String getLinkText()
	{
		if (Registry.isStandaloneMode())
		{
			return "TEST-STANDALONE-MODE";
		}
		return Localization.getLocalizedString("sapproductconfig.config.breadcrumb");
	}

	private String getConfigurationUrl(final ProductModel productModel)
	{
		final String productUrl = super.getProductModelUrlResolver().resolve(productModel);
		return productUrl + "/config";
	}


	protected Converter<ProductModel, ProductData> getProductConverter()
	{
		return productConverter;
	}

	@Required
	public void setProductConverter(final Converter<ProductModel, ProductData> productConverter)
	{
		this.productConverter = productConverter;
	}


}