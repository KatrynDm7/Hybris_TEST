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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSubQueryCondition;
import de.hybris.platform.core.GenericValueCondition;
import de.hybris.platform.core.Operator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.impl.DefaultTypeService;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.hybris.backoffice.widgets.advancedsearch.engine.AdvancedSearchQueryData;
import com.hybris.cockpitng.search.data.SearchAttributeDescriptor;
import com.hybris.cockpitng.search.data.SearchQueryCondition;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;


@IntegrationTest
public class GenericConditionQueryBuilderTest extends ServicelayerTransactionalTest
{

	private static final String typeCode = "Product";
	private static final String typeCodeProductReference = "ProductReference";
	private final Set<Character> queryBuilderSeparators = Sets.newHashSet(' ', ',', ';', '\t', '\n', '\r');
	@Resource
	private DefaultTypeService typeService;
	@Resource
	private ModelService modelService;
	private GenericConditionQueryBuilder queryBuilder;
	private CatalogVersionModel version1, version2;

	@Before
	public void prepare()
	{
		queryBuilder = new GenericConditionQueryBuilder();
		queryBuilder.setTypeService(typeService);
		queryBuilder.setSeparators(queryBuilderSeparators);
		prepareTestObjects();
	}

	private void prepareTestObjects()
	{
		final CatalogModel catalogModel = modelService.create(CatalogModel.class);
		catalogModel.setId("catalogModelId");
		modelService.save(catalogModel);

		version1 = modelService.create(CatalogVersionModel.class);
		version1.setVersion("version1");
		version1.setCatalog(catalogModel);
		modelService.save(version1);

		version2 = modelService.create(CatalogVersionModel.class);
		version2.setVersion("version2");
		version2.setCatalog(catalogModel);
		modelService.save(version2);
	}

	@Test
	public void testSearchByStringAttribute()
	{
		// given
		final GenericQuery genericQuery = new GenericQuery(typeCode);
		final AdvancedSearchQueryData.Builder builder = new AdvancedSearchQueryData.Builder(typeCode);

		final List<SearchQueryCondition> entries = new LinkedList<>();

		final SearchQueryCondition entry = new SearchQueryCondition();
		entry.setOperator(ValueComparisonOperator.STARTS_WITH);
		entry.setDescriptor(new SearchAttributeDescriptor("code", 0));
		entry.setValue("abcd");
		entries.add(entry);


		builder.conditions(entries);
		final SearchQueryData searchQueryData = builder.build();
		// when
		final List<GenericCondition> genericConditions = queryBuilder.buildQuery(genericQuery, typeCode,
				new SearchAttributeDescriptor("code"), searchQueryData);
		// then
		Assertions.assertThat(genericConditions).hasSize(1);
		Assertions.assertThat(genericConditions.get(0)).isInstanceOf(GenericValueCondition.class);
		final GenericValueCondition returnedCondition = (GenericValueCondition) genericConditions.get(0);
		Assertions.assertThat(returnedCondition.getField().getQualifier()).isEqualTo("code");
		Assertions.assertThat(returnedCondition.getOperator()).isEqualTo(Operator.LIKE);
		Assertions.assertThat(returnedCondition.getValue()).isInstanceOf(String.class);
		Assertions.assertThat(returnedCondition.getValue()).isEqualTo("abcd%");
	}

