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
package de.hybris.platform.cms2.servicelayer.interceptor.impl;

import static org.junit.Assert.fail;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForTemplateModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


public class ContentSlotForTemplateValidateInterceptorTest extends ServicelayerTransactionalTest
{

	@Resource
	private ModelService modelService;
	private CatalogVersionModel catalogVersion;
	private ContentSlotModel contentSlot;
	private PageTemplateModel pageTemplate;

	@Before
	public void setUp()
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId("testCatalog");
		modelService.save(catalog);

		catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setCatalog(catalog);
		catalogVersion.setActive(Boolean.TRUE);
		catalogVersion.setVersion("Demo");
		modelService.save(catalogVersion);
	}

	@Test(expected = InterceptorException.class)
	public void testPagePositionAndContentSlotUnique() throws Throwable
	{
		try
		{
			ContentSlotForTemplateModel csm = modelService.create(ContentSlotForTemplateModel.class);
			csm.setPageTemplate(getPageTemplate());
			csm.setPosition("testPosition");
			csm.setContentSlot(getContentSlot());
			csm.setCatalogVersion(catalogVersion);
			csm.setUid("first");
			modelService.save(csm);

			csm = modelService.create(ContentSlotForTemplateModel.class);
			csm.setUid("second");
			csm.setPageTemplate(getPageTemplate());
			csm.setPosition("testPosition");
			csm.setContentSlot(getContentSlot());
			csm.setCatalogVersion(catalogVersion);
			modelService.save(csm);
			fail(ModelSavingException.class.getCanonicalName() + " should have been thrown!");
		}
		catch (final ModelSavingException mse)
		{
			throw mse.getCause();
		}
	}

	private ContentSlotModel getContentSlot()
	{
		if (contentSlot == null)
		{
			contentSlot = modelService.create(ContentSlotModel.class);
			contentSlot.setUid("testContentSlot");
			contentSlot.setCatalogVersion(catalogVersion);
			modelService.save(contentSlot);
		}
		return contentSlot;
	}

	private PageTemplateModel getPageTemplate()
	{
		if (pageTemplate == null)
		{
			pageTemplate = modelService.create(PageTemplateModel.class);
			pageTemplate.setUid("testPageTemplate");
			pageTemplate.setCatalogVersion(catalogVersion);
			modelService.save(pageTemplate);
		}
		return pageTemplate;
	}

}
