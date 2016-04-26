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
package de.hybris.platform.financialfacades.facades.impl;

import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.financialservices.enums.QuoteBindingState;
import de.hybris.platform.financialservices.model.InsuranceQuoteModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;


@UnitTest
public class DefaultInsuranceCartFacadeTest
{
	@Mock
	CartService cartService;

	@Mock
	ModelService modelService;

	@Mock
	ProductService productService;

	@InjectMocks
	DefaultInsuranceCartFacade defaultInsuranceCartFacade;

	@Before
	public void setup()
	{
		defaultInsuranceCartFacade = new DefaultInsuranceCartFacade();

		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldSetVisitedForGivenProgressId()
	{
		final String progressId = "testId";

		final CartModel cartModel = new CartModel();
		final InsuranceQuoteModel quoteModel = new InsuranceQuoteModel();
		cartModel.setInsuranceQuote(quoteModel);

		Mockito.when(cartService.hasSessionCart()).thenReturn(true);
		Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);

		defaultInsuranceCartFacade.setVisitedStepToQuoteInCurrentCart(progressId);

		verify(modelService, Mockito.times(1)).save(quoteModel);
		Assert.assertNotNull(quoteModel.getQuoteVisitiedStepList());
		Assert.assertEquals(progressId, quoteModel.getQuoteVisitiedStepList().get(0));
	}

	@Test
	public void shouldNotSaveIfCartIsNull()
	{
		final String progressId = "testId";

		Mockito.when(cartService.hasSessionCart()).thenReturn(false);

		defaultInsuranceCartFacade.setVisitedStepToQuoteInCurrentCart(progressId);

		verify(modelService, Mockito.times(0)).save(Mockito.any());
	}

	@Test
	public void shouldNotSetVisitedProgressIdMoreThanOnce()
	{
		final String progressId = "testId";

		final CartModel cartModel = new CartModel();
		final InsuranceQuoteModel quoteModel = new InsuranceQuoteModel();
		final List<String> progressIdList = new ArrayList<String>();
		progressIdList.add(progressId);
		cartModel.setInsuranceQuote(quoteModel);
		quoteModel.setQuoteVisitiedStepList(progressIdList);

		Mockito.when(cartService.hasSessionCart()).thenReturn(true);
		Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);

		defaultInsuranceCartFacade.setVisitedStepToQuoteInCurrentCart(progressId);

