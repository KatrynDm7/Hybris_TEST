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
package de.hybris.platform.customerticketingaddon.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.customerticketingaddon.constants.CustomerticketingaddonWebConstants;
import de.hybris.platform.customerticketingaddon.forms.SupportTicketForm;
import de.hybris.platform.customerticketingfacades.TicketFacade;
import de.hybris.platform.customerticketingfacades.data.StatusData;
import de.hybris.platform.customerticketingfacades.data.TicketData;
import de.hybris.platform.site.BaseSiteService;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Controller for Customer Support tickets.
 */
@Controller
@RequestMapping("/my-account")
public class AccountSupportTicketsPageController extends AbstractSearchPageController
{
	// CMS Pages
	private static final String SUPPORT_TICKETS_PAGE = "support-tickets";
	private static final String ADD_SUPPORT_TICKET_PAGE = "add-support-ticket";
	private static final String UPDATE_SUPPORT_TICKET_PAGE = "update-support-ticket";
	private static final String SUPPORT_TICKETS = "supportTickets";
	private static final String SUPPORT_TICKET_FORM = "supportTicketForm";
	private static final String SUPPORT_TICKET_DATA = "ticketData";
	private static final String SUPPORT_TICKET_CODE_PATH_VARIABLE_PATTERN = "{ticketId:.*}";
	private static final String REDIRECT_TO_SUPPORT_TICKETS_PAGE = REDIRECT_PREFIX + "/my-account/support-tickets";

	@Resource(name = "accountBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	@Resource(name = "defaultTicketFacade")
	private TicketFacade ticketFacade;

	@Resource(name = "cartFacade")
	private CartFacade cartFacade;

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@Resource
	private BaseSiteService baseSiteService;

	@RequestMapping(value = "/support-tickets", method = RequestMethod.GET)
	@RequireHardLogIn
	public String supportTickets(@RequestParam(value = "page", defaultValue = "0") final int pageSize,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode, final Model model)
			throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(SUPPORT_TICKETS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(SUPPORT_TICKETS_PAGE));
		model.addAttribute(CustomerticketingaddonWebConstants.BREAD_CRUMBS,
				accountBreadcrumbBuilder.getBreadcrumbs("text.account.supporttickets.history"));
		model.addAttribute(CustomerticketingaddonWebConstants.META_ROBOTS, "noindex,nofollow");

		final PageableData pageableData = createPageableData(0, pageSize, sortCode, showMode);
		final SearchPageData<TicketData> searchPageData = ticketFacade.getTickets(pageableData);
		model.addAttribute(SUPPORT_TICKETS, searchPageData.getResults());

		return getViewForPage(model);
	}

