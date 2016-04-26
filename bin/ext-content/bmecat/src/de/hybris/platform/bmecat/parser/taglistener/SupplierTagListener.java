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
import de.hybris.platform.bmecat.parser.Address;
import de.hybris.platform.bmecat.parser.BMECatObjectProcessor;
import de.hybris.platform.bmecat.parser.Company;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;


/**
 * Parses the &lt;Supplier&gt; tag
 * 
 * 
 * 
 */
public class SupplierTagListener extends DefaultBMECatTagListener
{
	public SupplierTagListener(final TagListener parent)
	{
		super(parent);
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new StringValueTagListener(this, BMECatConstants.XML.TAG.SUPPLIER_ID, true),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.SUPPLIER_NAME), new MimeInfoTagListener(this),
				new AddressTagListener(this) });
	}

	@Override
	public Object processEndElement(final BMECatObjectProcessor processor)
	{
		final Company supplier = new Company();
		supplier.setName((String) getSubTagValue(BMECatConstants.XML.TAG.SUPPLIER_NAME));
		supplier.setAddress((Address) getSubTagValue(BMECatConstants.XML.TAG.ADDRESS));
		supplier.setMimeInfos(getSubTagValueCollection(BMECatConstants.XML.TAG.MIME_INFO));
		final Map ids = (Map) getSubTagValue(BMECatConstants.XML.TAG.SUPPLIER_ID);
		if (ids != null)
		{
			supplier.setId((String) ids.get(SimpleValueTagListener.UNSPECIFIED_TYPE));
			supplier.setBuyerSpecificID((String) ids.get(Company.TYPE_BUYER_SPECIFIC));
			supplier.setDunsID((String) ids.get(Company.TYPE_DUNS));
			supplier.setIlnID((String) ids.get(Company.TYPE_ILN));
			supplier.setSupplierSpecificID((String) ids.get(Company.TYPE_SUPPLIER_SPECIFIC));
		}
		return supplier;
	}

	public String getTagName()
	{
		return BMECatConstants.XML.TAG.SUPPLIER;
	}

}
