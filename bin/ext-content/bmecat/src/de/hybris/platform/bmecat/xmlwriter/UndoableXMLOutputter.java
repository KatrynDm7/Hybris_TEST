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

import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.znerd.xmlenc.XMLEventListenerState;
import org.znerd.xmlenc.XMLOutputter;


/**
 * XML outputter wrapper implementation which allows to write a certain amount of data <b> and </b> removing it
 * afterwards from the output. This class uses a {@link de.hybris.platform.bmecat.xmlwriter.UndoableWriterWrapper}
 * instance.
 * <p>
 * 
 * <pre>
 * UndoableXMLOutputter xout = new UndoableXMLOutputter(actualWriter);
 * 
 * // start undoable writing
 * xout.markSavePoint();
 * try
 * {
 * 	// ... write xml ...
 * 
 * 	// finally store changes
 * 	xout.commitSavePoint();
 * }
 * catch (Exception e)
 * {
 * 	// discard any changes and restore outputter state
 * 	xout.restoreSavePoint();
 * }
 * </pre>
 * 
 * 
 */
public class UndoableXMLOutputter extends XMLOutputter
{

	private String[] elementStack;
	private XMLEventListenerState state;

	UndoableXMLOutputter(final Writer writer, final String encoding) throws UnsupportedEncodingException
	{
		super(new UndoableWriterWrapper(writer), encoding);
	}

	/**
	 * Starts undoable xml writing. Be sure to call either {@link #commitSavePoint()} or {@link #restoreSavePoint()} at
	 * the end of undoable writing period.
	 * 
	 * @throws IllegalStateException
	 *            if called twice before either {@link #commitSavePoint()} or {@link #restoreSavePoint()} have been
	 *            called
	 */
	public void markSavePoint()
	{
		if (hasSavePoint())
		{
			throw new IllegalStateException("xml outputter already holds a savepoint - use commit or rollback before calling again");
		}

		// save current state
		state = getState();
		// save element stack (copy raw data)
		final String[] tmp = getElementStack();
		elementStack = new String[tmp.length];
		System.arraycopy(tmp, 0, elementStack, 0, tmp.length);
		// mark on stream
		((UndoableWriterWrapper) getWriter()).markSavePoint();
	}

	/**
	 * Tells wether or not undoable writing has been started or not.
	 */
	public boolean hasSavePoint()
	{
		return state != null && elementStack != null;
	}

	/**
	 * Discards all xml data which has been written since {@link #markSavePoint()} has been called. The prevous outputter
	 * state is restored (opened tag, indentation level, ... ).
	 * <p>
	 * From now on undoable writing is disabled until {@link #markSavePoint()} is called again.
	 */
	public void restoreSavePoint()
	{
		if (!hasSavePoint())
		{
			throw new IllegalStateException("xml outputter has no savepoint");
		}

		// throw away changes on stream
		((UndoableWriterWrapper) getWriter()).restoreSavePoint();
		// reset state
		setState(state, elementStack);
		this.state = null;
		this.elementStack = null;
	}

	/**
	 * Stores all xml data which has been written since {@link #markSavePoint()} has been called to the underlying
	 * writer.
	 * <p>
	 * From now on undoable writing is disabled until {@link #markSavePoint()} is called again.
	 */
	public void commitSavePoint()
	{
		if (!hasSavePoint())
		{
			throw new IllegalStateException("xml outputter has no savepoint");
		}

		((UndoableWriterWrapper) getWriter()).commitSavePoint();
		this.state = null;
		this.elementStack = null;
	}
}
