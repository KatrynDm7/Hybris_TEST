package de.hybris.platform.cuppy.web.components;

import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cuppy.services.impl.NoCompetitionAvailableException;
import de.hybris.platform.cuppy.web.data.BetData;
import de.hybris.platform.cuppy.web.data.GroupData;
import de.hybris.platform.cuppy.web.data.MatchData;
import de.hybris.platform.cuppy.web.facades.MatchFacade;
import de.hybris.platform.servicelayer.i18n.FormatFactory;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Img;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;


public class BetComponent extends Div implements BetPopupCallback
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BetComponent.class);

	protected final Map<String, HtmlBasedComponent> componentsMap = new HashMap<String, HtmlBasedComponent>();

	public void init()
	{
		try
		{
			createGroups(getMatchFacade().getGroups());
		}
		catch (final NoCompetitionAvailableException e)
		{
			this.appendChild(new Label(Labels.getLabel("error.nocompetition")));
		}
	}

	private void createGroups(final List<GroupData> groupDatas)
	{
		for (final GroupData groupData : groupDatas)
		{
			final Div groupDiv = new Div();
			groupDiv.setSclass("groupDiv");
			final Label label = new Label(groupData.getName());
			label.setSclass("groupLabel");
			groupDiv.appendChild(label);
			this.appendChild(groupDiv);

			final List<MatchData> matches = getMatchFacade().getMatches(groupData);
			for (final MatchData matchData : matches)
			{
				final HtmlBasedComponent matchRowContainer = new Div();
				matchRowContainer.appendChild(createMatchInfoComponent(matchData));
				componentsMap.put(String.valueOf(matchData.getId()), matchRowContainer);
				groupDiv.appendChild(matchRowContainer);
			}
		}
	}

	public static HtmlBasedComponent renderMatchRow(final Component parent, final MatchData matchData)
	{
		final Hbox hbox = new Hbox();
		hbox.setSclass("matchRow");
		hbox.setWidth("100%");
		hbox.setWidths("80px,20px,40px,20px,80px");
		hbox.setAlign("center");
		hbox.appendChild(new Label(matchData.getHomeTeam()));
		Img img = new Img();
		img.setSclass("teamIcon");
		img.setDynamicProperty("src", matchData.getHomeFlagUrl());
		hbox.appendChild(img);
		final Div betLabelDiv = new Div();
		hbox.appendChild(betLabelDiv);

		final Label label = new Label((matchData.getHomeBet() == null ? "-" : matchData.getHomeBet()) + " : "
				+ (matchData.getGuestBet() == null ? "-" : matchData.getGuestBet()));
		label.setSclass("betDisplay");
		betLabelDiv.appendChild(label);

		img = new Img();
		img.setSclass("teamIcon");
		img.setDynamicProperty("src", matchData.getGuestFlagUrl());
		hbox.appendChild(img);
		hbox.appendChild(new Label(matchData.getGuestTeam()));

		parent.appendChild(hbox);

		return betLabelDiv;
	}

	private HtmlBasedComponent createMatchInfoComponent(final MatchData matchData)
	{
		final Div row = new Div();
		final HtmlBasedComponent matchRow = renderMatchRow(row, matchData);

		final Div infoDiv = new Div();
		infoDiv.setSclass("matchInfo");
		infoDiv.appendChild(new Label(matchData.getLocation() == null ? "" : matchData.getLocation() + ", "));
		infoDiv.appendChild(new Label(getFormatFactory().createDateTimeFormat(DateFormat.MEDIUM, DateFormat.SHORT).format(
				matchData.getDate())));
		row.appendChild(infoDiv);

		final BetPopup betPopup = new BetPopup(this, matchData);
		row.appendChild(betPopup);

		row.setStyle("cursor: pointer;");

		if (matchData.isMatchBetable())
		{
			row.addEventListener(Events.ON_CLICK, new EventListener()
			{
				@Override
				public void onEvent(final Event event) throws Exception //NOPMD
				{
					betPopup.open(matchRow, "after_start");
				}
			});
		}
		else
		{
			if (matchData.isMatchFinished())
			{
				UITools.modifySClass(row, "matchFinished", true);
				final Div resultDiv = new Div();
				resultDiv.setSclass("resultDiv");
				createResultContent(resultDiv, matchData);
				row.appendChild(resultDiv);
			}
			else
			{
				UITools.modifySClass(row, "matchRunning", true);
			}

			row.addEventListener(Events.ON_CLICK, new EventListener()
			{
				@Override
				public void onEvent(final Event event) throws Exception //NOPMD
				{
					final List<BetData> closedBets = getMatchFacade().getClosedBets(matchData.getId());

					if (CollectionUtils.isEmpty(closedBets))
					{
						try
						{
							Messagebox.show(Labels.getLabel("bets.error.no_other"));
						}
						catch (final InterruptedException e)
						{
							LOG.error("Could not show message box: ", e);
						}
					}
					else
					{
						final OtherTipsWindow window = new OtherTipsWindow(closedBets);
						window.setTitle(matchData.getHomeTeam() + " - " + matchData.getGuestTeam() + " :  "
								+ Labels.getLabel("bets.match.windowtitle"));
						row.appendChild(window);
					}
				}
			});
		}

		return row;
	}

	public static void createResultContent(final HtmlBasedComponent parent, final MatchData matchData)
	{
		parent.appendChild(new Label(Labels.getLabel("bets.matchFinished") + " - " + Labels.getLabel("bets.goals") + ": "
				+ matchData.getHomeGoals() + " : " + matchData.getGuestGoals() + " (" + Labels.getLabel("bets.score") + ": "
				+ matchData.getScore() + ")"));
	}

	@Override
	public void onUpdateBet(final int matchID)
	{
		final HtmlBasedComponent component = componentsMap.get(String.valueOf(matchID));
		if (component != null)
		{
			component.getChildren().clear();
			component.appendChild(createMatchInfoComponent(getMatchFacade().getMatch(matchID)));
		}
	}

	private MatchFacade getMatchFacade()
	{
		return (MatchFacade) SpringUtil.getBean("matchFacade");
	}

	private FormatFactory getFormatFactory()
	{
		return (FormatFactory) SpringUtil.getBean("formatFactory");
	}
}
