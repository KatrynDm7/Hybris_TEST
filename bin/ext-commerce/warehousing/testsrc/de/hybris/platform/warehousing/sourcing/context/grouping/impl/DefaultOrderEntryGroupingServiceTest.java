package de.hybris.platform.warehousing.sourcing.context.grouping.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.warehousing.sourcing.context.grouping.OrderEntryGroup;
import de.hybris.platform.warehousing.sourcing.context.grouping.OrderEntryMatcher;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


public class DefaultOrderEntryGroupingServiceTest
{
	private static final String MATCHING_INFO_A = "MATCH A";
	private static final String MATCHING_INFO_B = "MATCH B";
	private static final String MATCHING_INFO_C = "MATCH C";
	private static final String MATCHING_INFO_D = "MATCH D";

	private static final Integer MATCHING_NUMBER_1 = 1;
	private static final Integer MATCHING_NUMBER_2 = 2;
	private static final Integer MATCHING_NUMBER_3 = 3;
	private static final Integer MATCHING_NUMBER_4 = 4;

	private final DefaultOrderEntryGroupingService groupingService = new DefaultOrderEntryGroupingService();
	private final OrderEntryMatcher<String> infoMatcher = new InfoMatcher();
	private final OrderEntryMatcher<Integer> numberMatcher = new EntryNumberMatcher();
	private OrderEntryGroup singleGroup;


	@Before
	public void setUp()
	{

	}

	/*
	 * splitGroupByMatcher tests
	 */
	@Test
	public void shouldSplitSingleGroupByMatcherIntoSingleGroup_SingleEntry()
	{
		final AbstractOrderEntryModel orderEntry = new AbstractOrderEntryModel();
		orderEntry.setInfo(MATCHING_INFO_A);
		singleGroup = new OrderEntryGroup(Sets.newHashSet(orderEntry));

		final Set<OrderEntryGroup> groups = groupingService.splitGroupByMatcher(singleGroup, infoMatcher);
		Assert.assertEquals(1, groups.size());

		final Collection<AbstractOrderEntryModel> entries = groups.iterator().next().getEntries();
		Assert.assertEquals(1, entries.size());
		Assert.assertEquals(MATCHING_INFO_A, entries.iterator().next().getInfo());
	}

	@Test
	public void shouldSplitSingleGroupByMatcherIntoSingleGroup_MultipleEntries()
	{
		final AbstractOrderEntryModel orderEntry1 = new AbstractOrderEntryModel();
		orderEntry1.setInfo(MATCHING_INFO_A);
		final AbstractOrderEntryModel orderEntry2 = new AbstractOrderEntryModel();
		orderEntry2.setInfo(MATCHING_INFO_A);
		singleGroup = new OrderEntryGroup(Sets.newHashSet(orderEntry1, orderEntry2));

		final Set<OrderEntryGroup> groups = groupingService.splitGroupByMatcher(singleGroup, infoMatcher);
		Assert.assertEquals(1, groups.size());

		final Collection<AbstractOrderEntryModel> entries = groups.iterator().next().getEntries();
		Assert.assertEquals(2, entries.size());
		entries.forEach(entry -> Assert.assertEquals(MATCHING_INFO_A, entry.getInfo()));
	}

	@Test
	public void shouldSplitSingleGroupByMatcherIntoMultipleGroups_MultipleEntries()
	{
		final AbstractOrderEntryModel orderEntry1 = new AbstractOrderEntryModel();
		orderEntry1.setInfo(MATCHING_INFO_A);
		final AbstractOrderEntryModel orderEntry2 = new AbstractOrderEntryModel();
		orderEntry2.setInfo(MATCHING_INFO_B);
		final AbstractOrderEntryModel orderEntry3 = new AbstractOrderEntryModel();
		orderEntry3.setInfo(MATCHING_INFO_B);
		final AbstractOrderEntryModel orderEntry4 = new AbstractOrderEntryModel();
		orderEntry4.setInfo(MATCHING_INFO_A);
		singleGroup = new OrderEntryGroup(Sets.newHashSet(orderEntry1, orderEntry2, orderEntry3, orderEntry4));

		final Set<OrderEntryGroup> groups = groupingService.splitGroupByMatcher(singleGroup, infoMatcher);
		Assert.assertEquals(2, groups.size());

		for(final OrderEntryGroup group : groups)
		{
			final Collection<AbstractOrderEntryModel> entries = group.getEntries();
			Assert.assertEquals(2, entries.size());
			Assert.assertEquals(entries.iterator().next().getInfo(), entries.iterator().next().getInfo());
		}
	}

