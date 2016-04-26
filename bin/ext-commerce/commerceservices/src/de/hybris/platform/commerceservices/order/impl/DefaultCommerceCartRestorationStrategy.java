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

import de.hybris.platform.commerceservices.order.*;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartFactory;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.site.BaseSiteService;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class DefaultCommerceCartRestorationStrategy extends AbstractCommerceCartStrategy implements
        CommerceCartRestorationStrategy
{
	private static final Logger LOG = Logger.getLogger(DefaultCommerceCartRestorationStrategy.class);
    private static final int DEFAULT_CART_VALIDITY_PERIOD = 12960000;
    private int cartValidityPeriod = DEFAULT_CART_VALIDITY_PERIOD;
    private CartFactory cartFactory;
    private TimeService timeService;
    private KeyGenerator guidKeyGenerator;
    private BaseSiteService baseSiteService;
    private CommerceAddToCartStrategy commerceAddToCartStrategy;

	@Override
	public CommerceCartRestoration restoreCart(final CommerceCartParameter parameter) throws CommerceCartRestorationException
	{
		final CartModel cartModel = parameter.getCart();
		final CommerceCartRestoration restoration = new CommerceCartRestoration();
		final List<CommerceCartModification> modifications = new ArrayList<>();
		if (cartModel != null)
		{
			if (getBaseSiteService().getCurrentBaseSite().equals(cartModel.getSite()))
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Restoring from cart " + cartModel.getCode() + ".");
				}
				if (isCartInValidityPeriod(cartModel))
				{
					cartModel.setCalculated(Boolean.FALSE);
					if (!cartModel.getPaymentTransactions().isEmpty())
					{
						// clear payment transactions
						clearPaymentTransactionsOnCart(cartModel);
						// reset guid since its used as a merchantId for payment subscriptions and is a base id for generating PaymentTransaction.code
						// see de.hybris.platform.payment.impl.DefaultPaymentServiceImpl.authorize(DefaultPaymentServiceImpl.java:177)
						cartModel.setGuid(getGuidKeyGenerator().generate().toString());
					}

					getModelService().save(cartModel);
					try
					{
						getCommerceCartCalculationStrategy().recalculateCart(parameter);
					}
					catch (final IllegalStateException ex)
					{
						LOG.error("Failed to recalculate order [" + cartModel.getCode() + "]", ex);
					}

					getCartService().setSessionCart(cartModel);

					if (LOG.isDebugEnabled())
					{
						LOG.debug("Cart " + cartModel.getCode() + " was found to be valid and was restored to the session.");
					}
				}
				else
				{
					try
					{
						modifications.addAll(rebuildSessionCart(parameter));
					}
					catch (final CommerceCartModificationException e)
					{
						throw new CommerceCartRestorationException(e.getMessage(), e);
					}
				}
			}
			else
			{
				LOG.warn(String.format("Current Site %s does not equal to cart %s Site %s",
						getBaseSiteService().getCurrentBaseSite(), cartModel, cartModel.getSite()));
			}
		}
		restoration.setModifications(modifications);
		return restoration;
	}

    protected void rewriteEntriesFromCartToCart(final CommerceCartParameter parameter, final CartModel fromCartModel, final CartModel toCartModel,
                                                final List<CommerceCartModification> modifications)
            throws CommerceCartModificationException
    {

        for (final AbstractOrderEntryModel entry : fromCartModel.getEntries())
        {
            final CommerceCartParameter newCartParameter = new CommerceCartParameter();
            parameter.setEnableHooks(true);
            newCartParameter.setCart(toCartModel);
            newCartParameter.setProduct(entry.getProduct());
            newCartParameter.setPointOfService(entry.getDeliveryPointOfService());
            newCartParameter.setQuantity(entry.getQuantity() == null ? 0l : entry.getQuantity().longValue());
            newCartParameter.setUnit(entry.getUnit());
            newCartParameter.setCreateNewEntry(false);

            CommerceCartModification modification = getCommerceAddToCartStrategy().addToCart(newCartParameter);

            if (modification.getQuantityAdded() == 0)
            {
                parameter.setPointOfService(null);
                // add to cart without pointOfService
                modification = getCommerceAddToCartStrategy().addToCart(newCartParameter);
                modification.setDeliveryModeChanged(Boolean.TRUE);
            }

            modifications.add(modification);
        }
    }

    protected Collection<CommerceCartModification> rebuildSessionCart(final CommerceCartParameter parameter)
            throws CommerceCartModificationException
    {
        final List<CommerceCartModification> modifications = new ArrayList<>();
        final CartModel newCart = getCartFactory().createCart();

        if (!parameter.getCart().equals(newCart))
        {
            rewriteEntriesFromCartToCart(parameter, parameter.getCart(), newCart, modifications);

            newCart.setCalculated(Boolean.FALSE);
            getCartService().setSessionCart(newCart);
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Cart " + parameter.getCart().getCode()
                        + " was found and was invalid. A new cart was created and populated.");
            }
            getCommerceCartCalculationStrategy().calculateCart(parameter);
            getModelService().remove(parameter.getCart());
        }
        return modifications;
    }

    protected boolean isCartInValidityPeriod(final CartModel cartModel)
    {
        return new DateTime(cartModel.getModifiedtime()).isAfter(new DateTime(getTimeService().getCurrentTime())
                .minusSeconds(getCartValidityPeriod()));
    }

    protected void clearPaymentTransactionsOnCart(final CartModel cartModel)
    {
        getModelService().removeAll(cartModel.getPaymentTransactions());
        cartModel.setPaymentTransactions(Collections.EMPTY_LIST);
    }

    protected int getCartValidityPeriod()
    {
        return cartValidityPeriod;
    }

    // Optional injection point
    public void setCartValidityPeriod(final int cartValidityPeriod)
    {
        this.cartValidityPeriod = cartValidityPeriod;
    }

    protected CartFactory getCartFactory()
    {
        return cartFactory;
    }

    @Required
    public void setCartFactory(final CartFactory cartFactory)
    {
        this.cartFactory = cartFactory;
    }

    protected TimeService getTimeService()
    {
        return timeService;
    }

    @Required
    public void setTimeService(final TimeService timeService)
    {
        this.timeService = timeService;
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

    protected KeyGenerator getGuidKeyGenerator()
    {
        return guidKeyGenerator;
    }

    @Required
    public void setGuidKeyGenerator(final KeyGenerator guidKeyGenerator)
    {
        this.guidKeyGenerator = guidKeyGenerator;
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
}
