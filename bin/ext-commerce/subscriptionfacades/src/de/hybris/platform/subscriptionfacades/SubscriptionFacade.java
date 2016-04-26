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
package de.hybris.platform.subscriptionfacades;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.subscriptionfacades.action.SubscriptionUpdateActionEnum;
import de.hybris.platform.subscriptionfacades.data.SubscriptionBillingData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionBillingDetailFileStream;
import de.hybris.platform.subscriptionfacades.data.SubscriptionData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPaymentData;
import de.hybris.platform.subscriptionfacades.exceptions.SubscriptionFacadeException;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * Facade which provides functionality to manage subscriptions.
 */
public interface SubscriptionFacade
{

	/**
	 * Updates an newly-created Payment Type with the Subscription Service information. This method differs from
	 * {@link #changePaymentMethod(CCPaymentInfoData, String, boolean, Map)} because it must be used only during the
	 * creation of a new payment method (created by the PaymentFacade).
	 * {@link #changePaymentMethod(CCPaymentInfoData, String, boolean, Map)} must be used changing details at a later
	 * time.
	 *
	 * @param paymentType
	 *           the payment info data relating to the recently-created Payment Type
	 * @param paramMap
	 *           the detail map containing new subscription information.
	 * @return an updated paymentInfo, or null if the update fails.
	 * @throws SubscriptionFacadeException
	 */
	@Nonnull
	CCPaymentInfoData updateCreatedPaymentMethod(@Nonnull final CCPaymentInfoData paymentType, final Map<String, String> paramMap)
			throws SubscriptionFacadeException;

	/**
	 * Creates or updates a subscription profile for the current user.
	 *
	 * @param parameters
	 *           provider specific parameters
	 *
	 * @return {@link SubscriptionPaymentData} containing the results of the operation
	 */
	@Nonnull
	SubscriptionPaymentData updateProfile(@Nullable final Map<String, String> parameters) throws SubscriptionFacadeException;

	/**
	 * Creates subscriptions for the given order and current user.
	 *
	 * @param order
	 *           the order to create the subscriptions for
	 * @param parameters
	 *           provider specific parameters
	 *
	 * @return {@link SubscriptionPaymentData} containing the results of the operation
	 */
	SubscriptionPaymentData createSubscriptions(@Nonnull final OrderData order, Map<String, String> parameters)
			throws SubscriptionFacadeException;

	/**
	 * Initializes a transaction with the subscription billing provider.
	 *
	 * @param clientIpAddress
	 *           the IP address of the calling client. May be empty.
	 * @param returnUrl
	 *           the URL to redirect to after the silent from post. May not be null or empty.
	 * @param cancelReturnUrl
	 *           the URL to redirect to in case of errors after the silent from post. May not be null or empty.
	 * @param parameters
	 *           provider specific parameters
	 *
	 * @return {@link SubscriptionPaymentData} containing the results of the operation (hpfUrl and session token).
	 *         result.getParameters().get("sessionTransactionToken") returns not-null String value.
	 */
	@Nonnull
	SubscriptionPaymentData initializeTransaction(final String clientIpAddress, final String returnUrl,
			final String cancelReturnUrl, final Map<String, String> parameters) throws SubscriptionFacadeException;

	/**
	 * Finalizes an established subscription with the subscription billing provider.
	 *
	 * @param authorizationRequestId
	 *           the request ID
	 * @param authorizationRequestToken
	 *           the request token retrieved from the result of a {@link SubscriptionFacade#initializeTransaction} call.
	 * @param parameters
	 *           provider specific parameters
	 *
	 * @return {@link SubscriptionPaymentData} containing the results of the operation
	 */
	@Nonnull
	SubscriptionPaymentData finalizeTransaction(@Nullable final String authorizationRequestId,
												@Nullable final String authorizationRequestToken,
												@Nullable final Map<String, String> parameters) throws SubscriptionFacadeException;

