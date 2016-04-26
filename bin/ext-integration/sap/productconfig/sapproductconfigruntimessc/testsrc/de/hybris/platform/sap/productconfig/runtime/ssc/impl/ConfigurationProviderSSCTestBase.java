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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.ManualTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.impl.DefaultProductService;
import de.hybris.platform.sap.productconfig.runtime.interf.ConfigurationParameterB2B;
import de.hybris.platform.sap.productconfig.runtime.interf.KBKey;
import de.hybris.platform.sap.productconfig.runtime.interf.PricingConfigurationParameter;
import de.hybris.platform.sap.productconfig.runtime.interf.external.CharacteristicValue;
import de.hybris.platform.sap.productconfig.runtime.interf.external.Configuration;
import de.hybris.platform.sap.productconfig.runtime.interf.external.Instance;
import de.hybris.platform.sap.productconfig.runtime.interf.external.impl.CharacteristicValueImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.external.impl.ConfigurationImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.external.impl.InstanceImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.impl.ConfigModelFactoryImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.impl.ConfigurationProviderTestBase;
import de.hybris.platform.sap.productconfig.runtime.interf.impl.KBKeyImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticValueModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.InstanceModelImpl;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.sap.custdev.projects.fbs.slc.cfg.ConfigSession;
import com.sap.custdev.projects.fbs.slc.cfg.client.IPriceData;
import com.sap.custdev.projects.fbs.slc.cfg.command.beans.FactData;


//unfortunately the hybris unit test runner tries runs this class, although its abstract...
//hence we tag it as "manual" test, so it gets not executed
@ManualTest
public abstract class ConfigurationProviderSSCTestBase extends ConfigurationProviderTestBase
{

	@Mock
	protected I18NService i18nService;

	@Mock
	protected CommonI18NService commonI18nService;

	@Mock
	protected PricingConfigurationParameter pricingConfigurationParameter;

	@Mock
	protected ConfigurationParameterB2B configurationParameterB2B;

	@Mock
	protected DefaultProductService productService;

