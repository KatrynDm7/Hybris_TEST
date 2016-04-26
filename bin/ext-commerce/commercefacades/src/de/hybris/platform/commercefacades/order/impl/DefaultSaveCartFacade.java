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
package de.hybris.platform.commercefacades.order.impl;

import de.hybris.platform.commercefacades.order.SaveCartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartRestorationData;
import de.hybris.platform.commercefacades.order.data.CommerceSaveCartParameterData;
import de.hybris.platform.commercefacades.order.data.CommerceSaveCartResultData;
import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.commerceservices.order.CommerceSaveCartService;
import de.hybris.platform.commerceservices.order.CommerceSaveCartTextGenerationStrategy;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.localization.Localization;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the interface {@link SaveCartFacade}
 */
public class DefaultSaveCartFacade extends DefaultCartFacade implements SaveCartFacade
{
	private CommerceSaveCartService commerceSaveCartService;
	private CommerceSaveCartTextGenerationStrategy saveCartTextGenerationStrategy;
	private ConfigurationService configurationService;

	@Override
	public CommerceSaveCartResultData saveCart(final CommerceSaveCartParameterData inputParameters)
			throws CommerceSaveCartException
	{
		final CommerceSaveCartParameter parameter = new CommerceSaveCartParameter();

		if (StringUtils.isEmpty(inputParameters.getCartId()))
		{
			parameter.setCart(getCartService().getSessionCart());
		}
		else
		{
			final CartModel cartForCodeAndUser = getCommerceCartService().getCartForCodeAndUser(inputParameters.getCartId(),
					getUserService().getCurrentUser());

			if (cartForCodeAndUser == null)
			{
				throw new CommerceSaveCartException("Cannot find a cart for code [" + inputParameters.getCartId() + "]");
			}
			parameter.setCart(cartForCodeAndUser);
		}

		parameter.setEnableHooks(inputParameters.isEnableHooks());
		parameter.setName(generateSaveCartName(parameter.getCart(), inputParameters.getName()));
		parameter.setDescription(generateSaveCartDescription(parameter.getCart(), inputParameters.getDescription()));

		final CommerceSaveCartResult saveCartResult = getCommerceSaveCartService().saveCart(parameter);

		final CommerceSaveCartResultData saveCartResultData = new CommerceSaveCartResultData();
		saveCartResultData.setSavedCartData(getCartConverter().convert(saveCartResult.getSavedCart()));
		return saveCartResultData;
	}

	@Override
	public CommerceSaveCartResultData flagForDeletion(final String cartId) throws CommerceSaveCartException
	{
		final CommerceSaveCartParameter parameter = new CommerceSaveCartParameter();

		if (StringUtils.isEmpty(cartId))
		{
			throw new CommerceSaveCartException("No parameters defined");
		}
		else
		{
			final CartModel cartForCodeAndUser = getCommerceCartService().getCartForCodeAndUser(cartId,
					getUserService().getCurrentUser());

			if (cartForCodeAndUser == null)
			{
				throw new CommerceSaveCartException("Cannot find a cart for code [" + cartId + "]");
			}
			parameter.setCart(cartForCodeAndUser);
			parameter.setEnableHooks(getConfigurationService().getConfiguration().getBoolean(
					CommerceServicesConstants.FLAGFORDELETIONHOOK_ENABLED, true));
		}

		final CommerceSaveCartResult saveCartResult = getCommerceSaveCartService().flagForDeletion(parameter);

		final CommerceSaveCartResultData flagForDeletionResultData = new CommerceSaveCartResultData();
		flagForDeletionResultData.setSavedCartData(getCartConverter().convert(saveCartResult.getSavedCart()));

		return flagForDeletionResultData;
	}

	@Override
	public CartRestorationData restoreSavedCart(final CommerceSaveCartParameterData parameters) throws CommerceSaveCartException
	{
		final CommerceSaveCartParameter parameter = new CommerceSaveCartParameter();

		final CartModel cartForCodeAndUser = getCommerceCartService().getCartForCodeAndUser(parameters.getCartId(),
				getUserService().getCurrentUser());

		if (null == cartForCodeAndUser)
		{
			throw new CommerceSaveCartException("Cannot find a cart for code [" + parameters.getCartId() + "]");
		}

		parameter.setCart(cartForCodeAndUser);
		parameter.setEnableHooks(parameters.isEnableHooks());

		return getCartRestorationConverter().convert(getCommerceSaveCartService().restoreSavedCart(parameter));
	}

