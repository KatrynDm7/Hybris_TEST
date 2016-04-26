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
import de.hybris.platform.xyformsservices.enums.YFormDataTypeEnum;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;

import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



/**
 * No transformation is applied to the formData content. It only adds the reference Id to the yForm Data.
 */
public class ReferenceIdTransformerYFormPreprocessorStrategy extends TransformerYFormPreprocessorStrategy
{
	public static final String REFERENCE_ID = "refId";

	@Override
	protected String transform(final String xmlContent, final Map<String, Object> params) throws YFormProcessorException
	{
		return xmlContent;
	}

	@Override
	protected void validate(final String applicationId, final String formId, final YFormDataActionEnum action,
			final String formDataId, final Map<String, Object> params) throws YFormServiceException
	{
		super.validate(applicationId, formId, action, formDataId, params);
		if (params.get(REFERENCE_ID) == null)
		{
			throw new YFormServiceException("Reference Id must be provided");
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	protected void save(final String applicationId, final String formId, final String formDataId, final String content,
			final Map<String, Object> params) throws YFormServiceException
	{
		// current algorithm to detect if a form needs to be cloned needs a DATA record
		final String refId = (String) params.get(REFERENCE_ID);
		try
		{
			getYFormFacade().getYFormData(formDataId, YFormDataTypeEnum.DATA);
		}
		catch (final YFormServiceException e)
		{
			getYFormFacade().createOrUpdateYFormData(applicationId, formId, formDataId, YFormDataTypeEnum.DATA, refId, content);
		}

		// We create or update always a DRAFT record, which is the one orbeon is going to pick up.
		getYFormFacade().createOrUpdateYFormData(applicationId, formId, formDataId, YFormDataTypeEnum.DRAFT, refId, content);
	}
}