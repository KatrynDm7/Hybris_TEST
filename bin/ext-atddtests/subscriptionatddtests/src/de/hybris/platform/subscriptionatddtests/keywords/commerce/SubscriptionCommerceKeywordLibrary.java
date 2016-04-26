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
package de.hybris.platform.subscriptionatddtests.keywords.commerce;

import static de.hybris.platform.atddengine.xml.XmlAssertions.assertXPathEvaluatesTo;
import static org.junit.Assert.*;

import de.hybris.platform.atddengine.keywords.AbstractKeywordLibrary;
import de.hybris.platform.basecommerce.site.dao.BaseSiteDao;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.order.dao.CommerceCartDao;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.subscriptionatddtests.converters.ObjectXStreamAliasConverter;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.user.daos.UserDao;
import de.hybris.platform.subscriptionfacades.order.SubscriptionCartFacade;
import de.hybris.platform.util.DiscountValue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Robotframework library for commerce cart related keywords
 */
public class SubscriptionCommerceKeywordLibrary extends AbstractKeywordLibrary
{
	private static final Logger LOG = Logger.getLogger(SubscriptionCommerceKeywordLibrary.class);

	private static final double DELTA = 0.01;

	@Autowired
	private CartService cartService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private ProductFacade productFacade;

	@Autowired
	private StoreSessionFacade storeSessionFacade;

	@Autowired
	private UserFacade userFacade;

	@Autowired
	private SubscriptionCartFacade cartFacade;

	@Autowired
	private CommerceCartDao commerceCartDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private BaseSiteDao baseSiteDao;

	@Autowired
	private UserService userService;

	@Autowired
	private Converter<CartModel, CartData> cartConverter;

	@Autowired
	private CommerceCartService commerceCartService;

	@Autowired
	private ObjectXStreamAliasConverter xSubscriptionStreamAliasConverter;

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>verify number of child carts</i>
	 * <p>
	 *
	 * @param expectedCartCount
	 *           the expected number of child carts
	 */
	public void verifyNumberOfChildCarts(final int expectedCartCount)
	{
		assertEquals("Number of child carts does not match the expected value", expectedCartCount, cartService.getSessionCart()
				.getChildren().size());
	}

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>verify product xml</i>
	 * <p>
	 *
	 * @param productCode
	 *           code the code of the product to verify
	 * @param xpath
	 *           the XPath expression to evaluate
	 * @param expectedXml
	 *           the expected XML
	 */
	public void verifyProductXml(final String productCode, final String xpath, final String expectedXml)
	{
		try
		{
			final ProductData product = productFacade.getProductForCodeAndOptions(productCode,
					Arrays.asList(ProductOption.BASIC, ProductOption.PRICE));
			final String productXml = xSubscriptionStreamAliasConverter.getXStreamXmlFromObject(product);

			assertXPathEvaluatesTo("The product XML does not match the expectations:", productXml, xpath, expectedXml,
					"transformation/IgnoreEmptyLists.xsl");
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.error("Product with code " + productCode + " does not exist", e);
			fail("Product with code " + productCode + " does not exist");
		}
		catch (final IllegalArgumentException e)
		{
			LOG.error("Either the expected XML is malformed or the product code is null", e);
			fail("Either the expected XML is malformed or the product code is null");
		}
	}

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>verify cart total for billing event</i>
	 * <p>
	 *
	 * @param billingEventCode
	 *           the billing event to verify the order total for
	 * @param expectedTotal
	 *           the expected order total for the billing event
	 */
	public void verifyCartTotalForBillingEvent(final String billingEventCode, final double expectedTotal)
	{
		final AbstractOrderModel orderToCheck = getOrderForBillingEvent(billingEventCode, cartService.getSessionCart());
		double orderTotal;

		try
		{
			if (orderToCheck == null)
			{
				orderTotal = 0;
			}
			else
			{
				orderTotal = orderToCheck.getTotalPrice();
			}
			assertEquals("The order total for billing event [" + billingEventCode + "] does not match the expected value",
					expectedTotal, orderTotal, DELTA);
		}
		catch (final Exception e)
		{
			LOG.error("", e);
		}

	}

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>get discount price</i>
	 * <p>
	 *
	 * @param billingEventCode
	 *           the billing event to verify the discount price for
	 * @param expectedDiscountPrice
	 *           the expected discount price for the billing event
	 */
	public void getDiscountPrice(final String billingEventCode, final double expectedDiscountPrice)
	{
		final AbstractOrderModel orderToCheck = getOrderForBillingEvent(billingEventCode, cartService.getSessionCart());

		assertNotNull("No order found for the given billing event [" + billingEventCode + "]", orderToCheck);
		double discounts = 0;
		final List<AbstractOrderEntryModel> entries = orderToCheck.getEntries();
		if (entries != null)
		{
			for (final AbstractOrderEntryModel entry : entries)
			{
				final List<DiscountValue> discountValues = entry.getDiscountValues();
				if (discountValues != null)
				{
					for (final DiscountValue dValue : discountValues)
					{
						discounts += dValue.getAppliedValue();
					}
				}
			}
		}

		assertEquals("The discount for billing event [" + billingEventCode + "] does not match the expected value",
				expectedDiscountPrice, discounts, DELTA);

	}

