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
package de.hybris.platform.sap.productconfig.runtime.interf.impl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.ManualTest;
import de.hybris.platform.sap.productconfig.runtime.interf.ConfigurationProvider;
import de.hybris.platform.sap.productconfig.runtime.interf.KBKey;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticValueModelImpl;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


/**
 * Base class for configuration provider tests
 *
 */
// unfortunately the hybris unit test runner tries runs this class, although its abstract...
// hence we tag it as "manual" test, so it gets not executed
@ManualTest
public abstract class ConfigurationProviderTestBase
{

	private static final int MANIFACTURER_CSTIC = 0;
	private static final int MONITOR_CSTIC = 1;
	private static final String READ_ONLY_AUTHOR = "S";
	private KBKey kbKey_SAP_SIMPLE_POC;
	protected KBKey kbKey_WCEM_SIMPLE_TEST;
	protected KBKey kbKey_WCEM_DEPENDENCY_PC;
	protected KBKey kbKey_WEC_HOUSE;
	protected KBKey kbKey_WCEM_MULTILEVEL;
	protected KBKey kbKey_WCEM_MULTILEVEL_PRODUCT_CODE_ONLY;
	protected KBKey kbKey_WEC_DRAGON_CAR;
	protected KBKey kbKey_SAP_CFG_1010;

	protected ConfigurationProvider provider;
	private boolean mockProvider;

	@Before
	public void setUp() throws Exception
	{

		provider = createProvider();

		final String providerClassName = provider.getClass().getName();
		mockProvider = providerClassName
				.equalsIgnoreCase("de.hybris.platform.sap.productconfig.runtime.mock.impl.ConfigurationProviderMockImpl");

		kbKey_SAP_SIMPLE_POC = new KBKeyImpl("YSAP_SIMPLE_POC", "YSAP_SIMPLE_POC_KB", "WEFCLNT504", "3800"); // WET->WEFCLNT504
		kbKey_WCEM_SIMPLE_TEST = new KBKeyImpl("WCEM_SIMPLE_TEST", "WCEM_SIMPLE_TEST_KB", "WEFCLNT504", "3800");
		kbKey_WCEM_DEPENDENCY_PC = new KBKeyImpl("WCEM_DEPENDENCY_PC", "WCEM_DEPENDENCY_PC_KB", "WEFCLNT504", "1");
		kbKey_WEC_HOUSE = new KBKeyImpl("WEC_HOUSE", "WEC_HOUSE", "WEFCLNT504", "3800");
		kbKey_WCEM_MULTILEVEL = new KBKeyImpl("WCEM_MULTILEVEL", "WCEM_MULTILEVEL_KB", "WEFCLNT504", "3800");
		kbKey_WCEM_MULTILEVEL_PRODUCT_CODE_ONLY = new KBKeyImpl("WCEM_MULTILEVEL", null, null, null);
		kbKey_WEC_DRAGON_CAR = new KBKeyImpl("WEC_DRAGON_CAR", null, null, null);
		kbKey_SAP_CFG_1010 = new KBKeyImpl("WCEM_CFG_NB_B2B", null, null, null);
	}


	protected abstract ConfigurationProvider createProvider();


	@Test
	public void testMultiCheckBoxRemoveAllEntryAddOne()
	{
		ConfigModel configModel = provider.createDefaultConfiguration(kbKey_WCEM_SIMPLE_TEST);
		final String configId = configModel.getId();

		CsticModel cstic = configModel.getRootInstance().getCstic("WCEM_FLAG_MULTI");

		Assert.assertEquals(2, cstic.getAssignedValues().size());
		Assert.assertEquals(9, cstic.getAssignableValues().size());

		final CsticValueModel csticValue = cstic.getAssignableValues().get(8);
		cstic.setChangedByFrontend(true);
		cstic.clearValues();
		cstic.addValue(csticValue.getName());

		provider.updateConfiguration(configModel);

		configModel = provider.retrieveConfigurationModel(configId);
		cstic = configModel.getRootInstance().getCstic("WCEM_FLAG_MULTI");

		Assert.assertEquals(1, cstic.getAssignedValues().size());
		Assert.assertEquals(9, cstic.getAssignableValues().size());
	}

