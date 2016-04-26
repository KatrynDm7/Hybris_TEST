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
 * Object which holds the value of a parsed &lt;ADDRESS&gt; tag
 * 
 * 
 */
public class Address extends AbstractValueObject
{
	private String name;
	private String name2;
	private String name3;
	private String contact;
	private String street;
	private String zip;
	private String boxno;
	private String zipbox;
	private String city;
	private String state;
	private String country;
	private String phone;
	private String fax;
	private String email;
	private String publicKey;
	private String url;
	private String remarks;

	/**
	 * BMECat: ADDRESS.BOXNO
	 * 
	 * @return Returns the boxno.
	 */
	public String getBoxno()
	{
		return boxno;
	}

	/**
	 * @param boxno
	 *           The boxno to set.
	 */
	public void setBoxno(final String boxno)
	{
		this.boxno = boxno;
	}

	/**
	 * BMECat: ADDRESS.CITY
	 * 
	 * @return Returns the city.
	 */
	public String getCity()
	{
		return city;
	}

	/**
	 * @param city
	 *           The city to set.
	 */
	public void setCity(final String city)
	{
		this.city = city;
	}

	/**
	 * BMECat: ADDRESS.CONTACT
	 * 
	 * @return Returns the contact.
	 */
	public String getContact()
	{
		return contact;
	}

	/**
	 * @param contact
	 *           The contact to set.
	 */
	public void setContact(final String contact)
	{
		this.contact = contact;
	}

	/**
	 * BMECat: ADDRESS.COUNTRY
	 * 
	 * @return Returns the country.
	 */
	public String getCountry()
	{
		return country;
	}

	/**
	 * @param country
	 *           The country to set.
	 */
	public void setCountry(final String country)
	{
		this.country = country;
	}

	/**
	 * BMECat: ADDRESS.EMAIL
	 * 
	 * @return Returns the email.
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * @param email
	 *           The email to set.
	 */
	public void setEmail(final String email)
	{
		this.email = email;
	}

	/**
	 * BMECat: ADDRESS.FAX
	 * 
	 * @return Returns the fax.
	 */
	public String getFax()
	{
		return fax;
	}

	/**
	 * @param fax
	 *           The fax to set.
	 */
	public void setFax(final String fax)
	{
		this.fax = fax;
	}

	/**
	 * BMECat: ADDRESS.NAME
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
	 * BMECat: ADDRESS.NAME2
	 * 
	 * @return Returns the name2.
	 */
	public String getName2()
	{
		return name2;
	}

	/**
	 * @param name2
	 *           The name2 to set.
	 */
	public void setName2(final String name2)
	{
		this.name2 = name2;
	}

	/**
	 * BMECat: ADDRESS.NAME3
	 * 
	 * @return Returns the name3.
	 */
	public String getName3()
	{
		return name3;
	}

	/**
	 * @param name3
	 *           The name3 to set.
	 */
	public void setName3(final String name3)
	{
		this.name3 = name3;
	}

	/**
	 * BMECat: ADDRESS.PHONE
	 * 
	 * @return Returns the phone.
	 */
	public String getPhone()
	{
		return phone;
	}

	/**
	 * @param phone
	 *           The phone to set.
	 */
	public void setPhone(final String phone)
	{
		this.phone = phone;
	}

	/**
	 * BMECat: ADDRESS.PUBLIC_KEY
	 * 
	 * @return Returns the publicKey.
	 */
	public String getPublicKey()
	{
		return publicKey;
	}

	/**
	 * @param publicKey
	 *           The publicKey to set.
	 */
	public void setPublicKey(final String publicKey)
	{
		this.publicKey = publicKey;
	}

	/**
	 * BMECat: ADDRESS.ADDRESS_REMARKS
	 * 
	 * @return Returns the remarks.
	 */
	public String getRemarks()
	{
		return remarks;
	}

	/**
	 * @param remarks
	 *           The remarks to set.
	 */
	public void setRemarks(final String remarks)
	{
		this.remarks = remarks;
	}

	/**
	 * BMECat: ADDRESS.STATE
	 * 
	 * @return Returns the state.
	 */
	public String getState()
	{
		return state;
	}

	/**
	 * @param state
	 *           The state to set.
	 */
	public void setState(final String state)
	{
		this.state = state;
	}

	/**
	 * BMECat: ADDRESS.STREET
	 * 
	 * @return Returns the street.
	 */
	public String getStreet()
	{
		return street;
	}

	/**
	 * @param street
	 *           The street to set.
	 */
	public void setStreet(final String street)
	{
		this.street = street;
	}

	/**
	 * BMECat: ADDRESS.URL
	 * 
	 * @return Returns the url.
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * @param url
	 *           The url to set.
	 */
	public void setUrl(final String url)
	{
		this.url = url;
	}

	/**
	 * BMECat: ADDRESS.ZIP
	 * 
	 * @return Returns the zip.
	 */
	public String getZip()
	{
		return zip;
	}

	/**
	 * @param zip
	 *           The zip to set.
	 */
	public void setZip(final String zip)
	{
		this.zip = zip;
	}

	/**
	 * BMECat: ADDRESS.ZIPBOX
	 * 
	 * @return Returns the zipbox.
	 */
	public String getZipbox()
	{
		return zipbox;
	}

	/**
	 * @param zipbox
	 *           The zipbox to set.
	 */
	public void setZipbox(final String zipbox)
	{
		this.zipbox = zipbox;
	}
}
