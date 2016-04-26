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
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.user.Address;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;Address&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class AddressTagWriter extends XMLTagWriter
{
	private final String addressType;

	/**
	 * @param parent
	 * @param addressType
	 */
	public AddressTagWriter(final XMLTagWriter parent, final String addressType)
	{
		super(parent);
		this.addressType = addressType;
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.NAME));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.CONTACT));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.STREET));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.ZIP));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.CITY));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.COUNTRY));

		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.EMAIL));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.FAX));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.PHONE)); // -> BMECAT.PHONE1
		//addSubTagWriter( new SimpleTagWriter( this, BMECatConstants.XML.TAG.PHONE2 ) ); // -> BMECAT.PHONE2

	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getTagName()
	 */
	@Override
	protected String getTagName()
	{
		return BMECatConstants.XML.TAG.ADDRESS;
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getAttributesMap(java.lang.Object)
	 */
	@Override
	protected Map getAttributesMap(final Object object)
	{
		return Collections.singletonMap("type", addressType);
	}

	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		try
		{
			final Address address = (Address) object;
			getSubTagWriter(BMECatConstants.XML.TAG.NAME).write(xmlOut, address.getAttribute(Address.COMPANY));
			getSubTagWriter(BMECatConstants.XML.TAG.CONTACT).write(xmlOut, address.getAttribute(Address.LASTNAME));

			String street = (String) address.getAttribute(Address.STREETNAME);
			final String streetnumber = (String) address.getAttribute(Address.STREETNUMBER);
			if (streetnumber != null && streetnumber.length() > 0)
			{
				street = street + " " + streetnumber;
			}

			getSubTagWriter(BMECatConstants.XML.TAG.STREET).write(xmlOut, street);
			getSubTagWriter(BMECatConstants.XML.TAG.ZIP).write(xmlOut, address.getAttribute(Address.POSTALCODE));
			getSubTagWriter(BMECatConstants.XML.TAG.CITY).write(xmlOut, address.getAttribute(Address.TOWN));

			getSubTagWriter(BMECatConstants.XML.TAG.EMAIL).write(xmlOut, address.getAttribute(Address.EMAIL));
			getSubTagWriter(BMECatConstants.XML.TAG.FAX).write(xmlOut, address.getAttribute(Address.FAX));
			getSubTagWriter(BMECatConstants.XML.TAG.PHONE).write(xmlOut, address.getAttribute(Address.PHONE1));
			// TODO getSubTagWriter( BMECatConstants.XML.TAG.PHONE ).write( xmlOut, (String)address.getField( Address.PHONE2 ) );

			final Country country = address.getCountry();
			if (country != null)
			{
				getSubTagWriter(BMECatConstants.XML.TAG.COUNTRY).write(xmlOut, country.getIsoCode());
			}
		}
		catch (final JaloSecurityException e)
		{
			throw new JaloSystemException(e);
		}
	}
}