	/*
	 * splitGroupsByMatcher tests
	 */
	@Test
	public void shouldSplitSingleGroupSetByMatcherIntoSingleGroup_SingleEntry()
	{
		final AbstractOrderEntryModel orderEntry = new AbstractOrderEntryModel();
		orderEntry.setInfo(MATCHING_INFO_A);
		singleGroup = new OrderEntryGroup(Sets.newHashSet(orderEntry));

		final Set<OrderEntryGroup> groups = groupingService.splitGroupsByMatcher(Sets.newHashSet(singleGroup), infoMatcher);
		Assert.assertEquals(1, groups.size());

		final Collection<AbstractOrderEntryModel> entries = groups.iterator().next().getEntries();
		Assert.assertEquals(1, entries.size());
		Assert.assertEquals(MATCHING_INFO_A, entries.iterator().next().getInfo());
	}

	@Test
	public void shouldSplitSingleGroupSetByMatcherIntoSingleGroup_MultipleEntries()
	{
		final AbstractOrderEntryModel orderEntry1 = new AbstractOrderEntryModel();
		orderEntry1.setInfo(MATCHING_INFO_A);
		final AbstractOrderEntryModel orderEntry2 = new AbstractOrderEntryModel();
		orderEntry2.setInfo(MATCHING_INFO_A);
		singleGroup = new OrderEntryGroup(Sets.newHashSet(orderEntry1, orderEntry2));

		final Set<OrderEntryGroup> groups = groupingService.splitGroupsByMatcher(Sets.newHashSet(singleGroup), infoMatcher);
		Assert.assertEquals(1, groups.size());

		final Collection<AbstractOrderEntryModel> entries = groups.iterator().next().getEntries();
		Assert.assertEquals(2, entries.size());
		entries.forEach(entry -> Assert.assertEquals(MATCHING_INFO_A, entry.getInfo()));
	}

	@Test
	public void shouldSplitSingleGroupSetByMatcherIntoMultipleGroups_MultipleEntries()
	{
		final AbstractOrderEntryModel orderEntry1 = new AbstractOrderEntryModel();
		orderEntry1.setInfo(MATCHING_INFO_A);
		final AbstractOrderEntryModel orderEntry2 = new AbstractOrderEntryModel();
		orderEntry2.setInfo(MATCHING_INFO_B);
		final AbstractOrderEntryModel orderEntry3 = new AbstractOrderEntryModel();
		orderEntry3.setInfo(MATCHING_INFO_B);
		final AbstractOrderEntryModel orderEntry4 = new AbstractOrderEntryModel();
		orderEntry4.setInfo(MATCHING_INFO_A);
		singleGroup = new OrderEntryGroup(Sets.newHashSet(orderEntry1, orderEntry2, orderEntry3, orderEntry4));

		final Set<OrderEntryGroup> groups = groupingService.splitGroupsByMatcher(Sets.newHashSet(singleGroup), infoMatcher);
		Assert.assertEquals(2, groups.size());

		for (final OrderEntryGroup group : groups)
		{
			final Collection<AbstractOrderEntryModel> entries = group.getEntries();
			Assert.assertEquals(2, entries.size());
			Assert.assertEquals(entries.iterator().next().getInfo(), entries.iterator().next().getInfo());
		}
	}

	@Test
	public void shouldSplitMultipleGroupSetsByMatcherIntoSameGroups()
	{
		final AbstractOrderEntryModel orderEntry1 = new AbstractOrderEntryModel();
		orderEntry1.setInfo(MATCHING_INFO_A);
		final AbstractOrderEntryModel orderEntry2 = new AbstractOrderEntryModel();
		orderEntry2.setInfo(MATCHING_INFO_A);
		final AbstractOrderEntryModel orderEntry3 = new AbstractOrderEntryModel();
		orderEntry3.setInfo(MATCHING_INFO_B);
		final AbstractOrderEntryModel orderEntry4 = new AbstractOrderEntryModel();
		orderEntry4.setInfo(MATCHING_INFO_B);

		final OrderEntryGroup groupA = new OrderEntryGroup(Sets.newHashSet(orderEntry1, orderEntry2));
		final OrderEntryGroup groupB = new OrderEntryGroup(Sets.newHashSet(orderEntry3, orderEntry4));

		final Set<OrderEntryGroup> groups = groupingService.splitGroupsByMatcher(Sets.newHashSet(groupA, groupB), infoMatcher);
		Assert.assertEquals(2, groups.size());

		for (final OrderEntryGroup group : groups)
		{
			final Collection<AbstractOrderEntryModel> entries = group.getEntries();
			Assert.assertEquals(2, entries.size());
			Assert.assertEquals(entries.iterator().next().getInfo(), entries.iterator().next().getInfo());
		}
	}

