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
package de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;
import de.hybris.platform.variants.model.VariantProductModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

/**
 * This ValueProvider will provide the product keywords.
 */
public class ProductKeywordsValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private CommonI18NService commonI18NService;
	private SessionService sessionService;
	private FieldNameProvider fieldNameProvider;

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	protected FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}


	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty, final Object model) throws FieldValueProviderException
	{
		if (model == null)
		{
			throw new IllegalArgumentException("No model given");
		}

		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();

		if (indexedProperty.isLocalized())
		{
			final Collection<LanguageModel> languages = indexConfig.getLanguages();
			for (final LanguageModel language : languages)
			{
				final Object value = getSessionService().executeInLocalView(new SessionExecutionBody()
				{
					@Override
					public Object execute()
					{
						getCommonI18NService().setCurrentLanguage(language);
						return getPropertyValue(model, indexedProperty);
					}
				});

				if (value != null)
				{
					final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, language.getIsocode());
					for (final String fieldName : fieldNames)
					{
						fieldValues.add(new FieldValue(fieldName, value));
					}
				}
			}
		}

		return fieldValues;
	}

	protected Object getPropertyValue(final Object model, final IndexedProperty indexedProperty)
	{
		if (model instanceof ProductModel)
		{
			final Set<String> keywords = new HashSet<String>();
			collectProductKeywords(keywords, (ProductModel) model);

			if (!keywords.isEmpty())
			{
				final StringBuilder buf = new StringBuilder();

				for (final String keyword : keywords)
				{
					buf.append(keyword).append(' ');
				}

				return buf.toString();
			}
		}
		return null;
	}

	protected void collectProductKeywords(final Set<String> words, final ProductModel product)
	{
		final List<KeywordModel> keywords = product.getKeywords();
		if (keywords != null && !keywords.isEmpty())
		{
			for (final KeywordModel keyword : keywords)
			{
				words.add(keyword.getKeyword());
			}
		}

		if (product instanceof VariantProductModel)
		{
			final ProductModel baseProduct = ((VariantProductModel) product).getBaseProduct();
			if (baseProduct != null)
			{
				collectProductKeywords(words, baseProduct);
			}
		}
	}
}
