package de.hybris.platform.warehousing.sourcing.strategy.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.data.sourcing.SourcingResult;
import de.hybris.platform.warehousing.data.sourcing.SourcingResults;
import de.hybris.platform.warehousing.sourcing.factor.SourcingFactorFactory;
import de.hybris.platform.warehousing.sourcing.fitness.FitnessService;
import de.hybris.platform.warehousing.sourcing.result.SourcingResultFactory;
import de.hybris.platform.warehousing.sourcing.util.SourcingContextBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class NoSplittingStrategyTest
{

	private static final Long QUANTITY_MILK = new Long(4);
	private static final Long QUANTITY_CHEESE = new Long(3);
	private static final Long NO_STOCK = new Long(0);
	private static final Long SUFFICIENT_STOCK = new Long(5);

	@Mock
	private FitnessService fitnessService;
	@Mock
	private SourcingFactorFactory sourcingFactorFactory;
	@Mock
	private SourcingResultFactory sourcingResultFactory;

	private WarehouseModel montreal;
	private WarehouseModel brossard;
	private ProductModel milk;
	private ProductModel cheese;

	enum Warehouse
	{
		Montreal, Brossard
	}

	@InjectMocks
	private NoSplittingStrategy noSplittingStratgey;

	@Before
	public void setUp() throws Exception
	{
		montreal = new WarehouseModel();
		montreal.setCode(Warehouse.Montreal.name());
		brossard = new WarehouseModel();
		brossard.setCode(Warehouse.Brossard.name());

		milk = new ProductModel();
		milk.setCode("milk");
		cheese = new ProductModel();
		cheese.setCode("cheese");
	}

	/**
	 * Given an order with 2 entries:<br>
	 * entry 1 : {quantity: 3, product: cheese}<br>
	 * entry 2 : {quantity: 4, product: milk}<br>
	 *
	 * Result:<br>
	 * the source should not complete. There is no stock in the locations<br>
	 * {Warehouse: Montreal, Availability : {[cheese,0], [milk,0]}<br>
	 * {Warehouse: Brossard, Availability : {[cheese,0], [milk,0]}<br>
	 *
	 * Assert:<br>
	 * It verifies the number of {@link SourcingResult}<br>
	 * It checks if the source is completed.
	 */
	@Test
	public void noSplittingSourcingNoStock()
	{
		final SourcingLocation montrealAvailability = createSourcingLocation(montreal);
		final Map<ProductModel, Long> availability = new HashMap<ProductModel, Long>();
		availability.put(cheese, NO_STOCK);
		availability.put(milk, NO_STOCK);
		montrealAvailability.setAvailability(availability);

		final SourcingResults results = new SourcingResults();
		results.setResults(Sets.newHashSet());

		final SourcingContextBuilder builder = new SourcingContextBuilder().withOrderEntry(createOrderEntry(QUANTITY_MILK, milk))
				.withOrderEntry(createOrderEntry(QUANTITY_CHEESE, cheese)).withSourcingLocation(montrealAvailability)
				.withResults(results);

		final SourcingContext context = builder.build();

		when(fitnessService.sortByFitness(anyCollectionOf(SourcingLocation.class))).thenReturn(
				new ArrayList<SourcingLocation>(context.getSourcingLocations()));

		noSplittingStratgey.source(context);
		assertTrue(context.getResult().getResults().isEmpty());

		assertFalse(context.getResult().isComplete());
	}

	/**
	 * Given an order with 2 entries:<br>
	 * entry 1 : {quantity: 3, product: cheese}<br>
	 * entry 2 : {quantity: 4, product: milk}<br>
	 *
	 * Result:<br>
	 * the source should complete using the first location (Montreal)<br>
	 * {Warehouse: Montreal, Availability : {[cheese,4], [milk,3]}<br>
	 * {Warehouse: Brossard, Availability : {[cheese,4], [milk,3]}<br>
	 *
	 * Assert:<br>
	 * It verifies the number of {@link SourcingResult}<br>
	 * It checks if the source is completed.
	 */
	@Test
	public void noSplittingSourceFirstLocation()
	{
		final Map<ProductModel, Long> availability = new HashMap<ProductModel, Long>();
		availability.put(milk, QUANTITY_MILK);
		availability.put(cheese, QUANTITY_CHEESE);

		final SourcingLocation montrelStock = createSourcingLocation(montreal);
		montrelStock.setAvailability(availability);
		final SourcingLocation brossardStock = createSourcingLocation(brossard);
		brossardStock.setAvailability(availability);

		final SourcingResults results = new SourcingResults();
		results.setResults(Sets.newHashSet());

		final SourcingContextBuilder builder = new SourcingContextBuilder().withOrderEntry(createOrderEntry(QUANTITY_MILK, milk))
				.withOrderEntry(createOrderEntry(QUANTITY_CHEESE, cheese)).withSourcingLocation(montrelStock)
				.withSourcingLocation(brossardStock).withResults(results);

		final SourcingContext context = builder.build();

		when(fitnessService.sortByFitness(anyCollectionOf(SourcingLocation.class))).thenReturn(
				new ArrayList<SourcingLocation>(context.getSourcingLocations()));
		when(sourcingResultFactory.create(anyCollectionOf(AbstractOrderEntryModel.class), any(SourcingLocation.class))).thenReturn(
				createSourcingResult(montreal, context));

		noSplittingStratgey.source(context);

		assertEquals(1, context.getResult().getResults().size());
		assertTrue(context.getResult().isComplete());
	}

	/**
	 * Given an order with 2 entries:<br>
	 * entry 1 : {quantity: 3, product: cheese}<br>
	 * entry 2 : {quantity: 4, product: milk}<br>
	 *
	 * Result:<br>
	 * the source should complete using the second location(Brossard)<br>
	 * {Warehouse: Montreal, Availability : {[cheese,0], [milk,0]}<br>
	 * {Warehouse: Brossard, Availability : {[cheese,5], [milk,5]}<br>
	 *
	 * Assert:<br>
	 * It verifies the number of {@link SourcingResult}<br>
	 * It checks if the source is completed.
	 */
	@Test
	public void noSplittingSourceSecondLocation()
	{
		final SourcingLocation montrealStock = createSourcingLocation(montreal);
		final Map<ProductModel, Long> availability = new HashMap<ProductModel, Long>();
		availability.put(milk, NO_STOCK);
		availability.put(cheese, NO_STOCK);
		montrealStock.setAvailability(availability);

		final SourcingLocation brossardStock = createSourcingLocation(brossard);
		final Map<ProductModel, Long> avail2 = new HashMap<ProductModel, Long>();
		avail2.put(milk, SUFFICIENT_STOCK);
		avail2.put(cheese, SUFFICIENT_STOCK);
		brossardStock.setAvailability(avail2);

		final SourcingResults results = new SourcingResults();
		results.setResults(Sets.newHashSet());
		final SourcingContextBuilder builder = new SourcingContextBuilder().withOrderEntry(createOrderEntry(QUANTITY_MILK, milk))
				.withOrderEntry(createOrderEntry(QUANTITY_CHEESE, cheese)).withSourcingLocation(montrealStock)
				.withSourcingLocation(brossardStock).withResults(results);

		final SourcingContext context = builder.build();

		when(fitnessService.sortByFitness(anyCollectionOf(SourcingLocation.class))).thenReturn(
				new ArrayList<SourcingLocation>(context.getSourcingLocations()));
		when(sourcingResultFactory.create(anyCollectionOf(AbstractOrderEntryModel.class), any(SourcingLocation.class))).thenReturn(
				createSourcingResult(brossard, context));

		noSplittingStratgey.source(context);

		assertEquals(1, context.getResult().getResults().size());

		final SourcingResult result = context.getResult().getResults().iterator().next();
		assertTrue(context.getResult().isComplete());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFail_nullContext()
	{
		noSplittingStratgey.source(null);
	}

	private SourcingLocation createSourcingLocation(final WarehouseModel warehouse)
	{
		final SourcingLocation location = new SourcingLocation();
		location.setWarehouse(warehouse);
		return location;
	}

	private AbstractOrderEntryModel createOrderEntry(final Long quantity, final ProductModel product)
	{
		final AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
		entry.setQuantity(quantity);
		entry.setProduct(product);
		return entry;
	}

	private SourcingResult createSourcingResult(final WarehouseModel warehouse, final SourcingContext context)
	{
		final SourcingResult sourcingResult = new SourcingResult();
		sourcingResult.setWarehouse(warehouse);
		final Map<AbstractOrderEntryModel, Long> allocations = new HashMap<>();
		context.getOrderEntries().forEach(entry -> allocations.put(entry, entry.getQuantity()));
		sourcingResult.setAllocation(allocations);
		return sourcingResult;
	}
}
