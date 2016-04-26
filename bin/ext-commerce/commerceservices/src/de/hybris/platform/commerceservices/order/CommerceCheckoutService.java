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
package de.hybris.platform.commerceservices.order;

import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;

import java.math.BigDecimal;


/**
 * Service for checkout and place order functionality
 */
public interface CommerceCheckoutService
{
	/**
	 * @deprecated use {@link #setDeliveryAddress(de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter)} instead
	 *
	 * @param cartModel    the cart
	 * @param addressModel the address, a null address will cause the delivery address to be removed from the cart.
	 * @return true if successful
	 * @deprecated use {@link #setDeliveryAddress(de.hybris.platform.core.model.order.CartModel, de.hybris.platform.core.model.user.AddressModel, boolean)}
	 *             Sets the given address as delivery address on the cart A null address will cause removal of deliveryAddress from
	 *             the cart.
	 */
	@Deprecated
	boolean setDeliveryAddress(CartModel cartModel, AddressModel addressModel);

	/**
	 * @deprecated use {@link #setDeliveryAddress(de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter)} instead
	 * Sets the given address as delivery address on the cart and also marks the address as delivery address. A null
	 * address will cause removal of deliveryAddress from the cart.
	 *
	 * @param cartModel             the cart
	 * @param addressModel          the address a null address will cause the delivery address to be removed from the cart.
	 * @param flagAsDeliveryAddress true to mark the given address as delivery address
	 * @return true if successful
	 */
	@Deprecated
	boolean setDeliveryAddress(CartModel cartModel, AddressModel addressModel, boolean flagAsDeliveryAddress);

	/**
	 * Sets the given address as delivery address on the cart and also marks the address as delivery address. A null
	 * address will cause removal of deliveryAddress from the cart.
	 * @param parameter A parameter object
	 * @return true if successful
	 */
	boolean setDeliveryAddress(final CommerceCheckoutParameter parameter);

	/**
	 * @deprecated use {@link #setDeliveryAddress(de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter)} instead.
	 * Sets the given delivery mode on the cart
	 *
	 * @param cartModel         the cart
	 * @param deliveryModeModel the delivery mode
	 * @return true if successful
	 */
	@Deprecated
	boolean setDeliveryMode(CartModel cartModel, DeliveryModeModel deliveryModeModel);

	/**
	 * Sets the given delivery mode on the cart
	 *
	 * @param parameter A parameter object for cart and deliverymode
	 * @return true if successful
	 */
	boolean setDeliveryMode(CommerceCheckoutParameter parameter);


	/**
	 * @deprecated use {@link #validateDeliveryMode(de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter)} instead
	 * Validates the current delivery mode on the cart and clears if not valid
	 * the cart is then calculated via {@link CommerceCartCalculationStrategy#calculateCart(de.hybris.platform.core.model.order.CartModel)}
	 *
	 * @param cartModel the cart
	 */
	@Deprecated
	void validateDeliveryMode(CartModel cartModel);

	/**
	 * Validates the current delivery mode on the cart and clears if not valid
	 * the cart is then calculated via {@link CommerceCartCalculationStrategy#calculateCart(de.hybris.platform.core.model.order.CartModel)}
	 *
	 * @param parameter The parameter object holding the cart
	 */
	void validateDeliveryMode(final CommerceCheckoutParameter parameter);

	/**
	 * @deprecated use {@link #setPaymentInfo(de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter)} instead.
	 * Sets the given payment info on the cart
	 *
	 * @param cartModel        the cart
	 * @param paymentInfoModel the payment details
	 * @return true if successful
	 */
	@Deprecated
	boolean setPaymentInfo(CartModel cartModel, PaymentInfoModel paymentInfoModel);

	/**
	 * Sets the given payment info on the cart
	 *
	 * @param parameter A parameter object for cart and payment details
	 *
	 * @return true if successful
	 */
	boolean setPaymentInfo(final CommerceCheckoutParameter parameter);

