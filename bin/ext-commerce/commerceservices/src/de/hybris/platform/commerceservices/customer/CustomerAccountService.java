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
package de.hybris.platform.commerceservices.customer;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.CardInfo;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Collection;
import java.util.List;


/**
 * Handles customer account management capabilities. Provides methods for registering, verifying users, managing
 * customer addresses, orders and payment information.
 */
public interface CustomerAccountService
{
	/**
	 * Creates a payment subscription using specified <code>paymentProvider</code>.
	 * 
	 * @param customerModel
	 *           the customer model to add a new payment subscription <code>cardPaymentInfoModel</code>
	 * @param cardInfo
	 *           the card details
	 * @param billingInfo
	 *           the payment address details
	 * @param titleCode
	 *           the title code of the payment address details. this parameter can be <code>null</code>
	 * @param paymentProvider
	 *           the payment provider value specifies provider that will be used to create subscription ID.
	 * @param saveInAccount
	 *           <code>true</code> to save in customer account to reuse the subscription for other transactions
	 * @return The created payment info holding the card details and the subscription id
	 */
	CreditCardPaymentInfoModel createPaymentSubscription(CustomerModel customerModel, CardInfo cardInfo, BillingInfo billingInfo,
			String titleCode, String paymentProvider, boolean saveInAccount);

	/**
	 * Sets the default <code>paymentInfoModel</code> for the specified <code>customerModel</code>. Both arguments must
	 * be set and <code>paymentInfoModel</code> must belong to the specified <code>customerModel</code>.
	 * 
	 * @param customerModel
	 *           the customer model to set default <code>paymentInfoModel</code>
	 * @param paymentInfoModel
	 *           the payment info model to be set as default
	 * @throws IllegalArgumentException
	 *            the illegal argument exception if any argument is null
	 */
	void setDefaultPaymentInfo(CustomerModel customerModel, PaymentInfoModel paymentInfoModel);


	/**
	 * Retrieves the customer's list of credit card payment information.
	 * 
	 * @param customerModel
	 *           the customer model for which to retrieve payment info
	 * @param saved
	 *           <code>true</code> to retrieve only saved credit card payment information
	 * @return the customer's list of credit card payment info
	 */
	List<CreditCardPaymentInfoModel> getCreditCardPaymentInfos(CustomerModel customerModel, boolean saved);

	/**
	 * Retrieves the customer's credit card payment info given its code
	 * 
	 * @param customerModel
	 *           the customer
	 * @param code
	 *           the code
	 * @return The credit card payment info
	 */
	CreditCardPaymentInfoModel getCreditCardPaymentInfoForCode(CustomerModel customerModel, String code);


	/**
	 * Delete credit card payment info. Before deleting it is checked if the given payment info belongs to the given
	 * customer.
	 * 
	 * @param customerModel
	 *           the customer model that should be the address owner
	 * @param creditCardPaymentInfo
	 *           payment info model that will be deleted
	 * @throws IllegalArgumentException
	 *            the illegal argument exception is thrown when given payment info does not belong to the given customer
	 *            or any argument is null
	 */
	void deleteCCPaymentInfo(CustomerModel customerModel, CreditCardPaymentInfoModel creditCardPaymentInfo);


	/**
	 * unlink credit card payment info. Before deleting it is checked if the given payment info belongs to the given
	 * customer.
	 * 
	 * @param customerModel
	 *           the customer model that should be the address owner
	 * @param creditCardPaymentInfo
	 *           payment info model that will be unlinked
	 * @throws IllegalArgumentException
	 *            the illegal argument exception is thrown when given payment info does not belong to the given customer
	 *            or any argument is null
	 */
	void unlinkCCPaymentInfo(CustomerModel customerModel, CreditCardPaymentInfoModel creditCardPaymentInfo);


	/**
	 * Returns all available customer titles in the system.
	 * 
	 * @return the titles
	 */
	Collection<TitleModel> getTitles();

	/**
	 * Returns all address book entries from the given customer.
	 * 
	 * @param customerModel
	 *           the customer model to retrieve addresses from
	 * @return the address book entries of the given user
	 * @throws IllegalArgumentException
	 *            the illegal argument exception if any argument is null
	 */
	List<AddressModel> getAllAddressEntries(CustomerModel customerModel);