	/**
	 * Switches the currency of the session to the given value. This logic is copied from
	 * StoreSessionController.selectCurrency
	 *
	 * @param currency
	 *           the currency to switch to
	 */
	public void setCartCurrencyTo(final String currency)
	{
		storeSessionFacade.setCurrentCurrency(currency);
		userFacade.syncSessionCurrency();
		cartFacade.refreshProductXMLs();
	}

	/**
	 * Prepares the session currency for a test case.
	 *
	 * @param currency
	 *           the currency to switch to
	 */
	public void prepareCurrency(final String currency)
	{
		final String backup = storeSessionFacade.getCurrentCurrency().getIsocode();
		sessionService.setAttribute("atdd.currency.backup", backup);

		setCartCurrencyTo(currency);
	}

	/**
	 * Resets the currency after a test case
	 */
	public void resetCurrency()
	{
		final String backup = sessionService.getAttribute("atdd.currency.backup");
		if (StringUtils.isNotEmpty(backup))
		{
			setCartCurrencyTo(backup);
		}
	}

	/**
	 * Returns the order (cart) to check for the given billing event
	 *
	 * @param billingEventCode
	 *           the code of the billing event to find the order for
	 * @return the order to check
	 */
	private AbstractOrderModel getOrderForBillingEvent(final String billingEventCode, final AbstractOrderModel abstractOrder)
	{
		AbstractOrderModel orderToCheck = null;

		if (abstractOrder.getBillingTime().getCode().equals(billingEventCode))
		{
			orderToCheck = abstractOrder;
		}
		else
		{
			for (final AbstractOrderModel orderModel : abstractOrder.getChildren())
			{
				if (orderModel.getBillingTime().getCode().equals(billingEventCode))
				{
					orderToCheck = orderModel;
				}
			}
		}
		return orderToCheck;
	}

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>get cart for billing time and master cart</i>
	 * <p>
	 *
	 * @param billingTime
	 *           the billing time: the (child) cart which has the this billing time is returned
	 * @param masterCart
	 *           the master order of the multi-cart
	 * @return the {@link CartModel}
	 */
	public CartModel getCartForBillingTimeAndMasterCart(final String billingTime, final CartModel masterCart)
	{
		return (CartModel) getOrderForBillingEvent(billingTime, masterCart);
	}

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>get cart data for billing time and master cart</i>
	 * <p>
	 *
	 * @param billingTime
	 *           the billing time: the (child) cart which has the this billing time is returned
	 * @param masterCart
	 *           the master order of the multi-cart
	 * @return the {@link CartData}
	 */
	public CartData getCartDataForBillingTimeAndMasterCart(final String billingTime, final CartData masterCart)
	{
		final CartModel cartForCodeAndUser = commerceCartDao.getCartForCodeAndUser(masterCart.getCode(),
				userService.getCurrentUser());

		final CartModel cartModel = (CartModel) getOrderForBillingEvent(billingTime, cartForCodeAndUser);

		return cartConverter.convert(cartModel);
	}

	/**
	 * Returns carts for the specified user and site
	 */
	public List<CartModel> getCartsForUserAndSite(final String userUid, final String siteName)
	{
		return commerceCartDao.getCartsForSiteAndUser(baseSiteDao.findBaseSiteByUID(siteName), userDao.findUserByUID(userUid));
	}

