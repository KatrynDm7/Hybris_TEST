package de.hybris.platform.warehousing.util.dao.impl;

import de.hybris.platform.ordersplitting.model.VendorModel;


public class VendorDaoImpl extends AbstractWarehousingDao<VendorModel>
{
	@Override
	protected String getQuery()
	{
		return "SELECT {pk} FROM {Vendor} WHERE {code}=?code";
	}

}