	@Test
	public void testSearchByManyStringTokens()
	{
		// given
		final GenericQuery genericQuery = new GenericQuery(typeCode);
		final AdvancedSearchQueryData.Builder builder = new AdvancedSearchQueryData.Builder(typeCode);

		final List<SearchQueryCondition> entries = new LinkedList<>();

		final SearchQueryCondition entry = new SearchQueryCondition();
		entry.setOperator(ValueComparisonOperator.ENDS_WITH);
		entry.setDescriptor(new SearchAttributeDescriptor("code"));
		entry.setValue("abcd efgh");
		entries.add(entry);

		builder.conditions(entries).tokenizable(true);
		final SearchQueryData searchQueryData = builder.build();
		// when
		final List<GenericCondition> genericConditions = queryBuilder.buildQuery(genericQuery, typeCode,
				new SearchAttributeDescriptor("code"), searchQueryData);
		// then
		Assertions.assertThat(genericConditions).hasSize(1);
		Assertions.assertThat(genericConditions.get(0)).isInstanceOf(GenericConditionList.class);
		final GenericConditionList returnedCondition = (GenericConditionList) genericConditions.get(0);
		Assertions.assertThat(returnedCondition.getConditionList()).hasSize(2);
		Assertions.assertThat(returnedCondition.getConditionList().get(0)).isInstanceOf(GenericValueCondition.class);
		Assertions.assertThat(returnedCondition.getConditionList().get(1)).isInstanceOf(GenericValueCondition.class);
		final GenericValueCondition condition1 = (GenericValueCondition) returnedCondition.getConditionList().get(0);
		final GenericValueCondition condition2 = (GenericValueCondition) returnedCondition.getConditionList().get(1);
		Assertions.assertThat(condition1.getField().getQualifier()).isEqualTo("code");
		Assertions.assertThat(condition1.getOperator()).isEqualTo(Operator.LIKE);
		Assertions.assertThat(condition1.getValue()).isInstanceOf(String.class);
		Assertions.assertThat(condition1.getValue()).isEqualTo("%abcd");
		Assertions.assertThat(condition2.getField().getQualifier()).isEqualTo("code");
		Assertions.assertThat(condition2.getOperator()).isEqualTo(Operator.LIKE);
		Assertions.assertThat(condition2.getValue()).isInstanceOf(String.class);
		Assertions.assertThat(condition2.getValue()).isEqualTo("%efgh");
	}


	@Test
	public void testSearchByEnumAttribute()
	{
		// given
		final GenericQuery genericQuery = new GenericQuery(typeCode);
		final AdvancedSearchQueryData.Builder builder = new AdvancedSearchQueryData.Builder(typeCode);

		final List<SearchQueryCondition> entries = new LinkedList<>();
		final SearchQueryCondition entry = new SearchQueryCondition();
		entry.setOperator(ValueComparisonOperator.EQUALS);
		entry.setDescriptor(new SearchAttributeDescriptor("approvalStatus", 0));
		entry.setValue(ArticleApprovalStatus.APPROVED);
		entries.add(entry);
		builder.conditions(entries);

		final SearchQueryData searchQueryData = builder.build();
		// when
		final List<GenericCondition> genericConditions = queryBuilder.buildQuery(genericQuery, typeCode,
				new SearchAttributeDescriptor("approvalStatus"), searchQueryData);
		// then
		Assertions.assertThat(genericConditions).hasSize(1);
		Assertions.assertThat(genericConditions.get(0)).isInstanceOf(GenericValueCondition.class);
		final GenericValueCondition returnedCondition = (GenericValueCondition) genericConditions.get(0);
		Assertions.assertThat(returnedCondition.getField().getQualifier()).isEqualTo("approvalStatus");
		Assertions.assertThat(returnedCondition.getOperator()).isEqualTo(Operator.EQUAL);
		Assertions.assertThat(returnedCondition.getValue()).isInstanceOf(ArticleApprovalStatus.class);
		Assertions.assertThat(returnedCondition.getValue()).isEqualTo(ArticleApprovalStatus.APPROVED);
	}