	protected String multiLevelXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SOLUTION><CONFIGURATION CFGINFO=\"VCOND=WEC_SURCHARGE\" CLIENT=\"000\" COMPLETE=\"T\" CONSISTENT=\"T\" KBBUILD=\"2\" KBNAME=\"WCEM_MULTILEVEL_KB\" KBPROFILE=\"WCEM_MULTILEVEL_PROFILE\" KBVERSION=\"3800\" LANGUAGE=\"E\" LANGUAGE_ISO=\"EN\" NAME=\"SCE 5.0\" ROOT_NR=\"1\" SCEVERSION=\" \"><INST AUTHOR=\"5\" CLASS_TYPE=\"300\" COMPLETE=\"T\" CONSISTENT=\"T\" INSTANCE_GUID=\"\" INSTANCE_ID=\"01\" NR=\"1\" OBJ_KEY=\"WCEM_MULTILEVEL\" OBJ_TXT=\"SAP Complex Multi level Test\" OBJ_TYPE=\"MARA\" QTY=\"1.0\" UNIT=\"ST\"><CSTICS><CSTIC AUTHOR=\" \" CHARC=\"EXP_NO_USERS\" CHARC_TXT=\"Expected Number of Users\" VALUE=\"200.0\"/><CSTIC AUTHOR=\"8\" CHARC=\"CART_TYPE\" CHARC_TXT=\"Cart Type\" INVISIBLE=\"T\" VALUE=\"CRMJ\" VALUE_TXT=\"CRM-Java\"/><CSTIC AUTHOR=\" \" CHARC=\"WCEM_BACKEND\" CHARC_TXT=\"Backend connected to WCEM\" VALUE=\"ERP\" VALUE_TXT=\"SAP ERP\"/><CSTIC AUTHOR=\"8\" CHARC=\"WCEM_RELEASE\" CHARC_TXT=\"WCEM Release (planned)\" VALUE=\"2.0\" VALUE_TXT=\"2.0\"/><CSTIC AUTHOR=\"8\" CHARC=\"WCEM_CRM_FULLFILLMENT\" CHARC_TXT=\"WCEM: SAP Fulfillment Compnt\" INVISIBLE=\"T\" VALUE=\"ERP\" VALUE_TXT=\"Order Fullfillment using ERP\"/><CSTIC AUTHOR=\"5\" CHARC=\"WCEM_ERP_CATALOG_SCENARIO\" CHARC_TXT=\"WCEM ERP: Catalog Scenario\" INVISIBLE=\"T\" VALUE=\"TRX\" VALUE_TXT=\"Trex\"/><CSTIC AUTHOR=\"5\" CHARC=\"WCEM_CRM_BACKEND_RELEASE\" CHARC_TXT=\"Connected CRM Backend Release\" INVISIBLE=\"T\" VALUE=\"NO\" VALUE_TXT=\"No CRM\"/></CSTICS></INST><PARTS><PART AUTHOR=\"6\" CLASS_TYPE=\"300\" OBJ_KEY=\"WCEM_SAPWEC\" OBJ_TYPE=\"MARA\" PARENT_NR=\"1\" POS_NR=\"0010\" S=\"T\"><INST AUTHOR=\"6\" CLASS_TYPE=\"300\" COMPLETE=\"T\" CONSISTENT=\"T\" INSTANCE_GUID=\"\" INSTANCE_ID=\"02\" NR=\"2\" OBJ_KEY=\"WCEM_SAPWEC\" OBJ_TXT=\"Web Channel, the Java Part\" OBJ_TYPE=\"MARA\" QTY=\"1.0\" UNIT=\"ST\"/></PART><PART AUTHOR=\"2\" CLASS_TYPE=\"300\" OBJ_KEY=\"WCEM_ERP\" OBJ_TYPE=\"MARA\" PARENT_NR=\"1\" POS_NR=\"0030\" S=\"T\"><INST AUTHOR=\"6\" CLASS_TYPE=\"300\" COMPLETE=\"T\" CONSISTENT=\"T\" INSTANCE_GUID=\"\" INSTANCE_ID=\"03\" NR=\"3\" OBJ_KEY=\"WCEM_ERP\" OBJ_TXT=\"Web Channel, the ERP System\" OBJ_TYPE=\"MARA\" QTY=\"1.0\" UNIT=\"ST\"><CSTICS><CSTIC AUTHOR=\" \" CHARC=\"WCEM_ERP_BACKEND_RELEASE\" CHARC_TXT=\"Connected ERP Backend Release\" VALUE=\"2005\" VALUE_TXT=\"SAP ERP 6.0\"/><CSTIC AUTHOR=\" \" CHARC=\"WCEM_ERP_CATALOG_SCENARIO\" CHARC_TXT=\"WCEM ERP: Catalog Scenario\" VALUE=\"TRX\" VALUE_TXT=\"Trex\"/></CSTICS></INST></PART><PART AUTHOR=\"2\" CLASS_TYPE=\"\" OBJ_KEY=\"WCEM_ERP_TREX\" OBJ_TYPE=\"MARA\" PARENT_NR=\"3\" POS_NR=\"0010\" S=\"T\"><INST AUTHOR=\"6\" CLASS_TYPE=\"\" COMPLETE=\"T\" CONSISTENT=\"T\" INSTANCE_GUID=\"\" INSTANCE_ID=\"05\" NR=\"5\" OBJ_KEY=\"WCEM_ERP_TREX\" OBJ_TXT=\"Web Channel, ERP TREX\" OBJ_TYPE=\"MARA\" QTY=\"1.0\" UNIT=\"ST\"/></PART></PARTS><NON_PARTS/></CONFIGURATION><SALES_STRUCTURE><ITEM INSTANCE_GUID=\"\" INSTANCE_ID=\"1\" INSTANCE_NR=\"1\" LINE_ITEM_GUID=\"\" PARENT_INSTANCE_NR=\"\"><SUB_ITEM><ITEM INSTANCE_GUID=\"\" INSTANCE_ID=\"2\" INSTANCE_NR=\"2\" LINE_ITEM_GUID=\"\" PARENT_INSTANCE_NR=\"1\"/><ITEM INSTANCE_GUID=\"\" INSTANCE_ID=\"3\" INSTANCE_NR=\"3\" LINE_ITEM_GUID=\"\" PARENT_INSTANCE_NR=\"1\"><SUB_ITEM><ITEM INSTANCE_GUID=\"\" INSTANCE_ID=\"5\" INSTANCE_NR=\"5\" LINE_ITEM_GUID=\"\" PARENT_INSTANCE_NR=\"3\"/></SUB_ITEM></ITEM></SUB_ITEM></ITEM></SALES_STRUCTURE></SOLUTION>";

	protected BaseConfigurationProviderSSCImpl providerSSC;

	protected BigDecimal priceValue;

	@Mock
	protected IPriceData price;


	@Test
	public void testDependencyPcCreateDefaultConfigurationGerman()
	{
		final String WORD1 = "DE:";
		final String WORD2 = "MERKMAL";
		final String WORD3 = "SCHREIBGESCH";
		final String WORD4 = "OBLIGATORISCH";

		Mockito.when(i18nService.getCurrentLocale()).thenReturn(Locale.GERMAN);

		final ConfigModel configModel = provider.createDefaultConfiguration(kbKey_WCEM_DEPENDENCY_PC);

		final List<CsticModel> cstics = configModel.getRootInstance().getCstics();

		Assert.assertEquals("Configuration schould have three cstcis", 12, cstics.size());

		// Check german names for cstics / values
		CsticModel cstic = configModel.getRootInstance().getCstic("WCEM_RO_REQ_INPUT");
		Assert.assertTrue("CStic " + cstic.getName() + " should have a german language dependent name", cstic
				.getLanguageDependentName().toUpperCase().startsWith(WORD1));

		cstic = configModel.getRootInstance().getCstic("WCEM_SET_INPUT_RO");
		Assert.assertTrue("CStic " + cstic.getName() + " should have a german language dependent name", cstic
				.getLanguageDependentName().toUpperCase().indexOf(WORD2) > -1);

		Assert.assertTrue("Value of CStic " + cstic.getName() + " should have a german language dependent name", cstic
				.getAssignableValues().get(0).getLanguageDependentName().toUpperCase().indexOf(WORD3) > -1);

		cstic = configModel.getRootInstance().getCstic("WCEM_SET_INPUT_REQ");
		Assert.assertTrue("CStic " + cstic.getName() + " should have a german language dependent name", cstic
				.getLanguageDependentName().toUpperCase().indexOf(WORD2) > -1);

		Assert.assertTrue("Value of CStic " + cstic.getName() + " should have a german language dependent name", cstic
				.getAssignableValues().get(0).getLanguageDependentName().toUpperCase().indexOf(WORD4) > -1);

	}

