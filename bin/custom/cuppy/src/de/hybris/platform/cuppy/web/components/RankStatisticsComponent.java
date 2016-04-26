package de.hybris.platform.cuppy.web.components;

import de.hybris.platform.cuppy.services.impl.NoCompetitionAvailableException;
import de.hybris.platform.cuppy.web.data.PlayerRankingData;
import de.hybris.platform.cuppy.web.facades.PlayerFacade;

import java.util.List;

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
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Timer;


public class RankStatisticsComponent extends Div
{
	private int index = 0;

	public RankStatisticsComponent()
	{
		super();

		final Grid rankingGrid = new Grid();
		rankingGrid.setSclass("rankingGrid");


		final Rows gridRows = new Rows();
		rankingGrid.appendChild(gridRows);

		final Columns gridColumns = new Columns();
		gridColumns.setHeight("30px");
		rankingGrid.appendChild(gridColumns);
		renderGridHeader(gridColumns);
		this.appendChild(rankingGrid);

		updateRankingData(gridRows);

		final Timer timer = new Timer();
		timer.setRepeats(true);
		timer.setRunning(true);
		timer.setDelay(6000);
		timer.addEventListener(Events.ON_TIMER, new EventListener()
		{

			@Override
			public void onEvent(final Event event)
			{
				updateRankingData(gridRows);
			}
		});
		this.appendChild(timer);
	}

	private void updateRankingData(final Rows rows)
	{
		rows.getChildren().clear();
		try
		{
			final List<PlayerRankingData> rankings = getPlayerFacade().getRankings();
			if (index >= rankings.size())
			{
				index = 0;
			}
			for (int i = 0; index < rankings.size() && i < 10; index++, i++)
			{
				final PlayerRankingData ranking = rankings.get(index);
				final HtmlBasedComponent rankingRow = createRankingRow(ranking);
				rows.appendChild(rankingRow);
			}
		}
		catch (final NoCompetitionAvailableException e)
		{
			this.appendChild(new Label(Labels.getLabel("error.nocompetition")));
		}

	}

	private void renderGridHeader(final Columns cols)
	{
		final Column rankCol = new Column(Labels.getLabel("rankings.rank"));
		cols.appendChild(rankCol);
		final Column flagCol = new Column("");
		cols.appendChild(flagCol);
		final Column nameCol = new Column(Labels.getLabel("rankings.name"));
		nameCol.setSort("auto");
		cols.appendChild(nameCol);
		final Column scoreCol = new Column(Labels.getLabel("rankings.score"));
		cols.appendChild(scoreCol);
		final Column onlineCol = new Column("");
		cols.appendChild(onlineCol);
	}

	private HtmlBasedComponent createRankingRow(final PlayerRankingData ranking)
	{
		final Row row = new Row();
		row.setSclass("rankingRow");

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
		row.appendChild(scoreLabel);

		final Image onlineImg = new Image("/cuppy/images/online.gif");
		onlineImg.setSclass("onlineImg");
		onlineImg.setTooltiptext(Labels.getLabel("rankings.playeronline.tooltip"));
		row.appendChild(onlineImg);
		row.setAttribute("playerOnline", Boolean.valueOf(ranking.isPlayerOnline()));

		return row;
	}

	private PlayerFacade getPlayerFacade()
	{
		return (PlayerFacade) SpringUtil.getBean("playerFacade");
	}
}
