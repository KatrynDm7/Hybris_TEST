package de.hybris.platform.yacceleratorordermanagement.actions.returns;

import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.warehousing.model.ReturnProcessModel;

import org.apache.log4j.Logger;


/**
 * Business logic to execute when tax was reversed successfully.
 */
public class SuccessTaxReverseAction extends AbstractProceduralAction<ReturnProcessModel> {
	private static final Logger LOG = Logger.getLogger(SuccessTaxReverseAction.class);

	@Override
	public void executeAction(final ReturnProcessModel process) throws RetryLaterException, Exception {
		LOG.debug("Process: " + process.getCode() + " in step " + getClass().getSimpleName());

		// TODO: implement the logic of success tax reverse
	}
}
