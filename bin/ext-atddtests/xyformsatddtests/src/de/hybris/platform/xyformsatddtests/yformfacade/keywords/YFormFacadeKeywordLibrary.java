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
package de.hybris.platform.xyformsatddtests.yformfacade.keywords;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.platform.atddengine.keywords.AbstractKeywordLibrary;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.xyformsfacades.data.YFormDataData;
import de.hybris.platform.xyformsfacades.data.YFormDefinitionData;
import de.hybris.platform.xyformsfacades.form.YFormFacade;
import de.hybris.platform.xyformsfacades.strategy.GetYFormDefinitionsForProductStrategy;
import de.hybris.platform.xyformsfacades.utils.FormDefinitionUtils;
import de.hybris.platform.xyformsservices.enums.YFormDataTypeEnum;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;
import de.hybris.platform.xyformsservices.form.YFormService;
import de.hybris.platform.xyformsservices.model.YFormDataHistoryModel;
import de.hybris.platform.xyformsservices.model.YFormDataModel;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Robotframework library class for YFormFacade
 */
public class YFormFacadeKeywordLibrary extends AbstractKeywordLibrary
{
	private static final Logger LOG = Logger.getLogger(YFormFacadeKeywordLibrary.class);
	private static MessageFormat YFORM_DEFINITION_TEMLATE = new MessageFormat(
			"<xh:html xmlns:xh=\"http://www.w3.org/1999/xhtml\" xmlns:xf=\"http://www.w3.org/2002/xforms\" xmlns:xxf=\"http://orbeon.org/oxf/xml/xforms\"><xh:head><xh:title>Untitled Form</xh:title><xf:model id=\"fr-form-model\" xxf:expose-xpath-types=\"true\"><xf:instance id=\"fr-form-instance\" xxf:exclude-result-prefixes=\"#all\"><form><section-1><control-1>1</control-1></section-1></form></xf:instance><xf:instance xxf:readonly=\"true\" id=\"fr-form-metadata\" xxf:exclude-result-prefixes=\"#all\"><metadata><application-name>{0}</application-name><form-name>{1}</form-name><title xml:lang=\"en\">Untitled Form</title><description xml:lang=\"en\">Description</description></metadata></xf:instance></xf:model></xh:head><xh:body>{2}</xh:body></xh:html>");

	@Autowired
	private YFormFacade yFormFacade;

	@Autowired
	private YFormService yformService;

	@Autowired
	private GetYFormDefinitionsForProductStrategy getYFormDefinitionsForProductStrategy;

	public String getYFormDefinitionFromTemplate(final String applicationId, final String formId, final String content)
	{
		final String definition = YFORM_DEFINITION_TEMLATE.format(new Object[]
		{ applicationId, formId, content });
		return definition;
	}

	/**
	 * Verify for a given applicationId and formId a form definition is returned.
	 * 
	 * @param applicationId
	 * @param formId
	 */
	public void verifyYFormDefinitionExists(final String applicationId, final String formId)
	{
		YFormDefinitionData yFormDefinition = null;
		try
		{
			yFormDefinition = yFormFacade.getYFormDefinition(applicationId, formId);
			assertNotNull("yFormDefinition is not null", yFormDefinition);
			assertEquals(yFormDefinition.getApplicationId(), applicationId);
			assertEquals(yFormDefinition.getFormId(), formId);
		}
		catch (final YFormServiceException e)
		{
			LOG.error(e.getMessage());
		}
	}

	/**
	 * Verify for a given applicationId and formId a form definition is returned.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param version
	 */
	public void verifyYFormDefinitionWithVersionExists(final String applicationId, final String formId, final String version)
	{
		YFormDefinitionData yFormDefinition = null;
		try
		{
			yFormDefinition = yFormFacade.getYFormDefinition(applicationId, formId, new Integer(version).intValue());
			assertNotNull("yFormDefinition is not null", yFormDefinition);
			assertEquals(yFormDefinition.getApplicationId(), applicationId);
			assertEquals(yFormDefinition.getFormId(), formId);
			assertEquals(yFormDefinition.getVersion(), new Integer(version).intValue());
		}
		catch (final YFormServiceException e)
		{
			LOG.error(e.getMessage());
		}
	}

