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
package de.hybris.platform.xyformsservices.daos;

import de.hybris.platform.xyformsservices.enums.YFormDataTypeEnum;
import de.hybris.platform.xyformsservices.model.YFormDataModel;
import de.hybris.platform.xyformsservices.model.YFormDefinitionModel;

import java.util.List;


/**
 * Provides methods for accessing informations about yForms
 */
public interface YFormDao
{
	/**
	 * Retrieves the latest Form Definition given an application id and a form id.
	 * 
	 * @param applicationId
	 * @param formId
	 * @return Form Definition
	 */
	YFormDefinitionModel findYFormDefinition(final String applicationId, final String formId);

	/**
	 * Retrieves the Form Definitions given an application id and a form id.
	 * 
	 * @param applicationId
	 * @param formId
	 * @return Form Definition
	 */
	List<YFormDefinitionModel> findYFormDefinitions(final String applicationId, final String formId);

	/**
	 * Retrieves a Form Definition given an application id, form id and version number
	 * 
	 * @param applicationId
	 * @param formId
	 * @param version
	 * @return Form Definition
	 */
	YFormDefinitionModel findYFormDefinition(final String applicationId, final String formId, final int version);

	/**
	 * Retrieves a Form Data given a data id and type.
	 * 
	 * @param formDataId
	 * @param type
	 * @return Form Data
	 */
	YFormDataModel findYFormData(final String formDataId, final YFormDataTypeEnum type);

	/**
	 * Retrieves a Form Data given an application id, form id and reference id.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param refId
	 * @return Form Data
	 */
	YFormDataModel findYFormData(final String applicationId, final String formId, final String refId, final YFormDataTypeEnum type);

	/**
	 * Retrieves a Form Data given a refeernce id.
	 * 
	 * @param refId
	 * @return Form Data
	 */
	List<YFormDataModel> findYFormDataByRefId(final String refId);
}