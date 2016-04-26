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
package de.hybris.platform.commerceservices.order.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.order.CommerceCartRestoration;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationStrategy;
import de.hybris.platform.commerceservices.order.CommerceCloneSavedCartStrategy;
import de.hybris.platform.commerceservices.order.CommerceFlagForDeletionStrategy;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.commerceservices.order.CommerceSaveCartService;
import de.hybris.platform.commerceservices.order.CommerceSaveCartStrategy;
import de.hybris.platform.commerceservices.order.dao.SaveCartDao;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the interface {@link CommerceSaveCartService}
 */
public class DefaultCommerceSaveCartService implements CommerceSaveCartService
{
	private SaveCartDao saveCartDao;

	private CommerceSaveCartStrategy commerceSaveCartStrategy;
	private CommerceFlagForDeletionStrategy commerceFlagForDeletionStrategy;
	private CommerceCartRestorationStrategy commerceSaveCartRestorationStrategy;
	private CommerceCloneSavedCartStrategy commerceCloneSavedCartStrategy;

	@Override
	public CommerceSaveCartResult saveCart(final CommerceSaveCartParameter parameters) throws CommerceSaveCartException
	{
		return this.getCommerceSaveCartStrategy().saveCart(parameters);
	}

	@Override
	public CommerceSaveCartResult flagForDeletion(final CommerceSaveCartParameter parameters) throws CommerceSaveCartException
	{
		return this.getCommerceFlagForDeletionStrategy().flagForDeletion(parameters);
	}

	@Override
	public CommerceCartRestoration restoreSavedCart(final CommerceSaveCartParameter parameter) throws CommerceSaveCartException
	{
		final CommerceCartParameter commerceCartParameter = new CommerceCartParameter();
		commerceCartParameter.setCart(parameter.getCart());
		commerceCartParameter.setEnableHooks(parameter.isEnableHooks());

		try
		{
			return getCommerceSaveCartRestorationStrategy().restoreCart(commerceCartParameter);
		}
		catch (final CommerceCartRestorationException e)
		{
			throw new CommerceSaveCartException(e.getMessage(), e);
		}
	}

	@Override
	public SearchPageData<CartModel> getSavedCartsForSiteAndUser(final PageableData pageableData, final BaseSiteModel baseSite,
			final UserModel user, final List<OrderStatus> orderStatus)
	{
		return getSaveCartDao().getSavedCartsForSiteAndUser(pageableData, baseSite, user, orderStatus);
	}

	@Override
	public CommerceSaveCartResult cloneSavedCart(final CommerceSaveCartParameter parameter) throws CommerceSaveCartException
	{
		final CommerceSaveCartParameter commerceSaveCartParameter = new CommerceSaveCartParameter();
		commerceSaveCartParameter.setName(parameter.getName());
		commerceSaveCartParameter.setDescription(parameter.getDescription());
		commerceSaveCartParameter.setCart(getCommerceCloneSavedCartStrategy().cloneSavedCart(parameter).getSavedCart());
		return saveCart(commerceSaveCartParameter);
	}

	protected CommerceSaveCartStrategy getCommerceSaveCartStrategy()
	{
		return commerceSaveCartStrategy;
	}

	@Required
	public void setCommerceSaveCartStrategy(final CommerceSaveCartStrategy commerceSaveCartStrategy)
	{
		this.commerceSaveCartStrategy = commerceSaveCartStrategy;
	}

	protected CommerceFlagForDeletionStrategy getCommerceFlagForDeletionStrategy()
	{
		return commerceFlagForDeletionStrategy;
	}

	@Required
	public void setCommerceFlagForDeletionStrategy(final CommerceFlagForDeletionStrategy commerceFlagForDeletionStrategy)
	{
		this.commerceFlagForDeletionStrategy = commerceFlagForDeletionStrategy;
	}

	protected SaveCartDao getSaveCartDao()
	{
		return saveCartDao;
	}

	@Required
	public void setSaveCartDao(final SaveCartDao saveCartDao)
	{
		this.saveCartDao = saveCartDao;
	}

	protected CommerceCartRestorationStrategy getCommerceSaveCartRestorationStrategy()
	{
		return commerceSaveCartRestorationStrategy;
	}

	@Required
	public void setCommerceSaveCartRestorationStrategy(final CommerceCartRestorationStrategy commerceSaveCartRestorationStrategy)
	{
		this.commerceSaveCartRestorationStrategy = commerceSaveCartRestorationStrategy;
	}

	protected CommerceCloneSavedCartStrategy getCommerceCloneSavedCartStrategy()
	{
		return commerceCloneSavedCartStrategy;
	}

	@Required
	public void setCommerceCloneSavedCartStrategy(final CommerceCloneSavedCartStrategy commerceCloneSavedCartStrategy)
	{
		this.commerceCloneSavedCartStrategy = commerceCloneSavedCartStrategy;
	}
}
