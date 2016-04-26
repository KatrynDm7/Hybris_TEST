/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.servicelayer.user;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.SearchRestrictionModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.AfterSessionUserChangeEvent;
import de.hybris.platform.servicelayer.exceptions.ClassMismatchException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.exceptions.CannotDecodePasswordException;
import de.hybris.platform.servicelayer.user.exceptions.PasswordEncoderNotFoundException;
import de.hybris.platform.servicelayer.user.interceptors.ModifySystemUsersInterceptor;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.context.ApplicationListener;


@IntegrationTest
public class UserServiceTest extends ServicelayerBaseTest
{
	@Resource
	private UserService userService;

	@Resource
	private ModelService modelService;

	@Resource
	private TypeService typeService;

	@Resource
	private EventService eventService;

	@Before
	public void setUp() throws Exception
	{
		final User jaloUser = UserManager.getInstance().getAnonymousCustomer();
		assertTrue("Groups is not empty (Jalo)", jaloUser.getGroups().isEmpty());

		final CoreBasicDataCreator creator = new CoreBasicDataCreator();
		creator.createBasicC2L();
		creator.createBasicUserGroups();

		assertFalse("Groups is empty (Jalo)", jaloUser.getGroups().isEmpty());
		final UserGroup userGroup = UserManager.getInstance().getUserGroupByGroupID("customergroup");
		assertNotNull(userGroup);
		assertTrue(jaloUser.isMemberOf(userGroup));
	}

	@Test
	public void testGetUserForUIDFails()
	{
		//null test
		try
		{
			userService.getUserForUID(null);
			fail("exception was expected but didn't happen");
		}
		catch (final IllegalArgumentException e) //NOPMD
		{
			//ok here
		}
		catch (final Exception e)
		{
			fail("unexpected exception");
		}

		//empty test
		try
		{
			userService.getUserForUID("");
			fail("exception was expected but didn't happen");
		}
		catch (final UnknownIdentifierException e) //NOPMD
		{
			// ok here
		}
		catch (final Exception e)
		{
			fail("unexpected exception");
		}

		//unknown test
		try
		{
			userService.getUserForUID("sgagvgaw2kw1okskvfs");
			fail("exception was expected but didn't happen");
		}
		catch (final UnknownIdentifierException e) //NOPMD
		{
			//ok here
		}
		catch (final Exception e)
		{
			fail("unexpected exception");
		}
	}

	@Test
	public void testSetAndGetCurrentUser()
	{
		final UserModel admin = userService.getAdminUser();

		userService.setCurrentUser(admin);

		final UserModel gotUser = userService.getCurrentUser();

		assertEquals("no admin user", admin, gotUser);
	}

	@Test
	public void testGetCurrentUser()
	{
		final UserModel user = userService.getAnonymousUser();

		// set user in jalo session
		JaloSession.getCurrentSession().setUser((User) modelService.getSource(user));

		// get current from user service
		final UserModel actual = userService.getCurrentUser();

		assertNotNull("Current user is null.", actual);
		assertEquals("Current user differs.", user, actual);
	}

	@Test
	public void testSetCurrentUser()
	{
		JaloSession.getCurrentSession().setUser(UserManager.getInstance().getAdminEmployee());

		// setup final application listener for AfterUserChangedEvent
		final ApplicationListener<AfterSessionUserChangeEvent> userChangeListener = Mockito.mock(ApplicationListener.class);
		Mockito.doNothing().when(userChangeListener).onApplicationEvent(Mockito.any(AfterSessionUserChangeEvent.class));
		try
		{
			eventService.registerEventListener(userChangeListener);

			// get a user reference
			final UserModel user = userService.getAnonymousUser();

			// set current user
			userService.setCurrentUser(user);

			// user set on jalo session?
			final UserModel actual = modelService.get(JaloSession.getCurrentSession().getUser());

			assertNotNull("Current user is null.", actual);
			assertEquals("Current user differs.", user, actual);

			// event received?
			Mockito.verify(userChangeListener, Mockito.times(1)).onApplicationEvent(
					Mockito.argThat(new ArgumentMatcher<AfterSessionUserChangeEvent>()
					{

						@Override
						public boolean matches(final Object argument)
						{
							if (argument instanceof AfterSessionUserChangeEvent)
							{
								final AfterSessionUserChangeEvent event = (AfterSessionUserChangeEvent) argument;
								return event.getSource().equals(JaloSession.getCurrentSession());
							}
							return false;
						}
					}));
		}
		finally
		{
			eventService.unregisterEventListener(userChangeListener);
		}
	}

