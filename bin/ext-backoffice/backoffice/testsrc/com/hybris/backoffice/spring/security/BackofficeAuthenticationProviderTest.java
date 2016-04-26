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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.impl.DefaultUserService;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


@UnitTest
public class BackofficeAuthenticationProviderTest
{

	private BackofficeAuthenticationProvider backofficeAuthenticationProvider;
	@Mock
	private DefaultUserService userService;
	@Mock
	private Authentication mockedAuthentication;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		backofficeAuthenticationProvider = new BackofficeAuthenticationProvider()
		{
			@Override
			protected Authentication coreAuthenticate(final Authentication authentication)
			{
				return mockedAuthentication;
			}
		};
		backofficeAuthenticationProvider.setUserService(userService);
	}

	@Test
	public void testAuthenticate()
	{
		// Make sure only employees can login to the backoffice
		Mockito.when(userService.getUserForUID("employee")).thenReturn(new EmployeeModel());
		Mockito.when(userService.getUserForUID("customer")).thenReturn(new CustomerModel());
		Mockito.when(userService.getUserForUID(Mockito.anyString(), Mockito.<Class<? extends UserModel>> any()))
				.thenCallRealMethod();

		final Authentication employeeAuth = Mockito.mock(Authentication.class);
		Mockito.when(employeeAuth.getName()).thenReturn("employee");
		final Authentication customerAuth = Mockito.mock(Authentication.class);
		Mockito.when(customerAuth.getName()).thenReturn("customer");

		// should throw an AuthenticationException
		boolean exceptionCaught = false;
		try
		{
			backofficeAuthenticationProvider.authenticate(customerAuth);
		}
		catch (final AuthenticationException e)
		{
			exceptionCaught = true;
		}
		Assert.assertTrue(exceptionCaught);
	}

	@Test
	public void testEmployeeLoginDisabledExplicity()
	{
		final EmployeeModel employee = Mockito.mock(EmployeeModel.class);

		Mockito.when(userService.getUserForUID("employee")).thenReturn(employee);
		Mockito.when(userService.getUserForUID(Mockito.anyString(), Mockito.<Class<? extends UserModel>> any()))
				.thenCallRealMethod();

		final Authentication employeeAuth = Mockito.mock(Authentication.class);
		Mockito.when(employeeAuth.getName()).thenReturn("employee");


		Mockito.when(employee.getBackOfficeLoginDisabled()).thenReturn(Boolean.FALSE);

		// should not throw an DisabledException, flag is false
		final Authentication authenticate = backofficeAuthenticationProvider.authenticate(employeeAuth);
		Assert.assertNotNull(authenticate);

		// setting flag to true
		Mockito.when(employee.getBackOfficeLoginDisabled()).thenReturn(Boolean.TRUE);


		// should throw an DisabledException
		boolean exceptionCaught = false;
		try
		{
			backofficeAuthenticationProvider.authenticate(employeeAuth);
		}
		catch (final AuthenticationException e)
		{
			exceptionCaught = true;
		}
		Assert.assertTrue(exceptionCaught);
	}

	@Test
	public void testNewEmployeeDisabled()
	{
		// new employees are OOTB disabled
		Mockito.when(userService.getUserForUID("newone")).thenReturn(new EmployeeModel());
		Mockito.when(userService.getUserForUID(Mockito.anyString(), Mockito.<Class<? extends UserModel>> any()))
				.thenCallRealMethod();

		final Authentication employeeAuth = Mockito.mock(Authentication.class);
		Mockito.when(employeeAuth.getName()).thenReturn("newone");

		// should throw an DisabledException
		boolean exceptionCaught = false;
		try
		{
			backofficeAuthenticationProvider.authenticate(employeeAuth);
		}
		catch (final AuthenticationException e)
		{
			exceptionCaught = true;
		}
		Assert.assertTrue(exceptionCaught);
	}

	@Test
	public void testLoginDisabledAdmin()
	{
		final String testLoginDisabledAdmin = "testLoginDisabledAdmin";
		final EmployeeModel admin = createEmployee(testLoginDisabledAdmin);
		assignToAdminGroup(admin);

		// disabled directly
		Mockito.when(admin.getBackOfficeLoginDisabled()).thenReturn(Boolean.TRUE);

		final Authentication authentication = getAuthentication(testLoginDisabledAdmin);
		Assert.assertNotNull(backofficeAuthenticationProvider.authenticate(authentication));
	}

	@Test
	public void testAdminLoginDisabledOnDirectGroup()
	{
		final String testAdminLoginDisabledOnDirectGroup = "testAdminLoginDisabledOnDirectGroup";
		final EmployeeModel admin = createEmployee(testAdminLoginDisabledOnDirectGroup);
		assignToAdminGroup(admin);

		// admin in disabled direct group
		createDirectDisabledGroup(admin);

		// should not throw an DisabledException
		final Authentication authentication = getAuthentication(testAdminLoginDisabledOnDirectGroup);
		Assert.assertNotNull(backofficeAuthenticationProvider.authenticate(authentication));
	}

	@Test
	public void testAdminLoginDisabledOnParentGroup()
	{
		final String testAdminLoginDisabledOnParentGroup = "testAdminLoginDisabledOnParentGroup";
		final EmployeeModel admin = createEmployee(testAdminLoginDisabledOnParentGroup);
		assignToAdminGroup(admin);

		final UserGroupModel directGroup = createDirectGroup(admin);

		createParentDisabledGroup(directGroup);

		final Authentication authentication = getAuthentication(testAdminLoginDisabledOnParentGroup);
		Assert.assertNotNull(backofficeAuthenticationProvider.authenticate(authentication));
	}

	@Test
	public void testEmployeeLoginActiveOnDirectGroup()
	{
		final String employeeLoginActiveOnDirectGroup = "employeeLoginActiveOnDirectGroup";

		final EmployeeModel employee = createEmployee(employeeLoginActiveOnDirectGroup);

		createDirectActiveGroup(employee);

		final Authentication authentication = getAuthentication(employeeLoginActiveOnDirectGroup);
		Assert.assertNotNull(backofficeAuthenticationProvider.authenticate(authentication));
	}

	@Test
	public void testEmployeeLoginActiveOnParentGroup()
	{
		final String employeeLoginActiveOnParentGroup = "employeeLoginActiveOnParentGroup";

		final EmployeeModel employee = createEmployee(employeeLoginActiveOnParentGroup);

		final UserGroupModel directGroup = createDirectGroup(employee);
		createParentActiveGroup(directGroup);

		final Authentication authentication = getAuthentication(employeeLoginActiveOnParentGroup);
		Assert.assertNotNull(backofficeAuthenticationProvider.authenticate(authentication));
	}

	@Test
	public void testEmployeeLoginActiveOnDisabledGroup()
	{
		final String employeeLoginActiveOnDisabledGroup = "employeeLoginActiveOnDisabledGroup";

		final EmployeeModel employee = createEmployee(employeeLoginActiveOnDisabledGroup);

		createDirectDisabledGroup(employee);

		final Authentication authentication = getAuthentication(employeeLoginActiveOnDisabledGroup);

		boolean exceptionCaught = false;
		try
		{
			backofficeAuthenticationProvider.authenticate(authentication);
		}
		catch (final AuthenticationException e)
		{
			exceptionCaught = true;
		}
		Assert.assertTrue(exceptionCaught);
	}

	@Test
	public void testEmployeeLoginDisabledOnParentDisabledGroup()
	{
		final String employeeLoginDisabledOnParentGroup = "employeeLoginDisabledOnParentGroup";

		final EmployeeModel employee = createEmployee(employeeLoginDisabledOnParentGroup);

		final UserGroupModel directGroup = createDirectGroup(employee);
		createParentDisabledGroup(directGroup);

		final Authentication authentication = getAuthentication(employeeLoginDisabledOnParentGroup);
		boolean exceptionCaught = false;
		try
		{
			backofficeAuthenticationProvider.authenticate(authentication);
		}
		catch (final AuthenticationException e)
		{
			exceptionCaught = true;
		}
		Assert.assertTrue(exceptionCaught);
	}

	@Test
	public void testEmployeeLoginDisabledOnNotSetGroups()
	{
		final String employeeLoginDisabledOnParentGroup = "employeeLoginDisabledOnParentGroup";

		final EmployeeModel employee = createEmployee(employeeLoginDisabledOnParentGroup);

		final UserGroupModel directGroup = createDirectGroup(employee);
		createParentGroup(directGroup);

		final Authentication authentication = getAuthentication(employeeLoginDisabledOnParentGroup);
		boolean exceptionCaught = false;
		try
		{
			backofficeAuthenticationProvider.authenticate(authentication);
		}
		catch (final AuthenticationException e)
		{
			exceptionCaught = true;
		}
		Assert.assertTrue(exceptionCaught);
	}

	private void assignToAdminGroup(final EmployeeModel employee)
	{
		Mockito.when(userService.isAdmin(employee)).thenReturn(Boolean.TRUE);
	}

	private void createDirectDisabledGroup(final EmployeeModel employee)
	{
		final UserGroupModel directDisabledGroup = createDirectGroup(employee);
		Mockito.when(directDisabledGroup.getBackOfficeLoginDisabled()).thenReturn(Boolean.TRUE);
	}

	private void createDirectActiveGroup(final EmployeeModel employee)
	{
		final UserGroupModel directActiveGroup = createDirectGroup(employee);
		Mockito.when(directActiveGroup.getBackOfficeLoginDisabled()).thenReturn(Boolean.FALSE);
	}

	private UserGroupModel createDirectGroup(final EmployeeModel employee)
	{
		final Set<PrincipalGroupModel> list = new HashSet<>();
		final UserGroupModel group = Mockito.mock(UserGroupModel.class);
		Mockito.when(group.getBackOfficeLoginDisabled()).thenReturn(null);
		group.setUid("directtestgroup");
		list.add(group);
		Mockito.when(employee.getGroups()).thenReturn(list);
		return group;
	}

	private EmployeeModel createEmployee(final String employeeLoginActiveOnDirectGroup)
	{
		final EmployeeModel employee = Mockito.mock(EmployeeModel.class);
		employee.setUid(employeeLoginActiveOnDirectGroup);
		employee.setName(employeeLoginActiveOnDirectGroup);
		Mockito.when(employee.getBackOfficeLoginDisabled()).thenReturn(null);
		Mockito.when(userService.getUserForUID(employeeLoginActiveOnDirectGroup, EmployeeModel.class)).thenReturn(employee);
		return employee;
	}

	private void createParentDisabledGroup(final UserGroupModel childGroup)
	{
		final UserGroupModel parentGroup = createParentGroup(childGroup);
		Mockito.when(parentGroup.getBackOfficeLoginDisabled()).thenReturn(Boolean.TRUE);
	}

	private void createParentActiveGroup(final UserGroupModel childGroup)
	{
		final UserGroupModel parentGroup = createParentGroup(childGroup);
		Mockito.when(parentGroup.getBackOfficeLoginDisabled()).thenReturn(Boolean.FALSE);
	}

	private UserGroupModel createParentGroup(final UserGroupModel childGroup)
	{
		final UserGroupModel parentGroup = Mockito.mock(UserGroupModel.class);
		Mockito.when(parentGroup.getBackOfficeLoginDisabled()).thenReturn(null);
		parentGroup.setUid("parentestgroup");
		Mockito.when(childGroup.getGroups()).thenReturn(Collections.<PrincipalGroupModel> singleton(parentGroup));
		return parentGroup;
	}

	private Authentication getAuthentication(final String name)
	{
		final Authentication employeeAuth = Mockito.mock(Authentication.class);
		Mockito.when(employeeAuth.getName()).thenReturn(name);
		return employeeAuth;
	}

}
