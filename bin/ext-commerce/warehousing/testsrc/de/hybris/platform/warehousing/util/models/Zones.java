package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.deliveryzone.model.ZoneModel;
import de.hybris.platform.warehousing.util.builder.ZoneModelBuilder;
import de.hybris.platform.warehousing.util.dao.WarehousingDao;

import org.springframework.beans.factory.annotation.Required;


public class Zones extends AbstractItems<ZoneModel>
{
	public static final String CODE_UNITED_STATES = "usa";

	private Countries countries;
	private WarehousingDao<ZoneModel> zoneDao;

	public ZoneModel UnitedStates()
	{
		return getOrSaveAndReturn(() -> getZoneDao().getByCode(CODE_UNITED_STATES), //
				() -> ZoneModelBuilder.aModel() //
				.withCode(CODE_UNITED_STATES) //
				.withCountries(getCountries().UnitedStates()) //
				.build());
	}

	public Countries getCountries()
	{
		return countries;
	}

	@Required
	public void setCountries(final Countries countries)
	{
		this.countries = countries;
	}

	public WarehousingDao<ZoneModel> getZoneDao()
	{
		return zoneDao;
	}

	@Required
	public void setZoneDao(final WarehousingDao<ZoneModel> zoneDao)
	{
		this.zoneDao = zoneDao;
	}
}
