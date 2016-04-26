package de.hybris.platform.yacceleratorordermanagement.actions.returns;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;

import de.hybris.platform.returns.model.ReturnRequestModel;

import de.hybris.platform.warehousing.returns.service.impl.DefaultRefundAmountCalculationService;
import de.hybris.platform.warehousing.model.ReturnProcessModel;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class InitialCalculateRefundAmountActionTest
{
	@InjectMocks
	private InitialCalculateRefundAmountAction action;

	@Mock
	private DefaultRefundAmountCalculationService refundAmountCalculationService;

	@Mock
	private ReturnProcessModel returnProcessModel;

	@Mock
	private ReturnRequestModel returnRequest;

	@Before
	public void setup()
	{
		when(returnProcessModel.getReturnRequest()).thenReturn(returnRequest);
	}

	@Test
	public void shouldCalculateOriginalRefundAmount() throws Exception
	{
		action.executeAction(returnProcessModel);
		verify(refundAmountCalculationService).calculateRefundAmount(returnRequest);
	}
}
