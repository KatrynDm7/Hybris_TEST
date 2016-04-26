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

import de.hybris.bootstrap.xml.TagListener;
import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.bmecat.parser.BMECatObjectProcessor;
import de.hybris.platform.bmecat.parser.Catalog;
import de.hybris.platform.bmecat.parser.Company;

import java.util.Arrays;
import java.util.Collection;


/**
 * Parses the &lt;Header&gt; tag
 * 
 * 
 * 
 */
public class HeaderTagListener extends DefaultBMECatTagListener
{
	public HeaderTagListener(final TagListener parent)
	{
		super(parent);
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new CatalogTagListener(this), new StringValueTagListener(this, BMECatConstants.XML.TAG.GENERATOR_INFO),
				new BuyerTagListener(this), new SupplierTagListener(this), new AgreementTagListener(this) });
	}

	@Override
	public Object processEndElement(final BMECatObjectProcessor processor)
	{
		final Catalog catalog = (Catalog) getSubTagValue(BMECatConstants.XML.TAG.CATALOG);
		catalog.setSupplier((Company) getSubTagValue(BMECatConstants.XML.TAG.SUPPLIER));
		catalog.setBuyer((Company) getSubTagValue(BMECatConstants.XML.TAG.BUYER));
		catalog.setGeneratorInfo((String) getSubTagValue(BMECatConstants.XML.TAG.GENERATOR_INFO));
		catalog.setAgreements(getSubTagValueCollection(BMECatConstants.XML.TAG.AGREEMENT));
		return catalog;
	}

	public String getTagName()
	{
		return BMECatConstants.XML.TAG.HEADER;
	}
}
