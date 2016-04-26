package de.hybris.platform.warehousing.process.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.warehousing.process.BusinessProcessException;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultOrderProcessServiceTest
{
	private static final String SUFFIX = "SUFFIX";
	private static final String PROCESS_CODE = "PROCESS_CODE";
	private static final String CODE = "CODE";
	private static final String EXPECTED_PREFIX = PROCESS_CODE + "-" + CODE;

	@InjectMocks
	private final DefaultOrderProcessService service = new DefaultOrderProcessService();

	@Mock
	private BaseStoreService baseStoreService;

	@Mock
	private OrderModel order;
	@Mock
	private BaseStoreModel baseStore;
	@Mock
	private OrderProcessModel orderProcess1;
	@Mock
	private OrderProcessModel orderProcess2;

	@Before
	public void setUp()
	{
		when(orderProcess1.getCode()).thenReturn("GARBAGE");
		when(orderProcess2.getCode()).thenReturn(EXPECTED_PREFIX + SUFFIX);

		when(order.getCode()).thenReturn(CODE);
		when(order.getStore()).thenReturn(baseStore);
		when(baseStore.getSubmitOrderProcessCode()).thenReturn(PROCESS_CODE);
		when(baseStoreService.getCurrentBaseStore()).thenReturn(baseStore);
		when(order.getOrderProcess()).thenReturn(Lists.newArrayList(orderProcess1, orderProcess2));
	}

	@Test(expected = BusinessProcessException.class)
	public void shouldFailGetProcessCode_noProcesses()
	{
		when(order.getOrderProcess()).thenReturn(Collections.emptyList());

		service.getProcessCode(order);
	}

	@Test(expected = BusinessProcessException.class)
	public void shouldFailGetProcessCode_noOrderStoreAndNoCurrentStore()
	{
		when(order.getStore()).thenReturn(null);
		when(baseStoreService.getCurrentBaseStore()).thenReturn(null);

		service.getProcessCode(order);
	}

	@Test(expected = BusinessProcessException.class)
	public void shouldFailGetProcessCode_noFulfillmentProcessCode()
	{
		when(baseStore.getSubmitOrderProcessCode()).thenReturn(null);

		service.getProcessCode(order);
	}

	@Test(expected = BusinessProcessException.class)
	public void shouldFailGetProcessCode_noValidProcess()
	{
		when(orderProcess2.getCode()).thenReturn("MORE_GARBAGE");

		service.getProcessCode(order);
	}

	@Test(expected = BusinessProcessException.class)
	public void shouldFailGetProcessCode_TwoValidProcesses()
	{
		when(orderProcess1.getCode()).thenReturn(EXPECTED_PREFIX + "OTHER_SUFFIX");

		service.getProcessCode(order);
	}

	@Test
	public void shouldGetProcessCode()
	{
		final String code = service.getProcessCode(order);
		assertEquals(EXPECTED_PREFIX + SUFFIX, code);
	}
}
