package com.hybris.backoffice.cockpitng.user;


import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


public class BackofficeCockpitUserServiceTest
{
	public static final String ADAM = "Adam";

	private BackofficeCockpitUserService backofficeCockpitUserService;
	private UserService userService;

	@Before
	public void setUp()
	{
		backofficeCockpitUserService = new BackofficeCockpitUserService();
		userService = Mockito.mock(UserService.class);
	}

	@Test
	public void testGetCurrentUserWithSetValue()
	{
		getBackofficeCockpitUserService().setUserService(getUserService());
		final UserModel currentUser = createUserModel();
		Mockito.when(getUserService().getCurrentUser()).thenReturn(currentUser);
		Mockito.when(currentUser.getUid()).thenReturn("Adam");

		Assert.assertEquals(ADAM, getBackofficeCockpitUserService().getCurrentUser());
	}

	@Test
	public void testGetCurrentUserWithoutSetValue()
	{
		getBackofficeCockpitUserService().setUserService(getUserService());
		Mockito.when(getUserService().getCurrentUser()).thenReturn(null);

		Assert.assertEquals(null, getBackofficeCockpitUserService().getCurrentUser());
	}

	@Test
	public void testIsAdmin()
	{
		getBackofficeCockpitUserService().setUserService(getUserService());
		final String userID = "123";
		final UserModel currentUser = createUserModel();
		Mockito.when(getUserService().getUserForUID(userID)).thenReturn(currentUser);
		Mockito.when(getUserService().isAdmin(currentUser)).thenReturn(Boolean.TRUE);

		Assert.assertEquals(Boolean.TRUE, getBackofficeCockpitUserService().isAdmin(userID));
	}

	@Test
	public void testSetCurrentUser(){

		getBackofficeCockpitUserService().setUserService(getUserService());
		final String userID = "123";

		final UserModel currentUser = createUserModel();
		Mockito.when(getUserService().getUserForUID(userID)).thenReturn(currentUser);

		getBackofficeCockpitUserService().setCurrentUser(userID);
	}


	public UserModel createUserModel()
	{
		return Mockito.mock(UserModel.class);
	}


	public BackofficeCockpitUserService getBackofficeCockpitUserService()
	{
		return backofficeCockpitUserService;
	}

	public UserService getUserService()
	{
		return userService;
	}


}
