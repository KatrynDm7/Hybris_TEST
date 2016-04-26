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
package de.hybris.platform.hyend2.ui.strategies;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class PrecedenceRulesAllowedTypesStrategyTest
{
	PrecedenceRulesAllowedTypesStrategy strategyToTest = new PrecedenceRulesAllowedTypesStrategy();
	private final List<String> returnedCodes = Arrays.asList("Category", "User");

	@Mock
	TypeService mockTypeService;

	@Mock
	FlexibleSearchService mockFlexibleSearchService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		strategyToTest.setFlexibleSearchService(mockFlexibleSearchService);
		strategyToTest.setTypeService(mockTypeService);
	}

	@Test
	public void testStrategyReturnsCorrectAmountOfTypes()
	{
		//given
		final FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {" + ComposedTypeModel.CODE + "} FROM { "
				+ ComposedTypeModel._TYPECODE + "} WHERE {" + ComposedTypeModel.HYEND2PRECEDENCERULESENABLED + "}=?par",
				Collections.singletonMap("par", Boolean.TRUE));
		query.setResultClassList(Collections.singletonList(String.class));
		final SearchResult mockResultSet = mock(SearchResult.class);
		given(mockResultSet.getResult()).willReturn(returnedCodes);
		given(mockFlexibleSearchService.search(Mockito.eq(query))).willReturn(mockResultSet);
		final ObjectType mockObjectType1 = mock(ObjectType.class);
		final ObjectType mockObjectType2 = mock(ObjectType.class);
		given(mockTypeService.getObjectType(returnedCodes.get(0))).willReturn(mockObjectType1);
		given(mockTypeService.getObjectType(returnedCodes.get(1))).willReturn(mockObjectType2);

		//when
		final Set<ObjectType> allowedTypes = strategyToTest.getAllowedTypes();

		//then
		assertThat(allowedTypes).hasSize(2);
		assertThat(allowedTypes).containsOnly(mockObjectType1, mockObjectType2);
	}
}
