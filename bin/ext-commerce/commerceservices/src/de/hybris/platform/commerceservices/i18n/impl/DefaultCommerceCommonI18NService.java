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

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.i18n.impl.DefaultCommonI18NService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Implementation for {@link CommerceCommonI18NService}. Class also extends {@link DefaultCommonI18NService} to provide
 * currency and language -related methods.
 */
public class DefaultCommerceCommonI18NService implements CommerceCommonI18NService
{
	private BaseSiteService baseSiteService;
	private BaseStoreService baseStoreService;
	private I18NService i18nService;
	private CommonI18NService commonI18NService;
	private final Map<String, Locale> locales = new ConcurrentHashMap<>();

	@Override
	public List<CurrencyModel> getAllCurrencies()
	{
		final BaseSiteModel siteModel = getBaseSiteService().getCurrentBaseSite();
		if (siteModel != null)
		{
			final List<BaseStoreModel> stores = siteModel.getStores();
			if (!CollectionUtils.isEmpty(stores))
			{
				final List<CurrencyModel> result = new ArrayList<CurrencyModel>();
				for (final BaseStoreModel baseStoreModel : stores)
				{
					final Set<CurrencyModel> currencies = baseStoreModel.getCurrencies();
					if (!CollectionUtils.isEmpty(currencies))
					{
						for (final CurrencyModel currency : currencies)
						{
							if (!result.contains(currency))
							{
								result.add(currency);
							}
						}
					}
				}
				return result;
			}
		}
		return Collections.emptyList();
	}

	@Override
	public Collection<LanguageModel> getAllLanguages()
	{
		final BaseStoreModel store = getBaseStoreService().getCurrentBaseStore();
		if (store == null || store.getLanguages() == null)
		{
			// Fallback to all system languages
			return commonI18NService.getAllLanguages();
		}
		else
		{
			return store.getLanguages();
		}
	}

	@Override
	public LanguageModel getDefaultLanguage()
	{
		final BaseStoreModel store = getBaseStoreService().getCurrentBaseStore();
		if (store == null || store.getDefaultLanguage() == null)
		{
			// Fallback to the first available language
			final Collection<LanguageModel> allLanguages = getAllLanguages();
			if (!allLanguages.isEmpty())
			{
				return allLanguages.iterator().next();
			}
			// No fallback
			return null;
		}
		else
		{
			return store.getDefaultLanguage();
		}
	}


	@Override
	public CurrencyModel getDefaultCurrency()
	{
		final BaseStoreModel store = getBaseStoreService().getCurrentBaseStore();
		if (store == null || store.getDefaultCurrency() == null)
		{
			// Fallback to first available currency
			final List<CurrencyModel> allCurrencies = getAllCurrencies();
			if (!CollectionUtils.isEmpty(allCurrencies))
			{
				return allCurrencies.get(0);
			}
			return null;
		}
		else
		{
			return store.getDefaultCurrency();
		}
	}

	@Override
	public Collection<CountryModel> getAllCountries()
	{
		final BaseStoreModel store = getBaseStoreService().getCurrentBaseStore();
		if (store == null || store.getDeliveryCountries() == null)
		{
			// Fallback to all countries
			return getCommonI18NService().getAllCountries();
		}
		else
		{
			return store.getDeliveryCountries();
		}
	}

	@Override
	public CurrencyModel getCurrentCurrency()
	{
		return getCommonI18NService().getCurrentCurrency();
	}

	@Override
	public void setCurrentCurrency(final CurrencyModel currency)
	{
		getCommonI18NService().setCurrentCurrency(currency);
	}

	@Override
	public Locale getCurrentLocale()
	{
		return getI18nService().getCurrentLocale();
	}

	@Override
	public Locale getLocaleForLanguage(final LanguageModel language)
	{
		final BaseSiteModel site = getBaseSiteService().getCurrentBaseSite();
		if (site != null)
		{
			final String locale = site.getLocale();
			if (locale != null && !locale.isEmpty())
			{
				return createLocale(locale);
			}
			else
			{
				final List<LanguageModel> fallbackLanguages = language.getFallbackLanguages();
				if (fallbackLanguages != null && !fallbackLanguages.isEmpty())
				{
					for (final LanguageModel fallbackLanguage : fallbackLanguages)
					{
						final Locale fallbackLocale = getLocaleForLanguage(fallbackLanguage);
						if (fallbackLocale != null)
						{
							return fallbackLocale;
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public LanguageModel getCurrentLanguage()
	{
		return getCommonI18NService().getCurrentLanguage();
	}

	@Override
	public void setCurrentLanguage(final LanguageModel language)
	{
		getCommonI18NService().setCurrentLanguage(language);

		// Set the session locale
		final Locale locale = getLocaleForLanguage(language);
		if (locale != null)
		{
			getI18nService().setCurrentLocale(locale);
		}
	}

	protected Locale createLocale(final String localeString)
	{
		Locale locale = getLocales().get(localeString);
		if (locale != null)
		{
			return locale;
		}
		else
		{
			final String[] loc = Utilities.parseLocaleCodes(localeString);
			locale = new Locale(loc[0], loc[1], loc[2]);
			getLocales().put(localeString, locale);
			return locale;
		}
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	protected I18NService getI18nService()
	{
		return i18nService;
	}

	@Required
	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
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

	protected Map<String, Locale> getLocales()
	{
		return locales;
	}
}
