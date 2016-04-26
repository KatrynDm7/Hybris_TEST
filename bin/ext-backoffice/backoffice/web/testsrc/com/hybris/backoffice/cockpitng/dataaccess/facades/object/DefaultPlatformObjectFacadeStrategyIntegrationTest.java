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
 */

package com.hybris.backoffice.cockpitng.dataaccess.facades.object;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import com.hybris.backoffice.cockpitng.dataaccess.facades.common.PlatformFacadeStrategyHandleCache;
import com.hybris.backoffice.cockpitng.dataaccess.facades.object.savedvalues.ItemModificationHistoryService;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;
import com.hybris.cockpitng.labels.LabelService;

import java.util.Locale;

import javax.annotation.Resource;

import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;


@IntegrationTest
public class DefaultPlatformObjectFacadeStrategyIntegrationTest extends ServicelayerTransactionalTest
{

	@InjectMocks
	protected DefaultPlatformObjectFacadeStrategy platformObjectFacadeStrategy = new DefaultPlatformObjectFacadeStrategy();

	@Resource
	private ModelService modelService;
	@Resource
	private TypeService typeService;
	@Resource
	private CatalogService catalogService;

	@Mock
	private ItemModificationHistoryService itemModificationHistoryService;
	@Mock
	private LabelService labelService;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		createCoreData();
		createDefaultCatalog();
		platformObjectFacadeStrategy.setModelService(modelService);
		platformObjectFacadeStrategy.setTypeService(typeService);
		final PlatformFacadeStrategyHandleCache cache = new PlatformFacadeStrategyHandleCache();
		cache.setTypeService(typeService);
		platformObjectFacadeStrategy.setPlatformFacadeStrategyHandleCache(cache);
	}

	@Test
	public void testAttributesPersistence() throws ObjectSavingException
	{
		final ProductModel product = modelService.create(ProductModel._TYPECODE);
		final CatalogVersionModel activeCatalogVersion = catalogService.getDefaultCatalog().getActiveCatalogVersion();
		product.setCatalogVersion(activeCatalogVersion);
		product.setCode(Long.toString(System.currentTimeMillis(), 24));
		modelService.saveAll();

		final CatalogVersionModel newCatalogVersion = modelService.create(CatalogVersionModel._TYPECODE);
		newCatalogVersion.setCatalog(activeCatalogVersion.getCatalog());
		newCatalogVersion.setVersion("TestPersisted");

		product.setCatalogVersion(newCatalogVersion);

		modelService.detachAll();

		platformObjectFacadeStrategy.save(product, null);

		Assertions.assertThat(modelService.isNew(newCatalogVersion)).isFalse();
		Assertions.assertThat(modelService.isUpToDate(newCatalogVersion)).isTrue();

		final KeywordModel keyword = modelService.create(KeywordModel._TYPECODE);
		keyword.setKeyword("testKeyword");
		final LanguageModel language = modelService.create(LanguageModel._TYPECODE);
		language.setIsocode("pl");
		language.setName("Polish");
		keyword.setLanguage(language);

		product.setKeywords(ImmutableList.of(keyword));
		product.setDescription("English description", Locale.ENGLISH);


		product.setArticleStatus(Maps.newHashMap(), Locale.ENGLISH);

		newCatalogVersion.setVersion("TestNotPersisted");

		Assertions.assertThat(modelService.isNew(keyword)).isTrue();
		Assertions.assertThat(modelService.isNew(language)).isTrue();
		platformObjectFacadeStrategy.save(product, null);
		Assertions.assertThat(modelService.isNew(keyword)).isFalse();
		Assertions.assertThat(modelService.isUpToDate(keyword)).isTrue();
		Assertions.assertThat(modelService.isNew(language)).isFalse();
		Assertions.assertThat(modelService.isUpToDate(language)).isTrue();
		Assertions.assertThat(modelService.isNew(newCatalogVersion)).isFalse();
		Assertions.assertThat(modelService.isUpToDate(newCatalogVersion)).isFalse();
	}
}
