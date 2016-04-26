package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.ordersplitting.daos.WarehouseDao;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.util.builder.WarehouseModelBuilder;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;


public class Warehouses extends AbstractItems<WarehouseModel>
{
	public static final String CODE_MONTREAL = "montreal";
	public static final String CODE_BOSTON = "boston";

	private WarehouseDao warehouseDao;
	private BaseStores baseStores;
	private Vendors vendors;
	private DeliveryModes deliveryModes;

	public WarehouseModel Montreal()
	{
		return getFromCollectionOrSaveAndReturn(() -> getWarehouseDao().getWarehouseForCode(CODE_MONTREAL), //
				() -> WarehouseModelBuilder.fromModel(Default()) //
						.withCode(CODE_MONTREAL) //
						.withName(CODE_MONTREAL, Locale.ENGLISH) //
						.withDeliveryModes(getDeliveryModes().Pickup(), getDeliveryModes().Regular()) //
						.build());
	}

	public WarehouseModel Boston()
	{
		return getFromCollectionOrSaveAndReturn(() -> getWarehouseDao().getWarehouseForCode(CODE_BOSTON), //
				() -> WarehouseModelBuilder.fromModel(Default()) //
						.withCode(CODE_BOSTON) //
						.withName(CODE_BOSTON, Locale.ENGLISH) //
						.withDeliveryModes(getDeliveryModes().Pickup(), getDeliveryModes().Regular()) //
						.build());
	}

	protected WarehouseModel Default()
	{
		return WarehouseModelBuilder.aModel() //
				.withBaseStores(getBaseStores().NorthAmerica()) //
				.withDefault(Boolean.TRUE) //
				.withVendor(getVendors().Hybris()) //
				.build();
	}

	public WarehouseDao getWarehouseDao()
	{
		return warehouseDao;
	}

	@Required
	public void setWarehouseDao(final WarehouseDao warehouseDao)
	{
		this.warehouseDao = warehouseDao;
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

	public Vendors getVendors()
	{
		return vendors;
	}

	@Required
	public void setVendors(final Vendors vendors)
	{
		this.vendors = vendors;
	}

	public DeliveryModes getDeliveryModes()
	{
		return deliveryModes;
	}

	@Required
	public void setDeliveryModes(final DeliveryModes deliveryModes)
	{
		this.deliveryModes = deliveryModes;
	}

}
