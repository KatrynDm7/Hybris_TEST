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
 */
package de.hybris.platform.xyformsfacades.strategy.preprocessor;

/**
 * Exception that is thrown during the execution of a {@link YFormPreprocessorStrategy}
 */
public class YFormProcessorException extends Exception
{
	public YFormProcessorException()
	{
		super();
	}

	public YFormProcessorException(final String msg)
	{
		super(msg);
	}

	public YFormProcessorException(final Throwable t)
	{
		super(t);
	}

	public YFormProcessorException(final String msg, final Throwable t)
	{
		super(msg, t);
	}
}
