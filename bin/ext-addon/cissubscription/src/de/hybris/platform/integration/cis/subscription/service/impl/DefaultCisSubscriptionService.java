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
 */
package de.hybris.platform.integration.cis.subscription.service.impl;

import de.hybris.platform.cissubscription.data.CisSubscriptionUpdateAction;
import de.hybris.platform.integration.cis.subscription.service.CisSubscriptionService;

import java.util.Date;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.cis.api.subscription.model.CisChangePaymentMethodRequest;
import com.hybris.cis.api.subscription.model.CisPaymentMethodUpdateRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionCancelSubscriptionRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionChangeStateRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionCreateRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionOrderPostRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionPayNowRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionProfileRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionReplacePaymentMethodRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionSessionFinalizeRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionSessionInitRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionUpdateRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionUpgradeRequest;
import com.hybris.cis.client.rest.subscription.SubscriptionClient;
import com.hybris.commons.client.RestResponse;


/**
 * Implementing class for delegating requests to the CIS server
 */
public class DefaultCisSubscriptionService implements CisSubscriptionService
{
	private static final Logger LOG = Logger.getLogger(DefaultCisSubscriptionService.class);

	private SubscriptionClient subscriptionClient;

	@Override
	@Deprecated
	public RestResponse post(final String cisClientRef, final CisSubscriptionOrderPostRequest orderPostRequest)
	{
		return getSubscriptionClient().postSubscription(cisClientRef, orderPostRequest);
	}

	@Override
	public RestResponse createSubscription(final String cisClientRef, final CisSubscriptionCreateRequest createSubscriptionRequest)
	{
		return getSubscriptionClient().createSubscription(cisClientRef, createSubscriptionRequest);
	}

	@Override
	@Deprecated
	public RestResponse replacePaymentMethod(final String cisClientRef,
			final CisSubscriptionReplacePaymentMethodRequest replacePaymentRequest)
	{
		return getSubscriptionClient().replaceSubscriptionPaymentMethod(cisClientRef, replacePaymentRequest);
	}

	@Override
	public RestResponse replacePaymentMethod(final String cisClientRef, final String merchantSubscriptionId,
			final String merchantPaymentMethodId, final String effectiveFrom)
	{
		return getSubscriptionClient().replaceSubscriptionPaymentMethod(cisClientRef, merchantSubscriptionId,
				merchantPaymentMethodId, effectiveFrom);
	}



	@Override
	public RestResponse processPayNow(final String cisClientRef, final CisSubscriptionPayNowRequest payNowRequest)
	{
		return getSubscriptionClient().processPayNow(cisClientRef, payNowRequest);
	}

	@Override
	public RestResponse initializeTransaction(final String cisClientRef, final CisSubscriptionSessionInitRequest initRequest)
	{
		return getSubscriptionClient().initializeTransaction(cisClientRef, initRequest);
	}

	@Override
	public RestResponse hpfUrl(final String cisClientRef)
	{
		return getSubscriptionClient().hpfUrl(cisClientRef);
	}

	@Override
	public RestResponse finalizeTransaction(final String cisClientRef, final CisSubscriptionSessionFinalizeRequest finalizeRequest)
	{
		return getSubscriptionClient().finalizeTransaction(cisClientRef, finalizeRequest);
	}

	@Override
	public RestResponse updateProfile(final String cisClientRef, final CisSubscriptionProfileRequest profileRequest)
	{
		return getSubscriptionClient().updateProfile(cisClientRef, profileRequest);
	}

	@Override
	@Deprecated
	public RestResponse changePaymentMethod(final String cisClientRef, final CisChangePaymentMethodRequest changePaymentRequest)
	{
		return getSubscriptionClient().changePaymentMethod(cisClientRef, changePaymentRequest);
	}

	@Override
	public RestResponse updatePaymentMethod(final String cisClientRef, final CisPaymentMethodUpdateRequest updatePaymentRequest)
	{
		return getSubscriptionClient().updatePaymentMethod(cisClientRef, updatePaymentRequest);
	}


	@Override
	@Deprecated
	public RestResponse cancelSubscription(final String cisClientRef,
			final CisSubscriptionCancelSubscriptionRequest cancelSubscriptionRequest)
	{
		return getSubscriptionClient().cancelSubscription(cisClientRef, cancelSubscriptionRequest);
	}

	@Override
	public RestResponse cancelSubscription(final String cisClientRef, final String merchantSubscriptionId,
			final String effectiveFrom)
	{
		return getSubscriptionClient().cancelSubscription(cisClientRef, merchantSubscriptionId, effectiveFrom);
	}

	@Override
	public RestResponse createCustomerProfile(final String cisClientRef, final CisSubscriptionProfileRequest createAccountRequest)
	{
		return getSubscriptionClient().createProfile(cisClientRef, createAccountRequest);
	}

	@Override
	public RestResponse getCustomerProfile(final String cisClientRef, final String merchantAccountId)
	{
		return getSubscriptionClient().getProfile(cisClientRef, merchantAccountId);
	}

	protected SubscriptionClient getSubscriptionClient()
	{
		return this.subscriptionClient;
	}

	@Required
	public void setSubscriptionClient(final SubscriptionClient subscriptionClient)
	{
		this.subscriptionClient = subscriptionClient;
	}

	@Override
	public RestResponse updateSubscription(final String cisClientRef, final String subscriptionId, final boolean force,
			final CisSubscriptionUpdateAction updateAction)
	{
		if (CisSubscriptionUpdateAction.CANCEL.equals(updateAction))
		{
			final CisSubscriptionCancelSubscriptionRequest request = new CisSubscriptionCancelSubscriptionRequest();
			request.setMerchantSubscriptionId(subscriptionId);
			request.setForce(force);
			return cancelSubscription(cisClientRef, request);
		}
		throw new NotImplementedException(String.format("Update action %s not yet implemented", updateAction.name()));
	}

	@Override
	public RestResponse upgradeSubscription(final String cisClientRef,
			final CisSubscriptionUpgradeRequest subscriptionUpgradeRequest)
	{
		return getSubscriptionClient().upgradeSubscription(cisClientRef, subscriptionUpgradeRequest);
	}

	@Override
	public RestResponse updateSubscription(final String cisClientRef, final CisSubscriptionUpdateRequest subscriptionUpdateRequest)
	{
		return getSubscriptionClient().updateSubscription(cisClientRef, subscriptionUpdateRequest);
	}

	@Override
	public RestResponse changeSubscriptionState(final String cisClientRef,
			final CisSubscriptionChangeStateRequest subscriptionChangeStateRequest)
	{
		return getSubscriptionClient().changeSubscriptionState(cisClientRef, subscriptionChangeStateRequest);
	}


	@Override
	public RestResponse getBillingActivityList(final String cisClientRef, final String subscriptionId, final Date fromDate,
			final Date toDate)
	{
		return getSubscriptionClient().getBillingActivityList(cisClientRef, subscriptionId, fromDate, toDate);
	}


	@Override
	public RestResponse getBillingActivityDetail(final String cisClientRef, final String billingActivityId)
	{
		return getSubscriptionClient().getBillingActivityDetail(cisClientRef, billingActivityId);
	}
}