	@Test
	public void testMultiCheckBoxRemoveAllEntry()
	{
		ConfigModel configModel = provider.createDefaultConfiguration(kbKey_WCEM_SIMPLE_TEST);
		final String configId = configModel.getId();

		CsticModel cstic = configModel.getRootInstance().getCstic("WCEM_FLAG_MULTI");

		Assert.assertEquals(2, cstic.getAssignedValues().size());
		Assert.assertEquals(9, cstic.getAssignableValues().size());


		cstic.clearValues();
		provider.updateConfiguration(configModel);

		configModel = provider.retrieveConfigurationModel(configId);
		cstic = configModel.getRootInstance().getCstic("WCEM_FLAG_MULTI");

		Assert.assertEquals(0, cstic.getAssignedValues().size());
		Assert.assertEquals(9, cstic.getAssignableValues().size());
	}


	@Test
	public void testAuthorValueChanged()
	{
		ConfigModel configModel = provider.createDefaultConfiguration(kbKey_WCEM_SIMPLE_TEST);
		final String configId = configModel.getId();

		CsticModel cstic = configModel.getRootInstance().getCstic("WCEM_STRING_SIMPLE");

		cstic.setSingleValue("TEST");
		cstic.setChangedByFrontend(true);
		provider.updateConfiguration(configModel);

		configModel = provider.retrieveConfigurationModel(configId);
		cstic = configModel.getRootInstance().getCstic("WCEM_STRING_SIMPLE");

		Assert.assertEquals("TEST", cstic.getSingleValue());
		Assert.assertEquals(CsticModel.AUTHOR_USER, cstic.getAuthor());
	}


	@Test
	public void testPOCcreateDefaultConfiguration()
	{

		final ConfigModel configModel = provider.createDefaultConfiguration(kbKey_SAP_SIMPLE_POC);

		final List<CsticModel> cstics = configModel.getRootInstance().getCstics();

		Assert.assertEquals("Configuration schould have three cstcis", 3, cstics.size());

		// first characteristic is a check box
		CsticModel cstic = cstics.get(0);
		Assert.assertEquals("CStic " + cstic.getName() + " should be X", "X", cstic.getAssignedValues().get(0).getName());
		Assert.assertEquals("CStic Type " + cstic.getName() + " should be a STRING", CsticModel.TYPE_STRING, cstic.getValueType());
		Assert.assertEquals("CStic " + cstic.getName() + " has a wrong language specific name ", "Simple Flag: Hide options",
				cstic.getLanguageDependentName());

		// second characteristic a numeric without default value
		cstic = cstics.get(1);
		Assert.assertEquals("CStic " + cstic.getName() + " has no default value", 0, cstic.getAssignedValues().size());
		Assert.assertEquals("CStic Type " + cstic.getName() + " should be numeric", CsticModel.TYPE_FLOAT, cstic.getValueType());
		Assert.assertEquals("CStic " + cstic.getName() + " has a wrong language specific name ", "Num w/o decimal",
				cstic.getLanguageDependentName());
		Assert.assertFalse("CStic Type " + cstic.getName() + " should not be visible", cstic.isVisible());

		// third characteristic is radio button with a default value
		cstic = cstics.get(2);
		Assert.assertEquals("CStic " + cstic.getName() + " should be ", "VAL3", cstic.getAssignedValues().get(0).getName());
		Assert.assertEquals("CStic Type " + cstic.getName() + " should be radio button", CsticModel.TYPE_STRING,
				cstic.getValueType());
		Assert.assertEquals("Characteristic has 4 possible values:", 4, cstic.getAssignableValues().size());
		Assert.assertEquals("CStic " + cstic.getName() + " has a wrong language specific name ", "Radio Button Group",
				cstic.getLanguageDependentName());
		Assert.assertFalse("CStic Type " + cstic.getName() + " should not be visible", cstic.isVisible());
	}

	@Test
	public void testPOCchangeDefaultConfiguration()
	{

		ConfigModel configModel = provider.createDefaultConfiguration(kbKey_SAP_SIMPLE_POC);

		final String configId = configModel.getId();

		// Uncheck CB

		List<CsticModel> cstics = configModel.getRootInstance().getCstics();
		final CsticModel csticCB = cstics.get(0);
		final CsticValueModel value = createCsticValueModel();
		value.setName(" ");
		final List<CsticValueModel> assignedValues = new ArrayList<CsticValueModel>();
		assignedValues.add(value);
		csticCB.setAssignedValues(assignedValues);

		provider.updateConfiguration(configModel);

		configModel = provider.retrieveConfigurationModel(configId);

		cstics = configModel.getRootInstance().getCstics();

		// second characteristic (numeric) has to be visible now
		CsticModel cstic = cstics.get(1);
		Assert.assertTrue("CStic Type " + cstic.getName() + " should be visible", cstic.isVisible());

		// third characteristic (radio button) has to be visible now
		cstic = cstics.get(2);
		Assert.assertTrue("CStic Type " + cstic.getName() + " should be visible", cstic.isVisible());

	}

