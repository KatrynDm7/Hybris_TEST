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
package de.hybris.platform.commerceservicesatddtests.keywords;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import de.hybris.platform.atddengine.keywords.AbstractKeywordLibrary;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.SaveCartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartRestorationData;
import de.hybris.platform.commercefacades.order.data.CommerceSaveCartParameterData;
import de.hybris.platform.commercefacades.order.data.CommerceSaveCartResultData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.commerceservices.order.dao.SaveCartDao;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;


public class CommerceServicesKeywordLibrary extends AbstractKeywordLibrary
{
	private static final Logger LOG = Logger.getLogger(CommerceServicesKeywordLibrary.class);
	private static final double DELTA = 0.01;
	private static final String ANONYMOUS_UID = "anonymous";
	public static final String SESSION_CART_PARAMETER_NAME = "cart";
	public static final String SITE = "testSite";

	@Autowired
	private CartService cartService;

	@Autowired
	private CartFacade cartFacade;

	@Autowired
	private UserService userService;

	@Autowired
	private BaseSiteService baseSiteService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private I18NService i18nService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private SaveCartFacade saveCartFacade;

	@Autowired
	private CommerceCartService commerceCartService;

	@Autowired
	private SaveCartDao saveCartDao;

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>add product to cart once</i>
	 * <p>
	 *
	 * @param productCode
	 *           the code of the product to add
	 */
	public void addProductToCartOnce(final String productCode)
	{
		addProductToCart(productCode, 1);
	}

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>add product to cart</i>
	 * <p>
	 *
	 * @param productCode
	 *           the code of the product to add
	 *
	 * @param quantity
	 *           the number of units to add
	 */
	public void addProductToCart(final String productCode, final long quantity)
	{
		try
		{
			assertEquals(quantity, cartFacade.addToCart(productCode, quantity).getQuantity());
		}
		catch (final Exception e)
		{
			// catch any exceptions that would get swallowed by the robot framework and log them
			LOG.error("An exception occured while adding a product to cart", e);
			fail(e.getMessage());
		}
	}

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>delete cart entry</i>
	 * <p>
	 *
	 * @param entryNumber
	 *           the entry number to delete
	 */
	public void deleteCartEntry(final long entryNumber)
	{
		try
		{
			assertEquals(0, cartFacade.updateCartEntry(entryNumber, 0).getQuantity());
		}
		catch (final CommerceCartModificationException e)
		{
			LOG.error("An exception occured while deleting a cart entry", e);
			fail(e.getMessage());
		}
	}

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>remove product from cart</i>
	 * <p>
	 *
	 * @param productCode
	 *           the code of the product to remove
	 */
	public void removeProductFromCart(final String productCode)
	{
		for (final OrderEntryData entry : cartFacade.getSessionCart().getEntries())
		{
			if (productCode.equals(entry.getProduct().getCode()))
			{
				deleteCartEntry(entry.getEntryNumber().longValue());
				break;
			}
		}
	}

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>verify cart total</i>
	 * <p>
	 *
	 * @param expectedTotal
	 *           the expected order total for the billing event
	 */
	public void verifyCartTotal(final double expectedTotal)
	{
		final CartModel sessionCart = cartService.getSessionCart();

		assertNotNull("The session cart is null", sessionCart);

		try
		{
			final double orderTotal = sessionCart.getTotalPrice().doubleValue();
			assertEquals("The order total for does not match the expected value", expectedTotal, orderTotal, DELTA);
		}
		catch (final Exception e)
		{
			LOG.error("An exception occured while calculating the order total", e);
		}
	}

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
	 * <i>prepare session</i>
	 * <p>
	 *
	 * @param baseSiteUid
	 *           the unique base site ID
	 */
	public void prepareSession(final String baseSiteUid)
	{
		setCurrentBaseSite(baseSiteUid);
		setCartUser(ANONYMOUS_UID);

		final Locale locale = i18nService.getCurrentLocale();
		sessionService.setAttribute("ATDD-Locale", locale);
		i18nService.setCurrentLocale(Locale.US);
	}

