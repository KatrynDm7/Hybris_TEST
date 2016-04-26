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

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.storelocator.model.PointOfServiceModel;


public class DefaultCommerceAddToCartStrategy extends AbstractCommerceAddToCartStrategy
{
	/**
	 * Adds an item to the cart for pickup in a given location
	 * 
	 * @param parameter
	 *           Cart parameters
	 * @return Cart modification information
	 * @throws de.hybris.platform.commerceservices.order.CommerceCartModificationException
	 * 
	 */
	@Override
	public CommerceCartModification addToCart(final CommerceCartParameter parameter) throws CommerceCartModificationException
	{
		CommerceCartModification modification = null;

		try
		{
			this.beforeAddToCart(parameter);
			validateAddToCart(parameter);

			final CartModel cartModel = parameter.getCart();
			final ProductModel productModel = parameter.getProduct();
			final long quantityToAdd = parameter.getQuantity();
			final UnitModel unit = parameter.getUnit();
			final boolean forceNewEntry = parameter.isCreateNewEntry();
			final PointOfServiceModel deliveryPointOfService = parameter.getPointOfService();

			try
			{
				getProductService().getProductForCode(productModel.getCode());
			}
			catch (final UnknownIdentifierException e)
			{
				modification = new CommerceCartModification();
				modification.setStatusCode(CommerceCartModificationStatus.UNAVAILABLE);
				modification.setQuantityAdded(0);
				modification.setQuantity(quantityToAdd);
				final CartEntryModel entry = new CartEntryModel();
				entry.setProduct(productModel);
				modification.setEntry(entry);
				return modification;
			}

			UnitModel orderableUnit = unit;

			if (orderableUnit == null)
			{
				try
				{
					orderableUnit = getProductService().getOrderableUnit(productModel);
				}
				catch (final ModelNotFoundException e)
				{
					throw new CommerceCartModificationException(e.getMessage(), e);
				}
			}

			// So now work out what the maximum allowed to be added is (note that this may be negative!)
			final long actualAllowedQuantityChange = getAllowedCartAdjustmentForProduct(cartModel, productModel, quantityToAdd,
					deliveryPointOfService);
			final Integer maxOrderQuantity = productModel.getMaxOrderQuantity();
			final long cartLevel = checkCartLevel(productModel, cartModel, deliveryPointOfService);
			final long cartLevelAfterQuantityChange = actualAllowedQuantityChange + cartLevel;

			if (actualAllowedQuantityChange > 0)
			{
				// We are allowed to add items to the cart
				CartEntryModel cartEntryModel = null;

				if (deliveryPointOfService == null)
				{
					// Modify the cart
					cartEntryModel = getCartService().addNewEntry(cartModel, productModel, actualAllowedQuantityChange, orderableUnit,
							APPEND_AS_LAST, !forceNewEntry);
				}
				else
				{
					// Find the entry to modify
					final Integer entryNumber = getEntryForProductAndPointOfService(cartModel, productModel, deliveryPointOfService);

					// Modify the cart
					cartEntryModel = getCartService().addNewEntry(cartModel, productModel, actualAllowedQuantityChange, orderableUnit,
							entryNumber.intValue(), (entryNumber.intValue() < 0) ? false : !forceNewEntry);

					if (cartEntryModel != null)
					{
						cartEntryModel.setDeliveryPointOfService(deliveryPointOfService);
					}
				}

				getModelService().save(cartEntryModel);
				getCommerceCartCalculationStrategy().calculateCart(cartModel);
				getModelService().save(cartEntryModel);

				// Create the card modification result
				modification = new CommerceCartModification();
				modification.setQuantityAdded(actualAllowedQuantityChange);
				modification.setQuantity(quantityToAdd);

				if (cartEntryModel != null)
				{
					modification.setEntry(cartEntryModel);
				}

				// Are we able to add the quantity we requested?
				if (isMaxOrderQuantitySet(maxOrderQuantity) && (actualAllowedQuantityChange < quantityToAdd)
						&& (cartLevelAfterQuantityChange == maxOrderQuantity.longValue()))
				{
					modification.setStatusCode(CommerceCartModificationStatus.MAX_ORDER_QUANTITY_EXCEEDED);
				}
				else if (actualAllowedQuantityChange == quantityToAdd)
				{
					modification.setStatusCode(CommerceCartModificationStatus.SUCCESS);
				}
				else
				{
					modification.setStatusCode(CommerceCartModificationStatus.LOW_STOCK);
				}

				return modification;
			}
			else
			{
				// Not allowed to add any quantity, or maybe even asked to reduce the quantity
				// Do nothing!
				modification = new CommerceCartModification();

				if (isMaxOrderQuantitySet(maxOrderQuantity) && (cartLevelAfterQuantityChange == maxOrderQuantity.longValue()))
				{
					modification.setStatusCode(CommerceCartModificationStatus.MAX_ORDER_QUANTITY_EXCEEDED);
				}
				else
				{
					modification.setStatusCode(CommerceCartModificationStatus.NO_STOCK);
				}
				modification.setQuantityAdded(0);
				modification.setQuantity(quantityToAdd);
				final CartEntryModel entry = new CartEntryModel();
				entry.setProduct(productModel);
				entry.setDeliveryPointOfService(deliveryPointOfService);
				modification.setEntry(entry);
				return modification;
			}
		}
		finally
		{
			this.afterAddToCart(parameter, modification);
		}
	}

}
