/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.productconfig.runtime.ssc;

import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;


/**
 * Helper for validating and formatting intervals of characteristic domain.
 */
public interface IntervalInDomainHelper
{

	/**
	 * Validates whether the given value belongs to one of intervals of characteristic domain.
	 *
	 * @param value
	 *           characteristic value
	 * @param cstic
	 *           characteristic model
	 * @return true if the value belongs to one of intervals of characteristic domain
	 */
	public boolean validateIntervals(final String value, final CsticModel cstic);


	/**
	 * Retrieves concatenated string of intervals in characteristic domain.
	 *
	 * @param cstic
	 *           characteristic model
	 * @return concatenated string of intervals in characteristic domain
	 */
	public String retrieveIntervalMask(final CsticModel cstic);

	/**
	 * Converts an interval of characteristic domain into an external format.
	 *
	 * @param interval
	 *           interval in internal format
	 * @return interval in external format
	 */
	public String formatNumericInterval(final String interval);

	/**
	 * Converts a characteristic value into an external format.
	 *
	 * @param value
	 *           characteristic value in internal format
	 * @return characteristic value in external format
	 */
	public String formatNumericValue(final String value);

	/**
	 * Retrieves error message.
	 *
	 * @param value
	 *           characteristic value in external format
	 * @param interval
	 *           interval in external format
	 * @return error message
	 */
	public String retrieveErrorMessage(String value, String interval);

}
