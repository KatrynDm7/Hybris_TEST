package de.hybris.platform.yacceleratorordermanagement.actions.consignment;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class UpdateConsignmentActionTest
{

	ConsignmentEntryModel consignmentEntryModel;
	ConsignmentProcessModel consignmentProcessModel;
	ConsignmentModel consignmentModel;

	@InjectMocks
	UpdateConsignmentAction action = new UpdateConsignmentAction();

	@Mock
	private ModelService modelService;

	@Before
	public void setup()
	{
		consignmentEntryModel = spy(new ConsignmentEntryModel());

		final Set<ConsignmentEntryModel> consignmentEntriesModel = new HashSet<>();
		consignmentEntriesModel.add(consignmentEntryModel);

		consignmentModel = new ConsignmentModel();
		consignmentModel.setConsignmentEntries(consignmentEntriesModel);

		consignmentProcessModel = new ConsignmentProcessModel();
		consignmentProcessModel.setConsignment(consignmentModel);
	}

	@Test
	public void shouldUpdateTheStatusToReadyForPickupWhenExecuted() throws Exception
	{
		action.setStatus(ConsignmentStatus.READY_FOR_PICKUP);
		action.executeAction(consignmentProcessModel);

		assertEquals(consignmentModel.getStatus().toString(), ConsignmentStatus.READY_FOR_PICKUP.toString());
	}

	@Test
	public void shouldUpdateTheStatusToReadyForShipWhenExecuted() throws Exception
	{
		action.setStatus(ConsignmentStatus.READY_FOR_SHIPPING);
		action.executeAction(consignmentProcessModel);

		assertEquals(consignmentModel.getStatus().toString(), ConsignmentStatus.READY_FOR_SHIPPING.toString());
	}
}
