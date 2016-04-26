package de.hybris.platform.omsbackoffice.widgets.cancelorder;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.warehousing.cancellation.OrderCancellationService;
import de.hybris.platform.warehousing.process.WarehousingBusinessProcessService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.engine.WidgetInstanceManager;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CancelOrderControllerTest
{
	private CancelOrderController controller;

	@Before
	public void setup()
	{
		controller = spy(new CancelOrderController());
		final WidgetInstanceManager widgetInstanceManager = mock(WidgetInstanceManager.class);
		controller.setWidgetInstanceManager(widgetInstanceManager);
		controller.setOrderBusinessProcessService(mock(WarehousingBusinessProcessService.class));

		final EnumerationService enumerationService = mock(EnumerationService.class);
		controller.setEnumerationService(enumerationService);
		when(enumerationService.getEnumerationValues(CancelReason.class)).thenReturn(
				Lists.newArrayList(CancelReason.LATEDELIVERY, CancelReason.CUSTOMERREQUEST));
	}

	@Test
	public void shouldValidateProcessCancellation()
	{

		doNothing().when(controller).showMessageBox();
		doNothing().when(controller).executeCancellationProcess();

		final Event obj = new Event(Messagebox.Button.YES.event);
		controller.processCancellation(obj);
		verify(controller).executeCancellationProcess();
	}

	@Test(expected = WrongValueException.class)
	public void shouldExpectWrongValueException_InputEntries()
	{
		controller.validateInputEntries();
	}

	@Test
	public void shouldExecuteCancellationProcess()
	{
		doNothing().when(controller).showMessageBox();
		final Combobox reason = new Combobox(controller.getEnumerationService().getEnumerationName(CancelReason.LATEDELIVERY,
				new Locale("en")));
		final Textbox orderNumber = new Textbox("0000001");
		final Textbox customerName = new Textbox("Bruce Lee");
		final Textbox date = new Textbox(LocalDate.now().toString());
		final Textbox comment = new Textbox("Cockpit NG is awesome!!");
		final OrderModel orderModel = mock(OrderModel.class);
		final OrderCancellationService cancellationService = mock(OrderCancellationService.class);

		controller.setCustomerName(customerName);
		controller.setCancelOrderReasons(reason);
		controller.setDate(date);
		controller.setOrderNumber(orderNumber);
		controller.setOrderModel(orderModel);
		controller.setCancelOrderComment(comment);
		controller.setOrderCancellationService(cancellationService);

		controller.setModelService(Mockito.mock(ModelService.class));

		when(orderModel.getEntries()).thenReturn(new ArrayList<AbstractOrderEntryModel>());

		when(controller.getModelService().get(orderModel.getPk())).thenReturn(orderModel);


		controller.executeCancellationProcess();


		verify(cancellationService).cancelOrder(orderModel, controller.createCancellationEntries());
	}

	@Test
	public void shouldValidateInputEntries()
	{
		doNothing().when(controller).showMessageBox();
		try
		{
			final Combobox reason = new Combobox(controller.getEnumerationService().getEnumerationName(CancelReason.LATEDELIVERY,
					new Locale("en")));
			controller.setCancelOrderReasons(reason);
			controller.validateInputEntries();

		}
		catch (final Exception e)
		{
			Assert.fail("It should pass because the inputs are correct");
		}
	}
}
