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
package de.hybris.platform.emsclientatddtests.keywords.emsclient;

import static org.junit.Assert.assertNotNull;

import de.hybris.platform.atddengine.keywords.AbstractKeywordLibrary;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;


public class LocalCommerceServicesKeywordLibrary extends AbstractKeywordLibrary
{
	private static final Logger LOG = Logger.getLogger(LocalCommerceServicesKeywordLibrary.class);
	public static final String SESSION_CART_PARAMETER_NAME = "cart";
	public static final String SITE = "testSite";

	@Autowired
	private CartService cartService;

	@Autowired
	private UserService userService;

	@Autowired
	private BaseSiteService baseSiteService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private CustomerFacade customerFacade;

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>set current base site</i>
	 * <p>
	 *
	 * @param baseSiteUid
	 *           the unique base site ID
	 */
	public void setCurrentBaseSite(final String baseSiteUid)
	{
		final BaseSiteModel baseSite = baseSiteService.getBaseSiteForUID(baseSiteUid);
		baseSiteService.setCurrentBaseSite(baseSite, true);
	}

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>login</i>
	 * <p>
	 *
	 * @param userUID
	 *           the unique user ID
	 */
	public void loginWithWorkaround(final String userUID)
	{
		setCartUser(userUID);
	}

	/**
	 * Sets the cart user and the session currency
	 *
	 * @param userUID
	 *           the unique user ID
	 */
	private void setCartUser(final String userUID)
	{
		final UserModel user = userService.getUserForUID(userUID);

		final CurrencyModel currency = setCurrencyAndBaseSite(user);

		// avoids that the cart is calculated for the wrong user
		//modelService.refresh(cartService.getSessionCart());
		final CartModel cartModel = cartService.getSessionCart();
		modelService.refresh(cartModel);

		// FIXME: this workaround updates the cart assigning to it the current user.
		// the previous invocation "cartService.changeCurrentCartUser(user)" should have done this, but it doesn't.
		cartModel.setUser(user);
		modelService.save(cartModel);

		// adding currency to session manually is only a workaround
		JaloSession.getCurrentSession().getSessionContext().setCurrency((Currency) modelService.toPersistenceLayer(currency));
		JaloSession.getCurrentSession().getSessionContext().setUser((User) modelService.getSource(user));

	}

	private CurrencyModel setCurrencyAndBaseSite(final UserModel user)
	{
		CurrencyModel currency = user.getSessionCurrency();
		if (currency == null)
		{
			BaseSiteModel baseSite = baseSiteService.getCurrentBaseSite();
			if (baseSite == null)
			{
				baseSite = baseSiteService.getAllBaseSites().iterator().next();
				assertNotNull("No base site was found. Please review your sample data!", baseSite);
				setCurrentBaseSite(baseSite.getUid());
			}
			final BaseStoreModel baseStore = baseSite.getStores().iterator().next();
			assertNotNull("No base store was found. Please review your sample data!", baseStore);
			currency = baseStore.getCurrencies().iterator().next();
		}
		assertNotNull("No currency was found. Please review your sample data!", currency);

		LOG.info(String.format("Setting cart user [%s] and currency [%s]", user.getUid(), currency.getIsocode()));

		cartService.changeCurrentCartUser(user);
		cartService.changeSessionCartCurrency(currency);
		return currency;
	}

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>createCustomerWithHybrisApi</i>
	 * <p>
	 *
	 * @param login
	 *           the unique user ID
	 * @param password
	 *           password
	 * @param titleCode
	 *           title code
	 * @param firstName
	 *           first name
	 * @param lastName
	 *           lastName
	 */
	public void createCustomerWithHybrisApi(final String login, final String password, final String titleCode,
			final String firstName, final String lastName)
	{
		LOG.info("login:" + login);
		LOG.info("password:" + password);
		LOG.info("titleCode:" + titleCode);
		LOG.info("firstName:" + firstName);
		LOG.info("lastName:" + lastName);

		final UserModel user = userService.getCurrentUser();

		final CurrencyModel currency = setCurrencyAndBaseSite(user);

		// adding currency to session manually is only a workaround
		JaloSession.getCurrentSession().getSessionContext().setCurrency((Currency) modelService.toPersistenceLayer(currency));
		JaloSession.getCurrentSession().getSessionContext().setUser((User) modelService.getSource(user));

		final RegisterData registerData = new RegisterData();
		registerData.setFirstName(firstName);
		registerData.setLastName(lastName);
		registerData.setLogin(login);
		registerData.setPassword(password);
		registerData.setTitleCode(titleCode);

		try
		{
			customerFacade.register(registerData);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			Assert.fail(e.getMessage());
		}
	}
}