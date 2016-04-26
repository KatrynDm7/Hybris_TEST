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
import de.hybris.platform.bmecat.parser.taglistener.CatalogTagListener;
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Currency;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;
import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;Catalog&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class CatalogTagWriter extends XMLTagWriter
{
	/**
	 * @param parent
	 */
	public CatalogTagWriter(final HeaderTagWriter parent)
	{
		super(parent, true);
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.LANGUAGE, true));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.CATALOG_ID, true));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.CATALOG_VERSION, true));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.CATALOG_NAME));
		addSubTagWriter(new DateTimeTagWriter(this, "generation_date"), "generation_date");
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.TERRITORY));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.CURRENCY));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.MIME_ROOT));

		final Map priceFlagMap = new LinkedMap();
		priceFlagMap.put(CatalogTagListener.PRICEFLAG_INCLFREIGHT, new SimpleTypedTagWriter(this,
				BMECatConstants.XML.TAG.PRICE_FLAG, CatalogTagListener.PRICEFLAG_INCLFREIGHT));
		priceFlagMap.put(CatalogTagListener.PRICEFLAG_INCLASSURANCE, new SimpleTypedTagWriter(this,
				BMECatConstants.XML.TAG.PRICE_FLAG, CatalogTagListener.PRICEFLAG_INCLASSURANCE));
		priceFlagMap.put(CatalogTagListener.PRICEFLAG_INCLDUTY, new SimpleTypedTagWriter(this, BMECatConstants.XML.TAG.PRICE_FLAG,
				CatalogTagListener.PRICEFLAG_INCLDUTY));
		priceFlagMap.put(CatalogTagListener.PRICEFLAG_INCLPACKING, new SimpleTypedTagWriter(this,
				BMECatConstants.XML.TAG.PRICE_FLAG, CatalogTagListener.PRICEFLAG_INCLPACKING));

		addSubTagWriter(BMECatConstants.XML.TAG.PRICE_FLAG, priceFlagMap);

		addSubTagWriter(new MimeInfoTagWriter(this));
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getTagName()
	 */
	@Override
	protected String getTagName()
	{
		return BMECatConstants.XML.TAG.CATALOG;
	}

	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		final BMECatExportContext exportContainer = (BMECatExportContext) object;
		final JaloSession session = JaloSession.getCurrentSession();
		final CatalogVersion catVersion = exportContainer.getCatVersion();
		final Catalog catalog = catVersion.getCatalog();

		getSubTagWriter(BMECatConstants.XML.TAG.LANGUAGE).write(xmlOut, session.getSessionContext().getLanguage().getIsoCode());
		getSubTagWriter(BMECatConstants.XML.TAG.CATALOG_ID).write(xmlOut, catalog.getId());
		getSubTagWriter(BMECatConstants.XML.TAG.CATALOG_VERSION).write(xmlOut, catVersion.getVersion());
		getSubTagWriter(BMECatConstants.XML.TAG.CATALOG_NAME).write(xmlOut, catalog.getName());
		getSubTagWriter(BMECatConstants.XML.TAG.DATETIME, "generation_date").write(xmlOut, catVersion.getGenerationDate());

		final Collection territories = catVersion.getTerritories();
		if (territories != null)
		{
			for (final Iterator it = territories.iterator(); it.hasNext();)
			{
				final Country country = (Country) it.next();
				getSubTagWriter(BMECatConstants.XML.TAG.TERRITORY).write(xmlOut, country.getIsoCode());
			}
		}

		final Currency curr = catVersion.getDefaultCurrency();
		if (curr != null)
		{
			getSubTagWriter(BMECatConstants.XML.TAG.CURRENCY).write(xmlOut, curr.getIsoCode());
		}
		getSubTagWriter(BMECatConstants.XML.TAG.MIME_ROOT).write(xmlOut, catVersion.getMimeRootDirectory());

		getSubTagWriter(BMECatConstants.XML.TAG.PRICE_FLAG, CatalogTagListener.PRICEFLAG_INCLFREIGHT).write(xmlOut,
				catVersion.isInclFreight());
		getSubTagWriter(BMECatConstants.XML.TAG.PRICE_FLAG, CatalogTagListener.PRICEFLAG_INCLASSURANCE).write(xmlOut,
				catVersion.isInclAssurance());
		getSubTagWriter(BMECatConstants.XML.TAG.PRICE_FLAG, CatalogTagListener.PRICEFLAG_INCLDUTY).write(xmlOut,
				catVersion.isInclDuty());
		getSubTagWriter(BMECatConstants.XML.TAG.PRICE_FLAG, CatalogTagListener.PRICEFLAG_INCLPACKING).write(xmlOut,
				catVersion.isInclPacking());
	}

}
