package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;

import com.google.common.collect.Sets;
import de.hybris.platform.storelocator.model.PointOfServiceModel;


public class OrderEntryModelBuilder
{
	private final OrderEntryModel model;

	private OrderEntryModelBuilder()
	{
		this.model = new OrderEntryModel();
	}

	private OrderEntryModelBuilder(final OrderEntryModel source)
	{
		this.model = source;
	}

	private OrderEntryModel getModel()
	{
		return this.model;
	}

	public static OrderEntryModelBuilder aModel()
	{
		return new OrderEntryModelBuilder();
	}

	public static OrderEntryModelBuilder fromModel(final OrderEntryModel source)
	{
		return new OrderEntryModelBuilder(source);
	}

	public OrderEntryModel build()
	{
		return getModel();
	}

	public OrderEntryModelBuilder withProduct(final ProductModel product)
	{
		getModel().setProduct(product);
		return this;
	}

	public OrderEntryModelBuilder withQuantity(final Long quantity)
	{
		getModel().setQuantity(quantity);
		return this;
	}

	public OrderEntryModelBuilder withUnit(final UnitModel unit)
	{
		getModel().setUnit(unit);
		return this;
	}

	public OrderEntryModelBuilder withConsignmentEntries(final ConsignmentEntryModel... entries)
	{
		getModel().setConsignmentEntries(Sets.newHashSet(entries));
		return this;
	}

	public OrderEntryModelBuilder withDeliveryPointOfService(final PointOfServiceModel deliveryPointOfService)
	{
		getModel().setDeliveryPointOfService(deliveryPointOfService);
		return this;
	}

	public OrderEntryModelBuilder withGiveAway(final Boolean giveAway)
	{
		getModel().setGiveAway(giveAway);
		return this;
	}

	public OrderEntryModelBuilder withRejected(final Boolean rejected)
	{
		getModel().setRejected(rejected);
		return this;
	}

}