	/**
	 * Returns all visible address book entries from the given customer.
	 * 
	 * @param customerModel
	 *           the customer model to retrieve addresses from
	 * @return the address book entries of the given user
	 * @throws IllegalArgumentException
	 *            the illegal argument exception if any argument is null
	 */
	List<AddressModel> getAddressBookEntries(CustomerModel customerModel);

	/**
	 * Returns all delivery address book entries from the given customer.
	 * 
	 * @param customerModel
	 *           the customer model to retrieve addresses from
	 * @return the address book entries of the given user
	 * @throws IllegalArgumentException
	 *            the illegal argument exception if any argument is null
	 */
	List<AddressModel> getAddressBookDeliveryEntries(CustomerModel customerModel);

	/**
	 * Get a customer address given its code
	 * 
	 * @param customerModel
	 *           the customer
	 * @param code
	 *           the address code
	 * @return the address
	 */
	AddressModel getAddressForCode(CustomerModel customerModel, String code);


	/**
	 * Returns a customer default shipping address
	 * 
	 * @param customerModel
	 * @return the default customer address or null if no address is set as default
	 */
	AddressModel getDefaultAddress(CustomerModel customerModel);


	/**
	 * Save address entry. If the given address model is not saved it will be assigned to the given customer and saved.
	 * If address is saved and belongs to other user it will be cloned, assigned to the given customer and the original
	 * address will be left as it is.
	 * 
	 * @param customerModel
	 *           the customer model to save/reassign address.
	 * @param addressModel
	 *           the address model to save/create
	 * @throws IllegalArgumentException
	 *            the illegal argument exception if any argument is null
	 */
	void saveAddressEntry(CustomerModel customerModel, AddressModel addressModel);


	/**
	 * Delete address entry. Before deleting it is checked if the given address belongs to the given customer.
	 * 
	 * @param customerModel
	 *           the customer model that should be the address owner
	 * @param addressModel
	 *           the address model that will be deleted
	 * @throws IllegalArgumentException
	 *            the illegal argument exception is thrown when given address does not belong to the given customer or
	 *            any argument is null
	 */
	void deleteAddressEntry(CustomerModel customerModel, AddressModel addressModel);


	/**
	 * Sets the default address entry. Because customer addresses are not sorted it sets given address as default
	 * customer payment address. If the address is not saved it will be created and assigned to the given user. If given
	 * address belongs to other user it will be cloned and assigned to the given customer and the original address will
	 * be left as it is.
	 * 
	 * @param customerModel
	 *           the customer model to set default payment address for
	 * @param addressModel
	 *           the address model to be set as default one
	 * @throws IllegalArgumentException
	 *            the illegal argument exception if any argument is null
	 */
	void setDefaultAddressEntry(CustomerModel customerModel, AddressModel addressModel);

	/**
	 * Clears default address on the given customer
	 * 
	 * @param customerModel
	 *           the customer
	 */
	void clearDefaultAddressEntry(CustomerModel customerModel);

	/**
	 * Register a user with given parameters
	 * 
	 * @param customerModel
	 *           the user data the user will be registered with
	 * @param password
	 *           the user's password
	 * @throws IllegalArgumentException
	 *            if required data is missing
	 * @throws DuplicateUidException
	 *            if the login is not unique
	 */
	void register(CustomerModel customerModel, String password) throws DuplicateUidException;

	/**
	 * Updates the current user with the given parameters
	 * 
	 * @param customerModel
	 *           the customer to update
	 * @param titleCode
	 *           the code for the title to set
	 * @param name
	 *           the full name to set on the customer
	 * @param login
	 *           the UID to set on the customer
	 * @throws DuplicateUidException
	 *            if the email is not unique
	 * @deprecated please use
	 *             {de.hybris.platform.commercefacades.customer.CustomerFacade#updateProfile(de.hybris.platform
	 *             .commercefacades.user.data.CustomerData)} instead.
	 */
	@Deprecated
	void updateProfile(CustomerModel customerModel, String titleCode, String name, String login) throws DuplicateUidException;

