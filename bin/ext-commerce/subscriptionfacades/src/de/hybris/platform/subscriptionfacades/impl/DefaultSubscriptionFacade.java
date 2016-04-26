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
package de.hybris.platform.subscriptionfacades.impl;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.subscriptionfacades.SubscriptionFacade;
import de.hybris.platform.subscriptionfacades.action.SubscriptionUpdateActionEnum;
import de.hybris.platform.subscriptionfacades.data.SubscriptionBillingData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionBillingDetailFileStream;
import de.hybris.platform.subscriptionfacades.data.SubscriptionData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPaymentData;
import de.hybris.platform.subscriptionfacades.exceptions.SubscriptionFacadeException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * Default implementation of the {@link SubscriptionFacade} interface providing No-op implementations of the interface
 * methods.
 */
public class DefaultSubscriptionFacade extends AbstractSubscriptionFacade
{
	private static final Logger LOG = Logger.getLogger(DefaultSubscriptionFacade.class);

	@Nonnull
	@Override
	public SubscriptionPaymentData updateProfile(final Map<String, String> parameters) throws SubscriptionFacadeException
	{
		LOG.debug("No-op implementation");
		return createEmptySubscriptionPaymentData();
	}

	@Override
	public SubscriptionPaymentData createSubscriptions(@Nonnull final OrderData order, final Map<String, String> parameters)
			throws SubscriptionFacadeException
	{
		LOG.debug("No-op implementation");
		return createEmptySubscriptionPaymentData();
	}

	@Nonnull
	@Override
	public SubscriptionPaymentData initializeTransaction(final String clientIpAddress, final String returnUrl,
			final String cancelReturnUrl, final Map<String, String> parameters) throws SubscriptionFacadeException
	{
		LOG.debug("No-op implementation");
		return createEmptySubscriptionPaymentData();
	}

	@Nonnull
	@Override
	public SubscriptionPaymentData finalizeTransaction(final String authorizationRequestId,
			final String authorizationRequestToken, final Map<String, String> parameters) throws SubscriptionFacadeException
	{
		LOG.debug("No-op implementation");
		return createEmptySubscriptionPaymentData();
	}

	@Nonnull
	@Override
	public CCPaymentInfoData changePaymentMethod(@Nonnull final CCPaymentInfoData paymentInfo, final String action,
			final boolean propagate, final Map<String, String> parameters) throws SubscriptionFacadeException
	{
		LOG.debug("No-op implementation.");
		return createEmptyCCPaymentInfoData();
	}

	@Nonnull
	@Override
	public SubscriptionPaymentData replacePaymentMethod(final String subscriptionId, final String paymentMethodId,
			final Map<String, String> parameters) throws SubscriptionFacadeException
	{
		LOG.debug("No-op implementation");
		return createEmptySubscriptionPaymentData();
	}

	@Nonnull
	@Override
	public SubscriptionData replacePaymentMethod(final String subscriptionId, final String paymentMethodId,
			final String effectiveFrom, final Map<String, String> parameters) throws SubscriptionFacadeException
	{
		LOG.debug("No-op implementation");
		return new SubscriptionData();
	}

	@Override
	public CCPaymentInfoData createPaymentSubscription(final Map<String, String> paymentParameters)
			throws SubscriptionFacadeException
	{
		LOG.debug("No-op implementation");
		return null;
	}

	@Nonnull
	@Override
	public SubscriptionPaymentData updateSubscription(final String subscriptionId, final boolean force,
			final SubscriptionUpdateActionEnum action, final Map<String, String> parameters) throws SubscriptionFacadeException
	{
		LOG.debug("No-op implementation");
		return createEmptySubscriptionPaymentData();
	}

	@Nonnull
	@Override
	public Collection<SubscriptionData> getSubscriptions() throws SubscriptionFacadeException
	{
		LOG.debug("No-op implementation");
		return new ArrayList<>();
	}

	@Override
	public SubscriptionData getSubscription(final String subscriptionId) throws SubscriptionFacadeException
	{
		LOG.debug("No-op implementation");
		return new SubscriptionData();
	}

	@Override
	public String hpfUrl() throws SubscriptionFacadeException
	{
		LOG.debug("No-op implementation");
		return StringUtils.EMPTY;
	}

	@Override
	public List<SubscriptionBillingData> getUpgradePreviewBillings(final String subscriptionId, final String upgradeId)
			throws SubscriptionFacadeException
	{
		LOG.debug("No-op implementation");
		return new ArrayList<>();
	}

	@Nonnull
	@Override
	public SubscriptionData updateSubscriptionAutorenewal(final String subscriptionId, final boolean isAutorenewal,
			final Map<String, String> parameters) throws SubscriptionFacadeException
	{
		LOG.debug("No-op implementation");
		return new SubscriptionData();
	}

	@Nonnull
	@Override
	public SubscriptionData changeSubscriptionState(final String subscriptionId, final String newStatus,
			final Map<String, String> parameters) throws SubscriptionFacadeException
	{
		LOG.debug("No-op implementation");
		return new SubscriptionData();
	}

	@Nonnull
	@Override
	public SubscriptionData extendSubscriptionTermDuration(final String subscriptionId, final Integer contractDurationExtension,
			final Map<String, String> parameters) throws SubscriptionFacadeException
	{
		LOG.debug("No-op implementation");
		return new SubscriptionData();
	}

	@Override
	public List<SubscriptionBillingData> getBillingActivityList(final String subscriptionId, final Map<String, String> parameters)
			throws SubscriptionFacadeException
	{
		LOG.debug("No-op implementation");
		return new ArrayList<>();
	}

	@Override
	public SubscriptionBillingDetailFileStream getBillingActivityDetail(final String billingActivityId,
			final Map<String, String> parameters) throws SubscriptionFacadeException
	{
		LOG.debug("No-op implementation");
		return new SubscriptionBillingDetailFileStream();
	}

	@Nonnull
	@Override
	protected SubscriptionPaymentData createEmptySubscriptionPaymentData()
	{
		final SubscriptionPaymentData subscriptionPaymentData = super.createEmptySubscriptionPaymentData();
		subscriptionPaymentData.getParameters().put("sessionTransactionToken", StringUtils.EMPTY);
		return subscriptionPaymentData;
	}
}
