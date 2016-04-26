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
package de.hybris.platform.acceleratorwebservicesaddon.controllers.v2;

import de.hybris.platform.acceleratorfacades.order.AcceleratorCheckoutFacade;
import de.hybris.platform.acceleratorfacades.payment.data.PaymentSubscriptionResultData;
import de.hybris.platform.acceleratorservices.payment.data.PaymentData;
import de.hybris.platform.acceleratorservices.payment.data.PaymentErrorField;
import de.hybris.platform.acceleratorwebservicesaddon.exceptions.PaymentProviderException;
import de.hybris.platform.acceleratorwebservicesaddon.payment.facade.CommerceWebServicesPaymentFacade;
import de.hybris.platform.acceleratorwebservicesaddon.validator.SopPaymentDetailsValidator;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartModificationDataList;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceDataList;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commercewebservicescommons.dto.order.CartModificationListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.PaymentDetailsWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.payment.PaymentRequestWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.payment.SopPaymentDetailsWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceListWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.commercewebservicescommons.mapping.DataMapper;
import de.hybris.platform.commercewebservicescommons.mapping.FieldSetLevelHelper;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}/carts")
@ApiVersion("v2")
public class ExtendedCartsController
{
	private final static Logger LOG = Logger.getLogger(ExtendedCartsController.class);
	@Resource
	private AcceleratorCheckoutFacade acceleratorCheckoutFacade;
	@Resource(name = "dataMapper")
	protected DataMapper dataMapper;
	@Resource(name = "commerceWebServicesPaymentFacade")
	protected CommerceWebServicesPaymentFacade commerceWebServicesPaymentFacade;
	@Resource(name = "userFacade")
	protected UserFacade userFacade;
	@Resource(name = "checkoutFacade")
	CheckoutFacade checkoutFacade;
	@Resource(name = "sopPaymentDetailsValidator")
	SopPaymentDetailsValidator sopPaymentDetailsValidator;

	/**
	 * Web service handler for getting consolidated pickup options<br>
	 * Request Method = <code>GET</code>
	 *
	 * @return {@link PointOfServiceListWsDTO} as response body
	 */
	@RequestMapping(value = "/{cartId}/consolidate", method = RequestMethod.GET)
	@ResponseBody
	public PointOfServiceListWsDTO getConsolidatedPickupOptions(
			@RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		final PointOfServiceDataList pointOfServices = new PointOfServiceDataList();
		pointOfServices.setPointOfServices(acceleratorCheckoutFacade.getConsolidatedPickupOptions());
		return dataMapper.map(pointOfServices, PointOfServiceListWsDTO.class, fields);
	}

