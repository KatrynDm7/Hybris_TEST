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
package de.hybris.platform.b2bacceleratoraddon.controllers.pages;


import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.util.XSSFilterUtil;
import de.hybris.platform.b2bacceleratoraddon.forms.QuoteOrderForm;
import de.hybris.platform.b2bacceleratoraddon.forms.ReorderForm;
import de.hybris.platform.b2bacceleratorfacades.order.B2BOrderFacade;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BOrderHistoryEntryData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@Scope("tenant")
@RequestMapping(value = "/my-account")
public class MyQuotesController extends AbstractSearchPageController
{
	private static final String REDIRECT_MY_ACCOUNT = REDIRECT_PREFIX + "/my-account";
	private static final String REDIRECT_TO_QUOTES_DETAILS = REDIRECT_PREFIX + "/my-account/my-quote/%s";
	private static final String MY_QUOTES_CMS_PAGE = "my-quotes";
	private static final String QUOTE_DETAIL_CMS_PAGE = "quote-detail";
	private static final String ORDER_CODE_PATH_VARIABLE_PATTERN = "{orderCode:.*}";
	private static final String NEGOTIATEQUOTE = "NEGOTIATEQUOTE";
	private static final String ACCEPTQUOTE = "ACCEPTQUOTE";
	private static final String CANCELQUOTE = "CANCELQUOTE";
	private static final String ADDADDITIONALCOMMENT = "ADDADDITIONALCOMMENT";

	@Resource(name = "b2bOrderFacade")
	private B2BOrderFacade orderFacade;

	@Resource(name = "accountBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	@RequestMapping(value = "/my-quotes", method = RequestMethod.GET)
	@RequireHardLogIn
	public String myQuotes(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final AbstractSearchPageController.ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode, final Model model)
			throws CMSItemNotFoundException
	{
		// Handle paged search results
		final PageableData pageableData = createPageableData(page, 5, sortCode, showMode);
		final SearchPageData<OrderHistoryData> searchPageData = orderFacade.getPagedOrderHistoryForStatuses(pageableData,
				OrderStatus.PENDING_QUOTE, OrderStatus.APPROVED_QUOTE, OrderStatus.REJECTED_QUOTE);
		populateModel(model, searchPageData, showMode);
		model.addAttribute(new ReorderForm());

		final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
		breadcrumbs.add(new Breadcrumb("/my-account/my-quotes", getMessageSource().getMessage(
				"text.account.manageQuotes.breadcrumb", null, getI18nService().getCurrentLocale()), null));
		model.addAttribute("breadcrumbs", breadcrumbs);
		storeCmsPageInModel(model, getContentPageForLabelOrId(MY_QUOTES_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MY_QUOTES_CMS_PAGE));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}

	@RequestMapping(value = "/my-quote/" + ORDER_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	@RequireHardLogIn
	public String quotesDetails(@PathVariable("orderCode") final String orderCode, final Model model)
			throws CMSItemNotFoundException
	{
		try
		{
			final OrderData orderDetails = orderFacade.getOrderDetailsForCode(orderCode);

			model.addAttribute("orderData", orderDetails);

			final List<B2BOrderHistoryEntryData> orderHistoryEntries = orderFacade.getOrderHistoryEntryData(orderCode);
			model.addAttribute("orderHistoryEntryData", orderHistoryEntries);

			model.addAttribute(new ReorderForm());

			if (!model.containsAttribute("quoteOrderDecisionForm"))
			{
				model.addAttribute("quoteOrderDecisionForm", new QuoteOrderForm());
			}

			final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
			breadcrumbs.add(new Breadcrumb("/my-account/my-quotes", getMessageSource().getMessage(
					"text.account.manageQuotes.breadcrumb", null, getI18nService().getCurrentLocale()), null));
			breadcrumbs.add(new Breadcrumb("/my-account/my-quotes/" + orderDetails.getCode(), getMessageSource().getMessage(
					"text.account.manageQuotes.details.breadcrumb", new Object[]
					{ orderDetails.getCode() }, "Quote Details {0}", getI18nService().getCurrentLocale()), null));
			model.addAttribute("breadcrumbs", breadcrumbs);

		}
		catch (final UnknownIdentifierException e)
		{
			LOG.warn("Attempted to load a order that does not exist or is not visible", e);
			return REDIRECT_MY_ACCOUNT;
		}
		storeCmsPageInModel(model, getContentPageForLabelOrId(QUOTE_DETAIL_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(QUOTE_DETAIL_CMS_PAGE));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}

	@RequestMapping(value = "/quote/quoteOrderDecision")
	@RequireHardLogIn
	public String quoteOrderDecision(@ModelAttribute("quoteOrderDecisionForm") final QuoteOrderForm quoteOrderForm,
			final Model model, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(MY_QUOTES_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MY_QUOTES_CMS_PAGE));
		String orderCode = null;
		try
		{
			orderCode = quoteOrderForm.getOrderCode();

			final String comment = XSSFilterUtil.filter(quoteOrderForm.getComments());

			if (NEGOTIATEQUOTE.equals(quoteOrderForm.getSelectedQuoteDecision()))
			{
				if (StringUtils.isBlank(comment))
				{
					setUpCommentIsEmptyError(quoteOrderForm, model);
					return quotesDetails(orderCode, model);
				}
				orderFacade.createAndSetNewOrderFromNegotiateQuote(orderCode, comment);
			}

			if (ACCEPTQUOTE.equals(quoteOrderForm.getSelectedQuoteDecision()))
			{
				final OrderData orderDetails = orderFacade.getOrderDetailsForCode(orderCode);
				final Date quoteExpirationDate = orderDetails.getQuoteExpirationDate();
				if (quoteExpirationDate != null && quoteExpirationDate.before(new Date()))
				{
					GlobalMessages.addErrorMessage(model, "text.quote.expired");
					return quotesDetails(orderCode, model);
				}
				orderFacade.createAndSetNewOrderFromApprovedQuote(orderCode, comment);
				return REDIRECT_PREFIX + "/checkout/orderConfirmation/" + orderCode;
			}

			if (CANCELQUOTE.equals(quoteOrderForm.getSelectedQuoteDecision()))
			{
				orderFacade.cancelOrder(orderCode, comment);
			}

			if (ADDADDITIONALCOMMENT.equals(quoteOrderForm.getSelectedQuoteDecision()))
			{
				if (StringUtils.isBlank(comment))
				{
					setUpCommentIsEmptyError(quoteOrderForm, model);
					return quotesDetails(orderCode, model);
				}
				orderFacade.addAdditionalComment(orderCode, comment);
				GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
						"text.confirmation.quote.comment.added");
				return String.format(REDIRECT_TO_QUOTES_DETAILS, orderCode);
			}
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.warn("Attempted to load a order that does not exist or is not visible", e);
			return REDIRECT_MY_ACCOUNT;
		}

		return REDIRECT_PREFIX + "/checkout/quote/orderConfirmation/" + orderCode;
	}

	protected void setUpCommentIsEmptyError(final QuoteOrderForm quoteOrderForm, final Model model)
			throws CMSItemNotFoundException
	{
		quoteOrderForm.setNegotiateQuote(true);
		model.addAttribute("quoteOrderDecisionForm", quoteOrderForm);
		GlobalMessages.addErrorMessage(model, "text.quote.empty");
	}
}
