package de.hybris.platform.warehousing.util.dao.impl;

import de.hybris.platform.core.model.order.OrderModel;


public class WarehousingOrderDaoImpl extends AbstractWarehousingDao<OrderModel>
{

	@Override
	protected String getQuery()
	{
		return "SELECT {pk} FROM {Order} WHERE {code}=?code";
	}
}
