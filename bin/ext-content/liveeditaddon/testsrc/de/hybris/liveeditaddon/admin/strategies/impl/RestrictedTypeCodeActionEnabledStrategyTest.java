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
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.liveeditaddon.admin.ComponentActionMenuRequestData;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test suite for {@link de.hybris.liveeditaddon.admin.strategies.impl.RestrictedTypeCodeActionEnabledStrategy}
 */
@UnitTest
public class RestrictedTypeCodeActionEnabledStrategyTest
{
	private final static String ENABLED_TYPE_CODE = "typeCode";
	private final static String COMPONENT_UID = "componentUid";
	private RestrictedTypeCodeActionEnabledStrategy strategy;
	private List<String> typeCodes;
	private ComponentActionMenuRequestData componentActionMenuRequestData;
	@Mock
	private CMSComponentService cmsComponentService;
	@Mock
	private TypeService typeService;
	@Mock
	private ComposedTypeModel composedTypeModel;
	@Mock
	private ComposedTypeModel anotherComposedTypeModel;
	@Mock
	private AbstractCMSComponentModel abstractCMSComponentModel;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		strategy = new RestrictedTypeCodeActionEnabledStrategy() {
			protected boolean isInitialized()
			{
				return true;
			}
		};
		typeCodes = new ArrayList<>();
		typeCodes.add(ENABLED_TYPE_CODE);
		strategy.setCmsComponentService(cmsComponentService);
		strategy.setTypeService(typeService);
		strategy.setTypeCodes(typeCodes);
	}

	@Test
	public void testIsEnabled() throws CMSItemNotFoundException
	{
		componentActionMenuRequestData = new ComponentActionMenuRequestData();
		componentActionMenuRequestData.setComponentUid(COMPONENT_UID);
		given(typeService.getComposedTypeForCode(ENABLED_TYPE_CODE)).willReturn(composedTypeModel);
		given(cmsComponentService.getAbstractCMSComponent(COMPONENT_UID)).willReturn(abstractCMSComponentModel);
		given(typeService.getComposedTypeForClass(abstractCMSComponentModel.getClass())).willReturn(composedTypeModel);

		Assert.assertTrue(strategy.isEnabled(componentActionMenuRequestData));
	}

	@Test
	public void testIsNotEnabled() throws CMSItemNotFoundException
	{
		componentActionMenuRequestData = new ComponentActionMenuRequestData();
		componentActionMenuRequestData.setComponentUid(COMPONENT_UID);
		given(typeService.getComposedTypeForCode(ENABLED_TYPE_CODE)).willReturn(composedTypeModel);
		given(cmsComponentService.getAbstractCMSComponent(COMPONENT_UID)).willReturn(abstractCMSComponentModel);
		given(typeService.getComposedTypeForClass(abstractCMSComponentModel.getClass())).willReturn(anotherComposedTypeModel);

		Assert.assertFalse(strategy.isEnabled(componentActionMenuRequestData));
	}

	@Test
	public void testIsVisible()
	{
		componentActionMenuRequestData = new ComponentActionMenuRequestData();
		strategy.setVisibleIfDisabled(true);
		Assert.assertTrue(strategy.isVisible(componentActionMenuRequestData, false));
	}

	@Test
	public void testIsNotVisible()
	{
		componentActionMenuRequestData = new ComponentActionMenuRequestData();
		strategy.setVisibleIfDisabled(false);
		Assert.assertFalse(strategy.isVisible(componentActionMenuRequestData, false));
	}

}