	@Test
	public void testSimpleCreateDefaultConfiguration()
	{

		final ConfigModel configModel = provider.createDefaultConfiguration(kbKey_WCEM_SIMPLE_TEST);

		final InstanceModel rootInstance = configModel.getRootInstance();
		final List<CsticModel> cstics = rootInstance.getCstics();
		Assert.assertTrue("Configuration schould have at least 16 cstcis, but has only " + cstics.size(), cstics.size() >= 14);



		// basic strings and numbers
		checkCstic(rootInstance, 0, 0, false, CsticModel.TYPE_STRING, "WCEM_STRING_SIMPLE");
		checkCstic(rootInstance, 0, 0, false, CsticModel.TYPE_STRING, "WCEM_STRING_SIMPLE_2");
		checkCstic(rootInstance, 0, 0, false, CsticModel.TYPE_FLOAT, "WCEM_NUMBER_SIMPLE");
		checkCstic(rootInstance, 1, 0, false, CsticModel.TYPE_FLOAT, "WCEM_LONG_NUMBER");
		Assert.assertEquals("9.99999999999999E9", rootInstance.getCstic("WCEM_LONG_NUMBER").getAssignedValues().get(0).getName());

		checkCstic(rootInstance, 0, 0, false, CsticModel.TYPE_FLOAT, "WCEM_NUM_NEG");

		//length tests
		checkCstic(rootInstance, 0, 0, false, CsticModel.TYPE_STRING, "WCEM_DUMMY");
		Assert.assertEquals("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM", rootInstance.getCstic("WCEM_DUMMY").getLanguageDependentName());
		checkCstic(rootInstance, 1, 8, false, CsticModel.TYPE_STRING, "WCEM_LONG_DESCRIPTION");
		checkCsticHasLongLangDeptNames(rootInstance, "WCEM_LONG_DESCRIPTION");
		checkCstic(rootInstance, 2, 8, true, CsticModel.TYPE_STRING, "WCEM_LONG_DESC_MULTI");
		checkCsticHasLongLangDeptNames(rootInstance, "WCEM_LONG_DESC_MULTI");

		// radio and ddlb
		checkCstic(rootInstance, 1, 4, false, CsticModel.TYPE_STRING, "WCEM_RADIO_BUTTON");
		checkCstic(rootInstance, 0, 9, false, CsticModel.TYPE_STRING, "WCEM_DDLB");
		checkCstic(rootInstance, 1, 9, false, CsticModel.TYPE_FLOAT, "WCEM_DDLB_NUM");

		// checkbox
		checkCstic(rootInstance, 0, 1, true, CsticModel.TYPE_STRING, "WCEM_SIMPLE_FLAG");

		// checkbox list
		checkCstic(rootInstance, 2, 9, true, CsticModel.TYPE_STRING, "WCEM_FLAG_MULTI");
		checkCstic(rootInstance, 0, 4, true, CsticModel.TYPE_FLOAT, "WCEM_FLAG_NUM");
	}

