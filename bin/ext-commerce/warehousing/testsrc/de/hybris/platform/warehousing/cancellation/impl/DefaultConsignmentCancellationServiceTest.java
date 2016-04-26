package de.hybris.platform.warehousing.cancellation.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.warehousing.cancellation.CancellationException;
import de.hybris.platform.warehousing.comment.WarehousingCommentService;
import de.hybris.platform.warehousing.data.cancellation.CancellationEntry;
import de.hybris.platform.warehousing.data.comment.WarehousingCommentContext;
import de.hybris.platform.warehousing.model.CancellationConsignmentEntryEventModel;
import de.hybris.platform.warehousing.model.CancellationOrderEntryEventModel;

import java.util.Collection;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultConsignmentCancellationServiceTest
{
	private static final String COMMENT_SUBJECT = "Cancel consignment entry";

	@InjectMocks
	private final DefaultConsignmentCancellationService cancellationService = new DefaultConsignmentCancellationService();

	@Mock
	private ModelService modelService;
	@Mock
	private WarehousingCommentService<ConsignmentEntryModel> consignmentEntryCommentService;

	private ConsignmentModel consignment1;
	private ConsignmentModel consignment2;
	private ConsignmentEntryModel consignmentEntry1;
	private ConsignmentEntryModel consignmentEntry2;
	private ConsignmentEntryModel consignmentEntry3;
	private ConsignmentEntryModel consignmentEntry4;
	private OrderModel order;
	private AbstractOrderEntryModel orderEntry1;
	private AbstractOrderEntryModel orderEntry2;
	private CancellationConsignmentEntryEventModel consignmentEntryEvent;
	private CancellationOrderEntryEventModel orderEntryEvent;

	@Mock
	private ProductModel mouse;
	@Mock
	private ProductModel batteries;
	@Mock
	private UserModel author;

	@Before
	public void setUp()
	{
		order = new OrderModel();
		orderEntry1 = spy(new AbstractOrderEntryModel());
		orderEntry1.setEntryNumber(1);
		orderEntry1.setQuantity(20L);
		orderEntry2 = spy(new AbstractOrderEntryModel());
		orderEntry2.setEntryNumber(2);
		orderEntry2.setQuantity(10L);
		order.setEntries(Lists.newArrayList(orderEntry1, orderEntry2));
		order.setUser(author);

		consignment1 = new ConsignmentModel();
		consignment1.setCode("consignment1234_1");
		consignmentEntry1 = spy(new ConsignmentEntryModel());
		consignmentEntry1.setOrderEntry(orderEntry1);
		consignmentEntry1.setQuantity(10L);

		consignmentEntry2 = spy(new ConsignmentEntryModel());
		consignmentEntry2.setOrderEntry(orderEntry2);
		consignmentEntry2.setQuantity(5L);
		consignment1.setConsignmentEntries(Sets.newHashSet(consignmentEntry1, consignmentEntry2));

		consignment2 = new ConsignmentModel();
		consignment2.setCode("consignment1234_2");
		consignmentEntry3 = spy(new ConsignmentEntryModel());
		consignmentEntry3.setOrderEntry(orderEntry1);
		consignmentEntry3.setQuantity(10L);

		consignmentEntry4 = spy(new ConsignmentEntryModel());
		consignmentEntry4.setOrderEntry(orderEntry2);
		consignmentEntry4.setQuantity(5L);
		consignment2.setConsignmentEntries(Sets.newHashSet(consignmentEntry3, consignmentEntry4));

		order.setConsignments(Sets.newHashSet(consignment1, consignment2));
		consignmentEntryEvent = new CancellationConsignmentEntryEventModel();
		when(modelService.create(CancellationConsignmentEntryEventModel.class)).thenReturn(consignmentEntryEvent);
		orderEntryEvent = new CancellationOrderEntryEventModel();
		when(modelService.create(CancellationOrderEntryEventModel.class)).thenReturn(orderEntryEvent);

		when(orderEntry1.getProduct()).thenReturn(mouse);
		when(mouse.getName()).thenReturn("Wireless Mouse");
		when(orderEntry2.getProduct()).thenReturn(batteries);
		when(batteries.getName()).thenReturn("Rechargeable batteries");
	}

	@Test
	public void shouldCancelConsignmentEntry_All_PendingEqualsCancellationEntry_WithNote()
	{
		when(consignmentEntry1.getQuantityPending()).thenReturn(10L);

		final CancellationEntry cancellationEntry = new CancellationEntry();
		cancellationEntry.setOrderEntry(orderEntry1);
		cancellationEntry.setQuantity(10L);
		cancellationEntry.setNotes("notes");

		final Optional<CancellationEntry> remaining = cancellationService.cancelConsignment(consignment1, cancellationEntry);
		assertFalse(remaining.isPresent());
		verify(modelService).save(consignmentEntryEvent);
		verify(consignmentEntryCommentService).createAndSaveComment(any(WarehousingCommentContext.class));
	}

	@Test
	public void shouldCancelConsignmentEntry_All_PendingEqualsCancellationEntry_NoNote()
	{
		when(consignmentEntry1.getQuantityPending()).thenReturn(10L);

		final CancellationEntry cancellationEntry = new CancellationEntry();
		cancellationEntry.setOrderEntry(orderEntry1);
		cancellationEntry.setQuantity(10L);

		final Optional<CancellationEntry> remaining = cancellationService.cancelConsignment(consignment1, cancellationEntry);
		assertFalse(remaining.isPresent());
		verify(modelService).save(consignmentEntryEvent);
		verifyZeroInteractions(consignmentEntryCommentService);
	}

	@Test
	public void shouldCancelConsignment_All_PendingEqualsCancellationEntry()
	{
		when(consignmentEntry1.getQuantityPending()).thenReturn(10L);
		when(consignmentEntry2.getQuantityPending()).thenReturn(10L);

		final CancellationEntry cancellationEntry1 = new CancellationEntry();
		cancellationEntry1.setOrderEntry(orderEntry1);
		cancellationEntry1.setQuantity(10L);
		cancellationEntry1.setNotes("notes 1");

		final CancellationEntry cancellationEntry2 = new CancellationEntry();
		cancellationEntry2.setOrderEntry(orderEntry2);
		cancellationEntry2.setQuantity(10L);
		cancellationEntry2.setNotes("notes 2");

		final Collection<CancellationEntry> remaining = cancellationService.cancelConsignment(consignment1,
				Sets.newHashSet(cancellationEntry1, cancellationEntry2));
		assertTrue(remaining.isEmpty());
		verify(modelService, times(2)).save(consignmentEntryEvent);
		verify(consignmentEntryCommentService, times(2)).createAndSaveComment(any(WarehousingCommentContext.class));
	}

	@Test
	public void shouldCancelConsignmentEntry_All_PendingLarger()
	{
		when(consignmentEntry1.getQuantityPending()).thenReturn(10L);

		final CancellationEntry cancellationEntry = new CancellationEntry();
		cancellationEntry.setOrderEntry(orderEntry1);
		cancellationEntry.setQuantity(7L);
		cancellationEntry.setNotes("notes");

		final Optional<CancellationEntry> remaining = cancellationService.cancelConsignment(consignment1, cancellationEntry);
		assertFalse(remaining.isPresent());
		verify(modelService).save(consignmentEntryEvent);
		verify(consignmentEntryCommentService).createAndSaveComment(any(WarehousingCommentContext.class));
	}

	@Test
	public void shouldCancelConsignmentEntry_Partial_CancellationEntryLarger()
	{
		when(consignmentEntry1.getQuantityPending()).thenReturn(7L);

		final CancellationEntry cancellationEntry = new CancellationEntry();
		cancellationEntry.setOrderEntry(orderEntry1);
		cancellationEntry.setQuantity(10L);
		cancellationEntry.setNotes("notes");

		final Optional<CancellationEntry> remaining = cancellationService.cancelConsignment(consignment1, cancellationEntry);
		assertEquals(Long.valueOf(3L), remaining.get().getQuantity());
		verify(modelService).save(consignmentEntryEvent);
		verify(consignmentEntryCommentService).createAndSaveComment(any(WarehousingCommentContext.class));
	}

	@Test
	public void shouldCancelConsignmentEntry_None_NoPendingQuantity()
	{
		when(consignmentEntry1.getQuantityPending()).thenReturn(0L);

		final CancellationEntry cancellationEntry = new CancellationEntry();
		cancellationEntry.setOrderEntry(orderEntry1);
		cancellationEntry.setQuantity(10L);
		cancellationEntry.setNotes("notes");

		final Optional<CancellationEntry> remaining = cancellationService.cancelConsignment(consignment1, cancellationEntry);
		assertEquals(Long.valueOf(10L), remaining.get().getQuantity());
		verifyZeroInteractions(modelService);
		verifyZeroInteractions(consignmentEntryCommentService);
	}

	@Test(expected = CancellationException.class)
	public void shouldFailCancelConsignmentEntry_NoMatchingOrderEntry()
	{
		consignment1.setConsignmentEntries(Sets.newHashSet(consignmentEntry1));

		final CancellationEntry cancellationEntry = new CancellationEntry();
		cancellationEntry.setOrderEntry(orderEntry2);
		cancellationService.cancelConsignment(consignment1, cancellationEntry);
	}

}
