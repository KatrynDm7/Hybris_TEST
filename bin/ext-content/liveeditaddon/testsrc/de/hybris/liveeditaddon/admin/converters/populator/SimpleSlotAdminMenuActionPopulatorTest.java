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
import de.hybris.liveeditaddon.admin.ComponentAdminMenuActionData;
import de.hybris.liveeditaddon.admin.SlotActionMenuRequestData;
import de.hybris.liveeditaddon.admin.strategies.ComponentSlotAdminActionEnabledStrategy;
import de.hybris.liveeditaddon.enums.CMSComponentAdminAction;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test suite for {@link de.hybris.liveeditaddon.admin.converters.populator.SimpleSlotAdminMenuActionPopulator}
 */
@UnitTest
public class SimpleSlotAdminMenuActionPopulatorTest
{
	private final static String TEST_ADDON = "testAddon";
	private final static String ACTION_TYPE_NAME = "actionTypeName";
	@Mock
	private EnumerationService enumerationService;
	private final CMSComponentAdminAction cmsComponentAdminAction = CMSComponentAdminAction.SHOW;
	private SimpleSlotAdminMenuActionPopulator populator;
	@Mock
	private ComponentSlotAdminActionEnabledStrategy componentSlotAdminActionEnabledStrategy;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		populator = new SimpleSlotAdminMenuActionPopulator();
		populator.setEnumerationService(enumerationService);
		populator.setComponentSlotAdminActionEnabledStrategy(componentSlotAdminActionEnabledStrategy);
		populator.setAddOn(TEST_ADDON);
		populator.setType(cmsComponentAdminAction);
		given(enumerationService.getEnumerationName(cmsComponentAdminAction)).willReturn(ACTION_TYPE_NAME);
	}

	@Test
	public void testPopulate()
	{
		final SlotActionMenuRequestData sourceSlotActionMenuRequestData = new SlotActionMenuRequestData();
		final ComponentAdminMenuActionData targetAdminMenuActionData = new ComponentAdminMenuActionData();
		given(Boolean.valueOf(componentSlotAdminActionEnabledStrategy.isEnabled(sourceSlotActionMenuRequestData))).willReturn(
				Boolean.TRUE);
		given(
				Boolean.valueOf(componentSlotAdminActionEnabledStrategy.isVisible(sourceSlotActionMenuRequestData,
						Boolean.TRUE.booleanValue()))).willReturn(Boolean.TRUE);

		populator.populate(sourceSlotActionMenuRequestData, targetAdminMenuActionData);

		Assert.assertEquals(targetAdminMenuActionData.getActionType(), cmsComponentAdminAction);
		Assert.assertEquals(targetAdminMenuActionData.getName(), ACTION_TYPE_NAME);
		Assert.assertEquals(targetAdminMenuActionData.getEnabled(), Boolean.TRUE);
		Assert.assertEquals(targetAdminMenuActionData.getVisible(), Boolean.TRUE);
		Assert.assertEquals(targetAdminMenuActionData.getAddon(), TEST_ADDON);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateWithNullSource()
	{
		final ComponentAdminMenuActionData targetAdminMenuActionData = new ComponentAdminMenuActionData();
		populator.populate(null, targetAdminMenuActionData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateWithNullTarget()
	{
		final SlotActionMenuRequestData sourceSlotActionMenuRequestData = new SlotActionMenuRequestData();
		populator.populate(sourceSlotActionMenuRequestData, null);
	}
}
