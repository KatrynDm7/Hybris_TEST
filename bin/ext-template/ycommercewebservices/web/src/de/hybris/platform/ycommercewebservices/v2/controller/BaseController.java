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
package de.hybris.platform.ycommercewebservices.v2.controller;

import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commercewebservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.PaginationWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.commercewebservicescommons.mapping.DataMapper;
import de.hybris.platform.commercewebservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.commercewebservicescommons.utils.YSanitizer;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.common.collect.Lists;


/**
 * Base Controller. It defines the exception handler to be used by all controllers. Extending controllers can add or
 * overwrite the exception handler if needed.
 */
@Controller
public class BaseController
{
	protected static final String DEFAULT_PAGE_SIZE = "20";
	protected static final String DEFAULT_CURRENT_PAGE = "0";
	protected static final String DEFAULT_FIELD_SET = FieldSetLevelHelper.DEFAULT_LEVEL;
	protected static final String HEADER_TOTAL_COUNT = "X-Total-Count";
	private static final Logger LOG = Logger.getLogger(BaseController.class);
	@Resource(name = "dataMapper")
	protected DataMapper dataMapper;

	protected static String logParam(final String paramName, final long paramValue)
	{
		return paramName + " = " + paramValue;
	}

	protected static String logParam(final String paramName, final Long paramValue)
	{
		return paramName + " = " + paramValue;
	}

	protected static String logParam(final String paramName, final String paramValue)
	{
		return paramName + " = " + logValue(paramValue);
	}

	protected static String logValue(final String paramValue)
	{
		return "'" + sanitize(paramValue) + "'";
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	@ExceptionHandler(
	{ ModelNotFoundException.class })
	public ErrorListWsDTO handleModelNotFoundException(final Exception ex)
	{
		LOG.info("Handling Exception for this request - " + ex.getClass().getSimpleName() + " - " + sanitize(ex.getMessage()));
		if (LOG.isDebugEnabled())
		{
			LOG.debug(ex);
		}
		final ErrorListWsDTO errorListDto = handleErrorInternal(UnknownIdentifierException.class.getSimpleName(), ex.getMessage());
		return errorListDto;
	}

	protected ErrorListWsDTO handleErrorInternal(final String type, final String message)
	{
		final ErrorListWsDTO errorListDto = new ErrorListWsDTO();
		final ErrorWsDTO error = new ErrorWsDTO();
		error.setType(type.replace("Exception", "Error"));
		error.setMessage(sanitize(message));
		errorListDto.setErrors(Lists.newArrayList(error));
		return errorListDto;
	}

	protected void validate(final Object object, final String objectName, final Validator validator)
	{
		final Errors errors = new BeanPropertyBindingResult(object, objectName);
		validator.validate(object, errors);
		if (errors.hasErrors())
		{
			throw new WebserviceValidationException(errors);
		}
	}

    /**
     * Adds pagination field to the 'fields' parameter
     * @param fields
     * @return fields with pagination
     */
    protected String addPaginationField(String fields)
    {
        if (StringUtils.isNotBlank(fields))
        {
            fields += ",";
        }
        fields += "pagination";
        return fields;
    }

	protected void setTotalCountHeader(final HttpServletResponse response, final PaginationWsDTO paginationDto)
	{
        if (paginationDto != null && paginationDto.getTotalResults() != null)
        {
            response.setHeader(HEADER_TOTAL_COUNT, String.valueOf(paginationDto.getTotalResults()));
        }
	}

    protected void setTotalCountHeader(final HttpServletResponse response, final PaginationData paginationDto)
    {
        if (paginationDto != null)
        {
            response.setHeader(HEADER_TOTAL_COUNT, String.valueOf(paginationDto.getTotalNumberOfResults()));
        }
    }

    protected static String sanitize(final String input)
    {
        return YSanitizer.sanitize(input);
    }
}
