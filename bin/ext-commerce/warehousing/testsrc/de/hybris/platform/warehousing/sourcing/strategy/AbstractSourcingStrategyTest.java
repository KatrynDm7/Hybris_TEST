package de.hybris.platform.warehousing.sourcing.strategy;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.data.sourcing.SourcingResult;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@UnitTest
public class AbstractSourcingStrategyTest
{

	private AbstractSourcingStrategy abstractStrategy;

	@Before
	public void setUp() throws Exception
	{
		abstractStrategy = new AbstractSourcingStrategy()
		{
			@Override
			public void source(final SourcingContext sourcingContext)
			{
			}
		};
	}

	@Test
	public void checkAvailabilityForProduct()
	{
		final ProductModel productModel = new ProductModel();
		final SourcingLocation sourcing = new SourcingLocation();

		final Map<ProductModel, Long> availability = new HashMap<ProductModel, Long>();
		availability.put(productModel, 5L);
		sourcing.setAvailability(availability);


		final Long availabilityForProduct = abstractStrategy.getAvailabilityForProduct(productModel, sourcing);
		Assert.assertEquals(5L, availabilityForProduct.longValue());
	}

	@Test
	public void noAvailabilityForSourcingLocation()
	{
		final ProductModel productModel = new ProductModel();
		final SourcingLocation sourcing = new SourcingLocation();


		final Long availabilityForProduct = abstractStrategy.getAvailabilityForProduct(productModel, sourcing);
		Assert.assertEquals(0L, availabilityForProduct.longValue());
	}

	@Test
	public void checkAvailabilityForDifferentProductModel()
	{
		final ProductModel cheese = new ProductModel();
		final ProductModel cracker = new ProductModel();
		final SourcingLocation sourcing = new SourcingLocation();

		final Map<ProductModel, Long> availability = new HashMap<ProductModel, Long>();
		availability.put(cheese, 5L);
		sourcing.setAvailability(availability);


		final Long availabilityForProduct = abstractStrategy.getAvailabilityForProduct(cracker, sourcing);
		Assert.assertEquals(0L, availabilityForProduct.longValue());
	}

	@Test
	public void checkQuantitySourced()
	{


		final Set<SourcingResult> results = new HashSet<SourcingResult>();
		final SourcingResult result = new SourcingResult();
		result.setWarehouse(new WarehouseModel());

		final Map<AbstractOrderEntryModel, Long> allocation = new HashMap<AbstractOrderEntryModel, Long>();

		final AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
		final ProductModel chicken = new ProductModel();
		entry.setProduct(chicken);
		allocation.put(entry, 5L);
		result.setAllocation(allocation);
		results.add(result);

		final long quantitySourced = abstractStrategy.getQuantitySourced(results, entry);
		Assert.assertEquals(5L, quantitySourced);
	}


	@Test
	public void checkQuantitySourcedNoAllocation()
	{
		final ProductModel chicken = new ProductModel();
		final AbstractOrderEntryModel chickenEntry = new AbstractOrderEntryModel();
		chickenEntry.setProduct(chicken);


		final ProductModel beef = new ProductModel();
		final Set<SourcingResult> results = new HashSet<SourcingResult>();
		final SourcingResult result = new SourcingResult();
		result.setWarehouse(new WarehouseModel());
		final Map<AbstractOrderEntryModel, Long> allocation = new HashMap<AbstractOrderEntryModel, Long>();
		final AbstractOrderEntryModel beefEntry = new AbstractOrderEntryModel();
		beefEntry.setProduct(beef);
		allocation.put(beefEntry, 5L);

		final long quantitySourced = abstractStrategy.getQuantitySourced(results, chickenEntry);
		Assert.assertEquals(0L, quantitySourced);
	}
}
