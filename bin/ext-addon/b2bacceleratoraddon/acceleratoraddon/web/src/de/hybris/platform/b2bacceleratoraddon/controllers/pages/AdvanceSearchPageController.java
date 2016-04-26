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
package de.hybris.platform.b2bacceleratoraddon.controllers.pages;


import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.SearchBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.acceleratorstorefrontcommons.util.MetaSanitizerUtil;
import de.hybris.platform.acceleratorstorefrontcommons.util.XSSFilterUtil;
import de.hybris.platform.b2bacceleratoraddon.controllers.B2bacceleratoraddonControllerConstants;
import de.hybris.platform.b2bacceleratoraddon.forms.AdvancedSearchForm;
import de.hybris.platform.b2bacceleratorfacades.api.search.SearchFacade;
import de.hybris.platform.b2bacceleratorfacades.search.data.ProductSearchStateData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.FacetValueData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.util.Config;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Lists;


@Controller
@Scope("tenant")
@RequestMapping("/search")
public class AdvanceSearchPageController extends AbstractSearchPageController
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(AdvanceSearchPageController.class);

	private static final String ADVANCED_FLEXIBLE_SEARCH_PAGE_SIZE_PARAM = "storefront.flexible.advance.search.pageSize";
	private static final String ADVANCED_SEARCH_PRODUCT_IDS_DELIMITER = "storefront.advancedsearch.delimiter";
	private static final String ADVANCED_SEARCH_PRODUCT_IDS_DELIMITER_DEFAULT = ",";

	private static final String ADVANCED_SEARCH_RESULT_TYPE_CATALOG = "catalog";
	private static final String ADVANCED_SEARCH_RESULT_TYPE_ORDER_FORM = "order-form";

	private static final String FUTURE_STOCK_ENABLED = "storefront.products.futurestock.enabled";

	private static final String NO_RESULTS_ADVANCED_PAGE_ID = "searchAdvancedEmpty";

	private static final String INFINITE_SCROLL = "infiniteScroll";
	protected static final String PAGINATION_TYPE = "pagination.type";
	protected static final String PAGINATION = "pagination";
	protected static final String IS_SHOW_ALLOWED = "isShowAllAllowed";


	@Resource(name = "searchBreadcrumbBuilder")
	private SearchBreadcrumbBuilder searchBreadcrumbBuilder;

	@Resource(name = "b2bProductFlexibleSearchFacade")
	private SearchFacade<ProductData, SearchStateData> flexibleSearchProductSearchFacade;

	@Resource(name = "b2bSolrProductSearchFacade")
	private SearchFacade<ProductData, SearchStateData> b2bSolrProductSearchFacade;

	@RequestMapping(value = "/advanced", method = RequestMethod.GET)
	public String advanceSearchResults(
			@RequestParam(value = "keywords", required = false, defaultValue = StringUtils.EMPTY) String keywords,
			@RequestParam(value = "searchResultType", required = false, defaultValue = ADVANCED_SEARCH_RESULT_TYPE_CATALOG) final String searchResultType,
			@RequestParam(value = "inStockOnly", required = false, defaultValue = "false") final boolean inStockOnly,
			@RequestParam(value = "onlyProductIds", required = false, defaultValue = "false") final boolean onlyProductIds,
			@RequestParam(value = "isCreateOrderForm", required = false, defaultValue = "false") final boolean isCreateOrderForm,
			@RequestParam(value = "q", defaultValue = StringUtils.EMPTY) String searchQuery,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode, final Model model)
			throws CMSItemNotFoundException
	{

		if (StringUtils.isNotBlank(keywords))
		{
			searchQuery = keywords;
		}
		else
		{
			if (StringUtils.isNotBlank(searchQuery))
			{
				keywords = StringUtils.split(searchQuery, ":")[0];
			}
		}

		// check if it is order form (either order form was selected or "Create Order Form"
		final boolean useFlexibleSearch = isUseFlexibleSearch(onlyProductIds, isCreateOrderForm);
		final PageableData pageableData = createPageableData(page, useFlexibleSearch ? getAdvanceFlexibleSearchPageSize()
				: getSearchPageSize(), sortCode, showMode);
		final SearchStateData searchState = createSearchStateData(searchQuery,
				isPopulateVariants(searchResultType, isCreateOrderForm));

		final SearchPageData<ProductData> searchPageData = performSearch(searchState, pageableData, useFlexibleSearch);
		populateModel(model, searchPageData, showMode);

		String metaInfoText = null;
		if (StringUtils.isEmpty(keywords))
		{
			metaInfoText = MetaSanitizerUtil.sanitizeDescription(getMessageSource().getMessage(
					"search.advanced.meta.description.title", null, getCurrentLocale()));
		}
		else
		{
			metaInfoText = MetaSanitizerUtil.sanitizeDescription(keywords);
		}

		model.addAttribute(WebConstants.BREADCRUMBS_KEY, searchBreadcrumbBuilder.getBreadcrumbs(null, metaInfoText, false));

		final AdvancedSearchForm form = new AdvancedSearchForm();
		form.setOnlyProductIds(Boolean.valueOf(onlyProductIds));
		form.setInStockOnly(Boolean.valueOf(inStockOnly));
		form.setKeywords(keywords);
		form.setCreateOrderForm(isCreateOrderForm);


		if (isCreateOrderForm)
		{
			form.setSearchResultType(ADVANCED_SEARCH_RESULT_TYPE_ORDER_FORM);
			final List<String> filterSkus = splitSkusAsList(keywords);
			form.setFilterSkus(filterSkus);
			form.setCreateOrderForm(Boolean.FALSE);
			form.setOnlyProductIds(Boolean.TRUE);
		}
		else
		{
			form.setSearchResultType(searchResultType);
		}

		model.addAttribute("advancedSearchForm", form);
		model.addAttribute("futureStockEnabled", Boolean.valueOf(Config.getBoolean(FUTURE_STOCK_ENABLED, false)));

		storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_ADVANCED_PAGE_ID));

		addMetaData(model, "search.meta.description.results", metaInfoText, "search.meta.description.on", PageType.PRODUCTSEARCH,
				ThirdPartyConstants.SeoRobots.NOINDEX_FOLLOW);

		return getViewForPage(model);
	}

	@RequestMapping(value = "/advanceSearchResults", method = RequestMethod.GET)
	public String productListerSearchResults(@RequestParam("q") final String searchQuery,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode,
			@RequestParam(value = "searchResultType", required = false) final String searchResultType,
			@RequestParam(value = "skuIndex", required = false, defaultValue = "0") final int skuIndex,
			@RequestParam(value = "isOrderForm", required = false, defaultValue = "false") final boolean isOrderForm,
			@RequestParam(value = "isOnlyProductIds", required = false, defaultValue = "false") final boolean isOnlyProductIds,
			@RequestParam(value = "isCreateOrderForm", required = false, defaultValue = "false") final boolean isCreateOrderForm,
			final Model model) throws CMSItemNotFoundException
	{

		final boolean useFlexibleSearch = isUseFlexibleSearch(isOnlyProductIds, isCreateOrderForm);
		// check if it is order form (either order form was selected or "Create Order Form"
		final PageableData pageableData = createPageableData(page, useFlexibleSearch ? getAdvanceFlexibleSearchPageSize()
				: getSearchPageSize(), sortCode, showMode);
		final SearchStateData searchState = createSearchStateData(searchQuery,
				isPopulateVariants(searchResultType, isCreateOrderForm));

		final SearchPageData<ProductData> searchPageData = performSearch(searchState, pageableData, useFlexibleSearch);
		populateModel(model, searchPageData, showMode);


		final SearchResultsData<ProductData> searchResultsData = new SearchResultsData<ProductData>();

		searchResultsData.setResults(searchPageData.getResults());
		searchResultsData.setPagination(searchPageData.getPagination());

		model.addAttribute("searchResultsData", searchResultsData);
		model.addAttribute("skuIndex", Integer.valueOf(skuIndex));
		model.addAttribute("isOrderForm", Boolean.valueOf(isOrderForm));
		model.addAttribute("isCreateOrderForm", Boolean.valueOf(isCreateOrderForm));


		if (isCreateOrderForm)
		{
			model.addAttribute("searchResultType", ADVANCED_SEARCH_RESULT_TYPE_ORDER_FORM);
			model.addAttribute("filterSkus", splitSkusAsList(searchQuery));
		}
		else
		{
			model.addAttribute("searchResultType", searchResultType);
		}

		return B2bacceleratoraddonControllerConstants.Views.Fragments.Product.ProductLister;
	}

	private ProductSearchStateData createSearchStateData(final String term, final boolean populateVariants)
	{

		final ProductSearchStateData searchState = new ProductSearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();
		searchQueryData.setValue(XSSFilterUtil.filter(term));
		searchState.setQuery(searchQueryData);
		searchState.setPopulateVariants(populateVariants);

		return searchState;
	}

	protected void addMetaData(final Model model, final String metaPrefixKey, final String searchText,
			final String metaPostfixKey, final PageType pageType, final String robotsBehaviour)
	{
		final String metaDescription = MetaSanitizerUtil.sanitizeDescription(getMessageSource().getMessage(metaPrefixKey, null,
				getCurrentLocale())
				+ " "
				+ searchText
				+ " "
				+ getMessageSource().getMessage(metaPostfixKey, null, getCurrentLocale())
				+ " "
				+ getSiteName());
		final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(searchText);
		setUpMetaData(model, metaKeywords, metaDescription);

		model.addAttribute("pageType", pageType.name());
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, robotsBehaviour);
	}

	protected List<String> splitSkusAsList(final String skus)
	{
		return Arrays.asList(StringUtils.split(skus,
				Config.getString(ADVANCED_SEARCH_PRODUCT_IDS_DELIMITER, ADVANCED_SEARCH_PRODUCT_IDS_DELIMITER_DEFAULT)));
	}

	private Locale getCurrentLocale()
	{
		return getI18nService().getCurrentLocale();
	}

	private boolean isUseFlexibleSearch(final boolean onlyProductIds, final boolean isCreateOrderForm)
	{
		return onlyProductIds || isCreateOrderForm;
	}

	private boolean isPopulateVariants(final String searchResultType, final boolean isCreateOrderForm)
	{
		return (searchResultType != null && StringUtils.equals(searchResultType, ADVANCED_SEARCH_RESULT_TYPE_ORDER_FORM))
				|| isCreateOrderForm;
	}

	protected ProductSearchPageData<SearchStateData, ProductData> performSearch(final SearchStateData searchState,
			final PageableData pageableData, final boolean useFlexibleSearch)
	{

		ProductSearchPageData<SearchStateData, ProductData> searchResult = createEmptySearchPageData();

		if (StringUtils.isNotBlank(searchState.getQuery().getValue()))
		{
			if (useFlexibleSearch)
			{
				searchResult = (ProductSearchPageData<SearchStateData, ProductData>) flexibleSearchProductSearchFacade.search(
						searchState, pageableData);
			}
			else
			{
				// search using solr.
				searchResult = (ProductSearchPageData<SearchStateData, ProductData>) b2bSolrProductSearchFacade.search(searchState,
						pageableData);
			}

		}

		return encodeSearchPageData(searchResult);
	}

	private ProductSearchPageData<SearchStateData, ProductData> createEmptySearchPageData()
	{
		final ProductSearchPageData productSearchPageData = new ProductSearchPageData();

		productSearchPageData.setResults(Lists.newArrayList());
		final PaginationData pagination = new PaginationData();
		pagination.setTotalNumberOfResults(0);
		productSearchPageData.setPagination(pagination);
		productSearchPageData.setSorts(Lists.newArrayList());

		return productSearchPageData;
	}

	private ProductSearchPageData<SearchStateData, ProductData> encodeSearchPageData(
			final ProductSearchPageData<SearchStateData, ProductData> searchPageData)
	{
		final SearchStateData currentQuery = searchPageData.getCurrentQuery();

		if (currentQuery != null)
		{
			final SearchQueryData query = currentQuery.getQuery();
			final String encodedQueryValue = StringEscapeUtils.escapeHtml(query.getValue());
			query.setValue(encodedQueryValue);
			currentQuery.setQuery(query);
			searchPageData.setCurrentQuery(currentQuery);

			final List<FacetData<SearchStateData>> facets = searchPageData.getFacets();
			if (CollectionUtils.isNotEmpty(facets))
			{
				for (final FacetData<SearchStateData> facetData : facets)
				{
					final List<FacetValueData<SearchStateData>> facetValueDatas = facetData.getValues();
					if (CollectionUtils.isNotEmpty(facetValueDatas))
					{
						for (final FacetValueData<SearchStateData> facetValueData : facetValueDatas)
						{
							final SearchStateData facetQuery = facetValueData.getQuery();
							final SearchQueryData queryData = facetQuery.getQuery();
							final String queryValue = queryData.getValue();
							if (StringUtils.isNotBlank(queryValue))
							{
								final String[] queryValues = queryValue.split(":");
								final StringBuilder queryValueBuilder = new StringBuilder();
								queryValueBuilder.append(StringEscapeUtils.escapeHtml(queryValues[0]));
								for (int i = 1; i < queryValues.length; i++)
								{
									queryValueBuilder.append(":").append(queryValues[i]);
								}
								queryData.setValue(queryValueBuilder.toString());
							}
						}
					}

					final List<FacetValueData<SearchStateData>> topFacetValueDatas = facetData.getTopValues();
					if (CollectionUtils.isNotEmpty(topFacetValueDatas))
					{
						for (final FacetValueData<SearchStateData> topFacetValueData : topFacetValueDatas)
						{
							final SearchStateData facetQuery = topFacetValueData.getQuery();
							final SearchQueryData queryData = facetQuery.getQuery();
							final String queryValue = queryData.getValue();
							if (StringUtils.isNotBlank(queryValue))
							{
								final String[] queryValues = queryValue.split(":");
								final StringBuilder queryValueBuilder = new StringBuilder();
								queryValueBuilder.append(StringEscapeUtils.escapeHtml(queryValues[0]));
								for (int i = 1; i < queryValues.length; i++)
								{
									queryValueBuilder.append(":").append(queryValues[i]);
								}
								queryData.setValue(queryValueBuilder.toString());
							}
						}
					}
				}
			}
		}
		return searchPageData;
	}

	@Override
	protected void populateModel(final Model model, final SearchPageData<?> searchPageData, final ShowMode showMode)
	{
		super.populateModel(model, searchPageData, showMode);

		if (StringUtils.equalsIgnoreCase(getSiteConfigService().getString(PAGINATION_TYPE, PAGINATION), INFINITE_SCROLL))
		{
			model.addAttribute(IS_SHOW_ALLOWED, false);
		}

		final String paginationType = getSiteConfigService().getString(PAGINATION_TYPE, "pagination");
		model.addAttribute("paginationType", paginationType);
	}

	protected int getAdvanceFlexibleSearchPageSize()
	{
		return getSiteConfigService().getInt(ADVANCED_FLEXIBLE_SEARCH_PAGE_SIZE_PARAM, 10);
	}

}
