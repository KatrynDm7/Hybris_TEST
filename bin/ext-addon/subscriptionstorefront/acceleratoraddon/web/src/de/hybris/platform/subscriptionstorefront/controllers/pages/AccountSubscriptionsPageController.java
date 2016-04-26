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
package de.hybris.platform.subscriptionstorefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.subscriptionfacades.SubscriptionFacade;
import de.hybris.platform.subscriptionfacades.action.SubscriptionUpdateActionEnum;
import de.hybris.platform.subscriptionfacades.data.SubscriptionBillingData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionBillingDetailFileStream;
import de.hybris.platform.subscriptionfacades.data.SubscriptionData;
import de.hybris.platform.subscriptionfacades.exceptions.SubscriptionFacadeException;
import de.hybris.platform.subscriptionfacades.order.SubscriptionCartFacade;
import de.hybris.platform.subscrptionstorefront.util.MessageHandler;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Controller for home page.
 */
@Controller
@RequestMapping("/my-account/subscription")
public class AccountSubscriptionsPageController extends AbstractSearchPageController
{

	// Internal Redirects
	private static final String REDIRECT_MY_ACCOUNT = REDIRECT_PREFIX + "/my-account";
	private static final String REDIRECT_URL_CART = REDIRECT_PREFIX + "/cart";
	private static final String REDIRECT_MY_ACCOUNT_SUBSCRIPTION = REDIRECT_PREFIX + "/my-account/subscription/";

	// CMS Pages
	private static final String SUBSCRIPTIONS_CMS_PAGE = "subscriptions";
	private static final String SUBSCRIPTION_COMPARISON_CMS_PAGE = "subscriptionComparison";
	private static final String SUBSCRIPTION_BILLING_ACTIVITY_CMS_PAGE = "subscriptionBillingActivity";
	private static final String SUBSCRIPTION_DETAILS_CMS_PAGE = "subscription";

	/**
	 * We use this suffix pattern because of an issue with Spring 3.1 where a Uri value is incorrectly extracted if it
	 * contains on or more '.' characters. Please see https://jira.springsource.org/browse/SPR-6164 for a discussion on
	 * the issue and future resolution.
	 */
	private static final String SUBSCRIPTION_ID_PATH_VARIABLE_PATTERN = "{subscriptionId:.*}";


	private static final Logger LOG = Logger.getLogger(AccountSubscriptionsPageController.class);

	@Resource(name = "userFacade")
	protected UserFacade userFacade;

