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
package de.hybris.platform.bmecat.xmlwriter;

import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.Keyword;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;CatalogStructure&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class CatalogStructureTagWriter extends XMLTagWriter
{
	/**
	 * @param parent
	 */
	public CatalogStructureTagWriter(final CatalogGroupSystemTagWriter parent)
	{
		super(parent, true);
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.GROUP_ID, true));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.GROUP_NAME, true));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.GROUP_DESCRIPTION));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.PARENT_ID, true));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.GROUP_ORDER));
		addSubTagWriter(new MimeInfoTagWriter(this));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.KEYWORD));
		addSubTagWriter(new UserDefinedExtensionsTagWriter(this));
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getTagName()
	 */
	@Override
	protected String getTagName()
	{
		return BMECatConstants.XML.TAG.CATALOG_STRUCTURE;
	}

	@Override
	protected Map getAttributesMap(final Object object)
	{
		final BMECatExportContext exportCtx = (BMECatExportContext) object;
		final Category category = exportCtx.getCategory();
		return Collections.singletonMap("type", category == null ? "root" : (category.getSubcategoryCount() > 0 ? "node" : "leaf"));
	}

	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		final BMECatExportContext exportCtx = (BMECatExportContext) object;
		final Category category = exportCtx.getCategory();

		if (category == null)
		{
			writeVirtualRootcategory(exportCtx, xmlOut);
		}
		else
		{
			writeCategoryContent(exportCtx, xmlOut, category);
		}
	}

	protected String getCategoryID(final Category cat)
	{
		return cat.getCode().trim(); // since codes must no be uniqe we prepend the PK
	}

	/**
	 * Writes a virtual category '0' with the catalog versions code as name.
	 */
	protected void writeVirtualRootcategory(final BMECatExportContext exportCtx, final XMLOutputter xmlOut)
	{
		getSubTagWriter(BMECatConstants.XML.TAG.GROUP_ID).write(xmlOut, "1");
		getSubTagWriter(BMECatConstants.XML.TAG.GROUP_NAME).write(xmlOut, exportCtx.getCatVersion().getVersion());
		getSubTagWriter(BMECatConstants.XML.TAG.PARENT_ID).write(xmlOut, "0");
	}

	protected void writeCategoryContent(final BMECatExportContext exportCtx, final XMLOutputter xmlOut, final Category category)
	{
		// TODO: root category must have '1' as ID !!! see bug 5730 
		final String name = category.getName();

		getSubTagWriter(BMECatConstants.XML.TAG.GROUP_ID).write(xmlOut, getCategoryID(category));
		getSubTagWriter(BMECatConstants.XML.TAG.GROUP_NAME).write(xmlOut, name != null ? name : category.getCode());
		getSubTagWriter(BMECatConstants.XML.TAG.GROUP_DESCRIPTION).write(xmlOut,
				CatalogManager.getInstance().getDescription(category));

		final Category parentCategory = category.getSupercategory();
		final String parentId;
		if (parentCategory == null)
		{
			parentId = "1"; // link to virtual root category
		}
		else
		{
			parentId = getCategoryID(parentCategory);
		}

		getSubTagWriter(BMECatConstants.XML.TAG.PARENT_ID).write(xmlOut, parentId);
		final int order = CatalogManager.getInstance().getOrderAsPrimitive(category);
		if (order > 0)
		{
			getSubTagWriter(BMECatConstants.XML.TAG.GROUP_ORDER).write(xmlOut, Integer.toString(order));
		}


		final Map catMediaMap = new HashMap();

		for (final Iterator it = getCategoryMediaMapping().entrySet().iterator(); it.hasNext();)
		{
			final Map.Entry entry = (Map.Entry) it.next();
			final Collection medias = (Collection) category.getProperty((String) entry.getValue());
			if (medias != null && !medias.isEmpty())
			{
				catMediaMap.put(entry.getKey(), medias);
			}
		}

		//reading/writing hybris specific product-media-attributes ('picture', 'thumbnail')
		final Map hybris2bmecatMediaMap = exportCtx.getHybris2BmecatMediaMap();
		for (final Iterator it = hybris2bmecatMediaMap.entrySet().iterator(); it.hasNext();)
		{
			final Map.Entry entry = (Map.Entry) it.next();
			final String hybris = ((EnumerationValue) entry.getKey()).getCode();
			final String bmecat = ((EnumerationValue) entry.getValue()).getCode();

			final Media media = category.getProperty(hybris) instanceof Media ? (Media) category.getProperty(hybris) : null;
			if (media != null)
			{
				final Collection collection = (Collection) catMediaMap.get(bmecat);
				if (collection != null)
				{
					collection.add(media);
				}
				else
				{
					catMediaMap.put(bmecat, Collections.singletonList(media));
				}
			}
		}

		if (!catMediaMap.isEmpty())
		{
			exportCtx.setStringPurpose2MediaCollectionMap(catMediaMap);
			getSubTagWriter(BMECatConstants.XML.TAG.MIME_INFO).write(xmlOut, exportCtx);
		}

		final Collection keywords = CatalogManager.getInstance().getKeywords(category);
		if (keywords != null && !keywords.isEmpty())
		{
			for (final Iterator keywordIt = keywords.iterator(); keywordIt.hasNext();)
			{
				final Keyword keyword = (Keyword) keywordIt.next();
				getSubTagWriter(BMECatConstants.XML.TAG.KEYWORD).write(xmlOut, keyword.getKeyword());
			}
		}
		final UserDefinedExtensionsTagWriter udxTagWriter = (UserDefinedExtensionsTagWriter) getSubTagWriter(BMECatConstants.XML.TAG.USER_DEFINED_EXTENSIONS);
		if (!udxTagWriter.getAllSubTagWriter().isEmpty())
		{
			udxTagWriter.write(xmlOut, category);
		}
	}


	private Map getCategoryMediaMapping()
	{
		final Map mapping = new HashMap();
		mapping.put("thumbnail", CatalogConstants.Attributes.Category.THUMBNAILS);
		mapping.put("normal", CatalogConstants.Attributes.Category.NORMAL);
		mapping.put("detail", CatalogConstants.Attributes.Category.DETAIL);
		mapping.put("data_sheet", CatalogConstants.Attributes.Category.DATA_SHEET);
		mapping.put("logo", CatalogConstants.Attributes.Category.LOGO);
		mapping.put("others", CatalogConstants.Attributes.Category.OTHERS);
		return mapping;
	}

	protected UserDefinedExtensionsTagWriter getUserDefinedExtensionsTagWriter()
	{
		return (UserDefinedExtensionsTagWriter) getSubTagWriter(BMECatConstants.XML.TAG.USER_DEFINED_EXTENSIONS);
	}
}
