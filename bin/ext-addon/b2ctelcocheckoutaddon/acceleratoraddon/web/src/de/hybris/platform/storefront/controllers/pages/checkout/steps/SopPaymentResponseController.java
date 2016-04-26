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
package de.hybris.platform.storefront.controllers.pages.checkout.steps;

import de.hybris.platform.acceleratorfacades.payment.data.PaymentSubscriptionResultData;
import de.hybris.platform.acceleratorservices.payment.data.PaymentErrorField;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.SopPaymentDetailsForm;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.storefront.controllers.ControllerConstants;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPaymentData;
import de.hybris.platform.subscriptionfacades.exceptions.SubscriptionFacadeException;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Silent Order Post (SOP) controller.
 */
@Controller
@RequestMapping(value = "/checkout/multi/sop")
public class SopPaymentResponseController extends PaymentMethodCheckoutStepController
{

	/**
	 * Handle SOP response.
	 *
	 * @param request
	 * @param sopPaymentDetailsForm
	 * @param bindingResult
	 * @param model
	 * @param redirectAttributes
	 * @return the view for the next step in the checkout process, determined by (
	 *         {@link #enterStep(Model, RedirectAttributes)}
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "/response", method = RequestMethod.POST)
	@RequireHardLogIn
	public String doHandleSopResponse(final HttpServletRequest request, @Valid final SopPaymentDetailsForm sopPaymentDetailsForm,
			final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes)
			throws CMSItemNotFoundException
	{
		final Map<String, String> resultMap = getRequestParameterMap(request);

		final boolean savePaymentInfo = true;

		final PaymentSubscriptionResultData paymentSubscriptionResultData = getPaymentFacade().completeSopCreateSubscription(
				resultMap, savePaymentInfo);

		if (paymentSubscriptionResultData.isSuccess() && paymentSubscriptionResultData.getStoredCard() != null
				&& StringUtils.isNotBlank(paymentSubscriptionResultData.getStoredCard().getSubscriptionId()))
		{
			CCPaymentInfoData newPaymentSubscription = paymentSubscriptionResultData.getStoredCard();
			final SubscriptionPaymentData result = subscriptionFinalizeTransaction(sopPaymentDetailsForm, newPaymentSubscription);

			if (result == null)
			{
				//add error message and finish.
				GlobalMessages.addErrorMessage(model, "checkout.multi.paymentMethod.addPaymentDetails.subscriptionError");
				return enterStep(model, redirectAttributes);

			}

			newPaymentSubscription = updateMerchantPaymentMethod(newPaymentSubscription, result.getParameters());
			if (newPaymentSubscription == null)
			{
				//... then the previous call has set it to null!
				//Add error message and finish.
				GlobalMessages.addErrorMessage(model, "checkout.multi.paymentMethod.addPaymentDetails.subscriptionError");
				return enterStep(model, redirectAttributes);
			}


			if (getUserFacade().getCCPaymentInfos(true).size() <= 1)
			{
				getUserFacade().setDefaultPaymentInfo(newPaymentSubscription);
			}
			getCheckoutFacade().setPaymentDetails(newPaymentSubscription.getId());
		}
		else if ((paymentSubscriptionResultData.getDecision() != null && paymentSubscriptionResultData.getDecision()
				.equalsIgnoreCase("error"))
				|| (paymentSubscriptionResultData.getErrors() != null && !paymentSubscriptionResultData.getErrors().isEmpty()))
		{
			// Have SOP errors that we can display

			setupAddPaymentPage(model);

			// Build up the SOP form data and render page containing form
			try
			{
				setupSilentOrderPostPage(sopPaymentDetailsForm, model);
			}
			catch (final Exception e)
			{
				LOG.error("Failed to build beginCreateSubscription request", e);
				GlobalMessages.addErrorMessage(model, "checkout.multi.paymentMethod.addPaymentDetails.generalError");
				return enterStep(model, redirectAttributes);
			}

			if (paymentSubscriptionResultData.getErrors() != null && !paymentSubscriptionResultData.getErrors().isEmpty())
			{
				GlobalMessages.addErrorMessage(model, "checkout.error.paymentethod.formentry.invalid");
				// Add in specific errors for invalid fields
				for (final PaymentErrorField paymentErrorField : paymentSubscriptionResultData.getErrors().values())
				{
					if (paymentErrorField.isMissing())
					{
						bindingResult.rejectValue(paymentErrorField.getName(), "checkout.error.paymentethod.formentry.sop.missing."
								+ paymentErrorField.getName(), "Please enter a value for this field");
					}
					if (paymentErrorField.isInvalid())
					{
						bindingResult.rejectValue(paymentErrorField.getName(), "checkout.error.paymentethod.formentry.sop.invalid."
								+ paymentErrorField.getName(), "This value is invalid for this field");
					}
				}
			}
			else if (paymentSubscriptionResultData.getDecision() != null
					&& paymentSubscriptionResultData.getDecision().equalsIgnoreCase("error"))
			{
				LOG.error("Failed to create subscription. Error occurred while contacting external payment services.");
				GlobalMessages.addErrorMessage(model, "checkout.multi.paymentMethod.addPaymentDetails.generalError");
			}

			return ControllerConstants.Views.Pages.MultiStepCheckout.SilentOrderPostPage;
		}
		else
		{
			// SOP ERROR!
			LOG.error("Failed to create subscription.  Please check the log files for more information");
			return REDIRECT_URL_ERROR + "/?decision=" + paymentSubscriptionResultData.getDecision() + "&reasonCode="
					+ paymentSubscriptionResultData.getResultCode();
		}

		return getCheckoutStep().nextStep();
	}

	/**
	 * Update the Merchant Payment Method ID on the current paymentInfo.
	 *
	 * @param map
	 *           the parameter map from the SOP response
	 * @param paymentInfo
	 *           the currently active paymentInfo data object.
	 * @return an updated paymentInfo object, or null if the update fails.
	 */
	protected CCPaymentInfoData updateMerchantPaymentMethod(final CCPaymentInfoData paymentInfo, final Map<String, String> map)
	{
		CCPaymentInfoData newPaymentSubscription = null; //default: null indicates error.
		try
		{
			newPaymentSubscription = getSubscriptionFacade().updateCreatedPaymentMethod(paymentInfo, map);
		}
		catch (final SubscriptionFacadeException e)
		{
			LOG.error("Failed to build createPaymentSubscription request", e);
		}
		return newPaymentSubscription;
	}

