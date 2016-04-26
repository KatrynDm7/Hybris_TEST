package de.hybris.platform.warehousing.util.dao.impl;

import de.hybris.platform.deliveryzone.model.ZoneModel;


public class ZoneDaoImpl extends AbstractWarehousingDao<ZoneModel>
{

	@Override
	protected String getQuery()
	{
		return "SELECT {pk} FROM {Zone} WHERE {code}=?code";
	}

}
