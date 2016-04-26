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
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticValueModelImpl;
import de.hybris.platform.sap.productconfig.runtime.ssc.TextConverter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sap.custdev.projects.fbs.slc.kbo.local.OrchestratedCstic;
import com.sap.sce.front.base.Cstic;
import com.sap.sce.front.base.PricingConditionRate;


@IntegrationTest
public class CommonConfigurationProviderSSCImplTest extends ConfigurationProviderSSCTestBase
{
	@Mock
	protected OrchestratedCstic mockedOrchestratedCstic;

	@Mock
	Cstic mockedFirstSharedCstic;

	@Mock
	PricingConditionRate mockedPricingConditionRate;

	@Override
	protected ConfigurationProvider createProvider()
	{
		//System.getProperties().put(SapproductconfigruntimesscConstants.RUNTIME_ENVIRONMENT,
		//		SapproductconfigruntimesscConstants.RUNTIME_ENVIRONMENT_STANDALONE);

		MockitoAnnotations.initMocks(this);
		providerSSC = new CommonConfigurationProviderSSCImpl();
		final TextConverter textConverter = new TextConverterImpl();
		providerSSC.setTextConverter(textConverter);
		providerSSC.setConfigModelFactory(new ConfigModelFactoryImpl());
		providerSSC.setI18NService(i18nService);

		final IntervalInDomainHelperImpl intervalInDomainHelper = new IntervalInDomainHelperImpl();
		intervalInDomainHelper.setI18NService(i18nService);
		providerSSC.setIntervalInDomainHelper(intervalInDomainHelper);

		Mockito.when(i18nService.getCurrentLocale()).thenReturn(Locale.ENGLISH);

		return providerSSC;
	}

	@Test
	public void testAssignableValueNotNull()
	{
		final String WCEM_STRING_RADIOBT_TMPL_ADDV = "WCEM_STRING_RADIOBT_TMPL_ADDV";
		final String WCEM_RADIO_BUTTON = "WCEM_RADIO_BUTTON";

		final ConfigModel config = provider.createDefaultConfiguration(kbKey_WCEM_SIMPLE_TEST);
		final InstanceModel rootInstance = config.getRootInstance();

		final CsticModel csticWithoutAdditonalValues = rootInstance.getCstic(WCEM_RADIO_BUTTON);
		assertTrue(csticWithoutAdditonalValues.getAssignableValues().size() == 4);

		final CsticModel csticWithAdditonalValues = rootInstance.getCstic(WCEM_STRING_RADIOBT_TMPL_ADDV);
		assertTrue(csticWithAdditonalValues.getAssignableValues().size() == 2);
	}

	@Test
	public void testCsticGroupAssignment()
	{
		final ConfigModel configModel = provider.createDefaultConfiguration(kbKey_WEC_HOUSE);
		final List<CsticGroupModel> groups = configModel.getRootInstance().getCsticGroups(); // retrieveCsticGroupsWithCstics();

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
				Arrays.asList("GARDEN TERRACE SWIMMINGPOOL PARKING PLAYAREA TENNISCOURT HELIPAD TREES POND CHIMNEY SHOP".split(" ")));
		assertEquals("Wrong cstic list in the 3rd group", outdoorsGroupCstic, outdoorsGroup.getCsticNames());

		final List<String> roomsGroupCstic = new ArrayList<String>(
				Arrays.asList("BEDROOMS BATHROOMS OFFICEROOM STORAGEROOM WASHROOM PETROOM HOBBYROOM".split(" ")));
		assertEquals("Wrong cstic list in the 4th) group", roomsGroupCstic, roomsGroup.getCsticNames());

