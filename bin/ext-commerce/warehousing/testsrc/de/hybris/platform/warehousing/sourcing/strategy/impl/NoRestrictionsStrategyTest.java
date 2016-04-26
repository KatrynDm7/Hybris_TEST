package de.hybris.platform.warehousing.sourcing.strategy.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.data.sourcing.SourcingFactor;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.data.sourcing.SourcingResult;
import de.hybris.platform.warehousing.sourcing.factor.SourcingFactorFactory;
import de.hybris.platform.warehousing.sourcing.fitness.FitnessService;
import de.hybris.platform.warehousing.sourcing.result.impl.DefaultSourcingResultFactory;
import de.hybris.platform.warehousing.sourcing.util.SourcingContextBuilder;
import de.hybris.platform.warehousing.sourcing.util.SourcingLocationBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


@UnitTest
public class NoRestrictionsStrategyTest
{

	private NoRestrictionsStrategy noRestrictionsStrategy;
	private FitnessService fitnessCalculationMock;

	enum Product
	{
		PROPANE_BARBECUE, CHARCOAL_BARBECUE
	}

	enum Warehouse
	{
		RECIFE, OLINDA, PAULISTA
	}

	@Before
	public void setup()
	{
		noRestrictionsStrategy = new NoRestrictionsStrategy();

		fitnessCalculationMock = Mockito.mock(FitnessService.class);
		noRestrictionsStrategy.setFitnessService(fitnessCalculationMock);

		noRestrictionsStrategy.setSourcingResultFactory(new DefaultSourcingResultFactory());

		final SourcingFactorFactory sourcingFactorFactoryMock = Mockito.mock(SourcingFactorFactory.class);
		noRestrictionsStrategy.setSourcingFactorFactory(sourcingFactorFactoryMock);

		Mockito.when(sourcingFactorFactoryMock.getAllSourcingFactors()).thenReturn(new HashSet<SourcingFactor>());
	}

	/**
	 * Given an order with 2 entries:<br>
	 * entry 1 : {quantity: 10, product: PROPANE_BARBECUE}<br>
	 * entry 2 : {quantity: 20, product: CHARCOAL_BARBECUE}<br>
	 *
	 * Result:<br>
	 * It should source complete from 1 location<br>
	 * {Warehouse: RECIFE, Availability : {[PROPANE_BARBECUE,10], [CHARCOAL_BARBECUE,20}}<br>
	 *
	 * Assert:<br>
	 * It verifies the number of {@link SourcingResult}<br>
	 * It verifies the {@link PointOfService} where the order entries were sourced<br>
	 * It checks if the source is completed.
	 */
	@Test
	public void testSourcingNoRestriction1Location()
	{
		//add entry 1
		final ProductModel propaneBarbecue = new ProductModel();
		propaneBarbecue.setCode(Product.PROPANE_BARBECUE.name());
		final long QTY_PROPANE_BARBECUE = 10;

		//add entry 2
		final ProductModel charcoalBarbecue = new ProductModel();
		charcoalBarbecue.setCode(Product.CHARCOAL_BARBECUE.name());
		final long QTY_CHARCOAL_BARBECUE = 20;

		//add location
		final long STOCK_RECIFE_PROPANE_BARBECUE = 10;
		final long STOCK_RECIFE_CHARCOAL_BARBECUE = 20;
		final SourcingLocation location1 = SourcingLocationBuilder.aSourcingLocation()
				.withCustomAvailabilityProductQuantity(propaneBarbecue, STOCK_RECIFE_PROPANE_BARBECUE)
				.withCustomAvailabilityProductQuantity(charcoalBarbecue, STOCK_RECIFE_CHARCOAL_BARBECUE)
				.withWarehouseCode(Warehouse.RECIFE.name()).build();

		final SourcingContext context = SourcingContextBuilder.aSourcingContext()
				.withOrderEntry(createOrderEntry(QTY_PROPANE_BARBECUE, propaneBarbecue))
				.withOrderEntry(createOrderEntry(QTY_CHARCOAL_BARBECUE, charcoalBarbecue)).withSourcingLocation(location1).build();

		Mockito.when(fitnessCalculationMock.sortByFitness(Mockito.anyCollectionOf(SourcingLocation.class))).thenReturn(
				new ArrayList<>(context.getSourcingLocations()));

		noRestrictionsStrategy.source(context);
		final Set<SourcingResult> results = context.getResult().getResults();

		Assert.assertEquals(1, results.size());

		Assert.assertEquals(1,
 results.stream().filter(result -> result.getWarehouse().getCode().equals(Warehouse.RECIFE.name()))
				.count());

		assertEqualsQuantityAllocation(results, Warehouse.RECIFE.name(), 2);


		Assert.assertTrue(context.getResult().isComplete());

		assertEqualQuantity(context, STOCK_RECIFE_PROPANE_BARBECUE, propaneBarbecue, Warehouse.RECIFE.name());
		assertEqualQuantity(context, STOCK_RECIFE_CHARCOAL_BARBECUE, charcoalBarbecue, Warehouse.RECIFE.name());

	}

