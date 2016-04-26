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
package de.hybris.platform.bmecat.parser;

import de.hybris.bootstrap.xml.AbstractValueObject;


/**
 * Object which holds the value of a parsed &lt;C2LVALUEOBJECT&gt; tag
 * 
 * 
 */
public abstract class C2LValueObject extends AbstractValueObject
{
	private String isoCode;

	public C2LValueObject()
	{
		super();
	}

	public C2LValueObject(final String isoCode)
	{
		this();
		setIsoCode(isoCode);
	}

	/**
	 * @return Returns the isoCode.
	 */
	public String getIsoCode()
	{
		return isoCode;
	}

	/**
	 * @param isoCode
	 *           The isoCode to set.
	 */
	public void setIsoCode(final String isoCode)
	{
		this.isoCode = isoCode;
	}

	@Override
	public int hashCode()
	{
		return this.isoCode.hashCode();
	}

	@Override
	public boolean equals(final Object obj)
	{
		return obj instanceof C2LValueObject && this.isoCode.equals(((C2LValueObject) obj).isoCode);
	}
}
