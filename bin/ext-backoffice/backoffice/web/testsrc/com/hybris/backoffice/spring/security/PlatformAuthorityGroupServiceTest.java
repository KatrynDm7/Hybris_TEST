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
 */
package com.hybris.backoffice.spring.security;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.cockpitng.core.user.impl.DefaultPlatformAuthorityGroupService;
import com.hybris.backoffice.daos.BackofficeRoleDao;
import com.hybris.backoffice.model.user.BackofficeRoleModel;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.core.user.impl.AuthorityGroup;
import com.hybris.cockpitng.util.CockpitSessionService;


@RunWith(MockitoJUnitRunner.class)
public class PlatformAuthorityGroupServiceTest
{
	private DefaultPlatformAuthorityGroupService authorityGroupService;
	@Mock
	private CockpitSessionService cockpitSessionService;
	@Mock
	private CockpitUserService cockpitUserService;
	@Mock
	private UserService userService;
	@Mock
	private BackofficeRoleDao backofficeRoleDao;

	@Before
	public void setUp()
	{
		authorityGroupService = new DefaultPlatformAuthorityGroupService();
		authorityGroupService.setUserService(userService);
		authorityGroupService.setCockpitUserService(cockpitUserService);
		authorityGroupService.setCockpitSessionService(cockpitSessionService);
		authorityGroupService.setBackofficeRoleDao(backofficeRoleDao);
	}

	@Test
	public void testGetActiveAuthorityGroupForUser()
	{

		final AuthorityGroup simpleGroup = Mockito.mock(AuthorityGroup.class);
		Mockito.when(simpleGroup.getAuthorities()).thenReturn(Collections.singletonList("role_simple"));
		Mockito.when(simpleGroup.getName()).thenReturn("simple");

		final AuthorityGroup advancedGroup = Mockito.mock(AuthorityGroup.class);
		Mockito.when(advancedGroup.getAuthorities()).thenReturn(Collections.singletonList("role_advanced"));
		Mockito.when(advancedGroup.getName()).thenReturn("advanced");

		Mockito.when(cockpitUserService.getCurrentUser()).thenReturn("simple");
		Mockito.when(cockpitSessionService.getAttribute("cockpitActiveAuthorityGroup")).thenReturn(simpleGroup);
		final AuthorityGroup group1 = authorityGroupService.getActiveAuthorityGroupForUser(simpleGroup.getName());
		Assert.assertTrue("role_simple".equals(group1.getAuthorities().get(0)));

		// Whenever the userNames are not the same null should be return
		Mockito.when(cockpitSessionService.getAttribute("cockpitActiveAuthorityGroup")).thenReturn(advancedGroup);
		final AuthorityGroup group2 = authorityGroupService.getActiveAuthorityGroupForUser(advancedGroup.getName());
		Assert.assertNull(group2);

	}

	@Test
	public void testGetAllAuthorityGroups()
	{
		final AuthorityGroup fullGroup = Mockito.mock(AuthorityGroup.class);
		Mockito.when(fullGroup.getAuthorities()).thenReturn(Arrays.asList("role_simple", "role_advanced"));
		Mockito.when(fullGroup.getName()).thenReturn("full");

		final Set<BackofficeRoleModel> backOfficeRoles = new LinkedHashSet<BackofficeRoleModel>();
		final BackofficeRoleModel roleSimple = Mockito.mock(BackofficeRoleModel.class);
		Mockito.when(roleSimple.getUid()).thenReturn("role_simple");
		final BackofficeRoleModel roleAdvanced = Mockito.mock(BackofficeRoleModel.class);
		Mockito.when(roleAdvanced.getUid()).thenReturn("role_advanced");

		backOfficeRoles.add(roleSimple);
		backOfficeRoles.add(roleAdvanced);

		Mockito.when(backofficeRoleDao.findAllBackofficeRoles()).thenReturn(backOfficeRoles);

		final List<AuthorityGroup> allGroups = authorityGroupService.getAllAuthorityGroups();
		Assert.assertEquals(backOfficeRoles.size(), allGroups.size());

		for (int i = 0; i < allGroups.size(); i++)
		{
			final String authorityCode = allGroups.get(i).getCode();
			final String expectedAuthorityCode = fullGroup.getAuthorities().get(i);
			Assert.assertTrue(expectedAuthorityCode.equals(authorityCode));
		}
	}

	@Test
	public void testGetAllAuthorityGroupsForUser()
	{
		final AuthorityGroup fullGroup = Mockito.mock(AuthorityGroup.class);
		Mockito.when(fullGroup.getAuthorities()).thenReturn(Arrays.asList("role_simple", "role_advanced"));
		Mockito.when(fullGroup.getCode()).thenReturn("full");

		final BackofficeRoleModel roleSimple = Mockito.mock(BackofficeRoleModel.class);
		Mockito.when(roleSimple.getUid()).thenReturn("role_simple");
		final BackofficeRoleModel roleAdvanced = Mockito.mock(BackofficeRoleModel.class);
		Mockito.when(roleAdvanced.getUid()).thenReturn("role_advanced");

		final Set<BackofficeRoleModel> backOfficeRoles = new HashSet();
		backOfficeRoles.add(roleAdvanced);
		backOfficeRoles.add(roleSimple);
		final UserModel userModel = new UserModel();
		userModel.setUid("full");

		Mockito.when(userService.getUserForUID("full")).thenReturn(userModel);
		Mockito.when(userService.getAllUserGroupsForUser(userModel, BackofficeRoleModel.class)).thenReturn(backOfficeRoles);

		final List<AuthorityGroup> allGroups = authorityGroupService.getAllAuthorityGroupsForUser(fullGroup.getCode());
		Assert.assertEquals(backOfficeRoles.size(), allGroups.size());

		final List<String> roleUids = new ArrayList<String>();
		for (final BackofficeRoleModel role : new ArrayList<BackofficeRoleModel>(backOfficeRoles))
		{
			roleUids.add(role.getUid());
		}

		final List<String> authoritiesCode = new ArrayList<String>();
		for (final AuthorityGroup authorityGroup : allGroups)
		{
			authoritiesCode.add(authorityGroup.getCode());
		}

		Assert.assertTrue(authoritiesCode.containsAll(roleUids));

	}

	@Test
	public void testGetAuthorityGroup()
	{

		final BackofficeRoleModel roleModel = Mockito.mock(BackofficeRoleModel.class);
		Mockito.when(roleModel.getUid()).thenReturn("role_advanced");
		Mockito.when(roleModel.getDescription()).thenReturn("This is an advanced user");
		Mockito.when(roleModel.getDisplayName()).thenReturn("this is loc name");

		Mockito.when(userService.getUserGroupForUID("role_advanced", BackofficeRoleModel.class)).thenReturn(roleModel);
		final AuthorityGroup group1 = authorityGroupService.getAuthorityGroup(roleModel.getUid());

		Assert.assertTrue("role_advanced".equals(group1.getCode()));
	}

}
