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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.xyformsservices.enums.YFormDataTypeEnum;

import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpression;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Tests for the EmbeddedFormXml class to check commit status and correct values returned by xpath queries
 *
 */
public class EmbeddedFormXmlTest
{
	@InjectMocks
	private EmbeddedFormXml formXml;

	@Mock
	private XPathExpression xpathExpression;

	private Document domDocument;
    
    private EmbeddedFormXmlParser embeddedFormXmlParser;

	@Mock
	private NodeList nodeList;

	@Mock
	private Node nodeZero;
	@Mock
	private Node nodeZeroChild;
	@Mock
	private Node nodeOne;
	@Mock
	private Node nodeOneChild;
	@Mock
	private Node nodeTwo;
	@Mock
	private Node nodeTwoChild;

	@Before
	public void setup() throws ParserConfigurationException
    {
		MockitoAnnotations.initMocks(this);
        final String xmlContent = "<form><test>test123</test><test2>test1</test2><test2>test2</test2><test2>test3</test2></form>";
        embeddedFormXmlParser = new EmbeddedFormXmlParser();
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        embeddedFormXmlParser.setBuilder(builderFactory.newDocumentBuilder());
        domDocument = embeddedFormXmlParser.parseContent(xmlContent);
        
		formXml = new EmbeddedFormXml("appId", "formId", "dataId", domDocument, YFormDataTypeEnum.DRAFT);
	}

	@Test
	public void testConstructor() throws Exception
	{
		assertEquals(formXml.getApplicationId(), "appId");
		assertEquals(formXml.getFormId(), "formId");
		assertEquals(formXml.getDataId(), "dataId");
		assertEquals(formXml.getDocument(), domDocument);
		assertEquals(formXml.getDataType(), YFormDataTypeEnum.DRAFT);
	}

	@Test
	public void testEmptyFormIsNotCommitted() throws Exception
	{
		assertTrue(formXml.isCommitted());
		formXml.setDocument(null);
		assertFalse(formXml.isCommitted());
	}

	@Test
	public void testSingleFormMock() throws Exception
	{
		final String singleNode = formXml.safelyEvaluateSingle("/form/test");
		assertEquals(singleNode, "test123");
	}

	@Test
	public void testMultipleFormMock() throws Exception
	{
		final List<String> multiNodes = formXml.safelyEvaluateMultiple("/form/test2");

		assertEquals(3, multiNodes.size());
		assertEquals(multiNodes.get(0), "test1");
		assertEquals(multiNodes.get(1), "test2");
		assertEquals(multiNodes.get(2), "test3");
	}

}