	/**
	 * Java implementation of the robot keyword <br/>
	 * <p>
	 * <i>reset system attributes</i>
	 * </p>
	 */
	public void resetSystemAttributes()
	{
		Locale locale = sessionService.getAttribute("ATDD-locale");

		if (locale == null)
		{
			locale = Locale.US;
		}

		i18nService.setCurrentLocale(locale);
		sessionService.removeAttribute("ATDD-Locale");
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
	public void login(final String userUID)
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

		// avoids that the cart is calculated for the wrong user
		modelService.refresh(cartService.getSessionCart());

		// adding currency to session manually is only a workaround
		JaloSession.getCurrentSession().getSessionContext().setCurrency((Currency) modelService.toPersistenceLayer(currency));
		JaloSession.getCurrentSession().getSessionContext().setUser((User) modelService.getSource(user));

	}

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>save cart with name and description</i>
	 * <p>
	 *
	 * @param name
	 *           name of the cart to be saved
	 * @param description
	 *           description of the cart to be saved
	 * @return the saved cart
	 * @throws CommerceSaveCartException
	 */
	public CartData saveCartWithNameAndDescription(final String name, final String description) throws CommerceSaveCartException
	{
		return saveGivenCartWithNameAndDescription(null, name, description);
	}

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>save given cart with name and description</i>
	 * <p>
	 *
	 * @param cartId
	 *           id/code of the cart to be saved
	 * @param name
	 *           name of the cart to be saved
	 * @param description
	 *           description of the cart to be saved
	 * @return the saved cart
	 * @throws CommerceSaveCartException
	 */
	public CartData saveGivenCartWithNameAndDescription(final String cartId, final String name, final String description)
			throws CommerceSaveCartException
	{
		final CommerceSaveCartParameterData parameters = new CommerceSaveCartParameterData();
		if (StringUtils.isEmpty(cartId))
		{
			getCurrentSessionCart().setSite(baseSiteService.getBaseSiteForUID("testSite"));
		}
		parameters.setCartId(cartId);
		parameters.setName(name);
		parameters.setDescription(description);
		parameters.setEnableHooks(true);
		final CommerceSaveCartResultData saveCartResultData = saveCartFacade.saveCart(parameters);
		final CartData savedCart = saveCartResultData.getSavedCartData();

		return savedCart;
	}

	/**
	 * Keyword implementation for flagging a saved cart for deletion
	 *
	 * @param cartToBeFlagged
	 *           the saved cart to be flagged for deletion
	 * @return cart data
	 * @throws CommerceSaveCartException
	 */
	public CartData flagForDeletion(final CartData cartToBeFlagged) throws CommerceSaveCartException
	{
		return flagCartForDeletion(cartToBeFlagged.getCode());
	}

	/**
	 * Keyword implementation for flagging a saved cart for deletion
	 *
	 * @param cartCode
	 *           the code of the saved cart to be flagged for deletion
	 * @return cart data
	 * @throws CommerceSaveCartException
	 */
	public CartData flagCartForDeletion(final String cartCode) throws CommerceSaveCartException
	{
		final CommerceSaveCartResultData result = saveCartFacade.flagForDeletion(cartCode);

		return result.getSavedCartData();
	}

	/**
	 * Keyword implementation for retrieve saved cart with cart code
	 *
	 * @param code
	 *           the cart code to be retrieved
	 * @return a cart data
	 * @throws CommerceSaveCartException
	 */
	public CartData retrieveSavedCartWithCode(final String code) throws CommerceSaveCartException
	{
		final CommerceSaveCartParameterData parameters = new CommerceSaveCartParameterData();
		parameters.setCartId(code);
		parameters.setEnableHooks(true);
		final CartData savedCart = saveCartFacade.getCartForCodeAndCurrentUser(parameters).getSavedCartData();
		return savedCart;
	}


	/**
	 * Keyword implementation for retrieve saved cart with cart code
	 *
	 * @param code
	 *           the cart code to be retrieved
	 * @return a cart restoration data
	 * @throws CommerceSaveCartException
	 */
	public CartRestorationData RestoreSavedCartWithCode(final String code) throws CommerceSaveCartException
	{
		baseSiteService.setCurrentBaseSite(baseSiteService.getBaseSiteForUID("testSite"), false);

		final CommerceSaveCartParameterData parameters = new CommerceSaveCartParameterData();
		parameters.setCartId(code);
		parameters.setEnableHooks(true);
		final CartRestorationData restorationData = saveCartFacade.restoreSavedCart(parameters);
		return restorationData;
	}

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>get session cart</i>
	 * <p>
	 *
	 * @return the session cart
	 */
	public CartModel getCurrentSessionCart()
	{
		return cartService.getSessionCart();
	}

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>create new session cart</i>
	 * <p>
	 *
	 * @return the new session cart
	 */
	public CartModel removeAndCreateNewSessionCart()
	{
		sessionService.removeAttribute(SESSION_CART_PARAMETER_NAME);
		final CartModel newSessionCart = cartService.getSessionCart();
		return newSessionCart;
	}

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>verify that date1 is n days later than date2</i>
	 * <p>
	 *
	 * @param date1
	 *           date that is expected to be n days later than date2
	 * @param days
	 *           number of days
	 * @param date2
	 *           base date
	 */
	public void verifyThatDate1IsNDaysLaterThanDate2(final Date date1, final int days, final Date date2)
	{
		final Date expectedDate = new DateTime(date2).plusDays(days).toDate();
		assertEquals(expectedDate, date1);
	}

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>get saved cart from list</i>
	 * </p>
	 *
	 * @param savedCartList
	 */
	public CartData getSavedCartFromList(final ArrayList<CartData> savedCartList, final String code)
	{
		if (savedCartList != null && savedCartList.size() > 0)
		{
			for (final CartData savedCart : savedCartList)
			{
				if (StringUtils.equals(code, savedCart.getCode()))
				{
					return savedCart;
				}
			}
		}

		return null;
	}

