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

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.subscriptionservices.enums.TermOfServiceRenewal;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.hybris.cis.api.subscription.model.CisTermsOfService;


/**
 * Populate the CisTermsOfService with the ProductData information
 */
public class CisTermsOfServicePopulator implements Populator<ProductData, CisTermsOfService>
{
	@Override
	public void populate(final ProductData source, final CisTermsOfService target) throws ConversionException
	{
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source == null)
		{
			return;
		}

		target.setId(source.getSubscriptionTerm().getId());
		target.setName(source.getSubscriptionTerm().getName());
		target.setNumber(source.getSubscriptionTerm().getTermOfServiceNumber());
		target.setFrequency(source.getSubscriptionTerm().getTermOfServiceFrequency().getCode());

		if (StringUtils.equals(TermOfServiceRenewal.AUTO_RENEWING.getCode(), source.getSubscriptionTerm().getTermOfServiceRenewal()
				.getCode()))
		{
			target.setAutoRenewal(Boolean.TRUE);
		}
		else
		{
			target.setAutoRenewal(Boolean.FALSE);
		}

		target.setCancellable(Boolean.valueOf(source.getSubscriptionTerm().isCancellable()));
		target.setBillingPlanId(source.getSubscriptionTerm().getBillingPlan().getId());
		target.setBillingPlanName(source.getSubscriptionTerm().getBillingPlan().getName());
		final Integer billingCycleDay = source.getSubscriptionTerm().getBillingPlan().getBillingCycleDay();
		target.setBillingCycleDay(billingCycleDay == null ? 1 : billingCycleDay.intValue());

		final String srcFrequency = source.getSubscriptionTerm().getBillingPlan().getBillingTime().getCode();
		String targetFrequency;
		switch (srcFrequency)
		{
			case "monthly":
				targetFrequency = "Month";
				break;
			case "quarterly":
				targetFrequency = "Quarter";
				break;
			case "yearly":
				targetFrequency = "Year";
				break;
			default:
				throw new ConversionException(String.format("Billing frequency [%s] not supported.", srcFrequency));
		}
		target.setBillingTime(targetFrequency);
	}

}
