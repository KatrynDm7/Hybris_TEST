package de.hybris.platform.warehousing.sourcing.fitness.normalize.impl;

import de.hybris.platform.warehousing.data.sourcing.SourcingFactorIdentifiersEnum;
import de.hybris.platform.warehousing.sourcing.fitness.normalize.FitnessNormalizer;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class DefaultFitnessNormalizerFactoryTest
{
	private final DefaultFitnessNormalizerFactory factory = new DefaultFitnessNormalizerFactory();
	private Map<SourcingFactorIdentifiersEnum, FitnessNormalizer> map;

	@Mock
	private FitnessNormalizer normalizer;

	@Before
	public void setUp()
	{
		map = new HashMap<>();
		factory.setFitnessNormalizerMap(map);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailInit_NullMap() throws Exception
	{
		map = null;
		factory.afterPropertiesSet();
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailInit_EmptyMap() throws Exception
	{
		factory.afterPropertiesSet();
	}

	@Test
	public void shouldGetNormalizer() throws Exception
	{
		map.put(SourcingFactorIdentifiersEnum.ALLOCATION, normalizer);
		final FitnessNormalizer result = factory.getNormalizer(SourcingFactorIdentifiersEnum.ALLOCATION);
		Assert.assertTrue(result == normalizer);
	}
}
