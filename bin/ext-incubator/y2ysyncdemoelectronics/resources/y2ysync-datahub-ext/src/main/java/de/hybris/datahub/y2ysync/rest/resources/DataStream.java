/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */

package de.hybris.datahub.y2ysync.rest.resources;

import java.util.List;


public class DataStream
{
	private String itemType;
	private String columns;
	private boolean delete;
	private List<String> urls;

	public String getItemType()
	{
		return itemType;
	}

	public void setItemType(final String itemType)
	{
		this.itemType = itemType;
	}

	public String getColumns()
	{
		return columns;
	}

	public void setColumns(final String columns)
	{
		this.columns = columns;
	}

	public List<String> getUrls()
	{
		return urls;
	}

	public void setUrls(final List<String> urls)
	{
		this.urls = urls;
	}

	public boolean isDelete()
	{
		return delete;
	}

	public void setDelete(final boolean delete)
	{
		this.delete = delete;
	}
}