	/**
	 * For a not previously existed yform definition with given applicationId and formId will throw exception.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param exception
	 */
	public void verifyYFormDefinitionNotExistedThrowsException(final String applicationId, final String formId,
			final String exception)
	{
		try
		{
			yFormFacade.getYFormDefinition(applicationId, formId);
			fail();
		}
		catch (final YFormServiceException e)
		{
			LOG.info("ExceptionName: " + e.getClass().getSimpleName());
			assertEquals(exception, e.getClass().getSimpleName());
		}
	}

	/**
	 * For a not previously existed yform definition with given applicationId and formId will throw exception.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param exception
	 */
	public void verifyYFormDefinitionWithVersionNotExistedThrowsException(final String applicationId, final String formId,
			final String version, final String exception)
	{
		try
		{
			yFormFacade.getYFormDefinition(applicationId, formId, new Integer(version).intValue());
			fail();
		}
		catch (final YFormServiceException e)
		{
			LOG.info("ExceptionName: " + e.getClass().getSimpleName());
			assertEquals(exception, e.getClass().getSimpleName());
		}
	}

	/**
	 * Create a new YFormDefinition by the given parameters. <br/>
	 * The applicationId should be a existed Catalog, or an exception will be thrown with catalog not available message.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param body
	 * @return YFormDefinitionData
	 */
	public YFormDefinitionData createYFormDefinition(final String applicationId, final String formId, final String body)
	{
		YFormDefinitionData yFormDefinition = null;
		try
		{
			yFormDefinition = yFormFacade.createYFormDefinition(applicationId, formId, body, null);
		}
		catch (final YFormServiceException e)
		{
			LOG.error(e.getMessage());
			fail(e.getMessage());
		}
		return yFormDefinition;
	}

	/**
	 * Update YFormDefinition if it is existed in the database. Catch AmbiguousIdentifierException and throws
	 * YFormServiceException with corresponding error messages on retrieval from the DAO.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param body
	 * @return YFormDefinitionData
	 */
	public YFormDefinitionData updateYFormDefinition(final String applicationId, final String formId, final String body)
	{
		YFormDefinitionData yFormDefinition = null;
		try
		{
			yFormDefinition = yFormFacade.updateYFormDefinition(applicationId, formId, body, null);
		}
		catch (final ModelNotFoundException e)
		{
			LOG.error(e.getMessage());
			fail(e.getMessage());
		}
		catch (final YFormServiceException e)
		{
			LOG.error(e.getMessage());
		}
		return yFormDefinition;
	}

	/**
	 * For a not previously existed yform definition with given applicationId and formId will throw exception.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param body
	 * @param exception
	 */
	public void verifyYFormDefinitionNotExistedOnUpdateThrowsException(final String applicationId, final String formId,
			final String body, final String exception)
	{
		try
		{
			yFormFacade.updateYFormDefinition(applicationId, formId, body, null);
			fail();
		}
		catch (final ModelNotFoundException e)
		{
			LOG.info("ExceptionName: " + e.getClass().getSimpleName());
			assertEquals(exception, e.getClass().getSimpleName());
		}
		catch (final YFormServiceException e)
		{
			LOG.error(e.getMessage());
		}
	}

	/**
	 * Verify the YForm Definition has been updated.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param updatedBody
	 */
	public void verifyYFormDefinitionUpdated(final String applicationId, final String formId, final String updatedBody)
	{
		YFormDefinitionData yFormDefinition = null;
		try
		{
			yFormDefinition = yFormFacade.getYFormDefinition(applicationId, formId);
			final String c1 = FormDefinitionUtils.normalize(yFormDefinition.getContent());
			final String c2 = FormDefinitionUtils.normalize(updatedBody);
			assertNotNull("yFormDefinition is not null", yFormDefinition);
			assertEquals(c1, c2);
		}
		catch (final YFormServiceException e)
		{
			LOG.error(e.getMessage());
		}
	}

	/**
	 * Verify the YForm Definition has been updated.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param updatedBody
	 */
	public void verifyYFormDefinitionWithVersionUpdated(final String applicationId, final String formId, final String version,
			final String updatedBody)
	{
		YFormDefinitionData yFormDefinition = null;
		try
		{
			final int v = new Integer(version).intValue();
			yFormDefinition = yFormFacade.getYFormDefinition(applicationId, formId, v);
			final String c1 = FormDefinitionUtils.normalize(yFormDefinition.getContent());
			final String c2 = FormDefinitionUtils.normalize(updatedBody);
			assertNotNull("yFormDefinition is not null", yFormDefinition);
			assertEquals(c1, c2);
			assertEquals(yFormDefinition.getVersion(), v);
		}
		catch (final YFormServiceException e)
		{
			LOG.error(e.getMessage());
		}
	}

