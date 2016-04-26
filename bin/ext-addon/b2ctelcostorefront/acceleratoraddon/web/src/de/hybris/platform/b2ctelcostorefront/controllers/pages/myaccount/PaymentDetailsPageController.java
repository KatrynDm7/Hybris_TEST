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
package de.hybris.platform.b2ctelcostorefront.controllers.pages.myaccount;

import de.hybris.platform.acceleratorfacades.payment.PaymentFacade;
import de.hybris.platform.acceleratorservices.enums.CheckoutPciOptionEnum;
import de.hybris.platform.acceleratorservices.payment.data.PaymentData;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractCheckoutController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.AddressForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.PaymentDetailsForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.SopPaymentDetailsForm;
import de.hybris.platform.b2ctelcostorefront.controllers.TelcoControllerConstants;
import de.hybris.platform.b2ctelcostorefront.forms.PaymentSubscriptionsForm;
import de.hybris.platform.b2ctelcostorefront.forms.validation.SbgSopPaymentDetailsValidator;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CardTypeData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.TitleData;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.subscriptionfacades.SubscriptionFacade;
import de.hybris.platform.subscriptionfacades.billing.CreditCardFacade;
import de.hybris.platform.subscriptionfacades.data.SubscriptionData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPaymentData;
import de.hybris.platform.subscriptionfacades.exceptions.SubscriptionFacadeException;
import de.hybris.platform.subscriptionservices.enums.SubscriptionStatus;
import de.hybris.platform.util.Config;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 *
 * B2cTelco My Account Payment Details Controller.
 *
 */
@Controller
@RequestMapping(PaymentDetailsPageController.CONTROLLER_CTXT)
public class PaymentDetailsPageController extends AbstractCheckoutController
{
	/**
	 *
	 */
	private static final String B2CTELCOCHECKOUTADDON = "addon:/b2ctelcocheckoutaddon/";

	/**
	 * This controller's default base context URI.
	 */
	static final String CONTROLLER_CTXT = "/my-account/my-payment-details";

	protected static final Logger LOG = Logger.getLogger(PaymentDetailsPageController.class);

	private static final String SILENT_ORDER_POST_PAGE = B2CTELCOCHECKOUTADDON + "pages/checkout/multi/silentOrderPostPage";
	private static final String EDIT_PM_SILENT_ORDER_POST_PAGE = B2CTELCOCHECKOUTADDON
			+ "pages/checkout/multi/editPaymentMethodSilentOrderPostPage";
	protected static final String PAYMENT_DETAILS_CMS_PAGE = "payment-details";


	// Internal Redirects
	private static final String REDIRECT_TO_PAYMENT_INFO_PAGE = REDIRECT_PREFIX + CONTROLLER_CTXT;
	private static final String REDIRECT_TO_PAYMENTMETHOD_EDIT_PAGE = REDIRECT_PREFIX + "/paymentDetails/edit-payment-details";
	private static final String REDIRECT_TO_MANAGE_PAYMENT_METHOD = REDIRECT_PREFIX + CONTROLLER_CTXT + "/manage";
	private static final String REDIRECT_TO_EDIT_PAYMENT_METHOD = REDIRECT_PREFIX + CONTROLLER_CTXT + "/edit?paymentInfoId=";

	//CMS Pages
	private static final String ADD_EDIT_ADDRESS_CMS_PAGE = "add-edit-address";
	private static final String PAYMENT_DETAILS_MANAGE_SUBSCRIPTIONS_CMS_PAGE = "payment-details-manage-subscriptions";

	private static final String CURRENT_PAYMENT_INFO_ID = PaymentDetailsPageController.class.getName() + ".paymentInfoId";

	private static Map<String, String> cybersourceSopCardTypes;


	@Resource(name = "subscriptionFacade")
	private SubscriptionFacade subscriptionFacade;

	@Resource(name = "customerFacade")
	protected CustomerFacade customerFacade;

	@Resource(name = "acceleratorCheckoutFacade")
	private CheckoutFacade checkoutFacade;