	@Test
	public void testSearchBySingleReference()
	{
		// given
		final GenericQuery genericQuery = new GenericQuery(typeCode);
		final AdvancedSearchQueryData.Builder builder = new AdvancedSearchQueryData.Builder(typeCode);
		final List<SearchQueryCondition> entries = new LinkedList<>();

		final SearchQueryCondition entry = new SearchQueryCondition();
		entry.setOperator(ValueComparisonOperator.EQUALS);
		entry.setDescriptor(new SearchAttributeDescriptor("catalogVersion"));
		entry.setValue(version1);
		entries.add(entry);

		builder.conditions(entries);
		final SearchQueryData searchQueryData = builder.build();

		// when
		final List<GenericCondition> genericConditions = queryBuilder.buildQuery(genericQuery, typeCode,
				new SearchAttributeDescriptor("catalogVersion"), searchQueryData);
		// then
		Assertions.assertThat(genericConditions).hasSize(1);
		Assertions.assertThat(genericConditions.get(0)).isInstanceOf(GenericValueCondition.class);
		final GenericValueCondition returnedCondition = (GenericValueCondition) genericConditions.get(0);
		Assertions.assertThat(returnedCondition.getField().getQualifier()).isEqualTo("catalogVersion");
		Assertions.assertThat(returnedCondition.getOperator()).isEqualTo(Operator.EQUAL);
		Assertions.assertThat(returnedCondition.getValue()).isInstanceOf(CatalogVersionModel.class);
		Assertions.assertThat(returnedCondition.getValue()).isEqualTo(version1);
	}

	@Test
	public void testSearchBySingleReference2Conditions()
	{
		// given
		final GenericQuery genericQuery = new GenericQuery(typeCode);
		final AdvancedSearchQueryData.Builder builder = new AdvancedSearchQueryData.Builder(typeCode);
		final List<SearchQueryCondition> entries = new LinkedList<>();

		final SearchQueryCondition entry1 = new SearchQueryCondition();
		entry1.setOperator(ValueComparisonOperator.EQUALS);
		entry1.setDescriptor(new SearchAttributeDescriptor("catalogVersion", 0));
		entry1.setValue(version1);
		entries.add(entry1);

		final SearchQueryCondition entry2 = new SearchQueryCondition();
		entry2.setOperator(ValueComparisonOperator.EQUALS);
		entry2.setDescriptor(new SearchAttributeDescriptor("catalogVersion", 1));
		entry2.setValue(version2);
		entries.add(entry2);

		builder.conditions(entries);
		final SearchQueryData searchQueryData = builder.build();
		// when
		final List<GenericCondition> genericConditions = queryBuilder.buildQuery(genericQuery, typeCode,
				new SearchAttributeDescriptor("catalogVersion"), searchQueryData);
		genericConditions.addAll(queryBuilder.buildQuery(genericQuery, typeCode,
				new SearchAttributeDescriptor("catalogVersion", 1), searchQueryData));
		// then
		Assertions.assertThat(genericConditions).hasSize(2);
		Assertions.assertThat(genericConditions.get(0)).isInstanceOf(GenericValueCondition.class);
		Assertions.assertThat(genericConditions.get(1)).isInstanceOf(GenericValueCondition.class);
		final GenericValueCondition returnedCondition = (GenericValueCondition) genericConditions.get(0);
		Assertions.assertThat(returnedCondition.getField().getQualifier()).isEqualTo("catalogVersion");
		Assertions.assertThat(returnedCondition.getOperator()).isEqualTo(Operator.EQUAL);
		Assertions.assertThat(returnedCondition.getValue()).isInstanceOf(CatalogVersionModel.class);
		Assertions.assertThat(returnedCondition.getValue()).isEqualTo(version1);
		final GenericValueCondition returnedCondition2 = (GenericValueCondition) genericConditions.get(1);
		Assertions.assertThat(returnedCondition2.getField().getQualifier()).isEqualTo("catalogVersion");
		Assertions.assertThat(returnedCondition2.getOperator()).isEqualTo(Operator.EQUAL);
		Assertions.assertThat(returnedCondition2.getValue()).isInstanceOf(CatalogVersionModel.class);
		Assertions.assertThat(returnedCondition2.getValue()).isEqualTo(version2);
	}

