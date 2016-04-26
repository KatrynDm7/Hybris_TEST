package de.hybris.platform.warehousing.process.impl;

import com.google.common.collect.Lists;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.warehousing.process.BusinessProcessException;
import de.hybris.platform.warehousing.model.ReturnProcessModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultReturnProcessServiceTest
{
	private static final String SUFFIX = "SUFFIX";
	private static final String PROCESS_CODE = "PROCESS_CODE";
	private static final String CODE = "CODE";
	private static final String EXPECTED_PREFIX = PROCESS_CODE + "-" + CODE;

	@InjectMocks
	private final DefaultReturnProcessService service = new DefaultReturnProcessService();

	@Mock
	private BaseStoreService baseStoreService;

	@Mock
	private ReturnRequestModel returnRequest;
	@Mock
	private OrderModel order;
	@Mock
	private BaseStoreModel baseStore;
	@Mock
	private ReturnProcessModel returnProcess1;
	@Mock
	private ReturnProcessModel returnProcess2;

	@Before
	public void setUp()
	{
		when(returnProcess1.getCode()).thenReturn("GARBAGE");
		when(returnProcess2.getCode()).thenReturn(EXPECTED_PREFIX + SUFFIX);

		when(returnRequest.getOrder()).thenReturn(order);

		when(returnRequest.getCode()).thenReturn(CODE);
		when(returnRequest.getOrder().getStore()).thenReturn(baseStore);
		when(baseStore.getCreateReturnProcessCode()).thenReturn(PROCESS_CODE);
		when(baseStoreService.getCurrentBaseStore()).thenReturn(baseStore);
		when(returnRequest.getReturnProcess()).thenReturn(Lists.newArrayList(returnProcess1, returnProcess2));
	}

	@Test(expected = BusinessProcessException.class)
	public void shouldFailGetProcessCode_noProcesses()
	{
		when(returnRequest.getReturnProcess()).thenReturn(Collections.emptyList());

		service.getProcessCode(returnRequest);
	}

	@Test(expected = BusinessProcessException.class)
	public void shouldFailGetProcessCode_noOrderStoreAndNoCurrentStore()
	{
		when(returnRequest.getOrder().getStore()).thenReturn(null);
		when(baseStoreService.getCurrentBaseStore()).thenReturn(null);

		service.getProcessCode(returnRequest);
	}

	@Test(expected = BusinessProcessException.class)
	public void shouldFailGetProcessCode_noFulfillmentProcessCode()
	{
		when(baseStore.getCreateReturnProcessCode()).thenReturn(null);

		service.getProcessCode(returnRequest);
	}

	@Test(expected = BusinessProcessException.class)
	public void shouldFailGetProcessCode_noValidProcess()
	{
		when(returnProcess2.getCode()).thenReturn("MORE_GARBAGE");

		service.getProcessCode(returnRequest);
	}

	@Test(expected = BusinessProcessException.class)
	public void shouldFailGetProcessCode_TwoValidProcesses()
	{
		when(returnProcess1.getCode()).thenReturn(EXPECTED_PREFIX + "OTHER_SUFFIX");

		service.getProcessCode(returnRequest);
	}

	@Test
	public void shouldGetProcessCode()
	{
		final String code = service.getProcessCode(returnRequest);
		assertEquals(EXPECTED_PREFIX + SUFFIX, code);
	}
}
