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
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.util.Utilities;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;CatalogGroupSystem&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class CatalogGroupSystemTagWriter extends XMLTagWriter
{
	private int categoriesToExport = 0;
	private int exportedCategories = 0;

	/**
	 * @param parent
	 */
	public CatalogGroupSystemTagWriter(final TNewCatalogTagWriter parent)
	{
		super(parent);
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.GROUP_SYSTEM_ID));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.GROUP_SYSTEM_NAME));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.GROUP_SYSTEM_DESCRIPTION));
		addSubTagWriter(new CatalogStructureTagWriter(this));
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getTagName()
	 */
	@Override
	protected String getTagName()
	{
		return BMECatConstants.XML.TAG.CATALOG_GROUP_SYSTEM;
	}

	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		final BMECatExportContext exportCtx = (BMECatExportContext) object;
		final CatalogVersion catVersion = exportCtx.getCatVersion();
		getSubTagWriter(BMECatConstants.XML.TAG.GROUP_SYSTEM_ID).write(xmlOut, catVersion.getCategorySystemID());
		getSubTagWriter(BMECatConstants.XML.TAG.GROUP_SYSTEM_NAME).write(xmlOut, catVersion.getCategorySystemName());
		getSubTagWriter(BMECatConstants.XML.TAG.GROUP_SYSTEM_DESCRIPTION).write(xmlOut, catVersion.getCategorySystemDescription());

		final int catCount;
		if (exportCtx.getCategoriesProvider() != null)
		{
			catCount = exportCtx.getCategoriesProvider().getItemCount();
		}
		else
		{
			catCount = catVersion.getAllVisibleCategoryCount();
		}
		this.categoriesToExport = catCount;

		if (catCount > 0)
		{
			/*
			 * write virtual root category first
			 */
			exportCtx.setCategory(null);
			getSubTagWriter(BMECatConstants.XML.TAG.CATALOG_STRUCTURE).write(xmlOut, exportCtx);

			/*
			 * export categories in ranges
			 */
			final int blockSize = 100;

			final boolean ignoreErrors = exportCtx.ignoreErrors();
			final UndoableXMLOutputter undoableXMLOut = ignoreErrors ? (UndoableXMLOutputter) xmlOut : null;

			final boolean suppressEmpty = exportCtx.suppressEmptyCategories();

			for (int i = 0; i <= catCount; i = i + blockSize)
			{
				final Collection categories;
				if (exportCtx.getCategoriesProvider() != null)
				{
					categories = exportCtx.getCategoriesProvider().getItems(i, blockSize);
				}
				else
				{
					categories = catVersion.getAllVisibleCategories(i, blockSize);
				}

				for (final Iterator it = categories.iterator(); it.hasNext();)
				{
					final Category cat = (Category) it.next();
					exportCtx.setCategory(cat);
					if (!suppressEmpty || cat.containsProducts())
					{
						if (ignoreErrors)
						{
							undoableXMLOut.markSavePoint();
							try
							{
								getSubTagWriter(BMECatConstants.XML.TAG.CATALOG_STRUCTURE).write(undoableXMLOut, exportCtx);
								undoableXMLOut.commitSavePoint();
							}
							catch (final Exception e)
							{
								if (isErrorEnabled())
								{
									error("error exporting category " + cat + " : " + e.getLocalizedMessage() + " : "
											+ Utilities.getStackTraceAsString(e));
								}
								exportCtx.notifyError();
								undoableXMLOut.restoreSavePoint();
								exportCtx.addSkippedCategory(cat);
							}
						}
						else
						{
							getSubTagWriter(BMECatConstants.XML.TAG.CATALOG_STRUCTURE).write(xmlOut, exportCtx);
						}
					}
					else
					{
						exportCtx.addSkippedCategory(cat);
						if (isInfoEnabled())
						{
							info("skipped category " + cat + " since it does not contain products");
						}
					}
					exportedCategories++;
				}
			}
		}
	}

	protected CatalogStructureTagWriter getCatalogStructureTagWriter()
	{
		return (CatalogStructureTagWriter) getSubTagWriter(BMECatConstants.XML.TAG.CATALOG_STRUCTURE);
	}


	/**
	 * @return Returns the categoriesToExport.
	 */
	public int getCategoriesToExport()
	{
		return categoriesToExport;
	}


	/**
	 * @return Returns the exportedCategories.
	 */
	public int getExportedCategories()
	{
		return exportedCategories;
	}
}
