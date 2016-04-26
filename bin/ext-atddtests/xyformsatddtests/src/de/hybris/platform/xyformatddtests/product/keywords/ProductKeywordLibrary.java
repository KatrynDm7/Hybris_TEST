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
package de.hybris.platform.xyformatddtests.product.keywords;

import de.hybris.platform.atddengine.keywords.AbstractKeywordLibrary;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.xyformsservices.model.YFormDefinitionModel;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * Robotframework library class for Product dynamic attributes
 */
public class ProductKeywordLibrary extends AbstractKeywordLibrary
{

	@Autowired
	private ProductService productService;

	public void verifyProductHasAllYFormDefinitions(final String productCode, final String... formDefinitionIds)
	{

		final ProductModel product = productService.getProductForCode(productCode);

		final List<YFormDefinitionModel> allYFormDefinitions = product.getAllYFormDefinitions();

		if (allYFormDefinitions == null && formDefinitionIds.length > 0 || allYFormDefinitions.size() != formDefinitionIds.length)
		{
			Assert.fail("Product [" + productCode + "] has different number of YFomDefinitions than expected");
		}
		final Set<String> formDefinitionIdsSet = new HashSet<>(Arrays.asList(formDefinitionIds));
		for (int i = 0; i < formDefinitionIds.length; i++)
		{
			final YFormDefinitionModel yFormDefinitionModel = allYFormDefinitions.get(i);
			Assert.assertTrue(formDefinitionIdsSet.contains(yFormDefinitionModel.getApplicationId() + ":"
					+ yFormDefinitionModel.getFormId()));
		}
	}
}
