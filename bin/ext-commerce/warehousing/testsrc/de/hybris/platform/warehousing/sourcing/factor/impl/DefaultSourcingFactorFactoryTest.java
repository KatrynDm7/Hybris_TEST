package de.hybris.platform.warehousing.sourcing.factor.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.util.Config;
import de.hybris.platform.warehousing.data.sourcing.SourcingFactor;
import de.hybris.platform.warehousing.data.sourcing.SourcingFactorIdentifiersEnum;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultSourcingFactorFactoryTest
{
	private static final int WEIGHT_ALLOCATION = 60;
	private static final int WEIGHT_DISTANCE = 20;
	private static final int WEIGHT_PRIORITY = 20;
	private static final int WEIGHT_TOO_LARGE = 30;

	@Spy
	private final StubSourcingFactorFactory sourcingFactorFactory = new StubSourcingFactorFactory();

	@Before
	public void setUp() throws Exception
	{
		sourcingFactorFactory.afterPropertiesSet();
	}

	@Test
	public void shouldGetSingleFactor()
	{
		final SourcingFactor factor = sourcingFactorFactory.getSourcingFactor(SourcingFactorIdentifiersEnum.ALLOCATION);

		assertEquals(SourcingFactorIdentifiersEnum.ALLOCATION, factor.getFactorId());
		assertEquals(WEIGHT_ALLOCATION, factor.getWeight());
	}

	@Test
	public void shouldGetAllFactors()
	{
		final Set<SourcingFactor> factors = sourcingFactorFactory.getAllSourcingFactors();
		assertEquals(3, factors.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailInit_FactorWeightGreaterThan100() throws Exception
	{
		when(sourcingFactorFactory.loadFactorValue(SourcingFactorIdentifiersEnum.DISTANCE)).thenReturn(WEIGHT_TOO_LARGE);

		sourcingFactorFactory.afterPropertiesSet();

		fail("Should have failed during afterPropertiesSet()");
	}

	/**
	 * Stub to remove the loading of the weights from the {@link Config} platform utility.
	 */
	public static class StubSourcingFactorFactory extends DefaultSourcingFactorFactory
	{
		@Override
		protected int loadFactorValue(final SourcingFactorIdentifiersEnum identifier)
		{
			if (identifier.equals(SourcingFactorIdentifiersEnum.ALLOCATION))
			{
				return WEIGHT_ALLOCATION;
			}
			else if (identifier.equals(SourcingFactorIdentifiersEnum.DISTANCE))
			{
				return WEIGHT_DISTANCE;
			}
			else
			{
				return WEIGHT_PRIORITY;
			}
		}
	}
}
