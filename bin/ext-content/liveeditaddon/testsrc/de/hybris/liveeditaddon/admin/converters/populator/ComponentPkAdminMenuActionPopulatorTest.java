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
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.core.PK;
import de.hybris.liveeditaddon.admin.ComponentActionMenuRequestData;
import de.hybris.liveeditaddon.admin.ComponentAdminMenuActionData;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test suite for {@link de.hybris.liveeditaddon.admin.converters.populator.ComponentPkAdminMenuActionPopulator}
 */
@UnitTest
public class ComponentPkAdminMenuActionPopulatorTest
{
	private final static String SOURCE_COMPONENT_UID = "testComponentUid";
	private ComponentPkAdminMenuActionPopulator populator;
	private PK pk = PK.BIG_PK;
	@Mock
	private CMSComponentService cmsComponentService;
	@Mock
	private AbstractCMSComponentModel abstractCMSComponentModel;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		populator = new ComponentPkAdminMenuActionPopulator();
		populator.setCmsComponentService(cmsComponentService);
	}

	@Test
	public void testPopulate() throws CMSItemNotFoundException
	{
		final ComponentActionMenuRequestData sourceActionMenuRequestData = new ComponentActionMenuRequestData();
		final ComponentAdminMenuActionData targetAdminMenuActionData = new ComponentAdminMenuActionData();
		sourceActionMenuRequestData.setComponentUid(SOURCE_COMPONENT_UID);
		given(cmsComponentService.getAbstractCMSComponent(SOURCE_COMPONENT_UID)).willReturn(abstractCMSComponentModel);
		given(abstractCMSComponentModel.getPk()).willReturn(pk);

		populator.populate(sourceActionMenuRequestData, targetAdminMenuActionData);

		Assert.assertEquals(targetAdminMenuActionData.getPk(), pk.getLong());
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
		final ComponentActionMenuRequestData sourceActionMenuRequestData = new ComponentActionMenuRequestData();
		populator.populate(sourceActionMenuRequestData, null);
	}
}
