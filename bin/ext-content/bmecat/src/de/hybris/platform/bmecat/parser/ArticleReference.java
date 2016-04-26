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
 * Object which holds the value of a parsed &lt;ARTICLEREFERENCE&gt; tag
 * 
 * 
 */
public class ArticleReference extends AbstractValueObject
{
	private String articleReferenceVariable;
	private String catalogID;
	private String catalogVersion;
	private String type;
	private Integer quantity;


	/**
	 * BMECat: ARTICLE_REFERENCE.ART_ID_TO
	 * 
	 * @return Returns the articleReference.
	 */
	public String getArticleReference()
	{
		return articleReferenceVariable;
	}

	/**
	 * BMECat: ARTICLE_REFERENCE.ART_ID_TO
	 * 
	 * @param articleReference
	 *           The articleReference to set.
	 */
	public void setArticleReference(final String articleReference)
	{
		this.articleReferenceVariable = articleReference;
	}

	/**
	 * BMECat: ARTICLE_REFERENCE.CATALOG_ID
	 * 
	 * @return Returns the catalogID.
	 */
	public String getCatalogID()
	{
		return catalogID;
	}

	/**
	 * @param catalogID
	 *           The catalogID to set.
	 */
	public void setCatalogID(final String catalogID)
	{
		this.catalogID = catalogID;
	}

	/**
	 * BMECat: ARTICLE_REFERENCE type="..."
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

	/**
	 * BMECat: ARTICLE_REFERENCE.CATALOG_VERSION
	 * 
	 * @return Returns the catalogVersion.
	 */
	public String getCatalogVersion()
	{
		return catalogVersion;
	}

	/**
	 * @param catalogVersion
	 *           The catalogVersion to set.
	 */
	public void setCatalogVersion(final String catalogVersion)
	{
		this.catalogVersion = catalogVersion;
	}

	/**
	 * BMECat: ARTICLE_REFERENCE quantity="..."
	 * 
	 * @return Returns the quantity.
	 */
	public Integer getQuantity()
	{
		return quantity;
	}

	/**
	 * @param quantity
	 *           The quantity to set.
	 */
	public void setQuantity(final Integer quantity)
	{
		this.quantity = quantity;
	}
}
