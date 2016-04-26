package de.hybris.platform.warehousing.sourcing.fitness.impl;

import de.hybris.platform.warehousing.data.sourcing.FitSourcingLocation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FitnessComparatorTest
{
	private FitSourcingLocation location1;
	private FitSourcingLocation location2;
	private FitnessComparator comparator;

	@Before
	public void setup()
	{
		location1 = new FitSourcingLocation();
		location2 = new FitSourcingLocation();
		comparator = new FitnessComparator();
	}

	@Test
	public void shouldCompareEquals()
	{
		// Given
		location1.setFitness(50d);
		location2.setFitness(50d);

		// Then
		final int comparisonResult = comparator.compare(location1, location2);
		Assert.assertTrue(0 == comparisonResult);
	}

	@Test
	public void shouldCompareGreaterThan()
	{
		// Given
		location1.setFitness(70d);
		location2.setFitness(50d);

		// Then
		final int comparisonResult = comparator.compare(location1, location2);
		Assert.assertTrue(-1 == comparisonResult);
	}

	@Test
	public void shouldCompareLessThan()
	{
		// Given
		location1.setFitness(20d);
		location2.setFitness(50d);

		// Then
		final int comparisonResult = comparator.compare(location1, location2);
		Assert.assertTrue(1 == comparisonResult);
	}
}
