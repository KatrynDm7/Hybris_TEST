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

import static org.junit.Assert.assertEquals;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.insurance.data.QuoteItemRequestData;
import de.hybris.platform.commercefacades.insurance.data.QuoteRequestData;
import de.hybris.platform.commercefacades.insurance.data.QuoteResponseData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.financialfacades.facades.QuoteServiceFacade;
import de.hybris.platform.financialservices.enums.QuoteWorkflowStatus;
import de.hybris.platform.financialservices.model.InsuranceQuoteModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class DefaultQuoteServiceFacadeTest
{
	@InjectMocks
	private QuoteServiceFacade quoteServiceFacade;

	@Mock
	private FlexibleSearchService flexibleSearchService;

	@Before
	public void setup()
	{
		quoteServiceFacade = new MockQuoteServiceFacade();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testRequestQuoteWorkFlowTypeLife()
	{
		final QuoteRequestData requestData = new QuoteRequestData();
		final QuoteItemRequestData requestItem = new QuoteItemRequestData();
		final List<QuoteItemRequestData> requestDataList = new ArrayList<QuoteItemRequestData>();

		requestItem.setId("insurances_life");
		requestDataList.add(requestItem);
		requestData.setItems(requestDataList);

		final QuoteResponseData responseData = quoteServiceFacade.requestQuoteWorkFlowType(requestData);

		assertEquals("In correct Quote WorkFlow type", responseData.getItems().get(0).getId(), "LIFE");
	}

	@Test
	public void testRequestQuoteWorkFlowTypeDefault()
	{
		final QuoteRequestData requestData = new QuoteRequestData();
		final QuoteItemRequestData requestItem = new QuoteItemRequestData();
		final List<QuoteItemRequestData> requestDataList = new ArrayList<QuoteItemRequestData>();

		requestItem.setId("any_other_category");
		requestDataList.add(requestItem);
		requestData.setItems(requestDataList);

		final QuoteResponseData responseData = quoteServiceFacade.requestQuoteWorkFlowType(requestData);

		assertEquals("In correct Quote WorkFlow type", responseData.getItems().get(0).getId(), "DEFAULT");

	}

	@Test
	public void testRequestQuoteWorkFlowStatusShouldReturnPending()
	{
		final QuoteRequestData requestData = new QuoteRequestData();
		final QuoteItemRequestData requestItem = new QuoteItemRequestData();
		final List<QuoteItemRequestData> requestDataList = new ArrayList<>();

		final String cartCode = "cartCode_1234";

		requestItem.setId(cartCode);
		requestDataList.add(requestItem);
		requestData.setItems(requestDataList);

		final CartModel cart = new CartModel();
		cart.setCode(cartCode);
		final InsuranceQuoteModel quoteModel = new InsuranceQuoteModel();
		quoteModel.setQuoteWorkflowStatus(QuoteWorkflowStatus.PENDING);
		final CategoryModel defaultCategory = new CategoryModel();
		defaultCategory.setCode("insurances_life");
		final CartEntryModel entryModel = new CartEntryModel();
		entryModel.setProduct(new ProductModel());
		entryModel.getProduct().setDefaultCategory(defaultCategory);
		final ArrayList<AbstractOrderEntryModel> entryList = new ArrayList<AbstractOrderEntryModel>();
		entryList.add(entryModel);
		cart.setEntries(entryList);
		cart.setInsuranceQuote(quoteModel);

		final ArrayList<Object> objectList = new ArrayList<Object>();
		objectList.add(cart);

		Mockito.when(flexibleSearchService.getModelsByExample(Mockito.any())).thenReturn(objectList);

		final QuoteResponseData responseData = quoteServiceFacade.requestQuoteWorkFlowStatus(requestData);

		assertEquals("PENDING", responseData.getItems().get(0).getId());

	}

	@Test
	public void testRequestQuoteWorkFlowStatusShouldReturnApprovedForNonLifeInsuranceCategory()
	{
		final QuoteRequestData requestData = new QuoteRequestData();
		final QuoteItemRequestData requestItem = new QuoteItemRequestData();
		final List<QuoteItemRequestData> requestDataList = new ArrayList<>();

		final String cartCode = "cartCode_1234";

		requestItem.setId(cartCode);
		requestDataList.add(requestItem);
		requestData.setItems(requestDataList);

		final CartModel cart = new CartModel();
		cart.setCode(cartCode);
		final InsuranceQuoteModel quoteModel = new InsuranceQuoteModel();
		quoteModel.setQuoteWorkflowStatus(QuoteWorkflowStatus.PENDING);
		final CategoryModel defaultCategory = new CategoryModel();
		defaultCategory.setCode("insurances_AUTO");
		final CartEntryModel entryModel = new CartEntryModel();
		entryModel.setProduct(new ProductModel());
		entryModel.getProduct().setDefaultCategory(defaultCategory);
		final ArrayList<AbstractOrderEntryModel> entryList = new ArrayList<AbstractOrderEntryModel>();
		entryList.add(entryModel);
		cart.setEntries(entryList);
		cart.setInsuranceQuote(quoteModel);

		final ArrayList<Object> objectList = new ArrayList<Object>();
		objectList.add(cart);

		Mockito.when(flexibleSearchService.getModelsByExample(Mockito.any())).thenReturn(objectList);

		final QuoteResponseData responseData = quoteServiceFacade.requestQuoteWorkFlowStatus(requestData);

		assertEquals("APPROVED", responseData.getItems().get(0).getId());
	}
}
