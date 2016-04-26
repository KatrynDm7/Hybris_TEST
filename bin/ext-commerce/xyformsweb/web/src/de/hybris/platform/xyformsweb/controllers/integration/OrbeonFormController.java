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
package de.hybris.platform.xyformsweb.controllers.integration;

import de.hybris.platform.xyformsfacades.data.YFormDataData;
import de.hybris.platform.xyformsfacades.data.YFormDefinitionData;
import de.hybris.platform.xyformsfacades.form.YFormFacade;
import de.hybris.platform.xyformsservices.enums.YFormDataTypeEnum;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;


/**
 * OrbeonFormController Integration for Orbeon Persistence API Use for providing Create, Read, Update and delete
 * services for integrated Orbeon functions. This will also handle Search and other Orbeon provided functions.
 */
@Controller
@Scope("tenant")
public class OrbeonFormController extends AbstractController
{
	@Resource(name = "yFormFacade")
	private YFormFacade yFormFacade;

	protected static final Logger LOG = Logger.getLogger(OrbeonFormController.class);
	protected static final String FR_SERVICE_RESOURCE_PREFIX = "/fr/service/hybris";
	protected static final String ORBEON_FORM_DEFINITION_VERSION = "Orbeon-Form-Definition-Version";
	protected static final String FORM_BUILDER_NEXT_VERSION = "next";
	protected static final String SEARCH_FORMDATA_EMPTY_SET = "<?xml version='1.0' encoding='utf-8'?><documents total='0' search-total='0' page-size='10' page-number='1' query=''></documents>";
	protected static final String SEARCH_FORMDEFINITIONS_EMTPY_SET = "<?xml version='1.0' encoding='utf-8'?><forms xmlns:oxf='http://www.orbeon.com/oxf/processors' xmlns:f='http//www.orbeon.com/function' xmlns:xsl='http://www.w3.org/1999/XSL/Transform' xmlns:xi='http://www.w3.org/2001/XInclude' xmlns:ev='http://www.w3.org/2001/xml-events' xmlns:sql='http://orbeon.org/oxf/xml/sql' xmlns:p='http://www.orbeon.com/oxf/pipeline' xmlns:xs='http://www.w3.org/2001/XMLSchema' xmlns:fr='http://orbeon.org/oxf/xml/form-runner' xmlns:xf='http://www.w3.org/2002/xforms' xmlns:odt='http://orbeon.org/oxf/xml/datatypes' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'></forms>";

