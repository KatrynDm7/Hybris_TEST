package de.hybris.platform.sap.productconfig.frontend.controllers;

import de.hybris.platform.commercefacades.storesession.data.CurrencyData;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.CsticValueData;
import de.hybris.platform.sap.productconfig.facades.GroupStatusType;
import de.hybris.platform.sap.productconfig.facades.KBKeyData;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.facades.UiType;
import de.hybris.platform.sap.productconfig.frontend.util.impl.ConstantHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AbstractProductConfigControllerTest
{

	protected static final String KB_VERSION = "123";
	protected static final String LOG_SYS = "ABC";
	protected static final String KB_NAME = "YSAP_SIMPLE_POC";
	protected static final String SIMPLE_VALUE = "SIMPLE";
	protected static final String PRODUCT_CODE = KB_NAME;
	protected static final String CONFIG_ID = "5";
	protected static final String PRODUCT_SESSION_PREFIX = "product_code_";
	protected KBKeyData kbKey;
	protected List<CsticData> csticList;
	protected ConfigurationData configData;

	public AbstractProductConfigControllerTest()
	{
		super();
	}

	protected KBKeyData createKbKey()
	{
		final KBKeyData kbKey = new KBKeyData();
		kbKey.setProductCode(PRODUCT_CODE);
		return kbKey;
	}

	protected ConfigurationData createConfigurationData()
	{
		final ConfigurationData configData = new ConfigurationData();
		configData.setConfigId(CONFIG_ID);
		configData.setKbKey(kbKey);

		final UiGroupData csticGroup = new UiGroupData();
		csticGroup.setCstics(csticList);
		csticGroup.setId(ConstantHandler.GENERAL_GROUP_NAME);
		final List<UiGroupData> csticGroups = new ArrayList<>();
		csticGroups.add(csticGroup);
		configData.setGroups(csticGroups);

		return configData;
	}

	protected List<UiGroupData> createEmptyGroup()
	{
		final UiGroupData csticGroup = new UiGroupData();
		csticGroup.setCstics(csticList);
		final List<UiGroupData> csticGroups = new ArrayList<>();
		csticGroups.add(csticGroup);
		return csticGroups;

	}

	protected List<UiGroupData> createCsticsGroup()
	{
		final List<UiGroupData> groups = createEmptyGroup();

		final List<CsticData> cstics = new ArrayList<>();
		final CsticData cstic = new CsticData();
		cstic.setName("numericCstic");
		cstic.setLangdepname("Numeric:");
		cstic.setType(UiType.NUMERIC);
		cstic.setVisible(true);
		cstic.setValue("123");
		cstic.setLastValidValue("123");
		cstic.setKey("root.group.numericCstic");
		cstic.setConflicts(Collections.EMPTY_LIST);
		cstics.add(cstic);

		groups.get(0).setCstics(cstics);

		return groups;
	}

	protected List<CsticData> createCsticsList()
	{
		final List<CsticData> cstics = new ArrayList<>();

		CsticData cstic = new CsticData();
		cstic.setKey("root.WCEM_STRING_SIMPLE");
		cstic.setName("WCEM_STRING_SIMPLE");
		cstic.setLangdepname("Simple String:");
		cstic.setType(UiType.STRING);
		cstic.setVisible(false);
		cstic.setValue(SIMPLE_VALUE);
		cstic.setConflicts(Collections.emptyList());

		List<CsticValueData> domainValues = new ArrayList<>();
		cstic.setDomainvalues(domainValues);
		cstics.add(cstic);

		cstic = new CsticData();
		cstic.setConflicts(Collections.emptyList());
		cstic.setKey("root.WCEM_STRING_RB");
		cstic.setName("WCEM_STRING_RB");
		cstic.setLangdepname("RADIO BUTTON String:");
		cstic.setType(UiType.RADIO_BUTTON);
		cstic.setVisible(true);
		domainValues = new ArrayList<>();
		CsticValueData value = new CsticValueData();
		value.setName("VAL1");
		value.setLangdepname("VALUE 1");
		domainValues.add(value);
		value = new CsticValueData();
		value.setName("VAL2");
		value.setLangdepname("VALUE 2");
		domainValues.add(value);
		cstic.setDomainvalues(domainValues);
		cstics.add(cstic);

		cstic = new CsticData();
		cstic.setConflicts(Collections.emptyList());
		cstic.setKey("root.WCEM_STRING_MULTI");
		cstic.setLangdepname("MULTI String:");
		cstic.setType(UiType.CHECK_BOX_LIST);
		cstic.setVisible(true);
		domainValues = new ArrayList<>();
		value = new CsticValueData();
		value.setName("VAL1");
		value.setLangdepname("VALUE 1");
		domainValues.add(value);
		value = new CsticValueData();
		value.setName("VAL2");
		value.setLangdepname("VALUE 2");
		domainValues.add(value);
		cstic.setDomainvalues(domainValues);
		cstics.add(cstic);

		cstic = new CsticData();
		cstic.setConflicts(Collections.emptyList());
		cstic.setKey("root.WCEM_NUMERIC");
		cstic.setLangdepname("Numeric:");
		cstic.setType(UiType.NUMERIC);
		cstic.setVisible(true);
		cstic.setValue("123");
		cstic.setLastValidValue("123");
		cstics.add(cstic);

		return cstics;
	}

	protected UiGroupData createCsticGroup(final String id, final GroupStatusType status, final boolean collapsed)
	{
		final UiGroupData group = new UiGroupData();
		group.setId(id);
		group.setGroupStatus(status);
		group.setCollapsed(collapsed);
		group.setConfigurable(true);

		return group;
	}

	protected CurrencyData createCurrencyData()
	{
		final CurrencyData data = new CurrencyData();
		data.setIsocode("EUR");
		return data;
	}

}
