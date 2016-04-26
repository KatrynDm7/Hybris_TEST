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
package de.hybris.platform.commerceservices.order.impl;

import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.commerceservices.order.CommercePaymentAuthorizationStrategy;
import de.hybris.platform.commerceservices.order.hook.AuthorizePaymentMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.strategies.GenerateMerchantTransactionCodeStrategy;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.payment.PaymentService;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


public class DefaultCommercePaymentAuthorizationStrategy implements CommercePaymentAuthorizationStrategy
{

	private GenerateMerchantTransactionCodeStrategy generateMerchantTransactionCodeStrategy;
	private I18NService i18nService;
	private PaymentService paymentService;
	private ModelService modelService;
	private List<AuthorizePaymentMethodHook> authorizePaymentHooks;
	private ConfigurationService configurationService;


	@Override
	public PaymentTransactionEntryModel authorizePaymentAmount(final CommerceCheckoutParameter parameter)
	{
		final CartModel cartModel = parameter.getCart();
		final BigDecimal amount = parameter.getAuthorizationAmount();
		final String securityCode = parameter.getSecurityCode();
		final String paymentProvider = parameter.getPaymentProvider();

		PaymentTransactionEntryModel transactionEntryModel = null;
		try
		{
			beforeAuthorizePaymentAmount(parameter);
			final PaymentInfoModel paymentInfo = cartModel.getPaymentInfo();
			if (paymentInfo instanceof CreditCardPaymentInfoModel
					&& StringUtils.isNotBlank(((CreditCardPaymentInfoModel) paymentInfo).getSubscriptionId()))
			{
				final Currency currency = getI18nService().getBestMatchingJavaCurrency(cartModel.getCurrency().getIsocode());
				final String merchantTransactionCode = getGenerateMerchantTransactionCodeStrategy().generateCode(cartModel);
				transactionEntryModel = getPaymentService().authorize(merchantTransactionCode, amount, currency,
						cartModel.getDeliveryAddress(), ((CreditCardPaymentInfoModel) paymentInfo).getSubscriptionId(), securityCode,
						paymentProvider);
				if (transactionEntryModel != null)
				{
					final PaymentTransactionModel paymentTransaction = transactionEntryModel.getPaymentTransaction();

					if (TransactionStatus.ACCEPTED.name().equals(transactionEntryModel.getTransactionStatus())
							|| TransactionStatus.REVIEW.name().equals(transactionEntryModel.getTransactionStatus()))
					{
						paymentTransaction.setOrder(cartModel);
						paymentTransaction.setInfo(paymentInfo);
						getModelService().saveAll(cartModel, paymentTransaction);
					}
					else
					{
						// TransactionStatus is error or reject remove the PaymentTransaction and TransactionEntry
						getModelService().removeAll(Arrays.asList(paymentTransaction, transactionEntryModel));
					}
				}
			}
			return transactionEntryModel;
		}
		finally
		{
			afterAuthorizePaymentAmount(parameter, transactionEntryModel);
		}
	}

	protected void beforeAuthorizePaymentAmount(final CommerceCheckoutParameter parameter)
	{
		if (getAuthorizePaymentHooks() != null
				&& (parameter.isEnableHooks() && getConfigurationService().getConfiguration().getBoolean(
						CommerceServicesConstants.AUTHORIZEPAYMENTHOOK_ENABLED, true)))
		{
			for (final AuthorizePaymentMethodHook athorizePaymentMethodHook : getAuthorizePaymentHooks())
			{
				athorizePaymentMethodHook.beforeAuthorizePaymentAmount(parameter);
			}
		}
	}

	protected void afterAuthorizePaymentAmount(final CommerceCheckoutParameter parameter,
			final PaymentTransactionEntryModel paymentTransactionEntryModel)
	{
		if (getAuthorizePaymentHooks() != null
				&& (parameter.isEnableHooks() && getConfigurationService().getConfiguration().getBoolean(
						CommerceServicesConstants.AUTHORIZEPAYMENTHOOK_ENABLED, true)))
		{
			for (final AuthorizePaymentMethodHook athorizePaymentMethodHook : getAuthorizePaymentHooks())
			{
				athorizePaymentMethodHook.afterAuthorizePaymentAmount(parameter, paymentTransactionEntryModel);
			}
		}
	}

	protected GenerateMerchantTransactionCodeStrategy getGenerateMerchantTransactionCodeStrategy()
	{
		return generateMerchantTransactionCodeStrategy;
	}

	@Required
	public void setGenerateMerchantTransactionCodeStrategy(
			final GenerateMerchantTransactionCodeStrategy generateMerchantTransactionCodeStrategy)
	{
		this.generateMerchantTransactionCodeStrategy = generateMerchantTransactionCodeStrategy;
	}

	protected I18NService getI18nService()
	{
		return i18nService;
	}

	@Required
	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected PaymentService getPaymentService()
	{
		return paymentService;
	}

	@Required
	public void setPaymentService(final PaymentService paymentService)
	{
		this.paymentService = paymentService;
	}

	protected List<AuthorizePaymentMethodHook> getAuthorizePaymentHooks()
	{
		return authorizePaymentHooks;
	}

	public void setAuthorizePaymentHooks(final List<AuthorizePaymentMethodHook> authorizePaymentHooks)
	{
		this.authorizePaymentHooks = authorizePaymentHooks;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
}
