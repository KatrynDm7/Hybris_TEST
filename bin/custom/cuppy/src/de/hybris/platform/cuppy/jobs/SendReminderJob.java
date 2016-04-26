/**
 * 
 */
package de.hybris.platform.cuppy.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.services.MailService;
import de.hybris.platform.cuppy.services.MatchService;
import de.hybris.platform.cuppy.services.PlayerService;
import de.hybris.platform.cuppy.services.SingletonScopedComponent;
import de.hybris.platform.servicelayer.cronjob.JobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 
 */
@SingletonScopedComponent(value = "sendReminderJob")
public class SendReminderJob implements JobPerformable<CronJobModel>
{
	private final static Logger LOG = Logger.getLogger(SendReminderJob.class);
	@Autowired
	private MailService mailService;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private MatchService matchService;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		LOG.info("Sending reminder mails ..");
		for (final PlayerModel player : playerService.getAllPlayers())
		{
			final List<MatchModel> matches = matchService.getTodayMatches(player);
			final List<MatchModel> matchesToSend = new ArrayList<MatchModel>();
			for (final MatchModel match : matches)
			{
				if (player.getCompetitions().contains(match.getGroup().getCompetition())
						&& matchService.getBet(match, player) == null)
				{
					matchesToSend.add(match);
				}
			}
			if (!matchesToSend.isEmpty())
			{
				LOG.info(".. to " + player.getUid() + " (" + player.getEMail() + ")");
				mailService.sendReminder(matchesToSend, player);
			}
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
}
