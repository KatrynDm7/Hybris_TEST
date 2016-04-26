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
package de.hybris.platform.b2ctelcofacades.bundle;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.configurablebundlefacades.data.BundleTemplateData;
import de.hybris.platform.b2ctelcofacades.data.DashboardData;


/**
 * Telco Guided Selling Facade. Facade is responsible for getting all information for Guided Selling Feature.
 */
public interface GuidedSellingFacade
{

	/**
	 * This method identifies the common ProductType within a specific component.
	 *
	 * @param bundleTemplateId
	 *           the component to check
	 * @return the product type (e.g. item type code) of the common component
	 */
	String getComponentProductType(final String bundleTemplateId);

	/**
	 * Finds the relative component id based on the current component. Automatic components with an automatic pick
	 * selection criteria will be omitted as well as non Device or ServiceAddOn components will be omited.
	 *
	 * @param bundleNo
	 *           the current bundleNo
	 * @param bundleTemplateId
	 *           the current bundleTemplate
	 * @param relativeposition
	 *           the relative position to jump to
	 * @return the id of the relative component to search for, in case it is not valid, the next one valid will be
	 *         returned
	 */
	String getRelativeComponentId(final String bundleNo, final String bundleTemplateId, final int relativeposition);

	/**
	 * This method gets the subsequent {@link BundleTemplateData} for <code>bundleNo</code> and
	 * <code>bundleTemplateId</code>.
	 *
	 * @param bundleNo
	 *           indicates to which bundle the product shall be added
	 * @param bundleTemplateId
	 *           The bundletemplate id to which the extras belong
	 * @return {@link BundleTemplateData}
	 */
	BundleTemplateData getComponentToEdit(String bundleNo, String bundleTemplateId);

	/**
	 * This method checks if the selection criteria is met for a particular component or not.
	 *
	 * @param bundleNo
	 *           indicates to which bundle the product shall be added
	 * @param bundleTemplateId
	 *           The bundletemplate id to which the extras belong
	 * @return boolean
	 */
	boolean checkIsComponentSelectionCriteriaMet(String bundleNo, String bundleTemplateId);

	/**
	 * Initiate a new or refine an existing search for bundle products.
	 *
	 * @param pageableData
	 *           the page to return
	 * @param searchQuery
	 *           the search query
	 * @param urlPrefix
	 *           the URL prefix to be replaced
	 * @param componentId
	 *           the id of the current component
	 * @param bundleNo
	 *           the bundle number
	 * @return the search results
	 */
	ProductSearchPageData<SearchStateData, ProductData> bundleSearch(PageableData pageableData, String searchQuery,
			String urlPrefix, String componentId, Integer bundleNo);

	/**
	 * Populate the dashboard data object for the current bundle number.
	 *
	 * @param bundleNo
	 *           current bundle number
	 * @param currentComponent
	 *           current component that is edited
	 * @return the data for the current dashboard
	 */
	DashboardData getDashboard(int bundleNo, String currentComponent);

}
