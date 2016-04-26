package de.hybris.platform.yacceleratorordermanagement.actions.consignment;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;

import org.apache.log4j.Logger;


/**
 * Update the consignment status to shipped.
 */
public class ConfirmShipConsignmentAction extends AbstractProceduralAction<ConsignmentProcessModel>
{
	private static final Logger LOG = Logger.getLogger(ConfirmShipConsignmentAction.class);

	@Override
	public void executeAction(final ConsignmentProcessModel consignmentProcessModel)
	{
		LOG.info("Process: " + consignmentProcessModel.getCode() + " in step " + getClass().getSimpleName());
		final ConsignmentModel consignment = consignmentProcessModel.getConsignment();
		consignment.setStatus(ConsignmentStatus.SHIPPED);
		save(consignment);
	}

}
