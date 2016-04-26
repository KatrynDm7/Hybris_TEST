/**
 * 
 */
package de.hybris.platform.cuppy.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cuppy.services.SingletonScopedComponent;
import de.hybris.platform.cuppy.services.StatisticsService;
import de.hybris.platform.servicelayer.cronjob.JobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * 
 */
@SingletonScopedComponent(value = "collectStatisticsJob")
public class CollectStatisticsJob implements JobPerformable<CronJobModel>
{
	@Autowired
	private StatisticsService statisticsService;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		statisticsService.updateTimpointStatistic();
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
}
