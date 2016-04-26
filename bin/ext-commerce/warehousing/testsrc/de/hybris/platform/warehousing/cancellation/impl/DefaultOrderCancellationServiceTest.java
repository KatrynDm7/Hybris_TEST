package de.hybris.platform.warehousing.cancellation.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
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
import de.hybris.platform.warehousing.cancellation.strategy.ConsignmentSelectionStrategy;
import de.hybris.platform.warehousing.comment.WarehousingCommentService;
import de.hybris.platform.warehousing.data.cancellation.CancellationEntry;
import de.hybris.platform.warehousing.data.comment.WarehousingCommentContext;
import de.hybris.platform.warehousing.model.CancellationConsignmentEntryEventModel;
import de.hybris.platform.warehousing.model.CancellationOrderEntryEventModel;

import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultOrderCancellationServiceTest
{
	private static final String ORDER_COMMENT_SUBJECT = "Cancel order entry";
	private static final String CONSIGNMENT_COMMENT_SUBJECT = "Cancel consignment entry";

	@InjectMocks
	private final DefaultOrderCancellationService cancellationService = new DefaultOrderCancellationService();

	@Mock
	private ModelService modelService;
	@Mock
	private ConsignmentSelectionStrategy consignmentSelectionStrategy;
	@Mock
	private WarehousingCommentService<AbstractOrderEntryModel> orderEntryCommentService;
	@Mock
	private WarehousingCommentService<ConsignmentEntryModel> consignmentEntryCommentService;

	private DefaultConsignmentCancellationService consignmentCancellationService;
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
		consignmentCancellationService = new DefaultConsignmentCancellationService();
		consignmentCancellationService.setModelService(modelService);
		consignmentCancellationService.setConsignmentEntryCommentService(consignmentEntryCommentService);
		cancellationService.setConsignmentCancellationService(consignmentCancellationService);

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
		orderEntryEvent = new CancellationOrderEntryEventModel();
		when(modelService.create(CancellationOrderEntryEventModel.class)).thenReturn(orderEntryEvent);
		consignmentEntryEvent = new CancellationConsignmentEntryEventModel();
		when(modelService.create(CancellationConsignmentEntryEventModel.class)).thenReturn(consignmentEntryEvent);

		when(orderEntry1.getProduct()).thenReturn(mouse);
		when(mouse.getName()).thenReturn("Wireless Mouse");
		when(orderEntry2.getProduct()).thenReturn(batteries);
		when(batteries.getName()).thenReturn("Rechargeable batteries");

		when(
				consignmentSelectionStrategy.selectConsignments(Mockito.any(OrderModel.class),
						Mockito.anyCollectionOf(CancellationEntry.class))).thenReturn(Lists.newArrayList(consignment1, consignment2));
	}

	/* Order Cancellation */
	@Test
	public void shouldCancelOrder_All_NoneAllocated_WithNote()
	{
		when(orderEntry1.getQuantityAllocated()).thenReturn(0L);

		final CancellationEntry cancellationEntry = new CancellationEntry();
		cancellationEntry.setOrderEntry(orderEntry1);
		cancellationEntry.setQuantity(10L);
		cancellationEntry.setNotes("notes");

		final Collection<CancellationEntry> remaining = cancellationService.cancelOrder(order, Sets.newHashSet(cancellationEntry));
		assertTrue(remaining.isEmpty());
		verify(modelService).save(orderEntryEvent);
		verify(orderEntryCommentService).createAndSaveComment(any(WarehousingCommentContext.class));
	}

	@Test
	public void shouldCancelOrder_All_NoneAllocated_NoNote()
	{
		when(orderEntry1.getQuantityAllocated()).thenReturn(0L);

		final CancellationEntry cancellationEntry = new CancellationEntry();
		cancellationEntry.setOrderEntry(orderEntry1);
		cancellationEntry.setQuantity(10L);

		final Collection<CancellationEntry> remaining = cancellationService.cancelOrder(order, Sets.newHashSet(cancellationEntry));
		assertTrue(remaining.isEmpty());
		verify(modelService).save(orderEntryEvent);
		verifyZeroInteractions(orderEntryCommentService);
	}

	@Test
	public void shouldCancelOrder_Partial_NoneAllocated_NoConsignments()
	{
		when(
				consignmentSelectionStrategy.selectConsignments(Mockito.any(OrderModel.class),
						Mockito.anyCollectionOf(CancellationEntry.class))).thenReturn(Collections.emptyList());
		when(orderEntry1.getQuantityAllocated()).thenReturn(0L);

		final CancellationEntry cancellationEntry = new CancellationEntry();
		cancellationEntry.setOrderEntry(orderEntry1);
		cancellationEntry.setQuantity(30L);

		final Collection<CancellationEntry> remaining = cancellationService.cancelOrder(order, Sets.newHashSet(cancellationEntry));
		assertEquals(Long.valueOf(10L), remaining.stream().findFirst().get().getQuantity());
		verify(modelService).save(orderEntryEvent);
	}

	@Test
	public void shouldCancelOrder_All_AllAllocated_1Consignment()
	{
		when(
				consignmentSelectionStrategy.selectConsignments(Mockito.any(OrderModel.class),
						Mockito.anyCollectionOf(CancellationEntry.class))).thenReturn(Lists.newArrayList(consignment1));
		when(orderEntry1.getQuantityAllocated()).thenReturn(20L);
		when(consignmentEntry1.getQuantityPending()).thenReturn(10L);

		final CancellationEntry cancellationEntry = new CancellationEntry();
		cancellationEntry.setOrderEntry(orderEntry1);
		cancellationEntry.setQuantity(10L);
		cancellationEntry.setNotes("notes");

		final Collection<CancellationEntry> remaining = cancellationService.cancelOrder(order, Sets.newHashSet(cancellationEntry));
		assertTrue(remaining.isEmpty());
		verify(modelService, never()).save(orderEntryEvent);
		verify(consignmentEntryCommentService).createAndSaveComment(any(WarehousingCommentContext.class));
	}

	@Test
	public void shouldCancelOrder_All_AllAllocated_2Consignments()
	{
		when(orderEntry1.getQuantityAllocated()).thenReturn(20L);
		when(consignmentEntry1.getQuantityPending()).thenReturn(10L);
		when(consignmentEntry3.getQuantityPending()).thenReturn(10L);

		final CancellationEntry cancellationEntry = new CancellationEntry();
		cancellationEntry.setOrderEntry(orderEntry1);
		cancellationEntry.setQuantity(20L);
		cancellationEntry.setNotes("notes");

		final Collection<CancellationEntry> remaining = cancellationService.cancelOrder(order, Sets.newHashSet(cancellationEntry));
		assertTrue(remaining.isEmpty());
		verify(modelService, never()).save(orderEntryEvent);
		verify(consignmentEntryCommentService, times(2)).createAndSaveComment(any(WarehousingCommentContext.class));
	}

	@Test
	public void shouldCancelOrder_All_AllAllocated_2Consignments_2CancelEntries_affectAllConsignmentEntries()
	{
		when(orderEntry1.getQuantityAllocated()).thenReturn(20L);
		when(orderEntry2.getQuantityAllocated()).thenReturn(10L);
		when(consignmentEntry1.getQuantityPending()).thenReturn(10L);
		when(consignmentEntry2.getQuantityPending()).thenReturn(5L);
		when(consignmentEntry3.getQuantityPending()).thenReturn(10L);
		when(consignmentEntry4.getQuantityPending()).thenReturn(5L);

		final CancellationEntry cancellationEntry1 = new CancellationEntry();
		cancellationEntry1.setOrderEntry(orderEntry2);
		cancellationEntry1.setQuantity(10L);

		final CancellationEntry cancellationEntry2 = new CancellationEntry();
		cancellationEntry2.setOrderEntry(orderEntry1);
		cancellationEntry2.setQuantity(20L);

		final Collection<CancellationEntry> remaining = cancellationService.cancelOrder(order,
				Sets.newHashSet(cancellationEntry2, cancellationEntry1));
		assertTrue(remaining.isEmpty());
		verify(modelService, times(0)).save(orderEntryEvent);
	}

	@Test
	public void shouldCancelOrder_Partial_AllAllocated_2Consignments_2CancelEntries_affect3ConsignmentEntries()
	{
		when(orderEntry1.getQuantityAllocated()).thenReturn(20L);
		when(orderEntry2.getQuantityAllocated()).thenReturn(10L);
		when(consignmentEntry1.getQuantityPending()).thenReturn(10L);
		when(consignmentEntry2.getQuantityPending()).thenReturn(5L);
		when(consignmentEntry3.getQuantityPending()).thenReturn(10L);
		when(consignmentEntry4.getQuantityPending()).thenReturn(5L);

		final CancellationEntry cancellationEntry1 = new CancellationEntry();
		cancellationEntry1.setOrderEntry(orderEntry2);
		cancellationEntry1.setQuantity(7L);

		final CancellationEntry cancellationEntry2 = new CancellationEntry();
		cancellationEntry2.setOrderEntry(orderEntry1);
		cancellationEntry2.setQuantity(10L);

		final Collection<CancellationEntry> remaining = cancellationService.cancelOrder(order,
				Sets.newHashSet(cancellationEntry2, cancellationEntry1));
		assertTrue(remaining.isEmpty());
		verify(modelService, times(0)).save(orderEntryEvent);
	}

	@Test
	public void shouldCancelOrder_Partial_AllAllocated_2Consignment()
	{
		when(orderEntry1.getQuantityAllocated()).thenReturn(20L);
		when(consignmentEntry1.getQuantityPending()).thenReturn(10L);
		when(consignmentEntry3.getQuantityPending()).thenReturn(10L);

		final CancellationEntry cancellationEntry = new CancellationEntry();
		cancellationEntry.setOrderEntry(orderEntry1);
		cancellationEntry.setQuantity(30L);

		final Collection<CancellationEntry> remaining = cancellationService.cancelOrder(order, Sets.newHashSet(cancellationEntry));
		assertEquals(Long.valueOf(10L), remaining.stream().findFirst().get().getQuantity());
		verify(modelService, times(0)).save(orderEntryEvent);
	}

	@Test
	public void shouldCancelOrder_All_HalfAllocatedHalfNotAllocated_1Consignment()
	{
		when(
				consignmentSelectionStrategy.selectConsignments(Mockito.any(OrderModel.class),
						Mockito.anyCollectionOf(CancellationEntry.class))).thenReturn(Lists.newArrayList(consignment1));
		when(orderEntry1.getQuantityAllocated()).thenReturn(15L);
		when(consignmentEntry1.getQuantityPending()).thenReturn(5L);

		final CancellationEntry cancellationEntry = new CancellationEntry();
		cancellationEntry.setOrderEntry(orderEntry1);
		cancellationEntry.setQuantity(10L);

		final Collection<CancellationEntry> remaining = cancellationService.cancelOrder(order, Sets.newHashSet(cancellationEntry));
		assertTrue(remaining.isEmpty());
		verify(modelService).save(orderEntryEvent);
	}

	@Test
	public void shouldCancelOrder_All_HalfAllocatedHalfNotAllocated_2Consignment()
	{
		when(orderEntry1.getQuantityAllocated()).thenReturn(10L);
		when(consignmentEntry1.getQuantityPending()).thenReturn(5L);
		when(consignmentEntry3.getQuantityPending()).thenReturn(5L);

		final CancellationEntry cancellationEntry = new CancellationEntry();
		cancellationEntry.setOrderEntry(orderEntry1);
		cancellationEntry.setQuantity(20L);

		final Collection<CancellationEntry> remaining = cancellationService.cancelOrder(order, Sets.newHashSet(cancellationEntry));
		assertTrue(remaining.isEmpty());
		verify(modelService).save(orderEntryEvent);
	}

	@Test
	public void shouldCancelOrder_Partial_HalfAllocatedHalfNotAllocated_2Consignment()
	{
		when(orderEntry1.getQuantityAllocated()).thenReturn(10L);
		when(consignmentEntry1.getQuantityPending()).thenReturn(5L);
		when(consignmentEntry3.getQuantityPending()).thenReturn(5L);

		final CancellationEntry cancellationEntry = new CancellationEntry();
		cancellationEntry.setOrderEntry(orderEntry1);
		cancellationEntry.setQuantity(30L);

		final Collection<CancellationEntry> remaining = cancellationService.cancelOrder(order, Sets.newHashSet(cancellationEntry));
		assertEquals(Long.valueOf(10L), remaining.stream().findFirst().get().getQuantity());
		verify(modelService).save(orderEntryEvent);
	}
}
