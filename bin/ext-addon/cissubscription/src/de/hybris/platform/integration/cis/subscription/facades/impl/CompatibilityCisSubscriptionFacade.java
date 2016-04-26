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
package de.hybris.platform.integration.cis.subscription.facades.impl;

import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.cissubscription.data.CisEmailPreference;
import de.hybris.platform.cissubscription.data.CisPayNowTransactionMode;
import de.hybris.platform.cissubscription.data.CisSubscriptionUpdateAction;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.integration.cis.subscription.service.CisSubscriptionService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.subscriptionfacades.action.SubscriptionUpdateActionEnum;
import de.hybris.platform.subscriptionfacades.data.OrderEntryPriceData;
import de.hybris.platform.subscriptionfacades.data.OrderPriceData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionBillingData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionBillingDetailFileStream;
import de.hybris.platform.subscriptionfacades.data.SubscriptionData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPaymentData;
import de.hybris.platform.subscriptionfacades.exceptions.SubscriptionFacadeException;
import de.hybris.platform.subscriptionfacades.impl.AbstractSubscriptionFacade;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.cis.api.model.AnnotationHashMap;
import com.hybris.cis.api.model.AnnotationHashMapEntryType;
import com.hybris.cis.api.model.CisAddress;
import com.hybris.cis.api.model.CisLineItem;
import com.hybris.cis.api.subscription.model.CisChangePaymentMethodRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionChangeStateRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionData;
import com.hybris.cis.api.subscription.model.CisSubscriptionItem;
import com.hybris.cis.api.subscription.model.CisSubscriptionOrder;
import com.hybris.cis.api.subscription.model.CisSubscriptionOrderPostRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionPayNowRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionProfileRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionProfileResult;
import com.hybris.cis.api.subscription.model.CisSubscriptionReplacePaymentMethodRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionSessionFinalizeRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionSessionInitRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionTransactionResult;
import com.hybris.cis.api.subscription.model.CisSubscriptionUpdateRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionUpgradeRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionUpgradeResult;
import com.hybris.commons.client.RestResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;


/**
 * CIS specific extension of the {@link AbstractSubscriptionFacade}.
 */
@Deprecated
public class CompatibilityCisSubscriptionFacade extends AbstractSubscriptionFacade
{

	private static final Logger LOG = Logger.getLogger(CompatibilityCisSubscriptionFacade.class);

	private CisSubscriptionService cisSubscriptionService;
	private Converter<RestResponse, SubscriptionPaymentData> cisPaymentConverter;
	private Converter<AddressData, CisAddress> cisAddressConverter;
	private Converter<AbstractOrderData, CisSubscriptionOrder> cisSubscriptionOrderConverter;
	private Converter<OrderEntryData, CisLineItem> cisLineItemConverter;
	private Converter<CreditCardPaymentInfoModel, CCPaymentInfoData> creditCardPaymentInfoConverter;
	private Converter<CustomerModel, CustomerData> customerConverter;
	private Converter<CisSubscriptionData, SubscriptionData> cisSubscriptionConverter;
	private Converter<Map<String, String>, CCPaymentInfoData> ccPaymentInfoDataConverter;
	private Converter<RestResponse, CCPaymentInfoData> cisCCPaymentInfoDataConverter;
	private BaseSiteService baseSiteService;
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;
	private Converter<OrderEntryData, CisSubscriptionItem> cisSubscriptionItemConverter;

	@Override
	@Nonnull
	public SubscriptionPaymentData updateProfile(@Nullable final Map<String, String> parameters) throws SubscriptionFacadeException
	{
		final CustomerData customer = getCustomerConverter().convert(getCustomerResolutionService().getCurrentCustomer());

		AddressData address = customer.getDefaultShippingAddress();
		if (address == null)
		{
			address = new AddressData();
			address.setFirstName(customer.getFirstName());
			address.setLastName(customer.getLastName());
		}

		final CisSubscriptionProfileRequest profileRequest = new CisSubscriptionProfileRequest();
		profileRequest.setEmailAddress(customer.getUid());
		profileRequest.setProfileId(getCurrentCustomerId());
		profileRequest.setCurrency(getCurrencyIso());
		profileRequest.setShippingAddress(getCisAddressConverter().convert(address));
		profileRequest.setParameters(convertVendorParameters(parameters));
		profileRequest.setCompany(null);
		profileRequest.setCustomerName(customer.getName());
		profileRequest.setEmailPreference(CisEmailPreference.PLAINTEXT.name().toLowerCase());
		profileRequest.setLanguagePreference((customer.getLanguage() == null) ? "en" : customer.getLanguage().getIsocode());

		final RestResponse<CisSubscriptionProfileResult> response = getCisSubscriptionService().updateProfile(
				getCisClientRef(null), profileRequest);

		return getCisPaymentConverter().convert(response);
	}

