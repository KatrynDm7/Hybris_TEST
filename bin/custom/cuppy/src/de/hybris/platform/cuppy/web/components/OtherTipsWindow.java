/**
 * 
 */
package de.hybris.platform.cuppy.web.components;

import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cuppy.web.data.BetData;

import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Img;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Window;


/**
 * @author andreas.thaler
 * 
 */
public class OtherTipsWindow extends Window
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(OtherTipsWindow.class);

	public OtherTipsWindow(final List<BetData> closedBets)
	{
		super();

		this.setWidth("600px");
		this.setHeight("500px");
		this.setSclass("otherbetsWindow");
		this.setClosable(true);

		final Grid grid = new Grid();
		grid.setSclass("rankingGrid");
		grid.setHeight("440px");

		final Rows gridRows = new Rows();
		grid.appendChild(gridRows);

		final Columns gridColumns = new Columns();
		gridColumns.setHeight("30px");
		grid.appendChild(gridColumns);
		renderGridHeader(gridColumns);
		this.appendChild(grid);

		for (final BetData betData : closedBets)
		{
			if (betData.getPlayerId().equals(UISessionUtils.getCurrentSession().getUser().getUid()))
			{
				continue;
			}

			final Row row = createGridRow(betData);
			gridRows.appendChild(row);
		}

		this.doHighlighted();
		this.addEventListener(Events.ON_CLOSE, new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception //NOPMD
			{
				OtherTipsWindow.this.detach();
			}
		});
	}

	private void renderGridHeader(final Columns cols)
	{
		Column col = new Column("");
		col.setSortAscending(new ComponentComparator<String>(true, "flagUrl"));
		col.setSortDescending(new ComponentComparator<String>(false, "flagUrl"));
		cols.appendChild(col);

		col = new Column(Labels.getLabel("bets.name"));
		col.setSortAscending(new ComponentComparator<String>(true, "name"));
		col.setSortDescending(new ComponentComparator<String>(false, "name"));
		cols.appendChild(col);

		col = new Column(Labels.getLabel("bets.bet"));
		cols.appendChild(col);

		col = new Column(Labels.getLabel("bets.score"));
		col.setSortAscending(new ComponentComparator<Integer>(true, "result"));
		col.setSortDescending(new ComponentComparator<Integer>(false, "result"));
		cols.appendChild(col);
	}

	private Row createGridRow(final BetData data)
	{
		final Row row = new Row();
		row.setSclass("rankingRow");

		final Img flagImg = new Img();
		flagImg.setDynamicProperty("src", data.getPlayerFlagUrl());
		row.appendChild(flagImg);
		row.setAttribute("flagUrl", data.getPlayerFlagUrl());

		Label label = new Label(data.getPlayerName());
		label.setWidth("240px");
		row.appendChild(label);
		row.setAttribute("name", data.getPlayerName());

		label = new Label(data.getHomeBet() + " : " + data.getGuestBet());
		label.setWidth("70px");
		label.setSclass("rankingScore");
		row.appendChild(label);

		label = new Label(String.valueOf(data.getScore()));
		label.setWidth("100px");
		label.setSclass("rankingScore");
		row.appendChild(label);
		row.setAttribute("result", Integer.valueOf(data.getScore()));

		return row;
	}
}
