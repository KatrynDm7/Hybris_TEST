/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.bmecat.jalo.bmecat2csv;

import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.jalo.CategoryManager;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.util.CSVCellDecorator;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


public class ClassificationGroupIDCellDecorator implements CSVCellDecorator
{

	private static final Logger LOG = Logger.getLogger(ClassificationGroupIDCellDecorator.class);

	public String decorate(final int position, final Map srcLine)
	{

		final String rawValue = (String) srcLine.get(Integer.valueOf(position));
		final String[] parts = rawValue.split(":");
		final StringBuilder groupID = new StringBuilder(parts[0]);
		String catalog = null;
		if (parts.length == 2)
		{
			catalog = parts[1];
		}

		if (findCategory(catalog, groupID.toString()))
		{
			return groupID.toString() + (catalog == null ? "" : ":" + catalog);
		}

		LOG.debug("Category [" + groupID + "] not found. Trying to adjust.");
		//change the format of id from "23-05-03-01" to "23050301"
		int pos = groupID.lastIndexOf("-");
		while (pos != -1)
		{
			groupID.deleteCharAt(pos);
			pos = groupID.lastIndexOf("-");
		}

		if (findCategory(catalog, groupID.toString()))
		{
			return groupID.toString() + (catalog == null ? "" : ":" + catalog);
		}
		else
		{
			final Map values = new HashMap();
			values.put("param", groupID.toString());
			final List list = JaloSession
					.getCurrentSession()
					.getFlexibleSearch()
					.search("SELECT {pk} FROM {ClassificationClass} WHERE {code}=?param", values,
							Collections.singletonList(ClassificationClass.class), true, true, 0, -1).getResult();
			if (list != null && !list.isEmpty())
			{
				return ((ClassificationClass) list.get(0)).getCode() + (catalog == null ? "" : ":" + catalog);
			}
			else
			{
				LOG.warn("Category [" + groupID + "] not found.");
				return groupID.toString() + (catalog == null ? "" : ":" + catalog);
			}
		}
	}

	private boolean findCategoryAtCatalog(final String catalogID, final String categoryCode)
	{
		final Catalog catalog = CatalogManager.getInstance().getCatalog(catalogID);
		if (catalog == null)
		{
			LOG.warn("Catalog [" + catalogID + "] not found. -> Can not search [" + categoryCode + "]");
			return false;
		}
		final Collection<Category> categories = catalog.getCategories(categoryCode);
		return (categories != null && !categories.isEmpty());
	}

	private boolean findCategory(final String catalogID, final String categoryCode)
	{
		if (catalogID != null)
		{
			return findCategoryAtCatalog(catalogID, categoryCode);
		}
		final Collection<Category> categories = CategoryManager.getInstance().getCategoriesByCode(categoryCode);
		return (categories != null && !categories.isEmpty());
	}
}