	@Override
	public SubscriptionPaymentData createSubscriptions(@Nonnull final OrderData order, final Map<String, String> parameters)
			throws SubscriptionFacadeException
	{
		try
		{
			final List<SubscriptionPaymentData> cisResults = new ArrayList<SubscriptionPaymentData>();

			if (hasNewSubscriptions(order))
			{
				cisResults.add(processSubscription(order, parameters));
			}

			if (hasPayNowPrice(order))
			{
				cisResults.add(processPayNow(order, parameters));
			}

			if (hasSubscriptionUpgrades(order))
			{
				cisResults.addAll(processSubscriptionUpgrades(order, parameters));
			}

			return combineCisResponseDataResults(cisResults);
		}
		catch (final Exception e)
		{
			LOG.error("An unexpected exception occured during the createSubscriptions call", e);
			throw new SubscriptionFacadeException("An unexpected exception occured during the createSubscriptions call", e);
		}
	}


	@Nonnull
	@Override
	public SubscriptionPaymentData initializeTransaction(final String clientIpAddress, final String returnUrl,
			final String cancelReturnUrl, final Map<String, String> parameters) throws SubscriptionFacadeException
	{
		if (StringUtils.isEmpty(returnUrl) || StringUtils.isEmpty(cancelReturnUrl))
		{
			throw new SubscriptionFacadeException("String parameters returnUrl and cancelReturnUrl may not be empty");
		}

		final CisSubscriptionSessionInitRequest initRequest = new CisSubscriptionSessionInitRequest();
		initRequest.setReturnUrl(returnUrl);
		initRequest.setCancelReturnUrl(cancelReturnUrl);
		initRequest.setIpAddress(clientIpAddress);
		initRequest.setMerchantAccountId(getCurrentCustomerId());
		initRequest.setParameters(convertVendorParameters(parameters));

		SubscriptionPaymentData paymentData;
		try
		{
			final RestResponse<CisSubscriptionTransactionResult> response = getCisSubscriptionService().initializeTransaction(
					getCisClientRef(null), initRequest);
			paymentData = getCisPaymentConverter().convert(response); // NOPMD
		}
		catch (final Exception e)
		{
			LOG.error("An unexpected exception occured during the initializeTransaction call", e);
			throw new SubscriptionFacadeException("An unexpected exception occured during the initializeTransaction call", e);
		}

		return paymentData;
	}

	@Override
	public String hpfUrl() throws SubscriptionFacadeException
	{
		final RestResponse<Void> hpfUrlResponse = getCisSubscriptionService().hpfUrl(getCisClientRef(null));

		if (hpfUrlResponse == null || hpfUrlResponse.getLocation() == null)
		{
			throw new SubscriptionFacadeException("The hpfUrl call did not return a valid response");
		}

		try
		{
			return hpfUrlResponse.getLocation().toURL().toString();
		}
		catch (final MalformedURLException | IllegalArgumentException e)
		{
			throw new SubscriptionFacadeException("A malformed or non absolute URL was returned", e);
		}
	}

	@Override
	@Nonnull
	public SubscriptionPaymentData finalizeTransaction(@Nullable final String authorizationRequestId,
													   @Nullable final String authorizationRequestToken,
													   @Nullable final Map<String, String> parameters) throws SubscriptionFacadeException
	{
		final CisSubscriptionSessionFinalizeRequest finalizeRequest = new CisSubscriptionSessionFinalizeRequest();
		finalizeRequest.setAuthorizationRequestId(authorizationRequestId);
		finalizeRequest.setAuthorizationRequestToken(authorizationRequestToken);
		finalizeRequest.setParameters(convertVendorParameters(parameters));

		RestResponse<CisSubscriptionTransactionResult> response = null;
		try
		{
			response = getCisSubscriptionService().finalizeTransaction(getCisClientRef(null), finalizeRequest);
		}
		catch (final Exception e)
		{
			LOG.error("An unexpected exception occured during the finalizeTransaction call", e);
			throw new SubscriptionFacadeException(String.format("An unexpected exception occured: %s", e.getMessage()), e);
		}

		if (response == null)
		{
			LOG.error("The finalizeTransaction response is null");
			throw new SubscriptionFacadeException("No response from subscription billing provider");
		}

		return getCisPaymentConverter().convert(response);
	}

