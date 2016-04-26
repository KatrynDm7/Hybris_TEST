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
package de.hybris.platform.xyformsservices.form.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.xyformsservices.daos.YFormDao;
import de.hybris.platform.xyformsservices.enums.YFormDataTypeEnum;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;
import de.hybris.platform.xyformsservices.model.YFormDataModel;
import de.hybris.platform.xyformsservices.model.YFormDefinitionModel;

import java.text.MessageFormat;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;


/**
 * Test class for DefaultYFormService
 */
@IntegrationTest
public class DefaultYFormServiceTest extends ServicelayerTest
{
	@Resource
	private DefaultYFormService yformService;

	@Resource
	private YFormDao yformDao;

	@Resource
	private ModelService modelService;

	private YFormDefinitionModel yFormDefinitionModel;
	private YFormDataModel yFormDataModel;
	private final String applicationId = "nnTestCatalog";
	private final String formId = "globalFormId";
	private final String formDataId = "globalFormDataId";

	private static MessageFormat YFORM_DEFINITION_TEMLATE = new MessageFormat(
			"<xh:html xmlns:xh=\"http://www.w3.org/1999/xhtml\" xmlns:xf=\"http://www.w3.org/2002/xforms\" xmlns:xxf=\"http://orbeon.org/oxf/xml/xforms\"><xh:head><xh:title>Untitled Form</xh:title><xf:model id=\"fr-form-model\" xxf:expose-xpath-types=\"true\"><xf:instance id=\"fr-form-instance\" xxf:exclude-result-prefixes=\"#all\"><form><section-1><control-1>1</control-1></section-1></form></xf:instance><xf:instance xxf:readonly=\"true\" id=\"fr-form-metadata\" xxf:exclude-result-prefixes=\"#all\"><metadata><application-name>{0}</application-name><form-name>{1}</form-name><title xml:lang=\"en\">Untitled Form</title><description xml:lang=\"en\">Description</description></metadata></xf:instance></xf:model></xh:head><xh:body>{2}</xh:body></xh:html>");

