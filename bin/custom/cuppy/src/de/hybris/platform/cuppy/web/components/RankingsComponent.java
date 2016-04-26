package de.hybris.platform.cuppy.web.components;

import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cuppy.services.impl.NoCompetitionAvailableException;
import de.hybris.platform.cuppy.web.data.MatchData;
import de.hybris.platform.cuppy.web.data.PlayerProfileData;
import de.hybris.platform.cuppy.web.data.PlayerRankingData;
import de.hybris.platform.cuppy.web.facades.MatchFacade;
import de.hybris.platform.cuppy.web.facades.PlayerFacade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Img;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Html;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Toolbarbutton;


public class RankingsComponent extends Div
{
	private static final String RANKING_DATA = "rankingData";
	private static final Logger LOG = Logger.getLogger(RankingsComponent.class);

	public RankingsComponent()
	{
		super();
		try
		{
			final List<PlayerRankingData> rankings = getPlayerFacade().getRankings();

			final Grid rankingGrid = new Grid();
			rankingGrid.setSclass("rankingGrid");
			rankingGrid.setHeight("440px");

			final Rows gridRows = new Rows();
			rankingGrid.appendChild(gridRows);

			final Columns gridColumns = new Columns();
			gridColumns.setHeight("30px");
			rankingGrid.appendChild(gridColumns);
			renderGridHeader(gridColumns);

			final Div detailsDiv = new Div();
			detailsDiv.setSclass("rankingUserDetails");

			for (final PlayerRankingData ranking : rankings)
			{
				final HtmlBasedComponent rankingRow = createRankingRow(ranking);
				if (isCurrentUser(ranking.getPlayerId()))
				{
					UITools.modifySClass(rankingRow, "currentUser", true);
					updateDetails(detailsDiv, ranking);
				}
				if (ranking.isPlayerOnline())
				{
					UITools.modifySClass(rankingRow, "playerOnline", true);
				}
				rankingRow.addEventListener(Events.ON_CLICK, new EventListener()
				{
					@Override
					public void onEvent(final Event event) throws Exception // NOPMD
					{
						updateDetails(detailsDiv, ranking);
					}
				});
				gridRows.appendChild(rankingRow);
			}

			this.appendChild(rankingGrid);
			this.appendChild(detailsDiv);
		}
		catch (final NoCompetitionAvailableException e)
		{
			this.appendChild(new Label(Labels.getLabel("error.nocompetition")));
		}
	}

	private void updateDetails(final HtmlBasedComponent detailsComponent, final PlayerRankingData ranking)
	{
		detailsComponent.getChildren().clear();
		detailsComponent.setAttribute(RANKING_DATA, ranking);

		final Div imgDiv = new Div();
		final Img img = new Img();
		imgDiv.setSclass("rankingUserDetailsImg");
		img.setDynamicProperty("src", ranking.getPictureUrl());
		imgDiv.appendChild(img);
		detailsComponent.appendChild(imgDiv);

		final Label rankLabel = new Label(String.valueOf(ranking.getRank()));
		rankLabel.setSclass("rankingUserDetailsRank");
		detailsComponent.appendChild(rankLabel);
		final Html nameLabel = new Html("<a target=\"_blank\" href=\"http://dev.hybris.de/display/hybrislife/"
				+ ranking.getPlayerEMail().replace('@', '*') + "\">" + ranking.getPlayerName() + "</a>");
		nameLabel.setSclass("rankingUserDetailsName");
		detailsComponent.appendChild(nameLabel);

		final Label scoreLabel = new Label(String.valueOf(ranking.getScore()));
		scoreLabel.setSclass("rankingUserDetailsScore");
		detailsComponent.appendChild(scoreLabel);

		final Div countryDiv = new Div();
		final Img flagImg = new Img();
		flagImg.setDynamicProperty("src", ranking.getFlagUrl());
		final Locale locale = UISessionUtils.getCurrentSession().getLocale();
		final Label countryLabel = new Label(ranking.getLocale().getDisplayCountry(locale));
		countryDiv.setSclass("rankingUserDetailsCountry");
		detailsComponent.appendChild(countryDiv);
		countryDiv.appendChild(flagImg);
		countryDiv.appendChild(countryLabel);

		final Toolbarbutton showTipsBtn = new Toolbarbutton(Labels.getLabel("rankings.showbets"));
		showTipsBtn.addEventListener(Events.ON_CLICK, new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception //NOPMD
			{
				final List<MatchData> matches = getMatchFacade().getClosedMatches(ranking.getPlayerId());
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
				else
				{
					final ArrayList<MatchData> revMatchData = new ArrayList<MatchData>(matches);
					Collections.reverse(revMatchData);
					final LastTipsWindow window = new LastTipsWindow(revMatchData);
					window.setTitle(Labels.getLabel("bets.player.windowtitle") + " " + ranking.getPlayerName());
					imgDiv.appendChild(window);
				}
			}
		});

