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
package de.hybris.platform.commercefacades.order.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultCartFacadeTest
{


	private DefaultCartFacade defaultCardFacade;

	@Mock
	private CommerceCommonI18NService commerceCommonI18NService;
	@Mock
	private AbstractPopulatingConverter<CommerceCartModification, CartModificationData> cartModificationConverter;
	@Mock
	private CartService cartService;
	@Mock
	private CommonI18NService commonI18NService;
	@Mock
	private AbstractPopulatingConverter<CartModel, CartData> cartConverter;
	@Mock
	private AbstractPopulatingConverter<CartModel, CartData> miniCartConverter;
	@Mock
	private ProductService productService;
	@Mock
	private CommerceCartService commerceCartService;
	@Mock
	private DeliveryService deliveryService;
	@Mock
	private BaseSiteService baseSiteService;
	@Mock
	private UserService userService;
	@Mock
	private Converter<CountryModel, CountryData> countryConverter;

	private CartModel cartModel;
	private CartModel cartModel1;

	private static final String SESSION_CART_GUID = "SESSION_CART_GUID";
	private static final String CART = "cart";
	private static final String CART1 = "cart1";

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		defaultCardFacade = new DefaultCartFacade();
		defaultCardFacade.setCartService(cartService);
		defaultCardFacade.setCartConverter(cartConverter);
		defaultCardFacade.setMiniCartConverter(miniCartConverter);
		defaultCardFacade.setProductService(productService);
		defaultCardFacade.setCommerceCartService(commerceCartService);
		defaultCardFacade.setCartModificationConverter(cartModificationConverter);
		defaultCardFacade.setCountryConverter(countryConverter);
		defaultCardFacade.setDeliveryService(deliveryService);
		defaultCardFacade.setBaseSiteService(baseSiteService);
		defaultCardFacade.setUserService(userService);
		cartModel = new CartModel();
		cartModel.setCode(CART);
		cartModel.setGuid(CART);
		cartModel1 = new CartModel();
		cartModel1.setCode(CART1);
		cartModel1.setGuid(CART1);
		final CartData cartData = new CartData();
		cartData.setCode(CART);
		final CartData cartData1 = new CartData();
		cartData1.setCode(CART1);

		given(cartConverter.convert(cartModel)).willReturn(cartData);
		given(cartConverter.convert(cartModel1)).willReturn(cartData1);
		given(miniCartConverter.convert(cartModel)).willReturn(cartData);
	}

	@Test
	public void testGetSessionCart()
	{
		given(Boolean.valueOf(cartService.hasSessionCart())).willReturn(Boolean.TRUE);
		given(cartService.getSessionCart()).willReturn(cartModel);

		final CurrencyModel curr = new CurrencyModel();
		curr.setIsocode("EUR");
		curr.setSymbol("$");
		curr.setDigits(Integer.valueOf(2));
		final LanguageModel languageModel = new LanguageModel();
		languageModel.setIsocode("en");

		given(commonI18NService.getCurrency(anyString())).willReturn(curr);
		given(commonI18NService.getCurrentCurrency()).willReturn(curr);
		given(commonI18NService.getCurrentLanguage()).willReturn(languageModel);
		given(commerceCommonI18NService.getLocaleForLanguage(languageModel)).willReturn(Locale.UK);

		final CartData cart = defaultCardFacade.getSessionCart();
		Assert.assertEquals(CART, cart.getCode());
	}

	@Test
	public void testGetSessionCartNull()
	{
		given(Boolean.valueOf(cartService.hasSessionCart())).willReturn(Boolean.FALSE);

		final CurrencyModel curr = new CurrencyModel();
		curr.setIsocode("EUR");
		curr.setSymbol("$");
		curr.setDigits(Integer.valueOf(2));
		final LanguageModel languageModel = new LanguageModel();
		languageModel.setIsocode("en");
		final CartData emptyCart = new CartData();

		given(miniCartConverter.convert(null)).willReturn(emptyCart);
		given(commonI18NService.getCurrency(anyString())).willReturn(curr);
		given(commonI18NService.getCurrentCurrency()).willReturn(curr);
		given(commonI18NService.getCurrentLanguage()).willReturn(languageModel);
		given(commerceCommonI18NService.getLocaleForLanguage(languageModel)).willReturn(Locale.UK);


		final CartData cart = defaultCardFacade.getSessionCart();
		Assert.assertNotNull(cart);
		Assert.assertEquals(emptyCart, cart);
	}

	@Test
	public void testHasSessionCartFalse()
	{
		given(Boolean.valueOf(cartService.hasSessionCart())).willReturn(Boolean.FALSE);
		final boolean hasCart = defaultCardFacade.hasSessionCart();
		Assert.assertEquals(Boolean.FALSE, Boolean.valueOf(hasCart));
	}

	@Test
	public void testHasSessionCartTrue()
	{
		given(Boolean.valueOf(cartService.hasSessionCart())).willReturn(Boolean.TRUE);
		final boolean hasCart = defaultCardFacade.hasSessionCart();
		Assert.assertEquals(Boolean.TRUE, Boolean.valueOf(hasCart));
	}

	@Test
	public void testGetMiniCart()
	{
		given(Boolean.valueOf(cartService.hasSessionCart())).willReturn(Boolean.TRUE);
		given(cartService.getSessionCart()).willReturn(cartModel);
		final CartData cart = defaultCardFacade.getMiniCart();
		Assert.assertEquals(CART, cart.getCode());
	}

	@Test
	public void testGetMiniCartEmpty()
	{
		given(Boolean.valueOf(cartService.hasSessionCart())).willReturn(Boolean.FALSE);

		final CurrencyModel curr = new CurrencyModel();
		curr.setIsocode("EUR");
		curr.setSymbol("$");
		curr.setDigits(Integer.valueOf(2));
		final LanguageModel languageModel = new LanguageModel();
		languageModel.setIsocode("en");
		final CartData emptyCart = new CartData();

		given(miniCartConverter.convert(null)).willReturn(emptyCart);
		given(commonI18NService.getCurrency(anyString())).willReturn(curr);
		given(commonI18NService.getCurrentCurrency()).willReturn(curr);
		given(commonI18NService.getCurrentLanguage()).willReturn(languageModel);
		given(commerceCommonI18NService.getLocaleForLanguage(languageModel)).willReturn(Locale.UK);

		final CartData cart = defaultCardFacade.getMiniCart();
		Assert.assertNotNull(cart);
		Assert.assertEquals(emptyCart, cart);
	}

	@Test
	public void testAddToCart() throws CommerceCartModificationException
	{
		final UnitModel unit = new UnitModel();
		unit.setCode("unit");
		final ProductModel product = new ProductModel();
		product.setCode("prodCode");
		product.setUnit(unit);
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(productService.getProductForCode(anyString())).willReturn(product);
		defaultCardFacade.addToCart("prodCode", 1);
	}

	@Test
	public void testUpdateCartEntry() throws CommerceCartModificationException
	{
		final UnitModel unit = new UnitModel();
		unit.setCode("unit");
		final ProductModel product = new ProductModel();
		product.setCode("prodCode");
		product.setUnit(unit);
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(productService.getProductForCode(anyString())).willReturn(product);
		defaultCardFacade.updateCartEntry(0, 1);
	}

	@Test
	public void testGetDeliveryCountries()
	{
		final CountryModel country = Mockito.mock(CountryModel.class);
		final List<CountryModel> deliveryCountries = new ArrayList<>();
		deliveryCountries.add(country);
		deliveryCountries.add(country);
		given(deliveryService.getDeliveryCountriesForOrder(null)).willReturn(deliveryCountries);
		given(country.getName()).willReturn("PL");

		given(countryConverter.convert(country)).willReturn(new CountryData());
		final List<CountryData> results = defaultCardFacade.getDeliveryCountries();
		verify(deliveryService).getDeliveryCountriesForOrder(null);
		verify(countryConverter, Mockito.times(2)).convert(country);
		Assert.assertEquals(results.size(), 2);
	}

	@Test
	public void testGetMostRecentCartForUser()
	{
		final BaseSiteModel mockBaseSite = Mockito.mock(BaseSiteModel.class);
		final UserModel mockUser = Mockito.mock(UserModel.class);
		given(baseSiteService.getCurrentBaseSite()).willReturn(mockBaseSite);
		given(userService.getCurrentUser()).willReturn(mockUser);
		final String excludedCartsGuid = SESSION_CART_GUID;

		//when there is only one cart for the user
		given(commerceCartService.getCartsForSiteAndUser(mockBaseSite, mockUser)).willReturn(Collections.EMPTY_LIST);

		final String cartGuid = defaultCardFacade.getMostRecentCartGuidForUser(Arrays.asList(excludedCartsGuid));
		Assert.assertNull(cartGuid);

		//when there is more than one cart for the user, it returns the first cart excluding carts in the list
		given(commerceCartService.getCartsForSiteAndUser(mockBaseSite, mockUser)).willReturn(Arrays.asList(cartModel));

		final String secondMostRecentCartGuid = defaultCardFacade.getMostRecentCartGuidForUser(Arrays.asList(excludedCartsGuid));
		Assert.assertEquals(CART, secondMostRecentCartGuid);

		//when there is more than one cart for the user, but excluding list is empty, it returns the first recently modified cart
		given(commerceCartService.getCartsForSiteAndUser(mockBaseSite, mockUser)).willReturn(Arrays.asList(cartModel1));

		final String firstMostRecentCartGuid = defaultCardFacade.getMostRecentCartGuidForUser(Collections.EMPTY_LIST);
		Assert.assertEquals(CART1, firstMostRecentCartGuid);
	}

	@Test
	public void testUpdateOrderEntryForMultiDUpdate() throws CommerceCartModificationException
	{
		final UnitModel unit = new UnitModel();
		unit.setCode("unit");

		final ProductModel product = new ProductModel();
		product.setCode("prodCode");
		product.setUnit(unit);

		// main entry as a base product
		final OrderEntryData orderEntryData1 = getOrderEntryData(1,1,"49042000");

		final List<OrderEntryData> orderEntryList = new ArrayList<>();
		orderEntryList.add(orderEntryData1);

		// sub entry 1 as a variant product1
		final OrderEntryData subOrderEntryData21 = getOrderEntryData(0,1,"49042000_1");

		// sub entry 1 as a variant product2
		final OrderEntryData subOrderEntryData22 = getOrderEntryData(1,1,"49042000_2");

		final List<OrderEntryData> orderSubEntryMultiDList = new ArrayList<>();
		orderSubEntryMultiDList.add(subOrderEntryData21);
		orderSubEntryMultiDList.add(subOrderEntryData22);

		orderEntryData1.setEntries(orderSubEntryMultiDList);

		final CartData cartData = new CartData();
		cartData.setEntries(orderEntryList);

		//-1 represents that it's a multi D entry
		final OrderEntryData findEntry = getOrderEntryData(-1,11,"49042000_2");
		findEntry.setEntries(null); // should be null to add or update

		final CommerceCartModification commerceCartModification = new CommerceCartModification();
		commerceCartModification.setQuantity(11);

		final CartModificationData cartModificationData = new CartModificationData();
		cartModificationData.setQuantity(11); //

		final CartModificationData resultCartModificationData;

		given(Boolean.valueOf(cartService.hasSessionCart())).willReturn(Boolean.TRUE);
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(defaultCardFacade.getSessionCart()).willReturn(cartData);
		given(commerceCartService.updateQuantityForCartEntry(Mockito.anyObject())).willReturn(commerceCartModification);
		given(cartModificationConverter.convert(commerceCartModification)).willReturn(cartModificationData);
		given(productService.getProductForCode(anyString())).willReturn(product);

		resultCartModificationData = defaultCardFacade.updateCartEntry(findEntry);
		verify(commerceCartService).updateQuantityForCartEntry(Mockito.anyObject());
		Assert.assertEquals(cartModificationData.getQuantity(), resultCartModificationData.getQuantity());
	}


	@Test
	public void testUpdateOrderEntryMultiDAdd() throws CommerceCartModificationException
	{
		final UnitModel unit = new UnitModel();
		unit.setCode("unit");
		final ProductModel product = new ProductModel();
		product.setCode("prodCode");
		product.setUnit(unit);

		final OrderEntryData existingEntry = getOrderEntryData(0,2,"490420001");
		final OrderEntryData addEntry = getOrderEntryData(-1,4,"490420001_01");


		final CommerceCartModification commerceCartModification = new CommerceCartModification();
		commerceCartModification.setQuantity(4);

		final List<OrderEntryData> orderEntryList = new ArrayList<>();
		orderEntryList.add(existingEntry);

		final CartData cartData = new CartData();
		cartData.setEntries(orderEntryList);

		final CartModificationData cartModificationData = new CartModificationData();
		cartModificationData.setQuantity(4);

		final CartModificationData resultCartModificationData;

		given(Boolean.valueOf(cartService.hasSessionCart())).willReturn(Boolean.TRUE);
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(defaultCardFacade.getSessionCart()).willReturn(cartData);
		given(cartModificationConverter.convert(commerceCartModification)).willReturn(cartModificationData);
		given(commerceCartService.addToCart(Mockito.anyObject())).willReturn(commerceCartModification);
		given(productService.getProductForCode(anyString())).willReturn(product);

		resultCartModificationData = defaultCardFacade.updateCartEntry(addEntry);
		verify(commerceCartService).addToCart(Mockito.anyObject());
		Assert.assertEquals(cartModificationData.getQuantity(), resultCartModificationData.getQuantity());
	}

	protected OrderEntryData getOrderEntryData(int entryNumber,long qty,String productCode)
	{
		final OrderEntryData orderEntryData = new OrderEntryData();
		orderEntryData.setEntryNumber(Integer.valueOf(entryNumber));
		orderEntryData.setQuantity(Long.valueOf(qty));
		orderEntryData.setProduct(new ProductData());
		orderEntryData.getProduct().setCode(productCode);

		return orderEntryData;
	}
}
