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
package de.hybris.platform.catalog.jalo.classification;


import de.hybris.platform.classificationsystems.jalo.ClassificationsystemsManager;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.type.ComposedType;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;


/**
 * 
 * 
 */
public class ClassificationImportCronJob extends GeneratedClassificationImportCronJob
{
	private static final Logger LOG = Logger.getLogger(ClassificationImportCronJob.class);

	/**
	 * Superclass method overridden to provide the default {@link ClassificationImportJob} instance if no job was
	 * provided.
	 */
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes)
			throws JaloBusinessException
	{
		if (allAttributes.get(JOB) == null)
		{
			allAttributes.put(JOB, ClassificationsystemsManager.getInstance().getOrCreateDefaultClassificationImportJob());
		}
		return super.createItem(ctx, type, allAttributes);
	}

	@Override
	public String getImpExScriptText(final SessionContext ctx)
	{
		// if job is running, the script is replaced by the dump file maybe (will be restored at end of job), so return an error message
		if (isRunning())
		{
			return "Script can not be shown while job is running!";
		}
		final ImpExMedia media = getJobMedia();
		if (media == null || !media.hasData())
		{
			return null;
		}
		else
		{
			final StringBuilder stringBuilder = new StringBuilder();
			BufferedReader reader = null;
			try
			{
				reader = new BufferedReader(new InputStreamReader(media.getDataFromStream()));
				boolean first = false;
				for (String s = reader.readLine(); s != null; s = reader.readLine())
				{
					if (!first)
					{
						stringBuilder.append("\n");
					}
					else
					{
						first = false;
					}
					stringBuilder.append(s);
				}
				return stringBuilder.toString();
			}
			catch (final JaloBusinessException e)
			{
				throw new JaloSystemException(e);
			}
			catch (final IOException e)
			{
				throw new JaloSystemException(e);
			}
			finally
			{
				if (reader != null)
				{
					try
					{
						reader.close();
					}
					catch (final IOException e)
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug(e.getMessage());
						}
					}
				}
			}
		}
	}

	@Override
	public void setImpExScriptText(final SessionContext ctx, final String txt)
	{
		ImpExMedia media = getJobMedia();
		if (media == null)
		{
			final Map params = new HashMap();
			params.put(Media.CODE, getCode() + "-" + System.currentTimeMillis());
			params.put(Media.MIME, "text/cvs");
			media = ImpExManager.getInstance().createImpExMedia(params);
		}
		if (txt == null)
		{
			try
			{
				media.removeData(false);
			}
			catch (final JaloBusinessException e1)
			{
				throw new JaloSystemException(e1);
			}
		}
		else
		{
			try
			{
				final ByteArrayOutputStream bos = new ByteArrayOutputStream(64 * 1024);
				final OutputStreamWriter writer = new OutputStreamWriter(bos);
				writer.write(txt);
				writer.flush();
				media.setData(new DataInputStream(new ByteArrayInputStream(bos.toByteArray())), "script.csv", "text/csv");
			}
			catch (final IOException e)
			{
				throw new JaloSystemException(e);
			}
		}
	}
}