	@Override
	@Nonnull
	public CCPaymentInfoData changePaymentMethod(@Nonnull final CCPaymentInfoData paymentInfo,@Nullable final String action,
			@Nullable final boolean propagate, final Map<String, String> parameters) throws SubscriptionFacadeException
	{
		final CisChangePaymentMethodRequest changeRequest = new CisChangePaymentMethodRequest();
		changeRequest.setMerchantPaymentMethodId(getMerchantPaymentMethodIdFromPaymentInfo(paymentInfo));
		changeRequest.setAction(action);
		changeRequest.setPropagate(propagate);
		changeRequest.setParameters(convertVendorParameters(parameters));

		final RestResponse<CisSubscriptionTransactionResult> response = getCisSubscriptionService().changePaymentMethod(
				getCisClientRef(null), changeRequest);

		return getCisCCPaymentInfoDataConverter().convert(response);
	}

	protected String getMerchantPaymentMethodIdFromPaymentInfo(final CCPaymentInfoData paymentInfo)
	{
		final CreditCardPaymentInfoModel paymentInfoModel = getModelService().get(PK.parse(paymentInfo.getId()));
		return paymentInfoModel.getSubscriptionId();
	}

	@Override
	@Nonnull
	public SubscriptionPaymentData replacePaymentMethod(@Nullable final String subscriptionId,@Nullable final String paymentMethodId,
														@Nullable final Map<String, String> parameters) throws SubscriptionFacadeException
	{
		final CisSubscriptionReplacePaymentMethodRequest replaceRequest = new CisSubscriptionReplacePaymentMethodRequest();
		replaceRequest.setMerchantSubscriptionId(subscriptionId);
		replaceRequest.setMerchantPaymentMethodId(paymentMethodId);
		replaceRequest.setParameters(convertVendorParameters(parameters));

		final RestResponse<CisSubscriptionTransactionResult> response = getCisSubscriptionService().replacePaymentMethod(
				getCisClientRef(null), replaceRequest);

		return getCisPaymentConverter().convert(response);
	}


	@Override
	@Nonnull
	public SubscriptionPaymentData updateSubscription(final String subscriptionId, final boolean force,
			final SubscriptionUpdateActionEnum action, final Map<String, String> parameters) throws SubscriptionFacadeException
	{
		final RestResponse<CisSubscriptionTransactionResult> response = getCisSubscriptionService().updateSubscription(
				getCisClientRef(null), subscriptionId, force, CisSubscriptionUpdateAction.valueOf(action.name()));

		return getCisPaymentConverter().convert(response);
	}

	@Override
	@Nonnull
	public SubscriptionData updateSubscriptionAutorenewal(final String subscriptionId, final boolean isAutorenewal,
			final Map<String, String> parameters) throws SubscriptionFacadeException
	{
		final CisSubscriptionUpdateRequest updateRequest = new CisSubscriptionUpdateRequest();
		updateRequest.setMerchantSubscriptionId(subscriptionId);
		updateRequest.setParameters(convertVendorParameters(parameters));
		updateRequest.setEffectiveFrom(getSubscriptionEffectiveFrom());
		updateRequest.setAutoRenewal(Boolean.valueOf(isAutorenewal));
		final RestResponse<CisSubscriptionData> response = getCisSubscriptionService().updateSubscription(getCisClientRef(null),
				updateRequest);
		response.getResult();
		return getCisSubscriptionConverter().convert(response.getResult());
	}

	@Override
	@Nonnull
	public SubscriptionData extendSubscriptionTermDuration(@Nullable final String subscriptionId,@Nullable final Integer contractDurationExtension,
														   @Nullable final Map<String, String> parameters) throws SubscriptionFacadeException
	{
		final CisSubscriptionUpdateRequest updateRequest = new CisSubscriptionUpdateRequest();
		updateRequest.setMerchantSubscriptionId(subscriptionId);
		updateRequest.setParameters(convertVendorParameters(parameters));
		updateRequest.setEffectiveFrom(getSubscriptionEffectiveFrom());
		updateRequest.setContractDurationExtension(contractDurationExtension);
		final RestResponse<CisSubscriptionData> response = getCisSubscriptionService().updateSubscription(getCisClientRef(null),
				updateRequest);
		response.getResult();
		return getCisSubscriptionConverter().convert(response.getResult());
	}