	/**
	 * Implementation of the robot keyword <br>
	 * <p>
	 * <i>verify equal list size</i>
	 * <p>
	 *
	 * Compares the size of the given list to an expected value
	 *
	 * @param list
	 *           the list whose size should be compared
	 * @param expectedSize
	 *           expected value
	 */
	public void verifyListSizeEquals(final List<Object> list, final int expectedSize)
	{
		final String errorMsg = "Number of elements in given list %d does not match expected value %d";
		assertEquals(String.format(errorMsg, Integer.valueOf(list.size()), Integer.valueOf(expectedSize)), expectedSize,
				list.size());
	}

	/**
	 * Implementation of the robot keyword <br>
	 * <p>
	 * <i>get saved carts to remove</i>
	 * </p>
	 *
	 * Determines the saved carts whose expiration date exceeds the given value.
	 *
	 * @return a list of saved carts to remove
	 */
	public List<CartModel> getSavedCartsToRemove()
	{
		final BaseSiteModel baseSite = baseSiteService.getBaseSiteForUID("testSite");
		final List<CartModel> cartsForRemoval = saveCartDao.getSavedCartsForRemovalForSite(baseSite);

		return cartsForRemoval;
	}

	/**
	 * Implementation of the robot keyword <br>
	 * <p>
	 * <i>decrease saved carts expiration date</i>
	 * </p>
	 *
	 * For a saved cart the expiration date is set to a value which causes a finding in the unsaveDao.
	 *
	 */
	public void decreaseSavedCartsExpirationDate()
	{
		final BaseSiteModel baseSite = baseSiteService.getBaseSiteForUID("testSite");
		final UserModel user = userService.getCurrentUser();
		final List<CartModel> cartModels = commerceCartService.getCartsForSiteAndUser(baseSite, user);

		for (final CartModel cartModel : cartModels)
		{
			final DateTime date = new DateTime(cartModel.getExpirationTime());

			cartModel.setExpirationTime(date.minusDays(31).toDate());
			modelService.save(cartModel);
		}
	}

	/**
	 * Implementation of the robot keyword <br>
	 * <p>
	 * <i>get saved carts for current user</i>
	 * </p>
	 *
	 * Lists all saved carts for the current user.
	 *
	 * @return lists of saved carts
	 */
	public List<CartData> getSavedCartsForCurrentUser()
	{
		return getListOfSavedCarts("");
	}

