package de.hybris.platform.warehousing.sourcing.context.grouping.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.warehousing.sourcing.context.grouping.OrderEntryGroup;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Sets;


public class OrderEntryGroupTest
{

	@Test
	public void shouldCreateGroup()
	{
		final AbstractOrderEntryModel entry = new AbstractOrderEntryModel();

		final Collection<AbstractOrderEntryModel> entries = Sets.newHashSet(entry);
		final OrderEntryGroup group = new OrderEntryGroup(entries);

		Assert.assertEquals(entries.iterator().next(), group.getEntries().iterator().next());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailCreateGroup_NullEntries()
	{
		new OrderEntryGroup(null);
		Assert.fail();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void shouldReturnImmutableList()
	{
		final AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
		final Collection<AbstractOrderEntryModel> entries = Sets.newHashSet(entry);
		final OrderEntryGroup group = new OrderEntryGroup(entries);

		group.getEntries().add(entry);
	}
}
