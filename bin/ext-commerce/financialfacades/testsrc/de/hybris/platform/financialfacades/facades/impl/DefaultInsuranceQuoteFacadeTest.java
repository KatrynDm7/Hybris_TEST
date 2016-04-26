/*
 *
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
 */

package de.hybris.platform.financialfacades.facades.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.insurance.data.InsuranceQuoteListingData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.financialfacades.facades.QuoteServiceFacade;
import de.hybris.platform.financialfacades.services.InsuranceQuoteService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * The class of DefaultInsuranceQuoteFacadeTest.
 */
@UnitTest
public class DefaultInsuranceQuoteFacadeTest
{

	@Mock
	private ModelService modelService;
	@Mock
	private CartService cartService;
	@Mock
	private InsuranceQuoteService quoteService;
	@Mock
	private Converter<CartData, InsuranceQuoteListingData> insuranceQuoteListingConverter;
	@InjectMocks
	private DefaultInsuranceQuoteFacade insuranceQuoteFacade;

	@Before
	public void setup()
	{
		insuranceQuoteFacade = new DefaultInsuranceQuoteFacade();
		final QuoteServiceFacade quoteServiceFacade = new MockQuoteServiceFacade();

		MockitoAnnotations.initMocks(this);
		insuranceQuoteFacade.setQuoteServiceFacade(quoteServiceFacade);
	}

	@Test
	public void shouldReturnQuoteWorkflowTypeLife()
	{
		final CartModel cartModel = new CartModel();
		final CartEntryModel entry = new CartEntryModel();
		final ProductModel productModel = new ProductModel();
		final CategoryModel defaultCategory = new CategoryModel();
		defaultCategory.setCode("insurances_life");
		productModel.setDefaultCategory(defaultCategory);
		entry.setProduct(productModel);
		final ArrayList<AbstractOrderEntryModel> entryList = new ArrayList<AbstractOrderEntryModel>();
		entryList.add(entry);
		cartModel.setEntries(entryList);

		Mockito.when(cartService.hasSessionCart()).thenReturn(Boolean.TRUE);
		Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);

		final String type = insuranceQuoteFacade.getQuoteWorkflowType();

		Assert.assertEquals("LIFE", type);
	}

	@Test
	public void shouldReturnQuoteWorkflowTypeDEFUALT()
	{
		final CartModel cartModel = new CartModel();
		final CartEntryModel entry = new CartEntryModel();
		final ProductModel productModel = new ProductModel();
		final CategoryModel defaultCategory = new CategoryModel();
		defaultCategory.setCode("insurances_other_stuff");
		productModel.setDefaultCategory(defaultCategory);
		entry.setProduct(productModel);
		final ArrayList<AbstractOrderEntryModel> entryList = new ArrayList<AbstractOrderEntryModel>();
		entryList.add(entry);
		cartModel.setEntries(entryList);

		Mockito.when(cartService.hasSessionCart()).thenReturn(Boolean.TRUE);
		Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);

		final String type = insuranceQuoteFacade.getQuoteWorkflowType();

		Assert.assertEquals("DEFAULT", type);
	}

	@Test
	public void shouldReturnQuoteWorkflowTypeNullWhenNoSessionCart()
	{
		Mockito.when(cartService.hasSessionCart()).thenReturn(Boolean.FALSE);

		final String type = insuranceQuoteFacade.getQuoteWorkflowType();

		Assert.assertNull(type);
	}

}
