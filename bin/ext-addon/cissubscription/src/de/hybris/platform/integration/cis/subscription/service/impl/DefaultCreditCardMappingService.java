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
package de.hybris.platform.integration.cis.subscription.service.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.order.data.CardTypeData;
import de.hybris.platform.integration.cis.subscription.service.CreditCardMappingService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.hybris.cis.common.utils.MapUtils;

import javax.annotation.Nonnull;


/**
 * Implements the functionality of converting the credit card codes in hybris to those of the provider
 */
public class DefaultCreditCardMappingService implements CreditCardMappingService
{
	private static final String VENDOR_MAPPING_PATH = "/vendor-mapping.properties";
	private static final String REQUEST_CONVERT_KEY = "%s.creditcard.%s.request";

	public Map<String, String> initialize()
	{
		final Map<String, String> vendorCCMapping = new HashMap<String, String>();

		for (final Entry<Object, Object> entry : MapUtils.loadPropertiesToSet(VENDOR_MAPPING_PATH))
		{
			vendorCCMapping.put((String) entry.getKey(), (String) entry.getValue());
		}

		return vendorCCMapping;
	}

	@Override
	public boolean convertCCToProviderSpecificName(@Nonnull final Collection<CardTypeData> creditCards, final String vendor)
	{
		validateParameterNotNullStandardMessage("creditCards", creditCards);

		boolean converted = false;
		final Map<String, String> vendorCCMapping = initialize();

		for (final CardTypeData cardType : creditCards)
		{
			final String key = String.format(REQUEST_CONVERT_KEY, vendor.toLowerCase(), cardType.getCode().toLowerCase());

			if (vendorCCMapping.containsKey(key))
			{
				final String mappingValue = vendorCCMapping.get(key);

				if (StringUtils.isNotEmpty(mappingValue))
				{
					cardType.setCode(mappingValue);
					converted = true;
				}
			}
		}

		return converted;
	}

}
