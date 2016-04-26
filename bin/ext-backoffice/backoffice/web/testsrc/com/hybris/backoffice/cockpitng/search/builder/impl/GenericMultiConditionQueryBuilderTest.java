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
 */

package com.hybris.backoffice.cockpitng.search.builder.impl;


import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericValueCondition;
import de.hybris.platform.core.Operator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.hybris.backoffice.widgets.advancedsearch.engine.AdvancedSearchQueryData;
import com.hybris.cockpitng.search.data.SearchQueryCondition;
import com.hybris.cockpitng.search.data.SearchQueryConditionList;
import com.hybris.cockpitng.search.data.SearchAttributeDescriptor;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;


public class GenericMultiConditionQueryBuilderTest
{
	private static final String TYPE_CODE = "Product";

	@InjectMocks
	private GenericMultiConditionQueryBuilder genericMultiConditionQueryBuilder = new GenericMultiConditionQueryBuilder();
	@Mock
	private GenericConditionQueryBuilder genericQueryBuilder;
	@Mock
	private GenericConditionQueryBuilder localizedQueryBuilder;
	@Mock
	private TypeService typeService;

	@Before
	public void setUp()
	{

		MockitoAnnotations.initMocks(this);
	}


	@Test
	public void testSearchByStringAttribute()
	{
		// given
		final GenericQuery genericQuery = new GenericQuery(TYPE_CODE);
		final AdvancedSearchQueryData.Builder builder = new AdvancedSearchQueryData.Builder(TYPE_CODE);

		final List<SearchQueryCondition> entries = new LinkedList<>();

		final SearchQueryCondition condition = new SearchQueryCondition();
		condition.setOperator(ValueComparisonOperator.STARTS_WITH);
		condition.setDescriptor(new SearchAttributeDescriptor("code", 0));
		condition.setValue("abcd");
		entries.add(condition);


		builder.conditions(entries);
		final SearchQueryData searchQueryData = builder.build();

		final AttributeDescriptorModel attributeDescriptorModel = Mockito.mock(AttributeDescriptorModel.class);
		Mockito.when(attributeDescriptorModel.getLocalized()).thenReturn(Boolean.FALSE);
		Mockito.when(typeService.getAttributeDescriptor(TYPE_CODE, "code")).thenReturn(attributeDescriptorModel);

		final GenericValueCondition codeCondition = Mockito.mock(GenericValueCondition.class);
		Mockito.when(genericQueryBuilder.buildQuery(genericQuery, TYPE_CODE, condition, searchQueryData)).thenReturn(
				Lists.newArrayList(codeCondition));

		// when
		final List<GenericCondition> genericConditions = genericMultiConditionQueryBuilder.buildQuery(genericQuery, TYPE_CODE,
				new SearchAttributeDescriptor("code"), searchQueryData);
		// then
		Assertions.assertThat(genericConditions).hasSize(1);
		Assertions.assertThat(genericConditions.get(0)).isInstanceOf(GenericValueCondition.class);
		final GenericValueCondition returnedCondition = (GenericValueCondition) genericConditions.get(0);
		Assertions.assertThat(returnedCondition).isEqualTo(codeCondition);
		Mockito.verify(localizedQueryBuilder, Mockito.never()).buildQuery(genericQuery, TYPE_CODE, condition, searchQueryData);

	}


	@Test
	public void testSearchByDescriptonAttribute()
	{
		// given
		final GenericQuery genericQuery = new GenericQuery(TYPE_CODE);
		final AdvancedSearchQueryData.Builder builder = new AdvancedSearchQueryData.Builder(TYPE_CODE);

		final List<SearchQueryCondition> entries = new LinkedList<>();

		final SearchQueryCondition condition = new SearchQueryCondition();
		condition.setOperator(ValueComparisonOperator.STARTS_WITH);
		condition.setDescriptor(new SearchAttributeDescriptor("description", 0));
		condition.setValue("abcd");
		entries.add(condition);


		builder.conditions(entries);
		final SearchQueryData searchQueryData = builder.build();

		final AttributeDescriptorModel attributeDescriptorModel = Mockito.mock(AttributeDescriptorModel.class);
		Mockito.when(attributeDescriptorModel.getLocalized()).thenReturn(Boolean.TRUE);
		Mockito.when(typeService.getAttributeDescriptor(TYPE_CODE, "description")).thenReturn(attributeDescriptorModel);

		final GenericValueCondition descriptionCondition = Mockito.mock(GenericValueCondition.class);
		Mockito.when(localizedQueryBuilder.buildQuery(genericQuery, TYPE_CODE, condition, searchQueryData)).thenReturn(
				Lists.newArrayList(descriptionCondition));

		// when
		final List<GenericCondition> genericConditions = genericMultiConditionQueryBuilder.buildQuery(genericQuery, TYPE_CODE,
				new SearchAttributeDescriptor("description"), searchQueryData);
		// then
		Assertions.assertThat(genericConditions).hasSize(1);
		Assertions.assertThat(genericConditions.get(0)).isInstanceOf(GenericValueCondition.class);
		final GenericValueCondition returnedCondition = (GenericValueCondition) genericConditions.get(0);
		Assertions.assertThat(returnedCondition).isEqualTo(descriptionCondition);
		Mockito.verify(genericQueryBuilder, Mockito.never()).buildQuery(genericQuery, TYPE_CODE, condition, searchQueryData);

	}


