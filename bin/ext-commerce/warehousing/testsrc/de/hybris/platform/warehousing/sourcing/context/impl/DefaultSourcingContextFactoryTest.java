package de.hybris.platform.warehousing.sourcing.context.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.sourcing.context.grouping.OrderEntryGroup;
import de.hybris.platform.warehousing.sourcing.context.populator.SourcingLocationPopulator;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


@UnitTest
public class DefaultSourcingContextFactoryTest
{
	private final DefaultSourcingContextFactory factory = new DefaultSourcingContextFactory();
	private AbstractOrderModel order;
	private AbstractOrderEntryModel entry1;
	private AbstractOrderEntryModel entry2;
	private WarehouseModel warehouse1;
	private WarehouseModel warehouse2;

	private final SourcingLocationPopulator slPopulator = Mockito.mock(SourcingLocationPopulator.class);

	@Before
	public void setUp()
	{
		factory.setSourcingLocationPopulators(Collections.singleton(slPopulator));

		order = new AbstractOrderModel();
		order.setCode("code");
		warehouse1 = new WarehouseModel();
		warehouse2 = new WarehouseModel();
		entry1 = new AbstractOrderEntryModel();
		entry1.setEntryNumber(1);
		entry1.setOrder(order);
		entry2 = new AbstractOrderEntryModel();
		entry2.setEntryNumber(2);
		entry2.setOrder(order);
		order.setEntries(Lists.newArrayList(entry1, entry2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailInit_SourcingLocationPopulatorsEmpty() throws Exception
	{
		factory.setSourcingLocationPopulators(Collections.emptySet());
		factory.afterPropertiesSet();
		Assert.fail();
	}

	@Test
	public void shouldCreateSourcingLocation()
	{
		final SourcingContext context = new SourcingContext();
		final SourcingLocation location = factory.createSourcingLocation(context, warehouse1);

		Mockito.verify(slPopulator).populate(warehouse1, location);
		Assert.assertEquals(warehouse1, location.getWarehouse());
	}

	@Test
	public void shouldCreateSourcingContext()
	{
		final OrderEntryGroup group = new OrderEntryGroup(Lists.newArrayList(entry1));
		final Collection<SourcingContext> contexts = factory.create(Lists.newArrayList(group),
				Sets.newHashSet(warehouse1, warehouse2));

		assertNotNull(contexts);
		assertFalse(contexts.isEmpty());
		assertEquals(1, contexts.size());

		final SourcingContext context = contexts.iterator().next();
		assertNotNull(context.getResult());
		assertNotNull(context.getOrderEntries());
		assertFalse(context.getOrderEntries().isEmpty());
		assertEquals(entry1, context.getOrderEntries().iterator().next());
		assertEquals(2, context.getSourcingLocations().size());
	}

	@Test
	public void shouldCreateSourcingContexts()
	{
		final OrderEntryGroup group1 = new OrderEntryGroup(Lists.newArrayList(entry1));
		final OrderEntryGroup group2 = new OrderEntryGroup(Lists.newArrayList(entry2));
		final Collection<SourcingContext> contexts = factory
.create(Lists.newArrayList(group1, group2),
				Sets.newHashSet(warehouse1, warehouse2));

		assertNotNull(contexts);
		assertFalse(contexts.isEmpty());
		assertEquals(2, contexts.size());

		final Iterator<SourcingContext> iterator = contexts.iterator();
		SourcingContext context = iterator.next();
		assertNotNull(context.getResult());
		assertNotNull(context.getOrderEntries());
		assertFalse(context.getOrderEntries().isEmpty());
		assertEquals(entry1, context.getOrderEntries().iterator().next());
		assertEquals(2, context.getSourcingLocations().size());

		context = iterator.next();
		assertNotNull(context.getResult());
		assertNotNull(context.getOrderEntries());
		assertFalse(context.getOrderEntries().isEmpty());
		assertEquals(entry2, context.getOrderEntries().iterator().next());
		assertEquals(2, context.getSourcingLocations().size());
	}
}