	@Resource(name = "accountBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	@Resource(name = "siteBaseUrlResolutionService")
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;

	@Resource(name = "creditCardFacade")
	private CreditCardFacade creditCardFacade;

	/** Copied from de.hybris.platform.storefront.controllers.pages.checkout.steps.AbstractCheckoutStepController class. */
	@Resource(name = "paymentFacade")
	private PaymentFacade paymentFacade;

	@Resource(name = "sopResponseUrlStrategy")
	private SopResponseUrlStrategy sopResponseUrlStrategy;

	@Resource(name = "myAccountPaymentDetailsView")
	private String myAccountPaymentDetailsView;

	@Resource(name = "myAccountPaymentDetailsAddView")
	private String myAccountPaymentDetailsAddView = SILENT_ORDER_POST_PAGE;

	@Resource(name = "myAccountSbgSopPaymentDetailsValidator")
	private SbgSopPaymentDetailsValidator myAccountSopPaymentDetailsValidator;


	@ModelAttribute("titles")
	public Collection<TitleData> getTitles()
	{
		return getUserFacade().getTitles();
	}

	@ModelAttribute("countries")
	public Collection<CountryData> getCountries()
	{
		return getCheckoutFacade().getDeliveryCountries();
	}

	@ModelAttribute("billingCountries")
	public Collection<CountryData> getBillingCountries()
	{
		return getCheckoutFacade().getBillingCountries();
	}

	@ModelAttribute("cardTypes")
	public Collection<CardTypeData> getCardTypes()
	{
		final Collection<CardTypeData> creditCards = getCheckoutFacade().getSupportedCardTypes();
		getCreditCardFacade().mappingStrategy(creditCards);

		return creditCards;
	}

	@ModelAttribute("months")
	public List<SelectOption> getMonths()
	{
		final List<SelectOption> months = new ArrayList<SelectOption>();

		months.add(new SelectOption("01", "01"));
		months.add(new SelectOption("02", "02"));
		months.add(new SelectOption("03", "03"));
		months.add(new SelectOption("04", "04"));
		months.add(new SelectOption("05", "05"));
		months.add(new SelectOption("06", "06"));
		months.add(new SelectOption("07", "07"));
		months.add(new SelectOption("08", "08"));
		months.add(new SelectOption("09", "09"));
		months.add(new SelectOption("10", "10"));
		months.add(new SelectOption("11", "11"));
		months.add(new SelectOption("12", "12"));

		return months;
	}

	@ModelAttribute("startYears")
	public List<SelectOption> getStartYears()
	{
		final List<SelectOption> startYears = new ArrayList<SelectOption>();
		final Calendar calender = new GregorianCalendar();

		for (int i = calender.get(Calendar.YEAR); i > (calender.get(Calendar.YEAR) - 6); i--)
		{
			startYears.add(new SelectOption(String.valueOf(i), String.valueOf(i)));
		}

		return startYears;
	}

	@ModelAttribute("expiryYears")
	public List<SelectOption> getExpiryYears()
	{
		final List<SelectOption> expiryYears = new ArrayList<SelectOption>();
		final Calendar calender = new GregorianCalendar();

		for (int i = calender.get(Calendar.YEAR); i < (calender.get(Calendar.YEAR) + 11); i++)
		{
			expiryYears.add(new SelectOption(String.valueOf(i), String.valueOf(i)));
		}

		return expiryYears;
	}


	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String beginAddPayment(final Model model) throws CMSItemNotFoundException
	{

		final CheckoutPciOptionEnum subscriptionPciOption = getCheckoutFlowFacade().getSubscriptionPciOption();

		setupAddPaymentPage(model);

		String view = TelcoControllerConstants.Views.Pages.Account.AccountPaymentInfoPage;

		if (CheckoutPciOptionEnum.SOP.equals(subscriptionPciOption))
		{
			// Build up the SOP form data and render page containing form
			try
			{
				final SopPaymentDetailsForm sopPaymentDetailsForm = new SopPaymentDetailsForm();
				setupSilentOrderPostPage(sopPaymentDetailsForm, model);
				view = getMyAccountPaymentDetailsAddView();
			}
			catch (final Exception e)
			{
				LOG.error("Failed to build beginCreateSubscription request", e);
				GlobalMessages.addErrorMessage(model, "checkout.multi.paymentMethod.addPaymentDetails.generalError");
			}
		}
		return view;
	}



	protected void setupSilentOrderPostPage(final SopPaymentDetailsForm sopPaymentDetailsForm, final Model model)
			throws SubscriptionFacadeException
	{
		model.addAttribute("sopPaymentDetailsForm", sopPaymentDetailsForm);
		try
		{

			final PaymentData silentOrderPageData = getPaymentFacade().beginSopCreateSubscription(getSopResponseUrl(),
					"/integration/merchant_callback");
			model.addAttribute("silentOrderPageData", silentOrderPageData);
			sopPaymentDetailsForm.setParameters(silentOrderPageData.getParameters());
			model.addAttribute("paymentFormUrl", silentOrderPageData.getPostUrl());

			initialiseSubscriptionTransaction(silentOrderPageData);


		}
		catch (final IllegalArgumentException e)
		{
			model.addAttribute("paymentFormUrl", "");
			model.addAttribute("silentOrderPageData", null);
			LOG.warn("Failed to set up silent order post page " + e.getMessage());
			GlobalMessages.addErrorMessage(model, "checkout.multi.sop.globalError");
		}

		final CartData cartData = getCheckoutFacade().getCheckoutCart();
		model.addAttribute("silentOrderPostForm", new PaymentDetailsForm());
		model.addAttribute("cartData", cartData);
		model.addAttribute("deliveryAddress", cartData.getDeliveryAddress());
		model.addAttribute("paymentInfos", getUserFacade().getCCPaymentInfos(true));
		model.addAttribute("sopCardTypes", getSopCardTypes());
		if (StringUtils.isNotBlank(sopPaymentDetailsForm.getBillTo_country()))
		{
			model.addAttribute("regions", getI18NFacade().getRegionsForCountryIso(sopPaymentDetailsForm.getBillTo_country()));
			model.addAttribute("country", sopPaymentDetailsForm.getBillTo_country());
		}
	}


	/**
	 * @param silentOrderPageData
	 * @throws SubscriptionFacadeException
	 */
	private void initialiseSubscriptionTransaction(final PaymentData silentOrderPageData) throws SubscriptionFacadeException
	{
		final String sopResponseUrl = silentOrderPageData.getPostUrl();
		Assert.notNull(sopResponseUrl, "SOP Response URL may not be null");
		final String subscriptionRestUrl = getSubscriptionFacade().hpfUrl();
		Assert.notNull(subscriptionRestUrl, "Subscription Rest URL may not be null");

		final String clientIpAddress = getClientIpAddr();
		final SubscriptionPaymentData initResult = getSubscriptionFacade().initializeTransaction(clientIpAddress,
				subscriptionRestUrl, sopResponseUrl, new HashMap<String, String>());
		final String sessionToken = initResult.getParameters().get("sessionTransactionToken");

		Assert.notNull(sessionToken, "Session token may not be null");

		getSessionService().setAttribute("authorizationRequestId", clientIpAddress);
		getSessionService().setAttribute("authorizationRequestToken", sessionToken);
	}



	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String doEditPaymentDetails(final Model model, @RequestParam("paymentInfoId") final String paymentInfoId)
			throws CMSItemNotFoundException
	{
		if (StringUtils.isBlank(paymentInfoId))
		{
			LOG.warn("Payment method id may not be blank.");
			return TelcoControllerConstants.Views.Pages.Account.AccountPaymentInfoPage;
		}

		final String clientIp = getClientIpAddr();
		setupAddPaymentPage(model);

		model.addAttribute("metaRobots", "no-index,no-follow");
		model.addAttribute("isEditMode", "true");
		model.addAttribute("isFirstPaymentMethod", Boolean.valueOf(getUserFacade().getCCPaymentInfos(true).size() == 1));

		final BaseSiteModel currentBaseSite = getBaseSiteService().getCurrentBaseSite();

		final String fullResponseUrl = getSiteBaseUrlResolutionService().getWebsiteUrlForSite(currentBaseSite, true,
				CONTROLLER_CTXT + "/editPaymentMethod");
		try
		{
			final URIBuilder responseUri = new URIBuilder(fullResponseUrl);
			responseUri.addParameter("editMode", "true");
			responseUri.addParameter("paymentInfoId", paymentInfoId);
			model.addAttribute("paymentFormUrl", responseUri.toString());
		}
		catch (final URISyntaxException e)
		{
			LOG.error(e);
		}

		getSessionService().setAttribute(CURRENT_PAYMENT_INFO_ID, paymentInfoId);
		final CCPaymentInfoData ccPaymentInfoData = getUserFacade().getCCPaymentInfoForCode(paymentInfoId);
		final String country = determineCurrentCountryIso(model, ccPaymentInfoData);
		adjustMonthFormat(ccPaymentInfoData);
		//re-use the ajax form logic.
		getCountryAddressForm(country, model);


		// Build up the SOP form data and render page containing form
		if (Config.getBoolean("accelerator.storefront.checkout.multistep.sop", false))
		{
			if (null == ccPaymentInfoData)
			{
				GlobalMessages.addErrorMessage(model, "text.account.paymentDetails.nonExisting.error");
				return TelcoControllerConstants.Views.Pages.Account.AccountPaymentInfoPage;
			}

			final SopPaymentDetailsForm sopPaymentDetailsForm = getSopPaymentDetailsForm(model);

			try
			{
				setupSilentOrderPostPage(sopPaymentDetailsForm, model, clientIp, ccPaymentInfoData);

				return EDIT_PM_SILENT_ORDER_POST_PAGE;
			}
			catch (final Exception e)
			{
				LOG.error("Failed to setup payment details form", e);
				GlobalMessages.addErrorMessage(model, "checkout.multi.paymentMethod.addPaymentDetails.generalError");
				model.addAttribute("sopPaymentDetailsForm", sopPaymentDetailsForm);

				return TelcoControllerConstants.Views.Pages.Account.AccountPaymentInfoPage;
			}
		}
		else
		{
			// If not using HOP we need to build up the payment details form
			final PaymentDetailsForm paymentDetailsForm = new PaymentDetailsForm();
			final AddressForm addressForm = new AddressForm();
			paymentDetailsForm.setBillingAddress(addressForm);
			model.addAttribute(paymentDetailsForm);

			return TelcoControllerConstants.Views.Pages.Account.AccountPaymentInfoPage;
		}
	}

	/**
	 * for telco months are displayed with a leading zero.
	 *
	 * @param ccPaymentInfoData
	 */
	private void adjustMonthFormat(final CCPaymentInfoData ccPaymentInfoData)
	{
		if (ccPaymentInfoData.getExpiryMonth() != null && ccPaymentInfoData.getExpiryMonth().length() == 1)
		{
			ccPaymentInfoData.setExpiryMonth("0" + ccPaymentInfoData.getExpiryMonth());
		}
		if (ccPaymentInfoData.getStartMonth() != null && ccPaymentInfoData.getStartMonth().length() == 1)
		{
			ccPaymentInfoData.setStartMonth("0" + ccPaymentInfoData.getStartMonth());
		}
	}

	/**
	 * @param model
	 * @param ccPaymentInfoData
	 * @return
	 */
	private String determineCurrentCountryIso(final Model model, final CCPaymentInfoData ccPaymentInfoData)
	{
		String country = getSopPaymentDetailsForm(model).getBillTo_country();
		final boolean allowNullCountry = model.containsAttribute("countryValidationErrorAfterPost") ? ((Boolean) model.asMap().get(
				"countryValidationErrorAfterPost")).booleanValue() : false;
		if (StringUtils.isEmpty(country) && !allowNullCountry)
		{
			//get the default value for the first page load:
			country = ccPaymentInfoData.getBillingAddress().getCountry().getIsocode();
		}
		return country;
	}

	@RequestMapping(value = "/editPaymentMethod", method = RequestMethod.POST)
	public String editPaymentMethod(@Valid @ModelAttribute("sopPaymentDetailsForm") final SopPaymentDetailsForm form,
			final BindingResult bindingResult, @RequestParam("paymentInfoId") final String paymentInfoId, final Model model,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		getSessionService().removeAttribute(CURRENT_PAYMENT_INFO_ID);
		try
		{
			getMyAccountSopPaymentDetailsValidator().validate(form, bindingResult);

			if (bindingResult.hasErrors())
			{
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
						"checkout.error.paymentmethod.formentry.invalid");

				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.sopPaymentDetailsForm",
						bindingResult);
				redirectAttributes.addFlashAttribute("sopPaymentDetailsForm", bindingResult.getTarget());
				redirectAttributes.addFlashAttribute("countryValidationErrorAfterPost",
						Boolean.valueOf(StringUtils.isEmpty(form.getBillTo_country())));
				return REDIRECT_TO_EDIT_PAYMENT_METHOD + paymentInfoId;
			}

			final CCPaymentInfoData ccPaymentInfoData = setupCCPaymentInfoData(form, paymentInfoId);
			if (null != ccPaymentInfoData)
			{
				final CCPaymentInfoData result = getSubscriptionFacade().changePaymentMethod(ccPaymentInfoData, null, true, null);

				// enrich result data with form data, which is not provided from the facade call
				result.setId(paymentInfoId);
				result.getBillingAddress().setTitleCode(ccPaymentInfoData.getBillingAddress().getTitleCode());
				result.setStartMonth(ccPaymentInfoData.getStartMonth());
				result.setStartYear(ccPaymentInfoData.getStartYear());
				result.setIssueNumber(ccPaymentInfoData.getIssueNumber());
				result.getBillingAddress().setRegion(
						getI18NFacade().getRegion(form.getBillTo_country(), form.getBillTo_country() + "-" + form.getBillTo_state()));

				getUserFacade().updateCCPaymentInfo(result);

				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
						"text.account.paymentDetails.editSuccessful");
			}
			else
			{
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
						"text.account.paymentDetails.nonExisting.error");
			}
		}
		catch (final SubscriptionFacadeException e)
		{
			LOG.error("Creating a new payment method failed", e);
			GlobalMessages.addErrorMessage(model, "checkout.multi.paymentMethod.addPaymentDetails.incomplete");
			return EDIT_PM_SILENT_ORDER_POST_PAGE;
		}
		catch (final ModelSavingException e)
		{
			LOG.error("Creating a new payment method failed", e);
			GlobalMessages.addErrorMessage(model, "checkout.multi.paymentMethod.addPaymentDetails.incomplete");
			return EDIT_PM_SILENT_ORDER_POST_PAGE;
		}

		return REDIRECT_PREFIX + CONTROLLER_CTXT;
	}

	/**
	 * Populate data for, and display the Billing address form.
	 *
	 * Copied and slightly modified from
	 * {@link SopPaymentResponseController#getCountryAddressForm(String, boolean, Model)}
	 *
	 * @param countryIsoCode
	 * @param paymentInfoId
	 *           the current paymentInfoId
	 * @param model
	 * @return The billing address form view.
	 */
	@RequestMapping(value = "/edit/billingaddressform", method = RequestMethod.GET)
	public String getCountryAddressForm(@RequestParam("countryIsoCode") final String countryIsoCode, final Model model)
	{

		//		final String paymentInfoId = String.valueOf(getSessionService().getAttribute(CURRENT_PAYMENT_INFO_ID));

		model.addAttribute("supportedCountries", getCountries());
		if (countryIsoCode != null)
		{
			model.addAttribute("regions", getI18NFacade().getRegionsForCountryIso(countryIsoCode));
			model.addAttribute("country", countryIsoCode);
		}


		final SopPaymentDetailsForm sopPaymentDetailsForm = getSopPaymentDetailsForm(model);
		model.addAttribute("sopPaymentDetailsForm", sopPaymentDetailsForm);
		sopPaymentDetailsForm.setBillTo_country(countryIsoCode);
		return TelcoControllerConstants.Views.Fragments.Checkout.BillingAddressForm;
	}

	@RequestMapping(value = "/payment-method-subscriptions", method = RequestMethod.GET)
	public String viewPaymentMethodSubscriptions(@RequestParam(value = "paymentInfoId") final String paymentMethodId,
			final Model model) throws CMSItemNotFoundException
	{
		if (StringUtils.isBlank(paymentMethodId))
		{
			LOG.warn("Payment method id may not be blank.");
			return REDIRECT_TO_PAYMENT_INFO_PAGE;
		}

		final CCPaymentInfoData ccPaymentInfoData = getUserFacade().getCCPaymentInfoForCode(paymentMethodId);
		model.addAttribute("paymentInfo", ccPaymentInfoData);

		try
		{
			final List<SubscriptionData> paymentSubscriptions = getNonCancelledSubscriptionsForPayment(ccPaymentInfoData
					.getSubscriptionId());

			if (paymentSubscriptions.size() > 0)
			{
				model.addAttribute("subscriptions", paymentSubscriptions);
				return TelcoControllerConstants.Views.Pages.Account.AccountPaymentMethodSubscriptionsPage;
			}

		}
		catch (final SubscriptionFacadeException e)
		{
			LOG.error("Error while retrieving subscriptions", e);
		}

		return REDIRECT_TO_PAYMENTMETHOD_EDIT_PAGE;
	}

	@RequestMapping(value = "/check-remove", method = RequestMethod.GET)
	public String checkRemovePaymentMethod(@RequestParam(value = "paymentInfoId") final String paymentMethodId, final Model model)
			throws CMSItemNotFoundException
	{
		if (StringUtils.isBlank(paymentMethodId))
		{
			LOG.warn("Payment method id may not be blank.");
			GlobalMessages.addErrorMessage(model, "text.account.paymentDetails.nonExisting.error");
			return TelcoControllerConstants.Views.Pages.Payment.PaymentNotFound;
		}

		final CCPaymentInfoData ccPaymentInfoData = getUserFacade().getCCPaymentInfoForCode(paymentMethodId);

		if (null == ccPaymentInfoData)
		{
			GlobalMessages.addErrorMessage(model, "text.account.paymentDetails.nonExisting.error");

			return TelcoControllerConstants.Views.Pages.Payment.PaymentNotFound;
		}

		model.addAttribute("paymentInfo", ccPaymentInfoData);

		try
		{
			final List<SubscriptionData> paymentSubscriptions = getNonCancelledSubscriptionsForPayment(ccPaymentInfoData
					.getSubscriptionId());

			if (paymentSubscriptions.size() == 0)
			{
				return TelcoControllerConstants.Views.Pages.Payment.ConfirmDeletePaymentMethodNoSubscriptions;
			}

			GlobalMessages.addErrorMessage(model, "text.account.paymentDetails.warningExistingSubscription.remove");
			model.addAttribute("subscriptions", paymentSubscriptions);
		}
		catch (final SubscriptionFacadeException e)
		{
			LOG.error(String.format("Removing payment method with id %s failed", paymentMethodId), e);
		}

		return TelcoControllerConstants.Views.Pages.Payment.WarningExistingPaymentSubscriptions;
	}

	private SopPaymentDetailsForm getSopPaymentDetailsForm(final Model model)
	{
		SopPaymentDetailsForm sopPaymentDetailsForm = new SopPaymentDetailsForm();
		if (model.containsAttribute("sopPaymentDetailsForm"))
		{
			sopPaymentDetailsForm = (SopPaymentDetailsForm) model.asMap().get("sopPaymentDetailsForm");
		}

		return sopPaymentDetailsForm;
	}

	@RequestMapping(value = "/remove", method = RequestMethod.GET)
	public String removePaymentMethod(@RequestParam(value = "paymentInfoId") final String paymentMethodId,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		final CCPaymentInfoData ccPaymentInfoData = getUserFacade().getCCPaymentInfoForCode(paymentMethodId);

		try
		{
			if (null == ccPaymentInfoData)
			{
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
						"text.account.paymentDetails.nonExisting.error");
			}
			else
			{
				subscriptionFacade.changePaymentMethod(ccPaymentInfoData, "disable", true, new HashMap<String, String>());
				getUserFacade().unlinkCCPaymentInfo(paymentMethodId);
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
						"text.account.profile.paymentCard.removed");
			}
		}
		catch (final SubscriptionFacadeException e)
		{
			LOG.error(String.format("Removing payment method with id %s failed", paymentMethodId), e);

			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					"text.account.paymentDetails.remove.error");
		}

		return getMyAccountPaymentDetailsView();
	}

	@RequestMapping(method = RequestMethod.GET)
	@RequireHardLogIn
	public String paymentDetails(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute("customerData", customerFacade.getCurrentCustomer());
		final List<CCPaymentInfoData> ccPaymentInfos = getUserFacade().getCCPaymentInfos(true);
		model.addAttribute("paymentInfoData", ccPaymentInfos);
		setUpSubscriptionsPerPaymentMethod(model, ccPaymentInfos);
		storeCmsPageInModel(model, getContentPageForLabelOrId(PAYMENT_DETAILS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.paymentDetails"));
		model.addAttribute("metaRobots", "noindex,nofollow");

		addMessageFromSession(model);
		return getViewForPage(model);
	}

	private void addMessageFromSession(final Model model)
	{
		for (final String messageType : new String[]
		{GlobalMessages.CONF_MESSAGES_HOLDER, GlobalMessages.INFO_MESSAGES_HOLDER, GlobalMessages.ERROR_MESSAGES_HOLDER })
		{
			if (getSessionService().getAttribute(messageType) != null)
			{
				GlobalMessages.addConfMessage(model, (String) getSessionService().getAttribute(messageType));
				getSessionService().removeAttribute(messageType);
			}
		}
	}


	@RequestMapping(value = "/set-default", method = RequestMethod.POST)
	@RequireHardLogIn
	public String setDefaultPaymentDetails(@RequestParam final String paymentInfoId)
	{
		CCPaymentInfoData paymentInfoData = null;
		if (StringUtils.isNotBlank(paymentInfoId))
		{
			paymentInfoData = getUserFacade().getCCPaymentInfoForCode(paymentInfoId);
		}
		getUserFacade().setDefaultPaymentInfo(paymentInfoData);
		return REDIRECT_TO_PAYMENT_INFO_PAGE;
	}

	/**
	 * Remove the selected Payment Method.
	 *
	 * @param model
	 *           The MAV model.
	 * @param paymentMethodId
	 *           The payment method Id.
	 * @param redirectAttributes
	 *           The Redirect Attributes object.
	 * @return The view @link {@link #REDIRECT_TO_PAYMENT_INFO_PAGE}
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@RequireHardLogIn
	public String removePaymentMethod(final Model model, @RequestParam(value = "paymentInfoId") final String paymentMethodId,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		getUserFacade().unlinkCCPaymentInfo(paymentMethodId);
		GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
				"text.account.profile.paymentCart.removed");
		return REDIRECT_TO_PAYMENT_INFO_PAGE;
	}

	/**
	 * @param model
	 * @param ccPaymentInfos
	 */
	private void setUpSubscriptionsPerPaymentMethod(final Model model, final List<CCPaymentInfoData> ccPaymentInfos)
	{
		if (CollectionUtils.isNotEmpty(ccPaymentInfos))
		{
			try
			{
				final Collection<SubscriptionData> allSubscriptions = subscriptionFacade.getSubscriptions();
				final HashMap<String, Integer> subscriptionsPerPaymentMethod = new HashMap<String, Integer>();

				for (final CCPaymentInfoData ccPaymentInfoData : ccPaymentInfos)
				{
					subscriptionsPerPaymentMethod.put(ccPaymentInfoData.getId(), Integer.valueOf(0));

					for (final SubscriptionData subscriptionData : allSubscriptions)
					{
						if (StringUtils.equals(subscriptionData.getPaymentMethodId(), ccPaymentInfoData.getSubscriptionId())
								&& !SubscriptionStatus.CANCELLED.getCode().equalsIgnoreCase(subscriptionData.getSubscriptionStatus()))
						{
							Integer amount = Integer.valueOf(1);
							if (subscriptionsPerPaymentMethod.containsKey(ccPaymentInfoData.getId()))
							{
								amount = Integer.valueOf(subscriptionsPerPaymentMethod.get(ccPaymentInfoData.getId()).intValue() + 1);
							}

							subscriptionsPerPaymentMethod.put(ccPaymentInfoData.getId(), amount);
						}
					}
				}

				model.addAttribute("subscriptionsPerPaymentMethod", subscriptionsPerPaymentMethod);
			}
			catch (final SubscriptionFacadeException e)
			{
				LOG.warn(e);
			}
		}

	}


	@RequestMapping(value = "/change-payment-method-subscription", method = RequestMethod.POST)
	public String changePaymentMethodForSubscriptions(@RequestParam(value = "paymentInfoId") final String oldPaymentMethodId,
			final PaymentSubscriptionsForm paymentSubscriptionsForm, final Model model, final RedirectAttributes redirectAttributes)
			throws CMSItemNotFoundException
	{
		if (paymentSubscriptionsForm != null)
		{
			final CCPaymentInfoData ccToDelete = getUserFacade().getCCPaymentInfoForCode(oldPaymentMethodId);
			model.addAttribute("paymentInfo", ccToDelete);

			final CCPaymentInfoData ccPaymentInfoData = getUserFacade().getCCPaymentInfoForCode(
					paymentSubscriptionsForm.getNewPaymentMethodId());

			if (null == ccPaymentInfoData)
			{
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
						"text.account.paymentDetails.nonExisting.error");

				return REDIRECT_TO_PAYMENT_INFO_PAGE;
			}

			if (paymentSubscriptionsForm.getSubscriptionsToChange() != null)
			{
				try
				{
					for (final String subscriptionId : paymentSubscriptionsForm.getSubscriptionsToChange())
					{
						subscriptionFacade.replacePaymentMethod(subscriptionId, ccPaymentInfoData.getSubscriptionId(), null);
					}

					GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
							"text.account.paymentDetails.associatedSubscriptions.changeSuccessful");

				}
				catch (final SubscriptionFacadeException e)
				{
					GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
							"text.account.paymentDetails.manageSubscriptions.unable");
				}
			}
		}

		return REDIRECT_TO_MANAGE_PAYMENT_METHOD + "?paymentInfoId=" + oldPaymentMethodId;
	}

	@RequestMapping(value = "/manage", method = RequestMethod.GET)
	public String managePaymentMethod(@RequestParam(value = "paymentInfoId") final String paymentMethodId, final Model model,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		if (StringUtils.isBlank(paymentMethodId))
		{
			LOG.warn("Payment method id may not be blank.");
			return REDIRECT_TO_PAYMENT_INFO_PAGE;
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(PAYMENT_DETAILS_MANAGE_SUBSCRIPTIONS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PAYMENT_DETAILS_MANAGE_SUBSCRIPTIONS_CMS_PAGE));
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.paymentDetails"));
		model.addAttribute("metaRobots", "no-index,no-follow");

		final CCPaymentInfoData ccPaymentInfoData = getUserFacade().getCCPaymentInfoForCode(paymentMethodId);
		if (null == ccPaymentInfoData)
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					"text.account.paymentDetails.nonExisting.error");
		}
		else
		{
			model.addAttribute("paymentInfo", ccPaymentInfoData);

			try
			{
				final List<SubscriptionData> paymentSubscriptions = getNonCancelledSubscriptionsForPayment(ccPaymentInfoData
						.getSubscriptionId());

				final List<CCPaymentInfoData> paymentInfoDatas = getUserFacade().getCCPaymentInfos(true);
				final List<SelectOption> paymentMethods = new ArrayList<SelectOption>();

				for (final CCPaymentInfoData data : paymentInfoDatas)
				{
					if (!StringUtils.equals(paymentMethodId, data.getId()))
					{
						final StringBuffer paymentInfoString = new StringBuffer();
						paymentInfoString.append(data.getCardTypeData().getName() + ", "
								+ data.getCardNumber().replace("****", "**** ") + ", Exp: " + data.getExpiryMonth() + " / "
								+ data.getExpiryYear());
						paymentMethods.add(new SelectOption(data.getId(), paymentInfoString.toString()));
					}
				}

				model.addAttribute("paymentMethods", paymentMethods);
				model.addAttribute("subscriptions", paymentSubscriptions);
				model.addAttribute("paymentSubscriptionsForm", setupPaymentSubscriptionForm(paymentSubscriptions));

				return getViewForPage(model);
			}
			catch (final SubscriptionFacadeException e)
			{
				LOG.error(String.format("Managing payment method with id %s failed", paymentMethodId), e);
			}
		}

		return REDIRECT_TO_PAYMENT_INFO_PAGE;
	}

	private List<SubscriptionData> getNonCancelledSubscriptionsForPayment(final String paymentMethodId)
			throws SubscriptionFacadeException
	{
		final List<SubscriptionData> subscriptionList = subscriptionFacade.getSubscriptionsForPaymentMethod(paymentMethodId);
		final List<SubscriptionData> nonCanclledsubscriptionList = new ArrayList<SubscriptionData>();

		for (final SubscriptionData subscription : subscriptionList)
		{
			if (!SubscriptionStatus.CANCELLED.getCode().equalsIgnoreCase(subscription.getSubscriptionStatus()))
			{
				nonCanclledsubscriptionList.add(subscription);
			}
		}

		return nonCanclledsubscriptionList;
	}

	/**
	 * Fills up the form used for managing subscriptions.
	 *
	 * @param paymentSubscriptions
	 * @return the filled form
	 */
	private PaymentSubscriptionsForm setupPaymentSubscriptionForm(final List<SubscriptionData> paymentSubscriptions)
	{
		final PaymentSubscriptionsForm paymentSubscriptionsForm = new PaymentSubscriptionsForm();

		if (paymentSubscriptions != null)
		{
			final List<String> subscriptionIds = new ArrayList<String>();
			for (final SubscriptionData subscriptionData : paymentSubscriptions)
			{
				subscriptionIds.add(subscriptionData.getId());
			}

			paymentSubscriptionsForm.setSubscriptionsToChange(subscriptionIds);
		}

		return paymentSubscriptionsForm;
	}

	protected void setupAddPaymentPage(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute("metaRobots", "no-index,no-follow");
		model.addAttribute("hasNoPaymentInfo", Boolean.valueOf(hasNoPaymentInfo()));
		model.addAttribute("cancelUrl", "/my-account/my-payment-details");
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, getAccountBreadcrumbBuilder()
				.getBreadcrumbs("text.account.paymentDetails"));
		final ContentPageModel contentPage = getContentPageForLabelOrId(PAYMENT_DETAILS_CMS_PAGE);
		storeCmsPageInModel(model, contentPage);
		setUpMetaDataForContentPage(model, contentPage);
	}

	protected void setupSilentOrderPostPage(final SopPaymentDetailsForm sopPaymentDetailsForm, final Model model,
			final String clientIpAddress, final CCPaymentInfoData ccPaymentInfoData)
	{

		try
		{
			final String postUrl = getSubscriptionFacade().hpfUrl();
			final SubscriptionPaymentData initResult = getSubscriptionFacade().initializeTransaction(clientIpAddress,
					getSopResponseUrl(), getSopResponseUrl(), new HashMap<String, String>());
			final String sessionToken = initResult.getParameters().get("sessionTransactionToken");

			Assert.notNull(sessionToken, "Session token may not be null");
			Assert.notNull(postUrl, "Post URL may not be null");

			getSessionService().setAttribute("authorizationRequestId", clientIpAddress);
			getSessionService().setAttribute("authorizationRequestToken", sessionToken);

			model.addAttribute("postUrl", postUrl);
			model.addAttribute("sessionToken", sessionToken);
		}
		catch (final SubscriptionFacadeException e)
		{
			model.addAttribute("postUrl", null);
			model.addAttribute("sessionToken", null);
			LOG.warn("Failed to initialize session for silent order post page", e);
			GlobalMessages.addErrorMessage(model, "checkout.multi.sop.globalError");
		}
		catch (final IllegalArgumentException e)
		{
			model.addAttribute("postUrl", null);
			model.addAttribute("sessionToken", null);
			LOG.warn("Failed to set up silent order post page", e);
			GlobalMessages.addErrorMessage(model, "checkout.multi.sop.globalError");
		}

		if (!model.containsAttribute("accErrorMsgs"))
		{
			setupSopPaymentDetailsForm(sopPaymentDetailsForm, ccPaymentInfoData);
		}

		model.addAttribute("sopPaymentDetailsForm", sopPaymentDetailsForm);
		model.addAttribute("paymentInfo", ccPaymentInfoData);
	}

	private void setupSopPaymentDetailsForm(final SopPaymentDetailsForm sopPaymentDetailsForm,
			final CCPaymentInfoData ccPaymentInfoData)
	{
		if (null != ccPaymentInfoData)
		{
			sopPaymentDetailsForm.setCard_accountNumber(ccPaymentInfoData.getCardNumber());
			sopPaymentDetailsForm.setCard_cardType(ccPaymentInfoData.getCardType());
			sopPaymentDetailsForm.setCard_expirationMonth(ccPaymentInfoData.getExpiryMonth());
			sopPaymentDetailsForm.setCard_expirationYear(ccPaymentInfoData.getExpiryYear());
			sopPaymentDetailsForm.setCard_issueNumber(ccPaymentInfoData.getIssueNumber());
			sopPaymentDetailsForm.setCard_nameOnCard(ccPaymentInfoData.getAccountHolderName());
			sopPaymentDetailsForm.setCard_startMonth(ccPaymentInfoData.getStartMonth());
			sopPaymentDetailsForm.setCard_startYear(ccPaymentInfoData.getStartYear());

			setupBillingAddress(sopPaymentDetailsForm, ccPaymentInfoData.getBillingAddress());
		}
	}

	private void setupBillingAddress(final SopPaymentDetailsForm sopPaymentDetailsForm, final AddressData address)
	{
		sopPaymentDetailsForm.setUseDeliveryAddress(false);

		if (null != address)
		{
			sopPaymentDetailsForm.setBillTo_titleCode(address.getTitleCode());
			sopPaymentDetailsForm.setBillTo_firstName(address.getFirstName());
			sopPaymentDetailsForm.setBillTo_lastName(address.getLastName());
			sopPaymentDetailsForm.setBillTo_street1(address.getLine1());
			sopPaymentDetailsForm.setBillTo_street2(address.getLine2());
			sopPaymentDetailsForm.setBillTo_city(address.getTown());
			sopPaymentDetailsForm.setBillTo_postalCode(address.getPostalCode());

			if (null != address.getCountry())
			{
				sopPaymentDetailsForm.setBillTo_country(address.getCountry().getIsocode());
			}
			if (null != address.getRegion())
			{
				sopPaymentDetailsForm.setBillTo_state(address.getRegion().getIsocodeShort());
			}
		}
	}

	protected String getClientIpAddr()
	{
		final ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		final HttpServletRequest request = sra.getRequest();
		String clientIp = request.getHeader("X-Forwarded-For");
		if (StringUtils.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp))
		{
			clientIp = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp))
		{
			clientIp = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp))
		{
			clientIp = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp))
		{
			clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp))
		{
			clientIp = request.getRemoteAddr();
		}
		return clientIp;
	}

	/**
	 * Get the Silent Order Post Response URL.
	 *
	 * @return The silent Order Post URL
	 */
	protected String getSopResponseUrl()
	{

		return getSopResponseUrlStrategy().getUrl();
	}

	protected boolean hasNoPaymentInfo()
	{
		getCheckoutFacade().setPaymentInfoIfAvailable();
		final CartData cartData = getCheckoutFacade().getCheckoutCart();
		return (cartData == null || cartData.getPaymentInfo() == null);
	}


	protected Collection<CardTypeData> getSopCardTypes()
	{
		final Collection<CardTypeData> sopCardTypes = new ArrayList<CardTypeData>();

		final List<CardTypeData> supportedCardTypes = getCheckoutFacade().getSupportedCardTypes();
		for (final CardTypeData supportedCardType : supportedCardTypes)
		{
			// Add credit cards for all supported cards that have mappings for cybersource SOP
			if (cybersourceSopCardTypes.containsKey(supportedCardType.getCode()))
			{
				sopCardTypes.add(createCardTypeData(cybersourceSopCardTypes.get(supportedCardType.getCode()),
						supportedCardType.getName()));
			}
		}
		return sopCardTypes;
	}

	protected CardTypeData createCardTypeData(final String code, final String name)
	{
		final CardTypeData cardTypeData = new CardTypeData();
		cardTypeData.setCode(code);
		cardTypeData.setName(name);
		return cardTypeData;
	}

	private CCPaymentInfoData setupCCPaymentInfoData(final SopPaymentDetailsForm form, final String paymentInfoId)
	{
		final CCPaymentInfoData ccPaymentInfoData = getUserFacade().getCCPaymentInfoForCode(paymentInfoId);

		if (null != form && null != ccPaymentInfoData)
		{
			final AddressData addressData = new AddressData();
			addressData.setFirstName(form.getBillTo_firstName());
			addressData.setLastName(form.getBillTo_lastName());
			addressData.setLine1(form.getBillTo_street1());
			addressData.setLine2(form.getBillTo_street2());
			addressData.setTown(form.getBillTo_city());
			addressData.setPostalCode(form.getBillTo_postalCode());
			addressData.setTitleCode(form.getBillTo_titleCode());
			if (StringUtils.isNotEmpty(form.getBillTo_country()))
			{
				final CountryData countryData = new CountryData();
				countryData.setIsocode(form.getBillTo_country());
				addressData.setCountry(countryData);
				addressData.setRegion(getI18NFacade().getRegion(form.getBillTo_country(),
						form.getBillTo_country() + "-" + form.getBillTo_state()));
			}

			ccPaymentInfoData.setBillingAddress(addressData);
		}
		return ccPaymentInfoData;
	}

	/**
	 * Checks if there are any items in the cart.
	 *
	 * @return returns true if items found in cart.
	 */
	protected boolean hasItemsInCart()
	{
		final CartData cartData = getCheckoutFacade().getCheckoutCart();

		return (cartData.getEntries() != null && !cartData.getEntries().isEmpty());
	}

	protected SubscriptionFacade getSubscriptionFacade()
	{
		return subscriptionFacade;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	protected SiteBaseUrlResolutionService getSiteBaseUrlResolutionService()
	{
		return siteBaseUrlResolutionService;
	}

	protected ResourceBreadcrumbBuilder getAccountBreadcrumbBuilder()
	{
		return accountBreadcrumbBuilder;
	}

	protected CreditCardFacade getCreditCardFacade()
	{
		return creditCardFacade;
	}


	protected PaymentFacade getPaymentFacade()
	{
		return paymentFacade;
	}

	protected void setPaymentFacade(final PaymentFacade paymentFacade)
	{
		this.paymentFacade = paymentFacade;
	}

	protected SopResponseUrlStrategy getSopResponseUrlStrategy()
	{
		return sopResponseUrlStrategy;
	}

	public void setSopResponseUrlStrategy(final SopResponseUrlStrategy sopResponseUrlStrategy)
	{
		this.sopResponseUrlStrategy = sopResponseUrlStrategy;
	}



	/**
	 * @return the myAccountPaymentDetailsView
	 */
	protected String getMyAccountPaymentDetailsView()
	{
		return myAccountPaymentDetailsView;
	}

	/**
	 * @param myAccountPaymentDetailsView
	 *           the myAccountPaymentDetailsView to set
	 */
	public void setMyAccountPaymentDetailsView(final String myAccountPaymentDetailsView)
	{
		this.myAccountPaymentDetailsView = myAccountPaymentDetailsView;
	}

	/**
	 * @return the myAccountPaymentDetailsAddView
	 */
	protected String getMyAccountPaymentDetailsAddView()
	{
		return myAccountPaymentDetailsAddView;
	}

	/**
	 * @param myAccountPaymentDetailsAddView
	 *           the myAccountPaymentDetailsAddView to set
	 */
	public void setMyAccountPaymentDetailsAddView(final String myAccountPaymentDetailsAddView)
	{
		this.myAccountPaymentDetailsAddView = myAccountPaymentDetailsAddView;
	}

	protected SbgSopPaymentDetailsValidator getMyAccountSopPaymentDetailsValidator()
	{
		return myAccountSopPaymentDetailsValidator;
	}

	public void setMyAccountSopPaymentDetailsValidator(final SbgSopPaymentDetailsValidator myAccountSopPaymentDetailsValidator)
	{
		this.myAccountSopPaymentDetailsValidator = myAccountSopPaymentDetailsValidator;
	}

	static
	{
		cybersourceSopCardTypes = new HashMap<String, String>();
		// Map hybris card type to Cybersource SOP credit card
		cybersourceSopCardTypes.put("visa", "001");
		cybersourceSopCardTypes.put("master", "002");
		cybersourceSopCardTypes.put("amex", "003");
		cybersourceSopCardTypes.put("diners", "005");
		cybersourceSopCardTypes.put("maestro", "024");
	}


}
