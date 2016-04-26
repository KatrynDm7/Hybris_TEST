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
package de.hybris.platform.sap.core.jco.rec.version000.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;
import com.sap.conn.jco.JCoRecord;
import com.sap.conn.jco.JCoRecordMetaData;

import de.hybris.platform.sap.core.jco.monitor.jaxb.JcoDocumentBuilderFactory;
import de.hybris.platform.sap.core.jco.rec.version000.JCoRecXMLParser;
import de.hybris.platform.sap.core.jco.rec.version000.JCoRecXMLParserException;


/**
 * This implementation of {@link JCoRecXMLParser} is the default parser used in JCoRec.<br>
 * Parsing of single functions is not supported.
 */
class JCoRecDefaultXMLParser implements JCoRecXMLParser
{
	
	/**
	 * Logger.
	 */
	static final Logger LOG = Logger.getLogger(JCoRecDefaultXMLParser.class.getName());

	@Override
	public void parse(final JCoRecRepository repo, final String functionKey, final File f) throws JCoRecXMLParserException
	{
		//Parsing of single functions is not supported. Use {@link #parse(JCoRecRepository, File)} instead.
		throw new JCoRecXMLParserException("Not Supported");
	}

	/**
	 * Parses a xml file to {@code Document doc} and further parses the {@link JCoRecordMetaData},
	 * {@link JCoFunctionTemplate}, {@link JCoFunction} and {@link JCoRecord} represented in {@code doc} to the given
	 * {@link JCoRecRepository}.
	 * 
	 * @param repo
	 *           the {@link JCoRecRepository} where the parsed Objects are appended
	 * @param file
	 *           the {@link File} to parse
	 * @throws JCoRecXMLParserException
	 *            in case of an error while parsing the file or if the file does not exist
	 * 
	 * @see de.hybris.platform.sap.core.jco.rec.version000.JCoRecXMLParser#parse(JCoRecRepository, File) java.io.File)
	 */
	@Override
	public void parse(final JCoRecRepository repo, final File file) throws JCoRecXMLParserException
	{
		if (file.exists() == false)
		{
			throw new JCoRecXMLParserException("File not found during parsing: " + file.getAbsolutePath());
		}
		
		Reader stringReader = null;
		
		try
		{

			final DocumentBuilder documentBuilder = JcoDocumentBuilderFactory.getInstance().newDocumentBuilder();

			String xml = readFileAsString(file);
			xml = xml.replaceAll("(>\\s*)(\\n|\\r|\\s)+<", "><");

            stringReader = new StringReader(xml);
			final Document doc = documentBuilder.parse(new InputSource(stringReader));

			if (doc == null)
			{
				throw new JCoRecXMLParserException("Could not create xml document for parsing. File path is "
						+ file.getAbsolutePath() + ".");
			}

			new JCoRecMetaDataParser().parseMetaDataRepository(doc, repo.getMetaDataRepository());
			new JCoRecFunctionParser().parseRepository(doc, repo);

		}
		catch (final SAXException | IOException | ParserConfigurationException e)
		{
			throw new JCoRecXMLParserException(e);
		}
		finally
		{
			
			if (stringReader != null)
			{   
				try
				{
					stringReader.close();
				}
				catch(IOException ex)
				{
					// Log the close() method exception	
					LOG.error("Error when closing the StringReader! " + ex.getMessage(), ex);
					
				}
			}
			
		}
	}

	/**
	 * Reads the contend of a {@link File} as a {@link String}.
	 * 
	 * @param file
	 *           the {@link File} to be read
	 * @return the content as a {@link String}
	 * @throws java.io.IOException
	 *            if an error occurs while reading the file
	 */
	protected String readFileAsString(final File file) throws java.io.IOException
	{
		final byte[] buffer = new byte[(int) file.length()];
		BufferedInputStream f = null;
		try
		{
			f = new BufferedInputStream(new FileInputStream(file));
			f.read(buffer);
		}
		finally
		{
			if (f != null)
			{   
				try
				{
					f.close();
				}
				catch(IOException ex)
				{
					// Log the close() method exception	
					LOG.error("Error when closing the BufferedInputStream! " + ex.getMessage(), ex);
				}
			}
		}
		return new String(buffer);
	}


}
