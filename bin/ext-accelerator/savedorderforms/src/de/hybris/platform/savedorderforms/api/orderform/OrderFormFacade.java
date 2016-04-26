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
package de.hybris.platform.savedorderforms.api.orderform;

import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.savedorderforms.orderform.data.OrderFormData;

import java.util.List;

public interface OrderFormFacade
{

    /**
     * Creates an Order Form
     *
     * @param orderForm
     * @return OrderFormData
    */
    OrderFormData createOrderForm(OrderFormData orderForm);

    /**
     * Updates an Order Form
     *
     * @param code
     * @param orderForm
     * @return OrderFormData
     */
    OrderFormData updateOrderForm(String code, OrderFormData orderForm);

    /**
     * Finds an Order Form using its code.
     *
     * @param code
     * @return OrderFormData
     * @throws {@link }de.hybris.platform.savedorderforms.exception.DomainException} if order form is not found.
     */
    OrderFormData getOrderFormForCode(String code);

    /**
     * Gets a list of Order Forms for the current user.
     *
     * @return List<OrderFormData></OrderFormData>
     */
    List<OrderFormData> getOrderFormsForCurrentUser();

    /**
     * Deletes an Order Form using its code.
     *
     * @param code
     */
    void removeOrderForm(String code);

    /**
     * Adds a saved order form to a given cart.
     *
     * @param orderFormCode
     * @param cartId
     */
    void addOrderFormToCart(String orderFormCode, String cartId) throws CommerceCartModificationException;
}
