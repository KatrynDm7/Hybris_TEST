package de.hybris.platform.warehousing.util;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * This is a Sourcing order builder implementation of the Builder interface
 */

public class OrderBuilder
{

	public static final Double DEFAULT_DELIVER_LATITUDE = 35.6673;
	public static final Double DEFAULT_DELIVER_LONGITDUE = 139.75429;
	public static final String DEFAULT_COUNTRY_CODE = "US";

	private Double deliveryLatitude = DEFAULT_DELIVER_LATITUDE;
	private Double deliveryLongitude = DEFAULT_DELIVER_LONGITDUE;
	private final String countryCode = DEFAULT_COUNTRY_CODE;
	private DeliveryModeModel deliveryModeModel;
	private PointOfServiceModel pointOfServiceModel;
	private BaseStoreModel baseStore;

	public static OrderBuilder aSourcingOrder()
	{
		return new OrderBuilder();
	}

	public OrderModel build(final AddressModel address, final CurrencyModel currency, final ProductService productService,
			final Map<String, Long> productInfo)
	{

		address.setLatitude(DEFAULT_DELIVER_LATITUDE);
		address.setLongitude(DEFAULT_DELIVER_LONGITDUE);

		final OrderModel order = new OrderModel();
		order.setDeliveryMode(deliveryModeModel);
		order.setStore(baseStore);
		order.setDeliveryAddress(address);
		order.setCurrency(currency);
		order.setDate(new Date());
		order.setUser(setDefaultUser());

		final List<AbstractOrderEntryModel> entries = new ArrayList<AbstractOrderEntryModel>();

		productInfo.entrySet().stream().forEach(product -> {
			final OrderEntryModel entry = new OrderEntryModel();
			entry.setQuantity(product.getValue());
			entry.setProduct(productService.getProductForCode(product.getKey()));

			entries.add(entry);
			entry.setOrder(order);

			final UnitModel unit = new UnitModel();

			final String uuid = UUID.randomUUID().toString().replaceAll("-", "");

			unit.setUnitType("piece");
			unit.setCode(uuid);
			entry.setUnit(unit);
		});
		order.setEntries(entries);

		return order;
	}

	public OrderModel build(final AddressModel address, final CurrencyModel currency, final ProductService productService,
			final Map<String, Long> productInfo, final PointOfServiceModel pointOfServiceModel,
			final DeliveryModeModel deliveryModeModel)
	{

		final OrderModel order = new OrderModel();
		address.setLatitude(DEFAULT_DELIVER_LATITUDE);
		address.setLongitude(DEFAULT_DELIVER_LONGITDUE);

		order.setDeliveryAddress(address);
		order.setDeliveryMode(deliveryModeModel);
		order.setCurrency(currency);
		order.setDate(new Date());
		order.setUser(setDefaultUser());

		final List<AbstractOrderEntryModel> entries = new ArrayList<AbstractOrderEntryModel>();

		productInfo.entrySet().stream().forEach(product -> {
			final UnitModel unit = new UnitModel();
			final String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			unit.setUnitType("piece");
			unit.setCode(uuid);

			final OrderEntryModel entry = new OrderEntryModel();
			entry.setQuantity(product.getValue());
			entry.setProduct(productService.getProductForCode(product.getKey()));
			entry.setOrder(order);
			entry.setDeliveryPointOfService(pointOfServiceModel);
			entry.setUnit(unit);
			entries.add(entry);
		});
		order.setEntries(entries);

		return order;
	}

	public OrderBuilder withDeliveryGeoCode(final double deliveryLatitude, final double deliveryLongitude)
	{

		this.deliveryLatitude = deliveryLatitude;
		this.deliveryLongitude = deliveryLongitude;
		return this;
	}

	public UserModel setDefaultUser()
	{

		final UserModel user = new UserModel();
		user.setUid("2002928137");
		return user;
	}

	public OrderBuilder withBaseStore(final BaseStoreModel baseStore)
	{
		this.baseStore = baseStore;
		return this;
	}
}
