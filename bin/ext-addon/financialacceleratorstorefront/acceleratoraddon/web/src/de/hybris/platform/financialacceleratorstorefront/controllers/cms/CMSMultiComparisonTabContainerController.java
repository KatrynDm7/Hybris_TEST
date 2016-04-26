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
package de.hybris.platform.financialacceleratorstorefront.controllers.cms;

import de.hybris.platform.cms2.model.contents.components.CMSImageComponentModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.financialacceleratorstorefront.controllers.ControllerConstants;
import de.hybris.platform.financialservices.model.components.CMSComparisonTabComponentModel;
import de.hybris.platform.financialservices.model.components.CMSMultiComparisonTabContainerModel;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * CMS Multi Comparison Tab Container Controller
 */
@Controller("CMSMultiComparisonTabContainerController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.CMSMultiComparisonTabContainer)
public class CMSMultiComparisonTabContainerController extends
		SubstitutingCMSAddOnComponentController<CMSMultiComparisonTabContainerModel>
{
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final CMSMultiComparisonTabContainerModel component)
	{
		final List<SimpleCMSComponentModel> tabComponents = new ArrayList<SimpleCMSComponentModel>();
		if (CollectionUtils.isNotEmpty(component.getSimpleCMSComponents()))
		{
			for (final SimpleCMSComponentModel tabComponent : component.getSimpleCMSComponents())
			{
				if (tabComponent instanceof CMSComparisonTabComponentModel)
				{
					tabComponents.add(tabComponent);
				}
				else if (tabComponent instanceof CMSImageComponentModel)
				{
					model.addAttribute("imageComponent", tabComponent);
				}
			}
			model.addAttribute("tabComponents", tabComponents);
		}
	}

}
