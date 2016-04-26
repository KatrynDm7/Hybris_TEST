package de.hybris.platform.warehousing.shipping.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.warehousing.data.shipping.ShippedEntry;
import de.hybris.platform.warehousing.model.ShippedConsignmentEntryEventModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultShippingServiceTest
{
	@Mock
	private ModelService modelService;

	private ConsignmentModel consignment;
	private ConsignmentEntryModel consignmentEntry1;
	private ConsignmentEntryModel consignmentEntry2;

	@InjectMocks
	private DefaultShippingService shippingService;

	@Before
	public void setUp()
	{
		consignment = new ConsignmentModel();
		consignmentEntry1 = spy(new ConsignmentEntryModel());
		consignmentEntry1.setQuantity(10L);

		consignmentEntry2 = spy(new ConsignmentEntryModel());
		consignmentEntry2.setQuantity(5L);
		consignment.setConsignmentEntries(Sets.newHashSet(consignmentEntry1, consignmentEntry2));

		doNothing().when(modelService).save(any());
		when(modelService.create(ShippedConsignmentEntryEventModel.class)).thenReturn(new ShippedConsignmentEntryEventModel());
	}

	@Test
	public void shouldShip_1ConsignmentEntry_FullQuantity() throws ShippingException
	{
		when(consignmentEntry1.getQuantityPending()).thenReturn(10L);

		final ShippedEntry shipped = new ShippedEntry();
		shipped.setConsignmentEntry(consignmentEntry1);
		shipped.setQuantity(10L);

		shippingService.confirmShippedConsignmentEntries(Arrays.asList(shipped));
		verify(modelService).save(any());
		verify(modelService).refresh(consignmentEntry1);
	}

	@Test
	public void shouldShip_1ConsignmentEntry_PartialQuantity() throws ShippingException
	{
		when(consignmentEntry1.getQuantityPending()).thenReturn(10L);

		final ShippedEntry shipped = new ShippedEntry();
		shipped.setConsignmentEntry(consignmentEntry1);
		shipped.setQuantity(7L);

		shippingService.confirmShippedConsignmentEntries(Arrays.asList(shipped));
		verify(modelService).save(any());
		verify(modelService).refresh(consignmentEntry1);
	}

	@Test
	public void shouldShip_1ConsignmentEntry_OverQuantity() throws ShippingException
	{
		when(consignmentEntry1.getQuantityPending()).thenReturn(10L);

		final ShippedEntry shipped = new ShippedEntry();
		shipped.setConsignmentEntry(consignmentEntry1);
		shipped.setQuantity(100L);

		shippingService.confirmShippedConsignmentEntries(Arrays.asList(shipped));
		verify(modelService).save(any());
		verify(modelService).refresh(consignmentEntry1);
	}

	@Test
	public void shouldShip_2ConsignmentEntries_FullQuantity() throws ShippingException
	{
		when(consignmentEntry1.getQuantityPending()).thenReturn(10L);
		when(consignmentEntry2.getQuantityPending()).thenReturn(5L);

		final ShippedEntry shipped1 = new ShippedEntry();
		shipped1.setConsignmentEntry(consignmentEntry1);
		shipped1.setQuantity(10L);

		final ShippedEntry shipped2 = new ShippedEntry();
		shipped2.setConsignmentEntry(consignmentEntry2);
		shipped2.setQuantity(5L);

		shippingService.confirmShippedConsignmentEntries(Arrays.asList(shipped1, shipped2));
		verify(modelService, times(2)).save(any());
		verify(modelService).refresh(consignmentEntry1);
		verify(modelService).refresh(consignmentEntry2);
	}

	@Test
	public void shouldShip_2ConsignmentEntries_PartialQuantity() throws ShippingException
	{
		when(consignmentEntry1.getQuantityPending()).thenReturn(10L);
		when(consignmentEntry2.getQuantityPending()).thenReturn(5L);

		final ShippedEntry shipped1 = new ShippedEntry();
		shipped1.setConsignmentEntry(consignmentEntry1);
		shipped1.setQuantity(7L);

		final ShippedEntry shipped2 = new ShippedEntry();
		shipped2.setConsignmentEntry(consignmentEntry2);
		shipped2.setQuantity(1L);

		shippingService.confirmShippedConsignmentEntries(Arrays.asList(shipped1, shipped2));
		verify(modelService, times(2)).save(any());
		verify(modelService).refresh(consignmentEntry1);
		verify(modelService).refresh(consignmentEntry2);
	}

	@Test
	public void shouldShip_2ConsignmentEntries_OverQuantity() throws ShippingException
	{
		when(consignmentEntry1.getQuantityPending()).thenReturn(10L);
		when(consignmentEntry2.getQuantityPending()).thenReturn(5L);

		final ShippedEntry shipped1 = new ShippedEntry();
		shipped1.setConsignmentEntry(consignmentEntry1);
		shipped1.setQuantity(70L);

		final ShippedEntry shipped2 = new ShippedEntry();
		shipped2.setConsignmentEntry(consignmentEntry2);
		shipped2.setQuantity(100L);

		shippingService.confirmShippedConsignmentEntries(Arrays.asList(shipped1, shipped2));
		verify(modelService, times(2)).save(any());
		verify(modelService).refresh(consignmentEntry1);
		verify(modelService).refresh(consignmentEntry2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFail_nullShippedEntries()
	{
		shippingService.confirmShippedConsignmentEntries(null);
	}

	@Test
	public void shouldNotShip_EmptyCollection()
	{
		shippingService.confirmShippedConsignmentEntries(Collections.emptyList());
		verifyZeroInteractions(modelService);
	}

	@Test
	public void shouldCreateShippedEntry()
	{
		when(consignmentEntry1.getQuantityPending()).thenReturn(5L);
		final ShippedEntry entry = shippingService.createShippedEntry(consignmentEntry1);

		assertEquals(consignmentEntry1.getQuantityPending(), entry.getQuantity());
		assertEquals(consignmentEntry1, entry.getConsignmentEntry());
	}

	@Test
	public void shouldCreateShippedEntries()
	{
		when(consignmentEntry1.getQuantityPending()).thenReturn(5L);
		when(consignmentEntry2.getQuantityPending()).thenReturn(1L);
		final List<ShippedEntry> entries = new ArrayList<ShippedEntry>(shippingService.createShippedEntries(consignment));

		assertNotNull(entries);
		assertEquals(2, entries.size());

		entries.forEach(action -> {
			if (action.getConsignmentEntry().equals(consignmentEntry1))
			{
				assertEquals(consignmentEntry1, action.getConsignmentEntry());
				assertEquals(consignmentEntry1.getQuantityPending(), action.getQuantity());

			}
			else if (action.getConsignmentEntry().equals(consignmentEntry2))
			{

				assertEquals(consignmentEntry2, action.getConsignmentEntry());
				assertEquals(consignmentEntry2.getQuantityPending(), action.getQuantity());

			}
			else
			{
				Assert.fail("Should not have more consignments");
			}

		});
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFail_nullConsignment()
	{
		shippingService.createShippedEntries(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFail_nullConsignmentEntry()
	{
		shippingService.createShippedEntry(null);
	}
}