	@Override
	@Nonnull
	public Collection<SubscriptionData> getSubscriptions() throws SubscriptionFacadeException
	{
		final RestResponse<CisSubscriptionProfileResult> response = getCisSubscriptionService().getCustomerProfile(
				getCisClientRef(null), getCurrentCustomerId());

		final CisSubscriptionProfileResult result = response.getResult();

		if (result != null && isNotEmpty(result.getSubscriptions()))
		{
			return Converters.convertAll(result.getSubscriptions(), getCisSubscriptionConverter());
		}

		return Collections.EMPTY_LIST;
	}

	@Override
	@Nullable
	public SubscriptionData getSubscription(final String subscriptionId) throws SubscriptionFacadeException
	{
		final Collection<SubscriptionData> subscriptions = getSubscriptions();

		if (isNotEmpty(subscriptions))
		{
			for (final SubscriptionData subscription : subscriptions)
			{
				if (StringUtils.equals(subscriptionId, subscription.getId()))
				{
					return subscription;
				}
			}
		}

		return null;
	}

	@Override
	@Nonnull
	public SubscriptionData changeSubscriptionState(final String subscriptionId, final String newStatus,
			final Map<String, String> parameters) throws SubscriptionFacadeException
	{
		final CisSubscriptionChangeStateRequest request = new CisSubscriptionChangeStateRequest();
		request.setEffectiveFrom(getSubscriptionEffectiveFrom());
		request.setMerchantSubscriptionId(subscriptionId);
		request.setNewState(newStatus);
		request.setParameters(convertVendorParameters(parameters));

		final RestResponse<CisSubscriptionData> response = getCisSubscriptionService().changeSubscriptionState(
				getCisClientRef(null), request);
		return getCisSubscriptionConverter().convert(response.getResult());
	}

	@Override
	@Nonnull
	public CCPaymentInfoData createPaymentSubscription(final Map<String, String> paymentParameters)
			throws SubscriptionFacadeException
	{
		final CCPaymentInfoData paymentInfoData = new CCPaymentInfoData();
		getCcPaymentInfoDataConverter().convert(paymentParameters, paymentInfoData);
		try
		{
			final CCPaymentInfoData newPaymentSubscription = getCheckoutFacade().createPaymentSubscription(paymentInfoData);

			final CreditCardPaymentInfoModel paymentInfoModel = getModelService().get(PK.parse(newPaymentSubscription.getId()));
			paymentInfoModel.setSubscriptionId(paymentParameters.get("merchantPaymentMethodId"));
			paymentInfoModel.setSaved(true);
			getModelService().save(paymentInfoModel);

			return newPaymentSubscription;
		}
		catch (final ModelSavingException e)
		{
			LOG.error("Saving the new payment info failed", e);
			throw new SubscriptionFacadeException("Saving the new payment info failed", e);
		}
	}

	protected SubscriptionPaymentData processSubscription(final AbstractOrderData orderData, final Map<String, String> parameters)
			throws SubscriptionFacadeException
	{
		final CisSubscriptionOrder cisSubscriptionOrder = getCisSubscriptionOrderConverter().convert(orderData);
		cisSubscriptionOrder.setMerchantAccountId(orderData.getMerchantCustomerId());

		final CisSubscriptionOrderPostRequest request = new CisSubscriptionOrderPostRequest();
		request.setSubscriptionOrder(cisSubscriptionOrder);
		request.setMerchantAccountId(orderData.getMerchantCustomerId());
		request.setParameters(convertVendorParameters(parameters));
		if (orderData.getPaymentInfo() != null)
		{
			request.setMerchantPaymentMethodId(orderData.getPaymentInfo().getSubscriptionId());
		}

		final RestResponse<CisSubscriptionTransactionResult> response = getCisSubscriptionService().post(orderData.getGuid(),
				request);

		return getCisPaymentConverter().convert(response);
	}

