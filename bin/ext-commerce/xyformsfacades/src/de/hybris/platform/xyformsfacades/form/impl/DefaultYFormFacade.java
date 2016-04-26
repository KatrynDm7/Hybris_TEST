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
package de.hybris.platform.xyformsfacades.form.impl;

import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.xyformsfacades.data.YFormDataData;
import de.hybris.platform.xyformsfacades.data.YFormDefinitionData;
import de.hybris.platform.xyformsfacades.form.YFormFacade;
import de.hybris.platform.xyformsfacades.proxy.ProxyFacade;
import de.hybris.platform.xyformsfacades.strategy.preprocessor.YFormPreprocessorStrategy;
import de.hybris.platform.xyformsfacades.strategy.preprocessor.YFormProcessorException;
import de.hybris.platform.xyformsfacades.utils.FormDefinitionUtils;
import de.hybris.platform.xyformsservices.enums.YFormDataActionEnum;
import de.hybris.platform.xyformsservices.enums.YFormDataTypeEnum;
import de.hybris.platform.xyformsservices.enums.YFormDefinitionStatusEnum;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;
import de.hybris.platform.xyformsservices.form.YFormService;
import de.hybris.platform.xyformsservices.model.YFormDataModel;
import de.hybris.platform.xyformsservices.model.YFormDefinitionModel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom2.Element;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Default Form Facade to handle yForm definitions and yForm data.
 */
public class DefaultYFormFacade implements YFormFacade
{
	private static final Logger LOG = Logger.getLogger(DefaultYFormFacade.class);

	protected static final String ORBEON_APPLICATION_ID = "orbeon";
	protected static final String ORBEON_FORM_ID = "builder";
	protected static final String TEMPLATE_FORM_ID = "template";

	@Resource(name = "yformService")
	private YFormService yformService;

	@Resource(name = "yformDefinitionConverter")
	private Converter<YFormDefinitionModel, YFormDefinitionData> yformDefinitionConverter;

	@Resource(name = "yformDataConverter")
	private Converter<YFormDataModel, YFormDataData> yformDataConverter;

	@Resource(name = "proxyFacade")
	private ProxyFacade proxyFacade;

	/**
	 * Generates a new form Data id
	 */
	@Override
	public String getNewFormDataId()
	{
		return UUID.randomUUID().toString();
	}

	/**
	 * For a given application id and form id a form definition is returned.
	 * 
	 * @param applicationId
	 * @param formId
	 * @return YFormDefinitionData
	 */
	@Override
	public YFormDefinitionData getYFormDefinition(final String applicationId, final String formId) throws YFormServiceException
	{
		final YFormDefinitionModel yformDefinition = yformService.getYFormDefinition(applicationId, formId);
		return yformDefinitionConverter.convert(yformDefinition);
	}

	/**
	 * For a given application id, form id and version number a form definition is returned.
	 * 
	 * @param applicationId
	 * @param formId
	 * @return YFormDefinitionData
	 */
	@Override
	public YFormDefinitionData getYFormDefinition(final String applicationId, final String formId, final int version)
			throws YFormServiceException
	{
		final YFormDefinitionModel yformDefinition = yformService.getYFormDefinition(applicationId, formId, version);
		return yformDefinitionConverter.convert(yformDefinition);
	}


	/**
	 * For a given document id a form definition is returned.
	 * 
	 * @param documentId
	 * @return YFormDefinitionData
	 */
	@Override
	public YFormDefinitionData getYFormDefinition(final String documentId) throws YFormServiceException
	{
		// Check first if there is a DRAFT document
		final YFormDataModel yformData = this.getYFormDataModel(documentId);

		final YFormDefinitionModel yformDefinition = yformData.getFormDefinition();
		return yformDefinitionConverter.convert(yformDefinition);
	}