	@Test
	public void testSimpleChangeDefaultConfiguration()
	{

		ConfigModel configModel = provider.createDefaultConfiguration(kbKey_WCEM_SIMPLE_TEST);
		final String configId = configModel.getId();

		InstanceModel rootInstance = configModel.getRootInstance();

		// basic strings and numbers
		setSingleValue(rootInstance, "WCEM_STRING_SIMPLE", "abcd");
		setSingleValue(rootInstance, "WCEM_STRING_SIMPLE_2", "abcd");
		setSingleValue(rootInstance, "WCEM_NUMBER_SIMPLE", "5");
		setSingleValue(rootInstance, "WCEM_LONG_NUMBER", "0.1");
		setSingleValue(rootInstance, "WCEM_NUM_NEG", "-1");


		// radio and ddlb
		setSingleValueFromAssigableValue(rootInstance, "WCEM_RADIO_BUTTON", 3);
		setSingleValueFromAssigableValue(rootInstance, "WCEM_DDLB", 8);
		setSingleValueFromAssigableValue(rootInstance, "WCEM_DDLB_NUM", 3);

		// checkbox
		setSingleValue(rootInstance, "WCEM_SIMPLE_FLAG", "X");

		addValueFromAssigableValue(rootInstance, "WCEM_FLAG_MULTI", 5);
		addValueFromAssigableValue(rootInstance, "WCEM_FLAG_MULTI", 8);

		provider.updateConfiguration(configModel);
		configModel = provider.retrieveConfigurationModel(configId);

		rootInstance = configModel.getRootInstance();

		// basic strings and numbers
		assertSingleValue(rootInstance, "WCEM_STRING_SIMPLE", "ABCD");
		assertSingleValue(rootInstance, "WCEM_STRING_SIMPLE_2", "abcd");
		assertSingleValue(rootInstance, "WCEM_NUMBER_SIMPLE", "5.0");
		assertSingleValue(rootInstance, "WCEM_LONG_NUMBER", "0.1");
		assertSingleValue(rootInstance, "WCEM_NUM_NEG", "-1.0");

		// radio and ddlb
		assertSingleValueFromAssigableValue(rootInstance, "WCEM_RADIO_BUTTON", 3);
		assertSingleValueFromAssigableValue(rootInstance, "WCEM_DDLB", 8);
		assertSingleValueFromAssigableValue(rootInstance, "WCEM_DDLB_NUM", 3);

		// checkbox
		assertSingleValue(rootInstance, "WCEM_SIMPLE_FLAG", "X");

		CsticModel csticModel = rootInstance.getCstic("WCEM_FLAG_MULTI");
		assertEquals(4, csticModel.getAssignedValues().size());

		// change some values a second time

		// basic strings and numbers
		setSingleValue(rootInstance, "WCEM_NUMBER_SIMPLE", "5.0");
		setSingleValue(rootInstance, "WCEM_NUM_NEG", "1");

		// checkbox
		setSingleValue(rootInstance, "WCEM_SIMPLE_FLAG", "");


		provider.updateConfiguration(configModel);
		configModel = provider.retrieveConfigurationModel(configId);

		rootInstance = configModel.getRootInstance();

		// basic strings and numbers
		assertSingleValue(rootInstance, "WCEM_NUMBER_SIMPLE", "5.0");
		assertSingleValue(rootInstance, "WCEM_NUM_NEG", "1.0");

		// checkbox
		csticModel = rootInstance.getCstic("WCEM_SIMPLE_FLAG");
		assertEquals(0, csticModel.getAssignedValues().size());
	}

	@Test
	public void testGetExternalConfigurationForDependencyProduct()
	{
		ConfigModel configModel = provider.createDefaultConfiguration(kbKey_WCEM_DEPENDENCY_PC);
		String xmlConfig = provider.retrieveExternalConfiguration(configModel.getId());

		assertNotNull(xmlConfig);
		checkAssignedValuesInExtConfig(xmlConfig, "Value 8", "Value 5");
		checkUnAssignedValuesInExtConfig(xmlConfig, "Philip", "Samsung");

		configModel = setManufacturer(configModel, "PHILIPS");
		xmlConfig = provider.retrieveExternalConfiguration(configModel.getId());
		checkAssignedValuesInExtConfig(xmlConfig, "Philip", "Value 8", "Value 5", "247E3LSU");
		checkUnAssignedValuesInExtConfig(xmlConfig, "Value 1", "Samsung");

		configModel = setManufacturer(configModel, "SAMSUNG");
		xmlConfig = provider.retrieveExternalConfiguration(configModel.getId());
		checkAssignedValuesInExtConfig(xmlConfig, "Samsung", "Value 8", "Value 5");
		checkUnAssignedValuesInExtConfig(xmlConfig, "Value 1", "Philip", "247E3LSU", "S24A350H", "S27A950D");
	}

	@Test
	public void testMultilevel()
	{
		ConfigModel configModel = provider.createDefaultConfiguration(kbKey_WCEM_MULTILEVEL);
		final String configId = configModel.getId();

		final List<InstanceModel> subInstancesOfRoot = configModel.getRootInstance().getSubInstances();
		assertEquals("Wrong number of sub instances for " + configModel.getRootInstance().getName(), 1, subInstancesOfRoot.size());

		// Root instance
		InstanceModel rootInstance = configModel.getRootInstance();
		// check INDUSTRY is empty
		assertEquals("INDUSTRY should be empty", 0, rootInstance.getCstic("INDUSTRY").getAssignedValues().size());

		// Subinstance WCEM_SAPWEC
		InstanceModel instance_WCEM_SAPWEC = rootInstance.getSubInstances().get(0);
		// check WCEM_USED_SCENARIOS has no value assigned
		assertEquals("WCEM_USED_SCENARIOS should not have a value assigned", 0, instance_WCEM_SAPWEC
				.getCstic("WCEM_USED_SCENARIOS").getAssignedValues().size());


		// update INDUSTRY and WCEM_USED_SCENARIOS
		rootInstance.getCstic("INDUSTRY").setSingleValue("ABC");
		instance_WCEM_SAPWEC.getCstic("WCEM_USED_SCENARIOS").setSingleValue("ECO");
		provider.updateConfiguration(configModel);

		// retrieve updated configuration
		configModel = provider.retrieveConfigurationModel(configId);
		rootInstance = configModel.getRootInstance();
		instance_WCEM_SAPWEC = rootInstance.getSubInstances().get(0);

		// check INDUSTRY contains correct value
		Assert.assertTrue("INDUSTRY has a wrong value", rootInstance.getCstic("INDUSTRY").getAssignedValues().get(0).getName()
				.equalsIgnoreCase("ABC"));

		// check WCEM_USED_SCENARIOS contains correct value
		Assert.assertTrue("WCEM_USED_SCENARIOS has a wrong value", instance_WCEM_SAPWEC.getCstic("WCEM_USED_SCENARIOS")
				.getAssignedValues().get(0).getName().equalsIgnoreCase("ECO"));

	}