	/**
	 * Returns cart by code and user id
	 */
	public CartModel getCartForCodeAndUser(final String cartCode, final String userUid)
	{
		return commerceCartDao.getCartForCodeAndUser(cartCode, userDao.findUserByUID(userUid));
	}

	/**
	 * Java implementation of the robot keyword <br>
	 * <p>
	 * <i>verify cloned child carts</i>
	 * <p>
	 */
	public void verifyClonedChildCarts(final String originalCartCode, final String clonedCartCode)
	{
		final CartModel originalMasterCartModel = commerceCartService.getCartForCodeAndUser(originalCartCode,
				userService.getCurrentUser());
		final CartModel clonedMasterCartModel = commerceCartService.getCartForCodeAndUser(clonedCartCode,
				userService.getCurrentUser());

		if (CollectionUtils.isEmpty(clonedMasterCartModel.getChildren()))
		{
			assertTrue("Cloned cart does not have the same [child carts size] as the original cart.",
					CollectionUtils.isEmpty(originalMasterCartModel.getChildren()));
		}
		else
		{
			assertEquals("Cloned cart does not have the same [child carts size] as the original cart.", originalMasterCartModel
					.getChildren().size(), clonedMasterCartModel.getChildren().size());

			for (int i = 0; i < originalMasterCartModel.getChildren().size(); ++i)
			{
				final CartModel originalChildCart = (CartModel) ((List<AbstractOrderModel>) originalMasterCartModel.getChildren())
						.get(i);
				final CartModel clonedChildCart = (CartModel) ((List<AbstractOrderModel>) clonedMasterCartModel.getChildren()).get(i);

				verifyChildCartClone(originalChildCart, clonedChildCart);
				assertEquals("Cloned child cart has wrong [parent] code.", clonedMasterCartModel.getCode(), clonedChildCart
						.getParent().getCode());

			}
		}
	}

	private void verifyChildCartClone(final CartModel originalChildCart, final CartModel clonedChildCart)
	{
		assertFalse("Cloned child cart has the same [code] as the original child cart",
				originalChildCart.getCode().equals(clonedChildCart.getCode()));
		assertEquals("Cloned child cart does not have the same [user] as the original child cart", originalChildCart.getUser()
				.getUid(), clonedChildCart.getUser().getUid());
		assertEquals("Cloned child cart does not have the same [currency] as the original child cart",
				originalChildCart.getCurrency(), clonedChildCart.getCurrency());
		assertEquals("Cloned child cart does not have the same [totalPrice] as the original child cart",
				originalChildCart.getTotalPrice(), clonedChildCart.getTotalPrice());

		verifyClonedChildCartEntries(originalChildCart, clonedChildCart);
	}

	private void verifyClonedChildCartEntries(final CartModel originalChildCart, final CartModel clonedChildCart)
	{
		if (CollectionUtils.isEmpty(clonedChildCart.getEntries()))
		{
			assertTrue("Cloned child cart does not have the same [entries size] as the original child cart.",
					CollectionUtils.isEmpty(originalChildCart.getEntries()));
			return;
		}

		for (int i = 0; i < originalChildCart.getEntries().size(); i++)
		{
			final CartEntryModel originalCartEntryModel = (CartEntryModel) originalChildCart.getEntries().get(i);
			final CartEntryModel clonedCartEntryModel = (CartEntryModel) clonedChildCart.getEntries().get(i);

			// verify master entry is not equal
			assertFalse(String.format(
					"Master entry for entry [%s] in cart [%s] have the same [code] as the original child cart entry.",
					clonedCartEntryModel.getPk().toString(), clonedChildCart.getCode()), originalCartEntryModel.getMasterEntry()
					.getPk().getLongValue() == clonedCartEntryModel.getMasterEntry().getPk().getLongValue());

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
			assertEquals(String.format("Cloned cart entry with number %d does not have the same [unit] as the original cart entry",
					originalCartEntryModel.getEntryNumber()), originalCartEntryModel.getUnit().getCode(), clonedCartEntryModel
					.getUnit().getCode());

			// verify quantity is equal
			assertEquals(String.format(
					"Cloned cart entry with number %d does not have the same [quantity] as the original cart entry",
					originalCartEntryModel.getEntryNumber()), originalCartEntryModel.getQuantity(), clonedCartEntryModel.getQuantity());

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
}
