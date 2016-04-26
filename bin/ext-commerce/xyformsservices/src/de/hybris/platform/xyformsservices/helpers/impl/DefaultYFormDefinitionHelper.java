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
package de.hybris.platform.xyformsservices.helpers.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;
import de.hybris.platform.xyformsservices.form.YFormService;
import de.hybris.platform.xyformsservices.helpers.YFormDefinitionHelper;
import de.hybris.platform.xyformsservices.model.YFormDefinitionModel;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


public class DefaultYFormDefinitionHelper implements YFormDefinitionHelper
{
	private static final Logger LOG = Logger.getLogger(DefaultYFormDefinitionHelper.class);

	private YFormService yFormService;

	/**
	 * Returns an ordered List of {@link YFormDefinitionModel}s that assigned to the categories and their all
	 * supercategories in the Category hierarchy. Each {@link YFormDefinitionModel} is unique in the List. The order of
	 * the elements is specified by the formDefinitionComparator.
	 * 
	 * @param categories
	 * @return an ordered List of unique {@link YFormDefinitionModel} elements
	 * @throws NullPointerException
	 *            if categories is null
	 */
	@Override
	public List<YFormDefinitionModel> getAllYFormDefinitions(final Collection<CategoryModel> categories)
	{
		Preconditions.checkNotNull(categories, "categories cannot be null");

		final Set<YFormDefinitionModel> forms = collectYFormDefinitionForms(createCategorySet(categories));

		return CollectionUtils.isNotEmpty(forms) ? Lists.newArrayList(forms) : null;
	}

	/**
	 * Returns an ordered List of {@link YFormDefinitionModel}s that assigned to the category and its all supercategories
	 * in the Category hierarchy. Each {@link YFormDefinitionModel} is unique in the List. The order of the elements is
	 * specified by the formDefinitionComparator.
	 * 
	 * @param category
	 * @return an ordered List of unique {@link YFormDefinitionModel} elements
	 * @throws NullPointerException
	 *            if category is null
	 */
	@Override
	public List<YFormDefinitionModel> getAllYFormDefinitions(final CategoryModel category)
	{

		Preconditions.checkNotNull(category, "category cannot be null");

		final Set<YFormDefinitionModel> forms = collectYFormDefinitionForms(createCategorySet(category));

		return CollectionUtils.isNotEmpty(forms) ? Lists.newArrayList(forms) : null;
	}

	/**
	 * It gathers all the {@link YFormDefinitionModel}s that assigned to the categories. The order of the elements is
	 * specified by the formDefinitionComparator.
	 * 
	 * @param categories
	 * @return SortedSet of YFormDefinitions if the categories have YFormDefinitions assigned to themselves, otherwise
	 *         empty set
	 * @throws NullPointerException
	 *            if categories is null
	 */
	protected final Set<YFormDefinitionModel> collectYFormDefinitionForms(final Collection<CategoryModel> categories)
	{
		Preconditions.checkNotNull(categories, "categories cannot be null");

		final Set<YFormDefinitionModel> forms = createYFormDefinitionSet();

		for (final CategoryModel category : categories)
		{
			final Set<YFormDefinitionModel> yFormDefinitions = category.getYFormDefinitions();

			if (CollectionUtils.isNotEmpty(yFormDefinitions))
			{
				for (final YFormDefinitionModel yFormDefinition : yFormDefinitions)
				{
					final YFormDefinitionModel latestVersionFormDefinition = getLatestVersionFormDefinition(yFormDefinition);
					if (latestVersionFormDefinition != null)
					{
						forms.add(latestVersionFormDefinition);
					}
				}
			}
		}

		return forms;
	}


	/**
	 * Get the latest version of form definition by given form definition.
	 * 
	 * @param yFormDefinition
	 *           form definition model
	 * @return the latest form definition model or null if no form definition find.
	 */
	protected YFormDefinitionModel getLatestVersionFormDefinition(final YFormDefinitionModel yFormDefinition)
	{
		try
		{
			return getYFormService().getYFormDefinition(yFormDefinition.getApplicationId(), yFormDefinition.getFormId());
		}
		catch (final YFormServiceException e)
		{
			LOG.warn(e.getMessage(), e);
			return null;
		}
	}

	private Set<YFormDefinitionModel> createYFormDefinitionSet()
	{

		return new LinkedHashSet<YFormDefinitionModel>();
	}

	/**
	 * Creates a set of the categories and their all supercategories.
	 */
	private Set<CategoryModel> createCategorySet(final Collection<CategoryModel> categories)
	{
		final Set<CategoryModel> categorySet = Sets.newHashSet();

		for (final CategoryModel category : categories)
		{
			categorySet.addAll(createCategorySet(category));
		}

		return categorySet;
	}

	/**
	 * Creates a set of the category and its all supercategories.
	 */
	private Set<CategoryModel> createCategorySet(final CategoryModel category)
	{

		final Set<CategoryModel> categories = Sets.newHashSet();

		categories.add(category);

		final Collection<CategoryModel> allSupercategories = category.getAllSupercategories();

		if (CollectionUtils.isNotEmpty(allSupercategories))
		{
			categories.addAll(allSupercategories);
		}
		return categories;
	}

	protected YFormService getYFormService()
	{
		return yFormService;
	}

	@Required
	public void setYFormService(final YFormService yFormService)
	{
		this.yFormService = yFormService;
	}
}
