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

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.order.*;
import de.hybris.platform.commerceservices.order.dao.CommerceCartDao;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderParameter;
import de.hybris.platform.commerceservices.service.data.CommerceTaxEstimateResult;
import de.hybris.platform.commerceservices.strategies.CartValidationStrategy;
import de.hybris.platform.commerceservices.strategies.StaleCartRemovalStrategy;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Default implementation of {@link CommerceCartService}
 */
public class DefaultCommerceCartService implements CommerceCartService
{

	protected static final String ESTIMATED_TAXES = "ESTIMATED_TAXES";

	private ModelService modelService;
	private BaseSiteService baseSiteService;
	private CommerceCartDao commerceCartDao;
	private SessionService sessionService;
	private CartValidationStrategy cartValidationStrategy;
	private CommerceCartCalculationStrategy commerceCartCalculationStrategy;
	private CommerceCartEstimateTaxesStrategy commerceCartEstimateTaxesStrategy;
	private CommerceCartHashCalculationStrategy commerceCartHashCalculationStrategy;
	private StaleCartRemovalStrategy staleCartRemovalStrategy;
	private CommerceAddToCartStrategy commerceAddToCartStrategy;
	private CommerceUpdateCartEntryStrategy commerceUpdateCartEntryStrategy;
	private CommerceCartRestorationStrategy commerceCartRestorationStrategy;
	private CommerceCartSplitStrategy commerceCartSplitStrategy;
	private CommerceRemoveEntriesStrategy commerceRemoveEntriesStrategy;
    private CommerceCartMergingStrategy commerceCartMergingStrategy;

    @Override
	@Deprecated
	public CommerceCartModification addToCart(final CartModel cartModel, final ProductModel productModel,
			final long quantityToAdd, final UnitModel unit, final boolean forceNewEntry) throws CommerceCartModificationException
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setProduct(productModel);
		parameter.setQuantity(quantityToAdd);
		parameter.setUnit(unit);
		parameter.setCreateNewEntry(forceNewEntry);

