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
package de.hybris.platform.assistedservicefacades.util;

import de.hybris.platform.assistedservicefacades.constants.AssistedservicefacadesConstants;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.util.Config;

import java.text.SimpleDateFormat;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.WebUtils;


public final class AssistedServiceUtils
{
	private AssistedServiceUtils()
	{
	}

	public static String getCardLastFourDigits(final CustomerModel customer)
	{
		final String cardNumber = customer.getDefaultPaymentInfo() instanceof CreditCardPaymentInfoModel ? ((CreditCardPaymentInfoModel) customer
				.getDefaultPaymentInfo()).getNumber() : null;
		return cardNumber == null ? "----" : cardNumber.subSequence(cardNumber.length() >= 4 ? cardNumber.length() - 4 : 0,
				cardNumber.length()).toString();
	}

	public static String getCreationDate(final CustomerModel customer)
	{
		return customer.getCreationtime() != null ? new SimpleDateFormat("dd/MM/YYYY").format(customer.getCreationtime())
				: "--/--/----";
	}

	/**
	 * Erase SAML sso cookie from browser.
	 *
	 * @param response
	 */
	public static void eraseSamlCookie(final HttpServletResponse response)
	{
		final String cookieName = Config.getParameter(AssistedservicefacadesConstants.SSO_COOKIE_NAME);
		if (cookieName != null)
		{
			final Cookie cookie = new Cookie(cookieName, "");
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
	}

	/**
	 * Get SAML sso cookie.
	 *
	 * @param request
	 * @return saml SSO cookie if persist
	 */
	public static Cookie getSamlCookie(final HttpServletRequest request)
	{
		final String cookieName = Config.getParameter(AssistedservicefacadesConstants.SSO_COOKIE_NAME);
		return cookieName != null ? WebUtils.getCookie(request, cookieName) : null;
	}
}