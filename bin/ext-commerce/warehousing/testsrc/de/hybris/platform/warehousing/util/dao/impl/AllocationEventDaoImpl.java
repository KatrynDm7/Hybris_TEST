package de.hybris.platform.warehousing.util.dao.impl;

import de.hybris.platform.warehousing.model.AllocationEventModel;


public class AllocationEventDaoImpl extends AbstractWarehousingDao<AllocationEventModel>
{
	@Override
	protected String getQuery()
	{
		return "SELECT {a.pk} FROM {AllocationEvent as a " //
				+ "JOIN ConsignmentEntry as e ON {a.consignmentEntry} = {e.pk} " //
				+ "JOIN Consignment as c ON {e.consignment} = {c.pk}} " //
				+ "WHERE {c.code}=?code";
	}

}