	protected CsticValueModel createCsticValueModel()
	{
		return new CsticValueModelImpl();
	}

	/**
	 * @param rootInstance
	 * @param string
	 * @param string2
	 * @param i
	 */
	protected void addValueFromAssigableValue(final InstanceModel rootInstance, final String csticName,
			final int indexAssignableValues)
	{
		final CsticModel csticModel = rootInstance.getCstic(csticName);
		csticModel.addValue(csticModel.getAssignableValues().get(indexAssignableValues).getName());
	}


	@Test
	public void testAssignNonAssignableValue()
	{
		final ConfigModel configModel = provider.createDefaultConfiguration(kbKey_WCEM_SIMPLE_TEST);

		InstanceModel rootInstance = configModel.getRootInstance();

		setSingleValue(rootInstance, "WCEM_DDLB", "NOT_A_VALID_DOMAIN_VALUE");


		provider.updateConfiguration(configModel);
		rootInstance = configModel.getRootInstance();

		assertSingleValue(rootInstance, "WCEM_DDLB", "NOT_A_VALID_DOMAIN_VALUE");
	}


	@Test(expected = RuntimeException.class)
	public void testAssignInvalidNumber()
	{
		final ConfigModel configModel = provider.createDefaultConfiguration(kbKey_WCEM_SIMPLE_TEST);

		final InstanceModel rootInstance = configModel.getRootInstance();

		setSingleValue(rootInstance, "WCEM_NUMBER_SIMPLE", "aaaa");


		provider.updateConfiguration(configModel);

	}


	protected void assertSingleValueFromAssigableValue(final InstanceModel rootInstance, final String csticName,
			final int indexAssignableValues)
	{
		final CsticModel csticModel = rootInstance.getCstic(csticName);
		assertEquals(csticModel.getAssignableValues().get(indexAssignableValues).getName(), csticModel.getSingleValue());
		assertEquals(1, csticModel.getAssignedValues().size());

	}


	protected void setSingleValueFromAssigableValue(final InstanceModel rootInstance, final String csticName,
			final int indexAssignableValues)
	{
		final CsticModel csticModel = rootInstance.getCstic(csticName);
		csticModel.setSingleValue(csticModel.getAssignableValues().get(indexAssignableValues).getName());

	}


	@Test
	public void testMultiValueWithReadOnlyElements()
	{
		ConfigModel configModel = provider.createDefaultConfiguration(kbKey_WCEM_DEPENDENCY_PC);
		final String configId = configModel.getId();

		CsticModel flagCstic = configModel.getRootInstance().getCstic("WCEM_SIMPLE_FLAG");
		CsticModel multiValueCstic = configModel.getRootInstance().getCstic("WCEM_FLAG_MULTI");
		Assert.assertEquals(2, multiValueCstic.getAssignedValues().size());
		Assert.assertEquals("Value 5", multiValueCstic.getAssignedValues().get(0).getLanguageDependentName());
		Assert.assertEquals("Value 8", multiValueCstic.getAssignedValues().get(1).getLanguageDependentName());

		flagCstic.setSingleValue("X");
		provider.updateConfiguration(configModel);

		configModel = provider.retrieveConfigurationModel(configId);
		flagCstic = configModel.getRootInstance().getCstic("WCEM_SIMPLE_FLAG");
		multiValueCstic = configModel.getRootInstance().getCstic("WCEM_FLAG_MULTI");

		final CsticValueModel value2 = getValueWithName(multiValueCstic.getAssignableValues(), "2");
		final CsticValueModel value9 = getValueWithName(multiValueCstic.getAssignableValues(), "9");

		Assert.assertEquals("X", flagCstic.getSingleValue());
		Assert.assertEquals("Value 2", value2.getLanguageDependentName());
		Assert.assertEquals("Value 9", value9.getLanguageDependentName());


		Assert.assertTrue("Value 2 should be readonly", isReadOnly(value2));
		Assert.assertTrue("Value 9 should be readonly", isReadOnly(value9));

	}


