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
package de.hybris.platform.integration.cis.subscription.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.subscriptionfacades.data.SubscriptionBillingData;

import java.util.List;

import com.hybris.cis.api.subscription.model.CisSubscriptionBillingInfo;


/**
 * Populate the {@link SubscriptionBillingData} with the {@link CisSubscriptionBillingInfo} data
 */
public class CisUpgradePreviewBillingPopulator implements
		Populator<List<CisSubscriptionBillingInfo>, List<SubscriptionBillingData>>
{
	@Override
	public void populate(final List<CisSubscriptionBillingInfo> cisSubscriptionBillings,
			final List<SubscriptionBillingData> subscriptionBillingDatas) throws ConversionException
	{
		if (cisSubscriptionBillings != null)
		{
			for (final CisSubscriptionBillingInfo cisSubscriptionBilling : cisSubscriptionBillings)
			{
				subscriptionBillingDatas.add(populate(cisSubscriptionBilling));
			}
		}
	}

	public SubscriptionBillingData populate(final CisSubscriptionBillingInfo cisSubscriptionBilling) throws ConversionException
	{
		final SubscriptionBillingData subscriptionBillingData = new SubscriptionBillingData();

		subscriptionBillingData.setBillingDate(cisSubscriptionBilling.getBillingDate());
		subscriptionBillingData.setBillingPeriod(cisSubscriptionBilling.getBillingPeriod());
		subscriptionBillingData.setPaymentAmount(cisSubscriptionBilling.getAmount());
		subscriptionBillingData.setPaymentStatus(cisSubscriptionBilling.getStatus());

		return subscriptionBillingData;
	}
}
