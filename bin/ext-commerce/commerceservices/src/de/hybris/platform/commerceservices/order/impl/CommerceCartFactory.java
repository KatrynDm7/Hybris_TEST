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

import de.hybris.platform.commerceservices.strategies.NetGrossStrategy;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.impl.DefaultCartFactory;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import org.springframework.beans.factory.annotation.Required;


/**
 * Extension of {@link DefaultCartFactory} to additionally initialize the following fields:
 * <ul>
 * <li>net/gross setting, delegated to an instance of {@link NetGrossStrategy}</li>
 * <li>{@link de.hybris.platform.basecommerce.jalo.site.BaseSite}</li>
 * <li>{@link de.hybris.platform.store.BaseStore}</li>
 * </ul>
 */
public class CommerceCartFactory extends DefaultCartFactory
{
	private NetGrossStrategy netGrossStrategy;
	private BaseSiteService baseSiteService;
	private BaseStoreService baseStoreService;
	private KeyGenerator guidKeyGenerator;

	@Override
	protected CartModel createCartInternal()
	{
		final CartModel cart = super.createCartInternal();
		cart.setNet(Boolean.valueOf(getNetGrossStrategy().isNet()));
		cart.setSite(getBaseSiteService().getCurrentBaseSite());
		cart.setStore(getBaseStoreService().getCurrentBaseStore());
		cart.setGuid(getGuidKeyGenerator().generate().toString());
		return cart;
	}

	protected NetGrossStrategy getNetGrossStrategy()
	{
		return netGrossStrategy;
	}

	@Required
	public void setNetGrossStrategy(final NetGrossStrategy netGrossStrategy)
	{
		this.netGrossStrategy = netGrossStrategy;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService siteService)
	{
		this.baseSiteService = siteService;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService service)
	{
		this.baseStoreService = service;
	}

	protected KeyGenerator getGuidKeyGenerator()
	{
		return guidKeyGenerator;
	}

	@Required
	public void setGuidKeyGenerator(final KeyGenerator guidKeyGenerator)
	{
		this.guidKeyGenerator = guidKeyGenerator;
	}
}
