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
package de.hybris.platform.assistedservicefacades;

import de.hybris.platform.assistedservicefacades.exception.AssistedServiceFacadeException;
import de.hybris.platform.assistedservicefacades.util.AssistedServiceSession;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * Facade interface for assisted service functionality.
 */
public interface AssistedServiceFacade
{
	/**
	 * Adds a customer and/or cart to the current session.
	 *
	 * @param customerId
	 *           id to identify the customer by (usually email address)
	 * @param cartId
	 *           the id of the customer cart to pick up
	 */
	void emulateCustomer(final String customerId, final String cartId) throws AssistedServiceFacadeException;

	/**
	 * Remove customer and/or cart from the current session.
	 */
	void stopEmulateCustomer();

	/**
	 * Starts the Assisted Service Mode.
	 */
	void launchAssistedServiceMode();

	/**
	 * Ends the Assisted Service Mode.
	 */
	void quitAssistedServiceMode();

	/**
	 * Logs in Assisted Service agent using provided credentials.
	 *
	 * @param username
	 * @param password
	 * @throws AssistedServiceFacadeException
	 *            In case of bad credentials
	 */
	void loginAssistedServiceAgent(final String username, final String password) throws AssistedServiceFacadeException;

	/**
	 * Logs in Assisted Service agent based on request.
	 *
	 * @param request
	 * @throws AssistedServiceFacadeException
	 *            In case of bad/missing parameters in request
	 */
	void loginAssistedServiceAgent(final HttpServletRequest request) throws AssistedServiceFacadeException;

	/**
	 * Logs out Assisted Service agent by removing session attribute.
	 *
	 * @throws AssistedServiceFacadeException
	 */
	void logoutAssistedServiceAgent() throws AssistedServiceFacadeException;

	/**
	 * Whether or not Assisted Service module is launched.
	 *
	 * @return true when assisted service mode has been launched
	 */
	boolean isAssistedServiceModeLaunched();

	/**
	 * Returns timeout for assisted service agent session in seconds.
	 */
	int getAssistedServiceSessionTimeout();

	/**
	 * Returns timer value (in seconds) for assisted service agent session, that displays inside widget.
	 */
	int getAssistedServiceSessionTimerValue();

	/**
	 * Returns assisted service attributes map for page model.
	 *
	 * @return String->Object map for page model.
	 */
	Map<String, Object> getAssistedServiceSessionAttributes();

	/**
	 * Get list of customers which username or email starts with provided value.
	 *
	 * @param username
	 *           uid or customer's name
	 * @return suggested customers
	 * @throws AssistedServiceFacadeException
	 */
	List<CustomerModel> getSuggestedCustomers(final String username) throws AssistedServiceFacadeException;

	/**
	 * Personify customer based on customer stored on login step and current session cart.
	 *
	 * @throws AssistedServiceFacadeException
	 */
	void emulateAfterLogin() throws AssistedServiceFacadeException;

	/**
	 * Binds customer with provided id to cart if it's anonymous cart.
	 */
	void bindCustomerToCart(final String customerId, final String cartId) throws AssistedServiceFacadeException;

	/**
	 * Creates a new customer by it email and name.
	 *
	 * @param customerId
	 *           email of to be newly created customer that is used as uid
	 *
	 * @param customerName
	 *           name of to be newly created customer (firstname and surname separated by space symbol)
	 */
	CustomerModel createNewCustomer(String customerId, String customerName) throws AssistedServiceFacadeException;

	/**
	 * Checks if there is ASM information in the current session or no, this will be used by Session Restriction mainly
	 *
	 * @return boolean value to indicates if there are a valid assisted service info in the session or no.
	 */
	boolean isAssistedServiceAgentLoggedIn();

	/**
	 * Returns collection of a customer's carts
	 *
	 * @param customer
	 *           customer model whose carts to be returned
	 * @return collection of the customer's cart models
	 */
	Collection<CartModel> getCartsForCustomer(final CustomerModel customer);

	/**
	 * Returns ASM session object with all information about current asm session.
	 *
	 * @return asm session object
	 */
	AssistedServiceSession getAsmSession();
}