		final List<String> sportsGroupCstic = new ArrayList<String>(
				Arrays.asList("SWIMMINGPOOL GYM TENNISCOURT SAUNA FISH PLAYAREA".split(" ")));
		assertEquals("Wrong cstic list in the 5th group", sportsGroupCstic, sportsGroup.getCsticNames());

	}

	@Test
	public void testDeltaPriceMapping() throws Exception
	{
		final CommonConfigurationProviderSSCImpl myProvider = new CommonConfigurationProviderSSCImpl();
		myProvider.setConfigModelFactory(new ConfigModelFactoryImpl());

		final String valueName = "ABC";
		Mockito.when(mockedOrchestratedCstic.getValueLangDependentName("ABC")).thenReturn("abc");
		Mockito.when(Boolean.valueOf(mockedOrchestratedCstic.isValueUserOwned("ABC"))).thenReturn(Boolean.TRUE);
		Mockito.when(mockedPricingConditionRate.getConditionRateValue()).thenReturn(BigDecimal.ONE);
		Mockito.when(mockedPricingConditionRate.getConditionRateUnitName()).thenReturn("USD");

		final CsticValueModel modelValue = myProvider.createModelValue(mockedOrchestratedCstic, valueName, true, true,
				mockedPricingConditionRate);

		assertTrue("wrong price value", 0 == BigDecimal.ONE.compareTo(modelValue.getDeltaPrice().getPriceValue()));
		assertEquals("wrong currency", "USD", modelValue.getDeltaPrice().getCurrency());
	}

	@Test
	public void testDeltaPriceMapping_emptyPrice() throws Exception
	{
		final CommonConfigurationProviderSSCImpl myProvider = new CommonConfigurationProviderSSCImpl();
		myProvider.setConfigModelFactory(new ConfigModelFactoryImpl());

		final String valueName = "ABC";
		Mockito.when(mockedOrchestratedCstic.getValueLangDependentName("ABC")).thenReturn("abc");
		Mockito.when(Boolean.valueOf(mockedOrchestratedCstic.isValueUserOwned("ABC"))).thenReturn(Boolean.TRUE);
		Mockito.when(mockedPricingConditionRate.getConditionRateValue()).thenReturn(BigDecimal.ZERO);
		Mockito.when(mockedPricingConditionRate.getConditionRateUnitName()).thenReturn("");

		final CsticValueModel modelValue = myProvider.createModelValue(mockedOrchestratedCstic, valueName, true, true,
				mockedPricingConditionRate);

		assertSame(PriceModel.NO_PRICE, modelValue.getDeltaPrice());
	}

	@Test
	public void testDeltaPriceMapping_noPrice() throws Exception
	{
		final CommonConfigurationProviderSSCImpl myProvider = new CommonConfigurationProviderSSCImpl();
		myProvider.setConfigModelFactory(new ConfigModelFactoryImpl());

		final String valueName = "ABC";
		Mockito.when(mockedOrchestratedCstic.getValueLangDependentName("ABC")).thenReturn("abc");
		Mockito.when(Boolean.valueOf(mockedOrchestratedCstic.isValueUserOwned("ABC"))).thenReturn(Boolean.TRUE);

		final CsticValueModel modelValue = myProvider.createModelValue(mockedOrchestratedCstic, valueName, true, true, null);

		assertSame(PriceModel.NO_PRICE, modelValue.getDeltaPrice());
	}

	@Test
	public void testDeltaPriceMapping_zeroPrice() throws Exception
	{
		final CommonConfigurationProviderSSCImpl myProvider = new CommonConfigurationProviderSSCImpl();
		myProvider.setConfigModelFactory(new ConfigModelFactoryImpl());

		final String valueName = "ABC";
		Mockito.when(mockedOrchestratedCstic.getValueLangDependentName("ABC")).thenReturn("abc");
		Mockito.when(Boolean.valueOf(mockedOrchestratedCstic.isValueUserOwned("ABC"))).thenReturn(Boolean.TRUE);
		Mockito.when(mockedPricingConditionRate.getConditionRateValue()).thenReturn(BigDecimal.ZERO);
		Mockito.when(mockedPricingConditionRate.getConditionRateUnitName()).thenReturn("USD");

		final CsticValueModel modelValue = myProvider.createModelValue(mockedOrchestratedCstic, valueName, true, true,
				mockedPricingConditionRate);

		assertTrue("wrong price value", 0 == BigDecimal.ZERO.compareTo(modelValue.getDeltaPrice().getPriceValue()));
		assertEquals("wrong currency", "USD", modelValue.getDeltaPrice().getCurrency());
	}

	@Test
	public void testCreateCsticValues() throws Exception
	{
		final CommonConfigurationProviderSSCImpl myProvider = new CommonConfigurationProviderSSCImpl();
		myProvider.setConfigModelFactory(new ConfigModelFactoryImpl());

		final CsticModel csticModel = new CsticModelImpl();
		csticModel.setAllowsAdditionalValues(true);
		csticModel.setConstrained(true);
		csticModel.setAuthor(CsticModel.AUTHOR_USER);

		Mockito.when(mockedOrchestratedCstic.getValues()).thenReturn("A D".split(" "));
		Mockito.when(mockedOrchestratedCstic.getDynamicDomain()).thenReturn("A B C".split(" "));
		Mockito.when(mockedOrchestratedCstic.getTypicalDomain()).thenReturn("A B C".split(" "));
		Mockito.when(mockedOrchestratedCstic.getFirstSharedCstic()).thenReturn(mockedFirstSharedCstic);
		Mockito.when(mockedFirstSharedCstic.getDeltaPrices()).thenReturn(null);

		Mockito.when(mockedOrchestratedCstic.getValueLangDependentName(Mockito.anyString())).thenReturn("xxx");
		Mockito.when(Boolean.valueOf(mockedOrchestratedCstic.isValueUserOwned(Mockito.anyString()))).thenReturn(Boolean.TRUE);
		Mockito.when(Boolean.valueOf(mockedOrchestratedCstic.isValueDefault(Mockito.anyString()))).thenReturn(Boolean.TRUE);

		myProvider.createCsticValues(mockedOrchestratedCstic, csticModel);

		final List<CsticValueModel> assignableValues = csticModel.getAssignableValues();
		final List<CsticValueModel> assignedValues = csticModel.getAssignedValues();

		assertEquals("wrong number assignable values", 4, assignableValues.size());
		assertEquals("wrong number assigned values", 2, assignedValues.size());

		assertEquals("wrong assignable values [0]", "A", assignableValues.get(0).getName());
		assertEquals("wrong assignable values [1]", "B", assignableValues.get(1).getName());
		assertEquals("wrong assignable values [2]", "C", assignableValues.get(2).getName());
		assertEquals("wrong assignable values [3]", "D", assignableValues.get(3).getName());
		assertTrue("value should be selectable", assignableValues.get(0).isSelectable());
		assertTrue("value should be selectable", assignableValues.get(1).isSelectable());
		assertTrue("value should be selectable", assignableValues.get(2).isSelectable());
		assertTrue("value should be selectable", assignableValues.get(3).isSelectable());
		assertTrue("value should be a domain value", assignableValues.get(0).isDomainValue());
		assertTrue("value should be a domain value", assignableValues.get(1).isDomainValue());
		assertTrue("value should be a domain value", assignableValues.get(2).isDomainValue());
		assertTrue("value should not be a domain value", !assignableValues.get(3).isDomainValue());

		assertEquals("wrong assigned values [0]", "A", assignedValues.get(0).getName());
		assertEquals("wrong assigned values [1]", "D", assignedValues.get(1).getName());

		assertEquals("wrong cstic author", CsticModel.AUTHOR_DEFAULT, csticModel.getAuthor());
	}

	@Test
	public void testPreparePlaceholderForInterval() throws Exception
	{
		final CommonConfigurationProviderSSCImpl sscProviderCommon = (CommonConfigurationProviderSSCImpl) provider;

		final CsticModel cstic = new CsticModelImpl();
		cstic.setIntervalInDomain(true);
		cstic.setValueType(CsticModel.TYPE_INTEGER);

		final List<CsticValueModel> assignableValues = new ArrayList<CsticValueModel>();
		final CsticValueModel csticValueInterval1 = new CsticValueModelImpl();
		csticValueInterval1.setName("10 - 20");
		csticValueInterval1.setDomainValue(true);
		assignableValues.add(csticValueInterval1);
		final CsticValueModel csticValueInterval2 = new CsticValueModelImpl();
		csticValueInterval2.setName("50 - 60");
		csticValueInterval2.setDomainValue(true);
		assignableValues.add(csticValueInterval2);
		cstic.setAssignableValues(assignableValues);

		sscProviderCommon.preparePlaceholderForInterval(cstic);
		assertEquals("10 - 20 ; 50 - 60", cstic.getPlaceholder());
	}

	@Test
	public void testAdjustIntervalInDomain() throws Exception
	{
		final CommonConfigurationProviderSSCImpl sscProviderCommon = (CommonConfigurationProviderSSCImpl) provider;

		final CsticModel cstic = new CsticModelImpl();
		cstic.setIntervalInDomain(false);
		cstic.setAllowsAdditionalValues(true);
		cstic.setValueType(CsticModel.TYPE_INTEGER);

		final List<CsticValueModel> assignableValues = new ArrayList<CsticValueModel>();
		final CsticValueModel csticValueInterval1 = new CsticValueModelImpl();
		csticValueInterval1.setName("10 - 20");
		csticValueInterval1.setDomainValue(true);
		assignableValues.add(csticValueInterval1);
		final CsticValueModel csticValueInterval2 = new CsticValueModelImpl();
		csticValueInterval2.setName("50 - 60");
		csticValueInterval2.setDomainValue(true);
		assignableValues.add(csticValueInterval2);
		cstic.setAssignableValues(assignableValues);

		sscProviderCommon.adjustIntervalInDomain(cstic);
		assertTrue(cstic.isIntervalInDomain());
	}
}
