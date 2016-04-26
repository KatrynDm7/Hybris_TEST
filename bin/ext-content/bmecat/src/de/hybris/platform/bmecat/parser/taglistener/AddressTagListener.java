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

import java.util.Arrays;
import java.util.Collection;


/**
 * Parses the &lt;Address&gt; tag
 * 
 * 
 * 
 */
public class AddressTagListener extends DefaultBMECatTagListener
{
	public AddressTagListener(final DefaultBMECatTagListener parent)
	{
		super(parent);
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new StringValueTagListener(this, BMECatConstants.XML.TAG.NAME),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.NAME2),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.NAME3),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.CONTACT),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.STREET),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.ZIP),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.BOXNO),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.CITY),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.COUNTRY),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.ZIPBOX),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.STATE),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.PHONE),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.FAX),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.EMAIL),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.PUBLIC_KEY),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.URL),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.ADDRESS_REMARKS) });
	}

	@Override
	public Object processEndElement(final BMECatObjectProcessor processor)
	{
		final Address address = new Address();
		address.setName((String) getSubTagValue(BMECatConstants.XML.TAG.NAME));
		address.setName2((String) getSubTagValue(BMECatConstants.XML.TAG.NAME2));
		address.setName3((String) getSubTagValue(BMECatConstants.XML.TAG.NAME3));
		address.setContact((String) getSubTagValue(BMECatConstants.XML.TAG.CONTACT));
		address.setStreet((String) getSubTagValue(BMECatConstants.XML.TAG.STREET));
		address.setZip((String) getSubTagValue(BMECatConstants.XML.TAG.ZIP));
		address.setZipbox((String) getSubTagValue(BMECatConstants.XML.TAG.ZIPBOX));
		address.setBoxno((String) getSubTagValue(BMECatConstants.XML.TAG.BOXNO));
		address.setCity((String) getSubTagValue(BMECatConstants.XML.TAG.CITY));
		address.setCountry((String) getSubTagValue(BMECatConstants.XML.TAG.COUNTRY));
		address.setState((String) getSubTagValue(BMECatConstants.XML.TAG.STATE));
		address.setPhone((String) getSubTagValue(BMECatConstants.XML.TAG.PHONE));
		address.setFax((String) getSubTagValue(BMECatConstants.XML.TAG.FAX));
		address.setEmail((String) getSubTagValue(BMECatConstants.XML.TAG.EMAIL));
		address.setPublicKey((String) getSubTagValue(BMECatConstants.XML.TAG.PUBLIC_KEY));
		address.setUrl((String) getSubTagValue(BMECatConstants.XML.TAG.URL));
		address.setRemarks((String) getSubTagValue(BMECatConstants.XML.TAG.ADDRESS_REMARKS));
		return address;
	}

	public String getTagName()
	{
		return BMECatConstants.XML.TAG.ADDRESS;
	}
}
