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
package de.hybris.platform.commerceservices.address;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum for errors associated with a given address field.
 */
public enum AddressErrorCode
{
	MISSING("missing"),
	INVALID("invalid"),
	TRUNCATED("truncated"),
	CORRECTED("corrected"),
	UNKNOWN("unknown");

	private String errorString;
	private static final Map<String, AddressErrorCode> LOOKUPMAP = new HashMap<String, AddressErrorCode>();

	static
	{
		for (final AddressErrorCode err : AddressErrorCode.values())
		{
			LOOKUPMAP.put(err.getErrorString(), err);
		}
	}

	private AddressErrorCode(final String errorString)
	{
		this.errorString = errorString;
	}

	public String getErrorString()
	{
		return errorString;
	}

	public static AddressErrorCode lookup(final String typeKey)
	{
		AddressErrorCode errorCode = LOOKUPMAP.get(typeKey);
		if (errorCode == null)
		{
			errorCode = UNKNOWN;
		}
		return errorCode;
	}
}
