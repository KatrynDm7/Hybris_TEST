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
package de.hybris.platform.commercesearch.atdd.keywords;

import static org.junit.Assert.fail;

import de.hybris.platform.atddengine.keywords.AbstractKeywordLibrary;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commercesearch.data.BoostRuleData;
import de.hybris.platform.commercesearch.enums.SolrBoostConditionOperator;
import de.hybris.platform.commercesearch.facet.config.FacetSearchStateData;
import de.hybris.platform.commercesearch.searchandizing.boost.BoostFacade;
import de.hybris.platform.commercesearch.searchandizing.facet.reconfiguration.FacetAdminService;
import de.hybris.platform.commercesearch.searchandizing.heroproduct.HeroProductFacade;
import de.hybris.platform.commercesearch.searchandizing.searchprofile.dao.SearchProfileDao;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.indexer.IndexerService;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.store.BaseStoreModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;


public class CommerceSearchKeywordLibrary extends AbstractKeywordLibrary
{
	private static final Logger LOG = Logger.getLogger(CommerceSearchKeywordLibrary.class);

	@Autowired
	private IndexerService indexerService;

	@Autowired
	private FacetSearchConfigService facetSearchConfigService;

	@Autowired
	private BaseSiteService baseSiteService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private HeroProductFacade heroProductFacade;

	@Autowired
	private ProductSearchFacade productSearchFacade;

	@Autowired
	private BoostFacade boostFacade;

	@Autowired
	private WarehouseService warehouseService;

	@Autowired
	private CommerceStockService commerceStockService;

	@Autowired
	private StockService stockService;

	@Autowired
	private ProductService productService;

	@Autowired
	private FacetAdminService facetAdminService;

	@Autowired
	private SearchProfileDao searchProfileDao;


	public void addProductToHeroes(final String categoryCode, final String productCode)
	{
		try
		{
			heroProductFacade.addHeroProduct(categoryCode, productCode);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception during execution of the addProductToHeroes keyword: ", ex);
			fail(ex.getMessage());
		}
	}

	public void removeProductFromHeroes(final String categoryCode, final String productCode)
	{
		try
		{
			heroProductFacade.removeHeroProduct(categoryCode, productCode);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception during execution of the removeProductFromHeroes keyword: ", ex);
			fail(ex.getMessage());
		}
	}

	public void checkProductIsFirstResult(final String categoryCode, final String productCode)
	{
		checkProductIsFirstResult(categoryCode, productCode, null);
	}

	public void checkProductIsFirstResult(final String categoryCode, final String productCode, final String sortOrder)
	{
		try
		{
			ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> result = productSearchFacade
					.categorySearch(categoryCode);
			if (sortOrder != null)
			{
				final PaginationData pagination = result.getPagination();
				pagination.setSort(sortOrder);
				result = productSearchFacade.categorySearch(categoryCode, result.getCurrentQuery(), pagination);
			}
			Assert.assertTrue(0 != result.getResults().size());
			Assert.assertEquals(productCode, result.getResults().get(0).getCode());
		}
		catch (final Exception ex)
		{
			LOG.error("Exception during execution of the checkProductIsFirstResult keyword: ", ex);
			fail(ex.getMessage());
		}
	}

	public void checkProductIsNotFirstResult(final String categoryCode, final String productCode)
	{
		checkProductIsNotFirstResult(categoryCode, productCode, null);
	}

	public void checkProductIsNotFirstResult(final String categoryCode, final String productCode, final String sortOrder)
	{
		try
		{
			ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> result = productSearchFacade
					.categorySearch(categoryCode);
			if (sortOrder != null)
			{
				final PaginationData pagination = result.getPagination();
				pagination.setSort(sortOrder);
				result = productSearchFacade.categorySearch(categoryCode, result.getCurrentQuery(), pagination);
			}
			Assert.assertTrue(0 != result.getResults().size());
			Assert.assertNotSame(productCode, result.getResults().get(0).getCode());
		}
		catch (final Exception ex)
		{
			LOG.error("Exception during execution of the checkProductIsNotFirstResult keyword: ", ex);
			fail(ex.getMessage());
		}
	}

