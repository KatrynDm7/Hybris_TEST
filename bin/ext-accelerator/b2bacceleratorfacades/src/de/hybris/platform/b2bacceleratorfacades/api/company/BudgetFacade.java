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
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BBudgetData;
import de.hybris.platform.b2bacceleratorfacades.search.data.BudgetSearchStateData;


public interface BudgetFacade extends SearchFacade<B2BBudgetData, BudgetSearchStateData>
{
	/**
	 * get BudgetData based on code
	 * 
	 * @param budgetCode
	 * @return B2BBudgetData
	 */
	public B2BBudgetData getBudgetDataForCode(final String budgetCode);

	/**
	 * @param b2bBudgetData
	 */
	public void addBudget(B2BBudgetData b2bBudgetData);

	/**
	 * Enable or disable a budget
	 * 
	 * @param b2BudgetCode
	 * @param active
	 * @throws
	 */
    public void enableDisableBudget(String b2BudgetCode, boolean active);


	/**
	 * Update budget details for edit operation
	 * 
	 * @param b2BBudgetData
	 */
    public void updateBudget(B2BBudgetData b2BBudgetData);
}
