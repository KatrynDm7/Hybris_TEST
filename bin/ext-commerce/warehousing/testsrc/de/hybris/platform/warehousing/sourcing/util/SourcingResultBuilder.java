package de.hybris.platform.warehousing.sourcing.util;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingResult;

import java.util.HashMap;
import java.util.Objects;


public class SourcingResultBuilder
{
	private final SourcingResult result;

	private SourcingResultBuilder()
	{
		result = new SourcingResult();
	}

	private SourcingResult getResult()
	{
		return this.result;
	}

	public static SourcingResultBuilder aResult()
	{
		return new SourcingResultBuilder();
	}

	public SourcingResult build()
	{
		return getResult();
	}

	public SourcingResultBuilder withWarehouse(final WarehouseModel warehouse)
	{
		getResult().setWarehouse(warehouse);
		return this;
	}

	public SourcingResultBuilder withAllocation(final AbstractOrderEntryModel orderEntry, final Long quantity)
	{
		if (Objects.isNull(getResult().getAllocation()))
		{
			getResult().setAllocation(new HashMap<>());
		}
		getResult().getAllocation().put(orderEntry, quantity);
		return this;
	}
}
