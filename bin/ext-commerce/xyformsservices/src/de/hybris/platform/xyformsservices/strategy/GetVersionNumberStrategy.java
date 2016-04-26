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
 */
package de.hybris.platform.xyformsservices.strategy;



/**
 * Strategy for getting a new form number based on some parameters.
 */
public interface GetVersionNumberStrategy
{
	/**
	 * Returns the next version number available for a form definition indentified by applicationId and formId
	 * 
	 * @param applicationId
	 * @param formId
	 */
	public int execute(final String applicationId, final String formId);
}
