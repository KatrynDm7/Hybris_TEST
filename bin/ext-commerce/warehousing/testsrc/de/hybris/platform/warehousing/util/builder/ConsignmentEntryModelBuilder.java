package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;


public class ConsignmentEntryModelBuilder
{
	private final ConsignmentEntryModel model;

	private ConsignmentEntryModelBuilder()
	{
		model = new ConsignmentEntryModel();
	}

	private ConsignmentEntryModel getModel()
	{
		return this.model;
	}

	public static ConsignmentEntryModelBuilder aModel()
	{
		return new ConsignmentEntryModelBuilder();
	}

	public ConsignmentEntryModel build()
	{
		return getModel();
	}

	public ConsignmentEntryModelBuilder withQuantity(final Long quantity)
	{
		getModel().setQuantity(quantity);
		return this;
	}

	public ConsignmentEntryModelBuilder withConsignment(final ConsignmentModel consignment)
	{
		getModel().setConsignment(consignment);
		return this;
	}

	public ConsignmentEntryModelBuilder withOrderEntry(final AbstractOrderEntryModel entry)
	{
		getModel().setOrderEntry(entry);
		return this;
	}

}
