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
package de.hybris.platform.commerceservices.strategies.impl;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.commerceservices.strategies.CartValidationStrategy;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public class CartValidationWithoutCartAlteringStrategy implements CartValidationStrategy
{
	private ProductService productService;
	private CommerceStockService commerceStockService;
	private BaseStoreService baseStoreService;

	@Override
	@Deprecated
	public List<CommerceCartModification> validateCart(final CartModel cartModel)
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		return this.validateCart(parameter);
	}

	@Override
	public List<CommerceCartModification> validateCart(final CommerceCartParameter parameter)
	{
		final CartModel cartModel = parameter.getCart();

		if (cartModel != null && cartModel.getEntries() != null && !cartModel.getEntries().isEmpty())
		{
			final List<CommerceCartModification> modifications = new ArrayList<CommerceCartModification>();

			for (final AbstractOrderEntryModel orderEntryModel : cartModel.getEntries())
			{
				modifications.add(validateCartEntry((CartEntryModel) orderEntryModel));
			}

			return modifications;
		}
		else
		{
			return Collections.emptyList();
		}
	}

	protected CommerceCartModification validateCartEntry(final CartEntryModel cartEntryModel)
	{
		final Long stockLevel = getStockLevel(cartEntryModel);
		final long cartEntryLevel = cartEntryModel.getQuantity().longValue();

		final CommerceCartModification modification = new CommerceCartModification();
		modification.setEntry(cartEntryModel);
		modification.setQuantity(cartEntryLevel);//Old quantity for this entry


		// First verify that the product exists
		try
		{
			getProductService().getProductForCode(cartEntryModel.getProduct().getCode());
		}
		catch (final UnknownIdentifierException e)
		{
			modification.setStatusCode(CommerceCartModificationStatus.UNAVAILABLE);
			modification.setQuantityAdded(0);//New quantity for this entry
			return modification;
		}

		if (stockLevel != null)
		{
			if (stockLevel.longValue() <= 0)
			{
				modification.setStatusCode(CommerceCartModificationStatus.NO_STOCK);
				modification.setQuantityAdded(0);//New quantity for this entry
			}
			else if (cartEntryLevel > stockLevel.longValue())
			{
				modification.setStatusCode(CommerceCartModificationStatus.LOW_STOCK);
				modification.setQuantityAdded(stockLevel.longValue());//New quantity for this entry
			}
			else
			{
				modification.setStatusCode(CommerceCartModificationStatus.SUCCESS);
				modification.setQuantityAdded(cartEntryLevel);
			}
		}
		else
		{ //in case forceInStock status - stock value can be returned as null
			modification.setStatusCode(CommerceCartModificationStatus.SUCCESS);
			modification.setQuantityAdded(cartEntryLevel);
		}

		return modification;
	}

	protected Long getStockLevel(final CartEntryModel cartEntryModel)
	{
		final ProductModel product = cartEntryModel.getProduct();

		if (hasPointOfService(cartEntryModel))
		{
			return getCommerceStockService().getStockLevelForProductAndPointOfService(product,
					cartEntryModel.getDeliveryPointOfService());
		}
		else
		{
			return getCommerceStockService().getStockLevelForProductAndBaseStore(product,
					getBaseStoreService().getCurrentBaseStore());
		}
	}

	protected boolean hasPointOfService(final CartEntryModel cartEntryModel)
	{
		return cartEntryModel.getDeliveryPointOfService() != null;
	}


	public ProductService getProductService()
	{
		return productService;
	}

	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	public CommerceStockService getCommerceStockService()
	{
		return commerceStockService;
	}

	@Required
	public void setCommerceStockService(final CommerceStockService commerceStockService)
	{
		this.commerceStockService = commerceStockService;
	}

	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}
}
