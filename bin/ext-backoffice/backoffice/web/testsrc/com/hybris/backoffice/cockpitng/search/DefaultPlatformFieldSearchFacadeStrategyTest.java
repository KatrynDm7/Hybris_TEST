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
package com.hybris.backoffice.cockpitng.search;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.genericsearch.GenericSearchService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hybris.backoffice.cockpitng.dataaccess.facades.search.DefaultPlatformFieldSearchFacadeStrategy;
import com.hybris.backoffice.cockpitng.search.builder.impl.GenericConditionQueryBuilder;
import com.hybris.backoffice.cockpitng.search.builder.impl.GenericMultiConditionQueryBuilder;
import com.hybris.backoffice.cockpitng.search.builder.impl.LocalizedGenericConditionQueryBuilder;
import com.hybris.backoffice.widgets.advancedsearch.engine.AdvancedSearchQueryData;
import com.hybris.cockpitng.dataaccess.facades.search.FieldSearchFacadeStrategy;
import com.hybris.cockpitng.search.data.SearchQueryCondition;
import com.hybris.cockpitng.search.data.SearchQueryConditionList;
import com.hybris.cockpitng.search.data.SearchAttributeDescriptor;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.SimpleSearchQueryData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import com.hybris.cockpitng.search.data.pageable.Pageable;


@IntegrationTest
public class DefaultPlatformFieldSearchFacadeStrategyTest extends ServicelayerTransactionalTest
{

	private static final String TEST_CATALOG_ID = "testCatalog";
	private static final String TEST_VERSION = "summer";
	private static final int PRODUCT_COUNT = 30;
	private static final String TEST_PRODUCT_CODE = "testProductCode_";
	private static final String TEST_PRODUCT_EAN = "testProductEAN_";
	private static final String TEST_MANUFACTURER = "XYZ";
	private static final String CODE ="code";
	private static final String EAN ="ean";
	private static final String PRODUCT_CODE ="Product";

	private final Set<Character> queryBuilderSeparators = Sets.newHashSet(' ', ',', ';', '\t', '\n', '\r');
	private FieldSearchFacadeStrategy<ItemModel> fieldSearchFacadeStrategy;
	@Resource
	private GenericSearchService genericSearchService;
	@Resource
	private ModelService modelService;
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	private I18NService i18nService;
	@Resource
	private TypeService typeService;
	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Before
	public void setUpTestData()
	{
		fieldSearchFacadeStrategy = new DefaultPlatformFieldSearchFacadeStrategy<>();
		((DefaultPlatformFieldSearchFacadeStrategy) fieldSearchFacadeStrategy).setGenericSearchService(genericSearchService);

		final GenericConditionQueryBuilder genericConditionBuilder = new GenericConditionQueryBuilder();
		genericConditionBuilder.setTypeService(typeService);
		genericConditionBuilder.setSeparators(queryBuilderSeparators);



		final LocalizedGenericConditionQueryBuilder localizedConditionQueryBuilder = new LocalizedGenericConditionQueryBuilder();
		localizedConditionQueryBuilder.setCommonI18NService(commonI18NService);
		localizedConditionQueryBuilder.setI18nService(i18nService);
		localizedConditionQueryBuilder.setTypeService(typeService);
		localizedConditionQueryBuilder.setSeparators(queryBuilderSeparators);

		((DefaultPlatformFieldSearchFacadeStrategy) fieldSearchFacadeStrategy).setTypeService(typeService);


		final GenericMultiConditionQueryBuilder multiCondition = new GenericMultiConditionQueryBuilder();
		multiCondition.setTypeService(typeService);
		multiCondition.setGenericQueryBuilder(genericConditionBuilder);
		multiCondition.setLocalizedQueryBuilder(localizedConditionQueryBuilder);
		((DefaultPlatformFieldSearchFacadeStrategy) fieldSearchFacadeStrategy).setGenericMultiConditionQueryBuilder(multiCondition);


		final CatalogModel testCatalog = createTestCatalog();
		final CatalogVersionModel testCatalogVersion = createTestCatalogVersion(testCatalog);
		for (int i = 0; i < PRODUCT_COUNT; i++)
		{
			createTestProduct(i, testCatalogVersion);
		}
		modelService.saveAll();
	}

