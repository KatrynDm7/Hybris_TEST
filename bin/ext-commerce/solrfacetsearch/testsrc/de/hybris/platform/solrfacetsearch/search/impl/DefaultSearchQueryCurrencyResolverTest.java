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
package de.hybris.platform.solrfacetsearch.search.impl;

import static org.mockito.BDDMockito.given;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class DefaultSearchQueryCurrencyResolverTest
{
	private DefaultSearchQueryCurrencyResolver defaultSearchQueryCurrencyResolver;

	private FacetSearchConfig facetSearchConfig;
	private IndexedType indexedType;

	@Mock
	private CommonI18NService commonI18NService;

	@Mock
	private UserService userService;

	@Mock
	private UserModel userModel;

	@Mock
	private CurrencyModel currencyModel;

	@Before
	public void setUp()
	{
		defaultSearchQueryCurrencyResolver = new DefaultSearchQueryCurrencyResolver();
		facetSearchConfig = new FacetSearchConfig();
		indexedType = new IndexedType();

		MockitoAnnotations.initMocks(this);

		given(userService.getCurrentUser()).willReturn(userModel);

		defaultSearchQueryCurrencyResolver.setCommonI18NService(commonI18NService);
		defaultSearchQueryCurrencyResolver.setUserService(userService);
	}

	@Test
	public void testResolveCurrencyFailed()
	{
		// when
		final CurrencyModel resolvedCurrency = defaultSearchQueryCurrencyResolver.resolveCurrency(facetSearchConfig, indexedType);

		// then
		Assert.assertNull(resolvedCurrency);
	}

	@Test
	public void testResolveCurrencyBySessionCurrency()
	{
		// given
		given(userModel.getSessionCurrency()).willReturn(currencyModel);

		// when
		final CurrencyModel resolvedCurrency = defaultSearchQueryCurrencyResolver.resolveCurrency(facetSearchConfig, indexedType);

		// then
		Assert.assertEquals(resolvedCurrency, currencyModel);
	}

	@Test
	public void testResolveCurrencyByServiceCurrency()
	{
		// given
		given(commonI18NService.getCurrentCurrency()).willReturn(currencyModel);

		// when
		final CurrencyModel resolvedCurrency = defaultSearchQueryCurrencyResolver.resolveCurrency(facetSearchConfig, indexedType);

		// then
		Assert.assertEquals(resolvedCurrency, currencyModel);
	}
}
