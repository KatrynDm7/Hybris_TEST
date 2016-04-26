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
package de.hybris.platform.financialacceleratorstorefrontatddtests.insurance.keywords;

import static org.junit.Assert.fail;

import de.hybris.platform.atddengine.keywords.AbstractKeywordLibrary;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


public class InsuranceKeywordLibrary extends AbstractKeywordLibrary
{

	@Autowired
	private SessionService sessionService;

	private static List<String> attributeAsList = new ArrayList();

	protected static String COMMA_DELIMITTER = ",";

	static
	{
		attributeAsList.add("tripDetailsTravellerAges");
	}


	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>prepare insurance session</i>
	 * <p>
	 *
	 * @param delimitter
	 * @param sessionAttributes
	 */
	public void prepareInsuranceSession(final String delimitter, final String... sessionAttributes)
	{
		for (final String nameValue : sessionAttributes)
		{
			bindToSession(nameValue.split(delimitter));
		}

	}

	/**
	 * Helper method to bind the name value pair from the given String array to the hybris session
	 *
	 * @param nameValuePair
	 */
	protected void bindToSession(final String nameValuePair[])
	{
		if (nameValuePair == null || nameValuePair.length != 2)
		{
			fail("Invalid nameValuePair argument to bind ");
		}


		if (attributeAsList.contains(nameValuePair[0]))
		{
			sessionService.setAttribute(nameValuePair[0], Arrays.asList(nameValuePair[1].split(COMMA_DELIMITTER)));
		}
		else
		{
			sessionService.setAttribute(nameValuePair[0], nameValuePair[1]);
		}
	}
}