	/**
	 * For a given id a form data is returned.
	 * 
	 * @param formDataId
	 * @return YFormDataData
	 */
	public YFormDataData getYFormData(final String formDataId)
	{
		YFormDataData yFormData = null;
		try
		{
			yFormData = yFormFacade.getYFormData(formDataId, YFormDataTypeEnum.DATA);
		}
		catch (final YFormServiceException e)
		{
			LOG.error(e.getMessage());
			fail(e.getMessage());
		}
		return yFormData;
	}

	/**
	 * Verify YForm Data existed
	 * 
	 * @param formDataId
	 */
	public void verifyYFormDataExists(final String formDataId)
	{
		YFormDataData yFormData = null;
		try
		{
			yFormData = yFormFacade.getYFormData(formDataId, YFormDataTypeEnum.DATA);
			assertNotNull("YFormDataData is not null", yFormData);
		}
		catch (final YFormServiceException e)
		{
			LOG.error(e.getMessage());
		}
	}

	/**
	 * Verify YForm Data existed
	 * 
	 * @param formDataId
	 * @param formDataType
	 */
	public void verifyYFormDataWithTypeExists(final String formDataId, final String formDataType)
	{
		YFormDataData yFormData = null;
		final YFormDataTypeEnum type = YFormDataTypeEnum.valueOf(formDataType.toUpperCase());
		try
		{
			yFormData = yFormFacade.getYFormData(formDataId, type);
			assertNotNull("YFormDataData is not null", yFormData);
		}
		catch (final YFormServiceException e)
		{
			LOG.error(e.getMessage());
		}
	}

	/**
	 * Verify YForm Data existed with given attribute values
	 * 
	 * @param formDataId
	 */
	public void verifyYFormDataExists(final String formDataId, final String refId, final String content)
	{
		YFormDataData yFormData = null;
		try
		{
			yFormData = yFormFacade.getYFormData(formDataId, YFormDataTypeEnum.DATA);
			assertNotNull("YFormDataData is not null", yFormData);
			assertEquals(refId, yFormData.getRefId());
			assertEquals(content, yFormData.getContent());
		}
		catch (final YFormServiceException e)
		{
			LOG.error(e.getMessage());
		}
	}

	/**
	 * Verify YForm Data List is returned by refId
	 * 
	 * @param refId
	 * @param expectedFormDataIds
	 */
	public void verifyYFormDataRetrievedByRefId(final String refId, final String... expectedFormDataIds)
	{
		final List<YFormDataModel> yFormDataModels = yformService.getYFormDataByRefId(refId);
		assertNotNull("YFormDataData is not null", yFormDataModels);
		assertEquals(expectedFormDataIds.length, yFormDataModels.size());

		final List<String> actualFormDataIds = new ArrayList<String>();

		for (final YFormDataModel yFormDataModel : yFormDataModels)
		{
			actualFormDataIds.add(yFormDataModel.getId());
		}

		final Collection disjunction = CollectionUtils.disjunction(actualFormDataIds, Arrays.asList(expectedFormDataIds));
		assertTrue("Different set of YFormData has been returned by refId [" + refId + "] than expected", disjunction.isEmpty());
	}

	/**
	 * For a not previously existed yform definition with given applicationId and formId will throw exception.
	 * 
	 * @param formDataId
	 * @param exception
	 */
	public void verifyYFormDataNotExistedThrowsException(final String formDataId, final String exception)
	{
		try
		{
			yFormFacade.getYFormData(formDataId, YFormDataTypeEnum.DATA);
			fail();
		}
		catch (final YFormServiceException e)
		{
			LOG.info("ExceptionName: " + e.getClass().getSimpleName());
			assertEquals(exception, e.getClass().getSimpleName());
		}
	}

