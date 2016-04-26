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
package de.hybris.platform.customerticketingfacades.dao.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerticketingfacades.dao.DefaultCustomerTicketDao;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.ticket.dao.impl.DefaultTicketDao;
import de.hybris.platform.ticket.model.CsTicketModel;

import java.util.Collections;
import java.util.List;


public class DefaultCustomerTicketDaoImpl extends DefaultTicketDao implements DefaultCustomerTicketDao
{

	@Override
	public List<CsTicketModel> findTicketsByCustomerOrderByModifiedTime(final UserModel customer)
	{
		if (customer == null)
		{
			throw new IllegalArgumentException("customer must not be null");
		}

		final SearchResult<CsTicketModel> result = getFlexibleSearchService().search(
				"SELECT {pk} FROM {" + CsTicketModel._TYPECODE + "} WHERE {" + CsTicketModel.CUSTOMER + "} = ?customer ORDER BY {"
						+ CsTicketModel.MODIFIEDTIME + "} DESC", Collections.singletonMap("customer", customer));
		return result.getResult();
	}
}
