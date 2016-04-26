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

import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.commerceservices.strategies.CartCleanStrategy;
import de.hybris.platform.commerceservices.strategies.CartValidationStrategy;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public class DefaultCartValidationStrategy implements CartValidationStrategy, CartCleanStrategy
{
	private ModelService modelService;
	private CartService cartService;
	private ProductService productService;
	private CommerceStockService commerceStockService;
	private BaseStoreService baseStoreService;
	private UserService userService;

	protected CommerceCartModification validateCartEntry(final CartModel cartModel, final CartEntryModel cartEntryModel)
	{
		// First verify that the product exists
		try
		{
			getProductService().getProductForCode(cartEntryModel.getProduct().getCode());
		}
		catch (final UnknownIdentifierException e)
		{
			final CommerceCartModification modification = new CommerceCartModification();
			modification.setStatusCode(CommerceCartModificationStatus.UNAVAILABLE);
			modification.setQuantityAdded(0);
			modification.setQuantity(0);

			final CartEntryModel entry = new CartEntryModel();
			entry.setProduct(cartEntryModel.getProduct());

			modification.setEntry(entry);

			getModelService().remove(cartEntryModel);
			getModelService().refresh(cartModel);

			return modification;
		}

		// Overall availability of this product
		final Long stockLevel = getStockLevel(cartEntryModel);

		// Overall stock quantity in the cart
		final long cartLevel = getCartLevel(cartEntryModel, cartModel);

		// Stock quantity for this cartEntry
		final long cartEntryLevel = cartEntryModel.getQuantity().longValue();

		// New stock quantity for this cartEntry
		final long newOrderEntryLevel;

		Long stockLevelForProductInBaseStore = null;

		if (stockLevel != null)
		{
			// this product is not available at the given point of service.
			if (isProductNotAvailableInPOS(cartEntryModel, stockLevel))
			{

				stockLevelForProductInBaseStore = getCommerceStockService().getStockLevelForProductAndBaseStore(
						cartEntryModel.getProduct(), getBaseStoreService().getCurrentBaseStore());

				if (stockLevelForProductInBaseStore != null)
				{
					newOrderEntryLevel = Math.min(cartEntryLevel, stockLevelForProductInBaseStore.longValue());
				}
				else
				{
					newOrderEntryLevel = Math.min(cartEntryLevel, cartLevel);
				}
			}
			else
			{
				// if stock is available.. get either requested quantity if its lower than available stock or maximum stock.
				newOrderEntryLevel = Math.min(cartEntryLevel, stockLevel.longValue());
			}
		}
		else
		{
			// if stock is not available.. play save.. only allow quantity that was already in cart.
			newOrderEntryLevel = Math.min(cartEntryLevel, cartLevel);
		}

		// this product is not available at the given point of service.
		if (stockLevelForProductInBaseStore != null && stockLevelForProductInBaseStore.longValue() != 0)
		{
			final CommerceCartModification modification = new CommerceCartModification();
			modification.setStatusCode(CommerceCartModificationStatus.MOVED_FROM_POS_TO_STORE);
			final CartEntryModel existingEntryForProduct = getExistingShipCartEntryForProduct(cartModel, cartEntryModel.getProduct());
			if (existingEntryForProduct != null)
			{
				getModelService().remove(cartEntryModel);
				final long quantityAdded = stockLevelForProductInBaseStore.longValue() >= cartLevel ? newOrderEntryLevel : cartLevel
						- stockLevelForProductInBaseStore.longValue();
				modification.setQuantityAdded(quantityAdded);
				final long updatedQuantity = (stockLevelForProductInBaseStore.longValue() <= cartLevel ? stockLevelForProductInBaseStore
						.longValue() : cartLevel);
				modification.setQuantity(updatedQuantity);
				existingEntryForProduct.setQuantity(Long.valueOf(updatedQuantity));
				getModelService().save(existingEntryForProduct);
				modification.setEntry(existingEntryForProduct);
			}
			else
			{
				modification.setQuantityAdded(newOrderEntryLevel);
				modification.setQuantity(cartEntryLevel);
				cartEntryModel.setDeliveryPointOfService(null);
				modification.setEntry(cartEntryModel);
				getModelService().save(cartEntryModel);
			}

			getModelService().refresh(cartModel);

			return modification;
		}
		else if ((stockLevel != null && stockLevel.longValue() <= 0) || newOrderEntryLevel < 0)
		{
			// If no stock is available or the cart is full for this product, remove the entry from the cart
			final CommerceCartModification modification = new CommerceCartModification();
			modification.setStatusCode(CommerceCartModificationStatus.NO_STOCK);
			modification.setQuantityAdded(0);//New quantity for this entry
			modification.setQuantity(cartEntryLevel);//Old quantity for this entry
			final CartEntryModel entry = new CartEntryModel();
			entry.setProduct(cartEntryModel.getProduct());
			modification.setEntry(entry);
			getModelService().remove(cartEntryModel);
			getModelService().refresh(cartModel);

			return modification;
		}
		else if (cartEntryLevel != newOrderEntryLevel)
		{
			// If the orderLevel has changed for this cartEntry, then recalculate the quantity
			final CommerceCartModification modification = new CommerceCartModification();
			modification.setStatusCode(CommerceCartModificationStatus.LOW_STOCK);
			modification.setQuantityAdded(newOrderEntryLevel);
			modification.setQuantity(cartEntryLevel);
			modification.setEntry(cartEntryModel);
			cartEntryModel.setQuantity(Long.valueOf(newOrderEntryLevel));
			getModelService().save(cartEntryModel);
			getModelService().refresh(cartModel);

			return modification;
		}
		else
		{
			final CommerceCartModification modification = new CommerceCartModification();
			modification.setStatusCode(CommerceCartModificationStatus.SUCCESS);
			modification.setQuantityAdded(cartEntryLevel);
			modification.setQuantity(cartEntryLevel);
			modification.setEntry(cartEntryModel);

			return modification;
		}
	}

	protected CartEntryModel getExistingShipCartEntryForProduct(final CartModel cartModel, final ProductModel product)
	{
		for (final CartEntryModel entryModel : getCartService().getEntriesForProduct(cartModel, product))
		{
			if (entryModel.getDeliveryPointOfService() == null)
			{
				return entryModel;
			}
		}
		return null;
	}

	protected boolean isProductNotAvailableInPOS(final CartEntryModel cartEntryModel, final Long stockLevel)
	{
		return stockLevel.longValue() <= 0 && hasPointOfService(cartEntryModel);
	}

	protected void validateDelivery(final CartModel cartModel)
	{
		if (cartModel.getDeliveryAddress() != null)
		{
			if (!isGuestUserCart(cartModel) && !getUserService().getCurrentUser().equals(cartModel.getDeliveryAddress().getOwner()))
			{
				cartModel.setDeliveryAddress(null);
				getModelService().save(cartModel);
			}
		}
	}

	protected void validatePayment(final CartModel cartModel)
	{
		if (cartModel.getPaymentInfo() != null)
		{
			if (!isGuestUserCart(cartModel) && !getUserService().getCurrentUser().equals(cartModel.getPaymentInfo().getUser()))
			{
				cartModel.setPaymentInfo(null);
				getModelService().save(cartModel);
			}
		}
	}

	protected boolean isGuestUserCart(final CartModel cartModel)
	{
		final ItemModel customer = cartModel.getUser();
		return customer instanceof CustomerModel && CustomerType.GUEST.equals(((CustomerModel) customer).getType());
	}

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

		cleanCart(cartModel);

		if (cartModel != null && cartModel.getEntries() != null && !cartModel.getEntries().isEmpty())
		{
			final List<CommerceCartModification> modifications = new ArrayList<CommerceCartModification>();

			for (final AbstractOrderEntryModel orderEntryModel : cartModel.getEntries())
			{
				modifications.add(validateCartEntry(cartModel, (CartEntryModel) orderEntryModel));
			}

			return modifications;
		}
		else
		{
			return Collections.emptyList();
		}
	}

	@Override
	public void cleanCart(final CartModel cartModel)
	{
		validatePayment(cartModel);
		validateDelivery(cartModel);
	}

	protected Long getStockLevel(final CartEntryModel cartEntryModel)
	{
		final ProductModel product = cartEntryModel.getProduct();
		final PointOfServiceModel pointOfService = cartEntryModel.getDeliveryPointOfService();

		if (hasPointOfService(cartEntryModel))
		{
			return getCommerceStockService().getStockLevelForProductAndPointOfService(product, pointOfService);
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

	protected long getCartLevel(final CartEntryModel cartEntryModel, final CartModel cartModel)
	{
		final ProductModel product = cartEntryModel.getProduct();
		long cartLevel = 0;
		for (final CartEntryModel entryModel : getCartService().getEntriesForProduct(cartModel, product))
		{
			cartLevel += entryModel.getQuantity() != null ? entryModel.getQuantity().longValue() : 0;
		}
		return cartLevel;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public CartService getCartService()
	{
		return cartService;
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
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

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}
}