	/**
	 * For a not previously existed yform definition with given applicationId and formId will throw exception.
	 * 
	 * @param formDataId
	 * @param formDataType
	 * @param exception
	 */
	public void verifyYFormDataWithTypeNotExistedThrowsException(final String formDataId, final String formDataType,
			final String exception)
	{
		try
		{
			yFormFacade.getYFormData(formDataId, YFormDataTypeEnum.valueOf(formDataType.toUpperCase()));
			fail();
		}
		catch (final YFormServiceException e)
		{
			LOG.info("ExceptionName: " + e.getClass().getSimpleName());
			assertEquals(exception, e.getClass().getSimpleName());
		}
	}

	/**
	 * Create YFormData is not previous existed. And assign to the corresponding YFormDefinition.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param formDataId
	 * @param body
	 * @return YFormDataData
	 */
	public YFormDataData createYFormData(final String applicationId, final String formId, final String formDataId,
			final String refId, final String body)
	{
		YFormDataData yFormData = null;
		try
		{
			yFormData = yFormFacade.createYFormData(applicationId, formId, formDataId, YFormDataTypeEnum.DATA, refId, body);
		}
		catch (final YFormServiceException e)
		{
			LOG.error(e.getMessage());
			fail(e.getMessage());
		}
		return yFormData;
	}

	/**
	 * Update YFormData by the given FormDataId
	 * 
	 * @param formDataId
	 * @param formDataBody
	 * @return YFormDataData
	 */
	public YFormDataData updateYFormDataById(final String formDataId, final String formDataBody)
	{
		YFormDataData yFormData = null;
		try
		{
			yFormData = yFormFacade.updateYFormData(formDataId, YFormDataTypeEnum.DATA, formDataBody);
		}
		catch (final YFormServiceException e)
		{
			LOG.error(e.getMessage());
			fail(e.getMessage());
		}
		return yFormData;
	}


	/**
	 * For a not previously existed yform data with given formDataId will throw exception.
	 * 
	 * @param formDataId
	 * @param formDataBody
	 * @param exception
	 */
	public void verifyYFormDataNotExistedOnUpdateThrowsException(final String formDataId, final String formDataBody,
			final String exception)
	{
		try
		{
			yFormFacade.updateYFormData(formDataId, YFormDataTypeEnum.DATA, formDataBody);
			fail();
		}
		catch (final ModelNotFoundException e)
		{
			LOG.info("ExceptionName: " + e.getClass().getSimpleName());
			assertEquals(exception, e.getClass().getSimpleName());
		}
		catch (final YFormServiceException e)
		{
			LOG.info("ExceptionName: " + e.getClass().getSimpleName());
		}
	}

	/**
	 * Verify the YForm Data has been updated.
	 * 
	 * @param formDataId
	 * @param updatedBody
	 */
	public void verifyYFormDataUpdated(final String formDataId, final String updatedBody)
	{
		YFormDataData yFormDataData = null;
		try
		{
			yFormDataData = yFormFacade.getYFormData(formDataId, YFormDataTypeEnum.DATA);
			assertNotNull("yFormDataData is not null", yFormDataData);
			assertEquals(yFormDataData.getContent(), updatedBody);
		}
		catch (final YFormServiceException e)
		{
			LOG.error(e.getMessage());
		}
	}

	/**
	 * Verify the YForm Data History has been created for YFrom Data change.
	 * 
	 * @param formDataId
	 */
	public void verifyYFormDataHistoryCreated(final String formDataId)
	{

		try
		{
			final YFormDataModel yFormDataModel = yformService.getYFormData(formDataId, YFormDataTypeEnum.DATA);

			final List<YFormDataHistoryModel> history = yFormDataModel.getHistory();

			assertNotNull("yFormDataHistory is not null", history);
			assertFalse("yFormDataHistory is not empty", history.isEmpty());

			final YFormDataHistoryModel latestYFormDataHistoryModel = history.get(history.size() - 1);

			assertEquals(yFormDataModel.getId(), latestYFormDataHistoryModel.getFormDataId());
			assertEquals(yFormDataModel.getContent(), latestYFormDataHistoryModel.getContent());
		}
		catch (final YFormServiceException e)
		{
			LOG.error(e.getMessage());
		}
	}