	/**
	 * Web service handler for consolidating pickup locations<br>
	 * Request Method = <code>POST</code>
	 *
	 * @param storeName
	 *           - name of store where items will be picked
	 * @return {@link CartModificationListWsDTO} as response body
	 */
	@RequestMapping(value = "/{cartId}/consolidate", method = RequestMethod.POST)
	@ResponseBody
	public CartModificationListWsDTO consolidatePickupLocations(@RequestParam(required = true) final String storeName,
			@RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
			throws CommerceCartModificationException
	{
		final CartModificationDataList modifications = new CartModificationDataList();
		modifications.setCartModificationList(acceleratorCheckoutFacade.consolidateCheckoutCart(storeName));
		final CartModificationListWsDTO result = dataMapper.map(modifications, CartModificationListWsDTO.class, fields);
		return result;
	}

	/**
	 * Method returns information needed for create subscription contacting directly with payment provider. These
	 * information contains payment provider url and list of parameters needed to create subscription. Parameters are
	 * partially filled with appropriate values.
	 *
	 * @param responseUrl
	 *           Value for orderPage_cancelResponseURL, 'orderPage_declineResponseURL, orderPage_receiptResponseURL
	 * @param baseSiteId
	 *           Base site identifier
	 * @return payment provider url and parameters describing subscription
	 */
	@RequestMapping(value = "/{cartId}/payment/sop/request", method = RequestMethod.GET)
	@ResponseBody
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	public PaymentRequestWsDTO getSopPaymentRequestDetails(@RequestParam(required = true) final String responseUrl,
			@RequestParam(required = false, defaultValue = "false") final boolean extendedMerchantCallback,
			@PathVariable final String baseSiteId, @PathVariable final String userId, @PathVariable final String cartId)
	{
		final PaymentData paymentData = commerceWebServicesPaymentFacade.beginSopCreateSubscription(responseUrl,
				buildMerchantCallbackUrl(extendedMerchantCallback, baseSiteId, userId, cartId));
		final PaymentRequestWsDTO result = dataMapper.map(paymentData, PaymentRequestWsDTO.class, "postUrl,parameters");
		return result;
	}

	/**
	 * Method build merchant callback url for given parameters
	 *
	 * @param extendedMerchantCallback
	 *           Define which url should be returned
	 * @param baseSiteId
	 *           Base site identifier
	 * @param userId
	 *           User identifier
	 * @param cartId
	 *           Cart identifier
	 * @return merchant callback url
	 */
	protected String buildMerchantCallbackUrl(final boolean extendedMerchantCallback, final String baseSiteId,
			final String userId, final String cartId)
	{
		if (extendedMerchantCallback)
		{
			return "/v2/" + baseSiteId + "/integration/users/" + userId + "/carts/" + cartId + "/payment/sop/response";
		}
		else
		{
			return "/v2/" + baseSiteId + "/integration/merchant_callback";
		}
	}

	/**
	 * Method handles response from payment provider and create payment details
	 *
	 * @param request
	 *           Http request
	 * @param sopPaymentDetails
	 *           Information returned by payment provider in create subscription process
	 * @param fields
	 *           Response configuration (list of fields, which should be returned in response)
	 * @return created payment details
	 */
	@RequestMapping(value = "/{cartId}/payment/sop/response", method = RequestMethod.POST)
	@ResponseBody
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	public PaymentDetailsWsDTO handleSopPaymentResponse(final HttpServletRequest request,
			final SopPaymentDetailsWsDTO sopPaymentDetails,
			@RequestParam(required = false, defaultValue = "DEFAULT") final String fields)
	{
		final Errors errors = validate(sopPaymentDetails, "SOP data", sopPaymentDetailsValidator);
		final PaymentSubscriptionResultData paymentSubscriptionResultData = commerceWebServicesPaymentFacade
				.completeSopCreateSubscription(getParameterMap(request), sopPaymentDetails.isSavePaymentInfo());

		final CCPaymentInfoData paymentInfoData = handlePaymentSubscriptionResultData(paymentSubscriptionResultData, errors);
		if (userFacade.getCCPaymentInfos(true).size() <= 1)
		{
			userFacade.setDefaultPaymentInfo(paymentInfoData);
		}
		checkoutFacade.setPaymentDetails(paymentInfoData.getId());

		return dataMapper.map(paymentInfoData, PaymentDetailsWsDTO.class, fields);
	}

	/**
	 * Method analyze payment subscription result data. If create subscription result is success it returns created
	 * payment info. Otherwise appropriate exception is thrown.
	 *
	 * @param paymentSubscriptionResultData
	 *           Data to analyze
	 * @param errors
	 *           Object storing validation errors. Can be null - then empty error object will be created
	 * @return payment info
	 */
	protected CCPaymentInfoData handlePaymentSubscriptionResultData(
			final PaymentSubscriptionResultData paymentSubscriptionResultData, Errors errors)
	{
		if (paymentSubscriptionResultData.isSuccess() && paymentSubscriptionResultData.getStoredCard() != null
				&& StringUtils.isNotBlank(paymentSubscriptionResultData.getStoredCard().getSubscriptionId()))
		{
			return paymentSubscriptionResultData.getStoredCard();
		}
		else if (paymentSubscriptionResultData.getErrors() != null && !paymentSubscriptionResultData.getErrors().isEmpty())
		{
			SopPaymentDetailsWsDTO sopPaymentDetailsWsDTO = null;
			if (errors == null)
			{
				sopPaymentDetailsWsDTO = new SopPaymentDetailsWsDTO();
				errors = new BeanPropertyBindingResult(sopPaymentDetailsWsDTO, "SOP data");
			}

			for (final PaymentErrorField paymentErrorField : paymentSubscriptionResultData.getErrors().values())
			{
				if (paymentErrorField.isMissing())
				{
					LOG.error("Missing: " + paymentErrorField.getName());
					errors.rejectValue(paymentErrorField.getName(), "field.required", "Please enter a value for this field");
				}
				if (paymentErrorField.isInvalid())
				{
					try
					{
						if (sopPaymentDetailsWsDTO != null)
						{
							PropertyUtils.setProperty(sopPaymentDetailsWsDTO, paymentErrorField.getName(), "invalid");
						}
					}
					catch (final Exception e)
					{
						LOG.error(e.toString());
					}
					LOG.error("Invalid: " + paymentErrorField.getName());
					errors.rejectValue(paymentErrorField.getName(), "field.invalid", new Object[]
					{ paymentErrorField.getName() }, "This value is invalid for this field");
				}
			}
			throw new WebserviceValidationException(errors);
		}
		else if (paymentSubscriptionResultData.getDecision() != null
				&& paymentSubscriptionResultData.getDecision().equalsIgnoreCase("error"))
		{
			LOG.error("Failed to create subscription. Error occurred while contacting external payment services.");
			throw new PaymentProviderException("Failed to create subscription. Decision :"
					+ paymentSubscriptionResultData.getDecision(), paymentSubscriptionResultData.getResultCode());
		}
		throw new PaymentProviderException("Failed to create payment details. Decision :"
				+ paymentSubscriptionResultData.getDecision(), paymentSubscriptionResultData.getResultCode());
	}

	/**
	 * Method for getting information about create subscription request results. If there is no response from payment
	 * provider - method will return 202-Accepted status. If subscription was created successfully payment details is
	 * returned. Otherwise error response will be returned.
	 *
	 * @param cartId
	 *           Cart identifier
	 * @param fields
	 *           Response configuration (list of fields, which should be returned in response)
	 * @param response
	 *           Http response
	 * @return payment details related with subscription
	 */
	@RequestMapping(value = "/{cartId}/payment/sop/response", method = RequestMethod.GET)
	@ResponseBody
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	public PaymentDetailsWsDTO getSopPaymentResponse(@PathVariable final String cartId,
			@RequestParam(required = false, defaultValue = "DEFAULT") final String fields, final HttpServletResponse response)
	{
		final PaymentSubscriptionResultData paymentSubscriptionResultData = commerceWebServicesPaymentFacade
				.getPaymentSubscriptionResult(cartId);
		if (paymentSubscriptionResultData == null) //still waiting for payment provider response
		{
			response.setStatus(HttpServletResponse.SC_ACCEPTED);
			return null;
		}

		final CCPaymentInfoData paymentInfoData = handlePaymentSubscriptionResultData(paymentSubscriptionResultData, null);
		return dataMapper.map(paymentInfoData, PaymentDetailsWsDTO.class, fields);
	}


	/**
	 * Method deletes payment provider response related to cart
	 * 
	 * @param cartId
	 *           Cart identifier
	 */
	@RequestMapping(value = "/{cartId}/payment/sop/response", method = RequestMethod.DELETE)
	@ResponseBody
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	public void deleteSopPaymentResponse(@PathVariable final String cartId)
	{
		commerceWebServicesPaymentFacade.removePaymentSubscriptionResult(cartId);
	}

	protected Map<String, String> getParameterMap(final HttpServletRequest request)
	{
		final Map<String, String> map = new HashMap<>();
		final Enumeration myEnum = request.getParameterNames();
		while (myEnum.hasMoreElements())
		{
			final String paramName = (String) myEnum.nextElement();
			final String paramValue = request.getParameter(paramName);
			map.put(paramName, paramValue);
		}
		return map;
	}

	protected Errors validate(final Object object, final String objectName, final Validator validator)
	{
		final Errors errors = new BeanPropertyBindingResult(object, objectName);
		validator.validate(object, errors);
		if (errors.hasErrors())
		{
			throw new WebserviceValidationException(errors);
		}
		return errors;
	}

}