	/**
	 * Changes an existing payment method.
	 *
	 * @param paymentInfo
	 *           {@link CCPaymentInfoData} the updated payment information
	 * @param action
	 *           {@link String} the change action to perform (currently supported: <b><i>"enable"</i></b>,
	 *           <b><i>"disable"</i></b>)
	 * @param propagate
	 *           determines whether to propagate the change to all billing subscriptions using the given payment method
	 * @param parameters
	 *           provider specific parameters
	 * @return {@link CCPaymentInfoData} containing the results of the operation
	 */
	@Nonnull
	CCPaymentInfoData changePaymentMethod(@Nonnull final CCPaymentInfoData paymentInfo,@Nullable final String action, final boolean propagate,
			final Map<String, String> parameters) throws SubscriptionFacadeException;

	/**
	 * Replaces the payment method for the given subscription.
	 *
	 * @param subscriptionId
	 *           the ID of the subscription to replace the payment method for
	 * @param paymentMethodId
	 *           the ID of the new payment method which replaces the current one
	 * @param parameters
	 *           provider specific parameters
	 *
	 * @return {@link SubscriptionPaymentData} containing the results of the operation
	 */
	@Nonnull
	SubscriptionPaymentData replacePaymentMethod(@Nullable final String subscriptionId,@Nullable final String paymentMethodId,
												 @Nullable final Map<String, String> parameters) throws SubscriptionFacadeException;

	/**
	 * @param subscriptionId
	 *           the ID of the subscription
	 * @param paymentMethodId
	 *           the ID of the payment method to set
	 * @param effectiveFrom
	 *           determines when the replacement will take effect
	 * @param parameters
	 *           provider specific parameters
	 * @return {@link SubscriptionData} containing the results of the operation
	 * @throws SubscriptionFacadeException
	 */
	@Nonnull
	SubscriptionData replacePaymentMethod(final String subscriptionId, final String paymentMethodId, final String effectiveFrom,
			final Map<String, String> parameters) throws SubscriptionFacadeException;

	/**
	 * Creates a payment subscription on the hybris side.
	 *
	 * @param paymentParameters
	 *           parameters retrieved from the result of a {@link SubscriptionFacade#finalizeTransaction} call.
	 * @return {@link CCPaymentInfoData} the payment information created based on the given parameters
	 */
	CCPaymentInfoData createPaymentSubscription(final Map<String, String> paymentParameters) throws SubscriptionFacadeException;

	/**
	 * Updates the given subscription (e.g. cancel)
	 *
	 * @param subscriptionId
	 *           the subscription to update
	 * @param force
	 *           determines whether to force the update or not
	 * @param action
	 *           the update action to perform
	 * @param parameters
	 *           provider specific parameters
	 *
	 * @return {@link SubscriptionPaymentData} containing the results of the operation
	 */
	@Nonnull
	SubscriptionPaymentData updateSubscription(final String subscriptionId, final boolean force,
			final SubscriptionUpdateActionEnum action, Map<String, String> parameters) throws SubscriptionFacadeException;

	/**
	 * Returns all subscriptions for the current user.
	 *
	 * @return {@link Collection}<{@link SubscriptionData}> the current user's subscription
	 */
	@Nonnull
	Collection<SubscriptionData> getSubscriptions() throws SubscriptionFacadeException;

	/**
	 * Returns the subscription with the given ID.
	 *
	 * @param subscriptionId
	 *           the ID of the subscription
	 * @return {@link SubscriptionData} the subscription with the given ID
	 */
	@Nullable
	SubscriptionData getSubscription(final String subscriptionId) throws SubscriptionFacadeException;

	/**
	 * Returns the HPF URL.
	 *
	 * @return the HPF URL
	 * @throws SubscriptionFacadeException
	 */
	String hpfUrl() throws SubscriptionFacadeException;

	/**
	 * Calculates an end date for a subscription product based on its subscription term details.
	 *
	 * @param subscriptionProductData
	 * @param startDate
	 *           start date of the subscription
	 * @return the calculated end date
	 */
	Date getSubscriptionEndDate(@Nonnull final ProductData subscriptionProductData, final Date startDate);

