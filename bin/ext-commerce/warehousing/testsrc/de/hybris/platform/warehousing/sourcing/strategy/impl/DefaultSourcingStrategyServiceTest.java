package de.hybris.platform.warehousing.sourcing.strategy.impl;

import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.sourcing.strategy.SourcingStrategy;
import de.hybris.platform.warehousing.sourcing.strategy.SourcingStrategyMapper;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


@RunWith(MockitoJUnitRunner.class)
public class DefaultSourcingStrategyServiceTest
{
	private final DefaultSourcingStrategyService sourcingStrategyService = new DefaultSourcingStrategyService();
	private final SourcingContext context = new SourcingContext();

	@Mock
	private SourcingStrategy sourcingStrategy1;
	@Mock
	private SourcingStrategy sourcingStrategy2;
	@Mock
	private SourcingStrategyMapper mapper1;
	@Mock
	private SourcingStrategyMapper mapper2;

	@Before
	public void setUp()
	{
		sourcingStrategyService.setDefaultStrategies(Lists.newArrayList(sourcingStrategy1, sourcingStrategy2));

		Mockito.when(mapper1.isMatch(context)).thenReturn(Boolean.TRUE);
		Mockito.when(mapper1.getStrategy()).thenReturn(sourcingStrategy1);

		Mockito.when(mapper2.isMatch(context)).thenReturn(Boolean.FALSE);
		Mockito.when(mapper2.getStrategy()).thenReturn(sourcingStrategy2);
	}

	@Test
	public void shouldGetNoStrategies_SingleMapper_NoMatch()
	{
		final List<SourcingStrategy> strategies = sourcingStrategyService.getStrategies(context, Lists.newArrayList(mapper2));
		Assert.assertTrue(strategies.isEmpty());
	}

	@Test
	public void shouldGetStrategies_SingleMapper_SingleMatch()
	{

		final List<SourcingStrategy> strategies = sourcingStrategyService.getStrategies(context, Lists.newArrayList(mapper1));
		Assert.assertEquals(1, strategies.size());
		Assert.assertTrue(strategies.contains(sourcingStrategy1));
	}

	@Test
	public void shouldGetStrategies_MultipleMappers_SingleMatch()
	{

		final List<SourcingStrategy> strategies = sourcingStrategyService.getStrategies(context,
				Lists.newArrayList(mapper1, mapper2));
		Assert.assertEquals(1, strategies.size());
		Assert.assertTrue(strategies.contains(sourcingStrategy1));
	}

	@Test
	public void shouldGetStrategies_MultipleMappers_MultipleMatches()
	{
		Mockito.when(mapper2.isMatch(context)).thenReturn(Boolean.TRUE);

		final List<SourcingStrategy> strategies = sourcingStrategyService.getStrategies(context,
				Lists.newArrayList(mapper1, mapper2));
		Assert.assertEquals(2, strategies.size());
		Assert.assertTrue(strategies.contains(sourcingStrategy1));
		Assert.assertTrue(strategies.contains(sourcingStrategy2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailInit_EmptyDefaultStrategies() throws Exception
	{
		sourcingStrategyService.setDefaultStrategies(Collections.emptyList());
		sourcingStrategyService.afterPropertiesSet();
		Assert.fail();
	}

	@Test
	public void shouldGetDefaultStrategies()
	{
		final List<SourcingStrategy> strategies = sourcingStrategyService.getDefaultStrategies();
		Assert.assertTrue(strategies.contains(sourcingStrategy1));
		Assert.assertTrue(strategies.contains(sourcingStrategy2));
	}

}