	private void createTestProduct(final int index, final CatalogVersionModel testCatalogVersion)
	{
		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode(TEST_PRODUCT_CODE + index);
		product.setEan(TEST_PRODUCT_EAN + index);
		product.setManufacturerAID(TEST_MANUFACTURER);
		product.setManufacturerName(TEST_MANUFACTURER);
		product.setCatalogVersion(testCatalogVersion);
	}

	private CatalogVersionModel createTestCatalogVersion(final CatalogModel testCatalog)
	{
		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setCatalog(testCatalog);
		catalogVersion.setVersion(TEST_VERSION);
		return catalogVersion;
	}

	private CatalogModel createTestCatalog()
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId(TEST_CATALOG_ID);
		return catalog;
	}

	@Test
	public void testFetchAllProducts() // NOPMD
	{
		final SimpleSearchQueryData sqd = new SimpleSearchQueryData(PRODUCT_CODE);
		sqd.setPageSize(PRODUCT_COUNT);

		Pageable<ItemModel> pageable = fieldSearchFacadeStrategy.search(sqd);
		Assertions.assertThat(pageable).isNotNull();
		Assertions.assertThat(pageable.getCurrentPage()).hasSize(PRODUCT_COUNT);
		Assertions.assertThat(pageable.hasNextPage()).isFalse();

		sqd.setPageSize(31);
		pageable = fieldSearchFacadeStrategy.search(sqd);
		Assertions.assertThat(pageable.getCurrentPage()).hasSize(PRODUCT_COUNT);
		Assertions.assertThat(pageable.hasNextPage()).isFalse();
	}

	@Test
	public void testFetchProductsPaging() // NOPMD
	{
		final SimpleSearchQueryData sqd = new SimpleSearchQueryData("Product");
		sqd.setPageSize(5);

		// take first 5
		final Pageable<ItemModel> pageable = fieldSearchFacadeStrategy.search(sqd);
		Assertions.assertThat(pageable).isNotNull();
		Assertions.assertThat(pageable.getCurrentPage()).hasSize(5);
		Assertions.assertThat(pageable.hasNextPage()).isTrue();
		Assertions.assertThat(pageable.hasPreviousPage()).isFalse();
		Assertions.assertThat(pageable.previousPage()).isEmpty();

		// 10
		Assertions.assertThat(pageable.nextPage()).hasSize(5);
		Assertions.assertThat(pageable.hasNextPage()).isTrue();

		// 15
		Assertions.assertThat(pageable.nextPage()).hasSize(5);
		Assertions.assertThat(pageable.hasNextPage()).isTrue();

		// 20
		Assertions.assertThat(pageable.nextPage()).hasSize(5);
		Assertions.assertThat(pageable.hasNextPage()).isTrue();

		// 25
		final List<ItemModel> page25_1 = pageable.nextPage();
		Assertions.assertThat(page25_1).hasSize(5);
		Assertions.assertThat(pageable.hasNextPage()).isTrue();

		// 25 again
		final List<ItemModel> page25_2 = pageable.getCurrentPage();
		Assertions.assertThat(page25_2).hasSize(5);
		Assertions.assertThat(pageable.hasNextPage()).isTrue();
		Assertions.assertThat(page25_1).isSameAs(page25_2);

		// 30
		Assertions.assertThat(pageable.nextPage()).hasSize(5);
		Assertions.assertThat(pageable.hasNextPage()).isFalse();
		Assertions.assertThat(pageable.nextPage()).isEmpty();

		pageable.setPageNumber(10);
		Assertions.assertThat(pageable.getCurrentPage()).isEmpty();
	}

	@Test
	public void testFetchProductsPaging2() // NOPMD
	{
		final SimpleSearchQueryData sqd = new SimpleSearchQueryData(PRODUCT_CODE);
		sqd.setPageSize(20);

		// take first 20
		final Pageable<ItemModel> pageable = fieldSearchFacadeStrategy.search(sqd);
		Assertions.assertThat(pageable).isNotNull();
		Assertions.assertThat(pageable.getCurrentPage()).hasSize(20);
		Assertions.assertThat(pageable.hasNextPage()).isTrue();

		// remaining 10
		Assertions.assertThat(pageable.nextPage()).hasSize(10);
		Assertions.assertThat(pageable.hasNextPage()).isFalse();
	}


	@Test
	public void testFetchProductsGetPageByNumber() // NOPMD
	{
		final SimpleSearchQueryData sqd = new SimpleSearchQueryData(PRODUCT_CODE);
		sqd.setPageSize(11);

		// take last 8 (page no 2)
		final Pageable<ItemModel> pageable = fieldSearchFacadeStrategy.search(sqd);
		Assertions.assertThat(pageable).isNotNull();
		final List<ItemModel> nextpage = pageable.nextPage();
		Assertions.assertThat(nextpage).hasSize(11);
		Assertions.assertThat(pageable.hasPreviousPage()).isTrue();
		Assertions.assertThat(pageable.hasNextPage()).isTrue();

		final List<ItemModel> last = pageable.nextPage();
		Assertions.assertThat(last).hasSize(8);
		Assertions.assertThat(pageable.hasPreviousPage()).isTrue();
		Assertions.assertThat(pageable.hasNextPage()).isFalse();

		final List<ItemModel> lastpage2 = pageable.getCurrentPage();
		Assertions.assertThat(last).isSameAs(lastpage2);
		Assertions.assertThat(pageable.hasNextPage()).isFalse();
		Assertions.assertThat(pageable.hasPreviousPage()).isTrue();

		final List<ItemModel> middlepage = pageable.previousPage();
		Assertions.assertThat(middlepage).hasSize(11);
		Assertions.assertThat(pageable.hasNextPage()).isTrue();
		Assertions.assertThat(pageable.hasPreviousPage()).isTrue();

		final List<ItemModel> middlepage2 = pageable.getCurrentPage();
		Assertions.assertThat(middlepage).isSameAs(middlepage2);
		Assertions.assertThat(pageable.hasNextPage()).isTrue();
		Assertions.assertThat(pageable.hasPreviousPage()).isTrue();

		final List<ItemModel> firstPage = pageable.previousPage();
		Assertions.assertThat(firstPage).hasSize(11);
		Assertions.assertThat(pageable.hasNextPage()).isTrue();
		Assertions.assertThat(pageable.hasPreviousPage()).isFalse();

		final List<ItemModel> firstPage2 = pageable.getCurrentPage();
		Assertions.assertThat(firstPage).isSameAs(firstPage2);
		Assertions.assertThat(pageable.hasNextPage()).isTrue();
		Assertions.assertThat(pageable.hasPreviousPage()).isFalse();

		Assertions.assertThat(pageable.previousPage()).isEmpty();
	}


	@Test
	public void testFetchProductsGetPageByTooHighNumber() // NOPMD
	{
		final SimpleSearchQueryData sqd = new SimpleSearchQueryData(PRODUCT_CODE);
		sqd.setPageSize(10);

		final Pageable<ItemModel> pageable = fieldSearchFacadeStrategy.search(sqd);

		final List<ItemModel> firstPage = pageable.getCurrentPage();
		// we only have 3 pages
		pageable.setPageNumber(100);
		Assertions.assertThat(pageable.getCurrentPage()).isEmpty();

		pageable.setPageNumber(0);
		final List<ItemModel> firstPageAgain = pageable.getCurrentPage();
		Assertions.assertThat(firstPage).isEqualTo(firstPageAgain);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFetchProductsWithNegativePageSize() // NOPMD
	{
		final SimpleSearchQueryData sqd = new SimpleSearchQueryData(PRODUCT_CODE);
		sqd.setPageSize(-1);

		fieldSearchFacadeStrategy.search(sqd);
	}

	@Test
	public void testFetchProductsByCodeEqual() // NOPMD
	{
		final String code = "testProductCode_10";

		final SimpleSearchQueryData sqd = new SimpleSearchQueryData(PRODUCT_CODE);
		sqd.setPageSize(10);
		sqd.setAttributes(Collections.singletonList(new SearchAttributeDescriptor(CODE)));
		sqd.setSearchQueryText(code);

		final Pageable<ItemModel> pageable = fieldSearchFacadeStrategy.search(sqd);
		Assertions.assertThat(pageable.getCurrentPage()).hasSize(1);
		final ProductModel testProduct10 = (ProductModel) (pageable.getCurrentPage().get(0));
		Assertions.assertThat(testProduct10.getCode()).isEqualTo(code);
	}

	@Test
	public void testFetchProductsByCodeOREanContains() // NOPMD
	{
		final String text = "EAN";

		final SimpleSearchQueryData sqd = new SimpleSearchQueryData(PRODUCT_CODE);
		sqd.setPageSize(PRODUCT_COUNT);

		sqd.setAttributes(Arrays.asList(new SearchAttributeDescriptor(CODE), new SearchAttributeDescriptor(EAN)));
		sqd.setSearchQueryText(text);
		sqd.setValueComparisonOperator(ValueComparisonOperator.CONTAINS);

		final Pageable<ItemModel> pageable = fieldSearchFacadeStrategy.search(sqd);
		Assertions.assertThat(pageable.getCurrentPage()).hasSize(30);
	}

	@Test
	public void testChangePageSize()
	{
		final SimpleSearchQueryData sqd = new SimpleSearchQueryData(PRODUCT_CODE);
		sqd.setValueComparisonOperator(ValueComparisonOperator.CONTAINS);
		sqd.setPageSize(14);

		final Pageable<ItemModel> pageable = fieldSearchFacadeStrategy.search(sqd);

		// we have 30 items , initial page size = 14 (3 pages)

		Assertions.assertThat(pageable.getCurrentPage()).hasSize(14);
		Assert.assertEquals(PRODUCT_COUNT, pageable.getTotalCount());
		Assertions.assertThat(pageable.hasNextPage()).isTrue();
		Assertions.assertThat(pageable.nextPage()).hasSize(14);

		// has last page (2 items)
		Assertions.assertThat(pageable.hasNextPage()).isTrue();
		// lets change page size
		final List<ItemModel> afterPageSizeChanged = pageable.setPageSize(16);
		Assertions.assertThat(afterPageSizeChanged).hasSize(16);
		Assertions.assertThat(pageable.getCurrentPage()).isSameAs(afterPageSizeChanged);

		// no items left
		Assertions.assertThat(pageable.hasNextPage()).isFalse();
		Assertions.assertThat(pageable.hasPreviousPage()).isTrue();

		// prev page = first page
		Assertions.assertThat(pageable.previousPage()).hasSize(16);
		Assertions.assertThat(pageable.hasPreviousPage()).isFalse();
	}

	@Test
	public void testSetPageNumber()
	{
		final SimpleSearchQueryData sqd = new SimpleSearchQueryData(PRODUCT_CODE);
		sqd.setValueComparisonOperator(ValueComparisonOperator.CONTAINS);
		final int pageSize = PRODUCT_COUNT - 2;
		sqd.setPageSize(pageSize);

		final Pageable<ItemModel> pageable = fieldSearchFacadeStrategy.search(sqd);
		pageable.setPageNumber(0);
		List<ItemModel> currentPage = pageable.getCurrentPage();
		Assertions.assertThat(currentPage).hasSize(pageSize);

		pageable.setPageNumber(1);
		currentPage = pageable.getCurrentPage();
		Assertions.assertThat(currentPage).hasSize(2);
	}


	// (X AND Y)
	@Test
	public void testSearchProductsByTwoANDedAttributes()
	{
		final SearchQueryCondition conditionX = createCondition(CODE, "testProductCode_0", ValueComparisonOperator.EQUALS);

		final SearchQueryCondition conditionY = createCondition(EAN, "testProductEAN_0", ValueComparisonOperator.EQUALS);

		final SearchQueryConditionList outerCondition = createConditionList(Lists.newArrayList(conditionX, conditionY),ValueComparisonOperator.AND);

		final AdvancedSearchQueryData.Builder builder = new AdvancedSearchQueryData.Builder(PRODUCT_CODE);
		builder.globalOperator(ValueComparisonOperator.AND);
		builder.conditions(outerCondition);
		builder.pageSize(5);

		final SearchQueryData searchQueryData = builder.build();

		final Pageable<ItemModel> pageable = fieldSearchFacadeStrategy.search(searchQueryData);

		Assertions.assertThat(pageable.getTotalCount()).isEqualTo(1);
		Assertions.assertThat(pageable.getCurrentPage()).hasSize(1);
		final List<String> retrievedCodes = pageable.getAllResults().stream().map(product->((ProductModel)product).getCode()).collect(Collectors.toList());
		Assertions.assertThat(CollectionUtils.isEqualCollection(retrievedCodes,Lists.newArrayList("testProductCode_0") )).isTrue();
	}

	// (X OR Y)
	@Test
	public void testSearchProductsByTwoORedCodes()
	{
		final SearchQueryCondition conditionX = createCondition(CODE, "testProductCode_0", ValueComparisonOperator.EQUALS);

		final SearchQueryCondition conditionY = createCondition(CODE, "testProductCode_1", ValueComparisonOperator.EQUALS);

		final SearchQueryConditionList outerCondition = createConditionList(Lists.newArrayList(conditionX, conditionY),ValueComparisonOperator.OR);

		final AdvancedSearchQueryData.Builder builder = new AdvancedSearchQueryData.Builder(PRODUCT_CODE);
		builder.globalOperator(ValueComparisonOperator.AND);
		builder.conditions(outerCondition);
		builder.pageSize(5);

		final SearchQueryData searchQueryData = builder.build();

		final Pageable<ItemModel> pageable = fieldSearchFacadeStrategy.search(searchQueryData);

		Assertions.assertThat(pageable.getTotalCount()).isEqualTo(2);
		Assertions.assertThat(pageable.getCurrentPage()).hasSize(2);
		final List<String> retrievedCodes = pageable.getAllResults().stream().map(product->((ProductModel)product).getCode()).collect(Collectors.toList());
		Assertions.assertThat(CollectionUtils.isEqualCollection(retrievedCodes,Lists.newArrayList("testProductCode_0", "testProductCode_1") )).isTrue();
	}


	// (X OR Y) AND A
	@Test
	public void testSearchProductsBytTwoORedCodesANDEan()
	{

		final SearchQueryCondition conditionX = createCondition(CODE,"testProductCode_0",ValueComparisonOperator.EQUALS);
		final SearchQueryCondition conditionY = createCondition(CODE,"testProductCode_1",ValueComparisonOperator.EQUALS);

		final SearchQueryConditionList firstGroup = createConditionList(Lists.newArrayList(conditionX, conditionY),ValueComparisonOperator.OR);


		final SearchQueryCondition conditionA = createCondition(EAN,"testProductEAN_0",ValueComparisonOperator.EQUALS);

		final SearchQueryConditionList outerCondition = createConditionList(Lists.newArrayList(firstGroup,conditionA),ValueComparisonOperator.AND);

		final AdvancedSearchQueryData.Builder builder = new AdvancedSearchQueryData.Builder(PRODUCT_CODE);
		builder.globalOperator(ValueComparisonOperator.AND);
		builder.conditions(outerCondition);
		builder.pageSize(5);

		final SearchQueryData searchQueryData = builder.build();

		final Pageable<ItemModel> pageable = fieldSearchFacadeStrategy.search(searchQueryData);

		Assertions.assertThat(pageable.getTotalCount()).isEqualTo(1);
		Assertions.assertThat(pageable.getCurrentPage()).hasSize(1);
		final List<String> retrievedCodes = pageable.getAllResults().stream().map(product->((ProductModel)product).getCode()).collect(Collectors.toList());
		Assertions.assertThat(retrievedCodes).containsExactly("testProductCode_0");
	}

	// (X AND Y) OR (A AND B)
	@Test
	public void testSearchProductsByCodesAndEansORed()
	{

		final SearchQueryCondition conditionX = createCondition(CODE,"testProductCode_0",ValueComparisonOperator.EQUALS);
		final SearchQueryCondition conditionY = createCondition(EAN,"testProductEAN_0",ValueComparisonOperator.EQUALS);


		final SearchQueryConditionList firstGroup = createConditionList(Lists.newArrayList(conditionX,conditionY), ValueComparisonOperator.AND);


		final SearchQueryCondition conditionA = createCondition(CODE,"testProductCode_1", ValueComparisonOperator.EQUALS );
		final SearchQueryCondition conditionB = createCondition(EAN,"testProductEAN_1", ValueComparisonOperator.EQUALS );


		final SearchQueryConditionList secondGroup = createConditionList(Lists.newArrayList(conditionA,conditionB),ValueComparisonOperator.AND);


		final SearchQueryConditionList outerCondition = createConditionList(Lists.newArrayList(firstGroup, secondGroup),ValueComparisonOperator.OR );



		final AdvancedSearchQueryData.Builder builder = new AdvancedSearchQueryData.Builder(PRODUCT_CODE);
		builder.globalOperator(ValueComparisonOperator.AND);
		builder.conditions(outerCondition);
		builder.pageSize(5);

		final SearchQueryData searchQueryData = builder.build();

		final Pageable<ItemModel> pageable = fieldSearchFacadeStrategy.search(searchQueryData);

		Assertions.assertThat(pageable.getTotalCount()).isEqualTo(2);
		Assertions.assertThat(pageable.getCurrentPage()).hasSize(2);
		final List<String> retrievedCodes = pageable.getAllResults().stream().map(product->((ProductModel)product).getCode()).collect(Collectors.toList());
		Assertions.assertThat(CollectionUtils.isEqualCollection(retrievedCodes,Lists.newArrayList("testProductCode_0", "testProductCode_1") )).isTrue();
	}

	// (X OR Y) OR (A OR B)

	@Test
	public void testSearchProductsByCodesAndEansAllORed()
	{

		final SearchQueryCondition conditionX = createCondition(CODE,"testProductCode_3", ValueComparisonOperator.EQUALS);
		final SearchQueryCondition conditionY = createCondition(EAN,"testProductEAN_0", ValueComparisonOperator.EQUALS);

		final SearchQueryConditionList firstGroup = createConditionList(Lists.newArrayList(conditionX,conditionY),ValueComparisonOperator.OR);

		final SearchQueryCondition conditionA = createCondition(CODE,"testProductCode_2", ValueComparisonOperator.EQUALS);
		final SearchQueryCondition conditionB = createCondition(EAN,"testProductEAN_1", ValueComparisonOperator.EQUALS);

		final SearchQueryConditionList secondGroup = createConditionList(Lists.newArrayList(conditionA, conditionB), ValueComparisonOperator.OR);

		final SearchQueryConditionList outerCondition = createConditionList(Lists.newArrayList(firstGroup, secondGroup),ValueComparisonOperator.OR);

		final AdvancedSearchQueryData.Builder builder = new AdvancedSearchQueryData.Builder(PRODUCT_CODE);
		builder.globalOperator(ValueComparisonOperator.AND);
		builder.conditions(outerCondition);
		builder.pageSize(5);

		final SearchQueryData searchQueryData = builder.build();

		final Pageable<ItemModel> pageable = fieldSearchFacadeStrategy.search(searchQueryData);

		Assertions.assertThat(pageable.getTotalCount()).isEqualTo(4);
		Assertions.assertThat(pageable.getCurrentPage()).hasSize(4);
		final List<String> retrievedCodes = pageable.getAllResults().stream().map(product->((ProductModel)product).getCode()).collect(Collectors.toList());
		Assertions.assertThat(CollectionUtils.isEqualCollection(retrievedCodes,Lists.newArrayList("testProductCode_0", "testProductCode_1","testProductCode_2", "testProductCode_3") )).isTrue();
	}



	// (X OR Y) AND (A OR B)
	@Test
	public void testSearchProductsByCodesAndEansANDed()
	{

		final SearchQueryCondition conditionX = createCondition(CODE,"testProductCode_0", ValueComparisonOperator.EQUALS);
		final SearchQueryCondition conditionY = createCondition(EAN,"testProductEAN_1", ValueComparisonOperator.EQUALS);


		final SearchQueryConditionList firtGroup = createConditionList(Lists.newArrayList(conditionX,conditionY),ValueComparisonOperator.OR);


		final SearchQueryCondition conditionA = createCondition(CODE,"testProductCode_1", ValueComparisonOperator.EQUALS);
		final SearchQueryCondition conditionB = createCondition(EAN,"testProductEAN_0", ValueComparisonOperator.EQUALS);

		final SearchQueryConditionList secondGroup = createConditionList(Lists.newArrayList(conditionA,conditionB),ValueComparisonOperator.OR);

		final SearchQueryConditionList outerCondition = createConditionList(Lists.newArrayList(firtGroup, secondGroup), ValueComparisonOperator.AND);


		final AdvancedSearchQueryData.Builder builder = new AdvancedSearchQueryData.Builder(PRODUCT_CODE);
		builder.globalOperator(ValueComparisonOperator.AND);
		builder.conditions(outerCondition);
		builder.pageSize(5);

		final SearchQueryData searchQueryData = builder.build();

		final Pageable<ItemModel> pageable = fieldSearchFacadeStrategy.search(searchQueryData);

		Assertions.assertThat(pageable.getTotalCount()).isEqualTo(2);
		Assertions.assertThat(pageable.getCurrentPage()).hasSize(2);
		final List<String> retrievedCodes = pageable.getAllResults().stream().map(product->((ProductModel)product).getCode()).collect(Collectors.toList());
		Assertions.assertThat(CollectionUtils.isEqualCollection(retrievedCodes,Lists.newArrayList("testProductCode_0", "testProductCode_1") )).isTrue();
	}


	// (X OR Y) AND (A OR B) OR (C AND D) ==> ((X OR Y) AND (A OR B)) OR (C AND D)
	@Test
	public void testSearchProductsByCodesAndEansThreeGroups()
	{

		final SearchQueryCondition conditionX = createCondition(CODE,"testProductCode_0",ValueComparisonOperator.EQUALS);
		final SearchQueryCondition conditionY = createCondition(CODE,"testProductCode_1",ValueComparisonOperator.EQUALS);

		final SearchQueryConditionList firstGroup = createConditionList(Lists.newArrayList(conditionX, conditionY), ValueComparisonOperator.OR);


		final SearchQueryCondition conditionA = createCondition(EAN,"testProductEAN_0",ValueComparisonOperator.EQUALS);
		final SearchQueryCondition conditionB = createCondition(EAN,"testProductEAN_1",ValueComparisonOperator.EQUALS);

		final SearchQueryConditionList secondGroup = createConditionList(Lists.newArrayList(conditionA, conditionB),ValueComparisonOperator.OR);

		final SearchQueryConditionList agregatedFirstAndSecondGroup = createConditionList(Lists.newArrayList(firstGroup, secondGroup),ValueComparisonOperator.AND);



		final SearchQueryCondition conditionC = createCondition(CODE,"testProductCode_2",ValueComparisonOperator.EQUALS);
		final SearchQueryCondition conditionD = createCondition(EAN,"testProductEAN_2",ValueComparisonOperator.EQUALS);


		final SearchQueryConditionList thirdGroup = createConditionList(Lists.newArrayList(conditionC,conditionD), ValueComparisonOperator.AND);

		final SearchQueryConditionList superOuter = createConditionList(Lists.newArrayList(agregatedFirstAndSecondGroup,thirdGroup),ValueComparisonOperator.OR );

		final AdvancedSearchQueryData.Builder builder = new AdvancedSearchQueryData.Builder(PRODUCT_CODE);
		builder.globalOperator(ValueComparisonOperator.AND);
		builder.conditions(superOuter);
		builder.pageSize(5);

		final SearchQueryData searchQueryData = builder.build();

		final Pageable<ItemModel> pageable = fieldSearchFacadeStrategy.search(searchQueryData);

		Assertions.assertThat(pageable.getTotalCount()).isEqualTo(3);
		Assertions.assertThat(pageable.getCurrentPage()).hasSize(3);
		final List<String> retrievedCodes = pageable.getAllResults().stream().map(product->((ProductModel)product).getCode()).collect(Collectors.toList());
		Assertions.assertThat(CollectionUtils.isEqualCollection(retrievedCodes,Lists.newArrayList("testProductCode_0", "testProductCode_1", "testProductCode_2") )).isTrue();
	}


	private SearchQueryConditionList createConditionList(final List<SearchQueryCondition> conditions, final ValueComparisonOperator operator){

		return new SearchQueryConditionList(operator, conditions);

	}

	private SearchQueryCondition createCondition(final String attribute, final Object value, final ValueComparisonOperator operator){

		final SearchQueryCondition condition = new SearchQueryCondition();
		condition.setOperator(operator);
		condition.setDescriptor(new SearchAttributeDescriptor(attribute));
		condition.setValue(value);

		return condition;
	}

}
