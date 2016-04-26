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
package de.hybris.liveeditaddon.admin.converters.populator;


import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.liveeditaddon.admin.ComponentAdminMenuActionData;
import de.hybris.liveeditaddon.admin.ComponentAdminMenuGroupData;
import de.hybris.liveeditaddon.admin.ComponentAdminMenuItemData;
import de.hybris.liveeditaddon.admin.SlotActionMenuRequestData;
import de.hybris.liveeditaddon.enums.CMSComponentAdminAction;
import de.hybris.liveeditaddon.enums.CMSComponentAdminActionGroup;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test suite for {@link de.hybris.liveeditaddon.admin.converters.populator.SlotAdminMenuGroupPopulator}
 */
@UnitTest
public class SlotAdminMenuGroupPopulatorTest
{
	private final static String ROOT_ACTION_GROUP_NAME = "rootActionGroupName";
	private final static Boolean CONVERTED_ADMIN_MENU_ITEM_VISIBLE = Boolean.TRUE;
	private final static Boolean CONVERTED_ADMIN_MENU_ITEM_ENABLED = Boolean.TRUE;
	private SlotAdminMenuGroupPopulator populator;
	private CMSComponentAdminActionGroup rootCMSComponentAdminActionGroup = CMSComponentAdminActionGroup.ROOT;
	private CMSComponentAdminAction cmsComponentAdminAction = CMSComponentAdminAction.CLONE;
	private ComponentAdminMenuActionData convertedAdminMenuActionData;
	private List<Converter<SlotActionMenuRequestData, ComponentAdminMenuItemData>> menuItemConverters;
	@Mock
	private EnumerationService enumerationService;
	@Mock
	private Converter<SlotActionMenuRequestData, ComponentAdminMenuItemData> menuItemConverter;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		populator = new SlotAdminMenuGroupPopulator();
		populator.setType(rootCMSComponentAdminActionGroup);
		populator.setEnumerationService(enumerationService);
		menuItemConverters = new ArrayList<>();
		populator.setMenuItemConverters(menuItemConverters);
	}

	@Test
	public void testPopulate()
	{
		final SlotActionMenuRequestData sourceSlotActionMenuRequestData = new SlotActionMenuRequestData();
		final ComponentAdminMenuGroupData targetAdminMenuGroupData = new ComponentAdminMenuGroupData();
		convertedAdminMenuActionData = new ComponentAdminMenuActionData();
		convertedAdminMenuActionData.setVisible(CONVERTED_ADMIN_MENU_ITEM_VISIBLE);
		convertedAdminMenuActionData.setEnabled(CONVERTED_ADMIN_MENU_ITEM_ENABLED);
		convertedAdminMenuActionData.setActionType(cmsComponentAdminAction);
		menuItemConverters.add(menuItemConverter);
		given(menuItemConverter.convert(sourceSlotActionMenuRequestData)).willReturn(convertedAdminMenuActionData);
		given(enumerationService.getEnumerationName(rootCMSComponentAdminActionGroup)).willReturn(ROOT_ACTION_GROUP_NAME);

		populator.populate(sourceSlotActionMenuRequestData, targetAdminMenuGroupData);

		Assert.assertEquals(targetAdminMenuGroupData.getActionGroupType(), rootCMSComponentAdminActionGroup);
		Assert.assertEquals(targetAdminMenuGroupData.getName(), ROOT_ACTION_GROUP_NAME);
		Assert.assertEquals(targetAdminMenuGroupData.getId(), rootCMSComponentAdminActionGroup.getCode());
		Assert.assertTrue(targetAdminMenuGroupData.getItems().contains(convertedAdminMenuActionData));
		Assert.assertEquals(targetAdminMenuGroupData.getItems().get(0).getEnabled(), CONVERTED_ADMIN_MENU_ITEM_ENABLED);
		Assert.assertEquals(targetAdminMenuGroupData.getItems().get(0).getId(), rootCMSComponentAdminActionGroup.getCode() + "_"
				+ cmsComponentAdminAction.getCode());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateWithNullSource()
	{
		final ComponentAdminMenuGroupData targetAdminMenuGroupData = new ComponentAdminMenuGroupData();
		populator.populate(null, targetAdminMenuGroupData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateWithNullTarget()
	{
		final SlotActionMenuRequestData sourceSlotActionMenuRequestData = new SlotActionMenuRequestData();
		populator.populate(sourceSlotActionMenuRequestData, null);
	}
}