	@Test
	public void testAddConflictingCsticNewConflict()
	{
		final BaseConfigurationProviderSSCImpl sscProvider = (BaseConfigurationProviderSSCImpl) provider;

		final CsticModel cstic = new CsticModelImpl();
		cstic.setName("CSTIC1");
		final List<CsticModel> cstics = new ArrayList<>();
		cstics.add(cstic);
		final InstanceModel instance = new InstanceModelImpl();
		instance.setCstics(cstics);

		final FactData fact = new FactData();
		fact.setObsName(cstic.getName());

		final List<CsticModel> conflictingCsics = new ArrayList<>();

		sscProvider.addConflictingCstic(instance, conflictingCsics, fact);
		assertEquals(1, conflictingCsics.size());
		assertEquals(cstic, conflictingCsics.get(0));
	}

	@Test
	public void testAddConflictingCsticExistingConflict()
	{
		final BaseConfigurationProviderSSCImpl sscProvider = (BaseConfigurationProviderSSCImpl) provider;

		final CsticModel cstic = new CsticModelImpl();
		cstic.setName("CSTIC1");
		final List<CsticModel> cstics = new ArrayList<>();
		cstics.add(cstic);
		final InstanceModel instance = new InstanceModelImpl();
		instance.setCstics(cstics);

		final FactData fact = new FactData();
		fact.setObsName(cstic.getName());

		final List<CsticModel> conflictingCsics = new ArrayList<>();
		conflictingCsics.add(cstic);

		sscProvider.addConflictingCstic(instance, conflictingCsics, fact);
		assertEquals(1, conflictingCsics.size());
		assertEquals(cstic, conflictingCsics.get(0));
	}

	@Test
	public void testAddConflictingCsticDifferentInstance()
	{
		final BaseConfigurationProviderSSCImpl sscProvider = (BaseConfigurationProviderSSCImpl) provider;

		final CsticModel cstic = new CsticModelImpl();
		cstic.setName("CSTIC1");
		final List<CsticModel> cstics = new ArrayList<>();

		final InstanceModel instance = new InstanceModelImpl();
		instance.setCstics(cstics);

		final FactData fact = new FactData();
		fact.setObsName(cstic.getName());

		final List<CsticModel> conflictingCsics = new ArrayList<>();

		sscProvider.addConflictingCstic(instance, conflictingCsics, fact);
		assertEquals(0, conflictingCsics.size());
	}

	@Test
	public void testBasePrice()
	{
		mockPricingConfigurationParameter();

		final ConfigurationContextAndPricingWrapperImpl contextAndPricingWrapper = new ConfigurationContextAndPricingWrapperImpl();
		contextAndPricingWrapper.setPricingConfigurationParameter(pricingConfigurationParameter);
		contextAndPricingWrapper.setProductService(productService);
		contextAndPricingWrapper.setI18NService(commonI18nService);
		contextAndPricingWrapper.setConfigModelFactory(new ConfigModelFactoryImpl());
		((BaseConfigurationProviderSSCImpl) provider).setContextAndPricingWrapper(contextAndPricingWrapper);

		final ConfigModel configModel = provider.createDefaultConfiguration(kbKey_WEC_DRAGON_CAR);

		final BigDecimal expectedBasePriceValue = new BigDecimal(22000);
		final BigDecimal expectedOptionPriceValue = BigDecimal.ZERO;
		final BigDecimal expectedTotalPriceValue = new BigDecimal(22000);

		final String expectedCurrency = "USD";

		final BigDecimal basePriceValue = configModel.getBasePrice().getPriceValue();
		final BigDecimal optionPriceValue = configModel.getSelectedOptionsPrice().getPriceValue();
		final BigDecimal totalPriceValue = configModel.getCurrentTotalPrice().getPriceValue();

		final String currency = configModel.getCurrentTotalPrice().getCurrency();

		assertEquals("Wrong Base Price", 0, expectedBasePriceValue.compareTo(basePriceValue));
		assertEquals("No Option Price expected", 0, expectedOptionPriceValue.compareTo(optionPriceValue));
		assertEquals("Wrong Total Price", 0, expectedTotalPriceValue.compareTo(totalPriceValue));
		assertEquals("Wrong Currency", expectedCurrency, currency);

	}

