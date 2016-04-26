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
package de.hybris.platform.xyformsfacades.form;

import de.hybris.platform.xyformsfacades.data.YFormDataData;
import de.hybris.platform.xyformsfacades.data.YFormDefinitionData;
import de.hybris.platform.xyformsfacades.strategy.preprocessor.YFormPreprocessorStrategy;
import de.hybris.platform.xyformsservices.enums.YFormDataActionEnum;
import de.hybris.platform.xyformsservices.enums.YFormDataTypeEnum;
import de.hybris.platform.xyformsservices.enums.YFormDefinitionStatusEnum;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;

import java.util.Map;


/**
 * Form Facade to handle yForm definitions and yForm data.
 */
public interface YFormFacade
{
	/**
	 * Generates a new form Data id
	 */
	public String getNewFormDataId();

	/**
	 * For a given application id and form id a form definition is returned.
	 * 
	 * @param applicationId
	 * @param formId
	 * @return YFormDefinitionData
	 */
	public YFormDefinitionData getYFormDefinition(final String applicationId, final String formId) throws YFormServiceException;

	/**
	 * This method is available for testing purposes, to get the latest YFormDefinition use other method.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param version
	 * @throws YFormServiceException
	 */
	public YFormDefinitionData getYFormDefinition(String applicationId, String formId, int version) throws YFormServiceException;

	/**
	 * For a given document id a form definition is returned.
	 * 
	 * @param documentId
	 * @return YFormDefinitionData
	 */
	public YFormDefinitionData getYFormDefinition(final String documentId) throws YFormServiceException;

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
	public YFormDefinitionData createYFormDefinition(final String applicationId, final String formId, final String content,
			final String documentId) throws YFormServiceException;

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
	public YFormDefinitionData updateYFormDefinition(final String applicationId, final String formId, final String content,
			final String documentId) throws YFormServiceException;

	/**
	 * For a given id a form data is returned. First the DRAFT version if there one, else the DATA version of it.
	 * 
	 * @param formDataId
	 * @throws YFormServiceException
	 */
	public YFormDataData getYFormData(String formDataId) throws YFormServiceException;

	/**
	 * For a given id and type a form data is returned.
	 * 
	 * @param formDataId
	 * @param type
	 * @return YFormDataData
	 */
	public YFormDataData getYFormData(final String formDataId, final YFormDataTypeEnum type) throws YFormServiceException;

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
	public YFormDataData getYFormData(final String applicationId, final String formId, final String refId,
			final YFormDataTypeEnum type) throws YFormServiceException;

	/**
	 * For the given application id, form id and reference id a form data is returned.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param refId
	 * @return YFormDataData
	 * @throws YFormServiceException
	 */
	public YFormDataData getYFormData(final String applicationId, final String formId, final String refId)
			throws YFormServiceException;

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
	public YFormDataData createYFormData(final String applicationId, final String formId, final String formDataId,
			final YFormDataTypeEnum type, final String refId, final String content) throws YFormServiceException;

	/**
	 * Update YFormData by the given form data id.
	 * 
	 * @param formDataId
	 * @param type
	 * @param content
	 * @return YFormDataData
	 */
	public YFormDataData updateYFormData(final String formDataId, final YFormDataTypeEnum type, final String content)
			throws YFormServiceException;

	/**
	 * Create or Update YFormDataModel.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param formDataId
	 * @param type
	 * @param refId
	 * @param formDataContent
	 * @return YFormDataData
	 */
	public YFormDataData createOrUpdateYFormData(final String applicationId, final String formId, final String formDataId,
			final YFormDataTypeEnum type, final String refId, final String formDataContent) throws YFormServiceException;

	/**
	 * Create or Update YFormDataModel.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param formDataId
	 * @param type
	 * @param formDataContent
	 * @return YFormDataData
	 */
	public YFormDataData createOrUpdateYFormData(final String applicationId, final String formId, final String formDataId,
			final YFormDataTypeEnum type, final String formDataContent) throws YFormServiceException;

	/**
	 * For a given application id, form id and form data id an inline form definition is returned.<br/>
	 * The inline form definition can be injected in to a page instead of getting entire xform tagged between
	 * <code><xhtml></xhtml></code>
	 * 
	 * @param applicationId
	 * @param formId
	 * @return String
	 */
	public String getInlineFormHtml(final String applicationId, final String formId, final String formDataId)
			throws YFormServiceException;

	/**
	 * For a given application id and form id an inline form definition is returned.<br/>
	 * The inline form definition can be injected in to a page instead of getting entire xform tagged between
	 * <code><xhtml></xhtml></code>
	 * 
	 * @param applicationId
	 * @param formId
	 * @throws YFormServiceException
	 */
	public String getInlineFormHtml(String applicationId, String formId) throws YFormServiceException;

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
	public String getInlineFormHtml(String applicationId, String formId, YFormDataActionEnum action, String formDataId)
			throws YFormServiceException;

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
	public String getInlineFormHtml(String applicationId, String formId, YFormDataActionEnum action, String formDataId,
			YFormPreprocessorStrategy strategy) throws YFormServiceException;

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
	String getInlineFormHtml(String applicationId, String formId, YFormDataActionEnum action, String formDataId,
			YFormPreprocessorStrategy strategy, Map<String, Object> params) throws YFormServiceException;

	/**
	 * @param applicationId
	 * @param formId
	 * @param formDataId
	 * @throws YFormServiceException
	 */
	public String getFormDataContent(String applicationId, String formId, String formDataId) throws YFormServiceException;

    /**
     * For a given application id and form id a form data content template (an empty content generated from
     * YFormDefinition with given form id) is returned.<br/>
     *
     * @param applicationId
     * @param formId
     * @throws YFormServiceException when there is no YFormDefinition with given applicationId and formId or YFormDefinition has wrong content
     */
    public String getFormDataContentTemplate(String applicationId, String formId) throws YFormServiceException;

	/**
	 * Given an application id, form id and version number, the yForm Data counterpart is created.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param version
	 * @throws YFormServiceException
	 */
	String recreateYFormDefinitionCounterpart(String applicationId, String formId, int version) throws YFormServiceException;

	/**
	 * Changes the state of a form definition. This will include all related versions.
	 * 
	 * @param status
	 */
	public void setFormDefinitionStatus(String applicationId, String formId, YFormDefinitionStatusEnum status)
			throws YFormServiceException;

	/**
	 * Indicates if the given Form Data is valid or not.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param formDataId
	 * @throws YFormServiceException
	 */
	boolean validate(String applicationId, String formId, String formDataId) throws YFormServiceException;

}
