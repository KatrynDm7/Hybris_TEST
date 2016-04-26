/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.hyend2.si;

import de.hybris.bootstrap.annotations.ManualTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.hyend2.daos.IndexSchemaDao;
import de.hybris.platform.hyend2.model.Hyend2IndexSchemaModel;
import de.hybris.platform.hyend2.services.ExportService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * @author michal.flasinski
 * 
 */
@ManualTest
public class IntegrationTest extends ServicelayerTest
{
	@Resource
	ExportGateway exportGateway;

	@Resource
	CommonI18NService commonI18NService;

	@Resource
	UserService userService;

	@Resource
	private IndexSchemaDao indexSchemaDao;

	@Resource
	CronJobService cronJobService;

	@Resource
	FlexibleSearchService flexibleSearchService;

	@Resource
	ModelService modelService;

	@Resource
	ExportService hyend2exportService;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createHardwareCatalog();
		importCsv("/data/hyend2sampleData.csv", "UTF-8");
	}

	@Test
	public void test() throws Exception
	{
		final List<LanguageModel> languages = new ArrayList();
		languages.add(commonI18NService.getLanguage("en"));
		final Hyend2IndexSchemaModel indexSchema = indexSchemaDao.findIndexSchemaByName("my index schema");
		System.out.println(indexSchema.getLastIndexTime());
		hyend2exportService.export(indexSchema, languages, "Baseline", Collections.EMPTY_LIST);

		final ProductModel product = flexibleSearchService
				.<ProductModel> search("SELECT {PK} FROM {Product} WHERE {code}='HW2310-1004'").getResult().get(0);
		product.setName("ABCDE", Locale.ENGLISH);
		modelService.save(product);

		final ProductModel product2 = flexibleSearchService
				.<ProductModel> search("SELECT {PK} FROM {Product} WHERE {code}='HW2300-2356'").getResult().get(0);
		product2.setName("ABCD", Locale.ENGLISH);
		modelService.save(product2);

		hyend2exportService.export(indexSchema, languages, "Partial", Collections.EMPTY_LIST);
	}
}
