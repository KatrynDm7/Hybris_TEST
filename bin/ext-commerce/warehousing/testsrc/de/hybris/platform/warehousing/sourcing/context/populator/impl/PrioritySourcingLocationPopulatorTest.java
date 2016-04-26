package de.hybris.platform.warehousing.sourcing.context.populator.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@UnitTest
public class PrioritySourcingLocationPopulatorTest
{
	private static Integer PRIORITY = 2;

	private final PrioritySourcingLocationPopulator populator = new PrioritySourcingLocationPopulator();
	private WarehouseModel warehouse;
	private SourcingLocation location;

	@Before
	public void setUp()
	{
		location = new SourcingLocation();
		warehouse = new WarehouseModel();
		warehouse.setPriority(PRIORITY);
	}

	@Test
	public void shouldSetPriority()
	{
		populator.populate(warehouse, location);
		Assert.assertEquals(PRIORITY, location.getPriority());

		warehouse.setPriority(null);
		populator.populate(warehouse, location);
		Assert.assertNull(location.getPriority());
	}
}