	@Test
	public void testBaseAndOptionsPrice()
	{
		mockPricingConfigurationParameter();

		final ConfigurationContextAndPricingWrapperImpl contextAndPricingWrapper = new ConfigurationContextAndPricingWrapperImpl();
		contextAndPricingWrapper.setPricingConfigurationParameter(pricingConfigurationParameter);
		contextAndPricingWrapper.setProductService(productService);
		contextAndPricingWrapper.setI18NService(commonI18nService);
		contextAndPricingWrapper.setConfigModelFactory(new ConfigModelFactoryImpl());
		((BaseConfigurationProviderSSCImpl) provider).setContextAndPricingWrapper(contextAndPricingWrapper);

		ConfigModel configModel = provider.createDefaultConfiguration(kbKey_WEC_DRAGON_CAR);

		// set Engine = Diesel
		configModel.getRootInstance().getCstic("WEC_DC_ENGINE").setSingleValue("D");
		provider.updateConfiguration(configModel);
		configModel = provider.retrieveConfigurationModel(configModel.getId());

		final BigDecimal expectedBasePriceValue = new BigDecimal(22000);
		final BigDecimal expectedOptionPriceValue = new BigDecimal(900);
		final BigDecimal expectedTotalPriceValue = new BigDecimal(22900);

		final String expectedCurrency = "USD";

		final BigDecimal basePriceValue = configModel.getBasePrice().getPriceValue();
		final BigDecimal optionPriceValue = configModel.getSelectedOptionsPrice().getPriceValue();
		final BigDecimal totalPriceValue = configModel.getCurrentTotalPrice().getPriceValue();

		final String currency = configModel.getCurrentTotalPrice().getCurrency();

		assertEquals("Wrong Base Price", 0, expectedBasePriceValue.compareTo(basePriceValue));
		assertEquals("Option Price expected", 0, expectedOptionPriceValue.compareTo(optionPriceValue));
		assertEquals("Wrong Total Price", 0, expectedTotalPriceValue.compareTo(totalPriceValue));
		assertEquals("Wrong Currency", expectedCurrency, currency);
	}

	protected void mockPricingConfigurationParameter()
	{
		Mockito.when(productService.getProductForCode(kbKey_WEC_DRAGON_CAR.getProductCode())).thenReturn(new ProductModel());
		Mockito.when(productService.getProductForCode(kbKey_SAP_CFG_1010.getProductCode())).thenReturn(new ProductModel());

		Mockito.when(Boolean.valueOf(pricingConfigurationParameter.isPricingSupported())).thenReturn(Boolean.TRUE);
		Mockito.when(pricingConfigurationParameter.getSalesOrganization()).thenReturn("3020");
		Mockito.when(pricingConfigurationParameter.getDistributionChannelForConditions()).thenReturn("30");
		Mockito.when(pricingConfigurationParameter.getDivisionForConditions()).thenReturn("00");
		Mockito.when(pricingConfigurationParameter.getPricingProcedure()).thenReturn("WECNUS");
		Mockito.when(pricingConfigurationParameter.getTargetForBasePrice()).thenReturn("BASE_PRICE");
		Mockito.when(pricingConfigurationParameter.getTargetForSelectedOptions()).thenReturn("OPTIONS");
		Mockito.when(pricingConfigurationParameter.retrieveCurrencySapCode(null)).thenReturn("USD");
		Mockito.when(pricingConfigurationParameter.retrieveUnitSapCode(null)).thenReturn("ST");


	}

	@Test
	public void testMultilevelOnlyProductCodeAvailable()
	{
		ConfigModel configModel = provider.createDefaultConfiguration(kbKey_WCEM_MULTILEVEL_PRODUCT_CODE_ONLY);
		final String configId = configModel.getId();

		final List<InstanceModel> subInstancesOfRoot = configModel.getRootInstance().getSubInstances();
		assertEquals("Wrong number of sub instances for " + configModel.getRootInstance().getName(), 1, subInstancesOfRoot.size());

		// Root instance
		InstanceModel rootInstance = configModel.getRootInstance();
		// check INDUSTRY is empty
		assertEquals("INDUSTRY should be empty", 0, rootInstance.getCstic("INDUSTRY").getAssignedValues().size());

		// Subinstance WCEM_SAPWEC
		InstanceModel instance_WCEM_SAPWEC = rootInstance.getSubInstances().get(0);
		// check WCEM_USED_SCENARIOS has no value assigned
		assertEquals("WCEM_USED_SCENARIOS should not have a value assigned", 0, instance_WCEM_SAPWEC
				.getCstic("WCEM_USED_SCENARIOS").getAssignedValues().size());


		// update INDUSTRY and WCEM_USED_SCENARIOS
		rootInstance.getCstic("INDUSTRY").setSingleValue("ABC");
		instance_WCEM_SAPWEC.getCstic("WCEM_USED_SCENARIOS").setSingleValue("ECO");
		provider.updateConfiguration(configModel);

		// retrieve updated configuration
		configModel = provider.retrieveConfigurationModel(configId);
		rootInstance = configModel.getRootInstance();
		instance_WCEM_SAPWEC = rootInstance.getSubInstances().get(0);

		// check INDUSTRY contains correct value
		Assert.assertTrue("INDUSTRY has a wrong value", rootInstance.getCstic("INDUSTRY").getAssignedValues().get(0).getName()
				.equalsIgnoreCase("ABC"));

		// check WCEM_USED_SCENARIOS contains correct value
		Assert.assertTrue("WCEM_USED_SCENARIOS has a wrong value", instance_WCEM_SAPWEC.getCstic("WCEM_USED_SCENARIOS")
				.getAssignedValues().get(0).getName().equalsIgnoreCase("ECO"));

	}

