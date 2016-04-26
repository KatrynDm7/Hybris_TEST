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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.sap.productconfig.runtime.interf.ConfigurationProvider;
import de.hybris.platform.sap.productconfig.runtime.interf.impl.ConfigModelFactoryImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticGroupModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sap.custdev.projects.fbs.slc.cfg.client.ICsticValueData;


@IntegrationTest
public class ConfigurationProviderSSCImplTest extends ConfigurationProviderSSCTestBase
{

	@Override
	protected ConfigurationProvider createProvider()
	{
		//System.getProperties().put(SapproductconfigruntimesscConstants.RUNTIME_ENVIRONMENT,
		//		SapproductconfigruntimesscConstants.RUNTIME_ENVIRONMENT_STANDALONE);

		MockitoAnnotations.initMocks(this);
		providerSSC = new ConfigurationProviderSSCImpl();
		providerSSC.setConfigModelFactory(new ConfigModelFactoryImpl());
		providerSSC.setI18NService(i18nService);

		final IntervalInDomainHelperImpl intervalInDomainHelper = new IntervalInDomainHelperImpl();
		intervalInDomainHelper.setI18NService(i18nService);
		providerSSC.setIntervalInDomainHelper(intervalInDomainHelper);

		Mockito.when(i18nService.getCurrentLocale()).thenReturn(Locale.ENGLISH);

		return providerSSC;
	}

	@Test
	public void testSetSelectable() throws Exception
	{
		final ConfigurationProviderSSCImpl myProvider = new ConfigurationProviderSSCImpl();
		myProvider.setConfigModelFactory(new ConfigModelFactoryImpl());

		final ICsticValueData csticValueData = ConfigurationSSCTestData.createCsticValueData();
		csticValueData.setValueSelectable("Y");

		final CsticValueModel modelValue = myProvider.createModelValue(csticValueData);

		assertTrue(modelValue.isSelectable());
	}

	@Test
	public void testDeltaPriceMapping() throws Exception
	{
		final ConfigurationProviderSSCImpl myProvider = new ConfigurationProviderSSCImpl();
		myProvider.setConfigModelFactory(new ConfigModelFactoryImpl());

		final ICsticValueData csticValueData = ConfigurationSSCTestData.createCsticValueData();
		csticValueData.setDeltaPrice(price);
		Mockito.when(price.getPricingRate()).thenReturn(BigDecimal.ONE);
		Mockito.when(price.getPricingUnit()).thenReturn("USD");

		final CsticValueModel modelValue = myProvider.createModelValue(csticValueData);

		assertTrue("wrong price value", 0 == BigDecimal.ONE.compareTo(modelValue.getDeltaPrice().getPriceValue()));
		assertEquals("wrong currency", "USD", modelValue.getDeltaPrice().getCurrency());
	}

	@Test
	public void testDeltaPriceMapping_emptyPrice() throws Exception
	{
		final ConfigurationProviderSSCImpl myProvider = new ConfigurationProviderSSCImpl();
		myProvider.setConfigModelFactory(new ConfigModelFactoryImpl());

		final ICsticValueData csticValueData = ConfigurationSSCTestData.createCsticValueData();
		csticValueData.setDeltaPrice(price);
		Mockito.when(price.getPricingRate()).thenReturn(BigDecimal.ZERO);
		Mockito.when(price.getPricingUnit()).thenReturn("");

		final CsticValueModel modelValue = myProvider.createModelValue(csticValueData);

		assertSame(PriceModel.NO_PRICE, modelValue.getDeltaPrice());
	}

	@Test
	public void testDeltaPriceMapping_noPrice() throws Exception
	{
		final ConfigurationProviderSSCImpl myProvider = new ConfigurationProviderSSCImpl();
		myProvider.setConfigModelFactory(new ConfigModelFactoryImpl());

		final ICsticValueData csticValueData = ConfigurationSSCTestData.createCsticValueData();
		csticValueData.setDeltaPrice(null);

		final CsticValueModel modelValue = myProvider.createModelValue(csticValueData);

		assertSame(PriceModel.NO_PRICE, modelValue.getDeltaPrice());
	}