	protected boolean isReadOnly(final CsticValueModel value)
	{
		return !value.isSelectable() || READ_ONLY_AUTHOR.equals(value.getAuthor());
	}

	/**
	 * @param values
	 * @param name
	 * @return
	 */
	private CsticValueModel getValueWithName(final List<CsticValueModel> values, final String name)
	{
		for (final CsticValueModel value : values)
		{
			if (value.getName().equalsIgnoreCase(name))
			{
				return value;
			}
		}
		return null;
	}

	@Test
	public void testSimpleUpdateOnlyChangedCstics()
	{
		final String cstic_WCEM_STRING_SIMPLE = "WCEM_STRING_SIMPLE";
		final String cstic_WCEM_STRING_SIMPLE_2 = "WCEM_STRING_SIMPLE_2";
		final String cstic_WCEM_RADIO_BUTTON = "WCEM_RADIO_BUTTON";

		// Create default configuration
		ConfigModel configModel = provider.createDefaultConfiguration(kbKey_WCEM_SIMPLE_TEST);
		final String configId = configModel.getId();

		InstanceModel rootInstance = configModel.getRootInstance();
		final List<CsticModel> cstics = rootInstance.getCstics();
		Assert.assertTrue("Configuration schould have at least 16 cstcis", cstics.size() >= 14);

		// Get "old" cstic values
		//final String oldValue_WCEM_STRING_SIMPLE = getFirstCsticValueName(rootInstance, cstic_WCEM_STRING_SIMPLE);
		final String oldValue_WCEM_STRING_SIMPLE_2 = getFirstCsticValueName(rootInstance, cstic_WCEM_STRING_SIMPLE_2);
		final String oldValue_WCEM_RADIO_BUTTON = getFirstCsticValueName(rootInstance, cstic_WCEM_RADIO_BUTTON);

		final String newValueToSet_WCEM_STRING_SIMPLE = "NEW_VALUE_WCEM_STRING_SIMPLE";
		final String newValueToSet_WCEM_STRING_SIMPLE_2 = "NEW_VALUE_WCEM_STRING_SIMPLE_2";

		String newValueToSet_WCEM_RADIO_BUTTON = null;
		final List<CsticValueModel> rbAssignableValues = rootInstance.getCstic(cstic_WCEM_RADIO_BUTTON).getAssignableValues();
		for (final CsticValueModel rbValue : rbAssignableValues)
		{
			if (!rbValue.getName().equalsIgnoreCase(oldValue_WCEM_RADIO_BUTTON))
			{
				newValueToSet_WCEM_RADIO_BUTTON = rbValue.getName();
			}
		}

		// Set "new" values for "WCEM_STRING_SIMPLE" and "WCEM_RADIO_BUTTON" and mark them as "changedByFrontend"
		// Set "new" value for "WCEM_STRING_SIMPLE_2", but do not mark it as "changedByFrontend"
		rootInstance.getCstic(cstic_WCEM_STRING_SIMPLE).setSingleValue(newValueToSet_WCEM_STRING_SIMPLE);
		rootInstance.getCstic(cstic_WCEM_STRING_SIMPLE_2).setSingleValue(newValueToSet_WCEM_STRING_SIMPLE_2);
		rootInstance.getCstic(cstic_WCEM_STRING_SIMPLE_2).setChangedByFrontend(false);
		rootInstance.getCstic(cstic_WCEM_RADIO_BUTTON).setSingleValue(newValueToSet_WCEM_RADIO_BUTTON);

		// Update configuration and retrieve "new" model
		provider.updateConfiguration(configModel);
		configModel = provider.retrieveConfigurationModel(configId);
		rootInstance = configModel.getRootInstance();

		// Check what characteristics have new values
		final String newValue_WCEM_STRING_SIMPLE = getFirstCsticValueName(rootInstance, cstic_WCEM_STRING_SIMPLE);
		final String newValue_WCEM_STRING_SIMPLE_2 = getFirstCsticValueName(rootInstance, cstic_WCEM_STRING_SIMPLE_2);
		final String newValue_WCEM_RADIO_BUTTON = getFirstCsticValueName(rootInstance, cstic_WCEM_RADIO_BUTTON);

		Assert.assertEquals("Value of the Characteristic WCEM_STRING_SIMPLE should be a new one", newValueToSet_WCEM_STRING_SIMPLE,
				newValue_WCEM_STRING_SIMPLE);
		Assert.assertEquals("Value of the Characteristic WCEM_STRING_SIMPLE_2 should not be changed",
				oldValue_WCEM_STRING_SIMPLE_2, newValue_WCEM_STRING_SIMPLE_2);
		Assert.assertEquals("Value of the Characteristic WCEM_RADIO_BUTTON should be a new one", newValueToSet_WCEM_RADIO_BUTTON,
				newValue_WCEM_RADIO_BUTTON);
	}

