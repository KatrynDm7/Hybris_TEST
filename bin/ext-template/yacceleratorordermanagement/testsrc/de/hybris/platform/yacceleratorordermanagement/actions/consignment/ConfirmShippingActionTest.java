package de.hybris.platform.yacceleratorordermanagement.actions.consignment;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.yacceleratorordermanagement.actions.consignment.ConfirmShipConsignmentAction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ConfirmShippingActionTest
{

    ConsignmentEntryModel consignmentEntryModel;
    ConsignmentProcessModel consignmentProcessModel;
    ConsignmentModel consignmentModel;
    
    @InjectMocks
	ConfirmShipConsignmentAction action = new ConfirmShipConsignmentAction();
	
	@Mock
	private ModelService modelService;

    @Before
    public void setup()
    {
        consignmentEntryModel = spy(new ConsignmentEntryModel());

        Set<ConsignmentEntryModel> consignmentEntriesModel = new HashSet<>();
        consignmentEntriesModel.add(consignmentEntryModel);

        consignmentModel = new ConsignmentModel();
        consignmentModel.setConsignmentEntries(consignmentEntriesModel);

        consignmentProcessModel = new ConsignmentProcessModel();
        consignmentProcessModel.setConsignment(consignmentModel);
    }

    @Test
    public void shouldSetConsignmentStatusToShippedWhenExecuted() throws Exception
    {
             
        action.executeAction(consignmentProcessModel);
        
        assertTrue(consignmentModel.getStatus().toString().equals(ConsignmentStatus.SHIPPED.toString()));
    }
}
