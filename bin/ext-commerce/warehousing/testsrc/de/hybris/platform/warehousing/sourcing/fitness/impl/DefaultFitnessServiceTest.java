package de.hybris.platform.warehousing.sourcing.fitness.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.warehousing.data.sourcing.FitSourcingLocation;
import de.hybris.platform.warehousing.data.sourcing.SourcingFactor;
import de.hybris.platform.warehousing.data.sourcing.SourcingFactorIdentifiersEnum;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.sourcing.factor.SourcingFactorFactory;
import de.hybris.platform.warehousing.sourcing.fitness.evaluation.FitnessEvaluator;
import de.hybris.platform.warehousing.sourcing.fitness.evaluation.FitnessEvaluatorFactory;
import de.hybris.platform.warehousing.sourcing.fitness.normalize.FitnessNormalizer;
import de.hybris.platform.warehousing.sourcing.fitness.normalize.FitnessNormalizerFactory;
import de.hybris.platform.warehousing.sourcing.util.SourcingLocationBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultFitnessServiceTest
{
	private SourcingFactor factor1;
	private SourcingFactor factor2;
	private Set<SourcingFactor> factors;
	private SourcingLocation location1;
	private SourcingLocation location2;
	private SourcingLocation location3;
	private Collection<SourcingLocation> sourcingLocations;
	private Comparator<FitSourcingLocation> fitnessComparator;

	@Mock
	private SourcingFactorFactory sourcingFactorFactory;
	@Mock
	private FitnessEvaluatorFactory fitnessEvaluatorFactory;
	@Mock
	private FitnessNormalizerFactory fitnessNormalizerFactory;
	@Mock
	private FitnessEvaluator evaluator1;
	@Mock
	private FitnessEvaluator evaluator2;
	@Mock
	private FitnessNormalizer fitnessNormalizer;

	@InjectMocks
	private final DefaultFitnessService fitnessService = new DefaultFitnessService();

	@Before
	public void setUp()
	{
		factors = new HashSet<>();
		factor1 = new SourcingFactor();
		factor1.setFactorId(SourcingFactorIdentifiersEnum.DISTANCE);
		factor1.setWeight(50);

		factor2 = new SourcingFactor();
		factor2.setFactorId(SourcingFactorIdentifiersEnum.PRIORITY);
		factor2.setWeight(50);

		factors.add(factor1);
		factors.add(factor2);

		location1 = SourcingLocationBuilder.aSourcingLocation().withWarehouseCode("loc1").build();
		location2 = SourcingLocationBuilder.aSourcingLocation().withWarehouseCode("loc2").build();
		location3 = SourcingLocationBuilder.aSourcingLocation().withWarehouseCode("loc3").build();

		sourcingLocations = new ArrayList<>();
		sourcingLocations.add(location1);
		sourcingLocations.add(location2);
		sourcingLocations.add(location3);

		fitnessComparator = new FitnessComparator();
		fitnessService.setFitnessComparator(fitnessComparator);

		Mockito.when(sourcingFactorFactory.getAllSourcingFactors()).thenReturn(factors);
		Mockito.when(fitnessEvaluatorFactory.getEvaluator(SourcingFactorIdentifiersEnum.DISTANCE)).thenReturn(evaluator1);
		Mockito.when(fitnessEvaluatorFactory.getEvaluator(SourcingFactorIdentifiersEnum.PRIORITY)).thenReturn(evaluator2);
		Mockito.when(fitnessNormalizerFactory.getNormalizer(SourcingFactorIdentifiersEnum.DISTANCE)).thenReturn(fitnessNormalizer);
		Mockito.when(fitnessNormalizerFactory.getNormalizer(SourcingFactorIdentifiersEnum.PRIORITY)).thenReturn(fitnessNormalizer);

		Mockito.when(evaluator1.evaluate(location1)).thenReturn(100d);
		Mockito.when(evaluator1.evaluate(location2)).thenReturn(300d);
		Mockito.when(evaluator1.evaluate(location3)).thenReturn(50d);

		Mockito.when(evaluator2.evaluate(location1)).thenReturn(3d);
		Mockito.when(evaluator2.evaluate(location2)).thenReturn(1d);
		Mockito.when(evaluator2.evaluate(location3)).thenReturn(2d);

		Mockito.when(fitnessNormalizer.normalize(100d, 450d)).thenReturn(100d / 450d * 100d); // 22.22
		Mockito.when(fitnessNormalizer.normalize(300d, 450d)).thenReturn(300d / 450d * 100d); // 66.66
		Mockito.when(fitnessNormalizer.normalize(50d, 450d)).thenReturn(50d / 450d * 100d); // 11.11

		Mockito.when(fitnessNormalizer.normalize(3d, 6d)).thenReturn(3d / 6d * 100d); // 50.00
		Mockito.when(fitnessNormalizer.normalize(1d, 6d)).thenReturn(1d / 6d * 100d); // 16.66
		Mockito.when(fitnessNormalizer.normalize(2d, 6d)).thenReturn(2d / 6d * 100d); // 33.33
	}

	@Test
	public void shouldCalculateFitness_SingleFactor_100Percent()
	{
		factor1.setWeight(100);
		factor2.setWeight(0);

		final FitSourcingLocation[] result = fitnessService.calculateFitness(sourcingLocations);

		Assert.assertEquals("loc1", result[0].getWarehouse().getCode());
		Assert.assertEquals(Double.valueOf(22.22222222222222d), result[0].getFitness());
		Assert.assertEquals("loc2", result[1].getWarehouse().getCode());
		Assert.assertEquals(Double.valueOf(66.66666666666666d), result[1].getFitness());
		Assert.assertEquals("loc3", result[2].getWarehouse().getCode());
		Assert.assertEquals(Double.valueOf(11.11111111111111d), result[2].getFitness());
	}

	@Test
	public void shouldCalculateFitness_SingleFactor_50Percent()
	{
		factor1.setWeight(50);
		factor2.setWeight(0);

		final FitSourcingLocation[] result = fitnessService.calculateFitness(sourcingLocations);

		Assert.assertEquals("loc1", result[0].getWarehouse().getCode());
		Assert.assertEquals(Double.valueOf(11.11111111111111d), result[0].getFitness());
		Assert.assertEquals("loc2", result[1].getWarehouse().getCode());
		Assert.assertEquals(Double.valueOf(33.33333333333333d), result[1].getFitness());
		Assert.assertEquals("loc3", result[2].getWarehouse().getCode());
		Assert.assertEquals(Double.valueOf(5.555555555555555d), result[2].getFitness());
	}

	@Test
	public void shouldCalculateFitness_MultipleFactors_0Percent()
	{
		factor1.setWeight(0);
		factor2.setWeight(0);

		final FitSourcingLocation[] result = fitnessService.calculateFitness(sourcingLocations);

		Assert.assertEquals("loc1", result[0].getWarehouse().getCode());
		Assert.assertEquals(Double.valueOf(0d), result[0].getFitness());
		Assert.assertEquals("loc2", result[1].getWarehouse().getCode());
		Assert.assertEquals(Double.valueOf(0d), result[1].getFitness());
		Assert.assertEquals("loc3", result[2].getWarehouse().getCode());
		Assert.assertEquals(Double.valueOf(0d), result[2].getFitness());
	}

	@Test
	public void shouldCalculateFitness_MultipleFactors_50Percent()
	{
		factor1.setWeight(50);
		factor2.setWeight(50);

		final FitSourcingLocation[] result = fitnessService.calculateFitness(sourcingLocations);

		Assert.assertEquals("loc1", result[0].getWarehouse().getCode());
		Assert.assertEquals(Double.valueOf(36.111111111111114d), result[0].getFitness());
		Assert.assertEquals("loc2", result[1].getWarehouse().getCode());
		Assert.assertEquals(Double.valueOf(41.66666666666666d), result[1].getFitness());
		Assert.assertEquals("loc3", result[2].getWarehouse().getCode());
		Assert.assertEquals(Double.valueOf(22.22222222222222d), result[2].getFitness());
	}

	@Test
	public void shouldSortResultsByFittest()
	{
		factor1.setWeight(100);
		factor2.setWeight(0);

		final List<SourcingLocation> result = fitnessService.sortByFitness(sourcingLocations);
		Assert.assertEquals("loc2", result.get(0).getWarehouse().getCode());
		Assert.assertEquals("loc1", result.get(1).getWarehouse().getCode());
		Assert.assertEquals("loc3", result.get(2).getWarehouse().getCode());
	}
}
