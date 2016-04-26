package de.hybris.platform.yacceleratorordermanagement.actions.returns;

import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.warehousing.model.ReturnProcessModel;

import org.apache.log4j.Logger;


/**
 * Update inventory and set the {@link ReturnRequestModel} status to COMPLETED.<br/>
 * A custom update inventory behavior must be implemented. This determines the steps to be executed after a successful
 * return.
 */
public class InventoryUpdateAction extends AbstractProceduralAction<ReturnProcessModel>
{
	private static final Logger LOG = Logger.getLogger(InventoryUpdateAction.class);

	@Override
	public void executeAction(final ReturnProcessModel process) throws RetryLaterException, Exception
	{
		LOG.debug("Process: " + process.getCode() + " in step " + getClass().getSimpleName());

		final ReturnRequestModel returnRequest = process.getReturnRequest();

		// TODO: implement the update inventory behavior that you want to apply after a successful return

		returnRequest.setStatus(ReturnStatus.COMPLETED);
		returnRequest.getReturnEntries().stream().forEach(entry -> {
			entry.setStatus(ReturnStatus.COMPLETED);
			getModelService().save(entry);
		});
		getModelService().save(returnRequest);
	}
}