	protected void assertSingleValue(final InstanceModel rootInstance, final String csticName, final String expectedValueName)
	{
		final CsticModel csticModel = rootInstance.getCstic(csticName);
		assertEquals(expectedValueName, csticModel.getSingleValue());
		assertEquals(1, csticModel.getAssignedValues().size());
	}

	protected void setSingleValue(final InstanceModel rootInstance, final String csticName, final String valueName)
	{
		final CsticModel csticModel = rootInstance.getCstic(csticName);
		csticModel.setSingleValue(valueName);
	}

	protected void checkCsticHasLongLangDeptNames(final InstanceModel rootInstance, final String name)
	{
		Assert.assertTrue(rootInstance.getCstic(name).getLanguageDependentName().length() >= 26);
		Assert.assertTrue(rootInstance.getCstic(name).getAssignableValues().size() >= 5);
		for (final CsticValueModel valueModel : rootInstance.getCstic(name).getAssignableValues())
		{
			Assert.assertTrue(valueModel.getLanguageDependentName().length() >= 30);
		}
	}

	protected void checkCstic(final InstanceModel rootInstance, final int numValues, final int numAssignableValues,
			final boolean multivalue, final int valueType, final String csticName)
	{

		final CsticModel cstic = rootInstance.getCstic(csticName);
		Assert.assertNotNull(csticName + " not found!", cstic);

		Assert.assertEquals(csticName + " has wrong value Type", valueType, cstic.getValueType());
		Assert.assertEquals(csticName + " number of assignable values does not match", numAssignableValues, cstic
				.getAssignableValues().size());
		if (multivalue)
		{
			Assert.assertTrue(csticName + " should be multi value", cstic.isMultivalued());
		}
		else
		{
			Assert.assertFalse(csticName + " should be single value", cstic.isMultivalued());
		}
		Assert.assertTrue(csticName + " should be visisble", cstic.isVisible());
		Assert.assertFalse(csticName + " should be editable", cstic.isReadonly());
		Assert.assertTrue(csticName + " has zero type length", cstic.getTypeLength() > 0);
		Assert.assertEquals(csticName + " number of assignedvalues does not match", numValues, cstic.getAssignedValues().size());

	}

	protected String getFirstCsticValueName(final InstanceModel instance, final String csticName)
	{
		String valueName = null;
		final List<CsticValueModel> assignedValues = instance.getCstic(csticName).getAssignedValues();
		if (assignedValues != null && assignedValues.size() > 0)
		{
			final CsticValueModel assignedValue = assignedValues.get(0);
			if (assignedValue != null)
			{
				valueName = assignedValue.getName();
			}
		}
		return valueName;
	}

	protected ConfigModel createDefaultConfiguration(final KBKey kbKey)
	{
		return provider.createDefaultConfiguration(kbKey);
	}

	@Test
	public void testWHENManufacturerChangesTHENShowCorrectMonitors()
	{

		ConfigModel configModel = provider.createDefaultConfiguration(kbKey_WCEM_DEPENDENCY_PC);

		configModel = setManufacturer(configModel, "ANY");
		verifyMonitors(configModel, "S27A950D", "S24A350H", "247E3LSU");

		configModel = setManufacturer(configModel, "PHILIPS");
		verifyMonitors(configModel, "247E3LSU");

		configModel = setManufacturer(configModel, "SAMSUNG");
		verifyMonitors(configModel, "S27A950D", "S24A350H");
	}