		verify(modelService, Mockito.times(0)).save(quoteModel);
		Assert.assertNotNull(quoteModel.getQuoteVisitiedStepList());
		Assert.assertEquals(progressId, quoteModel.getQuoteVisitiedStepList().get(0));
		Assert.assertEquals(1, quoteModel.getQuoteVisitiedStepList().size());
	}

	@Test
	public void shouldIsVisitedReturnFalseIfCartIsNull()
	{
		final String progressId = "testId";

		Mockito.when(cartService.hasSessionCart()).thenReturn(false);

		final boolean result = defaultInsuranceCartFacade.checkStepIsVisitedFromQuoteInCurrentCart(progressId);

		Assert.assertFalse(result);

	}

	@Test
	public void shouldReturnTrueForIsVisited()
	{
		final String progressId = "testId";

		final CartModel cartModel = new CartModel();
		final InsuranceQuoteModel quoteModel = new InsuranceQuoteModel();
		final List<String> progressIdList = new ArrayList<String>();
		progressIdList.add(progressId);
		cartModel.setInsuranceQuote(quoteModel);
		quoteModel.setQuoteVisitiedStepList(progressIdList);

		Mockito.when(cartService.hasSessionCart()).thenReturn(true);
		Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);

		final boolean result = defaultInsuranceCartFacade.checkStepIsVisitedFromQuoteInCurrentCart(progressId);

		Assert.assertEquals(progressId, quoteModel.getQuoteVisitiedStepList().get(0));
		Assert.assertTrue(result);
	}

	@Test
	public void shouldReturnFalseForIsVisited_Not_EmptyList()
	{
		final String progressId = "testId";
		final String progressId2 = "testId2";

		final CartModel cartModel = new CartModel();
		final InsuranceQuoteModel quoteModel = new InsuranceQuoteModel();
		final List<String> progressIdList = new ArrayList<String>();
		progressIdList.add(progressId);
		cartModel.setInsuranceQuote(quoteModel);
		quoteModel.setQuoteVisitiedStepList(progressIdList);

		Mockito.when(cartService.hasSessionCart()).thenReturn(true);
		Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);

		final boolean result = defaultInsuranceCartFacade.checkStepIsVisitedFromQuoteInCurrentCart(progressId2);

		Assert.assertEquals(progressId, quoteModel.getQuoteVisitiedStepList().get(0));
		Assert.assertFalse(result);
	}

	@Test
	public void shouldReturnFalseForIsVisited_EmptyList()
	{
		final String progressId = "testId";

		final CartModel cartModel = new CartModel();
		final InsuranceQuoteModel quoteModel = new InsuranceQuoteModel();
		cartModel.setInsuranceQuote(quoteModel);

		Mockito.when(cartService.hasSessionCart()).thenReturn(true);
		Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);

		final boolean result = defaultInsuranceCartFacade.checkStepIsVisitedFromQuoteInCurrentCart(progressId);
		Assert.assertFalse(result);
	}

	@Test
	public void shouldIsEnabledReturnTrueIfCartIsNull()
	{
		Mockito.when(cartService.hasSessionCart()).thenReturn(false);

		final boolean result = defaultInsuranceCartFacade.checkStepIsEnabledFromQuoteInCurrentCart();

		Assert.assertTrue(result);
	}

	@Test
	public void shouldIsEnabledReturnFalseForQuoteBind()
	{
		final CartModel cartModel = new CartModel();
		final InsuranceQuoteModel quoteModel = new InsuranceQuoteModel();
		cartModel.setInsuranceQuote(quoteModel);
		quoteModel.setState(QuoteBindingState.BIND);

		Mockito.when(cartService.hasSessionCart()).thenReturn(true);
		Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);

		final boolean result = defaultInsuranceCartFacade.checkStepIsEnabledFromQuoteInCurrentCart();

		Assert.assertFalse(result);
	}

	@Test
	public void shouldIsEnabledReturnTrueForQuoteUnbind()
	{
		final CartModel cartModel = new CartModel();
		final InsuranceQuoteModel quoteModel = new InsuranceQuoteModel();
		cartModel.setInsuranceQuote(quoteModel);
		quoteModel.setState(QuoteBindingState.UNBIND);

		Mockito.when(cartService.hasSessionCart()).thenReturn(true);
		Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);

		final boolean result = defaultInsuranceCartFacade.checkStepIsEnabledFromQuoteInCurrentCart();

		Assert.assertTrue(result);
	}

	@Test
	public void shouldBeSameCategoryInSessionCart() throws InvalidCartException
	{
		final String productCode = "product";
		final String defaultCategoryCode = "category";
		final ProductModel productModel = new ProductModel();
		final CartModel sessionCartModel = new CartModel();
		final List<AbstractOrderEntryModel> entries = Lists.newArrayList();
		sessionCartModel.setEntries(entries);

		final AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
		entries.add(entry);

		entry.setProduct(productModel);
		final CategoryModel categoryModel = new CategoryModel();
		categoryModel.setCode(defaultCategoryCode);
		productModel.setDefaultCategory(categoryModel);


		Mockito.when(productService.getProductForCode(productCode)).thenReturn(productModel);
		Mockito.when(cartService.getSessionCart()).thenReturn(sessionCartModel);
		Mockito.when(cartService.hasSessionCart()).thenReturn(true);

		final boolean result = defaultInsuranceCartFacade.isSameInsuranceInSessionCart(productCode);

		Assert.assertTrue(result);
	}

	@Test
	public void shouldBeDifferentCategoryInSessionCart() throws InvalidCartException
	{
		final String productCode = "product";
		final String defaultCategoryCode1 = "category1";
		final String defaultCategoryCode2 = "category2";

		final ProductModel productModel1 = new ProductModel();
		final ProductModel productModel2 = new ProductModel();

		final CartModel sessionCartModel = new CartModel();
		final List<AbstractOrderEntryModel> entries = Lists.newArrayList();
		sessionCartModel.setEntries(entries);

		final AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
		entries.add(entry);

		entry.setProduct(productModel1);

		final CategoryModel categoryModel1 = new CategoryModel();
		categoryModel1.setCode(defaultCategoryCode1);
		productModel1.setDefaultCategory(categoryModel1);

		final CategoryModel categoryModel2 = new CategoryModel();
		categoryModel2.setCode(defaultCategoryCode2);
		productModel2.setDefaultCategory(categoryModel2);


		Mockito.when(productService.getProductForCode(productCode)).thenReturn(productModel2);
		Mockito.when(cartService.getSessionCart()).thenReturn(sessionCartModel);
		Mockito.when(cartService.hasSessionCart()).thenReturn(true);

		final boolean result = defaultInsuranceCartFacade.isSameInsuranceInSessionCart(productCode);

		Assert.assertFalse(result);
	}

	@Test(expected = InvalidCartException.class)
	public void shouldThrowInvalidCartExceptionWithNSessionCart() throws InvalidCartException
	{
		final String productCode = "product";

		Mockito.when(cartService.hasSessionCart()).thenReturn(false);

		defaultInsuranceCartFacade.isSameInsuranceInSessionCart(productCode);
	}

	@Test(expected = InvalidCartException.class)
	public void shouldThrowInvalidCartExceptionWithNoEntries() throws InvalidCartException
	{
		final String productCode = "product";
		final CartModel sessionCartModel = new CartModel();
		final List<AbstractOrderEntryModel> emptyEntries = Lists.newArrayList();
		sessionCartModel.setEntries(emptyEntries);
		Mockito.when(cartService.getSessionCart()).thenReturn(sessionCartModel);
		Mockito.when(cartService.hasSessionCart()).thenReturn(true);

		defaultInsuranceCartFacade.isSameInsuranceInSessionCart(productCode);
	}
}