	@Test
	public void shouldSplitMultipleGroupSetsByMatcherIntoSmallerGroups()
	{
		final AbstractOrderEntryModel orderEntry1 = new AbstractOrderEntryModel();
		orderEntry1.setInfo(MATCHING_INFO_A);
		final AbstractOrderEntryModel orderEntry2 = new AbstractOrderEntryModel();
		orderEntry2.setInfo(MATCHING_INFO_B);
		final AbstractOrderEntryModel orderEntry3 = new AbstractOrderEntryModel();
		orderEntry3.setInfo(MATCHING_INFO_C);
		final AbstractOrderEntryModel orderEntry4 = new AbstractOrderEntryModel();
		orderEntry4.setInfo(MATCHING_INFO_D);

		final OrderEntryGroup groupA = new OrderEntryGroup(Sets.newHashSet(orderEntry1, orderEntry2));
		final OrderEntryGroup groupB = new OrderEntryGroup(Sets.newHashSet(orderEntry3, orderEntry4));

		final Set<OrderEntryGroup> groups = groupingService.splitGroupsByMatcher(Sets.newHashSet(groupA, groupB), infoMatcher);
		Assert.assertEquals(4, groups.size());
		groups.forEach(group -> Assert.assertEquals(1, group.getEntries().size()));
	}

	/*
	 * splitOrderByMatchers tests
	 */
	@Test
	public void shouldSplitSingleEntryOrderBySingleMatcherIntoSingleGroup()
	{
		final AbstractOrderModel order = new AbstractOrderModel();
		final AbstractOrderEntryModel orderEntry = new AbstractOrderEntryModel();
		orderEntry.setInfo(MATCHING_INFO_A);
		order.setEntries(Lists.newArrayList(orderEntry));

		final Set<OrderEntryGroup> groups = groupingService.splitOrderByMatchers(order, Sets.newHashSet(infoMatcher));
		Assert.assertEquals(1, groups.size());

		final Collection<AbstractOrderEntryModel> entries = groups.iterator().next().getEntries();
		Assert.assertEquals(1, entries.size());
		Assert.assertEquals(MATCHING_INFO_A, entries.iterator().next().getInfo());
	}

	@Test
	public void shouldSplitMultiEntryOrderBySingleMatcherIntoSingleGroup()
	{
		final AbstractOrderModel order = new AbstractOrderModel();
		final AbstractOrderEntryModel orderEntry1 = new AbstractOrderEntryModel();
		orderEntry1.setInfo(MATCHING_INFO_A);
		final AbstractOrderEntryModel orderEntry2 = new AbstractOrderEntryModel();
		orderEntry2.setInfo(MATCHING_INFO_A);
		order.setEntries(Lists.newArrayList(orderEntry1, orderEntry2));

		final Set<OrderEntryGroup> groups = groupingService.splitOrderByMatchers(order, Sets.newHashSet(infoMatcher));
		Assert.assertEquals(1, groups.size());

		final Collection<AbstractOrderEntryModel> entries = groups.iterator().next().getEntries();
		Assert.assertEquals(2, entries.size());
		entries.forEach(entry -> Assert.assertEquals(MATCHING_INFO_A, entry.getInfo()));
	}

	@Test
	public void shouldSplitSingleEntryOrderByMultipleMatchersIntoSingleGroup()
	{
		final AbstractOrderModel order = new AbstractOrderModel();
		final AbstractOrderEntryModel orderEntry = new AbstractOrderEntryModel();
		orderEntry.setInfo(MATCHING_INFO_A);
		orderEntry.setEntryNumber(MATCHING_NUMBER_1);
		order.setEntries(Lists.newArrayList(orderEntry));

		final Set<OrderEntryGroup> groups = groupingService
				.splitOrderByMatchers(order, Sets.newHashSet(infoMatcher, numberMatcher));
		Assert.assertEquals(1, groups.size());

		final Collection<AbstractOrderEntryModel> entries = groups.iterator().next().getEntries();
		Assert.assertEquals(1, entries.size());

		final AbstractOrderEntryModel entry = entries.iterator().next();
		Assert.assertEquals(MATCHING_INFO_A, entry.getInfo());
		Assert.assertEquals(MATCHING_NUMBER_1, entry.getEntryNumber());
	}

	@Test
	public void shouldSplitMultiEntryOrderByMultipleMatchersIntoSingleGroup()
	{
		final AbstractOrderModel order = new AbstractOrderModel();
		final AbstractOrderEntryModel orderEntry1 = new AbstractOrderEntryModel();
		orderEntry1.setInfo(MATCHING_INFO_A);
		orderEntry1.setEntryNumber(MATCHING_NUMBER_1);
		final AbstractOrderEntryModel orderEntry2 = new AbstractOrderEntryModel();
		orderEntry2.setInfo(MATCHING_INFO_A);
		orderEntry2.setEntryNumber(MATCHING_NUMBER_1);
		order.setEntries(Lists.newArrayList(orderEntry1, orderEntry2));

		final Set<OrderEntryGroup> groups = groupingService
				.splitOrderByMatchers(order, Sets.newHashSet(infoMatcher, numberMatcher));
		Assert.assertEquals(1, groups.size());

		final Collection<AbstractOrderEntryModel> entries = groups.iterator().next().getEntries();
		Assert.assertEquals(2, entries.size());
		entries.forEach(entry -> {
			Assert.assertEquals(MATCHING_INFO_A, entry.getInfo());
			Assert.assertEquals(MATCHING_NUMBER_1, entry.getEntryNumber());
		});
	}