	/**
	 * @param configModel
	 * @param numberOfMonitor
	 *
	 */
	protected void verifyMonitors(final ConfigModel configModel, final String... monitors)
	{
		List<CsticModel> cstics;
		cstics = configModel.getRootInstance().getCstics();
		final CsticModel monitorModelCstic = cstics.get(MONITOR_CSTIC);
		final int numOfMonitors = monitors.length;
		final List<CsticValueModel> monitorModelValues = monitorModelCstic.getAssignableValues();
		Assert.assertEquals("Wrong number of monitors ", numOfMonitors, monitorModelValues.size());

		for (int ii = 0; ii < numOfMonitors; ii++)
		{
			Assert.assertEquals("Wrong monitor on position " + ii, monitors[ii], monitorModelValues.get(ii).getName());

		}
	}


	/**
	 * @param configModel
	 * @param manufacturer
	 *
	 * @return
	 */
	protected ConfigModel setManufacturer(ConfigModel configModel, final String manufacturer)
	{
		final String configId = configModel.getId();

		final List<CsticModel> cstics = configModel.getRootInstance().getCstics();
		final CsticModel csticManufacturer = cstics.get(MANIFACTURER_CSTIC);

		final CsticValueModel value = createCsticValueModel();
		value.setName(manufacturer);
		final List<CsticValueModel> assignedValues = new ArrayList<CsticValueModel>();
		assignedValues.add(value);
		csticManufacturer.setAssignedValues(assignedValues);

		provider.updateConfiguration(configModel);

		configModel = provider.retrieveConfigurationModel(configId);
		return configModel;
	}


	@Test
	public void testWcemDependencyPcCsticVisibility()
	{
		ConfigModel configModel = provider.createDefaultConfiguration(kbKey_WCEM_DEPENDENCY_PC);
		final String configId = configModel.getId();
		InstanceModel rootInstance = configModel.getRootInstance();

		// check WCEM_DP_EXT_DD, WCEM_DP_SOUND_CARD, WCEM_DP_SOUND_CARD are not visible
		Assert.assertFalse("WCEM_DP_EXT_DD should not be visible", rootInstance.getCstic("WCEM_DP_EXT_DD").isVisible());
		Assert.assertFalse("WCEM_DP_SOUND_CARD should not be visible", rootInstance.getCstic("WCEM_DP_SOUND_CARD").isVisible());
		Assert.assertFalse("WCEM_DP_SOUND_CARD should not be visible", rootInstance.getCstic("WCEM_DP_SOUND_CARD").isVisible());

		// set WCEM_DP_ACCESSORY = "EXT_DD" and  "MULTIMEDIA"
		final List<CsticValueModel> assignedValues = new ArrayList<CsticValueModel>();
		addCsticValue(assignedValues, "EXT_DD", "External Disc", true, null, true);
		addCsticValue(assignedValues, "MULTIMEDIA", "Multimedia", true, null, true);
		rootInstance.getCstic("WCEM_DP_ACCESSORY").setAssignedValuesWithoutCheckForChange(assignedValues);
		rootInstance.getCstic("WCEM_DP_ACCESSORY").setChangedByFrontend(true);

		provider.updateConfiguration(configModel);
		configModel = provider.retrieveConfigurationModel(configId);
		rootInstance = configModel.getRootInstance();

		// cstic WCEM_DP_EXT_DD, WCEM_DP_SOUND_CARD and WCEM_DP_SOUND_CARD are visible)
		Assert.assertTrue("WCEM_DP_EXT_DD should be visible", rootInstance.getCstic("WCEM_DP_EXT_DD").isVisible());
		Assert.assertTrue("WCEM_DP_SOUND_CARD should be visible", rootInstance.getCstic("WCEM_DP_SOUND_CARD").isVisible());
		Assert.assertTrue("WCEM_DP_SOUND_CARD should be visible", rootInstance.getCstic("WCEM_DP_SOUND_CARD").isVisible());
	}

	@Test
	public void testWcemDependencyPcCsticReadonlyAdnRequired()
	{
		ConfigModel configModel = provider.createDefaultConfiguration(kbKey_WCEM_DEPENDENCY_PC);
		final String configId = configModel.getId();
		InstanceModel rootInstance = configModel.getRootInstance();

		// check WCEM_RO_REQ_INPUT is not readonly and not required
		Assert.assertFalse("WCEM_RO_REQ_INPUT should not be readonly", rootInstance.getCstic("WCEM_RO_REQ_INPUT").isReadonly());
		Assert.assertFalse("WCEM_RO_REQ_INPUT should not be required", rootInstance.getCstic("WCEM_RO_REQ_INPUT").isRequired());

		// select WCEM_SET_INPUT_RO and WCEM_SET_INPUT_REQ
		rootInstance.getCstic("WCEM_SET_INPUT_RO").setSingleValue("X");
		rootInstance.getCstic("WCEM_SET_INPUT_REQ").setSingleValue("X");
		provider.updateConfiguration(configModel);
		configModel = provider.retrieveConfigurationModel(configId);
		rootInstance = configModel.getRootInstance();

		// check WCEM_RO_REQ_INPUT is readonly and required
		Assert.assertTrue("WCEM_RO_REQ_INPUT should be readonly", rootInstance.getCstic("WCEM_RO_REQ_INPUT").isReadonly());
		Assert.assertTrue("WCEM_RO_REQ_INPUT should be required", rootInstance.getCstic("WCEM_RO_REQ_INPUT").isRequired());

	}

