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
package de.hybris.platform.subscriptionfacades.converters.populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.BillingPlanData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;
import de.hybris.platform.subscriptionfacades.data.TermOfServiceFrequencyData;
import de.hybris.platform.subscriptionfacades.data.TermOfServiceRenewalData;
import de.hybris.platform.subscriptionservices.enums.TermOfServiceFrequency;
import de.hybris.platform.subscriptionservices.enums.TermOfServiceRenewal;
import de.hybris.platform.subscriptionservices.model.BillingPlanModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Populator implementation for {@link SubscriptionTermModel} as source and {@link SubscriptionTermData} as target type.
 *
 * @param <SOURCE> source class
 * @param <TARGET> target class
 */
public class SubscriptionTermPopulator<SOURCE extends SubscriptionTermModel, TARGET extends SubscriptionTermData> implements
		Populator<SOURCE, TARGET>
{
	private Converter<BillingPlanModel, BillingPlanData> billingPlanConverter;
	private Converter<TermOfServiceFrequency, TermOfServiceFrequencyData> termOfServiceFrequencyConverter;
	private Converter<TermOfServiceRenewal, TermOfServiceRenewalData> termOfServiceRenewalConverter;

	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		target.setBillingPlan(getBillingPlanConverter().convert(source.getBillingPlan()));
		target.setCancellable(BooleanUtils.toBoolean(source.getCancellable()));
		target.setName(source.getName());
		target.setTermOfServiceFrequency(getTermOfServiceFrequencyConverter().convert(source.getTermOfServiceFrequency()));
		final int termOfServiceNumber = source.getTermOfServiceNumber() == null ? 0 : source.getTermOfServiceNumber();
		target.setTermOfServiceNumber(termOfServiceNumber);
		target.setTermOfServiceRenewal(getTermOfServiceRenewalConverter().convert(source.getTermOfServiceRenewal()));
	}

	protected Converter<BillingPlanModel, BillingPlanData> getBillingPlanConverter()
	{
		return billingPlanConverter;
	}

	@Required
	public void setBillingPlanConverter(final Converter<BillingPlanModel, BillingPlanData> billingPlanConverter)
	{
		this.billingPlanConverter = billingPlanConverter;
	}

	protected Converter<TermOfServiceFrequency, TermOfServiceFrequencyData> getTermOfServiceFrequencyConverter()
	{
		return termOfServiceFrequencyConverter;
	}

	@Required
	public void setTermOfServiceFrequencyConverter(
			final Converter<TermOfServiceFrequency, TermOfServiceFrequencyData> termOfServiceFrequencyConverter)
	{
		this.termOfServiceFrequencyConverter = termOfServiceFrequencyConverter;
	}

	protected Converter<TermOfServiceRenewal, TermOfServiceRenewalData> getTermOfServiceRenewalConverter()
	{
		return termOfServiceRenewalConverter;
	}

	@Required
	public void setTermOfServiceRenewalConverter(
			final Converter<TermOfServiceRenewal, TermOfServiceRenewalData> termOfServiceRenewalConverter)
	{
		this.termOfServiceRenewalConverter = termOfServiceRenewalConverter;
	}
}
