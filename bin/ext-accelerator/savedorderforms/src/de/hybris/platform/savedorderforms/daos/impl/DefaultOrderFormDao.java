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
package de.hybris.platform.savedorderforms.daos.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.savedorderforms.daos.OrderFormDao;
import de.hybris.platform.savedorderforms.model.OrderFormModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

import java.util.*;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Default implementation of the {@link de.hybris.platform.savedorderforms.daos.OrderFormDao}.
 */
public class DefaultOrderFormDao extends DefaultGenericDao<OrderFormModel> implements OrderFormDao
{

	public DefaultOrderFormDao(final String typecode)
	{
		super(typecode);
	}

	@Override
	public OrderFormModel findOrderFormByCode(final String code)
	{
		validateParameterNotNull(code, "Order Form code must not be null!");

        final List<OrderFormModel> resList = find(Collections.singletonMap(OrderFormModel.CODE, (Object) code));

        return resList.isEmpty() ? null : resList.get(0);
	}

    @Override
    public List<OrderFormModel> findOrderFormsForUser(final UserModel userModel)
    {
        validateParameterNotNull(userModel, "Order Form user must not be null!");

        final List<OrderFormModel> resList = find(Collections.singletonMap(OrderFormModel.USER, userModel));

        return resList;
    }

}