	@RequestMapping(value = "/add-support-ticket", method = RequestMethod.GET)
	@RequireHardLogIn
	public String addSupportTicket(final Model model) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_SUPPORT_TICKET_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_SUPPORT_TICKET_PAGE));

		final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
		breadcrumbs.add(new Breadcrumb("/my-account/support-tickets", getMessageSource().getMessage(
				"text.account.supporttickets.history", null, getI18nService().getCurrentLocale()), null));
		breadcrumbs.add(new Breadcrumb("#", getMessageSource().getMessage("text.account.supporttickets.addSupportTieckt", null,
				getI18nService().getCurrentLocale()), null));
		model.addAttribute(CustomerticketingaddonWebConstants.BREAD_CRUMBS, breadcrumbs);
		model.addAttribute(CustomerticketingaddonWebConstants.META_ROBOTS, "noindex,nofollow");
		model.addAttribute(SUPPORT_TICKET_FORM, new SupportTicketForm());

		return getViewForPage(model);
	}

	@RequestMapping(value = "/add-support-ticket", method = RequestMethod.POST)
	@RequireHardLogIn
	public String addSupportTicket(@Valid final SupportTicketForm supportTicketForm, final BindingResult bindingResult,
			final Model model, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "form.global.error");
			storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_SUPPORT_TICKET_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_SUPPORT_TICKET_PAGE));
			return getViewForPage(model);
		}

		final TicketData ticketData = ticketFacade.createTicket(this.populateTicketData(supportTicketForm));
		if (ticketData == null)
		{
			GlobalMessages.addErrorMessage(model, "text.account.supporttickets.tryLater");
			storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_SUPPORT_TICKET_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_SUPPORT_TICKET_PAGE));
			return getViewForPage(model);
		}

		GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
				"text.account.supportTicket.confirmation.ticket.added", null);

		return REDIRECT_TO_SUPPORT_TICKETS_PAGE;
	}

	@RequestMapping(value = "/support-ticket/" + SUPPORT_TICKET_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	@RequireHardLogIn
	public String getSupportTicket(@PathVariable("ticketId") final String ticketId, final Model model,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_SUPPORT_TICKET_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_SUPPORT_TICKET_PAGE));

		final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
		breadcrumbs.add(new Breadcrumb("/my-account/support-tickets", getMessageSource().getMessage(
				"text.account.supporttickets.history", null, getI18nService().getCurrentLocale()), null));
		breadcrumbs.add(new Breadcrumb("#", getMessageSource().getMessage("text.account.supporttickets.updateSupportTieckt", null,
				getI18nService().getCurrentLocale()), null));
		model.addAttribute(CustomerticketingaddonWebConstants.BREAD_CRUMBS, breadcrumbs);
		model.addAttribute(CustomerticketingaddonWebConstants.META_ROBOTS, "noindex,nofollow");
		model.addAttribute(SUPPORT_TICKET_FORM, new SupportTicketForm());
		try
		{
			final TicketData ticketData = ticketFacade.getTicket(ticketId);
			if (ticketData == null)
			{
				throw new Exception("Ticket not found for the given ID " + ticketId);
			}
			model.addAttribute(SUPPORT_TICKET_DATA, ticketData);
		}
		catch (final Exception e)
		{
			LOG.error("Attempted to load ticket details that does not exist or is not visible", e);
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
					"text.account.supporttickets.tryLater", null);
			return REDIRECT_TO_SUPPORT_TICKETS_PAGE;
		}

		return getViewForPage(model);
	}

	@RequestMapping(value = "/support-ticket/" + SUPPORT_TICKET_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.POST)
	@RequireHardLogIn
	public String updateSupportTicket(@Valid final SupportTicketForm supportTicketForm, final BindingResult bindingResult,
			final Model model, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "form.global.error");
			model.addAttribute(SUPPORT_TICKET_DATA, ticketFacade.getTicket(supportTicketForm.getId()));
			storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_SUPPORT_TICKET_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_SUPPORT_TICKET_PAGE));
			return getViewForPage(model);
		}

		final TicketData updateTicket = ticketFacade.updateTicket(this.populateTicketData(supportTicketForm));
		//Assuming there might have been an error occurred, If ticket data returned as null. Return to the update page.
		if (updateTicket == null)
		{

			GlobalMessages.addErrorMessage(model, "text.account.supporttickets.tryLater");
			model.addAttribute(SUPPORT_TICKET_DATA, ticketFacade.getTicket(supportTicketForm.getId()));
			storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_SUPPORT_TICKET_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_SUPPORT_TICKET_PAGE));
			return getViewForPage(model);
		}

		GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
				"text.account.supportTicket.confirmation.ticket.added", null);

		return REDIRECT_TO_SUPPORT_TICKETS_PAGE;
	}

	/**
	 * Populated the data from the form bean to ticket data object.
	 *
	 * @param supportTicketForm
	 * @return TicketData
	 */
	private TicketData populateTicketData(final SupportTicketForm supportTicketForm)
	{
		final TicketData ticketData = new TicketData();
		if (cartFacade.hasSessionCart())
		{
			final CartData cartData = cartFacade.getSessionCart();
			if (!cartData.getEntries().isEmpty())
			{
				ticketData.setCartId(cartData.getCode());
			}
		}
		if (StringUtils.isNotBlank(supportTicketForm.getId()))
		{
			ticketData.setId(supportTicketForm.getId());
		}
		final StatusData status = new StatusData();
		status.setId(supportTicketForm.getStatus());
		ticketData.setStatus(status);
		ticketData.setCustomerId(customerFacade.getCurrentCustomerUid());
		ticketData.setSubject(supportTicketForm.getSubject());
		ticketData.setMessage(supportTicketForm.getMessage());
		return ticketData;
	}
}
