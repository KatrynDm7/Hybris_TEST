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
package de.hybris.platform.sap.sapordermgmtb2bfacades.checkout.impl;

import de.hybris.platform.b2bacceleratorfacades.checkout.data.PlaceOrderData;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BDaysOfWeekData;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BPaymentTypeData;
import de.hybris.platform.b2bacceleratorfacades.order.data.ScheduledCartData;
import de.hybris.platform.b2bacceleratorfacades.order.data.TriggerData;
import de.hybris.platform.b2bacceleratorfacades.order.impl.DefaultB2BCheckoutFacade;
import de.hybris.platform.b2bacceleratorservices.enums.CheckoutPaymentType;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CardTypeData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.sap.core.common.exceptions.ApplicationBaseRuntimeException;
import de.hybris.platform.sap.sapcreditcheck.facades.impl.SapCreditCheckB2BCheckoutFacade;
import de.hybris.platform.sap.sapordermgmtb2bfacades.ProductImageHelper;
import de.hybris.platform.sap.sapordermgmtservices.BackendAvailabilityService;
import de.hybris.platform.sap.sapordermgmtservices.cart.CartService;
import de.hybris.platform.sap.sapordermgmtservices.checkout.CheckoutService;
import de.hybris.platform.sap.sapordermgmtservices.partner.SapPartnerService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;


/**
 *
 */
public class SapOrdermgmtB2BCheckoutFacade extends SapCreditCheckB2BCheckoutFacade
{

	private static final String MSG_NOT_SUPPORTED = "Not supported in the context of SAP order management";

	private static final Logger LOG = Logger.getLogger(DefaultB2BCheckoutFacade.class);

	private CartService sapCartService;
	private CheckoutService checkoutService;
	private SapPartnerService sapPartnerService;
	private ProductImageHelper productImageHelper;
	private Converter<AddressModel, AddressData> addressConverter;
	private BackendAvailabilityService backendAvailabilityService;
	private MessageSource messageSource;
	private I18NService i18nService;

	protected boolean isSyncOrdermgmtEnabled()
	{

		return (getBaseStoreService().getCurrentBaseStore().getSAPConfiguration() != null)
				&& (getBaseStoreService().getCurrentBaseStore().getSAPConfiguration().isSapordermgmt_enabled());

	}

	/**
	 * @return Is Backend down?
	 */
	public boolean isBackendDown()
	{
		return backendAvailabilityService.isBackendDown();
	}



	public MessageSource getMessageSource()
	{
		return messageSource;
	}

	@Required
	public void setMessageSource(final MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	protected I18NService getI18nService()
	{
		return i18nService;
	}

	@Required
	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commercefacades.order.CheckoutFacade#hasCheckoutCart()
	 */
	@Override
	public boolean hasCheckoutCart()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.hasCheckoutCart();
		}

