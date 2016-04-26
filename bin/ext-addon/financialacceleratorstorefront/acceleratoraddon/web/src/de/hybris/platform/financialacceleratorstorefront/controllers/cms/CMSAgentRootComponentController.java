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

package de.hybris.platform.financialacceleratorstorefront.controllers.cms;

import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.financialacceleratorstorefront.controllers.ControllerConstants;
import de.hybris.platform.financialfacades.facades.AgentFacade;
import de.hybris.platform.financialfacades.findagent.data.AgentData;
import de.hybris.platform.financialservices.model.components.CMSAgentRootComponentModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller("CMSAgentRootComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.CMSAgentRootComponent)
public class CMSAgentRootComponentController extends SubstitutingCMSAddOnComponentController<CMSAgentRootComponentModel>
{

	@Resource(name = "agentFacade")
	private AgentFacade agentFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final CMSAgentRootComponentModel component)
	{
		final List<CategoryData> categories = agentFacade.getCategories(component.getAgentRootCategory());

		final String activeCategory = defineActiveCategory(request, categories);

		model.addAttribute("activeCategory", activeCategory);

		final List<CategoryItem> categorizedAgents = new ArrayList<>(categories.size());

		for (final CategoryData category : categories)
		{
			final CategoryItem item = new CategoryItem();
			item.setCategory(category);
			if (category.getCode().equals(activeCategory))
			{
				item.setAgents(agentFacade.getAgentsByCategory(activeCategory));
			}
			categorizedAgents.add(item);
		}

		model.addAttribute("categories", categorizedAgents);
	}

	protected String defineActiveCategory(final HttpServletRequest request, final List<CategoryData> categories)
	{

		return request.getAttribute("activeCategory") != null ? request.getAttribute("activeCategory").toString() : !categories
				.isEmpty() ? categories.get(0).getCode() : "";
	}

	public static class CategoryItem
	{
		private CategoryData category;
		private List<AgentData> agents;

		public CategoryData getCategory()
		{
			return category;
		}

		public void setCategory(final CategoryData category)
		{
			this.category = category;
		}

		public List<AgentData> getAgents()
		{
			return agents;
		}

		public void setAgents(final List<AgentData> agents)
		{
			this.agents = agents;
		}
	}
}
