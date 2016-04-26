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
package de.hybris.platform.xyformsfacades.strategy.preprocessor;

import de.hybris.platform.xyformsservices.enums.YFormDataActionEnum;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;

import java.util.Map;


/**
 * Preprocessor that applies changes to YForm Items before rendering them.
 */
public interface YFormPreprocessorStrategy
{
	/**
	 * Preprocesses a form
	 * 
	 * @param applicationId
	 * @param formId
	 * @param action
	 * @param formDataId
	 * @throws YFormServiceException
	 * @throws YFormProcessorException
	 */
	public void process(String applicationId, String formId, YFormDataActionEnum action, String formDataId)
			throws YFormServiceException, YFormProcessorException;

	/**
	 * Preprocesses a form
	 * 
	 * @param applicationId
	 * @param formId
	 * @param action
	 * @param formDataId
	 * @param params
	 * @throws YFormServiceException
	 * @throws YFormProcessorException
	 */
	public void process(String applicationId, String formId, YFormDataActionEnum action, String formDataId,
			Map<String, Object> params) throws YFormServiceException, YFormProcessorException;

}