	/**
	 * Create a new YFormDefinition by the given parameters.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param content
	 * @param documentId
	 * @return YFormDefinitionData
	 * @throws YFormServiceException
	 */
	@Override
	public YFormDefinitionData createYFormDefinition(final String applicationId, final String formId, final String content,
			final String documentId) throws YFormServiceException
	{
		validateFormDefinitionMetadata(applicationId, formId, documentId);

		// We obtain title and description from the form definition itself
		final Element metadata = FormDefinitionUtils.getFormDefinitionMetadata(content);
		final String title = metadata.getChildText("title");
		final String description = metadata.getChildText("description");

		final YFormDefinitionModel yFormDefinition = yformService.createYFormDefinition(applicationId, formId, title, description,
				content, documentId);
		return yformDefinitionConverter.convert(yFormDefinition);
	}

	/**
	 * Checks if the metadata stored in the YFormData counterpart matches the given applicationId and formId
	 * 
	 * @param applicationId
	 * @param formId
	 * @param documentId
	 * @throws YFormServiceException
	 */
	protected void validateFormDefinitionMetadata(final String applicationId, final String formId, final String documentId)
			throws YFormServiceException
	{
		// at this point, using backoffice, this shouldn't be allowed. This because the records are created at
		// the same time and linked each other. This avoid the case were the user edits the form's content and
		// the builder tries to publish the form with those values.

		if (documentId != null)
		{
			final YFormDataModel formData = yformService.getYFormData(documentId, YFormDataTypeEnum.DATA);
			// If there is no formData associated to documentId, an exception is thrown.
			if (!applicationId.equals(formData.getApplicationId()) || !formId.equals(formData.getFormId()))
			{
				throw new YFormServiceException("Form Definition counterpart differs from given applicationId and formId");
			}
		}
	}

	/**
	 * Update YFormDefinition if it exists in the database.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param content
	 * @param documentId
	 * @return YFormDefinitionData
	 * @throws YFormServiceException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public YFormDefinitionData updateYFormDefinition(final String applicationId, final String formId, final String content,
			final String documentId) throws YFormServiceException
	{
		validateFormDefinitionMetadata(applicationId, formId, documentId);

		// here we should use title and description from the definition itself and modify the content.
		final YFormDefinitionData yform = this.getYFormDefinition(applicationId, formId); // only latest version is able to be updated
		final String definition = FormDefinitionUtils.getFormDefinitionContent(content, yform);

		final YFormDefinitionModel yformDefinition = yformService.updateYFormDefinition(applicationId, formId, definition,
				documentId);

		// ...and to keep info synchronized, we update the YFormDefinition Counterpart
		if (!StringUtils.isEmpty(documentId))
		{
			yformService.updateYFormData(documentId, YFormDataTypeEnum.DATA, definition);
		}
		return yformDefinitionConverter.convert(yformDefinition);
	}

	/**
	 * For a given id a form data is returned. First the DRAFT version if there one, else the DATA version of it.
	 * 
	 * @param formDataId
	 * @throws YFormServiceException
	 */
	@Override
	public YFormDataData getYFormData(final String formDataId) throws YFormServiceException
	{
		final YFormDataModel yFormDataModel = this.getYFormDataModel(formDataId);
		return yformDataConverter.convert(yFormDataModel);
	}

	/**
	 * For a given id a form data is returned, if there is first a DRAFT document it will be return, else the DATA
	 * version of it
	 * 
	 * @param formDataId
	 * @return YFormDataData
	 */
	protected YFormDataModel getYFormDataModel(final String formDataId) throws YFormServiceException
	{
		YFormDataModel yFormDataModel;
		try
		{
			yFormDataModel = yformService.getYFormData(formDataId, YFormDataTypeEnum.DRAFT);
		}
		catch (final YFormServiceException e)
		{
			yFormDataModel = null;
		}

		if (yFormDataModel == null)
		{
			yFormDataModel = yformService.getYFormData(formDataId, YFormDataTypeEnum.DATA);
		}
		return yFormDataModel;
	}

