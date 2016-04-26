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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Describes error that has happened during the import process.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportError
{
	/**
	 * A regular expression pattern for extracting an item ID from the impex source line. Item ID is a number between two
	 * ';' characters at the beginning of the impex line.
	 */
	private static final Pattern idPattern = Pattern.compile("\\s*;\\s*(\\d+)\\s*;.*");
	/**
	 * A regular expression pattern for extracting item type from an impex header line. The item type follows the impex
	 * command and is separated from it by at least one space. ';' separates the type from the rest of the header line,
	 * e.g. Product is captured from 'INSERT_UPDATE Product;...'
	 */
	private static final Pattern typePattern = Pattern.compile("(?:INSERT|INSERT_UPDATE|UPDATE|REMOVE)\\s+(\\w+);.*");
	@XmlElement(name = "canonicalItemId")
	private final Long canonicalItemId;
	private final String itemType;
	private final String scriptLine;
	private final String message;
	private final ErrorCode code;

	public ImportError()
	{
		this(null, null, null, null);
	}

	private ImportError(final String type, final String line, final String msg)
	{
		this(null, type, line, msg);
	}

	private ImportError(final Long id, final String type, final String line, final String msg)
	{
		canonicalItemId = id;
		itemType = type;
		scriptLine = line;
		message = msg;
		code = ErrorCode.classify(msg);
	}

	/**
	 * Creates an error.
	 *
	 * @param line rejected impex script line
	 * @param msg an error message explaining the problem
	 * @return new instance of the error
	 */
	public static ImportError create(final String line, final String msg)
	{
		if (msg != null)
		{
			final String nullSafeLn = line != null ? line : "";

			final Long dataHubId = extractItemIdFromSourceLine(nullSafeLn);
			return dataHubId != null ? new ImportError(dataHubId, null, nullSafeLn, msg) : new ImportError(
					itemTypeFromHeader(nullSafeLn), nullSafeLn, msg);
		}
		return null;
	}

	private static String itemTypeFromHeader(final String line)
	{
		final Matcher matcher = typePattern.matcher(line);
		return matcher.matches() ? matcher.group(1) : null;
	}

	private static Long extractItemIdFromSourceLine(final String line)
	{
		final Matcher matcher = idPattern.matcher(line);
		return matcher.matches() ? Long.valueOf(matcher.group(1)) : null;
	}

	/**
	 * Retrieves ID of the item that failed to import.
	 *
	 * @return item ID or <code>null</code>, if the error code indicates an impex header error.
	 */
	public Long getCanonicalItemId()
	{
		return canonicalItemId;
	}

	/**
	 * Determines type of the item that failed to import.
	 *
	 * @return item type
	 */
	public String getItemType()
	{
		return itemType;
	}

	/**
	 * Retrieves the line of the impex script, which resulted in this error
	 *
	 * @return line of the impex script that was rejected by the import.
	 */
	public String getScriptLine()
	{
		return scriptLine;
	}

	/**
	 * Reads the message describing the problem.
	 *
	 * @return an error message.
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * Retrieves code of the error, which uniquely defines what problem has happened.
	 *
	 * @return the error code.
	 */
	public ErrorCode getCode()
	{
		return code;
	}

	/**
	 * A shortcut method for retrieving error type. The error type determines how how many items affected by the error.
	 *
	 * @return type of this error.
	 */
	public ErrorType getType()
	{
		return code.getType();
	}
}
