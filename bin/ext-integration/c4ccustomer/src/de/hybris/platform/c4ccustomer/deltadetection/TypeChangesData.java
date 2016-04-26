/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.c4ccustomer.deltadetection;

import de.hybris.y2ysync.deltadetection.collector.BatchingCollector;

import javax.annotation.concurrent.Immutable;

/**
 * Helper structure to group item changes by type.
 */
@Immutable
public class TypeChangesData
{
	private final String impexHeader;
	private final String dataHubColumns;
	private final BatchingCollector collector;

	/**
	 * Full constructor.
	 *
	 * @param impexColumns semicolon-delimited source columns of impex
	 * @param dataHubColumns semicolon-delimited target columns of datahub processor
	 * @param collector collector user to store records
	 */
	public TypeChangesData(final String impexColumns, final String dataHubColumns, final BatchingCollector collector)
	{
		impexHeader = impexColumns;
		this.dataHubColumns = dataHubColumns;
		this.collector = collector;
	}

	/**
	 * @return impex header.
	 */
	public String getImpexHeader()
	{
		return impexHeader;
	}

	/**
	 * @return datahub column names.
	 */
	public String getDataHubColumns()
	{
		return dataHubColumns;
	}

	/**
	 * @return collector storing data.
	 */
	public BatchingCollector getCollector()
	{
		return collector;
	}
}
