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
package de.hybris.platform.commercefacades.order.converters.populator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.BaseOptionData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


@UnitTest
public class GroupOrderEntryPopulatorTest
{


	public static final long MIN_PRICE_RANGE_TEST = 4L;
	public static final long MAX_PRICE_RANGE_TEST = 6L;
	public static final String BASE_PRODUCT_2_PRICE_RANGE_TEST = "baseProduct2";
	public static final long TOTAL_PRICE_RANGE_TEST = 15L;
	@Mock
	ProductService productService;

	PriceDataFactory priceDataFactory = new PriceDataFactory()
	{

		@Override
		public PriceData create(final PriceDataType priceType, final BigDecimal value, final String currencyIso)
		{
			final PriceData priceData = new PriceData();

			priceData.setPriceType(priceType);
			priceData.setValue(value);
			priceData.setCurrencyIso(currencyIso);

			return priceData;
		}

		@Override
		public PriceData create(final PriceDataType priceType, final BigDecimal value, final CurrencyModel currency)
		{
			return null;
		}
	};

	@InjectMocks
	protected GroupOrderEntryPopulator populator = new GroupOrderEntryPopulator();


	@Before
	public void setUp()
	{

		MockitoAnnotations.initMocks(this);

		when(productService.getProductForCode(anyString())).thenAnswer(new Answer<Object>()
		{
			@Override
			public Object answer(final InvocationOnMock invocationOnMock) throws Throwable
			{

				final String code = (String) invocationOnMock.getArguments()[0];
				final ProductModel productModel = mock(ProductModel.class);
				when(productModel.getCode()).thenReturn(code);
				when(productModel.getName()).thenReturn("Name " + code);
				when(productModel.getDescription()).thenReturn("Description " + code);

				return productModel;
			}
		});


		populator.setPriceDataFactory(priceDataFactory);

	}

	@Test
	public void shouldGroupEntries()
	{

		final AbstractOrderData order = new AbstractOrderData();
		final ArrayList<OrderEntryData> originalEntries = new ArrayList<>();
		order.setEntries(originalEntries);

		originalEntries.add(createOrderEntry("productCode1", "baseProduct1", 1L, 1L));
		originalEntries.add(createOrderEntry("productCode2", "baseProduct1", 2L, 5L));
		originalEntries.add(createOrderEntry("productCode3", null, 3L, 5L));
		originalEntries.add(createOrderEntry("productCode4", BASE_PRODUCT_2_PRICE_RANGE_TEST, MIN_PRICE_RANGE_TEST,
				TOTAL_PRICE_RANGE_TEST));
		originalEntries.add(createOrderEntry("productCode5", BASE_PRODUCT_2_PRICE_RANGE_TEST, MIN_PRICE_RANGE_TEST + 1,
				TOTAL_PRICE_RANGE_TEST));
		originalEntries.add(createOrderEntry("productCode6", BASE_PRODUCT_2_PRICE_RANGE_TEST, MAX_PRICE_RANGE_TEST,
				TOTAL_PRICE_RANGE_TEST));
		originalEntries.add(createOrderEntry("productCode7", "baseProduct3", 7L, 7L));
		originalEntries.add(createOrderEntry("productCode8", null, 5L, 5L)); //Product without parent with multiple entries in the cart
		originalEntries.add(createOrderEntry("productCode8", null, 7L, 5L)); //Product without parent with multiple entries in the cart
		final OrderEntryData notGroupedEntry = createOrderEntry("productCode9", "nonMultid", MAX_PRICE_RANGE_TEST,
				TOTAL_PRICE_RANGE_TEST);
		notGroupedEntry.getProduct().getBaseOptions().get(0).setVariantType("AnotherTypeOfVariant");
		originalEntries.add(notGroupedEntry);

		populator.populate(mock(AbstractOrderModel.class), order);

		assertThat(Integer.valueOf(order.getEntries().size()), is(Integer.valueOf(7)));

		for (final OrderEntryData parentEntry : order.getEntries())
		{
			validatePriceRange(parentEntry);

			if (parentEntry.getEntries() != null)
			{
				long totalQuantities = 0;
				int totalPrice = 0;
				for (final OrderEntryData childEntry : parentEntry.getEntries())
				{
					final OrderEntryData firstEntry = parentEntry.getEntries().get(0);
					assertThat(childEntry.getProduct().getBaseProduct(), is(firstEntry.getProduct().getBaseProduct()));

					totalQuantities += childEntry.getQuantity().longValue();
					totalPrice += childEntry.getTotalPrice().getValue().intValue();
				}

				assertTrue(totalQuantities > 0);
				assertTrue(totalPrice > 0);

				assertThat(parentEntry.getQuantity(), is(Long.valueOf(totalQuantities)));
				assertThat(Integer.valueOf(parentEntry.getTotalPrice().getValue().intValue()), is(Integer.valueOf(totalPrice)));

				final OrderEntryData firstEntry = parentEntry.getEntries().get(0);
				validateProductInfo(parentEntry, firstEntry);
			}
		}



	}

	protected void validateProductInfo(final OrderEntryData parentEntry, final OrderEntryData firstEntry)
	{
		assertThat(parentEntry.getBasePrice(), is(firstEntry.getBasePrice()));
		assertThat(parentEntry.getProduct().getCode(), is(firstEntry.getProduct().getBaseProduct()));
		assertThat(parentEntry.getProduct().getImages(), is(firstEntry.getProduct().getImages()));
		assertThat(parentEntry.getProduct().getUrl(), is(firstEntry.getProduct().getUrl()));
	}

	protected void validatePriceRange(final OrderEntryData parentEntry)
	{

		if (BASE_PRODUCT_2_PRICE_RANGE_TEST.equals(parentEntry.getProduct().getBaseProduct()))
		{
			final long actualMinValue = parentEntry.getProduct().getPriceRange().getMinPrice().getValue().longValue();
			final long actualMaxValue = parentEntry.getProduct().getPriceRange().getMaxPrice().getValue().longValue();

			assertThat(Long.valueOf(actualMinValue), is(Long.valueOf(MIN_PRICE_RANGE_TEST)));
			assertThat(Long.valueOf(actualMaxValue), is(Long.valueOf(MAX_PRICE_RANGE_TEST)));
		}

	}


	public OrderEntryData createOrderEntry(final String productCode, final String baseProductCode, final long price,
			final long totalPrice)
	{
		final OrderEntryData entry = new OrderEntryData();

		final ProductData product = new ProductData();
		product.setCode(productCode);
		product.setBaseProduct(baseProductCode);
		product.setBaseOptions(new ArrayList<BaseOptionData>());
		if (baseProductCode != null)
		{
			final BaseOptionData baseOption = new BaseOptionData();
			baseOption.setVariantType(GroupOrderEntryPopulator.VARIANT_TYPE);
			product.getBaseOptions().add(baseOption);
		}
		product.setImages(new ArrayList<ImageData>());

		product.setMultidimensional(Boolean.valueOf(baseProductCode != null));

		entry.setProduct(product);

		entry.setBasePrice(new PriceData());
		entry.getBasePrice().setValue(BigDecimal.valueOf(price));
		entry.getBasePrice().setCurrencyIso("USD");
		entry.getBasePrice().setPriceType(PriceDataType.BUY);

		entry.setTotalPrice(new PriceData());
		entry.getTotalPrice().setCurrencyIso("USD");
		entry.getTotalPrice().setValue(BigDecimal.valueOf(totalPrice));
		entry.getTotalPrice().setPriceType(PriceDataType.BUY);

		entry.setQuantity(Long.valueOf(1L));

		return entry;
	}

}