	@Test
	public void testGetUserForUIDWithClass()
	{
		try
		{
			userService.getUserForUID("anonymous", EmployeeModel.class);
			fail();
		}
		catch (final ClassMismatchException e) //NOPMD
		{
			// ok
		}

		try
		{
			userService.getUserForUID("admin", CustomerModel.class);
			fail();
		}
		catch (final ClassMismatchException e) //NOPMD
		{
			// ok
		}

		final EmployeeModel admin1 = userService.getUserForUID("admin", EmployeeModel.class);
		final CustomerModel anon2 = userService.getUserForUID("anonymous", CustomerModel.class);

		final UserModel anon3 = userService.getUserForUID("anonymous", UserModel.class);
		final UserModel admin3 = userService.getUserForUID("admin", UserModel.class);

		assertNotNull(admin1);
		assertEquals(EmployeeModel.class, admin1.getClass());

		assertNotNull(anon2);
		assertEquals(CustomerModel.class, anon2.getClass());

		assertNotNull(anon3);
		assertNotNull(admin3);
		assertEquals(CustomerModel.class, anon3.getClass());
		assertEquals(EmployeeModel.class, admin3.getClass());
	}

	@Test
	public void testGetUserForUID()
	{
		final UserModel user = userService.getUserForUID("anonymous");
		assertNotNull("User", user);
		assertEquals("Login", "anonymous", user.getUid());
	}

	@Test
	public void testGetAllUserGroupsForUser()
	{
		final User jaloUser = UserManager.getInstance().getAnonymousCustomer();

		final UserModel user = userService.getUserForUID("anonymous");

		assertEquals(jaloUser.getPK(), user.getPk());
		assertNotNull("User", user);

		final Collection<UserGroupModel> groups = userService.getAllUserGroupsForUser(user);
		assertNotNull("Groups", groups);

		assertFalse(modelService.isModified(user));
		assertTrue(ModelContextUtils.getItemModelContext(user).isLoaded(UserModel.GROUPS));
		assertFalse(ModelContextUtils.getItemModelContext(user).isDirty(UserModel.GROUPS));

		final Set<PrincipalGroupModel> groupsDirect = user.getGroups();
		assertTrue(groups.containsAll(groupsDirect));

		final UserGroup userGroup = UserManager.getInstance().getUserGroupByGroupID("customergroup");
		assertNotNull(userGroup);
		assertTrue(jaloUser.isMemberOf(userGroup));
		assertTrue(jaloUser.getGroups().contains(userGroup));
		assertFalse("Groups is empty(Jalo)", jaloUser.getGroups().isEmpty());
		assertFalse("Groups is empty", groups.isEmpty());
	}

	@Test
	public void testModifySystemUsersInterceptorForRemove()
	{
		final UserModel anon = userService.getAnonymousUser();
		final UserModel admin = userService.getAdminUser();
		final UserGroupModel admingroup = userService.getAdminUserGroup();

		removePrincipal(anon);
		removePrincipal(admin);
		removePrincipal(admingroup);

		assertNotNull(userService.getAnonymousUser());
		assertNotNull(userService.getAdminUser());
		assertNotNull(userService.getAdminUserGroup());
	}



	@Test
	public void testModifySystemUsersInterceptorForModify()
	{
		final UserModel anon = userService.getAnonymousUser();
		final UserModel admin = userService.getAdminUser();
		final UserGroupModel admingroup = userService.getAdminUserGroup();

		modifyPrincipal(anon);
		modifyPrincipal(admin);
		modifyPrincipal(admingroup);

		assertEquals("anonymous id was changed", "anonymous", userService.getAnonymousUser().getUid());
		assertEquals("admin id was changed", "admin", userService.getAdminUser().getUid());
		assertEquals("admingroup id was changed", "admingroup", userService.getAdminUserGroup().getUid());
	}

	/*
	 * this method tries to remove the given principal which should fail!
	 */
	private void removePrincipal(final PrincipalModel principal)
	{
		try
		{
			modelService.remove(principal);
			fail("there should be a ModelSavingException!");
		}
		catch (final ModelRemovalException e)
		{
			assertTrue("The cause was not an InterceptorException!", e.getCause() instanceof InterceptorException);
			final InterceptorException ie = (InterceptorException) e.getCause();
			assertTrue(ie.getInterceptor() instanceof ModifySystemUsersInterceptor);
			assertTrue("You received a message " + e.getCause().getMessage()
					+ "\n, but it should contain 'It is not allowed to remove the system account: !' ", e.getCause().getMessage()
					.contains("It is not allowed to remove the system account: "));
		}
		catch (final Exception e)
		{
			fail("Got exception different as ModelSavingException");
		}
	}

