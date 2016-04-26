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
package de.hybris.platform.acceleratorservices.cronjob;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.acceleratorservices.model.CartRemovalCronJobModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.commerceservices.order.CommerceSaveCartService;
import de.hybris.platform.commerceservices.order.dao.CommerceCartDao;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.i18n.daos.CurrencyDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;


/**
 * Test class for testing the functionality of the CartRemovalJob.
 *
 * Attention: this class tests only the saved carts functionality of this job other functionalities can be enhanced
 */
@IntegrationTest
public class CartRemovalJobTest extends ServicelayerTransactionalTest
{
	private static final Logger LOG = Logger.getLogger(CartRemovalJobTest.class);//NOPMD

	private static final String BASESITE = "testBasesite";
	public static final String SESSION_CART_PARAMETER_NAME = "cart";

	private BaseSiteModel testBaseSite;
	private CurrencyModel currency;
	private UserGroupModel userGroup;
	private PageableData pageableData;
	private CartRemovalCronJobModel cartRemovalCronJobModel;

	@Resource
	private CartRemovalJob cartRemovalJob;

	@Resource
	private ModelService modelService;

	@Resource
	private CartService cartService;

	@Resource
	private SessionService sessionService;

	@Resource
	private BaseSiteService baseSiteService;

	@Resource
	private CommerceSaveCartService commerceSaveCartService;

	@Resource
	private CommerceCartDao commerceCartDao;

	@Resource
	private UserService userService;

	@Resource
	private CurrencyDao currencyDao;

	private boolean initialized = false;

	@Before
	public void setUp() throws Exception
	{
		setupTestDataEnvironment();

		final List<BaseSiteModel> baseSites = new ArrayList<BaseSiteModel>();
		baseSites.add(testBaseSite);

		cartRemovalCronJobModel = new CartRemovalCronJobModel();
		cartRemovalCronJobModel.setSites(baseSites);
		initialized = true;
	}

	/**
	 * No saved carts exist and test that no error happens
	 */
	@Test
	public void testNoSavedCartsAtAll()
	{
		final PerformResult result = cartRemovalJob.perform(cartRemovalCronJobModel);

		Assert.assertEquals(CronJobResult.SUCCESS, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());
	}

	/**
	 * Test that saved carts will not be removed if they are not expired
	 */
	@Test
	public void testNonExpiredSavedCarts()
	{
		final UserModel user = this.createTestUser("testUser1");
		CartModel cart = getCartForUser(user);

		cart = saveCart("savedCart_1", cart);

		SearchPageData<CartModel> savedCarts = commerceSaveCartService.getSavedCartsForSiteAndUser(pageableData, testBaseSite,
				user, null);
		Assert.assertEquals(savedCarts.getResults().size(), 1);

		final PerformResult result = cartRemovalJob.perform(cartRemovalCronJobModel);

		Assert.assertEquals(CronJobResult.SUCCESS, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());

		savedCarts = commerceSaveCartService.getSavedCartsForSiteAndUser(pageableData, testBaseSite, user, null);
		Assert.assertEquals(savedCarts.getResults().size(), 1);
	}

	/**
	 * Test that a non expired saved cart with exceeded max age date will not be removed
	 */
	@Test
	public void testSavedCartsWithExceededCartMaxAge() throws InterruptedException
	{
		final UserModel user = this.createTestUser("testUser1");
		CartModel cart = getCartForUser(user);

		cart = saveCart("savedCart_1", cart);

		SearchPageData<CartModel> savedCarts = commerceSaveCartService.getSavedCartsForSiteAndUser(pageableData, testBaseSite,
				user, null);
		Assert.assertEquals(savedCarts.getResults().size(), 1);

		// necessary date modification to state that cart has exceeded max age
		Thread.sleep(2000);

		final PerformResult result = cartRemovalJob.perform(cartRemovalCronJobModel);

		Assert.assertEquals(CronJobResult.SUCCESS, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());

		final List<CartModel> maxAgeExceededSavedCarts = commerceCartDao.getCartsForRemovalForSiteAndUser(new Date(), testBaseSite,
				null);
		Assert.assertEquals(maxAgeExceededSavedCarts.size(), 0);

		savedCarts = commerceSaveCartService.getSavedCartsForSiteAndUser(pageableData, testBaseSite, user, null);
		Assert.assertEquals(savedCarts.getResults().size(), 1);
	}

