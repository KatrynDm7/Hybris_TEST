package de.hybris.platform.yacceleratorordermanagement.aspects;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.warehousing.data.cancellation.CancellationEntry;
import de.hybris.platform.warehousing.process.BusinessProcessException;
import de.hybris.platform.warehousing.process.WarehousingBusinessProcessService;
import de.hybris.platform.yacceleratorordermanagement.constants.YAcceleratorOrderManagementConstants;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Aspect that will attempt to place a consignment into an "awaiting completion" state, then perform a given action,
 * then place the consignment into a verification state.
 */
public class ConsignmentActionAspect
{
	private static Logger LOGGER = LoggerFactory.getLogger(ConsignmentActionAspect.class);

	private static final String EXCEPTION_MESSAGE = "Could not process consignment with code [%s]";

	private WarehousingBusinessProcessService<ConsignmentModel> consignmentBusinessProcessService;
	private String choice;
	private Set<Class<?>> exceptionsToRethrow;

	/**
	 * Perform around advice.
	 *
	 * @param joinPoint
	 *           - the join point
	 * @return the remaining cancellation entries to be cancelled; never <tt>null</tt>
	 * @throws Throwable
	 *            when an exception occurs that has been cleared to be rethrown.
	 */
	public Collection<CancellationEntry> advise(final ProceedingJoinPoint joinPoint) throws Throwable
	{
		Collection<CancellationEntry> remainingEntries = Collections.emptyList();
		final ConsignmentModel consignment = getConsignment(joinPoint);
		LOGGER.debug("Running consignment action aspect for consignment with code: " + consignment.getCode());
		try
		{
			getConsignmentBusinessProcessService().triggerChoiceEvent(consignment,
					YAcceleratorOrderManagementConstants.CONSIGNMENT_ACTION_EVENT_NAME, getChoice());
			remainingEntries = (Collection<CancellationEntry>) joinPoint.proceed();
			triggerAfter(consignment);
		}
		catch (final BusinessProcessException e)
		{
			throw e;
		}
		catch (final Throwable e)
		{
			triggerAfter(consignment);
			if (getExceptionsToRethrow().stream().anyMatch(clazz -> (e.getClass().equals(clazz))))
			{
				throw e;
			}
			LOGGER.info(String.format(EXCEPTION_MESSAGE, consignment.getCode()));
		}
		return remainingEntries;
	}

	/**
	 * Get the consignment model from the join point.
	 *
	 * @param joinPoint
	 *           - the join point object
	 * @return the consignment model
	 */
	protected ConsignmentModel getConsignment(final ProceedingJoinPoint joinPoint)
	{
		return (ConsignmentModel) joinPoint.getArgs()[0];
	}

	protected void triggerAfter(final ConsignmentModel consignment)
	{
		getConsignmentBusinessProcessService().triggerSimpleEvent(consignment,
				YAcceleratorOrderManagementConstants.ACTION_COMPLETION_EVENT_NAME);
	}

	protected WarehousingBusinessProcessService<ConsignmentModel> getConsignmentBusinessProcessService()
	{
		return consignmentBusinessProcessService;
	}

	@Required
	public void setConsignmentBusinessProcessService(
			final WarehousingBusinessProcessService<ConsignmentModel> consignmentBusinessProcessService)
	{
		this.consignmentBusinessProcessService = consignmentBusinessProcessService;
	}

	protected String getChoice()
	{
		return choice;
	}

	@Required
	public void setChoice(final String choice)
	{
		this.choice = choice;
	}

	protected Set<Class<?>> getExceptionsToRethrow()
	{
		return exceptionsToRethrow;
	}

	@Required
	public void setExceptionsToRethrow(final Set<Class<?>> exceptionsToRethrow)
	{
		this.exceptionsToRethrow = exceptionsToRethrow;
	}

}
