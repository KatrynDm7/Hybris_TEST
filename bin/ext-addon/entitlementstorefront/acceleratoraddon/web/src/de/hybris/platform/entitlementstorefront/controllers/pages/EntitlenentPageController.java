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
package de.hybris.platform.entitlementstorefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.entitlementfacades.CoreEntitlementFacade;
import de.hybris.platform.entitlementfacades.data.EntitlementData;

import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Controller for My Entitlements page
 */
@Controller
@RequestMapping("/my-account/entitlements")
public class EntitlenentPageController extends AbstractSearchPageController
{

	// CMS Page
    private static final String ENTITLEMENTS_CMS_PAGE = "entitlements";

	@Resource(name = "accountBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

    @Resource(name = "coreEntitlementFacade")
    private CoreEntitlementFacade entitlementFacade;

    /**
     * Show My Entitlements page.
     * 
     * @param model
     * @return
     * @throws CMSItemNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET)
    public String entitlements(final Model model) throws CMSItemNotFoundException
    {
        final Collection<EntitlementData> entitlements = entitlementFacade.getUserGrants(getUser().getUid());

        storeCmsPageInModel(model, getContentPageForLabelOrId(ENTITLEMENTS_CMS_PAGE));
        setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ENTITLEMENTS_CMS_PAGE));

        model.addAttribute("grants", entitlements);

        model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.entitlements"));
        model.addAttribute("metaRobots", "no-index,no-follow");
        return getViewForPage(model);
    }
}
