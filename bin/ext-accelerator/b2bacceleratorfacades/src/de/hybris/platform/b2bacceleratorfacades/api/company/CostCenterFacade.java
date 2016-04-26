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
package de.hybris.platform.b2bacceleratorfacades.api.company;

import de.hybris.platform.b2bacceleratorfacades.api.search.SearchFacade;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BCostCenterData;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BSelectionData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;

import java.util.List;


public interface CostCenterFacade extends SearchFacade<B2BCostCenterData, SearchStateData>
{
	/**
	 * Gets all visible cost centers for the currently logged-in {@link de.hybris.platform.b2b.model.B2BCustomerModel}
	 * based on his parent B2Unit
	 * 
	 * @return A collection of {@link de.hybris.platform.b2bacceleratorfacades.order.data.B2BCostCenterData}
	 */
    public List<? extends B2BCostCenterData> getCostCenters();

	/**
	 * Gets all visible active cost centers for the currently logged-in
	 * {@link de.hybris.platform.b2b.model.B2BCustomerModel} based on his parent B2Unit
	 * 
	 * @return A collection of {@link de.hybris.platform.b2bacceleratorfacades.order.data.B2BCostCenterData}
	 */
    public List<? extends B2BCostCenterData> getActiveCostCenters();

	/**
	 * Get view details for a given Cost center code
	 * 
	 * @param costCenterCode
	 * @return B2BCostCenterData
	 */
    public B2BCostCenterData getCostCenterDataForCode(String costCenterCode);

	/**
	 * Update the cost center details for edit cost centers flow
	 * 
	 */
    public void updateCostCenter(B2BCostCenterData b2BCostCenterData);

	/**
	 * Add cost center
	 * 
	 */
    public void addCostCenter(B2BCostCenterData b2BCostCenterData);

	/**
	 * Enable/disable for a cost center. active set to true denotes enabling cost center and vice versa.
	 * 
	 * @param costCenterCode
	 * @param active
	 */
    public void enableDisableCostCenter(String costCenterCode, boolean active);


	/**
	 * Select a budget for cost center
	 * 
	 * @param costCenterCode
	 * @param budgetCode
	 * @return the resulting {@link B2BSelectionData}
	 */
    public B2BSelectionData selectBudgetForCostCenter(String costCenterCode, String budgetCode);

	/**
	 * Deselect a budget for a cost center
	 * 
	 * @param costCenterCode
	 * @param budgetCode
	 * @return the resulting {@link B2BSelectionData}
	 */
    public B2BSelectionData deSelectBudgetForCostCenter(String costCenterCode, String budgetCode);
}