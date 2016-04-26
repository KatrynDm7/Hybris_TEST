package de.hybris.platform.warehousingbackoffice.actions.printpicklist;

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
import de.hybris.platform.warehousingbackoffice.actions.printpicklist.PrintPickListAction;
import de.hybris.platform.warehousingbackoffice.services.printpicklist.PrintPickListService;

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
public class PrintPickListActionTest extends AbstractActionUnitTest<PrintPickListAction>
{
	@Spy
	private final PrintPickListAction printPickListAction = new PrintPickListAction();

	@Mock
	private ActionContext actionContext;

	@Mock
	private PrintPickListService printPickListService;

	@Mock
	private ConsignmentModel consignmentModel;

	@Override
	public PrintPickListAction getActionInstance()
	{
		return printPickListAction;
	}

	@Before
	public void setUp()
	{
		doReturn(printPickListService).when(printPickListAction).getPrintPickListService();
		when(actionContext.getData()).thenReturn(consignmentModel);
		doNothing().when(printPickListAction).evaluateScript("TEST");
	}

	@Test
	public void shouldNotRequireConfirmation()
	{
		assertFalse(printPickListAction.needsConfirmation(actionContext));
	}

	@Test
	public void shouldReturnNullConfirmationMessage()
	{
		assertEquals(null, printPickListAction.getConfirmationMessage(actionContext));
	}

	@Test
	public void cannotPerform_nullData()
	{
		when(actionContext.getData()).thenReturn(null);
		assertFalse(printPickListAction.canPerform(actionContext));
	}

	@Test
	public void cannotPerform_notInstanceOfConsignmentModel()
	{
		when(actionContext.getData()).thenReturn("test");
		assertFalse(printPickListAction.canPerform(actionContext));
	}

	@Test
	public void canPerform()
	{
		assertTrue(printPickListAction.canPerform(actionContext));
	}

	@Test
	public void shouldPerformPrintAction()
	{
		when(printPickListService.printPickList(any(ConsignmentModel.class), any(ActionContext.class))).thenReturn("TEST");

		final ActionResult<ConsignmentModel> result = printPickListAction.perform(actionContext);

		assertNotNull(result);
		assertEquals(ActionResult.SUCCESS, result.getResultCode());
		verify(printPickListService).printPickList(any(ConsignmentModel.class), any(ActionContext.class));
	}

}
