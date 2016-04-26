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
package de.hybris.platform.configurablebundlefacades.converters.populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.configurablebundlefacades.data.BundleTemplateData;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * ProductBundlePopulator to populate all the product data and the bundleTemplate data that belongs to the package
 *
 * @param <SOURCE>
 *           ProductModel
 * @param <TARGET>
 *           ProductData
 */
public class ProductBundlePopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		AbstractProductPopulator<SOURCE, TARGET>
{

	private Converter<BundleTemplateModel, BundleTemplateData> bundleTemplateConverter;

	private Converter<ProductModel, ProductData> productConverter;

	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		validateParameterNotNullStandardMessage("productData", productData);

		final List<BundleTemplateData> bundleTemplateDataList = new ArrayList<>();

		if (productModel != null && productModel.getBundleTemplates() != null)
		{
			//from the given product Id get component Ids in which the product is part of
			for (final BundleTemplateModel template : productModel.getBundleTemplates())
			{
				if (template.getParentTemplate() != null && template.getParentTemplate().getChildTemplates() != null)
				{
					// from component id get parent package and get all the components belongs to that Package.
					for (final BundleTemplateModel childTemplate : template.getParentTemplate().getChildTemplates())
					{
						//Populate the bundleTemplate data for each component.
						final BundleTemplateData bundleTemplateData = bundleTemplateConverter.convert(childTemplate);

						//Populate the product data that belongs to that component.
						//get the product information
						final List<ProductData> productDataList = Converters.convertAll(childTemplate.getProducts(), productConverter);

						//Bind the product data to bundleTemplate data
						bundleTemplateData.setProducts(productDataList);

						//collect all the components that belongs to the package
						bundleTemplateDataList.add(bundleTemplateData);
					}
				}
			}
		}

		productData.setSoldIndividually(BooleanUtils.toBoolean(productModel.getSoldIndividually()));
		productData.setBundleTemplates(bundleTemplateDataList);
	}


	protected Converter<BundleTemplateModel, BundleTemplateData> getBundleTemplateConverter()
	{
		return bundleTemplateConverter;
	}

	@Required
	public void setBundleTemplateConverter(final Converter<BundleTemplateModel, BundleTemplateData> bundleTemplateConverter)
	{
		this.bundleTemplateConverter = bundleTemplateConverter;
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
