package de.hybris.platform.warehousing.process.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
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
public class DefaultConsignmentProcessServiceTest
{
	private static final String SUFFIX = "_ordermanagement";
	private static final String CODE = "CODE";
	private static final String EXPECTED_CODE = CODE + SUFFIX;

	@InjectMocks
	private final DefaultConsignmentProcessService service = new DefaultConsignmentProcessService();


	@Mock
	private ConsignmentModel consignment;
	@Mock
	private ConsignmentProcessModel consignmentProcess1;
	@Mock
	private ConsignmentProcessModel consignmentProcess2;

	@Before
	public void setUp()
	{
		service.setConsignmentProcessSuffix(SUFFIX);

		when(consignmentProcess1.getCode()).thenReturn("GARBAGE");
		when(consignmentProcess2.getCode()).thenReturn(EXPECTED_CODE);

		when(consignment.getCode()).thenReturn(CODE);
		when(consignment.getConsignmentProcesses()).thenReturn(Lists.newArrayList(consignmentProcess1, consignmentProcess2));
	}

	@Test(expected = BusinessProcessException.class)
	public void shouldFailGetProcessCode_noProcesses()
	{
		when(consignment.getConsignmentProcesses()).thenReturn(Collections.emptyList());

		service.getProcessCode(consignment);
	}

	@Test(expected = BusinessProcessException.class)
	public void shouldFailGetProcessCode_noMatch()
	{
		when(consignmentProcess2.getCode()).thenReturn("MORE_GARBAGE");

		service.getProcessCode(consignment);
	}

	@Test
	public void shouldGetProcessCode()
	{
		final String code = service.getProcessCode(consignment);
		assertEquals(EXPECTED_CODE, code);
	}
}
