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
package de.hybris.platform.commerceservices.category;


import de.hybris.platform.category.model.CategoryModel;

import java.util.Collection;
import java.util.List;

/**
 * The CommerceCategoryService supports looking up categories within the current session product catalog. Other
 * types of session catalog, i.e. content catalogs and classification catalogs are excluded.
 */
public interface CommerceCategoryService
{
	/**
	 * Retrieves the {@link de.hybris.platform.category.model.CategoryModel} with the specific <code>code</code>.
	 *
	 * @param code
	 *           the code of the to be found {@link de.hybris.platform.category.model.CategoryModel}
	 * @return found {@link de.hybris.platform.category.model.CategoryModel}
	 *
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no category with the specified code can be found.
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            if more than one category with the specified <code>code</code> and <code>catalogVersion</code> was
	 *            found.
	 * @throws IllegalArgumentException
	 *            if parameter <code>code</code> is <code>null</code>.
	 */
	CategoryModel getCategoryForCode(String code);

	/**
	 * Returns <b>all</b> paths for this {@link CategoryModel}. Each path is a list of all categories in the path from
	 * root to this category.
	 *
	 * @param category
	 *           the (sub){@link CategoryModel}
	 * @return all paths for this {@link CategoryModel}, or empty collection if this category has no super-categories.
	 * @throws IllegalArgumentException
	 *            if parameter <code>category</code> is <code>null</code>.
	 */
	Collection<List<CategoryModel>> getPathsForCategory(CategoryModel category);
}