	@Resource(name = "accountBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	@Resource(name = "subscriptionFacade")
	private SubscriptionFacade subscriptionFacade;

	@Resource(name = "cartFacade")
	private SubscriptionCartFacade cartFacade;

	@Resource(name = "orderFacade")
	private OrderFacade orderFacade;

	@Resource(name = "productFacade")
	private ProductFacade productFacade;
	
	@Resource(name = "messageHandler")
	private MessageHandler messageHandler;

	@RequestMapping(method = RequestMethod.GET)
	public String subscriptions(@Nonnull final Model model) throws CMSItemNotFoundException
	{

		List<SubscriptionData> sortedSubscriptions = new ArrayList<>();
		try
		{
			final Collection<SubscriptionData> subscriptions = subscriptionFacade.getSubscriptions();

			if (subscriptions != null)
			{
				sortedSubscriptions = new ArrayList<>(subscriptions);
			}
		}
		catch (final SubscriptionFacadeException e)
		{
			LOG.error("Error while retrieving subscriptions", e);
		}

		final List<CCPaymentInfoData> paymentInfoData = userFacade.getCCPaymentInfos(true);

		final Map<String, CCPaymentInfoData> paymentInfoMap = new HashMap<>();

		for (final CCPaymentInfoData paymentInfo : paymentInfoData)
		{
			paymentInfoMap.put(paymentInfo.getSubscriptionId(), paymentInfo);
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(SUBSCRIPTIONS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(SUBSCRIPTIONS_CMS_PAGE));
		model.addAttribute("subscriptionFacade", subscriptionFacade);
		model.addAttribute("paymentInfoMap", paymentInfoMap);
		model.addAttribute("subscriptions", sortedSubscriptions);
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.subscriptions"));
		model.addAttribute("metaRobots", "no-index,no-follow");
		messageHandler.supplementModelWithMessages(model);
		return getViewForPage(model);
	}

	@RequestMapping(value = "/upgrades-comparison", method = RequestMethod.GET)
	public String upgradeSubscriptionComparison(
			@Nonnull @RequestParam(value = "subscriptionId") final String subscriptionId,
			@Nonnull final Model model) throws CMSItemNotFoundException
	{
		try
		{
			// initially leave upgradePreviewData empty because it's filled dynamically in method viewPotentialUpgradeBillingDetails
			final List<SubscriptionBillingData> upgradePreviewData = new ArrayList<>();
			model.addAttribute("upgradePreviewData", upgradePreviewData);

			final SubscriptionData subscriptionDetails = subscriptionFacade.getSubscription(subscriptionId);
			final ProductData subscriptionProductData = getProductForSubscription(subscriptionDetails);
			model.addAttribute("subscriptionProductData", subscriptionProductData);

			final List<ProductData> upsellingOptions = subscriptionFacade.getUpsellingOptionsForSubscription(subscriptionDetails
					.getProductCode());
			model.addAttribute("upsellingOptions", upsellingOptions);

			final List<Breadcrumb> breadcrumbs = buildSubscriptionDetailBreadcrumb(subscriptionDetails);
			model.addAttribute("breadcrumbs", breadcrumbs);

			model.addAttribute("subscriptionData", subscriptionDetails);

			final CartData cartData = cartFacade.getSessionCart();
			model.addAttribute("cartData", cartData);
		}
		catch (final UnknownIdentifierException | SubscriptionFacadeException e)
		{
			LOG.warn("Attempted to load a subscription that does not exist or is not visible", e);
			return REDIRECT_MY_ACCOUNT;
		}
		storeCmsPageInModel(model, getContentPageForLabelOrId(SUBSCRIPTION_COMPARISON_CMS_PAGE));
		model.addAttribute("metaRobots", "no-index,no-follow");
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(SUBSCRIPTION_COMPARISON_CMS_PAGE));

		return getViewForPage(model);
	}

	@RequestMapping(value = "/upgrade", method = RequestMethod.POST)
	public String upgradesubscription(@RequestParam(value = "productCode", required = true) final String productCode,
			@RequestParam(value = "subscriptionId", required = true) final String subscriptionId, final Model model,
			@RequestParam(value = "originalOrderCode", required = true) final String originalOrderCode,
			@RequestParam(value = "originalEntryNumber", required = true) final int originalEntryNumber)
	{
		CartModificationData cartModification;

		try
		{
			cartModification = cartFacade.addToCart(productCode, subscriptionId, originalOrderCode, originalEntryNumber);
			model.addAttribute("modifiedCartData", Collections.singletonList(cartModification));

			if (cartModification.getQuantityAdded() == 0L)
			{
				GlobalMessages.addErrorMessage(model, "basket.information.quantity.noItemsAdded." + cartModification.getStatusCode());
				model.addAttribute("errorMsg", "basket.information.quantity.noItemsAdded." + cartModification.getStatusCode());
			}
			else if (cartModification.getQuantityAdded() < 1)
			{
				GlobalMessages.addErrorMessage(model,
						"basket.information.quantity.reducedNumberOfItemsAdded." + cartModification.getStatusCode());
				model.addAttribute("errorMsg",
						"basket.information.quantity.reducedNumberOfItemsAdded." + cartModification.getStatusCode());
			}

			final CartData cartData = cartFacade.getSessionCart();
			model.addAttribute("cartData", cartData);
		}
		catch (final CommerceCartModificationException e)
		{
			model.addAttribute("errorMsg", "basket.error.occurred");
			GlobalMessages.addErrorMessage(model, "basket.information.quantity.noItemsAdded. " + e.getMessage());
			LOG.warn("Couldn't upgrade subscription '" + subscriptionId + "' to product '" + productCode + "'. AddToCart failed.", e);
		}

		return REDIRECT_URL_CART;
	}

	@RequestMapping(value = "/" + SUBSCRIPTION_ID_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String subscription(@PathVariable("subscriptionId") final String subscriptionId, final Model model)
			throws CMSItemNotFoundException
	{
		try
		{
			model.addAttribute("paymentInfos", userFacade.getCCPaymentInfos(true));

			final SubscriptionData subscriptionDetails = subscriptionFacade.getSubscription(subscriptionId);
			model.addAttribute("subscriptionData", subscriptionDetails);

			final ProductData subscriptionProductData = getProductForSubscription(subscriptionDetails);
			model.addAttribute("subscriptionProductData", subscriptionProductData);

			final List<Breadcrumb> breadcrumbs = buildSubscriptionDetailBreadcrumb(subscriptionDetails);
			model.addAttribute("breadcrumbs", breadcrumbs);

			final List<SelectOption> extensionOptions = new ArrayList<>();
			for (int i = 1; i <= 3; i++)
			{
				final String counter = String.valueOf(i);
				extensionOptions.add(new SelectOption(counter, getMessageSource().getMessage("text.account.subscription.extendTerm",
						new Object[]
						{ subscriptionDetails.getContractFrequency(), counter }, "Extend Subscription by {1} {0}",
						getI18nService().getCurrentLocale())));
			}
			model.addAttribute("extensionOptions", extensionOptions);

			final List<CCPaymentInfoData> paymentMethods = userFacade.getCCPaymentInfos(true);
			model.addAttribute("paymentInfos", paymentMethods);

			final List<ProductData> upsellingOptions = subscriptionFacade.getUpsellingOptionsForSubscription(subscriptionDetails
					.getProductCode());
			model.addAttribute("upgradable", CollectionUtils.isNotEmpty(upsellingOptions));

		}
		catch (final UnknownIdentifierException | SubscriptionFacadeException e)
		{
			LOG.warn("Attempted to load a subscription that does not exist or is not visible", e);
			return REDIRECT_MY_ACCOUNT;
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(SUBSCRIPTION_DETAILS_CMS_PAGE));
		model.addAttribute("metaRobots", "no-index,no-follow");
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(SUBSCRIPTION_DETAILS_CMS_PAGE));
		return getViewForPage(model);
	}

	protected List<Breadcrumb> buildSubscriptionDetailBreadcrumb(final SubscriptionData subscriptionData)
	{
		final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs("text.account.subscriptions");
		breadcrumbs.get(breadcrumbs.size() - 1).setUrl("/my-account/subscriptions");
		breadcrumbs.add(new Breadcrumb("#", getMessageSource().getMessage("text.account.subscription.subscriptionBreadcrumb",
				new Object[] {subscriptionData.getId()}, "Subscription {0}", getI18nService().getCurrentLocale()), null));
		return breadcrumbs;
	}

	@RequestMapping(value = "/set-autorenewal-status", method = RequestMethod.POST)
	public String setAutorenewalStatus(@RequestParam(value = "autorenew", required = true) final boolean autorenew,
			@RequestParam(value = "subscriptionId", required = true) final String subscriptionId,
			final RedirectAttributes redirectAttributes)
	{
		try
		{
			subscriptionFacade.updateSubscriptionAutorenewal(subscriptionId, autorenew, Collections.emptyMap());
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
					"text.account.subscription.changeAutorenew.success");
		}
		catch (final SubscriptionFacadeException e)
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					"text.account.subscription.changeAutorenew.unable");
			LOG.error(String.format("Unable to change auto-renew status to '%s' for subscription '%s'", String.valueOf(autorenew),
					subscriptionId), e);
		}

		return REDIRECT_MY_ACCOUNT_SUBSCRIPTION + subscriptionId;
	}

	@RequestMapping(value = "/change-state", method = RequestMethod.POST)
	public String changeSubscriptionState(@RequestParam(value = "newState", required = true) final String newState,
			@RequestParam(value = "subscriptionId", required = true) final String subscriptionId,
			final RedirectAttributes redirectAttributes)
	{
		try
		{
			subscriptionFacade.changeSubscriptionState(subscriptionId, newState, Collections.emptyMap());
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
					"text.account.subscription.changeState.success");
		}
		catch (final SubscriptionFacadeException e)
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					"text.account.subscription.changeState.unable");
			LOG.error(String.format("Unable to change state to '%s' for subscription '%s'", newState, subscriptionId), e);
		}

		return REDIRECT_MY_ACCOUNT_SUBSCRIPTION + subscriptionId;
	}

	@RequestMapping(value = "/extend-term-duration", method = RequestMethod.POST)
	public String extendSubscriptionTermDuration(
			@RequestParam(value = "contractDurationExtension", required = true) final Integer contractDurationExtension,
			@RequestParam(value = "subscriptionId", required = true) final String subscriptionId,
			final RedirectAttributes redirectAttributes)
	{
		try
		{
			subscriptionFacade.extendSubscriptionTermDuration(subscriptionId, contractDurationExtension, Collections.emptyMap());
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
					"text.account.subscription.extendTerm.success");
		}
		catch (final SubscriptionFacadeException e)
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					"text.account.subscription.extendTerm.unable");
			LOG.error(String.format("Unable to extend term duration by '%s' for subscription '%s'", contractDurationExtension,
					subscriptionId), e);
		}

		return REDIRECT_MY_ACCOUNT_SUBSCRIPTION + subscriptionId;
	}

	@RequestMapping(value = "/billing-activity/payment-method/replace", method = RequestMethod.POST)
	public String replaceSubscriptionPaymentMethod(
			@RequestParam(value = "paymentMethodId", required = true) final String paymentMethodId,
			@RequestParam(value = "subscriptionId", required = true) final String subscriptionId,
			@RequestParam(value = "effectiveFrom", required = true) final String effectiveFrom,
			final RedirectAttributes redirectAttributes)
	{
		try
		{
			subscriptionFacade.replacePaymentMethod(subscriptionId, paymentMethodId, effectiveFrom, Collections.emptyMap());
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
					"text.account.subscription.replacePaymentMethod.success");
		}
		catch (final SubscriptionFacadeException e)
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					"text.account.subscription.replacePaymentMethod.unable");
			LOG.error(String.format("Unable to replace payment method for subscription '%s'", subscriptionId), e);
		}

		return REDIRECT_MY_ACCOUNT_SUBSCRIPTION + subscriptionId;
	}

	@RequestMapping(value = "/cancel/" + SUBSCRIPTION_ID_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String cancelsubscription(@PathVariable(value = "subscriptionId") final String subscriptionId,
			final RedirectAttributes redirectAttributes)
	{
		try
		{
			subscriptionFacade.updateSubscription(subscriptionId, true, SubscriptionUpdateActionEnum.CANCEL, null);
		}
		catch (final SubscriptionFacadeException e)
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					"text.account.subscription.cancel.unable");
			LOG.error("Unable to cancel subscription", e);
		}

		return REDIRECT_MY_ACCOUNT_SUBSCRIPTION + subscriptionId;
	}

	@RequestMapping(value = "/billing-upgrade-details", method = RequestMethod.GET)
	public String viewPotentialUpgradeBillingDetails(@RequestParam(value = "subscriptionId") final String subscriptionId,
			@RequestParam(value = "upgradeId") final String upgradeId, final Model model) throws CMSItemNotFoundException
	{
		try
		{
			final List<SubscriptionBillingData> upgradePreviewData = subscriptionFacade.getUpgradePreviewBillings(subscriptionId,
					upgradeId);
			model.addAttribute("upgradePreviewData", upgradePreviewData);
			model.addAttribute("tabId", upgradeId);

			final SubscriptionData subscriptionDetails = subscriptionFacade.getSubscription(subscriptionId);
			model.addAttribute("subscriptionData", subscriptionDetails);

			final OrderData subscriptionOrderData = orderFacade.getOrderDetailsForCode(subscriptionDetails.getOrderNumber());
			ProductData subscriptionProductData = null;

			for (final OrderEntryData orderEntry : subscriptionOrderData.getEntries())
			{
				if (orderEntry.getEntryNumber().intValue() == subscriptionDetails.getOrderEntryNumber().intValue())
				{
					subscriptionProductData = orderEntry.getProduct();
					model.addAttribute("subProdData", subscriptionProductData);
					break;
				}
			}

			final List<ProductOption> productOptions = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE);
			final ProductData upgradeData = productFacade.getProductForCodeAndOptions(upgradeId, productOptions);
			model.addAttribute("upgradeData", upgradeData);
		}
		catch (final SubscriptionFacadeException e)
		{
			GlobalMessages.addErrorMessage(model, "form.global.error");
			return REDIRECT_MY_ACCOUNT;
		}

		return "addon:/subscriptionstorefront/pages/account/accountUpgradePotentialBillingDetails";
	}

	@RequestMapping(value = "/billing-activity", method = RequestMethod.GET)
	public String viewSubscriptionBillingActivity(
			@RequestParam(value = "subscriptionId", required = true) final String subscriptionId, final Model model)
			throws CMSItemNotFoundException
	{
		try
		{

			final SubscriptionData subscriptionData = subscriptionFacade.getSubscription(subscriptionId);
			model.addAttribute("subscriptionData", subscriptionData);

			final List<Breadcrumb> breadcrumbs = buildSubscriptionDetailBreadcrumb(subscriptionData);
			model.addAttribute("breadcrumbs", breadcrumbs);

			final List<SubscriptionBillingData> billingActivities = subscriptionFacade.getBillingActivityList(subscriptionId,
					Collections.emptyMap());

			Collections.sort(billingActivities, (billingData1, billingData2) -> {
					final DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
					try
					{
						final Date date1 = dateFormat.parse(billingData1.getBillingDate());
						final Date date2 = dateFormat.parse(billingData2.getBillingDate());
						return -date1.compareTo(date2);
					}
					catch (final ParseException e)
					{
						LOG.warn("Unable to parse billing date. Billing activities will probably not be sorted properly", e);
						return 1;
					}
				}
			);

			model.addAttribute("billingActivities", billingActivities);
		}
		catch (final SubscriptionFacadeException e)
		{
			LOG.warn("Viewing billing activities for subscriptions failed. Returning to subscription details page.", e);
			return REDIRECT_MY_ACCOUNT_SUBSCRIPTION + subscriptionId;
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(SUBSCRIPTION_BILLING_ACTIVITY_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(SUBSCRIPTION_BILLING_ACTIVITY_CMS_PAGE));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return getViewForPage(model);
	}

	@RequestMapping(value = "/billing-activity/download", method = RequestMethod.GET)
	public void downloadSubscriptionBillingActivityDetail(
			@RequestParam(value = "billingActivityId", required = true) final String billingActivityId,
			final HttpServletResponse response)
	{
		try
		{
			final SubscriptionBillingDetailFileStream fileStream = subscriptionFacade.getBillingActivityDetail(billingActivityId,
					Collections.emptyMap());

			response.setContentType(fileStream.getMimeType());
			response.setHeader("Content-Disposition", "attachment; filename=" + fileStream.getFileName());

			IOUtils.copy(fileStream.getInputStream(), response.getOutputStream());
			response.flushBuffer();
		}
		catch (final SubscriptionFacadeException | IOException e)
		{
			LOG.warn(String.format("Download of details for billing activity with id %s failed.", billingActivityId), e);
		}

	}


	protected ProductData getProductForSubscription(final SubscriptionData subscriptionDetails)
	{
		final OrderData subscriptionOrderData = orderFacade.getOrderDetailsForCode(subscriptionDetails.getOrderNumber());
		final OrderData orderData = orderFacade.getOrderDetailsForCode(subscriptionOrderData.getCode());
		ProductData productData = null;

		for (final OrderEntryData orderEntry : orderData.getEntries())
		{
			if (orderEntry.getEntryNumber().intValue() == subscriptionDetails.getOrderEntryNumber().intValue())
			{
				productData = orderEntry.getProduct();
				return productData;
			}
		}

		return null;
	}

	/**
	 * Data class used to hold a drop down select option value. Holds the code identifier as well as the display name.
	 */
	public static class SelectOption
	{
		private final String code;
		private final String name;

		public SelectOption(final String code, final String name)
		{
			this.code = code;
			this.name = name;
		}

		public String getCode()
		{
			return code;
		}

		public String getName()
		{
			return name;
		}
	}

}
