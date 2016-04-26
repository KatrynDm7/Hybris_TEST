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
package de.hybris.platform.timedaccesspromotionsfacades.product.converters.populator;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.PromotionData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.PromotionPriceRowModel;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.timedaccesspromotionsservices.model.FlashbuyPromotionModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test suite for {@link FlashbuyPromotionPopulator}
 */
@UnitTest
public class FlashbuyPromotionPopulatorTest
{
	private static final Long PRICE = Long.valueOf((long) 1.4);
	private static final Long RESET_COUNT_DOWN_TIME = Long.valueOf(0);
	private static final Long AVAILABLE_UNITS = Long.valueOf(40);
	private static final Long MAXIUM_AVAILABLE_UNIT_PER_UESER = Long.valueOf(1);
	private static final String ISO_CODE_ORIGIN = "isoCode";
	private static final String ISO_CODE_LATER = "isoCodeSecond";

	@Mock
	private CartService cartService;
	@Mock
	private PriceDataFactory priceDataFactory;
	@Mock
	private TimeService timeService;

	private final ArrayList<PromotionPriceRowModel> promotionPriceRowModel = new ArrayList();
	private FlashbuyPromotionPopulator flashbuyPromotionPopulator;
	private final CartModel cartModel = new CartModel();
	private final CurrencyModel cartCurrencyModel = new CurrencyModel();
	final CurrencyModel sourceCurrencyModel = new CurrencyModel();
	final PromotionPriceRowModel ppr = new PromotionPriceRowModel();
	final PriceData priceData = new PriceData();
	final Date currentDate = new Date();
	final Date startbuyAfter = new Date();
	final Date startbuyBefore = new Date();

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		flashbuyPromotionPopulator = new FlashbuyPromotionPopulator();
		flashbuyPromotionPopulator.setCartService(cartService);
		flashbuyPromotionPopulator.setPriceDataFactory(priceDataFactory);
		flashbuyPromotionPopulator.setTimeService(timeService);
	}

	@Test
	public void testPopulateWithSuccessfulResult()
	{
		final Long flagForCountDownTime;
		final FlashbuyPromotionModel source = new FlashbuyPromotionModel();
		startbuyAfter.setTime(currentDate.getTime() + Long.valueOf(100));

		source.setAvailableUnitsPerProduct(AVAILABLE_UNITS);
		source.setAvailableUnitsPerUserAndProduct(MAXIUM_AVAILABLE_UNIT_PER_UESER);
		source.setStartBuyDate(startbuyAfter);
		cartCurrencyModel.setIsocode(ISO_CODE_ORIGIN);
		cartModel.setCurrency(cartCurrencyModel);
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(timeService.getCurrentTime()).willReturn(currentDate);

		ppr.setPrice(Double.valueOf(PRICE));
		sourceCurrencyModel.setIsocode(ISO_CODE_ORIGIN);
		ppr.setCurrency(sourceCurrencyModel);
		promotionPriceRowModel.add(ppr);
		source.setProductFixedUnitPrice(promotionPriceRowModel);

		final PromotionData result = new PromotionData();
		flashbuyPromotionPopulator.populate(source, result);
		Assert.assertEquals(result.getAvailableUnitsPerProduct(), Long.valueOf(40));
		Assert.assertEquals(result.getAvailableUnitsPerUserAndProduct(), Long.valueOf(1));
		Assert.assertEquals(result.getItemType(), FlashbuyPromotionModel._TYPECODE);
		Assert.assertEquals(result.getStartBuyDate(), startbuyAfter);
		Assert.assertEquals(result.getCountDownTime(), Long.valueOf(100));
		Assert.assertEquals(result.getFixedPrice(),
				priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(ppr.getPrice()), ppr.getCurrency()));
	}

	@Test
	public void testPopulateWithDifferentIsocode()
	{
		final Long flagForCountDownTime;

		final FlashbuyPromotionModel source = new FlashbuyPromotionModel();
		startbuyAfter.setTime(currentDate.getTime() + Long.valueOf(100));

		source.setAvailableUnitsPerProduct(AVAILABLE_UNITS);
		source.setAvailableUnitsPerUserAndProduct(MAXIUM_AVAILABLE_UNIT_PER_UESER);
		source.setStartBuyDate(startbuyAfter);
		cartCurrencyModel.setIsocode(ISO_CODE_ORIGIN);
		cartModel.setCurrency(cartCurrencyModel);
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(timeService.getCurrentTime()).willReturn(currentDate);

		ppr.setPrice(Double.valueOf(PRICE));
		sourceCurrencyModel.setIsocode(ISO_CODE_LATER);
		ppr.setCurrency(sourceCurrencyModel);
		promotionPriceRowModel.add(ppr);
		source.setProductFixedUnitPrice(promotionPriceRowModel);

		final PromotionData result = new PromotionData();
		flashbuyPromotionPopulator.populate(source, result);
		Assert.assertEquals(result.getAvailableUnitsPerProduct(), Long.valueOf(40));
		Assert.assertEquals(result.getAvailableUnitsPerUserAndProduct(), Long.valueOf(1));
		Assert.assertEquals(result.getItemType(), FlashbuyPromotionModel._TYPECODE);
		Assert.assertEquals(result.getStartBuyDate(), startbuyAfter);
		Assert.assertEquals(result.getCountDownTime(), Long.valueOf(100));
		Assert.assertEquals(result.getFixedPrice(), null);
	}

	@Test
	public void testPopulateWithExpiredStartBuyDate()
	{
		final FlashbuyPromotionModel source = new FlashbuyPromotionModel();
		startbuyBefore.setTime(currentDate.getTime() - Long.valueOf(100));

		source.setAvailableUnitsPerProduct(AVAILABLE_UNITS);
		source.setAvailableUnitsPerUserAndProduct(MAXIUM_AVAILABLE_UNIT_PER_UESER);
		source.setStartBuyDate(startbuyBefore);
		cartCurrencyModel.setIsocode(ISO_CODE_ORIGIN);
		cartModel.setCurrency(cartCurrencyModel);
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(timeService.getCurrentTime()).willReturn(currentDate);

		ppr.setPrice(Double.valueOf(PRICE));
		sourceCurrencyModel.setIsocode(ISO_CODE_ORIGIN);
		ppr.setCurrency(sourceCurrencyModel);
		promotionPriceRowModel.add(ppr);
		source.setProductFixedUnitPrice(promotionPriceRowModel);

		final PromotionData result = new PromotionData();
		flashbuyPromotionPopulator.populate(source, result);
		Assert.assertEquals(result.getAvailableUnitsPerProduct(), Long.valueOf(40));
		Assert.assertEquals(result.getAvailableUnitsPerUserAndProduct(), Long.valueOf(1));
		Assert.assertEquals(result.getItemType(), FlashbuyPromotionModel._TYPECODE);
		Assert.assertEquals(result.getStartBuyDate(), startbuyBefore);
		Assert.assertEquals(result.getCountDownTime(), Long.valueOf(RESET_COUNT_DOWN_TIME));
		Assert.assertEquals(result.getFixedPrice(),
				priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(ppr.getPrice()), ppr.getCurrency()));
	}

	@Test
	public void testPopulateWithNotFlashbuyModelInput()
	{
		final AbstractPromotionModel source = new AbstractPromotionModel();
		final PromotionData result = new PromotionData();

		flashbuyPromotionPopulator.populate(source, result);
		Assert.assertEquals(result.getAvailableUnitsPerProduct(), null);
		Assert.assertEquals(result.getAvailableUnitsPerUserAndProduct(), null);
		Assert.assertEquals(result.getItemType(), null);
		Assert.assertEquals(result.getStartBuyDate(), null);
		Assert.assertEquals(result.getCountDownTime(), null);
		Assert.assertEquals(result.getFixedPrice(), null);
	}
}
