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
package de.hybris.platform.financialfacades.order.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.order.CommerceAddToCartStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartMergingException;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.order.impl.AbstractCommerceCartStrategy;
import de.hybris.platform.commerceservices.order.impl.CommerceCartMergingStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.access.AccessDeniedException;


/**
 * This is a drop-in replacement for the standard commerce cart merging strategy and has taken the code from the current
 * hybris 5.5 release version and made a simple modification to allow the developer to chose whether the merge should
 * actually take place, or whether the cart manipulation (when handling the change from an anonymous cart to a
 * previously saved cart for a user) should simply override the new logic. This is because prior to Hybris 5.3, the
 * anonymous cart would overwrite the infomation in the user's previously saved cart when logging in. The Financial
 * Services accelerator needs this behaviour to remain in place (and others may also depend on this behaviour). Ideally,
 * this approach will be adopted in the future and this code can be removed from this extension and merely configure the
 * correct component. At the very least, it needs tidying up before actual release so that we are using more protected
 * methods so that we can override / extend as necessary TODO: remove this code / amend as is necessary before release.
 */
public class ReplaceCartMergingStrategy extends AbstractCommerceCartStrategy implements CommerceCartMergingStrategy
{

	public enum MergeAction
	{
		MERGE, OVERWRITE
	}

	private UserService userService;
	private CommerceCartService commerceCartService;
	private BaseSiteService baseSiteService;
	private CommerceAddToCartStrategy commerceAddToCartStrategy;
	private MergeAction mergeAction;

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

		if (getMergeAction().equals(MergeAction.MERGE))
		{
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
		}
		finaliseFromCart(fromCart);
	}

	/**
	 * After cart merge, the system should handle the fromCart, if the mergeAction is OVERWRITE, it check if the cart
	 * that was retrieved by system, from the Customer's cart list, is saved explicitly or it's and abandon cart that can
	 * be discard.
	 */
	protected void finaliseFromCart(final CartModel fromCart)
	{
		boolean removeFromCart = Boolean.TRUE;
		if (getMergeAction().equals(MergeAction.OVERWRITE))
		{
			// SaveTime is used for determine if the cart has been explicitly saved as at 23/12/14.
			if (fromCart.getSaveTime() != null)
			{
				removeFromCart = Boolean.FALSE;
			}
		}
		if (removeFromCart)
		{
			getModelService().remove(fromCart);
		}
	}

	protected void mergeModificationToList(final CommerceCartModification modificationToAdd,
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

	protected String mergeStatusCodes(final String statusCode, final String statusCode1)
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

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}

	@Required
	public void setCommerceCartService(final CommerceCartService commerceCartService)
	{
		this.commerceCartService = commerceCartService;
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

	protected CommerceAddToCartStrategy getCommerceAddToCartStrategy()
	{
		return commerceAddToCartStrategy;
	}

	@Required
	public void setCommerceAddToCartStrategy(final CommerceAddToCartStrategy commerceAddToCartStrategy)
	{
		this.commerceAddToCartStrategy = commerceAddToCartStrategy;
	}

	protected MergeAction getMergeAction()
	{
		return mergeAction;
	}

	@Required
	public void setMergeAction(final MergeAction mergeAction)
	{
		this.mergeAction = mergeAction;
	}
}