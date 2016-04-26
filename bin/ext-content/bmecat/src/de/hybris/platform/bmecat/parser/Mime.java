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
 * Object which holds the value of a parsed &lt;MIME&gt; tag
 * 
 * 
 */
public class Mime extends AbstractValueObject
{
	private String type;
	private String description;
	private String source;
	private String alt;
	private String purpose;
	private Integer order;

	public static final class PURPOSE
	{
		public static final String THUMBNAIL = "thumbnail";
		public static final String NORMAL = "normal";
		public static final String DETAIL = "detail";
		public static final String DATA_SHEET = "data_sheet";
		public static final String LOGO = "logo";
		public static final String OTHERS = "others";
	}

	/**
	 * BMECat: MIME_INFO.MIME.MIME_ALT
	 * 
	 * @return Returns the alt.
	 */
	public String getAlt()
	{
		return alt;
	}

	/**
	 * @param alt
	 *           The alt to set.
	 */
	public void setAlt(final String alt)
	{
		this.alt = alt;
	}

	/**
	 * BMECat: MIME_INFO.MIME.MIME_DESCR
	 * 
	 * @return Returns the description.
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description
	 *           The description to set.
	 */
	public void setDescription(final String description)
	{
		this.description = description;
	}

	/**
	 * BMECat: MIME_INFO.MIME.MIME_ORDER
	 * 
	 * @return Returns the order.
	 */
	public Integer getOrder()
	{
		return order;
	}

	/**
	 * @param order
	 *           The order to set.
	 */
	public void setOrder(final Integer order)
	{
		this.order = order;
	}

	/**
	 * BMECat: MIME_INFO.MIME.MIME_PURPOSE
	 * 
	 * @return Returns the purpose.
	 */
	public String getPurpose()
	{
		return purpose;
	}

	/**
	 * @param purpose
	 *           The purpose to set.
	 */
	public void setPurpose(final String purpose)
	{
		this.purpose = purpose;
	}

	/**
	 * BMECat: MIME_INFO.MIME.MIME_SOURCE
	 * 
	 * @return Returns the source.
	 */
	public String getSource()
	{
		return source;
	}

	/**
	 * @param source
	 *           The source to set.
	 */
	public void setSource(final String source)
	{
		this.source = source;
	}

	/**
	 * BMECat: MIME_INFO.MIME.MIME_TYPE
	 * 
	 * @return Returns the type.
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type
	 *           The type to set.
	 */
	public void setType(final String type)
	{
		this.type = type;
	}
}
