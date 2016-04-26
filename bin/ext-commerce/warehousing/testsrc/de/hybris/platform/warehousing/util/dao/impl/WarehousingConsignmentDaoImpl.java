package de.hybris.platform.warehousing.util.dao.impl;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;


public class WarehousingConsignmentDaoImpl extends AbstractWarehousingDao<ConsignmentModel>
{

	@Override
	protected String getQuery()
	{
		return "SELECT {pk} FROM {Consignment} WHERE {code}=?code";
	}

}
