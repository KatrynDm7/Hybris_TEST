package de.hybris.platform.warehousing.sourcing.fitness.normalize.impl;

import org.junit.Assert;
import org.junit.Test;


public class DefaultFitnessNormalizerTest
{
	private final DefaultFitnessNormalizer normalizer = new DefaultFitnessNormalizer();

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailNormalize_TotalNull()
	{
		normalizer.normalize(5.0, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailNormalize_TotalZero()
	{
		normalizer.normalize(5.0, 0.0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailNormalize_TotalNegative()
	{
		normalizer.normalize(5.0, -5.0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailNormalize_FitnessNull()
	{
		normalizer.normalize(null, 5.0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailNormalize_FitnessNegative()
	{
		normalizer.normalize(-5.0, 5.0);
	}

	@Test
	public void shouldNormalize_FitnessZero()
	{
		final Double value = normalizer.normalize(0.0, 10.0);
		Assert.assertEquals(Double.valueOf(0.0), value);
	}

	@Test
	public void shouldNormalize_FitnessNan()
	{
		final Double value = normalizer.normalize(Double.NaN, 10.0);
		Assert.assertEquals(Double.valueOf(0.0), value);
	}

	@Test
	public void shouldNormalize_TotalNan()
	{
		final Double value = normalizer.normalize(10.0, Double.NaN);
		Assert.assertEquals(Double.valueOf(0.0), value);
	}

	@Test
	public void shouldNormalize()
	{
		final Double value = normalizer.normalize(5.0, 20.0);
		Assert.assertEquals(Double.valueOf(25.0), value);
	}
}
