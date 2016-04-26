package de.hybris.platform.warehousing.sourcing.fitness.evaluation.impl;

import static org.hamcrest.CoreMatchers.is;

import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class PriorityEvaluatorTest
{
	private PriorityEvaluator evaluator;
	private SourcingLocation location;

	@Before
	public void setup()
	{
		evaluator = new PriorityEvaluator();
		location = new SourcingLocation();
	}

	@Test
	public void shouldEvaluatePriority()
	{
		location.setPriority(5);

		final double loc1Eval = evaluator.evaluate(location);
		Assert.assertThat(5.0, is(loc1Eval));
	}

	@Test
	public void shouldReturnNanIfLocationPriorityIsNull()
	{
		location.setPriority(null);

		final double loc1Eval = evaluator.evaluate(location);
		Assert.assertThat(Double.NaN, is(loc1Eval));
	}
}
