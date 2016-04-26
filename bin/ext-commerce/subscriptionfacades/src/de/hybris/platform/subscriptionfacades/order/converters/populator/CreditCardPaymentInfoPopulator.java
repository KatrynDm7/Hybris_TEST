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
package de.hybris.platform.subscriptionfacades.order.converters.populator;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Nonnull;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Credit Card Payment Info Populator that populates subscription-specific information only.
 *
 *
 * Expects that the commercefacades' CreditCardPaymentInfoPopulator will also be used first.
 */
public class CreditCardPaymentInfoPopulator implements Populator<CreditCardPaymentInfoModel, CCPaymentInfoData>
{

	@Override
	public void populate(@Nonnull final CreditCardPaymentInfoModel source,
	                     @Nonnull final CCPaymentInfoData target)  throws ConversionException
	{
		validateParameterNotNullStandardMessage("target", target);
		validateParameterNotNullStandardMessage("source", source);
		target.setSubscriptionServiceId(source.getSubscriptionServiceId());
	}
}
