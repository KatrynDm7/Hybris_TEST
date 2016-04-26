package de.hybris.platform.yacceleratorordermanagement.aspects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.warehousing.cancellation.CancellationException;
import de.hybris.platform.warehousing.data.cancellation.CancellationEntry;
import de.hybris.platform.warehousing.process.BusinessProcessException;
import de.hybris.platform.warehousing.process.WarehousingBusinessProcessService;
import de.hybris.platform.yacceleratorordermanagement.constants.YAcceleratorOrderManagementConstants;

import java.util.Collection;
import java.util.Collections;

import org.aspectj.lang.ProceedingJoinPoint;
import org.fest.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ConsignmentActionAspectTest
{
	private static final String CHOICE = "CHOICE";

	@InjectMocks
	private final ConsignmentActionAspect aspect = new ConsignmentActionAspect();

	@Mock
	private WarehousingBusinessProcessService<ConsignmentModel> consignmentBusinessProcessService;

	@Mock
	private ProceedingJoinPoint joinPoint;
	@Mock
	private ConsignmentModel consignment;
	private Collection<CancellationEntry> result;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Throwable
	{
		aspect.setChoice(CHOICE);
		aspect.setExceptionsToRethrow(Sets.newHashSet(CancellationException.class));

		when(consignment.getCode()).thenReturn("CODE");
		when(joinPoint.getArgs()).thenReturn(Arrays.array(consignment));

		when(joinPoint.proceed()).thenReturn(Collections.emptyList());
		doNothing().when(consignmentBusinessProcessService).triggerChoiceEvent(consignment,
				YAcceleratorOrderManagementConstants.CONSIGNMENT_ACTION_EVENT_NAME, CHOICE);
		doNothing().when(consignmentBusinessProcessService).triggerSimpleEvent(consignment,
				YAcceleratorOrderManagementConstants.ACTION_COMPLETION_EVENT_NAME);
	}

	@Test
	public void shouldAdviseSuccessfully_WithRemainingCancellationEntries() throws Throwable
	{
		final CancellationEntry entry1 = new CancellationEntry();
		final CancellationEntry entry2 = new CancellationEntry();
		when(joinPoint.proceed()).thenReturn(java.util.Arrays.asList(entry1, entry2));

		result = aspect.advise(joinPoint);

		verify(consignmentBusinessProcessService).triggerChoiceEvent(consignment,
				YAcceleratorOrderManagementConstants.CONSIGNMENT_ACTION_EVENT_NAME, CHOICE);
		verify(consignmentBusinessProcessService).triggerSimpleEvent(consignment,
				YAcceleratorOrderManagementConstants.ACTION_COMPLETION_EVENT_NAME);

		assertNotNull(result);
		assertEquals(2, result.size());
	}

	@Test
	public void shouldAdviseSuccessfully_NoRemainingCancellationEntry() throws Throwable
	{
		result = aspect.advise(joinPoint);

		verify(consignmentBusinessProcessService).triggerChoiceEvent(consignment,
				YAcceleratorOrderManagementConstants.CONSIGNMENT_ACTION_EVENT_NAME, CHOICE);
		verify(consignmentBusinessProcessService).triggerSimpleEvent(consignment,
				YAcceleratorOrderManagementConstants.ACTION_COMPLETION_EVENT_NAME);

		verifyAspectResult(result);
	}

	@Test(expected = BusinessProcessException.class)
	public void shouldFailAdvice_BusinessProcessException() throws Throwable
	{
		doThrow(new BusinessProcessException("error")).when(consignmentBusinessProcessService)
		.triggerChoiceEvent(consignment, YAcceleratorOrderManagementConstants.CONSIGNMENT_ACTION_EVENT_NAME, CHOICE);

		result = aspect.advise(joinPoint);

		verify(consignmentBusinessProcessService).triggerChoiceEvent(consignment,
				YAcceleratorOrderManagementConstants.CONSIGNMENT_ACTION_EVENT_NAME, CHOICE);
		verify(consignmentBusinessProcessService, never()).triggerSimpleEvent(consignment,
				YAcceleratorOrderManagementConstants.ACTION_COMPLETION_EVENT_NAME);

		verifyAspectResult(result);
	}

	@Test
	public void shouldFailAdvice_UnexpectedException() throws Throwable
	{
		doThrow(new RuntimeException("error")).when(consignmentBusinessProcessService)
		.triggerChoiceEvent(consignment, YAcceleratorOrderManagementConstants.CONSIGNMENT_ACTION_EVENT_NAME, CHOICE);

		result = aspect.advise(joinPoint);

		verify(consignmentBusinessProcessService).triggerChoiceEvent(consignment,
				YAcceleratorOrderManagementConstants.CONSIGNMENT_ACTION_EVENT_NAME, CHOICE);
		verify(consignmentBusinessProcessService).triggerSimpleEvent(consignment,
				YAcceleratorOrderManagementConstants.ACTION_COMPLETION_EVENT_NAME);

		verifyAspectResult(result);
	}

	@Test(expected = CancellationException.class)
	public void shouldFailAdvice_ExpectedException() throws Throwable
	{
		doThrow(new CancellationException("error")).when(consignmentBusinessProcessService)
		.triggerChoiceEvent(consignment, YAcceleratorOrderManagementConstants.CONSIGNMENT_ACTION_EVENT_NAME, CHOICE);

		result = aspect.advise(joinPoint);

		verify(consignmentBusinessProcessService).triggerChoiceEvent(consignment,
				YAcceleratorOrderManagementConstants.CONSIGNMENT_ACTION_EVENT_NAME, CHOICE);
		verify(consignmentBusinessProcessService).triggerSimpleEvent(consignment,
				YAcceleratorOrderManagementConstants.ACTION_COMPLETION_EVENT_NAME);

		verifyAspectResult(result);
	}

	protected void verifyAspectResult(final Collection<CancellationEntry> result)
	{
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}
}