	@Test
	public void testSearchByCollectionTypeShouldReturnEmptyConditionList()
	{
		// project.detail
		// given
		final GenericQuery genericQuery = new GenericQuery(typeCode);
		final AdvancedSearchQueryData.Builder builder = new AdvancedSearchQueryData.Builder(typeCode);

		final MediaModel detail = modelService.create(MediaModel.class);
		detail.setCatalogVersion(version1);
		detail.setCode("someCode");
		modelService.save(detail);

		final List<SearchQueryCondition> entries = new LinkedList<>();
		final SearchQueryCondition entry1 = new SearchQueryCondition();
		entry1.setOperator(ValueComparisonOperator.CONTAINS);
		entry1.setDescriptor(new SearchAttributeDescriptor("detail"));
		entry1.setValue(detail);
		entries.add(entry1);

		builder.conditions(entries);
		final SearchQueryData searchQueryData = builder.build();
		// when
		final List<GenericCondition> conditions = queryBuilder.buildQuery(genericQuery, typeCode, new SearchAttributeDescriptor(
				"detail"), searchQueryData);
		Assertions.assertThat(conditions).isEmpty();
	}



	@Test
	public void testSearchByManyToOneRelation()
	{
		final ProductModel source = modelService.create(ProductModel.class);
		source.setCatalogVersion(version1);
		source.setCode("productCodeSource");
		modelService.save(source);
		final ProductModel target = modelService.create(ProductModel.class);
		target.setCatalogVersion(version1);
		target.setCode("productCodeTarget");
		modelService.save(target);
		final ProductReferenceModel productReference = modelService.create(ProductReferenceModel.class);
		productReference.setActive(Boolean.TRUE);
		productReference.setPreselected(Boolean.TRUE);
		productReference.setSource(source);
		productReference.setTarget(target);
		modelService.save(productReference);

		final GenericQuery genericQuery = new GenericQuery(typeCode);
		final List<SearchQueryCondition> entries = new LinkedList<>();

		final SearchQueryCondition entry1 = new SearchQueryCondition();
		entry1.setOperator(ValueComparisonOperator.EQUALS);
		entry1.setDescriptor(new SearchAttributeDescriptor("source"));
		entry1.setValue(source);
		entries.add(entry1);

		final AdvancedSearchQueryData.Builder builder = new AdvancedSearchQueryData.Builder(typeCodeProductReference);
		builder.conditions(entries);
		final SearchQueryData searchQueryData = builder.build();
		// when
		final List<GenericCondition> genericConditions = queryBuilder.buildQuery(genericQuery, typeCodeProductReference,
				new SearchAttributeDescriptor("source"), searchQueryData);
		Assertions.assertThat(genericConditions).isNotNull();

		Assertions.assertThat(genericConditions).hasSize(1);
		Assertions.assertThat(genericConditions.get(0)).isInstanceOf(GenericValueCondition.class);
		final GenericValueCondition valueCondition = (GenericValueCondition) genericConditions.get(0);
		Assertions.assertThat(valueCondition.getOperator()).isEqualTo(Operator.EQUAL);
	}

	@Test
	public void testSearchByManyToManyRelation()
	{
		final CategoryModel categoryModel = modelService.create(CategoryModel.class);
		categoryModel.setCatalogVersion(version1);
		categoryModel.setCode("categoryCode1");
		modelService.save(categoryModel);

		final GenericQuery genericQuery = new GenericQuery(typeCode);

		final List<SearchQueryCondition> entries = new LinkedList<>();
		final SearchQueryCondition entry1 = new SearchQueryCondition();
		entry1.setOperator(ValueComparisonOperator.CONTAINS);
		entry1.setDescriptor(new SearchAttributeDescriptor("supercategories"));
		entry1.setValue(categoryModel);
		entries.add(entry1);

		final AdvancedSearchQueryData.Builder builder = new AdvancedSearchQueryData.Builder(typeCode);
		builder.conditions(entries);
		final SearchQueryData searchQueryData = builder.build();
		// when
		final List<GenericCondition> genericConditions = queryBuilder.buildQuery(genericQuery, typeCode,
				new SearchAttributeDescriptor("supercategories"), searchQueryData);
		Assertions.assertThat(genericConditions).isNotNull();
		Assertions.assertThat(genericConditions).hasSize(1);
		Assertions.assertThat(genericConditions.get(0)).isInstanceOf(GenericSubQueryCondition.class);
		final GenericSubQueryCondition subQueryCondition = (GenericSubQueryCondition) genericConditions.get(0);
		Assertions.assertThat(subQueryCondition.getOperator()).isEqualTo(Operator.IN);
	}
}
