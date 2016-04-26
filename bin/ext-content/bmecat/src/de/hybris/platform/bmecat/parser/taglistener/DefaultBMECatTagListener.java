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
package de.hybris.platform.bmecat.parser.taglistener;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.TagListener;
import de.hybris.platform.bmecat.parser.BMECatObjectProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;


/**
 * Parses the &lt;DefaultBMECat&gt; tag
 * 
 * 
 * 
 */
public abstract class DefaultBMECatTagListener implements TagListener
{
	private static final Logger LOG = Logger.getLogger(DefaultBMECatTagListener.class.getName()); //NOPMD

	private Map sublisteners = null;

	private StringBuilder charactersVariable = null;
	private Object result = null;
	private DefaultBMECatTagListener parent;
	private Map subtagvalues = null;
	private Map attributesMap = null;

	private int endLineNumber = -1;
	private int startLineNumber = -1;

	/**
	 * Dummy constructor; use this one if you need the method <code>getTagName()</name> only!
	 */
	public DefaultBMECatTagListener()
	{
		parent = null;
	}

	public DefaultBMECatTagListener(final TagListener parent)
	{
		this((DefaultBMECatTagListener) parent);
	}

	public DefaultBMECatTagListener(final DefaultBMECatTagListener parent)
	{
		this.parent = parent;
	}

	protected Collection getParents()
	{
		final Set parents = new HashSet();
		DefaultBMECatTagListener parent = getParent();
		while (parent != null)
		{
			parents.add(parent.getTagName());
			parent = parent.getParent();
		}
		return parents;
	}

	/**
	 * @see de.hybris.platform.bmecat.parser.taglistener.TagListener#getResult()
	 */
	public Object getResult()
	{
		return result;
	}

	public void addResult(final Object res)
	{
		result = res;
	}

	/**
	 * @see de.hybris.platform.bmecat.parser.taglistener.TagListener#getListenersMap()
	 */
	public synchronized Map getListenersMap()
	{
		return sublisteners != null ? sublisteners : Collections.EMPTY_MAP;
	}

	/**
	 * @see de.hybris.platform.bmecat.parser.taglistener.TagListener#hasSubTags()
	 */
	public boolean hasSubTags()
	{
		return !getListenersMap().isEmpty();
	}

	/**
	 * @see de.hybris.platform.bmecat.parser.taglistener.TagListener#hasAttributes()
	 */
	public boolean hasAttributes()
	{
		return attributesMap != null && !attributesMap.isEmpty();
	}

	/**
	 * @param attributes
	 */
	private void readAttributes(final Attributes attributes)
	{
		final Map tmp = new HashMap();
		for (int i = 0; i < attributes.getLength(); i++)
		{
			tmp.put(attributes.getQName(i), attributes.getValue(i));
		}
		if (!tmp.isEmpty())
		{
			attributesMap = Collections.unmodifiableMap(tmp);
		}
	}

	/**
	 * @see de.hybris.platform.bmecat.parser.taglistener.TagListener#getAttribute(java.lang.String)
	 */
	public String getAttribute(final String qname)
	{
		return attributesMap != null ? (String) attributesMap.get(qname) : null;
	}

	protected DefaultBMECatTagListener getParent()
	{
		return parent;
	}

	public void setParent(final DefaultBMECatTagListener parent)
	{
		this.parent = parent;
	}

	public final void registerSubTagListener(final TagListener tagListener)
	{
		if (sublisteners == null)
		{
			sublisteners = new HashMap();
		}
		sublisteners.put(tagListener.getTagName(), tagListener);
	}

	protected void deregisterSubTagListeners()
	{
		sublisteners = null;
	}

	public final void startElement(final ObjectProcessor processor, final Attributes attributes) throws ParseAbortException
	{
		final BMECatObjectProcessor bmecatProcessor = (BMECatObjectProcessor) processor;
		startElement(bmecatProcessor, attributes);
	}