		return this.addToCart(parameter);
	}

	@Override
	@Deprecated
	public CommerceCartModification addToCart(final CartModel cartModel, final ProductModel productModel,
			final PointOfServiceModel deliveryPointOfService, final long quantity, final UnitModel unit, final boolean forceNewEntry)
			throws CommerceCartModificationException
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setProduct(productModel);
		parameter.setPointOfService(deliveryPointOfService);
		parameter.setQuantity(quantity);
		parameter.setUnit(unit);
		parameter.setCreateNewEntry(forceNewEntry);

		return this.addToCart(parameter);
	}

	@Override
	public CommerceCartModification addToCart(final CommerceCartParameter parameter) throws CommerceCartModificationException
	{
		return this.getCommerceAddToCartStrategy().addToCart(parameter);
	}

	@Override
	public List<CommerceCartModification> validateCart(final CommerceCartParameter parameter)
			throws CommerceCartModificationException
	{
		final CartModel cartModel = parameter.getCart();
		validateParameterNotNull(cartModel, "Cart model cannot be null");

		final List<CommerceCartModification> modifications = getCartValidationStrategy().validateCart(parameter);

		// We only care about modifications that weren't successful
		final List<CommerceCartModification> errorModifications = new ArrayList<CommerceCartModification>(modifications.size());
		for (final CommerceCartModification modification : modifications)
		{
			if (!CommerceCartModificationStatus.SUCCESS.equals(modification.getStatusCode()))
			{
				errorModifications.add(modification);
			}
		}
		calculateCart(parameter);

		return errorModifications;
	}

	@Override
	@Deprecated
	public List<CommerceCartModification> validateCart(final CartModel cartModel) throws CommerceCartModificationException
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		return this.validateCart(parameter);
	}

	@Override
	@Deprecated
	public boolean calculateCart(final CartModel cartModel)
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		return this.calculateCart(parameter);
	}

	@Override
	public boolean calculateCart(final CommerceCartParameter parameters)
	{
		validateParameterNotNull(parameters.getCart(), "Cart model cannot be null");
		return getCommerceCartCalculationStrategy().calculateCart(parameters);
	}

	@Override
	@Deprecated
	public void recalculateCart(final CartModel cartModel) throws CalculationException
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		this.recalculateCart(parameter);
	}

	@Override
	public void recalculateCart(final CommerceCartParameter parameters)
	{
		validateParameterNotNull(parameters.getCart(), "Cart model cannot be null");
		getCommerceCartCalculationStrategy().recalculateCart(parameters);
	}

	@Override
	@Deprecated
	public void removeAllEntries(final CartModel cartModel)
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		this.removeAllEntries(parameter);
	}

	@Override
	public void removeAllEntries(final CommerceCartParameter parameter)
	{
		getCommerceRemoveEntriesStrategy().removeAllEntries(parameter);
		calculateCart(parameter);
	}

	@Override
	@Deprecated
	public CommerceCartModification updateQuantityForCartEntry(final CartModel cartModel, final long entryNumber,
			final long newQuantity) throws CommerceCartModificationException
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setEntryNumber(entryNumber);
		parameter.setQuantity(newQuantity);

		return this.updateQuantityForCartEntry(parameter);
	}

	@Override
	public CommerceCartModification updateQuantityForCartEntry(final CommerceCartParameter parameters)
			throws CommerceCartModificationException
	{
		return this.getCommerceUpdateCartEntryStrategy().updateQuantityForCartEntry(parameters);
	}

	@Override
	@Deprecated
	public CommerceCartModification updatePointOfServiceForCartEntry(final CartModel cartModel, final long entryNumber,
			final PointOfServiceModel pointOfServiceModel) throws CommerceCartModificationException
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setEntryNumber(entryNumber);
		parameter.setPointOfService(pointOfServiceModel);

		return this.updatePointOfServiceForCartEntry(parameter);
	}

	@Override
	public CommerceCartModification updatePointOfServiceForCartEntry(final CommerceCartParameter parameters)
			throws CommerceCartModificationException
	{
		return this.getCommerceUpdateCartEntryStrategy().updatePointOfServiceForCartEntry(parameters);
	}

	@Override
	@Deprecated
	public CommerceCartModification updateToShippingModeForCartEntry(final CartModel cartModel, final long entryNumber)
			throws CommerceCartModificationException
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setEntryNumber(entryNumber);
		return this.updateToShippingModeForCartEntry(parameter);
	}

	@Override
	public CommerceCartModification updateToShippingModeForCartEntry(final CommerceCartParameter parameters)
			throws CommerceCartModificationException
	{
		return this.getCommerceUpdateCartEntryStrategy().updateToShippingModeForCartEntry(parameters);
	}

	@Override
	@Deprecated
	public long split(final CartModel cartModel, final long entryNumber) throws CommerceCartModificationException
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setEntryNumber(entryNumber);
		return this.split(parameter);
	}

	@Override
	public long split(final CommerceCartParameter parameters) throws CommerceCartModificationException
	{
		return this.getCommerceCartSplitStrategy().split(parameters);
	}

	@Override
	@Deprecated
	public CommerceCartRestoration restoreCart(final CartModel cartModel) throws CommerceCartRestorationException
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		return this.restoreCart(parameter);
	}

	@Override
	public CommerceCartRestoration restoreCart(final CommerceCartParameter parameters) throws CommerceCartRestorationException
	{
		return getCommerceCartRestorationStrategy().restoreCart(parameters);
	}

	@Override
	public void removeStaleCarts(final CartModel currentCart, final BaseSiteModel baseSite, final UserModel user)
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(currentCart);
		parameter.setBaseSite(baseSite);
		parameter.setUser(user);
		this.removeStaleCarts(parameter);
	}

	@Override
	public void removeStaleCarts(final CommerceCartParameter parameters)
	{
		getStaleCartRemovalStrategy().removeStaleCarts(parameters);
	}

    @Override
    public void mergeCarts(final CartModel fromCart, final CartModel toCart, final List<CommerceCartModification> modifications)
            throws CommerceCartMergingException {
        this.getCommerceCartMergingStrategy().mergeCarts(fromCart, toCart, modifications);
    }


    @Override
	public CartModel getCartForGuidAndSiteAndUser(final String guid, final BaseSiteModel site, final UserModel user)
	{
		validateParameterNotNull(site, "site cannot be null");
		validateParameterNotNull(user, "user cannot be null");

		return getCommerceCartDao().getCartForGuidAndSiteAndUser(guid, site, user);
	}

	@Override
	public CartModel getCartForGuidAndSite(final String guid, final BaseSiteModel site)
	{
		validateParameterNotNull(guid, "guid cannot be null");
		validateParameterNotNull(site, "site cannot be null");

		return getCommerceCartDao().getCartForGuidAndSite(guid, site);
	}

	@Override
	public CartModel getCartForCodeAndUser(final String code, final UserModel user)
	{
		validateParameterNotNull(code, "code cannot be null");
		validateParameterNotNull(user, "user cannot be null");
		return getCommerceCartDao().getCartForCodeAndUser(code, user);
	}

	@Override
	public List<CartModel> getCartsForSiteAndUser(final BaseSiteModel site, final UserModel user)
	{
		validateParameterNotNull(site, "site cannot be null");
		validateParameterNotNull(user, "user cannot be null");
		return getCommerceCartDao().getCartsForSiteAndUser(site, user);
	}
	
	@Override
	public BigDecimal estimateTaxes(final CartModel cartModel, final String deliveryZipCode, final String deliveryCountryIso)
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setDeliveryZipCode(deliveryZipCode);
		parameter.setDeliveryCountryIso(deliveryCountryIso);
		return this.estimateTaxes(parameter).getTax();
	}

	@Override
	public CommerceTaxEstimateResult estimateTaxes(final CommerceCartParameter parameters)
	{
		final CartModel cartModel = parameters.getCart();
		final String deliveryZipCode = parameters.getDeliveryZipCode();
		final String deliveryCountryIso = parameters.getDeliveryCountryIso();

		//try to get the cached estimated tax if the hash of the cart is still the same as it was when calculated
		final HashAndTaxEstimate hashAndEstimatedTax = getSessionService().getAttribute(ESTIMATED_TAXES);

		final CommerceOrderParameter parameter = new CommerceOrderParameter();
		parameter.setOrder(cartModel);
		parameter.setAdditionalValues(Arrays.asList(deliveryZipCode, deliveryCountryIso));
		final CommerceTaxEstimateResult commerceTaxEstimateResult = new CommerceTaxEstimateResult();

		final String cartHash = getCommerceCartHashCalculationStrategy().buildHashForAbstractOrder(parameter);
		if (hashAndEstimatedTax != null && hashAndEstimatedTax.getHash().equals(cartHash))
		{
			commerceTaxEstimateResult.setTax(hashAndEstimatedTax.getTaxEstimate());
			return commerceTaxEstimateResult;
		}
		else
		{
			final BigDecimal tax = getCommerceCartEstimateTaxesStrategy().estimateTaxes(cartModel, deliveryZipCode,
					deliveryCountryIso);
			//cache the new calculated value
			getSessionService().setAttribute(ESTIMATED_TAXES, new HashAndTaxEstimate(cartHash, tax));
			commerceTaxEstimateResult.setTax(tax);
			return commerceTaxEstimateResult;
		}
	}

	/**
	 * @deprecated use {@link CommerceCartCalculationStrategy} before calculation method hooks
	 */
	@Deprecated
	protected void beforeCalculate(final CartModel cartModel, final boolean recalculate)
	{
		// Empty method - extension point
	}

	/**
	 * @deprecated use {@link CommerceCartCalculationStrategy} before calculation method hooks
	 */
	@Deprecated
	protected void afterCalculate(final CartModel cartModel, final boolean recalculate)
	{
		// Empty method - extension point
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

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	protected CommerceCartDao getCommerceCartDao()
	{
		return commerceCartDao;
	}

	@Required
	public void setCommerceCartDao(final CommerceCartDao commerceCartDao)
	{
		this.commerceCartDao = commerceCartDao;
	}

	protected CartValidationStrategy getCartValidationStrategy()
	{
		return cartValidationStrategy;
	}

	@Required
	public void setCartValidationStrategy(final CartValidationStrategy cartValidationStrategy)
	{
		this.cartValidationStrategy = cartValidationStrategy;
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

	static class HashAndTaxEstimate
	{
		private String hash;
		private BigDecimal taxEstimate;

		public HashAndTaxEstimate(final String hash, final BigDecimal taxEstimate)
		{
			this.hash = hash;
			this.taxEstimate = taxEstimate;
		}

		public String getHash()
		{
			return hash;
		}

		public void setHash(final String hash)
		{
			this.hash = hash;
		}

		public BigDecimal getTaxEstimate()
		{
			return taxEstimate;
		}

		public void setTaxEstimate(final BigDecimal taxEstimate)
		{
			this.taxEstimate = taxEstimate;
		}

	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	protected StaleCartRemovalStrategy getStaleCartRemovalStrategy()
	{
		return staleCartRemovalStrategy;
	}

	@Required
	public void setStaleCartRemovalStrategy(final StaleCartRemovalStrategy staleCartRemovalStrategy)
	{
		this.staleCartRemovalStrategy = staleCartRemovalStrategy;
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

	protected CommerceUpdateCartEntryStrategy getCommerceUpdateCartEntryStrategy()
	{
		return commerceUpdateCartEntryStrategy;
	}

	@Required
	public void setCommerceUpdateCartEntryStrategy(final CommerceUpdateCartEntryStrategy commerceUpdateCartEntryStrategy)
	{
		this.commerceUpdateCartEntryStrategy = commerceUpdateCartEntryStrategy;
	}

	protected CommerceCartHashCalculationStrategy getCommerceCartHashCalculationStrategy()
	{
		return commerceCartHashCalculationStrategy;
	}

	@Required
	public void setCommerceCartHashCalculationStrategy(
			final CommerceCartHashCalculationStrategy commerceCartHashCalculationStrategy)
	{
		this.commerceCartHashCalculationStrategy = commerceCartHashCalculationStrategy;
	}

	protected CommerceCartEstimateTaxesStrategy getCommerceCartEstimateTaxesStrategy()
	{
		return commerceCartEstimateTaxesStrategy;
	}

	@Required
	public void setCommerceCartEstimateTaxesStrategy(final CommerceCartEstimateTaxesStrategy commerceCartEstimateTaxesStrategy)
	{
		this.commerceCartEstimateTaxesStrategy = commerceCartEstimateTaxesStrategy;
	}

	protected CommerceCartRestorationStrategy getCommerceCartRestorationStrategy()
	{
		return commerceCartRestorationStrategy;
	}

	@Required
	public void setCommerceCartRestorationStrategy(final CommerceCartRestorationStrategy commerceCartRestorationStrategy)
	{
		this.commerceCartRestorationStrategy = commerceCartRestorationStrategy;
	}

    protected CommerceCartMergingStrategy getCommerceCartMergingStrategy()
    {
        return commerceCartMergingStrategy;
    }

    @Required
    public void setCommerceCartMergingStrategy(final CommerceCartMergingStrategy commerceCartMergingStrategy)
    {
        this.commerceCartMergingStrategy = commerceCartMergingStrategy;
    }

	protected CommerceCartSplitStrategy getCommerceCartSplitStrategy()
	{
		return commerceCartSplitStrategy;
	}

	@Required
	public void setCommerceCartSplitStrategy(final CommerceCartSplitStrategy commerceCartSplitStrategy)
	{
		this.commerceCartSplitStrategy = commerceCartSplitStrategy;
	}

	protected CommerceRemoveEntriesStrategy getCommerceRemoveEntriesStrategy()
	{
		return commerceRemoveEntriesStrategy;
	}

	@Required
	public void setCommerceRemoveEntriesStrategy(final CommerceRemoveEntriesStrategy commerceRemoveEntriesStrategy)
	{
		this.commerceRemoveEntriesStrategy = commerceRemoveEntriesStrategy;
	}
}
