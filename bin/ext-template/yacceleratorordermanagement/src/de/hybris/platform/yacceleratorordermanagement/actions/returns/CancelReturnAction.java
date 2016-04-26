package de.hybris.platform.yacceleratorordermanagement.actions.returns;


import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.warehousing.model.ReturnProcessModel;

import org.apache.log4j.Logger;


/**
 * Cancel a return by updating the {@link ReturnRequestModel} status to CANCELED.
 */
public class CancelReturnAction extends AbstractProceduralAction<ReturnProcessModel>
{
	private static final Logger LOG = Logger.getLogger(CancelReturnAction.class);

	@Override
	public void executeAction(final ReturnProcessModel process) throws RetryLaterException, Exception
	{
		LOG.debug("Process: " + process.getCode() + " in step " + getClass().getSimpleName());

		final ReturnRequestModel returnRequest = process.getReturnRequest();
		returnRequest.setStatus(ReturnStatus.CANCELED);
		returnRequest.getReturnEntries().stream().forEach(entry -> {
			entry.setStatus(ReturnStatus.CANCELED);
			getModelService().save(entry);
		});
		getModelService().save(returnRequest);
	}
}
