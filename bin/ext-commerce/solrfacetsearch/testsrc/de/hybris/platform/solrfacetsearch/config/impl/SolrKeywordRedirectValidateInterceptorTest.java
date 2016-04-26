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
package de.hybris.platform.solrfacetsearch.config.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.daos.LanguageDao;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultModelService;
import de.hybris.platform.solrfacetsearch.enums.KeywordRedirectMatchType;
import de.hybris.platform.solrfacetsearch.integration.AbstractIntegrationTest;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrFacetSearchKeywordRedirectModel;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrURIRedirectModel;

import javax.annotation.Resource;

import org.fest.assertions.Fail;
import org.junit.Test;


/**
 *
 */
public class SolrKeywordRedirectValidateInterceptorTest extends AbstractIntegrationTest
{
	@Resource
	private DefaultModelService modelService;

	@Resource
	private LanguageDao languageDao;

	private LanguageModel language;
	private LanguageModel language2;

	private static final String KEYWORD_STRING_1 = "keyword1";

	@Override
	protected void loadInitialData() throws Exception
	{
		importConfig("/test/integration/DefaultSolrFacetSearchKeywordDaoTest.csv");

		language = languageDao.findLanguagesByCode("cz").get(0);
		language2 = languageDao.findLanguagesByCode("pl").get(0);

		initializeIndexedType();
	}

	private SolrFacetSearchKeywordRedirectModel createKeyword(final String keyword, final LanguageModel language,
			final KeywordRedirectMatchType matchType)
	{
		final SolrFacetSearchKeywordRedirectModel result = modelService.create(SolrFacetSearchKeywordRedirectModel.class);
		result.setKeyword(keyword.trim());
		result.setFacetSearchConfig(getSolrFacetSearchConfigModel());
		result.setMatchType(matchType);
		result.setLanguage(language);

		final SolrURIRedirectModel redir = new SolrURIRedirectModel();
		redir.setUrl("www.hybris.com");
		result.setRedirect(redir);

		modelService.save(result);
		return result;
	}

	@Test
	public void createInvalidKeywordTest()
	{
		createKeyword(KEYWORD_STRING_1, language, KeywordRedirectMatchType.CONTAINS);
		try
		{
			createKeyword(KEYWORD_STRING_1, language, KeywordRedirectMatchType.CONTAINS);
			Fail.fail();
		}
		catch (final ModelSavingException e)
		{
			//ok

		}
		createKeyword(KEYWORD_STRING_1, language2, KeywordRedirectMatchType.CONTAINS);
	}
}
