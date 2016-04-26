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
package de.hybris.platform.xyformsservices.form;

import de.hybris.platform.xyformsservices.enums.YFormDataTypeEnum;
import de.hybris.platform.xyformsservices.enums.YFormDefinitionStatusEnum;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;
import de.hybris.platform.xyformsservices.model.YFormDataModel;
import de.hybris.platform.xyformsservices.model.YFormDefinitionModel;

import java.util.List;


/**
 * Implements methods for managing yForms.
 */
public interface YFormService
{
	/**
	 * For a given applicationId and formId a form definition is returned.
	 * 
	 * @param applicationId
	 * @param formId
	 * @return Form Definition
	 */
	public YFormDefinitionModel getYFormDefinition(final String applicationId, final String formId) throws YFormServiceException;

	/**
	 * For testing purposes this method is provided, it shouldn't be used to get the latest YFormDefinition
	 * 
	 * @param applicationId
	 * @param formId
	 * @param version
	 * @throws YFormServiceException
	 */
	public YFormDefinitionModel getYFormDefinition(String applicationId, String formId, int version) throws YFormServiceException;


	/**
	 * Update YFormDefinition if it exists in the database. <br/>
	 * 
	 * @param applicationId
	 * @param formId
	 * @param content
	 * @param documentId
	 * @throws YFormServiceException
	 */
	public YFormDefinitionModel updateYFormDefinition(final String applicationId, final String formId, final String content,
			final String documentId) throws YFormServiceException;

	/**
	 * Create a new YFormDefinition by the given parameters. <br/>
	 * The applicationId should be a existed Catalog, or an exception will be thrown with catalog not available message.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param title
	 * @param description
	 * @param content
	 * @param documentId
	 * @throws YFormServiceException
	 */
	public YFormDefinitionModel createYFormDefinition(final String applicationId, final String formId, final String title,
			final String description, final String content, final String documentId) throws YFormServiceException;

	/**
	 * For a given id and type a form data is returned.
	 * 
	 * @param formDataId
	 * @param type
	 * @return Form Data
	 */
	public YFormDataModel getYFormData(final String formDataId, final YFormDataTypeEnum type) throws YFormServiceException;

	/**
	 * Create or Update YFormDataModel.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param formDataId
	 * @param type
	 * @param refId
	 * @param content
	 */
	public YFormDataModel createOrUpdateYFormData(final String applicationId, final String formId, final String formDataId,
			final YFormDataTypeEnum type, final String refId, final String content) throws YFormServiceException;

	/**
	 * Update YFormData by the given form data id.
	 * 
	 * @param formDataId
	 * @param type
	 * @param content
	 */
	public YFormDataModel updateYFormData(final String formDataId, final YFormDataTypeEnum type, final String content)
			throws YFormServiceException;

	/**
	 * Create YFormData if no previous exists.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param formDataId
	 * @param type
	 * @param refId
	 * @param content
	 */
	public YFormDataModel createYFormData(final String applicationId, final String formId, final String formDataId,
			final YFormDataTypeEnum type, final String refId, final String content) throws YFormServiceException;


	/**
	 * Create YFormData if no previous exists.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param formDataId
	 * @param type
	 * @param refId
	 * @param content
	 * @param ownerApplicationId
	 * @param ownerFormId
	 */
	public YFormDataModel createYFormData(final String applicationId, final String formId, final String formDataId,
			final YFormDataTypeEnum type, final String refId, final String content, String ownerApplicationId, String ownerFormId)
			throws YFormServiceException;

	/**
	 * Create YFormData if no previous existes.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param formDataId
	 * @param type
	 * @param refId
	 * @param content
	 * @param ownerApplicationId
	 * @param ownerFormId
	 * @param system
	 */
	public YFormDataModel createYFormData(final String applicationId, final String formId, final String formDataId,
			final YFormDataTypeEnum type, final String refId, final String content, String ownerApplicationId, String ownerFormId,
			boolean system) throws YFormServiceException;

	/**
	 * For the given parameters a YFormDataModel is returned.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param refId
	 * @param type
	 * @return YFormDataModel
	 * @throws YFormServiceException
	 */
	YFormDataModel getYFormData(final String applicationId, final String formId, final String refId, final YFormDataTypeEnum type)
			throws YFormServiceException;

	/**
	 * For the given refId a list of YFormDataModel is returned.
	 * 
	 * @param refId
	 * @return List<YFormDataModel>
	 */
	List<YFormDataModel> getYFormDataByRefId(final String refId);

	/**
	 * Sets the status for one or several YFormDefinition.
	 * 
	 * @param status
	 */
	public void setFormDefinitionStatus(String applicationId, String formId, YFormDefinitionStatusEnum status);

}
