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
package de.hybris.platform.commerceservices.impex.impl;

import de.hybris.platform.impex.jalo.translators.AbstractValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.servicelayer.exceptions.SystemException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.log4j.Logger;


/**
 * Generic ValueTranslator that sets a string attribute. The value is loaded from a file. Supports loading files, jar
 * resources or zip file entries.
 */
public class FileLoaderValueTranslator extends AbstractValueTranslator
{
	private static final Logger LOG = Logger.getLogger(FileLoaderValueTranslator.class.getName());

	@Override
	public String exportValue(final Object value) throws JaloInvalidParameterException
	{
		return null;
	}

	@Override
	public Object importValue(final String valueExpr, final Item toItem) throws JaloInvalidParameterException
	{
		if (toItem == null)
		{
			setError();
		}
		else if ((valueExpr != null) && (valueExpr.length() > 0))
		{
			try
			{
				return importData(valueExpr);
			}
			catch (final Exception e)
			{
				LOG.error(valueExpr, e);
				setError();
			}
		}
		return null;
	}

	public String importData(final String path)
	{
		if ((path == null) || (path.length() <= 0))
		{
			throw new IllegalArgumentException("Invalid path definition!");
		}

		if (isAbsolutePath(path))
		{
			return loadFromFile(path);
		}
		else if (isJarBasedPath(path))
		{
			return loadFromJarResource(path);
		}
		else if (isZipBasedPath(path))
		{
			return loadFromZipFile(path);
		}

		LOG.error("Unknown file type for path [" + path + "]");
		setError();
		return null;
	}

	protected String loadFromJarResource(final String path)
	{
		final StringTokenizer tokenizer = new StringTokenizer(path, "&");
		try
		{
			if (tokenizer.countTokens() > 2)
			{
				throw new IllegalArgumentException("Invalid path definition (missing '&' sign) in " + path);
			}

			Class classLoaderClass = Item.class;
			String className = null;
			if (tokenizer.countTokens() == 2)
			{
				className = tokenizer.nextToken();
				className = className.substring("jar:".length());
				if (className.length() == 0)
				{
					throw new IllegalArgumentException("Invalid path definition (missing character between 'jar:' and '&' sign) at "
							+ path);
				}

				try
				{
					classLoaderClass = Class.forName(className);
				}
				catch (final ClassNotFoundException e)
				{
					throw new IllegalArgumentException("Can not instantiate class " + className + " defined in " + path, e);
				}
			}

			String mediaPath = FilenameUtils.separatorsToUnix(tokenizer.nextToken());
			if (className == null)
			{
				mediaPath = mediaPath.substring("jar:".length());
			}
			if (mediaPath.length() == 0)
			{
				throw new IllegalArgumentException("Invalid path definition at " + path);
			}

			try (final InputStream inputStream = classLoaderClass.getResourceAsStream(mediaPath))
			{
				if (inputStream == null)
				{
					throw new IllegalArgumentException("Can not find entry " + mediaPath + " using classloader of class "
							+ classLoaderClass.getName());
				}

				try (final InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8"))
				{
					return readFromStream(reader);
				}
			}
			catch (final IOException e)
			{
				throw new SystemException("IOException: " + mediaPath, e);
			}
		}
		catch (final NoSuchElementException e)
		{
			throw new IllegalArgumentException("Invalid path definition at " + path, e);
		}
	}

	protected String loadFromFile(final String path)
	{
		final String filename = path.substring("file:".length(), path.length()).trim();
		try (final FileReader reader = new FileReader(new File(FilenameUtils.separatorsToSystem(filename))))
		{
			return readFromStream(reader);
		}
		catch (final IOException e)
		{
			throw new SystemException("IOException: " + filename, e);
		}
	}

	protected String readFromStream(final Reader reader) throws IOException
	{
		final StringBuilder inputString = new StringBuilder();

		try (final StringBuilderWriter writer = new StringBuilderWriter(inputString);
				final BufferedReader input = new BufferedReader(reader, 2048))
		{
			//read file without worries about DOS attack - OCC-1253
			IOUtils.copy(input, writer);
		}

		//replace all new line separators with declared in property
		final String newLineSeperator = System.getProperty("line.separator");
		final Pattern pattern = Pattern.compile("(\\r|\\n|\\r\\n)+");
		final Matcher metcher = pattern.matcher(inputString);

		final StringBuffer outputString = new StringBuffer();
		while (metcher.find())
		{
			metcher.appendReplacement(outputString, newLineSeperator);
		}
		metcher.appendTail(outputString);

		outputString.append(newLineSeperator);

		return outputString.toString();
	}

	protected String loadFromZipFile(final String path)
	{
		final StringTokenizer tokenizer = new StringTokenizer(path, "&");
		try
		{
			if (tokenizer.countTokens() != 2)
			{
				throw new IllegalArgumentException("Invalid path definition");
			}
			String zfLocation = FilenameUtils.separatorsToSystem(tokenizer.nextToken());
			final String mediaPath = FilenameUtils.separatorsToSystem(tokenizer.nextToken());
			if ((zfLocation.length() > 0) && (mediaPath.length() > 0))
			{
				zfLocation = zfLocation.substring("zip:".length());
				try (ZipFile zipFile = new ZipFile(new File(zfLocation)))
				{
					ZipEntry zipEntry = zipFile.getEntry(new File(mediaPath).getPath());

					if (zipEntry == null)
					{
						zipEntry = zipFile.getEntry(new File(FilenameUtils.separatorsToUnix(mediaPath)).getPath());
					}
					if (zipEntry == null)
					{
						zipEntry = zipFile.getEntry(new File(FilenameUtils.separatorsToWindows(mediaPath)).getPath());
					}

					if (zipEntry != null)
					{
						try (final InputStreamReader reader = new InputStreamReader(zipFile.getInputStream(zipEntry)))
						{
							return readFromStream(reader);
						}
						catch (final IOException e)
						{
							throw new SystemException("IOException: " + mediaPath, e);
						}
					}

					throw new IllegalArgumentException("Invalid path definition: Zip entry does not exist");
				}
			}

			throw new IllegalArgumentException("Invalid path definition");
		}
		catch (final NoSuchElementException e)
		{
			throw new IllegalArgumentException("Invalid path definition", e);
		}
		catch (final IOException e)
		{
			throw new SystemException("Error while reading from zip", e);
		}
	}

	protected boolean isAbsolutePath(final String path)
	{
		return path.toLowerCase().startsWith("file:"); //NOPMD
	}

	protected boolean isJarBasedPath(final String path)
	{
		return path.toLowerCase().startsWith("jar:"); //NOPMD
	}

	protected boolean isZipBasedPath(final String path)
	{
		return path.toLowerCase().startsWith("zip:"); //NOPMD
	}
}