	/**
	 * For a given id and type a form data is returned.
	 * 
	 * @param formDataId
	 * @return YFormDataData
	 */
	@Override
	public YFormDataData getYFormData(final String formDataId, final YFormDataTypeEnum type) throws YFormServiceException
	{
		final YFormDataModel yFormDataModel = yformService.getYFormData(formDataId, type);
		return yformDataConverter.convert(yFormDataModel);
	}

	/**
	 * For the given application id, form id, reference id and form type a form data is returned.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param refId
	 * @param type
	 * @return YFormDataData
	 * @throws YFormServiceException
	 */
	@Override
	public YFormDataData getYFormData(final String applicationId, final String formId, final String refId,
			final YFormDataTypeEnum type) throws YFormServiceException
	{
		YFormDataData yFormData = null;

		try
		{
			final YFormDataModel yFormDataModel = yformService.getYFormData(applicationId, formId, refId, type);
			yFormData = yformDataConverter.convert(yFormDataModel);
		}
		catch (final YFormServiceException e)
		{
			if (!(e.getCause() instanceof ModelNotFoundException))
			{
				throw e;
			}
		}

		return yFormData;
	}

	/**
	 * For the given application id, form id and reference id a form data is returned.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param refId
	 * @return YFormDataData
	 * @throws YFormServiceException
	 */
	@Override
	public YFormDataData getYFormData(final String applicationId, final String formId, final String refId)
			throws YFormServiceException
	{
		YFormDataModel yFormDataModel;
		try
		{
			yFormDataModel = yformService.getYFormData(applicationId, formId, refId, YFormDataTypeEnum.DRAFT);
		}
		catch (final YFormServiceException e)
		{
			yFormDataModel = null;
		}

		if (yFormDataModel == null)
		{
			yFormDataModel = yformService.getYFormData(applicationId, formId, refId, YFormDataTypeEnum.DATA);
		}
		return yformDataConverter.convert(yFormDataModel);
	}

	/**
	 * Given an application id, form id and version number, the yForm Data counterpart is created.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param version
	 * @throws YFormServiceException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String recreateYFormDefinitionCounterpart(final String applicationId, final String formId, final int version)
			throws YFormServiceException
	{
		final YFormDefinitionData yformDefinition = this.getYFormDefinition(applicationId, formId, version);

		String template = yformDefinition.getContent();
		if (StringUtils.isEmpty(template))
		{
			// We get the template from the application's library, when there is no library available, then the
			// default one is the orbeon's one.
			try
			{
				template = this.getYFormDefinition(yformDefinition.getApplicationId(), TEMPLATE_FORM_ID).getContent();
			}
			catch (final YFormServiceException e)
			{
				template = this.getYFormDefinition(ORBEON_APPLICATION_ID, TEMPLATE_FORM_ID).getContent();
			}
		}
		final String content = FormDefinitionUtils.getFormDefinitionContent(template, yformDefinition);

		String formDataId = yformDefinition.getDocumentId();
		if (formDataId == null)
		{
			formDataId = this.getNewFormDataId();
		}

		// we verify if the formDataId already exists
		try
		{
			yformService.getYFormData(formDataId, YFormDataTypeEnum.DATA);
			LOG.warn("You are trying to recreate a couterpart that already exists [" + applicationId + "][" + formId + "]["
					+ version + "]");
			return formDataId;
		}
		catch (final YFormServiceException e)
		{
			// nothing
		}

		yformService.createYFormData(ORBEON_APPLICATION_ID, ORBEON_FORM_ID, formDataId, YFormDataTypeEnum.DATA, null, content,
				applicationId, formId, true);
		this.updateYFormDefinition(applicationId, formId, content, formDataId);

		return formDataId;
	}

	/**
	 * Update YFormData by the given form data id.
	 * 
	 * @param formDataId
	 * @param type
	 * @param content
	 * @return YFormDataData
	 */
	@Override
	public YFormDataData updateYFormData(final String formDataId, final YFormDataTypeEnum type, final String content)
			throws YFormServiceException
	{
		final YFormDataModel yFormDataModel = yformService.updateYFormData(formDataId, type, content);
		return yformDataConverter.convert(yFormDataModel);
	}