	/**
	 * Changes user password.
	 * 
	 * @param userModel
	 *           the user to change the password for
	 * @param oldPassword
	 *           old password to confirm
	 * @param newPassword
	 *           new password to set
	 * @throws PasswordMismatchException
	 *            if the given old password does not match the one stored in the system
	 */
	void changePassword(UserModel userModel, String oldPassword, String newPassword) throws PasswordMismatchException;

	/**
	 * Sends a forgotten password event
	 * 
	 * @param customerModel
	 *           the user
	 */
	void forgottenPassword(CustomerModel customerModel);

	/**
	 * Update the password for the user by decrypting and validating the token.
	 * 
	 * @param token
	 *           the password reset token
	 * @param newPassword
	 *           the new plain text password
	 * @throws IllegalArgumentException
	 *            If the new password is empty or the token is invalid or expired
	 * @throws TokenInvalidatedException
	 *            if the token was already used or there is a newer token
	 */
	void updatePassword(String token, String newPassword) throws TokenInvalidatedException;

	/**
	 * Returns the specified order for the supplied user.
	 * 
	 * @param customerModel
	 *           the user to retrieve order for
	 * @param code
	 *           the code of the order to retrieve
	 * @param store
	 *           the current store
	 * @return the order
	 */
	OrderModel getOrderForCode(CustomerModel customerModel, String code, BaseStoreModel store);

	/**
	 * Returns the order of the supplied user filtering by OrderStatus
	 * 
	 * @param customerModel
	 *           the user to retrieve orders for
	 * @param store
	 *           the current store
	 * @param status
	 *           One or more OrderStatuses to include in the result
	 * @return the list of orders
	 */
	List<OrderModel> getOrderList(CustomerModel customerModel, BaseStoreModel store, OrderStatus[] status);

	/**
	 * Returns the order of the supplied user filtering by OrderStatus
	 * 
	 * @param customerModel
	 *           the user to retrieve orders for
	 * @param store
	 *           the current store
	 * @param status
	 *           One or more OrderStatuses to include in the result
	 * @param pageableData
	 *           pagination information
	 * @return the list of orders
	 */
	SearchPageData<OrderModel> getOrderList(CustomerModel customerModel, BaseStoreModel store, OrderStatus[] status,
			PageableData pageableData);

	/**
	 * Changes uid for current user
	 * 
	 * @param newUid
	 *           given new uid
	 * @param currentPassword
	 *           password checked for authorization change
	 * @throws DuplicateUidException
	 *            if the newUid already exists in the system
	 * @throws PasswordMismatchException
	 *            if given currentPassword does not match the store one for the current user
	 */
	void changeUid(final String newUid, final String currentPassword) throws DuplicateUidException, PasswordMismatchException;

	/**
	 * Fetch the {@link OrderModel} for the given GUID,Store details and the expiry date for Guest Customers.
	 *
	 * @param guid
	 *             GUID of the order which was created from the {@link de.hybris.platform.core.model.order.CartModel}
	 * @param store
	 *             {@link BaseStoreModel} for the current store which the Guest Customer is accessing.
	 *
	 * @return the order
	 */
	OrderModel getGuestOrderForGUID(String guid, BaseStoreModel store);

	/**
	 * Registers the Guest customer to session cart for anonymous checkout
	 *
	 * @param customerModel
	 * @param password
	 * @throws DuplicateUidException
	 */
	void registerGuestForAnonymousCheckout(CustomerModel customerModel, String password) throws DuplicateUidException;

	/**
	 * Returns the orderModel based on the guid and base store
	 *
	 * @param guid
	 * @param store
	 * @return the order
	 */
	OrderModel getOrderDetailsForGUID(String guid, BaseStoreModel store);

	/**
	 * Converts a Guest Customer of anonymous checkout to a regular customer
	 *
	 * @param pwd
	 * @param orderGUID
	 * @throws DuplicateUidException
	 */
	void convertGuestToCustomer(String pwd, String orderGUID) throws DuplicateUidException;

    /**
     * Returns the orderModel based on order code and base store
     *
     * @param code
     * @param store
     * @return the order
     */
    OrderModel getOrderForCode(String code, BaseStoreModel store);
}
