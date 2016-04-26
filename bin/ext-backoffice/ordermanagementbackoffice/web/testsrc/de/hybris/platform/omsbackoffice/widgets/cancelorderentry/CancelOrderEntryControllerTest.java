package de.hybris.platform.omsbackoffice.widgets.cancelorderentry;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.warehousing.cancellation.OrderCancellationService;
import de.hybris.platform.warehousing.process.WarehousingBusinessProcessService;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Textbox;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.engine.WidgetInstanceManager;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CancelOrderEntryControllerTest
{

	private CancelOrderEntryController controller;

	@Before
	public void setup()
	{
		controller = spy(new CancelOrderEntryController());
		final WidgetInstanceManager widgetInstanceManager = mock(WidgetInstanceManager.class);
		controller.setWidgetInstanceManager(widgetInstanceManager);
		controller.setOrderBusinessProcessService(mock(WarehousingBusinessProcessService.class));
		controller.setCockpitEventQueue(mock(CockpitEventQueue.class));
		controller.setModelService(mock(ModelService.class));
		when(controller.getModelService().get(Mockito.any())).thenReturn(new OrderEntryModel());

		final EnumerationService enumerationService = mock(EnumerationService.class);
		controller.setEnumerationService(enumerationService);
		when(enumerationService.getEnumerationValues(CancelReason.class)).thenReturn(
				Lists.newArrayList(CancelReason.LATEDELIVERY, CancelReason.CUSTOMERREQUEST));
	}

	@Test(expected = WrongValueException.class)
	public void shouldExpectWrongValueException_InputEntries()
	{
		controller.validateInpuEntries();
	}

	@Test
	public void shouldExecuteCancellationProcess()
	{
		final Combobox reason = new Combobox(CancelReason.LATEDELIVERY.getCode());
		final Textbox orderNumber = new Textbox("0000001");
		final Textbox customerName = new Textbox("Bruce Lee");
		final Textbox date = new Textbox(LocalDate.now().toString());
		final Textbox comment = new Textbox("Cockpit NG is awesome!!");

		final OrderModel order = mock(OrderModel.class);
		final OrderEntryModel entry = mock(OrderEntryModel.class);
		entry.setOrder(order);

		final OrderCancellationService cancellationService = mock(OrderCancellationService.class);

		controller.setCustomerName(customerName);
		controller.setCancelOrderEntryReason(reason);
		controller.setDate(date);
		controller.setOrderNumber(orderNumber);
		controller.setOrderEntry(entry);
		controller.setCancelOrderEntryComment(comment);
		controller.setOrderCancellationService(cancellationService);
		controller.setQtyCancelled(new Longbox(10L));
		controller.processCancellation();

		verify(cancellationService).cancelOrder(Mockito.any(), Mockito.any());
	}

	@Test
	public void shouldValidateInputEntries()
	{
		try
		{
			final Combobox reason = new Combobox(CancelReason.LATEDELIVERY.getCode());
			controller.setCancelOrderEntryReason(reason);
			controller.setQtyCancelled(new Longbox(10L));
			final OrderEntryModel entry = Mockito.mock(OrderEntryModel.class);

			controller.setOrderEntry(entry);
			when(controller.getOrderEntry().getQuantityPending()).thenReturn(new Long(20));

			controller.validateInpuEntries();

		}
		catch (final WrongValueException e)
		{
			Assert.fail("It should pass because the inputs are correct");
		}
	}

}
