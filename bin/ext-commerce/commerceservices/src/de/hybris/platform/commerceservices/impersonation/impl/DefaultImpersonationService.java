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
package de.hybris.platform.commerceservices.impersonation.impl;

import de.hybris.platform.basecommerce.exceptions.BaseSiteActivationException;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commerceservices.impersonation.ImpersonationContext;
import de.hybris.platform.commerceservices.impersonation.ImpersonationService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.enums.UserTaxGroup;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * The impersonation service executes code passed in a
 * {@link de.hybris.platform.commerceservices.impersonation.ImpersonationService.Executor} in the context of user,
 * currency, language, site, etc.
 * 
 */
public class DefaultImpersonationService implements ImpersonationService
{
	private static final Logger LOG = Logger.getLogger(DefaultImpersonationService.class);

	private SessionService sessionService;
	private CatalogVersionService catalogVersionService;
	private UserService userService;
	private SearchRestrictionService searchRestrictionService;
	private FlexibleSearchService flexibleSearchService;
	private CommonI18NService i18nService;
	private BaseStoreService baseStoreService;
	private BaseSiteService baseSiteService;

	@Override
	public <R, T extends Throwable> R executeInContext(final ImpersonationContext context, final Executor<R, T> wrapper) throws T
	{
		final Object result = getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{
				try
				{
					//impersonate
					configureSession(context);
					//execute the injected code
					return wrapper.execute();
				}
				catch (final Throwable e)//NOPMD
				{
					return e;
				}
			}
		});

		if (result instanceof Throwable)
		{
			throw (T) result;
		}
		else
		{
			return (R) result;
		}
	}

	/**
	 * Impersonates local session with parameters passed in context.
	 * 
	 * @param context
	 *           The context to impersonate
	 */
	protected void configureSession(final ImpersonationContext context)
	{
		final BaseSiteModel site = determineSessionBaseSite(context);

		if (site != null)
		{
			try
			{
				getBaseSiteService().setCurrentBaseSite(site, true);
			}
			catch (final BaseSiteActivationException e)
			{
				LOG.error("Failed to activate BaseSite [" + site + "] in the session context.", e);
			}
		}
		else
		{
			LOG.error("Couldn't determine the site from the context. Hence the site related session state won't be adjusted.");
		}

		final UserModel user = determineSessionUser(context);

		if (user != null)
		{
			getUserService().setCurrentUser(user);
		}

		final UserTaxGroup taxGroup = determineSessionTaxGroup(context);

		if (taxGroup != null)
		{
			getSessionService().setAttribute(Europe1Constants.PARAMS.UTG, taxGroup);
		}

		final Collection<CatalogVersionModel> catalogVersions = determineSessionCatalogVersions(context);

		if (catalogVersions != null && !catalogVersions.isEmpty())
		{
			getCatalogVersionService().setSessionCatalogVersions(catalogVersions);
		}

		final LanguageModel language = determineSessionLanguage(context);

		if (language != null)
		{
			getI18nService().setCurrentLanguage(language);
		}

		final CurrencyModel currency = determineSessionCurrency(context);

		if (currency != null)
		{
			getI18nService().setCurrentCurrency(currency);
		}
	}

	/**
	 * @param context
	 *           The context to impersonate
	 * @return base site that should be used in session context. No null returned.
	 */
	protected BaseSiteModel determineSessionBaseSite(final ImpersonationContext context)
	{
		BaseSiteModel site = context.getSite();
		if (site == null && context.getOrder() != null)
		{
			site = context.getOrder().getSite();
		}
		if (site == null)
		{
			site = baseSiteService.getCurrentBaseSite();//fallback
		}
		return site;
	}

	/**
	 * @param context
	 *           The context to impersonate
	 * @return currency that should be used in session context. No null returned.
	 */
	protected CurrencyModel determineSessionCurrency(final ImpersonationContext context)
	{
		CurrencyModel currency = context.getCurrency();
		if (currency == null && context.getOrder() instanceof OrderModel)
		{
			currency = context.getOrder().getCurrency();
		}
		if (currency == null)
		{
			currency = getI18nService().getCurrentCurrency();
		}
		return currency;
	}

	/**
	 * @param context
	 *           The context to impersonate
	 * @return language that should be used in session context. No null returned.
	 */
	protected LanguageModel determineSessionLanguage(final ImpersonationContext context)
	{
		LanguageModel language = context.getLanguage();
		if (language == null && context.getOrder() instanceof OrderModel)
		{
			language = ((OrderModel)context.getOrder()).getLanguage();
		}
		if (language == null)
		{
			language = getI18nService().getCurrentLanguage();
		}
		return language;
	}

	/**
	 * @param context
	 *           The context to impersonate
	 * @return catalog versions that should be used in session context. No null returned.
	 */
	protected Collection<CatalogVersionModel> determineSessionCatalogVersions(final ImpersonationContext context)
	{
		Collection<CatalogVersionModel> catalogVersions = context.getCatalogVersions();
		if (catalogVersions == null)
		{
			catalogVersions = getSessionCatalogVersionsForUser(determineSessionUser(context));
		}
		return catalogVersions;
	}

	/**
	 * @param context
	 *           The context to impersonate
	 * @return tax group that should be used into session context.
	 */
	protected UserTaxGroup determineSessionTaxGroup(final ImpersonationContext context)
	{
		// Try the tax group in the context
		final UserTaxGroup contextTaxGroup = context.getTaxGroup();
		if (contextTaxGroup != null)
		{
			return contextTaxGroup;
		}

		// Try the order in the context
		final AbstractOrderModel order = context.getOrder();
		if (order != null) // use tax group from order if order present and tax group missing
		{
			final BaseStoreModel orderBaseStore = order.getStore();
			if (orderBaseStore != null)
			{
				final UserTaxGroup orderBaseStoreTaxGroup = orderBaseStore.getTaxGroup();
				if (orderBaseStoreTaxGroup != null)
				{
					return orderBaseStoreTaxGroup;
				}
			}
		}

		// Try the site in the context
		final BaseSiteModel site = context.getSite();
		if (site != null)
		{
			final List<BaseStoreModel> stores = site.getStores();
			if (stores != null && !stores.isEmpty())
			{
				for (final BaseStoreModel store : stores)
				{
					if (store.getTaxGroup() != null)
					{
						return store.getTaxGroup();
					}
				}
			}
		}

		// Try the current base store in the session
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		if (currentBaseStore != null)
		{
			final UserTaxGroup currentBaseStoreTaxGroup = currentBaseStore.getTaxGroup();
			if (currentBaseStoreTaxGroup != null)
			{
				return currentBaseStoreTaxGroup;
			}
		}

		return null;
	}

	/**
	 * @param context
	 *           The context to impersonate
	 * @return user that should be used into session context. No null returned.
	 */
	protected UserModel determineSessionUser(final ImpersonationContext context)
	{
		final AbstractOrderModel order = context.getOrder();
		UserModel user = context.getUser();
		if (user == null && order != null)//use user from order if user missing nad order present
		{
			user = order.getUser();
		}
		if (user == null)
		{
			user = getUserService().getCurrentUser();//fallback
		}
		return user;
	}

	/**
	 * Get the session catalog versions for the specified user.
	 * 
	 * Get the set of catalog versions that should be visible to the specified user and should be set on the user's
	 * session. These catalog versions will be used in all search restrictions for code run in the impersonation context
	 * of the user.
	 * 
	 * The default implementation is to get all the active catalog versions. Sub-classes can implement a different
	 * strategy.
	 * 
	 * @param user
	 *           The user
	 * @return The set of catalog versions for this specified user
	 */
	protected Set<CatalogVersionModel> getSessionCatalogVersionsForUser(final UserModel user)
	{
		//
		// We are trying to duplicate the default behavior of the CatalogManager.
		// The CatalogManager intercepts web requests for the frontend website and
		// sets the session catalog versions for the customer.
		// The behavior of the CatalogManager is to get all the active catalog versions
		// and then filter these by the URL requested.
		// Obviously we can't use the URL filtering to identify the catalog so we
		// just go with all the active ones.
		// We can't use catalog read permissions as these only apply to backend
		// visibility restrictions - i.e. they can only be set for employees not
		// for customers.
		//
		final Collection<CatalogVersionModel> activeCatalogVersions = getActiveCatalogVersions();

		if (activeCatalogVersions.isEmpty())
		{
			LOG.error("Cannot find any active catalog versions for user [" + user.getUid() + "].");
		}

		return new HashSet<CatalogVersionModel>(activeCatalogVersions);
	}

	/**
	 * Get all the active catalog versions in the system.
	 * 
	 * @return The active catalog versions or an empty list if none found
	 */
	protected Collection<CatalogVersionModel> getActiveCatalogVersions()
	{
		// Create a temporary session context and disable restrictions
		final Object result = getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{
				getSearchRestrictionService().disableSearchRestrictions();

				// Find all the active catalog versions
				final String query = "SELECT {" + ItemModel.PK + "} FROM {" + CatalogVersionModel._TYPECODE + "*} WHERE {"
						+ CatalogVersionModel.ACTIVE + "}=?active ORDER BY {" + ItemModel.CREATIONTIME + "}";

				return getFlexibleSearchService().search(query, Collections.singletonMap("active", Boolean.TRUE)).getResult();
			}
		});

		if (result instanceof Collection)
		{
			return (Collection<CatalogVersionModel>) result;
		}
		return Collections.emptyList();
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

	protected CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected SearchRestrictionService getSearchRestrictionService()
	{
		return searchRestrictionService;
	}

	@Required
	public void setSearchRestrictionService(final SearchRestrictionService searchRestrictionService)
	{
		this.searchRestrictionService = searchRestrictionService;
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	protected CommonI18NService getI18nService()
	{
		return i18nService;
	}

	@Required
	public void setI18nService(final CommonI18NService i18nService)
	{
		this.i18nService = i18nService;
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

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}
}
