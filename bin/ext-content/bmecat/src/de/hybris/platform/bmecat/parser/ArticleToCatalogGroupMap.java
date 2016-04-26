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
 * Object which holds the value of a parsed &lt;ARTICLETOCATALOGGROUPMAP&gt; tag
 * 
 * 
 */
public class ArticleToCatalogGroupMap extends AbstractValueObject
{
	private String articleID;
	private String catalogGroupID;
	private Integer order;
	private int mode;

	/**
	 * BMECat: ARTICLE_TO_CATALOGGROUP_MAP.ART_ID
	 * 
	 * @return Returns the articleID.
	 */
	public String getArticleID()
	{
		return articleID;
	}

	/**
	 * The articleID to set.
	 * 
	 * @param articleID
	 */
	public void setArticleID(final String articleID)
	{
		this.articleID = articleID;
	}

	/**
	 * BMECat: ARTICLE_TO_CATALOGGROUP_MAP.CATALOG_GROUP_ID
	 * 
	 * @return Returns the catalogGroupID.
	 */
	public String getCatalogGroupID()
	{
		return catalogGroupID;
	}

	/**
	 * The catalogGroupID to set.
	 * 
	 * @param catalogGroupID
	 */
	public void setCatalogGroupID(final String catalogGroupID)
	{
		this.catalogGroupID = catalogGroupID;
	}

	/**
	 * BMECat: ARTICLE_TO_CATALOGGROUP_MAP.ARTICLE_TO_CATALOGGROUP_MAP_ORDER
	 * 
	 * @return Returns the order.
	 */
	public Integer getOrder()
	{
		return order;
	}

	/**
	 * The order to set.
	 * 
	 * @param order
	 */
	public void setOrder(final Integer order)
	{
		this.order = order;
	}

	/**
	 * 
	 * 
	 * @return Returns the mode.
	 */
	public int getMode()
	{
		return mode;
	}

	/**
	 * @param mode
	 *           The mode to set.
	 */
	public void setMode(final int mode)
	{
		this.mode = mode;
	}
}
