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
package de.hybris.platform.savedorderforms.daos;


import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.savedorderforms.model.OrderFormModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.List;

/**
 * The {@link de.hybris.platform.savedorderforms.model.OrderFormModel} DAO.
 *
 * @spring.bean orderFormDao
 */
public interface OrderFormDao extends Dao
{
	/**
	 * Returns for the given Order Form <code>code</code> the {@link de.hybris.platform.savedorderforms.model.OrderFormModel} model.
	 *
	 * @param code
	 *           the order form <code>code</code>
	 * @return a {@link de.hybris.platform.savedorderforms.model.OrderFormModel}
	 */
	OrderFormModel findOrderFormByCode(final String code);

    /**
     * Returns for the given user a list of {@link de.hybris.platform.savedorderforms.model.OrderFormModel} models.
     *
     * @param userModel
     *           the user who owns the order forms
     * @return a list of {@link de.hybris.platform.savedorderforms.model.OrderFormModel}
     */
    List<OrderFormModel> findOrderFormsForUser(final UserModel userModel);

}
