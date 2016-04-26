/**
 * 
 */
package de.hybris.platform.cuppy.web.components;

import de.hybris.platform.cuppy.services.impl.NoCompetitionAvailableException;
import de.hybris.platform.cuppy.web.data.MatchStatisticData;
import de.hybris.platform.cuppy.web.facades.StatisticsFacade;
import de.hybris.platform.servicelayer.i18n.FormatFactory;

import java.text.DateFormat;
import java.text.NumberFormat;

import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Timer;


/**
 * @author andreas.thaler
 * 
 */
public class MatchStatisticsComponent extends Div
{
	public MatchStatisticsComponent()
	{
		super();

		final Div div = new Div();
		this.appendChild(div);

		updateLabel(div);

		final Timer timer = new Timer();
		timer.setRepeats(true);
		timer.setRunning(true);
		timer.setDelay(6000);
		timer.addEventListener(Events.ON_TIMER, new EventListener()
		{

			@Override
			public void onEvent(final Event event)
			{
				updateLabel(div);
			}
		});
		this.appendChild(timer);
	}

	private void updateLabel(final Div div)
	{
		div.getChildren().clear();

		try
		{
			final MatchStatisticData data = getStatisticsFacade().getRandomMatchStatistic();


			Label label = new Label("Match: " + data.getMatch().getHomeTeam() + "-" + data.getMatch().getGuestTeam());
			div.appendChild(label);
			label = new Label("Cat: " + data.getMatch().getGroup().getName() + "-"
					+ getFormatFactory().createDateTimeFormat(DateFormat.MEDIUM, DateFormat.SHORT).format(data.getMatch().getDate()));
			div.appendChild(label);

			label = new Label(
					"Status: "
							+ (data.getMatch().isMatchBetable() ? "Not Started" : (data.getMatch().isMatchFinished() ? "Finished"
									: "Running")));
			div.appendChild(label);

			label = new Label("Not Placed bets: " + data.getPlayersNotPlacedBetsCount() + " (" + data.getPlayersNotPlacedBetsPerc()
					+ "%)");
			div.appendChild(label);

			if (data.getMatch().isMatchFinished())
			{
				label = new Label("Result: " + data.getMatch().getHomeGoals() + ":" + data.getMatch().getGuestGoals());
				div.appendChild(label);

				final NumberFormat nbf = getFormatFactory().createNumberFormat();
				label = new Label("Avg. Score: " + nbf.format(data.getAverageScore()));
				div.appendChild(label);
			}
		}
		catch (final NoCompetitionAvailableException e)
		{
			this.appendChild(new Label(Labels.getLabel("error.nocompetition")));
		}

	}

	private StatisticsFacade getStatisticsFacade()
	{
		return (StatisticsFacade) SpringUtil.getBean("statisticsFacade");
	}

	private FormatFactory getFormatFactory()
	{
		return (FormatFactory) SpringUtil.getBean("formatFactory");
	}
}