	@Test
	public void testCreateConfigurationFromExternalSourceToday()
	{
		final KBKey kbKeyColorTest = new KBKeyImpl("WEC_COLOR_TEST");
		createConfigurationFromExternalSource(kbKeyColorTest);
	}

	@Test
	public void testCreateConfigurationFromExternalSourceYesterday()
	{
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		final KBKey kbKeyColorTest = new KBKeyImpl("WEC_COLOR_TEST", null, null, null, cal.getTime());
		createConfigurationFromExternalSource(kbKeyColorTest);
	}

	private void createConfigurationFromExternalSource(final KBKey kbKeyColorTest)
	{
		final Configuration extConfig = new ConfigurationImpl();

		// add instance
		final Instance extInstance = new InstanceImpl();
		extInstance.setComplete(true);
		extInstance.setConsistent(true);
		extInstance.setId("1");
		extInstance.setAuthor("5");
		extInstance.setClassType("300");
		extInstance.setObjectKey("WEC_COLOR_TEST");
		extInstance.setObjectText("Color Test");
		extInstance.setObjectType("MARA");
		extInstance.setQuantity("1.0");
		extInstance.setQuantityUnit("ST");

		extConfig.addInstance(extInstance);
		extConfig.setRootInstance(extInstance);

		// add cstic / value
		final CharacteristicValue extCsticValue = new CharacteristicValueImpl();
		extCsticValue.setCharacteristic("WEC_TEMPL_TEST_DEF");
		extCsticValue.setCharacteristicText("Template test default");
		extCsticValue.setAuthor("8");
		extCsticValue.setValue("12-AA");
		extCsticValue.setValueText("A value");
		extCsticValue.setInstId("1");

		extConfig.addCharacteristicValue(extCsticValue);

		extConfig.setKbKey(kbKeyColorTest);

		final ConfigModel configModel = provider.createConfigurationFromExternalSource(extConfig);

		assertNotNull(configModel);

		assertEquals("Wrong instance name", "WEC_COLOR_TEST", configModel.getRootInstance().getName());

		final CsticModel cstic = configModel.getRootInstance().getCstic("WEC_TEMPL_TEST_DEF");
		assertEquals("Wrong cstic value", "12-AA", cstic.getAssignedValues().get(0).getName());
		assertEquals("Wrong cstic value author", "8", cstic.getAssignedValues().get(0).getAuthorExternal());
	}

	@Test
	public void testMultiLevelFromExternal()
	{
		final ConfigModel configModel = provider.createConfigurationFromExternalSource(kbKey_WCEM_MULTILEVEL, multiLevelXML);

		assertNotNull(configModel);
		assertTrue(configModel.isConsistent());
		assertTrue(configModel.isComplete());
		assertEquals(kbKey_WCEM_MULTILEVEL.getProductCode(), configModel.getRootInstance().getName());
	}

	@Test
	public void testReleaseSession()
	{
		providerSSC.getSessionMap().put("123", new ConfigSession());
		providerSSC.releaseSession("123");
		assertEquals("Session object not released", providerSSC.getSessionMap().size(), 0);
		providerSSC.releaseSession("123");
	}

	protected void mockConfigurationParameterForCustomerSpecificPrice()
	{
		Mockito.when(productService.getProductForCode(kbKey_WCEM_MULTILEVEL.getProductCode())).thenReturn(new ProductModel());

		Mockito.when(Boolean.valueOf(pricingConfigurationParameter.isPricingSupported())).thenReturn(Boolean.TRUE);
		Mockito.when(pricingConfigurationParameter.getSalesOrganization()).thenReturn("3020");
		Mockito.when(pricingConfigurationParameter.getDistributionChannelForConditions()).thenReturn("30");
		Mockito.when(pricingConfigurationParameter.getDivisionForConditions()).thenReturn("00");
		Mockito.when(pricingConfigurationParameter.getPricingProcedure()).thenReturn("WECNUS");
		Mockito.when(pricingConfigurationParameter.getTargetForBasePrice()).thenReturn("BASE_PRICE");
		Mockito.when(pricingConfigurationParameter.getTargetForSelectedOptions()).thenReturn("OPTIONS");
		Mockito.when(pricingConfigurationParameter.retrieveCurrencySapCode(null)).thenReturn("USD");
		Mockito.when(pricingConfigurationParameter.retrieveUnitSapCode(null)).thenReturn("ST");

		Mockito.when(Boolean.valueOf(configurationParameterB2B.isSupported())).thenReturn(Boolean.TRUE);
		Mockito.when(configurationParameterB2B.getCustomerNumber()).thenReturn("0000100171");
	}

