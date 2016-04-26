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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.znerd.xmlenc.LineBreak;
import org.znerd.xmlenc.XMLOutputter;


/**
 * Super class of all <code>XMLTagWriter</code>.
 * 
 * 
 * @deprecated replaced by {@link de.hybris.bootstrap.xml.XMLTagWriter}
 */
@Deprecated
@SuppressWarnings("deprecation")
public abstract class XMLTagWriter
{
	private final XMLTagWriter parent;
	private final boolean mandatory;
	private final Map subTagWriterMap = new LinkedMap();

	private static final Logger LOG = Logger.getLogger(XMLTagWriter.class.getName());

	/**
	 * Constructs an instance of <code>XMLTagWriter</code>.
	 * 
	 * @param parent
	 *           the parent <code>XMLTagWriter</code> of this instance
	 */
	public XMLTagWriter(final XMLTagWriter parent)
	{
		this(parent, false);
	}

	/**
	 * Constructs an instance of <code>XMLTagWriter</code>.
	 * 
	 * @param parent
	 *           the parent <code>XMLTagWriter</code> of this instance
	 * @param mandatory
	 *           <code>true</code> if this is a mandatory tag, <code>false</code> otherwise
	 */
	public XMLTagWriter(final XMLTagWriter parent, final boolean mandatory)
	{
		this.parent = parent;
		this.mandatory = mandatory;
	}

	protected boolean isInfoEnabled()
	{
		return this.parent != null ? this.parent.isInfoEnabled() : LOG.isInfoEnabled();
	}

	protected boolean isDebugEnabled()
	{
		return this.parent != null ? this.parent.isDebugEnabled() : LOG.isDebugEnabled();
	}

	protected boolean isWarnEnabled()
	{
		return this.parent != null ? this.parent.isWarnEnabled() : LOG.isEnabledFor(Level.WARN);
	}

	protected boolean isErrorEnabled()
	{
		return this.parent != null ? this.parent.isErrorEnabled() : LOG.isEnabledFor(Level.ERROR);
	}

	protected void info(final String message)
	{
		if (this.parent != null)
		{
			this.parent.info(message);
		}
		else
		{
			LOG.info(message);
		}
	}

	protected void debug(final String message)
	{
		if (this.parent != null)
		{
			this.parent.debug(message);
		}
		else
		{
			LOG.debug(message);
		}
	}

	protected void warn(final String message)
	{
		if (this.parent != null)
		{
			this.parent.warn(message);
		}
		else
		{
			LOG.warn(message);
		}
	}

	protected void error(final String message)
	{
		if (this.parent != null)
		{
			this.parent.error(message);
		}
		else
		{
			LOG.error(message);
		}
	}

	/**
	 * Returns the name of the xml tag.
	 * 
	 * @return the name of the tag
	 */
	protected abstract String getTagName();

	/**
	 * Here you can write out the data to the given <code>XMLOutputter</code>. This method will write the content between
	 * the start- and endtag.
	 * 
	 * @param xmlOut
	 *           the <code>XMLOutputter</code> to write to
	 * @param object
	 *           the data object, it depends on the parent what kind of object you get here
	 * @throws IOException
	 */
	protected abstract void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException;

	/**
	 * Overwrite this method, if you want to specify attributes at the actual tag. The name of the attribute has to be
	 * the key of the <code>Map</code>.
	 * 
	 * @param object
	 *           the data object, it depends on the parent what kind of object you get here
	 * @return the attributes <code>Map</code>
	 */
	protected Map getAttributesMap(final Object object)
	{
		return Collections.EMPTY_MAP;
	}

