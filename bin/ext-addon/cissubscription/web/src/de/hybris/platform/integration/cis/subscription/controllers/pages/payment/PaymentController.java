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
package de.hybris.platform.integration.cis.subscription.controllers.pages.payment;

import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CardTypeData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.TitleData;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.integration.cis.subscription.forms.AddressForm;
import de.hybris.platform.integration.cis.subscription.forms.PaymentForm;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.subscriptionfacades.SubscriptionFacade;
import de.hybris.platform.subscriptionfacades.exceptions.SubscriptionFacadeException;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.lang.LocaleUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * PaymentController
 */
@Controller
public class PaymentController
{
	private static final Logger LOG = Logger.getLogger(PaymentController.class);
	private static final String PAYMENT_FORM_PAGE = "pages/payment/paymentFormPage";

	@Resource(name = "subscriptionFacade")
	private SubscriptionFacade subscriptionFacade;

	@Resource(name = "checkoutFacade")
	private CheckoutFacade checkoutFacade;

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = "i18NService")
	private I18NService i18NService;

	@Resource(name = "modelService")
	private ModelService modelService;

	protected SubscriptionFacade getSubscriptionFacade()
	{
		return subscriptionFacade;
	}

	protected CheckoutFacade getCheckoutFacade()
	{
		return checkoutFacade;
	}

	protected UserFacade getUserFacade()
	{
		return userFacade;
	}

	protected I18NService getI18NService()
	{
		return i18NService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	public Map<String, String> getCountries()
	{
		final Collection<CountryData> countries = getCheckoutFacade().getDeliveryCountries();
		final Map<String, String> countryMap = new TreeMap<String, String>();

		for (final CountryData country : countries)
		{
			countryMap.put(country.getIsocode(), country.getName() + " (" + country.getIsocode() + ")");
		}

		return countryMap;
	}

	public Map<String, String> getCardTypes()
	{
		final Collection<CardTypeData> creditCards = getCheckoutFacade().getSupportedCardTypes();
		final Map<String, String> cardTypeMap = new TreeMap<String, String>();

		for (final CardTypeData creditCard : creditCards)
		{
			cardTypeMap.put(creditCard.getCode(), creditCard.getName());
		}

		return cardTypeMap;
	}

	public Map<String, String> getTitles()
	{
		final Collection<TitleData> titles = getUserFacade().getTitles();
		final Map<String, String> titleMap = new TreeMap<String, String>();

		for (final TitleData title : titles)
		{
			titleMap.put(title.getCode(), title.getName());
		}

		return titleMap;
	}

	@ModelAttribute("months")
	public Map<String, String> getMonths()
	{
		final Map<String, String> months = new TreeMap<String, String>();

		months.put("01", "01");
		months.put("02", "02");
		months.put("03", "03");
		months.put("04", "04");
		months.put("05", "05");
		months.put("06", "06");
		months.put("07", "07");
		months.put("08", "08");
		months.put("09", "09");
		months.put("10", "10");
		months.put("11", "11");
		months.put("12", "12");

		return months;
	}

	@ModelAttribute("startYears")
	public Map<String, String> getStartYears()
	{
		final Map<String, String> startYears = new TreeMap<String, String>();
		final Calendar calender = new GregorianCalendar();

		for (int i = calender.get(Calendar.YEAR); i > (calender.get(Calendar.YEAR) - 6); i--)
		{
			startYears.put(String.valueOf(i), String.valueOf(i));
		}

		return startYears;
	}

	@ModelAttribute("expiryYears")
	public Map<String, String> getExpiryYears()
	{
		final Map<String, String> expiryYears = new TreeMap<String, String>();
		final Calendar calender = new GregorianCalendar();

		for (int i = calender.get(Calendar.YEAR); i < (calender.get(Calendar.YEAR) + 11); i++)
		{
			expiryYears.put(String.valueOf(i), String.valueOf(i));
		}

		return expiryYears;
	}

	@RequestMapping(value = "/addPaymentMethod", method = RequestMethod.GET)
	public String addPaymentMethod(final Model model, @RequestParam("sessionToken") final String sessionToken,
			@RequestParam("billingAddress") final String billingAddressPk, @RequestParam("lang") final String lang)
	{
		Assert.notNull(sessionToken, "request param sessionToken may not be null");
		Assert.notNull(lang, "request param lang may not be null");

		getI18NService().setCurrentLocale(LocaleUtils.toLocale(lang));

		try
		{
			final String postUrl = getSubscriptionFacade().hpfUrl();

			model.addAttribute("paymentForm", populatePaymentForm(sessionToken, getBillingAddress(billingAddressPk)));
			model.addAttribute("postUrl", postUrl);
			model.addAttribute("countries", getCountries());
			model.addAttribute("cardTypes", getCardTypes());
			model.addAttribute("titles", getTitles());

			return PAYMENT_FORM_PAGE;
		}
		catch (final SubscriptionFacadeException e)
		{
			LOG.error("Error retrieving post URL", e);
			throw new IllegalStateException("Error retrieving post URL", e);
		}
	}


	/**
	 * Returns the default billing address for the given customer.
	 * 
	 * @param billingAddressPk
	 *           the customer's unique ID
	 * @return the default billing address of the user
	 */
	private AddressModel getBillingAddress(final String billingAddressPk)
	{
		return "0".equals(billingAddressPk) ? null : (AddressModel) getModelService().get(PK.parse(billingAddressPk));
	}

	/**
	 * Creates and populates the payment form model.
	 * 
	 * @param sessionToken
	 *           the session token
	 * @param billingAddress
	 *           the billing address
	 * @return {@link PaymentForm}
	 */
	protected PaymentForm populatePaymentForm(final String sessionToken, final AddressModel billingAddress)
	{
		final PaymentForm paymentForm = new PaymentForm();

		paymentForm.setSessionToken(sessionToken);

		// pre-populate billing address
		if (billingAddress != null)
		{
			final AddressForm addressForm = new AddressForm();
			if (billingAddress.getTitle() != null)
			{
				addressForm.setTitleCode(billingAddress.getTitle().getCode());
			}
			addressForm.setFirstName(billingAddress.getFirstname());
			addressForm.setLastName(billingAddress.getLastname());
			addressForm.setLine1(billingAddress.getLine1());
			addressForm.setLine2(billingAddress.getLine2());
			addressForm.setTownCity(billingAddress.getTown());
			addressForm.setPostcode(billingAddress.getPostalcode());
			if (billingAddress.getCountry() != null)
			{
				addressForm.setCountryIso(billingAddress.getCountry().getIsocode());
			}
			paymentForm.setBillingAddress(addressForm);
		}

		return paymentForm;
	}
}
