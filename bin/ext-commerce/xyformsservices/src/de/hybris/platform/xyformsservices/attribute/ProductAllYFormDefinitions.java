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
package de.hybris.platform.xyformsservices.attribute;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.xyformsservices.helpers.YFormDefinitionHelper;
import de.hybris.platform.xyformsservices.model.YFormDefinitionModel;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Supports return all the {@link YFormDefinitionModel}s from a Product Category hierarchy
 */
public class ProductAllYFormDefinitions implements DynamicAttributeHandler<List<YFormDefinitionModel>, ProductModel>,
		Serializable
{
	private transient YFormDefinitionHelper yFormDefinitionHelper;

	/**
	 * Returns all the {@link YFormDefinitionModel}s that are assigned to its supercategories and all their
	 * supercategories
	 * */
	@Override
	public List<YFormDefinitionModel> get(final ProductModel product)
	{
		final Collection<CategoryModel> supercategories = product.getSupercategories();

		if (CollectionUtils.isNotEmpty(supercategories))
		{
			return getYFormDefinitionHelper().getAllYFormDefinitions(supercategories);
		}

		return null;
	}

	@Override
	public void set(final ProductModel product, final List<YFormDefinitionModel> value)
	{
		throw new UnsupportedOperationException();
	}

	protected YFormDefinitionHelper getYFormDefinitionHelper()
	{
		return yFormDefinitionHelper;
	}

	@Required
	public void setYFormDefinitionHelper(final YFormDefinitionHelper yFormDefinitionHelper)
	{
		this.yFormDefinitionHelper = yFormDefinitionHelper;
	}

}