	@Test
	public void shouldSplitMultiEntryOrderByMultipleMatchersIntoTwoGroups()
	{
		final AbstractOrderModel order = new AbstractOrderModel();
		final AbstractOrderEntryModel orderEntry1 = new AbstractOrderEntryModel();
		orderEntry1.setInfo(MATCHING_INFO_A);
		orderEntry1.setEntryNumber(MATCHING_NUMBER_1);
		final AbstractOrderEntryModel orderEntry2 = new AbstractOrderEntryModel();
		orderEntry2.setInfo(MATCHING_INFO_A);
		orderEntry2.setEntryNumber(MATCHING_NUMBER_1);
		final AbstractOrderEntryModel orderEntry3 = new AbstractOrderEntryModel();
		orderEntry3.setInfo(MATCHING_INFO_B);
		orderEntry3.setEntryNumber(MATCHING_NUMBER_2);
		final AbstractOrderEntryModel orderEntry4 = new AbstractOrderEntryModel();
		orderEntry4.setInfo(MATCHING_INFO_B);
		orderEntry4.setEntryNumber(MATCHING_NUMBER_2);
		order.setEntries(Lists.newArrayList(orderEntry1, orderEntry2, orderEntry3, orderEntry4));

		final Set<OrderEntryGroup> groups = groupingService
				.splitOrderByMatchers(order, Sets.newHashSet(infoMatcher, numberMatcher));
		Assert.assertEquals(2, groups.size());

		for (final OrderEntryGroup group : groups)
		{
			final Collection<AbstractOrderEntryModel> entries = group.getEntries();
			Assert.assertEquals(2, entries.size());

			final Iterator<AbstractOrderEntryModel> it = entries.iterator();
			final AbstractOrderEntryModel entry1 = it.next();
			final AbstractOrderEntryModel entry2 = it.next();
			Assert.assertEquals(entry1.getInfo(), entry2.getInfo());
			Assert.assertEquals(entry1.getEntryNumber(), entry2.getEntryNumber());
		}
	}

	@Test
	public void shouldSplitMultiEntryOrderByMultipleMatchersIntoFourGroups()
	{
		final AbstractOrderModel order = new AbstractOrderModel();
		final AbstractOrderEntryModel orderEntry1 = new AbstractOrderEntryModel();
		orderEntry1.setInfo(MATCHING_INFO_A);
		orderEntry1.setEntryNumber(MATCHING_NUMBER_1);
		final AbstractOrderEntryModel orderEntry2 = new AbstractOrderEntryModel();
		orderEntry2.setInfo(MATCHING_INFO_A);
		orderEntry2.setEntryNumber(MATCHING_NUMBER_2);
		final AbstractOrderEntryModel orderEntry3 = new AbstractOrderEntryModel();
		orderEntry3.setInfo(MATCHING_INFO_B);
		orderEntry3.setEntryNumber(MATCHING_NUMBER_3);
		final AbstractOrderEntryModel orderEntry4 = new AbstractOrderEntryModel();
		orderEntry4.setInfo(MATCHING_INFO_B);
		orderEntry4.setEntryNumber(MATCHING_NUMBER_4);
		order.setEntries(Lists.newArrayList(orderEntry1, orderEntry2, orderEntry3, orderEntry4));

		final Set<OrderEntryGroup> groups = groupingService
				.splitOrderByMatchers(order, Sets.newHashSet(infoMatcher, numberMatcher));
		Assert.assertEquals(4, groups.size());
		groups.forEach(group -> Assert.assertEquals(1, group.getEntries().size()));
	}

	/**
	 * Matcher to match order entries based on {@link AbstractOrderEntryModel#getInfo()} attribute.
	 */
	private static class InfoMatcher implements OrderEntryMatcher<String>
	{
		@Override
		public String getMatchingObject(final AbstractOrderEntryModel orderEntry)
		{
			return orderEntry.getInfo();
		}
	}

	/**
	 * Matcher to match order entries based on {@link AbstractOrderEntryModel#getEntryNumber()} attribute.
	 */
	private static class EntryNumberMatcher implements OrderEntryMatcher<Integer>
	{
		@Override
		public Integer getMatchingObject(final AbstractOrderEntryModel orderEntry)
		{
			return orderEntry.getEntryNumber();
		}
	}
}