	/**
	 * Given an order with 2 entries:<br>
	 * entry 1 : {quantity: 10, product: PROPANE_BARBECUE}<br>
	 * entry 2 : {quantity: 20, product: CHARCOAL_BARBECUE}<br>
	 *
	 * Result:<br>
	 * It should source PROPANE_BARBECUE from 2 locations<br>
	 * {Warehouse: RECIFE, Availability : {[PROPANE_BARBECUE,5], [CHARCOAL_BARBECUE,20}}<br>
	 * {Warehouse: OLINDA, Availability : {[PROPANE_BARBECUE,5]}
	 *
	 * Assert:<br>
	 * It verifies the number of {@link SourcingResult}<br>
	 * It verifies the {@link PointOfService} where the order entries were sourced<br>
	 * It checks if the source is completed.
	 */
	@Test
	public void testSourcingNoRestrictionSplit2Location()
	{
		//add entry 1
		final ProductModel propaneBarbecue = new ProductModel();
		propaneBarbecue.setCode(Product.PROPANE_BARBECUE.name());
		final long QTY_PROPANE_BARBECUE = 10;
		//add entry 2
		final ProductModel charcoalBarbecue = new ProductModel();
		charcoalBarbecue.setCode(Product.CHARCOAL_BARBECUE.name());
		final long QTY_CHARCOAL_BARBECUE = 20;

		final long STOCK_RECIFE_PROPANE_BARBECUE = 5;
		final long STOCK_RECIFE_CHARCOAL_BARBECUE = 20;
		final long STOCK_OLINDA_PROPANE_BARBECUE = 5;
		final SourcingLocation location1 = SourcingLocationBuilder.aSourcingLocation()
				.withCustomAvailabilityProductQuantity(propaneBarbecue, STOCK_RECIFE_PROPANE_BARBECUE)
				.withCustomAvailabilityProductQuantity(charcoalBarbecue, STOCK_RECIFE_CHARCOAL_BARBECUE)
				.withWarehouseCode(Warehouse.RECIFE.name()).build();

		final SourcingLocation location2 = SourcingLocationBuilder.aSourcingLocation()
				.withCustomAvailabilityProductQuantity(propaneBarbecue, STOCK_OLINDA_PROPANE_BARBECUE)
				.withWarehouseCode(Warehouse.OLINDA.name()).build();

		final SourcingContext context = SourcingContextBuilder.aSourcingContext()
				.withOrderEntry(createOrderEntry(QTY_PROPANE_BARBECUE, propaneBarbecue))
				.withOrderEntry(createOrderEntry(QTY_CHARCOAL_BARBECUE, charcoalBarbecue)).withSourcingLocation(location1)
				.withSourcingLocation(location2).build();

		Mockito.when(fitnessCalculationMock.sortByFitness(Mockito.anyCollectionOf(SourcingLocation.class))).thenReturn(
				new ArrayList<>(context.getSourcingLocations()));

		noRestrictionsStrategy.source(context);
		final Set<SourcingResult> results = context.getResult().getResults();
		Assert.assertEquals(2, results.size());



		final Predicate<? super SourcingResult> filter1 = result -> result.getWarehouse().getCode().equals(Warehouse.RECIFE.name());
		Assert.assertEquals(1, results.stream().filter(filter1).count());

		assertEqualsQuantityAllocation(results, Warehouse.RECIFE.name(), 2);


		final Predicate<? super SourcingResult> filter2 = result -> result.getWarehouse().getCode().equals(Warehouse.OLINDA.name());
		Assert.assertEquals(1, results.stream().filter(filter2).count());

		assertEqualsQuantityAllocation(results, Warehouse.OLINDA.name(), 1);

		Assert.assertTrue(context.getResult().isComplete());

		//Assert quantity
		assertEqualQuantity(context, STOCK_RECIFE_PROPANE_BARBECUE, propaneBarbecue, Warehouse.RECIFE.name());
		assertEqualQuantity(context, STOCK_RECIFE_CHARCOAL_BARBECUE, charcoalBarbecue, Warehouse.RECIFE.name());
		assertEqualQuantity(context, STOCK_OLINDA_PROPANE_BARBECUE, propaneBarbecue, Warehouse.OLINDA.name());
	}



