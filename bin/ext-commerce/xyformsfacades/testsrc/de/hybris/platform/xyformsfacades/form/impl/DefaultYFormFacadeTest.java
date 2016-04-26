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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.xyformsfacades.proxy.ProxyFacade;
import de.hybris.platform.xyformsservices.enums.YFormDataActionEnum;
import de.hybris.platform.xyformsservices.enums.YFormDataTypeEnum;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;
import de.hybris.platform.xyformsservices.form.YFormService;
import de.hybris.platform.xyformsservices.model.YFormDataModel;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test class for DefaultYFormFacade
 */
@UnitTest
public class DefaultYFormFacadeTest extends ServicelayerTest
{
	@Resource(name = "yFormFacade")
	private DefaultYFormFacade yformFacade;

	@Mock
	private YFormService yformService;

	@Mock
	private ProxyFacade proxyFacade;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void validateFormDataWithDraftTypeShouldReturnFalse() throws YFormServiceException
	{
		final String applicationId = "dummy";
		final String formId = "dummy";
		final String formDataId = "dummy";
		final String content = "class=\"xforms-invalid\"";

		final YFormDataModel yformData = new YFormDataModel();
		yformData.setApplicationId(applicationId);
		yformData.setFormId(formId);
		yformData.setId(formDataId);
		yformData.setType(YFormDataTypeEnum.DRAFT);

		given(yformService.getYFormData(formDataId, YFormDataTypeEnum.DRAFT)).willReturn(yformData);
		given(yformService.getYFormData(formDataId, YFormDataTypeEnum.DATA)).willReturn(yformData);
		yformFacade.setYFormService(yformService);

		given(proxyFacade.getInlineFormHtml(applicationId, formId, YFormDataActionEnum.EDIT, formDataId)).willReturn(content);
		yformFacade.setProxyFacade(proxyFacade);

		final boolean result = yformFacade.validate(applicationId, formId, formDataId);
		assertFalse(result);
	}

	@Test
	public void validateFormDataWithDataTypeAndInvalidContentShouldReturnFalse() throws YFormServiceException
	{
		final String applicationId = "dummy";
		final String formId = "dummy";
		final String formDataId = "dummy";
		final String content = "class=\"xforms-invalid\"";

		final YFormDataModel yformData = new YFormDataModel();
		yformData.setApplicationId(applicationId);
		yformData.setFormId(formId);
		yformData.setId(formDataId);
		yformData.setType(YFormDataTypeEnum.DATA);

		given(yformService.getYFormData(formDataId, YFormDataTypeEnum.DRAFT)).willReturn(yformData);
		given(yformService.getYFormData(formDataId, YFormDataTypeEnum.DATA)).willReturn(yformData);
		yformFacade.setYFormService(yformService);

		given(proxyFacade.getInlineFormHtml(applicationId, formId, YFormDataActionEnum.EDIT, formDataId)).willReturn(content);
		yformFacade.setProxyFacade(proxyFacade);

		final boolean result = yformFacade.validate(applicationId, formId, formDataId);
		assertFalse(result);
	}

	@Test
	public void validateFormDataWithDataTypeAndValidContentShouldReturnTrue() throws YFormServiceException
	{
		final String applicationId = "dummy";
		final String formId = "dummy";
		final String formDataId = "dummy";
		final String content = "class=\"foo bar\"";

		final YFormDataModel yformData = new YFormDataModel();
		yformData.setApplicationId(applicationId);
		yformData.setFormId(formId);
		yformData.setId(formDataId);
		yformData.setType(YFormDataTypeEnum.DATA);

		given(yformService.getYFormData(formDataId, YFormDataTypeEnum.DRAFT)).willReturn(yformData);
		given(yformService.getYFormData(formDataId, YFormDataTypeEnum.DATA)).willReturn(yformData);
		yformFacade.setYFormService(yformService);

		given(proxyFacade.getInlineFormHtml(applicationId, formId, YFormDataActionEnum.EDIT, formDataId)).willReturn(content);
		yformFacade.setProxyFacade(proxyFacade);

		final boolean result = yformFacade.validate(applicationId, formId, formDataId);
		assertTrue(result);
	}
}