	public final void startElement(final BMECatObjectProcessor processor, final Attributes attributes) throws ParseAbortException
	{
		readAttributes(attributes);
		final Object value = processStartElement(processor);
		if (value != null)
		{
			if (getParent() != null)
			{
				getParent().addSubTagValue(this.getTagName(), value);
			}
			else
			{
				addResult(value);
			}
		}
		for (final Iterator it = createSubTagListeners().iterator(); it.hasNext();)
		{
			final TagListener tagListener = (TagListener) it.next();
			registerSubTagListener(tagListener);
		}
	}

	public final void endElement(final ObjectProcessor processor) throws ParseAbortException
	{
		final BMECatObjectProcessor bmecatProcessor = (BMECatObjectProcessor) processor;
		endElement(bmecatProcessor);
	}

	public final void endElement(final BMECatObjectProcessor processor) throws ParseAbortException
	{
		final Object value = processEndElement(processor);
		if (value != null)
		{
			if (getParent() != null)
			{
				getParent().addSubTagValue(this.getTagName(), value);
			}
			else
			{
				addResult(value);
			}
		}
		deregisterSubTagListeners();
		clear();
	}

	protected Object processStartElement(final BMECatObjectProcessor processor) throws ParseAbortException
	{
		return null;
	}

	protected abstract Object processEndElement(BMECatObjectProcessor processor) throws ParseAbortException;

	private void clear()
	{
		subtagvalues = null;
		attributesMap = null;
		charactersVariable = null;
		startLineNumber = -1;
		endLineNumber = -1;
	}

	/**
	 * @param subTagListenerName
	 */
	protected Object getSubTagValue(final String subTagListenerName)
	{
		return subtagvalues != null ? subtagvalues.get(subTagListenerName) : null;
	}

	protected Collection getSubTagValueCollection(final String subTagListenerName)
	{
		final Object val = getSubTagValue(subTagListenerName);
		return val != null ? (val instanceof Collection ? (Collection) val : Collections.singleton(val)) : Collections.EMPTY_LIST;
	}

	protected void addSubTagValue(final String subTagListenerName, final Object newValue)
	{
		if (subtagvalues == null)
		{
			subtagvalues = new HashMap();
		}
		final Object present = subtagvalues.get(subTagListenerName);
		if (present == null)
		{
			subtagvalues.put(subTagListenerName, newValue);
		}
		else
		{
			if (present instanceof Collection)
			{
				((Collection) present).add(newValue);
			}
			else if (present instanceof Map)
			{
				((Map) present).putAll((Map) newValue);
			}
			else
			{
				final ArrayList tmp = new ArrayList(Collections.singletonList(present));
				tmp.add(newValue);
				subtagvalues.put(subTagListenerName, tmp);
			}
		}
	}

	public TagListener getSubTagListener(final String tagname)
	{
		return (TagListener) getListenersMap().get(tagname);
	}

	/**
	 * @see de.hybris.platform.bmecat.parser.taglistener.TagListener#characters(char[], int, int)
	 */
	public final void characters(final char[] characterArray, final int offset, final int length)
	{
		if (charactersVariable == null)
		{
			charactersVariable = new StringBuilder();
		}
		charactersVariable.append(characterArray, offset, length);
	}

	public Map getAttributesMap()
	{
		return attributesMap != null ? attributesMap : Collections.EMPTY_MAP;
	}

	public String getCharacters()
	{
		final String trimmed = charactersVariable != null ? charactersVariable.toString().trim() : null;
		return trimmed != null && !"".equals(trimmed) ? trimmed : null;
	}

	protected Collection createSubTagListeners()
	{
		return Collections.EMPTY_LIST;
	}

	/**
	 * @see de.hybris.platform.bmecat.parser.taglistener.TagListener#setEndLineNumber(int)
	 */
	public void setEndLineNumber(final int endLineNumber)
	{
		this.endLineNumber = endLineNumber;
	}

	/**
	 * @see de.hybris.platform.bmecat.parser.taglistener.TagListener#setStartLineNumber(int)
	 */
	public void setStartLineNumber(final int startLineNumber)
	{
		this.startLineNumber = startLineNumber;
	}

	/**
	 * @return Returns the endLineNumber.
	 */
	public int getEndLineNumber()
	{
		return endLineNumber;
	}

	/**
	 * @return Returns the startLineNumber.
	 */
	public int getStartLineNumber()
	{
		return startLineNumber;
	}
}
