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
package de.hybris.platform.acceleratorwebservicesaddon.payment.facade.impl;

import de.hybris.platform.acceleratorfacades.payment.data.PaymentSubscriptionResultData;
import de.hybris.platform.acceleratorfacades.payment.impl.DefaultPaymentFacade;
import de.hybris.platform.acceleratorservices.payment.data.PaymentData;
import de.hybris.platform.acceleratorwebservicesaddon.model.payment.PaymentSubscriptionResultModel;
import de.hybris.platform.acceleratorwebservicesaddon.payment.facade.CommerceWebServicesPaymentFacade;
import de.hybris.platform.acceleratorwebservicesaddon.payment.service.PaymentSubscriptionResultService;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class DefaultCommerceWebServicesPaymentFacade extends DefaultPaymentFacade implements CommerceWebServicesPaymentFacade
{

	private final static Logger LOG = Logger.getLogger(DefaultCommerceWebServicesPaymentFacade.class);

	private ConfigurationService configurationService;
	private PaymentSubscriptionResultService paymentSubscriptionResultService;
	private ModelService modelService;

	@Override
	public PaymentData beginSopCreateSubscription(final String responseUrl, final String merchantCallbackUrl)
	{
		final String siteName = getCurrentSiteName();
		final String fullMerchantCallbackUrl = getFullResponseUrl(merchantCallbackUrl, siteName, true);

		final CustomerModel customerModel = getCurrentUserForCheckout();
		final AddressModel paymentAddress = getDefaultPaymentAddress(customerModel);

		return getPaymentService().beginSopCreatePaymentSubscription(siteName, responseUrl, fullMerchantCallbackUrl, customerModel,
				null, paymentAddress);
	}

	/**
	 * Resolves a given URL to a full URL including server and port, etc.
	 *
	 * @param url
	 *           - the URL to resolve
	 * @param isSecure
	 *           - flag to indicate whether the final URL should use a secure connection or not.
	 * @return a full URL including HTTP protocol, server, port, path etc.
	 */
	protected String getFullResponseUrl(final String url, final String siteName, final boolean isSecure)
	{
		String baseUrlKey = "webroot.commercewebservices." + siteName + (isSecure ? ".https" : ".http");
		String baseUrl = getConfigurationService().getConfiguration().getString(baseUrlKey, null);
		if (baseUrl == null)
		{
			baseUrlKey = "webroot.commercewebservices." + (isSecure ? "https" : "http");
			baseUrl = getConfigurationService().getConfiguration().getString(baseUrlKey, "");
		}
		final String fullResponseUrl = baseUrl + url;
		return fullResponseUrl;
	}

	@Override
	public void savePaymentSubscriptionResult(final PaymentSubscriptionResultData paymentSubscriptionResultData,
			final String cartId)
	{

		PaymentSubscriptionResultModel paymentSubscriptionResultModel = getPaymentSubscriptionResultForCart(cartId);
		if (paymentSubscriptionResultModel == null)
		{
			paymentSubscriptionResultModel = modelService.create(PaymentSubscriptionResultModel.class);
			paymentSubscriptionResultModel.setCartId(cartId);
		}
		paymentSubscriptionResultModel.setSuccess(paymentSubscriptionResultData.isSuccess());
		paymentSubscriptionResultModel.setResult(paymentSubscriptionResultData);
		paymentSubscriptionResultService.savePaymentSubscriptionResult(paymentSubscriptionResultModel);
	}

	@Override
	public PaymentSubscriptionResultData getPaymentSubscriptionResult(final String cartId)
	{
		final PaymentSubscriptionResultModel paymentSubscriptionResultModel = getPaymentSubscriptionResultForCart(cartId);
		if (paymentSubscriptionResultModel != null)
		{
			final PaymentSubscriptionResultData paymentSubscriptionResultData = (PaymentSubscriptionResultData) paymentSubscriptionResultModel
					.getResult();
			return paymentSubscriptionResultData;
		}
		return null;
	}

	protected PaymentSubscriptionResultModel getPaymentSubscriptionResultForCart(final String cartId)
	{
		try
		{
			return paymentSubscriptionResultService.findPaymentSubscriptionResultByCart(cartId);
		}
		catch (final UnknownIdentifierException e)
		{
			return null;
		}
	}

	@Override
	public void removePaymentSubscriptionResult(final String cartId)
	{
		try
		{
			paymentSubscriptionResultService.removePaymentSubscriptionResultForCart(cartId);
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.warn(e.toString());
		}
	}

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	public PaymentSubscriptionResultService getPaymentSubscriptionResultService()
	{
		return paymentSubscriptionResultService;
	}

	@Required
	public void setPaymentSubscriptionResultService(final PaymentSubscriptionResultService paymentSubscriptionResultService)
	{
		this.paymentSubscriptionResultService = paymentSubscriptionResultService;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