	@Test
	public void testCustomerPrice()
	{
		mockConfigurationParameterForCustomerSpecificPrice();

		final ConfigurationContextAndPricingWrapperImpl contextAndPricingWrapper = new ConfigurationContextAndPricingWrapperImpl();
		contextAndPricingWrapper.setPricingConfigurationParameter(pricingConfigurationParameter);
		contextAndPricingWrapper.setConfigurationParameterB2B(configurationParameterB2B);
		contextAndPricingWrapper.setProductService(productService);
		contextAndPricingWrapper.setI18NService(commonI18nService);
		contextAndPricingWrapper.setConfigModelFactory(new ConfigModelFactoryImpl());
		((BaseConfigurationProviderSSCImpl) provider).setContextAndPricingWrapper(contextAndPricingWrapper);

		final ConfigModel configModel = provider.createDefaultConfiguration(kbKey_WCEM_MULTILEVEL);

		final BigDecimal expectedBasePriceValue = new BigDecimal(999);
		final BigDecimal expectedOptionPriceValue = BigDecimal.ZERO;
		final BigDecimal expectedTotalPriceValue = new BigDecimal(999);

		final String expectedCurrency = "USD";

		final BigDecimal basePriceValue = configModel.getBasePrice().getPriceValue();
		final BigDecimal optionPriceValue = configModel.getSelectedOptionsPrice().getPriceValue();
		final BigDecimal totalPriceValue = configModel.getCurrentTotalPrice().getPriceValue();

		final String currency = configModel.getCurrentTotalPrice().getCurrency();

		assertEquals("Wrong Base Price", 0, expectedBasePriceValue.compareTo(basePriceValue));
		assertEquals("No Option Price expected", 0, expectedOptionPriceValue.compareTo(optionPriceValue));
		assertEquals("Wrong Total Price", 0, expectedTotalPriceValue.compareTo(totalPriceValue));
		assertEquals("Wrong Currency", expectedCurrency, currency);
	}

	@Test
	public void testDeltaPrice()
	{
		mockPricingConfigurationParameter();

		final ConfigurationContextAndPricingWrapperImpl contextAndPricingWrapper = new ConfigurationContextAndPricingWrapperImpl();
		contextAndPricingWrapper.setPricingConfigurationParameter(pricingConfigurationParameter);
		contextAndPricingWrapper.setProductService(productService);
		contextAndPricingWrapper.setI18NService(commonI18nService);
		contextAndPricingWrapper.setConfigModelFactory(new ConfigModelFactoryImpl());
		((BaseConfigurationProviderSSCImpl) provider).setContextAndPricingWrapper(contextAndPricingWrapper);

		final ConfigModel configModel = provider.createDefaultConfiguration(kbKey_SAP_CFG_1010);
		final CsticModel cstic = configModel.getRootInstance().getCstic("WCEM_DEMO_MEMORY");
		final CsticValueModel csticValue = cstic.getAssignableValues().get(0);
		priceValue = csticValue.getDeltaPrice().getPriceValue();
		final BigDecimal expectedPrice = new BigDecimal("240.00");
		final String msg = "Expected delta price " + expectedPrice.toPlainString() + ", but saw " + priceValue.toPlainString()
				+ " for value " + csticValue.getName();
		assertEquals(msg, 0, expectedPrice.compareTo(priceValue));
	}

	@Test
	public void testReplaceConflictTextNoText()
	{
		final BaseConfigurationProviderSSCImpl sscProvider = (BaseConfigurationProviderSSCImpl) provider;

		final IntervalInDomainHelperImpl intervalInDomainHelper = Mockito.spy(new IntervalInDomainHelperImpl());
		Mockito.doReturn("Value 40 is not part of interval 10 - 20 ; 50 - 60").when(intervalInDomainHelper)
				.retrieveErrorMessage("40", "10 - 20 ; 50 - 60");
		intervalInDomainHelper.setI18NService(i18nService);
		Mockito.when(i18nService.getCurrentLocale()).thenReturn(Locale.ENGLISH);
		sscProvider.setIntervalInDomainHelper(intervalInDomainHelper);

		final CsticModel cstic = new CsticModelImpl();
		cstic.setName("CSTIC1");
		cstic.setIntervalInDomain(true);
		cstic.setValueType(CsticModel.TYPE_INTEGER);

		final List<CsticValueModel> assignedValues = new ArrayList<CsticValueModel>();
		final CsticValueModel csticValue = new CsticValueModelImpl();
		csticValue.setName("40");
		assignedValues.add(csticValue);
		cstic.setAssignedValuesWithoutCheckForChange(assignedValues);

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

		final String conflictText = sscProvider.replaceConflictText(cstic, null);

		assertEquals("Value 40 is not part of interval 10 - 20 ; 50 - 60", conflictText);
	}

	@Test
	public void testReplaceConflictTextWhenTextExists()
	{
		final BaseConfigurationProviderSSCImpl sscProvider = (BaseConfigurationProviderSSCImpl) provider;
		final String conflictText = sscProvider.replaceConflictText(new CsticModelImpl(), "ConflictText");
		assertEquals("ConflictText", conflictText);
	}
}
