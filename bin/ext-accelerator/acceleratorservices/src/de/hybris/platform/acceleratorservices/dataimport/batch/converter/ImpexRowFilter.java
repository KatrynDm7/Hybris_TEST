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
package de.hybris.platform.acceleratorservices.dataimport.batch.converter;

import java.util.Map;


/**
 * Filter to filter out rows not matching a specified filter expression.
 */
public interface ImpexRowFilter
{
	/**
	 * Evaluate a single row and return a false, if the row should be filtered.
	 * 
	 * @param row
	 *           CSV row containing column indexes and values
	 * @return false, if the row should not be converted
	 */
	boolean filter(Map<Integer, String> row);
}
