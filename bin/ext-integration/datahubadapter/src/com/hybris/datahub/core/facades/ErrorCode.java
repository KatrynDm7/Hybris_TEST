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

import java.util.regex.Pattern;


/**
 * All possible error codes reported from the import service.
 */
public enum ErrorCode
{
	/**
	 * An error when one of the required attribute values is not present in the impex file.
	 */
	MISSING_REQUIRED_ATTRIBUTE(ErrorType.ATTRIBUTE_VALUE, ".*NULL for.*mandatory attribute.*"),
	/**
	 * Indicates that an attribute declared in the impex header does not exist.
	 */
	UNKNOWN_ATTRIBUTE(ErrorType.HEADER, "unknown attribute '[a-zA-Z0-9_$]+' in header.*"),
	/**
	 * The item specified by a data row cannot be found in the system.
	 */
	NOT_EXISTING_ITEM(ErrorType.REFERENCE, "no existing item found for.*"),
	/**
	 * The attribute value referring another item or enum value does not exist.
	 */
	REFERENCE_VIOLATION(ErrorType.REFERENCE, "(?:.*could not resolve item for.*)|(?:.*cannot resolve value '[^']*'.*)"),
	/**
	 * The item being imported violates a primary/unique key on already existing item.
	 */
	UNIQUE_KEY_VIOLATION(ErrorType.ATTRIBUTE_VALUE, "conflict between existing item.*"),
	/**
	 * The compound attribute value does not provide all parts of the value.
	 */
	NOT_ENOUGH_ATTRIBUTE_VALUES(ErrorType.ATTRIBUTE_VALUE, ".*attribute [a-zA-Z0-9_.]+ does not provide enough values.*"),
	/**
	 * The provided value is not matching either the declared data type or the required format for the value.
	 */
	INVALID_DATA_FORMAT(ErrorType.ATTRIBUTE_VALUE, ".*cannot parse \\w+ '[^']*' with format specified pattern.*"),
	/**
	 * Finds more than one items to update matching the data row.
	 */
	MORE_THAN_ONE_ITEM_FOUND(ErrorType.ATTRIBUTE_VALUE, "more than one item found for '[^']+'.*"),
	/**
	 * Item type specified in the impex block header does not exist in the system.
	 */
	UNKNOWN_TYPE(ErrorType.HEADER, "unknown type '\\w+' in header.*"),
	/**
	 * The header record for a data block is not recognized or not present.
	 */
	HEADER_NOT_FOUND(ErrorType.HEADER, "no current header for value line.*"),
	/**
	 * The base product type cannot be resolved for a variant data record.
	 */
	NO_VARIANT_TYPE(ErrorType.ATTRIBUTE_VALUE, ".*got no variant type.*"),
	/**
	 * The item cannot be deleted.
	 */
	CANNOT_REMOVE_ITEM(ErrorType.REFERENCE, ".*could not remove item.*"),
	/**
	 * Indicates a problem that could not be classified. This error code has no scope defined.
	 */
	UNCLASSIFIED("\\s*(?:\\S+\\s*)+"); // cannot be a blank line

	private ErrorType errorType;
	private String matchPattern;

	private ErrorCode(final String pattern)
	{
		this(null, pattern);
	}

	private ErrorCode(final ErrorType type, final String pattern)
	{
		errorType = type;
		matchPattern = pattern;
	}

	/**
	 * Classifies the error based on the error message submitted.
	 *
	 * @param msg an error message to analyze.
	 * @return an error code matching the problem described by the message or <code>UNCLASSIFIED</code> error, if the
	 * error could not be classified.
	 */
	public static ErrorCode classify(final String msg)
	{
		return msg != null ? findMatchingCode(msg) : null;
	}

	private static ErrorCode findMatchingCode(final String msg)
	{
		for (final ErrorCode code : ErrorCode.values())
		{
			if (code.matches(msg))
			{
				return code;
			}
		}
		return null;
	}

	private boolean matches(final String msg)
	{
		return Pattern.matches(matchPattern, msg);
	}

	/**
	 * Retrieves type of this error code.
	 *
	 * @return type for this error code
	 */
	public ErrorType getType()
	{
		return errorType;
	}
}