	/**
	 * Finalize the Subscription Transaction.
	 *
	 * @param sopPaymentDetailsForm
	 * @param newPaymentSubscription
	 * @return the new SubscriptionPaymentData, or null if this method fails.
	 */
	protected SubscriptionPaymentData subscriptionFinalizeTransaction(final SopPaymentDetailsForm sopPaymentDetailsForm,
			final CCPaymentInfoData newPaymentSubscription)
	{
		SubscriptionPaymentData result = null;
		try
		{
			final String authorizationRequestId = (String) getSessionService().getAttribute("authorizationRequestId");
			final String authorizationRequestToken = (String) getSessionService().getAttribute("authorizationRequestToken");
			final Map<String, String> createPaymentDetailsMap = createPaymentDetailsMap(newPaymentSubscription);

			result = getSubscriptionFacade().finalizeTransaction(authorizationRequestId, authorizationRequestToken,
					createPaymentDetailsMap);
		}
		catch (final SubscriptionFacadeException e)
		{
			LOG.error("Failed to build finalizeTransaction request", e);
		}
		return result;
	}

	/**
	 * Populate data for, and display the Billing address form.
	 *
	 * @param countryIsoCode
	 * @param useDeliveryAddress
	 * @param model
	 * @return The billing address form view.
	 */
	@RequestMapping(value = "/billingaddressform", method = RequestMethod.GET)
	public String getCountryAddressForm(@RequestParam("countryIsoCode") final String countryIsoCode,
			@RequestParam("useDeliveryAddress") final boolean useDeliveryAddress, final Model model)
	{
		model.addAttribute("supportedCountries", getCountries());
		model.addAttribute("regions", getI18NFacade().getRegionsForCountryIso(countryIsoCode));
		model.addAttribute("country", countryIsoCode);

		final SopPaymentDetailsForm sopPaymentDetailsForm = new SopPaymentDetailsForm();
		model.addAttribute("sopPaymentDetailsForm", sopPaymentDetailsForm);
		if (useDeliveryAddress)
		{
			final AddressData deliveryAddress = getCheckoutFacade().getCheckoutCart().getDeliveryAddress();

			if (deliveryAddress.getRegion() != null && !StringUtils.isEmpty(deliveryAddress.getRegion().getIsocode()))
			{
				sopPaymentDetailsForm.setBillTo_state(deliveryAddress.getRegion().getIsocodeShort());
			}

			sopPaymentDetailsForm.setBillTo_firstName(deliveryAddress.getFirstName());
			sopPaymentDetailsForm.setBillTo_lastName(deliveryAddress.getLastName());
			sopPaymentDetailsForm.setBillTo_street1(deliveryAddress.getLine1());
			sopPaymentDetailsForm.setBillTo_street2(deliveryAddress.getLine2());
			sopPaymentDetailsForm.setBillTo_city(deliveryAddress.getTown());
			sopPaymentDetailsForm.setBillTo_postalCode(deliveryAddress.getPostalCode());
			sopPaymentDetailsForm.setBillTo_country(deliveryAddress.getCountry().getIsocode());
		}
		return ControllerConstants.Views.Fragments.Checkout.BillingAddressForm;
	}

	private Map<String, String> createPaymentDetailsMap(final CCPaymentInfoData payInfo)
	{
		final Map<String, String> map = new HashMap<>();

		// Mask the card number
		final String maskedCardNumber = maskAccountNumber(payInfo.getCardNumber());
		map.put("cardNumber", maskedCardNumber);
		map.put("cardType", payInfo.getCardType());
		map.put("expiryMonth", payInfo.getExpiryMonth());
		map.put("expiryYear", payInfo.getExpiryYear());
		map.put("issueNumber", payInfo.getIssueNumber());
		map.put("nameOnCard", payInfo.getAccountHolderName());
		map.put("startMonth", payInfo.getStartMonth());
		map.put("startYear", payInfo.getStartYear());


		final AddressData billingAddress = payInfo.getBillingAddress();
		map.put("billingAddress_countryIso", billingAddress.getCountry().getIsocode());
		map.put("billingAddress_firstName", billingAddress.getFirstName());
		map.put("billingAddress_titleCode", billingAddress.getTitleCode());
		map.put("billingAddress_lastName", billingAddress.getLastName());
		map.put("billingAddress_line1", billingAddress.getLine1());
		map.put("billingAddress_line2", billingAddress.getLine2());
		map.put("billingAddress_postcode", billingAddress.getPostalCode());
		map.put("billingAddress_townCity", billingAddress.getTown());

		return map;
	}

	private String maskAccountNumber(final String accountNumber)
	{
		String maskedCardNumber = "************";
		if (accountNumber.length() >= 4)
		{
			final String endPortion = accountNumber.trim().substring(accountNumber.length() - 4);
			maskedCardNumber = maskedCardNumber + endPortion;
		}
		return maskedCardNumber;
	}


}