	/**
	 * Returns a list of possible upgrade options for the given subscription product.
	 *
	 * @param productCode
	 * @return {@link List} of {@link ProductData}
	 */
	List<ProductData> getUpsellingOptionsForSubscription(final String productCode);

	/**
	 * Returns a list of the possible billing changes between the current subscription and an upgrade option.
	 *
	 * @param subscriptionId
	 *           the current subscription
	 * @param upgradeId
	 *           the upgrade option
	 * @return {@link List} of {@link SubscriptionBillingData}
	 */
	List<SubscriptionBillingData> getUpgradePreviewBillings(final String subscriptionId, final String upgradeId)
			throws SubscriptionFacadeException;

	/**
	 * Returns the {@link AbstractOrderEntryModel} for the given orderCode and entryNumber.
	 *
	 * @param orderCode
	 * @param entryNumber
	 * @return {@link AbstractOrderEntryModel} or <code>null</code> if not found
	 */
	AbstractOrderEntryModel getOrderEntryForOrderCodeAndEntryNumber(final String orderCode, final int entryNumber);

	/**
	 * Updates the auto-renewal status of an existing subscription.
	 *
	 * @param subscriptionId
	 *           the ID of the subscription
	 * @param isAutorenewal
	 *           the new auto-renewal status of the subscription
	 * @param parameters
	 *           provider specific parameters
	 * @return {@link SubscriptionData} containing the results of the operation
	 * @throws SubscriptionFacadeException
	 */
	@Nonnull
	SubscriptionData updateSubscriptionAutorenewal(final String subscriptionId, boolean isAutorenewal,
			Map<String, String> parameters) throws SubscriptionFacadeException;

	/**
	 * Updates the state of an existing subscription.
	 *
	 * @param subscriptionId
	 *           the ID of the subscription
	 * @param newStatus
	 *           new status of the subscription
	 * @param parameters
	 *           provider specific parameters
	 * @return {@link SubscriptionData} containing the results of the operation
	 * @throws SubscriptionFacadeException
	 */
	@Nonnull
	SubscriptionData changeSubscriptionState(final String subscriptionId, final String newStatus,
			final Map<String, String> parameters) throws SubscriptionFacadeException;

	/**
	 * Extends the subscription term duration of an existing subscription.
	 *
	 * @param subscriptionId
	 *           the ID of the subscription
	 * @param contractDurationExtension
	 *           extends the duration x times
	 * @param parameters
	 *           provider specific parameters
	 * @return {@link SubscriptionData} containing the results of the operation
	 * @throws SubscriptionFacadeException
	 */
	@Nonnull
	SubscriptionData extendSubscriptionTermDuration(@Nullable final String subscriptionId,@Nullable final Integer contractDurationExtension,
													@Nullable final Map<String, String> parameters) throws SubscriptionFacadeException;

	/**
	 * Returns a list of billing activities for the given subscription.
	 *
	 * @param subscriptionId
	 *           the ID of the subscription
	 * @param parameters
	 *           provider specific parameters
	 * @return {@link List}<{@link SubscriptionBillingData}>
	 * @throws SubscriptionFacadeException
	 */
	List<SubscriptionBillingData> getBillingActivityList(final String subscriptionId, final Map<String, String> parameters)
			throws SubscriptionFacadeException;

	/**
	 * Returns a file stream containing detail information for the specified billing activity.
	 *
	 * @param billingActivityId
	 *           the ID of the billing activity
	 * @param parameters
	 *           provider specific parameters
	 * @return {@link SubscriptionBillingDetailFileStream}
	 * @throws SubscriptionFacadeException
	 */
	SubscriptionBillingDetailFileStream getBillingActivityDetail(final String billingActivityId,
			final Map<String, String> parameters) throws SubscriptionFacadeException;

	/**
	 * Returns a list of subscriptions for the given paymentMethodId of the current user.
	 *
	 * @param paymentMethodId
	 *           the ID of the payment method
	 * @return list of subscriptions
	 * @throws SubscriptionFacadeException
	 */
	@Nonnull
	List<SubscriptionData> getSubscriptionsForPaymentMethod(final String paymentMethodId) throws SubscriptionFacadeException;
}
