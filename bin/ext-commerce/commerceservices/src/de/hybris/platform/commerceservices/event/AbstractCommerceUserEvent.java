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
package de.hybris.platform.commerceservices.event;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.store.BaseStoreModel;


/**
 * Abstract base class for user events generated in the commerceservices layer.
 */
public abstract class AbstractCommerceUserEvent<T extends BaseSiteModel> extends AbstractEvent
{
	private BaseStoreModel baseStore;
	private T site;
	private CustomerModel customer;
	private LanguageModel language;
	private CurrencyModel currency;

	/**
	 * @return the baseStore
	 */
	public BaseStoreModel getBaseStore()
	{
		return baseStore;
	}

	/**
	 * @param baseStore
	 *           the baseStore to set
	 */
	public void setBaseStore(final BaseStoreModel baseStore)
	{
		this.baseStore = baseStore;
	}

	/**
	 * @return the baseSite
	 */
	public T getSite()
	{
		return site;
	}

	/**
	 * @param site
	 *           the baseSite to set
	 */
	public void setSite(final T site)
	{
		this.site = site;
	}

	/**
	 * @return the customer
	 */
	public CustomerModel getCustomer()
	{
		return customer;
	}

	/**
	 * @param customer
	 *           the customer to set
	 */
	public void setCustomer(final CustomerModel customer)
	{
		this.customer = customer;
	}

	/**
	 * @return the language
	 */
	public LanguageModel getLanguage()
	{
		return language;
	}

	/**
	 * @param language
	 *           the language to set
	 */
	public void setLanguage(final LanguageModel language)
	{
		this.language = language;
	}

	/**
	 *
	 * @return  CurrencyModel
	 */
	public CurrencyModel getCurrency()
	{
		return currency;
	}

	/**
	 *
	 * @param currency
	 */
	public void setCurrency(final CurrencyModel currency)
	{
		this.currency = currency;
	}

}
