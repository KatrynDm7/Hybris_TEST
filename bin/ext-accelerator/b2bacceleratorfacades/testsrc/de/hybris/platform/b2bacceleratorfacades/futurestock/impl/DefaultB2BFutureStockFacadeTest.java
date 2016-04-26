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
package de.hybris.platform.b2bacceleratorfacades.futurestock.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.futurestock.FutureStockService;
import de.hybris.platform.commercefacades.product.data.FutureStockData;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * Unit tests for {@link DefaultB2BFutureStockFacade}
 */
@UnitTest
@RunWith(value = MockitoJUnitRunner.class)
public class DefaultB2BFutureStockFacadeTest
{
	private final SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");

	private DefaultB2BFutureStockFacade b2bFutureStockFacade;

	@Mock
	private FutureStockService futureStockService;
	@Mock
	private ProductService productService;
	@Mock
	private CommerceCommonI18NService commerceCommonI18NService;
	@Mock
	LanguageModel languageModel;


	@Before
	public void setUp()
	{
		b2bFutureStockFacade = new DefaultB2BFutureStockFacade();
		b2bFutureStockFacade.setFutureStockService(futureStockService);
		b2bFutureStockFacade.setProductService(productService);
		b2bFutureStockFacade.setCommerceCommonI18NService(commerceCommonI18NService);
	}

	private Map<String, Map<Date, Integer>> getFutureMap(final String productCode)
	{
		final Map<String, Map<Date, Integer>> productsMap = new HashMap<>();
		final Map<Date, Integer> futureMap = new HashMap<Date, Integer>();
		try
		{
			futureMap.put(dateformat.parse("20130505"), 1);
			futureMap.put(dateformat.parse("20130303"), 2);
			futureMap.put(dateformat.parse("20130304"), 3);
			futureMap.put(dateformat.parse("20130101"), 4);
			futureMap.put(dateformat.parse("20130102"), 5);
		}
		catch (final ParseException e)
		{
			e.printStackTrace();
		}
		productsMap.put(productCode, futureMap);
		return productsMap;
	}

	@Test
	public void testGetFutureAvailability()
	{
		final String productCode = "sku01";
		final ProductModel product = new ProductModel();
		product.setCode(productCode);

		Mockito.when(futureStockService.getFutureAvailability(Mockito.any(List.class))).thenReturn(getFutureMap(productCode));
		Mockito.when(productService.getProductForCode(productCode)).thenReturn(product);
		Mockito.when(languageModel.getIsocode()).thenReturn("en");
		Mockito.when(commerceCommonI18NService.getDefaultLanguage()).thenReturn(languageModel);
		Mockito.when(commerceCommonI18NService.getLocaleForLanguage(languageModel)).thenReturn(Locale.ENGLISH);

		final List<FutureStockData> orderedFutureStock = b2bFutureStockFacade.getFutureAvailability(productCode);
		Assert.assertNotNull(orderedFutureStock);
		Assert.assertEquals(5, orderedFutureStock.size());
		FutureStockData fsdOld = orderedFutureStock.get(0);
		Assert.assertNotNull(fsdOld);
		// check if returned list is ordered
		for (int i = 1; i < orderedFutureStock.size(); i++)
		{
			final FutureStockData fsd = orderedFutureStock.get(i);
			Assert.assertNotNull(fsd);
			// current element should have a date that is newer or equal to last element

			final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");

			try
			{
				final Date newDate = dateFormat.parse(fsd.getFormattedDate());
				final Date oldDate = dateFormat.parse(fsdOld.getFormattedDate());

				Assert.assertTrue(newDate.compareTo(oldDate) > 0);
				fsdOld = fsd;
			}
			catch (final ParseException pe)
			{
				Assert.assertTrue(pe.getMessage(), false);
			}

		}
	}
}