		if (isBackendDown())
		{
			return false;
		}
		return sapCartService.hasSessionCart();
	}

	/**
	 * @return the sapPartnerService
	 */
	public SapPartnerService getSapPartnerService()
	{
		return sapPartnerService;
	}

	/**
	 * @param sapPartnerService
	 *           the sapPartnerService to set
	 */
	public void setSapPartnerService(final SapPartnerService sapPartnerService)
	{
		this.sapPartnerService = sapPartnerService;
	}

	/**
	 * @return the addressConverter
	 */
	@Override
	public Converter<AddressModel, AddressData> getAddressConverter()
	{
		return addressConverter;
	}

	/**
	 * @param addressConverter
	 *           the addressConverter to set
	 */
	@Override
	public void setAddressConverter(final Converter<AddressModel, AddressData> addressConverter)
	{
		this.addressConverter = addressConverter;
	}

	/**
	 * @return the backendAvailabilityService
	 */
	public BackendAvailabilityService getBackendAvailabilityService()
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

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commercefacades.order.CheckoutFacade#getCheckoutCart()
	 */
	@Override
	public CartData getCheckoutCart()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.getCheckoutCart();
		}

		if (isBackendDown())
		{
			return new CartData();
		}

		final CartData checkoutCart = sapCartService.getSessionCart();
		productImageHelper.enrichWithProductImages(checkoutCart);
		return checkoutCart;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commercefacades.order.CheckoutFacade#getSupportedDeliveryAddresses(boolean)
	 */
	@Override
	public List<AddressData> getSupportedDeliveryAddresses(final boolean visibleAddressesOnly)
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.getSupportedDeliveryAddresses(visibleAddressesOnly);
		}

		if (isBackendDown())
		{
			return null;
		}

		final Collection<AddressModel> addressesForOwner = sapPartnerService.getAllowedDeliveryAddresses();
		final List<AddressData> result = new ArrayList<>();
		for (final AddressModel model : addressesForOwner)
		{
			result.add(addressConverter.convert(model));
		}
		return result;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.commercefacades.order.CheckoutFacade#setDeliveryAddress(de.hybris.platform.commercefacades.
	 * user.data.AddressData)
	 */
	@Override
	public boolean setDeliveryAddress(final AddressData usedAddress)
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.setDeliveryAddress(usedAddress);
		}

		//obsolete?
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commercefacades.order.CheckoutFacade#removeDeliveryAddress()
	 */
	@Override
	public boolean removeDeliveryAddress()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.removeDeliveryAddress();
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commercefacades.order.CheckoutFacade#getSupportedDeliveryModes()
	 */
	@Override
	public List<? extends DeliveryModeData> getSupportedDeliveryModes()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.getSupportedDeliveryModes();
		}

		if (isBackendDown())
		{
			return null;
		}

		return checkoutService.getSupportedDeliveryModes();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commercefacades.order.CheckoutFacade#setDeliveryAddressIfAvailable()
	 */
	@Override
	public boolean setDeliveryAddressIfAvailable()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.setDeliveryAddressIfAvailable();
		}

		// nothing needed here as the delivery address will be either determined from the SD partners
		// attached to the order, or the user will explicitly select a different one
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commercefacades.order.CheckoutFacade#setDeliveryModeIfAvailable()
	 */
	@Override
	public boolean setDeliveryModeIfAvailable()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.setDeliveryModeIfAvailable();
		}

		// nothing to do, as the delivery mode will be either determined from SD settings,
		// or the user will explicitly select one
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commercefacades.order.CheckoutFacade#setPaymentInfoIfAvailable()
	 */
	@Override
	public boolean setPaymentInfoIfAvailable()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.setPaymentInfoIfAvailable();
		}

		handleNotSupportedLogging("setPaymentInfoIfAvailable");
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commercefacades.order.CheckoutFacade#setDeliveryMode(java.lang.String)
	 */
	@Override
	public boolean setDeliveryMode(final String deliveryModeCode)
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.setDeliveryMode(deliveryModeCode);
		}


		if (isBackendDown())
		{
			return false;
		}

		return checkoutService.setDeliveryMode(deliveryModeCode);
	}

	@Override
	protected DeliveryModeData getDeliveryMode()
	{

		if (!isSyncOrdermgmtEnabled())
		{
			return super.getDeliveryMode();
		}

		return getCheckoutCart().getDeliveryMode();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commercefacades.order.CheckoutFacade#removeDeliveryMode()
	 */
	@Override
	public boolean removeDeliveryMode()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.removeDeliveryMode();
		}

		if (isBackendDown())
		{
			return false;
		}

		return checkoutService.setDeliveryMode("");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commercefacades.order.CheckoutFacade#getDeliveryCountries()
	 */
	@Override
	public List<CountryData> getDeliveryCountries()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.getDeliveryCountries();
		}

		//We don't return any countries. Specifying a manual address currently not supported
		handleNotSupportedLogging("getDeliveryCountries");
		return Collections.EMPTY_LIST;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commercefacades.order.CheckoutFacade#getBillingCountries()
	 */
	@Override
	public List<CountryData> getBillingCountries()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.getBillingCountries();
		}

		//We don't return any countries. Specifying a manual address currently not supported
		handleNotSupportedLogging("getBillingCountries");
		return Collections.EMPTY_LIST;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commercefacades.order.CheckoutFacade#setPaymentDetails(java.lang.String)
	 */
	@Override
	public boolean setPaymentDetails(final String paymentInfoId)
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.setPaymentDetails(paymentInfoId);
		}

		handleNotSupportedException();
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commercefacades.order.CheckoutFacade#getSupportedCardTypes()
	 */
	@Override
	public List<CardTypeData> getSupportedCardTypes()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.getSupportedCardTypes();
		}


		//credit card payment currently not supported
		handleNotSupportedLogging("getSupportedCardTypes");
		return Collections.EMPTY_LIST;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.commercefacades.order.CheckoutFacade#createPaymentSubscription(de.hybris.platform.commercefacades
	 * .order.data.CCPaymentInfoData)
	 */
	@Override
	public CCPaymentInfoData createPaymentSubscription(final CCPaymentInfoData paymentInfoData)
	{

		if (!isSyncOrdermgmtEnabled())
		{
			return super.createPaymentSubscription(paymentInfoData);
		}

		handleNotSupportedException();
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commercefacades.order.CheckoutFacade#authorizePayment(java.lang.String)
	 */
	@Override
	public boolean authorizePayment(final String securityCode)
	{

		if (!isSyncOrdermgmtEnabled())
		{
			return super.authorizePayment(securityCode);
		}

		//handleNotSupportedException();
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commercefacades.order.CheckoutFacade#placeOrder()
	 */
	@Override
	public OrderData placeOrder() throws InvalidCartException
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.placeOrder();
		}

		if (isBackendDown())
		{
			//We should never reach this method as checkout is forbidden
			throw new ApplicationBaseRuntimeException("Place order not allowed if backend is down");
		}

		return checkoutService.placeOrder();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commercefacades.order.CheckoutFacade#containsTaxValues()
	 */
	@Override
	public boolean containsTaxValues()
	{

		if (!isSyncOrdermgmtEnabled())
		{
			return super.containsTaxValues();
		}
		//Prices from ERP already contain taxes
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commercefacades.order.CheckoutFacade#prepareCartForCheckout()
	 */
	@Override
	public void prepareCartForCheckout()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			super.prepareCartForCheckout();

			return;
		}
		//no specific preparation needed in our scenario

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commercefacades.order.CheckoutFacade#setDefaultPaymentInfoForCheckout()
	 */
	@Override
	public boolean setDefaultPaymentInfoForCheckout()
	{

		if (!isSyncOrdermgmtEnabled())
		{
			return super.setDefaultPaymentInfoForCheckout();

		}

		handleNotSupportedException();
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commercefacades.order.CheckoutFacade#setDefaultDeliveryAddressForCheckout()
	 */
	@Override
	public boolean setDefaultDeliveryAddressForCheckout()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.setDefaultDeliveryAddressForCheckout();

		}
		// nothing needed as this will be determined from the backend
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commercefacades.order.CheckoutFacade#setCheapestDeliveryModeForCheckout()
	 */
	@Override
	public boolean setCheapestDeliveryModeForCheckout()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.setCheapestDeliveryModeForCheckout();

		}

		handleNotSupportedException();
		return false;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commercefacades.order.CheckoutFacade#hasPickUpItems()
	 */
	@Override
	public boolean hasPickUpItems()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.hasPickUpItems();

		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.b2bacceleratorfacades.order.B2BCheckoutFacade#getDaysOfWeekForReplenishmentCheckoutSummary()
	 */
	@Override
	public List<B2BDaysOfWeekData> getDaysOfWeekForReplenishmentCheckoutSummary()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.getDaysOfWeekForReplenishmentCheckoutSummary();

		}

		handleNotSupportedLogging("getDaysOfWeekForReplenishmentCheckoutSummary");
		return Collections.EMPTY_LIST;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.b2bacceleratorfacades.order.B2BCheckoutFacade#scheduleOrder(de.hybris.platform.
	 * b2bacceleratorfacades.order.data.TriggerData)
	 */
	@Override
	public ScheduledCartData scheduleOrder(final TriggerData trigger)
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.scheduleOrder(trigger);

		}

		handleNotSupportedException();
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.b2bacceleratorfacades.order.B2BCheckoutFacade#createCartFromOrder(java.lang.String)
	 */
	@Override
	public void createCartFromOrder(final String orderCode)
	{
		if (!isSyncOrdermgmtEnabled())
		{
			super.createCartFromOrder(orderCode);
			return;

		}

		handleNotSupportedException();

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
	public void setSapCartService(final CartService sapCartService)
	{
		this.sapCartService = sapCartService;
	}

	protected void handleNotSupportedException()
	{
		throw new ApplicationBaseRuntimeException(MSG_NOT_SUPPORTED);
	}

	protected void handleNotSupportedLogging(final String call)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(MSG_NOT_SUPPORTED + ", ignoring: " + call);
		}
	}

	/**
	 * @return the checkoutService
	 */
	public CheckoutService getCheckoutService()
	{
		return checkoutService;
	}

	/**
	 * @param checkoutService
	 *           the checkoutService to set
	 */
	public void setCheckoutService(final CheckoutService checkoutService)
	{
		this.checkoutService = checkoutService;
	}

	/**
	 * @return the productImageHelper
	 */
	public ProductImageHelper getProductImageHelper()
	{
		return productImageHelper;
	}

	/**
	 * @param productImageHelper
	 *           the productImageHelper to set
	 */
	public void setProductImageHelper(final ProductImageHelper productImageHelper)
	{
		this.productImageHelper = productImageHelper;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.b2bacceleratorfacades.api.cart.CheckoutFacade#updateCheckoutCart(de.hybris.platform.commercefacades
	 * .order.data.CartData)
	 */
	@Override
	public CartData updateCheckoutCart(final CartData cartData)
	{

		if (!isSyncOrdermgmtEnabled())
		{
			return super.updateCheckoutCart(cartData);

		}

		if (isBackendDown())
		{
			//We should never reach this method as checkout is forbidden
			throw new ApplicationBaseRuntimeException("UpdateCheckoutCart is not allowed if backend is down");
		}

		return checkoutService.updateCheckoutCart(cartData);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.b2bacceleratorfacades.api.cart.CheckoutFacade#getPaymentTypes()
	 */
	@Override
	public List<B2BPaymentTypeData> getPaymentTypes()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.getPaymentTypes();

		}

		final B2BPaymentTypeData accountPaymentType = new B2BPaymentTypeData();
		accountPaymentType.setCode(CheckoutPaymentType.ACCOUNT.getCode().toLowerCase());
		accountPaymentType.setDisplayName(getMessageSource().getMessage("sap.paymenttype.account", null,
				getI18nService().getCurrentLocale()));

		return Arrays.asList(accountPaymentType);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.b2bacceleratorfacades.api.cart.CheckoutFacade#placeOrder(de.hybris.platform.b2bacceleratorfacades
	 * .checkout.data.PlaceOrderData)
	 */
	@Override
	public OrderData placeOrder(final PlaceOrderData placeOrderData) throws InvalidCartException
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.placeOrder(placeOrderData);

		}

		if (isBackendDown())
		{
			//We should never reach this method as checkout is forbidden
			throw new ApplicationBaseRuntimeException("Place order not allowed if backend is down");
		}

		return checkoutService.placeOrder();
	}
}
