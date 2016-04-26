package de.hybris.platform.warehousing.sourcing.fitness.evaluation.impl;

import static org.hamcrest.CoreMatchers.is;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;


public class AllocationEvaluatorTest
{
	private static final Long FIVE = 5L;
	private static final Long FIFTEEN = 15L;
	private static final Long TEN = 10L;

	private static final String CODE_PRODUCT1 = "sku1";
	private static final String CODE_PRODUCT2 = "sku2";

	private final AllocationFitnessEvaluator evaluator = new AllocationFitnessEvaluator();

	private AbstractOrderEntryModel entry1;
	private AbstractOrderEntryModel entry2;

	private Map<ProductModel, Long> availability;

	private ProductModel product1;
	private ProductModel product2;

	private SourcingContext sourcingContext;
	private SourcingLocation sourcingLocation;

	@Before
	public void setup()
	{
		product1 = new ProductModel();
		product1.setCode(CODE_PRODUCT1);
		product2 = new ProductModel();
		product2.setCode(CODE_PRODUCT2);

		entry1 = new AbstractOrderEntryModel();
		entry1.setProduct(product1);
		entry2 = new AbstractOrderEntryModel();
		entry2.setProduct(product2);

		sourcingContext = new SourcingContext();
		sourcingContext.setOrderEntries(Sets.newHashSet(entry1, entry2));

		availability = new HashMap<>();

		sourcingLocation = new SourcingLocation();
		sourcingLocation.setAvailability(availability);
		sourcingLocation.setContext(sourcingContext);
	}

	@Test
	public void shouldHandleFullAvailability()
	{
		// Given
		entry1.setQuantity(TEN);
		entry2.setQuantity(FIVE);
		availability.put(product1, TEN);
		availability.put(product2, TEN);

		// When
		final double quantityUnassigned = evaluator.evaluate(sourcingLocation);

		// Then
		Assert.assertThat(0d, is(quantityUnassigned));
	}

	@Test
	public void shouldHandlePartialAvailability()
	{
		// Given
		entry1.setQuantity(TEN);
		entry2.setQuantity(FIFTEEN);
		availability.put(product1, TEN);
		availability.put(product2, TEN);

		// When
		final double quantityUnassigned = evaluator.evaluate(sourcingLocation);

		// Then
		Assert.assertThat(5d, is(quantityUnassigned));
	}

	@Test
	public void shouldHandleLargeAvailability()
	{
		// Given
		entry1.setQuantity(TEN);
		entry2.setQuantity(FIFTEEN);
		availability.put(product1, 1000L);
		availability.put(product2, 1000L);

		// When
		final double quantityUnassigned = evaluator.evaluate(sourcingLocation);

		// Then
		Assert.assertThat(0d, is(quantityUnassigned));
	}

	@Test
	public void shouldHandleNoAvailability()
	{
		// Given
		entry1.setQuantity(TEN);
		entry2.setQuantity(FIFTEEN);
		availability.put(product1, 0L);
		availability.put(product2, 0L);

		// When
		final double quantityUnassigned = evaluator.evaluate(sourcingLocation);

		// Then
		Assert.assertThat(25d, is(quantityUnassigned));
	}

}
