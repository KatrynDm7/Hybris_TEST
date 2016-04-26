/**
 * 
 */
package de.hybris.platform.cuppy.web.components;

import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cuppy.services.impl.NoCompetitionAvailableException;
import de.hybris.platform.cuppy.web.data.MatchStatisticData;
import de.hybris.platform.cuppy.web.data.OverallStatisticData;
import de.hybris.platform.cuppy.web.facades.StatisticsFacade;
import de.hybris.platform.servicelayer.i18n.FormatFactory;
import de.hybris.platform.util.Config;

import java.text.DateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
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
public class StatusBarComponent extends Div
{
	private static final Logger LOG = Logger.getLogger(StatusBarComponent.class);

	public StatusBarComponent()
	{
		super();

		final Label statusLabel = new Label();
		statusLabel.setSclass("statusbar");
		statusLabel.setValue(Labels.getLabel("status.welcome"));
		this.appendChild(statusLabel);

		final Div chatButton = new Div();
		chatButton.setSclass("chatButton");
		chatButton.setTooltiptext("Chat");
		chatButton.addEventListener(Events.ON_CLICK, new EventListener()
		{
			@Override
			public void onEvent(final Event event)
			{
				openChatWindow(chatButton);
			}
		});
		this.appendChild(chatButton);

		final Timer timer = new Timer();
		timer.setRepeats(true);
		timer.setRunning(true);
		timer.setDelay(4000);
		timer.addEventListener(Events.ON_TIMER, new EventListener()
		{

			@Override
			public void onEvent(final Event event)
			{
				refreshStatusLabel(statusLabel);
				refreshChatStatus(chatButton);
			}
		});
		this.appendChild(timer);
	}

	private void refreshStatusLabel(final Label label)
	{
		if (isRedeployNear())
		{
			UITools.modifySClass(label, "statusbar-warn", true);
			label.setValue(Labels.getLabel("redeploy.warn"));
			return;
		}
		UITools.modifySClass(label, "statusbar-warn", false);

		String ret = "";
		Object attribute = label.getAttribute("index");
		if (attribute == null)
		{
			attribute = Integer.valueOf(0);
		}
		if (attribute instanceof Integer)
		{
			final DateFormat dateFormat = getFormatFactory().createDateTimeFormat(DateFormat.LONG, DateFormat.LONG);
			try
			{
				final OverallStatisticData data = getStatisticsFacade().getOverallStatistic();

				int index = ((Integer) attribute).intValue();
				switch (index)
				{
					case 1:
						ret = Labels.getLabel("frontend.currentPlayers", new String[]
						{ String.valueOf(data.getPlayersOnlineCount()), String.valueOf(data.getPlayersOnlineMaxCount()) });
						break;
					case 2:
						ret = Labels.getLabel("frontend.registeredPlayers", new String[]
						{ String.valueOf(data.getPlayersCountOverall()), String.valueOf(data.getPlayersCount()) });
						break;
					case 3:
						ret = Labels.getLabel("frontend.systemtime", new String[]
						{ dateFormat.format(new Date()) });
						break;
					//				case 3:
					//					ret = Labels.getLabel("frontend.score", new String[]
					//					{ nbf.format(data.getAverageScore()) });
					//					break;
					default:
						ret = getNextMatchLabel();
						break;
				}

				index = index < 3 ? index + 1 : 0;
				label.setAttribute("index", Integer.valueOf(index));
			}
			catch (final NoCompetitionAvailableException e)
			{
				ret = Labels.getLabel("frontend.systemtime", new String[]
				{ dateFormat.format(new Date()) });
			}
		}

		label.setValue(ret);
	}

	public void refreshChatStatus(final Div chatBtn)
	{
		final int newMessageCount = getChatClientController().getNewMessageCount();
		chatBtn.getChildren().clear();
		if (newMessageCount > 0)
		{
			chatBtn.appendChild(new Label(String.valueOf(newMessageCount)));
		}
	}

	private void openChatWindow(final Div chatBtn)
	{
		try
		{
			CuppyChatWindow chatWindow = null;
			for (final Object cmp : this.getParent().getChildren())
			{
				if (cmp instanceof CuppyChatWindow)
				{
					chatWindow = (CuppyChatWindow) cmp;
					break;
				}
			}

			if (chatWindow == null)
			{
				chatWindow = new CuppyChatWindow(1000, this, chatBtn);
				chatWindow.setTitle("Cuppy Chat");
				chatWindow.setTop("20px");
				chatWindow.setWidth("300px");
				chatWindow.setHeight("300px");
				this.getParent().appendChild(chatWindow);
			}

			chatWindow.doOverlapped();
		}
		catch (final Exception e)
		{
			LOG.error("Could not open chat window, reason: ", e);
		}
	}

	private String getNextMatchLabel()
	{
		final MatchStatisticData match = getStatisticsFacade().getNextBetableMatchStatistic();
		if (match == null)
		{
			return null;
		}
		final int playersLeft = match.getPlayersNotPlacedBetsCount();
		final int playersLeftPerc = match.getPlayersNotPlacedBetsPerc();

		return Labels.getLabel("info.match", new Object[]
		{ match.getMatch().getHomeTeam() + "-" + match.getMatch().getGuestTeam(), String.valueOf(match.getTimeToBet() / 1000 / 60),
				Integer.toString(playersLeft), Integer.toString(playersLeftPerc) });
	}

	private boolean isRedeployNear()
	{
		return Config.getBoolean("redeploy.warn", false);
	}

	private StatisticsFacade getStatisticsFacade()
	{
		return (StatisticsFacade) SpringUtil.getBean("statisticsFacade");
	}

	private FormatFactory getFormatFactory()
	{
		return (FormatFactory) SpringUtil.getBean("formatFactory");
	}

	private CuppyChatClientController getChatClientController()
	{
		return (CuppyChatClientController) SpringUtil.getBean("chatClientController");
	}
}
