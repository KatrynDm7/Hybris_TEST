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
package de.hybris.platform.solrfacetsearch.search.impl;

import static org.mockito.BDDMockito.given;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class DefaultSearchQueryLanguageResolverTest
{

	private DefaultSearchQueryLanguageResolver defaultSearchQueryLanguageResolver;

	private FacetSearchConfig facetSearchConfig;
	private IndexedType indexedType;

	@Mock
	private CommonI18NService commonI18NService;

	@Mock
	private UserService userService;

	@Mock
	private UserModel userModel;

	@Mock
	private LanguageModel languageModel;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		defaultSearchQueryLanguageResolver = new DefaultSearchQueryLanguageResolver();
		facetSearchConfig = new FacetSearchConfig();
		indexedType = new IndexedType();

		given(userService.getCurrentUser()).willReturn(userModel);

		defaultSearchQueryLanguageResolver.setUserService(userService);
		defaultSearchQueryLanguageResolver.setCommonI18NService(commonI18NService);
	}

	@Test
	public void testResolveLanguageFailed()
	{
		// when
		final LanguageModel language = defaultSearchQueryLanguageResolver.resolveLanguage(facetSearchConfig, indexedType);

		// then
		Assert.assertNull(language);
	}

	@Test
	public void testResolveLanguageFromSession()
	{
		// given
		given(userModel.getSessionLanguage()).willReturn(languageModel);

		// when
		final LanguageModel resolvedLanguage = defaultSearchQueryLanguageResolver.resolveLanguage(facetSearchConfig, indexedType);

		// then
		Assert.assertEquals(resolvedLanguage, languageModel);
	}

	@Test
	public void testResolveLanguageFromService()
	{
		// given
		given(commonI18NService.getCurrentLanguage()).willReturn(languageModel);

		// when
		final LanguageModel resolvedLanguage = defaultSearchQueryLanguageResolver.resolveLanguage(facetSearchConfig, indexedType);

		// then
		Assert.assertEquals(resolvedLanguage, languageModel);
	}
}
