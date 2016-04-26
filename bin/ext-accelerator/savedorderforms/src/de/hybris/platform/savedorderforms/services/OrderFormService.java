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
package de.hybris.platform.savedorderforms.services;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.savedorderforms.model.OrderFormModel;

import java.util.List;

/**
 * Service to read and update {@link de.hybris.platform.savedorderforms.model.OrderFormModel OrderFormModel}s.
 *
 * @spring.bean orderFormService
 */
public interface OrderFormService
{
	/**
	 * Returns the OrderForm with the specified code.
	 *
	 * @param code
	 *           the code of the OrderForm
	 * @return the OrderForm with the specified code.
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no OrderForm with the specified code is found
	 * @throws IllegalArgumentException
	 *            if parameter code is <code>null</code>
	 */
	OrderFormModel getOrderFormForCode(String code);

    /**
     * Returns a list of OrderForms for a given user.
     *
     * @param userModel
     *           the user who owns the order forms
     * @return a list of OrderForm models
     */
    List<OrderFormModel> getOrderFormsForUser(UserModel userModel);
}
