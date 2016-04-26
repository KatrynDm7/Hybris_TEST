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

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.hybris.cis.api.model.CisAddress;
import com.hybris.cis.api.subscription.model.CisPaymentMethod;
import com.hybris.cis.api.subscription.model.CisPaymentMethodResult;
import com.hybris.commons.client.RestResponse;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.converters.Populator;

/**
 * Populates a {@link CCPaymentInfoData} from a {@link RestResponse}.
 */
public class CisCCPaymentInfoDataPopulator implements Populator<RestResponse, CCPaymentInfoData>
{
   @Override
   public void populate(final RestResponse source, final CCPaymentInfoData target) throws IllegalArgumentException
   {
      Assert.notNull(target, "Parameter target cannot be null.");
      Assert.notNull(source, "Parameter source cannot be null.");

      if (source.getResult() instanceof CisPaymentMethodResult) {
         final CisPaymentMethod cisPaymentMethod = ((CisPaymentMethodResult) source.getResult()).getPaymentMethod();

         if (cisPaymentMethod != null) {
            target.setAccountHolderName(cisPaymentMethod.getCardHolder());
            target.setCardNumber(cisPaymentMethod.getCcNumber());
            target.setSubscriptionId(cisPaymentMethod.getMerchantPaymentMethodId());
            target.setSaved(true);

            populateCardType(cisPaymentMethod.getCardType(), target);

            target.setExpiryMonth(StringUtils.leftPad(Integer.toString(cisPaymentMethod.getExpirationMonth()), 2, "0"));
            target.setExpiryYear(Integer.toString(cisPaymentMethod.getExpirationYear()));

            final AddressData billingAddress = new AddressData();

            populateBillingAddress(billingAddress, cisPaymentMethod.getBillingAddress());

            target.setBillingAddress(billingAddress);
         }
      }
   }

   private void populateCardType(final String source, final CCPaymentInfoData target)
   {
      switch (source)
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
            target.setCardType(source);
            break;
      }

   }

   protected void populateBillingAddress(final AddressData billingAddress, final CisAddress source)
   {
	  billingAddress.setTitleCode(source.getTitle());
      billingAddress.setFirstName(source.getFirstName());
      billingAddress.setLastName(source.getLastName());
      billingAddress.setLine1(source.getAddressLine1());
      billingAddress.setLine2(source.getAddressLine2());
      final CountryData country = new CountryData();
      country.setIsocode(source.getCountry());
      billingAddress.setCountry(country);
      billingAddress.setPostalCode(source.getZipCode());
      billingAddress.setTown(source.getCity());
   }
}