	/*
	 * this method tries to modify the uid for the given principal and save this. This should fail!
	 */
	private void modifyPrincipal(final PrincipalModel principal)
	{
		try
		{
			principal.setUid("xxx");
			modelService.save(principal);
			fail("there should be a ModelSavingException!");
		}
		catch (final ModelSavingException e)
		{
			assertTrue("The cause was not an InterceptorException!", e.getCause() instanceof InterceptorException);
			final InterceptorException ie = (InterceptorException) e.getCause();
			assertTrue(ie.getInterceptor() instanceof ModifySystemUsersInterceptor);
			assertTrue("You received a message " + e.getCause().getMessage()
					+ "\n, but it should contain 'It is not allowed to modify the UID for the system account: ' ", e.getCause()
					.getMessage().contains("It is not allowed to modify the UID for the system account: "));
		}
		catch (final Exception e)
		{
			fail("Got exception different as ModelSavingException");

		}
		finally
		{
			modelService.refresh(principal);
		}
	}

	@Test
	public void testGetSystemUsersWithRestrictedUser()
	{
		final CustomerModel user = modelService.create(CustomerModel.class);
		user.setUid("xxx");
		modelService.save(user);

		final SearchRestrictionModel searchRestriction = modelService.create(SearchRestrictionModel.class);
		searchRestriction.setActive(Boolean.TRUE);
		searchRestriction.setGenerate(Boolean.TRUE);
		searchRestriction.setCode("test_restriction");
		searchRestriction.setPrincipal(user);
		searchRestriction.setQuery("{" + PrincipalModel.UID + "} NOT IN ( 'anonymous', 'admin', 'admingroup' )");
		searchRestriction.setRestrictedType(typeService.getComposedTypeForClass(PrincipalModel.class));
		modelService.save(searchRestriction);

		userService.setCurrentUser(user);
		assertNotNull(userService.getAnonymousUser());
		assertNotNull(userService.getAdminUser());
		assertNotNull(userService.getAdminUserGroup());

		modelService.remove(searchRestriction); //just in case...
	}

	@Test
	public void testGetTitleForCode() throws Exception
	{
		final TitleModel title = modelService.create(TitleModel.class);
		title.setCode("chef");
		title.setName("Cheffe");

		modelService.save(title);

		final TitleModel current = userService.getTitleForCode(title.getCode());
		assertNotNull("Title is null.", current);
		assertEquals("Title differs.", title, current);
	}

	@Test
	public void testGetUserGroupForUID() throws Exception
	{
		final UserGroupModel group = modelService.create(UserGroupModel.class);
		group.setUid("Testgroup");
		group.setName("Testgroup");

		modelService.save(group);

		final UserGroupModel current = userService.getUserGroupForUID(group.getUid());
		assertNotNull("Group is null.", current);
		assertEquals("Group differs.", group, current);
	}

	@Test
	public void testGetPasswordWithMD5Encoding() throws ConsistencyCheckException
	{
		final EmployeeModel testUser = modelService.create(EmployeeModel.class);
		testUser.setUid("testUser");
		modelService.save(testUser);
		userService.setPassword(testUser.getUid(), "pwd");
		assertEquals("pwd", userService.getPassword(testUser.getUid()));

		try
		{
			final User axel = UserManager.getInstance().createEmployee("axel");
			axel.setName("Axel");
			axel.setPassword("axel", "md5");
			userService.getPassword(axel.getUID());
			fail("expected " + CannotDecodePasswordException.class.getName());
		}
		catch (final CannotDecodePasswordException e) //NOPMD
		{
			//fine!
		}
	}

	@Test
	public void testSetPasswordWithUnknownEncoding()
	{

		final EmployeeModel axel = modelService.create(EmployeeModel.class);
		axel.setUid("axel2");
		axel.setName("Axel2");
		modelService.save(axel);

		try
		{
			userService.setPassword(axel.getUid(), "blub", "enc");
			fail("expected " + PasswordEncoderNotFoundException.class.getName());
		}
		catch (final PasswordEncoderNotFoundException e) //NOPMD
		{
			//fine!
		}
	}

	@Test
	public void testSetPassword()
	{
		final EmployeeModel user = modelService.create(EmployeeModel.class);
		user.setUid("222");
		modelService.save(user);
		assertTrue(modelService.isUpToDate(user));
		assertFalse(modelService.isNew(user));
		assertFalse(modelService.isModified(user));

		userService.setPassword(user.getUid(), "password");

		assertTrue(modelService.isUpToDate(user));
		assertFalse(modelService.isNew(user));
		assertFalse(modelService.isModified(user));
		assertEquals(PasswordEncoderConstants.DEFAULT_ENCODING, user.getPasswordEncoding());
		assertEquals("password", user.getEncodedPassword());
	}

	@Test
	public void testUserIDWithSpaces()
	{
		final EmployeeModel user = modelService.create(EmployeeModel.class);
		user.setUid(" moo ");
		modelService.save(user);

		try
		{
			userService.getUserForUID("moo");
			fail("user should not be found! expected UnknownIdentifierException");
		}
		catch (final UnknownIdentifierException e)
		{
			// ok
		}
		catch (final Exception e)
		{
			fail("user should not be found!");
		}
		assertEquals(user, userService.getUserForUID(" moo "));
	}

}
