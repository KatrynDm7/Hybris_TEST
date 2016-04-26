package de.hybris.platform.warehousingbackoffice.actions.printpacklabel;

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
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.warehousing.process.WarehousingBusinessProcessService;
import de.hybris.platform.warehousingbackoffice.services.printpacklabel.PrintPackLabelService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.testing.AbstractActionUnitTest;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PrintPackLabelActionTest extends AbstractActionUnitTest<PrintPackLabelAction>
{
	@Spy
	@InjectMocks
	private final PrintPackLabelAction printPackLabelAction = new PrintPackLabelAction();

	@Mock
	private ActionContext actionContext;

	@Mock
	private PrintPackLabelService printPackLabelService;

	@Mock
	private ConsignmentModel consignmentModel;

	@Mock
	private ModelService modelService;

	@Mock
	private WarehousingBusinessProcessService<ConsignmentModel> consignmentBusinessProcessService;

	@Override
	public PrintPackLabelAction getActionInstance()
	{
		return printPackLabelAction;
	}

	@Before
	public void setUp()
	{
		doReturn(printPackLabelService).when(printPackLabelAction).getPrintPackLabelService();
		when(actionContext.getData()).thenReturn(consignmentModel);
		doNothing().when(printPackLabelAction).evaluateScript("TEST");
	}

	@Test
	public void shouldNotRequireConfirmation()
	{
		assertFalse(printPackLabelAction.needsConfirmation(actionContext));
	}

	@Test
	public void shouldReturnNullConfirmationMessage()
	{
		assertEquals(null, printPackLabelAction.getConfirmationMessage(actionContext));
	}

	@Test
	public void cannotPerform_nullData()
	{
		when(actionContext.getData()).thenReturn(null);
		assertFalse(printPackLabelAction.canPerform(actionContext));
	}

	@Test
	public void cannotPerform_notInstanceOfConsignmentModel()
	{
		when(actionContext.getData()).thenReturn("test");
		assertFalse(printPackLabelAction.canPerform(actionContext));
	}

	@Test
	public void canPerform()
	{
		assertTrue(printPackLabelAction.canPerform(actionContext));
	}

	@Test
	public void shouldPerformPrintAction()
	{
		when(printPackLabelService.printPackLabel(any(ConsignmentModel.class), any(ActionContext.class))).thenReturn("TEST");
		doNothing().when(consignmentBusinessProcessService).triggerChoiceEvent(consignmentModel,
				PrintPackLabelAction.CONSIGNMENT_ACTION_EVENT_NAME, PrintPackLabelAction.PACK_CONSIGNMENT_CHOICE);

		final ActionResult<ConsignmentModel> result = printPackLabelAction.perform(actionContext);

		assertNotNull(result);
		assertEquals(ActionResult.SUCCESS, result.getResultCode());
		verify(printPackLabelService).printPackLabel(any(ConsignmentModel.class), any(ActionContext.class));
	}

}