	/**
	 * Processes the entries that upgrade an existing subscription to another subscription product
	 *
	 */
	protected List<SubscriptionPaymentData> processSubscriptionUpgrades(final AbstractOrderData orderData,
			final Map<String, String> parameters) throws SubscriptionFacadeException
	{
		final List<SubscriptionPaymentData> subscriptionPayments = new ArrayList<SubscriptionPaymentData>();

		for (final OrderEntryData orderEntryData : orderData.getEntries())
		{
			if (StringUtils.isEmpty(orderEntryData.getOriginalSubscriptionId()))
			{
				continue;
			}

			final CisSubscriptionUpgradeRequest request = new CisSubscriptionUpgradeRequest();
			request.setMerchantAccountId(orderData.getMerchantCustomerId());
			request.setCurrency(getCurrencyIso());
			request.setMerchantPaymentMethodId(getMerchantPaymentMethodIdFromPaymentInfo(orderData.getPaymentInfo()));
			request.setOrderId(orderData.getCode());
			request.setOrderDate(orderData.getCreationTime());
			request.setEffectiveFrom(getSubscriptionEffectiveFrom());
			request.setMerchantSourceSubscriptionId(orderEntryData.getOriginalSubscriptionId());
			request.setMerchantAccountId(orderData.getMerchantCustomerId());
			request.setSubscriptionItem(getCisSubscriptionItemConverter().convert(orderEntryData));
			request.setSettlement(getSubscriptionSettlement());
			request.setPreview(false);
			request.setParameters(convertVendorParameters(parameters));

			final RestResponse<CisSubscriptionUpgradeResult> response = getCisSubscriptionService().upgradeSubscription(
					orderData.getGuid(), request);
			subscriptionPayments.add(getCisPaymentConverter().convert(response));
		}

		return subscriptionPayments;
	}

	protected SubscriptionPaymentData processPayNow(final AbstractOrderData orderData, final Map<String, String> parameters)
			throws SubscriptionFacadeException
	{
		if (orderData == null || CollectionUtils.isEmpty(orderData.getOrderPrices()))
		{
			return getCisPaymentConverter().convert(null);
		}

		final CisSubscriptionPayNowRequest payNowRequest = new CisSubscriptionPayNowRequest();
		final PriceData payNowPrice = getPayNowPrice(orderData);
		payNowRequest.setAmount(payNowPrice.getValue());
		payNowRequest.setCurrency(payNowPrice.getCurrencyIso());
		payNowRequest.setMerchantTransactionDescription(generateMerchantTransactionDescription(orderData,
				orderData.getMerchantCustomerId()));
		payNowRequest.setMerchantTransactionId(generateMerchantTransactionId(orderData, orderData.getMerchantCustomerId()));
		payNowRequest.setParameters(convertVendorParameters(parameters));
		payNowRequest.setLineItems(convertCisLineItems(orderData));
		if (orderData.getPaymentInfo() != null)
		{
			payNowRequest.setPaymentMethodId(orderData.getPaymentInfo().getSubscriptionId());
		}
		payNowRequest.setProfileId(orderData.getMerchantCustomerId());
		payNowRequest.setTransactionMode(CisPayNowTransactionMode.AUTHCAPTURE.name());

		final RestResponse<CisSubscriptionTransactionResult> response = getCisSubscriptionService().processPayNow(
				orderData.getGuid(), payNowRequest);
		return getCisPaymentConverter().convert(response);
	}

	protected AnnotationHashMap convertVendorParameters(final Map<String, String> parameters)
	{
		final AnnotationHashMap annotationHashMap = new AnnotationHashMap();

		if (MapUtils.isNotEmpty(parameters))
		{
			final List<AnnotationHashMapEntryType> entryList = new ArrayList<AnnotationHashMapEntryType>();
			for (final Map.Entry<String, String> entry : parameters.entrySet())
			{
				final AnnotationHashMapEntryType annotationEntry = new AnnotationHashMapEntryType(entry);
				entryList.add(annotationEntry);
			}
			annotationHashMap.setEntries(entryList);
		}

		return annotationHashMap;
	}

	protected boolean hasPayNowPrice(final AbstractOrderData orderData)
	{
		final PriceData payNowPrice = getPayNowPrice(orderData);

		if (payNowPrice == null || BigDecimal.valueOf(0.00).equals(payNowPrice.getValue()))
		{
			return false;
		}

		return true;
	}

	protected PriceData getPayNowPrice(final AbstractOrderData orderData)
	{
		for (final OrderPriceData curPrice : orderData.getOrderPrices())
		{
			if ("paynow".equalsIgnoreCase(curPrice.getBillingTime().getCode()))
			{
				return curPrice.getTotalPrice();
			}
		}

		return null;
	}

