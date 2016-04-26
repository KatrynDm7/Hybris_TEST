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
package de.hybris.platform.sap.productconfig.frontend.util.impl;


import static junit.framework.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.GroupStatusType;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.frontend.UiCsticStatus;
import de.hybris.platform.sap.productconfig.frontend.UiGroupStatus;
import de.hybris.platform.sap.productconfig.frontend.UiStatus;
import de.hybris.platform.sap.productconfig.frontend.controllers.AbstractProductConfigControllerTest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class UiStatusSyncTest extends AbstractProductConfigControllerTest
{

	private UiStatusSync uiStatusSync;

	@Before
	public void setup()
	{
		uiStatusSync = new UiStatusSync();
	}

	@Test
	public void testUiStatusUpdate()
	{
		final ConfigurationData configData = createConfigurationData();

		List<UiGroupData> groups = new ArrayList<>();
		final UiGroupData uiGroupData = createCsticGroup("1", GroupStatusType.ERROR, true);
		final List<CsticData> cstics = new ArrayList<>();
		final CsticData cstic = new CsticData();
		cstic.setKey("ABC");
		cstic.setLongText("lorem ipsum");
		cstic.setShowFullLongText(false);
		cstics.add(cstic);
		uiGroupData.setCstics(cstics);
		groups.add(uiGroupData);

		groups.add(createCsticGroup("2", GroupStatusType.WARNING, true));
		groups.add(createCsticGroup("3", GroupStatusType.DEFAULT, true));
		configData.setGroups(groups);

		configData.setSpecificationTreeCollapsed(false);
		configData.setPriceSummaryCollapsed(true);

		final UiStatus uiStatus = new UiStatus();
		final List<UiGroupStatus> uiGroups = new ArrayList<>();
		final UiGroupStatus uiGroupStatus = createUiGroup("1", false);

		final List<UiCsticStatus> csticsStatus = new ArrayList<>();
		final UiCsticStatus csticStatus = new UiCsticStatus();
		csticStatus.setId(cstics.get(0).getKey());
		csticStatus.setShowFullLongText(true);
		csticsStatus.add(csticStatus);
		uiGroupStatus.setCstics(csticsStatus);

		uiGroups.add(uiGroupStatus);
		uiGroups.add(createUiGroup("2", true));
		uiGroups.add(createUiGroup("3", true));

		uiStatus.setGroups(uiGroups);
		uiStatus.setPriceSummaryCollapsed(false);
		uiStatus.setSpecificationTreeCollapsed(true);

		uiStatusSync.updateUiStatus(configData, uiStatus);

		groups = configData.getGroups();
		assertEquals(uiGroups.get(0).isCollapsed(), groups.get(0).isCollapsed());
		assertEquals(uiGroups.get(1).isCollapsed(), groups.get(1).isCollapsed());
		assertEquals(uiGroups.get(2).isCollapsed(), groups.get(2).isCollapsed());

		assertEquals(uiStatus.isPriceSummaryCollapsed(), configData.isPriceSummaryCollapsed());
		assertEquals(uiStatus.isSpecificationTreeCollapsed(), configData.isSpecificationTreeCollapsed());

		assertEquals(uiGroupStatus.getCstics().get(0).isShowFullLongText(), groups.get(0).getCstics().get(0).isShowFullLongText());
	}

	@Test
	public void testStatusInitialUiGroupStatus()
	{
		final ConfigurationData configData = createConfigurationData();

		List<UiGroupData> groups = new ArrayList<>();
		groups.add(createCsticGroup("1", GroupStatusType.ERROR, true));
		groups.add(createCsticGroup("2", GroupStatusType.WARNING, true));
		groups.add(createCsticGroup("3", GroupStatusType.DEFAULT, true));
		configData.setGroups(groups);

		uiStatusSync.setInitialStatus(configData);

		groups = configData.getGroups();
		assertEquals(false, groups.get(0).isCollapsed());
		assertEquals(true, groups.get(1).isCollapsed());
		assertEquals(true, groups.get(2).isCollapsed());

		assertEquals(false, configData.isPriceSummaryCollapsed());
		assertEquals(false, configData.isSpecificationTreeCollapsed());
	}


	@Test
	public void testStatusInitialUiGroupStatus_nonConfigurable()
	{
		final ConfigurationData configData = createConfigurationData();

		List<UiGroupData> groups = new ArrayList<>();
		final UiGroupData csticGroup = createCsticGroup("1", GroupStatusType.ERROR, true);
		csticGroup.setConfigurable(false);
		csticGroup.setCstics(createCsticsList());
		groups.add(csticGroup);
		groups.add(createCsticGroup("2", GroupStatusType.WARNING, true));
		groups.add(createCsticGroup("3", GroupStatusType.DEFAULT, true));
		configData.setGroups(groups);

		uiStatusSync.setInitialStatus(configData);

		groups = configData.getGroups();
		assertEquals(true, groups.get(0).isCollapsed());
		assertEquals(false, groups.get(0).getCstics().get(0).isShowFullLongText());
		assertEquals(false, groups.get(1).isCollapsed());
		assertEquals(true, groups.get(2).isCollapsed());

		assertEquals(false, configData.isPriceSummaryCollapsed());
		assertEquals(false, configData.isSpecificationTreeCollapsed());
	}


	@Test
	public void testSetUiStatus()
	{
		final ConfigurationData configData = createConfigurationData();

		final List<UiGroupData> groups = new ArrayList<>();
		groups.add(createCsticGroup("1", GroupStatusType.ERROR, true));
		groups.add(createCsticGroup("2", GroupStatusType.WARNING, true));
		groups.add(createCsticGroup("3", GroupStatusType.DEFAULT, false));
		configData.setGroups(groups);

		configData.setSpecificationTreeCollapsed(false);
		configData.setPriceSummaryCollapsed(true);

		final UiStatus uiStatus = uiStatusSync.storeUiStatusInSession(configData);

		final List<UiGroupStatus> uiGroupsStatus = uiStatus.getGroups();

		assertEquals(true, uiGroupsStatus.get(0).isCollapsed());
		assertEquals(true, uiGroupsStatus.get(1).isCollapsed());
		assertEquals(false, uiGroupsStatus.get(2).isCollapsed());

		assertEquals(true, uiStatus.isPriceSummaryCollapsed());
		assertEquals(false, uiStatus.isSpecificationTreeCollapsed());
	}

	@Test
	public void testSetLongTextStatus()
	{
		final ConfigurationData configData = createConfigurationData();

		final UiGroupData uiGroup = createCsticGroup("1", GroupStatusType.DEFAULT, true);
		final List<CsticData> cstics = createCsticsList();

		final CsticData cstic = cstics.get(0);
		cstic.setLongText("lorem ipsum");
		cstic.setShowFullLongText(true);
		uiGroup.setCstics(cstics);
		final List<UiGroupData> groups = new ArrayList<>();
		groups.add(uiGroup);
		configData.setGroups(groups);

		final UiStatus uiStatus = uiStatusSync.storeUiStatusInSession(configData);
		final List<UiGroupStatus> uiGroupsStatus = uiStatus.getGroups();
		final List<UiCsticStatus> uiCsticStatus = uiGroupsStatus.get(0).getCstics();

		assertEquals(cstics.size(), uiCsticStatus.size());


	}

	private UiGroupStatus createUiGroup(final String id, final boolean collapsed)
	{
		final UiGroupStatus uiGroup = new UiGroupStatus();

		uiGroup.setId(id);
		uiGroup.setCollapsed(collapsed);

		return uiGroup;
	}

}
