package de.hybris.platform.yacceleratorordermanagement.actions.returns;

import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.warehousing.returns.service.RefundAmountCalculationService;
import de.hybris.platform.warehousing.model.ReturnProcessModel;

import java.util.Set;

import org.apache.log4j.Logger;


/**
 * Perform some initial calculation to determine the amount for refund.
 */
public class InitialCalculateRefundAmountAction extends AbstractProceduralAction<ReturnProcessModel>
{
	private static final Logger LOG = Logger.getLogger(InitialCalculateRefundAmountAction.class);

	private RefundAmountCalculationService refundAmountCalculationService;
	
	@Override
	public void executeAction(final ReturnProcessModel process) throws RetryLaterException, Exception
	{
		LOG.debug("Process: " + process.getCode() + " in step " + getClass().getSimpleName());

		final ReturnRequestModel returnRequest = process.getReturnRequest();
		getRefundAmountCalculationService().calculateRefundAmount(returnRequest);
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}

	protected RefundAmountCalculationService getRefundAmountCalculationService()
	{
		return refundAmountCalculationService;
	}

	public void setRefundAmountCalculationService(final RefundAmountCalculationService refundAmountCalculationService)
	{
		this.refundAmountCalculationService = refundAmountCalculationService;
	}

}
