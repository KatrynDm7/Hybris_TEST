package de.hybris.platform.warehousing.sourcing.result.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.data.sourcing.SourcingResult;
import de.hybris.platform.warehousing.data.sourcing.SourcingResults;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Sets;


@UnitTest
public class DefaultSourcingResultFactoryTest
{
	private final DefaultSourcingResultFactory factory = new DefaultSourcingResultFactory();

	@Test
	public void shouldCreateSourcingResult_singleOrderEntry()
	{
		final AbstractOrderEntryModel orderEntry = new AbstractOrderEntryModel();
		final SourcingLocation location = new SourcingLocation();
		final WarehouseModel warehouse = new WarehouseModel();
		location.setWarehouse(warehouse);
		final Long quantity = Long.valueOf(0L);

		final SourcingResult result = factory.create(orderEntry, location, quantity);
		assertTrue(orderEntry == result.getAllocation().keySet().iterator().next());
		assertTrue(warehouse == result.getWarehouse());
		assertTrue(quantity == result.getAllocation().values().iterator().next());
	}

	@Test
	public void shouldCreateSourcingResult_MultiOrderEntries()
	{
		final AbstractOrderEntryModel entry1 = new AbstractOrderEntryModel();
		entry1.setQuantity(2L);
		final AbstractOrderEntryModel entry2 = new AbstractOrderEntryModel();
		entry2.setQuantity(3L);
		final SourcingLocation location = new SourcingLocation();
		final WarehouseModel warehouse = new WarehouseModel();
		location.setWarehouse(warehouse);

		final SourcingResult result = factory.create(Arrays.asList(entry1, entry2), location);
		assertEquals(warehouse, result.getWarehouse());
		assertNotNull(result.getAllocation());
		assertFalse(result.getAllocation().isEmpty());
		assertEquals(2, result.getAllocation().size());
		assertTrue(result.getAllocation().containsKey(entry1));
		assertTrue(2L == result.getAllocation().get(entry1));
		assertTrue(result.getAllocation().containsKey(entry2));
		assertTrue(3L == result.getAllocation().get(entry2));
		assertTrue(warehouse == result.getWarehouse());
	}

	@Test
	public void shouldMergeResults()
	{
		final SourcingResults results1 = new SourcingResults();
		final SourcingResult result1 = new SourcingResult();
		final SourcingResult result2 = new SourcingResult();
		final WarehouseModel warehouse = new WarehouseModel();

		final Map<AbstractOrderEntryModel, Long> allocation1 = new HashMap<AbstractOrderEntryModel, Long>();
		allocation1.put(new AbstractOrderEntryModel(), 5l);
		result1.setAllocation(allocation1);
		result1.setWarehouse(warehouse);

		final Map<AbstractOrderEntryModel, Long> allocation2 = new HashMap<AbstractOrderEntryModel, Long>();
		allocation2.put(new AbstractOrderEntryModel(), 5l);
		result2.setWarehouse(warehouse);
		result2.setAllocation(allocation2);
		results1.setResults(Sets.newHashSet(result1, result2));

		final SourcingResults results2 = new SourcingResults();
		final SourcingResult result3 = new SourcingResult();
		final SourcingResult result4 = new SourcingResult();
		final Map<AbstractOrderEntryModel, Long> allocation3 = new HashMap<AbstractOrderEntryModel, Long>();
		allocation3.put(new AbstractOrderEntryModel(), 5l);
		result3.setAllocation(allocation3);
		result3.setWarehouse(warehouse);

		final Map<AbstractOrderEntryModel, Long> allocation4 = new HashMap<AbstractOrderEntryModel, Long>();
		allocation4.put(new AbstractOrderEntryModel(), 5l);
		result4.setAllocation(allocation4);
		result4.setWarehouse(warehouse);
		results2.setResults(Sets.newHashSet(result3, result4));

		final SourcingResults results = factory.create(Sets.newHashSet(results1, results2));
		assertEquals(4, results.getResults().size());
		assertTrue(results.getResults().contains(result1));
		assertTrue(results.getResults().contains(result2));
		assertTrue(results.getResults().contains(result3));
		assertTrue(results.getResults().contains(result4));
	}

