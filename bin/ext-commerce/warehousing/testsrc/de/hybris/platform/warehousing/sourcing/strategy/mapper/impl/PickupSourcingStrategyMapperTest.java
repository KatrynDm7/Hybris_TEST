package de.hybris.platform.warehousing.sourcing.strategy.mapper.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingContext;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;


public class PickupSourcingStrategyMapperTest
{
	private final PickupSourcingStrategyMapper mapper = new PickupSourcingStrategyMapper();
	private SourcingContext context;
	private AbstractOrderEntryModel orderEntry1;
	private AbstractOrderEntryModel orderEntry2;
	private PointOfServiceModel pos;

	@Before
	public void setUp()
	{
		pos = new PointOfServiceModel();

		context = new SourcingContext();
		orderEntry1 = new AbstractOrderEntryModel();
		orderEntry1.setDeliveryPointOfService(pos);

		orderEntry2 = new AbstractOrderEntryModel();
		orderEntry2.setDeliveryPointOfService(pos);
		context.setOrderEntries(Sets.newHashSet(orderEntry1, orderEntry2));
	}

	@Test
	public void shouldBeNoMatch_NoEntries()
	{
		context.setOrderEntries(Collections.emptySet());

		final Boolean match = mapper.isMatch(context);
		Assert.assertFalse(match);
	}

	@Test
	public void shouldBeNoMatch_NoPosInEntries()
	{
		orderEntry1.setDeliveryPointOfService(null);
		orderEntry2.setDeliveryPointOfService(null);

		final Boolean match = mapper.isMatch(context);
		Assert.assertFalse(match);
	}

	@Test
	public void shouldBeNoMatch_Entry1NoPos()
	{
		orderEntry1.setDeliveryPointOfService(null);

		final Boolean match = mapper.isMatch(context);
		Assert.assertFalse(match);
	}

	@Test
	public void shouldBeNoMatch_Entry2NoPos()
	{
		orderEntry2.setDeliveryPointOfService(null);

		final Boolean match = mapper.isMatch(context);
		Assert.assertFalse(match);
	}

	@Test
	public void shouldMatch_SingleEntry()
	{
		context.setOrderEntries(Sets.newHashSet(orderEntry1));

		final Boolean match = mapper.isMatch(context);
		Assert.assertTrue(match);
	}

	@Test
	public void shouldMatch_MultipleEntries()
	{
		final Boolean match = mapper.isMatch(context);
		Assert.assertTrue(match);
	}
}