		detailsComponent.appendChild(showTipsBtn);
		detailsComponent.setTooltiptext(ranking.getPlayerEMail());
	}

	private void renderGridHeader(final Columns cols)
	{
		final Column trendCol = new Column("");
		trendCol.setSortAscending(new ComponentComparator<Integer>(true, "trend"));
		trendCol.setSortDescending(new ComponentComparator<Integer>(false, "trend"));
		cols.appendChild(trendCol);
		final Column rankCol = new Column(Labels.getLabel("rankings.rank"));
		rankCol.setSortAscending(new ComponentComparator<Integer>(true, "rank"));
		rankCol.setSortDescending(new ComponentComparator<Integer>(false, "rank"));
		cols.appendChild(rankCol);
		final Column flagCol = new Column("");
		flagCol.setSortAscending(new ComponentComparator<String>(true, "flagUrl"));
		flagCol.setSortDescending(new ComponentComparator<String>(false, "flagUrl"));
		cols.appendChild(flagCol);
		final Column nameCol = new Column(Labels.getLabel("rankings.name"));
		nameCol.setSort("auto");
		nameCol.setSortAscending(new ComponentComparator<String>(true, "name"));
		nameCol.setSortDescending(new ComponentComparator<String>(false, "name"));
		cols.appendChild(nameCol);
		final Column scoreCol = new Column(Labels.getLabel("rankings.score"));
		scoreCol.setSortAscending(new ComponentComparator<Integer>(true, "score"));
		scoreCol.setSortDescending(new ComponentComparator<Integer>(false, "score"));
		cols.appendChild(scoreCol);
		final Column onlineCol = new Column("");
		onlineCol.setSortAscending(new ComponentComparator<Boolean>(true, "playerOnline"));
		onlineCol.setSortDescending(new ComponentComparator<Boolean>(false, "playerOnline"));
		cols.appendChild(onlineCol);
	}

	private HtmlBasedComponent createRankingRow(final PlayerRankingData ranking)
	{
		final Row row = new Row();
		row.setSclass("rankingRow");

		final int rankDifference = ranking.getLastRank() - ranking.getRank();
		row.setAttribute("trend", Integer.valueOf(rankDifference));
		final Div trendDiv = new Div();
		if (rankDifference > 0)
		{
			if (rankDifference > 1)
			{
				trendDiv.setSclass("rankingTrendUpUp");
			}
			else
			{
				trendDiv.setSclass("rankingTrendUp");
			}
		}
		else if (rankDifference < 0)
		{
			if (rankDifference < -1)
			{
				trendDiv.setSclass("rankingTrendDownDown");
			}
			else
			{
				trendDiv.setSclass("rankingTrendDown");
			}
		}
		else
		{
			trendDiv.setSclass("rankingTrendNoTrend");
		}
		trendDiv.setTooltiptext(Labels.getLabel("bets.toolbar.matchday") + ": " + ranking.getLastMatchday() + "->"
				+ ranking.getMatchday() + ", " + Labels.getLabel("rankings.rank") + ": " + ranking.getLastRank() + " -> "
				+ ranking.getRank() + ", " + Labels.getLabel("rankings.score") + ": " + ranking.getLastScore() + "->"
				+ ranking.getScore());
		row.appendChild(trendDiv);

		final Label rankLabel = new Label(String.valueOf(ranking.getRank()));
		rankLabel.setSclass("rankingRank");
		row.setAttribute("rank", Integer.valueOf(ranking.getRank()));
		row.appendChild(rankLabel);

		final Img flagImage = new Img();
		flagImage.setDynamicProperty("src", ranking.getFlagUrl());
		flagImage.setSclass("rankingFlag");
		row.appendChild(flagImage);
		row.setAttribute("flagUrl", ranking.getFlagUrl());

		final Label nameLabel = new Label(ranking.getPlayerName());
		nameLabel.setSclass("rankingName");
		row.setAttribute("name", ranking.getPlayerName());
		row.appendChild(nameLabel);

		final Label scoreLabel = new Label(String.valueOf(ranking.getScore()));
		scoreLabel.setSclass("rankingScore");
		row.setAttribute("score", Integer.valueOf(ranking.getScore()));
		row.appendChild(scoreLabel);

		final Image onlineImg = new Image("/cuppy/images/online.gif");
		onlineImg.setSclass("onlineImg");
		onlineImg.setTooltiptext(Labels.getLabel("rankings.playeronline.tooltip"));
		row.appendChild(onlineImg);
		row.setAttribute("playerOnline", Boolean.valueOf(ranking.isPlayerOnline()));

		return row;
	}

	private boolean isCurrentUser(final String playerID)
	{
		final PlayerProfileData profile = getPlayerFacade().getCurrentPlayer();
		return profile != null && profile.getId().equals(playerID);

	}

	private PlayerFacade getPlayerFacade()
	{
		return (PlayerFacade) SpringUtil.getBean("playerFacade");
	}

	private MatchFacade getMatchFacade()
	{
		return (MatchFacade) SpringUtil.getBean("matchFacade");
	}
}