	/**
	 * Test if expired saved carts exist will be removed
	 */
	@Test
	public void testExpiredSavedCarts()
	{
		final UserModel user = this.createTestUser("testUser1");

		CartModel cart = getCartForUser(user);

		cart = saveCart("savedCart_1", cart);

		cart = removeAndCreateNewSessionCart();
		cart = getCartForUser(user);
		cart = saveCart("savedCart_2", cart);

		SearchPageData<CartModel> savedCarts = commerceSaveCartService.getSavedCartsForSiteAndUser(pageableData, testBaseSite,
				user, null);
		Assert.assertEquals(savedCarts.getResults().size(), 2);

		decreaseSavedCartsExpirationDate(user);

		final PerformResult result = cartRemovalJob.perform(cartRemovalCronJobModel);

		Assert.assertEquals(CronJobResult.SUCCESS, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());

		savedCarts = commerceSaveCartService.getSavedCartsForSiteAndUser(pageableData, testBaseSite, user, null);
		Assert.assertEquals(savedCarts.getResults().size(), 0);
	}

	private CartModel saveCart(final String name, final CartModel cart)
	{
		final CommerceSaveCartParameter parameter = new CommerceSaveCartParameter();
		parameter.setName(name);
		parameter.setDescription(name.toUpperCase());
		parameter.setEnableHooks(true);
		parameter.setCart(cart);

		try
		{
			final CommerceSaveCartResult result = commerceSaveCartService.saveCart(parameter);
			return result.getSavedCart();
		}
		catch (final CommerceSaveCartException e)
		{
			LOG.error(e.getMessage(), e);
			Assert.fail();
		}

		return null;
	}

	private CartModel getCartForUser(final UserModel user)
	{
		cartService.changeCurrentCartUser(user);
		cartService.changeSessionCartCurrency(user.getSessionCurrency());

		// avoids that the cart is calculated for the wrong user
		modelService.refresh(cartService.getSessionCart());
		cartService.changeCurrentCartUser(user);

		// adding currency to session manually is only a workaround
		JaloSession.getCurrentSession().getSessionContext()
				.setCurrency((de.hybris.platform.jalo.c2l.Currency) modelService.toPersistenceLayer(currency));
		JaloSession.getCurrentSession().getSessionContext().setUser((User) modelService.getSource(user));
		cartService.changeCurrentCartUser(user);

		return cartService.getSessionCart();
	}

	private CartModel removeAndCreateNewSessionCart()
	{
		sessionService.removeAttribute(SESSION_CART_PARAMETER_NAME);
		return cartService.getSessionCart();
	}

	private void setupTestDataEnvironment() throws ImpExException
	{
		if (!initialized)
		{
			importCsv("/acceleratorservices/test/testCartRemovalJob.impex", "utf-8");

			pageableData = new PageableData();
			pageableData.setPageSize(100);
			pageableData.setCurrentPage(0);

			testBaseSite = baseSiteService.getBaseSiteForUID(BASESITE);
			userGroup = userService.getUserGroupForUID("customergroup");
			currency = currencyDao.findCurrenciesByCode("USD").get(0);

			baseSiteService.setCurrentBaseSite(testBaseSite, true);
		}
	}

	private CustomerModel createTestUser(final String customerId)
	{
		final CustomerModel customer = new CustomerModel();
		customer.setUid(customerId);
		customer.setCustomerID(customerId);
		customer.setName(customerId);
		customer.setSessionCurrency(currency);

		final Set<PrincipalGroupModel> userGroups = new HashSet<PrincipalGroupModel>();
		userGroups.add(userGroup);
		customer.setGroups(userGroups);

		modelService.save(customer);

		return customer;
	}

	private void decreaseSavedCartsExpirationDate(final UserModel user)
	{
		final SearchPageData<CartModel> searchPageData = commerceSaveCartService.getSavedCartsForSiteAndUser(pageableData,
				testBaseSite, user, null);

		for (final CartModel cartModel : searchPageData.getResults())
		{
			final DateTime date = new DateTime(cartModel.getExpirationTime());

			cartModel.setExpirationTime(date.minusDays(31).toDate());
			modelService.save(cartModel);
		}
	}
}
