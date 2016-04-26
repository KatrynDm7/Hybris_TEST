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
package de.hybris.platform.xyformsservices.daos.impl;

import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.xyformsservices.daos.YFormDao;
import de.hybris.platform.xyformsservices.enums.YFormDataTypeEnum;
import de.hybris.platform.xyformsservices.jalo.YFormDefinition;
import de.hybris.platform.xyformsservices.model.YFormDataModel;
import de.hybris.platform.xyformsservices.model.YFormDefinitionModel;

import java.util.List;

import org.apache.log4j.Logger;


/**
 * Data Access Object for managing yForms
 */
public class DefaultYFormDao extends AbstractItemDao implements YFormDao
{
	private static final Logger LOG = Logger.getLogger(DefaultYFormDao.class);

	private static final String QUERY_FORMDATA_BY_APPLICATIONID_FORMID_REFID = "SELECT {formData." + YFormDataModel.PK
			+ "} FROM {" + YFormDataModel._TYPECODE + " AS formData}, {" + YFormDefinitionModel._TYPECODE + " AS formDefinition}"
			+ " WHERE {formData." + YFormDataModel.REFID + "} =?refId AND {formData." + YFormDataModel.TYPE
			+ "} =?type AND {formData." + YFormDataModel.FORMDEFINITION + "} = {formDefinition." + YFormDefinitionModel.PK
			+ "} AND {formDefinition." + YFormDefinitionModel.APPLICATIONID + "} =?applicationId AND {formDefinition."
			+ YFormDefinitionModel.FORMID + "} =?formId";

	private static final String QUERY_LATEST_FORMDEFINITION = "SELECT {formDefinition." + YFormDefinitionModel.PK + "} "
			+ "from {" + YFormDefinitionModel._TYPECODE + " as formDefinition}" + " WHERE {formDefinition."
			+ YFormDefinitionModel.APPLICATIONID + "} = ?applicationId and " + " {formDefinition." + YFormDefinition.FORMID
			+ "} = ?formId and " + "{formDefinition." + YFormDefinitionModel.VERSION + "} = ({{SELECT MAX({fd."
			+ YFormDefinitionModel.VERSION + "}) from {" + YFormDefinitionModel._TYPECODE + " as fd} WHERE {fd."
			+ YFormDefinitionModel.APPLICATIONID + "} = ?applicationId AND {fd." + YFormDefinitionModel.FORMID + "} = ?formId }})";

	@Override
	public YFormDefinitionModel findYFormDefinition(final String applicationId, final String formId)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(QUERY_LATEST_FORMDEFINITION);
		query.addQueryParameter("applicationId", applicationId);
		query.addQueryParameter("formId", formId);

		return getFlexibleSearchService().searchUnique(query);
	}

	@Override
	public YFormDefinitionModel findYFormDefinition(final String applicationId, final String formId, final int version)
	{
		final YFormDefinitionModel example = new YFormDefinitionModel();
		example.setApplicationId(applicationId);
		example.setFormId(formId);
		example.setVersion(version);
		return getFlexibleSearchService().getModelByExample(example);
	}

	@Override
	public List<YFormDefinitionModel> findYFormDefinitions(final String applicationId, final String formId)
	{
		final YFormDefinitionModel example = new YFormDefinitionModel();
		example.setApplicationId(applicationId);
		example.setFormId(formId);
		return getFlexibleSearchService().getModelsByExample(example);
	}

	@Override
	public YFormDataModel findYFormData(final String formDataId, final YFormDataTypeEnum type)
	{
		final YFormDataModel example = new YFormDataModel();
		example.setId(formDataId);
		example.setType(type);

		return getFlexibleSearchService().getModelByExample(example);
	}

	@Override
	public List<YFormDataModel> findYFormDataByRefId(final String refId)
	{
		final YFormDataModel example = new YFormDataModel();
		example.setRefId(refId);

		return getFlexibleSearchService().getModelsByExample(example);
	}

	@Override
	public YFormDataModel findYFormData(final String applicationId, final String formId, final String refId,
			final YFormDataTypeEnum type)
	{
		FlexibleSearchQuery query = null;

		query = new FlexibleSearchQuery(QUERY_FORMDATA_BY_APPLICATIONID_FORMID_REFID);
		query.addQueryParameter("type", type);
		query.addQueryParameter("refId", refId);
		query.addQueryParameter("applicationId", applicationId);
		query.addQueryParameter("formId", formId);

		return getFlexibleSearchService().searchUnique(query);
	}
}