	/**
	 * Needs to be overridden with provider specific logic to return the correct value for effectiveFrom
	 *
	 * @return value for {@link CisSubscriptionUpgradeRequest#setEffectiveFrom(String)}
	 */
	protected String getSubscriptionEffectiveFrom()
	{
		return "NEXT";
	}

	/**
	 * Needs to be overridden with provider specific logic to return the correct value for settlement
	 *
	 * @return value for {@link CisSubscriptionUpgradeRequest#setSettlement(String)}
	 */
	protected String getSubscriptionSettlement()
	{
		return "none";
	}

	protected boolean hasNewSubscriptions(final AbstractOrderData orderData)
	{
		for (final OrderEntryData entry : orderData.getEntries())
		{
			if (entry.getProduct().getSubscriptionTerm() != null && StringUtils.isEmpty(entry.getOriginalSubscriptionId()))
			{
				return true;
			}
		}
		return false;
	}

	protected boolean hasSubscriptionUpgrades(final AbstractOrderData orderData)
	{
		for (final OrderEntryData entry : orderData.getEntries())
		{
			if (StringUtils.isNotEmpty(entry.getOriginalSubscriptionId()))
			{
				return true;
			}
		}
		return false;
	}

	protected SubscriptionPaymentData combineCisResponseDataResults(final List<SubscriptionPaymentData> cisResults)
	{
		if (CollectionUtils.isEmpty(cisResults))
		{
			return getCisPaymentConverter().convert(null);
		}
		else
		{
			return cisResults.iterator().next();
		}
	}

	protected List<CisLineItem> convertCisLineItems(final AbstractOrderData orderData)
	{
		final List<CisLineItem> lineItems = new ArrayList<CisLineItem>();
		for (final OrderEntryData entry : orderData.getEntries())
		{
			for (final OrderEntryPriceData entryPrice : entry.getOrderEntryPrices())
			{
				if (entryPrice.getBillingTime() != null && "paynow".equalsIgnoreCase(entryPrice.getBillingTime().getCode())
						&& BigDecimal.valueOf(0.00).compareTo(entryPrice.getTotalPrice().getValue()) == -1)
				{
					lineItems.add(getCisLineItemConverter().convert(entry));
					break;
				}
			}
		}

		return lineItems;
	}

	protected String generateMerchantTransactionId(final AbstractOrderData orderData, final String customerId)
	{
		return orderData.getCode() + "_" + customerId;
	}

	protected String generateMerchantTransactionDescription(final AbstractOrderData orderData, final String customerId)
	{
		return "PayNow amount for order " + generateMerchantTransactionId(orderData, customerId);
	}

	protected String getCisClientRef(final AbstractOrderData orderData)
	{
		AbstractOrderData curOrderData = orderData;
		if (curOrderData == null)
		{
			curOrderData = getCheckoutFacade().getCheckoutCart();
		}
		if (curOrderData != null && StringUtils.isNotEmpty(curOrderData.getGuid()))
		{
			return curOrderData.getGuid();
		}
		if (getCurrentCustomerId() != null)
		{

			return getCurrentCustomerId();
		}
		return UUID.randomUUID().toString();
	}

	protected CisSubscriptionService getCisSubscriptionService()
	{
		return cisSubscriptionService;
	}

	@Required
	public void setCisSubscriptionService(final CisSubscriptionService cisSubscriptionService)
	{
		this.cisSubscriptionService = cisSubscriptionService;
	}

	protected Converter<RestResponse, SubscriptionPaymentData> getCisPaymentConverter()
	{
		return cisPaymentConverter;
	}

	@Required
	public void setCisPaymentConverter(final Converter<RestResponse, SubscriptionPaymentData> cisPaymentConverter)
	{
		this.cisPaymentConverter = cisPaymentConverter;
	}

	protected Converter<AddressData, CisAddress> getCisAddressConverter()
	{
		return cisAddressConverter;
	}

	@Required
	public void setCisAddressConverter(final Converter<AddressData, CisAddress> cisAddressConverter)
	{
		this.cisAddressConverter = cisAddressConverter;
	}

	protected Converter<AbstractOrderData, CisSubscriptionOrder> getCisSubscriptionOrderConverter()
	{
		return cisSubscriptionOrderConverter;
	}