	@Test
	public void shouldMergeComplete_AllComplete()
	{
		final WarehouseModel warehouse = new WarehouseModel();
		final Map<AbstractOrderEntryModel, Long> allocation1 = new HashMap<AbstractOrderEntryModel, Long>();
		allocation1.put(new AbstractOrderEntryModel(), 5l);

		final SourcingResults results1 = new SourcingResults();
		final SourcingResult result1 = new SourcingResult();
		result1.setWarehouse(warehouse);
		result1.setAllocation(allocation1);

		final SourcingResult result2 = new SourcingResult();
		result2.setWarehouse(warehouse);
		result2.setAllocation(allocation1);
		results1.setResults(Sets.newHashSet(result1, result2));
		results1.setComplete(Boolean.TRUE);

		final SourcingResults results2 = new SourcingResults();
		final SourcingResult result3 = new SourcingResult();
		result3.setWarehouse(warehouse);
		result3.setAllocation(allocation1);
		final SourcingResult result4 = new SourcingResult();
		result4.setWarehouse(warehouse);
		result4.setAllocation(allocation1);
		results2.setResults(Sets.newHashSet(result3, result4));
		results2.setComplete(Boolean.TRUE);

		final SourcingResults results = factory.create(Sets.newHashSet(results1, results2));
		assertTrue(results.isComplete());
	}

	@Test
	public void shouldMergeIncomplete_AllIncomplete()
	{
		final WarehouseModel warehouse = new WarehouseModel();
		final Map<AbstractOrderEntryModel, Long> allocation1 = new HashMap<AbstractOrderEntryModel, Long>();
		allocation1.put(new AbstractOrderEntryModel(), 5l);

		final SourcingResults results1 = new SourcingResults();
		final SourcingResult result1 = new SourcingResult();
		result1.setWarehouse(warehouse);
		result1.setAllocation(allocation1);

		final SourcingResult result2 = new SourcingResult();
		result2.setWarehouse(warehouse);
		result2.setAllocation(allocation1);
		results1.setResults(Sets.newHashSet(result1, result2));
		results1.setComplete(Boolean.FALSE);


		final SourcingResults results2 = new SourcingResults();
		final SourcingResult result3 = new SourcingResult();
		final SourcingResult result4 = new SourcingResult();
		results2.setResults(Sets.newHashSet(result3, result4));
		results2.setComplete(Boolean.FALSE);
		result3.setWarehouse(warehouse);
		result3.setAllocation(allocation1);
		result4.setWarehouse(warehouse);
		result4.setAllocation(allocation1);
		final SourcingResults results = factory.create(Sets.newHashSet(results1, results2));
		assertFalse(results.isComplete());
	}

	@Test
	public void shouldMergeIncomplete_PartialComplete()
	{
		final WarehouseModel warehouse = new WarehouseModel();
		final Map<AbstractOrderEntryModel, Long> allocation1 = new HashMap<AbstractOrderEntryModel, Long>();
		allocation1.put(new AbstractOrderEntryModel(), 5l);

		final SourcingResults results1 = new SourcingResults();
		final SourcingResult result1 = new SourcingResult();
		result1.setWarehouse(warehouse);
		result1.setAllocation(allocation1);
		final SourcingResult result2 = new SourcingResult();
		result2.setWarehouse(warehouse);
		result2.setAllocation(allocation1);

		results1.setResults(Sets.newHashSet(result1, result2));
		results1.setComplete(Boolean.TRUE);

		final SourcingResults results2 = new SourcingResults();
		final SourcingResult result3 = new SourcingResult();
		result3.setWarehouse(warehouse);
		result3.setAllocation(allocation1);
		final SourcingResult result4 = new SourcingResult();
		result4.setWarehouse(warehouse);
		result4.setAllocation(allocation1);
		results2.setResults(Sets.newHashSet(result3, result4));
		results2.setComplete(Boolean.FALSE);

		final SourcingResults results = factory.create(Sets.newHashSet(results1, results2));
		assertFalse(results.isComplete());
	}

	@Test
	public void shouldDoNothing_Incomplete()
	{
		final SourcingResults results = factory.create(Sets.newHashSet());
		assertFalse(results.isComplete());
	}
}