	/**
	 * @deprecated use {@link #authorizePayment(de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter)}
	 * Authorizes the total amount of the cart
	 *
	 * @param cartModel       the cart
	 * @param securityCode    the cv2 number
	 * @param paymentProvider the payment provider that will be used to authorize
	 * @return the payment transaction entry
	 */
	@Deprecated
	PaymentTransactionEntryModel authorizePayment(CartModel cartModel, String securityCode, String paymentProvider);

	/**
	 * @deprecated use {@link #authorizePayment(de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter)}
	 * Authorizes specified amount
	 *
	 * @param cartModel       the cart
	 * @param securityCode    the cv2 number
	 * @param paymentProvider the payment provider that will be used to authorize
	 * @param amount          the amount to authorize
	 * @return the payment transaction entry
	 */
	@Deprecated
	PaymentTransactionEntryModel authorizePayment(CartModel cartModel, String securityCode, String paymentProvider,
	                                              BigDecimal amount);

	/**
	 * Authorizes the total amount of the cart if {@link de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter#authorizationAmount} is null otherwise
	 * the passed in amout is authorized
	 *
	 * @param parameter A parameter object holding the cart, security code, payment provider and optionaly authorization amount.
	 * @return A payment transaction entry.
	 */
	PaymentTransactionEntryModel authorizePayment(CommerceCheckoutParameter parameter);


	/**
	 * @param cartModel the cart
	 * @return the order that has been created
	 * @throws de.hybris.platform.order.InvalidCartException
	 *          if the order cannot be placed
	 * @deprecated use {@link #placeOrder(de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter)} instead
	 *             Creates an order for the given cart
	 */
	@Deprecated
	OrderModel placeOrder(CartModel cartModel) throws InvalidCartException;


	/**
	 * Creates an order for the given cart
	 *
	 * @param cartModel        the cart
	 * @param salesApplication the sales application that placed the order
	 * @return the order that has been created
	 * @throws de.hybris.platform.order.InvalidCartException
	 *          if the order cannot be placed
	 */
	@Deprecated
	OrderModel placeOrder(CartModel cartModel, SalesApplication salesApplication) throws InvalidCartException;

	/**
	 * Creates an order for the given cart
	 *
	 *
	 *
	 *
	 * @param parameter
	 * @return the order that has been created
	 * @throws de.hybris.platform.order.InvalidCartException
	 *          if the order cannot be placed
	 */
	CommerceOrderResult placeOrder(final CommerceCheckoutParameter parameter) throws InvalidCartException;

	/**
	 * Get the payment provider name
	 *
	 * @return the payment provider
	 */
	String getPaymentProvider();

	/**
	 * @deprecated use {@link #removeDeliveryMode(de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter)} instead
	 * Removes delivery mode from cart.
	 * the cart is then calculated via {@link CommerceCartCalculationStrategy#calculateCart(de.hybris.platform.core.model.order.CartModel)}
	 *
	 * @param cartModel the cart
	 * @return true if the deliver mode is removed
	 */
	@Deprecated
	boolean removeDeliveryMode(CartModel cartModel);

	/**
	 * Removes delivery mode from cart.
	 * the cart is then calculated via {@link CommerceCartCalculationStrategy#calculateCart(de.hybris.platform.core.model.order.CartModel)}
	 * @param parameter A parameter object holding the cart
	 * @return true if success
	 */
	boolean removeDeliveryMode(CommerceCheckoutParameter parameter);

	/**
	 * @deprecated use {@link #calculateCart(de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter)} instead.
	 *
	 * Calculates the cartModel if the calculated flag is false.
	 *
	 * @param cartModel The current user's cartModel
	 */
	@Deprecated
	void calculateCart(CartModel cartModel);

	/**
	 * Calculates the cartModel if the calculated flag is false.
	 *
	 * @param parameter The parameter object for the current user's cartModel
	 */
	void calculateCart(final CommerceCheckoutParameter parameter);

}