	@Override
	public CommerceSaveCartResultData getCartForCodeAndCurrentUser(final CommerceSaveCartParameterData inputParameters)
			throws CommerceSaveCartException
	{
		CartModel cartToRetrieve = null;

		if (StringUtils.isNotEmpty(inputParameters.getCartId()))
		{
			cartToRetrieve = getCommerceCartService().getCartForCodeAndUser(inputParameters.getCartId(),
					getUserService().getCurrentUser());
		}
		else
		{
			throw new CommerceSaveCartException("Cart code cannot be empty");
		}

		if (null == cartToRetrieve)
		{
			throw new CommerceSaveCartException("Cannot find a cart for code [" + inputParameters.getCartId() + "]");
		}

		final CommerceSaveCartResultData saveCartResultData = new CommerceSaveCartResultData();
		saveCartResultData.setSavedCartData(getCartConverter().convert(cartToRetrieve));

		return saveCartResultData;
	}

	@Override
	public SearchPageData<CartData> getSavedCartsForCurrentUser(final PageableData pageableData,
			final List<OrderStatus> orderStatus)
	{
		final SearchPageData<CartData> result = new SearchPageData<>();
		final SearchPageData<CartModel> savedCartModels = commerceSaveCartService.getSavedCartsForSiteAndUser(pageableData,
				getBaseSiteService().getCurrentBaseSite(), getUserService().getCurrentUser(), orderStatus);

		result.setPagination(savedCartModels.getPagination());
		result.setSorts(savedCartModels.getSorts());

		final List<CartData> savedCartDatas = Converters.convertAll(savedCartModels.getResults(), getCartConverter());

		result.setResults(savedCartDatas);
		return result;
	}

	@Override
	public CommerceSaveCartResultData cloneSavedCart(final CommerceSaveCartParameterData parameter)
			throws CommerceSaveCartException
	{

		if (StringUtils.isEmpty(parameter.getCartId()))
		{
			throw new CommerceSaveCartException("Cart code cannot be empty");
		}

		final CommerceSaveCartParameter commerceSaveCartParameter = new CommerceSaveCartParameter();
		final CartModel cart = getCommerceCartService().getCartForCodeAndUser(parameter.getCartId(),
				getUserService().getCurrentUser());

		if (cart == null || cart.getSaveTime() == null)
		{
			throw new CommerceSaveCartException("Cannot find a saved cart for code [" + parameter.getCartId() + "]");
		}

		commerceSaveCartParameter.setCart(cart);

		commerceSaveCartParameter.setName(generateSaveCartName(cart, parameter.getName(),
				StringUtils.isEmpty(parameter.getName()) ? true : false));
		commerceSaveCartParameter.setDescription(generateSaveCartDescription(cart, parameter.getDescription()));
		commerceSaveCartParameter.setEnableHooks(parameter.isEnableHooks());

		final CommerceSaveCartResult saveCartResult = getCommerceSaveCartService().cloneSavedCart(commerceSaveCartParameter);
		final CommerceSaveCartResultData saveCartResultData = new CommerceSaveCartResultData();

		saveCartResultData.setSavedCartData(getCartConverter().convert(saveCartResult.getSavedCart()));

		return saveCartResultData;
	}

	protected String generateSaveCartName(final CartModel cartModel, final String name)
	{
		return generateSaveCartName(cartModel, name, false);
	}

	protected String generateSaveCartName(final CartModel cartModel, final String name, final boolean clone)
	{
		final String clonePrefix = Localization.getLocalizedString("commerceservices.cart.copyof");
		final StringBuffer nameBuffer = clone ? new StringBuffer(clonePrefix).append(" ") : new StringBuffer();

		if (StringUtils.isNotEmpty(name))
		{
			return nameBuffer.append(name).toString();
		}
		else if (StringUtils.isNotEmpty(cartModel.getName()))
		{
			return nameBuffer.append(cartModel.getName()).toString();
		}

		return getSaveCartTextGenerationStrategy().generateSaveCartName(cartModel);
	}

	protected String generateSaveCartDescription(final CartModel cartModel, final String description)
	{
		if (StringUtils.isNotEmpty(description))
		{
			return description;
		}
		else if (StringUtils.isNotEmpty(cartModel.getDescription()))
		{
			return cartModel.getDescription();
		}

		return getSaveCartTextGenerationStrategy().generateSaveCartDescription(cartModel);
	}

	protected CommerceSaveCartService getCommerceSaveCartService()
	{
		return commerceSaveCartService;
	}

	@Required
	public void setCommerceSaveCartService(final CommerceSaveCartService commerceSaveCartService)
	{
		this.commerceSaveCartService = commerceSaveCartService;
	}

	protected CommerceSaveCartTextGenerationStrategy getSaveCartTextGenerationStrategy()
	{
		return saveCartTextGenerationStrategy;
	}

	@Required
	public void setSaveCartTextGenerationStrategy(final CommerceSaveCartTextGenerationStrategy saveCartTextGenerationStrategy)
	{
		this.saveCartTextGenerationStrategy = saveCartTextGenerationStrategy;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
}
