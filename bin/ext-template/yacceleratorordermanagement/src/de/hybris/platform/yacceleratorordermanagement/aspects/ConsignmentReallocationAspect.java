package de.hybris.platform.yacceleratorordermanagement.aspects;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.warehousing.process.BusinessProcessException;
import de.hybris.platform.warehousing.process.WarehousingBusinessProcessService;
import de.hybris.platform.yacceleratorordermanagement.constants.YAcceleratorOrderManagementConstants;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Aspect that create a consignment process when a new consignment model is created during reallocation.
 */
public class ConsignmentReallocationAspect
{
	private static Logger LOGGER = LoggerFactory.getLogger(ConsignmentReallocationAspect.class);

	private static final String EXCEPTION_MESSAGE = "Could not process consignment.";

	private WarehousingBusinessProcessService<OrderModel> orderBusinessProcessService;
	private ModelService modelService;

	/**
	 * Perform after-returning advice.
	 *
	 * @param joinPoint
	 *           - the join point
	 * @param result
	 *           - the new consignment created during reallocation
	 */
	public void advise(final JoinPoint joinPoint, final Object result)
	{
		try
		{
			final ConsignmentModel consignment = (ConsignmentModel) result;
			final OrderModel order = (OrderModel) consignment.getOrder();

			final String orderProcessCode = getOrderBusinessProcessService().getProcessCode(order);
			final OrderProcessModel orderProcess = getOrderBusinessProcessService().getProcess(
					orderProcessCode);

			LOGGER.debug("Running consignment reallocation action aspect for consignment with code: " + consignment.getCode());

			final ConsignmentProcessModel subProcess = getOrderBusinessProcessService().<ConsignmentProcessModel> createProcess(
					consignment.getCode() + "_ordermanagement", YAcceleratorOrderManagementConstants.CONSIGNMENT_SUBPROCESS_NAME);
			subProcess.setParentProcess(orderProcess);
			subProcess.setConsignment(consignment);
			getModelService().save(subProcess);
			LOGGER.info("Start Consignment sub-process: '" + subProcess.getCode() + "'");
			getOrderBusinessProcessService().startProcess(subProcess);
		}
		catch (final BusinessProcessException e)
		{
			LOGGER.info(EXCEPTION_MESSAGE, e.getMessage());
		}
	}

	protected WarehousingBusinessProcessService<OrderModel> getOrderBusinessProcessService()
	{
		return orderBusinessProcessService;
	}

	@Required
	public void setOrderBusinessProcessService(final WarehousingBusinessProcessService<OrderModel> orderBusinessProcessService)
	{
		this.orderBusinessProcessService = orderBusinessProcessService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

}
