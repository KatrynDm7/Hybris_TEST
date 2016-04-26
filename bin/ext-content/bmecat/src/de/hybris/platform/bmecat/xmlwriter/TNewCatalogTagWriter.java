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
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.variants.jalo.VariantProduct;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;TNewCatalog&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class TNewCatalogTagWriter extends XMLTagWriter
{
	/**
	 * @param parent
	 */
	public TNewCatalogTagWriter(final BMECatTagWriter parent)
	{
		super(parent);
		addSubTagWriter(new CatalogGroupSystemTagWriter(this));
		addSubTagWriter(new ArticleTagWriter(this));
		addSubTagWriter(new ArticleToCatalogGroupMapTagWriter(this));
	}

	protected void calculateItemsCount(final BMECatExportContext exportCtx)
	{
		final CatalogVersion catVersion = exportCtx.getCatVersion();
		int itemsToExport = 0;

		// categories
		if (exportCtx.getCategoriesProvider() != null)
		{
			itemsToExport += exportCtx.getCategoriesProvider().getItemCount();
		}
		else
		{
			itemsToExport += catVersion.getAllVisibleCategoryCount();
		}
		// products
		if (exportCtx.getProductsProvider() != null)
		{
			itemsToExport += exportCtx.getProductsProvider().getItemCount();
		}
		else
		{
			itemsToExport += catVersion.getAllVisibleProductCount();
		}
		// medias ???
		((BMECatTagWriter) getParent()).initItemsCount(itemsToExport);
	}

	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		final BMECatExportContext exportCtx = (BMECatExportContext) object;

		calculateItemsCount(exportCtx);

		final CatalogVersion catVersion = exportCtx.getCatVersion();

		//gets only all root categories
		final boolean hasCategories;
		if (exportCtx.getCategoriesProvider() != null)
		{
			hasCategories = exportCtx.getCategoriesProvider().getItemCount() > 0;
		}
		else
		{
			hasCategories = catVersion.getAllVisibleCategoryCount() > 0;
		}

		if (hasCategories)
		{
			if (isInfoEnabled())
			{
				info("exporting CATALOG_GROUP_SYSTEM...");
			}
			getSubTagWriter(BMECatConstants.XML.TAG.CATALOG_GROUP_SYSTEM).write(xmlOut, exportCtx);
		}

		//block size for search
		final int blockSize = 100;

		//number of all products
		final int productCount;
		if (exportCtx.getProductsProvider() != null)
		{
			productCount = exportCtx.getProductsProvider().getItemCount();
		}
		else
		{
			productCount = catVersion.getAllVisibleProductCount();
		}

		final boolean ignoreErrors = exportCtx.ignoreErrors();
		final UndoableXMLOutputter undoableXMLOut = ignoreErrors ? (UndoableXMLOutputter) xmlOut : null;

		for (int i = 0; i <= productCount; i = i + blockSize)
		{
			final Collection products;
			if (exportCtx.getProductsProvider() != null)
			{
				products = exportCtx.getProductsProvider().getItems(i, blockSize);
			}
			else
			{
				products = catVersion.getAllVisibleProducts(i, blockSize);
			}

			for (final Iterator it = products.iterator(); it.hasNext();)
			{
				final Product product = (Product) it.next();
				if (!(product instanceof VariantProduct))
				{
					exportCtx.setProduct(product);
					if (isInfoEnabled())
					{
						info("exporting ARTICLE (" + product.getCode() + ")...");
					}

					if (ignoreErrors)
					{
						undoableXMLOut.markSavePoint();
						try
						{
							getSubTagWriter(BMECatConstants.XML.TAG.ARTICLE).write(undoableXMLOut, exportCtx);
							undoableXMLOut.commitSavePoint();
						}
						catch (final Exception e)
						{
							if (isErrorEnabled())
							{
								error("error exporting product " + product + " : " + e.getLocalizedMessage() + " : "
										+ Utilities.getStackTraceAsString(e));
							}
							exportCtx.notifyError();
							undoableXMLOut.restoreSavePoint();
							exportCtx.addSkippedProduct(product);
						}
					}
					else
					{
						getSubTagWriter(BMECatConstants.XML.TAG.ARTICLE).write(xmlOut, exportCtx);
					}
					((BMECatTagWriter) getParent()).incItemCount(1);
				}
			}
		}

		//number of all categories of this version
		final int catCount;
		if (exportCtx.getCategoriesProvider() != null)
		{
			catCount = exportCtx.getCategoriesProvider().getItemCount();
		}
		else
		{
			catCount = catVersion.getAllVisibleCategoryCount();
		}

		if (isInfoEnabled())
		{
			info("exporting ARTICLE_TO_CATALOGGROUP_MAP...");
		}

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
				if (isInfoEnabled())
				{
					info("  exporting ARTICLE_TO_CATALOGGROUP_MAP (" + cat.getCode() + ")...");
				}
				if (!exportCtx.isSkippedCategory(cat)) // dont export links for skipped category
				{
					// output products ordered as getProducts() returns them ( according to link sequence number! ) 
					int number = 0;
					for (final Iterator productIt = cat.getProducts().iterator(); productIt.hasNext(); number++)
					{
						final Product product = (Product) productIt.next();
						if (!exportCtx.isSkippedProduct(product)) // dont export links for skipped products 
						{
							getSubTagWriter(BMECatConstants.XML.TAG.ARTICLE_TO_CATALOGGROUP_MAP).write(xmlOut, new Object[]
							{ cat, product, Integer.valueOf(number) });
						}
					}
				}
				((BMECatTagWriter) getParent()).incItemCount(1);
			}
		}
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getTagName()
	 */
	@Override
	protected String getTagName()
	{
		return BMECatConstants.XML.TAG.T_NEW_CATALOG;
	}

	public ArticleTagWriter getArticleTagWriter()
	{
		return (ArticleTagWriter) getSubTagWriter(BMECatConstants.XML.TAG.ARTICLE);
	}

	public CatalogGroupSystemTagWriter getCatalogGroupSystemTagWriter()
	{
		return (CatalogGroupSystemTagWriter) getSubTagWriter(BMECatConstants.XML.TAG.CATALOG_GROUP_SYSTEM);
	}
}
