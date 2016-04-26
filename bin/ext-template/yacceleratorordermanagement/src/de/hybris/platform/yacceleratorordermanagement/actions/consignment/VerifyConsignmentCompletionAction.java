package de.hybris.platform.yacceleratorordermanagement.actions.consignment;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractAction;


/**
 * Checks the state of the consignment to perform changes (e.g., setting the consignment status) according to the
 * consignment's state changes resulted from the actions performed on it.
 */
public class VerifyConsignmentCompletionAction extends AbstractAction<ConsignmentProcessModel>
{
	private static Logger LOGGER = LoggerFactory.getLogger(VerifyConsignmentCompletionAction.class);

	protected enum Transition
	{
		OK, WAIT;

		public static Set<String> getStringValues()
		{
			final Set<String> res = new HashSet<String>();

			for (final Transition transition : Transition.values())
			{
				res.add(transition.toString());
			}
			return res;
		}
	}

	@Override
	public String execute(final ConsignmentProcessModel consignmentProcessModel) throws Exception
	{
		LOGGER.info("Process: " + consignmentProcessModel.getCode() + " in step " + getClass().getSimpleName());
		return consignmentProcessModel.getConsignment().getConsignmentEntries().stream()
				.anyMatch(consignmentEntry -> consignmentEntry.getQuantityPending() > 0) ? waitTransition() : okTransition(consignmentProcessModel);
	}


	protected String waitTransition() 
	{
		return Transition.WAIT.toString();
	}


	protected String okTransition(final ConsignmentProcessModel consignmentProcessModel)
	{
		final ConsignmentStatus status= ConsignmentStatus.CANCELLED;
		final ConsignmentModel consignment = consignmentProcessModel.getConsignment();
		consignment.setStatus(status);
		getModelService().save(consignment);

		return Transition.OK.toString();
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}

}
