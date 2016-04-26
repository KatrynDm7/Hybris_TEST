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
 */

package com.hybris.datahub.core.facades;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.groovy.runtime.StringBufferWriter;


/**
 * Encapsulates result of importing the data.
 */
@XmlRootElement(name = "publicationResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemImportResult
{
	@XmlElement(name = "crashReport")
	private final String importExceptionMessage;
	@XmlElement(name = "exportErrorDatas")
	private final List<ImportError> exportErrorDatas;

	/**
	 * Instantiates successful import result.
	 */
	public ItemImportResult()
	{
		this(null);
	}

	/**
	 * Instantiates error result for a crashed import process.
	 *
	 * @param e an exception that was intercepted from the import process.
	 */
	public ItemImportResult(final Exception e)
	{
		exportErrorDatas = new LinkedList<>();
		importExceptionMessage = extractMessage(e);
	}

	private static String extractMessage(final Exception e)
	{
		if (e != null)
		{
			return e.getMessage() == null ? e.getClass().getCanonicalName() : e.getMessage();
		}
		return null;
	}

	/**
	 * Determines whether the item import ran successfully or not.
	 *
	 * @return <code>true</code>, if import was successful; <code>false</code>, if the import crashed or there are
	 * rejected items.
	 */
	public boolean isSuccessful()
	{
		return exportErrorDatas.isEmpty() && importExceptionMessage == null;
	}

	/**
	 * Adds errors to this result
	 *
	 * @param errors import errors to add to this result
	 * @return this result
	 * @throws IllegalArgumentException if null passed in.
	 */
	public ItemImportResult addErrors(final Collection<ImportError> errors)
	{
		if (errors == null)
		{
			throw new IllegalArgumentException("Not expecting a null collection here");
		}


		for (final ImportError err : errors)
		{
			exportErrorDatas.add(err);
		}
		return this;
	}

	/**
	 * Retrieves all errors reported by the import process.
	 *
	 * @return a collection of errors or an empty collection, if all items were successfully imported.
	 */
	public Collection<ImportError> getErrors()
	{
		return Collections.unmodifiableList(exportErrorDatas);
	}

	/**
	 * Retrieves message of exception that happened during the import process.
	 *
	 * @return an exception or <code>null</code>, if there were no exception.
	 */
	public String getExceptionMessage()
	{
		return importExceptionMessage;
	}

	@Override
	public String toString()
	{
		final StringBuffer buf = new StringBuffer();
		try (final PrintWriter writer = new PrintWriter(new StringBufferWriter(buf)))
		{
			writer.print(isSuccessful() ? "SUCCESS" : "ERROR");
			if (importExceptionMessage != null)
			{
				writer.print("(");
				writer.print(importExceptionMessage);
				writer.print(")");
			}
			return buf.toString();
		}
	}

	/**
	 * Return the status of an {@link com.hybris.datahub.core.facades.ItemImportResult} based on whether the import had
	 * no errors or importExceptionMessage (SUCCESS), had errors but no importException (PARTIAL_SUCCESS), or had
	 * importException (FAILURE)
	 *
	 * @return The status of import
	 */
	public DatahubAdapterEventStatus getStatus()
	{
		if (isSuccessful())
		{
			return DatahubAdapterEventStatus.SUCCESS;
		}
		else if (!exportErrorDatas.isEmpty() && importExceptionMessage == null)
		{
			return DatahubAdapterEventStatus.PARTIAL_SUCCESS;
		}
		else
		{
			return DatahubAdapterEventStatus.FAILURE;
		}
	}

	/**
	 * Possible event status.
	 */
	public enum DatahubAdapterEventStatus
	{
		/**
		 * Indicates that all items were imported successfully.
		 */
		SUCCESS,
		/**
		 * Indicates that no items were imported successfully.
		 */
		FAILURE,
		/**
		 * Indicates that some items were imported successfully and some were rejected.
		 */
		PARTIAL_SUCCESS
	}
}
