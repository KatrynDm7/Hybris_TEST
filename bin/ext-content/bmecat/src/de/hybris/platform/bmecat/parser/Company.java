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

import java.util.Collection;


/**
 * Object which holds the value of a parsed &lt;COMPANY&gt; tag
 * 
 * 
 */
public class Company extends AbstractValueObject
{
	private String id;
	private String name;
	private String buyerSpecificID;
	private String supplierSpecificID;
	private String dunsID;
	private String ilnID;
	private Address address;
	private Collection medias;

	public static final String TYPE_DUNS = "duns";
	public static final String TYPE_ILN = "iln";
	public static final String TYPE_BUYER_SPECIFIC = "buyer_specific";
	public static final String TYPE_SUPPLIER_SPECIFIC = "supplier_specific";

	/**
	 * @return Returns the address.
	 */
	public Address getAddress()
	{
		return address;
	}

	/**
	 * @param address
	 *           The address to set.
	 */
	public void setAddress(final Address address)
	{
		this.address = address;
	}

	/**
	 * BMECat: BUYER.BUYER_ID type="buyer_specific"
	 * 
	 * @return Returns the buyerSpecificID.
	 */
	public String getBuyerSpecificID()
	{
		return buyerSpecificID;
	}

	/**
	 * @param buyerSpecificID
	 *           The buyerSpecificID to set.
	 */
	public void setBuyerSpecificID(final String buyerSpecificID)
	{
		this.buyerSpecificID = buyerSpecificID;
	}

	/**
	 * BMECat: BUYER.BUYER_ID type="duns"
	 * 
	 * @return Returns the dunsID.
	 */
	public String getDunsID()
	{
		return dunsID;
	}

	/**
	 * @param dunsID
	 *           The dunsID to set.
	 */
	public void setDunsID(final String dunsID)
	{
		this.dunsID = dunsID;
	}

	/**
	 * BMECat: BUYER.BUYER_ID type="iln"
	 * 
	 * @return Returns the ilnID.
	 */
	public String getIlnID()
	{
		return ilnID;
	}

	/**
	 * @param ilnID
	 *           The ilnID to set.
	 */
	public void setIlnID(final String ilnID)
	{
		this.ilnID = ilnID;
	}

	/**
	 * BMECat: BUYER.BUYER_ID type="supplier_specific"
	 * 
	 * @return Returns the supplierSpecificID.
	 */
	public String getSupplierSpecificID()
	{
		return supplierSpecificID;
	}

	/**
	 * BMECat: BUYER.BUYER_ID type="supplier_specific"
	 * 
	 * @param supplierSpecificID
	 *           The supplierSpecificID to set.
	 */
	public void setSupplierSpecificID(final String supplierSpecificID)
	{
		this.supplierSpecificID = supplierSpecificID;
	}

	/**
	 * BMECat: BUYER.BUYER_NAME or SUPPLIER.SUPPLIER_NAME
	 * 
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *           The name to set.
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * BMECat: SUPPLIER.MIME_INFO or BUYER.MIME_INFO
	 * 
	 * @return Returns the mimeInfos.
	 */
	public Collection<Mime> getMedias()
	{
		return medias;
	}

	/**
	 * @param medias
	 *           The mimeInfos to set. BMECat: SUPPLIER.MIME_INFO or BUYER.MIME_INFO
	 */
	public void setMimeInfos(final Collection<Mime> medias)
	{
		this.medias = medias;
	}

	/**
	 * BMECat: BUYER.BUYER_ID type="unspecific"
	 * 
	 * @return Returns the id.
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id
	 *           The id to set.
	 */
	public void setId(final String id)
	{
		this.id = id;
	}
}