	@Test
	public void testDeltaPriceMapping_zeroPrice() throws Exception
	{
		final ConfigurationProviderSSCImpl myProvider = new ConfigurationProviderSSCImpl();
		myProvider.setConfigModelFactory(new ConfigModelFactoryImpl());

		final ICsticValueData csticValueData = ConfigurationSSCTestData.createCsticValueData();
		csticValueData.setDeltaPrice(price);
		Mockito.when(price.getPricingRate()).thenReturn(BigDecimal.ZERO);
		Mockito.when(price.getPricingUnit()).thenReturn("USD");

		final CsticValueModel modelValue = myProvider.createModelValue(csticValueData);

		assertTrue("wrong price value", 0 == BigDecimal.ZERO.compareTo(modelValue.getDeltaPrice().getPriceValue()));
		assertEquals("wrong currency", "USD", modelValue.getDeltaPrice().getCurrency());
	}

	@Test
	public void testCsticGroupAssignment()
	{
		final ConfigModel configModel = provider.createDefaultConfiguration(kbKey_WEC_HOUSE);
		final List<CsticGroupModel> groups = configModel.getRootInstance().getCsticGroups(); //retrieveCsticGroupsWithCstics();

		assertEquals("Wrong number of groups", 5, groups.size());

		// Default
		final CsticGroupModel generalGroup = groups.get(0);
		final CsticGroupModel amenitiesGroup = groups.get(1);
		final CsticGroupModel outdoorsGroup = groups.get(2);
		final CsticGroupModel roomsGroup = groups.get(3);
		final CsticGroupModel sportsGroup = groups.get(4);

		assertTrue("Wrong name of the 1st (general) group",
				generalGroup.getName().equalsIgnoreCase(InstanceModel.GENERAL_GROUP_NAME));
		assertTrue("Wrong description of the 2nd group", amenitiesGroup.getDescription().equalsIgnoreCase("additional facilities"));
		assertTrue("Wrong description of the 3rd group", outdoorsGroup.getDescription().equalsIgnoreCase("Outside the house"));
		assertTrue("Wrong description of the 4th group", roomsGroup.getDescription().equalsIgnoreCase("Types of rooms"));
		assertTrue("Wrong description of the 5th group", sportsGroup.getDescription().equalsIgnoreCase("sports facility"));

		final List<String> generalGroupCstics = new ArrayList<String>(
				Arrays.asList("FLOORS ROOMS KITCHEN WOODENFLOORING BALCONY WIND STUDY LIBRARY DRESSINGAREA PLANTS CLOSETS CARPET"
						.split(" ")));
		assertEquals("Wrong cstic list in the 1st (general) group", generalGroupCstics, generalGroup.getCsticNames());

		final List<String> amenitiesGroupCstic = new ArrayList<String>(
				Arrays.asList("FIREPLACE POWERAREA CHIMNEY SOLAR GAS WATERPURIFIER MOSAIC POPCEILING".split(" ")));
		assertEquals("Wrong cstic list in the 2nd group", amenitiesGroupCstic, amenitiesGroup.getCsticNames());

		final List<String> outdoorsGroupCstic = new ArrayList<String>(
				Arrays.asList("GARDEN TERRACE SWIMMINGPOOL PARKING PLAYAREA TENNISCOURT HELIPAD TREES POND SHOP CHIMNEY".split(" ")));
		assertEquals("Wrong cstic list in the 3rd group", outdoorsGroupCstic, outdoorsGroup.getCsticNames());

		final List<String> roomsGroupCstic = new ArrayList<String>(
				Arrays.asList("BEDROOMS BATHROOMS OFFICEROOM STORAGEROOM WASHROOM PETROOM HOBBYROOM".split(" ")));
		assertEquals("Wrong cstic list in the 4th) group", roomsGroupCstic, roomsGroup.getCsticNames());

		final List<String> sportsGroupCstic = new ArrayList<String>(
				Arrays.asList("GYM SAUNA FISH SWIMMINGPOOL PLAYAREA TENNISCOURT".split(" ")));
		assertEquals("Wrong cstic list in the 5th group", sportsGroupCstic, sportsGroup.getCsticNames());

	}
}
