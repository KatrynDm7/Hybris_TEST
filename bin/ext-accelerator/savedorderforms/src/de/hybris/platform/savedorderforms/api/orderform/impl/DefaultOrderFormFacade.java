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
package de.hybris.platform.savedorderforms.api.orderform.impl;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.savedorderforms.api.orderform.OrderFormFacade;
import de.hybris.platform.savedorderforms.exception.DomainException;
import de.hybris.platform.savedorderforms.model.OrderFormEntryModel;
import de.hybris.platform.savedorderforms.model.OrderFormModel;
import de.hybris.platform.savedorderforms.orderform.data.OrderFormData;
import de.hybris.platform.savedorderforms.services.OrderFormService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import org.fest.util.Collections;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class DefaultOrderFormFacade implements OrderFormFacade
{
    private ModelService modelService;
    private OrderFormService orderFormService;
    private CartService cartService;
    private CommerceCartService commerceCartService;
    private ProductService productService;
    private BaseSiteService baseSiteService;
    private UserService userService;
    private Populator<OrderFormData, OrderFormModel> orderFormReversePopulator;
    private Converter<OrderFormModel, OrderFormData> orderFormConverter;

    @Override
    public OrderFormData createOrderForm(OrderFormData orderFormData)
    {
        final OrderFormModel orderFormModel = getModelService().create(OrderFormModel.class);
        getOrderFormReversePopulator().populate(orderFormData, orderFormModel);

        try
        {
            getModelService().save(orderFormModel);
        }
        catch (ModelSavingException e)
        {
            throw new DomainException(e.getMessage());
        }

        return orderFormData;
    }

    @Override
    public OrderFormData updateOrderForm(final String code, OrderFormData orderFormData)
    {
        final OrderFormModel orderFormModel = getOrderFormService().getOrderFormForCode(code);
        if (orderFormModel!=null)
        {
            getOrderFormReversePopulator().populate(orderFormData, orderFormModel);

            try
            {
                getModelService().save(orderFormModel);
            }
            catch (ModelSavingException e)
            {
                throw new DomainException(e.getMessage());
            }
        }

        return orderFormData;
    }

    @Override
    public OrderFormData getOrderFormForCode(final String code)
    {
        OrderFormData orderFormData = null;
        final OrderFormModel orderFormModel = getOrderFormService().getOrderFormForCode(code);
        if (orderFormModel != null)
        {
            if (!orderFormModel.getUser().equals(getUserService().getCurrentUser()))
            {
                throw new DomainException("Order Form user does not match");
            }

            orderFormData = getOrderFormConverter().convert(orderFormModel);
        }
        else
        {
            throw new DomainException("Order Form not found!");
        }

        return orderFormData;
    }

    @Override
    public List<OrderFormData> getOrderFormsForCurrentUser()
    {
        return Converters.convertAll(getOrderFormService().getOrderFormsForUser(getUserService().getCurrentUser()),
                getOrderFormConverter());
    }

    @Override
    public void removeOrderForm(String code)
    {
        final OrderFormModel orderFormModel = getOrderFormService().getOrderFormForCode(code);
        if (orderFormModel!=null)
        {
            getModelService().remove(orderFormModel);
        }
        else
        {
            throw new DomainException("Order Form not found!");
        }
    }

    @Override
    public void addOrderFormToCart(String orderFormCode, String cartId) throws CommerceCartModificationException
    {
        OrderFormModel orderFormModel = getOrderFormService().getOrderFormForCode(orderFormCode);

        if (orderFormModel != null)
        {
            final List<CartModel> carts= getCommerceCartService().getCartsForSiteAndUser(getBaseSiteService().getCurrentBaseSite(), getUserService().getCurrentUser());

            final CartModel cartModel;

            if (!Collections.isEmpty(carts))
            {
                cartModel = carts.get(0);
            }
            else
            {
                cartModel = getCartService().getSessionCart();
            }

            for (OrderFormEntryModel entry : orderFormModel.getEntries())
            {
                final ProductModel product = getProductService().getProductForCode(entry.getSku());

                final CommerceCartParameter parameter = new CommerceCartParameter();
                parameter.setEnableHooks(true);
                parameter.setCart(cartModel);
                parameter.setProduct(product);
                parameter.setQuantity(entry.getQuantity());
                parameter.setUnit(product.getUnit());
                parameter.setCreateNewEntry(false);

                final CommerceCartModification modification = getCommerceCartService().addToCart(parameter);
            }
        }
    }

    protected ModelService getModelService()
    {
        return modelService;
    }

    @Required
    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }

    protected OrderFormService getOrderFormService()
    {
        return orderFormService;
    }

    @Required
    public void setOrderFormService(final OrderFormService orderFormService)
    {
        this.orderFormService = orderFormService;
    }

    protected CartService getCartService()
    {
        return  cartService;
    }

    @Required
    public void setCartService(final CartService cartService)
    {
        this.cartService = cartService;
    }

    protected CommerceCartService getCommerceCartService()
    {
        return  commerceCartService;
    }

    @Required
    public void setCommerceCartService(final CommerceCartService commerceCartService)
    {
        this.commerceCartService = commerceCartService;
    }

    protected ProductService getProductService()
    {
        return  productService;
    }

    @Required
    public void setProductService(final ProductService productService)
    {
        this.productService = productService;
    }

    protected BaseSiteService getBaseSiteService()
    {
        return  baseSiteService;
    }

    @Required
    public void setBaseSiteService(final BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }

    protected UserService getUserService()
    {
        return userService;
    }

    @Required
    public void setUserService(final UserService userService)
    {
        this.userService = userService;
    }

    protected Populator<OrderFormData, OrderFormModel> getOrderFormReversePopulator()
    {
        return orderFormReversePopulator;
    }

    @Required
    public void setOrderFormReversePopulator(final Populator<OrderFormData, OrderFormModel> orderFormReversePopulator)
    {
        this.orderFormReversePopulator = orderFormReversePopulator;
    }

    protected Converter<OrderFormModel, OrderFormData> getOrderFormConverter()
    {
        return orderFormConverter;
    }

    @Required
    public void setOrderFormConverter(final Converter<OrderFormModel, OrderFormData> orderFormConverter)
    {
        this.orderFormConverter = orderFormConverter;
    }
}
