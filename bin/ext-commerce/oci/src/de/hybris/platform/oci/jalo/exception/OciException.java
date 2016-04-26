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
package de.hybris.platform.oci.jalo.exception;


/**
 * Exception used by oci. It contains an error string and an error code.
 * 
 * 
 * 
 */
public class OciException extends Exception
{
	// error codes

	/**
	 * Error code - unspecified error.
	 */
	public final static int UNSPECIFIED_ERROR = -1;

	/**
	 * Error code - the user is not logged in per OCI.
	 */
	public final static int NOT_OCI_LOGIN = 1;

	/**
	 * Error code - the outbound section is not available (not bound to the jalo session).
	 */
	public final static int NO_OUTBOUND_SECTION = 2;

	/**
	 * Error code - the hook URL field is missing or it's empty.
	 */
	public final static int NO_HOOK_URL = 3;

	/**
	 * Error code - some fields in outbound section are missing.
	 */
	public final static int OUTBOUND_FIELDS_MISSING = 4;

	/**
	 * Error code - required OCI field is missing or has no date
	 */
	public final static int OCI_FIELD_MISSING_OR_NO_DATA = 6;

	/**
	 * Error code - The current jalo Session is not an Oci Session
	 */
	public final static int NO_OCI_SESSION = 7;

	/**
	 * Error code - SRM Server should have a field, but this is missing
	 */
	public final static int SRM_FIELD_MISSING = 8;

	/**
	 * Error code - Login failed, bad password, unknown user, ...
	 * 
	 */
	public final static int LOGIN_FAILED = 9;


	private final int errorCode;

	/**
	 * unspecific error.
	 * 
	 */
	public OciException()
	{
		this("Unspecified error.", UNSPECIFIED_ERROR);
	}

	/**
	 * unspecific error with message.
	 * 
	 * @param errorMessage
	 *           the error message
	 */
	public OciException(final String errorMessage)
	{
		this(errorMessage, UNSPECIFIED_ERROR);
	}

	/**
	 * oci exception with message and error code
	 * 
	 * @param errorMessage
	 *           the error message
	 * @param errorCode
	 *           use for error code one of the constants from this class
	 */
	public OciException(final String errorMessage, final int errorCode)
	{
		super(errorMessage);
		this.errorCode = errorCode;
	}

	/**
	 * Returns the error code.
	 */
	public int getErrorCode()
	{
		return this.errorCode;
	}
}
