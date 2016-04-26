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
package de.hybris.liveeditaddon.admin.strategies.impl;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.CMSComponentTypeModel;
import de.hybris.platform.cms2.model.ComponentTypeGroupModel;
import de.hybris.platform.cms2.model.contents.ContentSlotNameModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.liveeditaddon.admin.SlotActionMenuRequestData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test suite for
 * {@link de.hybris.liveeditaddon.admin.strategies.impl.PositionComponentSlotAdminActionEnabledStrategy}
 */
@UnitTest
public class PositionComponentSlotAdminActionEnabledStrategyTest
{
	private final static String TYPE_CODE = "typeCode";
	private final static String PAGE_UID = "pageUid";
	private final static String POSITION = "position";
	private PositionComponentSlotAdminActionEnabledStrategy strategy;
	private Set<CMSComponentTypeModel> validComponentTypes;
	private Set<CMSComponentTypeModel> cmsComponentTypes;
	private List<ContentSlotNameModel> availableContentSlots;
	private SlotActionMenuRequestData slotActionMenuRequestData;
	@Mock
	private CMSPageService cmsPageService;
	@Mock
	private TypeService typeService;
	@Mock
	private ContentSlotNameModel contentSlotNameModel;
	@Mock
	private ComponentTypeGroupModel componentTypeGroupModel;
	@Mock
	private CMSComponentTypeModel cmsComponentTypeModel;
	@Mock
	private CMSComponentTypeModel invalidCMSComponentTypeModel;
	@Mock
	private AbstractPageModel abstractPageModel;
	@Mock
	private PageTemplateModel pageTemplateModel;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		strategy = new PositionComponentSlotAdminActionEnabledStrategy();
		strategy.setCmsPageService(cmsPageService);
		strategy.setTypeService(typeService);
		strategy.setTypeCode(TYPE_CODE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIsValidTypeWithNullParam()
	{
		strategy.isValidType(null);
	}

	@Test
	public void testIsValidType()
	{
		prepareValidType();
		Assert.assertTrue(strategy.isValidType(contentSlotNameModel));
	}

	@Test
	public void testIsNotValidType()
	{
		prepareInvalidType();
		Assert.assertFalse(strategy.isValidType(contentSlotNameModel));
	}

	@Test
	public void testIsEnabled() throws CMSItemNotFoundException
	{
		slotActionMenuRequestData = new SlotActionMenuRequestData();
		slotActionMenuRequestData.setPageUid(PAGE_UID);
		slotActionMenuRequestData.setPosition(POSITION);
		availableContentSlots = new ArrayList<>();
		availableContentSlots.add(contentSlotNameModel);
		given(cmsPageService.getPageForId(PAGE_UID)).willReturn(abstractPageModel);
		given(abstractPageModel.getMasterTemplate()).willReturn(pageTemplateModel);
		given(pageTemplateModel.getAvailableContentSlots()).willReturn(availableContentSlots);
		given(contentSlotNameModel.getName()).willReturn(POSITION);

		prepareValidType();

		Assert.assertTrue(strategy.isEnabled(slotActionMenuRequestData));
	}

	@Test
	public void testIsNotEnabled() throws CMSItemNotFoundException
	{
		slotActionMenuRequestData = new SlotActionMenuRequestData();
		slotActionMenuRequestData.setPageUid(PAGE_UID);
		slotActionMenuRequestData.setPosition(POSITION);
		availableContentSlots = new ArrayList<>();
		availableContentSlots.add(contentSlotNameModel);
		given(cmsPageService.getPageForId(PAGE_UID)).willReturn(abstractPageModel);
		given(abstractPageModel.getMasterTemplate()).willReturn(pageTemplateModel);
		given(pageTemplateModel.getAvailableContentSlots()).willReturn(availableContentSlots);
		given(contentSlotNameModel.getName()).willReturn(POSITION);

		prepareInvalidType();

		Assert.assertFalse(strategy.isEnabled(slotActionMenuRequestData));
	}

	@Test
	public void testIsVisible()
	{
		slotActionMenuRequestData = new SlotActionMenuRequestData();
		strategy.setVisibleIfDisabled(true);
		Assert.assertTrue(strategy.isVisible(slotActionMenuRequestData, false));
	}

	@Test
	public void testIsNotVisible()
	{
		slotActionMenuRequestData = new SlotActionMenuRequestData();
		strategy.setVisibleIfDisabled(false);
		Assert.assertFalse(strategy.isVisible(slotActionMenuRequestData, false));
	}

	private void prepareValidType()
	{
		validComponentTypes = new HashSet<>();
		validComponentTypes.add(cmsComponentTypeModel);
		cmsComponentTypes = new HashSet<>();
		cmsComponentTypes.add(cmsComponentTypeModel);
		given(typeService.getTypeForCode(TYPE_CODE)).willReturn(cmsComponentTypeModel);
		given(contentSlotNameModel.getCompTypeGroup()).willReturn(componentTypeGroupModel);
		given(componentTypeGroupModel.getCmsComponentTypes()).willReturn(cmsComponentTypes);
		given(contentSlotNameModel.getValidComponentTypes()).willReturn(validComponentTypes);	
	}

	private void prepareInvalidType()
	{
		validComponentTypes = new HashSet<>();
		validComponentTypes.add(invalidCMSComponentTypeModel);
		cmsComponentTypes = new HashSet<>();
		cmsComponentTypes.add(invalidCMSComponentTypeModel);
		given(typeService.getTypeForCode(TYPE_CODE)).willReturn(cmsComponentTypeModel);
		given(contentSlotNameModel.getCompTypeGroup()).willReturn(componentTypeGroupModel);
		given(componentTypeGroupModel.getCmsComponentTypes()).willReturn(cmsComponentTypes);
		given(contentSlotNameModel.getValidComponentTypes()).willReturn(validComponentTypes);
	}
}
