package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.warehousing.util.builder.ConsignmentEntryModelBuilder;
import de.hybris.platform.warehousing.util.builder.ConsignmentModelBuilder;
import de.hybris.platform.warehousing.util.dao.WarehousingDao;

import org.springframework.beans.factory.annotation.Required;


public class Consignments extends AbstractItems<ConsignmentModel>
{
	public static final String CODE_CAMERA_SHIPPED_FROM_MONREAL_TO_MONTREAL_NANCY_HOME = "camera-shipped-from-montreal-to-montreal-nancy-home";
	public static final String CODE_CAMERA_SHIPPED_FROM_BOSTON_TO_MONTREAL_NANCY_HOME = "camera-shipped-from-boston-to-montreal-nancy-home";

	private Addresses addresses;
	private DeliveryModes deliveryModes;
	private Orders orders;
	private Warehouses warehouses;
	private WarehousingDao<ConsignmentModel> warehousingConsignmentDao;

	public ConsignmentModel Camera_ShippedFromMontrealToMontrealNancyHome(final ConsignmentStatus status, final Long quantity)
	{
		return getOrSaveAndReturn(
				() -> getWarehousingConsignmentDao().getByCode(CODE_CAMERA_SHIPPED_FROM_MONREAL_TO_MONTREAL_NANCY_HOME),
				() -> {
					final ConsignmentEntryModel entry = Camera(quantity);
					final ConsignmentModel consignment = ConsignmentModelBuilder.aModel() //
							.withCode(CODE_CAMERA_SHIPPED_FROM_MONREAL_TO_MONTREAL_NANCY_HOME) //
							.withDeliveryMode(getDeliveryModes().Regular()) //
							.withWarehouse(getWarehouses().Montreal()) //
							.withShippingAddress(getAddresses().MontrealNancyHome()) //
							.withStatus(status) //
							.withEntries(entry) //
							.withOrder((OrderModel) (entry.getOrderEntry().getOrder())) //
							.build();
					entry.setConsignment(consignment);
					return consignment;
				});
	}

	public ConsignmentModel Camera_ShippedFromBostonToMontrealNancyHome(final ConsignmentStatus status, final Long quantity)
	{
		return getOrSaveAndReturn(
				() -> getWarehousingConsignmentDao().getByCode(CODE_CAMERA_SHIPPED_FROM_BOSTON_TO_MONTREAL_NANCY_HOME),
				() -> {
					final ConsignmentEntryModel entry = Camera(quantity);
					final ConsignmentModel consignment = ConsignmentModelBuilder.aModel() //
							.withCode(CODE_CAMERA_SHIPPED_FROM_BOSTON_TO_MONTREAL_NANCY_HOME) //
							.withDeliveryMode(getDeliveryModes().Regular()) //
							.withWarehouse(getWarehouses().Boston()) //
							.withShippingAddress(getAddresses().MontrealNancyHome()) //
							.withStatus(status) //
							.withEntries(entry) //
							.withOrder((OrderModel) (entry.getOrderEntry().getOrder())) //
							.build();
					entry.setConsignment(consignment);
					return consignment;
				});
	}

	protected ConsignmentEntryModel Camera(final Long quantity)
	{
		return ConsignmentEntryModelBuilder.aModel() //
				.withOrderEntry(getOrders().Camera_Shipped(quantity).getEntries().get(0)) //
				.withQuantity(quantity) //
				.build();
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

	public Warehouses getWarehouses()
	{
		return warehouses;
	}

	@Required
	public void setWarehouses(final Warehouses warehouses)
	{
		this.warehouses = warehouses;
	}

	public WarehousingDao<ConsignmentModel> getWarehousingConsignmentDao()
	{
		return warehousingConsignmentDao;
	}

	@Required
	public void setWarehousingConsignmentDao(final WarehousingDao<ConsignmentModel> warehousingConsignmentDao)
	{
		this.warehousingConsignmentDao = warehousingConsignmentDao;
	}

	public Orders getOrders()
	{
		return orders;
	}

	@Required
	public void setOrders(final Orders orders)
	{
		this.orders = orders;
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
