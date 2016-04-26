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
package de.hybris.platform.financialacceleratorstorefrontatddtests.quotation.keywords;

import static de.hybris.platform.atddengine.xml.XmlAssertions.assertXPathEvaluatesTo;
import static org.junit.Assert.fail;

import de.hybris.platform.atddengine.keywords.AbstractKeywordLibrary;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.financialacceleratorstorefrontatddtests.converters.XmlConverters;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


public class QuotationKeywordLibrary extends AbstractKeywordLibrary
{

	private static final Logger LOG = Logger.getLogger(QuotationKeywordLibrary.class);

	@Autowired
	private CartFacade cartFacade;

	@Autowired
	private XmlConverters xmlConverters;


	/**
	 * 
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>verify quote xml</i>
	 * <p>
	 * 
	 * @param xpath
	 * @param expectedXml
	 */
	public void verifyQuoteXml(final String xpath, final String expectedXml)
	{
		try
		{
			final CartData cartData = cartFacade.getSessionCart();
			final String cartXml = xmlConverters.getXmlFromObject(cartData);

			assertXPathEvaluatesTo("The Quote XML does not match the expectations :", cartXml, xpath, expectedXml,
					"transformation/removeQuoteElements.xsl");
		}
		catch (final IllegalArgumentException e)
		{
			LOG.error("Either the expected XML is malformed or the Card is null", e);
			fail("Either the expected XML is malformed or the Card is null");
		}
	}

}
