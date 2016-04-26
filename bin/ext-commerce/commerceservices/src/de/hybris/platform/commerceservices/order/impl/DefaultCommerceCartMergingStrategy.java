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

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.order.CommerceAddToCartStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartMergingException;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.access.AccessDeniedException;


public class DefaultCommerceCartMergingStrategy extends AbstractCommerceCartStrategy implements CommerceCartMergingStrategy
{

	private UserService userService;
	private CommerceCartService commerceCartService;
	private BaseSiteService baseSiteService;
	private CommerceAddToCartStrategy commerceAddToCartStrategy;

	@Override
	public void mergeCarts(final CartModel fromCart, final CartModel toCart, final List<CommerceCartModification> modifications)
			throws CommerceCartMergingException
	{
		final UserModel currentUser = userService.getCurrentUser();

		if (currentUser == null || userService.isAnonymousUser(currentUser))
		{
			throw new AccessDeniedException("Only logged user can merge carts!");
		}

		validateParameterNotNull(fromCart, "fromCart can not be null");
		validateParameterNotNull(toCart, "toCart can not be null");

		if (!getBaseSiteService().getCurrentBaseSite().equals(fromCart.getSite()))
		{
			throw new CommerceCartMergingException(String.format("Current site %s is not equal to cart %s site %s",
					getBaseSiteService().getCurrentBaseSite(), fromCart, fromCart.getSite()));
		}

		if (!getBaseSiteService().getCurrentBaseSite().equals(toCart.getSite()))
		{
			throw new CommerceCartMergingException(String.format("Current site %s is not equal to cart %s site %s",
					getBaseSiteService().getCurrentBaseSite(), toCart, toCart.getSite()));
		}

		if (fromCart.getGuid().equals(toCart.getGuid()))
		{
			throw new CommerceCartMergingException("Cannot merge cart to itself!");
		}

		try
		{
			for (final AbstractOrderEntryModel entry : fromCart.getEntries())
			{
				final CommerceCartParameter newCartParameter = new CommerceCartParameter();
				newCartParameter.setEnableHooks(true);
				newCartParameter.setCart(toCart);
				newCartParameter.setProduct(entry.getProduct());
				newCartParameter.setPointOfService(entry.getDeliveryPointOfService());
				newCartParameter.setQuantity(entry.getQuantity() == null ? 0l : entry.getQuantity().longValue());
				newCartParameter.setUnit(entry.getUnit());
				newCartParameter.setCreateNewEntry(false);

				mergeModificationToList(getCommerceAddToCartStrategy().addToCart(newCartParameter), modifications);
			}
		}
		catch (final CommerceCartModificationException e)
		{
			throw new CommerceCartMergingException(e.getMessage(), e);
		}

		toCart.setCalculated(Boolean.FALSE);
		//TODO payment transactions - to clear or not to clear...

		getModelService().save(toCart);
		getModelService().remove(fromCart);
	}

	private void mergeModificationToList(final CommerceCartModification modificationToAdd,
			final List<CommerceCartModification> toModificationList)
	{
		if (modificationToAdd.getEntry().getPk() != null)
		{
			for (final CommerceCartModification finalModification : toModificationList)
			{
				if (finalModification.getEntry().getPk() == null)
				{
					continue;
				}
				if (finalModification.getEntry().getPk().equals(modificationToAdd.getEntry().getPk()))
				{
					finalModification.setQuantity(finalModification.getQuantity() + modificationToAdd.getQuantity());
					finalModification.setQuantityAdded(finalModification.getQuantityAdded() + modificationToAdd.getQuantityAdded());
					finalModification.setStatusCode(mergeStatusCodes(modificationToAdd.getStatusCode(),
							finalModification.getStatusCode()));
					return;
				}
			}
		}

		toModificationList.add(modificationToAdd);
	}

	private String mergeStatusCodes(final String statusCode, final String statusCode1)
	{
		if (CommerceCartModificationStatus.SUCCESS.equals(statusCode))
		{
			return statusCode1;
		}
		else
		{
			return statusCode;
		}
	}

	public UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	public CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}

	@Required
	public void setCommerceCartService(final CommerceCartService commerceCartService)
	{
		this.commerceCartService = commerceCartService;
	}

	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	public CommerceAddToCartStrategy getCommerceAddToCartStrategy()
	{
		return commerceAddToCartStrategy;
	}

	@Required
	public void setCommerceAddToCartStrategy(final CommerceAddToCartStrategy commerceAddToCartStrategy)
	{
		this.commerceAddToCartStrategy = commerceAddToCartStrategy;
	}
}
