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
 * Parses the &lt;Buyer&gt; tag
 * 
 * 
 * 
 */
public class BuyerTagListener extends DefaultBMECatTagListener
{
	public BuyerTagListener(final TagListener parent)
	{
		super(parent);
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new StringValueTagListener(this, BMECatConstants.XML.TAG.BUYER_ID, true),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.BUYER_NAME), new AddressTagListener(this) });
	}

	@Override
	public Object processEndElement(final BMECatObjectProcessor processor)
	{
		final Company buyer = new Company();
		buyer.setName((String) getSubTagValue(BMECatConstants.XML.TAG.BUYER_NAME));
		buyer.setAddress((Address) getSubTagValue(BMECatConstants.XML.TAG.ADDRESS));
		final Map ids = (Map) getSubTagValue(BMECatConstants.XML.TAG.BUYER_ID);
		if (ids != null)
		{
			buyer.setId((String) ids.get(SimpleValueTagListener.UNSPECIFIED_TYPE));
			buyer.setBuyerSpecificID((String) ids.get(Company.TYPE_BUYER_SPECIFIC));
			buyer.setDunsID((String) ids.get(Company.TYPE_DUNS));
			buyer.setIlnID((String) ids.get(Company.TYPE_ILN));
			buyer.setSupplierSpecificID((String) ids.get(Company.TYPE_SUPPLIER_SPECIFIC));
		}
		return buyer;
	}

	public String getTagName()
	{
		return BMECatConstants.XML.TAG.BUYER;
	}
}
