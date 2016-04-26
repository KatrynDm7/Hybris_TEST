package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.basecommerce.enums.PointOfServiceTypeEnum;
import de.hybris.platform.storelocator.PointOfServiceDao;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousing.util.builder.PointOfServiceModelBuilder;

import org.springframework.beans.factory.annotation.Required;

public class PointsOfService extends AbstractItems<PointOfServiceModel>
{
	public static final String NAME_MONTREAL_DOWNTOWN = "montreal-downtown";
	public static final String NAME_BOSTON = "boston";

	private PointOfServiceDao pointOfServiceDao;
	private BaseStores baseStores;
	private Warehouses warehouses;
	private Countries countries;
	private Addresses addresses;

	public PointOfServiceModel Montreal_Downtown()
	{
		return getOrSaveAndReturn(() -> getPointOfServiceDao().getPosByName(NAME_MONTREAL_DOWNTOWN), //
				() -> PointOfServiceModelBuilder.aModel() //
				.withBaseStore(getBaseStores().NorthAmerica()) //
				.withName(NAME_MONTREAL_DOWNTOWN) //
				.withType(PointOfServiceTypeEnum.WAREHOUSE) //
				.withWarehouses(getWarehouses().Montreal()) //
				.withAddress(getAddresses().MontrealDeMaisonneuvePos()) //
				.build());
	}

	public PointOfServiceModel Boston()
	{
		return getOrSaveAndReturn(() -> getPointOfServiceDao().getPosByName(NAME_BOSTON), //
				() -> PointOfServiceModelBuilder.aModel() //
				.withBaseStore(getBaseStores().NorthAmerica()) //
				.withName(NAME_BOSTON) //
				.withType(PointOfServiceTypeEnum.STORE) //
				.withWarehouses(getWarehouses().Boston()) //
				.withAddress(getAddresses().Boston())
				.build());
	}

	public PointOfServiceDao getPointOfServiceDao()
	{
		return pointOfServiceDao;
	}

	@Required
	public void setPointOfServiceDao(final PointOfServiceDao pointOfServiceDao)
	{
		this.pointOfServiceDao = pointOfServiceDao;
	}

	public BaseStores getBaseStores()
	{
		return baseStores;
	}

	@Required
	public void setBaseStores(final BaseStores baseStores)
	{
		this.baseStores = baseStores;
	}

	public Warehouses getWarehouses()
	{
		return warehouses;
	}

	@Required
	public void setWarehouses(final Warehouses warehouses)
	{
		this.warehouses = warehouses;
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

	public Addresses getAddresses()
	{
		return addresses;
	}

	@Required
	public void setAddresses(final Addresses addresses)
	{
		this.addresses = addresses;
	}

}
