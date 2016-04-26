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
package de.hybris.platform.sap.core.bol.businessobject;

import de.hybris.platform.sap.core.common.exceptions.CoreBaseException;
import de.hybris.platform.sap.core.common.message.Message;
import de.hybris.platform.sap.core.common.message.MessageList;


/**
 * The <code>BusinessObjectException</code> is thrown if something goes wrong in the business object layer.
 */
public class BusinessObjectException extends CoreBaseException
{

	private static final long serialVersionUID = -3262069587327294963L;

	/**
	 * Standard constructor for BusinessObjectException using a simple message text. <br>
	 * 
	 * @param msg
	 *           message text.
	 * @param rootCause
	 *           exception which causes the exception
	 */
	public BusinessObjectException(final String msg, final Throwable rootCause)
	{
		super(msg, rootCause);
	}


	/**
	 * Constructor.
	 * 
	 * @param msg
	 *           Message for the Exception
	 * @param msgList
	 *           List of the messages added to the exception
	 */
	public BusinessObjectException(final String msg, final MessageList msgList)
	{
		super(msg);
		this.messageList = msgList;
	}


	/**
	 * Constructor.
	 * 
	 * @param msg
	 *           Message for the Exception
	 * @param message
	 *           message added to the exception
	 */
	public BusinessObjectException(final String msg, final Message message)
	{
		super(msg);
		addMessage(message);
	}


	/**
	 * Constructor.
	 * 
	 * @param msg
	 *           Message for the Exception
	 */
	public BusinessObjectException(final String msg)
	{
		super(msg);
	}

	/**
	 * Constructor.
	 */
	public BusinessObjectException()
	{
		super();
	}


}
