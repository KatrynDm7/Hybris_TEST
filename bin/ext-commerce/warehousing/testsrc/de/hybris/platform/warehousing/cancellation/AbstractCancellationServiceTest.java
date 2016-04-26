package de.hybris.platform.warehousing.cancellation;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.warehousing.data.cancellation.CancellationEntry;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class AbstractCancellationServiceTest
{
	private final Stub abstractCancellationService = new Stub();
	private final OrderEntryModel orderEntry = new OrderEntryModel();
	private CancellationEntry cancellationEntry;

	@Before
	public void setUp()
	{
		cancellationEntry = new CancellationEntry();
		cancellationEntry.setOrderEntry(orderEntry);
		cancellationEntry.setNotes("NOTES");
		cancellationEntry.setReason(CancelReason.CUSTOMERREQUEST.getCode());
		cancellationEntry.setQuantity(5L);
	}

	@Test
	public void shouldCloneEntry()
	{
		final CancellationEntry clone = abstractCancellationService.cloneCancellationEntryWithRemainder(cancellationEntry, 3L)
				.get();

		assertEquals(orderEntry, clone.getOrderEntry());
		assertEquals("NOTES", clone.getNotes());
		assertEquals(CancelReason.CUSTOMERREQUEST.getCode(), clone.getReason());
		assertEquals(Long.valueOf(2), clone.getQuantity());
	}

	@Test(expected = NoSuchElementException.class)
	public void shouldNotCloneEntry_NoRemainder()
	{
		abstractCancellationService.cloneCancellationEntryWithRemainder(cancellationEntry, 5L).get();
	}


	private static class Stub extends AbstractCancellationService
	{
		// Empty
	}
}
