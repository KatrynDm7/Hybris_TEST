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
package de.hybris.platform.bmecat.parser.taglistener;

import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.TagListener;
import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.bmecat.parser.BMECatObjectProcessor;
import de.hybris.platform.bmecat.parser.Catalog;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;


/**
 * Parses the &lt;Catalog&gt; tag
 * 
 * 
 * 
 */
public class CatalogTagListener extends DefaultBMECatTagListener
{
	public static final String PRICEFLAG_INCLASSURANCE = "incl_assurance";
	public static final String PRICEFLAG_INCLDUTY = "incl_duty";
	public static final String PRICEFLAG_INCLFREIGHT = "incl_freight";
	public static final String PRICEFLAG_INCLPACKING = "incl_packing";

	public CatalogTagListener(final TagListener parent)
	{
		super(parent);
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new StringValueTagListener(this, BMECatConstants.XML.TAG.LANGUAGE),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.CATALOG_ID),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.CATALOG_VERSION),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.CATALOG_NAME), new DateTimeValueTagListener(this),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.CURRENCY),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.MIME_ROOT),
				new BooleanValueTagListener(this, BMECatConstants.XML.TAG.PRICE_FLAG, true),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.TERRITORY), new AbortTagListener(this) });
	}

	@Override
	public Object processEndElement(final BMECatObjectProcessor processor) throws ParseAbortException
	{
		final Catalog catalog = new Catalog();
		catalog.setID((String) getSubTagValue(BMECatConstants.XML.TAG.CATALOG_ID));
		catalog.setVersion((String) getSubTagValue(BMECatConstants.XML.TAG.CATALOG_VERSION));
		catalog.setLanguage((String) getSubTagValue(BMECatConstants.XML.TAG.LANGUAGE));
		catalog.setName((String) getSubTagValue(BMECatConstants.XML.TAG.CATALOG_NAME));
		catalog.setGenerationDate((Date) getSubTagValue(BMECatConstants.XML.TAG.DATETIME));
		catalog.setDefaultCurrency((String) getSubTagValue(BMECatConstants.XML.TAG.CURRENCY));
		catalog.setMimeRootDirectory((String) getSubTagValue(BMECatConstants.XML.TAG.MIME_ROOT));
		catalog.setTerritories(getSubTagValueCollection(BMECatConstants.XML.TAG.TERRITORY));

		final Map priceFlags = (Map) getSubTagValue(BMECatConstants.XML.TAG.PRICE_FLAG);
		if (priceFlags != null)
		{
			catalog.setInclAssurance((Boolean) priceFlags.get(PRICEFLAG_INCLASSURANCE));
			catalog.setInclDuty((Boolean) priceFlags.get(PRICEFLAG_INCLDUTY));
			catalog.setInclFreight((Boolean) priceFlags.get(PRICEFLAG_INCLFREIGHT));
			catalog.setInclPacking((Boolean) priceFlags.get(PRICEFLAG_INCLPACKING));
		}
		return catalog;
	}

	public String getTagName()
	{
		return BMECatConstants.XML.TAG.CATALOG;
	}
}
