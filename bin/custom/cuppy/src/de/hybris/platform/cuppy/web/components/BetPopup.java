/**
 * 
 */
package de.hybris.platform.cuppy.web.components;

import de.hybris.platform.cuppy.web.data.MatchData;
import de.hybris.platform.cuppy.web.facades.MatchFacade;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.validation.exceptions.ValidationViolationException;

import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Toolbarbutton;


/**
 * @author andreas.thaler
 * 
 */
public class BetPopup extends Popup
{
	private final static Logger LOG = Logger.getLogger(BetPopup.class);

	private final MatchData matchData;
	private final BetPopupCallback callback;



	public BetPopup(final BetPopupCallback callback, final MatchData matchData)
	{
		super();
		this.callback = callback;
		this.matchData = matchData;

		final Div selectCnt = new Div();
		selectCnt.setSclass("betSelectCnt");
		this.appendChild(selectCnt);

		final Hbox betBox = new Hbox();
		this.appendChild(betBox);
		final Intbox homeBetInput = new Intbox();
		homeBetInput.setValue(matchData.getHomeBet());
		final Intbox guestBetInput = new Intbox();
		guestBetInput.setValue(matchData.getGuestBet());

		betBox.appendChild(homeBetInput);
		betBox.appendChild(new Label(" : "));
		betBox.appendChild(guestBetInput);


		final Toolbarbutton okBtn = new Toolbarbutton(Labels.getLabel("general.ok"));
		this.appendChild(okBtn);

		final EventListener okListener = new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception // NOPMD
			{
				firePlaceBet(homeBetInput.getValue(), guestBetInput.getValue());
			}
		};

		okBtn.addEventListener(Events.ON_CLICK, okListener);

		this.addEventListener(Events.ON_OPEN, new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception //NOPMD
			{
				homeBetInput.setFocus(true);
			}
		});

		homeBetInput.addEventListener(Events.ON_OK, new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception //NOPMD
			{
				guestBetInput.setFocus(true);
			}
		});

		guestBetInput.addEventListener(Events.ON_OK, okListener);


		final Label quickSelectBtn = new Label(Labels.getLabel("bets.quickselect"));
		selectCnt.appendChild(quickSelectBtn);
		final Image img = new Image();
		img.setSclass("quickSelectImg");
		selectCnt.appendChild(img);

		final Menupopup selectPopup = createQuickSelectPopup();
		selectCnt.appendChild(selectPopup);

		selectCnt.setPopup(selectPopup);
	}

	private MatchFacade getMatchFacade()
	{
		return (MatchFacade) SpringUtil.getBean("matchFacade");
	}


	private Menupopup createQuickSelectPopup()
	{
		final Menupopup ret = new Menupopup();

		for (int i = 0; i < 6; i++)
		{
			final Menu homeMenu = new Menu(String.valueOf(i));
			ret.appendChild(homeMenu);

			final Menupopup popup = new Menupopup();
			homeMenu.appendChild(popup);

			final Menuitem header = new Menuitem(i + " :");
			header.setSclass("betSelectMenuheader");
			popup.appendChild(header);
			final String headerUuid = header.getUuid();

			for (int j = 0; j < 6; j++)
			{
				final Integer home = Integer.valueOf(i);
				final Integer guest = Integer.valueOf(j);
				final Menuitem menuitem = new Menuitem(String.valueOf(j));
				popup.appendChild(menuitem);
				menuitem.addEventListener(Events.ON_CLICK, new EventListener()
				{
					@Override
					public void onEvent(final Event event) throws InterruptedException
					{
						firePlaceBet(home, guest);
					}
				});

				menuitem.setAction("onMouseOver: updateGuestBet(document.getElementById('" + headerUuid + "'),'" + i + " : " + j
						+ "');");
			}
		}

		return ret;
	}


	private void firePlaceBet(final Integer home, final Integer guest) throws InterruptedException
	{
		matchData.setHomeBet(home);
		matchData.setGuestBet(guest);
		try
		{
			getMatchFacade().placeBet(matchData);
		}
		catch (final ModelSavingException e)
		{
			if (e.getCause() instanceof ValidationViolationException)
			{
				Messagebox.show(e.getMessage());
			}
			else
			{
				LOG.warn("Could not place bet, reason: ", e);
			}
		}
		catch (final Exception e)
		{
			LOG.warn("Could not place bet, reason: ", e);
		}
		callback.onUpdateBet(matchData.getId());
		BetPopup.this.close();
	}
}

interface BetPopupCallback
{
	void onUpdateBet(final int matchID);
}
