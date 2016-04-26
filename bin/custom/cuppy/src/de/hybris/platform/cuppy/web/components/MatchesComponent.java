package de.hybris.platform.cuppy.web.components;

import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cuppy.services.impl.NoCompetitionAvailableException;
import de.hybris.platform.cuppy.web.data.BetData;
import de.hybris.platform.cuppy.web.data.MatchData;
import de.hybris.platform.cuppy.web.facades.MatchFacade;
import de.hybris.platform.servicelayer.i18n.FormatFactory;

import java.text.DateFormat;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Img;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Toolbarbutton;


public class MatchesComponent extends Div implements BetPopupCallback
{
	private static final Logger LOG = Logger.getLogger(MatchesComponent.class);

	private String lastDay = "";

	private final Div matchesDiv;
	private Component toolbarComponent;

	private int selectedMatchdayIndex = -1;
	private final List<Integer> matchdays;

	public MatchesComponent()
	{
		super();
		this.getChildren().clear();

		matchesDiv = new Div();
		matchesDiv.setSclass("betsWindow matchesWindow");
		this.appendChild(matchesDiv);

		List<Integer> matchdaysTemp;
		try
		{
			matchdaysTemp = getMatchFacade().getMatchdays();
			final Integer currentMatchday = getMatchFacade().getCurrentMatchday();
			for (int i = 0; i < matchdaysTemp.size(); i++)
			{
				if (matchdaysTemp.get(i).equals(currentMatchday))
				{
					selectedMatchdayIndex = i;
				}
			}
		}
		catch (final NoCompetitionAvailableException e)
		{
			matchdaysTemp = Collections.EMPTY_LIST;
		}
		matchdays = matchdaysTemp;
	}

	public void init()
	{
		init(null);
	}

	public void init(final Component toolbarComponent)
	{
		lastDay = "";
		this.toolbarComponent = toolbarComponent;
		final int selectedMatchday = renderToolbarComponent();
		if (selectedMatchday > -1 && selectedMatchdayIndex > -1)
		{
			renderMatches(selectedMatchday);
		}
		else
		{
			renderAllMatches();
		}
	}

	protected int renderToolbarComponent()
	{
		int ret = -1;
		if (toolbarComponent != null)
		{
			toolbarComponent.getChildren().clear();

			if (!matchdays.isEmpty())
			{
				final Div toolbarCnt = new Div();
				toolbarCnt.appendChild(new Label(Labels.getLabel("bets.toolbar.matchday")));

				final Combobox matchdayBox = new Combobox();
				matchdayBox.setSclass("matchdaySelector");

				final Comboitem allitem = new Comboitem(Labels.getLabel("bets.toolbar.matchday.all"));
				allitem.setValue(Integer.valueOf(-1));
				matchdayBox.appendChild(allitem);
				matchdayBox.setSelectedItem(allitem);

				for (int i = 0; i < matchdays.size(); i++)
				{
					final Comboitem citem = new Comboitem(String.valueOf(matchdays.get(i)));
					citem.setValue(Integer.valueOf(i));
					matchdayBox.appendChild(citem);
					if (selectedMatchdayIndex == i)
					{
						matchdayBox.setSelectedItem(citem);
						ret = matchdays.get(i).intValue();
					}
				}

				toolbarCnt.appendChild(matchdayBox);
				matchdayBox.addEventListener(Events.ON_CHANGE, new EventListener()
				{
					@Override
					public void onEvent(final Event event) throws Exception // NOPMD
					{
						selectedMatchdayIndex = matchdayBox.getSelectedIndex() - 1;
						init(toolbarComponent);
					}
				});

				final Toolbarbutton leftBtn = new Toolbarbutton("", "/cuppy/images/arrow_left.gif");
				leftBtn.setSclass("matchdayArrow");
				leftBtn.setDisabled(selectedMatchdayIndex < 0);
				leftBtn.addEventListener(Events.ON_CLICK, new EventListener()
				{
					@Override
					public void onEvent(final Event event)
					{
						if (selectedMatchdayIndex > -1)
						{
							selectedMatchdayIndex--;
							init(toolbarComponent);
						}
					}
				});
				toolbarCnt.appendChild(leftBtn);

				final Toolbarbutton rightBtn = new Toolbarbutton("", "/cuppy/images/arrow_right.gif");
				rightBtn.setSclass("matchdayArrow");
				rightBtn.setDisabled(selectedMatchdayIndex >= matchdays.size() - 1);
				rightBtn.addEventListener(Events.ON_CLICK, new EventListener()
				{
					@Override
					public void onEvent(final Event event)
					{
						if (selectedMatchdayIndex < matchdays.size())
						{
							selectedMatchdayIndex++;
							init(toolbarComponent);
						}
					}
				});
				toolbarCnt.appendChild(rightBtn);

				toolbarComponent.appendChild(toolbarCnt);
			}
		}
		return ret;
	}