	public String getYFormDefinitionFromTemplate(final String applicationId, final String formId, final String content)
	{
		return YFORM_DEFINITION_TEMLATE.format(new Object[]
		{ applicationId, formId, content });
	}

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);

		final String content = getYFormDefinitionFromTemplate(applicationId, formId, "");
		yFormDefinitionModel = modelService.create(YFormDefinitionModel.class);
		yFormDefinitionModel.setApplicationId(applicationId);
		yFormDefinitionModel.setFormId(formId);
		yFormDefinitionModel.setContent(content);
		yFormDefinitionModel.setVersion(1);
		modelService.save(yFormDefinitionModel);

		yFormDataModel = modelService.create(YFormDataModel.class);
		yFormDataModel.setContent("");
		yFormDataModel.setType(YFormDataTypeEnum.DATA);
		yFormDataModel.setId(formDataId);
		yFormDataModel.setFormDefinition(yFormDefinitionModel);
		yFormDataModel.setApplicationId(applicationId);
		yFormDataModel.setFormId(formId);
		modelService.save(yFormDataModel);
	}

	@After
	public void tearDown()
	{
		modelService.remove(yFormDataModel);
		modelService.remove(yFormDefinitionModel);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionWhenApplicationIdNull() throws YFormServiceException
	{
		yformService.getYFormDefinition(null, "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionWhenFormIdNull() throws YFormServiceException
	{
		yformService.getYFormDefinition("", null);
	}

	@Test(expected = YFormServiceException.class)
	public void shouldThrowYFormServiceExceptionWhenGotModelNotFoundException() throws YFormServiceException
	{
		final String emptyApplicationId = "";
		final String emptyFormId = "";
		yformService.getYFormDefinition(emptyApplicationId, emptyFormId);
	}

	@Test(expected = YFormServiceException.class)
	public void shouldThrowYFormServiceExceptionWhenGotAmbiguousIdentifierException_YFormDefinitionModel()
			throws YFormServiceException
	{
		final String emptyApplicationId = "";
		final String emptyFormId = "";
		yformService.getYFormDefinition(emptyApplicationId, emptyFormId);
	}

	@Test
	public void shouldReturnAYFormDefinitionModelWithModelExistsInDatabase_YFormDefinitionModel() throws YFormServiceException
	{
		final YFormDefinitionModel result = yformService.getYFormDefinition(applicationId, formId);
		assertEquals(yFormDefinitionModel, result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionWhenApplicationIdNull_YFormDefinition() throws YFormServiceException
	{
		yformService.getYFormDefinition(null, "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionWhenFormIdNull_YFormDefinition() throws YFormServiceException
	{
		yformService.getYFormDefinition("", null);
	}

	@Test
	public void shouldUpdateYFormDefinitionContentIfExisted() throws YFormServiceException
	{
		final String newContent = getYFormDefinitionFromTemplate(applicationId, formId, "updated");
		yformService.updateYFormDefinition(applicationId, formId, newContent, null);
		assertEquals(newContent, yFormDefinitionModel.getContent());
	}

	@Test
	public void shouldCreateYFormDefinitionWithDefinedApplicationIdIfNotExisted() throws YFormServiceException
	{
		final String newFormId = "test03";
		final String newContent = getYFormDefinitionFromTemplate(applicationId, newFormId, "updated");
		yformService.createYFormDefinition(applicationId, newFormId, null, null, newContent, null);
		final YFormDefinitionModel newYFormDefinitionModel = yformDao.findYFormDefinition(applicationId, newFormId);
		assertEquals(newContent, newYFormDefinitionModel.getContent());
	}

	@Test(expected = ModelNotFoundException.class)
	public void shouldThrowModelNotFoundExceptionIfYFormDefinitionNotExisted() throws YFormServiceException
	{
		final String newFormId = "test04";
		final String newContent = getYFormDefinitionFromTemplate(applicationId, newFormId, "updated");
		yformService.updateYFormDefinition(applicationId, newFormId, newContent, null);
	}

	@Test
	public void shouldUpdateYFormDefinitionIfExisted() throws YFormServiceException
	{
		final String newContent = getYFormDefinitionFromTemplate(applicationId, formId, "updated");
		yformService.updateYFormDefinition(applicationId, formId, newContent, null);
		final YFormDefinitionModel yFormDefinitionModel = yformDao.findYFormDefinition(applicationId, formId);
		assertEquals(newContent, yFormDefinitionModel.getContent());
	}

	@Test(expected = YFormServiceException.class)
	public void shouldThrowExceptionOnEmptyYFormDataId() throws YFormServiceException
	{
		yformService.getYFormData("", YFormDataTypeEnum.DATA);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionOnNullYFormDataId() throws YFormServiceException
	{
		yformService.getYFormData(null, YFormDataTypeEnum.DATA);
	}

	@Test
	public void shouldReturnOnExistYFormDataId() throws YFormServiceException
	{
		final YFormDataModel response = yformService.getYFormData(formDataId, YFormDataTypeEnum.DATA);
		assertEquals(response, yFormDataModel);
	}

	@Test
	public void shouldUpdateYFormDataById() throws YFormServiceException
	{
		final String newContent = "updated";
		yformService.updateYFormData(formDataId, YFormDataTypeEnum.DATA, newContent);
		final YFormDataModel yFormData = yformDao.findYFormData(formDataId, YFormDataTypeEnum.DATA);
		assertEquals(yFormData.getContent(), newContent);
	}

	@Test
	public void shouldCreateYFormData() throws YFormServiceException
	{
		final String newContent = "updated";
		final String newFormDataId = "newFormDataId";
		final String newRefId = "newRefId";
		yformService.createYFormData(applicationId, formId, newFormDataId, YFormDataTypeEnum.DATA, newRefId, newContent);
		final YFormDataModel yFormData = yformDao.findYFormData(newFormDataId, YFormDataTypeEnum.DATA);
		assertEquals(yFormData.getContent(), newContent);
	}
}
