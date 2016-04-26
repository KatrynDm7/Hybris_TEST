/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.productconfig.runtime.ssc.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticValueModelImpl;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;




@UnitTest
public class IntervalInDomainHelperImplTest extends TestCase
{

	private IntervalInDomainHelperImpl intervalInDomainHelper;

	@Mock
	protected I18NService i18nService;

	@Override
	@Before
	public void setUp() throws Exception
	{
		intervalInDomainHelper = new IntervalInDomainHelperImpl();

		MockitoAnnotations.initMocks(this);

		intervalInDomainHelper.setI18NService(i18nService);
		Mockito.when(i18nService.getCurrentLocale()).thenReturn(Locale.GERMAN);
	}

	@Test
	public void testFormatNumericValue()
	{
		final String formattedValue = intervalInDomainHelper.formatNumericValue("1111.22");
		assertEquals("1.111,22", formattedValue);
	}

	@Test
	public void testFormatNumericInterval()
	{
		final String formattedInterval = intervalInDomainHelper.formatNumericInterval("1111.22 - 3333.44");
		assertEquals("1.111,22 - 3.333,44", formattedInterval);
	}

	@Test
	public void testRetrieveIntervalMask()
	{
		final CsticModel cstic = createCstic();
		final String intervalMask = intervalInDomainHelper.retrieveIntervalMask(cstic);
		assertEquals("1.111,22 - 3.333,44 ; 5.555 - 6.666", intervalMask);
	}

	@Test
	public void testValidateIntervalsOK()
	{
		final CsticModel cstic = createCstic();
		final boolean result = intervalInDomainHelper.validateIntervals("5555", cstic);
		assertTrue(result);
	}

	@Test
	public void testValidateIntervalsError()
	{
		final CsticModel cstic = createCstic();
		final boolean result = intervalInDomainHelper.validateIntervals("999", cstic);
		assertFalse(result);
	}

	private CsticModel createCstic()
	{
		final CsticModel cstic = new CsticModelImpl();

		final List<CsticValueModel> assignableValues = new ArrayList<CsticValueModel>();
		final CsticValueModel csticValueInterval1 = new CsticValueModelImpl();
		csticValueInterval1.setName("1111.22 - 3333.44");
		csticValueInterval1.setDomainValue(true);
		assignableValues.add(csticValueInterval1);
		final CsticValueModel csticValueInterval2 = new CsticValueModelImpl();
		csticValueInterval2.setName("5555 - 6666");
		csticValueInterval2.setDomainValue(true);
		assignableValues.add(csticValueInterval2);
		cstic.setAssignableValues(assignableValues);

		return cstic;
	}

}
