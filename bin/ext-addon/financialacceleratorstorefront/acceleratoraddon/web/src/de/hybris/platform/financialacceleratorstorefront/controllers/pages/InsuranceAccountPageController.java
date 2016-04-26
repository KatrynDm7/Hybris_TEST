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
 */

package de.hybris.platform.financialacceleratorstorefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyListingData;
import de.hybris.platform.commercefacades.insurance.data.InsuranceQuoteListingData;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.financialfacades.facades.DocumentGenerationFacade;
import de.hybris.platform.financialfacades.facades.InsuranceCartFacade;
import de.hybris.platform.financialfacades.facades.InsurancePolicyFacade;
import de.hybris.platform.financialfacades.facades.InsuranceQuoteFacade;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Extended Controller for home page - adds functionality to AccountPageController
 */
@Controller
@Scope("tenant")
@RequestMapping("/my-account")
public class InsuranceAccountPageController extends AbstractSearchPageController
{
	// CMS Pages
	protected static final String MY_POLICIES_CMS_PAGE = "my-policies";
	protected static final String MY_QUOTES_CMS_PAGE = "my-quotes";

	@Resource(name = "accountBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	@Resource(name = "policyFacade")
	private InsurancePolicyFacade insurancePolicyFacade;

	@Resource(name = "quoteFacade")
	private InsuranceQuoteFacade quoteFacade;

	@Resource(name = "cartFacade")
	private InsuranceCartFacade cartFacade;

	@Resource(name = "documentGenerationFacade")
	DocumentGenerationFacade documentGenerationFacade;

	@Resource(name = "orderFacade")
	OrderFacade orderFacade;


	@RequestMapping(value = "/my-policies", method = RequestMethod.GET)
	@RequireHardLogIn
	public String myPolicies(final Model model) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(MY_POLICIES_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MY_POLICIES_CMS_PAGE));
		model.addAttribute("breadcrumbs", getAccountBreadcrumbBuilder().getBreadcrumbs("text.account.myPolicies"));
		model.addAttribute("metaRobots", "noindex,nofollow");

		final List<InsurancePolicyListingData> policies = getInsurancePolicyFacade().getPoliciesForCurrentCustomer();
		model.addAttribute("policies", policies);

		return getViewForPage(model);
	}

	@RequestMapping(value = "/my-quotes", method = RequestMethod.GET)
	@RequireHardLogIn
	public String myQuotes(final Model model) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(MY_QUOTES_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MY_QUOTES_CMS_PAGE));
		model.addAttribute("breadcrumbs", getAccountBreadcrumbBuilder().getBreadcrumbs("text.account.myQuotes"));
		model.addAttribute("metaRobots", "noindex,nofollow");

		final List<CartData> cartDataList = getCartFacade().getSavedCartsForCurrentUser();
		final List<InsuranceQuoteListingData> quotes = getQuoteFacade().sortCartListForQuoteListing(cartDataList);
		model.addAttribute("quotes", quotes);
		return getViewForPage(model);
	}

	@RequestMapping(value = "/pdf/print/{orderCode:.*}", method = RequestMethod.GET)
	public void pdfPrint(@PathVariable("orderCode") final String orderCode, final HttpServletResponse response) throws IOException
	{
		LOG.info("Pdf Print with orderCode - " + orderCode);
		final OrderData orderData = orderFacade.getOrderDetailsForCode(orderCode);
		if (CollectionUtils.isNotEmpty(orderData.getInsurancePolicy()))
		{
			documentGenerationFacade
					.generate(DocumentGenerationFacade.PDF_DOCUMENT, orderData.getInsurancePolicy().get(0), response);
		}
	}

	protected ResourceBreadcrumbBuilder getAccountBreadcrumbBuilder()
	{
		return accountBreadcrumbBuilder;
	}

	public void setAccountBreadcrumbBuilder(final ResourceBreadcrumbBuilder accountBreadcrumbBuilder)
	{
		this.accountBreadcrumbBuilder = accountBreadcrumbBuilder;
	}

	protected InsurancePolicyFacade getInsurancePolicyFacade()
	{
		return insurancePolicyFacade;
	}

	protected InsuranceQuoteFacade getQuoteFacade()
	{
		return quoteFacade;
	}

	public void setQuoteFacade(final InsuranceQuoteFacade quoteFacade)
	{
		this.quoteFacade = quoteFacade;
	}

	protected InsuranceCartFacade getCartFacade()
	{
		return cartFacade;
	}

	public void setCartFacade(final InsuranceCartFacade cartFacade)
	{
		this.cartFacade = cartFacade;
	}

}
