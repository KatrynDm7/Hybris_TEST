package de.hybris.platform.warehousing.sourcing.fitness.evaluation.impl;

import static org.hamcrest.CoreMatchers.is;

import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DistanceEvaluatorTest
{
	private DistanceEvaluator evaluator;
	private SourcingLocation location;

	@Before
	public void setup()
	{
		evaluator = new DistanceEvaluator();
		location = new SourcingLocation();
	}

	@Test
	public void shouldEvaluateDistance()
	{
		location.setDistance(1000d);

		final double loc1Eval = evaluator.evaluate(location);
		Assert.assertThat(1000d, is(loc1Eval));
	}

	@Test
	public void shouldReturnNanIfLocationDistanceIsNull()
	{
		location.setDistance(null);

		final double loc1Eval = evaluator.evaluate(location);
		Assert.assertThat(Double.NaN, is(loc1Eval));
	}

}
