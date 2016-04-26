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
import de.hybris.platform.catalog.jalo.Agreement;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.util.Utilities;

import java.io.IOException;
import java.util.Iterator;

import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;Header&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class HeaderTagWriter extends XMLTagWriter
{
	/**
	 * @param parent
	 */
	public HeaderTagWriter(final BMECatTagWriter parent)
	{
		super(parent, true);
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.GENERATOR_INFO));
		addSubTagWriter(new CatalogTagWriter(this));
		addSubTagWriter(new BuyerTagWriter(this));
		addSubTagWriter(new SupplierTagWriter(this));
		addSubTagWriter(new AgreementTagWriter(this));
		addSubTagWriter(new UserDefinedExtensionsTagWriter(this));
	}

	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		final BMECatExportContext exportCtx = (BMECatExportContext) object;
		final CatalogVersion catVersion = exportCtx.getCatVersion();
		getSubTagWriter(BMECatConstants.XML.TAG.GENERATOR_INFO).write(xmlOut, "hybris Platform, www.hybris.de");
		if (isInfoEnabled())
		{
			info("exporting CATALOG...");
		}
		getSubTagWriter(BMECatConstants.XML.TAG.CATALOG).write(xmlOut, object);

		final boolean ignoreErrors = exportCtx.ignoreErrors();
		final UndoableXMLOutputter undoableXMLOut = ignoreErrors ? (UndoableXMLOutputter) xmlOut : null;

		if (catVersion.getCatalog().getBuyer() != null)
		{
			if (isInfoEnabled())
			{
				info("exporting BUYER...");
			}
			exportCtx.setCompany(catVersion.getCatalog().getBuyer());
			if (ignoreErrors)
			{
				undoableXMLOut.markSavePoint();
				try
				{
					getSubTagWriter(BMECatConstants.XML.TAG.BUYER).write(xmlOut, exportCtx);
					undoableXMLOut.commitSavePoint();
				}
				catch (final Exception e)
				{
					if (isErrorEnabled())
					{
						error("error exporting buyer " + catVersion.getCatalog().getBuyer() + " : " + e.getLocalizedMessage() + " : "
								+ Utilities.getStackTraceAsString(e));
					}
					exportCtx.notifyError();
					undoableXMLOut.restoreSavePoint();
				}
			}
			else
			{
				getSubTagWriter(BMECatConstants.XML.TAG.BUYER).write(xmlOut, exportCtx);
			}
		}

		if (isInfoEnabled())
		{
			info("exporting SUPPLIER...");
		}
		exportCtx.setCompany(catVersion.getCatalog().getSupplier());
		if (ignoreErrors)
		{
			undoableXMLOut.markSavePoint();
			try
			{
				getSubTagWriter(BMECatConstants.XML.TAG.SUPPLIER).write(xmlOut, exportCtx);
				undoableXMLOut.commitSavePoint();
			}
			catch (final Exception e)
			{
				if (isErrorEnabled())
				{
					error("error exporting supplier " + catVersion.getCatalog().getSupplier() + " : " + e.getLocalizedMessage()
							+ " : " + Utilities.getStackTraceAsString(e));
				}
				exportCtx.notifyError();
				undoableXMLOut.restoreSavePoint();
			}
		}
		else
		{
			getSubTagWriter(BMECatConstants.XML.TAG.SUPPLIER).write(xmlOut, exportCtx);
		}

		if (isInfoEnabled())
		{
			info("exporting AGREEMENT(s)...");
		}
		for (final Iterator it = catVersion.getAgreements().iterator(); it.hasNext();)
		{
			final Agreement agreement = (Agreement) it.next();
			if (ignoreErrors)
			{
				undoableXMLOut.markSavePoint();
				try
				{
					getSubTagWriter(BMECatConstants.XML.TAG.AGREEMENT).write(xmlOut, agreement);
					undoableXMLOut.commitSavePoint();
				}
				catch (final Exception e)
				{
					if (isErrorEnabled())
					{
						error("error exporting agreement " + agreement + " : " + e.getLocalizedMessage() + " : "
								+ Utilities.getStackTraceAsString(e));
					}
					exportCtx.notifyError();
					undoableXMLOut.restoreSavePoint();
				}
			}
			else
			{
				getSubTagWriter(BMECatConstants.XML.TAG.AGREEMENT).write(xmlOut, agreement);
			}
		}

		if (isInfoEnabled())
		{
			info("exporting USER_DEFINED_EXTENSIONS...");
		}
		final UserDefinedExtensionsTagWriter udxTagWriter = (UserDefinedExtensionsTagWriter) getSubTagWriter(BMECatConstants.XML.TAG.USER_DEFINED_EXTENSIONS);
		if (!udxTagWriter.getAllSubTagWriter().isEmpty())
		{
			udxTagWriter.write(xmlOut, object);
		}
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getTagName()
	 */
	@Override
	protected final String getTagName()
	{
		return BMECatConstants.XML.TAG.HEADER;
	}

	protected UserDefinedExtensionsTagWriter getUserDefinedExtensionsTagWriter()
	{
		return (UserDefinedExtensionsTagWriter) getSubTagWriter(BMECatConstants.XML.TAG.USER_DEFINED_EXTENSIONS);
	}
}
