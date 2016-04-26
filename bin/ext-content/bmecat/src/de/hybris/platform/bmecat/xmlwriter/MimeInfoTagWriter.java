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

import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.jalo.media.Media;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;MimeInfo&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class MimeInfoTagWriter extends XMLTagWriter
{
	/**
	 * @param parent
	 */
	public MimeInfoTagWriter(final XMLTagWriter parent)
	{
		super(parent);

		final Map mimeTagWriters = new HashMap();
		mimeTagWriters.put("thumbnail", new MimeTagWriter(this, "thumbnail"));
		mimeTagWriters.put("normal", new MimeTagWriter(this, "normal"));
		mimeTagWriters.put("detail", new MimeTagWriter(this, "detail"));
		mimeTagWriters.put("data_sheet", new MimeTagWriter(this, "data_sheet"));
		mimeTagWriters.put("logo", new MimeTagWriter(this, "logo"));
		mimeTagWriters.put("others", new MimeTagWriter(this, "others"));
		addSubTagWriter(BMECatConstants.XML.TAG.MIME, mimeTagWriters);
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getTagName()
	 */
	@Override
	protected String getTagName()
	{
		return BMECatConstants.XML.TAG.MIME_INFO;
	}

	public static String getMimeID(final Media toExport)
	{
		if (toExport.hasData())
		{
			return toExport.getFileName() != null ? toExport.getFileName() : toExport.getPK().toString();
		}
		else
		{
			return toExport.getURL();
		}
	}

	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		final BMECatExportContext exportCtx = (BMECatExportContext) object;
		final Map purposeMediaMap = exportCtx.getStringPurpose2MediaCollectionMap();
		for (final Iterator it = purposeMediaMap.entrySet().iterator(); it.hasNext();)
		{
			final Map.Entry entry = (Map.Entry) it.next();
			final String purpose = (String) entry.getKey();
			final Collection mediasOfPurpose = (Collection) entry.getValue();
			if (mediasOfPurpose != null)
			{
				for (final Iterator mediaIt = mediasOfPurpose.iterator(); mediaIt.hasNext();)
				{
					final Media media = (Media) mediaIt.next();
					final MimeTagWriter mimeTagWriter = (MimeTagWriter) getSubTagWriter(BMECatConstants.XML.TAG.MIME, purpose);
					mimeTagWriter.write(xmlOut, media);

					if (media.hasData())
					{
						exportCtx.addToExportMedias(media);
					}
				}
			}
		}
	}
}
