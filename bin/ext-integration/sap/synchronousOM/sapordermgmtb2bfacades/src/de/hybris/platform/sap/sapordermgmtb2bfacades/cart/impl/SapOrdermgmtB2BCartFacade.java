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
package de.hybris.platform.sap.sapordermgmtb2bfacades.cart.impl;

import static de.hybris.platform.util.localization.Localization.getLocalizedString;

import de.hybris.platform.b2bacceleratorfacades.exception.DomainException;
import de.hybris.platform.b2bacceleratorfacades.exception.EntityValidationException;
import de.hybris.platform.b2bacceleratorfacades.order.impl.DefaultB2BCartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.CartRestorationData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commerceservices.order.CommerceCartMergingException;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.sap.core.common.exceptions.ApplicationBaseRuntimeException;
import de.hybris.platform.sap.productconfig.runtime.interf.ConfigurationProvider;
import de.hybris.platform.sap.productconfig.runtime.interf.ConfigurationProviderFactory;
import de.hybris.platform.sap.sapordermgmtb2bfacades.ProductImageHelper;
import de.hybris.platform.sap.sapordermgmtb2bfacades.cart.CartRestorationFacade;
import de.hybris.platform.sap.sapordermgmtservices.BackendAvailabilityService;
import de.hybris.platform.sap.sapordermgmtservices.cart.CartService;
import de.hybris.platform.sap.sapordermgmtservices.prodconf.ProductConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class SapOrdermgmtB2BCartFacade extends DefaultB2BCartFacade
{
	private static final Logger LOG = Logger.getLogger(SapOrdermgmtB2BCartFacade.class);
	private CartService sapCartService;
	private ProductImageHelper productImageHelper;
	private CartRestorationFacade cartRestorationFacade;
	private BackendAvailabilityService backendAvailabilityService;
	private ProductConfigurationService productConfigurationService;
	private ConfigurationProviderFactory configurationProviderFactory;
	private BaseStoreService baseStoreService;

	private static final String CART_MODIFICATION_ERROR = "basket.error.occurred";

	protected boolean isSyncOrdermgmtEnabled()
	{
		return (getBaseStoreService().getCurrentBaseStore().getSAPConfiguration() != null)
				&& (getBaseStoreService().getCurrentBaseStore().getSAPConfiguration().isSapordermgmt_enabled());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.order.CartFacade#getSessionCart()
	 */
	@Override
	public CartData getSessionCart()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.getSessionCart();
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug("getSessionCart");
		}
		if (isBackendDown())
		{
			final CartData cartWhenSessionDown = super.getSessionCart();
			cartWhenSessionDown.setBackendDown(true);
			cartWhenSessionDown.getTotalPrice().setFormattedValue("");
			return cartWhenSessionDown;
		}
		else
		{
			if (!isUserLoggedOn())
			{
				return new CartData();
			}
			final CartData sessionCart = getSapCartService().getSessionCart();
			sessionCart.setCode(super.getSessionCart().getCode());
			productImageHelper.enrichWithProductImages(sessionCart);
			return sessionCart;
		}
	}

	private boolean isUserLoggedOn()
	{
		final UserModel userModel = super.getUserService().getCurrentUser();
		return !super.getUserService().isAnonymousUser(userModel);
	}

	/**
	 * Returns the user model corresponding to the logged in user
	 *
	 * @return userModel
	 */
	protected UserModel getCurrentUser()
	{
		final UserModel userModel = super.getUserService().getCurrentUser();
		return userModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.order.CartFacade#getSessionCartWithEntryOrdering(boolean)
	 */
	@Override
	public CartData getSessionCartWithEntryOrdering(final boolean recentlyAddedFirst)
	{

		if (!isSyncOrdermgmtEnabled())
		{
			return super.getSessionCartWithEntryOrdering(recentlyAddedFirst);
		}


		if (LOG.isDebugEnabled())
		{
			LOG.debug("getSessionCartWithEntryOrdering called with: " + recentlyAddedFirst);
		}
		if (isBackendDown())
		{
			final CartData cartWhenSessionDown = super.getSessionCartWithEntryOrdering(recentlyAddedFirst);
			cartWhenSessionDown.setBackendDown(true);
			cartWhenSessionDown.getTotalPrice().setFormattedValue("");
			return cartWhenSessionDown;
		}
		else
		{
			if (!isUserLoggedOn())
			{
				return new CartData();
			}
			final CartData sessionCart = getSapCartService().getSessionCart(recentlyAddedFirst);
			sessionCart.setCode(super.getSessionCart().getCode());
			productImageHelper.enrichWithProductImages(sessionCart);
			return sessionCart;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.order.CartFacade#hasSessionCart()
	 */
	@Override
	public boolean hasSessionCart()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.hasSessionCart();
		}

		if (isBackendDown())
		{
			return super.hasSessionCart();
		}
		return getSapCartService().hasSessionCart();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.order.CartFacade#getMiniCart()
	 */
	@Override
	public CartData getMiniCart()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.getSessionCart();
		}

		return getSessionCart();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.order.CartFacade#addToCart(java.lang.String, long)
	 */
	@Override
	public CartModificationData addToCart(final String code, final long quantity) throws CommerceCartModificationException
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.addToCart(code, quantity);
		}


		if (isBackendDown())
		{
			final CartModificationData cartModificationBackendDown = super.addToCart(code, quantity);
			final OrderEntryData entry = cartModificationBackendDown.getEntry();
			entry.setBackendDown(true);
			return cartModificationBackendDown;
		}
		else
		{
			final CartModificationData cartModification = getSapCartService().addToCart(code, quantity);
			productImageHelper.enrichWithProductImages(cartModification.getEntry());

			if (this.cartRestorationFacade != null)
			{
				this.cartRestorationFacade.setSavedCart(getSapCartService().getSessionCart());
			}

			return cartModification;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.order.CartFacade#addToCart(java.lang.String, long, java.lang.String)
	 */
	@Override
	public CartModificationData addToCart(final String code, final long quantity, final String storeId)
			throws CommerceCartModificationException
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.addToCart(code, quantity, storeId);
		}

		LOG.info("addToCart called with store ID, ignoring: " + storeId);
		return this.addToCart(code, quantity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.order.CartFacade#validateCartData()
	 */
	@Override
	public List<CartModificationData> validateCartData() throws CommerceCartModificationException
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.validateCartData();
		}

		return getSapCartService().validateCartData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.order.CartFacade#updateCartEntry(long, long)
	 */
	@Override
	public CartModificationData updateCartEntry(final long entryNumber, final long quantity)
			throws CommerceCartModificationException
	{

		if (!isSyncOrdermgmtEnabled())
		{
			return super.updateCartEntry(entryNumber, quantity);
		}

		if (isBackendDown())
		{
			final String itemKey = getItemKey(entryNumber);
			checkForConfigurationRelease(quantity, itemKey);
			return super.updateCartEntry(entryNumber, quantity);
		}
		else
		{
			final CartModificationData cartModification = getSapCartService().updateCartEntry(entryNumber, quantity);

			if (this.cartRestorationFacade != null)
			{
				this.cartRestorationFacade.setSavedCart(getSapCartService().getSessionCart());
			}

			return cartModification;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.order.CartFacade#updateCartEntry(long, java.lang.String)
	 */
	@Override
	public CartModificationData updateCartEntry(final long entryNumber, final String storeId)
			throws CommerceCartModificationException
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.updateCartEntry(entryNumber, storeId);
		}

		throw new ApplicationBaseRuntimeException("Not supported: updateCartEntry(final long entryNumber, final String storeId)");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.order.CartFacade#restoreSavedCart(java.lang.String)
	 */
	@Override
	public CartRestorationData restoreSavedCart(final String code) throws CommerceCartRestorationException
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.restoreSavedCart(code);
		}

		if (isBackendDown())
		{
			return super.restoreSavedCart(code);
		}
		else
		{
			if (this.cartRestorationFacade != null)
			{
				final CartRestorationData hybrisCart = this.cartRestorationFacade.restoreSavedCart(code, this.getCurrentUser());

				return hybrisCart;

			}
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.order.CartFacade#getDeliveryCountries()
	 */
	@Override
	public List<CountryData> getDeliveryCountries()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.getDeliveryCountries();
		}

		//No delivery countries available, only choosing from existing addresses supported
		return Collections.emptyList();
		//return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.order.CartFacade#estimateExternalTaxes(java.lang.String, java.lang.String)
	 */
	@Override
	public CartData estimateExternalTaxes(final String deliveryZipCode, final String countryIsoCode)
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.estimateExternalTaxes(deliveryZipCode, countryIsoCode);
		}

		//We cannot support this, as the delivery costs are based on the ship-to party address in the ERP case
		throw new ApplicationBaseRuntimeException("Not supported: estimateExternalTaxes");

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.order.CartFacade#removeStaleCarts()
	 */
	@Override
	public void removeStaleCarts()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			super.removeStaleCarts();

			return;
		}

		//No stale carts in this scenario

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.order.CartFacade#restoreAnonymousCartAndTakeOwnership(java.lang.String)
	 */
	@Override
	public CartRestorationData restoreAnonymousCartAndTakeOwnership(final String guid) throws CommerceCartRestorationException
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.restoreAnonymousCartAndTakeOwnership(guid);
		}

		//No anonymous carts in our scenario
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.order.CartFacade#removeSessionCart()
	 */
	@Override
	public void removeSessionCart()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			super.removeSessionCart();

			return;
		}

		if (this.cartRestorationFacade != null)
		{
			this.cartRestorationFacade.removeSavedCart();
		}

		getSapCartService().removeSessionCart();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.order.CartFacade#getCartsForCurrentUser()
	 */
	@Override
	public List<CartData> getCartsForCurrentUser()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.getCartsForCurrentUser();

		}

		return Arrays.asList(getSessionCart());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.order.CartFacade#mergeCarts(de.hybris.platform.core.model.order.CartModel,
	 * de.hybris.platform.core.model.order.CartModel, java.util.List)
	 */
	@Override
	public void mergeCarts(final CartModel fromCart, final CartModel toCart, final List<CommerceCartModification> modifications)
			throws CommerceCartMergingException
	{
		if (!isSyncOrdermgmtEnabled())
		{
			super.mergeCarts(fromCart, toCart, modifications);

			return;

		}
		throw new ApplicationBaseRuntimeException("mergeCarts not supported");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.order.CartFacade#restoreAnonymousCartAndMerge(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public CartRestorationData restoreAnonymousCartAndMerge(final String fromAnonumousCartGuid, final String toUserCartGuid)
			throws CommerceCartMergingException, CommerceCartRestorationException
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.restoreAnonymousCartAndMerge(fromAnonumousCartGuid, toUserCartGuid);

		}

		throw new ApplicationBaseRuntimeException("restoreAnonymousCartAndMerge not supported");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.order.CartFacade#restoreCartAndMerge(java.lang.String, java.lang.String)
	 */
	@Override
	public CartRestorationData restoreCartAndMerge(final String fromUserCartGuid, final String toUserCartGuid)
			throws CommerceCartRestorationException, CommerceCartMergingException
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.restoreCartAndMerge(fromUserCartGuid, toUserCartGuid);

		}

		throw new ApplicationBaseRuntimeException("restoreCartAndMerge not supported");
	}

	@Override
	public CartModificationData addOrderEntry(final OrderEntryData cartEntry)
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.addOrderEntry(cartEntry);

		}

		CartModificationData cartModification = null;
		try
		{
			cartModification = addToCart(cartEntry.getProduct().getCode(), cartEntry.getQuantity().longValue());
		}
		catch (final CommerceCartModificationException e)
		{
			throw new DomainException(getLocalizedString(CART_MODIFICATION_ERROR), e);
		}

		return cartModification;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.b2bacceleratorfacades.api.cart.CartFacade#updateOrderEntry(de.hybris.platform.commercefacades
	 * .order.data.OrderEntryData)
	 */
	@Override
	public CartModificationData updateOrderEntry(final OrderEntryData cartEntry) throws EntityValidationException
	{

		if (!isSyncOrdermgmtEnabled())
		{
			return super.updateOrderEntry(cartEntry);

		}

		final long entryNumber = cartEntry.getEntryNumber().longValue();
		final long quantity = cartEntry.getQuantity().longValue();
		if (isBackendDown())
		{
			try
			{
				final String itemKey = getItemKey(entryNumber);
				checkForConfigurationRelease(quantity, itemKey);
				return super.updateCartEntry(entryNumber, quantity);
			}
			catch (final CommerceCartModificationException e)
			{
				throw new EntityValidationException("UpdateOrderEntry failed", e);
			}
		}
		else
		{

			final CartModificationData cartModificationData = getSapCartService().updateCartEntry(entryNumber, quantity);
			if (getCartRestorationFacade() != null)
			{
				try
				{
					getCartRestorationFacade().setSavedCart(getSessionCart());
				}
				catch (final CommerceCartModificationException e)
				{
					throw new EntityValidationException("UpdateOrderEntry failed", e);
				}
			}
			return cartModificationData;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.b2bacceleratorfacades.api.cart.CartFacade#addOrderEntryList(java.util.List)
	 */
	@Override
	public List<CartModificationData> addOrderEntryList(final List<OrderEntryData> cartEntries)
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.addOrderEntryList(cartEntries);

		}

		throw new ApplicationBaseRuntimeException("addOrderEntryList not supported");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.b2bacceleratorfacades.api.cart.CartFacade#updateOrderEntryList(java.util.List)
	 */
	@Override
	public List<CartModificationData> updateOrderEntryList(final List<OrderEntryData> cartEntries)
	{

		if (!isSyncOrdermgmtEnabled())
		{
			return super.updateOrderEntryList(cartEntries);

		}

		throw new ApplicationBaseRuntimeException("updateOrderEntryList not supported");
	}

	public ProductImageHelper getProductImageHelper()
	{
		return productImageHelper;
	}

	public void setProductImageHelper(final ProductImageHelper productImageHelper)
	{
		this.productImageHelper = productImageHelper;
	}

	@Override
	public void setUserService(final UserService userService)
	{
		super.setUserService(userService);
	}

	/**
	 * @return the cartRestorationFacade
	 */
	public CartRestorationFacade getCartRestorationFacade()
	{
		return cartRestorationFacade;
	}


	/**
	 * @param cartRestorationFacade
	 *           the cartRestorationFacade to set
	 */
	public void setCartRestorationFacade(final CartRestorationFacade cartRestorationFacade)
	{
		this.cartRestorationFacade = cartRestorationFacade;
	}

	/**
	 * @return Is Backend down?
	 */
	public boolean isBackendDown()
	{
		return backendAvailabilityService.isBackendDown();
	}


	/**
	 * @return the backendAvailabilityService
	 */
	protected BackendAvailabilityService getBackendAvailabilityService()
	{
		return backendAvailabilityService;
	}


	/**
	 * @param backendAvailabilityService
	 *           the backendAvailabilityService to set
	 */
	public void setBackendAvailabilityService(final BackendAvailabilityService backendAvailabilityService)
	{
		this.backendAvailabilityService = backendAvailabilityService;
	}

	@Override
	public boolean hasEntries()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.hasEntries();

		}

		if (isBackendDown())
		{
			return super.hasEntries();
		}
		else
		{
			boolean hasEntries = false;
			final CartData sessionCart = getSapCartService().getSessionCart();
			if (sessionCart != null && sessionCart.getEntries() != null)
			{
				hasEntries = sessionCart.getEntries().size() > 0;
			}
			return hasEntries;
		}
	}


	/**
	 * @return the productConfigurationService
	 */
	public ProductConfigurationService getProductConfigurationService()
	{
		return productConfigurationService;
	}


	/**
	 * @param productConfigurationService
	 *           the productConfigurationService to set
	 */
	public void setProductConfigurationService(final ProductConfigurationService productConfigurationService)
	{
		this.productConfigurationService = productConfigurationService;
	}


	public void checkForConfigurationRelease(final long quantity, final String itemKey)
	{
		if (quantity == 0)
		{
			final String configId = getProductConfigurationService().getGetConfigId(itemKey);
			if (configId != null)
			{
				getConfigurationProvider().releaseSession(configId);
			}
		}
	}



	ConfigurationProvider getConfigurationProvider()
	{
		return getConfigurationProviderFactory().getProvider();
	}


	String getItemKey(final long entryNumber)
	{
		final List<OrderEntryData> entries = getSessionCart().getEntries();
		for (final OrderEntryData entry : entries)
		{
			if (entry.getEntryNumber().longValue() == entryNumber)
			{
				return entry.getHandle();
			}
		}
		return null;
	}


	/**
	 * @return the configurationProviderFactory
	 */
	public ConfigurationProviderFactory getConfigurationProviderFactory()
	{
		return configurationProviderFactory;
	}


	/**
	 * @param configurationProviderFactory
	 *           the configurationProviderFactory to set
	 */
	public void setConfigurationProviderFactory(final ConfigurationProviderFactory configurationProviderFactory)
	{
		this.configurationProviderFactory = configurationProviderFactory;
	}

	/**
	 * Updates the information in the cart based on the content of the cartData
	 *
	 * @param cartData
	 *           the cart to modify and it's modifications.
	 * @return the updated cart.
	 */
	@Override
	public CartData update(final CartData cartData)
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.update(cartData);

		}

		throw new ApplicationBaseRuntimeException("not supported");

	}


	/**
	 * This gets the current cart.
	 *
	 * @return the current cart.
	 */
	@Override
	public CartData getCurrentCart()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.getSessionCart();

		}
		return getSessionCart();

	}


	/**
	 * @return the sapCartService
	 */
	public CartService getSapCartService()
	{
		return sapCartService;
	}


	/**
	 * @param sapCartService
	 *           the sapCartService to set
	 */
	@Required
	public void setSapCartService(final CartService sapCartService)
	{
		this.sapCartService = sapCartService;
	}

	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}




}