	/**
	 * Get yForm Definition. <br/>
	 * On passing in the applicationId and the formId will return a form.xhtml for the Persistence service call. If
	 * documentId is provided then it will return the form definition associated to it. This is valid when dealing with a
	 * form definition that has multiple versions.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param documentId
	 * @param response
	 */
	@RequestMapping(method = RequestMethod.GET, value = FR_SERVICE_RESOURCE_PREFIX
			+ "/crud/{applicationId}/{formId}/form/form.xhtml")
	@ResponseBody
	public String getFormDefinition(@PathVariable final String applicationId, @PathVariable final String formId,
			@RequestParam(value = "document", required = false) final String documentId, final HttpServletResponse response)
			throws ServletException, IOException, YFormServiceException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getFormDefinition: applicationId=" + applicationId + " | formId=" + formId
					+ (documentId != null ? " | documentId=" + documentId : ""));
		}

		YFormDefinitionData yformDefinition = null;

		//The source of the xhtml should be retrieved from the DB when that's available.
		try
		{
			if (documentId == null)
			{
				yformDefinition = yFormFacade.getYFormDefinition(applicationId, formId);
			}
			else
			{
				yformDefinition = yFormFacade.getYFormDefinition(documentId);
			}
		}
		catch (final Exception e)
		{
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return "";
		}

		String r = "";

		if (yformDefinition != null && yformDefinition.getContent() != null)
		{
			r = yformDefinition.getContent();
		}
		response.setHeader(ORBEON_FORM_DEFINITION_VERSION, "" + yformDefinition.getVersion());
		return r;
	}

	/**
	 * Put Form Definition. <br/>
	 * Publish at the FormBuilder will save the new form definition by calling this service function.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param documentId
	 * @param request
	 */
	@RequestMapping(method = RequestMethod.PUT, value = FR_SERVICE_RESOURCE_PREFIX
			+ "/crud/{applicationId}/{formId}/form/form.xhtml")
	public void putFormDefinition(@PathVariable final String applicationId, @PathVariable final String formId,
			@RequestParam(value = "document", required = false) final String documentId, final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException, IOException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("putFormDefinition: applicationId=" + applicationId + " | formId=" + formId
					+ (documentId != null ? " | documentId=" + documentId : ""));
		}

		final InputStream input = request.getInputStream();
		if (input != null)
		{
			final String content = IOUtils.toString(input, "UTF-8");
			try
			{
				// Checks if user has selected a new version, or update the existing one.
				// (presumably the latest one, the way to know this is through the documentId)

				// If the header is not present, we are going to assume we want to create a new version
				final String versionHeader = request.getHeader(ORBEON_FORM_DEFINITION_VERSION);

				final boolean update = !FORM_BUILDER_NEXT_VERSION.equals(versionHeader) && versionHeader != null;

				LOG.debug(versionHeader);

				final YFormDefinitionData yformDefinition;
				if (update)
				{
					// only the latest version is able to be updated
					yformDefinition = yFormFacade.updateYFormDefinition(applicationId, formId, content, documentId);
				}
				else
				{
					yformDefinition = yFormFacade.createYFormDefinition(applicationId, formId, content, documentId);
				}

				response.setStatus(HttpServletResponse.SC_OK);
				LOG.debug("Header [" + versionHeader + "] - Created Version [" + yformDefinition.getVersion() + "]");
				response.setHeader(ORBEON_FORM_DEFINITION_VERSION, "" + yformDefinition.getVersion());
			}
			catch (final YFormServiceException e)
			{
				LOG.error(e.getMessage(), e);
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getLocalizedMessage());
			}
		}
		else
		{
			response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, "there is no form definition received.");
		}
	}

	/**
	 * Retrieves the DRAFT version of a form data.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param formDataId
	 * @param response
	 */
	@RequestMapping(method = RequestMethod.GET, value = FR_SERVICE_RESOURCE_PREFIX
			+ "/crud/{applicationId}/{formId}/draft/{formDataId}/data.xml")
	@ResponseBody
	public String getFormDataDraft(@PathVariable final String applicationId, @PathVariable final String formId,
			@PathVariable final String formDataId, final HttpServletResponse response) throws ServletException, IOException,
			YFormServiceException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getFormData: applicationId=" + applicationId + " | formId=" + formId + " | formDataId=" + formDataId
					+ " | formDataType=DRAFT");
		}

		final YFormDataTypeEnum type = YFormDataTypeEnum.DRAFT;
		YFormDataData yformData = null;
		try
		{
			yformData = yFormFacade.getYFormData(formDataId, type);
		}
		catch (final Exception e)
		{
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return "";
		}

		if (yformData != null && yformData.getContent() != null)
		{
			return yformData.getContent();
		}

		return "";
	}

	/**
	 * Retrieves the DATA version of a form data.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param formDataId
	 * @param response
	 */
	@RequestMapping(method = RequestMethod.GET, value = FR_SERVICE_RESOURCE_PREFIX
			+ "/crud/{applicationId}/{formId}/data/{formDataId}/data.xml")
	@ResponseBody
	public String getFormDataData(@PathVariable final String applicationId, @PathVariable final String formId,
			@PathVariable final String formDataId, final HttpServletResponse response) throws ServletException, IOException,
			YFormServiceException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getFormData: applicationId=" + applicationId + " | formId=" + formId + " | formDataId=" + formDataId
					+ " | formDataType=DATA");
		}

		YFormDataData yformData = null;
		// Let's check first if there is a DRAFT record
		try
		{
			yformData = yFormFacade.getYFormData(formDataId, YFormDataTypeEnum.DRAFT);
			return yformData.getContent();
		}
		catch (final Exception e)
		{
			yformData = null;
		}

		// let's get the DATA version of it
		try
		{
			yformData = yFormFacade.getYFormData(formDataId, YFormDataTypeEnum.DATA);
		}
		catch (final Exception e)
		{
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return "";
		}

		return yformData.getContent();
	}

	/**
	 * Saves the amend form data back to the database. Creates a new YFormData if it does not exist previously.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param formDataType
	 * @param formDataId
	 * @param request
	 * @param response
	 */
	@RequestMapping(method = RequestMethod.PUT, value = FR_SERVICE_RESOURCE_PREFIX
			+ "/crud/{applicationId}/{formId}/{formDataType:data|draft}/{formDataId}/data.xml")
	public void putFormData(@PathVariable final String applicationId, @PathVariable final String formId,
			@PathVariable final String formDataType, @PathVariable final String formDataId, final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException, IOException, YFormServiceException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("putFormData: applicationId=" + applicationId + " | formId=" + formId + " | formDataId=" + formDataId
					+ " | formDataType=" + formDataType);
		}

		final YFormDataTypeEnum type = YFormDataTypeEnum.valueOf(formDataType.toUpperCase());

		final InputStream input = request.getInputStream();
		final String formDataContent = IOUtils.toString(input, "UTF-8");
		try
		{
			yFormFacade.createOrUpdateYFormData(applicationId, formId, formDataId, type, formDataContent);
			response.setStatus(HttpServletResponse.SC_OK);
		}
		catch (final YFormServiceException e)
		{
			response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, e.getLocalizedMessage());
		}
	}


	/**
	 * Search for form data, no actual implementation yet, only returns an empty xml, this to minimize error logs on the
	 * orbeon side.
	 * <p>
	 * This method is also called when versioning is enabled and the query is as follows:
	 * <p>
	 * <code>
	 *    <?xml version="1.0" encoding="UTF-8"?>
	 *    <search>
	 *       <drafts for-document-id="b7e555ee37b66eac4fb4d1de74870e98b17467f9">only</drafts>
	 *       <page-size>10</page-size>
	 *       <page-number>1</page-number>
	 *       <lang>en</lang>
	 *    </search>
	 * </code>
	 * <p>
	 * When a draft record is found for this document a dialog is shown to the user to make a choice, whether to use the
	 * DRAFT record or the DATA one.
	 * <p>
	 * In this implementation, an empty set is returned and the responsibility to deal with this scenario is for
	 * <code>getFormDataData()</code>
	 * 
	 * @param applicationId
	 * @param formId
	 * @param request
	 * @param response
	 */

	@RequestMapping(method = RequestMethod.POST, value = FR_SERVICE_RESOURCE_PREFIX + "/search/{applicationId}/{formId}")
	public void searchFormData(@PathVariable final String applicationId, @PathVariable final String formId,
			final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException,
			YFormServiceException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("searchFormData: applicationId=" + applicationId + " | formId=" + formId);
			LOG.debug(request.getHeader(ORBEON_FORM_DEFINITION_VERSION));

			final InputStream input = request.getInputStream();
			final String searchBody = IOUtils.toString(input, "UTF-8");
			LOG.debug("searchFormData Body:" + searchBody);
		}

		try
		{
			response.setStatus(HttpServletResponse.SC_OK);
			response.addHeader("Content-Type", "text/xml");
			IOUtils.write(SEARCH_FORMDATA_EMPTY_SET, response.getOutputStream());
		}
		catch (final IOException e)
		{
			response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, e.getLocalizedMessage());
		}
	}

	/**
	 * Search for form definitions, no actual implementation yet, only returns an empty xml, this to minimize error logs
	 * on the orbeon side.
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(method = RequestMethod.GET, value =
	{ FR_SERVICE_RESOURCE_PREFIX + "/form", FR_SERVICE_RESOURCE_PREFIX + "/form/{applicationId}",
			FR_SERVICE_RESOURCE_PREFIX + "/form/{applicationId}/{formId}" })
	public void searchFormDefinitions(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException, YFormServiceException
	{
		final Map<String, String> pathVariables = (Map<String, String>) request
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		final String applicationId = pathVariables.get("applicationId");
		final String formId = pathVariables.get("formId");

		if (LOG.isDebugEnabled())
		{
			LOG.debug("searchFormDefinitions [" + applicationId + "][" + formId + "]");
			LOG.debug(request.getHeader(ORBEON_FORM_DEFINITION_VERSION));

			final InputStream input = request.getInputStream();
			final String searchBody = IOUtils.toString(input, "UTF-8");
			LOG.debug("searchFormDefinition Body:" + searchBody);
		}

		try
		{
			response.setStatus(HttpServletResponse.SC_OK);
			response.addHeader("Content-Type", "text/xml");
			IOUtils.write(SEARCH_FORMDEFINITIONS_EMTPY_SET, response.getOutputStream());
		}
		catch (final IOException e)
		{
			response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, e.getLocalizedMessage());
		}
	}
}
