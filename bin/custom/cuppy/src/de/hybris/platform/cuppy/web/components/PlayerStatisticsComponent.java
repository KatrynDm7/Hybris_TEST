/**
 * 
 */
package de.hybris.platform.cuppy.web.components;

import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cuppy.services.impl.NoCompetitionAvailableException;
import de.hybris.platform.cuppy.web.data.PlayerStatisticData;
import de.hybris.platform.cuppy.web.facades.StatisticsFacade;
import de.hybris.platform.servicelayer.i18n.FormatFactory;

import java.text.NumberFormat;
import java.util.Locale;

import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Img;
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
public class PlayerStatisticsComponent extends Div
{
	public PlayerStatisticsComponent()
	{
		super();

		final Div detailsDiv = new Div();
		this.appendChild(detailsDiv);
		detailsDiv.setSclass("rankingUserDetails");

		updateLabel(detailsDiv);

		final Timer timer = new Timer();
		timer.setRepeats(true);
		timer.setRunning(true);
		timer.setDelay(6000);
		timer.addEventListener(Events.ON_TIMER, new EventListener()
		{

			@Override
			public void onEvent(final Event event)
			{
				updateLabel(detailsDiv);
			}
		});
		this.appendChild(timer);
	}

	private void updateLabel(final Div div)
	{
		try
		{
			final PlayerStatisticData data = getStatisticsFacade().getRandomPlayerStatistic();
			div.getChildren().clear();

			final Div imgDiv = new Div();
			final Img img = new Img();
			imgDiv.setSclass("rankingUserDetailsImg");
			img.setDynamicProperty("src", data.getPlayer().getPictureUrl());
			imgDiv.appendChild(img);
			div.appendChild(imgDiv);

			final Label rankLabel = new Label(String.valueOf(data.getPlayer().getRank()));
			rankLabel.setSclass("rankingUserDetailsRank");
			div.appendChild(rankLabel);
			final Label nameLabel = new Label(data.getPlayer().getPlayerName() + "-" + data.getPlayer().getPlayerEMail());
			nameLabel.setSclass("rankingUserDetailsName");
			div.appendChild(nameLabel);

			final Label scoreLabel = new Label(String.valueOf(data.getPlayer().getScore()));
			scoreLabel.setSclass("rankingUserDetailsScore");
			div.appendChild(scoreLabel);

			final Div countryDiv = new Div();
			final Img flagImg = new Img();
			flagImg.setDynamicProperty("src", data.getPlayer().getFlagUrl());
			final Locale locale = UISessionUtils.getCurrentSession().getLocale();
			final Label countryLabel = new Label(data.getPlayer().getLocale().getDisplayCountry(locale));
			countryDiv.setSclass("rankingUserDetailsCountry");
			div.appendChild(countryDiv);
			countryDiv.appendChild(flagImg);
			countryDiv.appendChild(countryLabel);

			div.setTooltiptext(data.getPlayer().getPlayerEMail());

			final NumberFormat nbf = getFormatFactory().createNumberFormat();
			final Label label = new Label("Avg. Score: " + nbf.format(data.getAverageScore()));
			div.appendChild(label);
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
