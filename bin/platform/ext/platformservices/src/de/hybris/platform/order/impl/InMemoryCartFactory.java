/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.order.impl;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.order.CartFactory;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.internal.model.order.InMemoryCartModel;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Date;

import org.springframework.beans.factory.annotation.Required;


/**
 * Implementation of {@link CartFactory} that creates {@link InMemoryCartModel}s.
 */
public class InMemoryCartFactory implements CartFactory
{

	private KeyGenerator keyGenerator;
	private ModelService modelService;
	private UserService userService;
	private CommonI18NService commonI18NService;


	@Override
	public CartModel createCart()
	{
		final UserModel user = userService.getCurrentUser();
		final CurrencyModel currency = commonI18NService.getCurrentCurrency();
		final InMemoryCartModel cart = modelService.create(InMemoryCartModel.class);
		cart.setCode(String.valueOf(keyGenerator.generate()));
		cart.setUser(user);
		cart.setCurrency(currency);
		cart.setDate(new Date());
		cart.setNet(isNetUser(user));
		return cart;
	}


	@Required
	public void setKeyGenerator(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	private Boolean isNetUser(final UserModel user)
	{
		final User userItem = modelService.getSource(user);
		final boolean result = OrderManager.getInstance().getPriceFactory().isNetUser(userItem);
		return Boolean.valueOf(result);

	}

}
