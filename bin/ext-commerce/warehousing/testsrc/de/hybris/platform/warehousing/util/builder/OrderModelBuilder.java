package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Date;

import com.google.common.collect.Lists;


public class OrderModelBuilder
{
	private final OrderModel model;

	private OrderModelBuilder()
	{
		model = new OrderModel();
	}

	private OrderModel getModel()
	{
		return this.model;
	}

	public static OrderModelBuilder aModel()
	{
		return new OrderModelBuilder();
	}

	public OrderModel build()
	{
		return getModel();
	}

	public OrderModelBuilder withCode(final String code)
	{
		getModel().setCode(code);
		return this;
	}

	public OrderModelBuilder withDeliveryMode(final DeliveryModeModel deliveryMode)
	{
		getModel().setDeliveryMode(deliveryMode);
		return this;
	}

	public OrderModelBuilder withCurrency(final CurrencyModel currency)
	{
		getModel().setCurrency(currency);
		return this;
	}

	public OrderModelBuilder withStore(final BaseStoreModel store)
	{
		getModel().setStore(store);
		return this;
	}

	public OrderModelBuilder withDeliveryAddress(final AddressModel deliveryAddress)
	{
		getModel().setDeliveryAddress(deliveryAddress);
		return this;
	}

	public OrderModelBuilder withDate(final Date date)
	{
		getModel().setDate(date);
		return this;
	}

	public OrderModelBuilder withUser(final UserModel user)
	{
		getModel().setUser(user);
		return this;
	}

	public OrderModelBuilder withEntries(final AbstractOrderEntryModel... entries)
	{
		getModel().setEntries(Lists.newArrayList(entries));
		return this;
	}

}
