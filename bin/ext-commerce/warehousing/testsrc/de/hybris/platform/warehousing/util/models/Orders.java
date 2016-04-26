package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.warehousing.util.builder.OrderEntryModelBuilder;
import de.hybris.platform.warehousing.util.builder.OrderModelBuilder;
import de.hybris.platform.warehousing.util.dao.WarehousingDao;

import java.util.Date;

import org.springframework.beans.factory.annotation.Required;


public class Orders extends AbstractItems<OrderModel>
{
	public static final String CODE_CAMERA_SHIPPED = "camera-shipped";
	public static final String CODE_CAMERA_PICKUP_MONTREAL = "camera-pickup-montreal";
	public static final String CODE_CAMERA_AND_MEMORY_CARD_SHIPPED = "camera-and-memory-card-shipped";

	private WarehousingDao<OrderModel> warehousingOrderDao;
	private DeliveryModes deliveryModes;
	private Currencies currencies;
	private BaseStores baseStores;
	private Products products;
	private Units units;
	private Users users;
	private PointsOfService pointsOfService;
	private Addresses addresses;

	public OrderModel Camera_Shipped(final Long quantity)
	{
		return getOrSaveAndReturn(() -> getWarehousingOrderDao().getByCode(CODE_CAMERA_SHIPPED), //
				() -> {
					final AbstractOrderEntryModel entry = Camera(quantity);
					final OrderModel order = OrderModelBuilder.aModel() //
							.withCode(CODE_CAMERA_SHIPPED) //
							.withCurrency(getCurrencies().AmericanDollar()) //
							.withStore(getBaseStores().NorthAmerica()) //
							.withDate(new Date()) //
							.withUser(getUsers().Nancy()) //
							.withEntries(entry) //
							.withDeliveryAddress(getAddresses().MontrealNancyHome())
							.build();
					entry.setOrder(order);
					return order;
				});
	}

	public OrderModel Camera_PickupInMontreal(final Long quantity)
	{
		return getOrSaveAndReturn(() -> getWarehousingOrderDao().getByCode(CODE_CAMERA_PICKUP_MONTREAL), //
				() -> {
					final AbstractOrderEntryModel entry = Camera_PickupMontreal(quantity);
					final OrderModel order = OrderModelBuilder.aModel() //
							.withCode(CODE_CAMERA_PICKUP_MONTREAL) //
							.withCurrency(getCurrencies().AmericanDollar()) //
							.withStore(getBaseStores().NorthAmerica()) //
							.withDate(new Date()) //
							.withUser(getUsers().Nancy()) //
							.withEntries(entry) //
							.withDeliveryAddress(getAddresses().MontrealDeMaisonneuvePos())
							.build();
					entry.setOrder(order);
					return order;
				});
	}

	public OrderModel CameraAndMemoryCard_Shipped(final Long cameraQty, final Long memoryCardQty)
	{
		return getOrSaveAndReturn(() -> getWarehousingOrderDao().getByCode(CODE_CAMERA_AND_MEMORY_CARD_SHIPPED), //
				() -> {
					final AbstractOrderEntryModel camera = Camera(cameraQty);
					final AbstractOrderEntryModel memoryCard = MemoryCard(memoryCardQty);
					final OrderModel order = OrderModelBuilder.aModel() //
							.withCode(CODE_CAMERA_SHIPPED) //
							.withCurrency(getCurrencies().AmericanDollar()) //
							.withStore(getBaseStores().NorthAmerica()) //
							.withDate(new Date()) //
							.withUser(getUsers().Nancy()) //
							.withEntries(camera, memoryCard) //
							.withDeliveryAddress(getAddresses().MontrealNancyHome())
							.build();
					camera.setOrder(order);
					memoryCard.setOrder(order);
					return order;
				});
	}

	protected OrderEntryModel Default(final Long quantity)
	{
		return OrderEntryModelBuilder.aModel() //
				.withQuantity(quantity) //
				.withUnit(units.Unit()) //
				.withGiveAway(Boolean.FALSE) //
				.withRejected(Boolean.FALSE) //
				.build();
	}

	protected OrderEntryModel Camera(final Long quantity)
	{
		return OrderEntryModelBuilder.fromModel(Default(quantity)) //
				.withProduct(getProducts().Camera()) //
				.build();
	}

	protected OrderEntryModel MemoryCard(final Long quantity)
	{
		return OrderEntryModelBuilder.fromModel(Default(quantity)) //
				.withProduct(getProducts().MemoryCard()) //
				.build();
	}

	protected OrderEntryModel Camera_PickupMontreal(final Long quantity)
	{
		return OrderEntryModelBuilder.fromModel(Camera(quantity)) //
				.withDeliveryPointOfService(getPointsOfService().Montreal_Downtown()) //
				.build();
	}

	public WarehousingDao<OrderModel> getWarehousingOrderDao()
	{
		return warehousingOrderDao;
	}

	@Required
	public void setWarehousingOrderDao(final WarehousingDao<OrderModel> warehousingOrderDao)
	{
		this.warehousingOrderDao = warehousingOrderDao;
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

	public Currencies getCurrencies()
	{
		return currencies;
	}

	@Required
	public void setCurrencies(final Currencies currencies)
	{
		this.currencies = currencies;
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

	public Products getProducts()
	{
		return products;
	}

	@Required
	public void setProducts(final Products products)
	{
		this.products = products;
	}

	public Units getUnits()
	{
		return units;
	}

	@Required
	public void setUnits(final Units units)
	{
		this.units = units;
	}

	public Users getUsers()
	{
		return users;
	}

	@Required
	public void setUsers(final Users users)
	{
		this.users = users;
	}

	public PointsOfService getPointsOfService()
	{
		return pointsOfService;
	}

	@Required
	public void setPointsOfService(final PointsOfService pointsOfService)
	{
		this.pointsOfService = pointsOfService;
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
