package de.hybris.platform.yacceleratorordermanagement.actions.consignment;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
public class VerifyConsignmentCompletionActionTest
{

	private ConsignmentEntryModel consignmentEntryModel;
	private ConsignmentProcessModel consignmentProcessModel;
	private ConsignmentModel consignmentModel;

	@InjectMocks
	private final VerifyConsignmentCompletionAction action = new VerifyConsignmentCompletionAction();

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
	public void shouldWaitWhenQuantityPendingIsMoreThanZero() throws Exception
	{
		when(consignmentEntryModel.getQuantityPending()).thenReturn(1L);

		final String transition = action.execute(consignmentProcessModel);
		assertTrue(VerifyConsignmentCompletionAction.Transition.WAIT.toString().equals(transition));
	}

	@Test
	public void shouldNotWaitWhenQuantityPendingEqualsZero() throws Exception
	{
		when(consignmentEntryModel.getQuantityPending()).thenReturn(0L);

		final String transition = action.execute(consignmentProcessModel);
		assertTrue(VerifyConsignmentCompletionAction.Transition.OK.toString().equals(transition));
	}


	@Test
	public void shouldSetConsignmentStatusToCancelledWhenQuantityPendingEqualsZero() throws Exception
	{
		when(consignmentEntryModel.getQuantityPending()).thenReturn(0L);

		action.execute(consignmentProcessModel);

		verify(modelService).save(consignmentModel);
		assertTrue(consignmentModel.getStatus().toString().equals(ConsignmentStatus.CANCELLED.toString()));
	}
}