	public void checkProductIsNotAHero(final String categoryCode, final String productCode)
	{
		try
		{
			Assert.assertFalse(heroProductFacade.getHeroProductsForCategory(categoryCode).contains(productCode));
		}
		catch (final Exception ex)
		{
			LOG.error("Exception during execution of the checkProductIsNotAHero keyword: ", ex);
			fail(ex.getMessage());
		}
	}

	public void checkCategoryHasProducts(final String categoryCode)
	{
		try
		{
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> result = productSearchFacade
					.categorySearch(categoryCode);
			Assert.assertTrue(0 != result.getResults().size());
		}
		catch (final Exception ex)
		{
			LOG.error("Exception during execution of the checkCategoryHasProducts keyword: ", ex);
			fail(ex.getMessage());
		}
	}

	public void updateSolrIndex()
	{
		final SolrFacetSearchConfigModel facetSearchConfigModel = baseSiteService.getCurrentBaseSite()
				.getSolrFacetSearchConfiguration();
		FacetSearchConfig facetSearchConfig;
		try
		{
			facetSearchConfig = facetSearchConfigService.getConfiguration(facetSearchConfigModel.getName());
			indexerService.performFullIndex(facetSearchConfig);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception during execution of the updateSolrIndex keyword: ", ex);
			fail(ex.getMessage());
		}
	}

	public void createCategoryBoostRule(final String fieldName, final String fieldValue, final int boostStrength,
			final String categoryCode, final String operator)
	{
		createBoostRule(fieldName, fieldValue, boostStrength, categoryCode, SolrBoostConditionOperator.valueOf(operator));
	}

	public void createGlobalBoostRule(final String fieldName, final String fieldValue, final int boostStrength,
			final String operator)
	{
		createBoostRule(fieldName, fieldValue, boostStrength, null, SolrBoostConditionOperator.valueOf(operator));
	}

	public void createBoostRule(final String fieldName, final String fieldValue, final int boostStrength,
			final String categoryCode, final SolrBoostConditionOperator operator)
	{
		try
		{
			final BoostRuleData boostRuleData = new BoostRuleData();
			boostRuleData.setPropertyName(fieldName);
			boostRuleData.setPropertyValue(fieldValue);
			boostRuleData.setBoostFactor(boostStrength);
			boostRuleData.setOperatorType(operator);
			boostFacade.createBoostRule(categoryCode, boostRuleData);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception during execution of the createBoostRule keyword: ", ex);
			fail(ex.getMessage());
		}
	}

	public void setInStock(final String productCode, final boolean value)
	{
		try
		{
			final ProductModel product = productService.getProductForCode(productCode);

			if (baseSiteService.getCurrentBaseSite().getStores().size() == 0)
			{
				fail("no stores found to get stock levels");
			}

			final List<WarehouseModel> warehouses = new ArrayList<WarehouseModel>();
			warehouses.add(warehouseService.getWarehouseForCode("warehouse_s"));

			for (final BaseStoreModel store : baseSiteService.getCurrentBaseSite().getStores())
			{
				if (store.getWarehouses().size() == 0)
				{
					for (final WarehouseModel warehouse : warehouses)
					{
						final List<BaseStoreModel> baseStores = new ArrayList<BaseStoreModel>(warehouse.getBaseStores());
						baseStores.add(store);
						warehouse.setBaseStores(baseStores);
						modelService.save(warehouse);
					}
					store.setWarehouses(warehouses);
					modelService.save(store);

				}

				if (!commerceStockService.isStockSystemEnabled(store))
				{
					fail("stock system disabled for store: " + store.getName());
				}

				stockService.updateActualStockLevel(product, warehouses.get(0), value ? 100 : 0, "test stock level");
				final StockLevelStatus stockLevelStatus = commerceStockService.getStockLevelStatusForProductAndBaseStore(product,
						store);
				final boolean commerceInStock = (stockLevelStatus != StockLevelStatus.OUTOFSTOCK);
				Assert.assertEquals(value, commerceInStock);
			}
		}
		catch (final Exception ex)
		{
			LOG.error("Exception during execution of the setInStock keyword: ", ex);
			fail(ex.getMessage());
		}
	}

