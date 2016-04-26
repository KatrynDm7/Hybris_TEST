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
package de.hybris.platform.financialacceleratorstorefront.component.renderer;

import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.financialservices.model.components.ComparisonPanelCMSComponentModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Map;

import javax.servlet.jsp.PageContext;

import org.springframework.beans.factory.annotation.Required;


/**
 * The class of ComparisonPanelCMSComponentRenderer.
 */
public class ComparisonPanelCMSComponentRenderer<C extends ComparisonPanelCMSComponentModel> extends
		DefaultAddOnSubstitutingCMSComponentRenderer<C>
{
	private Converter<ProductModel, ProductData> productConverter;


	@Override
	protected Map<String, Object> getVariablesToExpose(final PageContext pageContext, final C component)
	{
		final Map<String, Object> variables = super.getVariablesToExpose(pageContext, component);

		final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = new ProductCategorySearchPageData<>();
		searchPageData.setResults(Converters.convertAll(component.getProducts(), getProductConveter()));
		variables.put("searchPageData", searchPageData);


		final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> mandatoryOptionProductsData = new ProductCategorySearchPageData<>();
		mandatoryOptionProductsData.setResults(Converters.convertAll(component.getMandatoryBundleProducts(), getProductConveter()));
		variables.put("mandatoryOptionProducts", mandatoryOptionProductsData);

		return variables;
	}

	protected Converter<ProductModel, ProductData> getProductConveter()
	{
		return productConverter;
	}

	@Required
	public void setProductConverter(final Converter<ProductModel, ProductData> productConverter)
	{
		this.productConverter = productConverter;
	}
}
