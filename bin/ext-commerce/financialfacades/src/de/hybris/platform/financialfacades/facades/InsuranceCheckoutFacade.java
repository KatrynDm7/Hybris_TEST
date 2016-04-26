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
package de.hybris.platform.financialfacades.facades;

public interface InsuranceCheckoutFacade
{

	/**
	 * Checks whether the orderEntryData has valid Form Data or not
	 * 
	 * @return boolean
	 */
	boolean orderEntryHasValidFormData();

    /**
     * Checks whether the cart or session have saved form data.
     * @return boolean
     */
    boolean hasSavedFormData();
}