	@Test
	public void testSearchByConditionList()
	{
		// given
		final GenericQuery genericQuery = new GenericQuery(TYPE_CODE);
		final AdvancedSearchQueryData.Builder builder = new AdvancedSearchQueryData.Builder(TYPE_CODE);



		final SearchQueryCondition conditionDescription = new SearchQueryCondition();
		conditionDescription.setOperator(ValueComparisonOperator.STARTS_WITH);
		conditionDescription.setDescriptor(new SearchAttributeDescriptor("description", 0));
		conditionDescription.setValue("abcd");

		final SearchQueryCondition conditionCode = new SearchQueryCondition();
		conditionCode.setOperator(ValueComparisonOperator.STARTS_WITH);
		conditionCode.setDescriptor(new SearchAttributeDescriptor("code", 0));
		conditionCode.setValue("1234");


		builder.conditions(SearchQueryConditionList.or(conditionCode, conditionDescription));
		final SearchQueryData searchQueryData = builder.build();

		final AttributeDescriptorModel attributeCodeDescriptorModel = Mockito.mock(AttributeDescriptorModel.class);
		Mockito.when(attributeCodeDescriptorModel.getLocalized()).thenReturn(Boolean.FALSE);
		final AttributeDescriptorModel attributeDescDescriptorModel = Mockito.mock(AttributeDescriptorModel.class);
		Mockito.when(attributeDescDescriptorModel.getLocalized()).thenReturn(Boolean.TRUE);
		Mockito.when(typeService.getAttributeDescriptor(TYPE_CODE, "code")).thenReturn(attributeCodeDescriptorModel);
		Mockito.when(typeService.getAttributeDescriptor(TYPE_CODE, "description")).thenReturn(attributeDescDescriptorModel);

		final GenericValueCondition codeCondition = Mockito.mock(GenericValueCondition.class);
		Mockito.when(genericQueryBuilder.buildQuery(genericQuery, TYPE_CODE, conditionCode, searchQueryData)).thenReturn(
				Lists.newArrayList(codeCondition));

		final GenericValueCondition descriptionCondition = Mockito.mock(GenericValueCondition.class);
		Mockito.when(localizedQueryBuilder.buildQuery(genericQuery, TYPE_CODE, conditionDescription, searchQueryData)).thenReturn(
				Lists.newArrayList(descriptionCondition));

		// when
		final List<GenericCondition> genericConditions = genericMultiConditionQueryBuilder.buildQuery(genericQuery, TYPE_CODE,
				new SearchAttributeDescriptor(StringUtils.EMPTY), searchQueryData);
		// then
		Assertions.assertThat(genericConditions).hasSize(1);
		Assertions.assertThat(genericConditions.get(0)).isInstanceOf(GenericConditionList.class);
		Assertions.assertThat(((GenericConditionList) genericConditions.get(0)).getOperator()).isEqualTo(Operator.OR);
		Assertions.assertThat(CollectionUtils.isEqualCollection(
				((GenericConditionList) genericConditions.get(0)).getConditionList(),
				Lists.newArrayList(codeCondition, descriptionCondition)));
		Mockito.verify(genericQueryBuilder, Mockito.times(1)).buildQuery(genericQuery, TYPE_CODE, conditionCode, searchQueryData);
		Mockito.verify(localizedQueryBuilder, Mockito.times(1)).buildQuery(genericQuery, TYPE_CODE, conditionDescription,
				searchQueryData);

	}
}
