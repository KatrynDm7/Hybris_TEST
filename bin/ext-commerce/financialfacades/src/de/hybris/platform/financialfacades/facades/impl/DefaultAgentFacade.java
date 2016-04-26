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
package de.hybris.platform.financialfacades.facades.impl;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.financialfacades.email.FindAgentMailEvent;
import de.hybris.platform.financialfacades.email.FindAgentMailEventBuilder;
import de.hybris.platform.financialfacades.facades.AgentFacade;
import de.hybris.platform.financialfacades.findagent.data.AgentData;
import de.hybris.platform.financialservices.model.AgentModel;
import de.hybris.platform.financialservices.services.AgentService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of the {@link de.hybris.platform.financialfacades.facades.AgentFacade}
 * interface.
 */
public class DefaultAgentFacade implements AgentFacade
{
    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(DefaultAgentFacade.class);

    private CategoryService categoryService;
    private Converter<CategoryModel, CategoryData> categoryModelToDataConverter;
    private AgentService agentService;
    private Converter<AgentModel, AgentData> agentConverter;

    private EventService eventService;
    private BaseStoreService baseStoreService;
    private BaseSiteService baseSiteService;
    private CommonI18NService commonI18NService;

    private CartService cartService;
    private UserService userService;

    @Required
    public void setAgentService(final AgentService agentService)
    {
        this.agentService = agentService;
    }

    @Required
    public void setAgentConverter(final Converter<AgentModel, AgentData> agentConverter)
    {
        this.agentConverter = agentConverter;
    }

    protected Converter<AgentModel, AgentData> getAgentConverter()
    {
        return agentConverter;
    }

    @Override
    public AgentData getAgentByUid(final String agentUid)
    {
        if(StringUtils.isBlank(agentUid))
        {
            throw new IllegalArgumentException("Agent id must not be null or empty");
        }

        final AgentModel agentModel = agentService.getAgentForCode(agentUid);
        return getAgentConverter().convert(agentModel);
    }

    @Override
    public List<AgentData> getAgentsByCategory(final String categoryCode)
    {
        if(StringUtils.isBlank(categoryCode))
        {
            throw new IllegalArgumentException("Category code must not be null or empty");
        }

        final Collection<AgentModel> agents = agentService.getAgentsByCategory(categoryCode);
        return convertAgents(agents);
    }

    @Override
    public List<AgentData> getAgents()
    {
        final Collection<AgentModel> agentModels =  agentService.getAgents();
        return convertAgents(agentModels);
    }

    private List<AgentData> convertAgents(final Collection<AgentModel> agents)
    {
        final List<AgentData> result = new ArrayList<>(agents.size());
        for (final AgentModel agent : agents)
        {
            result.add(getAgentConverter().convert(agent));
        }
        return result;
    }

    private List<CategoryData> convertCategories(final Collection<CategoryModel> categories)
    {
        final List<CategoryData> result = new ArrayList<>(categories.size());
        for (final CategoryModel categoryModel : categories)
        {
            result.add(getCategoryConverter().convert(categoryModel));
        }
        return result;
    }

    @Override
    public List<CategoryData> getCategories(final String rootCategoryCode)
    {
        final CategoryModel rootCategory = getCategoryService().getCategoryForCode(rootCategoryCode);
        List<CategoryModel> children = rootCategory.getCategories();
        if (CollectionUtils.isEmpty(children))
        {
            children = Arrays.asList(rootCategory);
        }
        children = getNonEmptyCategories(children);
        return convertCategories(children);

/*
        // Java 8 way
        return getCategoryService().getCategoryForCode(rootCategoryCode).getCategories()
                .stream()
                .filter(it -> !Collections.isEmpty(agentService.getAgentsByCategory(it.getCode())))
                .map(cat -> getCategoryConverter().convert(cat))
                .collect(Collectors.toList());
*/
    }

    /**
     * Return only items of <code>categories</code> list, that contain agents.
     *
     * @param categories input list of categories to filter
     * @return filtered categories
     */
    protected List<CategoryModel> getNonEmptyCategories(final List<CategoryModel> categories)
    {
        final List<CategoryModel> result = new ArrayList<>(categories.size());
        for (final CategoryModel category : categories)
        {
            final Collection<AgentModel> agents = agentService.getAgentsByCategory(category.getCode());
            if (!CollectionUtils.isEmpty(agents))
            {
                result.add(category);
            }
        }
        return result;
    }

    @Override
    public void sendMail(String xml) {

        FindAgentMailEvent event = new FindAgentMailEventBuilder().build(xml);
        
        final AgentModel agent = agentService.getAgentForCode(event.getAgentEmail());

        getEventService().publishEvent(
                initializeEvent(event, agent)
        );
        
    }

    /**
     * This method initialize AbstractCommerceUserEvent.
     * Use it for setting default properties of AbstractCommerceUserEvent.
     *
     * @param e
     * @param customerModel
     * @return
     */
    protected AbstractCommerceUserEvent initializeEvent(FindAgentMailEvent e, CustomerModel customerModel)
    {
        e.setSite(this.getBaseSiteService().getCurrentBaseSite());
        e.setBaseStore(this.getBaseStoreService().getCurrentBaseStore());
        e.setCustomer(customerModel);
        e.setLanguage(this.getCommonI18NService().getCurrentLanguage());
        e.setCurrency(this.getCommonI18NService().getCurrentCurrency());
        if (getCartService().hasSessionCart()) {
            CartModel cartModel = getCartService().getSessionCart();
            e.setCartCode(cartModel.getCode());
        }
        UserModel userModel = getUserService().getCurrentUser();
        e.setAnonymousUser(getUserService().isAnonymousUser(userModel));
        e.setUserUid(userModel.getUid());
        return e;
    }

    public EventService getEventService() {
        return eventService;
    }

    @Required
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    protected CategoryService getCategoryService()
    {
        return categoryService;
    }

    @Required
    public void setCategoryService(final CategoryService categoryService)
    {
        this.categoryService = categoryService;
    }

    protected Converter<CategoryModel, CategoryData> getCategoryConverter()
    {
        return categoryModelToDataConverter;
    }

    @Required
    public void setCategoryConverter(
            final Converter<CategoryModel, CategoryData> categoryModelToDataConverter)
    {
        this.categoryModelToDataConverter = categoryModelToDataConverter;
    }

    @Required
    protected BaseStoreService getBaseStoreService() {
        return this.baseStoreService;
    }

    @Required
    public void setBaseStoreService(BaseStoreService service) {
        this.baseStoreService = service;
    }

    protected BaseSiteService getBaseSiteService() {
        return this.baseSiteService;
    }

    @Required
    public void setBaseSiteService(BaseSiteService siteService) {
        this.baseSiteService = siteService;
    }

    protected CommonI18NService getCommonI18NService() {
        return this.commonI18NService;
    }

    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService) {
        this.commonI18NService = commonI18NService;
    }

    protected CartService getCartService() {
        return cartService;
    }

    @Required
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    protected UserService getUserService() {
        return userService;
    }

    @Required
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}