	public List<CartData> getListOfSavedCarts(final String statusList)
	{
		setCurrentBaseSite("testSite");
		final List<OrderStatus> orderStatus = new ArrayList<>();
		if (StringUtils.isNotEmpty(statusList))
		{
			for (final String status : statusList.split(","))
			{
				orderStatus.add(OrderStatus.valueOf(status.trim()));
			}
		}
		final PageableData pageableData = new PageableData();
		pageableData.setPageSize(100);
		pageableData.setCurrentPage(0);
		return saveCartFacade.getSavedCartsForCurrentUser(pageableData, orderStatus).getResults();
	}

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>clone saved cart with code</i>
	 * <p>
	 */
	public CartData cloneSavedCart(final String cartCode, final String name, final String description)
			throws CommerceSaveCartException
	{
		final CommerceSaveCartParameterData commerceSaveCartParameterData = new CommerceSaveCartParameterData();
		commerceSaveCartParameterData.setCartId(cartCode);
		commerceSaveCartParameterData.setName(name);
		commerceSaveCartParameterData.setDescription(description);
		commerceSaveCartParameterData.setEnableHooks(true);
		return saveCartFacade.cloneSavedCart(commerceSaveCartParameterData).getSavedCartData();
	}

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>verify cart clone</i>
	 * <p>
	 */
	public void verifyCartClone(final String originalCartCode, final String clonedCartCode)
	{
		try
		{
			assertFalse("Cloned cart has the same [code] as the original cart", originalCartCode.equals(clonedCartCode));

			final CartModel originalCartModel = commerceCartService.getCartForCodeAndUser(originalCartCode,
					userService.getCurrentUser());
			final CartModel clonedCartModel = commerceCartService
					.getCartForCodeAndUser(clonedCartCode, userService.getCurrentUser());

			assertEquals("Cloned cart does not have the same [user] as the original cart", originalCartModel.getUser().getUid(),
					clonedCartModel.getUser().getUid());
			assertEquals("Cloned cart does not have the same [currency] as the original cart", originalCartModel.getCurrency(),
					clonedCartModel.getCurrency());
			assertEquals("Cloned cart does not have the same [totalPrice] as the original cart", originalCartModel.getTotalPrice(),
					clonedCartModel.getTotalPrice());
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>verify cloned cart entries</i>
	 * <p>
	 */
	public void verifyClonedCartEntries(final String originalCartCode, final String clonedCartCode)
	{
		try
		{
			final CartModel originalCartModel = commerceCartService.getCartForCodeAndUser(originalCartCode,
					userService.getCurrentUser());
			final CartModel clonedCartModel = commerceCartService
					.getCartForCodeAndUser(clonedCartCode, userService.getCurrentUser());

			if (originalCartModel.getEntries() == null)
			{
				assertNull(clonedCartModel.getEntries());
				return;
			}

			assertEquals("Cloned cart does not have the same number of cart entries as the original cart.", originalCartModel
					.getEntries().size(), clonedCartModel.getEntries().size());


			for (int i = 0; i < clonedCartModel.getEntries().size(); i++)
			{
				final CartEntryModel originalCartEntryModel = (CartEntryModel) originalCartModel.getEntries().get(i);
				final CartEntryModel clonedCartEntryModel = (CartEntryModel) clonedCartModel.getEntries().get(i);

				// verify entry number is equal
				assertEquals("Cloned cart entry does not have the same [entry numer] as the original cart entry",
						originalCartEntryModel.getEntryNumber(), clonedCartEntryModel.getEntryNumber());

				// verify PK is not equal
				assertFalse(
						String.format("Cart entry with number %d has not been deep cloned", originalCartEntryModel.getEntryNumber()),
						originalCartEntryModel.getPk().getLongValue() == clonedCartEntryModel.getPk().getLongValue());

				// verify product is the same
				assertEquals(String.format(
						"Cloned cart entry with number %d does not have the same [product] as the original cart entry",
						originalCartEntryModel.getEntryNumber()), originalCartEntryModel.getProduct().getCode(), clonedCartEntryModel
						.getProduct().getCode());

				// verify unit is the same
				assertEquals(String.format(
						"Cloned cart entry with number %d does not have the same [unit] as the original cart entry",
						originalCartEntryModel.getEntryNumber()), originalCartEntryModel.getUnit().getCode(), clonedCartEntryModel
						.getUnit().getCode());

				// verify quantity is equal
				assertEquals(String.format(
						"Cloned cart entry with number %d does not have the same [quantity] as the original cart entry",
						originalCartEntryModel.getEntryNumber()), originalCartEntryModel.getQuantity(),
						clonedCartEntryModel.getQuantity());

				// verify base price is equal
				assertEquals(String.format(
						"Cloned cart entry with number %d does not have the same [base price] as the original cart entry",
						originalCartEntryModel.getEntryNumber()), originalCartEntryModel.getBasePrice(),
						clonedCartEntryModel.getBasePrice());

				// verify total price is equal
				assertEquals(String.format(
						"Cloned cart entry with number %d does not have the same [total price] as the original cart entry",
						originalCartEntryModel.getEntryNumber()), originalCartEntryModel.getTotalPrice(),
						clonedCartEntryModel.getTotalPrice());

				// verify delivery address is the same
				assertEquals(String.format(
						"Cloned cart entry with number %d does not have the same [delivery address] as the original cart entry",
						originalCartEntryModel.getEntryNumber()), originalCartEntryModel.getDeliveryAddress(),
						clonedCartEntryModel.getDeliveryAddress());

				// verify delivery mode is the same
				assertEquals(String.format(
						"Cloned cart entry with number %d does not have the same [delivery mode] as the original cart entry",
						originalCartEntryModel.getEntryNumber()), originalCartEntryModel.getDeliveryMode(),
						clonedCartEntryModel.getDeliveryMode());
			}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
}
