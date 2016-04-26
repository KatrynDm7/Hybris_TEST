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
package de.hybris.platform.bmecat.xmlwriter;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;


/**
 * Writer wrapper implementation which allows to write a certain amount of (string) data <b> and </b> removing it
 * afterwards from the output.
 * <p>
 * 
 * <pre>
 * UndoableWriterWrapper wr = new UndoableWriterWrapper(actualWriter);
 * 
 * // start undoable writing
 * wr.markSavePoint();
 * try
 * {
 * 	// ... write into wrapper ...
 * 
 * 	// finally store changes
 * 	wr.commitSavePoint();
 * }
 * catch (Exception e)
 * {
 * 	// discard any changes
 * 	wr.restoreSavePoint();
 * }
 * </pre>
 */
public class UndoableWriterWrapper extends Writer
{
	private final Writer wrapped;
	private Writer tempWriter;
	private boolean flushCalled = false;
	private boolean closeCalled = false;

	public UndoableWriterWrapper(final Writer writer)
	{
		super();
		wrapped = writer;
	}

	private Writer getWriter()
	{
		return tempWriter != null ? tempWriter : wrapped;
	}

	@Override
	public void close() throws IOException
	{
		closeCalled = true;
		getWriter().close();
	}

	@Override
	public void flush() throws IOException
	{
		flushCalled = true;
		getWriter().flush();
	}

	@Override
	public void write(final char[] cbuf, final int off, final int len) throws IOException
	{
		getWriter().write(cbuf, off, len);
	}

	public void markSavePoint()
	{
		if (tempWriter != null)
		{
			throw new IllegalStateException("already got a temp writer");
		}

		tempWriter = new StringWriter();
		flushCalled = false;
		closeCalled = false;
	}

	public void restoreSavePoint()
	{
		if (tempWriter == null)
		{
			throw new IllegalStateException("got no temp writer");
		}
		tempWriter = null;
		flushCalled = false;
		closeCalled = false;
	}

	public void commitSavePoint()
	{
		if (tempWriter == null)
		{
			throw new IllegalStateException("got no temp writer");
		}
		try
		{
			tempWriter.flush();
			final StringBuffer stringBuffer = ((StringWriter) tempWriter).getBuffer();
			final char[] data = new char[stringBuffer.length()];
			stringBuffer.getChars(0, stringBuffer.length(), data, 0);
			wrapped.write(data, 0, data.length);
			if (flushCalled)
			{
				wrapped.flush();
			}
			if (closeCalled)
			{
				wrapped.close();
			}
		}
		catch (final IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			tempWriter = null;
			flushCalled = false;
			closeCalled = false;
		}
	}
}
