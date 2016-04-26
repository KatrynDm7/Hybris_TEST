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
package de.hybris.platform.storefront.yforms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;

import javax.xml.parsers.DocumentBuilder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;


/**
 * Tests for the EmbeddedFormXmlParser to ensure that valid / invalid content result in either a DOM document or null
 *
 */
public class EmbeddedFormXmlParserTest
{
	@InjectMocks
	private EmbeddedFormXmlParser formParser;

	@Mock
	private DocumentBuilder builder;

	@Mock
	private Document document;

	@Before
	public void setup() throws Exception
	{
		this.formParser = new EmbeddedFormXmlParser();
		MockitoAnnotations.initMocks(this);
		Mockito.when(builder.parse((InputSource) Matchers.isNotNull())).thenReturn(document);
	}

	@Test
	public void validXmlParseTest() throws Exception
	{
		final String xmlContent = "<test><item>something</item><test>";
		final Document parsedDocument = this.formParser.parseContent(xmlContent);
		assertEquals(document, parsedDocument);
	}

	@Test
	public void emptyXmlParseTest() throws Exception
	{
		final String xmlContent = "";
		final Document parsedDocument = this.formParser.parseContent(xmlContent);
		assertNull(parsedDocument);
	}

	@Test
	public void nullXmlParseTest() throws Exception
	{
		final String xmlContent = null;
		final Document parsedDocument = this.formParser.parseContent(xmlContent);
		assertNull(parsedDocument);
	}

	@Test
	public void nullBuilderTest() throws Exception
	{
		this.formParser.setBuilder(null); // simulates init() failing
		final String xmlContent = "<test><item>something</item><test>";
		final Document parsedDocument = this.formParser.parseContent(xmlContent);
		assertNull(parsedDocument);
	}

	@Test
	public void multiThreadParseTest() throws Exception
	{
		final EmbeddedFormXmlParser xmlParser = new EmbeddedFormXmlParser();
		xmlParser.init();
		final String xmlToParse1 = "<init><node1><node2>value in node2</node2><node2>second value in node 2</node2></node1><trailer>value in trailer 1</trailer></init>";
		final String xmlToParse2 = "<init><node1><node2>2nd value in node2</node2><node2>2nd second value in node 2</node2></node1><trailer>value in trailer 2</trailer></init>";
		final String xmlToParse3 = "<init><node1><node2>3rd value in node2</node2><node2>3rd second value in node 2</node2></node1><trailer>value in trailer 3</trailer></init>";
		final String xmlToParse = "<container>" + xmlToParse1 + xmlToParse2 + xmlToParse3 + "</container>";

		final long timeNow = new Date().getTime();


		final ParserThread thread1 = new ParserThread(xmlParser, "<test id=\"1\">" + xmlToParse + "</test>", timeNow + 2000);
		final ParserThread thread2 = new ParserThread(xmlParser, "<test id=\"2\">" + xmlToParse + "</test>", timeNow + 2000);
		final ParserThread thread3 = new ParserThread(xmlParser, "<test id=\"3\">" + xmlToParse + "</test>", timeNow + 2000);
		final ParserThread thread4 = new ParserThread(xmlParser, "<test id=\"4\">" + xmlToParse + "</test>", timeNow + 2000);
		final ParserThread thread5 = new ParserThread(xmlParser, "<test id=\"5\">" + xmlToParse + "</test>", timeNow + 2000);
		final ParserThread thread6 = new ParserThread(xmlParser, "<test id=\"6\">" + xmlToParse + "</test>", timeNow + 2000);
		final ParserThread thread7 = new ParserThread(xmlParser, "<test id=\"7\">" + xmlToParse + "</test>", timeNow + 2000);
		final ParserThread thread8 = new ParserThread(xmlParser, "<test id=\"8\">" + xmlToParse + "</test>", timeNow + 2000);
		final ParserThread thread9 = new ParserThread(xmlParser, "<test id=\"9\">" + xmlToParse + "</test>", timeNow + 2000);

		final Thread t1 = new Thread(thread1);
		final Thread t2 = new Thread(thread2);
		final Thread t3 = new Thread(thread3);
		final Thread t4 = new Thread(thread4);
		final Thread t5 = new Thread(thread5);
		final Thread t6 = new Thread(thread6);
		final Thread t7 = new Thread(thread7);
		final Thread t8 = new Thread(thread8);
		final Thread t9 = new Thread(thread9);

		try
		{
			t1.run();
			t2.run();
			t3.run();
			t4.run();
			t5.run();
			t6.run();
			t7.run();
			t8.run();
			t9.run();
		}
		catch (final Throwable t)
		{
			System.err.println(t);
		}

	}

	public class ParserThread implements Runnable
	{
		private final EmbeddedFormXmlParser threadParser;
		private final String xmlToParse;
		private Document xmlDocument;

		private final long waitTime;

		public ParserThread(final EmbeddedFormXmlParser threadParser, final String xmlToParse, final long waitTime)
		{
			this.threadParser = threadParser;
			this.xmlToParse = xmlToParse;
			this.waitTime = waitTime;
			this.xmlDocument = null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			// wait until the right time
			long timeNow = new Date().getTime();

			while (timeNow < waitTime)
			{
				try
				{
					Thread.sleep(1);
					timeNow = new Date().getTime();
				}
				catch (final Exception e)
				{
					System.err.println(e);
				}
			}

			this.setXmlDocument(this.threadParser.parseContent(xmlToParse));
			System.out.println("finished");
		}

		/**
		 * @return the threadParser
		 */
		public EmbeddedFormXmlParser getThreadParser()
		{
			return threadParser;
		}

		/**
		 * @return the xmlToParse
		 */
		public String getXmlToParse()
		{
			return xmlToParse;
		}

		/**
		 * @return the xmlDocument
		 */
		public Document getXmlDocument()
		{
			return xmlDocument;
		}

		public void setXmlDocument(final Document xmlDocument)
		{
			this.xmlDocument = xmlDocument;
		}
	}


}