	@Required
	public void setCisSubscriptionOrderConverter(
			final Converter<AbstractOrderData, CisSubscriptionOrder> cisSubscriptionOrderConverter)
	{
		this.cisSubscriptionOrderConverter = cisSubscriptionOrderConverter;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	protected SiteBaseUrlResolutionService getSiteBaseUrlResolutionService()
	{
		return siteBaseUrlResolutionService;
	}

	@Required
	public void setSiteBaseUrlResolutionService(final SiteBaseUrlResolutionService siteBaseUrlResolutionService)
	{
		this.siteBaseUrlResolutionService = siteBaseUrlResolutionService;
	}

	protected Converter<OrderEntryData, CisLineItem> getCisLineItemConverter()
	{
		return cisLineItemConverter;
	}

	@Required
	public void setCisLineItemConverter(final Converter<OrderEntryData, CisLineItem> cisLineItemConverter)
	{
		this.cisLineItemConverter = cisLineItemConverter;
	}

	@Override
	protected Converter<CreditCardPaymentInfoModel, CCPaymentInfoData> getCreditCardPaymentInfoConverter()
	{
		return creditCardPaymentInfoConverter;
	}

	@Override
	@Required
	public void setCreditCardPaymentInfoConverter(
			final Converter<CreditCardPaymentInfoModel, CCPaymentInfoData> creditCardPaymentInfoConverter)
	{
		this.creditCardPaymentInfoConverter = creditCardPaymentInfoConverter;
	}

	protected Converter<Map<String, String>, CCPaymentInfoData> getCcPaymentInfoDataConverter()
	{
		return ccPaymentInfoDataConverter;
	}

	@Required
	public void setCcPaymentInfoDataConverter(final Converter<Map<String, String>, CCPaymentInfoData> ccPaymentInfoDataConverter)
	{
		this.ccPaymentInfoDataConverter = ccPaymentInfoDataConverter;
	}

	protected Converter<CustomerModel, CustomerData> getCustomerConverter()
	{
		return customerConverter;
	}

	@Required
	public void setCustomerConverter(final Converter<CustomerModel, CustomerData> customerConverter)
	{
		this.customerConverter = customerConverter;
	}

	protected Converter<CisSubscriptionData, SubscriptionData> getCisSubscriptionConverter()
	{
		return cisSubscriptionConverter;
	}

	@Required
	public void setCisSubscriptionConverter(final Converter<CisSubscriptionData, SubscriptionData> cisSubscriptionConverter)
	{
		this.cisSubscriptionConverter = cisSubscriptionConverter;
	}

	protected Converter<OrderEntryData, CisSubscriptionItem> getCisSubscriptionItemConverter()
	{
		return cisSubscriptionItemConverter;
	}

	@Required
	public void setCisSubscriptionItemConverter(final Converter<OrderEntryData, CisSubscriptionItem> cisSubscriptionItemConverter)
	{
		this.cisSubscriptionItemConverter = cisSubscriptionItemConverter;
	}


	@Nonnull
	@Override
	public SubscriptionData replacePaymentMethod(final String subscriptionId, final String paymentMethodId,
			final String effectiveFrom, final Map<String, String> parameters) throws SubscriptionFacadeException
	{
		LOG.debug("No-op implementation.");
		return new SubscriptionData();
	}


	@Override
	public List<SubscriptionBillingData> getBillingActivityList(final String subscriptionId, final Map<String, String> parameters)
			throws SubscriptionFacadeException
	{
		LOG.debug("No-op implementation.");
		return new ArrayList<>();
	}


	@Override
	public SubscriptionBillingDetailFileStream getBillingActivityDetail(final String billingActivityId,
			final Map<String, String> parameters) throws SubscriptionFacadeException
	{
		LOG.debug("No-op implementation.");
		return new SubscriptionBillingDetailFileStream();
	}


	@Override
	public List<SubscriptionBillingData> getUpgradePreviewBillings(final String subscriptionId, final String upgradeId)
			throws SubscriptionFacadeException
	{
		LOG.debug("No-op implementation.");
		return new ArrayList<>();
	}

	protected Converter<RestResponse, CCPaymentInfoData> getCisCCPaymentInfoDataConverter()
	{
		return cisCCPaymentInfoDataConverter;
	}

	@Required
	public void setCisCCPaymentInfoDataConverter(final Converter<RestResponse, CCPaymentInfoData> cisCCPaymentInfoDataConverter)
	{
		this.cisCCPaymentInfoDataConverter = cisCCPaymentInfoDataConverter;
	}
}