	/**
	 * Create YFormData if no previous one exists. It assigns it to the corresponding YFormDefinition.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param formDataId
	 * @param type
	 * @param refId
	 * @param content
	 * @return YFormDataData
	 */
	@Override
	public YFormDataData createYFormData(final String applicationId, final String formId, final String formDataId,
			final YFormDataTypeEnum type, final String refId, final String content) throws YFormServiceException
	{
		final YFormDataModel yFormDataModel = yformService.createYFormData(applicationId, formId, formDataId, type, refId, content);
		return yformDataConverter.convert(yFormDataModel);
	}

	/**
	 * Create or Update YFormDataModel.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param formDataId
	 * @param type
	 * @param refId
	 * @param content
	 * @return YFormDataData
	 */
	@Override
	public YFormDataData createOrUpdateYFormData(final String applicationId, final String formId, final String formDataId,
			final YFormDataTypeEnum type, final String refId, final String content) throws YFormServiceException
	{
		final YFormDataModel yFormDataModel = yformService.createOrUpdateYFormData(applicationId, formId, formDataId, type, refId,
				content);
		return yformDataConverter.convert(yFormDataModel);
	}

	/**
	 * Create or Update YFormDataModel.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param formDataId
	 * @param type
	 * @param content
	 * @return YFormDataData
	 */
	@Override
	public YFormDataData createOrUpdateYFormData(final String applicationId, final String formId, final String formDataId,
			final YFormDataTypeEnum type, final String content) throws YFormServiceException
	{
		return this.createOrUpdateYFormData(applicationId, formId, formDataId, type, null, content);
	}

	/**
	 * For a given application id, form id and form data id an inline form definition is returned.<br/>
	 * The inline form definition can be injected in to a page instead of getting entire xform tagged between
	 * <code><xhtml></xhtml></code>
	 * 
	 * @param applicationId
	 * @param formId
	 * @return String
	 */
	@Override
	public String getInlineFormHtml(final String applicationId, final String formId, final String formDataId)
			throws YFormServiceException
	{
		return getInlineFormHtml(applicationId, formId, YFormDataActionEnum.EDIT, formDataId);
	}

	/**
	 * For a given application id and form id an inline form definition is returned.<br/>
	 * The inline form definition can be injected in to a page instead of getting entire xform tagged between
	 * <code><xhtml></xhtml></code>
	 * 
	 * @param applicationId
	 * @param formId
	 * @throws YFormServiceException
	 */
	@Override
	public String getInlineFormHtml(final String applicationId, final String formId) throws YFormServiceException
	{
		return getInlineFormHtml(applicationId, formId, YFormDataActionEnum.NEW, null);
	}

	/**
	 * For a given application id, form id, form data id and action an inline form definition is returned.<br/>
	 * The inline form definition can be injected in to a page instead of getting entire xform tagged between
	 * <code><xhtml></xhtml></code>
	 * 
	 * @param applicationId
	 * @param formId
	 * @param action
	 * @param formDataId
	 * @throws YFormServiceException
	 */
	@Override
	public String getInlineFormHtml(final String applicationId, final String formId, final YFormDataActionEnum action,
			final String formDataId) throws YFormServiceException
	{
		return getInlineFormHtml(applicationId, formId, action, formDataId, null);
	}

	/**
	 * For a given application id, form id, form data id, action and strategy an inline form definition is returned.<br/>
	 * The inline form definition can be injected in to a page instead of getting entire xform tagged between
	 * <code><xhtml></xhtml></code>
	 * 
	 * @param applicationId
	 * @param formId
	 * @param action
	 * @param formDataId
	 * @param strategy
	 * @throws YFormServiceException
	 */
	@Override
	public String getInlineFormHtml(final String applicationId, final String formId, final YFormDataActionEnum action,
			final String formDataId, final YFormPreprocessorStrategy strategy) throws YFormServiceException
	{
		return getInlineFormHtml(applicationId, formId, action, formDataId, strategy, new HashMap<String, Object>());
	}

