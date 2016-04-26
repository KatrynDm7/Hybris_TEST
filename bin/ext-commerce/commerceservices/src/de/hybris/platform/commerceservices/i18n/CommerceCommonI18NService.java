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
package de.hybris.platform.commerceservices.i18n;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;

import java.util.Collection;
import java.util.List;
import java.util.Locale;


/**
 * Interface to return list of currencies, based on sites and stores dependencies.
 * 
 */
public interface CommerceCommonI18NService
{
	/**
	 * Method for getting list of currencies stored as a base store property.
	 * 
	 * @return list of currencies supported.
	 */
	List<CurrencyModel> getAllCurrencies();

	/**
	 * Method for getting list of languages stored as a catalog version property.
	 * 
	 * @return list of languages supported.
	 */
	Collection<LanguageModel> getAllLanguages();

	/**
	 * Method for getting list of countries stored as a catalog version property.
	 *
	 * @return list of countries supported.
	 */
	Collection<CountryModel> getAllCountries();

	/**
	 * Method for getting the default language for the current store.
	 *
	 * @return the default language.
	 */
	LanguageModel getDefaultLanguage();

	/**
	 * Method for getting the default currency for the current store.
	 *
	 * @return the default currency.
	 */
	CurrencyModel getDefaultCurrency();

	/**
	 * Get the current language
	 *
	 * @return the current language
	 */
	LanguageModel getCurrentLanguage();

	/**
	 * Set current language.
	 *
	 * @param language
	 *           the language to be set as a current language.
	 */
	void setCurrentLanguage(LanguageModel language);

	/**
	 * Get the current currency
	 *
	 * @return the current currency
	 */
	CurrencyModel getCurrentCurrency();

	/**
	 * Set current currency.
	 *
	 * @param currency
	 *           the currency to be set as a current currency.
	 */
	void setCurrentCurrency(CurrencyModel currency);

	/**
	 * Get the current locale
	 *
	 * @return the current currency
	 */
	Locale getCurrentLocale();

	/**
	 * Get the site specific locale for the specified language
	 *
	 * @param language
	 *           the language
	 * @return the locale
	 */
	Locale getLocaleForLanguage(LanguageModel language);
}