	/**
	 * Given an order with 2 entries:<br>
	 * entry 1 : {quantity: 10, product: PROPANE_BARBECUE}<br>
	 * entry 2 : {quantity: 20, product: CHARCOAL_BARBECUE}<br>
	 *
	 * Result:<br>
	 * It should source PROPANE_BARBECUE from 3 locations<br>
	 * {Warehouse: RECIFE, Availability : {[PROPANE_BARBECUE,5], [CHARCOAL_BARBECUE,20}}<br>
	 * {Warehouse: OLINDA, Availability : {[PROPANE_BARBECUE,3]} <br>
	 * {Warehouse: PAULISTA, Availability : {[PROPANE_BARBECUE,2]}
	 *
	 * Assert:<br>
	 * It verifies the number of {@link SourcingResult}<br>
	 * It verifies the {@link PointOfService} where the order entries were sourced<br>
	 * It checks if the source is completed.
	 */
	@Test
	public void testSourceCompleteFrom3Locations()
	{
		//add entry 1
		final long QTY_PROPANE_BARBECUE = 10;
		final ProductModel propaneBarbecue = new ProductModel();
		propaneBarbecue.setCode(Product.PROPANE_BARBECUE.name());

		//add entry 2
		final long QTY_CHARCOAL_BARBECUE = 20;
		final ProductModel charcoalBarbecue = new ProductModel();
		charcoalBarbecue.setCode(Product.CHARCOAL_BARBECUE.name());

		final long STOCK_RECIFE_PROPANE_BARBECUE = 5;
		final long STOCK_RECIFE_CHARCOAL_BARBECUE = 20;
		final long STOCK_OLINDA_PROPANE_BARBECUE = 3;
		final long STOCK_PAULISTA_PROPANE_BARBECUE = 2;

		final SourcingLocation location1 = SourcingLocationBuilder.aSourcingLocation()
				.withCustomAvailabilityProductQuantity(propaneBarbecue, STOCK_RECIFE_PROPANE_BARBECUE)
				.withCustomAvailabilityProductQuantity(charcoalBarbecue, STOCK_RECIFE_CHARCOAL_BARBECUE)
				.withWarehouseCode(Warehouse.RECIFE.name()).build();

		final SourcingLocation location2 = SourcingLocationBuilder.aSourcingLocation()
				.withCustomAvailabilityProductQuantity(propaneBarbecue, STOCK_OLINDA_PROPANE_BARBECUE)
				.withWarehouseCode(Warehouse.OLINDA.name()).build();

		final SourcingLocation location3 = SourcingLocationBuilder.aSourcingLocation()
				.withCustomAvailabilityProductQuantity(propaneBarbecue, STOCK_PAULISTA_PROPANE_BARBECUE)
				.withWarehouseCode(Warehouse.PAULISTA.name()).build();

		final SourcingContext context = SourcingContextBuilder.aSourcingContext()
				.withOrderEntry(createOrderEntry(QTY_PROPANE_BARBECUE, propaneBarbecue))
				.withOrderEntry(createOrderEntry(QTY_CHARCOAL_BARBECUE, charcoalBarbecue)).withSourcingLocation(location1)
				.withSourcingLocation(location2).withSourcingLocation(location3).build();

		Mockito.when(fitnessCalculationMock.sortByFitness(Mockito.anyCollectionOf(SourcingLocation.class))).thenReturn(
				new ArrayList<>(context.getSourcingLocations()));

		noRestrictionsStrategy.source(context);

		final Set<SourcingResult> results = context.getResult().getResults();

		Assert.assertEquals(3, results.size());

		//RECIFE
		Assert.assertEquals(1,
 results.stream().filter(result -> result.getWarehouse().getCode().equals(Warehouse.RECIFE.name()))
				.count());

		assertEqualsQuantityAllocation(results, Warehouse.RECIFE.name(), 2);


		//OLINDA
		Assert.assertEquals(
				1,
				results.stream()
.filter(result -> result.getWarehouse().getCode().equals(Warehouse.OLINDA.name()))
				.count());
		assertEqualsQuantityAllocation(results, Warehouse.OLINDA.name(), 1);

		//PAULISTA
		Assert.assertEquals(
				1,
				results.stream()
.filter(result -> result.getWarehouse().getCode().equals(Warehouse.PAULISTA.name()))
				.count());

		assertEqualsQuantityAllocation(results, Warehouse.PAULISTA.name(), 1);


		Assert.assertTrue(context.getResult().isComplete());

		//Assert quantity sourced
		assertEqualQuantity(context, STOCK_RECIFE_PROPANE_BARBECUE, propaneBarbecue, Warehouse.RECIFE.name());
		assertEqualQuantity(context, STOCK_RECIFE_CHARCOAL_BARBECUE, charcoalBarbecue, Warehouse.RECIFE.name());
		assertEqualQuantity(context, STOCK_OLINDA_PROPANE_BARBECUE, propaneBarbecue, Warehouse.OLINDA.name());
		assertEqualQuantity(context, STOCK_PAULISTA_PROPANE_BARBECUE, propaneBarbecue, Warehouse.PAULISTA.name());
	}