	/**
	 * Create or Update YFormDataModel.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param formDataId
	 * @param formDataBody
	 * @return YFormDataData
	 */
	public YFormDataData createOrUpdateYFormData(final String applicationId, final String formId, final String formDataId,
			final String refId, final String formDataBody)
	{
		YFormDataData yFormData = null;
		try
		{
			yFormData = yFormFacade.createOrUpdateYFormData(applicationId, formId, formDataId, YFormDataTypeEnum.DATA, refId,
					formDataBody);
		}
		catch (final YFormServiceException e)
		{
			LOG.error(e.getMessage());
			fail(e.getMessage());
		}
		return yFormData;
	}

	/**
	 * Create or Update YFormDataModel.
	 * 
	 * @param applicationId
	 * @param formId
	 * @param formDataId
	 * @param formDataType
	 * @param formDataBody
	 * @return YFormDataData
	 */
	public YFormDataData createOrUpdateYFormDataWithType(final String applicationId, final String formId, final String formDataId,
			final String formDataType, final String formDataBody)
	{
		YFormDataData yFormData = null;
		final YFormDataTypeEnum type = YFormDataTypeEnum.valueOf(formDataType.toUpperCase());
		try
		{
			yFormData = yFormFacade.createOrUpdateYFormData(applicationId, formId, formDataId, type, formDataBody);
		}
		catch (final YFormServiceException e)
		{
			LOG.error(e.getMessage());
			fail(e.getMessage());
		}
		return yFormData;
	}

	/**
	 * Verify that yFormFacade return all the YFormDefitionData that relate to the product specified
	 * 
	 * @param productCode
	 * @param formDefinitionIds
	 *           in format of "applicationId:formId"
	 * */
	public void verifyReturnsAllYFormDefinitionDataForProduct(final String productCode, final String... formDefinitionIds)
	{

		List<YFormDefinitionData> yFormDefinitionDataList;
		try
		{
			yFormDefinitionDataList = getYFormDefinitionsForProductStrategy.execute(productCode);


			if (yFormDefinitionDataList == null && formDefinitionIds.length > 0
					|| yFormDefinitionDataList.size() != formDefinitionIds.length)
			{
				Assert.fail("Product [" + productCode + "] has different number of YFomDefinitions than expected");
			}
			final Set<String> formDefinitionIdsSet = new HashSet<>(Arrays.asList(formDefinitionIds));
			for (int i = 0; i < formDefinitionIds.length; i++)
			{
				final YFormDefinitionData yFormDefinitionData = yFormDefinitionDataList.get(i);
				Assert.assertTrue(formDefinitionIdsSet.contains(yFormDefinitionData.getApplicationId() + ":"
						+ yFormDefinitionData.getFormId()));
			}
		}
		catch (final YFormServiceException e)
		{
			LOG.error(e.getMessage());
			fail(e.getMessage());
		}

	}

	/**
	 * Verify that YFormServiceException is thrown when tries to return all the YFormDefitionData that relate to the
	 * product, but product does not
	 * 
	 * @param productCode
	 * */
	public void verifyThrowsExceptionWhenProductDoesNotExistWhenGetYFormDefinitionDataForProduct(final String productCode)
	{

		try
		{
			getYFormDefinitionsForProductStrategy.execute(productCode);
			fail();
		}
		catch (final YFormServiceException e)
		{
			LOG.info("ExceptionName: " + e.getClass().getSimpleName());
		}

	}


	/**
	 * Verify for a given form data id: applicationId, formId, version number and content are verified
	 * 
	 * @param applicationId
	 * @param formId
	 */
	public void verifyYFormDefinitionWithFormDataId(final String formDataId, final String applicationId, final String formId,
			final String version, final String content)
	{
		YFormDefinitionData yFormDefinition = null;
		try
		{
			yFormDefinition = yFormFacade.getYFormDefinition(formDataId);
			final String c1 = FormDefinitionUtils.normalize(yFormDefinition.getContent());
			final String c2 = FormDefinitionUtils.normalize(content);
			assertNotNull("yFormDefinition is not null", yFormDefinition);
			assertEquals(yFormDefinition.getApplicationId(), applicationId);
			assertEquals(yFormDefinition.getFormId(), formId);
			assertEquals(yFormDefinition.getVersion(), new Integer(version).intValue());
			assertEquals(c1, c2);
		}
		catch (final YFormServiceException e)
		{
			LOG.error(e.getMessage());
		}
	}
}
