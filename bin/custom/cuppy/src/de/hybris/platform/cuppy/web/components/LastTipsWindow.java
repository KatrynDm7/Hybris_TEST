/**
 * 
 */
package de.hybris.platform.cuppy.web.components;

import de.hybris.platform.cuppy.web.data.MatchData;

import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;


/**
 * @author andreas.thaler
 * 
 */
public class LastTipsWindow extends Window
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(LastTipsWindow.class);

	public LastTipsWindow(final List<MatchData> matches)
	{
		super();

		this.setWidth("600px");
		this.setHeight("500px");
		this.setSclass("betsWindow");
		this.setClosable(true);

		for (final MatchData matchData : matches)
		{
			final Div matchRow = new Div();
			matchRow.setSclass("matchRowCmp");
			BetComponent.renderMatchRow(matchRow, matchData);

			if (matchData.isMatchFinished())
			{
				BetComponent.createResultContent(matchRow, matchData);
			}
			this.appendChild(matchRow);
		}

		this.doHighlighted();
		this.addEventListener(Events.ON_CLOSE, new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception //NOPMD
			{
				LastTipsWindow.this.detach();
			}
		});
	}
}
