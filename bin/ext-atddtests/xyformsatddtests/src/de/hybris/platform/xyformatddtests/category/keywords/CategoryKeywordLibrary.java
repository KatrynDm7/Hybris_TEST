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
package de.hybris.platform.xyformatddtests.category.keywords;

import de.hybris.platform.atddengine.keywords.AbstractKeywordLibrary;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.xyformsservices.model.YFormDefinitionModel;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * Robotframework library class for Category dynamic attributes
 */
public class CategoryKeywordLibrary extends AbstractKeywordLibrary
{

	@Autowired
	private CategoryService categoryService;


	public void verifyCategoryHasAllYFormDefinitions(final String categoryCode, final String... formDefinitionIds)
	{

		final CategoryModel category = categoryService.getCategoryForCode(categoryCode);

		final List<YFormDefinitionModel> allYFormDefinitions = category.getAllYFormDefinitions();

		if (allYFormDefinitions == null && formDefinitionIds.length > 0 || allYFormDefinitions.size() != formDefinitionIds.length)
		{
			Assert.fail("Category [" + categoryCode + "] has different number of YFomDefinitions than expected");
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
