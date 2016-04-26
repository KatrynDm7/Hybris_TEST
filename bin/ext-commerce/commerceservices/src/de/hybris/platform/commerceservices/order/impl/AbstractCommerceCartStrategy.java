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

import de.hybris.platform.commerceservices.order.CommerceCartCalculationStrategy;
import de.hybris.platform.commerceservices.order.dao.CartEntryDao;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.commerceservices.strategies.ModifiableChecker;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public abstract class AbstractCommerceCartStrategy
{

	protected static final long DEFAULT_FORCE_IN_STOCK_MAX_QUANTITY = 9999;

	protected long forceInStockMaxQuantity = DEFAULT_FORCE_IN_STOCK_MAX_QUANTITY;

	private CommerceStockService commerceStockService;
	private ProductService productService;
	private CartService cartService;
	private ModelService modelService;
	private BaseStoreService baseStoreService;
	private ModifiableChecker<AbstractOrderEntryModel> entryOrderChecker;
	private CartEntryDao cartEntryDao;
	private CommerceCartCalculationStrategy commerceCartCalculationStrategy;

	/**
	 * Work out the allowed quantity adjustment for a product in the cart given a requested quantity change.
	 * 
	 * @param cartModel
	 *           the cart
	 * @param productModel
	 *           the product in the cart
	 * @param quantityToAdd
	 *           the amount to increase the quantity of the product in the cart, may be negative if the request is to
	 *           reduce the quantity
	 * @param pointOfServiceModel
	 *           the PointOfService to check stock at, can be null
	 * @return the allowed adjustment
	 */
	protected long getAllowedCartAdjustmentForProduct(final CartModel cartModel, final ProductModel productModel,
			final long quantityToAdd, final PointOfServiceModel pointOfServiceModel)
	{
		final long cartLevel = checkCartLevel(productModel, cartModel, pointOfServiceModel);
		final long stockLevel = getAvailableStockLevel(productModel, pointOfServiceModel);

		// How many will we have in our cart if we add quantity
		final long newTotalQuantity = cartLevel + quantityToAdd;

		// Now limit that to the total available in stock
		final long newTotalQuantityAfterStockLimit = Math.min(newTotalQuantity, stockLevel);

		// So now work out what the maximum allowed to be added is (note that
		// this may be negative!)
		final Integer maxOrderQuantity = productModel.getMaxOrderQuantity();

		if (isMaxOrderQuantitySet(maxOrderQuantity))
		{
			final long newTotalQuantityAfterProductMaxOrder = Math
					.min(newTotalQuantityAfterStockLimit, maxOrderQuantity.longValue());
			return newTotalQuantityAfterProductMaxOrder - cartLevel;
		}
		return newTotalQuantityAfterStockLimit - cartLevel;
	}

	/**
	 * Work out if the stock level is sufficient for a product in the cart given a requested quantity change.
	 *
	 * @param cartModel
	 *           the cart
	 * @param productModel
	 *           the product in the cart
	 * @param pointOfServiceModel
	 *           the PointOfService to check stock at, can be null
	 * @param quantityToAdd
	 *           the amount to increase the quantity of the product in the cart, may be negative if the request is to
	 *           reduce the quantity
	 * @return the allowed adjustment
	 */
	protected boolean isStockLevelSufficient(final CartModel cartModel, final ProductModel productModel,
			final PointOfServiceModel pointOfServiceModel, final long quantityToAdd)
	{
		final long cartLevel = checkCartLevel(productModel, cartModel, pointOfServiceModel);
		final long stockLevel = getAvailableStockLevel(productModel, pointOfServiceModel);

		final long newTotalQuantity = cartLevel + quantityToAdd;
		return newTotalQuantity <= stockLevel;
	}

	protected long getAvailableStockLevel(final ProductModel productModel, final PointOfServiceModel pointOfServiceModel)
	{
		final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
		if (!getCommerceStockService().isStockSystemEnabled(baseStore))
		{
			return getForceInStockMaxQuantity();
		}
		else
		{
			final Long availableStockLevel;

			if (pointOfServiceModel == null)
			{
				availableStockLevel = getCommerceStockService().getStockLevelForProductAndBaseStore(productModel, baseStore);
			}
			else
			{
				availableStockLevel = getCommerceStockService().getStockLevelForProductAndPointOfService(productModel,
						pointOfServiceModel);
			}

			if (availableStockLevel == null)
			{
				return getForceInStockMaxQuantity();
			}
			else
			{
				return availableStockLevel.longValue();
			}
		}
	}

	protected long checkCartLevel(final ProductModel productModel, final CartModel cartModel,
			final PointOfServiceModel pointOfServiceModel)
	{
		long cartLevel = 0;
		if (pointOfServiceModel == null)
		{
			for (final CartEntryModel entryModel : getCartService().getEntriesForProduct(cartModel, productModel))
			{
				if (entryModel.getDeliveryPointOfService() == null)
				{
					cartLevel += entryModel.getQuantity() != null ? entryModel.getQuantity().longValue() : 0;
				}
			}
		}
		else
		{
			for (final CartEntryModel entryModel : getCartEntryDao().findEntriesByProductAndPointOfService(cartModel, productModel,
					pointOfServiceModel))
			{
				cartLevel += entryModel.getQuantity() != null ? entryModel.getQuantity().longValue() : 0;
			}
		}
		return cartLevel;
	}

	protected boolean isMaxOrderQuantitySet(final Integer maxOrderQuantity)
	{
		return maxOrderQuantity != null;
	}


	protected void normalizeEntryNumbers(final CartModel cartModel)
	{
		final List<AbstractOrderEntryModel> entries = new ArrayList<AbstractOrderEntryModel>(cartModel.getEntries());
		Collections.sort(entries, new BeanComparator(AbstractOrderEntryModel.ENTRYNUMBER, new ComparableComparator()));
		for (int i = 0; i < entries.size(); i++)
		{
			entries.get(i).setEntryNumber(Integer.valueOf(i));
			getModelService().save(entries.get(i));
		}
	}

	protected boolean isOrderEntryUpdatable(final AbstractOrderEntryModel entryToUpdate)
	{
		return getEntryOrderChecker().canModify(entryToUpdate);
	}


	protected AbstractOrderEntryModel getEntryForNumber(final AbstractOrderModel order, final int number)
	{
		final List<AbstractOrderEntryModel> entries = order.getEntries();
		if (entries != null && !entries.isEmpty())
		{
			final Integer requestedEntryNumber = Integer.valueOf(number);
			for (final AbstractOrderEntryModel entry : entries)
			{
				if (entry != null && requestedEntryNumber.equals(entry.getEntryNumber()))
				{
					return entry;
				}
			}
		}
		return null;
	}

	protected Integer getEntryForProductAndPointOfService(final CartModel cartModel, final ProductModel productModel,
			final PointOfServiceModel pointOfServiceModel)
	{
		for (final CartEntryModel entryModel : getCartEntryDao().findEntriesByProductAndPointOfService(cartModel, productModel,
				pointOfServiceModel))
		{
			if (isOrderEntryUpdatable(entryModel))
			{
				return entryModel.getEntryNumber();
			}
		}
		return Integer.valueOf(-1);
	}

	protected ProductService getProductService()
	{
		return productService;
	}

	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	protected CartService getCartService()
	{
		return cartService;
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected CommerceStockService getCommerceStockService()
	{
		return commerceStockService;
	}

	@Required
	public void setCommerceStockService(final CommerceStockService commerceStockService)
	{
		this.commerceStockService = commerceStockService;
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

	protected long getForceInStockMaxQuantity()
	{
		return forceInStockMaxQuantity;
	}

	public void setForceInStockMaxQuantity(final long forceInStockMaxQuantity)
	{
		this.forceInStockMaxQuantity = forceInStockMaxQuantity;
	}

	protected ModifiableChecker<AbstractOrderEntryModel> getEntryOrderChecker()
	{
		return entryOrderChecker;
	}

	@Required
	public void setEntryOrderChecker(final ModifiableChecker<AbstractOrderEntryModel> entryOrderChecker)
	{
		this.entryOrderChecker = entryOrderChecker;
	}

	protected CartEntryDao getCartEntryDao()
	{
		return cartEntryDao;
	}

	@Required
	public void setCartEntryDao(final CartEntryDao cartEntryDao)
	{
		this.cartEntryDao = cartEntryDao;
	}

	protected CommerceCartCalculationStrategy getCommerceCartCalculationStrategy()
	{
		return commerceCartCalculationStrategy;
	}

	@Required
	public void setCommerceCartCalculationStrategy(final CommerceCartCalculationStrategy commerceCartCalculationStrategy)
	{
		this.commerceCartCalculationStrategy = commerceCartCalculationStrategy;
	}
}