	public void checkFacetIsInPosition(final String facetName, final int position, final String categoryCode)
	{
		try
		{
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> result = productSearchFacade
					.categorySearch(categoryCode);
			final List<FacetData<SearchStateData>> facets = result.getFacets();

			if (facets.size() < position)
			{
				fail("Only " + facets.size() + " facets defined, you're trying to check position " + position);
			}

			if (!facetName.equals(facets.get(position - 1).getName()) && !facetName.equals(facets.get(position - 1).getCode()))
			{
				final StringBuffer facetOrder = new StringBuffer();
				for (final FacetData data : facets)
				{
					facetOrder.append(data.getCode() + ",");
				}
				fail("expecting facet " + facetName + " at position " + position + " but found this order: " + facetOrder.toString());
			}
		}
		catch (final Exception ex)
		{
			LOG.error("Exception during execution of the checkFacetIsInPosition keyword: ", ex);
			fail(ex.getMessage());
		}
	}

	public void checkFacetIsInvisibleForGlobalConfiguration(final String facetName)
	{
		try
		{
			final FacetData<SearchStateData> facetThatShouldNotBeFound = findFacetInGlobalConfiguration(facetName);
			Assertions.assertThat(facetThatShouldNotBeFound).isNull();
		}
		catch (final Exception ex)
		{
			LOG.error("Exception during execution of the checkFacetIsInvisibleForGlobalConfiguration keyword: ", ex);
			fail(ex.getMessage());
		}
	}

	public void checkFacetIsVisibleForGlobalConfiguration(final String facetName)
	{
		try
		{
			final FacetData<SearchStateData> facetThatShouldBeFound = findFacetInGlobalConfiguration(facetName);
			Assertions.assertThat(facetThatShouldBeFound).isNotNull();
		}
		catch (final Exception ex)
		{
			LOG.error("Exception during execution of the checkFacetIsVisibleForGlobalConfiguration keyword: ", ex);
			fail(ex.getMessage());
		}
	}

	private FacetData<SearchStateData> findFacetInGlobalConfiguration(final String facetName)
	{
		final ProductSearchPageData result = productSearchFacade.textSearch("");
		final List<FacetData<SearchStateData>> facets = result.getFacets();

		return (FacetData) CollectionUtils.find(facets, new Predicate()
		{
			@Override
			public boolean evaluate(final Object o)
			{
				final FacetData<SearchStateData> data = (FacetData<SearchStateData>) o;
				return StringUtils.equals(data.getCode(), facetName);
			}
		});
	}

	public void checkFacetIsInvisibleForCategory(final String facetName, final String categoryCode)
	{
		try
		{
			final FacetData<SearchStateData> facetThatShouldNotBeFound = findFacetForGivenCategory(facetName, categoryCode);
			Assertions.assertThat(facetThatShouldNotBeFound).isNull();
		}
		catch (final Exception ex)
		{
			LOG.error("Exception during execution of the checkFacetIsInvisibleForCategory keyword: ", ex);
			fail(ex.getMessage());
		}
	}

	public void checkFacetIsVisibleForCategory(final String facetName, final String categoryCode)
	{
		try
		{
			final FacetData<SearchStateData> facetThatShouldBeFound = findFacetForGivenCategory(facetName, categoryCode);
			Assertions.assertThat(facetThatShouldBeFound).isNotNull();
		}
		catch (final Exception ex)
		{
			LOG.error("Exception during execution of the checkFacetIsVisibleForCategory keyword: ", ex);
			fail(ex.getMessage());
		}
	}

	private FacetData<SearchStateData> findFacetForGivenCategory(final String facetName, final String categoryCode)
	{
		final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> result = productSearchFacade
				.categorySearch(categoryCode);
		final List<FacetData<SearchStateData>> facets = result.getFacets();

		return (FacetData) CollectionUtils.find(facets, new Predicate()
		{
			@Override
			public boolean evaluate(final Object o)
			{
				final FacetData<SearchStateData> data = (FacetData<SearchStateData>) o;
				return StringUtils.equals(data.getCode(), facetName);
			}
		});
	}

