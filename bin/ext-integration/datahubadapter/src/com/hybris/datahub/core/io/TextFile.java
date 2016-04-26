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
 */

package com.hybris.datahub.core.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;


/**
 * A convenience class for reading text from a file.
 */
public class TextFile
{
	private final File filePath;

	/**
	 * Instantiates a text file.
	 *
	 * @param path a path to the text file on the file system.
	 */
	public TextFile(final String path)
	{
		filePath = new File(path);
	}

	/**
	 * Instantiates a text file.
	 *
	 * @param dir path to the directory, in which the file should be created.
	 * @param name name of the file to create.
	 */
	public TextFile(final String dir, final String name)
	{
		filePath = new File(dir, name);
	}

	/**
	 * Instantiates a text file.
	 *
	 * @param file a file, in which the text is stored.
	 */
	public TextFile(final File file)
	{
		filePath = file;
	}

	/**
	 * Retrieves location of this text file.
	 *
	 * @return path to this text file on the file system.
	 */
	public String getPath()
	{
		return filePath.getPath();
	}

	/**
	 * Retrieves location of this text file.
	 *
	 * @return path to this text file on the file system.
	 */
	public File getFilePath()
	{
		return filePath;
	}

	/**
	 * Saves text as the content of this text file
	 *
	 * @param txt a text to save in this file.
	 * @throws IOException when the text cannot be saved in this file.
	 */
	public void save(final String txt) throws IOException
	{
		prepareWrite();
		write(txt, overwritingWriter());
	}

	private FileWriter overwritingWriter() throws IOException
	{
		return new FileWriter(filePath, false);
	}

	/**
	 * Adds text to the current content of this file.
	 *
	 * @param txt a text to add.
	 * @return this text file.
	 * @throws IOException when saving the added text fails.
	 */
	public TextFile append(final String txt) throws IOException
	{
		prepareWrite();
		write(txt, appendingWriter());
		return this;
	}

	private FileWriter appendingWriter() throws IOException
	{
		return new FileWriter(filePath, true);
	}

	private void prepareWrite() throws IOException
	{
		final File fileDir = filePath.getParentFile();
		if (!fileDir.exists())
		{
			if (!fileDir.mkdirs())
			{
				throw new IOException("Failed to create file " + filePath.getPath());
			}
		}
	}

	private void write(final String txt, final FileWriter writer) throws IOException
	{
		try (final Writer file = new BufferedWriter(writer))
		{
			file.write(txt);
		}
	}

	/**
	 * Appends text to the current content of this file and adds a new line separator at the end.
	 *
	 * @param txt a line of text to add to this file content
	 * @return this text file
	 * @throws IOException when saving the added line fails.
	 */
	public TextFile appendLine(final String txt) throws IOException
	{
		try (final PrintWriter file = new PrintWriter(new BufferedWriter(appendingWriter())))
		{
			file.println(txt);
		}
		return this;
	}

	/**
	 * Reads content of this file.
	 *
	 * @return text saved in this file.
	 * @throws IOException if read failed.
	 */
	public String read() throws IOException
	{
		try (final Reader in = fileReader())
		{
			return IOUtils.toString(in);
		}
		catch (final FileNotFoundException e)
		{
			return "";
		}
	}

	private Reader fileReader() throws FileNotFoundException
	{
		return new BufferedReader(new FileReader(filePath));
	}

	/**
	 * Reads specified line from this file.
	 *
	 * @param lnum number of the line to read. The line number starts from 1; there is no line 0 in a text file.
	 * @return text on the specified line or <code>null</code>, if the line number does not exist in this file.
	 * @throws IOException when reading the specified line fails
	 */
	public String readLine(final long lnum) throws IOException
	{
		if (lnum > 0 && filePath.exists())
		{
			try (final Reader r = fileReader())
			{
				final LineIterator lit = IOUtils.lineIterator(r);
				for (int lineCnt = 1; lineCnt < lnum && lit.hasNext(); lineCnt++)
				{
					lit.nextLine();
				}
				return lit.hasNext() ? lit.nextLine() : null;
			}
		}
		return null;
	}

	/**
	 * Deletes file and its content from the file system.
	 *
	 * @throws IOException if this file cannot be deleted.
	 */
	public void delete() throws IOException
	{
		if (filePath.exists())
		{
			Files.delete(filePath.toPath());
		}
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj instanceof TextFile)
		{
			final TextFile another = (TextFile) obj;
			return filePath.equals(another.filePath);
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return filePath.hashCode();
	}

	@Override
	public String toString()
	{
		return "TextFile: " + filePath.toString();
	}
}
