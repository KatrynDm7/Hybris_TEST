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
 */
package de.hybris.platform.xyformsfacades.strategy;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.xyformsfacades.data.YFormDataData;
import de.hybris.platform.xyformsfacades.data.YFormDefinitionData;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;
import de.hybris.platform.xyformsservices.model.YFormDataModel;
import de.hybris.platform.xyformsservices.model.YFormDefinitionModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;


/**
 * Strategy used to get yForm Definitions associated to a Product.
 */
public class GetYFormDefinitionsForProductStrategy implements GetYFormDefinitionsForItemStrategy<String>
{
	@Resource(name = "productService")
	private ProductService productService;

	@Resource(name = "yformDefinitionConverter")
	private Converter<YFormDefinitionModel, YFormDefinitionData> yformDefinitionConverter;

	@Resource(name = "yformDataConverter")
	private Converter<YFormDataModel, YFormDataData> yformDataConverter;

	/**
	 * Returns a List of YFormDefinitionData that relate to the product
	 * 
	 * @return List<YFormDefinitionData>
	 * @throws YFormServiceException
	 */
	@Override
	public List<YFormDefinitionData> execute(final String code) throws YFormServiceException
	{
		final List<YFormDefinitionData> yFormDefinitionDataList = new ArrayList<YFormDefinitionData>();

		ProductModel product = null;
		try
		{
			product = getProductService().getProductForCode(code);
		}
		catch (final ModelNotFoundException e)
		{
			throw new YFormServiceException(e);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new YFormServiceException(e);
		}

		final List<YFormDefinitionModel> allYFormDefinitions = product.getAllYFormDefinitions();
		if (CollectionUtils.isNotEmpty(allYFormDefinitions))
		{
			for (final YFormDefinitionModel yFormDefinitionModel : allYFormDefinitions)
			{
				yFormDefinitionDataList.add(getYFormDefinitionConverter().convert(yFormDefinitionModel));
			}
		}

		return CollectionUtils.isNotEmpty(yFormDefinitionDataList) ? yFormDefinitionDataList : null;
	}

	protected ProductService getProductService()
	{
		return productService;
	}

	protected void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	protected Converter<YFormDefinitionModel, YFormDefinitionData> getYFormDefinitionConverter()
	{
		return yformDefinitionConverter;
	}

	protected Converter<YFormDataModel, YFormDataData> getYFormDataConverter()
	{
		return yformDataConverter;
	}
}