	protected void addCsticValue(final List<CsticValueModel> values, //
			final String name, //
			final String languageDependentName, //
			final boolean domainValue, //
			final String author, final boolean selectable) //
	{
		final CsticValueModel value = new CsticValueModelImpl();
		value.setName(name);
		value.setLanguageDependentName(languageDependentName);
		value.setDomainValue(domainValue);
		value.setAuthor(author);
		value.setSelectable(selectable);
		values.add(value);
	}



	@Test
	public void testAuthorAttribute()
	{
		ConfigModel configModel = provider.createDefaultConfiguration(kbKey_WCEM_SIMPLE_TEST);
		final String configId = configModel.getId();

		CsticModel cstic = configModel.getRootInstance().getCstic("WCEM_STRING_SIMPLE");
		final String defaultValue = cstic.getSingleValue();

		Assert.assertFalse(CsticModel.AUTHOR_USER.equals(cstic.getAuthor()));

		cstic.setSingleValue("changed by user");
		provider.updateConfiguration(configModel);
		configModel = provider.retrieveConfigurationModel(configId);

		cstic = configModel.getRootInstance().getCstic("WCEM_STRING_SIMPLE");
		Assert.assertEquals(CsticModel.AUTHOR_USER, cstic.getAuthor());

		cstic.setSingleValue(defaultValue);
		provider.updateConfiguration(configModel);
		configModel = provider.retrieveConfigurationModel(configId);

		cstic = configModel.getRootInstance().getCstic("WCEM_STRING_SIMPLE");

		if (mockProvider)
		{
			return;
		}
		// defaultValue is null -> therefore cstic has no assigned value
		Assert.assertEquals(CsticModel.AUTHOR_NOAUTHOR, cstic.getAuthor());
	}

	@Test
	public void testDependencySimpleFlag()
	{
		ConfigModel configModel = provider.createDefaultConfiguration(kbKey_WCEM_DEPENDENCY_PC);
		CsticModel multi = configModel.getRootInstance().getCstic("WCEM_FLAG_MULTI");
		assertEquals("Only 5 and 8 must be assigned", 2, multi.getAssignedValues().size());
		assertEquals("Unexpected value", "5", multi.getAssignedValues().get(0).getName());
		assertEquals("Unexpected value", "8", multi.getAssignedValues().get(1).getName());
		multi.setChangedByFrontend(false);

		final String configId = configModel.getId();

		final CsticModel simpleFlag = configModel.getRootInstance().getCstic("WCEM_SIMPLE_FLAG");
		simpleFlag.addValue("X");

		provider.updateConfiguration(configModel);
		configModel = provider.retrieveConfigurationModel(configId);

		multi = configModel.getRootInstance().getCstic("WCEM_FLAG_MULTI");
		assertEquals("Only 2 and 9 must be assigned", 2, multi.getAssignedValues().size());

		for (final CsticValueModel valueModel : multi.getAssignedValues())
		{
			if ("Value 2".equals(valueModel.getLanguageDependentName()) || "Value 9".equals(valueModel.getLanguageDependentName()))
			{
				continue;
			}

			fail("Wrong value found - " + valueModel.getLanguageDependentName());
		}
	}

	protected void checkAssignedValuesInExtConfig(final String xmlConfig, final String... values)
	{
		for (int i = 0; i < values.length; i++)
		{
			assertTrue(xmlConfig.contains(values[i]));
		}

	}

	protected void checkUnAssignedValuesInExtConfig(final String xmlConfig, final String... values)
	{
		for (int i = 0; i < values.length; i++)
		{
			Assert.assertFalse(xmlConfig.contains(values[i]));
		}

	}
}