	/**
	 * For a given application id, form id, form data id, action and strategy (plus parameters) an inline form definition
	 * is returned.<br/>
	 * The inline form definition can be injected in to a page instead of getting entire xform tagged between
	 * <code><xhtml></xhtml></code>
	 * 
	 * @param applicationId
	 * @param formId
	 * @param action
	 * @param formDataId
	 * @param strategy
	 * @param params
	 * @throws YFormServiceException
	 */
	@Override
	public String getInlineFormHtml(final String applicationId, final String formId, final YFormDataActionEnum action,
			final String formDataId, final YFormPreprocessorStrategy strategy, final Map<String, Object> params)
			throws YFormServiceException
	{
		try
		{
			// let's check first if the given yformDefinition is enabled or not
			yformService.getYFormDefinition(applicationId, formId);

			if (strategy != null)
			{
				strategy.process(applicationId, formId, action, formDataId, params);
			}

			final String content = this.proxyFacade.getInlineFormHtml(applicationId, formId, action, formDataId);

			return content;
		}
		catch (final YFormProcessorException e)
		{
			throw new YFormServiceException(e);
		}
	}

	/**
	 * For a given application id, form id and form data id an inline form definition is returned.<br/>
	 * The inline form definition can be injected in to a page instead of getting entire xform tagged between
	 * <code><xhtml></xhtml></code>
	 * 
	 * @param applicationId
	 * @param formId
	 * @param formDataId
	 * @throws YFormServiceException
	 */
	@Override
	public String getFormDataContent(final String applicationId, final String formId, final String formDataId)
			throws YFormServiceException
	{
		YFormDataModel data = null;
		String content = null;
		try
		{
			data = this.getYFormDataModel(formDataId);
			content = data.getContent();
		}
		catch (final YFormServiceException e)
		{
			content = this.getFormDataContentTemplate(applicationId, formId);
		}
		return content;
	}

    @Override
    public String getFormDataContentTemplate(String applicationId, String formId) throws YFormServiceException {

        final YFormDefinitionModel def = yformService.getYFormDefinition(applicationId, formId);

        String content = def.getContent();
        content = FormDefinitionUtils.getFormDefinition(content);

        return content;
    }

    /**
	/**
	 * Indicates if the given Form Data is valid or not.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param formDataId
	 * @throws YFormServiceException
	 */
	@Override
	public boolean validate(final String applicationId, final String formId, final String formDataId) throws YFormServiceException
	{
		YFormDataModel data = null;
		data = this.getYFormDataModel(formDataId);

		// If there is a DRAFT record, then it is not valid.
		if (YFormDataTypeEnum.DRAFT == data.getType())
		{
			return false;
		}

		final String content = getInlineFormHtml(applicationId, formId, formDataId);

		// the heuristic: if the generated form contains xforms-invalid (CSS class) then it is invalid
		return !content.contains("xforms-invalid");
	}

	/**
	 * Changes the state of a form definition. This will include all related versions.
	 * 
	 * @param status
	 */
	@Override
	public void setFormDefinitionStatus(final String applicationId, final String formId, final YFormDefinitionStatusEnum status)
			throws YFormServiceException
	{
		yformService.setFormDefinitionStatus(applicationId, formId, status);
	}

	protected YFormService getYFormService()
	{
		return yformService;
	}

	public void setYFormService(final YFormService yformService)
	{
		this.yformService = yformService;
	}

	public void setProxyFacade(final ProxyFacade proxyFacade)
	{
		this.proxyFacade = proxyFacade;
	}

	protected Converter<YFormDefinitionModel, YFormDefinitionData> getYFormDefinitionConverter()
	{
		return yformDefinitionConverter;
	}

	protected Converter<YFormDataModel, YFormDataData> getYFormDataConverter()
	{
		return yformDataConverter;
	}
}
