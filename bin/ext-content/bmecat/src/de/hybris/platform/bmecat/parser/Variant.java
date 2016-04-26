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
 * Object which holds the value of a parsed &lt;VARIANT&gt; tag
 * 
 */
public class Variant extends AbstractValueObject
{

	private String fvalue;
	private String supplierAidSupplement;


	/**
	 * @return Returns the fvalue.
	 */
	public String getFvalue()
	{
		return fvalue;
	}

	/**
	 * @param fvalue
	 *           The fvalue to set.
	 */
	public void setFvalue(final String fvalue)
	{
		this.fvalue = fvalue;
	}

	/**
	 * @return Returns the supplierAidSupplement.
	 */
	public String getSupplierAidSupplement()
	{
		return supplierAidSupplement;
	}

	/**
	 * @param supplierAidSupplement
	 *           The supplierAidSupplement to set.
	 */
	public void setSupplierAidSupplement(final String supplierAidSupplement)
	{
		this.supplierAidSupplement = supplierAidSupplement;
	}
}
