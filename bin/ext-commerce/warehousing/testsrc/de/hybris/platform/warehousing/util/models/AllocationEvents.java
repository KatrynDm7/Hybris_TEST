package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.warehousing.model.AllocationEventModel;
import de.hybris.platform.warehousing.util.builder.AllocationEventModelBuilder;
import de.hybris.platform.warehousing.util.dao.WarehousingDao;

import org.springframework.beans.factory.annotation.Required;


public class AllocationEvents extends AbstractItems<AllocationEventModel>
{
	private WarehousingDao<AllocationEventModel> allocationEventDao;
	private Consignments consignments;

	public AllocationEventModel Camera_ShippedFromMontrealToMontrealNancyHome(final Long quantity, final StockLevelModel stockLevel)
	{
		final ConsignmentModel consignment = getConsignments().Camera_ShippedFromMontrealToMontrealNancyHome(
				ConsignmentStatus.READY, quantity);
		return getOrSaveAndReturn( //
				() -> getAllocationEventDao().getByCode(consignment.getCode()), //
				() -> AllocationEventModelBuilder.aModel() //
						.withConsignmentEntry(consignment.getConsignmentEntries().iterator().next()) //
						.withQuantity(quantity) //
						.withStockLevel(stockLevel) //
						.build());
	}

	public AllocationEventModel Camera_ShippedFromBostonToMontrealNancyHome(final Long quantity, final StockLevelModel stockLevel)
	{
		final ConsignmentModel consignment = getConsignments().Camera_ShippedFromBostonToMontrealNancyHome(ConsignmentStatus.READY,
				quantity);
		return getOrSaveAndReturn( //
				() -> getAllocationEventDao().getByCode(consignment.getCode()), //
				() -> AllocationEventModelBuilder.aModel() //
						.withConsignmentEntry(consignment.getConsignmentEntries().iterator().next()) //
						.withQuantity(quantity) //
						.withStockLevel(stockLevel) //
						.build());
	}

	public WarehousingDao<AllocationEventModel> getAllocationEventDao()
	{
		return allocationEventDao;
	}

	@Required
	public void setAllocationEventDao(final WarehousingDao<AllocationEventModel> allocationEventDao)
	{
		this.allocationEventDao = allocationEventDao;
	}

	public Consignments getConsignments()
	{
		return consignments;
	}

	@Required
	public void setConsignments(final Consignments consignments)
	{
		this.consignments = consignments;
	}

}