	/**
	 * This method write the start- and endtag of your tag. It also calls the method <code>writeContent</code> to write
	 * the data between these two tags.
	 * 
	 * @param xmlOut
	 *           the <code>XMLOutputter</code> to write to
	 * @param object
	 *           the data object, it depends on the parent what kind of object you get here
	 */
	public final void write(final XMLOutputter xmlOut, final Object object)
	{
		if (object == null)
		{
			if (isMandatory())
			{
				if (isErrorEnabled())
				{
					error("tag: " + getTagName() + " is not optional!");
				}
				throw new RuntimeException("tag: " + getTagName() + " is not optional!");
			}
			else
			{
				if (isDebugEnabled())
				{
					debug("Skipped empty tag:" + getTagName());
				}
				return;
			}
		}

		try
		{
			xmlOut.startTag(getTagName());

			for (final Iterator it = getAttributesMap(object).entrySet().iterator(); it.hasNext();)
			{
				final Map.Entry entry = (Map.Entry) it.next();
				xmlOut.attribute((String) entry.getKey(), (String) entry.getValue());
			}

			writeContent(xmlOut, object);

			xmlOut.endTag();

			//sets linebreak back if it was changed during 'writeContent'
			xmlOut.setLineBreak(LineBreak.UNIX);

			if (getParent() == null)
			{
				xmlOut.endDocument();
			}
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Returns the parent of this <code>XMLTagWriter</code>
	 * 
	 * @return the parent <code>XMLTagWriter</code> or <code>null</code> if no parent was declared
	 */
	protected XMLTagWriter getParent()
	{
		return parent;
	}

	/**
	 * Here you can add or replace an sub-<code>XMLTagWriter</code> to this <code>XMLTagWriter</code>.
	 * 
	 * @param tagWriter
	 *           the new <code>XMLTagWriter</code>
	 */
	public void addSubTagWriter(final XMLTagWriter tagWriter)
	{
		subTagWriterMap.put(tagWriter.getTagName(), tagWriter);
	}

	/**
	 * Here you can add or replace an sub-<code>XMLTagWriter</code> to this <code>XMLTagWriter</code>. If you have more
	 * than one subtagwriter with the same tagname, than you have to specify a type for the tagwriter, so you can
	 * indentify it in the <code>getSubTagWriter</code>-method.
	 * 
	 * @param tagWriter
	 *           tagWriter the new <code>XMLTagWriter</code>
	 * @param type
	 *           the type of this <code>XMLTagWriter</code>
	 */
	public void addSubTagWriter(final XMLTagWriter tagWriter, final String type)
	{
		if (subTagWriterMap.containsKey(tagWriter.getTagName()))
		{
			final Map map = (Map) subTagWriterMap.get(tagWriter.getTagName());
			map.put(type, tagWriter);
		}
		else
		{
			final Map typeMap = new LinkedMap();
			typeMap.put(type, tagWriter);
			subTagWriterMap.put(tagWriter.getTagName(), typeMap);
		}
	}

	/**
	 * Here you can add or replace an sub-<code>XMLTagWriter</code> to this <code>XMLTagWriter</code>. If you have more
	 * than one subtagwriter with the same tagname, than you have to specify a type for the tagwriter, so you can
	 * indentify it in the <code>getSubTagWriter</code>-method.
	 * 
	 * @param tagName
	 *           name of the tag
	 * @param typeMap
	 *           key of the <code>Map</code> is a type (String) and value is a <code>XMLTagWriter</code>
	 */
	public void addSubTagWriter(final String tagName, final Map typeMap)
	{
		if (subTagWriterMap.containsKey(tagName))
		{
			final Map map = (Map) subTagWriterMap.get(tagName);
			map.putAll(typeMap);
		}
		else
		{
			subTagWriterMap.put(tagName, typeMap);
		}
	}

	/**
	 * Returns all tagwriter of this <code>XMLTagWriter</code>
	 * 
	 * @return all tagwriter of this <code>XMLTagWriter</code>
	 */
	public Collection getAllSubTagWriter()
	{
		final Collection tagWriter = new ArrayList();
		for (final Iterator it = subTagWriterMap.values().iterator(); it.hasNext();)
		{
			final Object object = it.next();
			if (object instanceof Map)
			{
				tagWriter.addAll(((Map) object).values());
			}
			else
			{
				tagWriter.add(object);
			}

		}
		return tagWriter;
	}

	/**
	 * Returns the <code>XMLTagWriter</code> with the given tagName
	 * 
	 * @param tagName
	 *           name of the tag
	 * @return the <code>XMLTagWriter</code> with the given tagName
	 * @throws IllegalArgumentException
	 *            if no <code>XMLTagWriter</code> was found for the given tag name
	 */
	public XMLTagWriter getSubTagWriter(final String tagName)
	{
		return getSubTagWriter(tagName, null);
	}

	/**
	 * Returns the <code>XMLTagWriter</code> with the given tagName
	 * 
	 * @param tagName
	 *           name of the tag
	 * @param type
	 *           type of the <code>XMLTagWriter</code>
	 * @return the <code>XMLTagWriter</code> with the given tagName
	 * @throws IllegalArgumentException
	 *            if no <code>XMLTagWriter</code> was found for the given tag name and type
	 */
	public XMLTagWriter getSubTagWriter(final String tagName, final String type)
	{
		if (subTagWriterMap.containsKey(tagName))
		{
			final Object object = subTagWriterMap.get(tagName);
			if (object instanceof Map)
			{
				if (type != null)
				{
					final XMLTagWriter tagWriter = (XMLTagWriter) ((Map) object).get(type);
					if (tagWriter == null)
					{
						throw new IllegalArgumentException("No TagWriter found for tagname: " + tagName + " and type: " + type);
					}
					else
					{
						return tagWriter;
					}
				}
				else
				{
					throw new IllegalArgumentException("Tag:" + tagName + " is a typed one but no type was provided!");
				}
			}
			else if (object instanceof XMLTagWriter)
			{
				return (XMLTagWriter) object;
			}
			else
			{
				throw new IllegalArgumentException("Wrong mapping! Should be Map or XMLTagWriter but is:"
						+ object.getClass().getName());
			}
		}
		else
		{
			throw new IllegalArgumentException("TagWriter for tag name: " + tagName + " not known!");
		}
	}

	/**
	 * @return Returns the mandatory.
	 */
	public boolean isMandatory()
	{
		return mandatory;
	}
}
