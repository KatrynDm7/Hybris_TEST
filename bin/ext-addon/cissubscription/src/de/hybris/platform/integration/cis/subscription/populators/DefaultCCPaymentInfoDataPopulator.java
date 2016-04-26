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

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.converters.Populator;

import java.util.Map;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;
import org.apache.commons.lang.StringUtils;


/**
 * Default credit card payment info populator.
 */
public class DefaultCCPaymentInfoDataPopulator implements Populator<Map<String, String>, CCPaymentInfoData>
{
	@Override
	public void populate(final Map<String, String> source, final CCPaymentInfoData target) throws IllegalArgumentException
	{
		validateParameterNotNullStandardMessage("target", source);
		validateParameterNotNullStandardMessage("source", target);

		target.setAccountHolderName(source.get("accountHolderName"));
		target.setCardNumber(source.get("cardNumber"));

		populateCardType(source, target);

		final String expirationDate = source.get("expiryDate");
		if (StringUtils.isNotEmpty(expirationDate))
		{
			// assuming date of format MM/yyyy
			target.setExpiryMonth(expirationDate.substring(0, 2));
			target.setExpiryYear(expirationDate.substring(3, 7));
		}

		final AddressData billingAddress = new AddressData();

		populateBillingAddress(billingAddress, source);

		target.setBillingAddress(billingAddress);
	}

	private void populateCardType(final Map<String, String> source, final CCPaymentInfoData target)
	{
		switch (source.get("cardType"))
		{
			case "Visa":
				target.setCardType("visa");
				break;
			case "American Express":
				target.setCardType("amex");
				break;
			case "MasterCard":
				target.setCardType("master");
				break;
			default:
				target.setCardType(source.get("cardType"));
				break;
		}

	}

	protected void populateBillingAddress(final AddressData billingAddress, final Map<String, String> source)
	{
		billingAddress.setFirstName(source.get("firstName"));
		billingAddress.setLastName(source.get("lastName"));
		billingAddress.setTitleCode(source.get("titleCode"));
		billingAddress.setLine1(source.get("addr1"));
		billingAddress.setLine2(source.get("addr2"));
		final CountryData country = new CountryData();
		country.setIsocode(source.get("country"));
		billingAddress.setCountry(country);
		billingAddress.setPostalCode(source.get("postalCode"));
		billingAddress.setTown(source.get("city"));
	}

}