	/**
	 * Given an order with 2 entries:<br>
	 * entry 1 : {quantity: 10, product: PROPANE_BARBECUE}<br>
	 * entry 2 : {quantity: 20, product: CHARCOAL_BARBECUE}<br>
	 *
	 * Result: It should not source. There is not enough stock for both entries<br>
	 * {Warehouse: RECIFE, Availability : {[PROPANE_BARBECUE,0]}<br>
	 * {Warehouse: OLINDA, Availability : {[CHARCOAL_BARBECUE,0]}
	 *
	 * Assert:<br>
	 * It verifies the number of {@link SourcingResult}<br>
	 * It verifies the {@link PointOfService} where the order entries were sourced<br>
	 * It checks if the source is completed.
	 */
	@Test
	public void testNothingSourced()
	{
		//add entry 1
		final long QTY_PROPANE_BARBECUE = 10;
		final ProductModel propaneBarbecue = new ProductModel();
		propaneBarbecue.setCode(Product.PROPANE_BARBECUE.name());

		//add entry 2
		final long QTY_CHARCOAL_BARBECUE = 20;
		final ProductModel charcoalBarbecue = new ProductModel();
		charcoalBarbecue.setCode(Product.CHARCOAL_BARBECUE.name());

		final long STOCK = 0;

		final SourcingLocation location1 = SourcingLocationBuilder.aSourcingLocation()
				.withCustomAvailabilityProductQuantity(propaneBarbecue, STOCK).withWarehouseCode(Warehouse.RECIFE.name()).build();

		final SourcingLocation location2 = SourcingLocationBuilder.aSourcingLocation()
				.withCustomAvailabilityProductQuantity(propaneBarbecue, STOCK).withWarehouseCode(Warehouse.OLINDA.name()).build();

		final SourcingContext context = SourcingContextBuilder.aSourcingContext()
				.withOrderEntry(createOrderEntry(QTY_PROPANE_BARBECUE, propaneBarbecue))
				.withOrderEntry(createOrderEntry(QTY_CHARCOAL_BARBECUE, charcoalBarbecue)).withSourcingLocation(location1)
				.withSourcingLocation(location2).build();

		Mockito.when(fitnessCalculationMock.sortByFitness(Mockito.anyCollectionOf(SourcingLocation.class))).thenReturn(
				new ArrayList<>(context.getSourcingLocations()));
		noRestrictionsStrategy.source(context);

		Assert.assertFalse(context.getResult().isComplete());
		Assert.assertTrue(context.getResult().getResults().isEmpty());
	}


