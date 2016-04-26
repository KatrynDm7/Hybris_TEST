package de.hybris.platform.warehousing.sourcing.filter;


import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;

import java.util.Collections;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.common.collect.Lists;


@UnitTest
public class SourcingFilterProcessorTest
{
	@Mock
	private SourcingLocationFilter filterA;
	@Mock
	private SourcingLocationFilter filterB;
	@Mock
	private SourcingLocationFilter filterC;

	private SourcingFilterProcessor processor;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		processor = new SourcingFilterProcessor();
	}

	@Test
	public void testProcessMultipleFiltersFromList() throws Exception
	{
		processor.setFilters(Lists.newArrayList(filterA, filterB));

		doAnswer(new Answer()
		{
			@SuppressWarnings("unchecked")
			@Override
			public Object answer(final InvocationOnMock invocation)
			{
				final Object[] args = invocation.getArguments();
				// manually triggering the next filter
				filterB.filterLocations((AbstractOrderModel) args[0], (Set<WarehouseModel>) args[1]);
				return null;
			}
		}).when(filterA).filterLocations(any(AbstractOrderModel.class), any(Set.class));

		doAnswer(new Answer()
		{
			@Override
			public Object answer(final InvocationOnMock invocation)
			{
				return null;
			}
		}).when(filterB).filterLocations(any(AbstractOrderModel.class), any(Set.class));

		processor.afterPropertiesSet();
		processor.filterLocations(new AbstractOrderModel(), Collections.<WarehouseModel> emptySet());

		verify(filterA).filterLocations(any(AbstractOrderModel.class), any(Set.class));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testProcessFilterWithNullOrder()
	{
		processor.filterLocations(null, Collections.<WarehouseModel> emptySet());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testProcessFilterWithNullLocations()
	{
		processor.filterLocations(new AbstractOrderModel(), null);
	}

	@Test(expected = IllegalStateException.class)
	public void testProcessFilterNullFilters()
	{
		processor.filterLocations(new AbstractOrderModel(), Collections.<WarehouseModel> emptySet());
	}
}
