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

import com.hybris.cis.api.subscription.model.CisSubscriptionBillingActivityList;
import com.hybris.cis.api.subscription.model.CisSubscriptionBillingInfo;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

/**
 * Populates a {@link List} of {@link SubscriptionBillingData} from a {@link CisSubscriptionBillingActivityList}.
 */
public class CisSubscriptionBillingActivityListPopulator implements
		Populator<CisSubscriptionBillingActivityList, List<SubscriptionBillingData>>
{
	@Override
	public void populate(final CisSubscriptionBillingActivityList source, final List<SubscriptionBillingData> target)
			throws ConversionException
	{
		validateParameterNotNullStandardMessage("source", target);

		final List<CisSubscriptionBillingInfo> billingInfos = source.getBillings();

		for (final CisSubscriptionBillingInfo billingInfo : billingInfos)
		{
			final SubscriptionBillingData billingData = new SubscriptionBillingData();
			billingData.setBillingId(billingInfo.getBillingId());
			billingData.setBillingPeriod(billingInfo.getBillingPeriod());
			billingData.setBillingDate(billingInfo.getBillingDate());
			billingData.setPaymentAmount(billingInfo.getAmount());
			billingData.setPaymentStatus(billingInfo.getStatus());
			target.add(billingData);
		}
	}

}
