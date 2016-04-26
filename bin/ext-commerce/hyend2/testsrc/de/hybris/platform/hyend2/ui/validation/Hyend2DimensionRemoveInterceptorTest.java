/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.hyend2.ui.validation;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.hyend2.model.Hyend2DimensionModel;
import de.hybris.platform.hyend2.model.Hyend2PrecedenceRuleModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * test for {@link Hyend2DimensionRemoveInterceptor}
 * 
 */
@UnitTest
public class Hyend2DimensionRemoveInterceptorTest
{
	Hyend2DimensionRemoveInterceptor hyend2DimensionRemoveInterceptor = new Hyend2DimensionRemoveInterceptor();

	@Mock
	private FlexibleSearchService mockFlexibleSearchService;

	@Mock
	private ModelService mockModelService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		hyend2DimensionRemoveInterceptor.setFlexibleSearchService(mockFlexibleSearchService);
		hyend2DimensionRemoveInterceptor.setModelService(mockModelService);
	}

	@Test
	public void testWhenDimensionAssignedToOnePR() throws InterceptorException
	{
		//given
		final Hyend2DimensionModel dimension = mock(Hyend2DimensionModel.class);
		final Hyend2PrecedenceRuleModel mockPrecedenceRule = mock(Hyend2PrecedenceRuleModel.class);
		final SearchResult mockSearchResult = mock(SearchResult.class);
		given(Integer.valueOf(mockSearchResult.getCount())).willReturn(Integer.valueOf(1));
		given(mockSearchResult.getResult()).willReturn(Collections.singletonList(mockPrecedenceRule));
		given(
				mockFlexibleSearchService.search("SELECT {PK} FROM {Hyend2PrecedenceRule} WHERE {fromDimension}=?dim OR {to}=?dim",
						Collections.singletonMap("dim", dimension))).willReturn(mockSearchResult);

		//when
		hyend2DimensionRemoveInterceptor.onRemove(dimension, null);

		//then
		Mockito.verify(mockModelService).remove(mockPrecedenceRule);
	}

	@Test
	public void testWhenDimensionNoAssignedToPR() throws InterceptorException
	{
		//given
		final Hyend2DimensionModel dimension = mock(Hyend2DimensionModel.class);
		final SearchResult mockSearchResult = mock(SearchResult.class);
		given(Integer.valueOf(mockSearchResult.getCount())).willReturn(Integer.valueOf(0));
		given(
				mockFlexibleSearchService.search("SELECT {PK} FROM {Hyend2PrecedenceRule} WHERE {fromDimension}=?dim OR {to}=?dim",
						Collections.singletonMap("dim", dimension))).willReturn(mockSearchResult);

		//when
		hyend2DimensionRemoveInterceptor.onRemove(dimension, null);

		//then
		Mockito.verifyNoMoreInteractions(mockModelService);
	}

	@Test
	public void testWhenNoDimensionPassed() throws InterceptorException
	{
		//given
		final ItemModel item = mock(ItemModel.class);

		//when
		hyend2DimensionRemoveInterceptor.onRemove(item, null);

		//then
		Mockito.verifyNoMoreInteractions(mockFlexibleSearchService);
		Mockito.verifyNoMoreInteractions(mockModelService);
	}
}
