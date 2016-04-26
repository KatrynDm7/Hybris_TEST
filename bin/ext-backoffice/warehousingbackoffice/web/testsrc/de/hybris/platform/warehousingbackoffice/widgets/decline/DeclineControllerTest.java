package de.hybris.platform.warehousingbackoffice.widgets.decline;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.warehousing.allocation.AllocationService;
import de.hybris.platform.warehousing.data.allocation.DeclineEntries;
import de.hybris.platform.warehousing.enums.DeclineReason;
import de.hybris.platform.warehousing.process.WarehousingBusinessProcessService;
import de.hybris.platform.warehousing.sourcing.filter.SourcingFilterProcessor;

import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Textbox;

import com.google.common.collect.Sets;
import com.hybris.backoffice.i18n.BackofficeLocaleService;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.engine.WidgetInstanceManager;


@UnitTest
public class DeclineControllerTest
{

	private DeclineController controller;

	@Before
	public void setup()
	{
		controller = spy(new DeclineController());
		final WidgetInstanceManager widgetInstanceManager = mock(WidgetInstanceManager.class);
		controller.setWidgetInstanceManager(widgetInstanceManager);
	}

	private void setControllerStates()
	{
		controller.setLocations(Sets.newHashSet("Recife"));
		controller.setDeclinedQuantity(new Longbox(5));
		controller.setDeclineReasons(new Combobox(controller.getEnumerationService().getEnumerationName(DeclineReason.OUTOFSTOCK,
				new Locale("en"))));
		controller.setDropDownLocations(new Combobox("Recife"));
		final ConsignmentEntryModel consignmentEntryModel = mock(ConsignmentEntryModel.class);
		when(consignmentEntryModel.getQuantityPending()).thenReturn(new Long(6));
		controller.setConsignmentEntryModel(consignmentEntryModel);
		controller.setDeclineComment(new Textbox("Cockpit NG is awesome"));

		final WarehouseService warehouseService = mock(WarehouseService.class);
		when(warehouseService.getWarehouseForCode(Mockito.anyString())).thenReturn(mock(WarehouseModel.class));
		controller.setWarehouseService(warehouseService);

		final AllocationService allocationService = mock(AllocationService.class);
		controller.setAllocationService(allocationService);


		final WarehousingBusinessProcessService<ConsignmentModel> consignmentBusinessProcessService = Mockito
				.mock(WarehousingBusinessProcessService.class);
		controller.setConsignmentBusinessProcessService(consignmentBusinessProcessService);

		controller.setCockpitEventQueue(mock(CockpitEventQueue.class));
		controller.setModelService(mock(ModelService.class));
		when(controller.getModelService().get(Mockito.any())).thenReturn(new ConsignmentEntryModel());

	}

	@Test
	public void shouldValidateInputEntriesForDecline()
	{
		try
		{
			setControllerStates();
			controller.validateInputEntries();
		}
		catch (final WrongValueException e)
		{
			Assert.fail("It should pass because the inputs are correct");
		}

	}

	@Test(expected = WrongValueException.class)
	public void shouldExpectWrongValueException_InputEntries()
	{
		controller.validateInputEntries();
	}

	@Test
	public void shouldDecline()
	{
		setControllerStates();
		controller.decline();
		Mockito.verify(controller.getAllocationService()).reallocate(Mockito.any(DeclineEntries.class));
	}

	@Test
	public void shouldInitializeFields()
	{
		final String shipmentNumber = "123456";
		final String customerName = "Jerome Depardieu";

		controller.setShipmentNumber(new Textbox());
		controller.setCustomerName(new Textbox());
		controller.setDate(new Textbox());
		controller.setProductCode(new Textbox());
		controller.setProductName(new Textbox());
		controller.setQuantityPending(new Textbox());
		controller.setDeclinedQuantity(new Longbox());

		final BackofficeLocaleService cockpitLocaleService = mock(BackofficeLocaleService.class);
		final Locale locale = new Locale("en");
		when(cockpitLocaleService.getCurrentLocale()).thenReturn(locale);
		controller.setCockpitLocaleService(cockpitLocaleService);
		when(controller.getLabel("warehousingbackoffice.decline.date.format")).thenReturn("MMM dd, yyyy");
		controller.setDropDownLocations(new Combobox());

		final ConsignmentModel consignmentModel = new ConsignmentModel();
		consignmentModel.setCode(shipmentNumber);

		final ConsignmentEntryModel consignmentEntryModel = spy(new ConsignmentEntryModel());
		consignmentEntryModel.setConsignment(consignmentModel);
		final Date date = new Date();

		consignmentEntryModel.setCreationtime(date);

		final AbstractOrderEntryModel orderEntry = new OrderEntryModel();
		final ProductModel product = new ProductModel();
		final String productCode = "000001";
		product.setCode(productCode);
		orderEntry.setProduct(product);
		final String productName = "No name";
		product.setName(productName, locale);
		final AbstractOrderModel order = new OrderModel();
		final UserModel user = new UserModel();

		user.setName(customerName);
		order.setUser(user);
		orderEntry.setOrder(order);
		consignmentEntryModel.setOrderEntry(orderEntry);
		final Long quantityPending = new Long(4);
		when(consignmentEntryModel.getQuantityPending()).thenReturn(quantityPending);

		final SourcingFilterProcessor reallocationFilterProcessor = Mockito.mock(SourcingFilterProcessor.class);
		controller.setReallocationFilterProcessor(reallocationFilterProcessor);

		final WarehouseModel warehouse = new WarehouseModel();
		warehouse.setCode("test warehouse");
		Mockito.doAnswer(invocation -> {
			@SuppressWarnings("unchecked")
			final Set<WarehouseModel> object = (HashSet<WarehouseModel>) invocation.getArguments()[1];
			object.add(warehouse);
			return null;
		}).when(reallocationFilterProcessor).filterLocations(order, Sets.newHashSet());

		controller.setConsignmentEntryModel(consignmentEntryModel);
		controller.initialize();

		Assert.assertEquals(shipmentNumber, controller.getShipmentNumber().getValue());
		Assert.assertEquals(customerName, controller.getCustomerName().getValue());
		Assert.assertEquals(productCode, controller.getProductCode().getValue());
		Assert.assertEquals(productName, controller.getProductName().getValue());
		Assert.assertEquals(quantityPending.longValue(), Long.valueOf(controller.getQuantityPending().getValue()).longValue());
		Assert.assertFalse(controller.getLocations().isEmpty());
	}
}