	protected void renderMatches(final int matchday)
	{
		renderMatches(getMatchFacade().getMatches(matchday));
	}

	protected void renderAllMatches()
	{
		try
		{
			renderMatches(getMatchFacade().getMatches());
		}
		catch (final Exception e)
		{
			matchesDiv.appendChild(new Label(Labels.getLabel("error.nocompetition")));
		}
	}

	protected void renderMatches(final List<MatchData> matches)
	{

		matchesDiv.getChildren().clear();


		if (CollectionUtils.isEmpty(matches))
		{
			try
			{
				Messagebox.show(Labels.getLabel("ranking.error.noclosedmatches"));
			}
			catch (final InterruptedException e)
			{
				LOG.error("Could not show message box: ", e);
			}
			return;
		}

		for (final MatchData matchData : matches)
		{
			final Div row = new Div();
			row.setSclass("matchRowCmp");
			renderMatchesRow(row, matchData);

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
						betPopup.open(row, "after_start");
					}
				});
			}
			else
			{
				if (matchData.isMatchFinished())
				{
					UITools.modifySClass(row, "matchFinished", true);
					BetComponent.createResultContent(row, matchData);
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

			matchesDiv.appendChild(row);
		}
	}

	protected void renderMatchesRow(final Component parent, final MatchData matchData)
	{
		final Div overallDiv = new Div();

		// show date if needed
		final String day = getFormatFactory().createDateTimeFormat(DateFormat.FULL, -1).format(matchData.getDate());

		if (!day.equalsIgnoreCase(lastDay))
		{
			final Div dateDiv = new Div();
			dateDiv.setSclass("matchesRowDate");
			dateDiv.appendChild(new Label(day));
			overallDiv.appendChild(dateDiv);
			final Separator sepp = new Separator("horizontal");
			sepp.setBar(true);
			overallDiv.appendChild(sepp);
			lastDay = day;
		}



		final Hbox hbox = new Hbox();
		hbox.setSclass("matchRow");
		hbox.setWidth("100%");
		hbox.setWidths("60px,110px,20px,40px,20px,110px");
		hbox.setAlign("center");


		final Div timeLabelDiv = new Div();
		timeLabelDiv.setSclass("timeLabelDiv");
		timeLabelDiv.setAlign("right");
		hbox.appendChild(timeLabelDiv);
		final DateFormat format = getFormatFactory().createDateTimeFormat(-1, DateFormat.SHORT);
		timeLabelDiv.appendChild(new Label(format.format(matchData.getDate())));

		final Label homeLabel = new Label(matchData.getHomeTeam());
		homeLabel.setSclass("teamLabel");
		hbox.appendChild(homeLabel);
		Img img = new Img();
		img.setDynamicProperty("src", matchData.getHomeFlagUrl());
		img.setSclass("teamIconImg");
		hbox.appendChild(img);
		final Div betLabelDiv = new Div();
		betLabelDiv.setSclass("betLOabelDiv");
		hbox.appendChild(betLabelDiv);

		final Label label = new Label((matchData.getHomeBet() == null ? "-" : matchData.getHomeBet()) + " : "
				+ (matchData.getGuestBet() == null ? "-" : matchData.getGuestBet()));
		label.setSclass("betDisplay");
		betLabelDiv.appendChild(label);

		img = new Img();
		img.setSclass("teamIconImg");
		img.setDynamicProperty("src", matchData.getGuestFlagUrl());
		hbox.appendChild(img);

		final Label guestLabel = new Label(matchData.getGuestTeam());
		guestLabel.setSclass("teamLabel");
		hbox.appendChild(guestLabel);

		overallDiv.appendChild(hbox);

		parent.appendChild(overallDiv);
	}

	@Override
	public void onUpdateBet(final int matchID)
	{
		init(toolbarComponent);
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
