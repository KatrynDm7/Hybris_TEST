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
package de.hybris.platform.commerceservices.i18n.impl;

import de.hybris.platform.commerceservices.i18n.LanguageResolver;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 * Default implementation of LanguageResolver.
 * 
 * @since 4.6
 * @spring.bean languageResolver
 */
public class CommerceLanguageResolver implements LanguageResolver
{
	private static final Logger LOG = Logger.getLogger(CommerceLanguageResolver.class);

	private CommonI18NService commonI18NService;
	private CommerceCommonI18NService commerceCommonI18NService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.yacceleratorstorefront.i18n.LanguageResolver#getLanguage(java.lang.String)
	 */
	@Override
	public LanguageModel getLanguage(final String isoCode)
	{
		// Get the languages defined for the current site
		Collection<LanguageModel> languages = getCommerceCommonI18NService().getAllLanguages();
		if (languages.isEmpty())
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("No supported languages found for the current site, get all session languages instead.");
			}

			// Fallback to all languages
			languages = getCommonI18NService().getAllLanguages();
		}

		Assert.notEmpty(languages, "No supported languages found for the current site.");

		// Look for the language with a matching iso code
		LanguageModel languageModel = null;
		for (final LanguageModel language : languages)
		{
			if (StringUtils.equals(language.getIsocode(), isoCode))
			{
				languageModel = language;
				break;
			}
		}

		Assert.notNull(languageModel, "Language to set is not supported.");

		return languageModel;
	}


	protected CommerceCommonI18NService getCommerceCommonI18NService()
	{
		return commerceCommonI18NService;
	}

	@Required
	public void setCommerceCommonI18NService(final CommerceCommonI18NService commerceCommonI18NService)
	{
		this.commerceCommonI18NService = commerceCommonI18NService;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}
}
