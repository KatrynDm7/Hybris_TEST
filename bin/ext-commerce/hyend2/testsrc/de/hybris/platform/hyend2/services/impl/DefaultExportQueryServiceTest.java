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
package de.hybris.platform.hyend2.services.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.TranslationResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;


/**
 * @author michal.flasinski
 * 
 */
public class DefaultExportQueryServiceTest
{
	private DefaultExportQueryService exportQueryService;

	@Mock
	private FlexibleSearchService flexibleSearchService;

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		exportQueryService = new DefaultExportQueryService()
		{
			@Override
			protected DataSource getDataSource()
			{
				return Mockito.mock(DataSource.class);

			}
		};
		exportQueryService.setFlexibleSearchService(flexibleSearchService);
		exportQueryService.setJdbcTemplate(jdbcTemplate);
	}

	@Test
	public void testQueryForPksToExport()
	{
		final List<Object> params = Collections.EMPTY_LIST;
		final String queryString = "SELECT {PK} FROM {Product}";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		final UserModel mockUser = Mockito.mock(UserModel.class);
		final CatalogVersionModel mockCv = Mockito.mock(CatalogVersionModel.class);
		final CatalogModel mockCatalog = Mockito.mock(CatalogModel.class);
		given(mockCv.getCatalog()).willReturn(mockCatalog);
		query.setUser(mockUser);
		query.setCatalogVersions(mockCv);
		final TranslationResult tanslationResult = new TranslationResult(queryString, params);
		given(flexibleSearchService.translate(query)).willReturn(tanslationResult);
		given(jdbcTemplate.queryForList(queryString, params.toArray(), String.class)).willReturn(Arrays.asList("1", "2"));

		final List<String> pks = exportQueryService.queryForPksToExport(query);

		assertThat(pks).containsExactly("1", "2");
	}
}
