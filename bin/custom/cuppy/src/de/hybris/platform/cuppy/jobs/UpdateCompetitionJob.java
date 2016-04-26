/**
 * 
 */
package de.hybris.platform.cuppy.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cuppy.model.UpdateCompetitionCronJobModel;
import de.hybris.platform.cuppy.services.UpdateCompetitionService;
import de.hybris.platform.servicelayer.cronjob.JobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * 
 */
public class UpdateCompetitionJob implements JobPerformable<UpdateCompetitionCronJobModel>
{
	private static final Logger LOG = Logger.getLogger(UpdateCompetitionJob.class);

	private UpdateCompetitionService updateService;

	@Override
	public PerformResult perform(final UpdateCompetitionCronJobModel cronJob)
	{
		final UpdateCompetitionCronJobModel updateCronJob = cronJob;
		try
		{
			if (updateCronJob.getLastStartTime() == null || updateCronJob.getTriggers().isEmpty()
					|| !CronJobResult.SUCCESS.equals(updateCronJob.getResult())
					|| updateService.isUpToDate(updateCronJob.getCompetition(), updateCronJob.getLastStartTime()))
			{
				updateService.update(updateCronJob.getCompetition());
			}
		}
		catch (final Exception e)
		{
			LOG.error("Error while updating competition", e);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.FINISHED);
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	@Override
	public boolean isAbortable()
	{
		return false;
	}

	@Override
	public boolean isPerformable()
	{
		return true;
	}

	@Required
	public void setUpdateService(final UpdateCompetitionService updateService)
	{
		this.updateService = updateService;
	}
}
