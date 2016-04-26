package de.hybris.platform.yacceleratorordermanagement.actions.returns;

import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.PaymentService;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.warehousing.model.ReturnProcessModel;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Part of the refund process that returns the money to the customer.
 */
public class CaptureRefundAction extends AbstractSimpleDecisionAction<ReturnProcessModel>
{
	private static final Logger LOG = Logger.getLogger(CaptureRefundAction.class);

	private PaymentService paymentService;

	@Override
	public Transition executeAction(final ReturnProcessModel process) throws RetryLaterException, Exception
	{
		LOG.debug("Process: " + process.getCode() + " in step " + getClass().getSimpleName());

		final ReturnRequestModel returnRequest = process.getReturnRequest();
		final List<PaymentTransactionModel> transactions = returnRequest.getOrder().getPaymentTransactions();

		if (transactions.isEmpty())
		{
			LOG.info("Unable to refund for ReturnRequest " + returnRequest.getCode() + ", no PaymentTransactions found");
			setReturnRequestStatus(returnRequest, ReturnStatus.PAYMENT_FAILED);
			return Transition.NOK;
		}
		//This assumes that the Order only has one PaymentTransaction
		final PaymentTransactionModel transaction = transactions.get(0);

		final BigDecimal customRefundAmount = returnRequest.getCustomRefundAmount();
		BigDecimal amountToRefund = null;

		if (customRefundAmount != null && customRefundAmount.compareTo(new BigDecimal(0)) > 0)
		{
			amountToRefund = customRefundAmount;
		}
		else
		{
			amountToRefund = returnRequest.getOriginalRefundAmount();
		}

		Transition result;
		try
		{
			getPaymentService().refundFollowOn(transaction, amountToRefund);
			setReturnRequestStatus(returnRequest, ReturnStatus.PAYMENT_CAPTURED);
			result = Transition.OK;
		}
		catch (final AdapterException e)
		{
			LOG.info("Unable to refund for ReturnRequest " + returnRequest.getCode() + ", exception ocurred: " + e.getMessage());
			setReturnRequestStatus(returnRequest, ReturnStatus.PAYMENT_FAILED);
			result = Transition.NOK;
		}

		return result;
	}

	/**
	 * Update the return status for all return entries in {@link ReturnRequestModel}
	 *
	 * @param returnRequest
	 *           - the return request
	 * @param status
	 *           - the return status
	 */
	protected void setReturnRequestStatus(final ReturnRequestModel returnRequest, final ReturnStatus status)
	{
		returnRequest.setStatus(status);
		returnRequest.getReturnEntries().stream().forEach(entry -> {
			entry.setStatus(status);
			getModelService().save(entry);
		});
		getModelService().save(returnRequest);
	}

	protected PaymentService getPaymentService()
	{
		return paymentService;
	}

	@Required
	public void setPaymentService(final PaymentService paymentService)
	{
		this.paymentService = paymentService;
	}

}