	/**
	 * Given an order with 2 entries:<br>
	 * entry 1 : {quantity: 10, product: PROPANE_BARBECUE}<br>
	 * entry 2 : {quantity: 20, product: CHARCOAL_BARBECUE}<br>
	 *
	 * Result: It should not source - not enough stock for PROPANE_BARBECUE<br>
	 * SourcingResult<br>
	 * {Warehouse: RECIFE, Availability : {[PROPANE_BARBECUE,5]}<br>
	 * {Warehouse: OLINDA, Availability : {[CHARCOAL_BARBECUE,20]}
	 *
	 * Assert:<br>
	 * It verifies the number of {@link SourcingResult}<br>
	 * It verifies the {@link PointOfService} where the order entries were sourced<br>
	 * It checks if the source is completed. It verifies the quantity sourced for each entry.
	 */
	@Test
	public void testPartiallySourced()
	{
		//add entry 1
		final ProductModel propaneBarbecue = new ProductModel();
		propaneBarbecue.setCode(Product.PROPANE_BARBECUE.name());
		final long QTY_PROPANE_BARBECUE = 10;

		//add entry 2
		final ProductModel charcoalBarbecue = new ProductModel();
		charcoalBarbecue.setCode(Product.CHARCOAL_BARBECUE.name());
		final long QTY_CHARCOAL_BARBECUE = 20;


		final long STOCK_RECIFE_PROPANE_BARBECUE = 5;
		final long STOCK_OLINDA_CHARCOAL_BARBECUE = 20;

		final SourcingLocation location1 = SourcingLocationBuilder.aSourcingLocation()
				.withCustomAvailabilityProductQuantity(propaneBarbecue, STOCK_RECIFE_PROPANE_BARBECUE)
				.withWarehouseCode(Warehouse.RECIFE.name()).build();

		final SourcingLocation location2 = SourcingLocationBuilder.aSourcingLocation()
				.withCustomAvailabilityProductQuantity(propaneBarbecue, STOCK_OLINDA_CHARCOAL_BARBECUE)
				.withWarehouseCode(Warehouse.OLINDA.name()).build();

		final SourcingContext context = SourcingContextBuilder.aSourcingContext()
				.withOrderEntry(createOrderEntry(QTY_PROPANE_BARBECUE, propaneBarbecue))
				.withOrderEntry(createOrderEntry(QTY_CHARCOAL_BARBECUE, charcoalBarbecue)).withSourcingLocation(location1)
				.withSourcingLocation(location2).build();

		Mockito.when(fitnessCalculationMock.sortByFitness(Mockito.anyCollectionOf(SourcingLocation.class))).thenReturn(
				new ArrayList<>(context.getSourcingLocations()));

		noRestrictionsStrategy.source(context);


		Assert.assertEquals(2, context.getResult().getResults().size());

		Assert.assertFalse(context.getResult().isComplete());

		//Assert quantity sourced
		assertEqualQuantity(context, STOCK_RECIFE_PROPANE_BARBECUE, propaneBarbecue, Warehouse.RECIFE.name());
		assertEqualQuantity(context, STOCK_OLINDA_CHARCOAL_BARBECUE, propaneBarbecue, Warehouse.OLINDA.name());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFail_nullContext()
	{
		noRestrictionsStrategy.source(null);
	}

	private AbstractOrderEntryModel createOrderEntry(final Long quantity, final ProductModel product)
	{
		final AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
		entry.setQuantity(quantity);
		entry.setProduct(product);
		return entry;
	}

	protected void assertEqualQuantity(final SourcingContext context, final long qtyExpected, final ProductModel product,
			final String posName)
	{
		final Predicate<? super SourcingResult> predicate = p -> p.getWarehouse().getCode().equals(posName)
				&& p.getAllocation().get(product) != null && p.getAllocation().get(product).longValue() == qtyExpected;

		final Optional<SourcingResult> result = context.getResult().getResults().stream().filter(predicate).findFirst();

		result.ifPresent(consumer -> {
			Assert.assertEquals(qtyExpected, consumer.getAllocation().get(product).longValue());
		});
	}

	protected void assertEqualsQuantityAllocation(final Set<SourcingResult> results, final String pos, final int qtyExpected)
	{
		results.stream().filter(result -> result.getWarehouse().getCode().equals(pos)).findFirst().ifPresent(consumer -> {
			Assert.assertEquals(qtyExpected, consumer.getAllocation().size());
		});
	}
}
