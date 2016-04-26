package de.hybris.platform.warehousingbackoffice.actions.printshippinglabel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.model.PickUpDeliveryModeModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.warehousingbackoffice.services.printshippinglabel.PrintShippingLabelService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.testing.AbstractActionUnitTest;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PrintShippingLabelActionTest extends AbstractActionUnitTest<PrintShippingLabelAction>
{
	@Spy
	private final PrintShippingLabelAction printShippingLabelAction = new PrintShippingLabelAction();

	@Mock
	private ActionContext actionContext;

	@Mock
	private PrintShippingLabelService printShippingLabelService;

	@Mock
	private ConsignmentModel consignmentModel;

	@Override
	public PrintShippingLabelAction getActionInstance()
	{
		return printShippingLabelAction;
	}

	@Before
	public void setUp()
	{
		doReturn(printShippingLabelService).when(printShippingLabelAction).getPrintShippingLabelService();
		when(actionContext.getData()).thenReturn(consignmentModel);
		doNothing().when(printShippingLabelAction).evaluateScript("TEST");
	}

	@Test
	public void shouldNotRequireConfirmation()
	{
		assertFalse(printShippingLabelAction.needsConfirmation(actionContext));
	}

	@Test
	public void shouldReturnNullConfirmationMessage()
	{
		assertEquals(null, printShippingLabelAction.getConfirmationMessage(actionContext));
	}

	@Test
	public void cannotPerform_nullData()
	{
		when(actionContext.getData()).thenReturn(null);
		assertFalse(printShippingLabelAction.canPerform(actionContext));
	}

	@Test
	public void cannotPerform_notInstanceOfConsignmentModel()
	{
		when(actionContext.getData()).thenReturn("test");
		assertFalse(printShippingLabelAction.canPerform(actionContext));
	}

	@Test
	public void cannotPerform_pickupOrder()
	{
		when(consignmentModel.getDeliveryMode()).thenReturn(new PickUpDeliveryModeModel());
		assertFalse(printShippingLabelAction.canPerform(actionContext));
	}

	@Test
	public void canPerform()
	{
		assertTrue(printShippingLabelAction.canPerform(actionContext));
	}

	@Test
	public void shouldPerformPrintAction()
	{
		when(printShippingLabelService.printShippingLabel(any(ConsignmentModel.class), any(ActionContext.class)))
				.thenReturn("TEST");

		final ActionResult<ConsignmentModel> result = printShippingLabelAction.perform(actionContext);

		assertNotNull(result);
		assertEquals(ActionResult.SUCCESS, result.getResultCode());
		verify(printShippingLabelService).printShippingLabel(any(ConsignmentModel.class), any(ActionContext.class));
	}

}