	public void setGlobalFacetPriority(final String facetName, final int priority)
	{
		try
		{
			final SolrFacetSearchConfigModel facetSearchConfigModel = baseSiteService.getCurrentBaseSite()
					.getSolrFacetSearchConfiguration();

			SolrIndexedPropertyModel indexProperty = null;
			final StringBuffer sb = new StringBuffer();
			for (final SolrIndexedPropertyModel p : facetSearchConfigModel.getSolrIndexedTypes().get(0).getSolrIndexedProperties())
			{
				if (p.getName().equalsIgnoreCase(facetName))
				{
					indexProperty = p;
					break;
				}
				sb.append(p.getName() + ", ");
			}
			if (indexProperty == null)
			{
				fail("invalid property name: " + facetName + ", available otions are: " + sb.toString());
			}

			indexProperty.setPriority(priority);
			modelService.save(indexProperty);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception during execution of the setGlobalFacetPriority keyword: ", ex);
			fail(ex.getMessage());
		}
	}

	public void moveFacetBelowOtherFacetForSelectedCategory(final String facetName, final String stationaryFacetName,
			final String categoryCode)
	{
		try
		{
			final FacetSearchStateData facetSearchStateData = new FacetSearchStateData();
			facetSearchStateData.setSelectedCategoryCode(categoryCode);
			facetAdminService.moveFacetBelow(facetName, stationaryFacetName, facetSearchStateData);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception during execution of the moveFacetBelowOtherFacetForSelectedCategory keyword: ", ex);
			fail(ex.getMessage());
		}
	}

	public void moveFacetAboveOtherFacetForSelectedCategory(final String facetName, final String stationaryFacetName,
			final String categoryCode)
	{
		try
		{
			final FacetSearchStateData facetSearchStateData = new FacetSearchStateData();
			facetSearchStateData.setSelectedCategoryCode(categoryCode);
			facetAdminService.moveFacetAbove(facetName, stationaryFacetName, facetSearchStateData);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception during execution of the moveFacetAboveOtherFacetForSelectedCategory keyword: ", ex);
			fail(ex.getMessage());
		}
	}

	public void moveFacetAboveOtherFacetForGlobalConfiguration(final String facetName, final String stationaryFacetName)
	{
		try
		{
			final FacetSearchStateData facetSearchStateData = new FacetSearchStateData();
			facetAdminService.moveFacetAbove(facetName, stationaryFacetName, facetSearchStateData);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception during execution of the moveFacetAboveOtherFacetForGlobalConfiguration keyword: ", ex);
			fail(ex.getMessage());
		}
	}

	public void moveFacetBelowOtherFacetForGlobalConfiguration(final String facetName, final String stationaryFacetName)
	{
		try
		{
			final FacetSearchStateData facetSearchStateData = new FacetSearchStateData();
			facetAdminService.moveFacetAbove(facetName, stationaryFacetName, facetSearchStateData);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception during execution of the moveFacetBelowOtherFacetForGlobalConfiguration keyword: ", ex);
			fail(ex.getMessage());
		}
	}

	public void toggleFacetGlobalVisibility(final String facetName)
	{
		try
		{
			facetAdminService.toggleFacetVisibility(facetName, new FacetSearchStateData());
		}
		catch (final Exception ex)
		{
			LOG.error("Exception during execution of the toggleFacetGlobalVisibility keyword: ", ex);
			fail(ex.getMessage());
		}
	}

	public void toggleFacetVisibilityForACategory(final String facetName, final String categoryCode)
	{
		try
		{
			final FacetSearchStateData facetSearchStateData = new FacetSearchStateData();
			facetSearchStateData.setSelectedCategoryCode(categoryCode);
			facetAdminService.toggleFacetVisibility(facetName, facetSearchStateData);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception during execution of the toggleFacetVisibilityForACategory keyword: ", ex);
			fail(ex.getMessage());
		}
	}
}
