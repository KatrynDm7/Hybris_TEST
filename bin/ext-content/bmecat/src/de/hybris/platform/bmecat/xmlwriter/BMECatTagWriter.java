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

import java.io.IOException;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;
import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;BMECat&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class BMECatTagWriter extends XMLTagWriter
{
	private int itemsToExport = 0;
	private int exportedItems = 0;

	public BMECatTagWriter()
	{
		super(null, true);
		addSubTagWriter(new HeaderTagWriter(this));
		addSubTagWriter(new TNewCatalogTagWriter(this));
	}

	protected void initItemsCount(final int total)
	{
		this.itemsToExport = total;
	}

	protected void incItemCount(final int toAdd)
	{
		this.exportedItems += toAdd;
	}

	public int getTotalItemsCount()
	{
		return this.itemsToExport;
	}

	public int getCurrentItemsCount()
	{
		return this.exportedItems;
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getTagName()
	 */
	@Override
	protected String getTagName()
	{
		return BMECatConstants.XML.TAG.BMECAT;
	}



	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getAttributesMap(java.lang.Object)
	 */
	@Override
	protected Map getAttributesMap(final Object object)
	{
		final Map attributesMap = new LinkedMap();
		attributesMap.put("version", "1.2");
		attributesMap.put("xmlns", "http://www.bmecat.org/XMLSchema/1.2/bmecat_new_catalog");

		/*
		 * These two attributes are incompatible with some bmecat readers. See Bug #4416.
		 * 
		 * attributesMap.put( "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance" ); attributesMap.put(
		 * "xsi:schemaLocation", "http://www.bmecat.org/XMLSchema/1.2/bmecat_new_catalog bmecat_new_catalog_1_2.xsd" );
		 */

		return attributesMap;
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#writeContent(org.znerd.xmlenc.XMLOutputter,
	 *      java.lang.Object)
	 */
	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		final BMECatExportContext exportContainer = (BMECatExportContext) object;

		if (isInfoEnabled())
		{
			info("exporting HEADER...");
		}
		getHeaderTagWriter().write(xmlOut, exportContainer);

		if (isInfoEnabled())
		{
			info("exporting T_NEW_CATALOG...");
		}
		if (BMECatConstants.Enumerations.TransactionModeEnum.T_NEW_CATALOG.equals(exportContainer.getTransactionMode().getCode()))
		{
			getTNewCatalogTagWriter().write(xmlOut, exportContainer);
		}
	}

	private HeaderTagWriter getHeaderTagWriter()
	{
		return (HeaderTagWriter) getSubTagWriter(BMECatConstants.XML.TAG.HEADER);
	}

	public TNewCatalogTagWriter getTNewCatalogTagWriter()
	{
		return (TNewCatalogTagWriter) getSubTagWriter(BMECatConstants.XML.TAG.T_NEW_CATALOG);
	}

	/**
	 * Returns the <code>UserDefinedExtensionsTagWriter</code> beneath the <code>HeaderTagWriter</code>
	 * 
	 * @return a <code>UserDefinedExtensionsTagWriter</code>
	 */
	public UserDefinedExtensionsTagWriter getHeaderUserDefinedExtensionsTagWriter()
	{
		return getHeaderTagWriter().getUserDefinedExtensionsTagWriter();
	}

	/**
	 * Returns the <code>UserDefinedExtensionsTagWriter</code> beneath the <code>ArticleTagWriter</code>
	 * 
	 * @return a <code>UserDefinedExtensionsTagWriter</code>
	 */
	public UserDefinedExtensionsTagWriter getArticleUserDefinedExtensionsTagWriter()
	{
		return getTNewCatalogTagWriter().getArticleTagWriter().getUserDefinedExtensionsTagWriter();
	}

	/**
	 * Returns the <code>UserDefinedExtensionsTagWriter</code> beneath the <code>CatalogStructureTagWriter</code>
	 * 
	 * @return a <code>UserDefinedExtensionsTagWriter</code>
	 */
	public UserDefinedExtensionsTagWriter getCatalogStructureUserDefinedExtensionsTagWriter()
	{
		return getTNewCatalogTagWriter().getCatalogGroupSystemTagWriter().getCatalogStructureTagWriter()
				.getUserDefinedExtensionsTagWriter();
	}
}
